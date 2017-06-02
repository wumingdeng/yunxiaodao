package com.synjones.angel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public class ReadIDCardUtil {
	
	//此接口一定要继承StdCallLibrary 否则读卡错误!
	private interface SynIDCardAPI extends StdCallLibrary  {
		String path = ReadIDCardUtil.class.getResource("/").getPath().substring(1).replace("/", "\\") + "SynIDCardAPI";
		SynIDCardAPI INSTANCE = (SynIDCardAPI)Native.loadLibrary(path, SynIDCardAPI.class);
		//打开端口号函数
		public int Syn_SetMaxRFByte(int iPort, char ucByte, int bIfOpen);
		public int Syn_GetCOMBaudEx(int iPort);
		public int Syn_SetCOMBaud(int iPort, int uiCurrBaud, int uiSetBaud);
		public int Syn_OpenPort(int iPort);
		public int Syn_ClosePort (int iPort);
		//身份证卡类函数
		public int Syn_StartFindIDCard(int iPort, char[] pucIIN, int iIfOpen);
		public int Syn_SelectIDCard(int iPort, char[] pucSN, int iIfOpen);
		public int Syn_ReadMsg(int iPort, int iIfOpen, IDCardData pINCardData);
		public int Syn_FindReader();
		//设置附加功能函数
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
         * 设置存照片的路径
         * 0:存放在C盘根目录下
         * 1：存在在当前路径下
         * 2：可以指定路径
         */
		SynIDCardAPI.INSTANCE.Syn_SetPhotoPath(2, System.getProperty("user.dir")+"\\scan\\");
		 /**
         * 设置照片格式
         * 0：BMP
         * 1：Jpeg
         * 2：照片保存格式设置为 Base64
         */
		SynIDCardAPI.INSTANCE.Syn_SetPhotoType(0);
        /**
         * 设置存身份证头像的文件名
         * 0:存成tmp格式
         * 1：照片保存文件名格式设置为 姓名
         * 2：照片保存文件名格式设置为 身份证号
         * 3：照片保存文件名格式设置为 姓名_身份证号
         */
		SynIDCardAPI.INSTANCE.Syn_SetPhotoName(2);
		SynIDCardAPI.INSTANCE.Syn_SetSexType(1);
		SynIDCardAPI.INSTANCE.Syn_SetNationType(1);
		SynIDCardAPI.INSTANCE.Syn_SetBornType(2);
		SynIDCardAPI.INSTANCE.Syn_SetUserLifeBType(3);
		SynIDCardAPI.INSTANCE.Syn_SetUserLifeEType(4,1);
		// 获取端口号
		String sMsg = "";
		Map<String, String> map = new HashMap<String, String>();
		System.out.println("card util 0000000000");
		int iPort = SynIDCardAPI.INSTANCE.Syn_FindReader();
		if (iPort == 0){
    		sMsg = "没有找到读卡器！";
    		map.put("errcode", "1");
    		map.put("errmsg", "没有找到读卡器");
    	}else{
    		if (iPort >1000){
    			sMsg = String.format("读卡器连接在USB端口 %d", iPort); 
    		}else{
    			try{
    				Thread.sleep(200);
    			}catch (InterruptedException exc) {
    				System.out.println("error");
    			}
    			int uiCurrBaud = SynIDCardAPI.INSTANCE.Syn_GetCOMBaudEx(iPort);
    			sMsg = String.format("读卡器连接在串口 %d,当前SAM波特率为 %d", iPort, uiCurrBaud); 
    		}
    		// 打开端口号
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
        					System.out.println("成功获取身份证信息。");
        					map.put("Name", new String(idcardData.Name, "GBK").trim());
        					map.put("Sex", new String(idcardData.Sex, "GBK").trim());
        					map.put("Nation", new String(idcardData.Nation, "GBK").trim()+"族");
        					map.put("Born", new String(idcardData.Born, "GBK").trim().replace('.', '-'));
        					map.put("Address", new String(idcardData.Address, "GBK").trim());
        					map.put("IDCardNo", new String(idcardData.IDCardNo, "GBK").trim());
        					map.put("errcode", "0");
                    		map.put("errmsg", "正确读取身份证信息");
        				}catch (UnsupportedEncodingException une) {
        					une.printStackTrace();
        				}
        			}else{
        				sMsg = "读取身份证信息错误！";
        				map.put("errcode", "0");
                		map.put("errmsg", "读取身份证信息错误");
        			}
        		}else{
        			sMsg = "不能使用读卡器！";
        			map.put("errcode", "3");
        			map.put("errmsg", "不能使用读卡器");
        		}
        	}else{
        		sMsg = "打开端口错误！";
        		map.put("errcode", "2");
        		map.put("errmsg", "打开端口错误");
        	}
        	SynIDCardAPI.INSTANCE.Syn_ClosePort(iPort);
    	}
		return map;
	}
}