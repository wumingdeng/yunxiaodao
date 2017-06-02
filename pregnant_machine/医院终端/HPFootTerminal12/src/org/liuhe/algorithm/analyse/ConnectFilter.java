package org.liuhe.algorithm.analyse;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ConnectFilter {							//连通区域标记算法（四领域）
	private int max_num;
	public BufferedImage filter(BufferedImage bufImage){
		long start = System.currentTimeMillis();
		int width = bufImage.getWidth();				//脚型图片宽
		int height = bufImage.getHeight();				//脚型图片高
		int[][] sign = new int[width][height];			//0为黑色像素，其他标记为1到N的整数
		ArrayList<Integer> list = new ArrayList<Integer>();
		int nextid = 1;									//标记号,初始标记为1
		if((bufImage.getRGB(0, 0)&0xff)!=0){			//第一行第一列的判定
			sign[0][0] = nextid;
			list.add(nextid);
			nextid++;
		}else{
			sign[0][0] = 0;
		}
		for(int i=1;i<width;i++){						//遍历第一行，从第二个像素开始
			int rgb = bufImage.getRGB(i, 0);
			if((rgb&0xff)!=0){							//如果为白色像素，不为黑。
				//判断左边相邻的像素是否有标记
				if(sign[i-1][0]!=0){
					sign[i][0] = sign[i-1][0];
				}else{
					sign[i][0] = nextid;
					list.add(nextid);
					nextid++;
				}
			}else{
				sign[i][0] = 0;
			}
		}
		for(int i=1;i<height;i++){						//遍历第一列，从第二个像素开始
			int rgb = bufImage.getRGB(0, i);
			if((rgb&0xff)!=0){
				//判断上边相邻的像素是否有标记
				if(sign[0][i-1]!=0){
					sign[0][i] = sign[0][i-1];
				}else{
					sign[0][i] = nextid;
					list.add(nextid);
					nextid++;
				}
			}else{
				sign[0][i] = 0;
			}
		}
		for(int i=1;i<width;i++){						//遍历方向  ：由左往右，
			for(int j=1;j<height;j++){					//遍历方向  ：由上至下
				int rgb = bufImage.getRGB(i, j);
				if((rgb&0xff)!=0){						//如果为白色的像素。
					if(sign[i-1][j]!=0&&sign[i][j-1]==0){						//左边有标记号，上边没有标记号
						sign[i][j] = sign[i-1][j];
					}else if(sign[i-1][j]==0&&sign[i][j-1]!=0){					//左边没有标记，上边有标记
						sign[i][j] = sign[i][j-1];
					}else if(sign[i-1][j]!=0&&sign[i][j-1]!=0){					//左边和上边都有标记
						if(sign[i-1][j]==sign[i][j-1]){							//左边和上边的标记号一致
							sign[i][j] = sign[i-1][j];
						}else if(sign[i-1][j]<sign[i][j-1]){					//左边小于上边的标记号//同化后，取小值
							sign[i][j] = sign[i-1][j];
							list.remove(new Integer(sign[i][j-1]));				//大标记号(上边的标记)要被同化，执行如下同化操作。
							for(int x=0;x<i;x++){								//0到i列
								for(int y=0;y<height;y++){
									if(sign[x][y]==sign[i][j-1]){
										sign[x][y]=sign[i-1][j];
									}
								}
							}
							for(int y=0;y<=j;y++){								//i列的情况
								if(sign[i][y]==sign[i][j-1]){
									sign[i][y]=sign[i-1][j];
								}
							}
						}else if(sign[i-1][j]>sign[i][j-1]){					//左边大于上边的标记号//同化后，取小值
							sign[i][j] = sign[i][j-1];
							list.remove(new Integer(sign[i-1][j]));				//移除这个编号，执行如下同化大标记号操作
							for(int x=0;x<i;x++){								//0到i列
								for(int y=0;y<height;y++){
									if(sign[x][y]==sign[i-1][j]){
										sign[x][y]=sign[i][j-1];
									}
								}
							}
							for(int y=0;y<=j;y++){								//i列的情况
								if(sign[i][y]==sign[i-1][j]){
									sign[i][y]=sign[i][j-1];
								}
							}
						}
					}else{													//左边和上边都没有标记
						sign[i][j]=nextid;
						list.add(nextid);
						nextid++;
					}
				}else{
					sign[i][j]=0;
				}
			}
		}
		System.out.println("*****ConnectFilter last nextid is:"+nextid);
		
		System.out.println("*****ConnectFilter list size :"+list.size());
		if(list.size()==0){
			return null;
		}
		int[][] id_num = new int[list.size()][2];	//第一个为编号，第二个为数量
		for(int i=0;i<list.size();i++){
			id_num[i][0] = list.get(i);
			id_num[i][1] = 0;						//初始化为0
		}
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(sign[i][j]==0){
					continue;
				}
				for(int k=0;k<list.size();k++){
					if(sign[i][j]==id_num[k][0]){
						id_num[k][1]++;
						break;
					}
				}
			}
		}
		
		int max_id = id_num[0][0];					//最多像素数的编号
		max_num = id_num[0][1];						//最多像素数的数目
		for(int i=1;i<list.size();i++){				//获得最多连通区域的像素编号和像素数目
			if(max_num<id_num[i][1]){
				max_num=id_num[i][1];
				max_id=id_num[i][0];
			}
		}
		System.out.println("********ConnectFilter max_id："+max_id+",max_num："+max_num);
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(sign[i][j]==0||sign[i][j]==max_id){
					continue;
				}
				bufImage.setRGB(i, j, 0x000000);	//修正为黑色背景（stamp下）
			}
		}
		long endd = System.currentTimeMillis();
		System.out.println("计算连通区域标记算法的时间为："+(endd-start)+"毫秒");
		return bufImage;
	}
	
	//返回最大的编号下面的像素数目
	public int getMax_num(){
		return max_num;
	}
	
//end...ConnectFilter.java
}
