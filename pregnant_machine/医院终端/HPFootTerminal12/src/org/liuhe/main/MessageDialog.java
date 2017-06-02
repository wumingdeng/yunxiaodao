package org.liuhe.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

public class MessageDialog {
	private JFrame frame;
	private JDialog dialog;
	private BackgroundPanel main_pane;
	
	private String message;
	private String title;
	private int type;
	
	private boolean draging = false;
    private Point draggingAnchor = null;
    
	private Font gen_font = new Font("微软雅黑", Font.PLAIN, 14);
	private Color gen_color = new Color(40,40,40);
	private Action_listener action_listener = new Action_listener();
	public static int INFORMATION_MESSAGE = 1;
	public static int WARNING_MESSAGE = 2;
	
	public MessageDialog(JFrame frame,String message,String title,int type){
		this.frame = frame;
		this.message = message;
		this.title = title;
		this.type = type;
	}
	
	public void create_option(){
		dialog = new JDialog(frame,true);
		dialog.setUndecorated(true);
		Point frame_point = frame.getLocationOnScreen();
		int frame_x = frame_point.x;
		int frame_y = frame_point.y;
		Dimension frame_size = frame.getSize();
		int frame_width = frame_size.width;
		int frame_height = frame_size.height;
		int dialog_width = 300;
		int dialog_height = 140;
		int dialog_x = (frame_width-dialog_width)/2;
		int dialog_y = (frame_height-dialog_height)/2;
		dialog.setBounds(frame_x+dialog_x, frame_y+dialog_y, dialog_width, dialog_height);
		
    	main_pane = new BackgroundPanel();
    	main_pane.setBorder(BorderFactory.createLineBorder(new Color(188,189,190)));
    	main_pane.setLayout(new BorderLayout());
    	dialog.setContentPane(main_pane);
    	createGUI();
    	dialog.setVisible(true);
	}
	
