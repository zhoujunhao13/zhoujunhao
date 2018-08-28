package com.zjh.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zjh.model.User;

@Controller
@RequestMapping("fastjson")
public class FastJsonController {
	
	@RequestMapping("user")
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
