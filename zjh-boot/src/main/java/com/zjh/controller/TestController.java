package com.zjh.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	/*@Autowired
	JmsSender sender;*/
	
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
	
	@RequestMapping("/a/{id}/{name}")
	public void a(@PathVariable Integer id, @PathVariable String name,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.setAttribute("id", id);
		session.setAttribute("name", name);
	}
	
	@RequestMapping("/b")
	public void b(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Integer id = (Integer) session.getAttribute("id");
		String name = (String) session.getAttribute("name");
		System.out.println(id + name);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			Map<String, String> map = new HashMap<>();
			map.put("id", id.toString());
			map.put("name", name);
			writer.write(map.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
