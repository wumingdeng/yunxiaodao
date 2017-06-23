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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;

import org.liuhe.main.MainJFrame;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PrintUtil implements Printable{
	private String clinic_info = null;
	private String wait_num = null;
	private String[] para = null;
	private String qrcode = null;

	/**
	 * 传递将要打印的参数
	 * @param clinic_info 专科+号码
	 * @param wait_num 等待人数
	 * */
	public void setCinicInfo(String clinic_info, String wait_num){
		this.clinic_info = clinic_info;
		this.wait_num = wait_num;
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
	
	public void genReport(){
		 String fileName = MainJFrame.scanExternProgramDir+"report_template\\2.pdf"; 
		 try {
			 PdfReader reader = new PdfReader(fileName);
			 PdfStamper ps = new PdfStamper(reader, 
					   new FileOutputStream(MainJFrame.scanExternProgramDir+"current_report.pdf"),'\0');
			 AcroFields s = ps.getAcroFields();
			 String[] str = {"123456789","2","3","1994-00-00",
						"130222111133338888"
						,"4","ssdsa","42","222","333","666"};
		   int i = 0;
		   java.util.Iterator<String> it = s.getFields().keySet().iterator();
		    while(it.hasNext()){
		    	String name = it.next().toString();
		    	s.setField(name, str[i++]);
		    	System.out.println(name);
			    	if(i>=str.length){
			    		break;
			    	}
		    	System.out.println(s.getField(name));
		    }
		    com.itextpdf.text.Image gif = com.itextpdf.text.Image.getInstance(MainJFrame.scanExternProgramDir+"fin.jpg");
			gif.setDpi(100, 100);
			gif.setBorderWidth(200);
			gif.scaleAbsolute(80, 100); 
			gif.setAbsolutePosition(400, 700);
			PdfContentByte over = ps.getOverContent(1);
			over.addImage(gif);
			   
			ps.setFormFlattening(true);
			s.setGenerateAppearances(true);
			ps.close();
			reader.close();
		 } catch (FileNotFoundException e) {
		   e.printStackTrace();
		 } catch (Exception e) {
		   e.printStackTrace();
		 } finally {
//			    doc.close();
		 }
	}
	
	public void doPrintReport(String reportPrinterName){
		//这是要打印文件的格式，如果是PDF文档要设为自动识别
		DocFlavor fileFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		
		//生成一个打印属性设置对象
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(1));//Copies-打印份数5份
		aset.add(MediaSizeName.ISO_A4);//A4纸张
		aset.add(Sides.ONE_SIDED);//双面打印
		//得到当前机器上所有已经安装的打印机
		//传入的参数是文档格式跟打印属性，只有支持这个格式与属性的打印机才会被找到
		PrintService[] services = PrintServiceLookup.lookupPrintServices(fileFormat, aset);
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
			Doc myDoc = new SimpleDoc(fiStream, fileFormat, null);  
			for(int i=0;i<services.length;i++){
				if(services[i].getName().equals(reportPrinterName)){
					//用打印服务生成一个文档打印任务，这里用的是第一个服务
				    //也可以进行筛选，services[i].getName()可以得到打印机名称，可用名称进行比较得到自己想要的打印机
					DocPrintJob job = services[i].createPrintJob();
			        try {
			            //最后一步，执行打印文档任务，传入的参数是Doc文档类，与属性(5份，双面,A4)
			            job.print(myDoc, aset);//成功后电脑会提示已有文档添加到打印队列
			        } catch (PrintException pe) {}
			        break;
				}
			}
		} 
	}
	
	/*public static void main(String[] args){
		PrintUtil util = new PrintUtil();
//		util.setCinicInfo("妇产科"+"  "+"079", "25");
		util.setCinicInfo(null, null);
//		util.setFeetPara(new String[]{"164.5","53.3","241.36","240.12","90.20","91.13","156.31","156.96","扁平趋势","正常足弓"});
		util.setFeetPara(new String[]{null,null,"241.36","240.12","90.20","91.13","156.31","156.96","扁平趋势","正常足弓"});
//		util.setFeetPara(null);
//		util.setQrcode("E:\\Javadata\\HPFootTerminal07\\picture\\qrcode_para.jpg");
		util.setQrcode(null);
		util.printpaper();
	}*/
	
	public void printpaper() {		
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
		book.append(this, pageFormat);
		printerJob.setPageable(book);
//		boolean doPrint = printerJob.printDialog();
//		if (doPrint) {
			try {
				printerJob.print();
			} catch (PrinterException exception) {
				System.err.println("Printing error: " + exception);
			}
//		}
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
					
					String leftLengthString = para[2].substring(0, para[2].indexOf("."))+"mm";
					String rightLengthString = para[3].substring(0, para[3].indexOf("."));
					String leftWidthString = para[4].substring(0, para[4].indexOf("."))+"mm";
					String rightWidthString = para[5].substring(0, para[5].indexOf("."));
					String leftArrowString = para[6].substring(0, para[6].indexOf("."))+"mm";
					String rightArrowtring = para[7].substring(0, para[7].indexOf("."));
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