package com.adaverso.kafkalibrary.producer.domain;

import java.util.Objects;

import org.springframework.beans.BeanUtils;

import com.adaverso.kafkalibrary.producer.enums.LibraryEventsType;

public class LibraryEvent {
	
	private Integer libraryEventId;
	private LibraryEventsType libraryEventsType;
	private Book book;
	
	public LibraryEvent() {}
	
	public LibraryEvent(Integer libraryEventId, Book book) {
		this.libraryEventId = libraryEventId;
		this.book = book;
	}
	
	public Integer getLibraryEventId() {
		return libraryEventId;
	}
	public void setLibraryEventId(Integer libraryEventId) {
		this.libraryEventId = libraryEventId;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	public LibraryEventsType getLibraryEventsType() {
		return libraryEventsType;
	}

	public void setLibraryEventsType(LibraryEventsType libraryEventsType) {
		this.libraryEventsType = libraryEventsType;
	}
	
	public static LibraryEventBuilder builder() {
		return new LibraryEventBuilder();
	}

	@Override
	public int hashCode() {
		return Objects.hash(libraryEventId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LibraryEvent other = (LibraryEvent) obj;
		return Objects.equals(libraryEventId, other.libraryEventId);
	}

	@Override
	public String toString() {
		return "LibraryEvent [libraryEventId=" + libraryEventId + ", libraryEventsType=" + libraryEventsType + ", book="
				+ book + "]";
	}
	
	public static class LibraryEventBuilder {
		
		private LibraryEvent libraryEvent;
		
		public LibraryEventBuilder() {
			this.libraryEvent = new LibraryEvent();
		}
		
		public LibraryEventBuilder libraryEventId(Integer libraryEventId) {
			this.libraryEvent.libraryEventId = libraryEventId;
			return this;
		}
		
		public LibraryEventBuilder libraryEventsType(LibraryEventsType libraryEventsType) {
			this.libraryEvent.libraryEventsType = libraryEventsType;
			return this;
		}
		
		public LibraryEventBuilder book(Book book) {
			this.libraryEvent.book = book;
			return this;
		}
		
		public LibraryEvent build() {
			LibraryEvent l = new LibraryEvent();
			BeanUtils.copyProperties(this.libraryEvent, l);
			return l;
		}
	}
}
