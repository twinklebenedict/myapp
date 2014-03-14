package com.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionController {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException() {
		return new ModelAndView("error", "errormsg", "Some exception is thrown");
	}
	
}
