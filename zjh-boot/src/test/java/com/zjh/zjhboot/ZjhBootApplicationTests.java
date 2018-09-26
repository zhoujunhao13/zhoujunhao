package com.zjh.zjhboot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.zjh.controller.TestController;
import com.zjh.dao.server.GoodsDao;
import com.zjh.model.GoodsModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZjhBootApplicationTests {
	
	private MockMvc mvc;
	
	@Autowired
	GoodsDao goodsDao;
	
	@Before
	public void setup() throws Exception{
		mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
	}

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/hello").accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
	}
	
	@Test
	public void regTest() {
		// 要验证的字符串
	    String str = "建行0.1费率";
	    // 邮箱验证规则
	    String regEx = "^(.*[0].[0-9]+(费率){1})$";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(str);
	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    System.out.println(rs);
	}
	
	@Test
	public void getGoods() {
		GoodsModel goodsModel = this.goodsDao.get("003003");
		System.out.println(goodsModel);
	}

}
