package org.liuhe.foot.pane;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Foot_Adjust extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTextField text_h;
	private JTextField text_v;
	private BufferedImage originalImage;
	private BufferedImage bufImage;
	
	private double scale;					//缩放比例尺
	private int dpi;						//分辨率
	private int orig_x;						//X轴坐标点
	private int orig_y;						//Y轴坐标点
	private int pane_width;
	private int pane_height;
	
	public boolean first_paint = true;		//第一次载入校验图片时的标志位
	
	private boolean draging = false;		//是否当前坐标点正在拖动中
	private boolean draged = false;			//判断是否启用了MouseMotion中的dragged()方法
    private int dragnum = 0;				//标记当前拖动的点位
    private int drag_method = 1;			//1：默认dragged拖动；2：微调拖动
	
	private BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[]{5,5}, 0);
	private DecimalFormat df = new DecimalFormat("#.##");
	private Mouse_listener mouseListener = new Mouse_listener();
    private MouseMotion_listener motionListener = new MouseMotion_listener();
    private Key_listener keyListener = new Key_listener();
    
    private int screen_x;
    private int screen_y;
    private Robot robot;						//定点鼠标位置
	
	private Circle circle_h01;
	private Circle circle_h02;
	private Line line_h;
	private Circle circle_v01;
	private Circle circle_v02;
	private Line line_v;
	private Cross cross_cursor;
	
	public boolean loading = false;			//是否正在扫描载入当中
	
	public Foot_Adjust(JTextField text_h,JTextField text_v,String dpi){
		this.text_h = text_h;
		this.text_v = text_v;
		this.dpi = Integer.parseInt(dpi);
		circle_h01 = new Circle();
		circle_h01.setTipText("H1");
		circle_h02 = new Circle();
		circle_h02.setTipText("H2");
		line_h = new Line();
		circle_v01 = new Circle();
		circle_v01.setTipText("V1");
		circle_v02 = new Circle();
		circle_v02.setTipText("V2");
		line_v = new Line();
		cross_cursor = new Cross();
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(motionListener);
		this.addKeyListener(keyListener);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	private void setOrigCoord(){								//初始化坐标点和参考线
		circle_h01.x = pane_width*0.2f;
		circle_h01.y = pane_height*0.5f;
		circle_h02.x = pane_width*0.8f;
		circle_h02.y = pane_height*0.5f;
		line_h.x1 = circle_h01.x;
		line_h.y1 = circle_h01.y;
		line_h.x2 = circle_h02.x;
		line_h.y2 = circle_h02.y;
		circle_v01.x = pane_width*0.5f;
		circle_v01.y = pane_height*0.2f;
		circle_v02.x = pane_width*0.5f;
		circle_v02.y = pane_height*0.8f;
		line_v.x1 = circle_v01.x;
		line_v.y1 = circle_v01.y;
		line_v.x2 = circle_v02.x;
		line_v.y2 = circle_v02.y;
	}
	
	public void scanImage(){									//扫描图像，初始化
    	loading = true;
    	originalImage = null;
    	bufImage = null;
    	repaint();
    }
    public void loadFailure(){									//载入失败
    	loading = false;
    	repaint();
    }
	
	private boolean loadImage() {								//载入图像,扫描背景图片
		File file = new File(System.getProperty("user.dir")+"\\picture\\scan_adjust.jpg");
		if(file.exists()){
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
			double scaleX = pane_width*1.0/image.getWidth(); 
			double scaleY = pane_height*1.0/image.getHeight();
			scale = (scaleX>scaleY)?scaleY:scaleX;
			scaleFilter(image,scale,scale);						//放大或缩小,剪切后的原始图片,并赋值BufImage
			orig_x = (pane_width-bufImage.getWidth())/2;
			orig_y = (pane_height-bufImage.getHeight())/2;
			loading = false;
			return true;
		}
		return false;
	}
	
	public void paintComponent(Graphics g){
		pane_width = this.getWidth();
		pane_height = this.getHeight();
		if(first_paint){
			if(loadImage()){
				setOrigCoord();
				first_paint = false;
			}
		}
		Graphics2D g2 = (Graphics2D) g;
		Rectangle2D rect = new Rectangle2D.Double(0, 0, this.getWidth()-1, this.getHeight()-1);
		g2.setPaint(Color.WHITE);
		g2.fill(rect);											//填充白色的背景
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		if(loading){											//正在扫描载入
			g2.setPaint(new Color(62,170,245));
			g2.setFont(new Font("黑体", Font.PLAIN+Font.BOLD, 28));
			FontMetrics fm = g2.getFontMetrics();
			String hint = "正在扫描载入...";
			g2.drawString(hint, (pane_width-fm.stringWidth(hint))/2, pane_height/2);
		}
		
		if(bufImage!=null){
			g2.drawImage(bufImage, orig_x, orig_y , bufImage.getWidth(), bufImage.getHeight() ,this);
			Composite old = g2.getComposite();					//设置参考坐标和辅助线的透明度
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			circle_h01.draw(g2);
			circle_h02.draw(g2);
			line_h.draw(g2);
			circle_v01.draw(g2);
			circle_v02.draw(g2);
			line_v.draw(g2);
			if(draging){
				cross_cursor.draw(g2);
			}
			g2.setComposite(old);
		}
		g2.setStroke(new BasicStroke(1.0f));					//绘制淡蓝色的边框
		Rectangle2D rect_draw = new Rectangle2D.Double(0, 0, this.getWidth()-1, this.getHeight()-1);
		g2.setPaint(new Color(56,248,249));	
		g2.draw(rect_draw);
	}
	
    private void scaleFilter(BufferedImage scale_image,double scaleX,double scaleY) {
		if (scale_image == null)
			return; 														//如果bufImage为空则直接返回BufferedImage.TYPE_INT_ARGB
		BufferedImage filteredBufImage = new BufferedImage((int)(scale_image.getWidth()*scaleX),(int)(scale_image.getHeight()*scaleY),scale_image.getType()); 
		AffineTransform transform = new AffineTransform(); 					//仿射变换对象
		transform.setToScale(scaleX, scaleY); 								//设置仿射变换的比例因子
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);	//创建仿射变换操作对象	
		imageOp.filter(scale_image, filteredBufImage);						//过滤图像，目标图像在filteredBufImage
		originalImage = filteredBufImage;									//将自适应后的图像设置为原始图像
		bufImage = originalImage;
	}
    
    private String getMeasure_mm(double pixs){	//获得测量尺寸,单位为毫米
		return df.format(pixs/dpi*25.4/scale);
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
    private class Mouse_listener implements MouseListener{
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			int cur_x = e.getX();
	    	int cur_y = e.getY();
	    	//当无正在拖动坐标点状态时，判断是否触发了哪个点位，默认拖动方式
			if(!draging){
				drag_method = 1;
				if((int) Math.sqrt((cur_x-circle_h01.x)*(cur_x-circle_h01.x)+(cur_y-circle_h01.y)*(cur_y-circle_h01.y))<10){
					dragnum = 1;				//当拖动H1，编号：1
					draging = true;
			    	cross_cursor.x = circle_h01.x;
					cross_cursor.y = circle_h01.y;
					repaint();
			    	Foot_Adjust.this.requestFocus();
			    	screen_x = e.getXOnScreen();
			    	screen_y = e.getYOnScreen();
				}else if((int) Math.sqrt((cur_x-circle_h02.x)*(cur_x-circle_h02.x)+(cur_y-circle_h02.y)*(cur_y-circle_h02.y))<10){
		    		dragnum = 2;				//当拖动H2，编号：2
		    		draging = true;
		    		cross_cursor.x = circle_h02.x;
					cross_cursor.y = circle_h02.y;
					repaint();
		    		Foot_Adjust.this.requestFocus();
		    		screen_x = e.getXOnScreen();
			    	screen_y = e.getYOnScreen();
		    	}else if((int) Math.sqrt((cur_x-circle_v01.x)*(cur_x-circle_v01.x)+(cur_y-circle_v01.y)*(cur_y-circle_v01.y))<10){
		    		dragnum = 3;				//当拖动V1，编号：3
		    		draging = true;
		    		cross_cursor.x = circle_v01.x;
					cross_cursor.y = circle_v01.y;
					repaint();
		    		Foot_Adjust.this.requestFocus();
		    		screen_x = e.getXOnScreen();
			    	screen_y = e.getYOnScreen();
		    	}else if((int) Math.sqrt((cur_x-circle_v02.x)*(cur_x-circle_v02.x)+(cur_y-circle_v02.y)*(cur_y-circle_v02.y))<10){
		    		dragnum = 4;				//当拖动V2，编号：4
		    		draging = true;
		    		cross_cursor.x = circle_v02.x;
					cross_cursor.y = circle_v02.y;
					repaint();
		    		Foot_Adjust.this.requestFocus();
		    		screen_x = e.getXOnScreen();
			    	screen_y = e.getYOnScreen();
		    	}
			}
			//当处于正在拖动状态时，再次触发面板，切换为微调方式
			else{
				drag_method = 2;
				
				if(dragnum==1){
					if((int) Math.sqrt((cur_x-circle_h01.x)*(cur_x-circle_h01.x)+(cur_y-circle_h01.y)*(cur_y-circle_h01.y))<10){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_h01.x,circle_h01.y);
						circle_h01.x = circle_h01.x+xy[0];
						circle_h01.y = circle_h01.y+xy[1];
						line_h.x1 = circle_h01.x;
			    		line_h.y1 = circle_h01.y;
			    		cross_cursor.x = circle_h01.x;
						cross_cursor.y = circle_h01.y;
						text_h.setText(getMeasure_mm(line_h.getLength()));
					}
				}else if(dragnum==2){
					if((int) Math.sqrt((cur_x-circle_h02.x)*(cur_x-circle_h02.x)+(cur_y-circle_h02.y)*(cur_y-circle_h02.y))<10){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_h02.x,circle_h02.y);
						circle_h02.x = circle_h02.x+xy[0];
						circle_h02.y = circle_h02.y+xy[1];
						line_h.x2 = circle_h02.x;
			    		line_h.y2 = circle_h02.y;
						cross_cursor.x = circle_h02.x;
						cross_cursor.y = circle_h02.y;
						text_h.setText(getMeasure_mm(line_h.getLength()));
					}
				}else if(dragnum==3){
					if((int) Math.sqrt((cur_x-circle_v01.x)*(cur_x-circle_v01.x)+(cur_y-circle_v01.y)*(cur_y-circle_v01.y))<10){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_v01.x,circle_v01.y);
						circle_v01.x = circle_v01.x+xy[0];
						circle_v01.y = circle_v01.y+xy[1];
						line_v.x1 = circle_v01.x;
			    		line_v.y1 = circle_v01.y;
			    		cross_cursor.x = circle_v01.x;
						cross_cursor.y = circle_v01.y;
						text_v.setText(getMeasure_mm(line_v.getLength()));
					}
				}else if(dragnum==4){
					if((int) Math.sqrt((cur_x-circle_v02.x)*(cur_x-circle_v02.x)+(cur_y-circle_v02.y)*(cur_y-circle_v02.y))<10){
						draging = false;
					}else{
						int[] xy = justCoord(cur_x,cur_y,circle_v02.x,circle_v02.y);
						circle_v02.x = circle_v02.x+xy[0];
						circle_v02.y = circle_v02.y+xy[1];
						line_v.x2 = circle_v02.x;
			    		line_v.y2 = circle_v02.y;
			    		cross_cursor.x = circle_v02.x;
						cross_cursor.y = circle_v02.y;
						text_v.setText(getMeasure_mm(line_v.getLength()));
					}
				}
				repaint();
			}
		}
		public void mouseReleased(MouseEvent e) {
			if(draged&&drag_method==1){
				if(draging){
					cross_cursor.x = -20;
					cross_cursor.y = -20;
					draging = false;
					draged = false;
					repaint();
				}
			}
		}
    }
    
    private class MouseMotion_listener implements MouseMotionListener{
		public void mouseDragged(MouseEvent e) {
			if(!draging||drag_method!=1){		//如果没有滑动坐标点，则返回。
				return;
			}
			draged = true;						//标志为已经启用拖动方法
			
			if(0<=e.getX()&&e.getX()<=pane_width){
				cross_cursor.x = e.getX();
			}
			if(0<=e.getY()&&e.getY()<=pane_height){
				cross_cursor.y = e.getY();
			}
			if(dragnum==1){
				circle_h01.x=cross_cursor.x;
	    		circle_h01.y=cross_cursor.y;
	    		line_h.x1 = circle_h01.x;
	    		line_h.y1 = circle_h01.y;
	    		repaint();
			}else if(dragnum==2){
				circle_h02.x=cross_cursor.x;
	    		circle_h02.y=cross_cursor.y;
	    		line_h.x2 = circle_h02.x;
	    		line_h.y2 = circle_h02.y;
	    		repaint();
			}else if(dragnum==3){
				circle_v01.x=cross_cursor.x;
	    		circle_v01.y=cross_cursor.y;
	    		line_v.x1 = circle_v01.x;
	    		line_v.y1 = circle_v01.y;
	    		repaint();
			}else if(dragnum==4){
				circle_v02.x=cross_cursor.x;
	    		circle_v02.y=cross_cursor.y;
	    		line_v.x2 = circle_v02.x;
	    		line_v.y2 = circle_v02.y;
	    		repaint();
			}
			if(dragnum==1||dragnum==2){
	    		text_h.setText(getMeasure_mm(line_h.getLength()));
			}else if(dragnum==3||dragnum==4){
				text_v.setText(getMeasure_mm(line_v.getLength()));
			}
			//screen_x=e.getXOnScreen();
	    	//screen_y=e.getYOnScreen();
		}
		public void mouseMoved(MouseEvent e) {}
    }
    
    private class Key_listener implements KeyListener{
		public void keyPressed(KeyEvent e) {
			if(!draging){						//如果没有滑动坐标点，则返回。
				return;
			}
			int key=e.getKeyCode();
			if(key==KeyEvent.VK_UP){			//按键：向上
				cross_cursor.y=cross_cursor.y-1;
				screen_y=screen_y-1;
				if(cross_cursor.y<0){
					cross_cursor.y=0;
					screen_y=screen_y+1;
				}
				if(dragnum==1){
					circle_h01.y=circle_h01.y-1;
					if(circle_h01.y<0){
						circle_h01.y=0;
					}
					line_h.y1 = circle_h01.y;
				}else if(dragnum==2){
					circle_h02.y=circle_h02.y-1;
					if(circle_h02.y<0){
						circle_h02.y=0;
					}
					line_h.y2 = circle_h02.y;
				}else if(dragnum==3){
					circle_v01.y=circle_v01.y-1;
					if(circle_v01.y<0){
						circle_v01.y=0;
					}
					line_v.y1 = circle_v01.y;
				}else if(dragnum==4){
					circle_v02.y=circle_v02.y-1;
					if(circle_v02.y<0){
						circle_v02.y=0;
					}
					line_v.y2 = circle_v02.y;
				}
				robot.mouseMove(screen_x, screen_y);
				repaint();
			}else if(key==KeyEvent.VK_DOWN){	//按键：向下
				cross_cursor.y=cross_cursor.y+1;
				screen_y=screen_y+1;
				if(cross_cursor.y>pane_height-1){
					cross_cursor.y=pane_height-1;
					screen_y=screen_y-1;
				}
				if(dragnum==1){
					circle_h01.y=circle_h01.y+1;
					if(circle_h01.y>pane_height-1){
						circle_h01.y=pane_height-1;
					}
					line_h.y1 = circle_h01.y;
				}else if(dragnum==2){
					circle_h02.y=circle_h02.y+1;
					if(circle_h02.y>pane_height-1){
						circle_h02.y=pane_height-1;
					}
					line_h.y2 = circle_h02.y;
				}else if(dragnum==3){
					circle_v01.y=circle_v01.y+1;
					if(circle_v01.y>pane_height-1){
						circle_v01.y=pane_height-1;
					}
					line_v.y1 = circle_v01.y;
				}else if(dragnum==4){
					circle_v02.y=circle_v02.y+1;
					if(circle_v02.y>pane_height-1){
						circle_v02.y=pane_height-1;
					}
					line_v.y2 = circle_v02.y;
				}
				robot.mouseMove(screen_x, screen_y);
				repaint();
			}else if(key==KeyEvent.VK_LEFT){	//按键：向左
				cross_cursor.x=cross_cursor.x-1;
				screen_x=screen_x-1;
				if(cross_cursor.x<0){
					cross_cursor.x=0;
					screen_x=screen_x+1;
				}
				if(dragnum==1){
					circle_h01.x=circle_h01.x-1;
					if(circle_h01.x<0){
						circle_h01.x=0;
					}
					line_h.x1 = circle_h01.x;
				}else if(dragnum==2){
					circle_h02.x=circle_h02.x-1;
					if(circle_h02.x<0){
						circle_h02.x=0;
					}
					line_h.x2 = circle_h02.x;
				}else if(dragnum==3){
					circle_v01.x=circle_v01.x-1;
					if(circle_v01.x<0){
						circle_v01.x=0;
					}
					line_v.x1 = circle_v01.x;
				}else if(dragnum==4){
					circle_v02.x=circle_v02.x-1;
					if(circle_v02.x<0){
						circle_v02.x=0;
					}
					line_v.x2 = circle_v02.x;
				}
				robot.mouseMove(screen_x, screen_y);
				repaint();
			}else if(key==KeyEvent.VK_RIGHT){	//按键：向右
				cross_cursor.x=cross_cursor.x+1;
				screen_x=screen_x+1;
				if(cross_cursor.x>pane_width-1){
					cross_cursor.x=pane_width-1;
					screen_x=screen_x-1;
				}
				if(dragnum==1){
					circle_h01.x=circle_h01.x+1;
					if(circle_h01.x>pane_width-1){
						circle_h01.x=pane_width-1;
					}
					line_h.x1 = circle_h01.x;
				}else if(dragnum==2){
					circle_h02.x=circle_h02.x+1;
					if(circle_h02.x>pane_width-1){
						circle_h02.x=pane_width-1;
					}
					line_h.x2 = circle_h02.x;
				}else if(dragnum==3){
					circle_v01.x=circle_v01.x+1;
					if(circle_v01.x>pane_width-1){
						circle_v01.x=pane_width-1;
					}
					line_v.x1 = circle_v01.x;
				}else if(dragnum==4){
					circle_v02.x=circle_v02.x+1;
					if(circle_v02.x>pane_width-1){
						circle_v02.x=pane_width-1;
					}
					line_v.x2 = circle_v02.x;
				}
				robot.mouseMove(screen_x, screen_y);
				repaint();
			}else if(key==KeyEvent.VK_ENTER){
				draged = false;
				cross_cursor.x = -20;
				cross_cursor.y = -20;
				repaint();
				Foot_Adjust.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			if(dragnum==1||dragnum==2){
	    		text_h.setText(getMeasure_mm(line_h.getLength()));
			}else if(dragnum==3||dragnum==4){
				text_v.setText(getMeasure_mm(line_v.getLength()));
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
    }
    
    private class Circle{						//参考坐标点
		float x,y;
		float radius = 5.0f;					//默认半径
		Color color = Color.RED;				//默认颜色
		String tiptext;							//文本说明
	    Font gen_font = new Font("微软雅黑", Font.PLAIN, 12);
		public void draw(Graphics2D g2d){
			g2d.setColor(color);
      	  	g2d.setStroke(new BasicStroke(2.0f));
      	  	Ellipse2D.Float circle = new Ellipse2D.Float(x-radius,y-radius,radius*2,radius*2);
      	  	g2d.draw(circle);
      	  	g2d.setStroke(new BasicStroke(1.0f));
      	  	g2d.setFont(gen_font);
      	  	g2d.drawString(tiptext, x+5, y-5);
		}
		public void setTipText(String tiptext){
			this.tiptext=tiptext;
		}
	}
    private class Line{							//辅助线
		float x1,y1;
		float x2,y2;
		Color color = Color.RED;
		public void draw(Graphics2D g2d){
			g2d.setColor(color);
			g2d.setStroke(bs);
			Line2D.Float line = new Line2D.Float(x1,y1,x2,y2);
			g2d.draw(line);
		}
		public float getLength(){
			return (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		}
	}
    private class Cross{
    	private float x;
		private float y;
    	public void draw(Graphics2D g2d){
    		
    		g2d.setColor(Color.red);
			g2d.setStroke(new BasicStroke(1.0f));
      	  	g2d.draw(new Ellipse2D.Float(x-8,y-8,16,16));
			g2d.setColor(Color.red);
    		g2d.setStroke(new BasicStroke(1.0f));
    		Line2D.Float line_x = new Line2D.Float(x-10,y,x+10,y);
    		g2d.draw(line_x);
    		Line2D.Float line_y = new Line2D.Float(x,y-10,x,y+10);
    		g2d.draw(line_y);
    	}
    }
//end...Foot_Adjust.java
}