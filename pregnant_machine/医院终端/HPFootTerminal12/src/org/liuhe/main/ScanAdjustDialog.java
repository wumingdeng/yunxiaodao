package org.liuhe.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.liuhe.algorithm.config.ScanConfig;
import org.liuhe.algorithm.scan.AcquireHelper;
import org.liuhe.background.pane.BackConfigPane;
import org.liuhe.background.pane.BackInfoPane;
import org.liuhe.element.button.ImageButton;
import org.liuhe.foot.pane.Foot_Adjust;

import com.asprise.util.jtwain.JTwainException;
import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.sun.awt.AWTUtilities;

public class ScanAdjustDialog {
	private JDialog dialog;						//父面板界面
	private ScanConfig scanConfig;				//校正参数配置类
	
	private JDialog parent_dialog;
	private BackConfigPane main_pane;
	private JPanel topPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	private Font gen_font = new Font("微软雅黑", Font.PLAIN, 13);
	private Font gen_font12 = new Font("微软雅黑", Font.PLAIN, 12);
	private Color gen_color = new Color(60,60,60);
    private Action_listener action_listener = new Action_listener();
    private Textvalue_listener text_listener = new Textvalue_listener();
    private DecimalFormat df = new DecimalFormat("#.####");
    private ImageButton button_close;
    private boolean draging = false; 
    private Point draggingAnchor = null;
    
    private JTextField text_h_real;
    private JTextField text_h_measure;
    private JTextField text_h_scale;
    private JTextField text_h_ave;
    
    private JTextField text_v_real;
    private JTextField text_v_measure;
    private JTextField text_v_scale;
    private JTextField text_v_ave;
    
    private JCheckBox check_scan;
    private JCheckBox check_scalex;
    private JCheckBox check_scaley;
    private JCheckBox check_scalez;
    private JCheckBox check_right;
    
    private Foot_Adjust pane_image;				//校验图片面板
    
    private JButton button_get;
    private JButton button_save;
    private JButton button_back;
	
    public ScanAdjustDialog(JDialog dialog,ScanConfig scan_config){
		this.parent_dialog = dialog;
		this.scanConfig = scan_config;
		parent_dialog.setEnabled(false);
	}
    
