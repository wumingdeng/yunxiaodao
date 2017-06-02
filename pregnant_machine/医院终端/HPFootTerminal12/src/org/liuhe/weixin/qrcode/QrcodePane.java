package org.liuhe.weixin.qrcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class QrcodePane extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private ImageIcon image = null;
	private int imageOrigWidth = 430;
	private int imageOrigHeight = 430;
	private int maxSize = 200;

	private String info;
	/**
    * 通过status标志获取面板显示状态
    * 0：不显示任何信息
    * 1：正在上传数据...
    * 2: 显示上传成功
    * 3：显示挂号信息
    * 4：显示挂号信息和二维码
    * -1：上传数据失败信息
    * */
	private int status = 0;
	public void initPane(){
		status = 0;
		repaint();
	}
	public void showGeting(){
		status = 1;
		repaint();
	}
	public void showUploaded(){
		status = 2;
		repaint();
	}
	/**
	 * 显示挂号信息
	 * 科室;专家;类型;初诊;number;wait
	 * 或者   科室;专家;类型
	 * */
	public void showClinic(String info){
		this.info = info;
		status = 3;
		repaint();
	}
	/**
	 * 显示挂号信息和二维码
	 * @param info 例如：科室  专家  类型
	 * */
	public void showQrcode(String info,String path){
		this.info = info;
		image = new ImageIcon(path);
		imageOrigWidth = image.getIconWidth();
		imageOrigHeight = image.getIconHeight();
		status = 4;
		repaint();
	}
	public void showError(String info){
		this.info = info;
		status = -1;
		repaint();
	}
	public int getStatus(){
		return status;
	}
	public ImageIcon getQrcodeImage(){
		return image;
	}
	
	public void paintComponent(Graphics g) {
		int width = this.getWidth(); 
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	if(status != 0){
    		g2.setPaint(new Color(63,169,245));
    		if(status == 1){
    			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
            	g2.drawString("正在上传数据...", 20, 50);
    		}else if(status == 2){
    			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
    			FontMetrics fm = g2.getFontMetrics();
    			String title = "上传数据成功！";
    			g2.drawString(title, (width-fm.stringWidth(title))/2, 60);
    		}else if(status == 3){
    			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 22));
            	FontMetrics fm = g2.getFontMetrics();
            	String title = "挂号成功！";
            	g2.drawString(title, (width-fm.stringWidth(title))/2, 40);
            	String[] array = info.split(";");
            	String clinic = array[0]+"  "+array[1]+"  "+array[2];
            	g2.drawString(clinic, (width-fm.stringWidth(clinic))/2, 75);
            	if(array.length > 3){
            		String number = "号源："+array[4];
                	g2.drawString(number, (width-fm.stringWidth(number))/2, 110);
                	String wait = "等待人数："+array[5];
                	g2.drawString(wait, (width-fm.stringWidth(wait))/2, 145);
            	}
    		}else if(status == 4){
    			double scaleX = width*1.0/imageOrigWidth;
        		double scaleY = (height-55-40)*1.0/imageOrigHeight;
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
            	g2.setPaint(new Color(40,40,40));
            	FontMetrics fm = null;
            	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 22));
                fm = g2.getFontMetrics();
            	String title = "挂号成功";
            	g2.drawString(title, (width-fm.stringWidth(title))/2, 26);
            	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 18));
            	fm = g2.getFontMetrics();
            	g2.drawString(info, (width-fm.stringWidth(info))/2, 50);
            	g2.drawImage(image.getImage(), (width-imageWidth)/2, 55, imageWidth,imageHeight,this);
                g2.setFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 15));
                String hint01 = "扫描二维码记录我的足迹";
                String hint02 = "获取更多脚型足部健康知识";
                fm = g2.getFontMetrics();
                g2.drawString(hint01, (width-fm.stringWidth(hint01))/2, imageHeight+55+16);
                g2.drawString(hint02, (width-fm.stringWidth(hint02))/2, imageHeight+55+35);
    		}else if(status == -1){
    			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
            	g2.drawString("上传数据失败，请重试", 20, 40);
            	g2.drawString(info, 20, 70);
    		}
    	}
    	//g2.setPaint(Color.BLACK);//new Color(242,242,242)
		//g2.drawRect(0, 0, width-1, height-1);
	}
