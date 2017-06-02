package org.liuhe.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class FileIOUtil {
	// 以字节为单位读取文件,一次读一个字节
	public static String readFileByByte(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			int tempbyte;
			String content = "";
			while ((tempbyte = in.read()) != -1) {
				//System.out.write(tempbyte);
				content = content + (char)tempbyte;
			}
			content = new String(content.getBytes("iso-8859-1"), "gbk");
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	// 以字节为单位读取文件，一次读取多个字节
	public static String readFileByBytes(String fileName){
		InputStream in = null;
		try {
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			String content = "";
			in = new FileInputStream(fileName);
			System.out.println("当前字节输入流中的字节数为:" + in.available());
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
				content = content + new String(tempbytes,"gbk").trim();
			}
			return content;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	// 以字符为单位读取文件，一次读取一个字符
	public static void readFileByChar(String fileName){
		File file = new File(fileName);
		Reader read = null;
		try{
			read = new FileReader(file);
			int tempchar;
			String content = "";
			while((tempchar = read.read()) != -1){
				content = content + (char)tempchar;
			}
			read.close();
			System.out.println(content);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// 以字符为单位读取文件，一次读取多个字符
	public static String readFileByChars(String fileName){
		File file = new File(fileName);
		Reader read = null;
		try{
			char[] tempchars = new char[30];
			int charread = 0;
			read = new FileReader(file);
			String content = "";
			while((charread = read.read(tempchars)) != -1){
				for (int i = 0; i < charread; i++) {
					content = content + tempchars[i];
				}
			}
			return content;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(read != null){
				try{
					read.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
	}
	// 以行为单位读取文件，一次读取一行
	public static String readFileByLines(String fileName){
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			String content = "";
			while ((tempString = reader.readLine()) != null){
				//将会过滤掉"\r\n"
				System.out.println("line " + line + ": " + tempString);
				line++;
				content = content + tempString + "\r\n";
			}
			reader.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	// 以字节为单位写文件
	public static boolean writeFileByBytes(String fileName,String content){
		File file = new File(fileName);
		OutputStream out= null;
		try {
			out = new FileOutputStream(file);
			byte[] bytes = content.getBytes();
			out.write(bytes);
			System.out.println("写文件" + file.getAbsolutePath() + "成功！");
			return true;
		} catch (IOException e){
			System.out.println("写文件" + file.getAbsolutePath() + "失败！");
			e.printStackTrace();
			return false;
		} finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	// 以字符为单位写文件
	public static boolean writeFileByChars(String fileName,String content){	
		File file = new File(fileName);
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file));
			writer.write(content);
			System.out.println("写文件" + file.getAbsolutePath() + "成功！");
			return true;
		} catch (IOException e){
			System.out.println("写文件" + file.getAbsolutePath() + "失败！");
			e.printStackTrace();
			return false;
		} finally {
			if (writer != null){
				try {
					writer.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	// 以行为单位写文件
	public static boolean writeFileByLines(String fileName,String content){
		File file = new File(fileName);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file));
			writer.println(content);
			//writer.print(true);//写入布尔类型
			//writer.print(123);//写入整数类型
			//writer.println();// 换行
			writer.flush();// 写入刷新文件
			System.out.println("写文件" + file.getAbsolutePath() + "成功！");
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("写文件" + file.getAbsolutePath() + "失败！");
			e.printStackTrace();
			return false;
		} finally {
			if (writer != null){
				writer.close();
			}
		}
	}
}