package org.liuhe.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.liuhe.algorithm.config.ServerConfig;
import org.liuhe.common.util.FileIOUtil;
import org.liuhe.common.util.HttpUtil;

public class FootStudyDAO {

	private ServerConfig serverConfig;
	public FootStudyDAO(ServerConfig serverConfig){
		this.serverConfig = serverConfig;
	}
		
	/**
	 * java 同步方法的使用，防止多线程同时执行方法。
	 * synchronized方法加锁,不管哪一个线程运行到这个方法时,都要检查有没有其它线程正在用这个方法，
	 * 有的话要等正在使用synchronized方法的线程运行完这个方法后再运行此线程,没有的话,直接运行。
	 */
	public synchronized boolean addStudyToTxt(FootStudyInfo object){
		String json = packgeJson(object,"add");
        String content = "sign=liuhe&oper=add&data="+json;
        String fileName = System.getProperty("user.dir")+"\\scan\\"+object.getMac_id()+".dat";
        return FileIOUtil.writeFileByBytes(fileName, content);
	}
	public synchronized boolean addStudyToSQL(String content){
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getDataUrl(),"POST",content);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode == 0){
        	return true;
        }else{
        	return false;
        }
	}
	public boolean editStudy(FootStudyInfo object){
		String json = packgeJson(object,"edit");
		String outputStr = "sign=liuhe&oper=edit&data="+json;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getDataUrl(),"POST",outputStr);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	return true;
        }else{
        	return false;
        }
	}
	
	private String packgeJson(FootStudyInfo object,String oper){
		String json = "{";
		json = json+"\"oper\":"+"\""+oper+"\",";
        //添加基本信息
		json = json+"\"mac_id\":"+"\""+object.getMac_id()+"\",";
		json = json+"\"open_id\":"+"\""+object.getOpen_id()+"\",";
		json = json+"\"card_id\":"+"\""+object.getCard_id()+"\",";
		if(oper.equals("add")){
	        json = json+"\"from_id\":"+"\""+object.getFrom_id()+"\",";
	        json = json+"\"from_app\":"+"\""+object.getFrom_app()+"\",";
	        json = json+"\"ticket\":"+"\""+object.getTicket()+"\",";
	        json = json+"\"scene\":"+"\""+object.getScene()+"\",";
	        json = json+"\"date_host\":"+"\""+object.getDate_host_str()+"\",";
	        json = json+"\"name\":"+"\""+object.getName()+"\",";
	        json = json+"\"sex\":"+"\""+object.getSex()+"\",";
	        json = json+"\"birth\":"+"\""+object.getBirth_str()+"\",";
	        json = json+"\"age\":"+"\""+object.getAge()+"\",";//json = json+"\"age\":"+object.getAge()+","; 
	        json = json+"\"district\":"+"\""+object.getDistrict()+"\",";
	        json = json+"\"province\":"+"\""+object.getProvince()+"\",";
	        json = json+"\"city\":"+"\""+object.getCity()+"\",";
	        json = json+"\"county\":"+"\""+object.getCounty()+"\",";
	        json = json+"\"country\":"+"\""+object.getCountry()+"\",";
	        json = json+"\"nation\":"+"\""+object.getNation()+"\",";
	        json = json+"\"height\":"+"\""+object.getHeight_float()+"\",";//json = json+"\"height\":"+object.getHeight_float()+",";
	        json = json+"\"weight\":"+"\""+object.getWeight_float()+"\","; //json = json+"\"weight\":"+object.getWeight_float()+",";
	        json = json+"\"date_yunfu\":"+"\""+object.getDate_yunfu_str()+"\",";
	        json = json+"\"hospital_no\":"+"\""+object.getHospital_no()+"\",";
	        json = json+"\"hospital_name\":"+"\""+object.getHospital_name()+"\",";
	        json = json+"\"clinic_dept\":"+"\""+object.getClinic_dept()+"\",";
	        json = json+"\"doctor_name\":"+"\""+object.getDoctor_name()+"\",";
	        json = json+"\"clinic_type\":"+"\""+object.getClinic_type()+"\",";
	        json = json+"\"user_id\":"+"\""+object.getUser_id()+"\",";//json = json+"\"user_id\":"+object.getUser_id()+",";
	        json = json+"\"period\":"+"\""+object.getPeriod()+"\",";//json = json+"\"period\":"+object.getPeriod()+",";   
		}
        //添加参数信息42
        json = json+"\"left_length\":"+object.getLeft_length_float()+",";
        json = json+"\"left_width\":"+object.getLeft_width_float()+",";
        json = json+"\"right_length\":"+object.getRight_length_float()+",";
        json = json+"\"right_width\":"+object.getRight_width_float()+",";
        //System.out.println(json);
        json = json+"\"left_length_725\":"+object.getLeft_length_725_float()+",";
        json = json+"\"left_length_635\":"+object.getLeft_length_635_float()+",";
        json = json+"\"left_length_68\":"+object.getLeft_length_68_float()+",";
        json = json+"\"left_length_41\":"+object.getLeft_length_41_float()+",";
        json = json+"\"left_width_725\":"+object.getLeft_width_725_float()+",";
        json = json+"\"left_width_635\":"+object.getLeft_width_635_float()+",";
        json = json+"\"left_width_68\":"+object.getLeft_width_68_float()+",";
        json = json+"\"left_width_41\":"+object.getLeft_width_41_float()+",";
        json = json+"\"left_width_41full\":"+object.getLeft_width_41full_float()+",";
        json = json+"\"right_length_725\":"+object.getRight_length_725_float()+",";
        json = json+"\"right_length_635\":"+object.getRight_length_635_float()+",";
        json = json+"\"right_length_68\":"+object.getRight_length_68_float()+",";
        json = json+"\"right_length_41\":"+object.getRight_length_41_float()+",";
        json = json+"\"right_width_725\":"+object.getRight_width_725_float()+",";
        json = json+"\"right_width_635\":"+object.getRight_width_635_float()+",";
        json = json+"\"right_width_68\":"+object.getRight_width_68_float()+",";
        json = json+"\"right_width_41\":"+object.getRight_width_41_float()+",";
        json = json+"\"right_width_41full\":"+object.getRight_width_41full_float()+",";
        json = json+"\"left_length_90\":"+object.getLeft_length_90_float()+",";
        json = json+"\"left_length_825\":"+object.getLeft_length_825_float()+",";
        json = json+"\"left_length_78\":"+object.getLeft_length_78_float()+",";
        json = json+"\"left_length_18\":"+object.getLeft_length_18_float()+",";
        json = json+"\"left_width_90\":"+object.getLeft_width_90_float()+",";
        json = json+"\"left_width_78\":"+object.getLeft_width_78_float()+",";
        json = json+"\"left_width_18\":"+object.getLeft_width_18_float()+",";
        json = json+"\"left_width_ratio\":"+object.getLeft_width_ratio_float()+",";
        json = json+"\"left_center_angle\":"+object.getLeft_center_angle_float()+",";
        json = json+"\"left_incline_angle\":"+object.getLeft_incline_angle_float()+",";
        json = json+"\"right_length_90\":"+object.getRight_length_90_float()+",";
        json = json+"\"right_length_825\":"+object.getRight_length_825_float()+",";
        json = json+"\"right_length_78\":"+object.getRight_length_78_float()+",";
        json = json+"\"right_length_18\":"+object.getRight_length_18_float()+",";
        json = json+"\"right_width_90\":"+object.getRight_width_90_float()+",";
        json = json+"\"right_width_78\":"+object.getRight_width_78_float()+",";
        json = json+"\"right_width_18\":"+object.getRight_width_18_float()+",";
        json = json+"\"right_width_ratio\":"+object.getRight_width_ratio_float()+",";
        json = json+"\"right_center_angle\":"+object.getRight_center_angle_float()+",";
        json = json+"\"right_incline_angle\":"+object.getRight_incline_angle_float()+",";
        //添加专家意见表10
        json = json+"\"left_foot_size\":"+"\""+object.getLeft_foot_size()+"\",";
        json = json+"\"left_foot_width\":"+"\""+object.getLeft_foot_width()+"\",";
        json = json+"\"left_foot_status\":"+"\""+object.getLeft_foot_status()+"\",";
        json = json+"\"right_foot_size\":"+"\""+object.getRight_foot_size()+"\",";
        json = json+"\"right_foot_width\":"+"\""+object.getRight_foot_width()+"\",";
        json = json+"\"right_foot_status\":"+"\""+object.getRight_foot_status()+"\",";
        json = json+"\"left_foot_width2\":"+"\""+object.getLeft_foot_width2()+"\",";
        json = json+"\"left_int_status\":"+"\""+object.getLeft_int_status()+"\",";
        json = json+"\"right_foot_width2\":"+"\""+object.getRight_foot_width2()+"\",";
        json = json+"\"right_int_status\":"+"\""+object.getRight_int_status()+"\",";
        //添加图片信息表4
        if(oper.equals("add")){
        	json = json+"\"left_url\":"+"\""+object.getLeft_url()+"\",";
            json = json+"\"right_url\":"+"\""+object.getRight_url()+"\",";
            json = json+"\"left_dpi\":"+object.getLeft_dpi()+",";
            json = json+"\"right_dpi\":"+object.getRight_dpi()+",";
//            json = json+"\"left_urla\":"+"\""+object.getLeft_urla()+"\",";
//            json = json+"\"right_urla\":"+"\""+object.getRight_urla()+"\",";
        }
        //添加坐标参数表78
        json = json+"\"lcircle_01_x\":"+object.getLcircle_01_x()+",";
        json = json+"\"lcircle_01_y\":"+object.getLcircle_01_y()+",";
        json = json+"\"lcircle_02_x\":"+object.getLcircle_02_x()+",";
        json = json+"\"lcircle_02_y\":"+object.getLcircle_02_y()+",";
        json = json+"\"lcircle_03_x\":"+object.getLcircle_03_x()+",";
        json = json+"\"lcircle_03_y\":"+object.getLcircle_03_y()+",";
        json = json+"\"rcircle_01_x\":"+object.getRcircle_01_x()+",";
        json = json+"\"rcircle_01_y\":"+object.getRcircle_01_y()+",";
        json = json+"\"rcircle_02_x\":"+object.getRcircle_02_x()+",";
        json = json+"\"rcircle_02_y\":"+object.getRcircle_02_y()+",";
        json = json+"\"rcircle_03_x\":"+object.getRcircle_03_x()+",";
        json = json+"\"rcircle_03_y\":"+object.getRcircle_03_y()+",";
        json = json+"\"lcircle_725_x\":"+object.getLcircle_725_x()+",";            
        json = json+"\"lcircle_725_y\":"+object.getLcircle_725_y()+",";
        json = json+"\"lcircle_635_x\":"+object.getLcircle_635_x()+",";
        json = json+"\"lcircle_635_y\":"+object.getLcircle_635_y()+",";
        json = json+"\"lcircle_4101_x\":"+object.getLcircle_4101_x()+",";
        json = json+"\"lcircle_4101_y\":"+object.getLcircle_4101_y()+",";
        json = json+"\"lcircle_4102_x\":"+object.getLcircle_4102_x()+",";
        json = json+"\"lcircle_4102_y\":"+object.getLcircle_4102_y()+",";
        json = json+"\"lbreak_01_x\":"+object.getLbreak_01_x()+",";
        json = json+"\"lbreak_01_y\":"+object.getLbreak_01_y()+",";
        json = json+"\"lbreak_02_x\":"+object.getLbreak_02_x()+",";
        json = json+"\"lbreak_02_y\":"+object.getLbreak_02_y()+",";
        json = json+"\"rcircle_725_x\":"+object.getRcircle_725_x()+",";
        json = json+"\"rcircle_725_y\":"+object.getRcircle_725_y()+",";
        json = json+"\"rcircle_635_x\":"+object.getRcircle_635_x()+",";
        json = json+"\"rcircle_635_y\":"+object.getRcircle_635_y()+",";
        json = json+"\"rcircle_4101_x\":"+object.getRcircle_4101_x()+",";
        json = json+"\"rcircle_4101_y\":"+object.getRcircle_4101_y()+",";
        json = json+"\"rcircle_4102_x\":"+object.getRcircle_4102_x()+",";
        json = json+"\"rcircle_4102_y\":"+object.getRcircle_4102_y()+",";
        json = json+"\"rbreak_01_x\":"+object.getRbreak_01_x()+",";
        json = json+"\"rbreak_01_y\":"+object.getRbreak_01_y()+",";
        json = json+"\"rbreak_02_x\":"+object.getRbreak_02_x()+",";
        json = json+"\"rbreak_02_y\":"+object.getRbreak_02_y()+",";
        if(oper.equals("add")){
        	json = json+"\"lfoot_top\":"+object.getLfoot_top()+",";
            json = json+"\"lfoot_bottom\":"+object.getLfoot_bottom()+",";
            json = json+"\"lfoot_right\":"+object.getLfoot_right()+",";
            json = json+"\"lfoot_left\":"+object.getLfoot_left()+",";
            json = json+"\"lscale\":"+object.getLscale()+",";
            json = json+"\"rfoot_top\":"+object.getRfoot_top()+",";
            json = json+"\"rfoot_bottom\":"+object.getRfoot_bottom()+",";
            json = json+"\"rfoot_right\":"+object.getRfoot_right()+",";
            json = json+"\"rfoot_left\":"+object.getRfoot_left()+",";
            json = json+"\"rscale\":"+object.getRscale()+",";
        	json = json+"\"lcircle_18_x\":"+object.getLcircle_18_x()+",";
            json = json+"\"lcircle_18_y\":"+object.getLcircle_18_y()+",";        
            json = json+"\"lcircle_90_x\":"+object.getLcircle_90_x()+",";
            json = json+"\"lcircle_90_y\":"+object.getLcircle_90_y()+",";
            json = json+"\"lcircle_825_x\":"+object.getLcircle_825_x()+",";
            json = json+"\"lcircle_825_y\":"+object.getLcircle_825_y()+",";
            json = json+"\"lcircle_78_x\":"+object.getLcircle_78_x()+",";
            json = json+"\"lcircle_78_y\":"+object.getLcircle_78_y()+",";
            json = json+"\"rcircle_18_x\":"+object.getRcircle_18_x()+",";
            json = json+"\"rcircle_18_y\":"+object.getRcircle_18_y()+",";
            json = json+"\"rcircle_90_x\":"+object.getRcircle_90_x()+",";
            json = json+"\"rcircle_90_y\":"+object.getRcircle_90_y()+",";
            json = json+"\"rcircle_825_x\":"+object.getRcircle_825_x()+",";
            json = json+"\"rcircle_825_y\":"+object.getRcircle_825_y()+",";
            json = json+"\"rcircle_78_x\":"+object.getRcircle_78_x()+",";
            json = json+"\"rcircle_78_y\":"+object.getRcircle_78_y()+",";
        }
        json = json+"\"lcircle_1801_x\":"+object.getLcircle_1801_x()+",";
        json = json+"\"lcircle_1801_y\":"+object.getLcircle_1801_y()+",";
        json = json+"\"lcircle_1802_x\":"+object.getLcircle_1802_x()+",";
        json = json+"\"lcircle_1802_y\":"+object.getLcircle_1802_y()+",";
        json = json+"\"rcircle_1801_x\":"+object.getRcircle_1801_x()+",";
        json = json+"\"rcircle_1801_y\":"+object.getRcircle_1801_y()+",";
        json = json+"\"rcircle_1802_x\":"+object.getRcircle_1802_x()+",";
        json = json+"\"rcircle_1802_y\":"+object.getRcircle_1802_y()+",";
        json = json+"\"lcircle_80_x\":"+object.getLcircle_80_x()+",";
        json = json+"\"lcircle_80_y\":"+object.getLcircle_80_y()+",";
        json = json+"\"lcircle_65_x\":"+object.getLcircle_65_x()+",";
        json = json+"\"lcircle_65_y\":"+object.getLcircle_65_y()+",";
        json = json+"\"rcircle_80_x\":"+object.getRcircle_80_x()+",";
        json = json+"\"rcircle_80_y\":"+object.getRcircle_80_y()+",";
        json = json+"\"rcircle_65_x\":"+object.getRcircle_65_x()+",";
        json = json+"\"rcircle_65_y\":"+object.getRcircle_65_y();
        // modify by kael
        if(oper.equals("edit")){
        	json = json+"}";
        }
        // modify by kael over
//        json = json+"}";
		return json;
	}
	
	public boolean deleteStudy(int primary_id){
		String outputStr = "sign=liuhe&oper=delete&record_id="+primary_id;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getDataUrl(),"POST",outputStr);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	return true;
        }else{
        	return false;
        }
	}
	
	/**
	 * 实现取号
	 * */	
	public ClinicInfo getClinicInfo(String open_id,String card_id){
		String outputStr = "sign=liuhe&hospital="+serverConfig.getHospital_no()+"&worktime="+serverConfig.getWorktime();
		outputStr = outputStr + (open_id!=null?"&open_id="+open_id:"");
		outputStr = outputStr + (card_id!=null?"&card_id="+card_id:"");
		System.out.println("取号POST的数据是："+outputStr);
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getClinicUrl(),"POST",outputStr);
		if(jsonObject == null){
			return null;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode == 0){
        	ClinicInfo clinicInfo = new ClinicInfo();
        	clinicInfo.setClinic(JSONArray.toList(jsonObject.getJSONArray("clinic"), ClinicInfo_item.class));
        	return clinicInfo;
        }else{
        	return null;
        }
	}
	
	/**
	 * 实现挂号
	 * 返回："初诊/复诊;number;wait"
	 * */
	public Map<String, String> registration(String clinic,String doctor,String type,String open_id,String card_id,String pat_name){
		String outputStr = "sign=liuhe&hospital="+serverConfig.getHospital_no()+"&clinic="+clinic+"&doctor="+doctor+"&type="+type;
		outputStr = outputStr + (open_id!=null?"&open_id="+open_id:"");
		outputStr = outputStr + (card_id!=null?"&card_id="+card_id:"");
		outputStr = outputStr + (pat_name!=null?"&pat_name="+pat_name:"");
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getClinicUrl(),"POST",outputStr);
		if(jsonObject == null){
			return null;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("errmsg", jsonObject.getString("errmsg"));
        	if(map.get("errmsg").equals("队列中")){
        		map.put("doctor", jsonObject.getString("doctor"));
        	}else{
        		map.put("status", jsonObject.getString("status"));
            	map.put("queue", jsonObject.getString("queue"));
            	map.put("wait", jsonObject.getString("wait"));
        	}
        	return map;
        }else{
        	return null;
        }
	}
	
	//新增二维码ticket和scene参数记录，便于验证扫描
	public boolean addVerifyInfo(String mac_id,String scene,String ticket){
		String outputStr = "sign=liuhe&mac_id="+mac_id+"&scene="+scene+"&ticket="+ticket;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getVerifyUrl(),"POST",outputStr);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode == 0){
        	return true;
        }else{
        	return false;
        }
	}
	
	//验证是否扫描带参二维码
	public String isVerifyInfo(String mac_id){
		String outputStr = "sign=liuhe&mac_id="+mac_id;
		System.out.println("isVerifyInfo:"+outputStr);
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getVerifyUrl(),"POST",outputStr);
		if(jsonObject == null){
			return null;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	return jsonObject.getString("open_id");
        }else{
        	return null;
        }
	}
		
	//查询yxd_userinfo信息
	public Map<String, String> getUserInfo(String idType,String idValue){
		String outputStr = "sign=liuhe&idType="+idType+"&idValue="+idValue;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getUserinfoUrl(),"POST",outputStr);
		if(jsonObject == null){
			return null;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	Map<String, String> map = new HashMap<String, String>();
        	if(!jsonObject.getString("id").equals("")){
        		map.put("id", jsonObject.getString("id"));
        	}
        	if(!jsonObject.getString("open_id").equals("")){
        		map.put("open_id", jsonObject.getString("open_id"));
        	}
        	if(!jsonObject.getString("card_id").equals("")){
        		map.put("card_id", jsonObject.getString("card_id"));
        	}
        	if(!jsonObject.getString("name").equals("")){
        		map.put("name", jsonObject.getString("name"));
        	}
        	if(!jsonObject.getString("sex").equals("")){
        		map.put("sex", jsonObject.getString("sex"));
        	}
        	if(!jsonObject.getString("nation").equals("")){
        		map.put("nation", jsonObject.getString("nation"));
        	}
        	if(!jsonObject.getString("date_birth").equals("")){
        		map.put("date_birth", jsonObject.getString("date_birth"));
        	}
        	if(!jsonObject.getString("district").equals("")){
        		map.put("district", jsonObject.getString("district"));
        	}
        	if(!jsonObject.getString("province").equals("")){
        		map.put("province", jsonObject.getString("province"));
        	}
        	if(!jsonObject.getString("city").equals("")){
        		map.put("city", jsonObject.getString("city"));
        	}
        	if(!jsonObject.getString("county").equals("")){
        		map.put("county", jsonObject.getString("county"));
        	}
        	if(!jsonObject.getString("date_yunfu").equals("")){
        		map.put("date_yunfu", jsonObject.getString("date_yunfu"));
        	}
        	if(!jsonObject.getString("height").equals("")){
        		map.put("height", jsonObject.getString("height"));
        	}
        	if(!jsonObject.getString("left_length").equals("")){
        		map.put("left_length", jsonObject.getString("left_length"));
        	}
        	if(!jsonObject.getString("right_length").equals("")){
        		map.put("right_length", jsonObject.getString("right_length"));
        	}
        	if(!jsonObject.getString("left_width").equals("")){
        		map.put("left_width", jsonObject.getString("left_width"));
        	}
        	if(!jsonObject.getString("right_width").equals("")){
        		map.put("right_width", jsonObject.getString("right_width"));
        	}
        	return map;
        }else{
        	return null;
        }
	}
	
	//添加身份证信息到userinfo
	public boolean addCardInfo(String open_id,String card_id,String name,String sex,String born,String nation,String address){
		String outputStr = "sign=liuhe&open_id="+open_id+"&card_id="+card_id+"&name="+name
			+"&sex="+sex+"&date_birth="+born+"&nation="+nation+"&address="+address;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getUserinfoUrl(),"POST",outputStr);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
		if(errcode==0){
			return true;
		}else{
			return false;
		}
	}
	
	//添加末次月经时间到userinfo
	public boolean addPeriodDate(String open_id, String period_str){
		String outputStr = "sign=liuhe&open_id="+open_id+"&period="+period_str;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getUserinfoUrl(),"POST",outputStr);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	return true;
        }else{
        	return false;
        }
	}
	
	//添加身高信息到userinfo
	public boolean addHeight(String open_id, String height){
		String outputStr = "sign=liuhe&open_id="+open_id+"&height="+height;
		JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getUserinfoUrl(),"POST",outputStr);
		if(jsonObject == null){
			return false;
		}
		int errcode = jsonObject.getInt("errcode");
        if(errcode==0){
        	return true;
        }else{
        	return false;
        }
	}
	
	//判断是否需要扫描脚型
	public boolean ifNeedScan(String open_id,String card_id){
		try {
			String outputStr = null;
			if(open_id!=null&&card_id!=null){
				outputStr = "sign=liuhe&open_id="+open_id+"&card_id="+card_id+"&space_day="+serverConfig.getInterval();
			}else{
				outputStr = "sign=liuhe&" + (open_id!=null?"open_id="+open_id:"card_id="+card_id)+"&space_day="+serverConfig.getInterval();
			}
			JSONObject jsonObject = HttpUtil.httpRequest(serverConfig.getDataUrl(),"POST",outputStr);
			if(jsonObject == null){
				return false;
			}
			int errcode = jsonObject.getInt("errcode");
            if(errcode==0){
            	String errmsg = jsonObject.getString("errmsg");
            	return Boolean.parseBoolean(errmsg);
            }else{
            	return false;
            }
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
}