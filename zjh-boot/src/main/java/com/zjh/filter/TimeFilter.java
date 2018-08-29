package com.zjh.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TimeFilter implements Filter {

	@Override
	public void destroy() {
		System.out.println("================过滤器销毁===============");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		filterChain.doFilter(request, response);
		System.out.println("filter耗时："+(System.currentTimeMillis()-start));
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("================过滤器开启===============");
	}

}
