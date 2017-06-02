package org.liuhe.algorithm.analyse;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

public class EroDilFilter extends Component{					//∏Ø ¥∫Õ≈Ú’Õ¬À≤®∆˜
	private static final long serialVersionUID = 1L;
	
	public BufferedImage mini_ero(BufferedImage bufImage,int time){
		Image image = (Image)bufImage;
		int iw = image.getWidth(this);
		int ih = image.getHeight(this);
		int[] pixels = new int[iw*ih];
		try {
			PixelGrabber pg = new PixelGrabber(image,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MinimumFilter filter = new MinimumFilter();
		for(int i=0;i<time;i++){
			pixels = filter.filterPixels(iw, ih, pixels, null);
		}
		ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
		Image tmp = createImage(ip);
		BufferedImage bufTemp = new BufferedImage(tmp.getWidth(this),tmp.getHeight(this),BufferedImage.TYPE_INT_RGB);
		Graphics g = bufTemp.getGraphics();
		g.drawImage(tmp, 0, 0, this);
		return bufTemp;
	}
	
	public BufferedImage max_dil(BufferedImage bufImage,int time){
		Image image = (Image)bufImage;
		int iw = image.getWidth(this);
		int ih = image.getHeight(this);
		int[] pixels=new int[iw*ih];
		try {
			PixelGrabber pg = new PixelGrabber(image,0,0,iw,ih,pixels,0,iw);
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MaximumFilter filter = new MaximumFilter();
		for(int i=0;i<time;i++){
			pixels = filter.filterPixels(iw, ih, pixels, null);
		}
		ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
		Image tmp = createImage(ip);
		BufferedImage bufTemp = new BufferedImage(tmp.getWidth(this),tmp.getHeight(this),BufferedImage.TYPE_INT_RGB);
		Graphics g = bufTemp.getGraphics();
		g.drawImage(tmp, 0, 0, this);
		return bufTemp;
	}
}
