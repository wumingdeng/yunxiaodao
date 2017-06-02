package org.liuhe.background.pane;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackConfigPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private ImageIcon image = null;
	private int title_height = 40;
	
    public BackConfigPane() {
    	String filepath = System.getProperty("user.dir")+"\\picture\\title_background.png";
    	if(new File(filepath).exists()){
    		image = new ImageIcon(filepath);
    	}
    }
	public void setTitle_height(int titleHeight) {
		title_height = titleHeight;
	}

	public void paintComponent(Graphics g) {
        int width = this.getWidth(); 
        int height = this.getHeight(); 
    	Graphics2D g2 = (Graphics2D) g;
    	//绘制全背景渐变
        g2.setPaint(new GradientPaint(0, 0, new Color(244,245,245), 0, height,new Color(221,223,224),true)); 
        g2.fillRect(0, 0, width, height);
        //绘制标题头样式
        if(image!=null){
        	g2.drawImage(image.getImage(), 0, 0, width,title_height,this);
        }
        //绘制圆角边框
        g2.setPaint(new Color(190,190,190));
    	g2.draw(new RoundRectangle2D.Float(0, 0, width-1, height-1, 20, 20));
    }

}