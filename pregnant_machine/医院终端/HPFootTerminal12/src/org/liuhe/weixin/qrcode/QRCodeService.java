package org.liuhe.weixin.qrcode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.sf.json.JSONObject;

public class QRCodeService{
	/**
	 * 创建临时带参二维码
	 * @param accessToken 接口访问凭证
	 * @param expireSeconds 二维码有效时间，单位为秒，最大不超过1800
	 * @param sceneId 场景ID
	 * @return WeixinQRCode
	 */
	@SuppressWarnings("unused")
	private static WeixinQRCode createTemporaryQRCode(String accessToken, int expireSeconds, long sceneId) {
		WeixinQRCode weixinQRCode = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		String jsonMsg = getQRCodeJson(expireSeconds, sceneId);
		JSONObject jsonObject = JsonUtil.httpsRequest(requestUrl, "POST", jsonMsg);
		if (null != jsonObject) {
			try {
				weixinQRCode = new WeixinQRCode();
				weixinQRCode.setTicket(jsonObject.getString("ticket"));
				weixinQRCode.setExpireSeconds(jsonObject.getInt("expire_seconds"));
				weixinQRCode.setUrl(jsonObject.getString("url"));
				System.out.println("创建临时带参二维码成功 ticket:{"+weixinQRCode.getTicket()+"} expire_seconds:{"+weixinQRCode.getExpireSeconds()+"}");
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				System.out.println("创建临时带参二维码失败 errcode:{"+errorCode+"} errmsg:{"+errorMsg+"}");
			}
		}
		return weixinQRCode;
	}
	private static String createTemporaryQRCode2(String accessToken, int expireSeconds, long sceneId) {
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
	//临时的二维码请求说明{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
	@SuppressWarnings("unused")
	private static String getQRCodeJson2(int expireSeconds,long sceneId){
		Scene scene = new Scene();
		scene.setScene_id(sceneId);
		Action_info action_info = new Action_info();
		action_info.setScene(scene);
		QR_Scan qr_scan = new QR_Scan();
		qr_scan.setAction_info(action_info);
		qr_scan.setExpire_seconds(expireSeconds);
		String jsonMessage = JSONObject.fromObject(qr_scan).toString();
		return jsonMessage;
	}
	
	/**
	 * 创建 *永久* 带参二维码。不需要使用永久二维码*没有过期时间*
	 * @param accessToken 接口访问凭证
	 * @param sceneId 场景ID
	 * @return ticket
	 */
	@SuppressWarnings("unused")
	//POST数据例子：{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
	private static String createPermanentQRCode(String accessToken, int sceneId) {
		String ticket = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		String jsonMsg = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
		JSONObject jsonObject = JsonUtil.httpsRequest(requestUrl, "POST", String.format(jsonMsg, sceneId));
		if (null != jsonObject) {
			try {
				ticket = jsonObject.getString("ticket");
				System.out.println("创建永久带参二维码成功 ticket:{"+ticket+"}");
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				System.out.println("创建永久带参二维码失败 errcode:{"+errorCode+"} errmsg:{"+errorMsg+"}");
			}
		}
		return ticket;
	}
	
	/**
	 * 根据ticket换取二维码
	 * @param ticket 二维码ticket
	 * @param savePath 保存路径
	 */
	/*@SuppressWarnings("unused")
	private static String getQRCode01(String ticket,String sceneId, String savePath) {
		String filePath = null;
		String requestUrl = "http://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
		requestUrl = requestUrl.replace("TICKET", urlEncodeUTF8(ticket));
		try {			
			URL url = new URL(requestUrl);
			if (!savePath.endsWith("\\")) {
				savePath += "\\";
			}
			filePath = savePath + sceneId + ".jpg";

			DataInputStream dis = new DataInputStream(url.openStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[2048];
			int size = 0;
			while ((size = dis.read(buf)) != -1){
				fos.write(buf, 0, size);
			}
			fos.close();
			dis.close();
		} catch (Exception e) {
			filePath = null;
			e.printStackTrace();
		}
		return filePath;
	}*/
	private static String getQRCodeByTicket(String ticket, String filePath) {
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
	@SuppressWarnings("unused")
	private static String getQRCodeURL(String ticket, String filePath) {
		String requestUrl = "http://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
		requestUrl = requestUrl.replace("TICKET", urlEncodeUTF8(ticket));
		return requestUrl;
	}
	/*public static synchronized String[] getWeixinQRCode(String tokenUrl,String appname){
		// 调用接口获取凭证
		Map<String, String> map = XmlUtil.getXmlByTelent(tokenUrl,appname);
		if(map==null||map.size()==0){
			System.out.println("get access_token failure");
			return new String[]{"token"};
		}
		String token = map.get("token");
		System.out.println("this time get the access_token is："+token);
		//一天3600*24=86400 两天172800 三天259200（该二维码有效时间，以秒为单位。 最大不超过604800（即7天））
		//场景值ID，临时二维码时为32位非0整型(0~~4294967295)
		long sceneId = (long) (Math.random()*4294967295L);
		System.out.println("random scene id is："+sceneId);
		WeixinQRCode qrcode = QRCodeService.createTemporaryQRCode(token, 86400, sceneId);
		//关注时：//关注公众账号：EventKey:qrscene_111122
		//Ticket:gQFi8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1BVTndfNnpsV2lFalNpdmFRRzFCAAIETDHgVQMEhAMAAA==
		//关注后，再次扫描//EventKey:111122
		//Ticket:gQFi8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1BVTndfNnpsV2lFalNpdmFRRzFCAAIETDHgVQMEhAMAAA==
		if(qrcode==null){
			System.out.println("get ticket failure");
			return new String[]{"ticket"};
		}
		String ticket = qrcode.getTicket();
		System.out.println("get weixin qrcode ticket:"+ticket);
		String savePath = System.getProperty("user.dir")+"\\scan";
		
		String[] para = new String[4];
		para[0] = getQRCode02(ticket,sceneId+"",savePath);
		if(para[0]==null){
			System.out.println("get qrcode image failure");
			return new String[]{"image"};
		}
		para[1] = sceneId+"";
		para[2] = ticket;
		para[3] = map.get("apply");
		return para;
	}*/
	public static synchronized String[] getWeixinQRCode(String token,String savePath){
		//一天3600*24=86400 两天172800 三天259200（该二维码有效时间，以秒为单位。 最大不超过604800（即7天））
		//场景值ID，临时二维码时为32位非0整型(0~~4294967295)
		long scene = (long) (Math.random()*4294967295L);
		System.out.println("random scene id is："+scene);
		
		String ticket = QRCodeService.createTemporaryQRCode2(token, 86400, scene);
		if(ticket == null){
			return new String[]{"ticket"};
		}
		
		//String requestUrl = getQRCodeByTicket2(ticket,savePath);
		String requestUrl = getQRCodeByTicket(ticket,savePath);
		//String requestUrl = getQRCodeURL(ticket,savePath);
		if(requestUrl == null){
			return new String[]{"image"};
		}
		
		String[] para = new String[3];
		para[0] = String.valueOf(scene);
		para[1] = ticket;
		para[2] = requestUrl;
		return para;
	}
	
	/*public static boolean getQRCode(String requestUrl,String filePath) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
	
}