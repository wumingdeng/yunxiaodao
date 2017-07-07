package org.liuhe.main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.liuhe.algorithm.config.AnalyseConfig;
import org.liuhe.algorithm.config.HWeightConfig;
import org.liuhe.algorithm.config.ScanConfig;
import org.liuhe.algorithm.config.ServerConfig;
import org.liuhe.algorithm.scan.AcquireHelper;
import org.liuhe.background.pane.BackFootPane;
import org.liuhe.background.pane.BackCardPane;
import org.liuhe.background.pane.BackgroundPane;
import org.liuhe.background.pane.ClinicButtonPane;
import org.liuhe.common.util.DateUtil;
import org.liuhe.common.util.FileIOUtil;
import org.liuhe.common.util.FootCalcUtil;
import org.liuhe.common.util.HWeightUtil;
import org.liuhe.common.util.PrintUtil;
import org.liuhe.common.util.RegexUtil;
import org.liuhe.database.ClinicInfo;
import org.liuhe.database.ClinicInfo_item;
import org.liuhe.database.FootImageService;
import org.liuhe.database.FootStudyInfo;
import org.liuhe.database.FootStudyService;
import org.liuhe.element.button.ClinicButton;
import org.liuhe.element.button.GeneralButton;
import org.liuhe.element.button.ImageButton;
import org.liuhe.element.button.RoundRectButton;
import org.liuhe.element.other.BeforePregnantWeightPane;
import org.liuhe.element.other.FirstActionButton;
import org.liuhe.element.other.PeriodDayPane;
import org.liuhe.element.other.StaturePane;
import org.liuhe.foot.pane.FootPane;
import org.liuhe.foot.pane.ForePane;
import org.liuhe.login.WechatQrcodePane;
import org.liuhe.weixin.qrcode.QRCodeService2;
import org.liuhe.weixin.qrcode.QRcodeInfo;
import org.liuhe.weixin.qrcode.QrcodePane;
import com.synjones.angel.ReadIDCardUtil;

