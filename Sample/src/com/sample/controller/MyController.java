package com.sample.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sample.service.TaskService;
import com.sample.utils.Mail;

@Controller
public class MyController {
	
	List<String> logs =  new ArrayList<>();
	
	@Autowired
	TaskService taskService;

	@RequestMapping(value = "/showlogs")
	@ResponseBody
	public String showlogs() {
		JsonObject jsonObj = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		JsonPrimitive jsonElement;
		Jedis jedis = new Jedis("localhost");
	    jedis.connect();
	    long len = jedis.llen("log");
	    List<String> logs = jedis.lrange("log", 0, len);
	    for (String log: logs) {
	    	jsonElement = new JsonPrimitive(log);
			jsonArray.add(jsonElement);
		}
	    jsonObj.add("logs", jsonArray);
		return jsonObj.toString();
	}
	
	@RequestMapping(value = "/sendmail")
	@ResponseBody
	public String sendMail(@RequestBody String body, HttpServletRequest request) throws Exception{
		JsonObject jsonObject = new JsonParser().parse(body.toString()).getAsJsonObject();
		JsonArray array = (JsonArray) jsonObject.get("logs");
		String contents = "";
		for (int i = 0; i < array.size(); i++) {
			contents += array.get(i).getAsString() + "\n";
		}
		Mail.sendMail(contents);
		return "Mail sent successfully";
	}
	
	@RequestMapping("/pushlogs")
	public ModelAndView pushLogs(@RequestBody String body, HttpServletRequest request) {
		String message = body;
		JsonObject jsonObj =  new JsonParser().parse(body.toString()).getAsJsonObject();
		String log = jsonObj.get("message").getAsString();
		if(log != null && !log.isEmpty()){
			Jedis jedis = new Jedis("localhost");
		    jedis.connect();
		    jedis.lpush("log", log);
//			logs.add(log);
		}
		return new ModelAndView("hello", "message", message);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "exception")
	public ModelAndView handleException(HttpServletRequest req, Exception ex, HttpServletResponse response) {
//		HttpStatus.valueOf(response.getStatus());
		return new ModelAndView("error", "errormsg", "Some exception is thrown");
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
