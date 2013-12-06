package com.todoappwithserver.model;

public class Task {
	
	int id;
	String title;
	String description;
	
	public Task() {	}
	
	public Task(String title, String description) {
		this.title = title;
		this.description = description;
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

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