//	
//	public void paintComponent2(Graphics g) {
//		int width = this.getWidth(); 
//        int height = this.getHeight();
//    	Graphics2D g2 = (Graphics2D) g;
//    	
//    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//    	if(hint!=null){
//    		g2.setPaint(new Color(63,169,245));
//    		if(hint.equals("get")){
//    			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
//            	g2.drawString("正在上传数据...", 20, 50);
//        	}else{
//        		if(hint.startsWith("error")){
//        			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 24));
//                	g2.drawString("上传数据失败，请重试", 20, 40);
//                	g2.drawString(hint, 20, 70);
//                }else if(hint.startsWith("success")){
//                	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 22));
//                	FontMetrics fm = g2.getFontMetrics();
//                	String title = "挂号成功！";
//                	g2.drawString(title, (width-fm.stringWidth(title))/2, 40);
//                	String[] array = clinic_info.split(";");
//                	String clinic = array[0]+"  "+array[1]+"  "+array[2];
//                	g2.drawString(clinic, (width-fm.stringWidth(clinic))/2, 75);
//                	if(array.length > 3){
//                		String number = "号源："+array[4];
//                    	g2.drawString(number, (width-fm.stringWidth(number))/2, 110);
//                    	String wait = "等待人数："+array[5];
//                    	g2.drawString(wait, (width-fm.stringWidth(wait))/2, 145);
//                	}
//                }else{
//                	double scaleX = width*1.0/imageOrigWidth;
//            		double scaleY = (height-55-40)*1.0/imageOrigHeight;
//                	double scale = (scaleX>scaleY)?scaleY:scaleX;
//                	int imageWidth = (int)(imageOrigWidth*scale);
//                	int imageHeight = (int)(imageOrigHeight*scale);
//                   	if(imageWidth>imageHeight){
//                		if(imageWidth>maxSize){
//                			imageWidth = maxSize;
//                			scale = maxSize*1.0/imageOrigWidth;
//                			imageHeight = (int) (imageOrigHeight*scale);
//                		}
//                	}else if(imageWidth<imageHeight){
//                		if(imageHeight>maxSize){
//                			imageHeight = maxSize;
//                			scale = maxSize*1.0/imageOrigHeight;
//                			imageWidth = (int) (imageOrigWidth*scale);
//                		}
//                	}else{
//                		if(imageWidth>maxSize){
//                			imageWidth = maxSize;
//                			imageHeight = maxSize;
//                		}
//                	}
//                	g2.setPaint(new Color(40,40,40));
//                	FontMetrics fm = null;
//                	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 22));
//                    fm = g2.getFontMetrics();
//                	String title = "挂号成功";
//                	g2.drawString(title, (width-fm.stringWidth(title))/2, 26);
//                	g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 18));
//                	fm = g2.getFontMetrics();
//                	g2.drawString(clinic_info, (width-fm.stringWidth(clinic_info))/2, 50);
//                	g2.drawImage(image.getImage(), (width-imageWidth)/2, 55, imageWidth,imageHeight,this);
//                    g2.setFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 15));
//                    String hint01 = "扫描二维码记录我的足迹";
//                    String hint02 = "获取更多脚型足部健康知识";
//                    fm = g2.getFontMetrics();
//                    g2.drawString(hint01, (width-fm.stringWidth(hint01))/2, imageHeight+55+16);
//                    g2.drawString(hint02, (width-fm.stringWidth(hint02))/2, imageHeight+55+35);
//                }
//        	}
//    	}
////        g2.setPaint(Color.BLACK);//new Color(242,242,242)
////        g2.drawRect(0, 0, width-1, height-1);
//	}
//	
}