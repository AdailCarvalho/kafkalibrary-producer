package com.adaverso.kafkalibrary.producer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.adaverso.kafkalibrary.producer.domain.Book;
import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.adaverso.kafkalibrary.producer.enums.LibraryEventsType;

/**
 * Integration test for LibrarysEventController. webEnvironment config
 * to launch in Random Port to avoid conflits with default Tomcat's 
 * port 8080
 * 
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryEventsControllerTest {
	
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void postLibraryEvent() {
		Book book = Book.builder()
						.id(1)
						.author("Guimarães Rosa")
						.name("Grande Sertão: Veredas")
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
	}
	
	private HttpEntity<LibraryEvent> getRequestBody(LibraryEvent libraryEvent) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		return new HttpEntity<>(libraryEvent, headers);
		
	}
}
