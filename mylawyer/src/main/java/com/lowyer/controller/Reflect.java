package com.lowyer.controller;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lowyer.model.ServiceSession;
import com.lowyer.utils.SpringContext;

@Component
public class Reflect {
	final static String SESSIONJSON_PARAM = "1";
	final static String SESSIONSTRING_PARAM = "2";
	final static String SESSIONLIST_PARAM = "3";
	final static String STRING_PARAM = "4";
	final static String VOID_PARAM = "5";
	final static String SESSIONMULTIPARTFILE_PARAM = "6";
	final static String SESSIONOUTPUTSTREAM_PARAM = "7";
	
	private static final Logger logger = LoggerFactory.getLogger(Reflect.class);
	
	public Reflect()
	{
		
	}

	public Object executeClassMethod(String method,String session,String param) throws Exception
	{
		try
		{
			JSONObject jsonparam = null;

			// 创建服务组件
			StringBuffer component = new StringBuffer();
			StringBuffer componentmethod = new StringBuffer();
			Object obj = createClassObject(method,component,componentmethod);
			if (obj == null) throw new Exception("method instance invalid");
			String methodname = componentmethod.toString();
			
			// 根据参数优先反射匹配的方法
			Object retobj = null; 
			StringBuffer sb = new StringBuffer();
			if (session != null && !session.equals(""))
			{
				//部分URL请求是被编码的
				session=URLDecoder.decode(session,"utf-8");
				ServiceSession sessionobj = JSON.parseObject(session,ServiceSession.class);	
				
				// ServiceSession,String
                retobj = invoke(SESSIONSTRING_PARAM,sb,obj,methodname,new Object[]{ sessionobj,param });
                if (sb.length() > 0) return retobj;
                
                // ServiceSession,JSONObject
                if (jsonparam == null) { try { jsonparam = JSON.parseObject(param); } catch(Exception ex) { } }
                retobj = invoke(SESSIONJSON_PARAM,sb,obj,methodname,new Object[]{ sessionobj,jsonparam });
                if (sb.length() > 0) return retobj;
                
                // String
                retobj = invoke(STRING_PARAM,sb,obj,methodname,new Object[]{ param });
                if (sb.length() > 0) { return retobj; }
				
				// void
				retobj = invoke(VOID_PARAM,sb,obj,methodname,null);
				if (sb.length() > 0) { return retobj; }
			}
			else
			{
				if (param != null && !param.equals(""))
				{
					// String
					retobj = invoke(STRING_PARAM,sb,obj,methodname,new Object[]{ param });
					if (sb.length() > 0) { return retobj; }
					
					// void
					retobj = invoke(VOID_PARAM,sb,obj,methodname,null);
					if (sb.length() > 0) { return retobj; }
				}
				else
				{
					// void
					retobj = invoke(VOID_PARAM,sb,obj,methodname,null);
					if (sb.length() > 0) { return retobj; }
					
					// String
					retobj = invoke(STRING_PARAM,sb,obj,methodname,new Object[]{ param });
					if (sb.length() > 0) { return retobj; }
				}
				
				// ServiceSession,String
				retobj = invoke(SESSIONSTRING_PARAM,sb,obj,methodname,new Object[]{ null,param });
				if (sb.length() > 0) { return retobj; }
				
				// ServiceSession,JSONObject
				if (jsonparam == null) { try { jsonparam = JSON.parseObject(param); } catch(Exception ex) { } }
				retobj = invoke(SESSIONJSON_PARAM,sb,obj,methodname,new Object[]{ null,jsonparam });
				if (sb.length() > 0) { return retobj; }
			}
			
			if (obj != null) {
				Method[] methodList = obj.getClass().getMethods();
				if (methodList != null) {
					StringBuffer buffer = new StringBuffer();

					for (Method rowMethod:methodList) {
						if (rowMethod !=null) {
							if (methodname.equalsIgnoreCase(rowMethod.getName())) {
								buffer.append(String.format("%1$s(", methodname));
								
								Class<?>[] paramsList = rowMethod.getParameterTypes();
								boolean first = true;
								for (Class<?> classzz:paramsList) {
									if (first) {
										first = false;
										buffer.append(classzz.getName());
									} else {
										buffer.append(","+classzz.getName());
									}
								}
								buffer.append(";\r\n");
							}
						}
					}
					logger.info(String.format("%1$s.%2$s method not found! but found:%3$s", component,methodname,buffer.toString()));
				}
			}
			
			throw new Exception(component+"."+methodname+" method not found!");
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
	
    public Object executeClassMethodForListParam(String method, String session, String param,List<?> list) throws Exception
    {
        try
        {
            // 创建服务组件
            StringBuffer component = new StringBuffer();
            StringBuffer componentmethod = new StringBuffer();
            Object obj = createClassObject(method, component, componentmethod);
            if (obj == null) throw new Exception("method instance invalid");
            String methodname = componentmethod.toString();

            // 根据参数优先反射匹配的方法
            Object retobj = null;
            StringBuffer sb = new StringBuffer();
            ServiceSession sessionobj = null;
            if (session != null && !session.equals("")) sessionobj = JSON.parseObject(session, ServiceSession.class);

            // ServiceSession,List
            retobj = invoke(SESSIONLIST_PARAM, sb, obj, methodname, new Object[] { sessionobj, param,list });
            if (sb.length() > 0) return retobj;

            // void
            retobj = invoke(VOID_PARAM, sb, obj, methodname, null);
            if (sb.length() > 0) { return retobj; }

            throw new Exception(component + "." + methodname + " method not found!");
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
    
    public Object executeClassMethodForMultipartFileParam(String method, String session, String param,MultipartFile file) throws Exception
    {
        try
        {
            // 创建服务组件
            StringBuffer component = new StringBuffer();
            StringBuffer componentmethod = new StringBuffer();
            Object obj = createClassObject(method, component, componentmethod);
            if (obj == null) throw new Exception("method instance invalid");
            String methodname = componentmethod.toString();

            // 根据参数优先反射匹配的方法
            Object retobj = null;
            StringBuffer sb = new StringBuffer();
            ServiceSession sessionobj = null;
            if (session != null && !session.equals("")) sessionobj = JSON.parseObject(session, ServiceSession.class);
            
            // ServiceSession,file
            retobj = invoke(SESSIONMULTIPARTFILE_PARAM, sb, obj, methodname, new Object[] { sessionobj, param,file });
            if (sb.length() > 0) return retobj;

            // void
            retobj = invoke(VOID_PARAM, sb, obj, methodname, null);
            if (sb.length() > 0) { return retobj; }

            throw new Exception(component + "." + methodname + " method not found!");
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
    
    public Object executeClassMethodForOutputStreamParam(String method, String session, String param,OutputStream os) throws Exception
    {
        try
        {
            // 创建服务组件
            StringBuffer component = new StringBuffer();
            StringBuffer componentmethod = new StringBuffer();
            Object obj = createClassObject(method, component, componentmethod);
            if (obj == null) throw new Exception("method instance invalid");
            String methodname = componentmethod.toString();

            // 根据参数优先反射匹配的方法
            Object retobj = null;
            StringBuffer sb = new StringBuffer();
            ServiceSession sessionobj = null;
            if (session != null && !session.equals("")) sessionobj = JSON.parseObject(session, ServiceSession.class);

            // ServiceSession,os
            retobj = invoke(SESSIONOUTPUTSTREAM_PARAM, sb, obj, methodname, new Object[] { sessionobj, param,os });
            if (sb.length() > 0) return retobj;

            // void
            retobj = invoke(VOID_PARAM, sb, obj, methodname, null);
            if (sb.length() > 0) { return retobj; }

            throw new Exception(component + "." + methodname + " method not found!");
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
	   
    private Object createClassObject(String servicemethod,StringBuffer component,StringBuffer methodname) throws Exception
    {
        // 分解组件名和方法名
        if (StringUtils.isEmpty(servicemethod)) throw new Exception("method name is empty");
        int lastdot = servicemethod.lastIndexOf(".");
        if (lastdot < 0) throw new Exception("method name invalid");
        component.append(servicemethod.substring(0, lastdot));
        methodname.append(servicemethod.substring(lastdot + 1));

        // 反射类对象
        Object obj;
        Class<?> cl;
        try {
            // 通过Spring工厂创建
            obj = SpringContext.getBean(component.toString());
        } catch (Exception beanEx) {
        		System.out.println(String.format("SpringContext.getBean('%1$s') onFaiure:%2$s", component.toString(),beanEx.getMessage()==null?beanEx.getLocalizedMessage():"NullPointerException"));
	        	// beanEx.printStackTrace();
            // 通过类名创建
	        	try {
	        		cl = Class.forName(component.toString());
	        		obj = cl.newInstance();
	        	} catch (Exception classEx) {
	        		throw new Exception(classEx.getMessage());
	        	}
        }
        
        return obj;
    }

    private Method getMethod(Class<?> classzz,String name, Class<?>... parameterTypes) {
    		Method classmethod = null;
    		try {
    			classmethod = classzz.getMethod(name, parameterTypes);
    		} catch(Exception ex) {
    			// 找不到匹配方法
    			if (classzz.isInterface()) {
    				System.out.println("classzz. interface");
    			}
//    			Class<?> classChild = classzz.getSuperclass();
//    			if (classChild != null) {
//    				return this.getMethod(classChild,name,parameterTypes);
//    			}
    		}
    		return classmethod;
    }
    
	@SuppressWarnings({ "rawtypes" })
	private Object invoke(String mode,StringBuffer find,Object objinst,String methodname,Object[] params) throws Exception
	{
		Method classmethod = null;
		try
		{
	        Class cl = objinst.getClass();
	        String className = cl.getName();
	        System.out.println("className:"+className);
	        
			if (mode.equalsIgnoreCase(SESSIONJSON_PARAM)) {
				//classmethod = cl.getMethod(methodname, new java.lang.Class[]{ ServiceSession.class,JSONObject.class });
				classmethod = this.getMethod(cl,methodname, new java.lang.Class[]{ ServiceSession.class,JSONObject.class });
			} else if (mode.equalsIgnoreCase(SESSIONSTRING_PARAM)) {
				//classmethod = cl.getMethod(methodname, new java.lang.Class[]{ ServiceSession.class,String.class });
				classmethod = this.getMethod(cl,methodname, new java.lang.Class[]{ ServiceSession.class,String.class });
			} else if (mode.equalsIgnoreCase(SESSIONLIST_PARAM)) {
				//classmethod = cl.getMethod(methodname, new java.lang.Class[]{ ServiceSession.class,String.class,List.class });
				classmethod = this.getMethod(cl,methodname, new java.lang.Class[]{ ServiceSession.class,String.class,List.class });
			} else if (mode.equalsIgnoreCase(STRING_PARAM)) {
				//classmethod = cl.getMethod(methodname, new java.lang.Class[]{ String.class });
				classmethod = this.getMethod(cl,methodname, new java.lang.Class[]{ String.class });
			}else if(mode.equalsIgnoreCase(SESSIONMULTIPARTFILE_PARAM)){
				classmethod = this.getMethod(cl,methodname, new java.lang.Class[]{ ServiceSession.class, String.class, MultipartFile.class });
			}else if(mode.equalsIgnoreCase(SESSIONOUTPUTSTREAM_PARAM)){
				classmethod = this.getMethod(cl,methodname, new java.lang.Class[]{ ServiceSession.class, String.class, OutputStream.class });
			}else if (mode.equalsIgnoreCase(VOID_PARAM)) {
				//classmethod = cl.getMethod(methodname);
				classmethod = this.getMethod(cl,methodname);
			}
		}
		catch(Exception ex)
		{
			// 找不到匹配方法
		}
				
		find.delete(0, find.length());
		if (classmethod != null) 
		{
			find.append("find");
			if (params != null) return classmethod.invoke(objinst, params);
			else return classmethod.invoke(objinst);
		}
		return null;
	}


}
