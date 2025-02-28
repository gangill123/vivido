package com.vivido.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;


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
	
	@GetMapping("/update")
	public String updatePage() {
	    System.out.println("update page method is called");
	    return "product/update";
	}
	
	
	

	
	

}
