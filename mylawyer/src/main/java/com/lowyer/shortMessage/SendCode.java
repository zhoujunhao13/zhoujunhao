package com.lowyer.shortMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

/**
 * 基于网易云信的发送短信验证功能
 * @author zjh
 *
 */
public class SendCode {
	//发送验证码的请求路径
	private static final String SERVER_URL="https://api.netease.im/sms/sendcode.action";
	//应用申请的appkey
	private static final String APP_KEY = "6a8de2eea0f2efbdb3d8e56a22cfa1e2";
	//应用申请appsecret
	private static final String APP_SECRET = "e9f41d87d06f";
	//随机数
	private static final String NONCE = "123456";
	//短信模板id
	private static final String TEMPLATEID = "9464676";
	//验证码长度
	private static final String CODELEN = "6";
	
	public static boolean sendMsg(String telphone) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(SERVER_URL);
		String curTime = String.valueOf((new Date()).getTime()/1000L);
		String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
		
		// 设置请求的header
		httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", telphone));
        nvps.add(new BasicNameValuePair("codeLen", CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String responseEntity = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        String code = JSON.parseObject(responseEntity).getString("code");
        if(code.equals("200")) {
        	return true;
        }
		return false;
	}

}