	private Point start;
    private Timer shakeTimer;  
    @SuppressWarnings("unused")
	private void startShake() {
        final long startTime = System.currentTimeMillis();
        start = dialog.getLocation();
        shakeTimer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                Random random = new Random(elapsed);
                int change = random.nextInt(50);
                dialog.setLocation(start.x + change, start.y + change);
                if (elapsed >= 1000) {
                    stopShake();
                }
            }
        });
        shakeTimer.start();
    }
    private void stopShake() {
        shakeTimer.stop();
        dialog.setLocation(start);
        dialog.repaint();
    }
	
	private void createGUI(){
		final JPanel titleTopPanel = new JPanel();					//标题头面板，自定义标题面板类
		titleTopPanel.setOpaque(false);
		titleTopPanel.setPreferredSize(new Dimension(titleTopPanel.getWidth(),30));
		titleTopPanel.setLayout(new BorderLayout());
		JPanel title_west_pane = new JPanel();
		title_west_pane.setLayout(new FlowLayout(FlowLayout.LEFT,20,4));
		title_west_pane.setOpaque(false);
		JLabel title_label = new JLabel();
		title_label.setFont(gen_font);
		title_label.setForeground(gen_color);
		title_label.setText(title);
		title_west_pane.add(title_label);
		titleTopPanel.add(title_west_pane,BorderLayout.WEST);
		/*JPanel title_east_pane=new JPanel();
		title_east_pane.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
		title_east_pane.setOpaque(false);
		JButton button_close=createFrameButton("button_close",30,26);
		button_close.addActionListener(action_listener);
		title_east_pane.add(button_close);
		titleTopPanel.add(title_east_pane,BorderLayout.EAST);*/
		main_pane.add(titleTopPanel, BorderLayout.NORTH);
		titleTopPanel.addMouseListener(new MouseAdapter(){
     		public void mousePressed(MouseEvent e) {
    			draging = true;
    			draggingAnchor = new Point(e.getX() + titleTopPanel.getX(), e.getY() + titleTopPanel.getY());
    		}
    		public void mouseReleased(MouseEvent e) {
    			draging = false;
    			if(dialog.getLocationOnScreen().y<0){
    				dialog.setLocation(dialog.getLocationOnScreen().x, 0);
    			}
    		}
     	});
		titleTopPanel.addMouseMotionListener(new MouseMotionAdapter(){
     		public void mouseDragged(MouseEvent e){
     			if(draging){
     				dialog.setLocation(e.getLocationOnScreen().x - draggingAnchor.x, e.getLocationOnScreen().y - draggingAnchor.y);
     			}
     		}
     	});
		//中间的提示面板图案和文本信息
		JPanel  center_pane = new JPanel();
		center_pane.setLayout(new BorderLayout(10,0));
		center_pane.setOpaque(false);
		main_pane.add(center_pane,BorderLayout.CENTER);
		
		JLabel pic_label = new JLabel();
		String pic_type = null;
		if(type==MessageDialog.INFORMATION_MESSAGE){
			pic_type = "information.png";
		}else if(type==MessageDialog.WARNING_MESSAGE){
			pic_type = "warning.png";
		}
		pic_label.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\"+pic_type));
		center_pane.add(pic_label,BorderLayout.WEST);
		
		JTextArea area = new JTextArea();
		area.setFont(gen_font);
	    area.setForeground(gen_color);
	    area.setOpaque(false);
	    area.setMargin(new Insets(30,20,10,20));
	    area.setLineWrap(true);
	    area.setEditable(false);
	    area.setText(message);
	    center_pane.add(area,BorderLayout.CENTER);
	    
		//底部确定按钮面板
		JPanel button_pane = new JPanel();
		button_pane.setPreferredSize(new Dimension(button_pane.getWidth(),33));
		button_pane.setOpaque(false);
		button_pane.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
		JButton sure_button = createFrameButton("button_sure",65,20);
		sure_button.addActionListener(action_listener);
		button_pane.add(sure_button);
		main_pane.add(button_pane,BorderLayout.SOUTH);
	}
	
	//创建图标按钮
	private JButton createFrameButton(String pname,int width,int height){
        String pic_url=System.getProperty("user.dir")+"\\picture\\"+pname+".png";
        String url_roll=System.getProperty("user.dir")+"\\picture\\"+pname+"_down.png";
        String url_down=System.getProperty("user.dir")+"\\picture\\"+pname+"_down.png"; 
    	ImageIcon pic_img=new ImageIcon(pic_url);
    	ImageIcon pic_roll=null;
    	if(new File(url_roll).exists()){
    		pic_roll=new ImageIcon(url_roll);
    	}
    	ImageIcon pic_down=null;
    	if(new File(url_down).exists()){
    		pic_down=new ImageIcon(url_down);
    	}
        JButton button = new JButton();
        button.setIcon(new ImageIcon(pic_img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        button.setMargin(new Insets(0,0,0,0));
        button.setHideActionText(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new BevelBorder(BevelBorder.RAISED));
        if(pic_roll!=null){
        	button.setRolloverIcon(new ImageIcon(pic_roll.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        }
        if(pic_down!=null){
        	button.setPressedIcon(new ImageIcon(pic_down.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        }
        return button;
    }
	private class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	}
	
	private class BackgroundPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		private ImageIcon image = null;
		public BackgroundPanel(){
			String filepath = System.getProperty("user.dir")+"\\picture\\title_background.png";
	    	if(new File(filepath).exists()){
	    		image = new ImageIcon(filepath);
	    	}
		}
		public void paintComponent(Graphics g) {
	        int width = getWidth(); 
	        int height = getHeight(); 
	    	Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, new Color(244,245,245), 0, height,new Color(221,223,224),true)); 
            g2.fillRect(0, 0, width-1, height-1);
            if(image==null){ 
	            g2.setPaint(new GradientPaint(0, 0, new Color(131,209,250), width/2, height/2,Color.WHITE,true)); 
	            g2.fillRect(0, 0, width, 30);
	        }else{
	        	g2.drawImage(image.getImage(), 0, 0, width,30,this);
	        }
	    }
	}
	
//end...MOptionPane.java
}