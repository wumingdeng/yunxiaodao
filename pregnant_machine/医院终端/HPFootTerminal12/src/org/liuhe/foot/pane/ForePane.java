package org.liuhe.foot.pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ForePane extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public void paintComponent(Graphics g) {
		int width = this.getWidth(); 
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	g2.setPaint(new Color(40,40,40));
    	g2.setFont(new Font("����", Font.PLAIN+Font.BOLD, 22));
    	FontMetrics fm = g2.getFontMetrics();
    	
    	int differ_v = 40-60;
    	int differ_h = -40;
    	String str01 = "�뵥������ʼ��⡱��ť";
    	g2.drawString(str01, (width-fm.stringWidth(str01))/2, 60+differ_v);
    	
    	g2.fillOval(40+differ_h, 100-13+differ_v, 10, 10);
    	String str02 = "���������ݣ���������";
    	g2.drawString(str02, 60+differ_h, 100+differ_v);
    	
    	g2.fillOval(40+differ_h, 140-13+differ_v, 10, 10);
    	String str03 = "�ȴ������ؼ�����";
    	g2.drawString(str03, 60+differ_h, 140+differ_v);
    	
    	g2.fillOval(40+differ_h, 180-13+differ_v, 10, 10);
    	String str04 = "ȡ��ⱨ��";
    	g2.drawString(str04, 60+differ_h, 180+differ_v);
    	
    	//g2.setPaint(Color.BLACK);
        //g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
}