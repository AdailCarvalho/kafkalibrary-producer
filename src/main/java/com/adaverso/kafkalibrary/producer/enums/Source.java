package com.adaverso.kafkalibrary.producer.enums;

public enum Source {
	
	SCANNER("book scanner");
	
	private String name;
	
	private Source(String name) {
		this.name= name;
	}

	public String getName() {
		return name;
	}
}
