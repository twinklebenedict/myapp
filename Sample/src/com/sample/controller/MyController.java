package com.sample.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sample.service.TaskService;

@Controller
public class MyController {
	
	List<String> logs =  new ArrayList<>();
	
	@Autowired
	TaskService taskService;

	@RequestMapping(value = "/showlogs")
	@ResponseBody
	public String helloWorld() {
		JsonObject jsonObj = new JsonObject();
		for (String log: logs) {
			jsonObj.addProperty(log, log);
		}
		
		return jsonObj.toString();
	}
	
	@RequestMapping("/pushlogs")
	public ModelAndView test(@RequestBody String body, HttpServletRequest request) {
		String message = body;
		JsonObject jsonObj =  new JsonParser().parse(body.toString()).getAsJsonObject();
		String log = jsonObj.get("message").getAsString();
		if(log != null && !log.isEmpty()){
			logs.add(log);
		}
		
		return new ModelAndView("hello", "message", message);
	}
	
	/*@RequestMapping("/oauth2callback")
	public ModelAndView helloWorld(HttpServletRequest request) {
		
		
		String message = "Hello World";
		
		return new ModelAndView("hello", "message", message);
	}*/
	
	/*@RequestMapping("/login")
	public ModelAndView helloWorld(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
		
		String message = "Hello World";
		
		return new ModelAndView("hello", "message", message);
	}*/
}
