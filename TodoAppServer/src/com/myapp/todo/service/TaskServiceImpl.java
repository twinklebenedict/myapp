package com.myapp.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.todo.dao.TaskDAO;
import com.myapp.todo.form.Task;

@Service
public class TaskServiceImpl implements TaskService{
	
	@Autowired
	TaskDAO taskDAO;

	@Transactional
	public void addTask(Task task) {
		taskDAO.addTask(task);
		
	}

	@Transactional
	public List<Task> listTask() {
		return taskDAO.listTask();
	}

	@Transactional
	public void removeTask(Integer id) {
		taskDAO.removeTask(id);
		
	}

	@Transactional
	public List<Task> getTasks(String email) {
		return taskDAO.getTasks(email);
	}

}
