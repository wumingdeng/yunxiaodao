package org.liuhe.algorithm.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

public class HWeightConfig {
	private String com;
	private String botelv;
	private String stop;
	private String data;
	private String parity;
	private String weight;
	
	public HWeightConfig(){
		loadHWeightConfig();
		getComs();
		System.out.println("--------------------------");
		System.out.println("com是："+com);
		System.out.println("botelv是："+botelv);
		System.out.println("stop是："+stop);
		System.out.println("data是："+data);
		System.out.println("parity是："+parity);
		System.out.println("--------------------------");
	}
	
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getBotelv() {
		return botelv;
	}
	public void setBotelv(String botelv) {
		this.botelv = botelv;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getParity() {
		return parity;
	}
	public void setParity(String parity) {
		this.parity = parity;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWeight() {
		return weight;
	}
	
	public void loadHWeightConfig(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\hweight.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		this.setCom(prop.getProperty("com"));
		this.setBotelv(prop.getProperty("botelv"));
		this.setStop(prop.getProperty("stop"));
		this.setData(prop.getProperty("data"));
		this.setParity(prop.getProperty("parity"));
		this.setWeight(prop.getProperty("weight"));
	}
	
	public void saveHWeightConfig(){
		Properties prop = new Properties();
		prop.setProperty("com", this.getCom());
		prop.setProperty("botelv", this.getBotelv());
		prop.setProperty("stop", this.getStop());
		prop.setProperty("data", this.getData());
		prop.setProperty("parity", this.getParity());
		prop.setProperty("weight", this.getWeight());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"\\config\\hweight.properties");
			prop.store(fos, "");   
		    fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}
	
	public Vector<String> getComs(){
		System.out.println("--------------------------");
		System.out.println("检查到当前系统的串口是：");
		Vector<String> vector = new Vector<String>();
		Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
        while (en.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) en.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	vector.add(portId.getName());
            	System.out.println(portId.getName());
            }
        }
        return vector;
	}
	
	public String parity(int parity){
		int[] intArr = {SerialPort.PARITY_EVEN,SerialPort.PARITY_ODD,SerialPort.PARITY_NONE,SerialPort.PARITY_MARK,SerialPort.PARITY_SPACE};
		String[] strArr = {"偶","奇","无","标志","空格"};
		for(int i=0;i<strArr.length;i++){
			if(parity==intArr[i]){
				return strArr[i];
			}
		}
		return "无";
	}
	public int parity(String parity){
		String[] strArr = {"偶","奇","无","标志","空格"};
		int[] intArr = {SerialPort.PARITY_EVEN,SerialPort.PARITY_ODD,SerialPort.PARITY_NONE,SerialPort.PARITY_MARK,SerialPort.PARITY_SPACE};
		for(int i=0;i<intArr.length;i++){
			if(parity.equals(strArr[i])){
				return intArr[i];
			}
		}
		return SerialPort.PARITY_NONE;
	}
	public String stop(int stop){
		int[] intArr = {SerialPort.STOPBITS_1,SerialPort.STOPBITS_1_5,SerialPort.STOPBITS_2};
		String[] strArr = {"1","1.5","2"};
		for(int i=0;i<intArr.length;i++){
			if(stop==intArr[i]){
				return strArr[i];
			}
		}
		return "1";
	}
	public int stop(String stop){
		String[] strArr = {"1","1.5","2"};
		int[] intArr = {SerialPort.STOPBITS_1,SerialPort.STOPBITS_1_5,SerialPort.STOPBITS_2};
		for(int i=0;i<strArr.length;i++){
			if(stop.equals(strArr[i])){
				return intArr[i];
			}
		}
		return SerialPort.STOPBITS_1;
	}
	
}