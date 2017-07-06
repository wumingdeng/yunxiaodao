package org.liuhe.common.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import org.liuhe.algorithm.config.ServerConfig;
import org.liuhe.main.MainJFrame;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.util.ArrayList;
import java.util.Calendar; 

import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;

class WeightRate{
	public double rateMin;
	public double rateMax;
	public double drateMin;
	public double drateMax;
	public WeightRate(double rmin,double rmax,double drmin,double drmax){
		rateMin = rmin;
		rateMax = rmax;
		drateMin = drmin;
		drateMax = drmax;
	}
};

public class PrintUtil implements Printable{
	private String clinic_info = null;
	private String wait_num = null;
	private String date_yunfu = null;
	private String no = null;
	private String left_foot_status = null;
	private String left_foot_advice = null;
	private String right_foot_status = null;
	private String right_foot_advice = null;
	private String[] para = null;
	private String qrcode = null;
	private float height;
	private boolean isSingle = true;
	public static int[] Weeks_index = {1,12,13,16,17,20,21,24,25,28,29,30,31,32,33,34,35,36,37,37,38,38,39,39,40,40} ;
	public static String[] Common_symptoms = {
			"�����ӹ�������������ǻ������λ�÷��͸ı䣬���°��׳��ܵ�ѹ�����ӣ�����������Ƶ��Ҳ�ᵼ���Ȳ��������ۡ�������ʹ��֢״���֡�",
			"������֫�����������ܵ�һ��Ӱ�죬վ�����û����߽�Զʱ��˫�ų��в�ͬ�̶ȵ�ˮ�ס�",
			"1��С�ȳ�\n2���㡢�㡢С�ȵȴ����ʹ��ɳڣ�\n3���������͵ĸı䣬��������Ҳ�ᷢ��ת�ƣ�����ȡ�β�Ǻ������������Щ��ʹ��\n4�����������������˯����������ӹ���ѹ����֫����ʹ��֫����ѪҺ�������裬������֫��������ˮ�ס��������š�",
			"1�������ӹ�����ѹ����ǻ��������ʹ��������֫����ѪҺ��������������˫��ˮ�ף��㱳���ڡ����ײ�ˮ��������;\n2���������صĴ�����ӣ�֧�������˫�ȼ���ƣ�ͼ��أ�¡��ĸ���ѹ�ȴ��ȵľ������������е������ͱ�������ƣ�ͣ����ᱳ��;\n3�����Ｄ�ط��ڻᵼ����ָ����ֺ�������ؽڲ�λ����ɳ�;\n4���Ȳ���",
			"1������Խ��Խ���أ��������Ȳ������º󱳺��Ȳ���ʹ��������;\n2���Ȳ������ܻ�Խ��Խ����;\n3�����桢С��ˮ������Խ��Խ���أ�վ��������̫�ã�ˮ�׻���ء�",
			"1�����ڸ���Ѹ�����󣬽��ס��������Ż���أ�\n2��������Ҫ�������������Ѫѹ�ۺ��������׳��֣�\n3���������屿�أ���������·�����������������ˤ����\n4���������Ȳ������º󱳺��Ȳ���ʹ�������ԣ�",
			"1�������������������̶ȣ�����������ձ���������̬��\n2����׽�Ĥ�ס�ϥ�ؽ�ʹ���±�ʹ�ȼ������أ�\n3�����Ʊ����",
			"1.����ͳܹ����ϴ����ۣ�����Ƶ����̥�������½�������;\n2.��ָ�ͽ�ֺ�Ĺؽ���ʹ����ʹ���أ��ؽں��ʹ����ɳ�;\n3.�Ȳ��ĸ����ǳ��أ��������־��κ���ʹ��������ʹ;\n4.���Ÿ���������������ܼ������ˣ�����������ء�",
			"1������ˮ�ף�\n2�������������ӣ�\n3������ƣ�͵�֢״���е����ɹ����Ȳ��ǳ��ۣ�\n4���ŵ׳�����",
			"1������λ�ڹ������²���ѹ�Ȱ��׺�ֱ����������Ƶ������������أ�\n2���ӹ����ʹ���֯�ܵ�������������������ʹ�м��أ�\n3������ֹͣ���ӣ���ʱ�������ؼ�������󣬱�־��̥���ѷ������졣",
			"1������λ�ڹ������²���ѹ�Ȱ��׺�ֱ����������Ƶ����������������أ�\n2�������ĸ��أ�ѹ�������ѪҺѭ����ˮ�׼��أ�\n3���ŵ׳�����",
			"1���������٣����е��¸�׹�ͣ�\n2������λ�ڹ������²���ѹ�Ȱ��׺�ֱ����������Ƶ����������������أ�\n3�������ĸ��أ�ѹ�������ѪҺѭ����ˮ�׼��ء�",
			"�������Ƶ���������ʷ�Ӧ���ӹ���������������������Ǽ���������źš�"
	} ;
	public static String[] Doctor_advices = {
			"1�������վ���������������ۣ�����Ϣ��\n2����ʱ���������װ����ЬҪ���ɡ����ʣ�����Ҫע�Ᵽ�ӣ� \n3����ʱ�ڵ��˶������ᡢ������Ϊ����ɢ����������Ҫ�Ľ�������",
			"1���ݱ�߸�Ь�����к������������ӣ����������ǰ�ƣ�վ��������ͷʱ�����������˫֫�ĸ������أ��߸�Ьʹ���岻�ȣ���ˤ�����߸�ЬҲ��������֫ѪҺѭ����\n2��ѡЬ����������Ϊ׼������з������𡢻������ܣ���ѡ��רҵ���и�Ь.\n3���ʵ���������չ�˶�������֮����һ�ߣ�վ��֮��̧̧�ȣ��������Լ����Ⱥͽ��ײ������͸У�������֫ˮ�ס�",
			"1���㡢�㡢С�ȵȴ����ʹ��ɳڣ����Ҫѡ�����ʡ��������𡢻������и�Ь��רҵ���и�Ь������Ľ��������⣬�𵽱������׺��㹭�����ã�\n2���Ȳ����ʱ�������Ȳ�ƣ�ͣ������Ȳ���ů�����оֲ���Ħ���ȷ�˯ǰ�ѽ�̧�ߣ�\n3�����ⳤʱ��վ��������˯�ˣ�������˯��ʱȡ�����λ��������ĸ�ӽ�����\n4������������Ӿ�ĺ�ʱ�ڣ�ˮ�������ɼ������ɳڵĹؽ����˻��ᣬ�������帺����",
			"1�����ⳤʱ��վ�������Ż�����ʱ���̧�߽ţ�������ʹ��֫�ľ���ѭ������һЩ��\n2������ˮ��ʱ����ѡ����һ����и�Ь�������ࣨ������ƣ�͡���������ֹ�������ͺ;������ţ�;\n 3���Ȳ����ʱ�������Ȳ�ƣ�ͣ������Ȳ���ů�����оֲ���Ħ���ȷ�˯ǰ�ѽ�̧�ߡ�",
			"1������ʱѡ�����ʡ�͸����������ɵ�רҵ�и�Ь��Ϊ��Ҫ��\n2��Ԥ���������ţ����ⳤʱ��վ����˯��ʱ�ڽŵ��·�һЩ��Ķ���������ѪҺѭ����\n3�������٤ȫ�ײ٣����㲿��Ħ�٣��ܹ���һ���Ļ������á�",
			"1��������ˮ�����η����룬Ԥ������ˮ�ף�С�������Ѫѹ�ۺ�����\n2�����ڼ�飬�����һЩ���˵Ļ��\n3�����ܽ��а�Ħ������ϢΪ����\n4����ʹ�����Ļ���Ӧ����ȥ��ҽ��\n5���ʵ�����Щ�˶���",
			"1.����ʱѡһ˫�ʺϵ�Ь�ӣ���˫���������⣻\n2.�ʵ�����Щ�˶������Ż�������ʱ�ѽ�̧�ߡ�",
			"1.���������������Ͷ�����Ҫ���ص���Ʒ���������Ӹ�ѹ��\n2��ѡһ˫רҵ���и�Ь����Э���ֵ��Ȳ�ѹ����\n3��ע����ʳ�ĵ�����",
			"1.��Ҫ����ˮ�ֵ���������ĸ���̥������Ҫ����ˮ�֣������ˮ��Խ�࣬����Խ�ܰ������ų����ڵ�ˮ�֣�\n2.ˮ�ױ��������վ����Ҫ�Թ��̵�ʳ��Է�ֹˮ�׼��أ�\n3.�ʵ���Ϣ����Ҫ̫�����ۣ�\n4.ѡ��Ь�׷�����Ь�������͸����Ь��",
			"1����ʱ�����õ��ӵ��ڱ����İ�����\n2��վʱҪע�����Ʋ�վֱ��������רҵ���и�Ь�ӣ�\n3����Ҫ�����",
			"1�����ᰴĦ�ٽ���֫ѪҺѭ����������֫���⣬������֫����Ȳ��ʣ�\n2������ѡ��һ˫���͸����Ь��",
			"�����ʵ����˶�����ת���˶����٤�ȣ����������ӱ����ʹ��ȼ���������ͻ�����Ƥ�����ԣ�������˳�����䡣�������ص����ӣ����帺���أ��˶�ʱһ��Ҫ�ر�ע�ⰲȫ���˶���������Ϊ�������ܹ���ƣ�͡�",
			"��΢��Ħ�Ȳ��������������������һ������ʹ��"
	};
	public static String[] Foot_Define={"վ��ʱ�㹭�߶ȵ�","վ��ʱ�㹭�߶��Ե�","վ��ʱ�㹭�߶�����","վ��ʱ�㹭�߶��Ը�","վ��ʱ�㹭�߶ȸ�"};

