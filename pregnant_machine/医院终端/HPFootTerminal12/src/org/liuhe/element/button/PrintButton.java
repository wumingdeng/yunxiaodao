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

public class PrintButton extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean hover = false;
	private String str_01 = "生成";
	private String str_02 = "报告";
	private Color font_color = new Color(62,170,245);
	
	public PrintButton(){
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
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
        int height = this.getHeight();
        int width = this.getWidth();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float tran = 1F;
        if (hover) {
            tran = 0.9F;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tran));

        GradientPaint p1 = null;
        GradientPaint p2 = null;
        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, height - 1, new Color(100, 100, 100));
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, height - 3, new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, height - 1, new Color(0, 0, 0));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, height - 3, new Color(0, 0, 0, 50));
        }
        
        g2d.setPaint(new GradientPaint(0, 0, new Color(245,246,246), 0, height-1,new Color(211,213,214),true));
        g2d.fillOval(0, 0, width-1, height-1);
        g2d.setPaint(p1);
        g2d.drawOval(0, 0, width-1, height-1);
        g2d.setPaint(p2);
        g2d.drawOval(1, 1, width-3, height-3);
        
        g2d.setPaint(font_color);
    	g2d.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 32));
    	FontMetrics fm = g2d.getFontMetrics();
    	if (getModel().isPressed()){
    		g2d.drawString(str_01, (width-fm.stringWidth(str_01))/2, height/2);
    		g2d.drawString(str_02, (width-fm.stringWidth(str_02))/2, height/2+fm.getHeight());
    	}else{
    		g2d.drawString(str_01, (width-fm.stringWidth(str_01))/2, height/2-1);
    		g2d.drawString(str_02, (width-fm.stringWidth(str_02))/2, height/2+fm.getHeight()-1);
    	}
	}
}
