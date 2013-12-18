package com.myapp.todo.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myapp.todo.form.Task;
import com.myapp.todo.service.TaskService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

@Controller
public class MyController {
	
	@Autowired
	TaskService taskService;

	@RequestMapping("/hello")
	public ModelAndView helloWorld(@RequestBody String body, HttpServletRequest request) {

		String message = "Hello World";
		
		/*StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { report an error }

		  try {
		    JsonObject jsonObject = new JsonParser().parse(jb.toString()).getAsJsonObject();
		    jsonObject.get("title");
		  } catch (Exception e) {
		    e.printStackTrace();
		  }*/
		
		try{
			JsonObject jsonObject = new JsonParser().parse(body.toString()).getAsJsonObject();
			Task task = new Task();
			task.setId(jsonObject.get("id").getAsInt());
			task.setTitle(jsonObject.get("title").getAsString());
			task.setDescription(jsonObject.get("description").getAsString());
			task.setEmail(jsonObject.get("email").getAsString());
			task.setTimestamp(jsonObject.get("timestamp").getAsLong());
			taskService.addTask(task);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		/*Task task = new Task();
		task.setTitle("title");
		task.setDescription("description");
		task.setEmail("mymail@gmail.com");
		taskService.addTask(task);*/
		
		return new ModelAndView("hello", "message", message);
	}

}
