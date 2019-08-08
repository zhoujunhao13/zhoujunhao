package com.zjh.test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.springframework.util.StringUtils;

public class Test {
	
	public static void main(String[] args) {
		/*language();
		StringBuffer a = new StringBuffer();
		StringBuffer b = new StringBuffer();
		String s = "11.22.33.44";
		int i = s.lastIndexOf(".");
		System.out.println(i);
		a.append(s.substring(0, i));
		b.append(s.substring(i+1));
		System.out.println(a+"   "+b);*/
		/*new Test().testInvoke(Test.class, "testRegixs", "你好那是十多年da jia hao\\\\");
		Date date = new Date(1559256315L);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
		System.out.println(dateFormat.format(date));
		System.out.println(new Date().compareTo(date));
		
		printList();*/
		
		//getMapValue();
		
		//orderByName("E:\\testorder");
		
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS").format(new Date(1560325770692L)));
		
		System.out.println(StringUtils.isEmpty(""));
	}
	
	public static void aa() {
		new Thread(() -> System.out.println("thread")).start();
		System.out.println("11");
	}
	
	public static void printList() {
		List<String> list = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		list.forEach(System.out::println);
		list.forEach(e->System.out.println(e));
	}
	
	public static void language() {
		List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
		  
	    System.out.println("Languages which starts with J :");
	    filter(languages, (str)->((String) str).startsWith("J"));
	 
	    System.out.println("Languages which ends with a ");
	    filter(languages, (str)->((String) str).endsWith("a"));
	 
	    System.out.println("Print all languages :");
	    filter(languages, (str)->true);
	 
	    System.out.println("Print no language : ");
	    filter(languages, (str)->false);
	 
	    System.out.println("Print language whose length greater than 4:");
	    filter(languages, (str)->((String) str).length() > 4);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void filter(List<String> names, Predicate condition) {
	    for(String name: names)  {
	        if(condition.test(name)) {
	            System.out.println(name + " ");
	        }
	    }
	}
	
	public String testRegixs(String param) {
		String regex="\\\\";
		//String s = "你好那是十多年\\";
		String s = param;
		String str = s.replaceAll(regex, "");
		System.out.println(str);
		return str;
	}
	
	public void testInvoke(Class<?> cls, String methodName, String param) {
		try {
			cls.getInterfaces();
			Constructor<?> con = cls.getConstructor();
			Object obj = con.newInstance();
			Method method = cls.getMethod(methodName, new Class[] {String.class});
			Object retObj = method.invoke(obj, new Object[] {param});
			System.out.println(retObj);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void getMapValue() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("e", "5");
		map.put("f", "6");
		System.out.println(map.values());
		System.out.println(map.keySet());
	}
	
	//按照文件名称排序
	public static void orderByName(String fliePath) {
	  List<File> files = Arrays.asList(new File(fliePath).listFiles());
	  Collections.sort(files, new Comparator< File>() {
	   @Override
	   public int compare(File o1, File o2) {
		if (o1.isDirectory() && o2.isFile())
	          return -1;
		if (o1.isFile() && o2.isDirectory())
	          return 1;
		return o1.getName().compareTo(o2.getName());
	   }
	  });
	   for (File f : files) {
	     System.out.println(f.getName());
	    }
	  }
	
	public void testThreadPool() {
		BlockingQueue<Runnable> workQueue = null;
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 2, TimeUnit.MINUTES, workQueue);
		
	}
}
