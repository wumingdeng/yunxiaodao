package org.liuhe.database;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.liuhe.algorithm.config.ServerConfig;

import net.sf.json.JSONObject;

public class FootImageService {
	
	private String boundary = "-------------------------7e020233150564";	//��ڷ�  
    private String prefix = "--";											//ǰ׺�ϴ�ʱ��Ҫ�������"--"
    private String end = "\r\n";											//����Ҳ��Ҫע�⣬��htmlЭ���У��� "/r/n"���У������� "/n"
    
    private ServerConfig serverConfig;
	public FootImageService(ServerConfig serverConfig){
		this.serverConfig = serverConfig;
	}
	/**
	 * java ͬ��������ʹ�ã���ֹ���߳�ͬʱִ�з�����
	 * synchronized��������,������һ���߳����е��������ʱ,��Ҫ�����û�������߳����������������
	 * �еĻ�Ҫ������ʹ��synchronized�������߳���������������������д��߳�,û�еĻ�,ֱ�����С�
	 */
	//��ӽ����о�����
	public String uploadFoot(String[] sourceName,String[] destName){
		try {
            URL http = new URL(serverConfig.getUploadUrl());
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();  
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);		//׼���������������  
            conn.setDoOutput(true);		//׼���������д������  
            
            //������������ϴ����ݵ�����ʽ  Ĭ�ϵ��Ǳ���ʽ ;ͨ��Content-TypeЭ����������ϴ�����
            conn.setRequestProperty("Content-Type","multipart/form-data; charset=UTF-8; boundary="+boundary); 
            conn.connect();
            //����һ�����������  
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            
            for(int i=0;i<sourceName.length;i++){
            	//���ǵ�һ�У����س�����
                out.writeBytes(prefix+boundary+end);
                //���ǵڶ��У��ļ����Ͷ�Ӧ��������
                out.writeBytes("Content-Disposition: form-data; name=\"file"+(i+1)+"\"; filename=\""+destName[i]+"\""+end);
                //���ǵ����У���һ��
                out.writeBytes(end);
                //����д��ͼƬ
                FileInputStream fileInputStream = new FileInputStream(new File(sourceName[i]));  
                byte[] b = new byte[1024*4];
                int len;
                //ѭ��������  
                while((len=fileInputStream.read(b))!=-1){
                    out.write(b, 0, len);
                }  
                //д�����ݺ� �س�����
                out.writeBytes(end);
                fileInputStream.close();
            }
            //��ӱ�־λ
            out.writeBytes(prefix+boundary+end);
            out.writeBytes("Content-Disposition: form-data; name=\"sign\";"+end);
            out.writeBytes(end);
            out.writeBytes("liuhe");
            out.writeBytes(end);
            //��Ӳ���λ
            out.writeBytes(prefix+boundary+end);
            out.writeBytes("Content-Disposition: form-data; name=\"oper\";"+end);
            out.writeBytes(end);
            out.writeBytes("upload");
            out.writeBytes(end);
            //��β...
            out.writeBytes(prefix + boundary + prefix + end);
            //�ر�����Ϣ  
            out.flush();
            out.close();
            
            //��ȡ������System.out.println("code:"+code);//�ɹ������򷵻�200
            int code = conn.getResponseCode();
            StringBuffer sb = new StringBuffer();  
            if(code == 200){
            	//����һ������������  ��ȡ���ص���Ϣ  �Ƿ��ϴ��ɹ�  
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String str = null;  
                while((str = reader.readLine())!=null){  
                    sb.append(str);
                }   
                reader.close(); 
            }
            conn.disconnect();
            
            //����JSON�ַ���
            String outfile = sb.toString();
            System.out.println("�����ϴ�ͼƬ�ӿڣ�"+outfile);
            JSONObject jsonObject = JSONObject.fromObject(outfile);
            if(jsonObject != null){
            	int errcode = jsonObject.getInt("errcode");
            	if(errcode == 0){
            		return jsonObject.getString("errmsg");
            	}else{
            		return null;
            	}
            }else{
            	return null;
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
	}
}