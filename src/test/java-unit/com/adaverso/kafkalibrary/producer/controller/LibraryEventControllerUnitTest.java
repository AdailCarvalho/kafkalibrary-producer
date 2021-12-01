package com.adaverso.kafkalibrary.producer.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.adaverso.kafkalibrary.producer.domain.Book;
import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.adaverso.kafkalibrary.producer.enums.LibraryEventType;
import com.adaverso.kafkalibrary.producer.event.LibraryEventProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * WebMvcTest annotation gives support for testing rest controllers.
 *
 */
@WebMvcTest(LibraryEventController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {
	
	//Will have access to all endpoints in the configured Controller
	@Autowired
	MockMvc mockMvc;

	//Inject the LibraryEventProducer to the test context, once it's present
	//in the tested Controller.
	@MockBean
	LibraryEventProducer libraryEventProducer;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void postLibraryEvent() throws Exception {
		LibraryEvent libraryEvent = this.generateLibraryEvent(null, LibraryEventType.NEW, 1, "Guimaraes Rosa", "Grande Sertao: Veredas", 1956);
		
		String requestBody = this.objectMapper.writeValueAsString(libraryEvent);
		when((this.libraryEventProducer).sendLibraryEventAsync2(isA(LibraryEvent.class))).thenReturn(null);
		
		this.mockMvc.perform(post("/v1/libraryevent")
					.content(requestBody)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
	}
	
	@Test
	public void postLibraryEvent_4xx() throws Exception {
		LibraryEvent libraryEvent = this.generateLibraryEvent(null, LibraryEventType.NEW, null, null, "Grande Sertao: Veredas", 1956);

		String requestBody = this.objectMapper.writeValueAsString(libraryEvent);
		when((this.libraryEventProducer).sendLibraryEventAsync2(isA(LibraryEvent.class))).thenReturn(null);
		
		String expected = "book.bookAuthor - must not be blank, book.bookId - must not be null";
		this.mockMvc.perform(post("/v1/libraryevent")
					.content(requestBody)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(expected));
	}

	@Test
	public void putLibraryEvent() throws Exception {
		LibraryEvent libraryEvent = this.generateLibraryEvent(555, LibraryEventType.UPDATE, 1, "Guimaraes Rosa", "Grande Sertao: Veredas", 1956);
		
		String requestBody = this.objectMapper.writeValueAsString(libraryEvent);
		when((this.libraryEventProducer).sendLibraryEventAsync2(isA(LibraryEvent.class))).thenReturn(null);
		
		this.mockMvc.perform(put("/v1/libraryevent")
					.content(requestBody)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
	}
	
	@Test
	public void putLibraryEvent_4xx() throws Exception {
		//null LibraryEventID 
		LibraryEvent libraryEvent = this.generateLibraryEvent(null, LibraryEventType.UPDATE, 1, "Guimaraes Rosa", "Grande Sertao: Veredas", 1956);
		
		String requestBody = this.objectMapper.writeValueAsString(libraryEvent);
		when((this.libraryEventProducer).sendLibraryEventAsync2(isA(LibraryEvent.class))).thenReturn(null);
		
		this.mockMvc.perform(put("/v1/libraryevent")
					.content(requestBody)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
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
