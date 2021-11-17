package com.adaverso.kafkalibrary.producer.enums;

public enum Topic {
	LIBRARY("library-events");
	
	private String name;
	
	private Topic(String name) {
		this.name= name;
	}
	
	public String getName() {
		return this.name;
	}
}
