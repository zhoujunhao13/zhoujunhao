package com.lowyer.component;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lowyer.model.ServiceResponse;
import com.lowyer.model.Test;
import com.lowyer.service.TestService;
import com.lowyer.utils.SpringContext;

public class TestServiceImpl implements TestService {
	
	public SqlSessionTemplate getSqlSessionTemplate() {
		return SpringContext.getBean("SqlSessionTemplate", SqlSessionTemplate.class);
	}

	@Override
	public ServiceResponse search() {
		List<Test> list = this.getSqlSessionTemplate().selectList("test.select");
		return ServiceResponse.buildSuccess(list);
	}

}
