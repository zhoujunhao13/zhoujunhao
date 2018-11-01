package com.lowyer.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lowyer.component.TestServiceImpl;
import com.lowyer.controller.Reflect;

@SuppressWarnings("deprecation")
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverts() {
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		// 中文乱码解决方案
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);//设定json格式且编码为UTF-8
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        return new HttpMessageConverters(converter);
	}
	
	/*@Bean
	public Reflect reflect() {
		return new Reflect();
	}*/
	
	@Bean(name="test")
	public TestServiceImpl testServiceImpl() {
		return new TestServiceImpl();
	}
	
}
