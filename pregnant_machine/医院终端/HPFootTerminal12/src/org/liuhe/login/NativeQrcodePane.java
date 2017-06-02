package org.liuhe.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JPanel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 生成本地登录二维码
 * 不需要连接网络即可使用
 * */

public class NativeQrcodePane extends JPanel{
private static final long serialVersionUID = 1L;
	
	private BufferedImage bufImage = null;
	private int imageOrigWidth = 300;
    private int imageOrigHeight = 300;
    private int maxSize = 300;
    private int status = 0;
	
	public boolean createQrcode(String appid,String loginurl,String mac){
		String qrcodeurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="
			+loginurl+"&response_type=code&scope=snsapi_base&state="+mac+"#wechat_redirect";
		
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(qrcodeurl,BarcodeFormat.QR_CODE, imageOrigWidth, imageOrigHeight, hints);
			bufImage = new BufferedImage(imageOrigWidth, imageOrigHeight,BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < imageOrigWidth; x++) {
				for (int y = 0; y < imageOrigHeight; y++) {
					bufImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}
        } catch (WriterException e) {
        	e.printStackTrace();
        	return false;
		}
        return true;
	}
	
	public void showQrcodeImage(){
		status = 1;
		repaint();
	}
	public void showLogined(){
		status = 2;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		int width = this.getWidth(); 
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
    	
    	g2.setPaint(Color.WHITE);
    	g2.fillRect(0, 0, width, height);
    	
    	if(status==1){
    		double scaleX = width*1.0/imageOrigWidth;
    		double scaleY = height*1.0/imageOrigHeight;
        	double scale = (scaleX>scaleY)?scaleY:scaleX;
        	int imageWidth = (int)(imageOrigWidth*scale);
        	int imageHeight = (int)(imageOrigHeight*scale);
        	if(imageWidth>imageHeight){
        		if(imageWidth>maxSize){
        			imageWidth = maxSize;
        			scale = maxSize*1.0/imageOrigWidth;
        			imageHeight = (int) (imageOrigHeight*scale);
        		}
        	}else if(imageWidth<imageHeight){
        		if(imageHeight>maxSize){
        			imageHeight = maxSize;
        			scale = maxSize*1.0/imageOrigHeight;
        			imageWidth = (int) (imageOrigWidth*scale);
        		}
        	}else{
        		if(imageWidth>maxSize){
        			imageWidth = maxSize;
        			imageHeight = maxSize;
        		}
        	}
            g2.drawImage(bufImage,(width-imageWidth)/2, 0, imageWidth,imageHeight,this);
    	}else if(status==2){
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setPaint(new Color(63,169,245));
        	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 40));
        	String hint = "登 录 成 功";
        	FontMetrics fm = g2.getFontMetrics();
            g2.drawString(hint, (width-fm.stringWidth(hint))/2, (height-fm.getHeight())/2);
    	}
//    	g2.setPaint(Color.red);
//    	g2.drawRect(0, 0, width-1, height-1);
	}
}
