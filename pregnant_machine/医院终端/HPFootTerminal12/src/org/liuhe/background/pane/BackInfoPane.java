package org.liuhe.background.pane;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
public class BackInfoPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private int differ_y = 0;						//Y��ƫ����
	private int differ_x = 0;						//X��ƫ����
	private int lineHeight = 106;					//ˮƽ�ָ���ƫ����
	private boolean fillRect = true;				//�Ƿ������佥�䱳��
	private boolean drawLine = false;				//�Ƿ����ˮƽ�ָ���
	
	private Color fillStartColor = new Color(245,246,247);
	private Color fillEndColor = new Color(215,217,218);
	private Color drawStartColor = new Color(223,225,224);
	private Color drawEndColor = new Color(189,190,191);

    public void setDiffer_y(int differ){
    	this.differ_y = differ;
    }
    public void setDiffer_x(int differ){
    	this.differ_x = differ;
    }
    public void isFillRect(boolean fillRect){
    	this.fillRect = fillRect;
    }
    public void isDrawLine(boolean drawLine){
    	this.drawLine = drawLine;
    }
    public void setLineHeight(int lineHeight){
    	this.lineHeight = lineHeight;
    }
    
    public void setFillEndColor(Color fillEndColor){
    	this.fillEndColor = fillEndColor;
    }
    public void setFillStartColor(Color fillStartColor){
    	this.fillStartColor = fillStartColor;
    }
    public void setDrawStartColor(Color drawStartColor){
    	this.drawStartColor = drawStartColor;
    }
    public void setDrawEndColor(Color drawEndColor){
    	this.drawEndColor = drawEndColor;
    }
    
	public void paintComponent(Graphics g) { 
    	Graphics2D g2 = (Graphics2D) g;
    	int width = this.getWidth(); 
        int height = this.getHeight();
        RoundRectangle2D rect = new RoundRectangle2D.Float(10-differ_x, 4-differ_y, width-21+differ_x*2, height-9+differ_y, 25, 25);
    	if(fillRect){
        	g2.setPaint(new GradientPaint(0, 0,fillStartColor, 0, height,fillEndColor,false));
        	g2.fill(rect);			//���Բ�Ǿ���
    	}
    	if(drawLine){				//����һ��ˮƽ�ָ���
    		g2.setColor(Color.WHITE);
            g2.drawLine(25, lineHeight, getWidth()-25, lineHeight);
    	}
    	g2.setPaint(new GradientPaint(0, 0, drawStartColor, 0, height,drawEndColor,false));
    	g2.draw(rect);				//����Բ�Ǿ��α߿�
    }
}