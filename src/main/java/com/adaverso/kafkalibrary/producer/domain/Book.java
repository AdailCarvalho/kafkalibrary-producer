package com.adaverso.kafkalibrary.producer.domain;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

public class Book {

	@NotNull
	private Integer bookId;
	
	@NotBlank
	private String bookName;
	
	@NotBlank
	private String bookAuthor;
	
	private Integer bookYear;
	
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
	
	public Integer getBookYear() {
		return bookYear;
	}

	public void setBookYear(Integer bookYear) {
		this.bookYear = bookYear;
	}

	public static BookBuilder builder() {
		return new BookBuilder();
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
		return "Book [bookId=" + bookId + ", bookName=" + bookName + ", bookAuthor=" + bookAuthor + ", bookYear="
				+ bookYear + "]";
	}

	public static class BookBuilder {
		
		private Book book;
		
		public BookBuilder() {
			this.book= new Book();
		}
		
		public BookBuilder id(Integer id) {
			this.book.bookId = id;
			return this;
		}
		
		public BookBuilder name(String name) {
			this.book.bookName = name;
			return this;
		}
		
		public BookBuilder author(String author) {
			this.book.bookAuthor = author;
			return this;
		}
		
		public BookBuilder year(Integer year) {
			this.book.bookYear = year;
			return this;
		}
		
		public Book build() {
			Book b = new Book();
			BeanUtils.copyProperties(this.book, b);
			return b;
		}
	}
}