    public void create_config_pane(){
		dialog = new JDialog(parent_dialog);
		dialog.setUndecorated(true);
		dialog.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if(parent_dialog.isEnabled()){
				}else{
					parent_dialog.setEnabled(true);
					parent_dialog.requestFocus();
				}
			}
			public void windowClosed(WindowEvent e){
				if(parent_dialog.isEnabled()){
				}else{
					parent_dialog.setEnabled(true);
					parent_dialog.requestFocus();
				}
			}
		});
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		int soft_width = 850;					//宽度850为首选项
		if( (int)(scrSize.getWidth())<850){		//如果屏幕宽度小于首选宽，则取屏幕最大宽度。
			soft_width = (int)(scrSize.getWidth());
		}
		int soft_x = ((int)(scrSize.getWidth())-soft_width)/2;
		Insets inset = Toolkit.getDefaultToolkit().getScreenInsets(dialog.getGraphicsConfiguration());
		int inset_bottom = inset.bottom;
		dialog.setLocation(soft_x, 0);
		dialog.setSize(soft_width, (int)(scrSize.getHeight())-inset_bottom);
    	main_pane = new BackConfigPane();
    	main_pane.setBorder(new EmptyBorder(1,1,1,1));
     	main_pane.setLayout(new BorderLayout());
    	dialog.setContentPane(main_pane);
    	createGUI();
    	dialog.setVisible(true);
    	AWTUtilities.setWindowShape(dialog, new RoundRectangle2D.Double( 0.0D, 0.0D, dialog.getWidth(), dialog.getHeight(), 20.0D,  20.0D));
	}
    
    private void createGUI(){
		initTopPanel();
		initLeftPanel();
		initRightPanel();
		main_pane.add(topPanel, BorderLayout.NORTH);
		main_pane.add(leftPanel, BorderLayout.WEST);
		main_pane.add(rightPanel, BorderLayout.CENTER);
	}
    
    private void initTopPanel(){
		topPanel.setLayout(new BorderLayout());
		topPanel.setOpaque(false);
		final JPanel titleTopPanel = new JPanel();
		titleTopPanel.setOpaque(false);
		titleTopPanel.setPreferredSize(new Dimension(titleTopPanel.getWidth(),40));
		titleTopPanel.setLayout(new BorderLayout());
		JPanel title_west_pane = new JPanel();
		title_west_pane.setLayout(new FlowLayout(FlowLayout.LEFT,20,4));
		title_west_pane.setOpaque(false);
		JLabel title_label = new JLabel();
		title_label.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\title_adjust.png"));
		title_west_pane.add(title_label);
		titleTopPanel.add(title_west_pane,BorderLayout.WEST);
		JPanel title_east_pane = new JPanel();
		title_east_pane.setLayout(new FlowLayout(FlowLayout.LEFT,20,1));
		title_east_pane.setOpaque(false);
		button_close = new ImageButton("button_close");
		button_close.addActionListener(action_listener);
		title_east_pane.add(button_close);
		titleTopPanel.add(title_east_pane,BorderLayout.EAST);
		topPanel.add(titleTopPanel, BorderLayout.NORTH);
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
	}
    
    public void initLeftPanel(){
    	leftPanel.setLayout(new BorderLayout(0,0));
		leftPanel.setOpaque(false);
		JScrollPane scroll_left = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_left.setOpaque(false);								//滑动面板,设置为不出现滚动条
		scroll_left.setBorder(BorderFactory.createEmptyBorder());
		scroll_left.getViewport().setOpaque(false);
		leftPanel.add(scroll_left,BorderLayout.CENTER);
		JPanel left_pane = new JPanel();							//滑动面板中承载的放置底层组件的面板,放置到scroll_left中
		left_pane.setOpaque(false);									//左边面板设置为宽度为：280px
		left_pane.setPreferredSize(new Dimension(280-20,left_pane.getHeight()));
		left_pane.setLayout(new BorderLayout(0,0));
		scroll_left.setViewportView(left_pane);
		
		JPanel panel_config = new JPanel();							//放置参数配置面板：水平校正面板、垂直校正面板
		panel_config.setLayout(new BorderLayout(0,0));
		panel_config.setOpaque(false);
		left_pane.add(panel_config,BorderLayout.NORTH);
		
		BackInfoPane pane_level_lay = new BackInfoPane();			//水平校正方向的背景面板*********
			pane_level_lay.setFillEndColor(new Color(224,225,226));
			pane_level_lay.isDrawLine(true);
		pane_level_lay.setLayout(new FlowLayout(FlowLayout.CENTER,15,8));
			pane_level_lay.setPreferredSize(new Dimension(280-20,130-10+30));
		JPanel pane_level = new JPanel();
		pane_level.setOpaque(false);
		pane_level.setPreferredSize(new Dimension(270-20-20,115-10+30));
		pane_level.setLayout(null);
        pane_level.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "水平方向（mm）", TitledBorder.LEADING, TitledBorder.TOP,
        		gen_font,new Color(78, 78, 78)));
		pane_level_lay.add(pane_level);
		panel_config.add(pane_level_lay,BorderLayout.NORTH);		//放置水平校正面板
		JLabel label_h_measure = new JLabel("测量尺寸：");			//水平测量尺寸
		label_h_measure.setFont(gen_font12);
		label_h_measure.setForeground(gen_color);
		label_h_measure.setBounds(15, 48, 90, 20);
		pane_level.add(label_h_measure);
		text_h_measure = new JTextField();
		text_h_measure.setFont(gen_font12);
		text_h_measure.setForeground(gen_color);
		text_h_measure.setBackground(Color.WHITE);
		text_h_measure.setBounds(105, 48, 112, 20);
		pane_level.add(text_h_measure);
		JLabel label_h_real = new JLabel("实际尺寸：");				//水平输入实际尺寸
		label_h_real.setFont(gen_font12);
		label_h_real.setForeground(gen_color);
		label_h_real.setBounds(15, 23, 90, 20);
		pane_level.add(label_h_real);
		text_h_real = new JTextField();
		text_h_real.setFont(gen_font12);
		text_h_real.setForeground(gen_color);
		text_h_real.setBackground(Color.WHITE);
		text_h_real.setBounds(105, 23, 112, 20);
		text_h_real.getDocument().addDocumentListener(text_listener);
		pane_level.add(text_h_real);
		JLabel label_h_scale=new JLabel("比 例 尺 ：");				//换算比例尺（实际尺寸/测量尺寸）
		label_h_scale.setFont(gen_font12);
		label_h_scale.setForeground(gen_color);
		label_h_scale.setBounds(15, 73, 90, 20);
		pane_level.add(label_h_scale);
		text_h_scale = new JTextField();
		text_h_scale.setFont(gen_font12);
		text_h_scale.setForeground(gen_color);
		text_h_scale.setBounds(105, 73, 112, 20);
		pane_level.add(text_h_scale);
		JLabel label_h_ave = new JLabel("统计平均值：");				//水平统计平均值
		label_h_ave.setFont(gen_font12);
		label_h_ave.setForeground(gen_color);
		label_h_ave.setBounds(15, 104, 90, 20);
		pane_level.add(label_h_ave);
		text_h_ave=new JTextField();
		text_h_ave.setFont(gen_font12);
		text_h_ave.setForeground(gen_color);
		text_h_ave.setBounds(105, 104, 112, 20);
		text_h_ave.setText(scanConfig.getAve_x());
		text_h_ave.addActionListener(action_listener);
		pane_level.add(text_h_ave);

		BackInfoPane pane_vertical_lay = new BackInfoPane();		//垂直校正方向的背景面板*********
		pane_vertical_lay.setFillEndColor(new Color(224,225,226));
		pane_vertical_lay.isDrawLine(true);
		pane_vertical_lay.setLayout(new FlowLayout(FlowLayout.CENTER,15,8));
		pane_vertical_lay.setPreferredSize(new Dimension(280-20,130-10+30));
		JPanel pane_vertical = new JPanel();
		pane_vertical.setOpaque(false);
		pane_vertical.setPreferredSize(new Dimension(270-20-20,115-10+30));
		pane_vertical.setLayout(null);
		pane_vertical.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "垂直方向（mm）", TitledBorder.LEADING, TitledBorder.TOP,
    		gen_font,new Color(78, 78, 78)));
		pane_vertical_lay.add(pane_vertical);
		panel_config.add(pane_vertical_lay,BorderLayout.SOUTH);		//放置垂直校正面板
		JLabel label_v_measure = new JLabel("测量尺寸：");			//垂直测量尺寸
		label_v_measure.setFont(gen_font12);
		label_v_measure.setForeground(gen_color);
		label_v_measure.setBounds(15, 48, 90, 20);
		pane_vertical.add(label_v_measure);
		text_v_measure = new JTextField();
		text_v_measure.setFont(gen_font12);
		text_v_measure.setForeground(gen_color);
		text_v_measure.setBackground(Color.WHITE);
		text_v_measure.setBounds(105, 48, 112, 20);
		pane_vertical.add(text_v_measure);
		JLabel label_v_real = new JLabel("实际尺寸：");				//垂直输入实际尺寸
		label_v_real.setFont(gen_font12);
		label_v_real.setForeground(gen_color);
		label_v_real.setBounds(15, 23, 90, 20);
		pane_vertical.add(label_v_real);
		text_v_real = new JTextField();
		text_v_real.setFont(gen_font12);
		text_v_real.setForeground(gen_color);
		text_v_real.setBackground(Color.WHITE);
		text_v_real.setBounds(105, 23, 112, 20);
		text_v_real.getDocument().addDocumentListener(text_listener);
		pane_vertical.add(text_v_real);
		JLabel label_v_scale = new JLabel("比 例 尺 ：");				//换算比例尺（实际尺寸/测量尺寸）
		label_v_scale.setFont(gen_font12);
		label_v_scale.setForeground(gen_color);
		label_v_scale.setBounds(15, 73, 90, 20);
		pane_vertical.add(label_v_scale);
		text_v_scale = new JTextField();
		text_v_scale.setFont(gen_font12);
		text_v_scale.setForeground(gen_color);
		text_v_scale.setBounds(105, 70+3, 110+2, 20);
		pane_vertical.add(text_v_scale);
		JLabel label_v_ave = new JLabel("统计平均值：");				//垂直统计平均值
		label_v_ave.setFont(gen_font12);
		label_v_ave.setForeground(gen_color);
		label_v_ave.setBounds(15, 104, 90, 20);
		pane_vertical.add(label_v_ave);
		text_v_ave = new JTextField();
		text_v_ave.setFont(gen_font12);
		text_v_ave.setForeground(gen_color);
		text_v_ave.setBounds(105, 104, 112, 20);
		text_v_ave.setText(scanConfig.getAve_y());
		text_v_ave.addActionListener(action_listener);
		pane_vertical.add(text_v_ave);
		
		BackInfoPane pane_area_lay = new BackInfoPane();			//选择复选框********
		pane_area_lay.setDrawStartColor(new Color(218,219,220));
		pane_area_lay.setDrawEndColor(new Color(178,179,180));
        pane_area_lay.setLayout(null);
        left_pane.add(pane_area_lay,BorderLayout.CENTER);
        check_scan = new JCheckBox();								//扫描导入
        check_scan.setOpaque(false);
        check_scan.setBounds(25, 13, 20, 20);
        	check_scan.setSelected(Boolean.parseBoolean(scanConfig.getUse_scan()));
        pane_area_lay.add(check_scan);
        JLabel label_scan = new JLabel();
        label_scan.setFont(gen_font);
        label_scan.setForeground(gen_color);
        label_scan.setText("是否在扫描导入时应用此参数");
        label_scan.setBounds(47, 13, 190, 20);
        pane_area_lay.add(label_scan);
        
        JPanel pane_scale = new JPanel();
        pane_scale.setLayout(null);
        pane_scale.setOpaque(false);
        pane_scale.setBounds(25, 38, 210, 80);
        pane_scale.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(216,217,218)));
        pane_area_lay.add(pane_scale);
        check_scalez = new JCheckBox();								//倒置翻转
        check_scalez.setOpaque(false);
        check_scalez.setBounds(0, 5, 20, 20);
        	check_scalez.setSelected(Boolean.parseBoolean(scanConfig.getScale_z()));
        pane_scale.add(check_scalez);
        JLabel label_scalez = new JLabel();
        label_scalez.setFont(gen_font);
        label_scalez.setForeground(gen_color);
        label_scalez.setText("是否在扫描时 倒置 翻转图片");
        label_scalez.setBounds(22, 5, 190, 20);
        pane_scale.add(label_scalez);
        check_scalex = new JCheckBox();								//水平翻转
        check_scalex.setOpaque(false);
        check_scalex.setBounds(0, 30, 20, 20);
        	check_scalex.setSelected(Boolean.parseBoolean(scanConfig.getScale_x()));
        pane_scale.add(check_scalex);
        JLabel label_scalex = new JLabel();
        label_scalex.setFont(gen_font);
        label_scalex.setForeground(gen_color);
        label_scalex.setText("是否在扫描时 水平 翻转图片");
        label_scalex.setBounds(22, 30, 190, 20);
        pane_scale.add(label_scalex);
        check_scaley = new JCheckBox();								//垂直翻转
        check_scaley.setOpaque(false);
        check_scaley.setBounds(0, 55, 20, 20);
        	check_scaley.setSelected(Boolean.parseBoolean(scanConfig.getScale_y()));
        pane_scale.add(check_scaley);
        JLabel label_scaley = new JLabel();
        label_scaley.setFont(gen_font);
        label_scaley.setForeground(gen_color);
        label_scaley.setText("是否在扫描时 垂直 翻转图片");
        label_scaley.setBounds(22, 55, 190, 20);
        pane_scale.add(label_scaley);
        
        JPanel pane_right = new JPanel();							//是否为右脚
        pane_right.setLayout(null);
        pane_right.setOpaque(false);
        pane_right.setBounds(25, 118, 210, 30);
        pane_right.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(216,217,218)));
        pane_area_lay.add(pane_right);
        
        check_right = new JCheckBox();
        check_right.setOpaque(false);
        check_right.setBounds(0, 5, 20, 20);
        	check_right.setSelected(Boolean.parseBoolean(scanConfig.getUse_right()));
        pane_right.add(check_right);
        JLabel label_right = new JLabel();
        label_right.setFont(gen_font);
        label_right.setForeground(gen_color);
        label_right.setText("是否当前扫描图片对应：右脚");
        label_right.setBounds(22, 5, 190, 20);
        pane_right.add(label_right);
    }
    
    public void initRightPanel(){
    	rightPanel.setLayout(new BorderLayout(10,10));
    	rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.WHITE));
    	rightPanel.setOpaque(false);
		BackInfoPane pane_button_lay = new BackInfoPane();			//创建底层按钮面版：获取、分析、保存、返回
	    pane_button_lay.setFillEndColor(new Color(234,235,236));
	    pane_button_lay.setDrawStartColor(new Color(198,199,200));
	    pane_button_lay.setDrawEndColor(new Color(159,160,161));
	    pane_button_lay.setDiffer_y(4);
	    pane_button_lay.setLayout(new BorderLayout(0,4));
	    JPanel pane_button = new JPanel();
	    pane_button.setOpaque(false);
	    pane_button.setLayout(new FlowLayout(FlowLayout.CENTER,8,5));
	    pane_button_lay.add(pane_button,BorderLayout.CENTER);
	    pane_button_lay.add(new JLabel(),BorderLayout.SOUTH);
	    button_get = new JButton("获取校正图片");
	    button_get.setFont(gen_font12);
	    button_get.setForeground(gen_color);
	    button_get.setPreferredSize(new Dimension(110,23));
	    button_save = new JButton("保存");
	    button_save.setFont(gen_font12);
	    button_save.setForeground(gen_color);
	    button_save.setPreferredSize(new Dimension(56,23));
	    button_back = new JButton("返回");
	    button_back.setFont(gen_font12);
	    button_back.setForeground(gen_color);
	    button_back.setPreferredSize(new Dimension(56,23));
	    pane_button.add(button_get);
	    pane_button.add(button_save);
	    pane_button.add(button_back);
	    button_get.addActionListener(action_listener);
	    button_save.addActionListener(action_listener);
	    button_back.addActionListener(action_listener);
	    //新建校正图片面板
	    pane_image = new Foot_Adjust(text_h_measure,text_v_measure,scanConfig.getDpi());	    
	    rightPanel.add(pane_image,BorderLayout.CENTER);
		rightPanel.add(new JLabel(),BorderLayout.NORTH);
		rightPanel.add(pane_button_lay,BorderLayout.SOUTH);
		rightPanel.add(new JLabel(),BorderLayout.EAST);
		rightPanel.add(new JLabel(),BorderLayout.WEST);
    }
    
    private class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==button_close||e.getSource()==button_back){
				dialog.dispose();
			}else if(e.getSource()==button_get){
				try {													//判断是否可以进行扫描；验证扫描装置
					if(SourceManager.instance().isTwainAvailable()){
						Source[] twainSources = SourceManager.instance().getAllSources();
						if(twainSources != null && twainSources.length>0){
							String[] source_name = new String[twainSources.length];
							for(int i=0;i<source_name.length;i++){
								source_name[i] = twainSources[i].getSourceName();
							}
							boolean have = false;
							for(int i=0;i<twainSources.length;i++){
								if(scanConfig.getSource().equals(source_name[i])){
									have = true;
									break;
								}
							}
							if(!have){
								JOptionPane.showMessageDialog(dialog,"配置文件的扫描仪来源没有和现在的硬件设备相匹配！" +
										"\n请重新在系统设置中指定来源！","提示",JOptionPane.WARNING_MESSAGE);
								return;
							}
						}else{
							JOptionPane.showMessageDialog(dialog,"未找到扫描设备！","提示",JOptionPane.WARNING_MESSAGE);
							return;
						}
					}else{
						JOptionPane.showMessageDialog(dialog,"未找到扫描设备！","提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
				} catch (JTwainException e1) {
					e1.printStackTrace();
				} finally{  
					SourceManager.closeSourceManager();
				}
				//执行扫描程序
				Thread thread = new Thread(){
					public void run(){
						String scale_z = scanConfig.getScale_z();
						String scale_x = scanConfig.getScale_x();
						String scale_y = scanConfig.getScale_y();
						if(check_scalez.isSelected()){
							scanConfig.setScale_z("true");
						}else{
							scanConfig.setScale_z("false");
						}
						if(check_scalex.isSelected()){
							scanConfig.setScale_x("true");
						}else{
							scanConfig.setScale_x("false");
						}
						if(check_scaley.isSelected()){
							scanConfig.setScale_y("true");
						}else{
							scanConfig.setScale_y("false");
						}
						pane_image.scanImage();
						AcquireHelper acquire = new AcquireHelper(scanConfig);
						boolean success = acquire.startAcquireOne(System.getProperty("user.dir")+"\\picture\\scan_adjust.jpg");	
						if(success){
							pane_image.first_paint = true;
							pane_image.repaint();
						}else{
							pane_image.loadFailure();
							JOptionPane.showMessageDialog(dialog,"扫描失败，请重试！（或者检查扫描设备）！","提示",JOptionPane.WARNING_MESSAGE);
						}
						scanConfig.setScale_z(scale_z);
						scanConfig.setScale_x(scale_x);
						scanConfig.setScale_y(scale_y);
					}
				};
				thread.start();
			}else if(e.getSource()==button_save){									//保存校正参数
				String regex="^(\\d+.\\d+)|(\\d+)$";								//校验比例参数是否符合格式
				if(text_h_ave.getText().trim().equals("")){
					if(text_h_scale.getText().trim().equals("")){
						text_h_ave.setText("1.0");
					}else{
						if(text_h_scale.getText().trim().matches(regex)){
							text_h_ave.setText(text_h_scale.getText().trim());
						}else{
							JOptionPane.showMessageDialog(dialog,"水平校正统计平均值不能为空！","提示",JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
				}else{
					if(!text_h_ave.getText().trim().matches(regex)){
						JOptionPane.showMessageDialog(dialog,"水平校正参数格式不正确！","提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				scanConfig.setAve_x(text_h_ave.getText().trim());
				if(text_v_ave.getText().trim().equals("")){
					if(text_v_scale.getText().trim().equals("")){
						text_v_ave.setText("1.0");
					}else{
						if(text_v_scale.getText().trim().matches(regex)){
							text_v_ave.setText(text_v_scale.getText().trim());
						}else{
							JOptionPane.showMessageDialog(dialog,"垂直校正统计平均值不能为空！","提示",JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
				}else{
					if(!text_v_ave.getText().trim().matches(regex)){
						JOptionPane.showMessageDialog(dialog,"垂直校正参数格式不正确！","提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				scanConfig.setAve_y(text_v_ave.getText().trim());
				if(check_scan.isSelected()){
					scanConfig.setUse_scan("true");
				}else{
					scanConfig.setUse_scan("false");
				}
				if(check_scalez.isSelected()){
					scanConfig.setScale_z("true");
				}else{
					scanConfig.setScale_z("false");
				}
				if(check_scalex.isSelected()){
					scanConfig.setScale_x("true");
				}else{
					scanConfig.setScale_x("false");
				}
				if(check_scaley.isSelected()){
					scanConfig.setScale_y("true");
				}else{
					scanConfig.setScale_y("false");
				}
				if(check_right.isSelected()){
					scanConfig.setUse_right("true");
				}else{
					scanConfig.setUse_right("false");
				}
				scanConfig.saveAdjustConfig();
				JOptionPane.showMessageDialog(dialog,"保存校正参数成功。","提示",JOptionPane.INFORMATION_MESSAGE);
			}else if(e.getSource()==text_h_ave){
				String string_scale=text_h_scale.getText().trim();
				if(string_scale.equals("")){
					return;
				}
				String string_ave=text_h_ave.getText().trim();
				if(string_ave.equals("")){
					return;
				}
				String regex="^(\\d+.\\d+)|(\\d+)$";
				if(string_scale.matches(regex)&&string_ave.matches(regex)){
					Float float_scale=Float.parseFloat(string_scale);
					Float float_ave=Float.parseFloat(string_ave);
					text_h_ave.setText(df.format((float_scale+float_ave)/2));
				}
			}
			else if(e.getSource()==text_v_ave){
				String string_scale=text_v_scale.getText().trim();
				if(string_scale.equals("")){
					return;
				}
				String string_ave=text_v_ave.getText().trim();
				if(string_ave.equals("")){
					return;
				}
				String regex="^(\\d+.\\d+)|(\\d+)$";
				if(string_scale.matches(regex)&&string_ave.matches(regex)){
					Float float_scale=Float.parseFloat(string_scale);
					Float float_ave=Float.parseFloat(string_ave);
					text_v_ave.setText(df.format((float_scale+float_ave)/2));
				}
			}
		}
    }
    
  //文本框输入监听****JTextField
    private class Textvalue_listener implements DocumentListener{
    	public void changedUpdate(DocumentEvent e) {
    		changed();
		}
		public void insertUpdate(DocumentEvent e) {
			changed();
		}
		public void removeUpdate(DocumentEvent e) {
			changed();
		}
		private void changed(){
			if(text_h_real.isFocusOwner()){
				String regex="^(\\d+.\\d+)|(\\d+)$";
				String string_measure=text_h_measure.getText().trim();
				if(!string_measure.matches(regex)){
					return;					//水平测量值的数值格式有问题！
				}
				String string_real=text_h_real.getText().trim();
				if(string_real.matches(regex)){
					float float_measure=Float.parseFloat(string_measure);
					float float_real=Float.parseFloat(string_real);
					text_h_scale.setText(df.format(float_real/float_measure));
				}
			}else if(text_v_real.isFocusOwner()){
				String regex="^(\\d+.\\d+)|(\\d+)$";
				String string_measure=text_v_measure.getText().trim();
				if(!string_measure.matches(regex)){
					return;					//垂直测量值的数值格式有问题！
				}
				String string_real=text_v_real.getText().trim();
				if(string_real.matches(regex)){
					float float_measure=Float.parseFloat(string_measure);
					float float_real=Float.parseFloat(string_real);
					text_v_scale.setText(df.format(float_real/float_measure));
				}
			}
		}
	}
   
//end...Adjust_config.java
}
