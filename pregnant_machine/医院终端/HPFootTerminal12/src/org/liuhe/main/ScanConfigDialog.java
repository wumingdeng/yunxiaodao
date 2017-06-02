package org.liuhe.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.liuhe.algorithm.config.AnalyseConfig;
import org.liuhe.algorithm.config.ScanConfig;
import org.liuhe.algorithm.scan.AcquireHelper;
import org.liuhe.background.pane.BackConfigPane;
import org.liuhe.background.pane.BackInfoPane;
import org.liuhe.background.pane.BackScrollPane;
import org.liuhe.element.button.ImageButton;
import org.liuhe.foot.pane.Foot_Algo;
import org.liuhe.foot.pane.Foot_Scan;

import com.asprise.util.jtwain.JTwainException;
import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.sun.awt.AWTUtilities;

public class ScanConfigDialog {
	private JDialog dialog;							//父面板
	private ScanConfig scanConfig;					//高级扫描配置参数
	private AnalyseConfig analyseConfig;
	
	private JDialog parent_dialog;
	private BackConfigPane  main_pane;
	private JPanel topPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	private Font gen_font = new Font("微软雅黑", Font.PLAIN, 13);
	private Font gen_font12 = new Font("微软雅黑", Font.PLAIN, 12);
	private Color gen_color = new Color(60,60,60);
    private Action_listener action_listener = new Action_listener();
    private Change_listener change_listener = new Change_listener();
    private Textvalue_listener text_listener = new Textvalue_listener();
    
    private ImageButton button_close;
    private boolean draging = false; 
    private Point draggingAnchor = null;
    
    private CardLayout card;						//采用卡片布局管理图像界面
    private JPanel pane_image;
	
    private JTextField text_bright;
	private JTextField text_contrast;
	private JSlider slider_bright;
	private JSlider slider_contrast;
	
	private JSpinner spinner_top;
	private JSpinner spinner_b02;
	private JSpinner spinner_b03;
	private JSpinner spinner_bottom;
	private JSpinner spinner_left;
	private JSpinner spinner_right;
	private JTextField text_top;
	private JTextField text_b02;
	private JTextField text_b03;
	private JTextField text_bottom;
	private JTextField text_left;
	private JTextField text_right;
	
	private JSpinner spinner_erosion;
	private JSpinner spinner_dilate;
	private JTextField text_erosion;
	private JTextField text_dilate;
	private JSlider slider_radius;
	private JSlider slider_threshold;
	private JTextField text_radius;
	private JTextField text_threshold;
	
	private JCheckBox check_special; 
	
	private JTextArea text_status;
	private JButton button_get;
	private JButton button_analyse;
	private JButton button_preserve;
	private JButton button_return;
	
	private Foot_Scan foot_scan;					//显示亮度对比度和剪切域
	private Foot_Algo foot_algo;					//显示算法模拟
	boolean isAlgo = false;							//是否为算法界面，判断位。
	
