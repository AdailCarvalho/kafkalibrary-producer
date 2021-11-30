package com.adaverso.kafkalibrary.producer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.adaverso.kafkalibrary.producer.domain.LibraryEvent;
import com.adaverso.kafkalibrary.producer.enums.LibraryEventType;
import com.adaverso.kafkalibrary.producer.event.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class LibraryEventController {
	
	@Autowired
	LibraryEventProducer libraryEventsProducer;

	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) 
			throws JsonProcessingException {
		
		libraryEvent.setLibraryEventsType(LibraryEventType.NEW);
		this.libraryEventsProducer.sendLibraryEventAsync2(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	
	@PutMapping("/v1/libraryevent")
	public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) 
			throws JsonProcessingException {
		
		if (libraryEvent.getLibraryEventId() == null) {
			return new ResponseEntity<>("Please, provide the libraryEventId value", HttpStatus.BAD_REQUEST);
		}
		
		libraryEvent.setLibraryEventsType(LibraryEventType.UPDATE);
		this.libraryEventsProducer.sendLibraryEventAsync2(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
	}
}
