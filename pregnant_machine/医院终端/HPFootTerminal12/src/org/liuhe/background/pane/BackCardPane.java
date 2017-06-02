package org.liuhe.background.pane;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackCardPane extends JPanel{
	private static final long serialVersionUID = 1L;
    private int top = 0;
    private int left = 15;
    private int bottom = 10;
    private int right = 15;
    private Color backColor = new Color(255,253,238); 
    private ImageIcon image_logo = null;
    private ImageIcon image_pregnant = null;
    public BackCardPane(){
    	setOpaque(false);
    	String filepath = System.getProperty("user.dir")+"\\picture\\logo100.png";
    	if(new File(filepath).exists()){
    		image_logo = new ImageIcon(filepath);
    	}
    	filepath = System.getProperty("user.dir")+"\\picture\\pregnant.png";
    	if(new File(filepath).exists()){
    		image_pregnant = new ImageIcon(filepath);
    	}
    }
	public void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setPaint(backColor);
    	g2.drawRect(0, 0, width, height);
        //Ìî³ä°×É«¾ØÐÎÔ²½Ç±ß¿ò
        RoundRectangle2D round_rect = new RoundRectangle2D.Float(left, top, width-1-(left+right), height-1-(top+bottom), 20, 20);
        g2.setPaint(Color.WHITE);
    	g2.fill(round_rect);
    	//»æÖÆ±³¾°Í¼Æ¬
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
    	if(image_logo != null){
        	g2.drawImage(image_logo.getImage(), width-right-20-image_logo.getIconWidth(), height-bottom-20-image_logo.getIconHeight()
        			, image_logo.getIconWidth(),image_logo.getIconHeight(),this);
        }
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    	if(image_pregnant!=null){
        	g2.drawImage(image_pregnant.getImage(), left-15, height-bottom-image_pregnant.getIconHeight()
        			, image_pregnant.getIconWidth(),image_pregnant.getIconHeight(),this);
        }
    	//»æÖÆ½¥±ä»òÕßµ¥É«±ß¿ò
    	//g2.setPaint(new Color(208,210,211));
        g2.setPaint(new GradientPaint(left, top, new Color(227,227,227), left, height-bottom,new Color(216,216,216),true));
        g2.draw(round_rect);
        //g2.setPaint(Color.red);
        //g2.drawRect(0, 0, width-1, height-1);
    }
}