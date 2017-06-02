package org.liuhe.background.pane;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class BackRoundPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private Color drawStartColor = new Color(233,234,235);
	private Color drawEndColor = new Color(215,217,218);
	public void setDrawColor(Color drawStartColor,Color drawEndColor){
		this.drawStartColor = drawStartColor;
		this.drawEndColor = drawEndColor;
	}
	public void paintComponent(Graphics g) {
		int width = this.getWidth();
		int height = this.getHeight();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		RoundRectangle2D round_rect = new RoundRectangle2D.Float(0, 0, width-1, height-1, 20, 20);
		//Ìî³ä°×É«¾ØÐÎÔ²½Ç±ß¿ò
		g2.setPaint(new GradientPaint(0, 0, new Color(245,245,246), 0, height-1,new Color(221,223,224)));
		g2.fill(round_rect);
		//»æÖÆÕû¸ö±³¾°½¥±ä
		g2.setPaint(new GradientPaint(0, 0, drawStartColor, 0, height-1,drawEndColor));
		g2.draw(round_rect);
    }
}
