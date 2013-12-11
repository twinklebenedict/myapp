package com.myapp.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.todo.form.Task;
import com.myapp.todo.service.TaskService;

@Controller
public class MyController {
	
	TaskService taskService;

	@RequestMapping("/hello")
	public ModelAndView helloWorld() {

		String message = "Hello World";
		
		Task task = new Task();
		task.setTitle("title");
		task.setDescription("description");
		task.setEmail("mymail@gmail.com");
		taskService.addTask(task);
		
		return new ModelAndView("hello", "message", message);
	}

}
