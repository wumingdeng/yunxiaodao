package org.liuhe.register;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class RegeditService {
	/**static SimpleDateFormat shortDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	将信息存储到注册表下的某个节点的某个变量中，有则修改，无则创建
	创建在：HKEY_LOCAL_MACHINE下的SOFTWARE下的FootKid节点之下*/
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
	//删除注册表中某个节点下的某个变量
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
	//删除注册表中某个节点下的某个节点
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
	//打开注册表项并读出相应的变量名的值
	public static String getValue(String folder,String subKeyNode,String subKeyName){
		String value = null;
		try{
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey(folder);
			RegistryKey subKey = software.openSubKey(subKeyNode);
			value = subKey.getStringValue(subKeyName);
		}catch(NoSuchKeyException e){
			//value="NoSuchKey";		//当不存在subKeyNode时，返回
			e.printStackTrace();
		}catch(NoSuchValueException e){
			//value="NoSuchValue";		//当不存在subKeyName时，返回
			e.printStackTrace();
		}catch(RegistryException e){
			//value="default";
			e.printStackTrace();
		}
		return value;
	}
	//System.out.println("添加变量是否成功："+setValue("SOFTWARE","Microsoft//Windows//CurrentVersion//Run","two","C//2.exe-EO21"));//修改或增加
	//System.out.println("得到的test注册值是："+getValue("SOFTWARE","Microsoft//Windows//CurrentVersion//Run","test"));//查看节点中的某个变量值
	//System.out.println("删除变量值是否成功："+deleteValue("SOFTWARE","Microsoft//Windows//CurrentVersion//Run","test"));//删除节点中的变量
	//System.out.println("删除节点是否成功："+deleteSubKey("SOFTWARE","Microsoft//Windows//CurrentVersion//Run"));//删除某个节点	
}