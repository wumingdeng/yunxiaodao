package org.liuhe.weixin.qrcode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.sf.json.JSONObject;

public class QRCodeService2{
	
	private static String getQRCodeByTicket2(String ticket, String filePath) {
		String requestUrl = "http://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
		requestUrl = requestUrl.replace("TICKET", urlEncodeUTF8(ticket));
		try{
			URL url = new URL(requestUrl);
			Image image = Toolkit.getDefaultToolkit().getImage(url);
			MediaTracker mediaTracker = new MediaTracker(new JPanel());
			mediaTracker.addImage(image, 0);
			mediaTracker.waitForID(0);
			BufferedImage bufImage = new BufferedImage(430,430,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2D = bufImage.createGraphics();
			g2D.drawImage(image, 0, 0,430,430, null);
			ImageIO.write(bufImage, "jpg", new File(filePath));
			return requestUrl;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static String urlEncodeUTF8(String source){
		String result = source;
		try{
			result = java.net.URLEncoder.encode(source , "utf-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取微信服务的access_token
	 * @param requestUrl 二维码请求地址
	 * */
	public static String getAppAccessToken(String requestUrl){
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(false);
            
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "UTF-8");
            
			InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(),"utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStreamReader = null;
			conn.disconnect();
			return buffer.toString().replaceAll("(\r|\n|\t)", "");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取微信二维scene
	 * 一天即3600*24=86400 （该二维码有效时间，以秒为单位。 最大不超过604800（即7天））
	 * */
	public static long getScene(){
		long scene = (long) (Math.random()*4294967295L);
		return scene;
	}
	
	/**
	 * 获取微信二维ticket
	 * */
	public static String getTicket(String accessToken, int expireSeconds, long sceneId){
		String ticket = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		String jsonMsg = getQRCodeJson(expireSeconds, sceneId);
		JSONObject jsonObject = JsonUtil.httpsRequest(requestUrl, "POST", jsonMsg);
		if (null != jsonObject) {
			try {
				ticket = jsonObject.getString("ticket");
				System.out.println("创建临时带参二维码成功 ticket:{"+ticket+"}");
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				System.out.println("创建临时带参二维码失败 errcode:{"+errorCode+"} errmsg:{"+errorMsg+"}");
			}
		}
		return ticket;
	}
	private static String getQRCodeJson(int expireSeconds,long sceneId){
		String jsonMsg = "{\"expire_seconds\": %d, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
		return String.format(jsonMsg, expireSeconds, sceneId);
	}
	
	/**
	 * 获取微信二维码图像
	 * */
	public static String getQRCodeByTicket(String ticket, String filePath) {
		String requestUrl = "http://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
		requestUrl = requestUrl.replace("TICKET", urlEncodeUTF8(ticket));
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[1024];
			int size = 0;
			while ((size = dis.read(buf)) != -1){
				fos.write(buf, 0, size);
			}
			fos.close();
			dis.close();
			conn.disconnect();
			return requestUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}