	public static WeightRate[] Weight_rate={null,new WeightRate(0.44,0.58,0.66,0.87),
			new WeightRate(0.35,0.5,0.53,0.75)
			,new WeightRate(0.23,0.33,0.46,0.66)
			,new WeightRate(0.17,0.27,0.34,0.54)};
	/**
	 * ���ݽ�Ҫ��ӡ�Ĳ���
	 * @param clinic_info ר��+����
	 * @param wait_num �ȴ�����
	 * */
	public void setCinicInfo(String clinic_info, String wait_num){
		this.clinic_info = clinic_info;
		this.wait_num = wait_num;
	}
	
	public void setReportParam(String no, String date_yunfu,boolean isSingle,float height){
		this.no = no;
		this.date_yunfu = date_yunfu;
		this.isSingle = isSingle;
		this.height = height;
	}
	
	public void setReportFootParam(String left_foot_status, String left_foot_advice,String right_foot_status, String right_foot_advice){
		this.left_foot_status = left_foot_status;
		this.left_foot_advice = left_foot_advice;
		this.right_foot_status = right_foot_status;
		this.right_foot_advice = right_foot_advice;
	}
	/**
	 * ���Ͳ�����Ϣ
	 * */
	public void setFeetPara(String[] para){
		this.para = para;
	}
	/**
	 * ��ά���ַ
	 * */
	public void setQrcode(String qrcode){
		this.qrcode = qrcode;
	}
	
