package org.liuhe.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	//��֤�Ƿ�Ϊ�����ʽ���ַ���
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
	//��֤�Ƿ������Float��
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
	//�ж��Ƿ�Ϊ###.##����ֵ
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
	//��֤����
	public static boolean checkEMail(String email){
		if(email==null||email.equals("")){
			return false;
		}
		//Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //��ƥ��   //���渴��ƥ��   
		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);  
		if(m.matches()){
			return true;
		}else{
			return false;
		}
	}
	//�ж��Ƿ�Ϊ�ϸ��пط����������ַ
	public static boolean checkUrl(String url){
		if(url==null||url.equals("")){
			return false;
		}
		// Pattern.CASE_INSENSITIVE �����ִ�Сд
		// ������http://��ͷ����֧��Я����ѯ�ַ���
		// [\\w-\\.] ����������·��������a-z0-9_-.��ô�����ַ�
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