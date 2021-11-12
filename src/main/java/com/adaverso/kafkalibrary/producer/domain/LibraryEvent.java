package com.adaverso.kafkalibrary.producer.domain;

import java.util.Objects;

public class LibraryEvent {
	
	private Integer libraryEventId;
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
		return "LibraryEvent [libraryEventId=" + libraryEventId + ", book=" + book + "]";
	}
}
