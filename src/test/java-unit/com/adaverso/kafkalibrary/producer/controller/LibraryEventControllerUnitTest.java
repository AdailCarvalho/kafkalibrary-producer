package com.adaverso.kafkalibrary.producer.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		String requestBody = this.objectMapper.writeValueAsString(libraryEvent);
		doNothing().when(this.libraryEventProducer).sendLibraryEventAsync2(isA(LibraryEvent.class));
		
		this.mockMvc.perform(post("/v1/libraryevent")
					.content(requestBody)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
	}
}
