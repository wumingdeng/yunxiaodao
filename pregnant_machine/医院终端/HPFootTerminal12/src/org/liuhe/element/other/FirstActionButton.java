package org.liuhe.element.other;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;

public class FirstActionButton extends JLabel{
	private static final long serialVersionUID = 1L;
	public void paintComponent(Graphics g) {
		int width = this.getWidth();
		int height = this.getHeight();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		RoundRectangle2D round_rect = new RoundRectangle2D.Float(0, 0, width-1, height-1, 20, 20);
		g2.setPaint(new Color(144,197,72));
		g2.fill(round_rect);
		g2.setPaint(Color.WHITE);
		g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 38));
		FontMetrics fm = g2.getFontMetrics();
     	g2.drawString("点击开始操作", (width-fm.stringWidth("点击开始操作"))/2, (height+fm.getHeight())/2-7);
    }
}