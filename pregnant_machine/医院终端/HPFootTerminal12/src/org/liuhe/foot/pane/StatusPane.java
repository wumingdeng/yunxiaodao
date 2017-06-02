package org.liuhe.foot.pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class StatusPane extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/*private final String status01 = "����ɨ��...";
	private final String status011 = "ɨ���������Ӳ���豸��";
	private final String status02 = "�ɹ���ȡ����ͼƬ";
	private final String status03 = "���ڷ�����������...";
	private final String status041 = "������ɣ���ʼ��ӡ���档";
	private final String status042 = "�������������¿�ʼɨ�裡";*/
	
	private String title = "����״̬��ʾ���";
	private Color color = new Color(63,169,245);
	private Font font = new Font("����", Font.PLAIN+Font.BOLD, 23);
	private String status01 = null;
	private String status02 = null;
	private String status03 = null;
	private String status041 = null;
	
	public void initStatus(){
		status01 = null;
		status02 = null;
		status03 = null;
		status041 = null;
		repaint();
	}
	
	public void setStatus01(String status01) {
		this.status01 = status01;
		repaint();
	}
	public void setStatus02(String status02) {
		this.status02 = status02;
		repaint();
	}
	public void setStatus03(String status03) {
		this.status03 = status03;
		repaint();
	}
	public void setStatus041(String status041) {
		this.status041 = status041;
		repaint();
	}

	public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	FontMetrics fm = g2.getFontMetrics();
    	g2.setPaint(color);
    	g2.setFont(font);
    	int fontHeight = fm.getHeight();
    	if(status01!=null){
    		g2.drawString(status01, 20, 15+fontHeight+20);
    		if(status02!=null){
    			g2.drawString(status02, 20, 15+(fontHeight+20)*2);
    			if(status03!=null){
    				g2.drawString(status03, 20, 15+(fontHeight+20)*3);
    				if(status041!=null){
    					g2.drawString(status041, 20, 15+(fontHeight+20)*4);
    				}
    			}
    		}
    	}else{
    		g2.drawString(title, 20, 20+fontHeight);
    	}
    	//g2.setPaint(Color.BLACK);
        //g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
	
}