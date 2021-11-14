package com.adaverso.kafkalibrary.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.adaverso.kafkalibrary.producer.events.LibraryEventsProducer;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class LibraryEventsController {
	
	@Autowired
	LibraryEventsProducer libraryEventsProducer;

	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) 
			throws JsonProcessingException {
		this.libraryEventsProducer.sendLibraryEvent(libraryEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
}
