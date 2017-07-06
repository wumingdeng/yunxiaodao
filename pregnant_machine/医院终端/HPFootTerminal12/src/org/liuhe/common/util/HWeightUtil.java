package org.liuhe.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import org.liuhe.algorithm.config.HWeightConfig;

public class HWeightUtil implements SerialPortEventListener{
	
	private HWeightConfig hwConfig = null;
	private SerialPort serialPort = null;
	private String result = "";
	private boolean isOK = false;
	
	public HWeightUtil(HWeightConfig hwConfig){
		this.hwConfig = hwConfig;
	}
	public HWeightUtil(String com, String botelv, String data, String stop, String parity){
		hwConfig = new HWeightConfig();
		hwConfig.setCom(com);
		hwConfig.setBotelv(botelv);
		hwConfig.setData(data);
		hwConfig.setStop(stop);
		hwConfig.setParity(parity);
	}
	
	public boolean doActionPerformed(){
		result = "";
		isOK = false;
		OutputStream outputStream = null;
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(hwConfig.getCom());
			serialPort = (SerialPort) portId.open("YXD_Frame", 2000);
			//serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setSerialPortParams(Integer.parseInt(hwConfig.getBotelv()), Integer.parseInt(hwConfig.getData())
					, Integer.parseInt(hwConfig.getStop()), Integer.parseInt(hwConfig.getParity()));
			outputStream = serialPort.getOutputStream();
			outputStream.write("R".getBytes());
			outputStream.flush();
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()){
	       case SerialPortEvent.CTS :
	            System.out.println("CTS event occured.");
	            break;
	       case SerialPortEvent.CD :
	            System.out.println("CD event occured.");
	            break;
	       case SerialPortEvent.BI :
	            System.out.println("BI event occured.");
	            break; 
	       case SerialPortEvent.DSR :
	            System.out.println("DSR event occured.");
	            break;
	       case SerialPortEvent.FE : 
	            System.out.println("FE event occured.");
	            break;
	       case SerialPortEvent.OE : 
	            System.out.println("OE event occured.");
	            break;
	       case SerialPortEvent.PE : 
	            System.out.println("PE event occured.");
	            break;
	       case SerialPortEvent.RI : 
	            System.out.println("RI event occured.");
	            break;
	       case SerialPortEvent.OUTPUT_BUFFER_EMPTY : 
	            System.out.println("OUTPUT_BUFFER_EMPTY event occured.");
	            break;
	       case SerialPortEvent.DATA_AVAILABLE : 
	            System.out.println("DATA_AVAILABLE event occured.");
		}
		InputStream inputStream = null;
		int newData = 0;
		try { 
			inputStream = serialPort.getInputStream();
			while (!result.endsWith("\r\n")){
				newData = inputStream.read();
				result = result + (char)newData;
			}
			result = result.trim();
			isOK = true;
			System.out.println("循环结束........");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally{
			try {
				inputStream.close();
				serialPort.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isOK(){
		return isOK;
	}
	public String getResultStr(){
		return result;
	}
	
	public Float getWeight(){
		String patternStr = "(wn)(-?\\d{1,}\\.\\d{1,})(lb|kg)";
	 	Pattern p = Pattern.compile(patternStr);
		Matcher m = p.matcher(result);
		while (m.find()){
			result = m.group(2);
		}
		System.out.println("测到的体重字符串是："+result);
		Float hwight_float = null;
		try{
			hwight_float = Float.parseFloat(result);
		}catch(Exception e){
			e.printStackTrace(); 
		}
		return hwight_float;
	}
	
}