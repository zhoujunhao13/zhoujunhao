package com.lowyer.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lowyer.model.ServiceSession;
import com.lowyer.utils.Utils;

@RestController
public class DataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);
	
	@Autowired
	Reflect rcm;
	
	@RequestMapping("/")
	public String test() {
		return "success";
	}
	
	@RequestMapping("/rest")
	//@ResponseBody
	public String rest(@RequestParam(value="method",required=false) String method, 
					   @RequestParam(value="session",required=false) String session,
					   @RequestBody String param,HttpServletRequest request) {
		ServiceSession serviceSession = ServiceSession.getServiceSession(request.getSession());
		/*if(serviceSession == null) {
			return "未登录";
		}*/
		return this.onRestService(method, JSON.toJSONString(serviceSession), null, null, param);
	}

	public String onRestService(String method,String session,String user_id,String user_name,String param) {
		LOGGER.info(String.format("---->method=%1$s session=%2$s param=%3$s user_id=%4$s user_name=%5$s",method+"",session+"",param+"",user_id+"",user_name+""));
	    try {
	        if (StringUtils.isEmpty(session)) {
	            ServiceSession ss = new ServiceSession();
	            session = JSON.toJSONString(ss);
	        }
	        
	        //System.out.println(String.format("当前会话:%1$s\r\n请求方法:%2$s\r\n请求参数:%3$s", session,method,param));
	        Object retdata = rcm.executeClassMethod(method, session, param);
	        
	        if (retdata == null) {
	        	return "";
	        } else if (retdata instanceof String) {
	        	return (String) retdata;
	        } else {
	        	return JSON.toJSONString(Utils.toNormalJSONObject(retdata));
	        	//return retdata;
	        	//return JSONObject.toJSONStringWithDateFormat(retdata, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);  
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    	JSONObject json = new JSONObject();
	    	json.put("msg", ex.getMessage());
	        return json.toJSONString();
	    }
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(@RequestBody(required=true) String param,HttpServletRequest request) {
		JSONObject json = JSONObject.parseObject(param);
		request.getSession().setAttribute("userId", json.getLong("userId"));
		request.getSession().setAttribute("userName", json.getString("userName"));
		return "登陆成功";
	}
	
}
