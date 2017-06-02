package org.liuhe.foot.pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.liuhe.algorithm.scan.ContrastFilter;

public class Foot_Scan extends JPanel{
	private static final long serialVersionUID = 1L;
	private BufferedImage originalImage;
	private BufferedImage bufImage;
	private double scale;					//���ű�����
	int orig_x;								//X�������
	int orig_y;								//Y�������
	
	float bright = 1.0f;					//���Ȳ���     0.0~2.0
	float contrast = 1.0f;					//�ԱȶȲ���	0.0~2.0
	double left;							//���е��ĸ���Ե
	double right;
	double top;
	double bottom;
	double b02;
	double b03;
	
	public boolean first_paint = true;		//��һ���������������
	public boolean loading = false;			//�Ƿ�����ɨ�����뵱��
	
	private int pane_width;
	private int pane_height;
	
	public void scanImage(){
    	loading = true;
    	originalImage = null;
    	bufImage = null;
    	repaint();
    }
    public void loadFailure(){
    	loading = false;
    	repaint();
    }
    
	public void paintComponent(Graphics g){
		pane_width = this.getWidth();
		pane_height = this.getHeight();
		if(first_paint){										//���Ϊ��һ�λ��ƵĻ�
			if(loadImage()){
				setAppearence();
				setBorder();
				first_paint = false;
			}
		}
		Graphics2D g2 = (Graphics2D) g;
		Rectangle2D rect = new Rectangle2D.Double(0, 0, this.getWidth()-1, this.getHeight()-1);
		g2.setPaint(Color.WHITE);
		g2.fill(rect);											//����ɫ�ı���
		
		if(loading){											//����ɨ������
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(new Color(62,170,245));
			g2.setFont(new Font("����", Font.PLAIN+Font.BOLD, 28));
			FontMetrics fm = g2.getFontMetrics();
			String hint = "����ɨ������...";
			g2.drawString(hint, (pane_width-fm.stringWidth(hint))/2, pane_height/2);
		}
		
		if(bufImage!=null){
			g2.drawImage(bufImage, orig_x, orig_y , bufImage.getWidth(), bufImage.getHeight() ,this);
			g2.setPaint(new Color(15,147,203));
			Rectangle2D rect_cut = new Rectangle2D.Double(left+orig_x, top+orig_y,
					bufImage.getWidth()-right-left, bufImage.getHeight()-bottom-top);
			g2.draw(rect_cut);									//���Ƽ��б߿�
			/*Line2D.Double line_b02 = Line2D.Double(left+orig_x, (bufImage.getHeight()/2.0-b02)+orig_y,
					(bufImage.getWidth()-right)+orig_x, (bufImage.getHeight()/2.0-b02)+orig_y);*/
			GeneralPath line_b02 = new GeneralPath();
			line_b02.moveTo(left+orig_x, (bufImage.getHeight()/2.0-b02)+orig_y);
			line_b02.lineTo((bufImage.getWidth()-right)+orig_x, (bufImage.getHeight()/2.0-b02)+orig_y);
			g2.draw(line_b02);
			GeneralPath line_b03 = new GeneralPath();
			line_b03.moveTo(left+orig_x, (bufImage.getHeight()/2.0+b03)+orig_y);
			line_b03.lineTo((bufImage.getWidth()-right)+orig_x, (bufImage.getHeight()/2.0+b03)+orig_y);
			g2.draw(line_b03);
		}
	}
	
	//����ͼ��,ɨ�豳��ͼƬ
	private boolean loadImage() {
		File file = new File(System.getProperty("user.dir")+"\\picture\\scan_back.jpg");
		if(file.exists()){
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
			System.out.println("��ʽ��ʾ���ĳ���"+pane_width+","+pane_height);
			double scaleX = pane_width*1.0/image.getWidth();
			double scaleY = pane_height*1.0/image.getHeight();
			scale = (scaleX>scaleY)?scaleY:scaleX;
			originalImage = scaleFilter(image,scale,scale);		//�Ŵ����С,���к��ԭʼͼƬ,����ֵBufImage
			orig_x = (pane_width-originalImage.getWidth())/2;
			orig_y = (pane_height-originalImage.getHeight())/2;
			loading = false;
			return true;
		}
		return false;
	}
	
	//�Ŵ������СͼƬ
    private BufferedImage scaleFilter(BufferedImage scale_image,double scaleX,double scaleY) {
		BufferedImage filteredBufImage = new BufferedImage((int)(scale_image.getWidth()*scaleX),(int)(scale_image.getHeight()*scaleY),scale_image.getType()); 
		AffineTransform transform = new AffineTransform(); 							//����任����
		transform.setToScale(scaleX, scaleY); 										//���÷���任�ı�������
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);			//��������任��������	
		imageOp.filter(scale_image, filteredBufImage);								//����ͼ��Ŀ��ͼ����filteredBufImage
		return filteredBufImage;													//������Ӧ���ͼ������Ϊԭʼͼ��
	}
    
    //������������ʵʱ���ڶԱȶȺ�����
    public void setOrigAppear(int bright,int contrast){			//����ԭʼ�����ȺͶԱȶ���Ϣ
    	this.bright = (bright+100)*1.0f/100;
    	this.contrast = (contrast+100)*1.0f/100;
    }
    public void setBrightness(int bright){						//��������,������ͼ��
    	this.bright = (bright+100)*1.0f/100;
    	setAppearence();
    }
    public void setContrast(int contrast){						//���öԱȶ�,������ͼ��
    	this.contrast = (contrast+100)*1.0f/100;
    	setAppearence();
    }
    private void setAppearence(){								//����ͼƬ��ۣ��Աȶȡ�����
    	if(originalImage!=null){
    		bufImage = null;
    		bufImage = originalImage;
    		ContrastFilter filter = new ContrastFilter();
    		filter.setBrightness(bright);
    		filter.setContrast(contrast);
    		bufImage = filter.filter(bufImage, null);
        	repaint();
    	}
    }
    
    //�����������������������Ҽ��б�Ե
    public void setOrigBorder(int top,int b02,int b03,int bottom,int left,int right){
    	this.left = left;										//����ԭʼ�ļ��б߽�����
    	this.right = right;
    	this.top = top;
    	this.bottom = bottom;
    	this.b02 = b02;
    	this.b03 = b03;
    }
    private void setBorder(){									//��ԭʼ�ı߽�ӳ�䵽����Ӧ��Ļ�߽�
    	this.left = this.left*scale;
    	this.right = this.right*scale;
    	this.top = this.top*scale;
    	this.bottom = this.bottom*scale;
    	this.b02 = this.b02*scale;
    	this.b03 = this.b03*scale;
    }
    
    public void setRight(int right){
    	this.right = right*scale;
    	repaint();
    }
    public void setLeft(int left){
    	this.left = left*scale;
    	repaint();
    }
    public void setTop(int top){
    	this.top = top*scale;
    	repaint();
    }
    public void setBottom(int bottom){
    	this.bottom = bottom*scale;
    	repaint();
    }
    public void setB02(int b02){
    	this.b02 = b02*scale;
    	repaint();
    }
    public void setB03(int b03){
    	this.b03 = b03*scale;
    	repaint();
    }
//end...Foot_Scan.java
}