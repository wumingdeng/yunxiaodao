package org.liuhe.element.field;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;

/**
 * 圆角的矩形文本输入框
 * */

public class RoundTextField extends JTextField{
	private static final long serialVersionUID = 1L;
	private int round = 15;
	public RoundTextField(){
		setOpaque(false);
		setMargin(new Insets(0,5,0,5));
	}
	public RoundTextField(String text){
		super.setText(text);
		setOpaque(false);
		setMargin(new Insets(0,5,0,5));
	}
	public void setLeft(int left){
		setMargin(new Insets(0,left,0,left));
	}
	protected void paintBorder(Graphics g){
		int height = getHeight();
		int width = getWidth();
		Graphics2D g2d = (Graphics2D)g.create();
	 	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	 	g2d.setColor(new Color(188,191,191));
	 	g2d.drawRoundRect(0, 0, width-1 , height-1 , round, round);
	 	g2d.dispose();
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		int height = getHeight();
		int width = getWidth();
		//g2d.setColor(new Color(237,238,238));
		//g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.white);
		g2d.fillRoundRect(0, 0, width - 1, height - 1, round, round);
		super.paintComponent(g2d);
	}
}