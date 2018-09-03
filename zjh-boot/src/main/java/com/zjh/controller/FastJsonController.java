package com.zjh.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zjh.model.User;

@Controller
@RequestMapping("fastjson")
public class FastJsonController {
	
	@RequestMapping("user")
	@CrossOrigin("http://127.0.0.1:8080")
	@ResponseBody
	public User getUser() {
		User user = new User();
		user.setAge(24);
		user.setName("zjh");
		user.setSex("男");
		user.setCreateDate(new Date());
		return user;
	}

}
