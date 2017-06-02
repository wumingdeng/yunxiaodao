package org.liuhe.element.button;

import java.awt.Cursor;
import java.awt.Insets;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

public class ImageButton extends JButton{
	private static final long serialVersionUID = 1L;
	public ImageButton(String name){
		//Icon pic_addPro = new ImageIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\product_add.png").getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
        String pic_url = System.getProperty("user.dir")+"\\picture\\"+name+".png";
        String url_roll = System.getProperty("user.dir")+"\\picture\\"+name+"_down.png";
        String url_down = System.getProperty("user.dir")+"\\picture\\"+name+"_down.png"; 
    	Icon pic_img = new ImageIcon(pic_url);
    	this.setIcon(pic_img);
        this.setMargin(new Insets(0,0,0,0));
        this.setHideActionText(true);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setBorder(new BevelBorder(BevelBorder.RAISED));
        Icon pic_roll = null;
    	if(new File(url_roll).exists()){
    		pic_roll = new ImageIcon(url_roll);
    	}
    	Icon pic_down = null;
    	if(new File(url_down).exists()){
    		pic_down = new ImageIcon(url_down);
    	}
        if(pic_roll!=null){
        	this.setRolloverIcon(pic_roll);
        }
        if(pic_down!=null){
        	this.setPressedIcon(pic_down);
        }
	}
}