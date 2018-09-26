package com.zjh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zjh.dao.server.GoodsDao;
import com.zjh.model.GoodsModel;

@Controller
@RequestMapping("goods")
public class GoodsController {
	
	@Autowired
	GoodsDao goodsDao;
	
	@GetMapping("/get/{itemCode}")
	@ResponseBody
	public GoodsModel getOne(@PathVariable("itemCode") String itemCode) {
		return goodsDao.get(itemCode);
	}
}
