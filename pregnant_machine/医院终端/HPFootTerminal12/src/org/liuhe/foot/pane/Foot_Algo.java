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
	int orig_x;								//X�������
	int orig_y;								//Y�������
	
	float bright = 1.0f;					//���Ȳ���     0.0~2.0
	float contrast = 1.0f;					//�ԱȶȲ���	0.0~2.0
	int left;								//���е��ĸ���Ե
	int right;
	int top;
	int bottom;
	
	float radius = 10.0f;
	float threshold = 0.16f;
	int erosion;
	int dilate;
	boolean useSpecial;
	
	public boolean first_paint = true;
	
	private JTextArea text_area;			//�ı���������ʾ������Ϣ
	private GeneralPath gPath = null;
	public boolean showPath = false;
	final BasicStroke[] bs = new BasicStroke[6];
    int index = 0;							//���������ʾ������
    Timer timer ;
    private boolean change = true;			//��ʽת����־
    
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
	public void setTop(int top){			//���÷���ʱ����������ҡ����ȶԱȶȡ����͸�ʴ��stamp�뾶����ֵ��������
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
	public void loadImage() {									//����ɨ�豳��ͼƬ
		File file = new File(System.getProperty("user.dir")+"\\picture\\scan_back.jpg");
		if(file.exists()){										//���ͼƬ���ڵĻ�
			BufferedImage image = null;
			try{
				image = ImageIO.read(file);
			}catch(Exception e){
				e.printStackTrace();
			}
			MediaTracker mt = new MediaTracker(this); 			//ʵ����ý�������
			mt.addImage(image, 0); 								//���Ӵ�����ͼ��ý�������
			try {
				mt.waitForAll(); 								//�ȴ�����ͼ��ļ������
			} catch (Exception ex) { 
				ex.printStackTrace(); 							//���������Ϣ
			}
			
			int width = image.getWidth()-right-left;			//���м��б߽紦������ȶԱȶȵĴ���
			int height = image.getHeight()/2-top-bottom;
			int[] pixels = new int[width*height];
			image.getRGB(left,top,width,height,pixels,0,width);
			BufferedImage aimImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			aimImage.setRGB(0,0,width,height,pixels,0,width);
			
			ContrastFilter filter = new ContrastFilter();
    		filter.setBrightness(bright);
    		filter.setContrast(contrast);
    		aimImage = filter.filter(aimImage, null);
			if(width>height){									//�����ȴ��ڸ߶ȣ���ʱ����ת90��
				aimImage = OverturnFilter.rotate90(aimImage);
			}
			
    		double scaleX = this.getWidth()*1.0/aimImage.getWidth();
			double scaleY = this.getHeight()*1.0/aimImage.getHeight();
			double scale = (scaleX>scaleY)?scaleY:scaleX;
			originalImage = scaleFilter(aimImage,scale,scale);	//�Ŵ����С,���к��ԭʼͼƬ,����ֵBufImage
    		
			bufImage = null;
			bufImage = originalImage;							//����ͼ��
			orig_x = (this.getWidth()-bufImage.getWidth())/2;
			orig_y = (this.getHeight()-bufImage.getHeight())/2;
			if(!timer.isRunning()){
				timer.start();									//���붨ʱ��
			}
			do_analyse();										//�������������������
		}
	}
	
	public void paintComponent(Graphics g){						//���ƴ�����ɨ��ͼƬ�ͽ��ͱ߽�����������
		if(first_paint){
			loadImage();
			first_paint = false;
		}
		Graphics2D g2 = (Graphics2D) g;
		Rectangle2D rect = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
		g2.setPaint(Color.WHITE);
		g2.fill(rect);											//����ɫ�ı���
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
	        	g2.draw(gPath);									//���ƺ�ɫ�Ľ��ͱ�Ե������
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
	//�Ŵ������СͼƬ
    private BufferedImage scaleFilter(BufferedImage scale_image,double scaleX,double scaleY) {
		BufferedImage filteredBufImage = new BufferedImage((int)(scale_image.getWidth()*scaleX),(int)(scale_image.getHeight()*scaleY),BufferedImage.TYPE_INT_RGB); 
		AffineTransform transform = new AffineTransform(); 							//����任����
		transform.setToScale(scaleX, scaleY); 										//���÷���任�ı�������
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);			//��������任��������	
		imageOp.filter(scale_image, filteredBufImage);								//����ͼ��Ŀ��ͼ����filteredBufImage
		return filteredBufImage;
	}
    //��ͼƬ�����˲�����ĵ���������ͼ
    public void do_analyse(){
    	Thread runner = new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);									//�߳�����1000����
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
					SpecialFilter filter_02 = new SpecialFilter();		//ִ�������˲�
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
				
				EroDilFilter filter_01 = new EroDilFilter();			//�ȸ�ʴ�㷨
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
				bufImage = filter_01.max_dil(bufImage,dilate);			//�������㷨
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
				StampFilter filter_03 = new StampFilter();				//ִ��Stamp�˲����õ���ֵͼ
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
				ConnectFilter filter_04 = new ConnectFilter();			//ִ����ͨ�������㷨
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
				SimpleEdgeFilter edge = new SimpleEdgeFilter();			//�򵥱�Ե�㷨
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
				
				start = System.currentTimeMillis();						//��ȡ��Ե����
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
		for(int i=20;i<height-20;i++){							//���Ե���Ե20�������ڵ�����
			boolean exist = false;
			int cur_i = i;
			int max_j = 0;
			int min_j = 0;
			for(int j=20;j<width-20;j++){						//�ж�ˮƽ�����ϵ��������������
				if((bufImage.getRGB(j,i) & 0xff)==0xff){		//��ɫ������Ϊ��ɫ���Ǳ�Ե�ߣ�������ִ����һ��ѭ����
					continue;
				}
				//����һ��Ϊ��ɫ��Ե�����ء�������
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
				if(min_j!=max_j){								//Ҫ��min_j��max_j����ȡ�������ִ�����֡�����Ҫ�ϸ�ĶԳ�
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
		list.addAll(list_left);									//�����������������Ҳ������ʱ�뻭����
		int half_size = list_right.size();
		for(int i=half_size-1;i>=0;i--){
			list.add(list_right.get(i));
		}														//�õ����б߽����ص���������
		System.out.println("      ���ͱ߽����ص�Ϊ��"+list.size());	
		int[][] pix = new int[list.size()][2];					//������������һ����ά������
		Iterator<PixelPoint> iterator = list.iterator();		//����list
		PixelPoint point;
		point = null;
		for(int i=0;iterator.hasNext();i++){
			point = iterator.next();
			pix[i][0] = point.getPixel_x();				//ˮƽ����
			pix[i][1] = point.getPixel_y();				//��ֱ����
			point = null;
		}												//���˵õ���׼ȷ�ı�Ե����ͼ��
		
		if(list.size()!=0){														//����������·��
    		int first_x = pix[0][0];
    		int first_y = pix[0][1];
    		gPath = new GeneralPath(); 											//GeneralPath����ʵ��
        	gPath.moveTo(orig_x+first_x,orig_y+first_y); 						//����·�����
        	int k=0;			
        	for(int i=1;i<list.size();i++){
        		k++;
        			if(k==10||i==half_size||i==half_size-1||i==list.size()-1){
        					gPath.lineTo(orig_x+pix[i][0], orig_y+pix[i][1]); 	//����
                    		gPath.moveTo(orig_x+pix[i][0], orig_y+pix[i][1]);	//�ƶ���ǰ��
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
