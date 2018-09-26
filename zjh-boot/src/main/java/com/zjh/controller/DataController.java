package com.zjh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjh.model.ServiceSession;

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
	public String rest(@RequestParam(value="method",required=false) String method, 
					   @RequestParam(value="session",required=false) String session) {
		return this.onRestService(method, session, null, null, null, null, null);
	}

	public String onRestService(String method,String session,String ent_id,String user_id,String user_name,String locale,String param) {
		LOGGER.info(String.format("---->method=%1$s session=%2$s param=%3$s ent_id=%4$s user_id=%5$s user_name=%6$s locale=%7$s",method+"",session+"",param+"",ent_id+"",user_id+"",user_name+"",locale+""));
	    try {
	        if (StringUtils.isEmpty(session) && !StringUtils.isEmpty(ent_id)) {
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
	        	//return JSON.toJSONString(Utils.toNormalJSONObject(retdata));
	        	return JSONObject.toJSONStringWithDateFormat(retdata, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);  
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    	JSONObject json = new JSONObject();
	    	json.put("msg", ex.getMessage());
	        return json.toJSONString();
	    }
	}
}
