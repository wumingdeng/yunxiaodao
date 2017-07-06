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
			"由于子宫慢慢变大，造成盆腔内器官位置发送改变，导致膀胱承受的压力增加，进而发生尿频，也会导致腿部紧绷发疼、腰酸腰痛的症状出现。",
			"由于下肢静脉回流常受到一定影响，站立过久或行走较远时，双脚常有不同程度的水肿。",
			"1、小腿抽筋；\n2、足、裸、小腿等处的韧带松弛；\n3、随着体型的改变，身体重心也会发生转移，你的腿、尾骨和其他肌肉会有些疼痛；\n4、孕妈妈如果是仰卧睡觉，增大的子宫会压迫下肢静脉使下肢静脉血液回流受阻，引起下肢及外阴部水肿、静脉曲张。",
			"1、由于子宫增大，压迫盆腔静脉，会使孕妈咪下肢静脉血液回流不畅，引起双腿水肿，足背及内、外踝部水肿尤其多见;\n2、随着体重的大幅增加，支撑身体的双腿肌肉疲劳加重，隆起的腹部压迫大腿的静脉，孕妈妈会感到腰部和背部容易疲劳，腰酸背疼;\n3、妊娠激素分泌会导致手指、脚趾和其他关节部位变得松弛;\n4、腿部抽筋。",
			"1、腹部越来越沉重，腰部、腿部及其下后背和腿部疼痛更加明显;\n2、腿部抽筋可能会越来越严重;\n3、脚面、小腿水肿现象越来越严重，站立、蹲坐太久，水肿会加重。",
			"1、由于腹部迅速增大，脚肿、静脉曲张会加重；\n2、孕妈妈要留意有无妊娠高血压综合征的征兆出现；\n3、由于身体笨重，孕妈妈走路身体后仰看不到脚易摔倒；\n4、腰部、腿部及其下后背和腿部疼痛更加明显；",
			"1、增大脊柱的生理弯曲程度，甚至会出现驼背等身体姿态；\n2、足底筋膜炎、膝关节痛和下背痛等疾病加重；\n3、脚掌变厚变宽。",
			"1.骨盆和耻骨联合处酸疼，尿意频繁，胎儿在逐渐下降到骨盆;\n2.手指和脚趾的关节胀痛，腰痛加重，关节和韧带逐渐松弛;\n3.腿部的负担非常重，常常出现痉挛和疼痛，腹部抽痛;\n4.随着腹部的膨大，消化功能继续减退，容易引起便秘。",
			"1、妊娠水肿；\n2、体重明显增加；\n3、容易疲劳等症状，感到腹股沟和腿部非常疼；\n4、脚底出汗。",
			"1、宝宝位于骨盆中下部，压迫膀胱和直肠，导致尿频、便秘问题加重；\n2、子宫和韧带组织受到的拉扯更大，腰背部疼痛感加重；\n3、体重停止增加，有时还有体重减轻的现象，标志着胎儿已发育成熟。",
			"1、宝宝位于骨盆中下部，压迫膀胱和直肠，导致尿频、便秘问题更加严重；\n2、宝宝的负重，压迫下身的血液循环，水肿加重；\n3、脚底出汗。",
			"1、产期来临，常感到下腹坠胀；\n2、宝宝位于骨盆中下部，压迫膀胱和直肠，导致尿频、便秘问题更加严重；\n3、宝宝的负重，压迫下身的血液循环，水肿加重。",
			"如果腹部频繁产生不适反应，子宫反复不规则的收缩，就是即将分娩的信号。"
	} ;
	public static String[] Doctor_advices = {
			"1、避免久站、久坐，避免劳累，多休息；\n2、这时孕妈妈的着装、穿鞋要宽松、舒适，冬天要注意保暧； \n3、这时期的运动以轻柔、慢节奏为主，散步还是最主要的健身方法。",
			"1、暂别高跟鞋，怀孕后随着体重增加，身体的重心前移，站立或行下头时腰背部肌肉和双肢的负担加重，高跟鞋使身体不稳，易摔倒，高跟鞋也不利于下肢血液循环；\n2、选鞋方面以舒适为准则，最好有防滑减震、护弓功能，或选择专业的孕妇鞋.\n3、适当地坐坐伸展运动，坐久之后走一走，站久之后抬抬腿，这样可以减轻腿和脚踝部的肿胀感，减少下肢水肿。",
			"1、足、裸、小腿等处的韧带松弛，因此要选购舒适、防滑减震、护弓的孕妇鞋，专业的孕妇鞋会让你的脚受力均衡，起到保护脚踝和足弓的作用；\n2、腿部抽筋时，避免腿部疲劳，做好腿部保暖，进行局部按摩和热敷，睡前把脚抬高；\n3、避免长时间站立及仰躺睡姿；孕妈妈睡觉时取左侧卧位最有利于母子健康；\n4、孕中期是游泳的好时期，水的阻力可减少逐渐松弛的关节损伤机会，减轻身体负担；",
			"1、避免长时间站立，坐着或躺着时最好抬高脚，这样会使下肢的静脉循环更好一些；\n2、出现水肿时，可选购大一点的孕妇鞋及弹力袜（可消除疲劳、腿痒，防止脚踝肿胀和静脉曲张）;\n 3、腿部抽筋时，避免腿部疲劳，做好腿部保暖，进行局部按摩和热敷，睡前把脚抬高。",
			"1、出门时选择舒适、透气、掌面宽松的专业孕妇鞋尤为重要；\n2、预防静脉曲张，避免长时间站立，睡觉时在脚底下放一些软的东西，改善血液循环；\n3、可做瑜伽全套操，或足部按摩操，能够起到一定的缓解作用。",
			"1、控制饮水量和盐分摄入，预防出现水肿，小心妊娠高血压综合征；\n2、定期检查，检查做一些适宜的活动；\n3、不能进行按摩，以休息为主；\n4、疼痛厉害的话，应马上去就医；\n5、适当地做些运动。",
			"1.出门时选一双适合的鞋子，让双脚受力均衡；\n2.适当地做些运动，坐着或者躺着时把脚抬高。",
			"1.尽量避免重体力劳动，不要提重的物品，以免增加腹压；\n2、选一双专业的孕妇鞋减轻协助分担腿部压力；\n3、注意饮食的调整。",
			"1.不要限制水分的摄入量，母体和胎儿都需要大量水分，摄入的水分越多，反而越能帮助你排出体内的水分；\n2.水肿避免久坐久站，不要吃过咸的食物，以防止水肿加重；\n3.适当休息，不要太过劳累；\n4.选择鞋底防滑、鞋跟厚、轻便透气的鞋。",
			"1、坐时可以用垫子垫在背部的凹处；\n2、站时要注意姿势并站直，尽量穿专业的孕妇鞋子；\n3、不要提重物。",
			"1、轻柔按摩促进下肢血液循环，放松下肢肌肉，改善下肢酸沉等不适；\n2、出门选择一双轻便透气的鞋。",
			"保持适当的运动（如转身运动、瑜伽等），可以增加背部和大腿肌肉的力量和会阴的皮肤弹性，有利于顺利分娩。但因体重的增加，身体负担重，运动时一定要特别注意安全，运动尤其以慢为主，不能过于疲劳。",
			"轻微按摩腿部、腰部、腹部，缓解第一产程阵痛。"
	};
	public static String[] Foot_Define={"站立时足弓高度低","站立时足弓高度稍低","站立时足弓高度正常","站立时足弓高度稍高","站立时足弓高度高"};

	public static WeightRate[] Weight_rate={null,new WeightRate(0.44,0.58,0.66,0.87),
			new WeightRate(0.35,0.5,0.53,0.75)
			,new WeightRate(0.23,0.33,0.46,0.66)
			,new WeightRate(0.17,0.27,0.34,0.54)};
	/**
	 * 传递将要打印的参数
	 * @param clinic_info 专科+号码
	 * @param wait_num 等待人数
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
	 * 脚型参数信息
	 * */
	public void setFeetPara(String[] para){
		this.para = para;
	}
	/**
	 * 二维码地址
	 * */
	public void setQrcode(String qrcode){
		this.qrcode = qrcode;
	}
	
	/**
	 * 查找出系统上提供的默认打印服务
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
     * 获取中文字体位置 
     * @return 
     *  @author kael 2017年6月23日 
     */  
    private String getChineseFont(){  
        //宋体（对应css中的 属性 font-family: SimSun; /*宋体*/）  
        String font1 ="C:/Windows/Fonts/msyh.ttc";  
        //判断系统类型，加载字体文件  
//        java.util.Properties prop = System.getProperties();  
//        String osName = prop.getProperty("os.name").toLowerCase();  
//        System.out.println(osName);  
//        if (osName.indexOf("linux")>-1) {  
//            font1="/usr/share/fonts/simsun.ttc";  
//        }  
        if(!new File(font1).exists()){  
            throw new RuntimeException("字体文件不存在,影响导出pdf中文显示！"+font1);  
        }  
        return font1;  
    }  
	
    public final byte SHAPE_SKINNY=1;
    public final byte SHAPE_NORMAL=2;
    public final byte SHAPE_FAT=3;
    public final byte SHAPE_OVERFAT=4;
    public final byte earlyStage=12;     //孕早期
    public final byte earlyAdd=2;  //早期体重增加
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
    	
    	DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
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
//		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
//		    String hehe = dateFormat.format( now ); 
		    Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
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
			String leftStatusString = para[8].replaceAll("足弓", "");
			String rightStatusString = para[9].replaceAll("足弓", "");
		    
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
			BaseFont bf = BaseFont.createFont(font_cn+",1", //注意这里有一个,1  
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
		    DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		    String bmi_str=decimalFormat.format(bmi);
		    
		    boolean leftok = true;
		    boolean rightok = true;
		    if(!this.left_foot_status.contains("正常")){
		    	leftok = false;
		    }
		    if(!this.right_foot_status.contains("正常")){
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
		aset.add(MediaSizeName.ISO_A4);//A4纸张
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
		//这是要打印文件的格式，如果是PDF文档要设为自动识别
		DocFlavor fileFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		//生成一个打印属性设置对象
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//		aset.add(new Copies(1));//Copies-打印份数5份
		aset.add(MediaSizeName.ISO_A4);//A4纸张
//		aset.add(Sides.ONE_SIDED);//双面打印
		//得到当前机器上所有已经安装的打印机
		//传入的参数是文档格式跟打印属性，只有支持这个格式与属性的打印机才会被找到
		PrintService[] services = PrintServiceLookup.lookupPrintServices(fileFormat, null);
		if(services.length > 1){
			//得到一个文件的输入流
			FileInputStream fiStream = null;
			 try {
				   fiStream = new FileInputStream(MainJFrame.scanExternProgramDir+"current_report.pdf");
			 } catch (FileNotFoundException ffne) {
			 }
			 if (fiStream == null) {
			    return;
			 }
			//得到要打印的文档类DOC
			DocAttributeSet das = new HashDocAttributeSet();
			Doc myDoc = new SimpleDoc(fiStream, fileFormat, das);  
			for(int i=0;i<services.length;i++){
				if(services[i].getName().equals(reportPrinterName)){
					//用打印服务生成一个文档打印任务，这里用的是第一个服务
				    //也可以进行筛选，services[i].getName()可以得到打印机名称，可用名称进行比较得到自己想要的打印机
					DocPrintJob job = services[i].createPrintJob();
			        try {
			            //最后一步，执行打印文档任务，传入的参数是Doc文档类，与属性(1份，单面,A4)
			            job.print(myDoc, aset);//成功后电脑会提示已有文档添加到打印队列
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
//		util.setCinicInfo("妇产科"+"  "+"079", "25");
		util.setCinicInfo(null, null);
//		util.setFeetPara(new String[]{"164.5","53.3","241.36","240.12","90.20","91.13","156.31","156.96","扁平趋势","正常足弓"});
		util.setFeetPara(new String[]{null,null,"241.36","240.12","90.20","91.13","156.31","156.96","扁平趋势","正常足弓"});
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
	      System.out.println("无法找到打印机:"+printerName);  
	      return ;  
	    } else{
	    	PrinterJob printerJob = PrinterJob.getPrinterJob();
		    PageFormat pageFormat = new PageFormat();
		    //pf.setOrientation(PageFormat.PORTRAIT);//设置成竖打
		    Paper paper = new Paper();
		    //计算打印的区域：58mm*3276mm(宽度是58毫米；高度是3276毫米)
		    //计算英寸：58/25.4=2.28346457英寸
		    //计算点数：2.28*72=164.409449点(一英寸有72点)
		    //纸张大小 A4的大小//A4(595 X 842)设置打印区域，其实0,0应该是72,72，因为A4纸的默认X,Y边距是72
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
//				//用打印服务生成一个文档打印任务，这里用的是第一个服务
//			    //也可以进行筛选，services[i].getName()可以得到打印机名称，可用名称进行比较得到自己想要的打印机
//				DocPrintJob job = services[i].createPrintJob();
//		        try {
//		            //最后一步，执行打印文档任务，传入的参数是Doc文档类，与属性(5份，双面,A4)
//		        	Paper paper = new Paper();
//		        	 PageFormat pageFormat = new PageFormat();
//		    	    //计算打印的区域：58mm*3276mm(宽度是58毫米；高度是3276毫米)
//		    	    //计算英寸：58/25.4=2.28346457英寸
//		    	    //计算点数：2.28*72=164.409449点(一英寸有72点)
//		    	    //纸张大小 A4的大小//A4(595 X 842)设置打印区域，其实0,0应该是72,72，因为A4纸的默认X,Y边距是72
//		    	    paper.setSize(590,840);
//		    	    paper.setImageableArea(15,5, 150,440);//144
//		    	    pageFormat.setPaper(paper);
//		    	    Book book = new Book();
//		    		book.append(util, pageFormat);
//		    		((PrinterJob) job).setPageable(book);
//		    		((PrinterJob) job).print();//成功后电脑会提示已有文档添加到打印队列
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
	    	System.out.println("无法找到打印机:"+printer);  
	    	return ;
	    }
		
		PrinterJob printerJob = PrinterJob.getPrinterJob();
	    PageFormat pageFormat = new PageFormat();
	    //pf.setOrientation(PageFormat.PORTRAIT);//设置成竖打
	    Paper paper = new Paper();
	    //计算打印的区域：58mm*3276mm(宽度是58毫米；高度是3276毫米)
	    //计算英寸：58/25.4=2.28346457英寸
	    //计算点数：2.28*72=164.409449点(一英寸有72点)
	    //纸张大小 A4的大小//A4(595 X 842)设置打印区域，其实0,0应该是72,72，因为A4纸的默认X,Y边距是72
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
				
				Font font = new Font("新宋体", Font.PLAIN, 13);
				g2.setFont(font);
				float height = font.getSize2D();
//				if(clinic_info != null){
//					g2.drawString("您好！您的排队号：", 0, (float)(1*height));
//					g2.translate(0, (float)(1*height+15));
//					
//					font = new Font("新宋体", Font.PLAIN, 20);
//					g2.setFont(font);
//					height = font.getSize2D();
//					g2.drawString(clinic_info, 10, (float)(1*height));
//					g2.drawLine(0, (int)(1*height+15), 200,(int)(1*height+15));
//					g2.translate(0, (float)(1*height+15));
//					
//					font = new Font("新宋体", Font.PLAIN, 13);
//					g2.setFont(font);
//					height = font.getSize2D();
//					g2.drawString("您所在的排队队列前还", 0, (float)(8+1*height));
//					g2.drawString("有" + wait_num + "人等候", 0, (float)(10+2*height));
//					g2.translate(0, (float)(10+2*height+8));
//					
//					font = new Font("新宋体", Font.PLAIN, 10);
//					g2.setFont(font);
//					height = font.getSize2D();
//					g2.drawString("请耐心留意叫号，过号作废", 0, (float)(1*height));
//					g2.translate(0, (float)(1*height));
//				}
				
				if(para != null){
					if(clinic_info != null){
						g2.drawLine(0, 10, 200,10);
					}
					font = new Font("新宋体", Font.PLAIN, 13);
					g2.setFont(font);
					height = font.getSize2D();
					g2.drawString("您的测量数据结果如下：", 0, (float)(18+1*height));
					g2.drawLine(0, (int)(1*height+31), 200,(int)(1*height+32));
					g2.translate(0, (float)(31+1*height+8));
					
					font = new Font("新宋体", Font.PLAIN, 10);
					g2.setFont(font);
					height = font.getSize2D();
					
					if(para[0] != null){
						g2.drawString("身高:"+para[0]+"cm    体重:"+para[1]+"kg", 0, (float)(1*height));
						g2.translate(0, (float)(1*height));
					}else{
						if(para[1] != null){
							g2.drawString("体重:"+para[1]+"kg", 0, (float)(1*height));
							g2.translate(0, (float)(1*height));
						}
					}
					
//					g2.drawString("左脚长:"+para[2]+"mm 右脚长:"+para[3]+"mm", 0, (float)(1*height+3));
//					g2.drawString("左脚宽:"+para[4]+"mm 右脚宽:"+para[5]+"mm", 0, (float)(2*height+6));
//					
//					g2.drawString("左足弓:"+para[6]+"mm 右足弓:"+para[7]+"mm", 0, (float)(3*height+8));
//					g2.drawString("左足态:"+para[8]+" 右足态:"+para[9], 0, (float)(4*height+10));
					
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
					String leftStatusString = para[8].replaceAll("足弓", "");
					String rightStatusString = para[9].replaceAll("足弓", "");
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
					g2.drawString("左脚长:"+leftLengthString+"右脚长:"+rightLengthString+"mm", 0, (float)(1*height+3));
					g2.drawString("左脚宽:"+leftWidthString+"右脚宽:"+rightWidthString+"mm", 0, (float)(2*height+6));
					
					g2.drawString("左足弓:"+leftArrowString+"右足弓:"+rightArrowtring+"mm", 0, (float)(3*height+8));
					g2.drawString("左足态:"+leftStatusString+"   右足态:"+rightStatusString, 0, (float)(4*height+10));
					
					if(qrcode == null){
						g2.drawString("请在公众号上查询更多信息", 0, (float)(5*height+14));
						g2.translate(0, (float)(5*height+14));
					}else{
						g2.drawString("请关注公众号获取更多信息", 0, (float)(5*height+14));
						g2.translate(0, (float)(5*height+14));
						Image src = Toolkit.getDefaultToolkit().getImage(qrcode);
						g2.drawImage(src,15,1,120,120,null);
						g2.translate(0, 121);
					
//						提示关注文字在二维码下方
//						g2.translate(0, (float)(3*height+6));
//						Image src = Toolkit.getDefaultToolkit().getImage(qrcode);
//						g2.drawImage(src,25,1,100,100,null);
//						g2.drawString("请关注公众号获取更多信息", 10, 103);
//						g2.translate(0, 103);
					}
				}
				
				font = new Font("新宋体", Font.PLAIN, 10);
				g2.setFont(font);
				height = font.getSize2D();
				g2.drawString("时间：" + DateUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"), 0, 35);
				g2.drawString("请妥善保管本凭条", 25, 35+height+4);
				
				return PAGE_EXISTS;
			default:
				return NO_SUCH_PAGE;
		}
	}

}