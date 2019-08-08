package com.zjh.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;

import com.zjh.model.OperLog;
import com.zjh.utils.SpringContext;
import com.zjh.utils.UniqueID;

public class OperLogServiceImpl {
	
	SqlSessionTemplate sessionTemplate = SpringContext.getBean("SqlSessionTemplate", SqlSessionTemplate.class);
	
	public Integer add(OperLog log) {
		log.setId(UniqueID.getUniqueID());
		return sessionTemplate.insert("test.insertLog", log);
	}

}
