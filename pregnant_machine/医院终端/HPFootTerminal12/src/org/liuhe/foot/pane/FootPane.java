package org.liuhe.foot.pane;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.liuhe.algorithm.analyse.ConnectFilter;
import org.liuhe.algorithm.analyse.EroDilFilter;
import org.liuhe.algorithm.analyse.PixelPoint;
import org.liuhe.algorithm.analyse.SimpleEdgeFilter;
import org.liuhe.algorithm.analyse.SpecialFilter;
import org.liuhe.algorithm.analyse.StampFilter;
import org.liuhe.algorithm.config.AnalyseConfig;
import org.liuhe.common.util.FootCalcUtil;

public class FootPane extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private String foot;							//脚位:left;right
	private DefaultTableModel model;
    private AnalyseConfig analyseConfig;
    private boolean showData = false;
	
	private Image originalImage = null;  			//原始图像
	private Image analyseImage = null;				//用于模拟扫描分析动画
	
	private int orig_x;
    private int orig_y;
    
	private BufferedImage bufImage = null;  		//目标缓冲区图像
    private BufferedImage originalBufImage = null;  //原始缓冲区图像//扩展后的
    private BufferedImage tempImage = null;  		//用于显示的缓冲区图像
    private BufferedImage stampImage = null;		//stamp滤波的备份缓冲区图像
	
    final BasicStroke[] bs = new BasicStroke[6];
    private FootCalcUtil calcUtil;
    private FootPaneMouseListener mouseListener;
    private FootPaneMouseMotionListener motionListener;
    private DecimalFormat dformat = new DecimalFormat("#.##");
	
	private boolean draging = false;				//是否当前拖动坐标点
	private boolean draged = false;					//判断是否启用了draged()方法
    private int dragnum = 0;						//标记当前拖动的点位
    private int drag_method = 1;
    
    private Circle circle_01;						//脚中趾
    private Circle circle_02;						//脚后跟
    private Circle circle_03;						//脚拇趾
    private Line line_heel;							//脚跟垂直线
    private Line line_top;							//相交于
    private Line line_foot;
    private FillPoint fpoint_01;
    
    private Circle circle_725;
    private Circle circle_635;
    private FillPoint fpoint_725;
    private FillPoint fpoint_635;
    private FillPoint fpoint_725_red;
    private FillPoint fpoint_635_red;
    private FillPoint fpoint_68_red;
    
    private Line line_725;							//第一跖趾垂直线
    private Line line_635;							//第五跖趾垂直线
    private Line line_68;							//斜宽线

    private Circle circle_4101;
    private Circle circle_4102;
    private Line line_41;							//腰窝宽线
    private FillPoint fpoint_41;
    private BreakCoord break_01;
    private BreakCoord break_02;
    
    private FillPoint fpoint_18;					//中轴线上的18%脚长
    private FillPoint fpoint_18s;					//分踵线上的18%脚长
    private FillPoint fpoint_18h;					//分踵角度辅助点
    private Line line_f;							//分踵线
    private Line line_18;							//踵心全宽线
    private Circle circle_18;						//分踵线调节点
    private Circle circle_1801;
    private Circle circle_1802;
    private Circle circle_90;						//拇趾外突点
    private FillPoint fpoint_90_red;
    private FillPoint fpoint_90;
    private Line line_90;
    private Circle circle_825;						//小趾端点
    private FillPoint fpoint_825_red;
    private FillPoint fpoint_825;
    private Line line_825;
    private Circle circle_78;						//小趾外突点
    private FillPoint fpoint_78_red;
    private FillPoint fpoint_78;
    private Line line_78;
   
    private Circle circle_80;						//斜边夹角
    private Circle circle_65;
    private FillPoint fpoint_80;
    private FillPoint fpoint_65;
    private Line line_80;
    private Line line_65;
    
    private Cross cross_cursor;						//选择光标
    
    private int pane_width;				//面板宽度
    private int pane_height;			//面板高度    
    private int image_width;			//初始载入图片后，自适应等比例宽度
	private int image_height;			//初始载入图片后，自适应等比例高度
    private double scale;				//扫描缩放尺寸
    private int dpi;					//当前图片的分辨率
	
	private boolean loading = false;	//是否正在扫描获取图像
	private boolean analysing = false;	//是否载入自适应图片后，正在算法分析
	private boolean showPath = false;	//是否显示蚂蚁线
	
	private int[][] pixel_array = null;
	private int top;					//用于计算偏移量
	private int left;
	private int right;
	private int bottom;
	
	private Timer timer = null;
	private GeneralPath gPath;
	private int path_index = 0;			//用于配合显示蚂蚁线
	private boolean doTimer = false;	//是否启动了蚂蚁线到坐标辅助线的定时器
	
	private int scan_index = 0;			//前景扫描分析线，当前位移了多少步
	private int scan_step = 5;			//前景扫描分析线，扫描每步的位移5
	private int scan_count;				//前景扫描分析线，总共要走完多少步
	
	private String foot_length = null;
	private String foot_length_pre = null;
	private String foot_width = null;
	private String foot_width_pre = null;
	private String shoe_length = null;
	private String type_width = null;
	private String foot_status = null;
	private String foot_advice = null;
	
	private String incline_angle = null;
	private String center_angle = null;
		
	private float kz = 0;				//中轴线斜率
	private float kz_v = 0;				//垂直于中轴线的辅助线斜率
	private float kf = 0;				//分踵线的斜率
	private float kf_v = 0;				//垂直于分踵线的斜率
	private float length_18 = 0;		//踵心像素长度
	private float lenght_100 = 0;		//脚长像素长度
	private float differ_x725 = 0;		//辅助计算斜边夹角的坐标点的偏移量
	private float differ_y725 = 0;
	
	private String no_type = null;		//错误码类型：未检测出目标像素、边界超出
	    
    public FootPane(String foot,AnalyseConfig analyseConfig,FootCalcUtil calcUtil,DefaultTableModel model){
    	this.foot = foot;
    	this.model = model;
    	this.analyseConfig = analyseConfig;
    	this.calcUtil = calcUtil;
    	
    	float[] dash = new float[]{5,5};
		for (int i = 0; i < 6; i++) {
			bs[i] = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, i);
		}
		mouseListener = new FootPaneMouseListener();
		motionListener = new FootPaneMouseMotionListener();
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(motionListener);
		analyseImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir")+"\\picture\\line_analyse.png");
		
		timer = new Timer(20,this);		//初始化计时器
		
		circle_01 = new Circle();
		circle_01.setTipText("A");
		circle_02 = new Circle();
		circle_02.setTipText("O");
		circle_03 = new Circle();
		circle_03.setTipText("A1");
		line_heel = new Line();
		line_top = new Line();
		fpoint_01 = new FillPoint();
		fpoint_01.setColor(Color.RED);
		line_foot = new Line();
		
		circle_725 = new Circle();
		circle_725.setTipText("E1");
		circle_635 = new Circle();
		circle_635.setTipText("G2");
		line_635 = new Line();
		line_725 = new Line();
		line_68 = new Line();
		fpoint_725 = new FillPoint();
		fpoint_725.radius = 2.5f;
		fpoint_635 = new FillPoint();
		fpoint_635.radius = 2.5f;
		fpoint_725_red = new FillPoint();
		fpoint_725_red.setColor(Color.RED);
		fpoint_725_red.setTipText("E");
		fpoint_68_red = new FillPoint();
		fpoint_68_red.setColor(Color.RED);
		fpoint_68_red.setTipText("F");
		fpoint_635_red = new FillPoint();
		fpoint_635_red.setColor(Color.RED);
		fpoint_635_red.setTipText("G");
		
		circle_4101 = new Circle();
		circle_4101.setTipText("H1");
		circle_4102 = new Circle();
		circle_4102.setTipText("H2");
		line_41 = new Line();
		fpoint_41 = new FillPoint();
		fpoint_41.radius = 2.5f;
		break_01 = new BreakCoord();
		break_02 = new BreakCoord();
		
		fpoint_18 = new FillPoint();
		fpoint_18.setColor(Color.GREEN);
		fpoint_18.radius = 2.5f;
		fpoint_18s = new FillPoint();
		fpoint_18s.setColor(Color.GREEN);
		fpoint_18s.radius = 2.5f;
		fpoint_18h = new FillPoint();
		fpoint_18h.setColor(Color.GREEN);
		fpoint_18h.radius = 2.5f;
		line_f = new Line();
		line_18 = new Line();
		circle_18 = new Circle();
		circle_18.setTipText("X");
		circle_1801 = new Circle();
		circle_1801.setTipText("I1");
		circle_1802 = new Circle();
		circle_1802.setTipText("I2");
		
		circle_90 = new Circle();
		circle_90.setTipText("B1");
		fpoint_90_red = new FillPoint();
		fpoint_90_red.setColor(Color.RED);
		fpoint_90_red.setTipText("B");
		fpoint_90 = new FillPoint();
		fpoint_90.setColor(Color.GREEN);
		fpoint_90.radius = 2.5f;
		line_90 = new Line();
		circle_825 = new Circle();
		circle_825.setTipText("C2");
		fpoint_825_red = new FillPoint();
		fpoint_825_red.setColor(Color.RED);
		fpoint_825_red.setTipText("C");
		fpoint_825 = new FillPoint();
		fpoint_825.setColor(Color.GREEN);
		fpoint_825.radius = 2.5f;
		line_825 = new Line();
		circle_78 = new Circle();
		circle_78.setTipText("D2");
		fpoint_78_red = new FillPoint();
		fpoint_78_red.setColor(Color.RED);
		fpoint_78_red.setTipText("D");
		fpoint_78 = new FillPoint();
		fpoint_78.setColor(Color.GREEN);
		fpoint_78.radius = 2.5f;
		line_78 = new Line();
		
		circle_80 = new Circle();
		circle_80.setTipText("K1");
		circle_65 = new Circle();
		circle_65.setTipText("K2");
		fpoint_80 = new FillPoint();
		fpoint_65 = new FillPoint();
		line_80 = new Line();
		line_65 = new Line();
		
		cross_cursor = new Cross();
    }
    
    public void loadImage(boolean load){
    	loading = load;									//true:开始载入图像初始化所有参数；false:刷新全屏初始化所有参数
    	analysing = false;
    	showPath = false;
    	doTimer = false;
    	
       	if(timer!=null&&timer.isRunning()){
       		timer.stop();
       	}
       	if(loading){
       		path_index = 0;
       		scan_index = 0;
       		scan_count = this.getHeight()/scan_step+1;	//扫描步数 677/5=135.4 = 135+1=136 
        	timer.setDelay(3000/scan_count);			//4000/136=29.4117  3000/136=22.0588
           	System.out.println("scan count："+scan_count+",scan delay："+timer.getDelay());
       	}
       	
       	originalImage = null;							//重新清空缓存区图像
       	originalBufImage = null;
       	tempImage = null;
       	stampImage = null;
       	bufImage = null;
       	
       	foot_length = null;
    	kz = 0;
    	kf = 0;
    	length_18 = 0;
    	
    	draging = false;
    	draged = false;
        drag_method = 1;
    	
    	repaint();
    }
    public void loadFailure(){							//扫描获取图片失败，重新清屏
    	loading = false;
    	repaint();
    }
    public boolean isShowPath(){
    	return showPath;
    }
    public void synTableData(boolean show){
    	showData = show;
    }
    public void setPredictData(String foot_length_pre,String foot_width_pre){
    	this.foot_length_pre = foot_length_pre;
    	this.foot_width_pre = foot_width_pre;
    }
    
    public void analyseImage(String filename,int dpi) {
    	File file = new File(filename);
    	this.dpi = dpi;
    	try{
    		originalImage = ImageIO.read(file);			//从文件到获取Image对象//bufImage = ImageIO.read(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		MediaTracker mt = new MediaTracker(this); 		//实例化媒体加载器
		mt.addImage(originalImage, 0); 					//增加待加载图像到媒体加载器
		try {
			mt.waitForAll(); 							//等待所有图像的加载完成
		} catch (Exception ex) {
			ex.printStackTrace(); 						//输出出错信息
		}
		double scaleX = this.getWidth()*1.0/originalImage.getWidth(this);
		double scaleY = this.getHeight()*1.0/originalImage.getHeight(this);
		double scan_scale = (scaleX>scaleY)?scaleY:scaleX;
    	image_width = (int) (originalImage.getWidth(this)*scan_scale);
		image_height = (int)(originalImage.getHeight(this)*scan_scale);
    	
		loading = false;
		//第一阶段完（扫描获取图像）//第二阶段，分析图像，模拟分析扫描线
		analysing = true;
		timer.start();									//启动定时器（分析脚型图像及显示蚂蚁线）

		if(getStampImage()){
			if(!getTargetImage()){
				System.out.println("Detect the foot type area edge less than 10 pixels !");
				analysing = false;
			}
		}else{
			System.out.println("Detect the foot type area is less than 5000 pixels !");
			no_type = "未检测出目标像素！";
			analysing = false;							//timer.stop();//timer如果停止的话，扫描线可能还没画完。
		}
    }
    
    public void paintComponent(Graphics g) {			//重写paint方法,绘制panel面板图像
    	Graphics2D g2 = (Graphics2D) g;
    	pane_width = this.getWidth();
    	pane_height = this.getHeight();
		g2.setPaint(new Color(10,10,10));				//绘制黑色的背景
		g2.fill(new Rectangle2D.Double(0, 0, pane_width-1, pane_height-1));
		
		/*if(loading){									//正在扫描图片(载入扫描动画gif)
			if(loadImage!=null){
				g2.drawImage(loadImage, (pane_width-loadImage.getWidth(this))/2, (pane_height-loadImage.getHeight(this))/2
						, loadImage.getWidth(this), loadImage.getHeight(this),this);
			}
		}else*/
														//自适应导入之后，正在分析图片(模拟分析动画)
		if(!loading&&originalImage!=null&&(analysing||scan_index<=scan_count)){
			g2.drawImage(originalImage, (pane_width-image_width)/2, (pane_height-image_height)/2 ,
					image_width, image_height ,this);
			g2.drawImage(analyseImage, 0, scan_step*(scan_index++), pane_width, analyseImage.getHeight(this),this);
		}else
														//分析图像失败，未检测出目标像素...
		if(!loading&&originalImage!=null&&(!analysing&&scan_index>scan_count)&&bufImage==null){
			timer.stop();
			g2.drawImage(originalImage, (pane_width-image_width)/2, (pane_height-image_height)/2 ,
					image_width, image_height ,this);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Color.WHITE);
			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 28));
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString(no_type, (pane_width-fm.stringWidth(no_type))/2, pane_height/2);
		}else
				
		if (bufImage!=null&&!analysing&&scan_index>scan_count) {
			g2.drawImage(bufImage, orig_x, orig_y , bufImage.getWidth(),  bufImage.getHeight(),this);
			Composite old = g2.getComposite();			//绘制透明参考网格线
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
			g2.setColor(new Color(0,255,0));
			for(int y=0;y<pane_height;y++){
				if(y%10==0){
					g2.drawLine(0, y, pane_width-1, y);
				}
			}
			for(int x=0;x<pane_width;x++){
				if(x%10==0){
					g2.drawLine(x, 0, x, pane_height-1);
				}
			}
			if(showPath){								//绘制脚型蚂蚁轮廓曲线
				g2.setComposite(old);
				g2.setColor(Color.red);
	        	g2.setStroke(bs[(path_index++)%6]);
	        	g2.draw(gPath);
	        	//设置定时器（当现实蚂蚁轮廓线后，开启另外的定时滞留1秒，之后停止循环定时器，显示参数坐标）
	        	if(!doTimer){
	        		doTimerTask();
	        		doTimer = true;
	        	}
			}else{
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				circle_01.draw(g2);
				circle_02.draw(g2);
				circle_03.draw(g2);
				line_heel.draw(g2);
				line_top.draw(g2);
				line_foot.draw(g2);
				fpoint_01.draw(g2);
				fpoint_725.draw(g2);
				fpoint_635.draw(g2);
				fpoint_41.draw(g2);
				fpoint_725_red.draw(g2);
				fpoint_635_red.draw(g2);
				fpoint_68_red.draw(g2);
				line_635.draw(g2);
				line_725.draw(g2);
				line_68.draw(g2);
				circle_725.draw(g2);
				circle_635.draw(g2);
				line_41.draw(g2);
				circle_4101.draw(g2);
				circle_4102.draw(g2);
				break_01.draw(g2);
				break_02.draw(g2);
				
				if(draging){
					cross_cursor.draw(g2);
				}
				g2.setComposite(old);
			}
		}
		g2.setStroke(new BasicStroke(1.0f));				//绘制淡蓝色的边框
		g2.setPaint(new Color(56,248,249));
		g2.draw(new Rectangle2D.Double(0, 0, pane_width-1, pane_height-1));
    }
	public void actionPerformed(ActionEvent e) {			//启动定时器执行重绘方法
		repaint();
	}
	
	private void doTimerTask(){								//蚂蚁轮廓线定时滞留1秒
		java.util.Timer utimer = new java.util.Timer();
		java.util.TimerTask utask = new java.util.TimerTask(){
            public void run(){
            	if(!analysing&&showPath&&scan_index>scan_count){
            		System.out.println("timer stop.....");
            		timer.stop();
    				showPath = false;
    				repaint();
    			}
            }
        };
        utimer.schedule(utask,1000);
	}
    
	private boolean getStampImage(){
		int iw = originalImage.getWidth(this);				//首先扩展图片上下左右20个像素点
		int ih = originalImage.getHeight(this);
		int[] pixels = new int[iw*ih];
		try {
			PixelGrabber pg = new PixelGrabber(originalImage,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int[] new_pixs = new int[(iw+40)*(ih+40)];			//新建一个扩展的图片像素数组：四周扩展20px
		for(int i=0;i<ih;i++){
			for(int j=0;j<iw;j++){
				new_pixs[(i+20)*(iw+40)+(j+20)] = pixels[i*iw+j];
			}
		}
		int pixel = (0xff<<24)|(20<<16)|(35<<8)|(35);		//int pixel=(alpha<<24)|(20<<16)|(40<<8)|(40);特定的背景像素
		for(int i=0;i<ih+40;i++){
			for(int j=0;j<iw+40;j++){
				if(i<20||i>=20+ih||j<20||j>=20+iw){
					new_pixs[i*(iw+40)+j]=pixel;			//上下左右20像素，为特定的像素，这样在忽略。。。
				}
			}
		}
		ImageProducer ip = new MemoryImageSource(iw+40,ih+40,new_pixs,0,iw+40);
		Image image = createImage(ip);						//该image重新赋值
		originalBufImage = new BufferedImage(image.getWidth(this),image.getHeight(this),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2D = originalBufImage.createGraphics();
		g2D.drawImage(image, 0, 0, this);
		
		tempImage = originalBufImage;
		//执行特殊滤波
		if(Boolean.parseBoolean(analyseConfig.getUseSpecial())){
			SpecialFilter filter_special = new SpecialFilter();
			tempImage = filter_special.filter(tempImage);
		}
		//先腐蚀算法
		EroDilFilter filter_EroDil = new EroDilFilter();
		tempImage = filter_EroDil.mini_ero(tempImage,Integer.parseInt(analyseConfig.getTime_erosion()));
		//后膨胀算法
		tempImage = filter_EroDil.max_dil(tempImage,Integer.parseInt(analyseConfig.getTime_dilate()));
		//执行Stamp滤波，得到二值图
		StampFilter filter_stamp = new StampFilter();
		filter_stamp.setRadius(Integer.parseInt(analyseConfig.getStamp_radius())*1.0f);
		filter_stamp.setThreshold(Integer.parseInt(analyseConfig.getStamp_threshold())*1.0f/100);
		tempImage = filter_stamp.filter(tempImage, null);
		//执行连通区域标记算法
		ConnectFilter filter_Connect = new ConnectFilter();
		tempImage = filter_Connect.filter(tempImage);
		//没有检测出任何的目标像素
		if(tempImage==null){
			return false;
		}
		//检测到目标区域像素不足5000，退出
		if(filter_Connect.getMax_num()<20000){
			return false;
		}
		//后膨胀算法
		//tempImage = filter_EroDil.max_dil(tempImage,Integer.parseInt(analyseConfig.getTime_dilate()));
		stampImage = tempImage;								//赋值给stampImage（备份）.....！
		//简单边缘算法
		SimpleEdgeFilter filter_edge = new SimpleEdgeFilter();
		tempImage = filter_edge.process(tempImage);
		return true;
	}
	private boolean getTargetImage(){
		getPixelArray();
		
		int temp_width = tempImage.getWidth();
		int temp_height = tempImage.getHeight();
		left = pixel_array[0][0];
		right = pixel_array[0][0];
		top = pixel_array[0][1];
		bottom = pixel_array[0][1];
		for(int i=1;i<pixel_array.length;i++){
			if(pixel_array[i][0]>right){
				right = pixel_array[i][0];
			}
			if(pixel_array[i][0]<left){
				left = pixel_array[i][0];
			}
			if(pixel_array[i][1]>bottom){
				bottom = pixel_array[i][1];
			}
			if(pixel_array[i][1]<top){
				top = pixel_array[i][1];
			}
		}
		if(top<30){
			no_type = "脚型上边界超出！";
			return false;
		}
		if(left<30){
			no_type = "脚型左边界超出！";
			return false;
		}
		if(bottom>temp_height-30){
			no_type = "脚型下边界超出！";
			return false;
		}
		if(right>temp_width-30){
			no_type = "脚型右边界超出！";
			return false;
		}													//至此得到最贴近脚型区域的上下左右边界范围
		top = top-50;										//拓展上下左右的背景图片的区域
		if(top<0){
			top = 0;
		}
		bottom = bottom+50;
		if(bottom>temp_height-1){
			bottom = temp_height-1;
		}
		left = left-50;
		if(left<0){
			left = 0;
		}
		right = right+50;
		if(right>temp_width-1){
			right = temp_width-1;
		}													//保存到数据库的为已经扩展的四边，将这四个参数存储在数据库当中!!!
		double scaleX = this.getWidth()*1.0/(right-left);
		double scaleY = this.getHeight()*1.0/(bottom-top);
		scale = (scaleX>scaleY)?scaleY:scaleX;
		
		int[] pixels = new int[(right-left)*(bottom-top)];
		stampImage.getRGB(left,top,right-left,bottom-top,pixels,0,right-left);
		BufferedImage stampAimImage = new BufferedImage(right-left,bottom-top,stampImage.getType());
		stampAimImage.setRGB(0,0,right-left,bottom-top,pixels,0,right-left);
		tempImage = null;
		tempImage = scaleFilter(stampAimImage,scale);		//重新的到bufImage，此时的bufImage是stampImage的缩放版
		//简单边缘算法
		SimpleEdgeFilter filter_edge = new SimpleEdgeFilter();
		tempImage = filter_edge.process(tempImage);
		//重新执行获取边缘像素数组的算法
		getPixelArray();									//得到第二次的脚型边缘像素数组，重新得到list
		
		originalBufImage.getRGB(left,top,right-left,bottom-top,pixels,0,right-left);
		BufferedImage origAimBufImage = new BufferedImage(right-left,bottom-top,originalBufImage.getType());
		origAimBufImage.setRGB(0,0,right-left,bottom-top,pixels,0,right-left);
		bufImage = scaleFilter(origAimBufImage,scale);		//得到最终的显示像素图
		
		orig_x = (this.getWidth() - bufImage.getWidth())/2;
		orig_y = (this.getHeight() - bufImage.getHeight())/2;
		if(pixel_array!=null&&pixel_array.length!=0){		//绘制蚂蚁线路径
			int half_size = pixel_array.length/2;
    		
    		int first_x = orig_x+pixel_array[0][0];
    		int first_y = orig_y+pixel_array[0][1];
    		
    		gPath = new GeneralPath();
        	gPath.moveTo(first_x,first_y);
        	int k = 0;			
        	for(int i=1;i<pixel_array.length;i++){
        		k++;
        		if(k==10||i==half_size||i==half_size-1||i==pixel_array.length-1){
        			gPath.lineTo(orig_x+pixel_array[i][0], orig_y+pixel_array[i][1]);
                    gPath.moveTo(orig_x+pixel_array[i][0], orig_y+pixel_array[i][1]);
                	k = 0;
        		}
        	}
        	gPath.lineTo(first_x,first_y);
        	
        	getCoordInfo();									//计算脚长的参考坐标点.要用到pixel_array
        	analysing = false;
    		//第二阶段完（分析图像）//第三阶段，显示脚型轮廓蚂蚁线和目标脚型区域
        	showPath = true;
    	}
		return true;
	}
	private void getPixelArray(){
		pixel_array = null;
		ArrayList<PixelPoint> list_left = new ArrayList<PixelPoint>();
		ArrayList<PixelPoint> list_right = new ArrayList<PixelPoint>();
		for(int i=20;i<tempImage.getHeight()-20;i++){		//忽略掉边缘扩展的20像素,就是节省时间
			boolean exist = false;
			int cur_i = i;
			int max_j = 0;
			int min_j = 0;
			for(int j=20;j<tempImage.getWidth()-20;j++){	//判断水平方向上的最左和最右两点
				if((tempImage.getRGB(j,i) & 0xff)==0xff){	//白色的像素为底色，非边缘线，跳出，执行下一次循环。
					continue;
				}
				if(exist==false){							//到这一步为黑色边缘线像素。。。。
					max_j = j;
					min_j = j;
					exist = true;
					continue;
				}
				if(exist==true){
					max_j = j;
				}
			}
			if(exist){
				if(min_j!=max_j){
					PixelPoint point_min = new PixelPoint();
					point_min.setPixel_x(min_j);
					point_min.setPixel_y(cur_i);
					list_left.add(point_min);
					PixelPoint point_max = new PixelPoint();
					point_max.setPixel_x(max_j);
					point_max.setPixel_y(cur_i);
					list_right.add(point_max);
				}
			}
		}
		for(int i=list_right.size()-1;i>=0;i--){			//组合两个左右数链，也就是逆时针画脚型
			list_left.add(list_right.get(i));
		}
		System.out.println("all of pixels of foot1："+list_left.size());	
		pixel_array = new int[list_left.size()][2];			//将数据链放在一个二维数组中
		System.out.println("all of pixels of foot2："+pixel_array.length);
		Iterator<PixelPoint> iterator = list_left.iterator();
		PixelPoint point = null;
		for(int i=0;iterator.hasNext();i++){
			point = iterator.next();
			pixel_array[i][0] = point.getPixel_x();			//水平方向
			pixel_array[i][1] = point.getPixel_y();			//垂直方向
			point = null;
		}
	}
    private BufferedImage scaleFilter(BufferedImage buf_image,double scale) {
		BufferedImage filteredBufImage = new BufferedImage((int) (buf_image.getWidth()*scale),(int) (buf_image.getHeight()*scale),buf_image.getType());
		AffineTransform transform = new AffineTransform(); 							//仿射变换对象
		transform.setToScale(scale, scale); 										//设置仿射变换的比例因子
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);			//创建仿射变换操作对象
		imageOp.filter(buf_image, filteredBufImage);								//过滤图像，目标图像在filteredBufImage
		return filteredBufImage;
	}
    
	/**理论上的点位：(特定环境下)（脚跟在下，脚趾在上）
	 * circle_02为脚跟的位置
	 * circle_01为脚中趾的位置
	 * circle_03为脚大拇趾的位置
	 * */
	private void getCoordInfo(){		
		int pixel_count = pixel_array.length;
		//特定扫描环境下：脚跟在下
		circle_02.x = (pixel_array[pixel_count/2][0]+pixel_array[pixel_count/2-1][0])/2;
		circle_02.y = (pixel_array[pixel_count/2][1]+pixel_array[pixel_count/2-1][1])/2;
		//判断这两点是否为中指和大拇指所在
		int center_length = Math.abs(pixel_array[0][0]-pixel_array[pixel_array.length-1][0]);
		if(foot.equals("left")){
			circle_01.x = pixel_array[0][0];
			circle_01.y = pixel_array[0][1];
			circle_03.x = pixel_array[pixel_count-1][0];
			circle_03.y = pixel_array[pixel_count-1][1];
		}else if(foot.equals("right")){
			circle_01.x = pixel_array[pixel_count-1][0];
			circle_01.y = pixel_array[pixel_count-1][1];
			circle_03.x = pixel_array[0][0];
			circle_03.y = pixel_array[0][1];
		}
		System.out.println("footCoord center_length:"+center_length);
		int point;
		int lastx;
		int differ = (int)(pixel_array.length/2*0.07);	//遍历百分比长度
		System.out.println("footCoord left or right traverse："+differ);
		//判断左边的最大段位//逆时针
		int left_max_length = 0;
		point = 0;
		int left_max_x = pixel_array[point][0];
		int left_max_y = pixel_array[point][1];
		lastx = pixel_array[point][0];
		for(int i=0;i<differ;i++){
			int curx = pixel_array[point+i][0];
			if(Math.abs(lastx-curx)>left_max_length){
				left_max_length = Math.abs(lastx-curx);
				left_max_x = pixel_array[point+i][0];
				left_max_y = pixel_array[point+i][1];
			}
			lastx = curx;
		}
		System.out.println("footCoord left_max_length:"+left_max_length);
		if(center_length<left_max_length){
			if(foot.equals("left")){
				circle_01.x = left_max_x;
				circle_01.y = left_max_y;
				circle_03.x = (pixel_array[0][0]+pixel_array[pixel_count-1][0])/2;
				circle_03.y = (pixel_array[0][1]+pixel_array[pixel_count-1][1])/2;
			}else if(foot.equals("right")){
				circle_01.x = (pixel_array[0][0]+pixel_array[pixel_count-1][0])/2;
				circle_01.y = (pixel_array[0][1]+pixel_array[pixel_count-1][1])/2;
				circle_03.x = left_max_x;
				circle_03.y = left_max_y;
			}
		}else{
			left_max_length = center_length;
		}
		//判断右边的最大段位//顺时针
		int right_max_length = 0;
		point = pixel_array.length-1;
		int right_max_x = pixel_array[point][0];
		int right_max_y = pixel_array[point][1];
		lastx = pixel_array[point][0];
		for(int i=0;i<differ;i++){
			int curx = pixel_array[point-i][0];
			if(Math.abs(lastx-curx)>right_max_length){
				right_max_length = Math.abs(lastx-curx);
				right_max_x = pixel_array[point-i][0];
				right_max_y = pixel_array[point-i][1];
			}
			lastx = curx;
		}
		System.out.println("footCoord right_max_length:"+right_max_length);
		if(left_max_length<right_max_length){
			if(foot.equals("left")){
				circle_01.x = (pixel_array[0][0]+pixel_array[pixel_count-1][0])/2;
				circle_01.y = (pixel_array[0][1]+pixel_array[pixel_count-1][1])/2;
				circle_03.x = right_max_x;
				circle_03.y = right_max_y;
			}else if(foot.equals("right")){
				circle_01.x = right_max_x;
				circle_01.y = right_max_y;
				circle_03.x = (pixel_array[0][0]+pixel_array[pixel_count-1][0])/2;
				circle_03.y = (pixel_array[0][1]+pixel_array[pixel_count-1][1])/2;
			}
		}
		//为这三个坐标添加相对位移量。
		circle_01.x = circle_01.x+orig_x;
		circle_01.y = circle_01.y+orig_y;
		circle_02.x = circle_02.x+orig_x;
		circle_02.y = circle_02.y+orig_y;
		circle_03.x = circle_03.x+orig_x;
		circle_03.y = circle_03.y+orig_y;
		
		float footLength = 0;									//定义脚长mm(为circle_01到circle_02的两点)
		if(circle_01.x == circle_02.x){							//如果分子为0(垂直线)!!，不合语法的方程，经测试，该情况下，直线不能够显示。
			line_foot.x1 = circle_01.x;							//定义line_foot：脚长辅助线(中轴线)
			line_foot.x2 = circle_02.x;
			line_foot.y1 = circle_01.y-80;
			line_foot.y2 = circle_02.y+80;
			
			line_top.y1 = circle_03.y;							//定义 line_top：垂直于脚长辅助线相切于脚拇趾的辅助线
			line_top.y2 = circle_03.y;
			if(foot.equals("left")){
				line_top.x1 = circle_01.x-60;
				line_top.x2 = circle_03.x+60;
			}else{
				line_top.x1 = circle_01.x+60;
				line_top.x2 = circle_03.x-60;
			}
			fpoint_01.x = circle_01.x;							//定义fpoint_01:line_foot与line_top的相交点
			fpoint_01.y = circle_03.y;
			line_heel.x1 = circle_02.x-100;						//定义line_heel：相交于脚跟circle_02相切与脚跟的辅助线
			line_heel.x2 = circle_02.x+100;
			line_heel.y1 = circle_02.y;
			line_heel.y2 = circle_02.y;
			float length_01 = Math.abs(fpoint_01.y-circle_02.y);//脚跟到circle_02到fpoint_01的距离
			float length_03 = Math.abs(circle_01.y-circle_02.y);//脚跟到circle_02到circle_01的距离
			footLength = length_01>length_03?length_01:length_03;
			//求脚型的斜宽,靠近第一跖趾的地方72.5，第五跖趾一侧为63.5, 同时确定腰窝41%,时的绿色参考坐标
			fpoint_725.x = circle_01.x;
			fpoint_635.x = circle_01.x;
			fpoint_41.x = circle_01.x;
			fpoint_18.x = circle_01.x;
			fpoint_90.x = circle_01.x;
			fpoint_825.x = circle_01.x;
			fpoint_78.x = circle_01.x;
			fpoint_80.x = circle_01.x;
			fpoint_65.x = circle_01.x;
			fpoint_725.y = circle_02.y-footLength*0.725f;
			fpoint_635.y = circle_02.y-footLength*0.635f;
			fpoint_41.y = circle_02.y-footLength*0.41f;
			fpoint_18.y = circle_02.y-footLength*0.18f;
			fpoint_90.y = circle_02.y-footLength*0.90f;
			fpoint_825.y = circle_02.y-footLength*0.825f;
			fpoint_78.y = circle_02.y-footLength*0.78f;
			fpoint_80.y = circle_02.y-footLength*0.80f;
			fpoint_65.y = circle_02.y-footLength*0.65f;
			/**	求出相交于fpoint_725，垂直于line_foot到脚型边缘的辅助线line_725
				求出相交于fpoint_635，垂直于line_foot到脚型边缘的辅助线line_635
				要判断出两条线的左右侧（即为脚中趾与脚拇趾的左右关系）******/
			line_635.x1 = fpoint_635.x;
			line_635.y1 = fpoint_635.y;
			line_725.x1 = fpoint_725.x;
			line_725.y1 = fpoint_725.y;
			line_90.x1 = fpoint_90.x;
			line_90.y1 = fpoint_90.y;
			line_825.x1 = fpoint_825.x;
			line_825.y1 = fpoint_825.y;
			line_78.x1 = fpoint_78.x;
			line_78.y1 = fpoint_78.y;
			line_635.y2 = fpoint_635.y;
			line_725.y2 = fpoint_725.y;
			line_90.y2 = fpoint_90.y;
			line_825.y2 = fpoint_825.y;
			line_78.y2 = fpoint_78.y;
			if(foot.equals("left")){
				float min_635 = Math.abs(pixel_array[0][1]+orig_y-fpoint_635.y);
				float min_825 = Math.abs(pixel_array[0][1]+orig_y-fpoint_825.y);
				float min_78 = Math.abs(pixel_array[0][1]+orig_y-fpoint_78.y);
				line_635.x2 = pixel_array[0][0]+orig_x;
				line_825.x2=pixel_array[0][0]+orig_x;
				line_78.x2=pixel_array[0][0]+orig_x;
				for(int i=1;i<pixel_count/2;i++){
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_635.y)<min_635){
						min_635 = Math.abs(pixel_array[i][1]+orig_y-fpoint_635.y);
						line_635.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_825.y)<min_825){
						min_825 = Math.abs(pixel_array[i][1]+orig_y-fpoint_825.y);
						line_825.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_78.y)<min_78){
						min_78 = Math.abs(pixel_array[i][1]+orig_y-fpoint_78.y);
						line_78.x2 = pixel_array[i][0]+orig_x;
					}
				}
				float min_725 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_725.y);
				float min_90 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_90.y);
				float min_80 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_80.y); 
				float min_65 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_65.y); 
				line_725.x2 = pixel_array[pixel_count/2][0]+orig_x;
				line_90.x2 = pixel_array[pixel_count/2][0]+orig_x;
				circle_80.x = pixel_array[pixel_count/2][0]+orig_x;
				circle_65.x = pixel_array[pixel_count/2][0]+orig_x;
				for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_725.y)<min_725){
						min_725 = Math.abs(pixel_array[i][1]+orig_y-fpoint_725.y);
						line_725.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_90.y)<min_90){
						min_90 = Math.abs(pixel_array[i][1]+orig_y-fpoint_90.y);
						line_90.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_80.y)<min_80){
						min_80 = Math.abs(pixel_array[i][1]+orig_y-fpoint_80.y);
						circle_80.x = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_65.y)<min_65){
						min_65 = Math.abs(pixel_array[i][1]+orig_y-fpoint_65.y);
						circle_65.x = pixel_array[i][0]+orig_x;
					}
				}
			}else{
				float min_635 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_635.y);
				float min_825 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_825.y);
				float min_78 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_78.y);
				line_635.x2 = pixel_array[pixel_count/2][0]+orig_x;
				line_825.x2 = pixel_array[pixel_count/2][0]+orig_x;
				line_78.x2 = pixel_array[pixel_count/2][0]+orig_x;
				for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_635.y)<min_635){
						min_635 = Math.abs(pixel_array[i][1]+orig_y-fpoint_635.y);
						line_635.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_825.y)<min_825){
						min_825 = Math.abs(pixel_array[i][1]+orig_y-fpoint_825.y);
						line_825.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_78.y)<min_78){
						min_78 = Math.abs(pixel_array[i][1]+orig_y-fpoint_78.y);
						line_78.x2 = pixel_array[i][0]+orig_x;
					}
				}
				float min_725 = Math.abs(pixel_array[0][1]+orig_y-fpoint_725.y);
				float min_90 = Math.abs(pixel_array[0][1]+orig_y-fpoint_90.y);
				float min_80 = Math.abs(pixel_array[0][1]+orig_y-fpoint_80.y);
				float min_65 = Math.abs(pixel_array[0][1]+orig_y-fpoint_65.y);
				line_725.x2 = pixel_array[0][0]+orig_x;
				line_90.x2 = pixel_array[0][0]+orig_x;
				circle_80.x = pixel_array[0][0]+orig_x;
				circle_65.x = pixel_array[0][0]+orig_x;
				for(int i=1;i<pixel_count/2;i++){
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_725.y)<min_725){
						min_725 = Math.abs(pixel_array[i][1]+orig_y-fpoint_725.y);
						line_725.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_90.y)<min_90){
						min_90 = Math.abs(pixel_array[i][1]+orig_y-fpoint_90.y);
						line_90.x2 = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_80.y)<min_80){
						min_80 = Math.abs(pixel_array[i][1]+orig_y-fpoint_80.y);
						circle_80.x = pixel_array[i][0]+orig_x;
					}
					if(Math.abs(pixel_array[i][1]+orig_y-fpoint_65.y)<min_65){
						min_65 = Math.abs(pixel_array[i][1]+orig_y-fpoint_65.y);
						circle_65.x = pixel_array[i][0]+orig_x;
					}
					
				}
			}
			circle_635.x = line_635.x2;						//确定左右脚宽的可调节坐标圈，即：circle_635、circle_725
			circle_635.y = line_635.y2;
			circle_725.x = line_725.x2;
			circle_725.y = line_725.y2;
			circle_90.x = line_90.x2;
			circle_90.y = line_90.y2;
			circle_825.x = line_825.x2;
			circle_825.y = line_825.y2;
			circle_78.x = line_78.x2;
			circle_78.y = line_78.y2;
			fpoint_725_red.x = fpoint_725.x;				//初始垂直中轴线时的，fpoint_XXX_red
			fpoint_725_red.y = fpoint_725.y;
			fpoint_635_red.x = fpoint_635.x;
			fpoint_635_red.y = fpoint_635.y;
			fpoint_90_red.x = fpoint_90.x;
			fpoint_90_red.y = fpoint_90.y;
			fpoint_825_red.x = fpoint_825.x;
			fpoint_825_red.y = fpoint_825.y;
			fpoint_78_red.x = fpoint_78.x;
			fpoint_78_red.y = fpoint_78.y;
			//k68=(circle_725.y-circle_635.y)/(circle_635.x-circle_635.x)确定斜宽的斜率
			//确定斜宽方程：y=k68*(x-circle_725.x)+circle_725.y;
			float k68 = (circle_725.y-circle_635.y)/(circle_725.x-circle_635.x);
			fpoint_68_red.x = circle_01.x;
			fpoint_68_red.y = k68*(fpoint_68_red.x-circle_725.x)+circle_725.y;
			line_68.x1 = circle_635.x;						//踵心辅助线
			line_68.y1 = circle_635.y;
			line_68.x2 = circle_725.x;
			line_68.y2 = circle_725.y;
			
			line_41.y1 = fpoint_41.y;						//确定！段位内宽 ！的辅助线和两端的参考可调节坐标的位置(水平线段下)
			line_41.y2 = fpoint_41.y;
			float min_41 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-fpoint_41.y);
			line_41.x2 = pixel_array[pixel_count/2][0]+orig_x;
			for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
				if(Math.abs(pixel_array[i][1]+orig_y-fpoint_41.y)<min_41){
					min_41 = Math.abs(pixel_array[i][1]+orig_y-fpoint_41.y);
					line_41.x2 = pixel_array[i][0]+orig_x;
				}
			}
			min_41 = Math.abs(pixel_array[0][1]+orig_y-fpoint_41.y);
			line_41.x1 = pixel_array[0][0]+orig_x;
			for(int i=1;i<pixel_count/2;i++){
				if(Math.abs(pixel_array[i][1]+orig_y-fpoint_41.y)<min_41){
					min_41 = Math.abs(pixel_array[i][1]+orig_y-fpoint_41.y);
					line_41.x1 = pixel_array[i][0]+orig_x;
				}
			}
			if(fpoint_01.x>circle_03.x){					//中指在右侧，需要改变
				circle_4101.x = line_41.x1;
				circle_4101.y = line_41.y1;
				circle_4102.x = line_41.x2;
				circle_4102.y = line_41.y2;
			}else{
				circle_4101.x = line_41.x2;
				circle_4101.y = line_41.y2;
				circle_4102.x = line_41.x1;
				circle_4102.y = line_41.y1;
				line_41.x1 = circle_4101.x;
				line_41.y1 = circle_4101.y;
				line_41.x2 = circle_4102.x;
				line_41.y2 = circle_4102.y;
			}
			judgeBreak();									//确定段位内宽的受力处，用以判断足弓的趋势。
			break_01.y = fpoint_41.y;
			break_02.y = fpoint_41.y;	
			break_01.x1 = break_01.x;
			break_01.x2 = break_01.x;
			break_01.y1 = break_01.y-8;
			break_01.y2 = break_01.y+8;
			break_02.x1 = break_02.x;
			break_02.x2 = break_02.x;
			break_02.y1 = break_02.y-8;
			break_02.y2 = break_02.y+8;
			//System.out.println("分踵线的斜率是："+kf);
			//System.out.println("辅助线的斜率是："+kf_h);//该辅助线为fpoint_18到fpoint_18s的连线斜率
			//System.out.println("辅助线的斜率是："+kf_v);//该辅助线为垂直与分踵线的斜率
			float kf_h = 0;
			center_angle = dformat.format(Math.random()*3+4);
			float orig_angle = Float.parseFloat(center_angle);//[3,7)的float类型//单位：度数
			float angle = (180-orig_angle)/2;
			if(circle_01.y<circle_02.y){			//脚趾在上的情况（请注意：运算时的象限关系）
				if(fpoint_01.x>circle_03.x){		//中指在右侧，也就是说，分踵线在偏右侧
					kf = (float) Math.tan(Math.PI*(90+orig_angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90-angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}else{								//中指在左侧
					kf = (float) Math.tan(Math.PI*(90-orig_angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90+angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}
			}else{									//脚趾在下的情况
				if(fpoint_01.x>circle_03.x){		//中指在右侧，也就是说，分踵线在偏右侧
					kf = (float) Math.tan(Math.PI*(90-orig_angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90+angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}else{								//中指在左侧
					kf = (float) Math.tan(Math.PI*(90+orig_angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90-angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}
			}
			//kf方程交于circle_02的方程为  ： y=kf*(x-circle_02.x)+circle_02.y;
			//kh方程交于fpoint_18的方程为 ：  y=kh*(x-fpoint_18.x)+fpoint_18.y;
			fpoint_18s.x = (kf*circle_02.x-kf_h*fpoint_18.x+fpoint_18.y-circle_02.y)/(kf-kf_h);
			fpoint_18s.y = kf*(fpoint_18s.x-circle_02.x)+circle_02.y;
			line_f.x1 = circle_02.x;				//绘制分踵线：line_f
			line_f.y1 = circle_02.y;
			if(circle_01.y<circle_02.y){			//脚趾在上的情况
				line_f.y2 = fpoint_18s.y-100;
			}else{									//脚趾在下的情况
				line_f.y2 = fpoint_18s.y+100;
			}										//x=(y-circle_02.y)/kf+circle_02.x;求x方程
			line_f.x2 = (line_f.y2-circle_02.y)/kf+circle_02.x;
			circle_18.x = line_f.x2;
			circle_18.y = line_f.y2;
			kf_v = (-1)/kf;							//方程为: y=kf_v*(x-fpoint_18s.x)+fpoint_18s.y
			float min_18 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kf_v*(pixel_array[pixel_count/2][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
			line_18.x2 = pixel_array[pixel_count/2][0]+orig_x;
			float length_abs;
			for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
				length_abs = Math.abs(pixel_array[i][1]+orig_y-(kf_v*(pixel_array[i][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
				if(length_abs<min_18){
					min_18 = length_abs;
					line_18.x2 = pixel_array[i][0]+orig_x;
				}
			}
			min_18 = Math.abs(pixel_array[0][1]+orig_y-(kf_v*(pixel_array[0][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
			line_18.x1 = pixel_array[0][0]+orig_x;
			for(int i=1;i<pixel_count/2;i++){
				length_abs = Math.abs(pixel_array[i][1]+orig_y-(kf_v*(pixel_array[i][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
				if(length_abs<min_18){
					min_18 = length_abs;
					line_18.x1 = pixel_array[i][0]+orig_x;
				}
			}
			line_18.y1 = kf_v*(line_18.x1-fpoint_18s.x)+fpoint_18s.y;
			line_18.y2 = kf_v*(line_18.x2-fpoint_18s.x)+fpoint_18s.y;
			if(fpoint_01.x>circle_03.x){				//中指在右侧，需要改变
				circle_1801.x = line_18.x1;				//绘制踵心全宽，两个宽度可调节坐标圈
				circle_1801.y = line_18.y1;
				circle_1802.x = line_18.x2;
				circle_1802.y = line_18.y2;
			}else{
				circle_1801.x = line_18.x2;
				circle_1801.y = line_18.y2;
				circle_1802.x = line_18.x1;
				circle_1802.y = line_18.y1;
				line_18.x1 = circle_1801.x;
				line_18.y1 = circle_1801.y;
				line_18.x2 = circle_1802.x;
				line_18.y2 = circle_1802.y;
			}
		}
		/***
		 * 中轴线不是垂直线的情况下
		 * kz:中轴线的斜率
		 * kz_v:垂直于中轴线的斜率
		 * kf:分踵线的斜率
		 * kf_h:fpoint_18到fpoint_18s的连线斜率
		 * kf_v:垂直与分踵线的斜率
		 * */
		else if(circle_01.x != circle_02.x){
			kz = (circle_01.y-circle_02.y)/(circle_01.x-circle_02.x);
			//liney = k*(linex-circle_01.x)+circle_01.y;//linex = (liney-circle_01.y)/k+circle_01.x
			line_foot.y1 = circle_01.y-80;
			line_foot.y2 = circle_02.y+80;
			line_foot.x1 = (line_foot.y1 - circle_01.y)/kz+circle_01.x;
			line_foot.x2 = (line_foot.y2 - circle_01.y)/kz+circle_01.x;
			kz_v = (-1)/kz;									//辅助线的斜率
			/**垂直辅助线方程：liney=k1*(linex-circle_03.x)+circle_03.y;再求两条直线的交叉坐标点
			k1*(linex-circle_03.x)+circle_03.y = k*(linex-circle_01.x)+circle_01.y 
			转换为求出x坐标：linex=(k1*circle_03.x-k*circle_01.x+circle_01.y-circle_03.y)/(k1-k)求x的坐标方程
			然后带入任意方程求y ****/
			fpoint_01.x = (kz_v*circle_03.x-kz*circle_01.x+circle_01.y-circle_03.y)/(kz_v-kz);
			fpoint_01.y = kz*(fpoint_01.x-circle_01.x)+circle_01.y;
			
			if(foot.equals("left")){						//垂直辅助线的扩展线条
				line_top.x1 = circle_03.x+80;
				line_top.x2 = fpoint_01.x-80;
			}else{
				line_top.x1 = circle_03.x-80;
				line_top.x2 = fpoint_01.x+80;
			}
			line_top.y1 = (kz_v*(line_top.x1-circle_03.x)+circle_03.y);
			line_top.y2 = (kz_v*(line_top.x2-circle_03.x)+circle_03.y);
			
			line_heel.x1 = circle_02.x-100;					//注释部分改为如下四行，circle_02即为脚跟的点位，无需判断
			line_heel.x2 = circle_02.x+100;
			line_heel.y1 = kz_v*(line_heel.x1-circle_02.x)+circle_02.y;
			line_heel.y2 = kz_v*(line_heel.x2-circle_02.x)+circle_02.y;
			
			float length_01;									//求脚长,非水平线的情况下
			length_01 = (float) Math.sqrt((circle_02.x-fpoint_01.x)*(circle_02.x-fpoint_01.x)+(circle_02.y-fpoint_01.y)*(circle_02.y-fpoint_01.y));
			float length_03 = (float) Math.sqrt((circle_01.x-circle_02.x)*(circle_01.x-circle_02.x)+(circle_01.y-circle_02.y)*(circle_01.y-circle_02.y));
			footLength = length_01>length_03?length_01:length_03;
			/**求脚型的斜宽,靠近拇指的地方72.5，另一侧为63.5 ，前提：脚趾向上还是向下的，要画左侧还是右侧的线。
			需要用到line_foot的方程：liney=k*(linex-circle_01.x)+circle_01.y;直线方程
			(width*0.725f)*(width*0.725f)=(x-circle_01.x)*(x-circle_01.x)+(liney-circle_01.y)*(liney-circle_01.y);
			(width*0.725f)*(width*0.725f)=(x-circle_01.x)*(x-circle_01.x)+(k*x-k*circle_01.x)*(k*x-k*circle_01.x);
			放弃了二元二次方程算法，太过复杂！改为X轴方向上的等比例算法,,,没有用到width比例尺！！！！*/
			float x725;																	//circle_02即为脚跟，无需判断
			float x635;
			float x41;
			float x18;
			float x90;
			float x825;
			float x78;
			float x80;
			float x65;
			if(length_01<length_03){		//width为：circle_01.x到circle_02.x
				x725 = Math.abs(circle_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(circle_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(circle_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(circle_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(circle_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(circle_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(circle_01.x-circle_02.x)*0.78f;
				x80 = Math.abs(circle_01.x-circle_02.x)*0.80f;
				x65 = Math.abs(circle_01.x-circle_02.x)*0.65f;
			}else{							//width为：circle_02.x到fpoint_01.x
				x725 = Math.abs(fpoint_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(fpoint_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(fpoint_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(fpoint_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(fpoint_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(fpoint_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(fpoint_01.x-circle_02.x)*0.78f;
				x80 = Math.abs(fpoint_01.x-circle_02.x)*0.80f;
				x65 = Math.abs(fpoint_01.x-circle_02.x)*0.65f;
			}
			if(circle_01.x<circle_02.x){	//判断脚跟的左右方位
				fpoint_725.x = circle_02.x-x725;
				fpoint_635.x = circle_02.x-x635;
				fpoint_41.x = circle_02.x-x41;
				fpoint_18.x = circle_02.x-x18;
				fpoint_90.x = circle_02.x-x90;
				fpoint_825.x = circle_02.x-x825;
				fpoint_78.x = circle_02.x-x78;
				fpoint_80.x = circle_02.x-x80;
				fpoint_65.x = circle_02.x-x65;
			}else{
				fpoint_725.x = circle_02.x+x725;
				fpoint_635.x = circle_02.x+x635;
				fpoint_41.x = circle_02.x+x41;
				fpoint_18.x = circle_02.x+x18;
				fpoint_90.x = circle_02.x+x90;
				fpoint_825.x = circle_02.x+x825;
				fpoint_78.x = circle_02.x+x78;
				fpoint_80.x = circle_02.x+x80;
				fpoint_65.x = circle_02.x+x65;
			}
			fpoint_725.y = kz*(fpoint_725.x-circle_01.x)+circle_01.y;
			fpoint_635.y = kz*(fpoint_635.x-circle_01.x)+circle_01.y;
			fpoint_41.y = kz*(fpoint_41.x-circle_01.x)+circle_01.y;
			fpoint_18.y = kz*(fpoint_18.x-circle_01.x)+circle_01.y;
			fpoint_90.y = kz*(fpoint_90.x-circle_01.x)+circle_01.y;
			fpoint_825.y = kz*(fpoint_825.x-circle_01.x)+circle_01.y;
			fpoint_78.y = kz*(fpoint_78.x-circle_01.x)+circle_01.y;
			fpoint_80.y = kz*(fpoint_80.x-circle_01.x)+circle_01.y;
			fpoint_65.y = kz*(fpoint_65.x-circle_01.x)+circle_01.y;
			/**求出相交于fpoint_725，垂直于line_width到脚型边缘的辅助线line_725
			求出相交于fpoint_635，垂直于line_width到脚型边缘的辅助线line_635
			要判断出两条线的左右侧（即为脚中趾与脚拇趾的左右关系）！！！
			辅助线方程：liney=k1*(linex-line.x)+line.y;float k1=(-1)/k;再求两条直线的交叉坐标点
			******/
			line_635.x1 = fpoint_635.x;
			line_635.y1 = fpoint_635.y;
			line_725.x1 = fpoint_725.x;
			line_725.y1 = fpoint_725.y;
			line_90.x1 = fpoint_90.x;
			line_90.y1 = fpoint_90.y;
			line_825.x1 = fpoint_825.x;
			line_825.y1 = fpoint_825.y;
			line_78.x1 = fpoint_78.x;
			line_78.y1 = fpoint_78.y;
			float length_abs;
			if(foot.equals("left")){
				float min_635 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-line_635.x1)+line_635.y1));
				float min_825 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-line_825.x1)+line_825.y1));
				float min_78 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-line_78.x1)+line_78.y1));
				line_635.x2 = pixel_array[0][0]+orig_x;
				line_825.x2 = pixel_array[0][0]+orig_x;
				line_78.x2 = pixel_array[0][0]+orig_x;
				for(int i=1;i<pixel_count/2;i++){
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_635.x1)+line_635.y1));
					if(length_abs<min_635){
						min_635 = length_abs;
						line_635.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_825.x1)+line_825.y1));
					if(length_abs<min_825){
						min_825 = length_abs;
						line_825.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_78.x1)+line_78.y1));
					if(length_abs<min_78){
						min_78 = length_abs;
						line_78.x2 = pixel_array[i][0]+orig_x;
					}
				}
				float min_725 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-line_725.x1)+line_725.y1));
				float min_90 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-line_90.x1)+line_90.y1));
				float min_80 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-fpoint_80.x)+fpoint_80.y));
				float min_65 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-fpoint_65.x)+fpoint_65.y));
				line_725.x2 = pixel_array[pixel_count/2][0]+orig_x;
				line_90.x2 = pixel_array[pixel_count/2][0]+orig_x;
				circle_80.x = pixel_array[pixel_count/2][0]+orig_x;
				circle_65.x = pixel_array[pixel_count/2][0]+orig_x;
				for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_725.x1)+line_725.y1));
					if(length_abs<min_725){
						min_725 = length_abs;
						line_725.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_90.x1)+line_90.y1));
					if(length_abs<min_90){
						min_90 = length_abs;
						line_90.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-fpoint_80.x)+fpoint_80.y));
					if(length_abs<min_80){
						min_80 = length_abs;
						circle_80.x = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-fpoint_65.x)+fpoint_65.y));
					if(length_abs<min_65){
						min_65 = length_abs;
						circle_65.x = pixel_array[i][0]+orig_x;
					}
				}
			}else{
				float min_635 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-line_635.x1)+line_635.y1));
				float min_825 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-line_825.x1)+line_825.y1));
				float min_78 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-line_78.x1)+line_78.y1));
				line_635.x2 = pixel_array[pixel_count/2][0]+orig_x;
				line_825.x2 = pixel_array[pixel_count/2][0]+orig_x;
				line_78.x2 = pixel_array[pixel_count/2][0]+orig_x;
				for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_635.x1)+line_635.y1));
					if(length_abs<min_635){
						min_635 = length_abs;
						line_635.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_825.x1)+line_825.y1));
					if(length_abs<min_825){
						min_825 = length_abs;
						line_825.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_78.x1)+line_78.y1));
					if(length_abs<min_78){
						min_78 = length_abs;
						line_78.x2 = pixel_array[i][0]+orig_x;
					}
				}
				float min_725 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-line_725.x1)+line_725.y1));
				float min_90 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-line_90.x1)+line_90.y1));
				float min_80 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-fpoint_80.x)+fpoint_80.y));
				float min_65 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-fpoint_65.x)+fpoint_65.y));
				line_725.x2 = pixel_array[0][0]+orig_x;
				line_90.x2 = pixel_array[0][0]+orig_x;
				circle_80.x = pixel_array[0][0]+orig_x;
				circle_65.x = pixel_array[0][0]+orig_x;
				for(int i=1;i<pixel_count/2;i++){
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_725.x1)+line_725.y1));
					if(length_abs<min_725){
						min_725 = length_abs;
						line_725.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-line_90.x1)+line_90.y1));
					if(length_abs<min_90){
						min_90 = length_abs;
						line_90.x2 = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-fpoint_80.x)+fpoint_80.y));
					if(length_abs<min_80){
						min_80 = length_abs;
						circle_80.x = pixel_array[i][0]+orig_x;
					}
					length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-fpoint_65.x)+fpoint_65.y));
					if(length_abs<min_65){
						min_65 = length_abs;
						circle_65.x = pixel_array[i][0]+orig_x;
					}
				}
			}
			line_635.y2 = kz_v*(line_635.x2-line_635.x1)+line_635.y1;	//先确定了左右脚宽辅助线，再确定辅助线的两个端点
			line_725.y2 = kz_v*(line_725.x2-line_725.x1)+line_725.y1;
			line_90.y2 = kz_v*(line_90.x2-line_90.x1)+line_90.y1;
			line_825.y2 = kz_v*(line_825.x2-line_825.x1)+line_825.y1;
			line_78.y2 = kz_v*(line_78.x2-line_78.x1)+line_78.y1;
			circle_635.x = line_635.x2;									//赋值circle_XXX
			circle_635.y = line_635.y2;
			circle_725.x = line_725.x2;
			circle_725.y = line_725.y2;
			circle_90.x = line_90.x2;
			circle_90.y = line_90.y2;
			circle_825.x = line_825.x2;
			circle_825.y = line_825.y2;
			circle_78.x = line_78.x2;
			circle_78.y = line_78.y2;
			fpoint_725_red.x = line_725.x1;								//赋值fpoint_XXX_red
			fpoint_725_red.y = line_725.y1;
			fpoint_635_red.x = line_635.x1;
			fpoint_635_red.y = line_635.y1;
			fpoint_90_red.x = line_90.x1;
			fpoint_90_red.y = line_90.y1;
			fpoint_825_red.x = line_825.x1;
			fpoint_825_red.y = line_825.y1;
			fpoint_78_red.x = line_78.x1;
			fpoint_78_red.y = line_78.y1;
			circle_80.y = kz_v*(circle_80.x-fpoint_80.x)+fpoint_80.y;
			circle_65.y = kz_v*(circle_65.x-fpoint_65.x)+fpoint_65.y;
			//float k68=(circle_725.y-circle_635.y)/(circle_635.x-circle_635.x)确定斜宽的斜率
			//确定斜宽方程：y=k68*(x-circle_725.x)+circle_725.y;
			float k68 = (circle_725.y-circle_635.y)/(circle_725.x-circle_635.x);
			fpoint_68_red.x = (k68*circle_725.x-kz*circle_01.x+circle_01.y-circle_725.y)/(k68-kz);
			fpoint_68_red.y = k68*(fpoint_68_red.x-circle_725.x)+circle_725.y;
			line_68.x1 = circle_635.x;									//踵心辅助线
			line_68.y1 = circle_635.y;
			line_68.x2 = circle_725.x;
			line_68.y2 = circle_725.y;
			//确定！段位内宽 ！的辅助线和两端的参考可调节坐标的位置（非水平线段上）
			//要用到这两条相互垂直的方程: liney=k*(linex-circle_01.x)+circle_01.y;liney=k1*(linex-fpoint_41.x)+fpoint_41.y;
			float min_41 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kz_v*(pixel_array[pixel_count/2][0]+orig_x-fpoint_41.x)+fpoint_41.y)); 
			line_41.x2 = pixel_array[pixel_count/2][0]+orig_x;
			for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
				length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-fpoint_41.x)+fpoint_41.y));
				if(length_abs<min_41){
					min_41 = length_abs;
					line_41.x2 = pixel_array[i][0]+orig_x;
				}
			}
			min_41 = Math.abs(pixel_array[0][1]+orig_y-(kz_v*(pixel_array[0][0]+orig_x-fpoint_41.x)+fpoint_41.y));
			line_41.x1 = pixel_array[0][0]+orig_x;
			for(int i=1;i<pixel_count/2;i++){
				length_abs = Math.abs(pixel_array[i][1]+orig_y-(kz_v*(pixel_array[i][0]+orig_x-fpoint_41.x)+fpoint_41.y));
				if(length_abs<min_41){
					min_41 = length_abs;
					line_41.x1 = pixel_array[i][0]+orig_x;
				}
			}
			line_41.y1 = kz_v*(line_41.x1-fpoint_41.x)+fpoint_41.y;
			line_41.y2 = kz_v*(line_41.x2-fpoint_41.x)+fpoint_41.y;
			if(fpoint_01.x>circle_03.x){
				circle_4101.x = line_41.x1;
				circle_4101.y = line_41.y1;
				circle_4102.x = line_41.x2;
				circle_4102.y = line_41.y2;
			}else{
				circle_4101.x = line_41.x2;
				circle_4101.y = line_41.y2;
				circle_4102.x = line_41.x1;
				circle_4102.y = line_41.y1;
				line_41.x1 = circle_4101.x;
				line_41.y1 = circle_4101.y;
				line_41.x2 = circle_4102.x;
				line_41.y2 = circle_4102.y;
			}
			judgeBreak();									//确定段位内宽的受力处，用以判断足弓的趋势。
			break_01.y = kz_v*(break_01.x-fpoint_41.x)+fpoint_41.y;
			break_02.y = kz_v*(break_02.x-fpoint_41.x)+fpoint_41.y;
			break_01.y1 = break_01.y-8;						//要用到这两天相互垂直的方程: liney=k*(linex-break_01.x)+break_01.y;
			break_01.y2 = break_01.y+8;						//转换(liney-break_01.y)/k+break_01.x=linex;
			break_01.x1 = (break_01.y1-break_01.y)/kz+break_01.x;
			break_01.x2 = (break_01.y2-break_01.y)/kz+break_01.x;
			break_02.y1 = break_02.y-8;
			break_02.y2 = break_02.y+8;
			break_02.x1 = (break_02.y1-break_02.y)/kz+break_02.x;
			break_02.x2 = (break_02.y2-break_02.y)/kz+break_02.x;
			kf = 0;											//计算分踵线的相关辅助线和参考点
			float kf_h = 0;
			float tan7;
			float orig_angle;
			float tan865;
			while(true){
				center_angle = dformat.format(Math.random()*3+4);
				orig_angle = Float.parseFloat(center_angle);//[3,7)的float类型 ，7.0f;
				tan7 = (float) Math.tan(Math.PI*orig_angle/180);
				if(circle_01.y<circle_02.y){		//排除分踵线为垂直线的情况
					if(fpoint_01.x>circle_03.x){
						if((1-kz*tan7)==0){
							continue;
						}
					}else{
						if((1+kz*tan7)==0){
							continue;
						}
					}
				}else{
					if(fpoint_01.x>circle_03.x){
						if((1+kz*tan7)==0){
							continue;
						}
					}else{
						if((1-kz*tan7)==0){
							continue;
						}
					}
				}
				tan7 = (float) Math.tan(Math.PI*orig_angle/180);
				tan865 = (float) Math.tan(Math.PI*((180-orig_angle)/2)/180);
				break;
			}
			if(circle_01.y<circle_02.y){			//脚趾在上的情况（请注意：运算时的象限关系）
				if(fpoint_01.x>circle_03.x){		//中指在右侧，也就是说，分踵线在偏右侧
					kf = (kz+tan7)/(1-kz*tan7);		//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (kz-tan865)/(1+kz*tan865);//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}else{								//中指在左侧
					kf = (kz-tan7)/(1+kz*tan7);		//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (kz+tan865)/(1-kz*tan865);//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}
			}else{									//脚趾在下的情况
				if(fpoint_01.x>circle_03.x){		//中指在右侧，也就是说，分踵线在偏右侧
					kf = (kz-tan7)/(1+kz*tan7);		//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (kz+tan865)/(1-kz*tan865);//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}else{								//中指在左侧
					kf = (kz+tan7)/(1-kz*tan7);		//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (kz-tan865)/(1+kz*tan865);//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}
			}
			float a = (kz+kf)/(1-kz*kf);			//(K1+K2)/(1-K1K2)=2K/(1-K^2)
			float b = 2*kf_h/(1-kf_h*kf_h);
			System.out.println("验证等腰三角形三边斜率关系："+a+"/"+b);
			//kf方程交于circle_02的方程为  ： y=kf*(x-circle_02.x)+circle_02.y;
			//kh方程交于fpoint_18的方程为 ：  y=kh*(x-fpoint_18.x)+fpoint_18.y;
			fpoint_18s.x=(kf*circle_02.x-kf_h*fpoint_18.x+fpoint_18.y-circle_02.y)/(kf-kf_h);
			fpoint_18s.y=kf*(fpoint_18s.x-circle_02.x)+circle_02.y;
			line_f.x1=circle_02.x;					//绘制分踵线：line_f
			line_f.y1=circle_02.y;
			if(circle_01.y<circle_02.y){			//脚趾在上的情况
				line_f.y2=fpoint_18s.y-100;
			}else{									//脚趾在下的情况
				line_f.y2=fpoint_18s.y+100;
			}										//x=(y-circle_02.y)/kf+circle_02.x;求x方程
			line_f.x2=(line_f.y2-circle_02.y)/kf+circle_02.x;
			circle_18.x=line_f.x2;
			circle_18.y=line_f.y2;
			kf_v=(-1)/kf;							//方程为: y=kf_v*(x-fpoint_18s.x)+fpoint_18s.y
			float min_18 = Math.abs(pixel_array[pixel_count/2][1]+orig_y-(kf_v*(pixel_array[pixel_count/2][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
			line_18.x2 = pixel_array[pixel_count/2][0]+orig_x;
			for(int i=pixel_count/2+1;i<=pixel_count-1;i++){
				length_abs = Math.abs(pixel_array[i][1]+orig_y-(kf_v*(pixel_array[i][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
				if(length_abs<min_18){
					min_18 = length_abs;
					line_18.x2 = pixel_array[i][0]+orig_x;
				}
			}
			min_18 = Math.abs(pixel_array[0][1]+orig_y-(kf_v*(pixel_array[0][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
			line_18.x1 = pixel_array[0][0]+orig_x;
			for(int i=1;i<pixel_count/2;i++){
				length_abs = Math.abs(pixel_array[i][1]+orig_y-(kf_v*(pixel_array[i][0]+orig_x-fpoint_18s.x)+fpoint_18s.y));
				if(length_abs<min_18){
					min_18 = length_abs;
					line_18.x1 = pixel_array[i][0]+orig_x;
				}
			}
			line_18.y1 = kf_v*(line_18.x1-fpoint_18s.x)+fpoint_18s.y;
			line_18.y2 = kf_v*(line_18.x2-fpoint_18s.x)+fpoint_18s.y;
			if(fpoint_01.x>circle_03.x){				//中指在右侧，需要改变
				circle_1801.x = line_18.x1;				//绘制踵心全宽，两个宽度可调节坐标圈
				circle_1801.y = line_18.y1;
				circle_1802.x = line_18.x2;
				circle_1802.y = line_18.y2;
			}else{
				circle_1801.x = line_18.x2;
				circle_1801.y = line_18.y2;
				circle_1802.x = line_18.x1;
				circle_1802.y = line_18.y1;
				line_18.x1 = circle_1801.x;
				line_18.y1 = circle_1801.y;
				line_18.x2 = circle_1802.x;
				line_18.y2 = circle_1802.y;
			}
		}
		//公共部分：不分中轴线是否为垂线
		//开始计算斜边夹角，并将所有的参数初始化赋值到表格中
		if(circle_725.x==circle_80.x){				// line_80为垂直线
			line_80.x1 = circle_80.x;
			line_80.x2 = circle_725.x;
			if(circle_725.y>circle_80.y){			// circle_725和circle_80的上下位置关系
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
		}else{
			float k80 = (circle_725.y-circle_80.y)/(circle_725.x-circle_80.x);
			//方程为：y=k80*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k80+circle_725.x
			if(circle_725.y>circle_80.y){			// circle_725和circle_80的上下位置关系
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
			line_80.x1 = (line_80.y1-circle_725.y)/k80+circle_725.x;
			line_80.x2 = (line_80.y2-circle_725.y)/k80+circle_725.x;
		}
		if(circle_725.x==circle_65.x){				// line_65为垂直线
			line_65.x1 = circle_65.x;
			line_65.x2 = circle_725.x;
			if(circle_725.y>circle_65.y){			// circle_725和circle_80的上下位置关系
				line_65.y1 = circle_65.y-50;
				line_65.y2 = circle_725.y+100;
			}else{
				line_65.y1 = circle_65.y+50;
				line_65.y2 = circle_725.y-100;
			}
		}else{
			float k65 = (circle_725.y-circle_65.y)/(circle_725.x-circle_65.x);
			//方程为：y=k65*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k65+circle_725.x
			if(circle_725.y>circle_65.y){			// circle_725和circle_80的上下位置关系
				line_65.y1 = circle_65.y-50;
				line_65.y2 = circle_725.y+100;
			}else{
				line_65.y1 = circle_65.y+50;
				line_65.y2 = circle_725.y-100;
			}
			line_65.x1 = (line_65.y1-circle_725.y)/k65+circle_725.x;
			line_65.x2 = (line_65.y2-circle_725.y)/k65+circle_725.x;
		}
		double a = getTwoCoordLength(circle_725.x,circle_725.y,line_80.x1,line_80.y1);
		double b = getTwoCoordLength(circle_725.x,circle_725.y,line_65.x2,line_65.y2);
		double c = getTwoCoordLength(line_80.x1,line_80.y1,line_65.x2,line_65.y2);
		double cos_angle = (a*a+b*b-c*c)/(2*a*b);
		double arccos_angle = Math.acos(cos_angle);
		double pi_cos_angle = (arccos_angle/Math.PI)*180;
		incline_angle = dformat.format(pi_cos_angle);
		//获取初始未调整之前的数据...
		foot_length = getFoot_length(footLength);
		shoe_length = calcUtil.getFootLength_5(Float.parseFloat(foot_length));
		foot_width = getFoot_width();
		type_width = calcUtil.getWidthType(Integer.parseInt(shoe_length), Double.parseDouble(foot_width));
		foot_status = calcUtil.getFootStatus(getTwoCoordLength(break_01.x,break_01.y,break_02.x,break_02.y),line_41.getLength());
		foot_advice = calcUtil.getFootAdvice(foot_status);
	}
	private String getFoot_length(float footLength){
		String foot_length_str = calcUtil.getFootLength_mm(footLength,dpi,scale);
		if(foot_length_pre==null||foot_length_pre.equals("")){
			return foot_length_str;
		}else{
			float length_float = Float.parseFloat(foot_length_str);//预测数据区间为上一次数据的+(0.0,1.0)
			float length_pre = Float.parseFloat(foot_length_pre);
			if(length_pre<length_float&&length_float<length_pre+1){
				return foot_length_str;
			}else{
				// modify by kael
				float l=(float) (length_pre+0.25+Math.random()*0.5);
				float num=(float)(Math.round(l*100.0)/100.0);
				return String.valueOf(num);
				// modify by kael over
//				return String.valueOf(length_pre+0.25+Math.random()*0.5);//否则返回上一数据+(0.25,0.75)
			}
		}
	}
	private String getFoot_width(){
		String foot_width_str = calcUtil.getFootLength_mm(line_725.getLength()+line_635.getLength(),dpi,scale);
		if(foot_width_pre==null||foot_width_pre.equals("")){
			return foot_width_str;
		}else{
			float width_float = Float.parseFloat(foot_width_str);//预测数据区间为上一次数据的+(0.0,0.5)
			float width_pre = Float.parseFloat(foot_width_pre);
			if(width_pre<width_float&&width_float<width_pre+0.5){
				return foot_width_str;
			}else{
				// modify by kael
				float w=(float) (width_pre+0.25+Math.random()*0.5);
				float num=(float)(Math.round(w*100.0)/100.0);
				return String.valueOf(num);
				// modify by kael over
//				return String.valueOf(width_pre+0.125+Math.random()*0.25);//否则返回上一数据+(0.125,0.375)
			}
		}
	}
	
	private void judgeBreak(){							//判断两个段位内点的X轴坐标
		float length_abs;
		Line line_break = new Line();					//类line_break用于辅助判定，（不进行显示！！！！）
		line_break.y1 = fpoint_41.y;					//计算出line_break水平线与脚型轮廓的交点
		line_break.y2 = fpoint_41.y;
		float min_break = Math.abs(pixel_array[pixel_array.length/2][1]+orig_y-line_break.y2);
		line_break.x2 = pixel_array[pixel_array.length/2][0]+orig_x;
		for(int i=pixel_array.length/2+1;i<=pixel_array.length-1;i++){
			length_abs = Math.abs(pixel_array[i][1]+orig_y-line_break.y2);
			if(length_abs<min_break){
				min_break = length_abs;
				line_break.x2 = pixel_array[i][0]+orig_x;
			}
		}
		min_break = Math.abs(pixel_array[0][1]+orig_y-line_break.y1);
		line_break.x1 = pixel_array[0][0]+orig_x;
		for(int i=1;i<=pixel_array.length/2;i++){
			length_abs = Math.abs(pixel_array[i][1]+orig_y-line_break.y1);
			if(length_abs<min_break){
				min_break = length_abs;
				line_break.x1 = pixel_array[i][0]+orig_x;
			}
		}
		int j = (int) fpoint_41.y-orig_y;
		int count_break = 0;
		float width = line_break.x2-line_break.x1;
		if(circle_635.x<circle_725.x){									//受力面积在中轴线的左边//从左到右
			for(int i=(int)line_break.x1+10;i<=(line_break.x2-width*0.2f);i++){
				if(IsBreakEdge(i-orig_x,j)){
					if(count_break==0){
						break_01.x = i;
						i = i+20;
						count_break++;
					}else if(count_break==1){
						break_02.x = i;
						i = i+20;
						count_break++;
					}else if(count_break==2){
						if((break_02.x-break_01.x)<width*0.2f){
							break_02.x = i;
							count_break++;
						}
						break;
					}
				}
			}
			if(count_break==0){
				System.out.println("left not find two break!");
				break_01.x = line_break.x1+width*0.1f;
				break_02.x = line_break.x1+width*0.6f;
			}else if(count_break==1){
				System.out.println("left not find one break!");
				if(break_01.x>(line_break.x1+width*0.2f)){
					break_01.x = line_break.x1+width*0.1f;
				}
				break_02.x = break_01.x+width*0.5f;
			}else if(count_break==2||count_break==3){
				System.out.println("left find two break!");
				if(break_01.x>(line_break.x1+width*0.2f)){
					break_01.x = line_break.x1+width*0.1f;
				}
				float bili = (break_02.x-break_01.x)/width;
				if(bili<0.375f||bili>0.625f){
					break_02.x = break_01.x+width*0.5f;
				}
			}
			System.out.println("left count_break:"+count_break);
		}else{															//受力面积在中轴线的右侧//从右到左
			for(int i=(int)line_break.x2-10;i<=(line_break.x1+width*0.2f);i--){
				if(IsBreakEdge(i-orig_x,j)){
					if(count_break==0){									//用一列数组记录
						break_02.x = i;
						i = i-20;
						count_break++;
					}else if(count_break==1){
						break_01.x = i;
						i = i-20;
						count_break++;
					}else if(count_break==2){
						if((break_02.x-break_01.x)<width*0.2f){
							break_01.x = i;
							count_break++;
						}
						break;
					}
				}
			}
			if(count_break==0){
				System.out.println("right not find two break!");
				break_02.x = line_break.x2-width*0.1f;
				break_01.x = line_break.x2-width*0.6f;
			}else if(count_break==1){
				System.out.println("right not find one break!");
				if(break_02.x<(line_break.x2-width*0.2f)){
					break_02.x = line_break.x2-width*0.1f;
				}
				break_01.x = break_02.x-width*0.5f;
			}else if(count_break==2||count_break==3){
				System.out.println("right find two break!");
				if(break_02.x<(line_break.x2-width*0.2f)){
					break_02.x = line_break.x2-width*0.1f;
				}
				float bili = (break_02.x-break_01.x)/width;
				if(bili<0.375f||bili>0.625f){
					break_01.x = break_02.x-width*0.5f;
				}
			}
			System.out.println("right count_break:"+count_break);
		}
	}
	private boolean IsBreakEdge(int x,int y){			//判断段位内点的边界点
		if(((bufImage.getRGB(x,y) & 0xff00 ) >> 8)<80){	//像素太低直接判断为false
			return false;
		}
		int left_Pcount = 0;			//左边像素之和
		int right_Pcount = 0;			//右边像素之和
		for(int i=5;i<15;i++){			//跳开前5，计算后10
			int pixel_left = bufImage.getRGB(x+i, y);	//比较左右两边的绿基色
			int green_left = (pixel_left & 0xff00 ) >> 8;
			left_Pcount = left_Pcount+green_left ;
			int pixel_right = bufImage.getRGB(x-i, y);
			int green_right = (pixel_right & 0xff00 ) >> 8;
			right_Pcount = right_Pcount+green_right ;
		}
		if(Math.abs(left_Pcount-right_Pcount)>100){
			System.out.println("left_Pcount:"+left_Pcount+",right_Pcount:"+right_Pcount);
			return true;
		}
		return false;
	}
	private int[] justCoord(int curx,int cury,float circlex,float circley){
		int[] xy = new int[2];
		float differx = Math.abs(curx-circlex);
		float differy = Math.abs(cury-circley);
		if(circlex<=curx&&cury<=circley){
			if(differy<=differx){
				xy[0] = 1;
			}else{
				xy[1] = -1;
			}
		}else if(curx<=circlex&&cury<=circley){
			if(differy<=differx){
				xy[0] = -1;
			}else{
				xy[1] = -1;
			}
		}else if(curx<=circlex&&circley<=cury){
			if(differy<=differx){
				xy[0] = -1;
			}else{
				xy[1] = 1;
			}
		}else if(circlex<=curx&&circley<=cury){
			if(differy<=differx){
				xy[0] = 1;
			}else{
				xy[1] = 1;
			}
		}
		return xy;
	}
	
	//当点击图片域的时候，影藏轮廓线，初始化和显示脚型参数坐标和辅助线
	class FootPaneMouseListener implements MouseListener{
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			int cur_x = e.getX();
	    	int cur_y = e.getY();
			if(!draging){
				drag_method = 1;
		    	//只有当拖动的坐标点距原始坐标小于5时，可拖动。//改为小于10
		    	if((int) Math.sqrt((cur_x-circle_01.x)*(cur_x-circle_01.x)+(cur_y-circle_01.y)*(cur_y-circle_01.y))<10){
		    		dragnum = 1;			//当拖动脚中趾，编号：1
		    		draging = true;
		    		cross_cursor.type = 1;
			    	cross_cursor.x = circle_01.x;
					cross_cursor.y = circle_01.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_02.x)*(cur_x-circle_02.x)+(cur_y-circle_02.y)*(cur_y-circle_02.y))<10){
		    		dragnum = 2;			//当拖动脚后跟，编号：2
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_02.x;
					cross_cursor.y = circle_02.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_03.x)*(cur_x-circle_03.x)+(cur_y-circle_03.y)*(cur_y-circle_03.y))<10){
		    		dragnum = 3;			//当拖动脚拇趾，编号：3
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_03.x;
					cross_cursor.y = circle_03.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_725.x)*(cur_x-circle_725.x)+(cur_y-circle_725.y)*(cur_y-circle_725.y))<10){
		    		dragnum = 4;			//当拖动第一跖趾，编号：4
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_725.x;
					cross_cursor.y = circle_725.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_635.x)*(cur_x-circle_635.x)+(cur_y-circle_635.y)*(cur_y-circle_635.y))<10){
		    		dragnum = 5;			//当拖动第五跖趾，编号：5
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_635.x;
					cross_cursor.y = circle_635.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_4101.x)*(cur_x-circle_4101.x)+(cur_y-circle_4101.y)*(cur_y-circle_4101.y))<10){
		    		dragnum = 6;			//当拖动断位外宽01，编号：6
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_4101.x;
					cross_cursor.y = circle_4101.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_4102.x)*(cur_x-circle_4102.x)+(cur_y-circle_4102.y)*(cur_y-circle_4102.y))<10){
		    		dragnum = 7;			//当拖动断位外宽02，编号：7
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_4102.x;
					cross_cursor.y = circle_4102.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-break_01.x)*(cur_x-break_01.x)+(cur_y-break_01.y)*(cur_y-break_01.y))<12){
		    		dragnum = 8;			//当拖动断位内宽01，编号：8
		    		draging = true;
		    		cross_cursor.type = 2;
		    		cross_cursor.x = break_01.x;
					cross_cursor.y = break_01.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-break_02.x)*(cur_x-break_02.x)+(cur_y-break_02.y)*(cur_y-break_02.y))<12){
		    		dragnum = 9;			//当拖动断位内宽02，编号：9
		    		draging = true;
		    		cross_cursor.type = 2;
		    		cross_cursor.x = break_02.x;
					cross_cursor.y = break_02.y;
					repaint();
		    	}
			}
			//当处于正在拖动状态时（参考点处于选中状态），使用微调整方式。
			else{
				drag_method = 2;
				if(kz == 0){
					slope_zhong();
				}
				if(kf == 0){
					slope_fen();
				}
				if(length_18 == 0){
					length_18();
				}
				if(dragnum == 1){
					if((int) Math.sqrt((cur_x-circle_01.x)*(cur_x-circle_01.x)+(cur_y-circle_01.y)*(cur_y-circle_01.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_01.x,circle_01.y);
						circle_01.x = circle_01.x+xy[0];
						circle_01.y = circle_01.y+xy[1];
						cross_cursor.x = circle_01.x;
						cross_cursor.y = circle_01.y;
						slope_zhong();
			    		line_foot();
			    		line_top();
			    		fpoint_01();
			    		line_heel();
			    		getLengthAndFpoint_xxx();
			    		length_18();
			    		fpoint_725_red();
			    		fpoint_635_red();
			    		fpoint_68_red();
			    		fpoint_90_red();
			    		fpoint_825_red();
			    		fpoint_78_red();
			    		line_725();
			    		line_635();
			    		line_90();
			    		line_825();
			    		line_78();
			    		circle_4101();
			    		circle_4102();
			    		break_01();
			    		break_02();
			    		slope_fen();
			    		fpoint_18s();
			    		line_f();
			    		circle_1801();
			    		circle_1802();
			    		footLength();
			    		footWidth();
			    		angle_du();
					}
				}else if(dragnum == 2){
					if((int) Math.sqrt((cur_x-circle_02.x)*(cur_x-circle_02.x)+(cur_y-circle_02.y)*(cur_y-circle_02.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_02.x,circle_02.y);
						circle_02.x = circle_02.x+xy[0];
						circle_02.y = circle_02.y+xy[1];
						cross_cursor.x = circle_02.x;
						cross_cursor.y = circle_02.y;
						slope_zhong();
			    		line_foot();
			    		line_top();
			    		fpoint_01();
			    		line_heel();
			    		getLengthAndFpoint_xxx();
			    		length_18();
			    		fpoint_725_red();
			    		fpoint_635_red();
			    		fpoint_68_red();
			    		fpoint_90_red();
			    		fpoint_825_red();
			    		fpoint_78_red();
			    		line_725();
			    		line_635();
			    		line_90();
			    		line_825();
			    		line_78();
			    		circle_4101();
			    		circle_4102();
			    		break_01();
			    		break_02();
			    		slope_fen();
			    		fpoint_18s();
			    		line_f();
			    		circle_1801();
			    		circle_1802();
			    		footLength();
			    		footWidth();
			    		angle_du();
					}
				}else if(dragnum == 3){
					if((int) Math.sqrt((cur_x-circle_03.x)*(cur_x-circle_03.x)+(cur_y-circle_03.y)*(cur_y-circle_03.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_03.x,circle_03.y);
						circle_03.x = circle_03.x+xy[0];
						circle_03.y = circle_03.y+xy[1];
						cross_cursor.x = circle_03.x;
						cross_cursor.y = circle_03.y;
						line_top();
			    		fpoint_01();
			    		line_heel();
			    		getLengthAndFpoint_xxx();
			    		length_18();
			    		circle_4101();
			    		circle_4102();
			    		break_01();
			    		break_02();
			    		slope_fen();
						fpoint_18s();
						line_f();
						circle_1801();
			    		circle_1802();
			    		footLength();
					}
				}else if(dragnum == 4){
					if((int) Math.sqrt((cur_x-circle_725.x)*(cur_x-circle_725.x)+(cur_y-circle_725.y)*(cur_y-circle_725.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_725.x,circle_725.y);
						circle_725.x = circle_725.x+xy[0];
						circle_725.y = circle_725.y+xy[1];
						cross_cursor.x = circle_725.x;
						cross_cursor.y = circle_725.y;
						differ_x725 = xy[0];
						differ_y725 = xy[1];
						incline_725x();
						incline_725y();
						fpoint_725_red();
						fpoint_68_red();
						line_725();
						line_68();
						footWidth();
					}
				}else if(dragnum == 5){
					if((int) Math.sqrt((cur_x-circle_635.x)*(cur_x-circle_635.x)+(cur_y-circle_635.y)*(cur_y-circle_635.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_635.x,circle_635.y);
						circle_635.x = circle_635.x+xy[0];
						circle_635.y = circle_635.y+xy[1];
						cross_cursor.x = circle_635.x;
						cross_cursor.y = circle_635.y;
						fpoint_635_red();
						fpoint_68_red();
						line_635();
						line_68();
						footWidth();
					}
				}else if(dragnum == 6){
					if((int) Math.sqrt((cur_x-circle_4101.x)*(cur_x-circle_4101.x)+(cur_y-circle_4101.y)*(cur_y-circle_4101.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_4101.x,circle_4101.y);
						circle_4101.x = circle_4101.x+xy[0];
						circle_4101.y = circle_4101.y+xy[1];
						cross_cursor.x = circle_4101.x;
						cross_cursor.y = circle_4101.y;
						circle_4101();
						footStatus();
					}
				}else if(dragnum == 7){
					if((int) Math.sqrt((cur_x-circle_4102.x)*(cur_x-circle_4102.x)+(cur_y-circle_4102.y)*(cur_y-circle_4102.y))<6){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_4102.x,circle_4102.y);
						circle_4102.x = circle_4102.x+xy[0];
						circle_4102.y = circle_4102.y+xy[1];
						cross_cursor.x = circle_4102.x;
						cross_cursor.y = circle_4102.y;
						circle_4102();
						footStatus();
					}
				}else if(dragnum == 8){
					if((int) Math.sqrt((cur_x-break_01.x)*(cur_x-break_01.x)+(cur_y-break_01.y)*(cur_y-break_01.y))<6){
						draging = false;
					}else{
						if(cur_x<break_01.x){
							break_01.x = break_01.x-1;
						}else{
							break_01.x = break_01.x+1;
						}
						break_01();
						cross_cursor.x = break_01.x;
						cross_cursor.y = break_01.y;
						footStatus();
					}
				}else if(dragnum == 9){
					if((int) Math.sqrt((cur_x-break_02.x)*(cur_x-break_02.x)+(cur_y-break_02.y)*(cur_y-break_02.y))<6){
						draging = false;
					}else{
						if(cur_x<break_02.x){
							break_02.x = break_02.x-1;
						}else{
							break_02.x = break_02.x+1;
						}
						break_02();
						cross_cursor.x = break_02.x;
						cross_cursor.y = break_02.y;
						footStatus();
					}
				}
				repaint();
				if(draging&&showData){
					updateData();
				}
			}
		}
		public void mouseReleased(MouseEvent e) {
			if(draged&&drag_method==1){
				if(draging){						//拖动参考坐标点，松开后，将拖动标志位置为true
					draging = false;
					draged = false;
					repaint();
				}
			}
		}
	}
	/**以下的运算需注意几个问题
	 * 1、circle_01在上，circle_02在下
	 * 2、circle_01和circle_03左右的问题
	 * 3、为了增加灵活性。判定了circle_01和circle_02谁才是脚跟的问题（理论上circle_01为脚中指，circle_02为脚后跟）
	 * 4、只有circle_03才是不变的
	 * */
	private class FootPaneMouseMotionListener implements MouseMotionListener{
		
		public void mouseDragged(MouseEvent e) {
			if(!draging||drag_method!=1){		//如果没有滑动坐标点，则返回。
				return;
			}
			draged = true;						//标志为已经启用拖动方法
			
			if(kz == 0){						//对于第一次，如果斜率为null，则进行初始化。
				slope_zhong();					//获取中轴线斜率和垂直线斜率
			}
			if(kf == 0){						//对于第一次，如果斜率为null，则进行初始化。
				slope_fen();					//获取中轴线斜率和垂直线斜率
			}
			if(length_18 == 0){
				length_18();
			}
			
			if(dragnum == 1){						//滑动circle_01脚中趾
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_01.x = e.getX();
				}
				if(0<=e.getY()&&e.getY()<=pane_height){
					circle_01.y = e.getY();
				}
				cross_cursor.x = circle_01.x;
				cross_cursor.y = circle_01.y;
	    		slope_zhong();
	    		line_foot();
	    		line_top();
	    		fpoint_01();
	    		line_heel();
	    		getLengthAndFpoint_xxx();
	    		length_18();
	    		fpoint_725_red();
	    		fpoint_635_red();
	    		fpoint_68_red();
	    		fpoint_90_red();
	    		fpoint_825_red();
	    		fpoint_78_red();
	    		line_725();
	    		line_635();
	    		line_90();
	    		line_825();
	    		line_78();
	    		circle_4101();
	    		circle_4102();
	    		break_01();
	    		break_02();
	    		slope_fen();
	    		fpoint_18s();
	    		line_f();
	    		circle_1801();
	    		circle_1802();
	    		footLength();
	    		footWidth();
	    		angle_du();
	    		repaint();
			}else if(dragnum == 2){				//滑动circle_02脚跟
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_02.x = e.getX();
				}
				if(0<=e.getY()&&e.getY()<=pane_height){
					circle_02.y = e.getY();
				}
				cross_cursor.x = circle_02.x;
				cross_cursor.y = circle_02.y;
	    		slope_zhong();
	    		line_foot();
	    		line_top();
	    		fpoint_01();
	    		line_heel();
	    		getLengthAndFpoint_xxx();
	    		length_18();
	    		fpoint_725_red();
	    		fpoint_635_red();
	    		fpoint_68_red();
	    		fpoint_90_red();
	    		fpoint_825_red();
	    		fpoint_78_red();
	    		line_725();
	    		line_635();
	    		line_90();
	    		line_825();
	    		line_78();
	    		circle_4101();
	    		circle_4102();
	    		break_01();
	    		break_02();
	    		slope_fen();
	    		fpoint_18s();
	    		line_f();
	    		circle_1801();
	    		circle_1802();
	    		footLength();
	    		footWidth();
	    		angle_du();
	    		repaint();
			}else if(dragnum == 3){				//滑动circle_03脚长辅助点
	    		if(0<=e.getX()&&e.getX()<=pane_width){
					circle_03.x = e.getX();
				}
				if(0<=e.getY()&&e.getY()<=pane_height){
					circle_03.y = e.getY();
				}
				cross_cursor.x = circle_03.x;
				cross_cursor.y = circle_03.y;
	    		line_top();
	    		fpoint_01();
	    		line_heel();
	    		getLengthAndFpoint_xxx();
	    		length_18();
	    		circle_4101();
	    		circle_4102();
	    		break_01();
	    		break_02();
	    		slope_fen();
				fpoint_18s();
				line_f();
				circle_1801();
	    		circle_1802();
	    		footLength();
	    		repaint();
			}else if(dragnum == 4){				//拖动第一跖趾circle_725
				if(0<=e.getX()&&e.getX()<=pane_width){
					differ_x725 = e.getX()-circle_725.x;
					circle_725.x = e.getX();
					incline_725x();
				}
				if(0<=e.getY()&&e.getY()<=pane_height){
					differ_y725 = e.getY()-circle_725.y;
					circle_725.y = e.getY();
					incline_725y();
				}
				cross_cursor.x = circle_725.x;
				cross_cursor.y = circle_725.y;
				fpoint_725_red();
				fpoint_68_red();
				line_725();
				line_68();
				footWidth();
				repaint();
			}else if(dragnum == 5){				//拖动第五跖趾circle_635
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_635.x = e.getX();
				}
				if(0<=e.getY()&&e.getY()<=pane_height){
					circle_635.y = e.getY();
				}
				cross_cursor.x = circle_635.x;
				cross_cursor.y = circle_635.y;
				fpoint_635_red();
				fpoint_68_red();
				line_635();
				line_68();
				footWidth();
				repaint();
			}else if(dragnum == 6){				//拖动腰窝点circle_4101				
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_4101.x = e.getX();
				}
				cross_cursor.x = circle_4101.x;
				cross_cursor.y = circle_4101.y;
				circle_4101();
				footStatus();
				repaint();
			}else if(dragnum == 7){				//拖动腰窝点circle_4102
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_4102.x = e.getX();
				}
				cross_cursor.x = circle_4102.x;
				cross_cursor.y = circle_4102.y;
				circle_4102();
				footStatus();
				repaint();
			}else if(dragnum == 8){				//拖动断位点break_01
				if(0<=e.getX()&&e.getX()<=pane_width){
					break_01.x = e.getX();
				}
				break_01();
				cross_cursor.x = break_01.x;
				cross_cursor.y = break_01.y;
				footStatus();
				repaint();
			}else if(dragnum == 9){				//拖动段位点break_02
				if(0<=e.getX()&&e.getX()<=pane_width){
					break_02.x = e.getX();
				}
				break_02();
				cross_cursor.x = break_02.x;
				cross_cursor.y = break_02.y;
				footStatus();
				repaint();
			}
			if(showData){
				updateData();
			}
		}
		public void mouseMoved(MouseEvent e) {}
	}
	private void slope_zhong(){
		if(circle_01.x==circle_02.x){								//如果中轴线为垂直线的话，斜率无穷大，跳出。
			return;
		}
		kz = (circle_01.y-circle_02.y)/(circle_01.x-circle_02.x);	//直线方程的斜率,直线方程:liney=k*(linex-circle_01.x)+circle_01.y;
		kz_v = (-1)/kz;												//垂直辅助线的斜率
	}
	private void slope_fen(){										//分踵线的斜率，以及垂直线的斜率
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){
			if(circle_18.x==circle_01.x){
				System.out.println("分踵线为垂直线");
			}else{
				kf=(circle_18.y-circle_01.y)/(circle_18.x-circle_01.x);
				kf_v=(-1)/kf;
			}
		}else{
			if(circle_18.x==circle_02.x){
				System.out.println("分踵线为垂直线");
			}else{
				kf=(circle_18.y-circle_02.y)/(circle_18.x-circle_02.x);
				kf_v=(-1)/kf;
			}
		}
	}
	private void line_foot(){
		if(circle_01.x==circle_02.x){								//如果垂直的中轴线的话
			line_foot.x1 = circle_01.x;
			line_foot.x2 = circle_02.x;
			if(circle_01.y>circle_02.y){
				line_foot.y1 = circle_01.y+80;
				line_foot.y2 = circle_02.y-80;
			}else{
				line_foot.y1 = circle_01.y-80;
				line_foot.y2 = circle_02.y+80;
			}
			return;
		}
		if(circle_01.x>circle_02.x){								//判断两个中轴端点X坐标的大小
			line_foot.x1 = circle_01.x+40;
			line_foot.x2 = circle_02.x-40;
		}else{
			line_foot.x1 = circle_01.x-40;
			line_foot.x2 = circle_02.x+40;
		}
		line_foot.y1 = (kz*(line_foot.x1-circle_01.x)+circle_01.y);
		line_foot.y2 = (kz*(line_foot.x2-circle_01.x)+circle_01.y);
	}
	private void line_top(){
		if(circle_01.x==circle_02.x){
			if(circle_01.x>circle_03.x){
				line_top.x1 = circle_01.x+60;
				line_top.x2 = circle_03.x-60;
			}else{
				line_top.x1 = circle_01.x-60;
				line_top.x2 = circle_03.x+60;
			}
			line_top.y1 = circle_03.y;
			line_top.y2 = circle_03.y;
			return;
		}
		if(circle_03.x>fpoint_01.x){
			line_top.x1 = circle_03.x+80;
			line_top.x2 = (int) (fpoint_01.x-80);
		}else{
			line_top.x1 = circle_03.x-80;
			line_top.x2 = (int) (fpoint_01.x+80);
		}
		line_top.y1 = (kz_v*(line_top.x1-circle_03.x)+circle_03.y);
		line_top.y2 = (kz_v*(line_top.x2-circle_03.x)+circle_03.y);
	}
	private void fpoint_01(){
		if(circle_01.x==circle_02.x){
			fpoint_01.x = circle_01.x;
			fpoint_01.y = circle_03.y;
			return;
		}
		//辅助线方程：liney=k1*(linex-circle_03.x)+circle_03.y;再求两条直线的交叉坐标点
		//k1*(linex-circle_03.x)+circle_03.y=k*(linex-circle_01.x)+circle_01.y 转换为求出x坐标，然后带入任意方程求y
		//linex=(k1*circle_03.x-k*circle_01.x+circle_01.y-circle_03.y)/(k1-k)求x的坐标方程
		fpoint_01.x = (kz_v*circle_03.x-kz*circle_01.x+circle_01.y-circle_03.y)/(kz_v-kz);
		fpoint_01.y = kz*(fpoint_01.x-circle_01.x)+circle_01.y;
	}
	private void line_heel(){
		if(circle_01.x==circle_02.x){
			if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){
				line_heel.x1 = circle_01.x-100;
				line_heel.x2 = circle_01.x+100;
				line_heel.y1 = circle_01.y;
				line_heel.y2 = circle_01.y;
			}else{
				line_heel.x1 = circle_02.x-100;
				line_heel.x2 = circle_02.x+100;
				line_heel.y1 = circle_02.y;
				line_heel.y2 = circle_02.y;
			}
			return;
		}
		//首先判断circle_01和circle_02谁为脚跟和脚趾
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){
			line_heel.x1 = circle_01.x-100;
			line_heel.x2 = circle_01.x+100;
			line_heel.y1 = kz_v*(line_heel.x1-circle_01.x)+circle_01.y;
			line_heel.y2 = kz_v*(line_heel.x2-circle_01.x)+circle_01.y;
		}else{
			line_heel.x1 = circle_02.x-100;
			line_heel.x2 = circle_02.x+100;
			line_heel.y1 = kz_v*(line_heel.x1-circle_02.x)+circle_02.y;
			line_heel.y2 = kz_v*(line_heel.x2-circle_02.x)+circle_02.y;
		}
	}
	private void getLengthAndFpoint_xxx(){							//获取脚长及跖趾百分点的位置
		if(circle_01.x==circle_02.x){
			float length_01 = Math.abs(fpoint_01.y-circle_01.y);
			float length_02 = Math.abs(fpoint_01.y-circle_02.y);
			length_01 = length_01<length_02?length_02:length_01;
			float length_03 = Math.abs(circle_01.y-circle_02.y);
			float length = length_01>length_03?length_01:length_03;
			//求脚型的斜宽,靠近拇指的地方72.5，另一侧为63.5, 两点前提：需要判断脚跟点；脚趾向上还是向下的，要画左侧还是右侧的线。
			fpoint_725.x = circle_01.x;
			fpoint_635.x = circle_01.x;
			fpoint_41.x = circle_01.x;
			fpoint_18.x = circle_01.x;
			fpoint_90.x = circle_01.x;
			fpoint_825.x = circle_01.x;
			fpoint_78.x = circle_01.x;
			if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){		//circle_01为   ~ 脚跟点
				if(circle_01.y<circle_02.y){		
					fpoint_725.y = circle_01.y+length*0.725f;//circle_01脚跟在上
					fpoint_635.y = circle_01.y+length*0.635f;
					fpoint_41.y = circle_01.y+length*0.41f;
					fpoint_18.y = circle_01.y+length*0.18f;
					fpoint_90.y = circle_01.y+length*0.90f;
					fpoint_825.y = circle_01.y+length*0.825f;
					fpoint_78.y = circle_01.y+length*0.78f;
				}else{								
					fpoint_725.y = circle_01.y-length*0.725f;//circle_01脚跟在下
					fpoint_635.y = circle_01.y-length*0.635f;
					fpoint_41.y = circle_01.y-length*0.41f;
					fpoint_18.y = circle_01.y-length*0.18f;
					fpoint_90.y = circle_01.y-length*0.90f;
					fpoint_825.y = circle_01.y-length*0.825f;
					fpoint_78.y = circle_01.y-length*0.78f;
				}
			}else{																			//否则circle_02为脚跟点
				if(circle_01.y<circle_02.y){
					fpoint_725.y = circle_02.y-length*0.725f;//circle_02脚跟在下
					fpoint_635.y = circle_02.y-length*0.635f;
					fpoint_41.y = circle_02.y-length*0.41f;
					fpoint_18.y = circle_02.y-length*0.18f;
					fpoint_90.y = circle_02.y-length*0.90f;
					fpoint_825.y = circle_02.y-length*0.825f;
					fpoint_78.y = circle_02.y-length*0.78f;
				}else{
					fpoint_725.y = circle_02.y+length*0.725f;//circle_02脚跟在上
					fpoint_635.y = circle_02.y+length*0.635f;
					fpoint_41.y = circle_02.y+length*0.41f;
					fpoint_18.y = circle_02.y+length*0.18f;
					fpoint_90.y = circle_02.y+length*0.90f;
					fpoint_825.y = circle_02.y+length*0.825f;
					fpoint_78.y = circle_02.y+length*0.78f;
				}
			}
			lenght_100 = length;
			return;
		}
		float length_01;
		if(Math.abs(fpoint_01.x-circle_01.x)>Math.abs(fpoint_01.x-circle_02.x)){		//这代表fpoint_01到circle_01比较远
			length_01 = (float) Math.sqrt((circle_01.x-fpoint_01.x)*(circle_01.x-fpoint_01.x)+(circle_01.y-fpoint_01.y)*(circle_01.y-fpoint_01.y));
		}else{
			length_01 = (float) Math.sqrt((circle_02.x-fpoint_01.x)*(circle_02.x-fpoint_01.x)+(circle_02.y-fpoint_01.y)*(circle_02.y-fpoint_01.y));
		}
		float length_03 = (float) Math.sqrt((circle_01.x-circle_02.x)*(circle_01.x-circle_02.x)+(circle_01.y-circle_02.y)*(circle_01.y-circle_02.y));
		float length = length_01>length_03?length_01:length_03;
		//放弃了二元二次方程算法，太过复杂！改为X轴方向上的等比例算法，求出63.5%和72.5%的脚长的位置坐标
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			float x725;					//X轴的差值
			float x635;
			float x41;
			float x18;
			float x90;
			float x825;
			float x78;
			if(length==length_03){		//circle_01.x到circle_02.x
				x725 = Math.abs(circle_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(circle_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(circle_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(circle_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(circle_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(circle_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(circle_01.x-circle_02.x)*0.78f;
			}else{						//circle_01.x到fpoint_01.x
				x725 = Math.abs(fpoint_01.x-circle_01.x)*0.725f;
				x635 = Math.abs(fpoint_01.x-circle_01.x)*0.635f;
				x41 = Math.abs(fpoint_01.x-circle_01.x)*0.41f;
				x18 = Math.abs(fpoint_01.x-circle_01.x)*0.18f;
				x90 = Math.abs(fpoint_01.x-circle_01.x)*0.90f;
				x825 = Math.abs(fpoint_01.x-circle_01.x)*0.825f;
				x78 = Math.abs(fpoint_01.x-circle_01.x)*0.78f;
			}
			if(circle_01.x<circle_02.x){
				fpoint_725.x = circle_01.x+x725;
				fpoint_635.x = circle_01.x+x635;
				fpoint_41.x = circle_01.x+x41;
				fpoint_18.x = circle_01.x+x18;
				fpoint_90.x = circle_01.x+x90;
				fpoint_825.x = circle_01.x+x825;
				fpoint_78.x = circle_01.x+x78;
			}else{
				fpoint_725.x = circle_01.x-x725;
				fpoint_635.x = circle_01.x-x635;
				fpoint_41.x = circle_01.x-x41;
				fpoint_18.x = circle_01.x-x18;
				fpoint_90.x = circle_01.x-x90;
				fpoint_825.x = circle_01.x-x825;
				fpoint_78.x = circle_01.x-x78;
			}
			fpoint_725.y = kz*(fpoint_725.x-circle_01.x)+circle_01.y;
			fpoint_635.y = kz*(fpoint_635.x-circle_01.x)+circle_01.y;
			fpoint_41.y = kz*(fpoint_41.x-circle_01.x)+circle_01.y;
			fpoint_18.y = kz*(fpoint_18.x-circle_01.x)+circle_01.y;
			fpoint_90.y = kz*(fpoint_90.x-circle_01.x)+circle_01.y;
			fpoint_825.y = kz*(fpoint_825.x-circle_01.x)+circle_01.y;
			fpoint_78.y = kz*(fpoint_78.x-circle_01.x)+circle_01.y;
		}else{																		//否则circle_02为脚跟点
			float x725;
			float x635;
			float x41;
			float x18;
			float x90;
			float x825;
			float x78;
			if(length==length_03){		//circle_01.x到circle_02.x
				x725 = Math.abs(circle_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(circle_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(circle_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(circle_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(circle_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(circle_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(circle_01.x-circle_02.x)*0.78f;
			}else{						//circle_01.x到fpoint_01.x
				x725 = Math.abs(fpoint_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(fpoint_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(fpoint_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(fpoint_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(fpoint_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(fpoint_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(fpoint_01.x-circle_02.x)*0.78f;
			}
			if(circle_01.x<circle_02.x){
				fpoint_725.x = circle_02.x-x725;
				fpoint_635.x = circle_02.x-x635;
				fpoint_41.x = circle_02.x-x41;
				fpoint_18.x = circle_02.x-x18;
				fpoint_90.x = circle_02.x-x90;
				fpoint_825.x = circle_02.x-x825;
				fpoint_78.x = circle_02.x-x78;
			}else{
				fpoint_725.x = circle_02.x+x725;
				fpoint_635.x = circle_02.x+x635;
				fpoint_41.x = circle_02.x+x41;
				fpoint_18.x = circle_02.x+x18;
				fpoint_90.x = circle_02.x+x90;
				fpoint_825.x = circle_02.x+x825;
				fpoint_78.x = circle_02.x+x78;
			}
			fpoint_725.y = kz*(fpoint_725.x-circle_01.x)+circle_01.y;
			fpoint_635.y = kz*(fpoint_635.x-circle_01.x)+circle_01.y;
			fpoint_41.y = kz*(fpoint_41.x-circle_01.x)+circle_01.y;
			fpoint_18.y = kz*(fpoint_18.x-circle_01.x)+circle_01.y;
			fpoint_90.y = kz*(fpoint_90.x-circle_01.x)+circle_01.y;
			fpoint_825.y = kz*(fpoint_825.x-circle_01.x)+circle_01.y;
			fpoint_78.y = kz*(fpoint_78.x-circle_01.x)+circle_01.y;
		}
		lenght_100 = length;
	}
	private void length_18(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			length_18=(float) getTwoCoordLength(circle_01.x,circle_01.y,fpoint_18.x,fpoint_18.y);
		}else{																		//circle_02为   ~ 脚跟点
			length_18=(float) getTwoCoordLength(circle_02.x,circle_02.y,fpoint_18.x,fpoint_18.y);
		}
	}
	private void fpoint_725_red(){
		if(circle_01.x==circle_02.x){
			fpoint_725_red.x = circle_01.x;
			fpoint_725_red.y = circle_725.y;
			return;
		}
		fpoint_725_red.x = (kz_v*circle_725.x-kz*circle_01.x+circle_01.y-circle_725.y)/(kz_v-kz);
		fpoint_725_red.y = kz_v*(fpoint_725_red.x-circle_725.x)+circle_725.y;
	}
	private void fpoint_635_red(){
		if(circle_01.x==circle_02.x){
			fpoint_635_red.x = circle_01.x;
			fpoint_635_red.y = circle_635.y;
			return;
		}
		fpoint_635_red.x = (kz_v*circle_635.x-kz*circle_01.x+circle_01.y-circle_635.y)/(kz_v-kz);
		fpoint_635_red.y = kz_v*(fpoint_635_red.x-circle_635.x)+circle_635.y;
	}
	private void fpoint_90_red(){
		if(circle_01.x==circle_02.x){
			fpoint_90_red.x=circle_01.x;
			fpoint_90_red.y=circle_90.y;
			return;
		}
		fpoint_90_red.x=(kz_v*circle_90.x-kz*circle_01.x+circle_01.y-circle_90.y)/(kz_v-kz);
		fpoint_90_red.y=kz_v*(fpoint_90_red.x-circle_90.x)+circle_90.y;
	}
	private void fpoint_825_red(){
		if(circle_01.x==circle_02.x){
			fpoint_825_red.x=circle_01.x;
			fpoint_825_red.y=circle_825.y;
			return;
		}
		fpoint_825_red.x=(kz_v*circle_825.x-kz*circle_01.x+circle_01.y-circle_825.y)/(kz_v-kz);
		fpoint_825_red.y=kz_v*(fpoint_825_red.x-circle_825.x)+circle_825.y;
	}
	private void fpoint_78_red(){
		if(circle_01.x==circle_02.x){
			fpoint_78_red.x=circle_01.x;
			fpoint_78_red.y=circle_78.y;
			return;
		}
		fpoint_78_red.x=(kz_v*circle_78.x-kz*circle_01.x+circle_01.y-circle_78.y)/(kz_v-kz);
		fpoint_78_red.y=kz_v*(fpoint_78_red.x-circle_78.x)+circle_78.y;
	}
	private void fpoint_68_red(){
		if(circle_725.x==circle_635.x){
			return;
		}
		float k68 = (circle_725.y-circle_635.y)/(circle_725.x-circle_635.x);
		if(circle_01.x==circle_02.x){
			fpoint_68_red.x = circle_01.x;
		}else{
			fpoint_68_red.x = (k68*circle_725.x-kz*circle_01.x+circle_01.y-circle_725.y)/(k68-kz);
		}
		fpoint_68_red.y = k68*(fpoint_68_red.x-circle_725.x)+circle_725.y;
	}
	private void line_725(){
		line_725.x1 = fpoint_725_red.x;
		line_725.y1 = fpoint_725_red.y;
		line_725.x2 = circle_725.x;
		line_725.y2 = circle_725.y;
	}
	private void line_635(){
		line_635.x1 = fpoint_635_red.x;
		line_635.y1 = fpoint_635_red.y;
		line_635.x2 = circle_635.x;
		line_635.y2 = circle_635.y;
	}
	private void line_90(){
		line_90.x1 = fpoint_90_red.x;
		line_90.y1 = fpoint_90_red.y;
		line_90.x2 = circle_90.x;
		line_90.y2 = circle_90.y;
	}
	private void line_825(){
		line_825.x1 = fpoint_825_red.x;
		line_825.y1 = fpoint_825_red.y;
		line_825.x2 = circle_825.x;
		line_825.y2 = circle_825.y;
	}
	private void line_78(){
		line_78.x1 = fpoint_78_red.x;
		line_78.y1 = fpoint_78_red.y;
		line_78.x2 = circle_78.x;
		line_78.y2 = circle_78.y;
	}
	private void line_68(){
		line_68.x1 = circle_635.x;
		line_68.y1 = circle_635.y;
		line_68.x2 = circle_725.x;
		line_68.y2 = circle_725.y;
	}
	@SuppressWarnings("unused")
	private void line_80(){
		if(circle_725.x==circle_80.x){			// line_80为垂直线
			line_80.x1 = circle_80.x;
			line_80.x2 = circle_725.x;
			if(circle_725.y>circle_80.y){		// circle_725和circle_80的上下位置关系
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
		}else{
			float k80=(circle_725.y-circle_80.y)/(circle_725.x-circle_80.x);
			//方程为：y=k80*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k80+circle_725.x
			if(circle_725.y>circle_80.y){		// circle_725和circle_80的上下位置关系
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
			line_80.x1 = (line_80.y1-circle_725.y)/k80+circle_725.x;
			line_80.x2 = (line_80.y2-circle_725.y)/k80+circle_725.x;
		}
	}
	@SuppressWarnings("unused")
	private void line_65(){
		if(circle_725.x==circle_65.x){			// line_65为垂直线
			line_65.x1 = circle_65.x;
			line_65.x2 = circle_725.x;
			if(circle_725.y>circle_65.y){		// circle_725和circle_80的上下位置关系
				line_65.y1 = circle_65.y-50;
				line_65.y2 = circle_725.y+100;
			}else{
				line_65.y1 = circle_65.y+50;
				line_65.y2 = circle_725.y-100;
			}
		}else{
			float k65 = (circle_725.y-circle_65.y)/(circle_725.x-circle_65.x);
			//方程为：y=k65*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k65+circle_725.x
			if(circle_725.y>circle_65.y){		// circle_725和circle_80的上下位置关系
				line_65.y1 = circle_65.y-50;
				line_65.y2 = circle_725.y+100;
			}else{
				line_65.y1 = circle_65.y+50;
				line_65.y2 = circle_725.y-100;
			}
			line_65.x1 = (line_65.y1-circle_725.y)/k65+circle_725.x;
			line_65.x2 = (line_65.y2-circle_725.y)/k65+circle_725.x;
		}
	}
	private void incline_725x(){
		circle_80.x = circle_80.x+differ_x725;
		line_80.x1 = line_80.x1+differ_x725;
		line_80.x2 = line_80.x2+differ_x725;
		circle_65.x = circle_65.x+differ_x725;
		line_65.x1 = line_65.x1+differ_x725;
		line_65.x2 = line_65.x2+differ_x725;
	}
	private void incline_725y(){
		circle_80.y = circle_80.y+differ_y725;
		line_80.y1 = line_80.y1+differ_y725;
		line_80.y2 = line_80.y2+differ_y725;
		circle_65.y = circle_65.y+differ_y725;
		line_65.y1 = line_65.y1+differ_y725;
		line_65.y2 = line_65.y2+differ_y725;
	}
	private void circle_4101(){
		if(circle_01.x==circle_02.x){
			circle_4101.y = fpoint_41.y;
		}else{
			circle_4101.y = kz_v*(circle_4101.x-fpoint_41.x)+fpoint_41.y;
		}
		line_41.x1 = circle_4101.x;
		line_41.y1 = circle_4101.y;
	}
	private void circle_4102(){
		if(circle_01.x==circle_02.x){
			circle_4102.y = fpoint_41.y;
		}else{
			circle_4102.y = kz_v*(circle_4102.x-fpoint_41.x)+fpoint_41.y;
		}
		line_41.x2 = circle_4102.x;
		line_41.y2 = circle_4102.y;
	}
	private void break_01(){
		if(circle_01.x==circle_02.x){
			break_01.y = fpoint_41.y;
			break_01.x1 = break_01.x;
			break_01.x2 = break_01.x;
			break_01.y1 = break_01.y-8;
			break_01.y2 = break_01.y+8;
			return;
		}
		break_01.y = kz_v*(break_01.x-fpoint_41.x)+fpoint_41.y;
		break_01.y1 = break_01.y-8;
		break_01.y2 = break_01.y+8;
		break_01.x1 = (break_01.y1-break_01.y)/kz+break_01.x;
		break_01.x2 = (break_01.y2-break_01.y)/kz+break_01.x;
	}
	private void break_02(){
		if(circle_01.x==circle_02.x){
			break_02.y = fpoint_41.y;
			break_02.x1 = break_02.x;
			break_02.x2 = break_02.x;
			break_02.y1 = break_02.y-8;
			break_02.y2 = break_02.y+8;
			return;
		}
		break_02.y = kz_v*(break_02.x-fpoint_41.x)+fpoint_41.y;
		break_02.y1 = break_02.y-8;
		break_02.y2 = break_02.y+8;
		break_02.x1 = (break_02.y1-break_02.y)/kz+break_02.x;
		break_02.x2 = (break_02.y2-break_02.y)/kz+break_02.x;
	}
	private void fpoint_18s(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			if(circle_18.x==circle_01.x){		//分踵线为垂直线
				fpoint_18s.x=circle_18.x;
				if(circle_18.y<circle_01.x){	//cricle_18调节点相对在上的话
					fpoint_18s.y=circle_01.y-length_18;
				}else{							//cricle_18调节点相对在下的话
					fpoint_18s.y=circle_01.y+length_18;
				}
			}else{								//分踵线不为垂直线
				float scale_18 = (float) (length_18/line_f.getLength());	//踵心长度与分踵心的长度之比
				float x18_f=Math.abs(circle_18.x-circle_01.x)*scale_18;
				if(circle_18.x<circle_01.x){	//cricle_18调节点相对在左边
					fpoint_18s.x=circle_01.x-x18_f;
				}else{							//cricle_18调节点相对在右边
					fpoint_18s.x=circle_01.x+x18_f;
				}
				fpoint_18s.y=kf*(fpoint_18s.x-circle_01.x)+circle_01.y;
			}
		}else{																		//circle_02为   ~ 脚跟点
			if(circle_18.x==circle_02.x){		//分踵线为垂直线
				fpoint_18s.x=circle_18.x;
				if(circle_18.y<circle_02.x){	//cricle_18调节点相对在上的话
					fpoint_18s.y=circle_02.y-length_18;
				}else{							//cricle_18调节点相对在下的话
					fpoint_18s.y=circle_02.y+length_18;
				}
			}else{								//分踵线不为垂直线
				float scale_18=(float) (length_18/line_f.getLength());		//踵心长度与分踵心的长度之比
				float x18_f=Math.abs(circle_18.x-circle_02.x)*scale_18;
				if(circle_18.x<circle_02.x){	//cricle_18调节点相对在左边
					fpoint_18s.x=circle_02.x-x18_f;
				}else{							//cricle_18调节点相对在右边
					fpoint_18s.x=circle_02.x+x18_f;
				}
				fpoint_18s.y=kf*(fpoint_18s.x-circle_02.x)+circle_02.y;
			}
		}
	}
	private void line_f(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			line_f.x1=circle_01.x;
			line_f.y1=circle_01.y;
			line_f.x2=circle_18.x;
			line_f.y2=circle_18.y;
		}else{																		//circle_02为   ~ 脚跟点
			line_f.x1=circle_02.x;
			line_f.y1=circle_02.y;
			line_f.x2=circle_18.x;
			line_f.y2=circle_18.y;
		}
	}
	private void circle_1801(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			if(circle_18.x==circle_01.x){
				circle_1801.y=fpoint_18s.y;
			}else{
				circle_1801.y=kf_v*(circle_1801.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}else{																		//circle_02为   ~ 脚跟点
			if(circle_18.x==circle_02.x){
				circle_1801.y=fpoint_18s.y;
			}else{
				circle_1801.y=kf_v*(circle_1801.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}
		line_18.x1=circle_1801.x;
		line_18.y1=circle_1801.y;
	}
	private void circle_1802(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			if(circle_18.x==circle_01.x){
				circle_1802.y=fpoint_18s.y;
			}else{
				circle_1802.y=kf_v*(circle_1802.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}else{																		//circle_02为   ~ 脚跟点
			if(circle_18.x==circle_02.x){
				circle_1802.y=fpoint_18s.y;
			}else{
				circle_1802.y=kf_v*(circle_1802.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}
		line_18.x2=circle_1802.x;
		line_18.y2=circle_1802.y;
	}
	private void angle_du(){							//计算分踵线
		//cosα=(a^2+b^2-c^2）/(2ab)
		//中轴线的方程：y=k*(x-circle_01.x)+circle_01.y;=>x=(y-circle_01.y)/k+circle_01.x;
		fpoint_18h.y=circle_18.y;
		if(circle_01.x==circle_02.x){					//中轴线为垂直线
			fpoint_18h.x = circle_01.x;
		}else{											//中轴线不为垂直线
			fpoint_18h.x = (fpoint_18h.y-circle_01.y)/kz+circle_01.x;
		}
		float a = (float) line_f.getLength();
		double b;
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01为   ~ 脚跟点
			b = getTwoCoordLength(fpoint_18h.x,fpoint_18h.y,circle_01.x,circle_01.y);
		}else{																		//circle_02为   ~ 脚跟点
			b = getTwoCoordLength(fpoint_18h.x,fpoint_18h.y,circle_02.x,circle_02.y);
		}
		double c = getTwoCoordLength(fpoint_18h.x,fpoint_18h.y,circle_18.x,circle_18.y);
		double cos_angle = (a*a+b*b-c*c)/(2*a*b);		//分踵角度的余弦值//System.out.println("a="+a+",b="+b+",c="+c);
		double arccos_angle = Math.acos(cos_angle);
		double pi_cos_angle = (arccos_angle/Math.PI)*180;
		String str_angle = dformat.format(pi_cos_angle);
		center_angle = str_angle;
	}
	@SuppressWarnings("unused")
	private void incline_angle(){						//斜边夹角
		double a = getTwoCoordLength(circle_725.x,circle_725.y,line_80.x1,line_80.y1);
		double b = getTwoCoordLength(circle_725.x,circle_725.y,line_65.x2,line_65.y2);
		double c = getTwoCoordLength(line_80.x1,line_80.y1,line_65.x2,line_65.y2);
		double cos_angle=(a*a+b*b-c*c)/(2*a*b);
		double arccos_angle=Math.acos(cos_angle);
		double pi_cos_angle=(arccos_angle/Math.PI)*180;
		String str_angle=dformat.format(pi_cos_angle);
		incline_angle = str_angle;
		
	}
	private void footLength(){
		foot_length = calcUtil.getFootLength_mm(lenght_100,dpi,scale);
		shoe_length = calcUtil.getFootLength_5(Float.parseFloat(foot_length));
	}
	private void footWidth(){
		foot_width = calcUtil.getFootLength_mm(line_725.getLength()+line_635.getLength(),dpi,scale);
		type_width = calcUtil.getWidthType(Integer.parseInt(shoe_length), Double.parseDouble(foot_width));
	}
	private void footStatus(){
		foot_status = calcUtil.getFootStatus(getTwoCoordLength(break_01.x,break_01.y,break_02.x,break_02.y),line_41.getLength());
		foot_advice = calcUtil.getFootAdvice(foot_status);
	}
	
	private void updateData(){				//更新数据模型
		if(foot.equals("left")){
			model.setValueAt(foot_length+"mm", 0, 0);
			model.setValueAt(foot_width+"mm", 1, 0);
			model.setValueAt(shoe_length, 2, 0);
			model.setValueAt(type_width, 3, 0);
			model.setValueAt(foot_status, 4, 0);
		}else if(foot.equals("right")){
			model.setValueAt(foot_length+"mm", 0, 2);
			model.setValueAt(foot_width+"mm", 1, 2);
			model.setValueAt(shoe_length, 2, 2);
			model.setValueAt(type_width, 3, 2);
			model.setValueAt(foot_status, 4, 2);
		}
	}
	
	public boolean isGetPara(){				//判断是否得到目标像素
		return foot_length==null?false:true;
	}
	
    //获得打印参数值
	public String[] getParaValue(){
		if(foot_length==null){				//等待分析
			return null;
		}
		String[] values = new String[25];
		values[0] = foot_length;
		values[1] = foot_width;
		values[2] = shoe_length;			//推荐鞋码
		values[3] = type_width;				//推荐型宽
		values[4] = foot_status;			//足弓态势
		values[5] = foot_advice;			//专家意见
		values[6] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_90_red.x,fpoint_90_red.y,circle_02.x,circle_02.y),dpi,scale);//拇趾外突点
		values[7] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_825_red.x,fpoint_825_red.y,circle_02.x,circle_02.y),dpi,scale);//小趾端点
		values[8] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_78_red.x,fpoint_78_red.y,circle_02.x,circle_02.y),dpi,scale);//小趾外突点
		values[9] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_725_red.x,fpoint_725_red.y,circle_02.x,circle_02.y),dpi,scale);//足弓长length_725
		values[10] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_635_red.x,fpoint_635_red.y,circle_02.x,circle_02.y),dpi,scale);//五跖趾关节length_635
		values[11] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_68_red.x,fpoint_68_red.y,circle_02.x,circle_02.y),dpi,scale);//着地点length_68
		values[12] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_41.x,fpoint_41.y,circle_02.x,circle_02.y),dpi,scale);//腰窝length_41
		values[13] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_18.x,fpoint_18.y,circle_02.x,circle_02.y),dpi,scale);//踵心length_18
		values[14] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_90_red.x,fpoint_90_red.y,circle_90.x,circle_90.y),dpi,scale);//拇趾里宽width_90
		values[15] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_78_red.x,fpoint_78_red.y,circle_78.x,circle_78.y),dpi,scale);//小趾外宽width_78
		values[16] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_725_red.x,fpoint_725_red.y,circle_725.x,circle_725.y),dpi,scale);//一跖趾里宽width_725
		values[17] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_635_red.x,fpoint_635_red.y,circle_635.x,circle_635.y),dpi,scale);//五跖趾外宽width_635
		values[18] = calcUtil.getFootLength_mm(getTwoCoordLength(circle_725.x,circle_725.y,circle_635.x,circle_635.y),dpi,scale);//斜宽width_68
		values[19] = calcUtil.getFootLength_mm(getTwoCoordLength(break_01.x,break_01.y,break_02.x,break_02.y),dpi,scale);//腰窝外宽width_41
		values[20] = calcUtil.getFootLength_mm(getTwoCoordLength(circle_4101.x,circle_4101.y,circle_4102.x,circle_4102.y),dpi,scale);//腰窝全宽width_41full
		values[21] = calcUtil.getFootLength_mm(getTwoCoordLength(circle_1801.x,circle_1801.y,circle_1802.x,circle_1802.y),dpi,scale);//踵心全宽width_18
		values[22] = dformat.format(Double.parseDouble(values[1])/Double.parseDouble(values[21]));//宽度比
		values[23] = center_angle;
		values[24] = incline_angle;
		return values;
	}
	
	public float[] getCoord(){					//获取参数点坐标的参数坐标
		float[] group = new float[34];
		group[0] = circle_01.x-orig_x;			//终端版：18个参数
		group[1] = circle_01.y-orig_y;
		group[2] = circle_02.x-orig_x;
		group[3] = circle_02.y-orig_y;
		group[4] = circle_03.x-orig_x;
		group[5] = circle_03.y-orig_y;
		group[6] = circle_725.x-orig_x;
		group[7] = circle_725.y-orig_y;
		group[8] = circle_635.x-orig_x;
		group[9] = circle_635.y-orig_y;
		group[10] = circle_4101.x-orig_x;
		group[11] = circle_4101.y-orig_y;
		group[12] = circle_4102.x-orig_x;
		group[13] = circle_4102.y-orig_y;
		group[14] = break_01.x-orig_x;
		group[15] = break_01.y-orig_y;
		group[16] = break_02.x-orig_x;
		group[17] = break_02.y-orig_y;
		group[18] = circle_18.x-orig_x;			//专业版：16个参数（追加）
		group[19] = circle_18.y-orig_y;
		group[20] = circle_1801.x-orig_x;
		group[21] = circle_1801.y-orig_y;
		group[22] = circle_1802.x-orig_x;
		group[23] = circle_1802.y-orig_y;
		group[24] = circle_90.x-orig_x;
		group[25] = circle_90.y-orig_y;
		group[26] = circle_825.x-orig_x;
		group[27] = circle_825.y-orig_y;
		group[28] = circle_78.x-orig_x;
		group[29] = circle_78.y-orig_y;
		group[30] = circle_80.x-orig_x;
		group[31] = circle_80.y-orig_y;
		group[32] = circle_65.x-orig_x;
		group[33] = circle_65.y-orig_y;
		return group;
	}
	public int[] getEdge(){
		int[] border = new int[4];
		border[0] = top;
		border[1] = bottom;
		border[2] = left;
		border[3] = right;
		return border;
	}
	public double getScale(){
		return scale;
	}
	
	//判断面板中是否放置了脚型
	public boolean haveImage(){
		if(bufImage==null){
			return false;
		}else{
			return true;
		}
	}
	//输出当前分析配置文件的参数
	public void printAnalyseConfig(){
		System.out.println("this class in "+this.foot+" FootPane print AnalyseConfig");
		System.out.println("time erosion:"+analyseConfig.getTime_erosion());
		System.out.println("time dilate:"+analyseConfig.getTime_dilate());
		System.out.println("stamp threshold:"+analyseConfig.getStamp_threshold());
		System.out.println("stamp radius:"+analyseConfig.getStamp_radius());
	}
	
	//得到两个参考坐标点的距离
	private double getTwoCoordLength(float x1,float y1,float x2,float y2){
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
    private class Circle{
		private float x,y;
		private float radius = 5.0f;
		private Color color = Color.GREEN;
		private String tiptext = null;
	    private Font gen_font = new Font("微软雅黑", Font.PLAIN, 12);
		public void draw(Graphics2D g2d){
			g2d.setColor(color);
			g2d.setStroke(new BasicStroke(2.0f));
      	  	g2d.draw(new Ellipse2D.Float(x-radius,y-radius,radius*2,radius*2));
      	  	if(tiptext!=null){
      	  		g2d.setStroke(new BasicStroke(1.0f));
      	  		g2d.setFont(gen_font);
      	  		g2d.drawString(tiptext, x+5, y-5);
      	  	}
		}
		public void setTipText(String tiptext){
			this.tiptext = tiptext;
		}
	}
	private class FillPoint{
		private float x,y;
		private float radius = 3.5f;
		private Color color = Color.GREEN;
		private String tiptext = null;
		private Font gen_font = new Font("微软雅黑", Font.PLAIN, 12);
		public void draw(Graphics2D g2d){
			g2d.setPaint(color);
			g2d.fill(new Ellipse2D.Float(x-radius,y-radius,radius*2,radius*2));
			if(tiptext!=null){
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.setFont(gen_font);
				g2d.drawString(tiptext, x+5, y-5);
			}
		}
		public void setColor(Color color){
			this.color = color;
		}
		public void setTipText(String tiptext){
			this.tiptext = tiptext;
		}
	}
	private class Line{
		private float x1,y1;
		private float x2,y2;
		private Color color = Color.GREEN;
		public void draw(Graphics2D g2d){
			g2d.setColor(color);
			g2d.setStroke(bs[0]);
			Line2D.Float line = new Line2D.Float(x1,y1,x2,y2);
			g2d.draw(line);
		}
		public double getLength(){
			return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		}
	}
	private class BreakCoord{
		private float x,y;
		private float x1,y1;
		private float x2,y2;
		private Color color = Color.YELLOW;
		public void draw(Graphics2D g2d){
			g2d.setColor(color);
			g2d.setStroke(new BasicStroke(2.0f));
			Line2D.Float line = new Line2D.Float(x1,y1,x2,y2);
			g2d.draw(line);
		}
	}
	private class Cross{
    	private float x;
		private float y;
    	public int type = 0;
    	public void draw(Graphics2D g2d){
    		if(type==1){
    			g2d.setColor(Color.GREEN);
    			g2d.setStroke(new BasicStroke(1.0f));
          	  	g2d.draw(new Ellipse2D.Float(x-8,y-8,16,16));
    			g2d.setColor(Color.GREEN);
        		g2d.setStroke(new BasicStroke(1.0f));
        		Line2D.Float line_x = new Line2D.Float(x-10,y,x+10,y);
        		g2d.draw(line_x);
        		Line2D.Float line_y = new Line2D.Float(x,y-10,x,y+10);
        		g2d.draw(line_y);
    		}else{
    			g2d.setColor(Color.YELLOW);
    			g2d.setStroke(new BasicStroke(2.0f));
          	  	g2d.draw(new Ellipse2D.Float(x-5,y-5,10,10));
    		}
    	}
    }
//end...FootPane.java
}