package com.myapp.todo.controller;

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
import com.myapp.todo.form.Task;
import com.myapp.todo.service.TaskService;

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
			JsonArray taskArray = (JsonArray)jsonObject.get("tasks");
			for(JsonElement taskEle : taskArray){
				JsonObject taskObj = (JsonObject) taskEle;
				Task task = new Task();
				task.setId(taskObj.get("id").getAsInt());
				task.setTitle(taskObj.get("title").getAsString());
				task.setDescription(taskObj.get("description").getAsString());
				task.setEmail(taskObj.get("email").getAsString());
				task.setTimestamp(taskObj.get("timestamp").getAsLong());
				taskService.addTask(task);
			}
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
	
	@RequestMapping("/getTasks")
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
	}

}
