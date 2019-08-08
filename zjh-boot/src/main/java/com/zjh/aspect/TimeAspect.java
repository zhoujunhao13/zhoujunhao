package com.zjh.aspect;

import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.zjh.dao.impl.OperLogServiceImpl;
import com.zjh.model.OperLog;

@Aspect
@Component
public class TimeAspect {
	
	//@Pointcut("execution(* com.zjh.dao.impl.UserServiceImpl..*(..))")
	@Pointcut("execution(* com.zjh.controller.FastJsonController..*(..))")
	public void pointCut(){}
	
	@Before("pointCut()")
	public void doBefore(JoinPoint joinPoint) {
		System.out.println("AOP before Advice...");
	}
	
	@After("pointCut()")
	public void doAfter() {
		System.out.println("AOP after Advice...");
	}
	
	@AfterReturning(pointcut="pointCut()", returning="returnVal")
	public void afterReturn(JoinPoint joinpoint, Object returnVal) {
		String method = joinpoint.getSignature().getName();
		System.out.println("AOP afterReturn Advice..." +method + ":::" + returnVal.toString());
	}
	
	@AfterThrowing(pointcut="pointCut()",throwing="error")
	public void afterThrowing(JoinPoint joinpoint, Throwable error) {
		System.out.println("AOP afterThrowing Advice..." + error);
	}
	
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("=============aspect处理=============");
		OperLog log = new OperLog();
		try {
			String method = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			/*for(Object arg : args) {
				System.out.println("参数为："+arg);
			}*/
			Object object = joinPoint.proceed();
			
			log.setOperator("admin");
			log.setOperName(method);
			log.setOperParams(Arrays.toString(args));
			if(object != null) {
				log.setResultMsg(object.toString());
			}else{
				log.setResultMsg(null);
			}
			log.setOperResult("success");
			log.setOperTime(new Date());
			return object;
		} catch (Exception e) {
			log.setOperResult("failure");
			log.setResultMsg(e.toString());
		}finally {
			OperLogServiceImpl impl = new OperLogServiceImpl();
			impl.add(log);
		}
		return null;
	}
	
}
