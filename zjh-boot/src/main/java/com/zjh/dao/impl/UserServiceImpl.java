package com.zjh.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;

import com.zjh.model.User;
import com.zjh.utils.SpringContext;
import com.zjh.utils.UniqueID;

public class UserServiceImpl {
	
	SqlSessionTemplate sessionTemplate = SpringContext.getBean("SqlSessionTemplate", SqlSessionTemplate.class);
	
	public int add(User user) {
		user.setId(UniqueID.getUniqueID());
		return sessionTemplate.insert("test.insertUser", user);
	}

}
