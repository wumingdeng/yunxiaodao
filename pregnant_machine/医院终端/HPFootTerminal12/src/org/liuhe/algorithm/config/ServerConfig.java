package org.liuhe.algorithm.config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.liuhe.register.GetMacAddr;

import net.sf.json.JSONObject;

public class ServerConfig {
	
	private String serverUrl;
	private String mac;
	private boolean available;
	
	private String hospital_no;
	private String hospital_name;
	private int hospital_scene;
	private String hospital_phone;
	private String hospital_web;
	private String machine_type;
	// modify by kael
	// machine kind 1 - old a3,2 - latest a4
	private int machine_kind;
	public static short MACHINE_KIND_A3=1;
	public static short MACHINE_KIND_A4=2;
	public static String receiptPrinterName;
	public static String reportPrinterName;
	public static boolean showIdPage;
	// modify by kael over
	
	private String wechatUrl;
	private String verifyUrl;
	private String userinfoUrl;
	private String clinicUrl;
	private String uploadUrl;
	private String dataUrl;
	private String lastReportUrl;
	
	private String worktime;
	private String interval;
	private String appName;
	
	public ServerConfig(){
		hospital_scene = 100001;
		loadServerConfig();
	}
	public int getHospitalScene() {
		return this.hospital_scene;
	}
	public void setHospitalScene(int scene) {
		this.hospital_scene = scene;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getMac() {
		return mac;
	}
	
	public void loadServerConfig(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\config\\server.properties");
			prop.load(fis);
			fis.close();
		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		}catch(IOException e3) {
			e3.printStackTrace();
		}
		setServerUrl(prop.getProperty("serverUrl"));
		setMachine_kind(Integer.parseInt(prop.getProperty("MACHINE_KIND")));
		if(prop.containsKey("RECEIPT")){
			if(prop.containsKey("REPORT")){
				ServerConfig.receiptPrinterName = prop.getProperty("RECEIPT");
				ServerConfig.reportPrinterName = prop.getProperty("REPORT");
			}else{
				ServerConfig.receiptPrinterName = prop.getProperty("RECEIPT");
				ServerConfig.reportPrinterName = "";
			}
		}else{
			ServerConfig.receiptPrinterName = "";
			ServerConfig.reportPrinterName = "";
		}
		if(prop.containsKey("SHOWID_PAGE")){
			ServerConfig.showIdPage=Integer.parseInt(prop.getProperty("SHOWID_PAGE"))==1?true:false;
		}else{
			ServerConfig.showIdPage=false;
		}
		
		getServerPara();
	}
	
