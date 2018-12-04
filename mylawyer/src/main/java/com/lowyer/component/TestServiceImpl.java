package com.lowyer.component;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.Service;
import com.lowyer.model.ServiceResponse;
import com.lowyer.model.ServiceSession;
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
	
	public JSONObject upload(ServiceSession session,JSONObject json) {
		MultipartFile file = json.getObject("file", MultipartFile.class);
		file.getOriginalFilename();
		return null;
	}

}
