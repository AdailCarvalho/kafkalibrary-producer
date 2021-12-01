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
import com.adaverso.kafkalibrary.producer.enums.LibraryEventType;

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
public class LibraryEventControllerIntgTest {
	
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
	@Timeout(value= 5)
	public void postLibraryEvent() {
		LibraryEvent libraryEvent = this.generateLibraryEvent(null, LibraryEventType.NEW, 1, "Guimaraes Rosa", "Grande Sertao: Veredas", 1956);
		
		ResponseEntity<LibraryEvent> response = this.restTemplate.exchange("/v1/libraryevent", HttpMethod.POST, 
				this.getRequestBody(libraryEvent), LibraryEvent.class);
		
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(this.consumer, "library-events");
		String expectedRecord = "{\"libraryEventId\":null,\"libraryEventsType\":\"NEW\",\"book\":{\"bookId\":1,\"bookName\":\"Grande Sertao: Veredas\",\"bookAuthor\":\"Guimaraes Rosa\",\"bookYear\":1956}}";
		String valueRecord = consumerRecord.value();
		
		assertEquals(expectedRecord, valueRecord);
	}
	
	@Test
	@Timeout(value= 5)
	public void putLibraryEvent() {
		LibraryEvent libraryEvent = this.generateLibraryEvent(369, LibraryEventType.UPDATE, 5, "Machado de Assis", "Memórias Póstumas de Brás Cubas", 1881);
		
		ResponseEntity<LibraryEvent> response = this.restTemplate.exchange("/v1/libraryevent", HttpMethod.PUT, 
				this.getRequestBody(libraryEvent), LibraryEvent.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(this.consumer, "library-events");
		String expectedRecord = "{\"libraryEventId\":369,\"libraryEventsType\":\"UPDATE\",\"book\":{\"bookId\":5,\"bookName\":\"Memórias Póstumas de Brás Cubas\",\"bookAuthor\":\"Machado de Assis\",\"bookYear\":1881}}";
		String valueRecord = consumerRecord.value();
		
		assertEquals(expectedRecord, valueRecord);
	}
	
	private HttpEntity<LibraryEvent> getRequestBody(LibraryEvent libraryEvent) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		return new HttpEntity<>(libraryEvent, headers);
		
	}
	
	private LibraryEvent generateLibraryEvent(Integer libraryEventId, LibraryEventType libraryEventType, 
			Integer bookId, String bookAuthor, String bookName, Integer bookYear) {
		Book book = Book.builder()
				.id(bookId)
				.author(bookAuthor)
				.name(bookName)
				.year(bookYear)
				.build();

		LibraryEvent libraryEvent = LibraryEvent.builder()
										.libraryEventId(libraryEventId)
										.libraryEventsType(libraryEventType)
										.book(book)
										.build();
		
		return libraryEvent;
		
	}
}
