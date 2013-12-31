package com.sample.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sample.form.Task;
import com.sample.service.TaskService;

@Controller
public class MyController {
	
	@Autowired
	TaskService taskService;

	@RequestMapping("/login")
	public ModelAndView helloWorld(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
		
		String message = "Hello World";
		
		return new ModelAndView("hello", "message", message);
	}
	
	@RequestMapping("/oauth2callback")
	public ModelAndView helloWorld(HttpServletRequest request) {
		
		String message = "Hello World";
		
		return new ModelAndView("hello", "message", message);
	}
	
	/*@RequestMapping("/getTasks")
	@ResponseBody
	public String getTasks(@RequestParam("email") String email ,HttpServletRequest request) {
		//Return tasks only for this email
		List<Task> tasks = taskService.getTasks(email);
		JsonArray taskArrray = new JsonArray();
		for (Task task : tasks) {
			if(task.getEmail().equals(email)){
				JsonObject taskObj = new JsonObject();
				taskObj.addProperty("id", task.getId());
				taskObj.addProperty("title", task.getTitle());
				taskObj.addProperty("description", task.getDescription());
				taskObj.addProperty("email", task.getEmail());
				taskObj.addProperty("timestamp", task.getTimestamp());
				taskArrray.add(taskObj);
			}
			
		}
		JsonObject finalObj = new JsonObject();
		finalObj.add("tasks", taskArrray);
		return finalObj.toString();
//		return new ModelAndView("hello", "message", jsonObject);
	}*/

}
