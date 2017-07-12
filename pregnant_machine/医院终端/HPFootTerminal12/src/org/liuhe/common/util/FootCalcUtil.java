package org.liuhe.common.util;

import java.text.DecimalFormat;

import org.liuhe.algorithm.config.FootConfig;

public class FootCalcUtil {
	public String[] advice;
	public static String[] status;
	private float[][] breadth_half;
	private DecimalFormat df = new DecimalFormat("#.##");
	
	public FootCalcUtil(){
		FootConfig config = new FootConfig();
		status = config.getStatus();
		advice = config.getAdvice();
		breadth_half = config.getBreadth();
		
		/**advice = new String[]{"����ֽŸ��ⷭ������ʱ���ڲ�������С�����ź������������б���ߡ��ܡ���ʱ�䳤��ʹ��" +
				"��ȥרҵҽԺ��ѯ����顣ע��Ӫ�����⣬�������س��꣬ѹ���㹭���������·�����¡��ܡ������赸�Ȼ�������Ų����⡣" +
				"�������Ÿ�̧�𡢽�ֺץ����˶�������ǰ���Ƽ��⡣",
				"�κκ��Ӷ�������ɽŲ����Ρ���Ҫ��ʱ�为�ػ�վ���������㹭��" +
				"ע��Ӫ�����⣬�������س��꣬ѹ���㹭����μ��������赸�Ȼ�������Ų����⡣",
				"�羭�����ֽ�ǰ��ʹ����ǰ�Ƽ�����ţ���ȥרҵҽԺ��ѯ����顣��Ҫ��ʱ�为�ػ�վ�����������ֺ��λѹ����" +
				"��ѡ���ͷЬ�����������Ь�Ĵ�С�����ɳ���Ь���ŵ������������Ħǰ���ƣ�ʹ���Ƽ����ɳڡ�����" +
				"��о�������ʹ����ʱ������ǰ�ƴ�������档"};
		status = new String[]{"ƽ��","���㹭","�����㹭","���㹭����","���㹭"};
		breadth_half = new float[][]{
				{115,46.8f,48.2f,49.5f,50.8f,52.1f,53.4f,54.7f},
				{120,48.6f,50.0f,51.3f,52.6f,53.9f,55.2f,56.5f},
				{125,50.4f,51.7f,53.0f,54.3f,55.6f,56.9f,58.2f},
				{130,52.2f,53.5f,54.8f,56.1f,57.4f,58.7f,60.0f},
				{135,54.0f,55.3f,56.6f,57.9f,59.2f,60.5f,61.8f},
				{140,55.8f,57.1f,58.4f,59.7f,61.0f,62.3f,63.6f},
				{145,57.6f,58.9f,60.2f,61.5f,62.8f,64.1f,65.4f},
				{150,59.4f,60.7f,62.0f,63.3f,64.6f,65.9f,67.2f},
				{155,61.2f,62.5f,63.8f,65.1f,66.4f,67.7f,69.0f},
				{160,63.0f,64.3f,65.6f,66.9f,68.2f,69.5f,70.8f},
				{165,64.8f,66.1f,67.4f,68.7f,70.0f,71.3f,72.6f},
				{170,66.6f,67.9f,69.2f,70.5f,71.8f,73.1f,74.4f},
				{175,68.4f,69.7f,71.0f,72.3f,73.6f,74.9f,76.2f},
				{180,70.2f,71.5f,72.8f,74.1f,75.4f,76.7f,78.0f},
				{185,72.0f,73.3f,74.6f,75.9f,77.2f,78.5f,79.8f},
				{190,73.8f,75.1f,76.4f,77.7f,79.0f,80.3f,81.6f},
				{195,75.6f,76.9f,78.2f,79.5f,80.8f,82.1f,83.4f},
				{200,77.4f,78.7f,80.0f,81.3f,82.6f,83.9f,85.2f},
				{205,79.2f,80.5f,81.8f,83.1f,84.4f,85.7f,87.0f},
				{210,81.0f,82.3f,83.6f,84.9f,86.2f,87.5f,88.8f},
				{215,81.3f,82.7f,84.0f,85.3f,86.6f,87.9f,89.2f},
				{220,84.6f,85.9f,87.2f,88.5f,89.8f,91.1f,92.4f},
				{225,86.4f,87.7f,89.0f,90.3f,91.6f,92.9f,94.2f},
				{230,88.1f,89.4f,90.7f,92.0f,93.3f,94.6f,95.9f},
				{235,89.5f,90.8f,92.1f,93.4f,94.7f,96.0f,97.3f},
				{240,90.9f,92.2f,93.5f,94.8f,96.1f,97.4f,98.7f},
				{245,92.3f,93.6f,94.9f,96.2f,97.5f,98.8f,100.1f},
				{250,93.7f,95.0f,96.3f,97.6f,98.9f,100.2f,101.5f},
				{255,95.1f,96.4f,97.7f,99.0f,100.3f,101.6f,102.9f},
				{260,96.5f,97.8f,99.1f,100.4f,101.7f,103.0f,104.3f},
				{265,97.9f,99.2f,100.5f,101.8f,103.1f,104.4f,105.7f},
				{270,99.3f,100.6f,101.9f,103.2f,104.5f,105.8f,107.1f},
				{275,100.7f,102.0f,103.3f,104.6f,105.9f,107.2f,108.5f},
				{280,102.1f,103.4f,104.7f,106.0f,107.3f,108.6f,109.9f},
				{285,103.5f,104.8f,106.1f,107.4f,108.7f,110.0f,111.3f},
				{290,104.9f,106.2f,107.5f,108.8f,110.1f,111.4f,112.7f},
		};*/
	}
	/**
	 * ����ʵ�ʺ��׳ߴ�
	 * @param pixs
	 * @param dpi
	 * @param scale
	 * @return �ַ���
	 * */
	public String getFootLength_mm(double pixs,int dpi,double scale){
		return df.format(pixs/dpi*25.4/scale);
	}
	/**
	 * ����ʵ������
	 * @param size �ų�
	 * @return �ַ���
	 * */
	public String getFootLength_5(float size){
		int size_5 = (int) (size/5.0);
		if((size%5.0)>=2.5){
			size_5++;
		}
		return (size_5*5)+"";
	}
	
//	public 
	
	
	/**
	 * ���ݽŵĶ�λ���Ͷ�λ�ڿ�ı�ֵ���ж�:�㹭̬��
	 * @param width_in
	 * @param width_out
	 * @return �ַ���
	 * */
	public String getFootStatus(double width_in,double width_out){
		double bili = width_in/width_out;
		if(width_in > width_out){
			return "���µ�����";
		}else if(bili > 0.875){
			return status[0];
		}else if(bili > 0.625){
			return status[1];
		}else if(bili > 0.375){
			return status[2];
		}else if(bili > 0.125){
			return status[3];
		}else{
			return status[4];
		}
	}
	/**
	 * �ж��㹭̬��
	 * @param state
	 * @return �㹭̬�ƴ���(1--5)
	 * */
	public int getFootStatusCode(String state){
		int statusCode = 0;
		for(int i=0;i<status.length;i++){
			if(state.equals(status[i])){
				statusCode = i+1;
				break;
			}
		}
		return statusCode;
	}
	/**
	 * ���ݽŵ��㹭̬�����ж�ר�����
	 * @param state �㹭̬��
	 * @return ר�����
	 * */
	public String getFootAdvice(String state){
		if(state.equals(status[0])||state.endsWith(status[1])){
			return advice[0];
		}else if(state.equals(status[2])){
			return advice[1];
		}else if(state.equals(status[3])||state.equals(status[4])){
			return advice[2];
		}else{
			return "���µ�����";
		}
	}
	/**
	 * ���ݽŵĳ��ȺͿ���жϽŰ��Ϳ�
	 * @param length �ų�
	 * @param width �ſ�
	 * @return ���Ϳ�
	 * */
	public String getWidthType(int length,double width){
		for(int i = 0;i < breadth_half.length;i++){
			if(breadth_half[i][0] == length){
				float type = 1.0f;
				for(int j = 1;j < breadth_half[i].length;j++){
					if(width <= breadth_half[i][j]){
						break;
					}
					type = type+0.5f;
				}
				return type+"�ͻ���";
			}
		}
		return "";
	}
	/**
	 * ���ݰ��Ϳ��ж������Ϳ�
	 * @param ���Ϳ�
	 * @return �����Ϳ�
	 * */
	public String getwidthname(String width){
		if(width != null){
			if(width.startsWith("1")){
				return "һ��";
			}else if(width.startsWith("2")){
				return "����";
			}else if(width.startsWith("3")){
				return "����";
			}else if(width.startsWith("4")){
				return "����";
			}
		}
		return "";
	}
}