/**	
	孕小岛 网络版(A3)
	功能：专科挂号系统、脚型扫描分析系统(扫描、分析、上传)
	公众号服务器端为：PHP程序（目前只供PHP）
	配置获取接口：微信token接口、扫描登录验证接口、用户信息接口、专科信息接口、图片上传接口、脚型数据上传接口
	上传方式：脚型图片和脚型数据都为HTTP接口上传
	新增：手动输入身高页面（身高页面在挂号取号后面）修改：将该页面提到末次月经页面的后面，只做一次输入
	取消注册模块
	二维码提前刷新预存
	脚型数据本地缓存定时上传
* */
public class MainJFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private BackgroundPane contentPane;
	private int center_width;
	
	private JPanel cardPane;
	private CardLayout card;
	private BackCardPane firstCardPane;			//信息首页
	private BackCardPane qrcodeCardPane;		//扫描二维码
	private BackCardPane periodCardPane;		//末次月经
	private BackCardPane statureCardPane;		//输入身高信息
	private BackCardPane beforeWeightCardPane;		//输入孕前體重信息
	private BackCardPane identityCardPane;		//身份证读取
	private BackCardPane clinicCardPane;		//专科选择
	private JPanel scanCardPane;				//数据采集页
	private BackCardPane backCardPane;			//信息提示页
	
	// 顶部面板
	private JPanel topPane;
	private GeneralButton button_setting;
	private GeneralButton button_first;
	private GeneralButton button_next;
	private ImageButton button_close;
	private ImageButton button_mini;
	private ImageButton button_max;
	private JLabel hospitalName;
	private CardLayout topCard;
	private JPanel title_center_pane;
	private JButton home;	
	
	// 首页屏保
	private JustGetWeightThread getWeightThread;
	// 後臺獲取體重綫程
	private GetWeightThread bgGetWeightThread;
	private UploadThread uploadThread;
	private GetQrcodeThread getQrcodeThread;
	private ArrayList<QRcodeInfo> qrcodeArr;
	// 二维码登录
	private WechatQrcodePane qrcodePane;
	private GeneralButton jumpQrcode;
	private QrcodeThread toQrcodeThread;
	// 末次月经面板
	private PeriodDayPane periodPane;
	private RoundRectButton periodSureButton;
	private GeneralButton jumpPeriod;
	// 身份证登录
	private GeneralButton jumpIdentity;
	private IdentityThread toCardThread;
	// 挂号面板组件
	private ClinicButtonPane clinicButPane;
	private GeneralButton clinic_first;
	private GeneralButton clinic_back;
	private GeneralButton clinic_next;
	private GeneralButton clinic_last;
	private JLabel clinic_page_label;
	private ClinicInfo clinicInfo;
	// 身高面板组件
	private StaturePane staturePane;
	// 孕前體重面板组件 modify by kael
	private BeforePregnantWeightPane beforeWeightPane;
	private RoundRectButton beforeWeightBut;
	private GeneralButton jumpBeforeWeightBut;
	private RoundRectButton statureSureBut;
	private GeneralButton jumpStature;
	// 扫描面板组件
	private BackFootPane scanLeftPane;
	private BackFootPane scanRightPane;
	private FootPane left_foot_pane;
	private FootPane right_foot_pane;
	private CardLayout centerCard;
	private JPanel centerCardPane;
	private DefaultTableModel tableModel;
	private JLabel hwLabel;
	private QrcodePane printQrcode;
	private JLabel timerLabel;
	private CardLayout buttonCard;
	private JPanel buttonCardPane;
	private RoundRectButton button_oper;
	private ScanFeetThread scanFeetThread;
	// 返回首页提示页
	private JLabel title_label;
	private JLabel clinic_label;
	private JLabel count_label;
	private BackTimerThread backThread;
	
	private boolean draging = false;
    private Point draggingAnchor = null;
	private Font gen_font14 = new Font("微软雅黑", Font.PLAIN, 14);
	private Font gen_font13 = new Font("微软雅黑", Font.PLAIN, 13);
	private Color gen_color = new Color(40,40,40);
	
	private Action_listener action_listener = new Action_listener();
	private Mouse_listener mouse_listener = new Mouse_listener();
	private AWTEvent_listener awtEvent_listener = new AWTEvent_listener();
	
	// modify by kael
	public static String scanExternProgramDir;
	// modify by kael end
	private String scanDir;
	private AcquireHelper acquire;			//扫描操作类
	private PrintUtil printUtil;			//打印工具类
	
	private FootStudyService sqlServer;		//数据库操作类
	private FootImageService ftpServer;		//图片上传操作类
	
	private ScanConfig scanConfig;			//扫描配置
	private AnalyseConfig analyseConfig;	//算法分析配置
	private FootCalcUtil calcUtil;			//脚型参数辅助工具类
	private ServerConfig serverConfig;		//服务器配置
	private HWeightConfig hwConfig;			//身高体重
	
	private FootStudyInfo studyinfo = null;
	
	private boolean isScaned = false;		//判断当前是否已经扫描成功
	private boolean isPost = false;			//判断该档案是否第一次Post出数据（第一次在符合条件下单击了打印报告）	
	private String result = "false";		//提示代码: 成功为：true 失败为：cracked(token,ticket,image),sql,ftp
	
	private String openID = null;
	private String cardID = null;
	
	public static void main(String[] args) {
		try {
			System.setProperty("awt.useSystemAAFontSettings", "on");
			System.setProperty("swing.aatext", "true");
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainJFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainJFrame() {
		initConfig();
		createFrame();
    	createMainPane();
    	setVisible(true);
    	setFrameSizeChange();
	}
	
	//监听窗体的尺寸的改变
	private void setFrameSizeChange(){
		
		this.addComponentListener(new ComponentAdapter(){        //为主面板添加窗口监听器
            @Override
            public void componentResized(ComponentEvent e)
            {
            	home.setLocation((int) (e.getComponent().getSize().getWidth()-80),-5);
                System.out.println("-----------");
            }
        });
	}
	
	private void initConfig(){
		studyinfo = new FootStudyInfo();
		scanDir = System.getProperty("user.dir")+"\\scan\\";
		// modify by kael
		scanExternProgramDir = System.getProperty("user.dir")+"\\scanExtern\\";
		// modify by kael end
		File scanDirFile = new File(scanDir);
		if(!scanDirFile.exists()){
			scanDirFile.mkdirs();
		}
		File dirFile = new File(scanDir);
		if(dirFile.exists()){
			File[] files = dirFile.listFiles();
			for(int i=0;i<files.length;i++){
				if(files[i].isFile()&&!files[i].getName().endsWith(".dat")){
					String mac_id = files[i].getName().substring(0, files[i].getName().lastIndexOf(".")-1);
					if(!new File(scanDir+mac_id+".dat").exists()){
						files[i].delete();
					}
				}
			}
		}
		// 扫描分析计算相关类
		scanConfig = new ScanConfig();
		acquire = new AcquireHelper(scanConfig);
		analyseConfig = new AnalyseConfig();
		calcUtil = new FootCalcUtil();
		
		// 服务器参数配置类
		serverConfig = new ServerConfig();
		sqlServer = new FootStudyService(serverConfig);
		ftpServer = new FootImageService(serverConfig);
		
		// 其他模块工具类
		hwConfig = new HWeightConfig();
		printUtil = new PrintUtil();
		
		// 后台线程类
		uploadThread = new UploadThread();
		uploadThread.start();
		qrcodeArr = new ArrayList<QRcodeInfo>();
		getQrcodeThread = new GetQrcodeThread();
		getQrcodeThread.start();
	}
	
	private void createFrame(){
		//setAlwaysOnTop(true);
    	setUndecorated(true);
		setIconImage((new ImageIcon(System.getProperty("user.dir")+"/picture/shortcut.ico")).getImage());
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setTitle("YXD Network V20170225");
    	
    	Toolkit tookit = Toolkit.getDefaultToolkit();
    	tookit.addAWTEventListener(awtEvent_listener, AWTEvent.KEY_EVENT_MASK);
    	Dimension scrSize = tookit.getScreenSize();
    	int width = 1024;//1280
    	if(width > scrSize.width){
    		width = scrSize.width;
    	}
    	int height = 768;//800
    	if(height > scrSize.height){
    		height = scrSize.height;
    	}
    	center_width = width*342/1280;
    	center_width = center_width-center_width%2;
    	
    	setBounds((scrSize.width-width)/2,(scrSize.height-height)/2 , width, height);
    	setMinimumSize(new Dimension(1024,680));
    	contentPane = new BackgroundPane();
    	contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    	contentPane.setLayout(new BorderLayout());
    	setContentPane(contentPane);
    	
    	
    	
    	home = new JButton();
		home.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\home.png"));
		home.setMargin(new Insets(0,0,0,0));
		home.setHideActionText(true);
		home.setBorderPainted(false);
		home.setFocusPainted(false);
		home.setContentAreaFilled(false);
		home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		home.setBorder(new BevelBorder(BevelBorder.RAISED));
		home.addActionListener(action_listener);
		home.setBounds(width-80,-5, 60, 60);
		this.add(home);
	}
	
	private void createMainPane(){
		// 界面头部面板
		initTopPane();
		contentPane.add(topPane, BorderLayout.NORTH);
		// 定义界面卡片面板：8个界面
		cardPane = new JPanel();
    	cardPane.setOpaque(false);
    	card = new CardLayout();
    	cardPane.setLayout(card);
    	contentPane.add(cardPane, BorderLayout.CENTER);
    	
		// 首页屏保
    	firstCardPane = new BackCardPane();
    	firstCardPane.setLayout(new BorderLayout());
		initFirstPane();
		cardPane.add("first", firstCardPane);
		// 登陆界面：二维码登陆
		qrcodeCardPane = new BackCardPane();
		qrcodeCardPane.setLayout(new BorderLayout());
		initQrcodePane();
		cardPane.add("qrcode", qrcodeCardPane);
		// 选择末次月经
		periodCardPane = new BackCardPane();
		periodCardPane.setLayout(new BorderLayout());
		initPeriodPane();
		cardPane.add("period", periodCardPane);
		// 身高手动输入页面
		statureCardPane = new BackCardPane();
		statureCardPane.setLayout(new BorderLayout());
		initStaturePane();
		cardPane.add("stature", statureCardPane);
		
		// 孕前體重手动输入页面
		beforeWeightCardPane = new BackCardPane();
		beforeWeightCardPane.setLayout(new BorderLayout());
		this.initBeforeWeightPane();
		cardPane.add("beforeWeight", beforeWeightCardPane);
		
		// 登陆界面：身份证验证
		identityCardPane = new BackCardPane();
		identityCardPane.setLayout(new BorderLayout());
		initIdentityPane();
		cardPane.add("identity", identityCardPane);
    	// 挂号界面
		clinicCardPane = new BackCardPane();
		clinicCardPane.setLayout(new BorderLayout());
		initClinicPane();
		cardPane.add("clinic", clinicCardPane);
		// 脚型扫描界面
    	scanCardPane = new JPanel();
    	scanCardPane.setOpaque(false);
    	scanCardPane.setLayout(new BorderLayout());
    	initScanPane();
		cardPane.add("scan", scanCardPane);
		// 返回首页提示页
		backCardPane = new BackCardPane();
		backCardPane.setLayout(new BorderLayout());
		initBackPane();
		cardPane.add("back", backCardPane);
	}
	
	private void initTopPane(){
		topPane = new JPanel();
		topPane.setOpaque(false);
		topPane.setLayout(new BorderLayout());
		topPane.setPreferredSize(new Dimension(topPane.getWidth(),38));
		
		JPanel title_west_pane = new JPanel();
		title_west_pane.setLayout(new FlowLayout(FlowLayout.LEFT,10,4));
		title_west_pane.setOpaque(false);
		topPane.add(title_west_pane,BorderLayout.WEST);
		
		JLabel space_01 = new JLabel();
		space_01.setPreferredSize(new Dimension(10,25));
		title_west_pane.add(space_01);
		
		if(serverConfig.getHospital_name() == null){
			hospitalName = new JLabel("@sujudao.com版权所有");
		}else{
			hospitalName = new JLabel("@"+serverConfig.getHospital_name());
		}
		hospitalName.setFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 22));
		hospitalName.setForeground(new Color(144,197,72));
		title_west_pane.add(hospitalName);
		
		
		
		JPanel title_east_pane = new JPanel();
		title_east_pane.setLayout(new FlowLayout(FlowLayout.LEFT,10,1));
		title_east_pane.setOpaque(false);
		topPane.add(title_east_pane,BorderLayout.EAST);
		
		
		
		JLabel card_label = new JLabel();
		card_label.setPreferredSize(new Dimension(140,30));
		//card_label.setBackground(Color.red);
		//card_label.setOpaque(true);
		title_east_pane.add(card_label);
		card_label.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					topCard.next(title_center_pane);
				}
			}
		});
		
		topCard = new CardLayout();
		title_center_pane = new JPanel();
		title_center_pane.setLayout(topCard);
		title_center_pane.setOpaque(false);
		topPane.add(title_center_pane,BorderLayout.CENTER);
		
		JPanel center_blank_pane = new JPanel();
		center_blank_pane.setOpaque(false);
		title_center_pane.add("top_blank",center_blank_pane);
		
		JPanel center_but_pane = new JPanel();
		center_but_pane.setOpaque(false);
		center_but_pane.setLayout(new FlowLayout(FlowLayout.LEFT,10,1));
		title_center_pane.add("top_button",center_but_pane);
		
		button_setting = new GeneralButton("系统设置");
		button_setting.setPreferredSize(new Dimension(70,25));
		button_setting.setDifferV(3);
		button_setting.setGenFont(gen_font14);
		button_setting.addActionListener(action_listener);
		center_but_pane.add(button_setting);
		
		JLabel space_02 = new JLabel("|");
		space_02.setFont(gen_font14);
		space_02.setHorizontalAlignment(JLabel.CENTER);
		space_02.setPreferredSize(new Dimension(10,25));
		center_but_pane.add(space_02);
		button_first = new GeneralButton("返回首页");
		button_first.setPreferredSize(new Dimension(70,25));
		button_first.setDifferV(3);
		button_first.setGenFont(gen_font14);
		button_first.addActionListener(action_listener);
		center_but_pane.add(button_first);
		
		JLabel space_03 = new JLabel("|");
		space_03.setFont(gen_font14);
		space_03.setHorizontalAlignment(JLabel.CENTER);
		space_03.setPreferredSize(new Dimension(10,25));
		center_but_pane.add(space_03);
		button_next = new GeneralButton("遍历页面");
		button_next.setPreferredSize(new Dimension(70,25));
		button_next.setDifferV(3);
		button_next.setGenFont(gen_font14);
		button_next.addActionListener(action_listener);
		center_but_pane.add(button_next);
		
		JLabel space_04 = new JLabel();
		space_04.setPreferredSize(new Dimension(20,25));
		center_but_pane.add(space_04);
		
		button_mini = new ImageButton("button_mini");
		button_mini.addActionListener(action_listener);
		center_but_pane.add(button_mini);
		button_max = new ImageButton("button_max");
		button_max.addActionListener(action_listener);
		center_but_pane.add(button_max);
		button_close = new ImageButton("button_close");
		button_close.addActionListener(action_listener);
		center_but_pane.add(button_close);
		
		topPane.addMouseListener(new MouseAdapter(){
     		public void mousePressed(MouseEvent e) {
    			draging = true;
    			draggingAnchor = new Point(e.getX() + 5, e.getY() + 5);
    		}
    		public void mouseReleased(MouseEvent e) {
    			draging = false;
    			if(getLocationOnScreen().y<0){
    				setLocation(getLocationOnScreen().x, 0);
    			}
    		}
     	});
		topPane.addMouseMotionListener(new MouseMotionAdapter(){
     		public void mouseDragged(MouseEvent e){
     			if(draging){
     				setLocation(e.getLocationOnScreen().x - draggingAnchor.x, e.getLocationOnScreen().y - draggingAnchor.y);
     			}
     		}
     	});
		contentPane.addMouseMotionListener(new MouseMotionListener(){
     		private boolean top = false;
     	    private boolean down = false;
     	    private boolean left = false;
     	    private boolean right = false;
     		public void mouseDragged(MouseEvent e) {
     			Dimension dimension = getSize();
                if(top){
                	dimension.setSize(dimension.getWidth() ,dimension.getHeight()-e.getY());
                	setSize(dimension);
                	setLocation(getLocationOnScreen().x, getLocationOnScreen().y + e.getY());
                } if(down){
                	dimension.setSize(dimension.getWidth() , e.getY());
                	setSize(dimension);
                } if(left){
                	dimension.setSize(dimension.getWidth() - e.getX() ,dimension.getHeight() );
                	setSize(dimension);
                	setLocation(getLocationOnScreen().x + e.getX(),getLocationOnScreen().y );
                } if(right){
                	dimension.setSize(e.getX(),dimension.getHeight());
                	setSize(dimension);
                }
                if(left||right){
                	int width = (getSize().width-(center_width+10))/2;
                	scanRightPane.setPreferredSize(new Dimension(width,scanRightPane.getHeight()));
         			scanLeftPane.setPreferredSize(new Dimension(width,scanLeftPane.getHeight()));
                }
			}
			public void mouseMoved(MouseEvent e) {
				if(e.getPoint().getX()<4&&e.getPoint().getY()<4){
					setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));		
	            	top = true;
	            	left = true;
	            	down = false;
	            	right = false;
	            }else if(Math.abs(e.getPoint().getX()- getSize().getWidth())<5&&e.getPoint().getY()<4){
	            	setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
     				top = true;
     				left = false;
     				down = false;
     				right = true;
     			}else if(Math.abs(e.getPoint().getY()- getSize().getHeight())<5
     					&& Math.abs(e.getPoint().getX()- getSize().getWidth())<5){
     				setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
     				top = false;
     				left = false;
     				down = true;
     				right = true;
     			}else if(Math.abs(e.getPoint().getY()- getSize().getHeight())<5&&e.getPoint().getX()<4){
     				setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
     				top = false;
     				left = true;
     				down = true;
     				right = false;
     			}else if(  e.getPoint().getY()<4 ){
     				setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
	            	top = true;
	            	right = false;
	            	down = false;
	            	left = false;
	            }else if(Math.abs(e.getPoint().getY()- getSize().getHeight())<5 ){
	            	setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
	            	down = true;
	            	left = false;
	            	top = false;
	            	right = false;
	            }else if(e.getPoint().getX()<4 ){
	            	setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
	            	left = true;
	            	top = false;
	            	right = false;
	            	down = false;
	            }else if(Math.abs(e.getPoint().getX()- getSize().getWidth())<5 ){
	            	setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
	            	right = true;
	            	down = false;
	            	left = false;
	            	top = false;
	            }else{
	            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	            	top = false;
	            	down = false;
	            	left = false;
	             	right = false;
	            }
			}
     	});
	}
	private void initFirstPane(){
		JPanel firstTopPane = new JPanel();
		firstTopPane.setOpaque(false);
		firstTopPane.setLayout(new BorderLayout(10,10));
		firstTopPane.setBorder(BorderFactory.createEmptyBorder(120, 5, 10, 40));
		firstCardPane.add(firstTopPane, BorderLayout.NORTH);
		firstCardPane.addMouseListener(mouse_listener);
		
		JPanel mainTitlePane = new JPanel();
		mainTitlePane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		mainTitlePane.setOpaque(false);
		firstTopPane.add(mainTitlePane,BorderLayout.NORTH);
		JLabel mainTitle = new JLabel("欢迎使用");
		mainTitle.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 60));
		mainTitle.setForeground(new Color(144,197,72));
		mainTitlePane.add(mainTitle);
		
		JPanel subTitlePane = new JPanel();
		subTitlePane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		subTitlePane.setOpaque(false);
		firstTopPane.add(subTitlePane,BorderLayout.CENTER);
		JLabel subTitle = new JLabel("孕小岛自助终端系统");
		subTitle.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 60));
		subTitle.setForeground(new Color(144,197,72));
		subTitlePane.add(subTitle);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 40));
		firstCardPane.add(buttonPane, BorderLayout.CENTER);
		FirstActionButton actionButton = new FirstActionButton();
		actionButton.setPreferredSize(new Dimension(280,75));
		buttonPane.add(actionButton);
		
		JPanel firstFooterPane = new JPanel();
		firstFooterPane.setOpaque(false);
		firstFooterPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		firstFooterPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 20, 80));
		firstCardPane.add(firstFooterPane, BorderLayout.SOUTH);		
	}
	private void toFirstPane(){
		card.show(cardPane, "first");
	}
	private void initQrcodePane(){
		JPanel qrcodeTopPane = new JPanel();
		qrcodeTopPane.setOpaque(false);
		qrcodeTopPane.setLayout(new BorderLayout(10,20));
		qrcodeTopPane.setBorder(BorderFactory.createEmptyBorder(80, 5, 10, 5));
		qrcodeCardPane.add(qrcodeTopPane, BorderLayout.NORTH);
		
		JLabel title = new JLabel("请使用微信扫一扫二维码");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 38));
		title.setForeground(new Color(144,197,72));
		qrcodeTopPane.add(title,BorderLayout.NORTH);
		
		qrcodePane = new WechatQrcodePane();
		qrcodePane.setOpaque(false);
		qrcodePane.setPreferredSize(new Dimension(qrcodePane.getWidth(),180));
		qrcodePane.addMouseListener(mouse_listener);
		qrcodeTopPane.add(qrcodePane, BorderLayout.CENTER);
		
		JLabel sub_title = new JLabel("关注孕小岛，关爱孕妇脚健康！");
		sub_title.setHorizontalAlignment(JLabel.CENTER);
		sub_title.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 28));
		sub_title.setForeground(new Color(212,184,218));
		qrcodeTopPane.add(sub_title,BorderLayout.SOUTH);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setPreferredSize(new Dimension(buttonPane.getWidth(),60));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT,60,10));
		qrcodeCardPane.add(buttonPane, BorderLayout.SOUTH);
		
		jumpQrcode = new GeneralButton("跳 过 >>");
		jumpQrcode.setPreferredSize(new Dimension(80,25));
		jumpQrcode.setDifferV(3);
		jumpQrcode.setGenFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 18));
		jumpQrcode.addActionListener(action_listener);
		buttonPane.add(jumpQrcode);
	}
	//skip是否跳过不执行获取当前mac_id
	//从首页进来skip为false，skip为true为当二维码加载失败时重新刷新
	//网络来源：获取access_token、获取二维码、上传验证消息
	private void toQrcodePane(boolean skip){
		toQrcodeThread = new QrcodeThread(skip);
		toQrcodeThread.start();
		if(!skip){
			if(this.bgGetWeightThread!=null){
				// stop last first
				this.bgGetWeightThread.stopRequest();
			}
			this.studyinfo.setCurrentWeight("0.0");
			this.bgGetWeightThread = new GetWeightThread();
			bgGetWeightThread.start();
		}
	}
	private void initPeriodPane(){
		JPanel periodTopPane = new JPanel();
		periodTopPane.setOpaque(false);
		periodTopPane.setLayout(new BorderLayout(10,20));
		periodTopPane.setBorder(BorderFactory.createEmptyBorder(70, 5, 10, 5));
		periodCardPane.add(periodTopPane, BorderLayout.NORTH);
		
		JPanel mainTitlePane = new JPanel();
		mainTitlePane.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainTitlePane.setOpaque(false);
		periodTopPane.add(mainTitlePane,BorderLayout.NORTH);
		JLabel mainTitle = new JLabel("请输入您的末次月经时间");
		mainTitle.setFont(new Font("宋体", Font.PLAIN+Font.BOLD, 38));
		mainTitle.setForeground(new Color(144,197,72));
		mainTitlePane.add(mainTitle);
		
		JPanel periodCenterPane = new JPanel();
		periodCenterPane.setOpaque(false);
		periodCenterPane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		periodTopPane.add(periodCenterPane, BorderLayout.CENTER);
		
		periodPane = new PeriodDayPane();
		periodCenterPane.add(periodPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		periodCardPane.add(buttonPane, BorderLayout.CENTER);
		periodSureButton = new RoundRectButton(" 确 定 ");
		periodSureButton.setGenFont(new Font("黑体", Font.PLAIN+Font.BOLD, 26));
		periodSureButton.setRoundAngle(50);
		periodSureButton.setPreferredSize(new Dimension(160,50));
		periodSureButton.addActionListener(action_listener);
		buttonPane.add(periodSureButton);
		
		JPanel jumpPane = new JPanel();
		jumpPane.setOpaque(false);
		jumpPane.setPreferredSize(new Dimension(jumpPane.getWidth(),60));
		jumpPane.setLayout(new FlowLayout(FlowLayout.RIGHT,60,10));
		periodCardPane.add(jumpPane, BorderLayout.SOUTH);
		jumpPeriod = new GeneralButton("跳 过 >>");
		jumpPeriod.setPreferredSize(new Dimension(80,25));
		jumpPeriod.setDifferV(3);
		jumpPeriod.setGenFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 18));
		jumpPeriod.addActionListener(action_listener);
		jumpPane.add(jumpPeriod);
	}
	private void toPeriodPane(){
		card.show(cardPane, "period");
	}
	private void initStaturePane(){
		JPanel statureTopPane = new JPanel();
		statureTopPane.setOpaque(false);
		statureTopPane.setLayout(new BorderLayout(10,20));
		statureTopPane.setBorder(BorderFactory.createEmptyBorder(70, 5, 10, 5));
		statureCardPane.add(statureTopPane, BorderLayout.NORTH);
		
		JPanel mainTitlePane = new JPanel();
		mainTitlePane.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainTitlePane.setOpaque(false);
		statureTopPane.add(mainTitlePane,BorderLayout.NORTH);
		JLabel mainTitle = new JLabel("请输入您的当前身高");
		mainTitle.setFont(new Font("宋体", Font.PLAIN+Font.BOLD, 38));
		mainTitle.setForeground(new Color(144,197,72));
		mainTitlePane.add(mainTitle);
		
		JPanel statureCenterPane = new JPanel();
		statureCenterPane.setOpaque(false);
		statureCenterPane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		statureTopPane.add(statureCenterPane, BorderLayout.CENTER);
		
		staturePane = new StaturePane();
		statureCenterPane.add(staturePane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		statureCardPane.add(buttonPane, BorderLayout.CENTER);
		
		statureSureBut = new RoundRectButton(" 确 定 ");
		statureSureBut.setGenFont(new Font("黑体", Font.PLAIN+Font.BOLD, 26));
		statureSureBut.setRoundAngle(50);
		statureSureBut.setPreferredSize(new Dimension(160,50));
		statureSureBut.addActionListener(action_listener);
		buttonPane.add(statureSureBut);
		
		JPanel jumpPane = new JPanel();
		jumpPane.setOpaque(false);
		jumpPane.setPreferredSize(new Dimension(jumpPane.getWidth(),60));
		jumpPane.setLayout(new FlowLayout(FlowLayout.RIGHT,60,10));
		statureCardPane.add(jumpPane, BorderLayout.SOUTH);
		jumpStature = new GeneralButton("跳 过 >>");
		jumpStature.setPreferredSize(new Dimension(80,25));
		jumpStature.setDifferV(3);
		jumpStature.setGenFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 18));
		jumpStature.addActionListener(action_listener);
		jumpPane.add(jumpStature);
	}
	private void toStaturePane(){
		staturePane.initStature();
		card.show(cardPane, "stature");
	}
	
	private void initBeforeWeightPane(){
		JPanel statureTopPane = new JPanel();
		statureTopPane.setOpaque(false);
		statureTopPane.setLayout(new BorderLayout(10,20));
		statureTopPane.setBorder(BorderFactory.createEmptyBorder(70, 5, 10, 5));
		this.beforeWeightCardPane.add(statureTopPane, BorderLayout.NORTH);
		
		JPanel mainTitlePane = new JPanel();
		mainTitlePane.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainTitlePane.setOpaque(false);
		statureTopPane.add(mainTitlePane,BorderLayout.NORTH);
		JLabel mainTitle = new JLabel("请输入您的孕前体重");
		mainTitle.setFont(new Font("宋体", Font.PLAIN+Font.BOLD, 38));
		mainTitle.setForeground(new Color(144,197,72));
		mainTitlePane.add(mainTitle);
		
		JPanel statureCenterPane = new JPanel();
		statureCenterPane.setOpaque(false);
		statureCenterPane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		statureTopPane.add(statureCenterPane, BorderLayout.CENTER);
		
		this.beforeWeightPane = new BeforePregnantWeightPane();
		statureCenterPane.add(beforeWeightPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		beforeWeightCardPane.add(buttonPane, BorderLayout.CENTER);
			
		beforeWeightBut = new RoundRectButton(" 确 定 ");
		beforeWeightBut.setGenFont(new Font("黑体", Font.PLAIN+Font.BOLD, 26));
		beforeWeightBut.setRoundAngle(50);
		beforeWeightBut.setPreferredSize(new Dimension(160,50));
		beforeWeightBut.addActionListener(action_listener);
		buttonPane.add(beforeWeightBut);
				
		
		JPanel jumpPane = new JPanel();
		jumpPane.setOpaque(false);
		jumpPane.setPreferredSize(new Dimension(jumpPane.getWidth(),60));
		jumpPane.setLayout(new FlowLayout(FlowLayout.RIGHT,60,10));
		beforeWeightCardPane.add(jumpPane, BorderLayout.SOUTH);
		jumpBeforeWeightBut = new GeneralButton("跳 过 >>");
		jumpBeforeWeightBut.setPreferredSize(new Dimension(80,25));
		jumpBeforeWeightBut.setDifferV(3);
		jumpBeforeWeightBut.setGenFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 18));
		jumpBeforeWeightBut.addActionListener(action_listener);
		jumpPane.add(jumpStature);
	}
	private void toBeforeWeighPane(){
		this.beforeWeightPane.initStature();
		card.show(cardPane, "beforeWeight");
	}
	
	private void initIdentityPane(){
		JPanel identityTopPane = new JPanel();
		identityTopPane.setOpaque(false);
		identityTopPane.setLayout(new BorderLayout(10,20));
		identityTopPane.setBorder(BorderFactory.createEmptyBorder(80, 5, 10, 5));
		identityCardPane.add(identityTopPane, BorderLayout.NORTH);
		
		JLabel title = new JLabel("请将身份证放在感应区上");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("宋体", Font.PLAIN+Font.BOLD, 38));
		title.setForeground(new Color(144,197,72));
		identityTopPane.add(title,BorderLayout.NORTH);
		
		JLabel identity = new JLabel();
		identity.setBackground(Color.red);
