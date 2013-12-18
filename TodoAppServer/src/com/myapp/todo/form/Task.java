package com.myapp.todo.form;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASK")
public class Task {
	
	@Id
	@Column (name = "ID")
	private Integer id;
	
	@Column (name = "TITLE")
	private String title;
	
	@Column (name = "DESCRIPTION")
	private String description;
	
	@Column (name = "EMAIL")
	private String email;
	
	@Column (name = "TIMESTAMP")
	private long timestamp;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