	public ScanConfigDialog(JDialog dialog,ScanConfig scan_config,AnalyseConfig analyse_config){
		this.parent_dialog = dialog;
		this.scanConfig = scan_config;
		this.analyseConfig = analyse_config;
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
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		int soft_width = 850;					//宽度850为首选项
		if( (int)(scrSize.getWidth())<850){		//如果屏幕宽度小于首选宽，则取屏幕最大宽度。
			soft_width = (int)(scrSize.getWidth());
		}
		int soft_x = ((int)(scrSize.getWidth())-soft_width)/2;
		Insets inset = Toolkit.getDefaultToolkit().getScreenInsets(dialog.getGraphicsConfiguration());
		int inset_bottom=inset.bottom;
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
		final JPanel titleTopPanel = new JPanel();					//标题头面板，自定义标题面板类
		titleTopPanel.setPreferredSize(new Dimension(titleTopPanel.getWidth(),40));
		titleTopPanel.setOpaque(false);
		titleTopPanel.setLayout(new BorderLayout());
		JPanel title_west_pane = new JPanel();
		title_west_pane.setLayout(new FlowLayout(FlowLayout.LEFT,20,4));
		title_west_pane.setOpaque(false);
		JLabel title_label = new JLabel();
		title_label.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\title_scan.png"));
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
	private void initLeftPanel(){
		leftPanel.setLayout(new BorderLayout(0,0));
		leftPanel.setOpaque(false);
		JScrollPane scroll_left = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_left.setOpaque(false);								//滑动面板,设置为不出现滚动条
		scroll_left.setBorder(BorderFactory.createEmptyBorder());
		scroll_left.getViewport().setOpaque(false);
		leftPanel.add(scroll_left,BorderLayout.CENTER);
		JPanel left_pane = new JPanel();							//滑动面板中承载的放置底层组件的面板,放置到scroll_left中
		left_pane.setOpaque(false);									//左边面板设置为宽度为：280px
		left_pane.setPreferredSize(new Dimension(280,left_pane.getHeight()));
		left_pane.setLayout(new BorderLayout(0,0));
		scroll_left.setViewportView(left_pane);
		
		JPanel panel_config = new JPanel();							//放置参数配置面板：对比度亮度面板、边缘剪切面板、扫描参数配置面板
		panel_config.setLayout(new BorderLayout(0,0));
		panel_config.setOpaque(false);
		left_pane.add(panel_config,BorderLayout.NORTH);
		
		BackInfoPane pane_contrast_lay = new BackInfoPane();		//对比度、亮度放置面板的背景面板*********
		pane_contrast_lay.setFillEndColor(new Color(224,225,226));
		pane_contrast_lay.setLayout(new FlowLayout(FlowLayout.CENTER,15,8));
		pane_contrast_lay.setPreferredSize(new Dimension(280,130));
		JPanel pane_contrast = new JPanel();
		pane_contrast.setOpaque(false);
		pane_contrast.setPreferredSize(new Dimension(270-20,115));
		pane_contrast.setLayout(null);
        pane_contrast.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "外观设置", TitledBorder.LEADING, TitledBorder.TOP,
        		gen_font,new Color(78, 78, 78)));
		pane_contrast_lay.add(pane_contrast);
        panel_config.add(pane_contrast_lay,BorderLayout.NORTH);		//将基本信息面板放在左面板的北边
        JLabel label_bright = new JLabel("Brightness");				//设置亮度模块****
        label_bright.setFont(gen_font12);
		label_bright.setForeground(gen_color);
		label_bright.setBounds(15, 22, 120, 20);
		pane_contrast.add(label_bright);
		slider_bright = new JSlider();								//设置亮度滑动条
		slider_bright.setOpaque(false);
		slider_bright.setMaximum(150);								//滑动条的最大值为100/150
		slider_bright.setMinimum(-100);								//滑动条的最小值为-100
		slider_bright.setValue(0);
		slider_bright.setBounds(20, 42, 170, 20);
		slider_bright.setValue(Integer.parseInt(scanConfig.getScan_bright()));
		pane_contrast.add(slider_bright);
		text_bright = new JTextField("0");							//亮度手动输入框
		text_bright.setFont(gen_font12);
		text_bright.setForeground(gen_color);
		text_bright.setBackground(Color.WHITE);
		text_bright.setBounds(195, 40, 40, 20);
		text_bright.setText(scanConfig.getScan_bright());
		pane_contrast.add(text_bright);
		slider_bright.addChangeListener(change_listener);			//添加监听ChangeListener
		text_bright.getDocument().addDocumentListener(text_listener);//添加文本监听
		JLabel label_contrast = new JLabel("Contrast");				//设置对比度模块****
		label_contrast.setFont(gen_font12);
		label_contrast.setForeground(gen_color);
		label_contrast.setBounds(15, 67, 120, 20);
		pane_contrast.add(label_contrast);
		slider_contrast = new JSlider();							//设置对比度滑动条100/150
		slider_contrast.setOpaque(false);
		slider_contrast.setMaximum(150);
		slider_contrast.setMinimum(-100);
		slider_contrast.setValue(0);
		slider_contrast.setBounds(20, 87, 170, 20);
		slider_contrast.setValue(Integer.parseInt(scanConfig.getScan_constrast()));
		pane_contrast.add(slider_contrast);
		text_contrast = new JTextField("0");						//设置对比度文本框
		text_contrast.setFont(gen_font12);
		text_contrast.setBackground(Color.WHITE);
		text_contrast.setForeground(gen_color);
		text_contrast.setBounds(195, 85, 40, 20);
		text_contrast.setText(scanConfig.getScan_constrast());
		pane_contrast.add(text_contrast);
		slider_contrast.addChangeListener(change_listener);			//添加监听ChangeListener
		text_contrast.getDocument().addDocumentListener(text_listener);
		
