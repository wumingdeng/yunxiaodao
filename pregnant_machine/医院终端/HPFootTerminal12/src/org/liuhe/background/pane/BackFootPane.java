package org.liuhe.background.pane;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class BackFootPane extends JPanel{
	private static final long serialVersionUID = 1L;
    private int top = 0;
    private int left = 15;
    private int bottom = 10;//8
    private int right = 15;
    
	public void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
        //填充白色矩形圆角边框
        RoundRectangle2D round_rect = new RoundRectangle2D.Float(left, top, width-1-(left+right), height-1-(top+bottom), 20, 20);
        g2.setPaint(Color.WHITE);
    	g2.fill(round_rect);
    	//绘制渐变或者单色边框
    	//g2.setPaint(new Color(208,210,211));
        g2.setPaint(new GradientPaint(left, top, new Color(227,227,227), left, height-bottom,new Color(216,216,216),true));
        g2.draw(round_rect);
        
        //g2.setPaint(Color.RED);
        //g2.drawRect(0, 0, width-1, height-1);
    }
}