//		ImageIcon identity_image = new ImageIcon(new ImageIcon(System.getProperty("user.dir")+"\\picture\\identity.png").getImage().
//				getScaledInstance(300, 300,Image.SCALE_DEFAULT));
		ImageIcon identity_image = new ImageIcon(System.getProperty("user.dir")+"\\picture\\identitycard.png");
		identity.setIcon(identity_image);
		identity.setHorizontalAlignment(JLabel.CENTER);							
		identityTopPane.add(identity, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setPreferredSize(new Dimension(buttonPane.getWidth(),60));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT,60,10));
		identityCardPane.add(buttonPane, BorderLayout.SOUTH);
		
		jumpIdentity = new GeneralButton("跳 过 >>");
		jumpIdentity.setPreferredSize(new Dimension(80,25));
		jumpIdentity.setDifferV(3);
		jumpIdentity.setGenFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 18));
		jumpIdentity.addActionListener(action_listener);
		buttonPane.add(jumpIdentity);
	}
	private void toIndentityPane(){
		toCardThread = new IdentityThread();
		toCardThread.start();
	}
	private void initClinicPane(){
		JPanel clinicTopPane = new JPanel();
		clinicTopPane.setOpaque(false);
		clinicTopPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		clinicTopPane.setBorder(BorderFactory.createEmptyBorder(60, 5, 0, 5));
		clinicCardPane.add(clinicTopPane, BorderLayout.NORTH);
		JLabel title = new JLabel("请选择科室/专家挂号排队");
		title.setFont(new Font("宋体", Font.PLAIN+Font.BOLD, 40));
		title.setForeground(new Color(144,197,72));
		clinicTopPane.add(title);
		
		clinicButPane = new ClinicButtonPane();
		clinicButPane.setOpaque(false);
		clinicButPane.setLayout(new FlowLayout(FlowLayout.LEFT,30,30));
		clinicButPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		clinicButPane.addMouseListener(mouse_listener);
		clinicCardPane.add(clinicButPane,BorderLayout.CENTER);
		
		JPanel clinicPagePane = new JPanel();
		clinicPagePane.setOpaque(false);
		clinicPagePane.setPreferredSize(new Dimension(clinicPagePane.getWidth(),63));
		clinicPagePane.setLayout(new FlowLayout(FlowLayout.RIGHT,2,5));
		clinicCardPane.add(clinicPagePane,BorderLayout.SOUTH);
		
		clinic_first = new GeneralButton("首页");
		clinic_first.setPreferredSize(new Dimension(40,25));
		clinic_first.setDifferV(-1);
        JLabel space01 = new JLabel("|");
        space01.setFont(gen_font13);
        space01.setForeground(gen_color);
        space01.setPreferredSize(new Dimension(10,25));
        space01.setHorizontalAlignment(JLabel.CENTER);
        space01.setVerticalAlignment(JLabel.BOTTOM);
        clinic_back = new GeneralButton("上一页");
        clinic_back.setPreferredSize(new Dimension(50,25));
        clinic_back.setDifferV(-1);
        JLabel space02 = new JLabel("|");
        space02.setFont(gen_font13);
        space02.setForeground(gen_color);
        space02.setPreferredSize(new Dimension(10,25));
        space02.setHorizontalAlignment(JLabel.CENTER);
        space02.setVerticalAlignment(JLabel.BOTTOM);
        clinic_next = new GeneralButton("下一页");
        clinic_next.setPreferredSize(new Dimension(50,25));
        clinic_next.setDifferV(-1);
        JLabel space03 = new JLabel("|");
        space03.setFont(gen_font13);
        space03.setForeground(gen_color);
        space03.setPreferredSize(new Dimension(10,25));
        space03.setHorizontalAlignment(JLabel.CENTER);
        space03.setVerticalAlignment(JLabel.BOTTOM);
        clinic_last = new GeneralButton("末页");
        clinic_last.setPreferredSize(new Dimension(40,25));
        clinic_last.setDifferV(-1);
        JLabel space04 = new JLabel();
        space04.setPreferredSize(new Dimension(20,25));
        clinicPagePane.add(clinic_first);
        clinicPagePane.add(space01);
        clinicPagePane.add(clinic_back);
        clinicPagePane.add(space02);
        clinicPagePane.add(clinic_next);
        clinicPagePane.add(space03);
        clinicPagePane.add(clinic_last);
        clinicPagePane.add(space04);
        clinic_first.addActionListener(action_listener);
        clinic_back.addActionListener(action_listener);
        clinic_next.addActionListener(action_listener);
        clinic_last.addActionListener(action_listener);
    	
    	clinic_page_label = new JLabel();
    	clinic_page_label.setFont(new Font("黑体", Font.PLAIN, 15));
    	clinic_page_label.setForeground(gen_color);
    	clinic_page_label.setPreferredSize(new Dimension(120,25));
    	clinic_page_label.setHorizontalAlignment(JLabel.CENTER);
    	clinic_page_label.setVerticalAlignment(JLabel.BOTTOM);
    	clinicPagePane.add(clinic_page_label);
    	
    	JLabel space06 = new JLabel();
        space06.setPreferredSize(new Dimension(20,30));
        clinicPagePane.add(space06);
	}
	//事件监测触发下开启线程模式,refresh是否获取失败需要重新刷新页面
	private void toClinicPane01(final boolean refresh){
		new Thread(new Runnable(){
			public void run() {
				clinicButPane.showGeting();
				clinic_page_label.setText("");
				if(refresh){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else{
					clinicButPane.removeAll();
					clinicButPane.updateUI();
					card.show(cardPane, "clinic");
				}
				clinicInfo = sqlServer.getClinicInfo(studyinfo.getOpen_id(),studyinfo.getCard_id());
				if(clinicInfo != null){
					clinicButPane.showClinic();
					List<ClinicInfo_item> list = clinicInfo.getClinic();
					for (int i = clinicInfo.startIndex(); i < clinicInfo.endIndex(); i++) {
						ClinicInfo_item info = list.get(i);
						ClinicButton clinicButton = new ClinicButton(info.getClinic_dept(),
								info.getDoctor_name(),info.getClinic_type(),info.getWait(),info.getCount_num());
						clinicButton.setPreferredSize(new Dimension(200,95));
						clinicButPane.add(clinicButton);
						clinicButton.addActionListener(action_listener);
					}
					clinicButPane.updateUI();
					clinic_page_label.setText("第"+clinicInfo.getPage_num()+"页，共"+clinicInfo.getPage_total()+"页");
				}else{
					clinicButPane.showError();
				}
			}
		}).start();
	}
	//扫描二维码或者读身份证后调取的页面
	private void toClinicPane02(){
		clinicButPane.removeAll();
		clinicButPane.updateUI();
		card.show(cardPane, "clinic");
		clinicButPane.showGeting();
		clinic_page_label.setText("");
		clinicInfo = sqlServer.getClinicInfo(studyinfo.getOpen_id(),studyinfo.getCard_id());
		if(clinicInfo != null){
			clinicButPane.showClinic();
			List<ClinicInfo_item> list = clinicInfo.getClinic();
			for (int i = clinicInfo.startIndex(); i < clinicInfo.endIndex(); i++) {
				ClinicInfo_item info = list.get(i);
				ClinicButton clinicButton = new ClinicButton(info.getClinic_dept(),
						info.getDoctor_name(),info.getClinic_type(),info.getWait(),info.getCount_num(),info.getid());
				clinicButton.setPreferredSize(new Dimension(200,95));
				clinicButPane.add(clinicButton);
				clinicButton.addActionListener(action_listener);
			}
			clinicButPane.updateUI();
			clinic_page_label.setText("第"+clinicInfo.getPage_num()+"页，共"+clinicInfo.getPage_total()+"页");
		}else{
			clinicButPane.showError();
		}
	}
	//专科列表页单击选择页按钮
	private void toClinicPane03(){
		new Thread(new Runnable(){
			public void run() {
				clinicButPane.showGeting();
				clinicButPane.removeAll();
				clinicButPane.updateUI();
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(clinicInfo != null){
					clinicButPane.showClinic();
					List<ClinicInfo_item> list = clinicInfo.getClinic();
					for (int i = clinicInfo.startIndex(); i < clinicInfo.endIndex(); i++) {
						ClinicInfo_item info = list.get(i);
						ClinicButton clinicButton = new ClinicButton(info.getClinic_dept(),
								info.getDoctor_name(),info.getClinic_type(),info.getWait(),info.getCount_num());
						clinicButton.setPreferredSize(new Dimension(200,95));
						clinicButPane.add(clinicButton);
						clinicButton.addActionListener(action_listener);
					}
					clinicButPane.updateUI();
					clinic_page_label.setText("第"+clinicInfo.getPage_num()+"页，共"+clinicInfo.getPage_total()+"页");
				}else{
					clinicButPane.showError();
				}
			}
		}).start();
	}
	private void initScanPane(){
		JPanel scanCenterPane = new JPanel();
		scanCenterPane.setOpaque(false);
		scanCenterPane.setLayout(new BorderLayout());
		scanCenterPane.setPreferredSize(new Dimension(center_width,scanCenterPane.getHeight()));
		scanCardPane.add(scanCenterPane, BorderLayout.CENTER);
		
		JLabel top_space_label = new JLabel();
		top_space_label.setOpaque(false);
		top_space_label.setPreferredSize(new Dimension(top_space_label.getWidth(),145));
		scanCenterPane.add(top_space_label,BorderLayout.NORTH);
		
		centerCardPane = new JPanel();
		centerCardPane.setOpaque(false);
		centerCard = new CardLayout();
		centerCardPane.setLayout(centerCard);
		scanCenterPane.add(centerCardPane,BorderLayout.CENTER);
		
		//注释掉forePane,就可测试print面板
		contentPane.setAlpha(0.25f);
		ForePane forePane = new ForePane();
		centerCardPane.add("fore",forePane);
		
		//打印面板（测量参数表格、挂号信息显示、二维码显示域）
		JPanel printPane = new JPanel();
		printPane.setOpaque(false);
		printPane.setLayout(new BorderLayout(0,0));
		centerCardPane.add("print",printPane);
		
		JPanel reportPane = new JPanel();
		reportPane.setOpaque(false);
		reportPane.setLayout(new BorderLayout(0,10));
		printPane.add(reportPane, BorderLayout.NORTH);
		
		JTable table = new JTable(5,3) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setForeground(gen_color);
		table.setFont(gen_font13);
		table.setRowHeight(19);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setIntercellSpacing(new Dimension(0,0));
		table.setRowSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setPreferredWidth(85);
		table.setOpaque(false);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 1L;
        	public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row,int column) {
        		Component cell = super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
        		if(column != 1){
        			setBorder(null);
        		}else{
        			setBorder(new MatteBorder(0,1,0,1,new Color(128,128,128)));
        		}
        		setHorizontalAlignment(SwingConstants.CENTER);
                return cell; 
        	}
        	public boolean isOpaque(){
        		return false;
        	}
		};
		table.setDefaultRenderer(Object.class, render);
		tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		String[] project = {"脚长","脚宽","推荐鞋码","推荐型宽","足弓态势"};
		String[] _value = {"240.21mm","80.36mm","240","1.0型基宽","高足弓态势"};
		for(int i=0;i<project.length;i++){
			tableModel.addRow(new Object[] {_value[i],project[i],_value[i]});
        }
		reportPane.add(table,BorderLayout.NORTH);
		
