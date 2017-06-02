package org.liuhe.background.pane;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private ImageIcon image_back = null;
	private ImageIcon image_logo = null;
	private ImageIcon image_center = null;
	private float alpha = 1.0f;
	
    public BackgroundPane() {
    	String filepath = System.getProperty("user.dir")+"\\picture\\background.png";
    	if(new File(filepath).exists()){
    		image_back = new ImageIcon(filepath);
    	}
    	filepath = System.getProperty("user.dir")+"\\picture\\bg_logo.png";
    	if(new File(filepath).exists()){
    		image_logo = new ImageIcon(filepath);
    	}
    	filepath = System.getProperty("user.dir")+"\\picture\\bg_center.png";
    	if(new File(filepath).exists()){
    		image_center = new ImageIcon(filepath);
    	}
    }
    
    /**
     * 设置中间图案bg_center.png的透明度
     * @param alpha 透明度
     * */
    public void setAlpha(float alpha){
    	this.alpha = alpha;
    	repaint();
    }
    
	public void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
    	
        if(image_back != null){
        	g2.drawImage(image_back.getImage(), 0, 0, width,height,this);
        }else{
        	g2.setPaint(new Color(255,253,238));
            g2.fillRect(0, 0, width, height);
        }
        //绘制顶部logo图案
		if(image_logo != null){
//			int logo_width = 280;
//			int logo_height = (int) ((280.0f/image_logo.getIconWidth())*image_logo.getIconHeight());
//			g2.drawImage(image_logo.getImage(), (width-logo_width)/2, 40, logo_width,logo_height,this);
			g2.drawImage(image_logo.getImage(), (width-image_logo.getIconWidth())/2, 40, image_logo.getIconWidth(),image_logo.getIconHeight(),this);
//			g2.drawImage(image_logo.getImage(), (width-200)/2, 40, 200,140,this);
		}
		//绘制中间图案
		if(image_center != null){
			Composite old = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.drawImage(image_center.getImage(), (width-image_center.getIconWidth())/2, 120, image_center.getIconWidth(),image_center.getIconHeight(),this);
			g2.setComposite(old);
		}
		//绘制边框线条
		g2.setPaint(new Color(10,10,10));
    	g2.drawRect(0, 0, width-1, height-1);
    }
}