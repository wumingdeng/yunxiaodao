package org.liuhe.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.liuhe.algorithm.config.AnalyseConfig;
import org.liuhe.algorithm.config.HWeightConfig;
import org.liuhe.algorithm.config.ScanConfig;
import org.liuhe.algorithm.config.ServerConfig;
import org.liuhe.background.pane.BackConfigPane;
import org.liuhe.background.pane.BackInfoPane;
import org.liuhe.common.util.HWeightUtil;
import org.liuhe.common.util.RegexUtil;
import org.liuhe.element.button.ImageButton;
import org.liuhe.element.combobox.SysCombobox;
import org.liuhe.element.field.RoundTextField;
import org.liuhe.element.field.SourceTextField;

import com.asprise.util.jtwain.JTwainException;
import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.sun.awt.AWTUtilities;

public class SystemSetupDialog {
	private JFrame frame;
	private JDialog dialog;
	private BackConfigPane contentPane;
	
	private Font gen_font12 = new Font("微软雅黑", Font.PLAIN, 12);
	private Color gen_color = new Color(40,40,40);
	private Action_listener action_listener = new Action_listener();
	
	private JButton button_close;
	private boolean draging = false;
    private Point draggingAnchor = null;
    
    private JList list;
    
    private JPanel right_pane = new JPanel();						//右边的面板
	private BackInfoPane right_pane_scan = new BackInfoPane();		//右边的scan面板
	private BackInfoPane right_pane_server = new BackInfoPane();	//右边的server配置面板
	private BackInfoPane right_pane_com = new BackInfoPane();		//COM口设置
	private CardLayout card;										//right_pane采用卡片布局
    
	private ScanConfig scanConfig;
	private AnalyseConfig analyseConfig;
	private ServerConfig serverConfig;
	private HWeightConfig hwConfig;
	
	private SourceTextField text_source;							//基本扫描配置
	private JTextField text_suffix;
	private JTextField text_dpi;
	private JTextField text_type;
	private JButton button_scan_save;
	private JButton button_scan_high;
	private JButton button_scan_adjust;
	
	private SysCombobox combox_com;									//身高体重串口调试
	private SysCombobox combox_bit;
	private SysCombobox combox_data;
	private SysCombobox combox_parity;
	private SysCombobox combox_stop;
	private JTextField error_weight;

	private JTextField text_weight;
	private JButton button_com_test;
	private JButton button_com_save;
	
	private RoundTextField server_name;								//服务器配置
	private JTextArea server_area;
	private JButton button_server_save;
	private JButton button_server_test;
	
	public SystemSetupDialog(JFrame frame,ScanConfig scanConfig,AnalyseConfig analyseConfig
			,ServerConfig serverConfig ,HWeightConfig hwConfig){
		this.frame = frame;
		this.scanConfig = scanConfig;
		this.serverConfig = serverConfig;
		this.analyseConfig = analyseConfig;
		this.hwConfig = hwConfig;
		frame.setEnabled(false);
	}
	
