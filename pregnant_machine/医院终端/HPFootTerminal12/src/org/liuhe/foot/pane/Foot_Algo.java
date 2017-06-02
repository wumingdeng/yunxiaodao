package org.liuhe.foot.pane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import org.liuhe.algorithm.analyse.ConnectFilter;
import org.liuhe.algorithm.analyse.EroDilFilter;
import org.liuhe.algorithm.analyse.PixelPoint;
import org.liuhe.algorithm.analyse.SimpleEdgeFilter;
import org.liuhe.algorithm.analyse.SpecialFilter;
import org.liuhe.algorithm.analyse.StampFilter;
import org.liuhe.algorithm.scan.ContrastFilter;
import org.liuhe.algorithm.scan.OverturnFilter;

public class Foot_Algo extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private BufferedImage originalImage;
	private BufferedImage bufImage;
	int orig_x;								//X轴坐标点
	int orig_y;								//Y轴坐标点
	
	float bright = 1.0f;					//亮度参数     0.0~2.0
	float contrast = 1.0f;					//对比度参数	0.0~2.0
	int left;								//剪切的四个边缘
	int right;
	int top;
	int bottom;
	
	float radius = 10.0f;
	float threshold = 0.16f;
	int erosion;
	int dilate;
	boolean useSpecial;
	
	public boolean first_paint = true;
	
	private JTextArea text_area;			//文本域用于显示处理信息
	private GeneralPath gPath = null;
	public boolean showPath = false;
	final BasicStroke[] bs = new BasicStroke[6];
    int index = 0;							//用于配合显示蚂蚁线
    Timer timer ;
    private boolean change = true;			//方式转换标志
    
	public Foot_Algo(JTextArea text_area){
		this.text_area = text_area;
		timer = new Timer(50,this);
		float[] dash = new float[]{5,5};
		for (int i = 0; i < 6; i++) {
			bs[i] = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, i);
		}
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==3){
					change = !change;
				}
			}
		});
	} 
	public void setTop(int top){			//设置分析时候的上下左右、亮度对比度、膨胀腐蚀、stamp半径及阈值，参数。
		this.top = top;
	}
	public void setBottom(int bottom){
		this.bottom = bottom;
	}
	public void setLeft(int left){
		this.left = left;
	}
	public void setRight(int right){
		this.right = right;
	}
	public void setBright(int bright){
	    this.bright = (bright+100)*1.0f/100;
	}
	public void setContrast(int contrast){
    	this.contrast = (contrast+100)*1.0f/100;
    }
	public void isUseSpecial(boolean useSpecial){
		this.useSpecial = useSpecial;
	}
	public void setErosion(int erosion){
		this.erosion = erosion;
	}
	public void setDilate(int dilate){
		this.dilate = dilate;
	}
	public void setRadius(int radius){
		this.radius = radius;
	}
	public void setThreshold(int threshold){
		this.threshold = threshold/100.0f;
	}
	public void loadImage() {									//载入扫描背景图片
		File file = new File(System.getProperty("user.dir")+"\\picture\\scan_back.jpg");
		if(file.exists()){										//如果图片存在的话
			BufferedImage image = null;
			try{
				image = ImageIO.read(file);
			}catch(Exception e){
				e.printStackTrace();
			}
			MediaTracker mt = new MediaTracker(this); 			//实例化媒体加载器
			mt.addImage(image, 0); 								//增加待加载图像到媒体加载器
			try {
				mt.waitForAll(); 								//等待所有图像的加载完成
			} catch (Exception ex) { 
				ex.printStackTrace(); 							//输出出错信息
			}
			
			int width = image.getWidth()-right-left;			//进行剪切边界处理和亮度对比度的处理
			int height = image.getHeight()/2-top-bottom;
			int[] pixels = new int[width*height];
			image.getRGB(left,top,width,height,pixels,0,width);
			BufferedImage aimImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			aimImage.setRGB(0,0,width,height,pixels,0,width);
			
			ContrastFilter filter = new ContrastFilter();
    		filter.setBrightness(bright);
    		filter.setContrast(contrast);
    		aimImage = filter.filter(aimImage, null);
			if(width>height){									//如果宽度大于高度，逆时针旋转90度
				aimImage = OverturnFilter.rotate90(aimImage);
			}
			
    		double scaleX = this.getWidth()*1.0/aimImage.getWidth();
			double scaleY = this.getHeight()*1.0/aimImage.getHeight();
			double scale = (scaleX>scaleY)?scaleY:scaleX;
			originalImage = scaleFilter(aimImage,scale,scale);	//放大或缩小,剪切后的原始图片,并赋值BufImage
    		
			bufImage = null;
			bufImage = originalImage;							//操作图像
			orig_x = (this.getWidth()-bufImage.getWidth())/2;
			orig_y = (this.getHeight()-bufImage.getHeight())/2;
			if(!timer.isRunning()){
				timer.start();									//进入定时器
			}
			do_analyse();										//进入脚型轮廓分析程序
		}
	}
	
	public void paintComponent(Graphics g){						//绘制处理后的扫描图片和脚型边界轮廓蚂蚁线
		if(first_paint){
			loadImage();
			first_paint = false;
		}
		Graphics2D g2 = (Graphics2D) g;
		Rectangle2D rect = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
		g2.setPaint(Color.WHITE);
		g2.fill(rect);											//填充白色的背景
		if(originalImage!=null){
			if(change){
				g2.drawImage(originalImage, orig_x, orig_y , originalImage.getWidth(), originalImage.getHeight() ,this);
			}else{
				if(bufImage!=null){
					g2.drawImage(bufImage, orig_x, orig_y , bufImage.getWidth(), bufImage.getHeight() ,this);
				}
			}
			if(showPath){
				g2.setColor(Color.red);
	        	g2.setStroke(bs[(index++)%6]);
	        	g2.draw(gPath);									//绘制红色的脚型边缘蚂蚁线
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		System.out.println("*********t i m e r*********");
		repaint();
	}
	public void stopTimer(){
		System.out.println("********s t o p**********");
		if(timer.isRunning()){
			timer.stop();
		}
	}
	//放大或者缩小图片
    private BufferedImage scaleFilter(BufferedImage scale_image,double scaleX,double scaleY) {
		BufferedImage filteredBufImage = new BufferedImage((int)(scale_image.getWidth()*scaleX),(int)(scale_image.getHeight()*scaleY),BufferedImage.TYPE_INT_RGB); 
		AffineTransform transform = new AffineTransform(); 							//仿射变换对象
		transform.setToScale(scaleX, scaleY); 										//设置仿射变换的比例因子
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);			//创建仿射变换操作对象	
		imageOp.filter(scale_image, filteredBufImage);								//过滤图像，目标图像在filteredBufImage
		return filteredBufImage;
	}
    //将图片进行滤波处理的到脚型轮廓图
    public void do_analyse(){
    	Thread runner = new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);									//线程休眠1000毫秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long start; 
				long end;
				long time;
				long total = 0;
				text_area.setText("");
				
				if(useSpecial){
					start = System.currentTimeMillis();
					SpecialFilter filter_02 = new SpecialFilter();		//执行特殊滤波
					bufImage = filter_02.filter(bufImage);
					end = System.currentTimeMillis();
					time = end-start;
					total = total+time;
					text_area.append("The time of special_filter took "+time+" ms\n");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				EroDilFilter filter_01 = new EroDilFilter();			//先腐蚀算法
		    	start = System.currentTimeMillis();
				bufImage = filter_01.mini_ero(bufImage,erosion);
				end = System.currentTimeMillis();
				time = end-start;
				total = total+time;
				text_area.append("The time of erosion_filter took "+time+" ms\n");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				start = System.currentTimeMillis();
				bufImage = filter_01.max_dil(bufImage,dilate);			//后膨胀算法
				end = System.currentTimeMillis();
				time = end-start;
				total = total+time;
				text_area.append("The time of dilate_filter took "+time+" ms\n");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				start = System.currentTimeMillis();
				StampFilter filter_03 = new StampFilter();				//执行Stamp滤波，得到二值图
				filter_03.setRadius(radius);
				filter_03.setThreshold(threshold);
				bufImage = filter_03.filter(bufImage, null);
				end = System.currentTimeMillis();
				time = end-start;
				total = total+time;
				text_area.append("The time of stamp_filter took "+time+" ms\n");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				start = System.currentTimeMillis();
				ConnectFilter filter_04 = new ConnectFilter();			//执行连通区域标记算法
				bufImage = filter_04.filter(bufImage);
				end = System.currentTimeMillis();
				time = end-start;
				total = total+time;
				text_area.append("The time of connect_filter took "+time+" ms\n");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(bufImage==null){
					text_area.append("Cant find the target pixels !!!");
					return ;
				}
				
				start = System.currentTimeMillis();
				SimpleEdgeFilter edge = new SimpleEdgeFilter();			//简单边缘算法
				bufImage = edge.process(bufImage);
				end = System.currentTimeMillis();
				time = end-start;
				total = total+time;
				text_area.append("The time of edge_filter took "+time+" ms\n");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				start = System.currentTimeMillis();						//获取边缘像素
				getPixArray();
				end = System.currentTimeMillis();
				time = end-start;
				total = total+time;
				text_area.append("The time of edge_array took "+time+" ms\n");
				text_area.append("The total of time is "+total+" ms");
				repaint();
			}
		};
		runner.start();
    }
    
    public void getPixArray(){
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
		ArrayList<PixelPoint> list = new ArrayList<PixelPoint>();
		ArrayList<PixelPoint> list_left = new ArrayList<PixelPoint>();
		ArrayList<PixelPoint> list_right = new ArrayList<PixelPoint>();
		for(int i=20;i<height-20;i++){							//忽略掉边缘20像素以内的像素
			boolean exist = false;
			int cur_i = i;
			int max_j = 0;
			int min_j = 0;
			for(int j=20;j<width-20;j++){						//判断水平方向上的最左和最右两点
				if((bufImage.getRGB(j,i) & 0xff)==0xff){		//白色的像素为底色，非边缘线，跳出，执行下一次循环。
					continue;
				}
				//到这一步为黑色边缘线像素。。。。
				if(exist==false){
					max_j = j;
					min_j = j;
					exist = true;
					continue;
				}
				if(exist==true){
					max_j=j;
				}
			}
			if(exist){
				if(min_j!=max_j){								//要求min_j和max_j不相等。避免出现大的折现。两边要严格的对称
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
		list.addAll(list_left);									//组合两个左右数链，也就是逆时针画脚型
		int half_size = list_right.size();
		for(int i=half_size-1;i>=0;i--){
			list.add(list_right.get(i));
		}														//得到所有边界像素的坐标数组
		System.out.println("      脚型边界像素点为："+list.size());	
		int[][] pix = new int[list.size()][2];					//将数据链放在一个二维数组中
		Iterator<PixelPoint> iterator = list.iterator();		//遍历list
		PixelPoint point;
		point = null;
		for(int i=0;iterator.hasNext();i++){
			point = iterator.next();
			pix[i][0] = point.getPixel_x();				//水平方向
			pix[i][1] = point.getPixel_y();				//垂直方向
			point = null;
		}												//至此得到了准确的边缘像素图像
		
		if(list.size()!=0){														//绘制蚂蚁线路径
    		int first_x = pix[0][0];
    		int first_y = pix[0][1];
    		gPath = new GeneralPath(); 											//GeneralPath对象实例
        	gPath.moveTo(orig_x+first_x,orig_y+first_y); 						//设置路径起点
        	int k=0;			
        	for(int i=1;i<list.size();i++){
        		k++;
        			if(k==10||i==half_size||i==half_size-1||i==list.size()-1){
        					gPath.lineTo(orig_x+pix[i][0], orig_y+pix[i][1]); 	//画线
                    		gPath.moveTo(orig_x+pix[i][0], orig_y+pix[i][1]);	//移动当前点
                		k=0;
        			}
        	}
        	gPath.lineTo(orig_x+first_x,orig_y+first_y);
        	gPath.moveTo(orig_x+first_x,orig_y+first_y);
        	showPath=true;
    	}
	}
//end...Foot_Alog.java
}
