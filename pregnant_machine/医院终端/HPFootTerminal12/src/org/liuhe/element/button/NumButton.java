package org.liuhe.element.button;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class NumButton extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean hover = false;
	private String title = null;
	private Color gen_color = new Color(40,40,40);
	private Font gen_font = new Font("Î¢ÈíÑÅºÚ",Font.PLAIN,23);
	private int round = 10;
	
	public NumButton(String title){
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
		return title;
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		int height = this.getHeight();
		int width = this.getWidth();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setPaint(Color.WHITE);
        g2d.fillRoundRect(0, 0, width - 1, height - 1, round, round);
        float tran = 1.0F;
        if (hover) {
            tran = 0.85F;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tran));
        
        GradientPaint p1 = null;
        GradientPaint p2 = null;
        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(40, 40, 40), 0, height - 1, new Color(100, 100, 100));
            p2 = new GradientPaint(0, 1, new Color(40, 40, 40, 50), 0, height - 3, new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, height - 1, new Color(40, 40, 40));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, height - 3, new Color(40, 40, 40, 50));
        }
        g2d.setPaint(new GradientPaint(0, 0, new Color(245,246,246), 0, height-1, new Color(211,213,214), true));
        g2d.fillRoundRect(0, 0, width - 1, height - 1, round, round);
        g2d.setPaint(p1);
        g2d.drawRoundRect(0, 0, width - 1, height - 1, round, round);
        g2d.setPaint(p2);
        g2d.drawRoundRect(1, 1, width - 3, height - 3, round-2, round-2);
        
        g2d.setPaint(gen_color);
    	g2d.setFont(gen_font);
    	FontMetrics fm = g2d.getFontMetrics();
    	if (getModel().isPressed()){
    		g2d.drawString(title, (width-fm.stringWidth(title))/2, (height+fm.getHeight())/2-5);
    	}else{
    		g2d.drawString(title, (width-fm.stringWidth(title))/2, (height+fm.getHeight())/2-5-1);
    	}
	}
	
}