package com.lowyer.component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.lowyer.model.ServiceResponse;
import com.lowyer.model.ServiceSession;
import com.lowyer.model.Test;
import com.lowyer.service.TestService;
import com.lowyer.shortMessage.MobileMessageCheck;
import com.lowyer.shortMessage.SendCode;
import com.lowyer.utils.SpringContext;
import com.lowyer.websocket.WebSocketServer;

public class TestServiceImpl implements TestService {
	
	@Autowired
	WebSocketServer webSocketServer;
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	public SqlSessionTemplate getSqlSessionTemplate() {
		return SpringContext.getBean("SqlSessionTemplate", SqlSessionTemplate.class);
	}

	@Override
	public ServiceResponse search() {
		List<Test> list = this.getSqlSessionTemplate().selectList("test.select");
		return ServiceResponse.buildSuccess(list);
	}
	
	public void test(ServiceSession session,JSONObject jsonparam) {
		System.out.println(session.getUserName());
		System.out.println(session.getUserId());
	}
	
	public JSONObject upload(ServiceSession session,JSONObject json) {
		MultipartFile file = json.getObject("file", MultipartFile.class);
		file.getOriginalFilename();
		return null;
	}
	
	public void sendMessage(ServiceSession session,JSONObject json) {
		String msg = json.getString("msg");
		String user = json.getString("user");
		webSocketServer.broadToOne(msg, user);
	}
	
	public void testRedis() {
		Test test = new Test();
		test.setAge(24);
		test.setId(1L);
		test.setName("zjh");
		ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
		operations.set("test", test);		//没有时间控制
		operations.set("test.test", test, 10, TimeUnit.SECONDS);		//设置时间为10秒，10后自动删除
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		boolean f = redisTemplate.hasKey("test.test");
		
		if(f) {
			System.out.println("true");
		}else {
			System.out.println("false");
		}
	}
	
	public JSONObject getRedisTest(String key) {
		ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
		Test test = (Test) operations.get(key);
		return JSONObject.parseObject(JSONObject.toJSONString(test));
	}
	
	public String testSendShoreMessage(JSONObject jsonparam) {
		String phone = jsonparam.getString("phone");
		try {
			boolean flag = SendCode.sendMsg(phone);
			if(flag) {
				return "success";
			}else {
				return "faile";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "faile";
		}
	}
	
	public String checkShortMessage(JSONObject jsonparam) {
		String phone = jsonparam.getString("phone");
		String code = jsonparam.getString("code");
		try {
			boolean flag = MobileMessageCheck.checkMsg(phone, code);
			if(flag) {
				return "success";
			}else {
				return "faile";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "faile";
		}
	}

}
