package org.liuhe.background.pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class ClinicButtonPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private Font gen_font = new Font("����", Font.PLAIN+Font.BOLD, 26);
	private Color gen_color = new Color(144,197,72);
	private int status = -1;
	/**
	 * ͨ��status��־�ж�ר�����״̬
	 * 0�����ڻ�ȡר�ƶ���
     * 1����ʾ��ȡ��ר�ƶ���
     * -1��δ��ȡ�õ���������
	 * */
	public void showGeting(){
		status = 0;
		repaint();
	}
	public void showClinic(){
		status = 1;
		repaint();
	}
	public void showError(){
		status = -1;
		repaint();
	}
	public int getStatus(){
		return status;
	}
	
	public void paintComponent(Graphics g) {
		int width = this.getWidth(); 
        int height = this.getHeight();
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	if(status == 0){
    		g2.setPaint(gen_color);
    		g2.setFont(gen_font);
    		String hint = "����ȡר�ƶ���...";
    		FontMetrics fm = g2.getFontMetrics();
        	g2.drawString(hint, (width-fm.stringWidth(hint))/2, 80);
    	}else if(status == 1){
    		//String hint = "��ȡ��ר�ƶ���";
    		//System.out.println(hint);
    	}else if(status == -1){
    		g2.setPaint(gen_color);
    		g2.setFont(gen_font);
    		String hint = "�� �� �� �� Ļ �� ��";
    		FontMetrics fm = g2.getFontMetrics();
        	g2.drawString(hint, (width-fm.stringWidth(hint))/2, 80);
    	}
    	//g2.setPaint(Color.GREEN);
    	//g2.drawRect(0, 0, width-1, height-1);
	}
}