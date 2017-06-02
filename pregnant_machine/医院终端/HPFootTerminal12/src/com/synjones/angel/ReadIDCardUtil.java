package com.synjones.angel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public class ReadIDCardUtil {
	
	//�˽ӿ�һ��Ҫ�̳�StdCallLibrary �����������!
	private interface SynIDCardAPI extends StdCallLibrary  {
		String path = ReadIDCardUtil.class.getResource("/").getPath().substring(1).replace("/", "\\") + "SynIDCardAPI";
		SynIDCardAPI INSTANCE = (SynIDCardAPI)Native.loadLibrary(path, SynIDCardAPI.class);
		//�򿪶˿ںź���
		public int Syn_SetMaxRFByte(int iPort, char ucByte, int bIfOpen);
		public int Syn_GetCOMBaudEx(int iPort);
		public int Syn_SetCOMBaud(int iPort, int uiCurrBaud, int uiSetBaud);
		public int Syn_OpenPort(int iPort);
		public int Syn_ClosePort (int iPort);
		//���֤���ຯ��
		public int Syn_StartFindIDCard(int iPort, char[] pucIIN, int iIfOpen);
		public int Syn_SelectIDCard(int iPort, char[] pucSN, int iIfOpen);
		public int Syn_ReadMsg(int iPort, int iIfOpen, IDCardData pINCardData);
		public int Syn_FindReader();
		//���ø��ӹ��ܺ���
		public int Syn_SetPhotoPath(int iOption, String cPhotopath);
		public int Syn_SetPhotoType(int iType);
		public int Syn_SetPhotoName(int iType);
		public int Syn_SetSexType( int iType );
		public int Syn_SetNationType(int iType);
		public int Syn_SetBornType(int iType);
		public int Syn_SetUserLifeBType(int iType);
		public int Syn_SetUserLifeEType(int iTyp, int iOption);	
	}
	
	public Map<String, String> readCardInfo() throws Exception{
		/**
         * ���ô���Ƭ��·��
         * 0:�����C�̸�Ŀ¼��
         * 1�������ڵ�ǰ·����
         * 2������ָ��·��
         */
		SynIDCardAPI.INSTANCE.Syn_SetPhotoPath(2, System.getProperty("user.dir")+"\\scan\\");
		 /**
         * ������Ƭ��ʽ
         * 0��BMP
         * 1��Jpeg
         * 2����Ƭ�����ʽ����Ϊ Base64
         */
		SynIDCardAPI.INSTANCE.Syn_SetPhotoType(0);
        /**
         * ���ô����֤ͷ����ļ���
         * 0:���tmp��ʽ
         * 1����Ƭ�����ļ�����ʽ����Ϊ ����
         * 2����Ƭ�����ļ�����ʽ����Ϊ ���֤��
         * 3����Ƭ�����ļ�����ʽ����Ϊ ����_���֤��
         */
		SynIDCardAPI.INSTANCE.Syn_SetPhotoName(2);
		SynIDCardAPI.INSTANCE.Syn_SetSexType(1);
		SynIDCardAPI.INSTANCE.Syn_SetNationType(1);
		SynIDCardAPI.INSTANCE.Syn_SetBornType(2);
		SynIDCardAPI.INSTANCE.Syn_SetUserLifeBType(3);
		SynIDCardAPI.INSTANCE.Syn_SetUserLifeEType(4,1);
		// ��ȡ�˿ں�
		String sMsg = "";
		Map<String, String> map = new HashMap<String, String>();
		System.out.println("card util 0000000000");
		int iPort = SynIDCardAPI.INSTANCE.Syn_FindReader();
		if (iPort == 0){
    		sMsg = "û���ҵ���������";
    		map.put("errcode", "1");
    		map.put("errmsg", "û���ҵ�������");
    	}else{
    		if (iPort >1000){
    			sMsg = String.format("������������USB�˿� %d", iPort); 
    		}else{
    			try{
    				Thread.sleep(200);
    			}catch (InterruptedException exc) {
    				System.out.println("error");
    			}
    			int uiCurrBaud = SynIDCardAPI.INSTANCE.Syn_GetCOMBaudEx(iPort);
    			sMsg = String.format("�����������ڴ��� %d,��ǰSAM������Ϊ %d", iPort, uiCurrBaud); 
    		}
    		// �򿪶˿ں�
    		System.out.println("card util 111111111");
    		int nRet = SynIDCardAPI.INSTANCE.Syn_OpenPort(iPort);
        	if (nRet == 0){
        		System.out.println("card util 2222222222");
        		if (SynIDCardAPI.INSTANCE.Syn_SetMaxRFByte(iPort, (char)80, 0) == 0){
        			System.out.println("card util 3333333333");
        			char[] pucIIN = new char[8];
        			char[] pucSN = new char[8];
                	IDCardData idcardData = new IDCardData();
        			nRet = SynIDCardAPI.INSTANCE.Syn_StartFindIDCard(iPort, pucIIN, 0);
        			nRet = SynIDCardAPI.INSTANCE.Syn_SelectIDCard(iPort, pucSN, 0);
        			if (SynIDCardAPI.INSTANCE.Syn_ReadMsg(iPort, 0, idcardData) == 0){
        				try{
        					System.out.println("�ɹ���ȡ���֤��Ϣ��");
        					map.put("Name", new String(idcardData.Name, "GBK").trim());
        					map.put("Sex", new String(idcardData.Sex, "GBK").trim());
        					map.put("Nation", new String(idcardData.Nation, "GBK").trim()+"��");
        					map.put("Born", new String(idcardData.Born, "GBK").trim().replace('.', '-'));
        					map.put("Address", new String(idcardData.Address, "GBK").trim());
        					map.put("IDCardNo", new String(idcardData.IDCardNo, "GBK").trim());
        					map.put("errcode", "0");
                    		map.put("errmsg", "��ȷ��ȡ���֤��Ϣ");
        				}catch (UnsupportedEncodingException une) {
        					une.printStackTrace();
        				}
        			}else{
        				sMsg = "��ȡ���֤��Ϣ����";
        				map.put("errcode", "0");
                		map.put("errmsg", "��ȡ���֤��Ϣ����");
        			}
        		}else{
        			sMsg = "����ʹ�ö�������";
        			map.put("errcode", "3");
        			map.put("errmsg", "����ʹ�ö�����");
        		}
        	}else{
        		sMsg = "�򿪶˿ڴ���";
        		map.put("errcode", "2");
        		map.put("errmsg", "�򿪶˿ڴ���");
        	}
        	SynIDCardAPI.INSTANCE.Syn_ClosePort(iPort);
    	}
		return map;
	}
}