	public void saveServerConfig(){
		Properties prop = new Properties();
		prop.setProperty("serverUrl", getServerUrl());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"\\config\\server.properties");
			prop.store(fos, "");
		    fos.close();
		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		}catch(IOException e3) {
			e3.printStackTrace();
		}
		getServerPara();
	}

	public void setWechatUrl(String wechatUrl) {
		this.wechatUrl = wechatUrl;
	}
	public String getWechatUrl() {
		return wechatUrl;
	}
	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}
	public String getVerifyUrl() {
		return verifyUrl;
	}
	public void setUserinfoUrl(String userinfoUrl) {
		this.userinfoUrl = userinfoUrl;
	}
	public void setClinicUrl(String clinicUrl) {
		this.clinicUrl = clinicUrl;
	}
	public String getClinicUrl() {
		return clinicUrl;
	}
	public String getUserinfoUrl() {
		return userinfoUrl;
	}
	public String getUploadUrl() {
		return uploadUrl;
	}
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	public String getDataUrl() {
		return dataUrl;
	}
	
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getInterval() {
		return interval;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setHospital_no(String hospital_no) {
		this.hospital_no = hospital_no;
	}
	public String getHospital_no() {
		return hospital_no;
	}
	public String getHospital_name() {
		return hospital_name;
	}
	public void setHospital_name(String hospitalName) {
		hospital_name = hospitalName;
	}
	public String getHospital_phone() {
		return hospital_phone;
	}
	public void setHospital_phone(String hospitalPhone) {
		hospital_phone = hospitalPhone;
	}
	public String getHospital_web() {
		return hospital_web;
	}
	public void setHospital_web(String hospitalWeb) {
		hospital_web = hospitalWeb;
	}
	public void setMachine_type(String machine_type) {
		this.machine_type = machine_type;
	}
	public String getMachine_type() {
		return machine_type;
	}
	public void setMachine_kind(int machine_kind) {
		this.machine_kind = machine_kind;
	}
	public int getMachine_kind() {
		return machine_kind;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public String getWorktime() {
		return worktime;
	}

	public void getServerPara(){
		try {
			URL url = new URL(getServerUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
            
            //当表单的ACTION为POST的时候，浏览器把form数据封装到http body中，然后发送到服务器
            //就可以通过request.getParameterMap()获取到参数了，但是在向服务器发送大量的文本、包含非ASCII字符的文本或二进制数据时这种编码方式效率很低
            //conn.setRequestProperty("Content-Type","multipart/form-data; charset=UTF-8; boundary="+boundary);
            //conn.connect();//连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
			//String param = "sign="+sign;//同样可以
            
            mac = GetMacAddr.getMacAddress();
            String sign = "sign="+URLEncoder.encode("liuhe","UTF-8");
            String oper = "mac_id="+URLEncoder.encode(mac,"UTF-8");
            String para = sign+"&"+oper;
            
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(para);
            out.flush();
            out.close();
			
			InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(),"utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			conn.disconnect();
			
			String outfile = buffer.toString();
			System.out.println("访问参数配置接口:"+outfile);
			JSONObject jsonObject = JSONObject.fromObject(outfile);
			if(jsonObject != null){
				int errcode = jsonObject.getInt("errcode");
	            if(errcode == 0){
	            	setAvailable(true);
	            	if(jsonObject.containsKey("hospital_scene")){
	            		setHospitalScene(jsonObject.getInt("hospital_scene"));
	            	}
	            	setMachine_type(jsonObject.getString("machine_type"));
					setHospital_no(jsonObject.getString("hospital_no"));
					setHospital_name(jsonObject.getString("hospital_name"));
					setWorktime(jsonObject.getString("worktime"));
					setWechatUrl(jsonObject.getString("wechat_url"));
					setVerifyUrl(jsonObject.getString("verify_url"));
					setUserinfoUrl(jsonObject.getString("userinfo_url"));
					setClinicUrl(jsonObject.getString("clinic_url"));
					setUploadUrl(jsonObject.getString("upload_url"));
					setDataUrl(jsonObject.getString("data_url"));
					setLastReportUrl(jsonObject.getString("latestreport_url"));
					setInterval(jsonObject.getString("space_day"));
					setAppName(jsonObject.getString("app_name"));
					System.out.println("-----------------------");
					System.out.println("Server服务器地址："+serverUrl);
					System.out.println("hospital_no："+hospital_no);
					System.out.println("hospital_name："+hospital_name);
					System.out.println("machine_type："+machine_type);
					System.out.println("wechatUrl："+wechatUrl);
					System.out.println("verifyUrl："+verifyUrl);
					System.out.println("userinfoUrl："+userinfoUrl);
					System.out.println("clinicUrl："+clinicUrl);
					System.out.println("uploadUrl："+uploadUrl);
					System.out.println("dataUrl："+dataUrl);
					System.out.println("worktime："+worktime);
					System.out.println("interval："+interval);
					System.out.println("appName："+appName);
					System.out.println("-----------------------");
	            }else{
	            	setAvailable(false);
	            }
			}else{
				setAvailable(false);
			}
		}catch (Exception e) {
			setAvailable(false);
			e.printStackTrace();
		}
	}	
	
	/**
	 * 验证连接数据库接口是否可用
	 * */
	public static String testSQLConnector(String connectorUrl){
		try {
			//String boundary = "-------------------------7e020233150564";
		    //String prefix = "--";
		    //String end = "\r\n";
		    
			URL url = new URL(connectorUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
            //当表单的ACTION为POST的时候，浏览器把form数据封装到http body中，然后发送到服务器
            //就可以通过request.getParameterMap()获取到参数了，但是在向服务器发送大量的文本、包含非ASCII字符的文本或二进制数据时这种编码方式效率很低
			
			//conn.setRequestProperty("Content-Type","multipart/form-data; charset=UTF-8; boundary="+boundary);
			//conn.connect();
			
			//DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			//out.writeBytes(prefix+boundary+end);
            //out.writeBytes("Content-Disposition: form-data; name=\"oper\";"+end);
            //out.writeBytes(end);
            //out.writeBytes("test");
            //out.writeBytes(end);
            //out.writeBytes(prefix + boundary + prefix + end);  
            //out.flush();
            //out.close();
			
            String sign = "sign="+URLEncoder.encode("liuhe","UTF-8");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(sign);
            out.flush();
            out.close();
            
			InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(),"utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			conn.disconnect();
			return buffer.toString();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getLastReportUrl() {
		return lastReportUrl;
	}

	public void setLastReportUrl(String lastReportUrl) {
		this.lastReportUrl = lastReportUrl;
	}
	
}