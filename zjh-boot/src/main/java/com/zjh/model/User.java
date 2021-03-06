package com.zjh.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class User {

	private Long id;
	
	private String name;
	
	private Integer age;
	
	private String sex;
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + ", createDate=" + createDate
				+ "]";
	}

}
