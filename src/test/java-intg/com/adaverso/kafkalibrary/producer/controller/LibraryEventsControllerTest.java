package com.adaverso.kafkalibrary.producer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.adaverso.kafkalibrary.producer.domain.Book;
import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.adaverso.kafkalibrary.producer.enums.LibraryEventsType;

/**
 * Integration test for LibrarysEventController. webEnvironment config
 * to launch in Random Port to avoid conflits with default Tomcat's 
 * port 8080.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
public class LibraryEventsControllerTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;
	
	private Consumer<Integer, String> consumer;
	
	/**
	 * Creates a consumer that will consume the messages the producer generates
	 */
	@BeforeEach
	void setUpConsumer() {
		Map<String, Object> configs = new HashMap<>
			(KafkaTestUtils.consumerProps("group1", "true", this.embeddedKafkaBroker));
		consumer = 
				new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
		this.embeddedKafkaBroker.consumeFromAllEmbeddedTopics(this.consumer);
	}
	
	/**
	 * Assures the consumer will shutdown after a kafka embedded test execution is done.
	 */
	@AfterEach
	void tearDownConsumer() {
		this.consumer.close();
	}

	/**
	 * Tests libraryevent's API request and consumes the produced message to check
	 * if the result is equal to expected. The Timeout annotation here is used 
	 * to await the assynchronous execution of the producer to complete.
	 */
	@Test
	@Timeout(value = 5)
	public void postLibraryEvent() {
		Book book = Book.builder()
						.id(1)
						.author("Guimaraes Rosa")
						.name("Grande Sertao: Veredas")
						.year(1956)
						.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
												.libraryEventId(null)
												.libraryEventsType(LibraryEventsType.NEW)
												.book(book)
												.build();
		
		ResponseEntity<LibraryEvent> response = this.restTemplate.exchange("/v1/libraryevent", HttpMethod.POST, 
				this.getRequestBody(libraryEvent), LibraryEvent.class);
		
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(this.consumer, "library-events");
		String expectedRecord = "{\"bookId\":1,\"bookName\":\"Grande Sertao: Veredas\",\"bookAuthor\":\"Guimaraes Rosa\",\"bookYear\":1956}";
		String valueRecord = consumerRecord.value();
		
		assertEquals(expectedRecord, valueRecord);
	}
	
	private HttpEntity<LibraryEvent> getRequestBody(LibraryEvent libraryEvent) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		return new HttpEntity<>(libraryEvent, headers);
		
	}
}
