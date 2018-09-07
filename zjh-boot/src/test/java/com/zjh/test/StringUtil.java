package com.zjh.test;

public class StringUtil {
	
	private static int	count	= 1000;
	/**
	 * 组装 String
	 * 
	 * @return
	 */
	public static String getString()
	{
		String str = "";
		for (int i = 0; i < count; i++)
		{
			str += "abc";
		}
		return str;
	}
	/**
	 * 组装StringBuilder并转化为String
	 * 
	 * @return
	 */
	public static String getStringBuilder()
	{
		StringBuilder strBuilder = new StringBuilder("");
		for (int i = 0; i < count; i++)
		{
			strBuilder.append("abc");
		}
		String str = strBuilder.toString();
		return str;
	}
	/**
	 * 组装StringBuffer并转化为String
	 * 
	 * @return
	 */
	public static String getStringBuffer()
	{
		StringBuffer strBuffer = new StringBuffer("");
		for (int i = 0; i < count; i++)
		{
			strBuffer.append("abc");
		}
		String str = strBuffer.toString();
		return str;
	}
	/**
	 * 打印字符串
	 * 
	 * @param s
	 */
	public static void print(String s)
	{
		System.out.println(s);
	}
	public static void main(String[] args)
	{
		/*
		 * 获得getString()运行时间
		 */
		long startTime = System.currentTimeMillis(); // 获取开始时间
		StringUtil.getString();
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("String 组装 耗时： " + (endTime - startTime) + "ms");
		
		/*
		 * 获得getStringBuilder()运行时间
		 */
		startTime = System.currentTimeMillis(); // 获取开始时间
		StringUtil.getStringBuilder();
		endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("StringBulider 组装并转化为String 耗时： " + (endTime - startTime) + "ms");
		
		/*
		 * 获得getStringBuffer()运行时间
		 */
		startTime = System.currentTimeMillis(); // 获取开始时间
		StringUtil.getStringBuffer();
		endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("StringBuffer  组装并转化为String 耗时： " + (endTime - startTime) + "ms");
		
	}

}
