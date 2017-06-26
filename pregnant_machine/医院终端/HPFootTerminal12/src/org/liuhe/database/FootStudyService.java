package org.liuhe.database;

import java.util.List;
import java.util.Map;

import org.liuhe.algorithm.config.ServerConfig;

public class FootStudyService {
	
	private FootStudyDAO dao;
	
	public FootStudyService(ServerConfig serverConfig){
		dao = new FootStudyDAO(serverConfig);
	}
	
	//添加档案记录
	public synchronized boolean addStudyInfoToTxt(FootStudyInfo info){
		return dao.addStudyToTxt(info);
	}
	public synchronized boolean addStudyInfoToSQL(String content){
		return dao.addStudyToSQL(content);
	}
	//修改档案记录
	public boolean editStudyInfo(FootStudyInfo info){
		return dao.editStudy(info);
	}
//	//删除档案记录
//	public boolean deleteStudyInfo(String mac_id){
//		return dao.deleteStudy(mac_id);
//	}
	
	//实现取号
	public ClinicInfo getClinicInfo(String open_id,String card_id){
		return dao.getClinicInfo(open_id, card_id);
	}
	//实现挂号
	public Map<String, String> registration(String clinic,String doctor,String type,String open_id,String card_id,String pat_name,int doctorid){
		return dao.registration(clinic, doctor, type, open_id, card_id, pat_name,doctorid);
	}
	
	//新增二维码ticket和scene参数记录，便于验证扫描
	public boolean addVerifyInfo(String mac_id,String scene,String ticket){
		return dao.addVerifyInfo(mac_id, scene, ticket);
	}
	//验证是否扫描带参二维码
	public String isVerifyInfo(String mac_id){
		return dao.isVerifyInfo(mac_id);
	}
	//根据获得的openid查询yxd_userinfo信息（不存在该openid则新增）
	public Map<String, String> getUserInfo(String idType,String idValue){
		return dao.getUserInfo(idType, idValue);
	}
	//判断是否需要扫描脚型
	public boolean ifNeedScan(String open_id,String card_id){
		return dao.ifNeedScan(open_id, card_id);
	}
	//添加末次月经时间
	public boolean addPeriodDate(String open_id, String period_str){
		return dao.addPeriodDate(open_id, period_str);
	}
	//添加身高信息到userinfo
	public boolean addHeight(String open_id, String height){
		return dao.addHeight(open_id, height);
	}
	//添加孕前體重信息到userinfo
	public boolean addWeight(String open_id, String weight,boolean isSingle){
		return dao.addWeight(open_id, weight,isSingle);
	}
	//添加身份证信息到userinfo
	public boolean addCardInfo(String open_id,String card_id,String name,String sex,String born,String nation,String address){
		return dao.addCardInfo(open_id, card_id, name, sex, born, nation, address);
	}
	public synchronized boolean sendWeightBaseinfo(FootStudyInfo info){
		return dao.sendWeightBaseinfo(info);
	}
//	//获取微信中控accessToken
//	public String getAppAccessToken(String appname){
//		return dao.getAppAccessToken(appname);
//	}
	
}