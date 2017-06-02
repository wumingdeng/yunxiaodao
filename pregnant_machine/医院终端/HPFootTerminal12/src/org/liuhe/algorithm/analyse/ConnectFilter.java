package org.liuhe.algorithm.analyse;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ConnectFilter {							//��ͨ�������㷨��������
	private int max_num;
	public BufferedImage filter(BufferedImage bufImage){
		long start = System.currentTimeMillis();
		int width = bufImage.getWidth();				//����ͼƬ��
		int height = bufImage.getHeight();				//����ͼƬ��
		int[][] sign = new int[width][height];			//0Ϊ��ɫ���أ��������Ϊ1��N������
		ArrayList<Integer> list = new ArrayList<Integer>();
		int nextid = 1;									//��Ǻ�,��ʼ���Ϊ1
		if((bufImage.getRGB(0, 0)&0xff)!=0){			//��һ�е�һ�е��ж�
			sign[0][0] = nextid;
			list.add(nextid);
			nextid++;
		}else{
			sign[0][0] = 0;
		}
		for(int i=1;i<width;i++){						//������һ�У��ӵڶ������ؿ�ʼ
			int rgb = bufImage.getRGB(i, 0);
			if((rgb&0xff)!=0){							//���Ϊ��ɫ���أ���Ϊ�ڡ�
				//�ж�������ڵ������Ƿ��б��
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
		for(int i=1;i<height;i++){						//������һ�У��ӵڶ������ؿ�ʼ
			int rgb = bufImage.getRGB(0, i);
			if((rgb&0xff)!=0){
				//�ж��ϱ����ڵ������Ƿ��б��
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
		for(int i=1;i<width;i++){						//��������  ���������ң�
			for(int j=1;j<height;j++){					//��������  ����������
				int rgb = bufImage.getRGB(i, j);
				if((rgb&0xff)!=0){						//���Ϊ��ɫ�����ء�
					if(sign[i-1][j]!=0&&sign[i][j-1]==0){						//����б�Ǻţ��ϱ�û�б�Ǻ�
						sign[i][j] = sign[i-1][j];
					}else if(sign[i-1][j]==0&&sign[i][j-1]!=0){					//���û�б�ǣ��ϱ��б��
						sign[i][j] = sign[i][j-1];
					}else if(sign[i-1][j]!=0&&sign[i][j-1]!=0){					//��ߺ��ϱ߶��б��
						if(sign[i-1][j]==sign[i][j-1]){							//��ߺ��ϱߵı�Ǻ�һ��
							sign[i][j] = sign[i-1][j];
						}else if(sign[i-1][j]<sign[i][j-1]){					//���С���ϱߵı�Ǻ�//ͬ����ȡСֵ
							sign[i][j] = sign[i-1][j];
							list.remove(new Integer(sign[i][j-1]));				//���Ǻ�(�ϱߵı��)Ҫ��ͬ����ִ������ͬ��������
							for(int x=0;x<i;x++){								//0��i��
								for(int y=0;y<height;y++){
									if(sign[x][y]==sign[i][j-1]){
										sign[x][y]=sign[i-1][j];
									}
								}
							}
							for(int y=0;y<=j;y++){								//i�е����
								if(sign[i][y]==sign[i][j-1]){
									sign[i][y]=sign[i-1][j];
								}
							}
						}else if(sign[i-1][j]>sign[i][j-1]){					//��ߴ����ϱߵı�Ǻ�//ͬ����ȡСֵ
							sign[i][j] = sign[i][j-1];
							list.remove(new Integer(sign[i-1][j]));				//�Ƴ������ţ�ִ������ͬ�����ǺŲ���
							for(int x=0;x<i;x++){								//0��i��
								for(int y=0;y<height;y++){
									if(sign[x][y]==sign[i-1][j]){
										sign[x][y]=sign[i][j-1];
									}
								}
							}
							for(int y=0;y<=j;y++){								//i�е����
								if(sign[i][y]==sign[i-1][j]){
									sign[i][y]=sign[i][j-1];
								}
							}
						}
					}else{													//��ߺ��ϱ߶�û�б��
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
		int[][] id_num = new int[list.size()][2];	//��һ��Ϊ��ţ��ڶ���Ϊ����
		for(int i=0;i<list.size();i++){
			id_num[i][0] = list.get(i);
			id_num[i][1] = 0;						//��ʼ��Ϊ0
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
		
		int max_id = id_num[0][0];					//����������ı��
		max_num = id_num[0][1];						//�������������Ŀ
		for(int i=1;i<list.size();i++){				//��������ͨ��������ر�ź�������Ŀ
			if(max_num<id_num[i][1]){
				max_num=id_num[i][1];
				max_id=id_num[i][0];
			}
		}
		System.out.println("********ConnectFilter max_id��"+max_id+",max_num��"+max_num);
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(sign[i][j]==0||sign[i][j]==max_id){
					continue;
				}
				bufImage.setRGB(i, j, 0x000000);	//����Ϊ��ɫ������stamp�£�
			}
		}
		long endd = System.currentTimeMillis();
		System.out.println("������ͨ�������㷨��ʱ��Ϊ��"+(endd-start)+"����");
		return bufImage;
	}
	
	//�������ı�������������Ŀ
	public int getMax_num(){
		return max_num;
	}
	
//end...ConnectFilter.java
}
