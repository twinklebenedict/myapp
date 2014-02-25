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

	/*@RequestMapping(value = "/showlogs")
	@ResponseBody
	public String helloWorld() {
		JsonObject jsonObj = new JsonObject();
		for (int i = 0; i < 10; i++) {
			jsonObj.addProperty("test"+i, "test"+i);
		}
		
		return jsonObj.toString();
	}
	
	@RequestMapping("/test")
	public ModelAndView test(@RequestBody String body, HttpServletRequest request) {
		String message = body;
		
		return new ModelAndView("hello", "message", message);
	}*/
	
	/*@RequestMapping("/oauth2callback")
	public ModelAndView helloWorld(HttpServletRequest request) {
		
		
		String message = "Hello World";
		
		return new ModelAndView("hello", "message", message);
	}*/
	
	@RequestMapping("/login")
	public ModelAndView helloWorld(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
		
		String message = "Hello World";
		
		return new ModelAndView("hello", "message", message);
	}
}
