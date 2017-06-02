package org.liuhe.algorithm.analyse;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

public class SpecialFilter extends Component{						//Ãÿ ‚¬À≤®–£—È
	private static final long serialVersionUID = 1L;
	public BufferedImage filter(BufferedImage bufImage){
		long start = System.currentTimeMillis();
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
		ColorModel cm = ColorModel.getRGBdefault();
		for(int i=1;i<ih;i++){
			for(int j=1;j<iw;j++){
				int alpha = cm.getAlpha(pixels[i*iw+j]);
				int red = cm.getRed(pixels[i*iw+j]);
				int green = cm.getGreen(pixels[i*iw+j]);
				int blue = cm.getBlue(pixels[i*iw+j]);
				
				if((i<50&&j<50)||(i<50&&j>iw-50)||					//π˝¬ÀÀƒ±ﬂœÒÀÿ
						(i>ih-50&&j<50)||(i>ih-50&&j>iw-50)){
					red = 10;
					green = 10;
					blue = 10;
				}else{
					if(red<30){										//π˝¬ÀµÕ∞µœÒÀÿ
						red = 10;
						green = 10;
						blue = 10;
					}else if((red<green)&&(red<blue)){				//π˝¬À“Ï≥£œÒÀÿ
						red = 10;
						green = 10;
						blue = 10;
					}else if(red>180||green>180||blue>180){			//π˝¬À∏ﬂ¡¡œÒÀÿ
						red = 10;
						green = 10;
						blue = 10;
					}else if(red+80<blue){							//π˝¬À∆´¿∂œÒÀÿ
						red = 10;
						green = 10;
						blue = 10;
					}else if(red+80<green){							//π˝¬À∆´¬ÃœÒÀÿ
						red = 10;
						green = 10;
						blue = 10;
					}
				}
				
				pixels[i*iw+j] = (alpha<<24)|(red<<16)|(green<<8)|(blue);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("calculate special filter spend time is:"+(end-start)+"ms");
		ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
		Image tmp = createImage(ip);
		BufferedImage bufTemp = new BufferedImage(tmp.getWidth(this),tmp.getHeight(this),BufferedImage.TYPE_INT_RGB);
		Graphics g = bufTemp.getGraphics();
		g.drawImage(tmp, 0, 0, this);
		bufImage = bufTemp;
		return bufImage;
	}
}
