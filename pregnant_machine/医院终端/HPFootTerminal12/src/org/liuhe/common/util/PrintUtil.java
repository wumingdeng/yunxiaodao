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
import java.util.Date;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

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
				if(clinic_info != null){
					g2.drawString("您好！您的排队号：", 0, (float)(1*height));
					g2.translate(0, (float)(1*height+15));
					
					font = new Font("新宋体", Font.PLAIN, 20);
					g2.setFont(font);
					height = font.getSize2D();
					g2.drawString(clinic_info, 10, (float)(1*height));
					g2.drawLine(0, (int)(1*height+15), 200,(int)(1*height+15));
					g2.translate(0, (float)(1*height+15));
					
					font = new Font("新宋体", Font.PLAIN, 13);
					g2.setFont(font);
					height = font.getSize2D();
					g2.drawString("您所在的排队队列前还", 0, (float)(8+1*height));
					g2.drawString("有" + wait_num + "人等候", 0, (float)(10+2*height));
					g2.translate(0, (float)(10+2*height+8));
					
					font = new Font("新宋体", Font.PLAIN, 10);
					g2.setFont(font);
					height = font.getSize2D();
					g2.drawString("请耐心留意叫号，过号作废", 0, (float)(1*height));
					g2.translate(0, (float)(1*height));
				}
				
				if(para != null){
					if(clinic_info != null){
						g2.drawLine(0, 10, 200,10);
					}
					font = new Font("新宋体", Font.PLAIN, 13);
					g2.setFont(font);
					height = font.getSize2D();
					g2.drawString("测量数据结果如下：", 0, (float)(18+1*height));
					g2.translate(0, (float)(18+1*height+8));
					
					font = new Font("新宋体", Font.PLAIN, 10);
					g2.setFont(font);
					height = font.getSize2D();
					
					if(para[0] != null){
						g2.drawString("身高:"+para[0]+"cm  体重:"+para[1]+"kg", 0, (float)(1*height));
						g2.translate(0, (float)(1*height));
					}else{
						if(para[1] != null){
							g2.drawString("体重:"+para[1]+"kg", 0, (float)(1*height));
							g2.translate(0, (float)(1*height));
						}
					}
					
					g2.drawString("左脚长:"+para[2]+"mm 右脚长:"+para[3]+"mm", 0, (float)(1*height+3));
					g2.drawString("左脚宽:"+para[4]+"mm 右脚宽:"+para[5]+"mm", 0, (float)(2*height+6));
					
					g2.drawString("左足弓:"+para[6]+"mm 右足弓:"+para[7]+"mm", 0, (float)(3*height+8));
					g2.drawString("左足态:"+para[8]+" 右足态:"+para[9], 0, (float)(4*height+10));
					
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