package org.liuhe.background.pane;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

public class BackScrollPane extends JScrollPane{
	private static final long serialVersionUID = 1L;
	private ImageIcon image = null;
    public BackScrollPane() {
    	this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    	this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	this.setBorder(BorderFactory.createEmptyBorder());
		this.getViewport().setOpaque(false);
		this.setOpaque(false);
    }
    public void setImage(ImageIcon image) {
        this.image = image;
    }
	public void paintComponent(Graphics g) {
        int width = getWidth(); 
        int height = getHeight();
    	Graphics2D g2 = (Graphics2D) g;
        if(image!=null){
        	g2.drawImage(image.getImage(), 0, 0, width-1,height-1,this);
        }else{
        	GradientPaint gradient_fill = new GradientPaint(0, 0, new Color(245,246,247), 0, height,new Color(215,217,218),false); 
        	GradientPaint gradient_draw = new GradientPaint(0, 0, new Color(223,225,224), 0, height,new Color(188,189,190),false); 
        	RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, width-1, height-1, 25, 25);
        	g2.setPaint(gradient_fill);
        	g2.fill(rect);
        	g2.setPaint(gradient_draw);
        	g2.draw(rect);
        }
    }
}