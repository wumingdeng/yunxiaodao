package org.liuhe.algorithm.analyse;

import java.awt.image.BufferedImage;
public class SimpleEdgeFilter{									//��Ե·����ȡ��������ȡ��ֵ��ͼ�Σ�
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
				if((bufImage.getRGB(x, y)&0xff)==0x00){			//���Ϊ��ɫ������Ϊ��ɫ���أ���Ϊ����
					temImage.setRGB(x, y, 0xffffff);
					continue;
				}
				//����Ϊ��ɫ���أ����������Ƿ��к�ɫ���أ��еĻ��ú�ɫ��Ե�ߣ�����������
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
