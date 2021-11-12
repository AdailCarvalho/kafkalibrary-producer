package com.adaverso.kafkalibrary.producer.domain;

import java.util.Objects;

public class Book {

	private Integer bookId;
	private String bookName;
	private String bookAuthor;
	
	public Book() {}
	
	public Book(Integer bookId, String bookName, String bookAuthor) {
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookAuthor = bookAuthor;
	}

	public Integer getBookId() {
		return bookId;
	}
	
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	
	public String getBookName() {
		return bookName;
	}
	
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(bookId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(bookId, other.bookId);
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", bookName=" + bookName + ", bookAuthor=" + bookAuthor + "]";
	}
}
