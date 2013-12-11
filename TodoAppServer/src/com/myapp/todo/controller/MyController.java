package com.myapp.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {

	@RequestMapping("/hello")
	public ModelAndView helloWorld() {

		String message = "Hello World";
		return new ModelAndView("hello", "message", message);
	}

}
