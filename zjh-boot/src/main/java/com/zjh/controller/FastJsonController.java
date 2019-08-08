package com.zjh.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zjh.dao.impl.UserServiceImpl;
import com.zjh.model.User;

@Controller
@RequestMapping("fastjson")
public class FastJsonController {
	
	@RequestMapping("user")
	//@CrossOrigin("http://127.0.0.1:8080")
	@ResponseBody
	public User getUser() {
		User user = new User();
		user.setAge(24);
		user.setName("zjh");
		user.setSex("男");
		user.setCreateDate(new Date());
		return user;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Integer addUser(@RequestBody String param) {
		JSONObject json = JSONObject.parseObject(param);
		User user = new User();
		user.setName(json.getString("name"));
		user.setAge(json.getInteger("age"));
		user.setSex(json.getString("sex"));
		UserServiceImpl serviceImpl = new UserServiceImpl();
		return serviceImpl.add(user);
	}

}