//		hwLabel = new JLabel("体重：70.0kg");//测试代码
		hwLabel = new JLabel();
		hwLabel.setPreferredSize(new Dimension(hwLabel.getWidth(),41));
		hwLabel.setForeground(gen_color);
		hwLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(128,128,128)));
		hwLabel.setHorizontalAlignment(JLabel.CENTER);
		hwLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		reportPane.add(hwLabel,BorderLayout.CENTER);
		
		printQrcode = new QrcodePane();
		printQrcode.setOpaque(false);
		printPane.add(printQrcode, BorderLayout.CENTER);
		//测试数据
//		printQrcode.showGeting();
		//printQrcode.showError("error:qrcode(ticket)");
		//printQrcode.showError("error:sql");
		//printQrcode.showQrcode("妇产科  李美丽  专家号", "D:\\HPFootTerminal03\\picture\\qrcode.jpg");
//		printQrcode.showClinic("妇产科;李美丽;专家号;初诊;059;12");
		//printQrcode.showClinic("妇产科;李美丽;专家号");
//		printQrcode.showUploaded();
		
		//开始检测按钮面板
		buttonCardPane = new JPanel();
		buttonCard = new CardLayout();
		buttonCardPane.setLayout(buttonCard);
		buttonCardPane.setOpaque(false);
		buttonCardPane.setPreferredSize(new Dimension(buttonCardPane.getWidth(),140));
		scanCenterPane.add(buttonCardPane,BorderLayout.SOUTH);
		
		JPanel buttonPane01 = new JPanel();
		buttonPane01.setOpaque(false);
		buttonPane01.setLayout(new FlowLayout(FlowLayout.CENTER,30,0));
		
		//timerLabel = new JLabel("15 秒后，自动返回");//测试代码
		timerLabel = new JLabel("");
		timerLabel.setPreferredSize(new Dimension(140,30));
		timerLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 3, 0));
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		timerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		timerLabel.setForeground(new Color(62,170,245));
		buttonPane01.add(timerLabel);
		
		button_oper = new RoundRectButton("开始检测");
		button_oper.setPreferredSize(new Dimension(240,80));
		button_oper.addActionListener(action_listener);
		buttonPane01.add(button_oper);
		buttonCardPane.add("pane01", buttonPane01);
		
		//左脚扫描面板
		scanLeftPane = new BackFootPane();
		scanCardPane.add(scanLeftPane, BorderLayout.WEST);
		scanLeftPane.setLayout(new BorderLayout(25,10));
		scanLeftPane.setPreferredSize(new Dimension((getSize().width-(center_width+10))/2,scanLeftPane.getHeight()));
		scanLeftPane.add(new JLabel(), BorderLayout.NORTH);
		scanLeftPane.add(new JLabel(), BorderLayout.EAST);
		scanLeftPane.add(new JLabel(), BorderLayout.WEST);
		JPanel bottom_left = new JPanel();
		bottom_left.setOpaque(false);
		bottom_left.setPreferredSize(new Dimension(bottom_left.getWidth(),10));
		scanLeftPane.add(bottom_left,BorderLayout.SOUTH);
		left_foot_pane = new FootPane("left",analyseConfig,calcUtil,tableModel);
		scanLeftPane.add(left_foot_pane, BorderLayout.CENTER);
		//右脚扫描面板
		scanRightPane = new BackFootPane();
		scanCardPane.add(scanRightPane, BorderLayout.EAST);
		scanRightPane.setLayout(new BorderLayout(25,10));
		scanRightPane.setPreferredSize(new Dimension((getSize().width-(center_width+10))/2,scanRightPane.getHeight()));
		scanRightPane.add(new JLabel(), BorderLayout.NORTH);
		scanRightPane.add(new JLabel(), BorderLayout.EAST);
		scanRightPane.add(new JLabel(), BorderLayout.WEST);
		JPanel bottom_right = new JPanel();
		bottom_right.setOpaque(false);
		bottom_right.setPreferredSize(new Dimension(bottom_right.getWidth(),10));
		scanRightPane.add(bottom_right,BorderLayout.SOUTH);
		right_foot_pane = new FootPane("right",analyseConfig,calcUtil,tableModel);
		scanRightPane.add(right_foot_pane, BorderLayout.CENTER);
	}
	//非挂号界面进来检查是否需要扫描脚型
	private void toScanPane(){
		if(studyinfo.getOpen_id()!=null||studyinfo.getCard_id()!=null){
			boolean ifNeedScan = true;
			if(!serverConfig.getInterval().equals("0")){
				ifNeedScan = sqlServer.ifNeedScan(studyinfo.getOpen_id(), studyinfo.getCard_id());
			}
			if(ifNeedScan){
				card.show(cardPane, "scan");
			}else{
				getWeightThread = new JustGetWeightThread();
				getWeightThread.start();
				toBackPane("系 统 消 息","您已在"+serverConfig.getInterval()+"天内完成过采集.","5");
			}
		}else{
			toBackPane("系 统 消 息","无身份验证！","5");
		}
	}
	private void initBackPane(){
		JPanel backTopPane = new JPanel();
		backTopPane.setOpaque(false);
		backTopPane.setLayout(new BorderLayout(10,20));
		backTopPane.setBorder(BorderFactory.createEmptyBorder(100, 330, 10, 15));
		backCardPane.add(backTopPane, BorderLayout.NORTH);
		
		title_label = new JLabel("提 示 标 题");// 您已经在专科队列中，请耐心等候！挂号成功，请等候叫号！
		title_label.setHorizontalAlignment(JLabel.LEFT);
		title_label.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 38));
		title_label.setForeground(new Color(144,197,72));
		title_label.setPreferredSize(new Dimension(title_label.getWidth(),100));
		backTopPane.add(title_label,BorderLayout.CENTER);
		
		clinic_label = new JLabel("提 示 信 息");//妇科  医生  普通号 妇科 医生 普通号 001
		clinic_label.setPreferredSize(new Dimension(clinic_label.getWidth(),100));
		clinic_label.setHorizontalAlignment(JLabel.LEFT);
		clinic_label.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 50));
		clinic_label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(144,197,72)));
		clinic_label.setForeground(new Color(144,197,72));
		backTopPane.add(clinic_label,BorderLayout.NORTH);
		
		JPanel countBackPane = new JPanel();
		countBackPane.setOpaque(false);
		countBackPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		backTopPane.add(countBackPane,BorderLayout.SOUTH);
		
		count_label = new JLabel("0");//3 0
		count_label.setHorizontalAlignment(JLabel.CENTER);
		count_label.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 40));
		count_label.setForeground(new Color(198,198,198));
		countBackPane.add(count_label);
		
		JLabel back_label = new JLabel("s后，返回首页...");
		back_label.setHorizontalAlignment(JLabel.CENTER);
		back_label.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 38));
		back_label.setForeground(new Color(198,198,198));
		countBackPane.add(back_label);
	}
	//适用于：已在队列中、挂号成功、不用采集、无身份验证
	private void toBackPane(String title,String clinic,String time){
		backThread = new BackTimerThread(title,clinic,time);
		backThread.start();
	}
	
	//末次月经面板与体重面板的初始化
	private void initPanePara(){
		beforeWeightPane.reset();
		periodPane.initPara();
	}
	
	//脚型扫描页中读秒或者单击返回初始化
	private void initPara(){
		studyinfo = new FootStudyInfo();
		
		isScaned = false;
		isPost = false;
		result = "false";
		
		openID = null;
		cardID = null;
		
		clinicInfo = null;
		
		left_foot_pane.loadImage(false);
		right_foot_pane.loadImage(false);
		left_foot_pane.synTableData(false);
		right_foot_pane.synTableData(false);
		centerCard.show(centerCardPane,"fore");
		//contentPane.setAlpha(1.0f);
		button_oper.setTitle("开始检测");
		hwLabel.setText("");
		timerLabel.setText("");
	}
	
	private class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			// 最小化、全屏、还原
			if(button == button_mini){
				setExtendedState(JFrame.ICONIFIED);
			}else if(button == button_max){
				if(getExtendedState() == JFrame.NORMAL){
					setExtendedState(JFrame.MAXIMIZED_BOTH);
				}else{
					setExtendedState(JFrame.NORMAL);
				}
				scanRightPane.setPreferredSize(new Dimension((getSize().width-(center_width+10))/2,scanRightPane.getHeight()));
				scanLeftPane.setPreferredSize(new Dimension((getSize().width-(center_width+10))/2,scanLeftPane.getHeight()));
			}
			// 退出系统
			else if(button == button_close){
				uploadThread.stopRequest();
				getQrcodeThread.stopRequest();
				if(bgGetWeightThread!=null){
					bgGetWeightThread.stopRequest();
				}
				System.exit(0);
			}
			// 弹出系统设置操作框
			else if(button == button_setting){
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						SystemSetupDialog setting = new SystemSetupDialog(MainJFrame.this,scanConfig,analyseConfig,serverConfig,hwConfig);
						setting.create_choice_dialog();
					}
				});
			}
			// 遍历下一页
			else if(button == button_next){
				card.next(cardPane);
			}
			// 返回屏保首页
			else if(button == button_first||button == home){
				if(button_oper.getTitle().equals("开始检测")||button_oper.getTitle().equals("请 重 试")){
					if(studyinfo.getHospital_no() != null){
						if(printUtil.havePrint() == null){
							toBackPane("挂号成功，请等候叫号！",studyinfo.getClinic_dept()+" "+studyinfo.getDoctor_name()+" "
									+studyinfo.getClinic_type()+" "+studyinfo.getQueue_num(),"5");
						}else{
							printUtil.setCinicInfo(studyinfo.getClinic_dept()+"  "+studyinfo.getQueue_num(), studyinfo.getWait_num());
							printUtil.setFeetPara(null);
							printUtil.setQrcode(null);
							printUtil.printpaper();
							toBackPane("挂号成功，请等候叫号！",studyinfo.getClinic_dept()+" "+studyinfo.getDoctor_name()+" "
									+studyinfo.getClinic_type()+" "+studyinfo.getQueue_num(),"5");
						}
					}else{
						if(toQrcodeThread != null){
							toQrcodeThread.stopRequest();
						}
						if(toCardThread != null){
							toCardThread.stopRequest();
						}
						if(backThread != null){
							backThread.stopRequest();
						}
						initPara();
						toFirstPane();
					}
				}
			}
			// 跳过扫描二维码
			else if(button == jumpQrcode){
				if(toQrcodeThread != null){
					toQrcodeThread.stopRequest();
				}
				toPeriodPane();
			}
			// 确认末次月经
			else if(button == periodSureButton){
				String period_str = periodPane.getDateStr();
				Date period_date = DateUtil.strToDate(period_str, "yyyy-MM-dd");
				Date now_date = new Date();
				if((period_date.getTime()+86400000)>=now_date.getTime()){
					MessageDialog option = new MessageDialog(MainJFrame.this,"输入末次月经时间大于等于当前时间！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(now_date);
				cal.add(Calendar.DAY_OF_YEAR, -315);
				Date before = cal.getTime();
				if(before.getTime()>=period_date.getTime()){
					MessageDialog option = new MessageDialog(MainJFrame.this,"请输入正确的末次月经时间！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}
				studyinfo.setDate_yunfu_str(period_str);
				int zhou = (int)(((now_date.getTime()-period_date.getTime())/(24*60*60*1000))/7);
				studyinfo.setPeriod(zhou);
				if(studyinfo.getOpen_id()==null){
					toStaturePane();
				}else{
					sqlServer.addPeriodDate(studyinfo.getOpen_id(), studyinfo.getDate_yunfu_str());
					if(studyinfo.getHeight()==null){
						toStaturePane();
					}else if(studyinfo.getWeight()==null){
						toBeforeWeighPane();
					}else if(studyinfo.getName()==null && ServerConfig.showIdPage){
						toIndentityPane();
					}else{
						if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
							toScanPane();
						}else if(serverConfig.getMachine_type().equals("2")){
//							toClinicPane01(false);
							toScanPane();
						}
					}
				}
			}
			// 跳过末次月经
			else if(button == jumpPeriod){
				if(studyinfo.getOpen_id() == null){
					toStaturePane();
				}else if(studyinfo.getHeight() == null){
					toStaturePane();
				}else if(studyinfo.getWeight() == null){
					toBeforeWeighPane();
				}else if(studyinfo.getName() == null && ServerConfig.showIdPage){
					toIndentityPane();
				}else{
					if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
						toScanPane();
					}else if(serverConfig.getMachine_type().equals("2")){
//						toClinicPane01(false);
						toScanPane();
					}
				}
			}
			// 确认身高采集
			else if(button == statureSureBut){
				String stature = staturePane.getStature();
				if(stature.equals("")){
					MessageDialog option = new MessageDialog(MainJFrame.this,"身高数值不能为空！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}
				if(!RegexUtil.isFloat(stature)){
					MessageDialog option = new MessageDialog(MainJFrame.this,"请输入正确的身高数值！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}
				studyinfo.setHeight(stature);
				if(studyinfo.getOpen_id() == null){
					toBeforeWeighPane();
				}else{
					sqlServer.addHeight(studyinfo.getOpen_id(), studyinfo.getHeight());
					if(studyinfo.getWeight()==null||studyinfo.getWeight().equals("0")||studyinfo.getWeight().equals("null")){
						toBeforeWeighPane();
					}else if(studyinfo.getName() == null && ServerConfig.showIdPage){
						toIndentityPane();
					}else{
						if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
							toScanPane();
						}else if(serverConfig.getMachine_type().equals("2")){
//							toClinicPane01(false);
							toScanPane();
						}
					}
				}
			}
			// 跳过身高采集
			else if(button == jumpStature){
				if(studyinfo.getOpen_id() == null){
					toBeforeWeighPane();
				}else if(studyinfo.getName() == null){
					toBeforeWeighPane();
				}else if(studyinfo.getWeight() == null){
					toBeforeWeighPane();
				}else{
					if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
						toScanPane();
					}else if(serverConfig.getMachine_type().equals("2")){
//						toClinicPane01(false);
						toScanPane();
					}
				}
			}
			// 确认孕前體重采集 modify by kael
			else if(button == beforeWeightBut){
				String stature = beforeWeightPane.getStature();
				boolean single = beforeWeightPane.getIsSingle();
				if(stature.equals("")){
					MessageDialog option = new MessageDialog(MainJFrame.this,"孕前体重数值不能为空！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}
				if(!RegexUtil.isFloat(stature)){
					MessageDialog option = new MessageDialog(MainJFrame.this,"请输入正确的孕前体重数值！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}
				studyinfo.setWeight(stature);
				studyinfo.setIsSingle(single);
				if(studyinfo.getOpen_id() == null && ServerConfig.showIdPage){
					toIndentityPane();
				}else{
					sqlServer.addWeight(studyinfo.getOpen_id(), studyinfo.getWeight(),single);
					if(studyinfo.getName() == null && ServerConfig.showIdPage){
						toIndentityPane();
					}else{
						if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
							toScanPane();
						}else if(serverConfig.getMachine_type().equals("2")){
//										toClinicPane01(false);
							toScanPane();
						}
					}
					beforeWeightPane.reset();
				}
			}
			// 跳过孕前體重采集 modify by kael
			else if(button == jumpBeforeWeightBut){
				if(studyinfo.getOpen_id() == null && ServerConfig.showIdPage){
					toIndentityPane();
				}else if(studyinfo.getName() == null && ServerConfig.showIdPage){
					toIndentityPane();
				}else{
					if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
						toScanPane();
					}else if(serverConfig.getMachine_type().equals("2")){
//						toClinicPane01(false);
						toScanPane();
					}
				}
			}
			// 跳过采集身份证信息
			else if(button == jumpIdentity){
				if(toCardThread != null){
					toCardThread.stopRequest();
				}
				if(serverConfig.getMachine_type().equals("0")||serverConfig.getMachine_type().equals("1")){
					toScanPane();
				}else if(serverConfig.getMachine_type().equals("2")){
//					toClinicPane01(false);
					toScanPane();
				}
			}
			// 触发扫描面板按钮
			else if(button == button_oper){
				// test print function code section
//				if(printUtil.havePrint() != null){
//					if(Integer.parseInt(serverConfig.getMachine_type()) >= 2){
//						printUtil.setCinicInfo(studyinfo.getClinic_dept()+"  "+studyinfo.getQueue_num(), studyinfo.getWait_num());
//					}else{
//						printUtil.setCinicInfo(null, null);
//					}
//					String[] para = {studyinfo.getHeight(),studyinfo.getWeight()
//							,studyinfo.getLeft_length(),studyinfo.getRight_length()
//							,studyinfo.getLeft_width(),studyinfo.getRight_width(),
//							studyinfo.getLeft_length_725(),studyinfo.getRight_length_725(),
//							studyinfo.getLeft_foot_status(),studyinfo.getRight_foot_status()};
//					printUtil.setFeetPara(para);
//					printUtil.setFeetPara(new String[]{"164.5","53.3","241.36","240.12","90.20","91.13","156.31","156.96","扁平趋势","正常足弓"});
//					if(studyinfo.getOpen_id() == null){
//						printUtil.setQrcode(qrcodePane.getImagePath());
//						qrcodeArr.remove(0);
//					}else{
//						printUtil.setQrcode(null);
//					}
//					// todo
//					printUtil.setReportParam(studyinfo.getMac_id(), studyinfo.getDate_yunfu_str(),studyinfo.isSingle());
//					printUtil.setReportFootParam("正常足弓", "任何忽视都可能造成脚部畸形。不要长时间负重或站立，保护足弓。注意营养均衡，避免体重超标，压迫足弓。多参加体育、舞蹈等活动，锻炼脚部肌肉。", "正常足弓", "任何忽视都可能造成脚部畸形。不要长时间负重或站立，保护足弓。注意营养均衡，避免体重超标，压迫足弓。多参加体育、舞蹈等活动，锻炼脚部肌肉。");
//					printUtil.printpaper();
//					boolean ok = printUtil.genReport();
//					if(ok){
//						printUtil.doPrintReportExtern(ServerConfig.reportPrinterName);
//					}
//				}
				if(!button_oper.getTitle().equals("正在检测")&&!button_oper.getTitle().equals("正在打印")){
					if(button_oper.getTitle().equals("开始检测")||button_oper.getTitle().equals("请 重 试")){
						scanFeetThread = new ScanFeetThread();
						scanFeetThread.start();
						button_oper.setEnabled(false);
					}else if(button_oper.getTitle().equals("返回首页")){
						scanFeetThread.stopRequest();
						initPara();
						toFirstPane();
					}
				}
			}
			// 专科页面首页
			else if(button == clinic_first){
				if(clinicInfo != null){
					if(!clinicInfo.isFirst()){
						clinicInfo.first();
						toClinicPane03();
					}
				}else{
					toClinicPane03();
				}
			}
			// 专科页面上一页
			else if(button == clinic_back){
				if(clinicInfo != null){
					if(clinicInfo.havePrev()){
						clinicInfo.prev();
						toClinicPane03();
					}
				}else{
					toClinicPane03();
				}
			}
			// 专科页面下一页
			else if(button == clinic_next){
				if(clinicInfo != null){
					if(clinicInfo.haveNext()){
						clinicInfo.next();
						toClinicPane03();
					}
				}else{
					toClinicPane03();
				}
			}
			// 专科页面末页
			else if(button == clinic_last){
				if(clinicInfo != null){
					if(!clinicInfo.isLast()){
						clinicInfo.last();
						toClinicPane03();
					}
				}else{
					toClinicPane03();
				}
			}
			// 触发选择专科医生按钮
			else{
				for(int i=0;i<clinicButPane.getComponentCount();i++){
					ClinicButton clinicBut = (ClinicButton) clinicButPane.getComponent(i);
					clinicBut.removeActionListener(action_listener);
				}
				
				ClinicButton clinicButton = ((ClinicButton)button);
				String clinic = clinicButton.getClinic();
				String doctor = clinicButton.getDoctor();
				String type = clinicButton.getType();
				String wait = clinicButton.getWait();
				String repeate_num = clinicButton.getRepeateNum();
				int id = clinicButton.getid();
				System.out.println("打印该专科按钮的信息： 医院："+serverConfig.getHospital_no()+" 专科："+clinic+" 医生："+doctor
						+" 号别："+type+" 等候人数："+wait+" 复诊次数："+repeate_num);

				Map<String, String> print_info = sqlServer.registration(clinic, doctor, type
						,studyinfo.getOpen_id(), studyinfo.getCard_id(), studyinfo.getName(),id);
				
				if(print_info == null){
					MessageDialog option = new MessageDialog(MainJFrame.this,"挂号失败，请重试！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					for(int i=0;i<clinicButPane.getComponentCount();i++){
						ClinicButton clinicBut = (ClinicButton) clinicButPane.getComponent(i);
						clinicBut.addActionListener(action_listener);
					}
					return;
				}
				
				if(print_info.get("errmsg").equals("队列中")){
					// modify by kael
					studyinfo.setDoctor_name(doctor);
					studyinfo.setCurrentDoctor_id(id);
					toScanPane();
					// modify by kael over
//					if(print_info.get("doctor").equals("医生")){
//						toBackPane("您已经在专科队列中，请耐心等候！",clinic+"  医生  普通号","5");
//					}else{
//						toBackPane("您已经在叫号队列中，请耐心等候！",clinic+"  "+doctor+"  "+type,"5");
//					}
//					studyinfo.setHospital_no(serverConfig.getHospital_no());
//					studyinfo.setHospital_name(serverConfig.getHospital_name());
//					studyinfo.setClinic_dept(clinic);
//					studyinfo.setDoctor_name(doctor);
//					studyinfo.setClinic_type(type);
//					studyinfo.setClinic_status(print_info.get("status"));
//					studyinfo.setQueue_num(print_info.get("queue"));
//					studyinfo.setWait_num(print_info.get("wait"));
//					card.show(cardPane, "scan");
				}else{
					if(print_info.get("status").equals("初诊")){
						//目前没有只有挂号的机器类型
						if(studyinfo.getOpen_id()!=null||studyinfo.getCard_id()!=null){
							boolean ifNeedScan = true;
							if(!serverConfig.getInterval().equals("0")){
								ifNeedScan = sqlServer.ifNeedScan(studyinfo.getOpen_id(), studyinfo.getCard_id());
							}
							if(ifNeedScan){
								studyinfo.setHospital_no(serverConfig.getHospital_no());
								studyinfo.setHospital_name(serverConfig.getHospital_name());
								studyinfo.setClinic_dept(clinic);
								studyinfo.setDoctor_name(doctor);
								studyinfo.setClinic_type(type);
								studyinfo.setClinic_status(print_info.get("status"));
								studyinfo.setQueue_num(print_info.get("queue"));
								studyinfo.setWait_num(print_info.get("wait"));
								card.show(cardPane, "scan");
								return;
							}
						}
						//1、游客直接打印挂号信息
						//2、不需要扫描脚型直接打印挂号信息
						if(printUtil.havePrint() == null){
							toBackPane("挂号成功，请等候叫号！",clinic+" "+doctor+" "+type+" "+print_info.get("queue"),"5");
						}else{
							toBackPane("挂号成功，请取票！",clinic+"  "+doctor+"  "+type,"5");
							printUtil.setCinicInfo(clinic+"  "+print_info.get("queue"), print_info.get("wait"));
							printUtil.setFeetPara(null);
							printUtil.setQrcode(null);
							printUtil.printpaper();
						}
					}else{
						toBackPane("复诊成功，请等候叫号！",clinic+" "+doctor+" "+type+" "+print_info.get("queue"),"5");
						printUtil.setCinicInfo(clinic+" "+print_info.get("queue")+"复", print_info.get("wait"));
						printUtil.setFeetPara(null);
						printUtil.setQrcode(null);
						printUtil.printpaper();
					}
				}
			}
		}
	}
	
	/**
	 * 1.首页触发获取二维码
	 * 2.二维码获取失败时触发尝试再获取
	 * 3.专科列表获取失败时触发尝试再获取
	 * */
	private class Mouse_listener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			if(!serverConfig.isAvailable()){
				serverConfig.getServerPara();
				if(!serverConfig.isAvailable()){
					MessageDialog option = new MessageDialog(MainJFrame.this,"载入服务器参数失败，请重试！","提示",MessageDialog.WARNING_MESSAGE);
					option.create_option();
					return;
				}else{
					hospitalName.setText("@"+serverConfig.getHospital_name());
				}
			}
			if(Integer.parseInt(serverConfig.getMachine_type()) > 2){
				MessageDialog option = new MessageDialog(MainJFrame.this,"机器类型为："+serverConfig.getMachine_type()+" 不支持！"
						,"提示",MessageDialog.WARNING_MESSAGE);
				option.create_option();
				return;
			}
			JPanel pane = (JPanel) e.getSource();
			if(pane == firstCardPane){
					toQrcodePane(false);
			}else if(pane == qrcodePane){
				if(qrcodePane.getStatus() == -1 && qrcodePane.getStatus() != 0 && qrcodePane.getStatus() != 1){
					if(qrcodePane.getWidth()/2-150 < e.getX() && e.getX() < qrcodePane.getWidth()/2+150){
						toQrcodePane(true);
					}
				}
			}else if(pane == clinicButPane){
				if(clinicButPane.getComponentCount() == 0 && clinicButPane.getStatus() == -1){
					toClinicPane01(true);
				}
			}
		}
	}
	
	/**
	 * 系统快捷键
	 * ctrl+enter ：退出系统
	 * ctrl+c或者ctrl+F2 ：打开配置面板
	 * ctrl+up ：上一页
	 * ctrl+down ：下一页
	 * ctrl+b ： 返回首页
	 * */
	private class AWTEvent_listener implements AWTEventListener {
        public void eventDispatched(AWTEvent event) { 
			if (event.getClass() == KeyEvent.class) {
				KeyEvent keyEvent = (KeyEvent) event;
				if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
					keyPressed(keyEvent);
				} else if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
					keyReleased(keyEvent);
				} else if (keyEvent.getID() == KeyEvent.KEY_TYPED) {
					keyTyped(keyEvent);
				}
			}
        }
        public void keyPressed(KeyEvent e) {
        	if(!MainJFrame.this.isEnabled()){
        		return;
        	}
			if(e.isControlDown()&&e.getKeyCode() == KeyEvent.VK_ENTER){
				System.exit(0);
			}else if(e.isControlDown()&&(e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_F2)){
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						SystemSetupDialog setting = new SystemSetupDialog(MainJFrame.this,scanConfig,analyseConfig,serverConfig,hwConfig);
						setting.create_choice_dialog();
					}
				});
			}else if(e.isControlDown()&&e.getKeyCode() == KeyEvent.VK_UP){
				card.previous(cardPane);
			}else if(e.isControlDown()&&e.getKeyCode() == KeyEvent.VK_DOWN){
				card.next(cardPane);
			}else if(e.isControlDown()&&(e.getKeyCode() == KeyEvent.VK_B)){
				if(button_oper.getTitle().equals("开始检测")||button_oper.getTitle().equals("请 重 试")){
					if(studyinfo.getHospital_no() != null){
						if(printUtil.havePrint() == null){
							toBackPane("挂号成功，请等候叫号！",studyinfo.getClinic_dept()+" "+studyinfo.getDoctor_name()+" "
									+studyinfo.getClinic_type()+" "+studyinfo.getQueue_num(),"5");
						}else{
							printUtil.setCinicInfo(studyinfo.getClinic_dept()+"  "+studyinfo.getQueue_num(), studyinfo.getWait_num());
							printUtil.setFeetPara(null);
							printUtil.setQrcode(null);
							printUtil.printpaper();
							toBackPane("挂号成功，请等候叫号！",studyinfo.getClinic_dept()+" "+studyinfo.getDoctor_name()+" "
									+studyinfo.getClinic_type()+" "+studyinfo.getQueue_num(),"5");
						}
					}else{
						if(toQrcodeThread != null){
							toQrcodeThread.stopRequest();
						}
						if(toCardThread != null){
							toCardThread.stopRequest();
						}
						if(backThread != null){
							backThread.stopRequest();
						}
						initPara();
						toFirstPane();
					}
				}
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
	
	/**
	 * 获取微信二维码线程
	 * */
	private class GetQrcodeThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			while ( !stopRequested ) {
				if(serverConfig.isAvailable() && qrcodeArr.size() < 3){
					System.out.println("run GetQrcodeThread time: "+System.currentTimeMillis());
					String access_token = null;
					while(access_token == null){
						access_token = QRCodeService2.getAppAccessToken(serverConfig.getWechatUrl());
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					long scene = 0;
					String ticket = null;
					while(ticket == null){
						scene = QRCodeService2.getScene();
						long scene2 = serverConfig.getHospitalScene();
						ticket = QRCodeService2.getTicket(access_token, 86400, scene2);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					String filePath = scanDir+"qrcode_"+scene+".jpg";
					String requestUrl = null;
					while(requestUrl == null){
						requestUrl = QRCodeService2.getQRCodeByTicket(ticket, filePath);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					String mac_id = serverConfig.getMac()+DateUtil.dateToStr(new Date(),"yyyyMMddHHmmss");					
					boolean verify = false;
					while(!verify){
						verify = sqlServer.addVerifyInfo(mac_id, String.valueOf(scene), ticket);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					QRcodeInfo qrcode = new QRcodeInfo();
					qrcode.setMac_id(mac_id);
					qrcode.setScene(String.valueOf(scene));
					qrcode.setTicket(ticket);
					qrcode.setFilePath(filePath);
					qrcode.setRequestUrl(requestUrl);
					qrcodeArr.add(qrcode);
				}
				if(!stopRequested){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
	
	/**
	 * 监测手机扫描二维码线程
	 * */
	private class QrcodeThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		private boolean skip;
		private int count;
		public QrcodeThread(boolean skip){
			this.skip = skip;
		} 
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			count = 0;
			if(!skip){
				card.show(cardPane, "qrcode");				
			}
			qrcodePane.showGeting();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(qrcodeArr.size() != 0){
				QRcodeInfo qrcode = qrcodeArr.get(0);
				studyinfo.setMac_id(qrcode.getMac_id());
				studyinfo.setScene(qrcode.getScene());
				studyinfo.setTicket(qrcode.getTicket());
				qrcodePane.showQrcodeImage(qrcode.getFilePath());
				while ( !stopRequested ) {
					System.out.println("Run VerifyQrcodeTask start time: "+System.currentTimeMillis());
					openID = sqlServer.isVerifyInfo(studyinfo.getTicket());//studyinfo.getMac_id());
					if(openID != null){
						qrcodeArr.remove(0);
						System.out.println(studyinfo.getScene()+"  "+qrcodeArr.size());
						studyinfo.setOpen_id(openID);
						Map<String, String> userinfo = sqlServer.getUserInfo("open_id",studyinfo.getOpen_id());
						if(userinfo != null){
							studyinfo.setUser_id(userinfo.get("id"));
							studyinfo.setCard_id(userinfo.get("card_id"));
							studyinfo.setName(userinfo.get("name"));
							studyinfo.setSex(userinfo.get("sex"));
							studyinfo.setNation(userinfo.get("nation"));
							studyinfo.setBirth_str(userinfo.get("date_birth"));
							studyinfo.setDistrict(userinfo.get("district"));
							studyinfo.setProvince(userinfo.get("province"));
							studyinfo.setCity(userinfo.get("city"));
							studyinfo.setCounty(userinfo.get("county"));
							studyinfo.setDate_yunfu_str(userinfo.get("date_yunfu"));
							studyinfo.setHeight(userinfo.get("height"));
							studyinfo.setWeight(userinfo.get("weight"));
							if(userinfo.containsKey("single")){
								studyinfo.setIsSingle(userinfo.get("single").equals("1"));
							}else{
								studyinfo.setIsSingle(true);
							}
							left_foot_pane.setPredictData(userinfo.get("left_length"), userinfo.get("left_width"));
							right_foot_pane.setPredictData(userinfo.get("right_length"), userinfo.get("right_width"));
						}
						System.out.println("openid:"+studyinfo.getOpen_id()+" ,yunfu_date:"+studyinfo.getDate_yunfu_str());
						if(studyinfo.getBirth_str() != null && !studyinfo.getBirth_str().equals("")){
							Calendar cal = Calendar.getInstance();
							cal.setTime(new Date());
							studyinfo.setAge(cal.get(Calendar.YEAR)-Integer.parseInt(studyinfo.getBirth_str().substring(0,4)));
						}
						if(studyinfo.getDate_yunfu_str() == null || studyinfo.getDate_yunfu_str().equals("")){
							toPeriodPane();
							return;
						}
						Date period_date = DateUtil.strToDate(studyinfo.getDate_yunfu_str(), "yyyy-MM-dd");
						Date now_date = new Date();
						int zhou = (int)(((now_date.getTime()-period_date.getTime())/(24*60*60*1000))/7);
						studyinfo.setPeriod(zhou);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DAY_OF_YEAR, -280);
						Date before = cal.getTime();
						if(before.getTime() >= period_date.getTime()){
							toPeriodPane();
						}else if(userinfo.get("height") == null || userinfo.get("height").equals("") || userinfo.get("height").equals("null")){
							toStaturePane();
						}else if(userinfo.get("weight") == null || userinfo.get("weight").equals("") || userinfo.get("weight").equals("null")){
							toBeforeWeighPane();
						}else if(ServerConfig.showIdPage && userinfo.get("name") == null || userinfo.get("name").equals("") || userinfo.get("name").equals("null")){
							toIndentityPane();
						}else{
							// modify by kael
							toScanPane();
							// modify by kael over
//							if(serverConfig.getMachine_type().equals("0") || serverConfig.getMachine_type().equals("1")){
//								toScanPane();
//							}else if(serverConfig.getMachine_type().equals("2")){
//								toClinicPane02();
//							}
						}
						stopRequested = true;
					}
					if(!stopRequested){
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					count++;
					if(count > 30){
						stopRequested = true;
						toFirstPane();
					}
				}
			}else{
				qrcodePane.showError();
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
	
	/**
	 * 监测身份证读卡器刷卡线程
	 * */
	private class IdentityThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			card.show(cardPane, "identity");
			while ( !stopRequested ) {
				System.out.println("run ScanCardTask time: "+System.currentTimeMillis());
				ReadIDCardUtil cardUtil = new ReadIDCardUtil();
				Map<String, String> map = null;
				try {
					map = cardUtil.readCardInfo();
					// modify by kael
//					map = new HashMap<String,String>();
//					map.put("Name", "大名");
//					map.put("Sex", "女");
//					map.put("Nation", "俄羅斯");
//					map.put("Born", "1990-3-20");
//					map.put("Address", "大名水水");
//					map.put("errcode", "0");
//					map.put("IDCardNo","33392023920");
					// modify by kael overs
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("map is null !");
				}
				cardUtil = null;
				if(map != null){
					String errcode = map.get("errcode");
					if(errcode.equals("0")){
						cardID = map.get("IDCardNo");
						if(cardID != null){
							studyinfo.setCard_id(cardID);
							studyinfo.setName(map.get("Name"));
							studyinfo.setSex(map.get("Sex"));
							studyinfo.setNation(map.get("Nation"));
							studyinfo.setBirth_str(map.get("Born"));
							studyinfo.setAddress(map.get("Address"));
							if(studyinfo.getBirth_str() != null){
								Calendar cal = Calendar.getInstance();
								cal.setTime(new Date());
								studyinfo.setAge(cal.get(Calendar.YEAR)-Integer.parseInt(studyinfo.getBirth_str().substring(0,4)));
							}
							if(studyinfo.getOpen_id() == null){
								Map<String, String> userinfo = sqlServer.getUserInfo("card_id",studyinfo.getCard_id());
								if(userinfo != null){
									studyinfo.setUser_id(userinfo.get("id"));
									studyinfo.setOpen_id(userinfo.get("open_id"));
									studyinfo.setDistrict(userinfo.get("district"));
									studyinfo.setProvince(userinfo.get("province"));
									studyinfo.setCity(userinfo.get("city"));
									studyinfo.setCounty(userinfo.get("county"));
									studyinfo.setDate_yunfu_str(userinfo.get("date_yunfu"));
									studyinfo.setHeight(userinfo.get("height"));
									left_foot_pane.setPredictData(userinfo.get("left_length"), userinfo.get("left_width"));
									right_foot_pane.setPredictData(userinfo.get("right_length"), userinfo.get("right_width"));
								}
								if(studyinfo.getDate_yunfu_str() != null && !studyinfo.getDate_yunfu_str().equals("")){
									Date period_date = DateUtil.strToDate(studyinfo.getDate_yunfu_str(), "yyyy-MM-dd");
									Date now_date = new Date();
									int zhou = (int)(((now_date.getTime()-period_date.getTime())/(24*60*60*1000))/7);
									studyinfo.setPeriod(zhou);
								}
							}else{
								sqlServer.addCardInfo(studyinfo.getOpen_id(), studyinfo.getCard_id(), studyinfo.getName()
										, studyinfo.getSex(),studyinfo.getBirth_str(), studyinfo.getNation(), studyinfo.getAddress());
							}
							if(serverConfig.getMachine_type().equals("0") || serverConfig.getMachine_type().equals("1")){
								toScanPane();
							}else if(serverConfig.getMachine_type().equals("2")){
//								toClinicPane02();
								toScanPane();
							}
							stopRequested = true;					
						}
					}else{
						//toBackPane("系 统 信 息","读卡器发生错误！请检查通信端口。","5");
						if(!stopRequested){
							if(serverConfig.getMachine_type().equals("0") || serverConfig.getMachine_type().equals("1")){
								toScanPane();
							}else if(serverConfig.getMachine_type().equals("2")){
//								toClinicPane02();
								toScanPane();
							}
							stopRequested = true;
						}
					}
				}
				if(!stopRequested){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
	
	/**
	 * 系统提示读秒返回首页线程
	 */
	private class BackTimerThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		private String title;
		private String clinic;
		private String time;
		public BackTimerThread(String title,String clinic,String time){
			this.title = title;
			this.clinic = clinic;
			this.time = time;
		}
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			card.show(cardPane, "back");
			
			studyinfo = new FootStudyInfo();
			openID = null;
			cardID = null;
			title_label.setText(title);
			clinic_label.setText(clinic);
			count_label.setText(time);
			while(!stopRequested){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int second = Integer.parseInt(count_label.getText());
				if(second == 0){
					stopRequested = true;
					initPanePara();
					toFirstPane();
				}else{
					second = second-1;
					count_label.setText(second+"");
				}
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
	/**
	 * 扫描脚型线程
	 * isScaned 脚型数据是否扫描成功
	 * isPost 所有数据(包括体重脚型数据)都测量后，是否第一次执行上传
	 * */
	private class ScanFeetThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			
			if(!isPost){
				button_oper.setTitle("正在检测");
				//machine_type：0：只采集脚数据
				//machine_type：1：只采集脚数据+体重测量
				//machine_type：2：只采集脚数据+体重测量+挂号系统
				//machine_type：3：只采集脚数据+体重测量+挂号系统+his系统
				if(!serverConfig.getMachine_type().equals("0")){
					if(hwLabel.getText().equals("")){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						if(studyinfo.getCurrentWeight().equals("0.0") || studyinfo.getCurrentWeight().length()==0 || studyinfo.getCurrentWeight()==null){
							System.out.println("机器类型不为0型且外设数据为空，进行身高体重等外设测量...");
							studyinfo.setCurrentWeight("0.0");
							hwLabel.setText("体重：0.0kg");
							HWeightUtil hweightUtil = new HWeightUtil(hwConfig);
							hweightUtil.doActionPerformed();
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
							if(times == 30){
								System.out.println("3秒后未读取到串口数据！退出读取数据");
								MessageDialog option = new MessageDialog(MainJFrame.this,"读取体重数据失败，请检查设备或重试！","提示",MessageDialog.WARNING_MESSAGE);
								option.create_option();
								button_oper.setTitle("开始检测");
								button_oper.setEnabled(true);
								return ;
							}
							Float weight = hweightUtil.getWeight();
							if(weight == null){
								System.out.println("读取的数据有误！退出重新测量");
								MessageDialog option = new MessageDialog(MainJFrame.this,"获取体重数据失败，请重试！","提示",MessageDialog.WARNING_MESSAGE);
								option.create_option();
								button_oper.setTitle("开始检测");
								button_oper.setEnabled(true);
								return ;
							}
							weight = weight + Float.parseFloat(hwConfig.getWeight());
							studyinfo.setCurrentWeight_float(weight);
							hwLabel.setText("体重："+weight+"kg");
						}else{
							hwLabel.setText("体重："+studyinfo.getCurrentWeight()+"kg");
						}
//						hwLabel.setText("体重："+weight+"kg");
					}
				}
				
				//扫描脚型数据
				//获取左右脚信息：文件格式、分辨率、文件路径及文件名
				if(!isScaned){
					left_foot_pane.loadImage(true);
					right_foot_pane.loadImage(true);
					System.out.println("扫描获取整张图像BufferedImage对象");
//					BufferedImage origImage = acquire.startAcquireOne();
					// modify by kael
					int k = serverConfig.getMachine_kind();
//					button_oper.setEnabled(false);
					BufferedImage origImage=null;
					if(k==ServerConfig.MACHINE_KIND_A3){
						origImage = acquire.startAcquireOne();
					}else if(k == ServerConfig.MACHINE_KIND_A4){
						origImage = acquire.startAcquireOneDir(scanExternProgramDir);
					}else{
						origImage = acquire.startAcquireOne();
					}
					button_oper.setEnabled(true);
					
					// modify by kael end
					if(origImage != null){
						studyinfo.setLeft_url(studyinfo.getMac_id()+"L"+scanConfig.getSuffix());
						studyinfo.setRight_url(studyinfo.getMac_id()+"R"+scanConfig.getSuffix());
						String lefturl = scanDir+studyinfo.getLeft_url();
						String righturl = scanDir+studyinfo.getRight_url();
						studyinfo.setLeft_url_ab(lefturl);
						studyinfo.setRight_url_ab(righturl);
						studyinfo.setLeft_dpi(Integer.parseInt(scanConfig.getDpi()));
						studyinfo.setRight_dpi(Integer.parseInt(scanConfig.getDpi()));
						
						System.out.println("将整图BufferedImage对象，切割生成左右脚型图片");
						acquire.startAcquireOne(origImage, lefturl, righturl);
						System.out.println("载入 左脚 型图片，并判断是否获得参数");
						left_foot_pane.analyseImage(lefturl,studyinfo.getLeft_dpi());
						if(!left_foot_pane.isGetPara()){
							System.out.println("left foot did not detect the target pixels -> exist");
							button_oper.setTitle("开始检测");
							return;
						}
						System.out.println("载入 左脚 型图片，并判断是否获得参数");
						right_foot_pane.analyseImage(righturl,studyinfo.getRight_dpi());
						if(!right_foot_pane.isGetPara()){
							System.out.println("right foot did not detect the target pixels -> exist");
							button_oper.setTitle("开始检测");
							return;
						}
						System.out.println("左脚型等待坐标参数的出现（扫描线走完）");
						while(left_foot_pane.isShowPath()){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.out.println("********* left wait 100ms ********");
						}
						System.out.println("右脚型等待坐标参数的出现（扫描线走完）");
						while(right_foot_pane.isShowPath()){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.out.println("********* right wait 100ms *******");
						}
						System.out.println("完成左右脚型分析->生成报告（数据上传）");
						isScaned = true;
					}else{
						MessageDialog option = new MessageDialog(MainJFrame.this,"扫描失败，请检查设备或重试！","提示",MessageDialog.WARNING_MESSAGE);
						option.create_option();
						button_oper.setTitle("开始检测");
						return ;
					}
				}
			}
			button_oper.setTitle("正在打印");
			String[] leftPara = left_foot_pane.getParaValue();
			String[] rightPara = right_foot_pane.getParaValue();
			printUtil.setReportFootParam(leftPara[4], leftPara[5],rightPara[4],rightPara[5]);
			if(!isPost){
				System.out.println("the first print in this record hint info");
				studyinfo.setDate_host_str(DateUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
				studyinfo.setFrom_id(serverConfig.getMac());
				studyinfo.setFrom_app(serverConfig.getAppName());
				tableModel.setValueAt(leftPara[0]+"mm", 0, 0);
				tableModel.setValueAt(leftPara[1]+"mm", 1, 0);
				tableModel.setValueAt(leftPara[2], 2, 0);
				tableModel.setValueAt(leftPara[3], 3, 0);
				tableModel.setValueAt(leftPara[4], 4, 0);
				tableModel.setValueAt(rightPara[0]+"mm", 0, 2);
				tableModel.setValueAt(rightPara[1]+"mm", 1, 2);
				tableModel.setValueAt(rightPara[2], 2, 2);
				tableModel.setValueAt(rightPara[3], 3, 2);
				tableModel.setValueAt(rightPara[4], 4, 2);
				left_foot_pane.synTableData(true);
				right_foot_pane.synTableData(true);
				centerCard.show(centerCardPane,"print");
				left_foot_pane.repaint();
				right_foot_pane.repaint();
				contentPane.setAlpha(0.25f);
			}
			studyinfo.setLeft_length(leftPara[0]);
			studyinfo.setLeft_width(leftPara[1]);
			studyinfo.setLeft_foot_size(leftPara[2]);
			studyinfo.setLeft_foot_width(leftPara[3]);
			studyinfo.setLeft_foot_width2(calcUtil.getwidthname(leftPara[3]));
			studyinfo.setLeft_foot_status(leftPara[4]);
			studyinfo.setLeft_int_status(calcUtil.getFootStatusCode(leftPara[4]));
			studyinfo.setRight_length(rightPara[0]);
			studyinfo.setRight_width(rightPara[1]);
			studyinfo.setRight_foot_size(rightPara[2]);
			studyinfo.setRight_foot_width(rightPara[3]);
			studyinfo.setRight_foot_width2(calcUtil.getwidthname(rightPara[3]));
			studyinfo.setRight_foot_status(rightPara[4]);
			studyinfo.setRight_int_status(calcUtil.getFootStatusCode(rightPara[4]));
			
			studyinfo.setLeft_length_90(leftPara[6]);
			studyinfo.setLeft_length_825(leftPara[7]);
			studyinfo.setLeft_length_78(leftPara[8]);
			studyinfo.setLeft_length_725(leftPara[9]);
			studyinfo.setLeft_length_635(leftPara[10]);
			studyinfo.setLeft_length_68(leftPara[11]);
			studyinfo.setLeft_length_41(leftPara[12]);
			studyinfo.setLeft_length_18(leftPara[13]);
			studyinfo.setLeft_width_90(leftPara[14]);
			studyinfo.setLeft_width_78(leftPara[15]);
			studyinfo.setLeft_width_725(leftPara[16]);
			studyinfo.setLeft_width_635(leftPara[17]);
			studyinfo.setLeft_width_68(leftPara[18]);
			studyinfo.setLeft_width_41(leftPara[19]);
			studyinfo.setLeft_width_41full(leftPara[20]);
			studyinfo.setLeft_width_18(leftPara[21]);
			studyinfo.setLeft_width_ratio(leftPara[22]);
			studyinfo.setLeft_center_angle(leftPara[23]);
			studyinfo.setLeft_incline_angle(leftPara[24]);
			studyinfo.setRight_length_90(rightPara[6]);
			studyinfo.setRight_length_825(rightPara[7]);
			studyinfo.setRight_length_78(rightPara[8]);
			studyinfo.setRight_length_725(rightPara[9]);
			studyinfo.setRight_length_635(rightPara[10]);
			studyinfo.setRight_length_68(rightPara[11]);
			studyinfo.setRight_length_41(rightPara[12]);
			studyinfo.setRight_length_18(rightPara[13]);
			studyinfo.setRight_width_90(rightPara[14]);
			studyinfo.setRight_width_78(rightPara[15]);
			studyinfo.setRight_width_725(rightPara[16]);
			studyinfo.setRight_width_635(rightPara[17]);
			studyinfo.setRight_width_68(rightPara[18]);
			studyinfo.setRight_width_41(rightPara[19]);
			studyinfo.setRight_width_41full(rightPara[20]);
			studyinfo.setRight_width_18(rightPara[21]);
			studyinfo.setRight_width_ratio(rightPara[22]);
			studyinfo.setRight_center_angle(rightPara[23]);
			studyinfo.setRight_incline_angle(rightPara[24]);
			float[] leftCoord = left_foot_pane.getCoord();
			studyinfo.setLcircle_01_x(leftCoord[0]);
			studyinfo.setLcircle_01_y(leftCoord[1]);
			studyinfo.setLcircle_02_x(leftCoord[2]);
			studyinfo.setLcircle_02_y(leftCoord[3]);
			studyinfo.setLcircle_03_x(leftCoord[4]);
			studyinfo.setLcircle_03_y(leftCoord[5]);
			float[] rightCoord = right_foot_pane.getCoord();
			studyinfo.setRcircle_01_x(rightCoord[0]);
			studyinfo.setRcircle_01_y(rightCoord[1]);
			studyinfo.setRcircle_02_x(rightCoord[2]);
			studyinfo.setRcircle_02_y(rightCoord[3]);
			studyinfo.setRcircle_03_x(rightCoord[4]);
			studyinfo.setRcircle_03_y(rightCoord[5]);
			studyinfo.setLcircle_725_x(leftCoord[6]);
			studyinfo.setLcircle_725_y(leftCoord[7]);
			studyinfo.setLcircle_635_x(leftCoord[8]);
			studyinfo.setLcircle_635_y(leftCoord[9]);
			studyinfo.setLcircle_4101_x(leftCoord[10]);
			studyinfo.setLcircle_4101_y(leftCoord[11]);
			studyinfo.setLcircle_4102_x(leftCoord[12]);
			studyinfo.setLcircle_4102_y(leftCoord[13]);
			studyinfo.setLbreak_01_x(leftCoord[14]);
			studyinfo.setLbreak_01_y(leftCoord[15]);
			studyinfo.setLbreak_02_x(leftCoord[16]);
			studyinfo.setLbreak_02_y(leftCoord[17]);
			studyinfo.setLcircle_80_x(leftCoord[30]);
			studyinfo.setLcircle_80_y(leftCoord[31]);
			studyinfo.setLcircle_65_x(leftCoord[32]);
			studyinfo.setLcircle_65_y(leftCoord[33]);
			studyinfo.setRcircle_725_x(rightCoord[6]);
			studyinfo.setRcircle_725_y(rightCoord[7]);
			studyinfo.setRcircle_635_x(rightCoord[8]);
			studyinfo.setRcircle_635_y(rightCoord[9]);
			studyinfo.setRcircle_4101_x(rightCoord[10]);
			studyinfo.setRcircle_4101_y(rightCoord[11]);
			studyinfo.setRcircle_4102_x(rightCoord[12]);
			studyinfo.setRcircle_4102_y(rightCoord[13]);
			studyinfo.setRbreak_01_x(rightCoord[14]);
			studyinfo.setRbreak_01_y(rightCoord[15]);
			studyinfo.setRbreak_02_x(rightCoord[16]);
			studyinfo.setRbreak_02_y(rightCoord[17]);
			studyinfo.setRcircle_80_x(rightCoord[30]);
			studyinfo.setRcircle_80_y(rightCoord[31]);
			studyinfo.setRcircle_65_x(rightCoord[32]);
			studyinfo.setRcircle_65_y(rightCoord[33]);
			studyinfo.setLcircle_1801_x(leftCoord[20]);
			studyinfo.setLcircle_1801_y(leftCoord[21]);
			studyinfo.setLcircle_1802_x(leftCoord[22]);
			studyinfo.setLcircle_1802_y(leftCoord[23]);
			studyinfo.setRcircle_1801_x(rightCoord[20]);
			studyinfo.setRcircle_1801_y(rightCoord[21]);
			studyinfo.setRcircle_1802_x(rightCoord[22]);
			studyinfo.setRcircle_1802_y(rightCoord[23]);
			if(!isPost){
				studyinfo.setHospital_no(serverConfig.getHospital_no());
				studyinfo.setHospital_name(serverConfig.getHospital_name());
				isPost = true;
				int[] leftEdge = left_foot_pane.getEdge();
				studyinfo.setLfoot_top(leftEdge[0]);
				studyinfo.setLfoot_bottom(leftEdge[1]);
				studyinfo.setLfoot_left(leftEdge[2]);
				studyinfo.setLfoot_right(leftEdge[3]);
				int[] rightEdge = right_foot_pane.getEdge();
				studyinfo.setRfoot_top(rightEdge[0]);
				studyinfo.setRfoot_bottom(rightEdge[1]);
				studyinfo.setRfoot_left(rightEdge[2]);
				studyinfo.setRfoot_right(rightEdge[3]);
				studyinfo.setLscale(left_foot_pane.getScale());
				studyinfo.setRscale(right_foot_pane.getScale());
				studyinfo.setLcircle_18_x(leftCoord[18]);
				studyinfo.setLcircle_18_y(leftCoord[19]);
				studyinfo.setLcircle_90_x(leftCoord[24]);
				studyinfo.setLcircle_90_y(leftCoord[25]);
				studyinfo.setLcircle_825_x(leftCoord[26]);
				studyinfo.setLcircle_825_y(leftCoord[27]);
				studyinfo.setLcircle_78_x(leftCoord[28]);
				studyinfo.setLcircle_78_y(leftCoord[29]);
				studyinfo.setRcircle_18_x(rightCoord[18]);
				studyinfo.setRcircle_18_y(rightCoord[19]);
				studyinfo.setRcircle_90_x(rightCoord[24]);
				studyinfo.setRcircle_90_y(rightCoord[25]);
				studyinfo.setRcircle_825_x(rightCoord[26]);
				studyinfo.setRcircle_825_y(rightCoord[27]);
				studyinfo.setRcircle_78_x(rightCoord[28]);
				studyinfo.setRcircle_78_y(rightCoord[29]);
			}
			// comment by kael
			// mark for false for edit/update
			if(result.equals("true")){
				boolean update = sqlServer.editStudyInfo(studyinfo);
				int type = 0;
				String message = null;
				if(update){
					type = MessageDialog.INFORMATION_MESSAGE;
					message = "数据修改成功。";
				}else{
					type = MessageDialog.WARNING_MESSAGE;
					message = "数据修改失败！";
				}
				MessageDialog option = new MessageDialog(MainJFrame.this,message,"提示",type);
				option.create_option();
			}else{
				System.out.println("显示正在上传......");
				printQrcode.showGeting();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(result.equals("false")||result.equals("ftp")){
					result = "sql";
				}
				if(result.equals("sql")){
					if(sqlServer.addStudyInfoToTxt(studyinfo)){
						result = "true";
					}else{
						result = "sql";
					}
				}
				if(result.equals("true")){
					// modify by kael
					printQrcode.showUploaded();
					// modify by kael over
//					if(Integer.parseInt(serverConfig.getMachine_type()) >= 2){
//						printQrcode.showClinic(studyinfo.getClinic_dept()+";"+studyinfo.getDoctor_name()+";"+studyinfo.getClinic_type()+";"
//								+studyinfo.getClinic_status()+";"+studyinfo.getQueue_num()+";"+studyinfo.getWait_num());
//					}else{
//						printQrcode.showUploaded();
//					}
//					button_oper.setEnabled(true);
					if(printUtil.havePrint() != null){
						if(Integer.parseInt(serverConfig.getMachine_type()) >= 2){
							printUtil.setCinicInfo(studyinfo.getClinic_dept()+"  "+studyinfo.getQueue_num(), studyinfo.getWait_num());
						}else{
							printUtil.setCinicInfo(null, null);
						}
						String[] para = {studyinfo.getHeight(),studyinfo.getWeight()
								,studyinfo.getLeft_length(),studyinfo.getRight_length()
								,studyinfo.getLeft_width(),studyinfo.getRight_width(),
								studyinfo.getLeft_length_725(),studyinfo.getRight_length_725(),
								studyinfo.getLeft_foot_status(),studyinfo.getRight_foot_status()};
						printUtil.setFeetPara(para);
						if(studyinfo.getOpen_id() == null){
							printUtil.setQrcode(qrcodePane.getImagePath());
							qrcodeArr.remove(0);
						}else{
							printUtil.setQrcode(null);
						}
						// 
						printUtil.printpaper();
						if(ServerConfig.reportPrinterName.length()>0){
							printUtil.setReportParam(studyinfo.getMac_id(), studyinfo.getDate_yunfu_str(),studyinfo.isSingle());
							boolean ok = printUtil.genReport();
							if(ok){
//								printUtil.doPrintReport(ServerConfig.reportPrinterName);
								printUtil.doPrintReportExtern(ServerConfig.reportPrinterName);
							}
						}
					}
					button_oper.setTitle("返回首页");
					timerLabel.setText("5 秒后，自动返回");
					while(!stopRequested){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int second = Integer.parseInt(timerLabel.getText().substring(0, 2).trim());
						if(second == 0){
							stopRequested = true;
							initPara();
							toFirstPane();
						}else{
							second = second-1;
							timerLabel.setText(second+" 秒后，自动返回");
						}
					}
				}else{
					printQrcode.showError("error:"+result);
					button_oper.setTitle("请 重 试");
				}
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
	
	/**
	 * 后台上传脚型数据线程
	 * */
	private class UploadThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			while ( !stopRequested ) {
				if(serverConfig.isAvailable()){
					System.out.println("run UploadTask time: "+System.currentTimeMillis());
					File dirFile = new File(scanDir);
					File[] files = dirFile.listFiles();
					for (int i = 0; i < files.length; i++) {
						if(files[i].exists()&&files[i].isFile()&&
								files[i].getName().endsWith(".dat")&&!files[i].getName().startsWith("null")){
							String mac_id = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
							String lefturl = mac_id + "L.jpg";
							String righturl = mac_id + "R.jpg";
							String lefturl_ab = scanDir + lefturl;
							String righturl_ab = scanDir + righturl;
							File leftFile = new File(lefturl_ab);
							File rightFile = new File(righturl_ab);
							File qrcodeFile = new File(scanDir + mac_id + ".jpg");
							
							if(leftFile.exists()&&rightFile.exists()){
								System.out.println("执行获取"+files[i].getName()+"文件内容");
								String content = FileIOUtil.readFileByByte(files[i].getAbsolutePath());
								if(content != null){
									String imgDirUrl = ftpServer.uploadFoot(new String[]{lefturl_ab,righturl_ab},new String[]{lefturl,righturl});
									if(imgDirUrl != null){
										System.out.println("脚型图片上传成功，执行文本数据上传。");
										String other = ",";
										other = other + "\"left_urla\":"+"\""+imgDirUrl+lefturl+"\",";
										other = other + "\"right_urla\":"+"\""+imgDirUrl+righturl+"\"";
										other = other + "}";
										content = content + other;
										System.out.println(content);
										if(sqlServer.addStudyInfoToSQL(content)){
											System.out.println("脚型文本数据上传成功，"+mac_id+"。");
											files[i].delete();
											qrcodeFile.delete();
											leftFile.delete();
											rightFile.delete();
										}
									}else{
										System.out.println("很遗憾，上传左右脚型图片失败！");
									}
								}else{
									System.out.println("很遗憾，本地读取脚型数据失败！");
								}
							}else{
								System.out.println("异常：脚型图片不存在，该档案的所有文件！");
								files[i].delete();
							}						
						}
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(!stopRequested){
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
	
	private class JustGetWeightThread extends Thread{
		public void run() {
//			studyinfo.setHospital_no(serverConfig.getHospital_no());
//			studyinfo.setHospital_name(serverConfig.getHospital_name());
//			studyinfo.setWeight_float(new Float(33.3));
//			sqlServer.sendWeightBaseinfo(studyinfo);
//			return ;
			if(!serverConfig.getMachine_type().equals("0")){
				if(hwLabel.getText().equals("")){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.out.println("机器类型不为0型且外设数据为空，进行身高体重等外设测量...");
					if(studyinfo.getCurrentWeight().equals("0.0") || studyinfo.getCurrentWeight().length()==0 || studyinfo.getCurrentWeight()==null){
						hwLabel.setText("体重：0.0kg");
						HWeightUtil hweightUtil = new HWeightUtil(hwConfig);
						hweightUtil.doActionPerformed();
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
						if(times == 30){
							System.out.println("3秒后未读取到串口数据！退出读取数据");
							MessageDialog option = new MessageDialog(MainJFrame.this,"读取体重数据失败，请检查设备或重试！","提示",MessageDialog.WARNING_MESSAGE);
							option.create_option();
							return ;
						}
						Float weight = hweightUtil.getWeight();
						if(weight == null){
							System.out.println("读取的数据有误！退出重新测量");
							MessageDialog option = new MessageDialog(MainJFrame.this,"获取体重数据失败，请重试！","提示",MessageDialog.WARNING_MESSAGE);
							option.create_option();
							return ;
						}
						weight = weight + Float.parseFloat(hwConfig.getWeight());
						studyinfo.setCurrentWeight_float(weight);
					}
//					studyinfo.setWeight("0.0");
					studyinfo.setHospital_no(serverConfig.getHospital_no());
					studyinfo.setHospital_name(serverConfig.getHospital_name());
					sqlServer.sendWeightBaseinfo(studyinfo);
					
					//获取最新的报告记录
					Map<String, String> map = new HashMap<String,String>();
					map = sqlServer.getLastReportInfo(openID);
					if(map!=null){
						studyinfo.setLeft_urla(map.get("left_urla"));
						studyinfo.setRight_urla(map.get("right_urla"));
						studyinfo.setLeft_foot_size(map.get("left_foot_size"));
						studyinfo.setLeft_foot_width(map.get("left_foot_width"));
						studyinfo.setLeft_foot_width2(map.get("left_foot_width2"));
						studyinfo.setLeft_foot_status(map.get("left_foot_status"));
						studyinfo.setRight_foot_size(map.get("right_foot_size"));
						studyinfo.setRight_foot_width(map.get("right_foot_width"));
						studyinfo.setRight_foot_width2(map.get("right_foot_width2"));
						studyinfo.setRight_foot_status(map.get("right_foot_status"));
						studyinfo.setLeft_length(map.get("left_length"));
						studyinfo.setRight_length(map.get("right_length"));
						studyinfo.setLeft_width(map.get("left_width"));
						studyinfo.setRight_width(map.get("right_width"));
					}
				}
			}
		}
	}
	
	private class GetWeightThread extends Thread{
		private volatile boolean stopRequested;
		private Thread runThread;
		public void run() {
			runThread = Thread.currentThread();
			stopRequested = false;
			while ( !stopRequested ) {
				try{
					HWeightUtil hweightUtil = new HWeightUtil(hwConfig);
					boolean getWeightHardwareOk = hweightUtil.doActionPerformed();
					if(getWeightHardwareOk){
						int times = 0;
						while(!hweightUtil.isOK()&&times<30){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							times++;
						}
						if(times == 30){
							continue;
						}
						Float weight = hweightUtil.getWeight();
						if(weight == null){
							continue;
						}
						studyinfo.setCurrentWeight_float(weight);
						stopRequested=true;
					}else{
//						MessageDialog option = new MessageDialog(MainJFrame.this,"没有体重测试仪器，请重试！","提示",MessageDialog.WARNING_MESSAGE);
//						option.create_option();
						// for test
//						studyinfo.setCurrentWeight("48.5");
						break;
					}
					if(!stopRequested){
						try {
							Thread.sleep(17000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}catch(Exception e){
//					System.out.println(e.getStackTrace());
				}
			}
		}
		public void stopRequest() {
			stopRequested = true;
			if ( runThread != null ) {
				runThread.interrupt();
			}
		}
	}
}