		BackInfoPane pane_edge_lay = new BackInfoPane();			//边缘剪切放置面板的背景面板*********
		pane_edge_lay.setFillEndColor(new Color(222,223,224));
		pane_edge_lay.setLayout(new FlowLayout(FlowLayout.CENTER,15,8));
		pane_edge_lay.setPreferredSize(new Dimension(280,140+50));
		JPanel pane_edge = new JPanel();
		pane_edge.setOpaque(false);
		pane_edge.setPreferredSize(new Dimension(250,125+50));
		pane_edge.setLayout(null);
        pane_edge.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "边缘设置", TitledBorder.LEADING, TitledBorder.TOP,
        		gen_font,new Color(78, 78, 78)));
		pane_edge_lay.add(pane_edge);
        panel_config.add(pane_edge_lay,BorderLayout.CENTER);		//将边界面板放在左面板的中央
        
        JLabel top_label = new JLabel("Border_Top");
		top_label.setFont(gen_font12);
		top_label.setForeground(gen_color);
		top_label.setBounds(15, 22, 110, 20);
		pane_edge.add(top_label);
		SpinnerModel model_top = new SpinnerNumberModel(Integer.parseInt(scanConfig.getCut_top()), 0, 500, 1);
		spinner_top = new JSpinner();
		spinner_top.setModel(model_top);
		spinner_top.setBounds(130, 22, 105, 20);
		text_top = new JTextField("0");
		text_top.setFont(gen_font12);
		text_top.setBackground(Color.WHITE);
		text_top.setForeground(gen_color);
		text_top.setText(scanConfig.getCut_top());
		spinner_top.setEditor(text_top);
		spinner_top.addChangeListener(change_listener);
		text_top.getDocument().addDocumentListener(text_listener);
		pane_edge.add(spinner_top);
		
		JLabel b02_label = new JLabel("Border_B#02");
		b02_label.setFont(gen_font12);
		b02_label.setForeground(gen_color);
		b02_label.setBounds(15, 47, 110, 20);
		pane_edge.add(b02_label);
		SpinnerModel model_b02 = new SpinnerNumberModel(Integer.parseInt(scanConfig.getCut_b02()), 0, 500, 1);
		spinner_b02 = new JSpinner();	
		spinner_b02.setModel(model_b02);
		spinner_b02.setBounds(130, 47, 105, 20);
		text_b02 = new JTextField("0");
		text_b02.setFont(gen_font12);
		text_b02.setBackground(Color.WHITE);
		text_b02.setForeground(gen_color);
		text_b02.setText(scanConfig.getCut_b02());
		spinner_b02.setEditor(text_b02);
		spinner_b02.addChangeListener(change_listener);
		text_b02.getDocument().addDocumentListener(text_listener);
		pane_edge.add(spinner_b02);
		
		JLabel b03_label = new JLabel("Border_B#03");
		b03_label.setFont(gen_font12);
		b03_label.setForeground(gen_color);
		b03_label.setBounds(15, 72, 110, 20);
		pane_edge.add(b03_label);
		SpinnerModel model_b03 = new SpinnerNumberModel(Integer.parseInt(scanConfig.getCut_b03()), 0, 500, 1);
		spinner_b03 = new JSpinner();	
		spinner_b03.setModel(model_b03);
		spinner_b03.setBounds(130, 72, 105, 20);
		text_b03 = new JTextField("0");
		text_b03.setFont(gen_font12);
		text_b03.setBackground(Color.WHITE);
		text_b03.setForeground(gen_color);
		text_b03.setText(scanConfig.getCut_b03());
		spinner_b03.setEditor(text_b03);
		spinner_b03.addChangeListener(change_listener);
		text_b03.getDocument().addDocumentListener(text_listener);
		pane_edge.add(spinner_b03);
		
		JLabel bottom_label = new JLabel("Border_Bottom");
		bottom_label.setFont(gen_font12);
		bottom_label.setForeground(gen_color);
		bottom_label.setBounds(15, 47+50, 110, 20);
		pane_edge.add(bottom_label);
		SpinnerModel model_bottom = new SpinnerNumberModel(Integer.parseInt(scanConfig.getCut_bottom()), 0, 500, 1);
		spinner_bottom = new JSpinner();
		spinner_bottom.setModel(model_bottom);
		spinner_bottom.setBounds(130, 47+50, 105, 20);
		text_bottom = new JTextField("0");
		text_bottom.setFont(gen_font12);
		text_bottom.setForeground(gen_color);
		text_bottom.setBackground(Color.WHITE);
		text_bottom.setText(scanConfig.getCut_bottom());
		spinner_bottom.setEditor(text_bottom);
		spinner_bottom.addChangeListener(change_listener);
		text_bottom.getDocument().addDocumentListener(text_listener);
		pane_edge.add(spinner_bottom);
        
		JLabel left_label = new JLabel("Border_Left");
		left_label.setFont(gen_font12);
		left_label.setForeground(gen_color);
		left_label.setBounds(15, 72+50, 110, 20);
		pane_edge.add(left_label);
		SpinnerModel model_left = new SpinnerNumberModel(Integer.parseInt(scanConfig.getCut_left()), 0, 500, 1);
		spinner_left = new JSpinner();
		spinner_left.setModel(model_left);
		spinner_left.setBounds(130, 72+50, 105, 20);
		text_left = new JTextField("0");
		text_left.setFont(gen_font12);
		text_left.setForeground(gen_color);
		text_left.setBackground(Color.WHITE);
		text_left.setText(scanConfig.getCut_left());
		spinner_left.setEditor(text_left);
		spinner_left.addChangeListener(change_listener);
		text_left.getDocument().addDocumentListener(text_listener);
		pane_edge.add(spinner_left);
		
		JLabel right_label = new JLabel("Border_Right");
		right_label.setFont(gen_font12);
		right_label.setForeground(gen_color);
		right_label.setBounds(15, 97+50, 110, 20);
		pane_edge.add(right_label);
		SpinnerModel model_right = new SpinnerNumberModel(Integer.parseInt(scanConfig.getCut_right()), 0, 500, 1);
		spinner_right = new JSpinner();
		spinner_right.setModel(model_right);
		spinner_right.setBounds(130, 97+50, 105, 20);
		text_right = new JTextField("0");
		text_right.setFont(gen_font12);
		text_right.setForeground(gen_color);
		text_right.setBackground(Color.WHITE);
		text_right.setText(scanConfig.getCut_right());
		spinner_right.setEditor(text_right);
		spinner_right.addChangeListener(change_listener);
		text_right.getDocument().addDocumentListener(text_listener);
		pane_edge.add(spinner_right);
		
		BackInfoPane pane_para_lay = new BackInfoPane();			//计算脚型轮廓参数放置面板的背景面板*********
		pane_para_lay.setLayout(new FlowLayout(FlowLayout.CENTER,15,8));
		pane_para_lay.setPreferredSize(new Dimension(280,180+25));
		JPanel pane_para = new JPanel();
		pane_para.setOpaque(false);
		pane_para.setPreferredSize(new Dimension(250,165+25));
			pane_para.setLayout(null);
        pane_para.setBorder(new TitledBorder(new LineBorder(Color.WHITE), "参数设置", TitledBorder.LEADING, TitledBorder.TOP,
        		gen_font,new Color(78, 78, 78)));
		pane_para_lay.add(pane_para);
        panel_config.add(pane_para_lay,BorderLayout.SOUTH);			//将参数信息面板放在左面板的南边
        
        JLabel erosion_label = new JLabel("Time_Erosion");
        erosion_label.setFont(gen_font12);
        erosion_label.setForeground(gen_color);
        erosion_label.setBounds(15, 22, 110, 20);
		pane_para.add(erosion_label);
		SpinnerModel model_erosion = new SpinnerNumberModel(Integer.parseInt(analyseConfig.getTime_erosion()), 0, 10, 1);
		spinner_erosion = new JSpinner();
		spinner_erosion.setModel(model_erosion);
		spinner_erosion.setBounds(130, 22, 105, 20);
		text_erosion = new JTextField("0");
		text_erosion.setFont(gen_font12);
		text_erosion.setForeground(gen_color);
		text_erosion.setBackground(Color.WHITE);
		text_erosion.setText(analyseConfig.getTime_erosion());
		spinner_erosion.setEditor(text_erosion);
		spinner_erosion.addChangeListener(change_listener);
		text_erosion.getDocument().addDocumentListener(text_listener);
		pane_para.add(spinner_erosion);
		
		JLabel dilate_label = new JLabel("Time_Dilate");
        dilate_label.setFont(gen_font12);
        dilate_label.setForeground(gen_color);
        dilate_label.setBounds(15, 47, 110, 20);
		pane_para.add(dilate_label);
		SpinnerModel model_dilate = new SpinnerNumberModel(Integer.parseInt(analyseConfig.getTime_dilate()), 0, 10, 1);
		spinner_dilate = new JSpinner();
		spinner_dilate.setModel(model_dilate);
		spinner_dilate.setBounds(130, 47, 105, 20);
		text_dilate = new JTextField("0");
		text_dilate.setFont(gen_font12);
		text_dilate.setForeground(gen_color);
		text_dilate.setBackground(Color.WHITE);
		text_dilate.setText(analyseConfig.getTime_dilate());
		spinner_dilate.setEditor(text_dilate);
		spinner_dilate.addChangeListener(change_listener);
		text_dilate.getDocument().addDocumentListener(text_listener);
		pane_para.add(spinner_dilate);
        
		JLabel label_radius = new JLabel("Stamp_Radius");
		label_radius.setFont(gen_font12);
		label_radius.setForeground(gen_color);
		label_radius.setBounds(15, 72, 120, 20);
		pane_para.add(label_radius);
		slider_radius = new JSlider();						//设置对比度滑动条
		slider_radius.setOpaque(false);
		slider_radius.setMaximum(100);						//半径设置为：0~100（0~100；当前10）
		slider_radius.setMinimum(0);
		slider_radius.setValue(0);
		slider_radius.setBounds(20, 92, 170, 20);
		slider_radius.setValue(Integer.parseInt(analyseConfig.getStamp_radius()));
		pane_para.add(slider_radius);
		text_radius = new JTextField("0");					//设置对比度文本框
		text_radius.setFont(gen_font12);
		text_radius.setBackground(Color.WHITE);
		text_radius.setForeground(gen_color);
		text_radius.setBounds(195, 90, 40, 20);
		text_radius.setText(analyseConfig.getStamp_radius());
		pane_para.add(text_radius);
		slider_radius.addChangeListener(change_listener);
		text_radius.getDocument().addDocumentListener(text_listener);
		
		JLabel label_threshold = new JLabel("Stamp_Threshold");
		label_threshold.setFont(gen_font12);
		label_threshold.setForeground(gen_color);
		label_threshold.setBounds(15, 117, 120, 20);
		pane_para.add(label_threshold);
		slider_threshold = new JSlider();					//设置对比度滑动条
		slider_threshold.setOpaque(false);
		slider_threshold.setMaximum(100);					//阈值设置为0~100(放入参数中为0.0~1.0)
		slider_threshold.setMinimum(0);
		slider_threshold.setValue(0);
		slider_threshold.setBounds(20, 137, 170, 20);
			slider_threshold.setValue(Integer.parseInt(analyseConfig.getStamp_threshold()));
		pane_para.add(slider_threshold);
		text_threshold = new JTextField("0");				//设置对比度文本框
		text_threshold.setFont(gen_font12);
		text_threshold.setBackground(Color.WHITE);
		text_threshold.setForeground(gen_color);
		text_threshold.setBounds(195, 135, 40, 20);
		text_threshold.setText(analyseConfig.getStamp_threshold());
		pane_para.add(text_threshold);
		slider_threshold.addChangeListener(change_listener);
		text_threshold.getDocument().addDocumentListener(text_listener);
		
		check_special = new JCheckBox();					//是否应用特殊像素复选框
		check_special.setOpaque(false);
		check_special.setBounds(20, 160, 20, 20);
		check_special.setSelected(Boolean.parseBoolean(analyseConfig.getUseSpecial()));
		pane_para.add(check_special);
        JLabel label_special = new JLabel();
        label_special.setFont(gen_font12);
        label_special.setForeground(gen_color);
        label_special.setText("是否应用特殊像素滤波器");
        label_special.setBounds(47, 160, 190, 20);
        pane_para.add(label_special);
		
		JPanel pane_area_lay = new JPanel();				//中间状态输出文本域********
        pane_area_lay.setOpaque(false);
        pane_area_lay.setLayout(new BorderLayout(11,4));	//保留左右10个像素的间距
        left_pane.add(pane_area_lay,BorderLayout.CENTER);
        BackScrollPane scroll_area = new BackScrollPane();
        scroll_area.setPreferredSize(new Dimension(240,180));
        text_status = new JTextArea();
	    text_status.setFont(gen_font12);
	    text_status.setForeground(gen_color);
	    text_status.setOpaque(false);
	    text_status.setMargin(new Insets(5,5,5,5));
	    text_status.setLineWrap(true);						//激活自动换行功能
	    scroll_area.setBorder(BorderFactory.createEmptyBorder());
	    scroll_area.setOpaque(false);
		scroll_area.getViewport().setOpaque(false);
	    scroll_area.getViewport().add(text_status);
	    pane_area_lay.add(scroll_area,BorderLayout.CENTER);
	    pane_area_lay.add(new JLabel(),BorderLayout.WEST);
	    pane_area_lay.add(new JLabel(),BorderLayout.EAST);
	    pane_area_lay.add(new JLabel(),BorderLayout.NORTH);
	    pane_area_lay.add(new JLabel(),BorderLayout.SOUTH);
	}
	private void initRightPanel(){
		rightPanel.setLayout(new BorderLayout(10,10));
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.WHITE));
		rightPanel.setOpaque(false);
		BackInfoPane pane_button_lay = new BackInfoPane();	//创建底层按钮面版：获取、分析、保存、返回
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
	    button_get = new JButton("扫描导入");
	    button_get.setFont(gen_font12);
	    button_get.setForeground(gen_color);
	    button_get.setPreferredSize(new Dimension(80,23));
	    button_analyse = new JButton("分析");
	    button_analyse.setFont(gen_font12);
	    button_analyse.setForeground(gen_color);
	    button_analyse.setPreferredSize(new Dimension(56,23));
	    button_preserve = new JButton("保存");
	    button_preserve.setFont(gen_font12);
	    button_preserve.setForeground(gen_color);
	    button_preserve.setPreferredSize(new Dimension(56,23));
	    button_return = new JButton("返回");
	    button_return.setFont(gen_font12);
	    button_return.setForeground(gen_color);
	    button_return.setPreferredSize(new Dimension(56,23));
	    pane_button.add(button_get);
	    pane_button.add(button_analyse);
	    pane_button.add(button_preserve);
	    pane_button.add(button_return);
	    button_get.addActionListener(action_listener);
	    button_analyse.addActionListener(action_listener);
	    button_preserve.addActionListener(action_listener);
	    button_return.addActionListener(action_listener);
		
		pane_image = new JPanel();							//新建center扫描脚型操作面板
		pane_image.setOpaque(false);
		card = new CardLayout();							//卡片布局
        pane_image.setLayout(card);
		pane_image.setBorder(BorderFactory.createLineBorder(new Color(56,248,249)));
		rightPanel.add(pane_image,BorderLayout.CENTER);
		rightPanel.add(new JLabel(),BorderLayout.NORTH);
		rightPanel.add(pane_button_lay,BorderLayout.SOUTH);
		rightPanel.add(new JLabel(),BorderLayout.EAST);
		rightPanel.add(new JLabel(),BorderLayout.WEST);
		
		foot_scan = new Foot_Scan();
		foot_scan.setOrigAppear(slider_bright.getValue(), slider_contrast.getValue());
		foot_scan.setOrigBorder((Integer)spinner_top.getValue(),(Integer)spinner_b02.getValue(),(Integer)spinner_b03.getValue(),
				(Integer)spinner_bottom.getValue(),(Integer)spinner_left.getValue(), (Integer)spinner_right.getValue());
		pane_image.add("foot_appear",foot_scan);
		
		foot_algo = new Foot_Algo(text_status);
		pane_image.add("foot_analyse",foot_algo);
	}
	private void showAppear(){
		card.show(pane_image, "foot_appear");
		isAlgo = false;
		foot_algo.stopTimer();
	}
	//按键监听器
	private class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==button_close||e.getSource()==button_return){
				foot_algo.stopTimer();
				dialog.dispose();
			}else if(e.getSource()==button_get){
				if(isAlgo){												//切换回显示面板
					showAppear();
				}
				try {													//判断是否可以进行扫描
					if(SourceManager.instance().isTwainAvailable()){
						Source[] twainSources = SourceManager.instance().getAllSources();
						if(twainSources != null && twainSources.length>0){
							String[] source_name=new String[twainSources.length];
							for(int i=0;i<source_name.length;i++){
								source_name[i]=twainSources[i].getSourceName();
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
						foot_scan.scanImage();
						AcquireHelper acquire = new AcquireHelper(scanConfig);
						boolean success = acquire.startAcquireOne(System.getProperty("user.dir")+"\\picture\\scan_back.jpg");
						if(success){
							foot_scan.first_paint = true;
							foot_scan.setOrigBorder((Integer)spinner_top.getValue(),(Integer)spinner_b02.getValue(),(Integer)spinner_b03.getValue(),
									(Integer)spinner_bottom.getValue(),(Integer)spinner_left.getValue(), (Integer)spinner_right.getValue());
							foot_scan.repaint();
						}else{
							foot_scan.loadFailure();
							JOptionPane.showMessageDialog(dialog,"扫描失败，请重试！（或者检查扫描设备）！","提示",JOptionPane.WARNING_MESSAGE);
						}
					}
				};
				thread.start();
			}else if(e.getSource()==button_analyse){
				foot_algo.setTop((Integer)spinner_top.getValue());
				foot_algo.setBottom((Integer)spinner_b02.getValue());
				foot_algo.setLeft((Integer)spinner_left.getValue());
				foot_algo.setRight((Integer)spinner_right.getValue());
				foot_algo.setBright(slider_bright.getValue());
				foot_algo.setContrast(slider_contrast.getValue());
				foot_algo.isUseSpecial(check_special.isSelected());
				foot_algo.setErosion((Integer)spinner_erosion.getValue());
				foot_algo.setDilate((Integer)spinner_dilate.getValue());
				foot_algo.setRadius(slider_radius.getValue());
				foot_algo.setThreshold(slider_threshold.getValue());
				card.show(pane_image, "foot_analyse");
				foot_algo.first_paint = true;
				isAlgo = true;
			}else if(e.getSource()==button_preserve){
				scanConfig.setScan_bright(slider_bright.getValue()+"");
				scanConfig.setScan_constrast(slider_contrast.getValue()+"");
				scanConfig.setCut_top(spinner_top.getValue()+"");
				scanConfig.setCut_b02(spinner_b02.getValue()+"");
				scanConfig.setCut_b03(spinner_b03.getValue()+"");
				scanConfig.setCut_bottom(spinner_bottom.getValue()+"");
				scanConfig.setCut_left(spinner_left.getValue()+"");
				scanConfig.setCut_right(spinner_right.getValue()+"");
				scanConfig.saveScanHighConfig();
				analyseConfig.setUseSpecial(check_special.isSelected()?"true":"false");
				analyseConfig.setTime_erosion(spinner_erosion.getValue()+"");
				analyseConfig.setTime_dilate(spinner_dilate.getValue()+"");
				analyseConfig.setStamp_radius(slider_radius.getValue()+"");
				analyseConfig.setStamp_threshold(slider_threshold.getValue()+"");
				analyseConfig.saveAnalyseConfig();
				JOptionPane.showMessageDialog(dialog,"参数设置成功","提示",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	//数值改变监听事件*****JSpinner***JSlider
	private class Change_listener implements ChangeListener{
		public void stateChanged(ChangeEvent e) {
			if(e.getSource()==slider_bright){
				if(slider_bright.isFocusOwner()){
					if(isAlgo){
						showAppear();
					}
					text_bright.setText(slider_bright.getValue()+"");
					foot_scan.setBrightness(slider_bright.getValue());
				}
			}else if(e.getSource()==slider_contrast){
				if(slider_contrast.isFocusOwner()){
					if(isAlgo){
						showAppear();
					}
					text_contrast.setText(slider_contrast.getValue()+"");
					foot_scan.setContrast(slider_contrast.getValue());
				}
			}else if(e.getSource()==spinner_top){
				if(isAlgo){
					showAppear();
				}
				text_top.setText(((Integer) spinner_top.getValue()).intValue()+"");
				foot_scan.setTop((Integer)spinner_top.getValue());
			}else if(e.getSource()==spinner_b02){
				if(isAlgo){
					showAppear();
				}
				text_b02.setText(((Integer) spinner_b02.getValue()).intValue()+"");
				foot_scan.setB02((Integer)spinner_b02.getValue());
			}else if(e.getSource()==spinner_b03){
				if(isAlgo){
					showAppear();
				}
				text_b03.setText(((Integer) spinner_b03.getValue()).intValue()+"");
				foot_scan.setB03((Integer)spinner_b03.getValue());
			}else if(e.getSource()==spinner_bottom){
				if(isAlgo){
					showAppear();
				}
				text_bottom.setText(((Integer) spinner_bottom.getValue()).intValue()+"");
				foot_scan.setBottom((Integer)spinner_bottom.getValue());
			}else if(e.getSource()==spinner_left){
				if(isAlgo){
					showAppear();
				}
				text_left.setText(((Integer)spinner_left.getValue()).intValue()+"");
				foot_scan.setLeft((Integer)spinner_left.getValue());
			}else if(e.getSource()==spinner_right){
				if(isAlgo){
					showAppear();
				}
				text_right.setText(((Integer)spinner_right.getValue()).intValue()+"");
				foot_scan.setRight((Integer)spinner_right.getValue());
			}else if(e.getSource()==slider_radius){
				if(slider_radius.isFocusOwner()){
					text_radius.setText(slider_radius.getValue()+"");
				}
			}else if(e.getSource()==slider_threshold){
				if(slider_threshold.isFocusOwner()){
					text_threshold.setText(slider_threshold.getValue()+"");
				}
			}else if(e.getSource()==spinner_erosion){
				text_erosion.setText(spinner_erosion.getValue()+"");
			}else if(e.getSource()==spinner_dilate){
				text_dilate.setText(spinner_dilate.getValue()+"");
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
			if(text_bright.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex = "^(-?\\d{1,4})$";
				String value = text_bright.getText().trim();
				if(value!=null&&value.matches(regex)){
					int value_int = Integer.parseInt(value);
					int max_value = slider_bright.getMaximum();
					int min_value = slider_bright.getMinimum();
					if(value_int<min_value){		//取值在-100~150之间
						value_int = min_value;
					}else if(value_int>max_value){
						value_int = max_value;
					}
					slider_bright.setValue(value_int);
					foot_scan.setBrightness(slider_bright.getValue());
				}
			}else if(text_contrast.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex = "^(-?\\d{1,4})$";
				String value = text_contrast.getText().trim();
				if(value!=null&&value.matches(regex)){
					int value_int = Integer.parseInt(value);
					int max_value = slider_contrast.getMaximum();
					int min_value = slider_contrast.getMinimum();
					if(value_int<min_value){		//取值在-100~150之间
						value_int = min_value;
					}else if(value_int>max_value){
						value_int = max_value;
					}
					slider_contrast.setValue(value_int);
					foot_scan.setContrast(slider_contrast.getValue());
				}
			}else if(text_top.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex="^(\\d{1,3})$";
				String value=text_top.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_top.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_top.removeChangeListener(listeners[i]);
					}
					spinner_top.setValue(Integer.parseInt(value));
					spinner_top.addChangeListener(new Change_listener());
					foot_scan.setTop((Integer)spinner_top.getValue());
				}
			}else if(text_b02.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex="^(\\d{1,3})$";
				String value=text_b02.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_b02.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_b02.removeChangeListener(listeners[i]);
					}
					spinner_b02.setValue(Integer.parseInt(value));
					spinner_b02.addChangeListener(new Change_listener());
					foot_scan.setB02((Integer)spinner_b02.getValue());
				}
			}else if(text_b03.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex="^(\\d{1,3})$";
				String value=text_b03.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_b03.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_b03.removeChangeListener(listeners[i]);
					}
					spinner_b03.setValue(Integer.parseInt(value));
					spinner_b03.addChangeListener(new Change_listener());
					foot_scan.setB03((Integer)spinner_b03.getValue());
				}
			}else if(text_bottom.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex="^(\\d{1,3})$";
				String value=text_bottom.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_bottom.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_bottom.removeChangeListener(listeners[i]);
					}
					spinner_bottom.setValue(Integer.parseInt(value));
					spinner_bottom.addChangeListener(new Change_listener());
					foot_scan.setBottom((Integer)spinner_bottom.getValue());
				}
			}else if(text_left.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex="^(\\d{1,3})$";
				String value=text_left.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_left.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_left.removeChangeListener(listeners[i]);
					}
					spinner_left.setValue(Integer.parseInt(value));
					spinner_left.addChangeListener(new Change_listener());
					foot_scan.setLeft((Integer)spinner_left.getValue());
				}
			}else if(text_right.isFocusOwner()){
				if(isAlgo){
					showAppear();
				}
				String regex="^(\\d{1,3})$";
				String value=text_right.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_right.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_right.removeChangeListener(listeners[i]);
					}
					spinner_right.setValue(Integer.parseInt(value));
					spinner_right.addChangeListener(new Change_listener());
					foot_scan.setRight((Integer)spinner_right.getValue());
				}
			}else if(text_radius.isFocusOwner()){
				String regex="^(\\d{1,3})$";
				String value=text_radius.getText().trim();
				if(value!=null&&value.matches(regex)){
					int value_int=Integer.parseInt(value);
					if(value_int<0){		//取值在0~100之间
						value_int=0;
					}else if(value_int>100){
						value_int=100;
					}
					slider_radius.setValue(value_int);
				}
			}else if(text_threshold.isFocusOwner()){
				String regex="^(\\d{1,3})$";
				String value=text_threshold.getText().trim();
				if(value!=null&&value.matches(regex)){
					int value_int=Integer.parseInt(value);
					if(value_int<0){		//取值在0~100之间
						value_int=0;
					}else if(value_int>100){
						value_int=100;
					}
					slider_threshold.setValue(value_int);
				}
			}else if(text_erosion.isFocusOwner()){
				String regex="^(\\d{1,2})$";
				String value=text_erosion.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_erosion.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_erosion.removeChangeListener(listeners[i]);
					}
					spinner_erosion.setValue(Integer.parseInt(value));
					spinner_erosion.addChangeListener(new Change_listener());
				}
			}else if(text_dilate.isFocusOwner()){
				String regex="^(\\d{1,2})$";
				String value=text_dilate.getText().trim();
				if(!value.equals("")&&value.matches(regex)){
					ChangeListener[] listeners=spinner_dilate.getChangeListeners();
					for(int i=0;i<listeners.length;i++){
						spinner_dilate.removeChangeListener(listeners[i]);
					}
					spinner_dilate.setValue(Integer.parseInt(value));
					spinner_dilate.addChangeListener(new Change_listener());
				}
			}
		}
	}
//end...High_config.java
}