	/**
	 * ���ҳ�ϵͳ���ṩ��Ĭ�ϴ�ӡ����
	 * */
	public String havePrint(){
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();
		if(service == null){
			return null;
		}else{
			return service.getName();
		}
	}
	
	/** 
     * ��ȡ��������λ�� 
     * @return 
     *  @author kael 2017��6��23�� 
     */  
    private String getChineseFont(){  
        //���壨��Ӧcss�е� ���� font-family: SimSun; /*����*/��  
        String font1 ="C:/Windows/Fonts/msyh.ttc";  
        //�ж�ϵͳ���ͣ����������ļ�  
//        java.util.Properties prop = System.getProperties();  
//        String osName = prop.getProperty("os.name").toLowerCase();  
//        System.out.println(osName);  
//        if (osName.indexOf("linux")>-1) {  
//            font1="/usr/share/fonts/simsun.ttc";  
//        }  
        if(!new File(font1).exists()){  
            throw new RuntimeException("�����ļ�������,Ӱ�쵼��pdf������ʾ��"+font1);  
        }  
        return font1;  
    }  
	
    public final byte SHAPE_SKINNY=1;
    public final byte SHAPE_NORMAL=2;
    public final byte SHAPE_FAT=3;
    public final byte SHAPE_OVERFAT=4;
    public final byte earlyStage=12;     //������
    public final byte earlyAdd=2;  //������������
    private int getShape(double bmi){
    	if(bmi>0 && bmi<18.5){
    		return SHAPE_SKINNY;
    	}else if(bmi>=18.5 && bmi<25){
    		return SHAPE_NORMAL;
    	}else if(bmi>=25 && bmi<30){
    		return SHAPE_FAT;
    	}else{
    		return SHAPE_OVERFAT;
    	}
    }
    
    private String getSuggestionWeight(double bmi,int week,double weight,boolean isSingle){
    	int shape = getShape(bmi);
    	if(week > 42){
    		week = 42;
    	}
    	if (week <= earlyStage) {
            return weight + "kg-" + (weight + earlyAdd) + "kg";
        }
    	
    	week -= earlyStage;
    	if(shape <1 && shape >= Weight_rate.length){
    		return weight+"kg-"+weight+"kg";
    	}
    	
    	WeightRate wr = Weight_rate[shape];
    	double minRate,maxRate;
    	if(isSingle){
    		minRate = wr.rateMin;
    		maxRate = wr.rateMax;
    	}else{
    		minRate = wr.drateMin;
    		maxRate = wr.drateMax;
    	}
    	double minWeight = weight + minRate * week;
    	double maxWeight = weight + earlyAdd + maxRate * week;
    	
    	DecimalFormat decimalFormat=new DecimalFormat("0.0");//���췽�����ַ���ʽ�������С������1λ,����0����.
	    String min_str=decimalFormat.format(minWeight);
	    String max_str=decimalFormat.format(maxWeight);
	    
    	return min_str+"kg-"+max_str+"kg";
    }
    
