package com.zjh.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

/**
 * java读取文件
 * @author zjh
 *
 */
public class ReadFromFile {
	
	public static void main(String[] args) {
		//readFileByBytes("D:\\我的文档\\Pictures\\狼1.jpg","E:\\lang.jpg");
		readFileByChars("E:\\账号密码.txt");
	}
	
	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件
	 * @param filename
	 */
	public static void readFileByBytes(String filename,String outFileName) {
		File file = new File(filename);
		File outFile = new File(outFileName);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(file);
			//out = new FileOutputStream(outFile);
			int tempbyte;
			while((tempbyte = in.read()) != -1) {
				System.out.println(tempbyte);
				//out.write(tempbyte);
			}
			in.close();
			//out.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
            System.out.println("以字节为单位读取文件内容，一次读取多个字节");
            //一次读取多个字节
            byte[] tempbytes = new byte[100];
            //int byteread = 0;
            in = new FileInputStream(filename);
            out = new FileOutputStream(outFile);
            ReadFromFile.showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((in.read(tempbytes)) != -1) {
                out.write(tempbytes);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try {
        		if(in != null) {
        			in.close();
        		}
        		if(out != null) {
        			out.close();
        		}
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
	}
	
	 /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     * @param fileName 文件名
     */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
			int temchar = 0;
			while((temchar=reader.read()) != -1) {
				System.out.print((char)temchar);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     * @param fileName 文件名
     */
	public static void readFileByLine(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 显示输入流中剩余的字节数
     * @param in
     */
    public static void showAvailableBytes(InputStream in)
    {
        try {
            System.out.println("当前字节流输入流中剩余的字节数为:"+in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
