package org.liuhe.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	//验证是否为年龄格式的字符串
	public static boolean isNumner(String number){
		if(number==null||number.equals("")){
			return false;
		}
		if(number.matches("^(\\d+)$")){
			return true;
		}else{
			return false;
		}
	}
	//验证是否包含了Float型
	public static boolean isCFloat(String number){
		if(number==null||number.equals("")){
			return false;
		}
		if(number.matches("^(-?\\d{1,3})(.\\d{1,2})?$")){
			return true;
		}else{
			return false;
		}
	}
	//判断是否为###.##的数值
	public static boolean isFloat(String number){
		if(number==null||number.equals("")){
			return false;
		}
		if(number.matches("^(\\d{2,3})(.\\d{1,2})?$")){
			return true;
		}else{
			return false;
		}
	}
	//验证邮箱
	public static boolean checkEMail(String email){
		if(email==null||email.equals("")){
			return false;
		}
		//Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配   //下面复杂匹配   
		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);  
		if(m.matches()){
			return true;
		}else{
			return false;
		}
	}
	//判断是否为合格中控服务器请求地址
	public static boolean checkUrl(String url){
		if(url==null||url.equals("")){
			return false;
		}
		// Pattern.CASE_INSENSITIVE 不区分大小写
		// 仅仅以http://打头，不支持携带查询字符串
		// [\\w-\\.] 限制域名和路径仅仅由a-z0-9_-.这么几个字符
		//"^http://[\\w-\\.]+(?:/|(?:/[\\w\\.\\-]+)*(?:/[\\w\\.\\-]+\\.do))?$"
		Pattern p =  Pattern.compile("^(http|https)://[\\w-\\.]+(/[\\w\\.\\-]+)*$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(url);
		if(m.matches()){
			return true;
		}else{
			return false;
		}
	}
	
}