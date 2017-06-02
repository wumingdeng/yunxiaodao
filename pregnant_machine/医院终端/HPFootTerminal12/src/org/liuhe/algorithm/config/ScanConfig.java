package org.liuhe.algorithm.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.asprise.util.jtwain.JTwainException;
import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;

public class ScanConfig {
	
	public ScanConfig(){
		long start = System.currentTimeMillis();
		System.out.println("[start time:"+start);
		loadScanBasicConfig();					//扫描仪基本设置
		loadScanHighConfig();					//扫描图像外观设置
		loadAdjustConfig();						//扫描图像缩放设置
		long end = System.currentTimeMillis();
		System.out.println("end time:"+start);
		System.out.println("the time of loading all scan properties："+(end-start)+"]");
	}
	
	private String source;						//扫描来源
	private String type;						//图像类型	//彩色
	private String dpi;							//分辨率		//100
	private String suffix;						//后缀		//.jpg .bmp
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDpi() {
		return dpi;
	}
	public void setDpi(String dpi) {
		this.dpi = dpi;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	private String cut_top;						//边缘剪切
	private String cut_left;
	private String cut_right;
	private String cut_bottom;
	private String cut_b02;
	private String cut_b03;
	private String scan_bright;					//扫描设置：亮度、对比度
	private String scan_constrast;
	public String getCut_top() {
		return cut_top;
	}
	public void setCut_top(String cutTop) {
		cut_top = cutTop;
	}
	public String getCut_left() {
		return cut_left;
	}
	public void setCut_left(String cutLeft) {
		cut_left = cutLeft;
	}
	public String getCut_right() {
		return cut_right;
	}
	public void setCut_right(String cutRight) {
		cut_right = cutRight;
	}
	public String getCut_bottom() {
		return cut_bottom;
	}
	public void setCut_bottom(String cutBottom) {
		cut_bottom = cutBottom;
	}
	public String getScan_bright() {
		return scan_bright;
	}
	public void setScan_bright(String bright) {
		this.scan_bright = bright;
	}
	public String getScan_constrast(){
		return scan_constrast;
	}
	public void setScan_constrast(String constrast) {
		this.scan_constrast = constrast;
	}
	public String getCut_b02() {
		return cut_b02;
	}
	public void setCut_b02(String cutB02) {
		cut_b02 = cutB02;
	}
	public String getCut_b03() {
		return cut_b03;
	}
	public void setCut_b03(String cutB03) {
		cut_b03 = cutB03;
	}

	private String ave_x;						//X方向扫描校正
	private String ave_y;						//Y方向扫描校正
	private String use_scan;					//是否扫描导入时应用
	private String scale_x;						//水平变换
	private String scale_y;						//垂直变换
	private String scale_z;						//倒置变换
	private String use_right;					//是否当前为右脚
	public String getAve_x() {
		return ave_x;
	}
	public void setAve_x(String aveX) {
		ave_x = aveX;
	}
	public String getAve_y() {
		return ave_y;
	}
	public void setAve_y(String aveY) {
		ave_y = aveY;
	}
	public String getUse_scan() {
		return use_scan;
	}
	public void setUse_scan(String useScan) {
		use_scan = useScan;
	}
	public String getScale_x() {
		return scale_x;
	}
	public void setScale_x(String scaleX) {
		scale_x = scaleX;
	}
	public String getScale_y() {
		return scale_y;
	}
	public void setScale_y(String scaleY) {
		scale_y = scaleY;
	}
	public String getScale_z() {
		return scale_z;
	}
	public void setScale_z(String scaleZ) {
		scale_z = scaleZ;
	}
	public String getUse_right() {
		return use_right;
	}
	public void setUse_right(String useRight) {
		use_right = useRight;
	}
	public void loadScanBasicConfig(){
		System.out.println("loadScanBasicConfig");
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\scan_basic_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		this.setSource(prop.getProperty("source"));
		this.setType(prop.getProperty("type"));
		this.setDpi(prop.getProperty("dpi"));
		this.setSuffix(prop.getProperty("suffix"));
	}
	public void loadScanHighConfig(){
		System.out.println("loadScanHighConfig");
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\scan_high_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		this.setCut_top(prop.getProperty("cut_top"));
		this.setCut_left(prop.getProperty("cut_left"));
		this.setCut_bottom(prop.getProperty("cut_bottom"));
		this.setCut_right(prop.getProperty("cut_right"));
		this.setScan_bright(prop.getProperty("scan_bright"));
		this.setScan_constrast(prop.getProperty("scan_contrast"));
		this.setCut_b02(prop.getProperty("cut_b02"));
		this.setCut_b03(prop.getProperty("cut_b03"));
	}
	
	public void loadAdjustConfig(){				//载入扫描校验参数
		System.out.println("loadAdjustConfig");
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\scan_adjust_config.properties");
			prop.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		this.setAve_x(prop.getProperty("ave_x"));
		this.setAve_y(prop.getProperty("ave_y"));
		this.setUse_scan(prop.getProperty("use_scan"));
		this.setScale_x(prop.getProperty("scale_x"));
		this.setScale_y(prop.getProperty("scale_y"));
		this.setScale_z(prop.getProperty("scale_z"));
		this.setUse_right(prop.getProperty("use_right"));
	}
	
	public void saveScanBasicConfig(){
		Properties prop = new Properties();
		prop.setProperty("source", this.getSource());
		prop.setProperty("type", this.getType());
		prop.setProperty("dpi", this.getDpi());
		prop.setProperty("suffix", this.getSuffix());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"\\config\\scan_basic_config.properties");
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
	public void saveScanHighConfig(){
		Properties prop = new Properties();
		prop.setProperty("cut_left", this.getCut_left());
		prop.setProperty("cut_bottom", this.getCut_bottom());
		prop.setProperty("cut_right", this.getCut_right());
		prop.setProperty("cut_top", this.getCut_top());
		prop.setProperty("scan_contrast",this.getScan_constrast());
		prop.setProperty("scan_bright",this.getScan_bright());
		prop.setProperty("cut_b02", this.getCut_b02());
		prop.setProperty("cut_b03", this.getCut_b03());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"\\config\\scan_high_config.properties");
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
	
	public void saveAdjustConfig(){				//保存扫描校正参数
		Properties prop = new Properties();
		prop.setProperty("ave_x", this.getAve_x());
		prop.setProperty("ave_y", this.getAve_y());
		prop.setProperty("use_scan", this.getUse_scan());
		prop.setProperty("scale_x", this.getScale_x());
		prop.setProperty("scale_y", this.getScale_y());
		prop.setProperty("scale_z", this.getScale_z());
		prop.setProperty("use_right", this.getUse_right());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"\\config\\scan_adjust_config.properties");
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
	
	public void outputConfig(){					//输出配置参数信息
		System.out.println("source:"+this.getSource());
		System.out.println("type:"+this.getType());
		System.out.println("dpi:"+this.getDpi());
		System.out.println("suffix:"+this.getSuffix());
		System.out.println("right:"+this.getCut_right());
		System.out.println("top:"+this.getCut_top());
		System.out.println("left:"+this.getCut_left());
		System.out.println("bottom:"+this.getCut_bottom());
		System.out.println("scan_bright:"+this.getScan_bright());
		System.out.println("scan_contrast:"+this.getScan_constrast());
	}
	
	public boolean checkScanAvailable(){
		try {
			if(SourceManager.instance().isTwainAvailable()){
				Source[] twainSources = SourceManager.instance().getAllSources();
				if(twainSources != null && twainSources.length>0){
					String[] source_name=new String[twainSources.length];
					for(int i=0;i<source_name.length;i++){
						source_name[i] = twainSources[i].getSourceName();
					}
					boolean have = false;
					for(int i=0;i<twainSources.length;i++){
						if(source.equals(source_name[i])){
							have = true;
							break;
						}
					}
					return have;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(JTwainException e1) {
			e1.printStackTrace();
			return false;
		}finally{
			try{
				SourceManager.closeSourceManager();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
//end...ScanConfig.java
}