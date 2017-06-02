package org.liuhe.element.button;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

public class ClinicButton extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean hover = false;
	
	private String clinic = null;
	private String doctor = null;
	private String type = null;
	private String wait = null;
	private String repeat_num = null;
	
	private Color gen_color = new Color(62,170,245);
	private Font gen_font = new Font("黑体", Font.PLAIN+Font.BOLD, 20);
	private int differV = 0;
	private int round = 20;
	
	private Color startFillColor = new Color(245,246,246);
	private Color endFillColor = new Color(211,213,214);
	
	public ClinicButton(String clinic,String doctor,String type,String wait,String repeat_num){
		this.clinic = clinic;
		this.doctor = doctor;
		this.type = type;
		this.wait = wait;
		this.repeat_num = repeat_num;
		if(repeat_num!=null&&!repeat_num.equals("0")){
			setRepeateColor();
		}
		
		setMargin(new Insets(0,0,0,0));
	    setHideActionText(true);
	    setBorderPainted(false);
	    setFocusPainted(false);
	    setContentAreaFilled(false);
	    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    
	    addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
	}
	
	public String getClinic(){
		return clinic;
	}
	public String getDoctor(){
		return doctor;
	}
	public String getType(){
		return type;
	}
	public String getWait(){
		return wait;
	}
	public String getRepeateNum(){
		return repeat_num;
	}
	
	private void setRepeateColor(){
		this.startFillColor = new Color(250,251,251);
		this.endFillColor = new Color(141,253,142);
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
        int height = this.getHeight();
        int width = this.getWidth();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        float tran = 1F;
        if (hover) {
            tran = 0.85F;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tran));
        GradientPaint p1 = null;
        GradientPaint p2 = null;
        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(40, 40, 40), 0, height - 1, new Color(100, 100, 100));
            p2 = new GradientPaint(0, 1, new Color(40, 40, 40, 50), 0, height - 3, new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, height - 1, new Color(40, 40, 40));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, height - 3, new Color(40, 40, 40, 50));
        }
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, width - 1,height - 1, round, round);
        g2d.setPaint(Color.WHITE);
        g2d.fill(r2d);
        g2d.setPaint(new GradientPaint(0, 0, startFillColor, 0, height-1, endFillColor, true));
        g2d.fill(r2d);
        g2d.setPaint(p1);
        g2d.drawRoundRect(0, 0, width - 1, height - 1, round, round);
        g2d.setPaint(p2);
        g2d.drawRoundRect(1, 1, width - 3, height - 3, round-2, round-2);
        g2d.setPaint(gen_color);
    	g2d.setFont(gen_font);
    	FontMetrics fm = g2d.getFontMetrics();
    	if(getModel().isPressed()){
    		g2d.drawString(clinic, (width-fm.stringWidth(clinic))/2, fm.getHeight()-differV);
    		String info = doctor+"  "+type;
    		g2d.drawString(info, (width-fm.stringWidth(info))/2, fm.getHeight()*2-differV);
    		g2d.drawLine(10, fm.getHeight()*2-differV+9, width-10, fm.getHeight()*2-differV+9);
    		g2d.drawString("排队人数："+wait, (width-fm.stringWidth("排队人数："+wait))/2, fm.getHeight()*3-differV+10);
    	}else{
    		g2d.drawString(clinic, (width-fm.stringWidth(clinic))/2, fm.getHeight()-differV-1);
    		String info = doctor+"  "+type;
    		g2d.drawString(info, (width-fm.stringWidth(info))/2, fm.getHeight()*2-differV-1);
    		g2d.drawLine(10, fm.getHeight()*2-differV-1+9, width-10, fm.getHeight()*2-differV-1+9);
    		g2d.drawString("排队人数："+wait, (width-fm.stringWidth("排队人数："+wait))/2, fm.getHeight()*3-differV-1+10);
    	}
    	
	}
}