	public boolean genReport(){
		 String fileName = MainJFrame.scanExternProgramDir+"report_template\\template.pdf"; 
		 try {
			 PdfReader reader = new PdfReader(fileName);
			 PdfStamper ps = new PdfStamper(reader, 
					   new FileOutputStream(MainJFrame.scanExternProgramDir+"current_report.pdf"),'\0');
			 AcroFields s = ps.getAcroFields();		    
		    Date now = new Date(); 
//		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//���Է�����޸����ڸ�ʽ
//		    String hehe = dateFormat.format( now ); 
		    Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�
		    int year = c.get(Calendar.YEAR); 
		    int month = c.get(Calendar.MONTH); 
		    int date = c.get(Calendar.DATE);
		    SimpleDateFormat format2= new SimpleDateFormat("yyyy-MM-dd");  
		    Date lastPeriod=null;
			try {
//				String date_yunfu = "2017-02-23T00:00:00.000Z";
				if(this.date_yunfu==null){
					this.date_yunfu = "";
				}
				String str_lastPeriod = this.date_yunfu.substring(0, this.date_yunfu.indexOf("T"));
				lastPeriod = format2.parse(str_lastPeriod);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(lastPeriod==null){
				return false;
			}
			if(para == null){
				return false;
			}
		    long weeks_long = ((now.getTime()-lastPeriod.getTime())/(7 * 24 * 3600 * 1000) + 1); 
		    			
		    int leftLengthIndex =  para[2].indexOf(".");
			int rightLengthIndex =  para[3].indexOf(".");
			int leftWidthIndex =  para[4].indexOf(".");
			int rightWidthIndex =  para[5].indexOf(".");
			int leftArrowIndex =  para[6].indexOf(".");
			int rightArrowIndex =  para[7].indexOf(".");
			
			String leftLengthString = para[2].substring(0, leftLengthIndex<0?para[2].length():leftLengthIndex)+"mm";
			String rightLengthString = para[3].substring(0, rightLengthIndex<0?para[3].length():rightLengthIndex)+"mm";
			String leftWidthString = para[4].substring(0, leftWidthIndex<0?para[4].length():leftWidthIndex)+"mm";
			String rightWidthString = para[5].substring(0, rightWidthIndex<0?para[5].length():rightWidthIndex)+"mm";
			String leftArrowString = para[6].substring(0, leftArrowIndex<0?para[6].length():leftArrowIndex)+"mm";
			String rightArrowtring = para[7].substring(0, rightArrowIndex<0?para[7].length():rightArrowIndex)+"mm";
			String leftStatusString = para[8].replaceAll("�㹭", "");
			String rightStatusString = para[9].replaceAll("�㹭", "");
		    
			int week_bound_start = -1;
			int week_bound_end = -1;
			int week_index = -1;
			for(int i=0;i<PrintUtil.Weeks_index.length/2;i+=2){
				int start = PrintUtil.Weeks_index[i];
				int end = PrintUtil.Weeks_index[i+1];
				if(weeks_long>=start && weeks_long<=end){
					week_bound_start = start;
					week_bound_end = end;
					week_index=i/2;
					break;
				}
			}
			
			if(week_index<0 || week_index>=PrintUtil.Common_symptoms.length){
				week_index = PrintUtil.Common_symptoms.length-1;
				week_bound_start = PrintUtil.Weeks_index[week_index];
				week_bound_end = PrintUtil.Weeks_index[week_index+1];
			}
			
			String font_cn = getChineseFont();  
			BaseFont bf = BaseFont.createFont(font_cn+",1", //ע��������һ��,1  
                     BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
			com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf,12);  
			ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();   
		    fontList.add(bf); 
		    
		    double weight_float = Double.parseDouble(para[1]);
		    double height_float = Double.parseDouble(para[0])/100.0;
		    
		    double bmi = 0.0;
		    if(height_float>0){
		    	bmi = weight_float / (height_float * height_float);
		    }
		    String suggestionWeight = this.getSuggestionWeight(bmi, (int) (weeks_long), weight_float, isSingle);
		    DecimalFormat decimalFormat=new DecimalFormat("0.0");//���췽�����ַ���ʽ�������С������2λ,����0����.
		    String bmi_str=decimalFormat.format(bmi);
		    
		    boolean leftok = true;
		    boolean rightok = true;
		    if(!this.left_foot_status.contains("����")){
		    	leftok = false;
		    }
		    if(!this.right_foot_status.contains("����")){
		    	rightok = false;
		    }
		    String foot_Annalyse = "";
		    String foot_Definitation = "";
		    String foot_Suggestion = "";
		    if(leftok && rightok){
		    	foot_Annalyse = this.left_foot_status;
		    	foot_Suggestion = this.left_foot_advice;
		    }else{
		    	if(!leftok){
		    		foot_Annalyse = this.left_foot_status;
		    		foot_Suggestion = this.left_foot_advice;
		    	}
		    	if(!rightok){
		    		foot_Annalyse = this.right_foot_status;
		    		foot_Suggestion = this.right_foot_advice;
		    	}
		    }

		    int definitationIdx = 2;
		    for(int i = 0;i<FootCalcUtil.status.length;i++){
		    	if(FootCalcUtil.status[i].contains(foot_Annalyse)){
		    		definitationIdx = i;
		    		break;
		    	}
		    }
	    	foot_Definitation = PrintUtil.Foot_Define[definitationIdx];
		    
		    s.setSubstitutionFonts(fontList);  
		    s.setField("No", this.no);
		    s.setField("Year", String.valueOf(year));
		    s.setField("Month", String.valueOf(month+1));
		    s.setField("Day", String.valueOf(date));
		    s.setField("Weeks", String.valueOf(weeks_long));
		    s.setField("Weight",para[1]+"kg" );
		    s.setField("Height",height+"cm" );
		    s.setField("LLength", leftLengthString);
		    s.setField("RLength", rightLengthString);
		    s.setField("LWidth", leftWidthString);
		    s.setField("RWidth", rightWidthString);
		    s.setField("LFootType", leftStatusString);
		    s.setField("RFootType", rightStatusString);
		    s.setField("LTypeWidth", leftArrowString);
		    s.setField("RTypeWidth", rightArrowtring);
		    s.setField("BMI", bmi_str);
		    s.setField("SuggestWeight", suggestionWeight);
		    s.setField("Syms", PrintUtil.Common_symptoms[week_index]);
		    s.setField("Advices", PrintUtil.Doctor_advices[week_index]);
		    s.setField("PregentWeeks", week_bound_start+"-"+week_bound_end);
		    s.setField("Annalyse", foot_Annalyse);
		    s.setField("Suggestion", foot_Suggestion);
		    s.setField("Definitation", foot_Definitation);
		    
		    com.itextpdf.text.Image gif = com.itextpdf.text.Image.getInstance(MainJFrame.scanExternProgramDir+"\\Image\\fin.jpg");
			gif.setDpi(100, 100);
			gif.setBorderWidth(200);
			gif.scaleAbsolute(150, 88); 
			gif.setAbsolutePosition(130, 500);
			PdfContentByte over = ps.getOverContent(1);
			over.addImage(gif);
			   
			ps.setFormFlattening(true);
			s.setGenerateAppearances(true);
			ps.close();
			reader.close();
			return true;
		 } catch (FileNotFoundException e) {
		   e.printStackTrace();
		   return false;
		 } catch (Exception e) {
		   e.printStackTrace();
		   return false;
		 } finally {
//			    doc.close();
		 }
	}
	
	public void doPrintReportExtern(String reportPrinterName){
		if(reportPrinterName.length()<=0){
			return;
		}
		// check printer is exist or not
		DocFlavor fileFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSizeName.ISO_A4);//A4ֽ��
		PrintService[] services = PrintServiceLookup.lookupPrintServices(fileFormat, null);
		boolean printerExist = false;
		if(services.length > 1){
			for(int i=0;i<services.length;i++){
				if(services[i].getName().equals(reportPrinterName)){
					printerExist = true;
					break;
				}
			}
		}
		if(!printerExist){
			return;
		}
		String fileName = MainJFrame.scanExternProgramDir+"current_report.pdf";
		String externProgramName = MainJFrame.scanExternProgramDir+"\\SumatraPDF\\SumatraPDF.exe";
		String externProgramNeededDllName = MainJFrame.scanExternProgramDir+"\\SumatraPDF\\libmupdf.dll";
		File fileexternProgram = new File(externProgramName);
		File fileexternProgramNeededDll = new File(externProgramNeededDllName);
		if(fileexternProgram.exists() && fileexternProgramNeededDll.exists()){
			String cmd = MainJFrame.scanExternProgramDir+"startPrint.bat";
			ProcessBuilder pb = new ProcessBuilder(cmd,MainJFrame.scanExternProgramDir,reportPrinterName,fileName);
			Process process;
			try {
				process = pb.start();
				process.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void doPrintReport(String reportPrinterName){
		if(reportPrinterName.length()<=0){
			return;
		}
		//����Ҫ��ӡ�ļ��ĸ�ʽ�������PDF�ĵ�Ҫ��Ϊ�Զ�ʶ��
		DocFlavor fileFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		//����һ����ӡ�������ö���
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//		aset.add(new Copies(1));//Copies-��ӡ����5��
		aset.add(MediaSizeName.ISO_A4);//A4ֽ��
//		aset.add(Sides.ONE_SIDED);//˫���ӡ
		//�õ���ǰ�����������Ѿ���װ�Ĵ�ӡ��
		//����Ĳ������ĵ���ʽ����ӡ���ԣ�ֻ��֧�������ʽ�����ԵĴ�ӡ���Żᱻ�ҵ�
		PrintService[] services = PrintServiceLookup.lookupPrintServices(fileFormat, null);
		if(services.length > 1){
			//�õ�һ���ļ���������
			FileInputStream fiStream = null;
			 try {
				   fiStream = new FileInputStream(MainJFrame.scanExternProgramDir+"current_report.pdf");
			 } catch (FileNotFoundException ffne) {
			 }
			 if (fiStream == null) {
			    return;
			 }
			//�õ�Ҫ��ӡ���ĵ���DOC
			DocAttributeSet das = new HashDocAttributeSet();
			Doc myDoc = new SimpleDoc(fiStream, fileFormat, das);  
			for(int i=0;i<services.length;i++){
				if(services[i].getName().equals(reportPrinterName)){
					//�ô�ӡ��������һ���ĵ���ӡ���������õ��ǵ�һ������
				    //Ҳ���Խ���ɸѡ��services[i].getName()���Եõ���ӡ�����ƣ��������ƽ��бȽϵõ��Լ���Ҫ�Ĵ�ӡ��
					DocPrintJob job = services[i].createPrintJob();
			        try {
			            //���һ����ִ�д�ӡ�ĵ����񣬴���Ĳ�����Doc�ĵ��࣬������(1�ݣ�����,A4)
			            job.print(myDoc, aset);//�ɹ�����Ի���ʾ�����ĵ���ӵ���ӡ����
			        } catch (PrintException pe) {
			        }
			        break;
				}
			}
		} 
	}
	
	/*
	public static void main(String[] args){
		PrintUtil util = new PrintUtil();
//		util.setCinicInfo("������"+"  "+"079", "25");
		util.setCinicInfo(null, null);
//		util.setFeetPara(new String[]{"164.5","53.3","241.36","240.12","90.20","91.13","156.31","156.96","��ƽ����","�����㹭"});
		util.setFeetPara(new String[]{null,null,"241.36","240.12","90.20","91.13","156.31","156.96","��ƽ����","�����㹭"});
//		util.setFeetPara(null);
//		util.setQrcode("E:\\Javadata\\HPFootTerminal07\\picture\\qrcode_para.jpg");
		util.setQrcode(null);
//		util.printpaper();
		
		HashAttributeSet hs = new HashAttributeSet();  
	    String printerName="SK58";  
	    hs.add(new PrinterName(printerName,null));  
	    PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs); 
	    if(pss.length==0)  
	    {  
	      System.out.println("�޷��ҵ���ӡ��:"+printerName);  
	      return ;  
	    } else{
	    	PrinterJob printerJob = PrinterJob.getPrinterJob();
		    PageFormat pageFormat = new PageFormat();
		    //pf.setOrientation(PageFormat.PORTRAIT);//���ó�����
		    Paper paper = new Paper();
		    //�����ӡ������58mm*3276mm(�����58���ף��߶���3276����)
		    //����Ӣ�磺58/25.4=2.28346457Ӣ��
		    //���������2.28*72=164.409449��(һӢ����72��)
		    //ֽ�Ŵ�С A4�Ĵ�С//A4(595 X 842)���ô�ӡ������ʵ0,0Ӧ����72,72����ΪA4ֽ��Ĭ��X,Y�߾���72
		    paper.setSize(590,840);
		    paper.setImageableArea(15,5, 150,440);//144
		    pageFormat.setPaper(paper);
		    Book book = new Book();
			book.append(util, pageFormat);
			printerJob.setPageable(book);
	    	 try {
				printerJob.setPrintService(pss[0]);
		         printerJob.print(); 
			} catch (PrinterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
	    }
	    util.doPrintReport("HP LaserJet Professional P1108");
//		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//		for(int i=0;i<services.length;i++){
//			System.out.println(services[i].getName());
//			if(services[i].getName().equals("Microsoft Print to PDF")){
//				//�ô�ӡ��������һ���ĵ���ӡ���������õ��ǵ�һ������
//			    //Ҳ���Խ���ɸѡ��services[i].getName()���Եõ���ӡ�����ƣ��������ƽ��бȽϵõ��Լ���Ҫ�Ĵ�ӡ��
//				DocPrintJob job = services[i].createPrintJob();
//		        try {
//		            //���һ����ִ�д�ӡ�ĵ����񣬴���Ĳ�����Doc�ĵ��࣬������(5�ݣ�˫��,A4)
//		        	Paper paper = new Paper();
//		        	 PageFormat pageFormat = new PageFormat();
//		    	    //�����ӡ������58mm*3276mm(�����58���ף��߶���3276����)
//		    	    //����Ӣ�磺58/25.4=2.28346457Ӣ��
//		    	    //���������2.28*72=164.409449��(һӢ����72��)
//		    	    //ֽ�Ŵ�С A4�Ĵ�С//A4(595 X 842)���ô�ӡ������ʵ0,0Ӧ����72,72����ΪA4ֽ��Ĭ��X,Y�߾���72
//		    	    paper.setSize(590,840);
//		    	    paper.setImageableArea(15,5, 150,440);//144
//		    	    pageFormat.setPaper(paper);
//		    	    Book book = new Book();
//		    		book.append(util, pageFormat);
//		    		((PrinterJob) job).setPageable(book);
//		    		((PrinterJob) job).print();//�ɹ�����Ի���ʾ�����ĵ���ӵ���ӡ����
//		        } catch (PrinterException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		        break;
//			}
//		}
	}
	*/
	
	private void _printpaper(String printer){
		if(printer.length()<=0){
			return;
		}
		HashAttributeSet hs = new HashAttributeSet();
	    hs.add(new PrinterName(printer,null));
	    PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs); 
	    if(pss.length==0){  
	    	System.out.println("�޷��ҵ���ӡ��:"+printer);  
	    	return ;
	    }
		
		PrinterJob printerJob = PrinterJob.getPrinterJob();
	    PageFormat pageFormat = new PageFormat();
	    //pf.setOrientation(PageFormat.PORTRAIT);//���ó�����
	    Paper paper = new Paper();
	    //�����ӡ������58mm*3276mm(�����58���ף��߶���3276����)
	    //����Ӣ�磺58/25.4=2.28346457Ӣ��
	    //���������2.28*72=164.409449��(һӢ����72��)
	    //ֽ�Ŵ�С A4�Ĵ�С//A4(595 X 842)���ô�ӡ������ʵ0,0Ӧ����72,72����ΪA4ֽ��Ĭ��X,Y�߾���72
	    paper.setSize(590,840);
	    paper.setImageableArea(15,5, 150,370);//144
	    pageFormat.setPaper(paper);
	    Book book = new Book();
		book.append(this, pageFormat);
		printerJob.setPageable(book);
//		boolean doPrint = printerJob.printDialog();
//		if (doPrint) {
			try {
				printerJob.setPrintService(pss[0]);
				printerJob.print();
			} catch (PrinterException exception) {
				System.err.println("Printing error: " + exception);
			}
//		}
	}
	
	public void printpaper() {		
		this._printpaper(ServerConfig.receiptPrinterName);
	}
	
	public void printpaper02() {
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		boolean doPrint = printerJob.printDialog();
		if(doPrint){
			printerJob.setPrintable(this);
			try {
				printerJob.print();
			} catch (PrinterException ex1) {
				System.out.println(ex1.getMessage());
			}
		}
	}
	
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)throws PrinterException {
		Graphics2D g2 = (Graphics2D) graphics;
		g2.setColor(Color.black);
		double x = pageFormat.getImageableX();
		double y = pageFormat.getImageableY()+1;
		switch (pageIndex) {
			case 0:
				g2.translate((float)x, (float)y);
				
				float[] dash1 = { 4.0f };
				g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 4.0f, dash1, 0.0f));
				
				Font font = new Font("������", Font.PLAIN, 13);
				g2.setFont(font);
				float height = font.getSize2D();
//				if(clinic_info != null){
//					g2.drawString("���ã������ŶӺţ�", 0, (float)(1*height));
//					g2.translate(0, (float)(1*height+15));
//					
//					font = new Font("������", Font.PLAIN, 20);
//					g2.setFont(font);
//					height = font.getSize2D();
//					g2.drawString(clinic_info, 10, (float)(1*height));
//					g2.drawLine(0, (int)(1*height+15), 200,(int)(1*height+15));
//					g2.translate(0, (float)(1*height+15));
//					
//					font = new Font("������", Font.PLAIN, 13);
//					g2.setFont(font);
//					height = font.getSize2D();
//					g2.drawString("�����ڵ��ŶӶ���ǰ��", 0, (float)(8+1*height));
//					g2.drawString("��" + wait_num + "�˵Ⱥ�", 0, (float)(10+2*height));
//					g2.translate(0, (float)(10+2*height+8));
//					
//					font = new Font("������", Font.PLAIN, 10);
//					g2.setFont(font);
//					height = font.getSize2D();
//					g2.drawString("����������кţ���������", 0, (float)(1*height));
//					g2.translate(0, (float)(1*height));
//				}
				
				if(para != null){
					if(clinic_info != null){
						g2.drawLine(0, 10, 200,10);
					}
					font = new Font("������", Font.PLAIN, 13);
					g2.setFont(font);
					height = font.getSize2D();
					g2.drawString("���Ĳ������ݽ�����£�", 0, (float)(18+1*height));
					g2.drawLine(0, (int)(1*height+31), 200,(int)(1*height+32));
					g2.translate(0, (float)(31+1*height+8));
					
					font = new Font("������", Font.PLAIN, 10);
					g2.setFont(font);
					height = font.getSize2D();
					
					if(para[0] != null){
						g2.drawString("���:"+para[0]+"cm    ����:"+para[1]+"kg", 0, (float)(1*height));
						g2.translate(0, (float)(1*height));
					}else{
						if(para[1] != null){
							g2.drawString("����:"+para[1]+"kg", 0, (float)(1*height));
							g2.translate(0, (float)(1*height));
						}
					}
					
//					g2.drawString("��ų�:"+para[2]+"mm �ҽų�:"+para[3]+"mm", 0, (float)(1*height+3));
//					g2.drawString("��ſ�:"+para[4]+"mm �ҽſ�:"+para[5]+"mm", 0, (float)(2*height+6));
//					
//					g2.drawString("���㹭:"+para[6]+"mm ���㹭:"+para[7]+"mm", 0, (float)(3*height+8));
//					g2.drawString("����̬:"+para[8]+" ����̬:"+para[9], 0, (float)(4*height+10));
					
					int leftLengthIndex =  para[2].indexOf(".");
					int rightLengthIndex =  para[3].indexOf(".");
					int leftWidthIndex =  para[4].indexOf(".");
					int rightWidthIndex =  para[5].indexOf(".");
					int leftArrowIndex =  para[6].indexOf(".");
					int rightArrowIndex =  para[7].indexOf(".");
					
					String leftLengthString = para[2].substring(0, leftLengthIndex<0?para[2].length():leftLengthIndex)+"mm";
					String rightLengthString = para[3].substring(0, rightLengthIndex<0?para[3].length():rightLengthIndex);
					String leftWidthString = para[4].substring(0, leftWidthIndex<0?para[4].length():leftWidthIndex)+"mm";
					String rightWidthString = para[5].substring(0, rightWidthIndex<0?para[5].length():rightWidthIndex);
					String leftArrowString = para[6].substring(0, leftArrowIndex<0?para[6].length():leftArrowIndex)+"mm";
					String rightArrowtring = para[7].substring(0, rightArrowIndex<0?para[7].length():rightArrowIndex);
					String leftStatusString = para[8].replaceAll("�㹭", "");
					String rightStatusString = para[9].replaceAll("�㹭", "");
					short totalLength=7;
					short leftLengthLeft = (short) (totalLength - leftLengthString.length());
					for(int i = 0;i<leftLengthLeft;i++){
						leftLengthString+=" ";
					}
					short leftWidthLeft = (short) (totalLength - leftWidthString.length());
					for(int i = 0;i<leftWidthLeft;i++){
						leftWidthString+=" ";
					}
					short leftArrorLeft = (short) (totalLength - leftArrowString.length());
					for(int i = 0;i<leftArrorLeft;i++){
						leftArrowString+=" ";
					}
					g2.drawString("��ų�:"+leftLengthString+"�ҽų�:"+rightLengthString+"mm", 0, (float)(1*height+3));
					g2.drawString("��ſ�:"+leftWidthString+"�ҽſ�:"+rightWidthString+"mm", 0, (float)(2*height+6));
					
					g2.drawString("���㹭:"+leftArrowString+"���㹭:"+rightArrowtring+"mm", 0, (float)(3*height+8));
					g2.drawString("����̬:"+leftStatusString+"   ����̬:"+rightStatusString, 0, (float)(4*height+10));
					
					if(qrcode == null){
						g2.drawString("���ڹ��ں��ϲ�ѯ������Ϣ", 0, (float)(5*height+14));
						g2.translate(0, (float)(5*height+14));
					}else{
						g2.drawString("���ע���ںŻ�ȡ������Ϣ", 0, (float)(5*height+14));
						g2.translate(0, (float)(5*height+14));
						Image src = Toolkit.getDefaultToolkit().getImage(qrcode);
						g2.drawImage(src,15,1,120,120,null);
						g2.translate(0, 121);
					
//						��ʾ��ע�����ڶ�ά���·�
//						g2.translate(0, (float)(3*height+6));
//						Image src = Toolkit.getDefaultToolkit().getImage(qrcode);
//						g2.drawImage(src,25,1,100,100,null);
//						g2.drawString("���ע���ںŻ�ȡ������Ϣ", 10, 103);
//						g2.translate(0, 103);
					}
				}
				
				font = new Font("������", Font.PLAIN, 10);
				g2.setFont(font);
				height = font.getSize2D();
				g2.drawString("ʱ�䣺" + DateUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"), 0, 35);
				g2.drawString("�����Ʊ��ܱ�ƾ��", 25, 35+height+4);
				
				return PAGE_EXISTS;
			default:
				return NO_SUCH_PAGE;
		}
	}

}