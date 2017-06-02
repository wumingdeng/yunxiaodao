package org.liuhe.common.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class VGrayScrollBarUI extends BasicScrollBarUI{
	public VGrayScrollBarUI(){
		super();
	}
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) { 
		 int width = thumbBounds.width;
		 int height = thumbBounds.height;
		 Graphics2D g2 = (Graphics2D)g;
		 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		 
		 g2.translate(thumbBounds.x, thumbBounds.y);
		 g2.setColor(new Color(210,210,213));
		 g2.fillRoundRect(1,1,width-2, height-2,5,5);
		 g2.setColor(new Color(155,155,155));
		 g2.drawRoundRect(1,1,width-2, height-2,5,5);
		 
		 g2.setColor(new Color(102,101,101));
		 g2.drawLine(4,height/2-3,width-4,height/2-3);
		 g2.drawLine(4,height/2,width-4,height/2);
		 g2.drawLine(4,height/2+3,width-4,height/2+3);
	}
	
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		int x = trackBounds.x;
		int y = trackBounds.y;
		int width = trackBounds.width;
		int height = trackBounds.height;
		Graphics2D g2 = (Graphics2D)g;
		
		g.setColor(new Color(231,231,233));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2.fillRect(x, y, width, height);
		g2.setPaint(new Color(170,170,170));
		g2.drawLine(x, y, x, y+height);
	}
	
	protected JButton createIncreaseButton(int orientation) {	//下面的按键
		JButton button = new BasicArrowButton(orientation){  
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				int width=getWidth();
				int height=getHeight();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); 
				
				g2.setColor(new Color(231,231,233));
				g2.fillRect(0, 0, width-1, height-1);
				
				g2.setColor(new Color(170,170,170));
				g2.drawLine(0,0,0,height);
				g2.drawLine(0,0,width,0-1);
				Polygon p=new Polygon();
		        p.addPoint(4,3);
		        p.addPoint(width-4,3);
		        p.addPoint(width/2, height-3);
		        g2.fillPolygon(p);
			}
		};
		button.setOpaque(false);
		return button;  
	}
	
	protected JButton createDecreaseButton(int orientation) {	//上面的按键
		JButton button = new BasicArrowButton(orientation){  
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				int width=getWidth();
				int height=getHeight();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); 
				
				g2.setColor(new Color(231,231,233));
				g2.fillRect(0, 0, width-1, height-1);
				
				g2.setColor(new Color(170,170,170));
				g2.drawLine(0,0,0,height); 
				g2.drawLine(0,height-1,width,height);
				Polygon p=new Polygon();
		        p.addPoint(4,height-3);
		        p.addPoint(width-4,height-3);
		        p.addPoint(width/2, 3);
		        g2.fillPolygon(p);
			}
		};
		button.setOpaque(false);
		return button;  
	}
	
	private Shape getArea(int x, int y, int w, int h) {
		  RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, w, h, 25, 25);
		  Area a = new Area(rect);
		  Rectangle2D rec = new Rectangle2D.Float(x, y + h / 2, w, h / 2);
		  Area b = new Area(rec);
		  a.add(b);
		  return a;
	}
}

