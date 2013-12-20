package com.myapp.todo.service;

import java.util.List;

import com.myapp.todo.form.Task;

public interface TaskService {
	
	public void addTask(Task task);
	public List<Task> listTask();
	public void removeTask(Integer id);
	public List<Task> getTasks(String email);
}
