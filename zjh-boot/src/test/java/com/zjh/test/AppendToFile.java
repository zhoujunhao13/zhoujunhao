package com.zjh.test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 追加内容到文件尾部
 * Created by zjh
 */
public class AppendToFile {
	
	/**
	 * 追加文件：使用FileWriter
	 * @param fileName 文件名
	 * @param content  追加内容
	 */
	public static void appendFile(String fileName,String content) {
		try {
			FileWriter writer = new FileWriter(fileName,true);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		appendFile("E:\\111.txt", "\n4444");
	}
	

}
