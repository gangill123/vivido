package com.vivido.controller;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ProductController {
	// http://localhost:8080
	@RequestMapping("/")
	public String main() {
	    System.out.println("Main method is called");
	    return "index";
	}

	//http://localhost:8080/management
	@GetMapping("/management")
	public String managementPage() {
	    System.out.println("Management page method is called");
	    return "product/management";
	}
	
	@GetMapping("/registration")
	public String registrationPage() {
	    System.out.println("registration page method is called");
	    return "product/registration";
	}

}
