package com.lowyer.model;

import javax.servlet.http.HttpSession;

public class ServiceSession {
	private Long userId;
	private String userName;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//通过HttpSession生成自己的ServiceSession
	public static ServiceSession getServiceSession(HttpSession session) {
		if(session != null) {
			if(session.getAttribute("userId") == null && session.getAttribute("userName") == null) {
				return new ServiceSession();
			}else {
				Long userid = (Long) session.getAttribute("userId");
				String username = (String) session.getAttribute("userName");
				return new ServiceSession(userid, username);
			}
		}
		return new ServiceSession();
	}
	
	public ServiceSession(Long userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
	}
	public ServiceSession() {
		super();
	}

}
