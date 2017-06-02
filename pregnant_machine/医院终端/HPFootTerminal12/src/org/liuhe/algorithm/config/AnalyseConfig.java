package org.liuhe.algorithm.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AnalyseConfig {
	private String time_erosion;			//腐蚀、膨胀
	private String time_dilate;
	private String stamp_radius;			//stamp半径和阈值
	private String stamp_threshold;
	private String useSpecial;				//是否应用特殊滤波
	
	public AnalyseConfig(){					//构造函数，读取分析参数
		loadAnalyseConfig();
	}
	
	public String getTime_erosion() {
		return time_erosion;
	}
	public void setTime_erosion(String timeErosion) {
		time_erosion = timeErosion;
	}
	public String getTime_dilate() {
		return time_dilate;
	}
	public void setTime_dilate(String timeDilate) {
		time_dilate = timeDilate;
	}
	public String getStamp_radius() {
		return stamp_radius;
	}
	public void setStamp_radius(String stampRadius) {
		stamp_radius = stampRadius;
	}
	public String getStamp_threshold() {
		return stamp_threshold;
	}
	public void setStamp_threshold(String stampThreshold) {
		stamp_threshold = stampThreshold;
	}
	public void setUseSpecial(String useSpecial) {
		this.useSpecial = useSpecial;
	}
	public String getUseSpecial() {
		return useSpecial;
	}
	
	public void loadAnalyseConfig(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\scan_analyse_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		this.setTime_dilate(prop.getProperty("time_dilate"));
		this.setTime_erosion(prop.getProperty("time_erosion"));
		this.setStamp_radius(prop.getProperty("stamp_radius"));
		this.setStamp_threshold(prop.getProperty("stamp_threshold"));
		this.setUseSpecial(prop.getProperty("use_special"));
	}
	
	public void saveAnalyseConfig(){
		Properties prop = new Properties();
		prop.setProperty("time_erosion", this.getTime_erosion());
		prop.setProperty("time_dilate", this.getTime_dilate());
		prop.setProperty("stamp_radius", this.getStamp_radius());
		prop.setProperty("stamp_threshold", this.getStamp_threshold());
		prop.setProperty("use_special", this.getUseSpecial());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"\\config\\scan_analyse_config.properties");
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
	
}
