package com.adaverso.kafkalibrary.producer.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.adaverso.kafkalibrary.producer.domain.Book;
import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.adaverso.kafkalibrary.producer.enums.LibraryEventType;
import com.adaverso.kafkalibrary.producer.enums.Topic;
import com.adaverso.kafkalibrary.producer.event.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {
	
	@Mock
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Spy
	ObjectMapper objectMapper = new ObjectMapper();
	
	@InjectMocks
	LibraryEventProducer eventProducer;

	@Test
	void sendLibraryEventAsync2OnFailure() throws JsonProcessingException {
		
		Book book = Book.builder()
				.id(1)
				.author("Guimaraes Rosa")
				.name("Grande Sertao: Veredas")
				.year(1956)
				.build();

		LibraryEvent libraryEvent = LibraryEvent.builder()
										.libraryEventId(null)
										.libraryEventsType(LibraryEventType.NEW)
										.book(book)
										.build();
		
		SettableListenableFuture<?> future = new SettableListenableFuture<>();
		future.setException(new RuntimeException("Error calling Kafka"));
		
		when(this.kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		
		assertThrows(Exception.class, () -> this.eventProducer.sendLibraryEventAsync2(libraryEvent).get());
	}
	

	@Test
	void sendLibraryEventAsync2OnSuccess() throws JsonProcessingException, InterruptedException, ExecutionException {
		//given
		Book book = Book.builder()
				.id(1)
				.author("Guimaraes Rosa")
				.name("Grande Sertao: Veredas")
				.year(1956)
				.build();

		LibraryEvent libraryEvent = LibraryEvent.builder()
										.libraryEventId(null)
										.libraryEventsType(LibraryEventType.NEW)
										.book(book)
										.build();
		
		SettableListenableFuture future = new SettableListenableFuture<>();
		String record = objectMapper.writeValueAsString(libraryEvent);
		ProducerRecord<Integer, String> producerRecord = 
				new ProducerRecord<Integer, String>
				(Topic.LIBRARY.getName(), libraryEvent.getLibraryEventId(), record);
		RecordMetadata metadata = new RecordMetadata(
				new TopicPartition(Topic.LIBRARY.getName(), 1), 1, 1, 324, System.currentTimeMillis(), 1, 2);
		SendResult<Integer, String> sendResult = new SendResult<>(producerRecord, metadata);
		future.set(sendResult);
		
		when(this.kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		
		//when
		ListenableFuture<SendResult<Integer, String>> listenableFuture = this.eventProducer.sendLibraryEventAsync2(libraryEvent);
		
		//then
		SendResult<Integer, String> sendResult1 = listenableFuture.get();
		assertThat(sendResult.getRecordMetadata().partition()).isEqualTo(1);
		
	}
}