	public void create_choice_dialog(){
		dialog = new JDialog(frame);
		dialog.setUndecorated(true);
		dialog.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if(!frame.isEnabled()){
					frame.setEnabled(true);
					frame.requestFocus();
				}
			}
			public void windowClosed(WindowEvent e){
				if(!frame.isEnabled()){
					frame.setEnabled(true);
					frame.requestFocus();
				}
			}
		});
		Point frame_point = frame.getLocationOnScreen();
		int frame_x = frame_point.x;
		int frame_y = frame_point.y;
		int dialog_width = 575;
		int dialog_height = 328-57+2;
		/*Dimension frame_size = frame.getSize();//居中对齐
		int frame_width = frame_size.width;
		int frame_height = frame_size.height;
		int dialog_x=(frame_width-dialog_width)/2;
		int dialog_y=(frame_height-dialog_height)/2;
		dialog.setBounds(frame_x+dialog_x, frame_y+dialog_y, dialog_width, dialog_height);*/
		int dialog_x = frame_x+20;
		int dialog_y = frame_y+43;
		dialog.setBounds(dialog_x, dialog_y, dialog_width, dialog_height);
    	
		contentPane = new BackConfigPane();
		contentPane.setTitle_height(42);
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(1,1,5,1));
    	dialog.setContentPane(contentPane);
    	createGUI();
    	dialog.setVisible(true);
    	AWTUtilities.setWindowShape(dialog, new RoundRectangle2D.Double( 0.0D, 0.0D, dialog.getWidth(), dialog.getHeight(), 20.0D,  20.0D));
	}
	
	private void createGUI(){
		final JPanel title_pane = new JPanel();					//界面可移动的标题头面板
		title_pane.setOpaque(false);
		title_pane.setPreferredSize(new Dimension(title_pane.getWidth(),44));
		title_pane.setLayout(new BorderLayout());
		contentPane.add(title_pane,BorderLayout.NORTH);
		JPanel title_west_pane = new JPanel();
		title_west_pane.setLayout(new FlowLayout(FlowLayout.LEFT,2,6));
		title_west_pane.setOpaque(false);
		JLabel title_label = new JLabel();
		title_label.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\title_system.png"));
		title_west_pane.add(title_label);
		title_pane.add(title_west_pane,BorderLayout.WEST);
		JPanel title_east_pane = new JPanel();
		title_east_pane.setLayout(new FlowLayout(FlowLayout.LEFT,10,2));
		title_east_pane.setOpaque(false);
		button_close = new ImageButton("button_close");
		button_close.addActionListener(action_listener);
		title_east_pane.add(button_close);
		title_pane.add(title_east_pane,BorderLayout.EAST);
		title_pane.addMouseListener(new MouseAdapter(){
     		public void mousePressed(MouseEvent e) {
    			draging = true;
    			draggingAnchor = new Point(e.getX() + title_pane.getX(), e.getY() + title_pane.getY());
    		}
    		public void mouseReleased(MouseEvent e) {
    			draging = false;
    			if(dialog.getLocationOnScreen().y<0){
    				dialog.setLocation(dialog.getLocationOnScreen().x, 0);
    			}
    		}
     	});
		title_pane.addMouseMotionListener(new MouseMotionAdapter(){
     		public void mouseDragged(MouseEvent e){
     			if(draging){
     				dialog.setLocation(e.getLocationOnScreen().x - draggingAnchor.x, e.getLocationOnScreen().y - draggingAnchor.y);
     			}
     		}
     	});
		
		DefaultListModel model = new DefaultListModel();
		//String msg[]={"扫描设置","数据库设置","FTP设置","二维码设置","系统注册"};
		String msg[]={"Scan Config","HWCom Config","Server Config"};
		for(int i=0;i<msg.length;i++){
			model.addElement(msg[i]);
		}
        list = new JList(model);
        list.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        list.setForeground(gen_color);
        list.setSelectedIndex(0);
        
        BackInfoPane list_pane = new BackInfoPane();
        list_pane.setBorder(new EmptyBorder(12,18,12,18));
        list_pane.setLayout(new BorderLayout());
        list_pane.setPreferredSize(new Dimension(160,list_pane.getHeight()));
        contentPane.add(list_pane,BorderLayout.WEST);
        JScrollPane scroll_pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane.getViewport().setView(list); 
        scroll_pane.setBorder(BorderFactory.createLineBorder(new Color(180,180,180)));
        list_pane.add(scroll_pane,BorderLayout.CENTER);
		
        list.addMouseListener(new Mouse_listener());			//为列表设置鼠标单击事件
        
        card = new CardLayout();								//卡片布局
        right_pane.setLayout(card);
        right_pane.setPreferredSize(new Dimension(410,250));
        right_pane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.WHITE));
        right_pane.setOpaque(false);
        contentPane.add(right_pane,BorderLayout.CENTER);		//创建初始化的右边面板        
        
        right_pane_scan.isFillRect(false);
        right_pane_scan.setLayout(null);
        right_pane_scan.setOpaque(false);
        create_rightpane_scan();
        right_pane.add("scan", right_pane_scan);
        
        right_pane_com.isFillRect(false);
        right_pane_com.setLayout(null);
        right_pane_com.setOpaque(false);
        create_rightpane_com();
        right_pane.add("wheight", right_pane_com);
        
        right_pane_server.isFillRect(false);
        right_pane_server.setLayout(null);
        right_pane_server.setOpaque(false);
        create_rightpane_sql();
        right_pane.add("server",right_pane_server);
	}
	private void create_rightpane_scan(){
        JPanel back_pane = new JPanel();
        back_pane.setOpaque(false);
        back_pane.setBounds(10, 12, 400, 280);
        back_pane.setLayout(null);
        right_pane_scan.add(back_pane);
        JLabel label_source = new JLabel("选择来源（source）");
        label_source.setFont(gen_font12);
        label_source.setForeground(gen_color);
        label_source.setBounds(10,3,120,20);
        back_pane.add(label_source);
        
        String[] sources = null;
        try {
			if(SourceManager.instance().isTwainAvailable()){
				Source[] twainSources = SourceManager.instance().getAllSources();
				if(twainSources != null && twainSources.length>0){
					sources = new String[twainSources.length];
					for(int i=0;i<sources.length;i++){
						sources[i] = twainSources[i].getSourceName();
					}
				}
			}
		} catch (JTwainException e) {
			e.printStackTrace();
		} finally{
			try{
				SourceManager.closeSourceManager();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		text_source = new SourceTextField(dialog,scanConfig.getSource(),sources);
		text_source.setFont(gen_font12);
		text_source.setBackground(Color.WHITE);
		text_source.setForeground(gen_color);
		text_source.setEditable(false);
		text_source.setBounds(140, 3, 240, 20);
		text_source.setText(scanConfig.getSource());
        back_pane.add(text_source);
		
        JLabel label_suffix = new JLabel("存储格式（suffix）");
		label_suffix.setFont(gen_font12);
		label_suffix.setForeground(gen_color);
		label_suffix.setBounds(10, 28, 120, 20);
		back_pane.add(label_suffix);

		text_suffix = new JTextField("JPG（Joint Picture Group）");
		text_suffix.setFont(gen_font12);
		text_suffix.setBackground(Color.WHITE);
		text_suffix.setForeground(gen_color);
		text_suffix.setEditable(false);
		text_suffix.setBounds(140, 28, 240, 20);
		back_pane.add(text_suffix);
		
		JLabel label_dpi = new JLabel("分辨率（dpi）");
		label_dpi.setFont(gen_font12);
		label_dpi.setForeground(gen_color);
		label_dpi.setBounds(10, 53, 120, 20);
		back_pane.add(label_dpi);
		text_dpi = new JTextField("100");
		text_dpi.setBounds(140, 53, 240, 20);
		text_dpi.setFont(gen_font12);
		text_dpi.setBackground(Color.WHITE);
		text_dpi.setForeground(gen_color);
		text_dpi.setText(scanConfig.getDpi());
		back_pane.add(text_dpi);
		
		JLabel label_type = new JLabel("图片类型（type）");
		label_type.setFont(gen_font12);
		label_type.setForeground(gen_color);
		label_type.setBounds(10, 78, 120, 20);
		back_pane.add(label_type);
		text_type = new JTextField("colorful");
		text_type.setFont(gen_font12);
		text_type.setBackground(Color.WHITE);
		text_type.setForeground(gen_color);
		text_type.setEditable(false);
		text_type.setBounds(140, 78, 240, 20);
		back_pane.add(text_type);
		
		button_scan_adjust = new JButton("扫描校正");
		button_scan_adjust.setFont(gen_font12);
		button_scan_adjust.setForeground(gen_color);
		button_scan_adjust.setBounds(103, 106, 90, 22);
		back_pane.add(button_scan_adjust);
		button_scan_high = new JButton("高级扫描配置");
		button_scan_high.setFont(gen_font12);
		button_scan_high.setForeground(gen_color);
		button_scan_high.setBounds(202, 106, 110, 22);
		back_pane.add(button_scan_high);
        button_scan_save = new JButton("保存");
        button_scan_save.setFont(gen_font12);
        button_scan_save.setForeground(gen_color);
        button_scan_save.setBounds(321, 106, 60, 22);
        back_pane.add(button_scan_save);
		
        button_scan_save.addActionListener(action_listener);
        button_scan_high.addActionListener(action_listener);
        button_scan_adjust.addActionListener(action_listener);
	}
	private void create_rightpane_com(){
        JPanel back_pane = new JPanel();
        back_pane.setOpaque(false);
        back_pane.setBounds(10, 12, 400, 280);
        back_pane.setLayout(null);
        right_pane_com.add(back_pane);
        
        JLabel label_com = new JLabel("串口（C）");
        label_com.setFont(gen_font12);
        label_com.setForeground(gen_color);
        label_com.setBounds(10,3,120,20);
        back_pane.add(label_com);
		
		combox_com = new SysCombobox(hwConfig.getComs());
		combox_com.setFont(gen_font12);
		combox_com.setForeground(gen_color);
		combox_com.setBounds(140, 3, 240, 20);
		combox_com.setSelectedItem(hwConfig.getCom());
        back_pane.add(combox_com);
		
        JLabel label_bit = new JLabel("波特率（B）");
        label_bit.setFont(gen_font12);
        label_bit.setForeground(gen_color);
        label_bit.setBounds(10, 28, 120, 20);
		back_pane.add(label_bit);
		
		combox_bit = new SysCombobox(new String[]{"9600","7200","4800"});
		combox_bit.setFont(gen_font12);
		combox_bit.setForeground(gen_color);
		combox_bit.setBounds(140, 28, 240, 20);
		combox_bit.setSelectedItem(hwConfig.getBotelv());
		back_pane.add(combox_bit);
		
		JLabel label_data = new JLabel("数据位（D）");
		label_data.setFont(gen_font12);
		label_data.setForeground(gen_color);
		label_data.setBounds(10, 53, 120, 20);
		back_pane.add(label_data);
		
		combox_data = new SysCombobox(new String[]{"5","6","7","8"});
		combox_data.setBounds(140, 53, 240, 20);
		combox_data.setFont(gen_font12);
		combox_data.setForeground(gen_color);
		combox_data.setSelectedItem(hwConfig.getData());
		back_pane.add(combox_data);
		
		JLabel label_parity = new JLabel("奇偶校验（P）");
		label_parity.setFont(gen_font12);
		label_parity.setForeground(gen_color);
		label_parity.setBounds(10, 78, 120, 20);
		back_pane.add(label_parity);
		
		combox_parity = new SysCombobox(new String[]{"偶","奇","无","标志","空格"});
		combox_parity.setFont(gen_font12);
		combox_parity.setForeground(gen_color);
		combox_parity.setBounds(140, 78, 240, 20);
		combox_parity.setSelectedItem(hwConfig.parity(Integer.parseInt(hwConfig.getParity())));
		back_pane.add(combox_parity);
		
		JLabel label_stop = new JLabel("停止位（S）");
		label_stop.setFont(gen_font12);
		label_stop.setForeground(gen_color);
		label_stop.setBounds(10, 103, 120, 20);
		back_pane.add(label_stop);
		
		combox_stop = new SysCombobox(new String[]{"1","1.5","2"});
		combox_stop.setFont(gen_font12);
		combox_stop.setForeground(gen_color);
		combox_stop.setBounds(140, 103, 240, 20);
		combox_stop.setSelectedItem(hwConfig.stop(Integer.parseInt(hwConfig.getStop())));
		back_pane.add(combox_stop);
		
		JLabel label_error = new JLabel("误差值（E）");
		label_error.setFont(gen_font12);
		label_error.setForeground(gen_color);
		label_error.setBounds(10, 128, 120, 20);
		back_pane.add(label_error);
		
		error_weight = new JTextField(hwConfig.getWeight());
		error_weight.setFont(gen_font12);
		error_weight.setForeground(gen_color);
		error_weight.setBackground(Color.WHITE);
//		error_weight.setBorder(BorderFactory.createLineBorder(new Color(171,173,179)));
		error_weight.setMargin(new Insets(0,2,0,2));
		error_weight.setBounds(140, 128, 240, 20);
		back_pane.add(error_weight);
		
		button_com_test = new JButton("串口测试");
		button_com_test.setFont(gen_font12);
		button_com_test.setForeground(gen_color);
		button_com_test.setBounds(220, 128+25, 90, 22);
        back_pane.add(button_com_test);
        button_com_test.addActionListener(action_listener);
        
        button_com_save = new JButton("保存");
        button_com_save.setFont(gen_font12);
        button_com_save.setForeground(gen_color);
        button_com_save.setBounds(321, 128+25, 60, 22);
        back_pane.add(button_com_save);
        button_com_save.addActionListener(action_listener);
        
		text_weight = new JTextField();
		text_weight.setOpaque(false);
		text_weight.setFont(gen_font12);
		text_weight.setBorder(null);
		text_weight.setForeground(gen_color);
		text_weight.setEditable(false);
		text_weight.setBounds(10, 128+25+25, 300, 22);
		back_pane.add(text_weight);
	}
	private void create_rightpane_sql(){
		JPanel back_pane = new JPanel();
        back_pane.setOpaque(false);
        back_pane.setBounds(10, 12, 390, 230);
        back_pane.setLayout(null);
        right_pane_server.add(back_pane);
        
        JLabel label01 = new JLabel();
        label01.setForeground(gen_color);
        label01.setFont(gen_font12);
        label01.setText("填写数据库服务器连接端口，正确访问数据库服务器");
        label01.setBounds(10, 0, 380, 20);
        back_pane.add(label01);
        
        JLabel label02 = new JLabel();
        label02.setForeground(gen_color);
        label02.setFont(gen_font12);
        label02.setText("例如：http://liuhe.com/action/server/dataserver");
        label02.setBounds(10, 20, 380, 20);
        back_pane.add(label02);
        
        server_name = new RoundTextField();
        server_name.setBackground(Color.WHITE);
        server_name.setForeground(gen_color);
        server_name.setFont(gen_font12);
        server_name.setText(serverConfig.getServerUrl());
        server_name.setBounds(10,45,320+48,20);
        back_pane.add(server_name);
        
        button_server_test = new JButton("连接测试");	
		button_server_test.setFont(gen_font12);
		button_server_test.setForeground(gen_color);
		button_server_test.setBounds(220, 73, 90, 22);
        back_pane.add(button_server_test);
        button_server_test.addActionListener(action_listener);
        
        button_server_save = new JButton("保存");
		button_server_save.setFont(gen_font12);
		button_server_save.setForeground(gen_color);
		button_server_save.setBounds(321, 73, 60, 22);
        back_pane.add(button_server_save);
        button_server_save.addActionListener(action_listener);
        
        JScrollPane scrollpane = new JScrollPane();
        scrollpane.setBorder(BorderFactory.createEmptyBorder());
        scrollpane.setBounds(10, 103, 368, 90);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);
        back_pane.add(scrollpane);
        server_area = new JTextArea();
        server_area.setEditable(false);
        server_area.setFont(gen_font12);
        server_area.setForeground(gen_color);
        server_area.setOpaque(false);
        server_area.setLineWrap(true);
        scrollpane.setViewportView(server_area);
	}
	
	//鼠标事件****JList
	private class Mouse_listener implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			int select_id = list.getSelectedIndex();
	    	if(select_id==0){
	    		card.show(right_pane, "scan");
	    	}else if(select_id==1){
	    		card.show(right_pane, "wheight");
	    	}else if(select_id==2){
	    		card.show(right_pane, "server");
	    	}
	    	if(select_id!=0&&text_source.isShow){
	    		text_source.hide_pop();
	    	}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	private class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==button_close){						//退出系统设置界面
				dialog.dispose();
			}
			else if(e.getSource()==button_scan_save){				//保存基本扫描设置
				if(!RegexUtil.isNumner(text_dpi.getText().trim())){
					JOptionPane.showMessageDialog(dialog,"请输入正确格式的数字！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				scanConfig.setSource(text_source.getText());
				scanConfig.setSuffix(".jpg");
				scanConfig.setDpi(text_dpi.getText().trim());
				scanConfig.setType(text_type.getText().trim());
				scanConfig.saveScanBasicConfig();
				JOptionPane.showMessageDialog(dialog,"保存基本扫描设置成功。","提示",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(e.getSource()==button_scan_high){				//选择高级扫描配置
				ScanConfigDialog configDialog = new ScanConfigDialog(dialog,scanConfig,analyseConfig,serverConfig);
				configDialog.create_config_pane();
			}
			else if(e.getSource()==button_scan_adjust){				//选择扫描校正
				ScanAdjustDialog adjust_config = new ScanAdjustDialog(dialog,scanConfig);
				adjust_config.create_config_pane();
			}
			else if(e.getSource()==button_server_save||				//保存上传服务器配置信息
					e.getSource()==button_server_test){
				final String url = server_name.getText().trim();
				if(!RegexUtil.checkUrl(url)){
					JOptionPane.showMessageDialog(dialog,"请输入正确格式的URL地址！","提示",JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				if(e.getSource()==button_server_save){
					serverConfig.setServerUrl(url);
					serverConfig.saveServerConfig();
					JOptionPane.showMessageDialog(dialog,"保存服务器设置参数成功。","提示",JOptionPane.INFORMATION_MESSAGE);
				}else{
					Thread runner = new Thread() {
						public void run() {
							server_area.setText("");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							String result = ServerConfig.testSQLConnector(url);
							if(result==null){
								server_area.setText("测试连接失败，请重试！");
							}else{
								server_area.setText("恭喜你！\n"+result);
							}
						}
					};
					runner.start();
				}
			}
			else if(e.getSource()==button_com_test){				//串口调试
				Thread runner = new Thread() {
					public void run() {
						text_weight.setText("");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						HWeightUtil hweightUtil = new HWeightUtil((String)combox_com.getSelectedItem()
								,(String)combox_bit.getSelectedItem(),(String)combox_data.getSelectedItem()
								,hwConfig.stop((String)combox_stop.getSelectedItem())+"",hwConfig.parity((String)combox_parity.getSelectedItem())+"");
						hweightUtil.doActionPerformed();
						try {
							Thread.sleep(200);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						int times = 0;
						while(!hweightUtil.isOK()&&times<30){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							times++;
							System.out.println("****************** HWeight wait 100ms ****************");
						}
						if(times==30){
							text_weight.setText("result：null");
							return;
						}
						text_weight.setText("result："+hweightUtil.getResultStr()+" ,weight："+hweightUtil.getWeight());
					}
				};
				runner.start();
			}
			else if(e.getSource()==button_com_save){				//保存串口测试参数
				hwConfig.setCom((String)combox_com.getSelectedItem());
				hwConfig.setBotelv((String)combox_bit.getSelectedItem());
				hwConfig.setData((String)combox_data.getSelectedItem());
				hwConfig.setParity(hwConfig.parity((String)combox_parity.getSelectedItem())+"");
				hwConfig.setStop(hwConfig.stop((String)combox_stop.getSelectedItem())+"");
				String error = error_weight.getText().trim();
				if(!RegexUtil.isCFloat(error)){
					JOptionPane.showMessageDialog(dialog,"请输入正确的误差值！","提示",JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				hwConfig.setWeight(error);
				hwConfig.saveHWeightConfig();
				JOptionPane.showMessageDialog(dialog,"保存串口设置参数成功。","提示",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
//end...SystemSetupDialog.java	
}