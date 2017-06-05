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
	
	private String foot;							//��λ:left;right
	private DefaultTableModel model;
    private AnalyseConfig analyseConfig;
    private boolean showData = false;
	
	private Image originalImage = null;  			//ԭʼͼ��
	private Image analyseImage = null;				//����ģ��ɨ���������
	
	private int orig_x;
    private int orig_y;
    
	private BufferedImage bufImage = null;  		//Ŀ�껺����ͼ��
    private BufferedImage originalBufImage = null;  //ԭʼ������ͼ��//��չ���
    private BufferedImage tempImage = null;  		//������ʾ�Ļ�����ͼ��
    private BufferedImage stampImage = null;		//stamp�˲��ı��ݻ�����ͼ��
	
    final BasicStroke[] bs = new BasicStroke[6];
    private FootCalcUtil calcUtil;
    private FootPaneMouseListener mouseListener;
    private FootPaneMouseMotionListener motionListener;
    private DecimalFormat dformat = new DecimalFormat("#.##");
	
	private boolean draging = false;				//�Ƿ�ǰ�϶������
	private boolean draged = false;					//�ж��Ƿ�������draged()����
    private int dragnum = 0;						//��ǵ�ǰ�϶��ĵ�λ
    private int drag_method = 1;
    
    private Circle circle_01;						//����ֺ
    private Circle circle_02;						//�ź��
    private Circle circle_03;						//��Ĵֺ
    private Line line_heel;							//�Ÿ���ֱ��
    private Line line_top;							//�ཻ��
    private Line line_foot;
    private FillPoint fpoint_01;
    
    private Circle circle_725;
    private Circle circle_635;
    private FillPoint fpoint_725;
    private FillPoint fpoint_635;
    private FillPoint fpoint_725_red;
    private FillPoint fpoint_635_red;
    private FillPoint fpoint_68_red;
    
    private Line line_725;							//��һ��ֺ��ֱ��
    private Line line_635;							//������ֺ��ֱ��
    private Line line_68;							//б����

    private Circle circle_4101;
    private Circle circle_4102;
    private Line line_41;							//���ѿ���
    private FillPoint fpoint_41;
    private BreakCoord break_01;
    private BreakCoord break_02;
    
    private FillPoint fpoint_18;					//�������ϵ�18%�ų�
    private FillPoint fpoint_18s;					//�������ϵ�18%�ų�
    private FillPoint fpoint_18h;					//����Ƕȸ�����
    private Line line_f;							//������
    private Line line_18;							//����ȫ����
    private Circle circle_18;						//�����ߵ��ڵ�
    private Circle circle_1801;
    private Circle circle_1802;
    private Circle circle_90;						//Ĵֺ��ͻ��
    private FillPoint fpoint_90_red;
    private FillPoint fpoint_90;
    private Line line_90;
    private Circle circle_825;						//Сֺ�˵�
    private FillPoint fpoint_825_red;
    private FillPoint fpoint_825;
    private Line line_825;
    private Circle circle_78;						//Сֺ��ͻ��
    private FillPoint fpoint_78_red;
    private FillPoint fpoint_78;
    private Line line_78;
   
    private Circle circle_80;						//б�߼н�
    private Circle circle_65;
    private FillPoint fpoint_80;
    private FillPoint fpoint_65;
    private Line line_80;
    private Line line_65;
    
    private Cross cross_cursor;						//ѡ����
    
    private int pane_width;				//�����
    private int pane_height;			//���߶�    
    private int image_width;			//��ʼ����ͼƬ������Ӧ�ȱ������
	private int image_height;			//��ʼ����ͼƬ������Ӧ�ȱ����߶�
    private double scale;				//ɨ�����ųߴ�
    private int dpi;					//��ǰͼƬ�ķֱ���
	
	private boolean loading = false;	//�Ƿ�����ɨ���ȡͼ��
	private boolean analysing = false;	//�Ƿ���������ӦͼƬ�������㷨����
	private boolean showPath = false;	//�Ƿ���ʾ������
	
	private int[][] pixel_array = null;
	private int top;					//���ڼ���ƫ����
	private int left;
	private int right;
	private int bottom;
	
	private Timer timer = null;
	private GeneralPath gPath;
	private int path_index = 0;			//���������ʾ������
	private boolean doTimer = false;	//�Ƿ������������ߵ����긨���ߵĶ�ʱ��
	
	private int scan_index = 0;			//ǰ��ɨ������ߣ���ǰλ���˶��ٲ�
	private int scan_step = 5;			//ǰ��ɨ������ߣ�ɨ��ÿ����λ��5
	private int scan_count;				//ǰ��ɨ������ߣ��ܹ�Ҫ������ٲ�
	
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
		
	private float kz = 0;				//������б��
	private float kz_v = 0;				//��ֱ�������ߵĸ�����б��
	private float kf = 0;				//�����ߵ�б��
	private float kf_v = 0;				//��ֱ�ڷ����ߵ�б��
	private float length_18 = 0;		//�������س���
	private float lenght_100 = 0;		//�ų����س���
	private float differ_x725 = 0;		//��������б�߼нǵ�������ƫ����
	private float differ_y725 = 0;
	
	private String no_type = null;		//���������ͣ�δ����Ŀ�����ء��߽糬��
	    
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
		
		timer = new Timer(20,this);		//��ʼ����ʱ��
		
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
    	loading = load;									//true:��ʼ����ͼ���ʼ�����в�����false:ˢ��ȫ����ʼ�����в���
    	analysing = false;
    	showPath = false;
    	doTimer = false;
    	
       	if(timer!=null&&timer.isRunning()){
       		timer.stop();
       	}
       	if(loading){
       		path_index = 0;
       		scan_index = 0;
       		scan_count = this.getHeight()/scan_step+1;	//ɨ�貽�� 677/5=135.4 = 135+1=136 
        	timer.setDelay(3000/scan_count);			//4000/136=29.4117  3000/136=22.0588
           	System.out.println("scan count��"+scan_count+",scan delay��"+timer.getDelay());
       	}
       	
       	originalImage = null;							//������ջ�����ͼ��
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
    public void loadFailure(){							//ɨ���ȡͼƬʧ�ܣ���������
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
    		originalImage = ImageIO.read(file);			//���ļ�����ȡImage����//bufImage = ImageIO.read(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		MediaTracker mt = new MediaTracker(this); 		//ʵ����ý�������
		mt.addImage(originalImage, 0); 					//���Ӵ�����ͼ��ý�������
		try {
			mt.waitForAll(); 							//�ȴ�����ͼ��ļ������
		} catch (Exception ex) {
			ex.printStackTrace(); 						//���������Ϣ
		}
		double scaleX = this.getWidth()*1.0/originalImage.getWidth(this);
		double scaleY = this.getHeight()*1.0/originalImage.getHeight(this);
		double scan_scale = (scaleX>scaleY)?scaleY:scaleX;
    	image_width = (int) (originalImage.getWidth(this)*scan_scale);
		image_height = (int)(originalImage.getHeight(this)*scan_scale);
    	
		loading = false;
		//��һ�׶��꣨ɨ���ȡͼ��//�ڶ��׶Σ�����ͼ��ģ�����ɨ����
		analysing = true;
		timer.start();									//������ʱ������������ͼ����ʾ�����ߣ�

		if(getStampImage()){
			if(!getTargetImage()){
				System.out.println("Detect the foot type area edge less than 10 pixels !");
				analysing = false;
			}
		}else{
			System.out.println("Detect the foot type area is less than 5000 pixels !");
			no_type = "δ����Ŀ�����أ�";
			analysing = false;							//timer.stop();//timer���ֹͣ�Ļ���ɨ���߿��ܻ�û���ꡣ
		}
    }
    
    public void paintComponent(Graphics g) {			//��дpaint����,����panel���ͼ��
    	Graphics2D g2 = (Graphics2D) g;
    	pane_width = this.getWidth();
    	pane_height = this.getHeight();
		g2.setPaint(new Color(10,10,10));				//���ƺ�ɫ�ı���
		g2.fill(new Rectangle2D.Double(0, 0, pane_width-1, pane_height-1));
		
		/*if(loading){									//����ɨ��ͼƬ(����ɨ�趯��gif)
			if(loadImage!=null){
				g2.drawImage(loadImage, (pane_width-loadImage.getWidth(this))/2, (pane_height-loadImage.getHeight(this))/2
						, loadImage.getWidth(this), loadImage.getHeight(this),this);
			}
		}else*/
														//����Ӧ����֮�����ڷ���ͼƬ(ģ���������)
		if(!loading&&originalImage!=null&&(analysing||scan_index<=scan_count)){
			g2.drawImage(originalImage, (pane_width-image_width)/2, (pane_height-image_height)/2 ,
					image_width, image_height ,this);
			g2.drawImage(analyseImage, 0, scan_step*(scan_index++), pane_width, analyseImage.getHeight(this),this);
		}else
														//����ͼ��ʧ�ܣ�δ����Ŀ������...
		if(!loading&&originalImage!=null&&(!analysing&&scan_index>scan_count)&&bufImage==null){
			timer.stop();
			g2.drawImage(originalImage, (pane_width-image_width)/2, (pane_height-image_height)/2 ,
					image_width, image_height ,this);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Color.WHITE);
			g2.setFont(new Font("����", Font.PLAIN+Font.BOLD, 28));
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString(no_type, (pane_width-fm.stringWidth(no_type))/2, pane_height/2);
		}else
				
		if (bufImage!=null&&!analysing&&scan_index>scan_count) {
			g2.drawImage(bufImage, orig_x, orig_y , bufImage.getWidth(),  bufImage.getHeight(),this);
			Composite old = g2.getComposite();			//����͸���ο�������
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
			if(showPath){								//���ƽ���������������
				g2.setComposite(old);
				g2.setColor(Color.red);
	        	g2.setStroke(bs[(path_index++)%6]);
	        	g2.draw(gPath);
	        	//���ö�ʱ��������ʵ���������ߺ󣬿�������Ķ�ʱ����1�룬֮��ֹͣѭ����ʱ������ʾ�������꣩
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
		g2.setStroke(new BasicStroke(1.0f));				//���Ƶ���ɫ�ı߿�
		g2.setPaint(new Color(56,248,249));
		g2.draw(new Rectangle2D.Double(0, 0, pane_width-1, pane_height-1));
    }
	public void actionPerformed(ActionEvent e) {			//������ʱ��ִ���ػ淽��
		repaint();
	}
	
	private void doTimerTask(){								//���������߶�ʱ����1��
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
		int iw = originalImage.getWidth(this);				//������չͼƬ��������20�����ص�
		int ih = originalImage.getHeight(this);
		int[] pixels = new int[iw*ih];
		try {
			PixelGrabber pg = new PixelGrabber(originalImage,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int[] new_pixs = new int[(iw+40)*(ih+40)];			//�½�һ����չ��ͼƬ�������飺������չ20px
		for(int i=0;i<ih;i++){
			for(int j=0;j<iw;j++){
				new_pixs[(i+20)*(iw+40)+(j+20)] = pixels[i*iw+j];
			}
		}
		int pixel = (0xff<<24)|(20<<16)|(35<<8)|(35);		//int pixel=(alpha<<24)|(20<<16)|(40<<8)|(40);�ض��ı�������
		for(int i=0;i<ih+40;i++){
			for(int j=0;j<iw+40;j++){
				if(i<20||i>=20+ih||j<20||j>=20+iw){
					new_pixs[i*(iw+40)+j]=pixel;			//��������20���أ�Ϊ�ض������أ������ں��ԡ�����
				}
			}
		}
		ImageProducer ip = new MemoryImageSource(iw+40,ih+40,new_pixs,0,iw+40);
		Image image = createImage(ip);						//��image���¸�ֵ
		originalBufImage = new BufferedImage(image.getWidth(this),image.getHeight(this),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2D = originalBufImage.createGraphics();
		g2D.drawImage(image, 0, 0, this);
		
		tempImage = originalBufImage;
		//ִ�������˲�
		if(Boolean.parseBoolean(analyseConfig.getUseSpecial())){
			SpecialFilter filter_special = new SpecialFilter();
			tempImage = filter_special.filter(tempImage);
		}
		//�ȸ�ʴ�㷨
		EroDilFilter filter_EroDil = new EroDilFilter();
		tempImage = filter_EroDil.mini_ero(tempImage,Integer.parseInt(analyseConfig.getTime_erosion()));
		//�������㷨
		tempImage = filter_EroDil.max_dil(tempImage,Integer.parseInt(analyseConfig.getTime_dilate()));
		//ִ��Stamp�˲����õ���ֵͼ
		StampFilter filter_stamp = new StampFilter();
		filter_stamp.setRadius(Integer.parseInt(analyseConfig.getStamp_radius())*1.0f);
		filter_stamp.setThreshold(Integer.parseInt(analyseConfig.getStamp_threshold())*1.0f/100);
		tempImage = filter_stamp.filter(tempImage, null);
		//ִ����ͨ�������㷨
		ConnectFilter filter_Connect = new ConnectFilter();
		tempImage = filter_Connect.filter(tempImage);
		//û�м����κε�Ŀ������
		if(tempImage==null){
			return false;
		}
		//��⵽Ŀ���������ز���5000���˳�
		if(filter_Connect.getMax_num()<20000){
			return false;
		}
		//�������㷨
		//tempImage = filter_EroDil.max_dil(tempImage,Integer.parseInt(analyseConfig.getTime_dilate()));
		stampImage = tempImage;								//��ֵ��stampImage�����ݣ�.....��
		//�򵥱�Ե�㷨
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
			no_type = "�����ϱ߽糬����";
			return false;
		}
		if(left<30){
			no_type = "������߽糬����";
			return false;
		}
		if(bottom>temp_height-30){
			no_type = "�����±߽糬����";
			return false;
		}
		if(right>temp_width-30){
			no_type = "�����ұ߽糬����";
			return false;
		}													//���˵õ�����������������������ұ߽緶Χ
		top = top-50;										//��չ�������ҵı���ͼƬ������
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
		}													//���浽���ݿ��Ϊ�Ѿ���չ���ıߣ������ĸ������洢�����ݿ⵱��!!!
		double scaleX = this.getWidth()*1.0/(right-left);
		double scaleY = this.getHeight()*1.0/(bottom-top);
		scale = (scaleX>scaleY)?scaleY:scaleX;
		
		int[] pixels = new int[(right-left)*(bottom-top)];
		stampImage.getRGB(left,top,right-left,bottom-top,pixels,0,right-left);
		BufferedImage stampAimImage = new BufferedImage(right-left,bottom-top,stampImage.getType());
		stampAimImage.setRGB(0,0,right-left,bottom-top,pixels,0,right-left);
		tempImage = null;
		tempImage = scaleFilter(stampAimImage,scale);		//���µĵ�bufImage����ʱ��bufImage��stampImage�����Ű�
		//�򵥱�Ե�㷨
		SimpleEdgeFilter filter_edge = new SimpleEdgeFilter();
		tempImage = filter_edge.process(tempImage);
		//����ִ�л�ȡ��Ե����������㷨
		getPixelArray();									//�õ��ڶ��εĽ��ͱ�Ե�������飬���µõ�list
		
		originalBufImage.getRGB(left,top,right-left,bottom-top,pixels,0,right-left);
		BufferedImage origAimBufImage = new BufferedImage(right-left,bottom-top,originalBufImage.getType());
		origAimBufImage.setRGB(0,0,right-left,bottom-top,pixels,0,right-left);
		bufImage = scaleFilter(origAimBufImage,scale);		//�õ����յ���ʾ����ͼ
		
		orig_x = (this.getWidth() - bufImage.getWidth())/2;
		orig_y = (this.getHeight() - bufImage.getHeight())/2;
		if(pixel_array!=null&&pixel_array.length!=0){		//����������·��
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
        	
        	getCoordInfo();									//����ų��Ĳο������.Ҫ�õ�pixel_array
        	analysing = false;
    		//�ڶ��׶��꣨����ͼ��//�����׶Σ���ʾ�������������ߺ�Ŀ���������
        	showPath = true;
    	}
		return true;
	}
	private void getPixelArray(){
		pixel_array = null;
		ArrayList<PixelPoint> list_left = new ArrayList<PixelPoint>();
		ArrayList<PixelPoint> list_right = new ArrayList<PixelPoint>();
		for(int i=20;i<tempImage.getHeight()-20;i++){		//���Ե���Ե��չ��20����,���ǽ�ʡʱ��
			boolean exist = false;
			int cur_i = i;
			int max_j = 0;
			int min_j = 0;
			for(int j=20;j<tempImage.getWidth()-20;j++){	//�ж�ˮƽ�����ϵ��������������
				if((tempImage.getRGB(j,i) & 0xff)==0xff){	//��ɫ������Ϊ��ɫ���Ǳ�Ե�ߣ�������ִ����һ��ѭ����
					continue;
				}
				if(exist==false){							//����һ��Ϊ��ɫ��Ե�����ء�������
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
		for(int i=list_right.size()-1;i>=0;i--){			//�����������������Ҳ������ʱ�뻭����
			list_left.add(list_right.get(i));
		}
		System.out.println("all of pixels of foot1��"+list_left.size());	
		pixel_array = new int[list_left.size()][2];			//������������һ����ά������
		System.out.println("all of pixels of foot2��"+pixel_array.length);
		Iterator<PixelPoint> iterator = list_left.iterator();
		PixelPoint point = null;
		for(int i=0;iterator.hasNext();i++){
			point = iterator.next();
			pixel_array[i][0] = point.getPixel_x();			//ˮƽ����
			pixel_array[i][1] = point.getPixel_y();			//��ֱ����
			point = null;
		}
	}
    private BufferedImage scaleFilter(BufferedImage buf_image,double scale) {
		BufferedImage filteredBufImage = new BufferedImage((int) (buf_image.getWidth()*scale),(int) (buf_image.getHeight()*scale),buf_image.getType());
		AffineTransform transform = new AffineTransform(); 							//����任����
		transform.setToScale(scale, scale); 										//���÷���任�ı�������
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);			//��������任��������
		imageOp.filter(buf_image, filteredBufImage);								//����ͼ��Ŀ��ͼ����filteredBufImage
		return filteredBufImage;
	}
    
	/**�����ϵĵ�λ��(�ض�������)���Ÿ����£���ֺ���ϣ�
	 * circle_02Ϊ�Ÿ���λ��
	 * circle_01Ϊ����ֺ��λ��
	 * circle_03Ϊ�Ŵ�Ĵֺ��λ��
	 * */
	private void getCoordInfo(){		
		int pixel_count = pixel_array.length;
		//�ض�ɨ�軷���£��Ÿ�����
		circle_02.x = (pixel_array[pixel_count/2][0]+pixel_array[pixel_count/2-1][0])/2;
		circle_02.y = (pixel_array[pixel_count/2][1]+pixel_array[pixel_count/2-1][1])/2;
		//�ж��������Ƿ�Ϊ��ָ�ʹ�Ĵָ����
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
		int differ = (int)(pixel_array.length/2*0.07);	//�����ٷֱȳ���
		System.out.println("footCoord left or right traverse��"+differ);
		//�ж���ߵ�����λ//��ʱ��
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
		//�ж��ұߵ�����λ//˳ʱ��
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
		//Ϊ����������������λ������
		circle_01.x = circle_01.x+orig_x;
		circle_01.y = circle_01.y+orig_y;
		circle_02.x = circle_02.x+orig_x;
		circle_02.y = circle_02.y+orig_y;
		circle_03.x = circle_03.x+orig_x;
		circle_03.y = circle_03.y+orig_y;
		
		float footLength = 0;									//����ų�mm(Ϊcircle_01��circle_02������)
		if(circle_01.x == circle_02.x){							//�������Ϊ0(��ֱ��)!!�������﷨�ķ��̣������ԣ�������£�ֱ�߲��ܹ���ʾ��
			line_foot.x1 = circle_01.x;							//����line_foot���ų�������(������)
			line_foot.x2 = circle_02.x;
			line_foot.y1 = circle_01.y-80;
			line_foot.y2 = circle_02.y+80;
			
			line_top.y1 = circle_03.y;							//���� line_top����ֱ�ڽų������������ڽ�Ĵֺ�ĸ�����
			line_top.y2 = circle_03.y;
			if(foot.equals("left")){
				line_top.x1 = circle_01.x-60;
				line_top.x2 = circle_03.x+60;
			}else{
				line_top.x1 = circle_01.x+60;
				line_top.x2 = circle_03.x-60;
			}
			fpoint_01.x = circle_01.x;							//����fpoint_01:line_foot��line_top���ཻ��
			fpoint_01.y = circle_03.y;
			line_heel.x1 = circle_02.x-100;						//����line_heel���ཻ�ڽŸ�circle_02������Ÿ��ĸ�����
			line_heel.x2 = circle_02.x+100;
			line_heel.y1 = circle_02.y;
			line_heel.y2 = circle_02.y;
			float length_01 = Math.abs(fpoint_01.y-circle_02.y);//�Ÿ���circle_02��fpoint_01�ľ���
			float length_03 = Math.abs(circle_01.y-circle_02.y);//�Ÿ���circle_02��circle_01�ľ���
			footLength = length_01>length_03?length_01:length_03;
			//����͵�б��,������һ��ֺ�ĵط�72.5��������ֺһ��Ϊ63.5, ͬʱȷ������41%,ʱ����ɫ�ο�����
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
			/**	����ཻ��fpoint_725����ֱ��line_foot�����ͱ�Ե�ĸ�����line_725
				����ཻ��fpoint_635����ֱ��line_foot�����ͱ�Ե�ĸ�����line_635
				Ҫ�жϳ������ߵ����Ҳࣨ��Ϊ����ֺ���Ĵֺ�����ҹ�ϵ��******/
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
			circle_635.x = line_635.x2;						//ȷ�����ҽſ�Ŀɵ�������Ȧ������circle_635��circle_725
			circle_635.y = line_635.y2;
			circle_725.x = line_725.x2;
			circle_725.y = line_725.y2;
			circle_90.x = line_90.x2;
			circle_90.y = line_90.y2;
			circle_825.x = line_825.x2;
			circle_825.y = line_825.y2;
			circle_78.x = line_78.x2;
			circle_78.y = line_78.y2;
			fpoint_725_red.x = fpoint_725.x;				//��ʼ��ֱ������ʱ�ģ�fpoint_XXX_red
			fpoint_725_red.y = fpoint_725.y;
			fpoint_635_red.x = fpoint_635.x;
			fpoint_635_red.y = fpoint_635.y;
			fpoint_90_red.x = fpoint_90.x;
			fpoint_90_red.y = fpoint_90.y;
			fpoint_825_red.x = fpoint_825.x;
			fpoint_825_red.y = fpoint_825.y;
			fpoint_78_red.x = fpoint_78.x;
			fpoint_78_red.y = fpoint_78.y;
			//k68=(circle_725.y-circle_635.y)/(circle_635.x-circle_635.x)ȷ��б���б��
			//ȷ��б���̣�y=k68*(x-circle_725.x)+circle_725.y;
			float k68 = (circle_725.y-circle_635.y)/(circle_725.x-circle_635.x);
			fpoint_68_red.x = circle_01.x;
			fpoint_68_red.y = k68*(fpoint_68_red.x-circle_725.x)+circle_725.y;
			line_68.x1 = circle_635.x;						//���ĸ�����
			line_68.y1 = circle_635.y;
			line_68.x2 = circle_725.x;
			line_68.y2 = circle_725.y;
			
			line_41.y1 = fpoint_41.y;						//ȷ������λ�ڿ� ���ĸ����ߺ����˵Ĳο��ɵ��������λ��(ˮƽ�߶���)
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
			if(fpoint_01.x>circle_03.x){					//��ָ���Ҳ࣬��Ҫ�ı�
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
			judgeBreak();									//ȷ����λ�ڿ���������������ж��㹭�����ơ�
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
			//System.out.println("�����ߵ�б���ǣ�"+kf);
			//System.out.println("�����ߵ�б���ǣ�"+kf_h);//�ø�����Ϊfpoint_18��fpoint_18s������б��
			//System.out.println("�����ߵ�б���ǣ�"+kf_v);//�ø�����Ϊ��ֱ������ߵ�б��
			float kf_h = 0;
			center_angle = dformat.format(Math.random()*3+4);
			float orig_angle = Float.parseFloat(center_angle);//[3,7)��float����//��λ������
			float angle = (180-orig_angle)/2;
			if(circle_01.y<circle_02.y){			//��ֺ���ϵ��������ע�⣺����ʱ�����޹�ϵ��
				if(fpoint_01.x>circle_03.x){		//��ָ���Ҳ࣬Ҳ����˵����������ƫ�Ҳ�
					kf = (float) Math.tan(Math.PI*(90+orig_angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90-angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}else{								//��ָ�����
					kf = (float) Math.tan(Math.PI*(90-orig_angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90+angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}
			}else{									//��ֺ���µ����
				if(fpoint_01.x>circle_03.x){		//��ָ���Ҳ࣬Ҳ����˵����������ƫ�Ҳ�
					kf = (float) Math.tan(Math.PI*(90-orig_angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90+angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}else{								//��ָ�����
					kf = (float) Math.tan(Math.PI*(90+orig_angle)/180);	//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (float) Math.tan(Math.PI*(90-angle)/180);	//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}
			}
			//kf���̽���circle_02�ķ���Ϊ  �� y=kf*(x-circle_02.x)+circle_02.y;
			//kh���̽���fpoint_18�ķ���Ϊ ��  y=kh*(x-fpoint_18.x)+fpoint_18.y;
			fpoint_18s.x = (kf*circle_02.x-kf_h*fpoint_18.x+fpoint_18.y-circle_02.y)/(kf-kf_h);
			fpoint_18s.y = kf*(fpoint_18s.x-circle_02.x)+circle_02.y;
			line_f.x1 = circle_02.x;				//���Ʒ����ߣ�line_f
			line_f.y1 = circle_02.y;
			if(circle_01.y<circle_02.y){			//��ֺ���ϵ����
				line_f.y2 = fpoint_18s.y-100;
			}else{									//��ֺ���µ����
				line_f.y2 = fpoint_18s.y+100;
			}										//x=(y-circle_02.y)/kf+circle_02.x;��x����
			line_f.x2 = (line_f.y2-circle_02.y)/kf+circle_02.x;
			circle_18.x = line_f.x2;
			circle_18.y = line_f.y2;
			kf_v = (-1)/kf;							//����Ϊ: y=kf_v*(x-fpoint_18s.x)+fpoint_18s.y
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
			if(fpoint_01.x>circle_03.x){				//��ָ���Ҳ࣬��Ҫ�ı�
				circle_1801.x = line_18.x1;				//��������ȫ��������ȿɵ�������Ȧ
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
		 * �����߲��Ǵ�ֱ�ߵ������
		 * kz:�����ߵ�б��
		 * kz_v:��ֱ�������ߵ�б��
		 * kf:�����ߵ�б��
		 * kf_h:fpoint_18��fpoint_18s������б��
		 * kf_v:��ֱ������ߵ�б��
		 * */
		else if(circle_01.x != circle_02.x){
			kz = (circle_01.y-circle_02.y)/(circle_01.x-circle_02.x);
			//liney = k*(linex-circle_01.x)+circle_01.y;//linex = (liney-circle_01.y)/k+circle_01.x
			line_foot.y1 = circle_01.y-80;
			line_foot.y2 = circle_02.y+80;
			line_foot.x1 = (line_foot.y1 - circle_01.y)/kz+circle_01.x;
			line_foot.x2 = (line_foot.y2 - circle_01.y)/kz+circle_01.x;
			kz_v = (-1)/kz;									//�����ߵ�б��
			/**��ֱ�����߷��̣�liney=k1*(linex-circle_03.x)+circle_03.y;��������ֱ�ߵĽ��������
			k1*(linex-circle_03.x)+circle_03.y = k*(linex-circle_01.x)+circle_01.y 
			ת��Ϊ���x���꣺linex=(k1*circle_03.x-k*circle_01.x+circle_01.y-circle_03.y)/(k1-k)��x�����귽��
			Ȼ��������ⷽ����y ****/
			fpoint_01.x = (kz_v*circle_03.x-kz*circle_01.x+circle_01.y-circle_03.y)/(kz_v-kz);
			fpoint_01.y = kz*(fpoint_01.x-circle_01.x)+circle_01.y;
			
			if(foot.equals("left")){						//��ֱ�����ߵ���չ����
				line_top.x1 = circle_03.x+80;
				line_top.x2 = fpoint_01.x-80;
			}else{
				line_top.x1 = circle_03.x-80;
				line_top.x2 = fpoint_01.x+80;
			}
			line_top.y1 = (kz_v*(line_top.x1-circle_03.x)+circle_03.y);
			line_top.y2 = (kz_v*(line_top.x2-circle_03.x)+circle_03.y);
			
			line_heel.x1 = circle_02.x-100;					//ע�Ͳ��ָ�Ϊ�������У�circle_02��Ϊ�Ÿ��ĵ�λ�������ж�
			line_heel.x2 = circle_02.x+100;
			line_heel.y1 = kz_v*(line_heel.x1-circle_02.x)+circle_02.y;
			line_heel.y2 = kz_v*(line_heel.x2-circle_02.x)+circle_02.y;
			
			float length_01;									//��ų�,��ˮƽ�ߵ������
			length_01 = (float) Math.sqrt((circle_02.x-fpoint_01.x)*(circle_02.x-fpoint_01.x)+(circle_02.y-fpoint_01.y)*(circle_02.y-fpoint_01.y));
			float length_03 = (float) Math.sqrt((circle_01.x-circle_02.x)*(circle_01.x-circle_02.x)+(circle_01.y-circle_02.y)*(circle_01.y-circle_02.y));
			footLength = length_01>length_03?length_01:length_03;
			/**����͵�б��,����Ĵָ�ĵط�72.5����һ��Ϊ63.5 ��ǰ�᣺��ֺ���ϻ������µģ�Ҫ����໹���Ҳ���ߡ�
			��Ҫ�õ�line_foot�ķ��̣�liney=k*(linex-circle_01.x)+circle_01.y;ֱ�߷���
			(width*0.725f)*(width*0.725f)=(x-circle_01.x)*(x-circle_01.x)+(liney-circle_01.y)*(liney-circle_01.y);
			(width*0.725f)*(width*0.725f)=(x-circle_01.x)*(x-circle_01.x)+(k*x-k*circle_01.x)*(k*x-k*circle_01.x);
			�����˶�Ԫ���η����㷨��̫�����ӣ���ΪX�᷽���ϵĵȱ����㷨,,,û���õ�width�����ߣ�������*/
			float x725;																	//circle_02��Ϊ�Ÿ��������ж�
			float x635;
			float x41;
			float x18;
			float x90;
			float x825;
			float x78;
			float x80;
			float x65;
			if(length_01<length_03){		//widthΪ��circle_01.x��circle_02.x
				x725 = Math.abs(circle_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(circle_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(circle_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(circle_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(circle_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(circle_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(circle_01.x-circle_02.x)*0.78f;
				x80 = Math.abs(circle_01.x-circle_02.x)*0.80f;
				x65 = Math.abs(circle_01.x-circle_02.x)*0.65f;
			}else{							//widthΪ��circle_02.x��fpoint_01.x
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
			if(circle_01.x<circle_02.x){	//�жϽŸ������ҷ�λ
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
			/**����ཻ��fpoint_725����ֱ��line_width�����ͱ�Ե�ĸ�����line_725
			����ཻ��fpoint_635����ֱ��line_width�����ͱ�Ե�ĸ�����line_635
			Ҫ�жϳ������ߵ����Ҳࣨ��Ϊ����ֺ���Ĵֺ�����ҹ�ϵ��������
			�����߷��̣�liney=k1*(linex-line.x)+line.y;float k1=(-1)/k;��������ֱ�ߵĽ��������
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
			line_635.y2 = kz_v*(line_635.x2-line_635.x1)+line_635.y1;	//��ȷ�������ҽſ����ߣ���ȷ�������ߵ������˵�
			line_725.y2 = kz_v*(line_725.x2-line_725.x1)+line_725.y1;
			line_90.y2 = kz_v*(line_90.x2-line_90.x1)+line_90.y1;
			line_825.y2 = kz_v*(line_825.x2-line_825.x1)+line_825.y1;
			line_78.y2 = kz_v*(line_78.x2-line_78.x1)+line_78.y1;
			circle_635.x = line_635.x2;									//��ֵcircle_XXX
			circle_635.y = line_635.y2;
			circle_725.x = line_725.x2;
			circle_725.y = line_725.y2;
			circle_90.x = line_90.x2;
			circle_90.y = line_90.y2;
			circle_825.x = line_825.x2;
			circle_825.y = line_825.y2;
			circle_78.x = line_78.x2;
			circle_78.y = line_78.y2;
			fpoint_725_red.x = line_725.x1;								//��ֵfpoint_XXX_red
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
			//float k68=(circle_725.y-circle_635.y)/(circle_635.x-circle_635.x)ȷ��б���б��
			//ȷ��б���̣�y=k68*(x-circle_725.x)+circle_725.y;
			float k68 = (circle_725.y-circle_635.y)/(circle_725.x-circle_635.x);
			fpoint_68_red.x = (k68*circle_725.x-kz*circle_01.x+circle_01.y-circle_725.y)/(k68-kz);
			fpoint_68_red.y = k68*(fpoint_68_red.x-circle_725.x)+circle_725.y;
			line_68.x1 = circle_635.x;									//���ĸ�����
			line_68.y1 = circle_635.y;
			line_68.x2 = circle_725.x;
			line_68.y2 = circle_725.y;
			//ȷ������λ�ڿ� ���ĸ����ߺ����˵Ĳο��ɵ��������λ�ã���ˮƽ�߶��ϣ�
			//Ҫ�õ��������໥��ֱ�ķ���: liney=k*(linex-circle_01.x)+circle_01.y;liney=k1*(linex-fpoint_41.x)+fpoint_41.y;
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
			judgeBreak();									//ȷ����λ�ڿ���������������ж��㹭�����ơ�
			break_01.y = kz_v*(break_01.x-fpoint_41.x)+fpoint_41.y;
			break_02.y = kz_v*(break_02.x-fpoint_41.x)+fpoint_41.y;
			break_01.y1 = break_01.y-8;						//Ҫ�õ��������໥��ֱ�ķ���: liney=k*(linex-break_01.x)+break_01.y;
			break_01.y2 = break_01.y+8;						//ת��(liney-break_01.y)/k+break_01.x=linex;
			break_01.x1 = (break_01.y1-break_01.y)/kz+break_01.x;
			break_01.x2 = (break_01.y2-break_01.y)/kz+break_01.x;
			break_02.y1 = break_02.y-8;
			break_02.y2 = break_02.y+8;
			break_02.x1 = (break_02.y1-break_02.y)/kz+break_02.x;
			break_02.x2 = (break_02.y2-break_02.y)/kz+break_02.x;
			kf = 0;											//��������ߵ���ظ����ߺͲο���
			float kf_h = 0;
			float tan7;
			float orig_angle;
			float tan865;
			while(true){
				center_angle = dformat.format(Math.random()*3+4);
				orig_angle = Float.parseFloat(center_angle);//[3,7)��float���� ��7.0f;
				tan7 = (float) Math.tan(Math.PI*orig_angle/180);
				if(circle_01.y<circle_02.y){		//�ų�������Ϊ��ֱ�ߵ����
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
			if(circle_01.y<circle_02.y){			//��ֺ���ϵ��������ע�⣺����ʱ�����޹�ϵ��
				if(fpoint_01.x>circle_03.x){		//��ָ���Ҳ࣬Ҳ����˵����������ƫ�Ҳ�
					kf = (kz+tan7)/(1-kz*tan7);		//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (kz-tan865)/(1+kz*tan865);//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}else{								//��ָ�����
					kf = (kz-tan7)/(1+kz*tan7);		//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (kz+tan865)/(1-kz*tan865);//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}
			}else{									//��ֺ���µ����
				if(fpoint_01.x>circle_03.x){		//��ָ���Ҳ࣬Ҳ����˵����������ƫ�Ҳ�
					kf = (kz-tan7)/(1+kz*tan7);		//tan(a-X) = (tana-tanX)/(1+tanatanX)
					kf_h = (kz+tan865)/(1-kz*tan865);//tan(a+X) = (tana+tanX)/(1-tanatanX)
				}else{								//��ָ�����
					kf = (kz+tan7)/(1-kz*tan7);		//tan(a+X) = (tana+tanX)/(1-tanatanX)
					kf_h = (kz-tan865)/(1+kz*tan865);//tan(a-X) = (tana-tanX)/(1+tanatanX)
				}
			}
			float a = (kz+kf)/(1-kz*kf);			//(K1+K2)/(1-K1K2)=2K/(1-K^2)
			float b = 2*kf_h/(1-kf_h*kf_h);
			System.out.println("��֤��������������б�ʹ�ϵ��"+a+"/"+b);
			//kf���̽���circle_02�ķ���Ϊ  �� y=kf*(x-circle_02.x)+circle_02.y;
			//kh���̽���fpoint_18�ķ���Ϊ ��  y=kh*(x-fpoint_18.x)+fpoint_18.y;
			fpoint_18s.x=(kf*circle_02.x-kf_h*fpoint_18.x+fpoint_18.y-circle_02.y)/(kf-kf_h);
			fpoint_18s.y=kf*(fpoint_18s.x-circle_02.x)+circle_02.y;
			line_f.x1=circle_02.x;					//���Ʒ����ߣ�line_f
			line_f.y1=circle_02.y;
			if(circle_01.y<circle_02.y){			//��ֺ���ϵ����
				line_f.y2=fpoint_18s.y-100;
			}else{									//��ֺ���µ����
				line_f.y2=fpoint_18s.y+100;
			}										//x=(y-circle_02.y)/kf+circle_02.x;��x����
			line_f.x2=(line_f.y2-circle_02.y)/kf+circle_02.x;
			circle_18.x=line_f.x2;
			circle_18.y=line_f.y2;
			kf_v=(-1)/kf;							//����Ϊ: y=kf_v*(x-fpoint_18s.x)+fpoint_18s.y
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
			if(fpoint_01.x>circle_03.x){				//��ָ���Ҳ࣬��Ҫ�ı�
				circle_1801.x = line_18.x1;				//��������ȫ��������ȿɵ�������Ȧ
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
		//�������֣������������Ƿ�Ϊ����
		//��ʼ����б�߼нǣ��������еĲ�����ʼ����ֵ�������
		if(circle_725.x==circle_80.x){				// line_80Ϊ��ֱ��
			line_80.x1 = circle_80.x;
			line_80.x2 = circle_725.x;
			if(circle_725.y>circle_80.y){			// circle_725��circle_80������λ�ù�ϵ
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
		}else{
			float k80 = (circle_725.y-circle_80.y)/(circle_725.x-circle_80.x);
			//����Ϊ��y=k80*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k80+circle_725.x
			if(circle_725.y>circle_80.y){			// circle_725��circle_80������λ�ù�ϵ
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
			line_80.x1 = (line_80.y1-circle_725.y)/k80+circle_725.x;
			line_80.x2 = (line_80.y2-circle_725.y)/k80+circle_725.x;
		}
		if(circle_725.x==circle_65.x){				// line_65Ϊ��ֱ��
			line_65.x1 = circle_65.x;
			line_65.x2 = circle_725.x;
			if(circle_725.y>circle_65.y){			// circle_725��circle_80������λ�ù�ϵ
				line_65.y1 = circle_65.y-50;
				line_65.y2 = circle_725.y+100;
			}else{
				line_65.y1 = circle_65.y+50;
				line_65.y2 = circle_725.y-100;
			}
		}else{
			float k65 = (circle_725.y-circle_65.y)/(circle_725.x-circle_65.x);
			//����Ϊ��y=k65*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k65+circle_725.x
			if(circle_725.y>circle_65.y){			// circle_725��circle_80������λ�ù�ϵ
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
		//��ȡ��ʼδ����֮ǰ������...
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
			float length_float = Float.parseFloat(foot_length_str);//Ԥ����������Ϊ��һ�����ݵ�+(0.0,1.0)
			float length_pre = Float.parseFloat(foot_length_pre);
			if(length_pre<length_float&&length_float<length_pre+1){
				return foot_length_str;
			}else{
				// modify by kael
				float l=(float) (length_pre+0.25+Math.random()*0.5);
				float num=(float)(Math.round(l*100.0)/100.0);
				return String.valueOf(num);
				// modify by kael over
//				return String.valueOf(length_pre+0.25+Math.random()*0.5);//���򷵻���һ����+(0.25,0.75)
			}
		}
	}
	private String getFoot_width(){
		String foot_width_str = calcUtil.getFootLength_mm(line_725.getLength()+line_635.getLength(),dpi,scale);
		if(foot_width_pre==null||foot_width_pre.equals("")){
			return foot_width_str;
		}else{
			float width_float = Float.parseFloat(foot_width_str);//Ԥ����������Ϊ��һ�����ݵ�+(0.0,0.5)
			float width_pre = Float.parseFloat(foot_width_pre);
			if(width_pre<width_float&&width_float<width_pre+0.5){
				return foot_width_str;
			}else{
				// modify by kael
				float w=(float) (width_pre+0.25+Math.random()*0.5);
				float num=(float)(Math.round(w*100.0)/100.0);
				return String.valueOf(num);
				// modify by kael over
//				return String.valueOf(width_pre+0.125+Math.random()*0.25);//���򷵻���һ����+(0.125,0.375)
			}
		}
	}
	
	private void judgeBreak(){							//�ж�������λ�ڵ��X������
		float length_abs;
		Line line_break = new Line();					//��line_break���ڸ����ж�������������ʾ����������
		line_break.y1 = fpoint_41.y;					//�����line_breakˮƽ������������Ľ���
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
		if(circle_635.x<circle_725.x){									//��������������ߵ����//������
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
		}else{															//��������������ߵ��Ҳ�//���ҵ���
			for(int i=(int)line_break.x2-10;i<=(line_break.x1+width*0.2f);i--){
				if(IsBreakEdge(i-orig_x,j)){
					if(count_break==0){									//��һ�������¼
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
	private boolean IsBreakEdge(int x,int y){			//�ж϶�λ�ڵ�ı߽��
		if(((bufImage.getRGB(x,y) & 0xff00 ) >> 8)<80){	//����̫��ֱ���ж�Ϊfalse
			return false;
		}
		int left_Pcount = 0;			//�������֮��
		int right_Pcount = 0;			//�ұ�����֮��
		for(int i=5;i<15;i++){			//����ǰ5�������10
			int pixel_left = bufImage.getRGB(x+i, y);	//�Ƚ��������ߵ��̻�ɫ
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
	
	//�����ͼƬ���ʱ��Ӱ�������ߣ���ʼ������ʾ���Ͳ�������͸�����
	class FootPaneMouseListener implements MouseListener{
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			int cur_x = e.getX();
	    	int cur_y = e.getY();
			if(!draging){
				drag_method = 1;
		    	//ֻ�е��϶���������ԭʼ����С��5ʱ�����϶���//��ΪС��10
		    	if((int) Math.sqrt((cur_x-circle_01.x)*(cur_x-circle_01.x)+(cur_y-circle_01.y)*(cur_y-circle_01.y))<10){
		    		dragnum = 1;			//���϶�����ֺ����ţ�1
		    		draging = true;
		    		cross_cursor.type = 1;
			    	cross_cursor.x = circle_01.x;
					cross_cursor.y = circle_01.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_02.x)*(cur_x-circle_02.x)+(cur_y-circle_02.y)*(cur_y-circle_02.y))<10){
		    		dragnum = 2;			//���϶��ź������ţ�2
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_02.x;
					cross_cursor.y = circle_02.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_03.x)*(cur_x-circle_03.x)+(cur_y-circle_03.y)*(cur_y-circle_03.y))<10){
		    		dragnum = 3;			//���϶���Ĵֺ����ţ�3
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_03.x;
					cross_cursor.y = circle_03.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_725.x)*(cur_x-circle_725.x)+(cur_y-circle_725.y)*(cur_y-circle_725.y))<10){
		    		dragnum = 4;			//���϶���һ��ֺ����ţ�4
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_725.x;
					cross_cursor.y = circle_725.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_635.x)*(cur_x-circle_635.x)+(cur_y-circle_635.y)*(cur_y-circle_635.y))<10){
		    		dragnum = 5;			//���϶�������ֺ����ţ�5
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_635.x;
					cross_cursor.y = circle_635.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_4101.x)*(cur_x-circle_4101.x)+(cur_y-circle_4101.y)*(cur_y-circle_4101.y))<10){
		    		dragnum = 6;			//���϶���λ���01����ţ�6
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_4101.x;
					cross_cursor.y = circle_4101.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-circle_4102.x)*(cur_x-circle_4102.x)+(cur_y-circle_4102.y)*(cur_y-circle_4102.y))<10){
		    		dragnum = 7;			//���϶���λ���02����ţ�7
		    		draging = true;
		    		cross_cursor.type = 1;
		    		cross_cursor.x = circle_4102.x;
					cross_cursor.y = circle_4102.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-break_01.x)*(cur_x-break_01.x)+(cur_y-break_01.y)*(cur_y-break_01.y))<12){
		    		dragnum = 8;			//���϶���λ�ڿ�01����ţ�8
		    		draging = true;
		    		cross_cursor.type = 2;
		    		cross_cursor.x = break_01.x;
					cross_cursor.y = break_01.y;
					repaint();
		    	}else if((int) Math.sqrt((cur_x-break_02.x)*(cur_x-break_02.x)+(cur_y-break_02.y)*(cur_y-break_02.y))<12){
		    		dragnum = 9;			//���϶���λ�ڿ�02����ţ�9
		    		draging = true;
		    		cross_cursor.type = 2;
		    		cross_cursor.x = break_02.x;
					cross_cursor.y = break_02.y;
					repaint();
		    	}
			}
			//�����������϶�״̬ʱ���ο��㴦��ѡ��״̬����ʹ��΢������ʽ��
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
				if(draging){						//�϶��ο�����㣬�ɿ��󣬽��϶���־λ��Ϊtrue
					draging = false;
					draged = false;
					repaint();
				}
			}
		}
	}
	/**���µ�������ע�⼸������
	 * 1��circle_01���ϣ�circle_02����
	 * 2��circle_01��circle_03���ҵ�����
	 * 3��Ϊ����������ԡ��ж���circle_01��circle_02˭���ǽŸ������⣨������circle_01Ϊ����ָ��circle_02Ϊ�ź����
	 * 4��ֻ��circle_03���ǲ����
	 * */
	private class FootPaneMouseMotionListener implements MouseMotionListener{
		
		public void mouseDragged(MouseEvent e) {
			if(!draging||drag_method!=1){		//���û�л�������㣬�򷵻ء�
				return;
			}
			draged = true;						//��־Ϊ�Ѿ������϶�����
			
			if(kz == 0){						//���ڵ�һ�Σ����б��Ϊnull������г�ʼ����
				slope_zhong();					//��ȡ������б�ʺʹ�ֱ��б��
			}
			if(kf == 0){						//���ڵ�һ�Σ����б��Ϊnull������г�ʼ����
				slope_fen();					//��ȡ������б�ʺʹ�ֱ��б��
			}
			if(length_18 == 0){
				length_18();
			}
			
			if(dragnum == 1){						//����circle_01����ֺ
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
			}else if(dragnum == 2){				//����circle_02�Ÿ�
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
			}else if(dragnum == 3){				//����circle_03�ų�������
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
			}else if(dragnum == 4){				//�϶���һ��ֺcircle_725
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
			}else if(dragnum == 5){				//�϶�������ֺcircle_635
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
			}else if(dragnum == 6){				//�϶����ѵ�circle_4101				
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_4101.x = e.getX();
				}
				cross_cursor.x = circle_4101.x;
				cross_cursor.y = circle_4101.y;
				circle_4101();
				footStatus();
				repaint();
			}else if(dragnum == 7){				//�϶����ѵ�circle_4102
				if(0<=e.getX()&&e.getX()<=pane_width){
					circle_4102.x = e.getX();
				}
				cross_cursor.x = circle_4102.x;
				cross_cursor.y = circle_4102.y;
				circle_4102();
				footStatus();
				repaint();
			}else if(dragnum == 8){				//�϶���λ��break_01
				if(0<=e.getX()&&e.getX()<=pane_width){
					break_01.x = e.getX();
				}
				break_01();
				cross_cursor.x = break_01.x;
				cross_cursor.y = break_01.y;
				footStatus();
				repaint();
			}else if(dragnum == 9){				//�϶���λ��break_02
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
		if(circle_01.x==circle_02.x){								//���������Ϊ��ֱ�ߵĻ���б�������������
			return;
		}
		kz = (circle_01.y-circle_02.y)/(circle_01.x-circle_02.x);	//ֱ�߷��̵�б��,ֱ�߷���:liney=k*(linex-circle_01.x)+circle_01.y;
		kz_v = (-1)/kz;												//��ֱ�����ߵ�б��
	}
	private void slope_fen(){										//�����ߵ�б�ʣ��Լ���ֱ�ߵ�б��
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){
			if(circle_18.x==circle_01.x){
				System.out.println("������Ϊ��ֱ��");
			}else{
				kf=(circle_18.y-circle_01.y)/(circle_18.x-circle_01.x);
				kf_v=(-1)/kf;
			}
		}else{
			if(circle_18.x==circle_02.x){
				System.out.println("������Ϊ��ֱ��");
			}else{
				kf=(circle_18.y-circle_02.y)/(circle_18.x-circle_02.x);
				kf_v=(-1)/kf;
			}
		}
	}
	private void line_foot(){
		if(circle_01.x==circle_02.x){								//�����ֱ�������ߵĻ�
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
		if(circle_01.x>circle_02.x){								//�ж���������˵�X����Ĵ�С
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
		//�����߷��̣�liney=k1*(linex-circle_03.x)+circle_03.y;��������ֱ�ߵĽ��������
		//k1*(linex-circle_03.x)+circle_03.y=k*(linex-circle_01.x)+circle_01.y ת��Ϊ���x���꣬Ȼ��������ⷽ����y
		//linex=(k1*circle_03.x-k*circle_01.x+circle_01.y-circle_03.y)/(k1-k)��x�����귽��
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
		//�����ж�circle_01��circle_02˭Ϊ�Ÿ��ͽ�ֺ
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
	private void getLengthAndFpoint_xxx(){							//��ȡ�ų�����ֺ�ٷֵ��λ��
		if(circle_01.x==circle_02.x){
			float length_01 = Math.abs(fpoint_01.y-circle_01.y);
			float length_02 = Math.abs(fpoint_01.y-circle_02.y);
			length_01 = length_01<length_02?length_02:length_01;
			float length_03 = Math.abs(circle_01.y-circle_02.y);
			float length = length_01>length_03?length_01:length_03;
			//����͵�б��,����Ĵָ�ĵط�72.5����һ��Ϊ63.5, ����ǰ�᣺��Ҫ�жϽŸ��㣻��ֺ���ϻ������µģ�Ҫ����໹���Ҳ���ߡ�
			fpoint_725.x = circle_01.x;
			fpoint_635.x = circle_01.x;
			fpoint_41.x = circle_01.x;
			fpoint_18.x = circle_01.x;
			fpoint_90.x = circle_01.x;
			fpoint_825.x = circle_01.x;
			fpoint_78.x = circle_01.x;
			if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){		//circle_01Ϊ   ~ �Ÿ���
				if(circle_01.y<circle_02.y){		
					fpoint_725.y = circle_01.y+length*0.725f;//circle_01�Ÿ�����
					fpoint_635.y = circle_01.y+length*0.635f;
					fpoint_41.y = circle_01.y+length*0.41f;
					fpoint_18.y = circle_01.y+length*0.18f;
					fpoint_90.y = circle_01.y+length*0.90f;
					fpoint_825.y = circle_01.y+length*0.825f;
					fpoint_78.y = circle_01.y+length*0.78f;
				}else{								
					fpoint_725.y = circle_01.y-length*0.725f;//circle_01�Ÿ�����
					fpoint_635.y = circle_01.y-length*0.635f;
					fpoint_41.y = circle_01.y-length*0.41f;
					fpoint_18.y = circle_01.y-length*0.18f;
					fpoint_90.y = circle_01.y-length*0.90f;
					fpoint_825.y = circle_01.y-length*0.825f;
					fpoint_78.y = circle_01.y-length*0.78f;
				}
			}else{																			//����circle_02Ϊ�Ÿ���
				if(circle_01.y<circle_02.y){
					fpoint_725.y = circle_02.y-length*0.725f;//circle_02�Ÿ�����
					fpoint_635.y = circle_02.y-length*0.635f;
					fpoint_41.y = circle_02.y-length*0.41f;
					fpoint_18.y = circle_02.y-length*0.18f;
					fpoint_90.y = circle_02.y-length*0.90f;
					fpoint_825.y = circle_02.y-length*0.825f;
					fpoint_78.y = circle_02.y-length*0.78f;
				}else{
					fpoint_725.y = circle_02.y+length*0.725f;//circle_02�Ÿ�����
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
		if(Math.abs(fpoint_01.x-circle_01.x)>Math.abs(fpoint_01.x-circle_02.x)){		//�����fpoint_01��circle_01�Ƚ�Զ
			length_01 = (float) Math.sqrt((circle_01.x-fpoint_01.x)*(circle_01.x-fpoint_01.x)+(circle_01.y-fpoint_01.y)*(circle_01.y-fpoint_01.y));
		}else{
			length_01 = (float) Math.sqrt((circle_02.x-fpoint_01.x)*(circle_02.x-fpoint_01.x)+(circle_02.y-fpoint_01.y)*(circle_02.y-fpoint_01.y));
		}
		float length_03 = (float) Math.sqrt((circle_01.x-circle_02.x)*(circle_01.x-circle_02.x)+(circle_01.y-circle_02.y)*(circle_01.y-circle_02.y));
		float length = length_01>length_03?length_01:length_03;
		//�����˶�Ԫ���η����㷨��̫�����ӣ���ΪX�᷽���ϵĵȱ����㷨�����63.5%��72.5%�Ľų���λ������
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			float x725;					//X��Ĳ�ֵ
			float x635;
			float x41;
			float x18;
			float x90;
			float x825;
			float x78;
			if(length==length_03){		//circle_01.x��circle_02.x
				x725 = Math.abs(circle_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(circle_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(circle_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(circle_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(circle_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(circle_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(circle_01.x-circle_02.x)*0.78f;
			}else{						//circle_01.x��fpoint_01.x
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
		}else{																		//����circle_02Ϊ�Ÿ���
			float x725;
			float x635;
			float x41;
			float x18;
			float x90;
			float x825;
			float x78;
			if(length==length_03){		//circle_01.x��circle_02.x
				x725 = Math.abs(circle_01.x-circle_02.x)*0.725f;
				x635 = Math.abs(circle_01.x-circle_02.x)*0.635f;
				x41 = Math.abs(circle_01.x-circle_02.x)*0.41f;
				x18 = Math.abs(circle_01.x-circle_02.x)*0.18f;
				x90 = Math.abs(circle_01.x-circle_02.x)*0.90f;
				x825 = Math.abs(circle_01.x-circle_02.x)*0.825f;
				x78 = Math.abs(circle_01.x-circle_02.x)*0.78f;
			}else{						//circle_01.x��fpoint_01.x
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
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			length_18=(float) getTwoCoordLength(circle_01.x,circle_01.y,fpoint_18.x,fpoint_18.y);
		}else{																		//circle_02Ϊ   ~ �Ÿ���
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
		if(circle_725.x==circle_80.x){			// line_80Ϊ��ֱ��
			line_80.x1 = circle_80.x;
			line_80.x2 = circle_725.x;
			if(circle_725.y>circle_80.y){		// circle_725��circle_80������λ�ù�ϵ
				line_80.y1 = circle_80.y-50;
				line_80.y2 = circle_725.y+100;
			}else{
				line_80.y1 = circle_80.y+50;
				line_80.y2 = circle_725.y-100;
			}
		}else{
			float k80=(circle_725.y-circle_80.y)/(circle_725.x-circle_80.x);
			//����Ϊ��y=k80*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k80+circle_725.x
			if(circle_725.y>circle_80.y){		// circle_725��circle_80������λ�ù�ϵ
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
		if(circle_725.x==circle_65.x){			// line_65Ϊ��ֱ��
			line_65.x1 = circle_65.x;
			line_65.x2 = circle_725.x;
			if(circle_725.y>circle_65.y){		// circle_725��circle_80������λ�ù�ϵ
				line_65.y1 = circle_65.y-50;
				line_65.y2 = circle_725.y+100;
			}else{
				line_65.y1 = circle_65.y+50;
				line_65.y2 = circle_725.y-100;
			}
		}else{
			float k65 = (circle_725.y-circle_65.y)/(circle_725.x-circle_65.x);
			//����Ϊ��y=k65*(x-circle_725.x)+circle_725.y;=> x=(y-circle_725.y)/k65+circle_725.x
			if(circle_725.y>circle_65.y){		// circle_725��circle_80������λ�ù�ϵ
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
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			if(circle_18.x==circle_01.x){		//������Ϊ��ֱ��
				fpoint_18s.x=circle_18.x;
				if(circle_18.y<circle_01.x){	//cricle_18���ڵ�������ϵĻ�
					fpoint_18s.y=circle_01.y-length_18;
				}else{							//cricle_18���ڵ�������µĻ�
					fpoint_18s.y=circle_01.y+length_18;
				}
			}else{								//�����߲�Ϊ��ֱ��
				float scale_18 = (float) (length_18/line_f.getLength());	//���ĳ���������ĵĳ���֮��
				float x18_f=Math.abs(circle_18.x-circle_01.x)*scale_18;
				if(circle_18.x<circle_01.x){	//cricle_18���ڵ���������
					fpoint_18s.x=circle_01.x-x18_f;
				}else{							//cricle_18���ڵ�������ұ�
					fpoint_18s.x=circle_01.x+x18_f;
				}
				fpoint_18s.y=kf*(fpoint_18s.x-circle_01.x)+circle_01.y;
			}
		}else{																		//circle_02Ϊ   ~ �Ÿ���
			if(circle_18.x==circle_02.x){		//������Ϊ��ֱ��
				fpoint_18s.x=circle_18.x;
				if(circle_18.y<circle_02.x){	//cricle_18���ڵ�������ϵĻ�
					fpoint_18s.y=circle_02.y-length_18;
				}else{							//cricle_18���ڵ�������µĻ�
					fpoint_18s.y=circle_02.y+length_18;
				}
			}else{								//�����߲�Ϊ��ֱ��
				float scale_18=(float) (length_18/line_f.getLength());		//���ĳ���������ĵĳ���֮��
				float x18_f=Math.abs(circle_18.x-circle_02.x)*scale_18;
				if(circle_18.x<circle_02.x){	//cricle_18���ڵ���������
					fpoint_18s.x=circle_02.x-x18_f;
				}else{							//cricle_18���ڵ�������ұ�
					fpoint_18s.x=circle_02.x+x18_f;
				}
				fpoint_18s.y=kf*(fpoint_18s.x-circle_02.x)+circle_02.y;
			}
		}
	}
	private void line_f(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			line_f.x1=circle_01.x;
			line_f.y1=circle_01.y;
			line_f.x2=circle_18.x;
			line_f.y2=circle_18.y;
		}else{																		//circle_02Ϊ   ~ �Ÿ���
			line_f.x1=circle_02.x;
			line_f.y1=circle_02.y;
			line_f.x2=circle_18.x;
			line_f.y2=circle_18.y;
		}
	}
	private void circle_1801(){
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			if(circle_18.x==circle_01.x){
				circle_1801.y=fpoint_18s.y;
			}else{
				circle_1801.y=kf_v*(circle_1801.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}else{																		//circle_02Ϊ   ~ �Ÿ���
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
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			if(circle_18.x==circle_01.x){
				circle_1802.y=fpoint_18s.y;
			}else{
				circle_1802.y=kf_v*(circle_1802.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}else{																		//circle_02Ϊ   ~ �Ÿ���
			if(circle_18.x==circle_02.x){
				circle_1802.y=fpoint_18s.y;
			}else{
				circle_1802.y=kf_v*(circle_1802.x-fpoint_18s.x)+fpoint_18s.y;
			}
		}
		line_18.x2=circle_1802.x;
		line_18.y2=circle_1802.y;
	}
	private void angle_du(){							//���������
		//cos��=(a^2+b^2-c^2��/(2ab)
		//�����ߵķ��̣�y=k*(x-circle_01.x)+circle_01.y;=>x=(y-circle_01.y)/k+circle_01.x;
		fpoint_18h.y=circle_18.y;
		if(circle_01.x==circle_02.x){					//������Ϊ��ֱ��
			fpoint_18h.x = circle_01.x;
		}else{											//�����߲�Ϊ��ֱ��
			fpoint_18h.x = (fpoint_18h.y-circle_01.y)/kz+circle_01.x;
		}
		float a = (float) line_f.getLength();
		double b;
		if(Math.abs(fpoint_01.y-circle_01.y)>Math.abs(fpoint_01.y-circle_02.y)){	//circle_01Ϊ   ~ �Ÿ���
			b = getTwoCoordLength(fpoint_18h.x,fpoint_18h.y,circle_01.x,circle_01.y);
		}else{																		//circle_02Ϊ   ~ �Ÿ���
			b = getTwoCoordLength(fpoint_18h.x,fpoint_18h.y,circle_02.x,circle_02.y);
		}
		double c = getTwoCoordLength(fpoint_18h.x,fpoint_18h.y,circle_18.x,circle_18.y);
		double cos_angle = (a*a+b*b-c*c)/(2*a*b);		//����Ƕȵ�����ֵ//System.out.println("a="+a+",b="+b+",c="+c);
		double arccos_angle = Math.acos(cos_angle);
		double pi_cos_angle = (arccos_angle/Math.PI)*180;
		String str_angle = dformat.format(pi_cos_angle);
		center_angle = str_angle;
	}
	@SuppressWarnings("unused")
	private void incline_angle(){						//б�߼н�
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
	
	private void updateData(){				//��������ģ��
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
	
	public boolean isGetPara(){				//�ж��Ƿ�õ�Ŀ������
		return foot_length==null?false:true;
	}
	
    //��ô�ӡ����ֵ
	public String[] getParaValue(){
		if(foot_length==null){				//�ȴ�����
			return null;
		}
		String[] values = new String[25];
		values[0] = foot_length;
		values[1] = foot_width;
		values[2] = shoe_length;			//�Ƽ�Ь��
		values[3] = type_width;				//�Ƽ��Ϳ�
		values[4] = foot_status;			//�㹭̬��
		values[5] = foot_advice;			//ר�����
		values[6] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_90_red.x,fpoint_90_red.y,circle_02.x,circle_02.y),dpi,scale);//Ĵֺ��ͻ��
		values[7] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_825_red.x,fpoint_825_red.y,circle_02.x,circle_02.y),dpi,scale);//Сֺ�˵�
		values[8] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_78_red.x,fpoint_78_red.y,circle_02.x,circle_02.y),dpi,scale);//Сֺ��ͻ��
		values[9] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_725_red.x,fpoint_725_red.y,circle_02.x,circle_02.y),dpi,scale);//�㹭��length_725
		values[10] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_635_red.x,fpoint_635_red.y,circle_02.x,circle_02.y),dpi,scale);//����ֺ�ؽ�length_635
		values[11] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_68_red.x,fpoint_68_red.y,circle_02.x,circle_02.y),dpi,scale);//�ŵص�length_68
		values[12] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_41.x,fpoint_41.y,circle_02.x,circle_02.y),dpi,scale);//����length_41
		values[13] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_18.x,fpoint_18.y,circle_02.x,circle_02.y),dpi,scale);//����length_18
		values[14] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_90_red.x,fpoint_90_red.y,circle_90.x,circle_90.y),dpi,scale);//Ĵֺ���width_90
		values[15] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_78_red.x,fpoint_78_red.y,circle_78.x,circle_78.y),dpi,scale);//Сֺ���width_78
		values[16] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_725_red.x,fpoint_725_red.y,circle_725.x,circle_725.y),dpi,scale);//һ��ֺ���width_725
		values[17] = calcUtil.getFootLength_mm(getTwoCoordLength(fpoint_635_red.x,fpoint_635_red.y,circle_635.x,circle_635.y),dpi,scale);//����ֺ���width_635
		values[18] = calcUtil.getFootLength_mm(getTwoCoordLength(circle_725.x,circle_725.y,circle_635.x,circle_635.y),dpi,scale);//б��width_68
		values[19] = calcUtil.getFootLength_mm(getTwoCoordLength(break_01.x,break_01.y,break_02.x,break_02.y),dpi,scale);//�������width_41
		values[20] = calcUtil.getFootLength_mm(getTwoCoordLength(circle_4101.x,circle_4101.y,circle_4102.x,circle_4102.y),dpi,scale);//����ȫ��width_41full
		values[21] = calcUtil.getFootLength_mm(getTwoCoordLength(circle_1801.x,circle_1801.y,circle_1802.x,circle_1802.y),dpi,scale);//����ȫ��width_18
		values[22] = dformat.format(Double.parseDouble(values[1])/Double.parseDouble(values[21]));//��ȱ�
		values[23] = center_angle;
		values[24] = incline_angle;
		return values;
	}
	
	public float[] getCoord(){					//��ȡ����������Ĳ�������
		float[] group = new float[34];
		group[0] = circle_01.x-orig_x;			//�ն˰棺18������
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
		group[18] = circle_18.x-orig_x;			//רҵ�棺16��������׷�ӣ�
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
	
	//�ж�������Ƿ�����˽���
	public boolean haveImage(){
		if(bufImage==null){
			return false;
		}else{
			return true;
		}
	}
	//�����ǰ���������ļ��Ĳ���
	public void printAnalyseConfig(){
		System.out.println("this class in "+this.foot+" FootPane print AnalyseConfig");
		System.out.println("time erosion:"+analyseConfig.getTime_erosion());
		System.out.println("time dilate:"+analyseConfig.getTime_dilate());
		System.out.println("stamp threshold:"+analyseConfig.getStamp_threshold());
		System.out.println("stamp radius:"+analyseConfig.getStamp_radius());
	}
	
	//�õ������ο������ľ���
	private double getTwoCoordLength(float x1,float y1,float x2,float y2){
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
    private class Circle{
		private float x,y;
		private float radius = 5.0f;
		private Color color = Color.GREEN;
		private String tiptext = null;
	    private Font gen_font = new Font("΢���ź�", Font.PLAIN, 12);
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
		private Font gen_font = new Font("΢���ź�", Font.PLAIN, 12);
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