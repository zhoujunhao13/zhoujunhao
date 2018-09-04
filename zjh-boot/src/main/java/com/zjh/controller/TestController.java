package com.zjh.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjh.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "FastJson测试", tags = { "测试接口" })
@RestController
@RequestMapping("test")
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
	
	@ApiOperation("获取用户信息")
    @ApiImplicitParam(name = "name", value = "用户名", dataType = "string", paramType = "query")
    @GetMapping("/test/{name}")
    public User test(@PathVariable("name") String name) {
        User user = new User();

        user.setName("zjh");
		user.setAge(24);
		user.setSex("男");
        user.setCreateDate(new Date());

        return user;
    }

}
