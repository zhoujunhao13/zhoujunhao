package com.zjh.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zjh.Servlet.ServletTest;
import com.zjh.controller.Reflect;
import com.zjh.dao.impl.GoodsDaoImpl;
import com.zjh.dao.server.GoodsDao;
import com.zjh.filter.TimeFilter;
import com.zjh.interceptor.TimeInterceptor;
import com.zjh.listener.ListenerTest;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	TimeInterceptor timeInterceptor;
	
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
	
	@Bean
	public ServletRegistrationBean<ServletTest> servletRegistrationBean(){
		return new ServletRegistrationBean<ServletTest>(new ServletTest(), "/servletTest");
	}
	
	@Bean
	public FilterRegistrationBean<TimeFilter> filterRegistrationBean(){
		FilterRegistrationBean<TimeFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		TimeFilter filter = new TimeFilter();
		filterRegistrationBean.setFilter(filter);
		
		List<String> urls = new ArrayList<>();
		urls.add("/*");
		filterRegistrationBean.setUrlPatterns(urls);
		return filterRegistrationBean;
	}
	
	@Bean
	public ServletListenerRegistrationBean<ListenerTest> listenerRegistrationBean(){
		return new ServletListenerRegistrationBean<ListenerTest>(new ListenerTest());
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(timeInterceptor);
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/fastjson/**").allowedOrigins("http://127.0.0.1:8080");
	}
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
	
	@Bean
	public Reflect reflect() {
		return new Reflect();
	}
	
	@Bean(name="goods")
	public GoodsDao goodsDao() {
		return new GoodsDaoImpl();
	}

}
