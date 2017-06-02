package org.liuhe.algorithm.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;

public class FootConfig {
	
	private float[][] breadth;
	private String[] advice;
	private String[] status;
	
	public FootConfig(){
		loadBreadth();
		loadAdvice();
		loadStatus();
	}
	
	public float[][] getBreadth() {
		return breadth;
	}
	public String[] getStatus(){
		return status;
	}
	public String[] getAdvice() {
		return advice;
	}

	private void loadBreadth(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\foot_breadth_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		int size = prop.size();
		boolean first = true;
		Enumeration<Object> keys = prop.keys();
		String value;
		String key;
		for(int i=0;i<size;i++){
			if(keys.hasMoreElements()){
				key = (String) keys.nextElement();
				value = prop.getProperty(key);
				String[] values = value.split(";");
				if(first){							//确定数组有多少列
					breadth = new float[size][values.length+1];
					first = false;
				}
				breadth[i][0] = Float.parseFloat(key);
				for(int j=1;j<values.length+1;j++){
					breadth[i][j]=Float.parseFloat(values[j-1]);
				}
			}
		}
	}
	
	private void loadStatus(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\foot_status_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		status = new String[5];
		status[0] = changeEncoding(prop.getProperty("status01"));
		status[1] = changeEncoding(prop.getProperty("status02"));
		status[2] = changeEncoding(prop.getProperty("status03"));
		status[3] = changeEncoding(prop.getProperty("status04"));
		status[4] = changeEncoding(prop.getProperty("status05"));
	}
	
	private void loadAdvice(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\foot_advice_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		advice = new String[3];
		advice[0] = changeEncoding(prop.getProperty("flat"));
		advice[1] = changeEncoding(prop.getProperty("normal"));
		advice[2] = changeEncoding(prop.getProperty("high"));
	}
	
	private String changeEncoding(String property){
		try {
			return new String(property.getBytes("iso-8859-1"), "gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private void loadAdviceBytxt(){
		File file = new File(System.getProperty("user.dir")+"\\config\\suggestion.txt");
		BufferedReader reader = null;
		advice = new String[3];
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int i = 0;
			while ((tempString = reader.readLine()) != null){
				advice[i] = tempString;
				i++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	public void inpuConfig(){
		for(int i=0;i<breadth.length;i++){
			System.out.println((int)breadth[i][0]+" : "+breadth[i][1]+";"+breadth[i][2]+";"+breadth[i][3]+";"+breadth[i][4]);
		}
		for(int i=0;i<status.length;i++){
			System.out.println("足弓态势"+(i+1)+"："+status[i]);
		}
		for(int i=0;i<advice.length;i++){
			System.out.println("专家意见"+(i+1)+"："+advice[i]);
		}
	}
	/*public static void main(String[] args){
		FootConfig config = new FootConfig();
		String[] status = config.getStatus();
		for(int i=0;i<status.length;i++){
			System.out.println("足弓态势"+(i+1)+"："+status[i]);
		}
		String[] advice = config.getAdvice();
		for(int i=0;i<advice.length;i++){
			System.out.println("专家意见"+(i+1)+"："+advice[i]);
		}
	}*/
}