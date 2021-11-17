package com.adaverso.kafkalibrary.producer.enums;

public enum Headers {
	
	SOURCE("event-source");
	
	private String name;
	
	private Headers(String name) {
		this.name= name;
	}
	
	public String getName() {
		return this.name;
	}
}
