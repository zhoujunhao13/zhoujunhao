package com.zjh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjh.model.User;

@RestController
public class TestController {
	
	@GetMapping("/user")
	public User getUser() {
		User user = new User();
		user.setName("zjh");
		user.setAge(24);
		user.setSex("男");
		return user;
	}
	
	@PostMapping("/hello")
	public String hello() {
		return "hello";
	}

}
