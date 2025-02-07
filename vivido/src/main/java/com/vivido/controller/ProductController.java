package com.vivido.controller;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


// @RestController와 @Controller를 동시에 사용할 수 없음
// JSON API와 View 렌더링을 모두 하려면 @RequestMapping 사용


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

}
