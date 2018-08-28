package com.zjh.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("freemarker")
public class FreemarkerController {
	
	@RequestMapping("hello")
	public String hello(Map<String, Object> map) {
		map.put("msg", "hello Freemarker");
		return "hello";
	}

}
