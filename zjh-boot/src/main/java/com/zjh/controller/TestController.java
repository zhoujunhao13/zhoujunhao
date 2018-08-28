package com.zjh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/rest")
	public String helloWorld() {
		return "helloworld";
	}
	
	@PostMapping("/hello")
	public String hello() {
		return "hello";
	}

}
