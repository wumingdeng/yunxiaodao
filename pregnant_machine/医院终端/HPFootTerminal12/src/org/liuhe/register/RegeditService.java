package org.liuhe.register;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class RegeditService {
	/**static SimpleDateFormat shortDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	����Ϣ�洢��ע����µ�ĳ���ڵ��ĳ�������У������޸ģ����򴴽�
	�����ڣ�HKEY_LOCAL_MACHINE�µ�SOFTWARE�µ�FootKid�ڵ�֮��*/
	public static boolean setValue(String folder,String subKeyNode,String subKeyName,String subKeyValue){
		try{
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
			RegistryKey subKey = software.createSubKey(subKeyNode, "");
			subKey.setValue(new RegStringValue(subKey,subKeyName,subKeyValue));
			subKey.closeKey();
			return true;
		}catch(NoSuchKeyException e){
			e.printStackTrace();
		}catch(NoSuchValueException e){
			e.printStackTrace();
		}catch(RegistryException e ){
			e.printStackTrace();
		}
		return false;
	}
	//ɾ��ע�����ĳ���ڵ��µ�ĳ������
	public static boolean deleteValue(String folder,String subKeyNode,String subKeyName){
		try{
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
			RegistryKey subKey = software.createSubKey(subKeyNode, "");
			subKey.deleteValue(subKeyName);
			subKey.closeKey();
			return true;
		}catch(NoSuchKeyException e){
			e.printStackTrace();
		}catch(NoSuchValueException e){
			e.printStackTrace();
		}catch(RegistryException e ){
			e.printStackTrace();
		}
		return false;
	}
	//ɾ��ע�����ĳ���ڵ��µ�ĳ���ڵ�
	public static boolean deleteSubKey(String folder,String subKeyNode){
		try{
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
			software.deleteSubKey(subKeyNode);
			return true;
		}catch(NoSuchKeyException e){
			e.printStackTrace();
		}catch(NoSuchValueException e){
			e.printStackTrace();
		}catch(RegistryException e ){
			e.printStackTrace();
		}
		return false;
	}
	//��ע����������Ӧ�ı�������ֵ
	public static String getValue(String folder,String subKeyNode,String subKeyName){
		String value = null;
		try{
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
			RegistryKey subKey = software.openSubKey(subKeyNode);
			value = subKey.getStringValue(subKeyName);
		}catch(NoSuchKeyException e){
			//value="NoSuchKey";		//��������subKeyNodeʱ������
			e.printStackTrace();
		}catch(NoSuchValueException e){
			//value="NoSuchValue";		//��������subKeyNameʱ������
			e.printStackTrace();
		}catch(RegistryException e){
			//value="default";
			e.printStackTrace();
		}
		return value;
	}
	//System.out.println("��ӱ����Ƿ�ɹ���"+setValue("SOFTWARE","Microsoft//Windows//CurrentVersion//Run","two","C//2.exe-EO21"));//�޸Ļ�����
	//System.out.println("�õ���testע��ֵ�ǣ�"+getValue("SOFTWARE","Microsoft//Windows//CurrentVersion//Run","test"));//�鿴�ڵ��е�ĳ������ֵ
	//System.out.println("ɾ������ֵ�Ƿ�ɹ���"+deleteValue("SOFTWARE","Microsoft//Windows//CurrentVersion//Run","test"));//ɾ���ڵ��еı���
	//System.out.println("ɾ���ڵ��Ƿ�ɹ���"+deleteSubKey("SOFTWARE","Microsoft//Windows//CurrentVersion//Run"));//ɾ��ĳ���ڵ�	
}