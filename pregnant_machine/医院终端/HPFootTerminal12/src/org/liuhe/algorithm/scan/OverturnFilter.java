package org.liuhe.algorithm.scan;

import java.awt.image.BufferedImage;

public class OverturnFilter {
	public static BufferedImage turn_horizontal(BufferedImage bufImage){		//水平翻转
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
		int tempRGB;
		for(int i=0;i<height;i++){
			for(int j=0;j<width/2;j++){
					tempRGB = bufImage.getRGB(j, i);
					bufImage.setRGB(j, i, bufImage.getRGB(width-1-j, i));
					bufImage.setRGB(width-1-j, i, tempRGB);
			}
		}
		return bufImage;
	}
	public static BufferedImage turn_vertical(BufferedImage bufImage){			//垂直翻转
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
		int tempRGB;
		for(int i=0;i<height/2;i++){
			for(int j=0;j<width;j++){
				tempRGB = bufImage.getRGB(j, i);
				bufImage.setRGB(j, i, bufImage.getRGB(j, height-1-i));
				bufImage.setRGB(j, height-1-i, tempRGB);
			}
		}
		return bufImage;
	}
	public static BufferedImage rotate90(BufferedImage bufImage){				//倒置翻转（逆时针旋转90度:还需上下翻转）
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
		BufferedImage rotateImage = new BufferedImage(height,width,bufImage.getType());
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				rotateImage.setRGB(i, j, bufImage.getRGB(j, i));
			}
		}
		return rotateImage;
	}
}
