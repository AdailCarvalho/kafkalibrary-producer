package com.adaverso.kafkalibrary.producer.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class LibraryEventsProducer {
	

	private static final Logger log = LoggerFactory.getLogger(LibraryEventsProducer.class);
	
	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	/**
	 * Sends data to a Kafka Topic. To set a default topic, use
	 * the kafka.template.default-topic property into the .yml
	 * file.
	 * 
	 * @param libraryEvent
	 * @throws JsonProcessingException
	 */
	public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent.getBook());
		
		ListenableFuture<SendResult<Integer, String>> listenableFuture=
				kafkaTemplate.sendDefault(key, value); 
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key, value, result);
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}
			
		});
	}

	private void handleFailure(Integer key, String value, Throwable ex) {
		log.error("Error sending the message. Exception: {}", ex.getMessage());
		try {
			throw ex;
		} catch (Throwable throwable) {
			log.error("Error in onFailure: {}", throwable.getMessage());
		}
		
	}

	private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		log.info("Message sent successfully for the key {} and value {}", key, value);
		log.info("Sent to partition: {}", result.getRecordMetadata().partition());
	}
}