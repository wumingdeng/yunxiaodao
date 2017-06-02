package org.liuhe.element.button;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class HPClinicButton extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean hover = false;
	private String title = null;
	private ImageIcon image_back = null;
	
	private Color font_color = new Color(30,30,30);
	private Font gen_font = new Font("黑体", Font.PLAIN, 20);
    
    public HPClinicButton(String title){
    	String filepath = System.getProperty("user.dir")+"\\picture\\hpbt.jpg";
    	if(new File(filepath).exists()){
    		image_back = new ImageIcon(filepath);
    	}
    	this.title = title;
    	setMargin(new Insets(0,0,0,0));
	    setHideActionText(true);
	    setBorderPainted(false);
	    setFocusPainted(false);
	    setContentAreaFilled(false);
	    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }
    
    public String getTitle(){
    	return this.title;
    }
    
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
        int height = this.getHeight();
        int width = this.getWidth();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float tran = 1F;
        if (hover) {
            tran = 0.8F;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tran));
        //绘制按钮背景
        if(image_back!=null){
        	g2d.drawImage(image_back.getImage(), 0, 0, width-1,height-1,this);
        }
        //绘制按钮标题
        g2d.setPaint(font_color);
    	g2d.setFont(gen_font);
    	FontMetrics fm = g2d.getFontMetrics();
    	if (getModel().isPressed()){
    		g2d.drawString(title, (width-fm.stringWidth(title))/2+40, (height+fm.getHeight())/2);
    	}else{
    		g2d.drawString(title, (width-fm.stringWidth(title))/2+40, (height+fm.getHeight())/2-1);
    	}
	}
    
}