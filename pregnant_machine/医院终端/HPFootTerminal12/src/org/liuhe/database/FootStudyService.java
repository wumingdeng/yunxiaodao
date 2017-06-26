package org.liuhe.database;

import java.util.List;
import java.util.Map;

import org.liuhe.algorithm.config.ServerConfig;

public class FootStudyService {
	
	private FootStudyDAO dao;
	
	public FootStudyService(ServerConfig serverConfig){
		dao = new FootStudyDAO(serverConfig);
	}
	
	//��ӵ�����¼
	public synchronized boolean addStudyInfoToTxt(FootStudyInfo info){
		return dao.addStudyToTxt(info);
	}
	public synchronized boolean addStudyInfoToSQL(String content){
		return dao.addStudyToSQL(content);
	}
	//�޸ĵ�����¼
	public boolean editStudyInfo(FootStudyInfo info){
		return dao.editStudy(info);
	}
//	//ɾ��������¼
//	public boolean deleteStudyInfo(String mac_id){
//		return dao.deleteStudy(mac_id);
//	}
	
	//ʵ��ȡ��
	public ClinicInfo getClinicInfo(String open_id,String card_id){
		return dao.getClinicInfo(open_id, card_id);
	}
	//ʵ�ֹҺ�
	public Map<String, String> registration(String clinic,String doctor,String type,String open_id,String card_id,String pat_name,int doctorid){
		return dao.registration(clinic, doctor, type, open_id, card_id, pat_name,doctorid);
	}
	
	//������ά��ticket��scene������¼��������֤ɨ��
	public boolean addVerifyInfo(String mac_id,String scene,String ticket){
		return dao.addVerifyInfo(mac_id, scene, ticket);
	}
	//��֤�Ƿ�ɨ����ζ�ά��
	public String isVerifyInfo(String mac_id){
		return dao.isVerifyInfo(mac_id);
	}
	//���ݻ�õ�openid��ѯyxd_userinfo��Ϣ�������ڸ�openid��������
	public Map<String, String> getUserInfo(String idType,String idValue){
		return dao.getUserInfo(idType, idValue);
	}
	//�ж��Ƿ���Ҫɨ�����
	public boolean ifNeedScan(String open_id,String card_id){
		return dao.ifNeedScan(open_id, card_id);
	}
	//���ĩ���¾�ʱ��
	public boolean addPeriodDate(String open_id, String period_str){
		return dao.addPeriodDate(open_id, period_str);
	}
	//��������Ϣ��userinfo
	public boolean addHeight(String open_id, String height){
		return dao.addHeight(open_id, height);
	}
	//�����ǰ�w����Ϣ��userinfo
	public boolean addWeight(String open_id, String weight,boolean isSingle){
		return dao.addWeight(open_id, weight,isSingle);
	}
	//������֤��Ϣ��userinfo
	public boolean addCardInfo(String open_id,String card_id,String name,String sex,String born,String nation,String address){
		return dao.addCardInfo(open_id, card_id, name, sex, born, nation, address);
	}
	public synchronized boolean sendWeightBaseinfo(FootStudyInfo info){
		return dao.sendWeightBaseinfo(info);
	}
//	//��ȡ΢���п�accessToken
//	public String getAppAccessToken(String appname){
//		return dao.getAppAccessToken(appname);
//	}
	
}