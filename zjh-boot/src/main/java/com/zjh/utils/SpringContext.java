package com.zjh.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    //通过 BeanFactory 接口创建实例
    static ApplicationContext context = null;
    
    @Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		SpringContext.context = arg0;
	}

    /*public static ConfigurableApplicationContext run(Class<?> source, String... args) {
	    	context = SpringApplication.run(source, args);
	    	SpringContext.setInstance(context);
	    	return context;
    }*/
    
	public static Object getBean(String name)
	{
		return context.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> clazz)
	{
		return (T) context.getBean(name, clazz);
	}

	public static Object getBean(String name, Object... args)
	{
		return context.getBean(name, args);
	}

	public static <T> T getBean(Class<T> requiredType, Object... args)
	{
		return context.getBean(requiredType, args);
	}

	/*public static BeanFactory getContext()
	{
		return context;
	}

	public synchronized static void setInstance(ConfigurableApplicationContext myContext)
	{
		context = myContext;
	}*/

}
