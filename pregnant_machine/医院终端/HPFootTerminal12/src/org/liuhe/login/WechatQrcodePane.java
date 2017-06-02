package org.liuhe.login;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class WechatQrcodePane extends JPanel{
private static final long serialVersionUID = 1L;
	private Image image = null;
    private String imagePath = null;
	private int imageOrigWidth = 300;
    private int imageOrigHeight = 300;
    private int maxSize = 300;
    private int status = 0;
    private Color gen_color = new Color(144,197,72);
    /**
     * 通过status标志获取二维码状态
     * 0：正获取二维码...
     * 1：显示二维码
     * 2：登 录 成 功
     * -1：获取二维码失败！
     * */
	public void showGeting(){
		status = 0;
		imagePath = null;
		image = null;
		repaint();
	}
	public void showQrcodeImage(String qrcodePath){
		status = 1;
		imagePath = qrcodePath;
		image = new ImageIcon(qrcodePath).getImage();
		imageOrigWidth = image.getWidth(this);
		imageOrigHeight = image.getHeight(this);
		repaint();
	}
	public void showLoginSuccess(){
		status = 2;
		repaint();
	}
	public void showError(){
		status = -1;
		repaint();
	}
	public int getStatus(){
		return status;
	}
	public String getImagePath(){
		return imagePath;
	}
	
	public void paintComponent(Graphics g) {
		int width = this.getWidth(); 
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
    	//g2.setPaint(Color.WHITE);
    	//g2.fillRect(0, 0, width-1, height-1);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	if(status == 0){
    		g2.setPaint(gen_color);
    		g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
    		String hint = "正获取二维码...";
    		FontMetrics fm = g2.getFontMetrics();
        	g2.drawString(hint, (width-fm.stringWidth(hint))/2, 50);
    	}else if(status == 1){
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
        	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.drawImage(image,(width-imageWidth)/2, 0, imageWidth-1, imageHeight-1,this);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setPaint(new Color(217,217,217));
        	g2.drawRect((width-imageWidth)/2, 0, imageWidth-1, imageHeight-1);
        	g2.drawRect((width-imageWidth)/2+1, 1, imageWidth-1-2, imageHeight-1-2);
        	g2.setPaint(Color.WHITE);
        	g2.drawLine((width-imageWidth)/2+40, 0, (width-imageWidth)/2+imageWidth-40, 0);
        	g2.drawLine((width-imageWidth)/2+40, 1, (width-imageWidth)/2+imageWidth-40, 1);
        	g2.drawLine((width-imageWidth)/2+40, imageHeight-1, (width-imageWidth)/2+imageWidth-40, imageHeight-1);
        	g2.drawLine((width-imageWidth)/2+40, imageHeight-1-1, (width-imageWidth)/2+imageWidth-40, imageHeight-1-1);
        	g2.drawLine((width-imageWidth)/2, 40, (width-imageWidth)/2, imageHeight-40);
        	g2.drawLine((width-imageWidth)/2+1, 40, (width-imageWidth)/2+1, imageHeight-40);
        	g2.drawLine((width-imageWidth)/2+imageWidth-1, 40, (width-imageWidth)/2+imageWidth-1, imageHeight-40);
        	g2.drawLine((width-imageWidth)/2+imageWidth-2, 40, (width-imageWidth)/2+imageWidth-2, imageHeight-40);
        	return;
    	}else if(status == 2){
    		g2.setPaint(gen_color);
        	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 40));
        	String hint = "登 录 成 功";
        	FontMetrics fm = g2.getFontMetrics();
            g2.drawString(hint, (width-fm.stringWidth(hint))/2, (height-fm.getHeight())/2);
    	}else if(status == -1){
    		g2.setPaint(gen_color);
    		g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
    		FontMetrics fm = g2.getFontMetrics();
        	String hint = "获取二维码失败！";
        	g2.drawString(hint, (width-fm.stringWidth(hint))/2, 40);
        	g2.drawString("请点击这里重试", (width-fm.stringWidth(hint))/2, 80);
    	}
    	g2.setPaint(new Color(217,217,217));
    	g2.drawRect((width-maxSize)/2, 0, maxSize-1, height-1);
    	g2.drawRect((width-maxSize)/2+1, 1, maxSize-1-2, height-1-2);
    	g2.setPaint(Color.WHITE);
    	g2.drawLine((width-maxSize)/2+40, 0, (width-maxSize)/2+maxSize-40, 0);
    	g2.drawLine((width-maxSize)/2+40, 1, (width-maxSize)/2+maxSize-40, 1);
    	g2.drawLine((width-maxSize)/2+40, height-1, (width-maxSize)/2+maxSize-40, height-1);
    	g2.drawLine((width-maxSize)/2+40, height-1-1, (width-maxSize)/2+maxSize-40, height-1-1);
    	g2.drawLine((width-maxSize)/2, 40, (width-maxSize)/2, height-40);
    	g2.drawLine((width-maxSize)/2+1, 40, (width-maxSize)/2+1, height-40);
    	g2.drawLine((width-maxSize)/2+maxSize-1, 40, (width-maxSize)/2+maxSize-1, height-40);
    	g2.drawLine((width-maxSize)/2+maxSize-2, 40, (width-maxSize)/2+maxSize-2, height-40);
    	//g2.setPaint(Color.red);
    	//g2.drawRect(0, 0, width-1, height-1);
	}
}