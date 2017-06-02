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

import javax.swing.JButton;

public class GeneralButton  extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean hover = false;
	private Color font_color = new Color(40,40,40);
	private Font gen_font = new Font("ºÚÌå", Font.PLAIN, 15);
	private String title = null;
	private int differ_v = 0;
	
	public GeneralButton(String title){
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
	
	public void setTitle(String title){
		this.title = title;
		repaint();
	}
	public String getTitle(){
		return this.title;
	}
	public void setDifferV(int differ){
		differ_v = differ;
	}
	public void setGenFont(Font font){
		this.gen_font = font;
	}
	public void setFontColor(Color color){
		this.font_color = color;
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
        g2d.setPaint(font_color);
    	g2d.setFont(gen_font);
    	FontMetrics fm = g2d.getFontMetrics();
    	if (getModel().isPressed()){
    		g2d.drawString(title, (width-fm.stringWidth(title))/2, (height+fm.getHeight())/2-differ_v);
    	}else{
    		g2d.drawString(title, (width-fm.stringWidth(title))/2, (height+fm.getHeight())/2-1-differ_v);
    	}
    	
//    	g2d.setPaint(new Color(10,10,10));
//    	g2d.drawRect(0, 0, width-1, height-1);
	}

}