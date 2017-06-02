package org.liuhe.algorithm.analyse;

import java.awt.image.BufferedImage;
public class SimpleEdgeFilter{									//边缘路径提取（首先提取二值化图形）
	public BufferedImage process(BufferedImage bufImage){
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
		BufferedImage temImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				if((x==0)||(x==width-1)||(y==0)||(y==height-1)){
					temImage.setRGB(x, y, 0xffffff);
					continue;
				}
				if((bufImage.getRGB(x, y)&0xff)==0x00){			//如果为黑色像素置为白色像素，作为背景
					temImage.setRGB(x, y, 0xffffff);
					continue;
				}
				//否则为白色像素，遍历四周是否有黑色像素，有的话置黑色边缘线，否则不做处理
				if((bufImage.getRGB(x  ,y-1) & 0xff)==0x00){
					temImage.setRGB(x, y, 0x000000);
				}else if((bufImage.getRGB(x-1,y) & 0xff)==0x00){
					temImage.setRGB(x, y, 0x000000);
				}else if((bufImage.getRGB(x+1,y) & 0xff)==0x00){
					temImage.setRGB(x, y, 0x000000);
				}else if((bufImage.getRGB(x  ,y+1) & 0xff)==0x00){
					temImage.setRGB(x, y, 0x000000);
				}else{
					temImage.setRGB(x, y, 0xffffff);
				}
			}
		}
		return temImage;
	}
//end...	
}
