package org.liuhe.element.button;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class DeleteButton extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean hover = false;
	
	public DeleteButton(){
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
		g2d.setPaint(Color.WHITE);
		g2d.drawOval(0, 0, width-1, height-1);
        float tran = 0.9F;
        if (hover) {
            tran = 0.7F;
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
        //g2d.setPaint(new GradientPaint(0, 0, new Color(0,111,228), width/2, height/2,new Color(0,185,255),true));
        g2d.setPaint(new GradientPaint(0, 0, new Color(124,170,57), width/2, height/2,new Color(188,255,90),true));
        g2d.fillOval(0, 0, width-1, height-1);
        g2d.setPaint(p1);
        g2d.drawOval(0, 0, width-1, height-1);
        g2d.setPaint(p2);
        g2d.drawOval(1, 1, width-3, height-3);
        
        Polygon path = new Polygon();
        path.addPoint(8,height/2);
        path.addPoint(17,height/2-10);
        path.addPoint(30,height/2-10);
        path.addPoint(23,height/2-4);
        path.addPoint(40,height/2-4);
        path.addPoint(40,height/2+4);
        path.addPoint(23,height/2+4);
        path.addPoint(30,height/2+10);
        path.addPoint(17,height/2+10);
        g2d.setPaint(Color.WHITE);
        g2d.fillPolygon(path);
	}

}