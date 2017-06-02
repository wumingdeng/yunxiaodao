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
	
	private String boundary = "-------------------------7e020233150564";	//编节符  
    private String prefix = "--";											//前缀上传时需要多出两个"--"
    private String end = "\r\n";											//这里也需要注意，在html协议中，用 "/r/n"换行，而不是 "/n"
    
    private ServerConfig serverConfig;
	public FootImageService(ServerConfig serverConfig){
		this.serverConfig = serverConfig;
	}
	/**
	 * java 同步方法的使用，防止多线程同时执行方法。
	 * synchronized方法加锁,不管哪一个线程运行到这个方法时,都要检查有没有其它线程正在用这个方法，
	 * 有的话要等正在使用synchronized方法的线程运行完这个方法后再运行此线程,没有的话,直接运行。
	 */
	//添加脚型研究数据
	public String uploadFoot(String[] sourceName,String[] destName){
		try {
            URL http = new URL(serverConfig.getUploadUrl());
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();  
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);		//准许向服务器读数据  
            conn.setDoOutput(true);		//准许向服务器写入数据  
            
            //设置向服务器上传数据的请求方式  默认的是表单形式 ;通过Content-Type协议向服务器上传数据
            conn.setRequestProperty("Content-Type","multipart/form-data; charset=UTF-8; boundary="+boundary); 
            conn.connect();
            //创建一个输出流对象，  
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            
            for(int i=0;i<sourceName.length;i++){
            	//这是第一行，并回车换行
                out.writeBytes(prefix+boundary+end);
                //这是第二行，文件名和对应服务器的
                out.writeBytes("Content-Disposition: form-data; name=\"file"+(i+1)+"\"; filename=\""+destName[i]+"\""+end);
                //这是第三行，空一行
                out.writeBytes(end);
                //以下写入图片
                FileInputStream fileInputStream = new FileInputStream(new File(sourceName[i]));  
                byte[] b = new byte[1024*4];
                int len;
                //循环读数据  
                while((len=fileInputStream.read(b))!=-1){
                    out.write(b, 0, len);
                }  
                //写完数据后 回车换行
                out.writeBytes(end);
                fileInputStream.close();
            }
            //添加标志位
            out.writeBytes(prefix+boundary+end);
            out.writeBytes("Content-Disposition: form-data; name=\"sign\";"+end);
            out.writeBytes(end);
            out.writeBytes("liuhe");
            out.writeBytes(end);
            //添加操作位
            out.writeBytes(prefix+boundary+end);
            out.writeBytes("Content-Disposition: form-data; name=\"oper\";"+end);
            out.writeBytes(end);
            out.writeBytes("upload");
            out.writeBytes(end);
            //结尾...
            out.writeBytes(prefix + boundary + prefix + end);
            //关闭流信息  
            out.flush();
            out.close();
            
            //获取返回码System.out.println("code:"+code);//成功访问则返回200
            int code = conn.getResponseCode();
            StringBuffer sb = new StringBuffer();  
            if(code == 200){
            	//创建一个输入流对象  获取返回的信息  是否上传成功  
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String str = null;  
                while((str = reader.readLine())!=null){  
                    sb.append(str);
                }   
                reader.close(); 
            }
            conn.disconnect();
            
            //处理JSON字符串
            String outfile = sb.toString();
            System.out.println("访问上传图片接口："+outfile);
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