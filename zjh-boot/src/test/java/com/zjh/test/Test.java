package com.zjh.test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Test {
	
	public static void main(String[] args) {
		//language();
		StringBuffer a = new StringBuffer();
		StringBuffer b = new StringBuffer();
		String s = "11.22.33.44";
		int i = s.lastIndexOf(".");
		System.out.println(i);
		a.append(s.substring(0, i));
		b.append(s.substring(i+1));
		System.out.println(a+"   "+b);
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

}
