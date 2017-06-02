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
	private Font gen_font = new Font("宋体", Font.PLAIN+Font.BOLD, 26);
	private Color gen_color = new Color(144,197,72);
	private int status = -1;
	/**
	 * 通过status标志判断专科面板状态
	 * 0：正在获取专科队列
     * 1：显示获取的专科队列
     * -1：未获取得到，请重试
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
    		String hint = "正获取专科队列...";
    		FontMetrics fm = g2.getFontMetrics();
        	g2.drawString(hint, (width-fm.stringWidth(hint))/2, 80);
    	}else if(status == 1){
    		//String hint = "获取的专科队列";
    		//System.out.println(hint);
    	}else if(status == -1){
    		g2.setPaint(gen_color);
    		g2.setFont(gen_font);
    		String hint = "请 点 击 屏 幕 重 试";
    		FontMetrics fm = g2.getFontMetrics();
        	g2.drawString(hint, (width-fm.stringWidth(hint))/2, 80);
    	}
    	//g2.setPaint(Color.GREEN);
    	//g2.drawRect(0, 0, width-1, height-1);
	}
}