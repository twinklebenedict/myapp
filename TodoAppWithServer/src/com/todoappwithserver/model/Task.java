package com.todoappwithserver.model;

public class Task {
	
	int id;
	String title;
	String description;
	long timeStamp;
	
	public Task() {	}
	
	public Task(String title, String description, long timeStamp) {
		this.title = title;
		this.description = description;
		this.timeStamp = timeStamp;
	}
	
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
