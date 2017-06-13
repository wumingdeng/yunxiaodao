package org.liuhe.algorithm.scan;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;

import org.liuhe.algorithm.config.ScanConfig;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public  class AcquireHelper {
	private ScanConfig config;
	
	public AcquireHelper(ScanConfig config){
		this.config = config;
	}
	
	/**
	 * 带绝对路径，该方法只获取
	 * 扫描配置全图：scan_back.jpg
	 * 扫描校正图片：scan_adjust.jpg
	 */
	public boolean startAcquireOne(String filename){					
		Source source = null;
		try {
			source = SourceManager.instance().selectSourceByName(config.getSource());
			//source = SourceManager.instance().getDefaultSource();
			if(source == null) {
				System.out.println("Source:"+config.getSource()+" There is no (default) source on the system!");
				return false;
			}
			source.open();
			source.setUIEnabled(false);									//不显示扫描源配置界面
			source.setIndicators(false);								//不显示进度条
			source.setXResolution(Integer.parseInt(config.getDpi()));	//设置水平分辨率
			source.setYResolution(Integer.parseInt(config.getDpi()));	//设置垂直分辨率
			source.setPixelType(2);										//设置图像色彩类型:0黑白1为灰度图片 2为彩色图片;3也是彩色图片,4默认为彩色
			BufferedImage originalBufImage = source.acquireImageAsBufferedImage();
			
			BufferedImage bufImage = null;
			if(filename.endsWith("scan_back.jpg")){
				bufImage = originalBufImage;
			}else{
				int top = Integer.parseInt(config.getCut_top());
				int b02 = Integer.parseInt(config.getCut_b02());
				int left = Integer.parseInt(config.getCut_left());
				int right = Integer.parseInt(config.getCut_right());
				int width = originalBufImage.getWidth()-(left+right);
				int height = originalBufImage.getHeight()/2-(top+b02);
				
				int[] pixels = new int[width*height];
				originalBufImage.getRGB(left,top,width,height,pixels,0,width);
				bufImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
				bufImage.setRGB(0,0,width,height,pixels,0,width);
				
				ContrastFilter filter = new ContrastFilter();
				filter.setBrightness((Integer.parseInt(config.getScan_bright())+100)*1.0f/100);
				filter.setContrast((Integer.parseInt(config.getScan_constrast())+100)*1.0f/100);
				bufImage = filter.filter(bufImage, null);

				if(Boolean.parseBoolean(config.getScale_z())){
					bufImage = OverturnFilter.rotate90(bufImage);
				}
				if(Boolean.parseBoolean(config.getScale_x())){
					bufImage = OverturnFilter.turn_horizontal(bufImage);
				}
				if(Boolean.parseBoolean(config.getScale_y())){
					bufImage = OverturnFilter.turn_vertical(bufImage);
				}
			}
			saveJpgFile(bufImage,filename);
			try {
				setJpgDpi(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}catch(Exception e1) {
			e1.printStackTrace();
			return false;
		}finally{
			try{
				source.close();
				SourceManager.closeSourceManager();
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取整张的扫描图像BufferedImage
	 * 并经过亮度和对比度的处理
	 * 水平、垂直、翻转
	 * */
	public BufferedImage startAcquireOne(){
		Source source = null;
		try {
			source = SourceManager.instance().selectSourceByName(config.getSource());
			if(source == null) {
				System.out.println("Source:"+config.getSource()+" There is no (default) source on the system!");
				return null;
			}
			source.open();
			source.setUIEnabled(false);									//不显示扫描源配置界面
			source.setIndicators(false);								//不显示进度条
			source.setXResolution(Integer.parseInt(config.getDpi()));	//设置水平分辨率
			source.setYResolution(Integer.parseInt(config.getDpi()));	//设置垂直分辨率
			source.setPixelType(2);										//设置图像色彩类型
			BufferedImage bufImage = source.acquireImageAsBufferedImage();
			//对剪切后的图像进行：亮度、对比度、灰度的处理
			ContrastFilter filter = new ContrastFilter();
			filter.setBrightness((Integer.parseInt(config.getScan_bright())+100)*1.0f/100);
			filter.setContrast((Integer.parseInt(config.getScan_constrast())+100)*1.0f/100);
			bufImage = filter.filter(bufImage, null);
			//对处理过后的图像进行：倒置翻转、左右翻转、上下翻转
			if(Boolean.parseBoolean(config.getScale_z())){
				bufImage = OverturnFilter.rotate90(bufImage);
			}
			if(Boolean.parseBoolean(config.getScale_x())){
				bufImage = OverturnFilter.turn_horizontal(bufImage);
			}
			if(Boolean.parseBoolean(config.getScale_y())){
				bufImage = OverturnFilter.turn_vertical(bufImage);
			}
			return bufImage;
		}catch(Exception e1) {
			e1.printStackTrace();
			return null;
		}finally{
			try{
				source.close();
				SourceManager.closeSourceManager();
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取整张的扫描图像BufferedImage
	 * 并经过亮度和对比度的处理
	 * 水平、垂直、翻转
	 * */
	public BufferedImage startAcquireOneDir(String scanDir){
		try {
			// modify by kael
			String cmd = scanDir+"start.bat";
			ProcessBuilder pb = new ProcessBuilder(cmd,scanDir);
			Process process = pb.start();
			process.waitFor();
			System.out.println(process.exitValue()); 
			BufferedImage bimg = ImageIO.read(new File(scanDir+"/Image/fin.jpg"));
			return bimg;
			// modify by kael over
		}catch(Exception e1) {
			e1.printStackTrace();
			return null;
		}finally{
			try{
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取整张的扫描图像BufferedImage
	 * 并经过亮度和对比度的处理
	 * 水平、垂直、翻转
	 * */
	public boolean startAcquireOneDir(String scanDir,String filename){
		try {
			// modify by kael
			String cmd = scanDir+"startConfig.bat";
			ProcessBuilder pb = new ProcessBuilder(cmd,scanDir);
			Process process = pb.start();
			process.waitFor();
			System.out.println(process.exitValue()); 
			BufferedImage bimg = ImageIO.read(new File(scanDir+"/Image/fin.jpg"));
			saveJpgFile(bimg,filename);
			try {
				setJpgDpi(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			// modify by kael over
		}catch(Exception e1) {
			e1.printStackTrace();
			return false;
		}finally{
			try{
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 分割左右脚图片
	 * 根据左右脚边界获取像素域
	 * 对左右脚进行比例校正
	 * */
	public void startAcquireOne(BufferedImage bufImage,String leftFile,String rightFile){
		if(!Boolean.parseBoolean(config.getUse_right())){
			String temp;
			temp = leftFile;
			leftFile = rightFile;
			rightFile = temp;
		}
		int top = Integer.parseInt(config.getCut_top());
		int b02 = Integer.parseInt(config.getCut_b02());
		int b03 = Integer.parseInt(config.getCut_b03());
		int bottom = Integer.parseInt(config.getCut_bottom());
		int left = Integer.parseInt(config.getCut_left());
		int right = Integer.parseInt(config.getCut_right());
		
		int fullWidth = bufImage.getWidth();
		int fullHeight = bufImage.getHeight();
		if(Boolean.parseBoolean(config.getScale_z())){
			int temp;
			temp = fullWidth;
			fullWidth = fullHeight;
			fullHeight = temp;
		}
		
		System.out.println("startAcquireOne orig width:"+bufImage.getWidth()+" height:"+bufImage.getHeight());
		System.out.println("change to width:"+fullWidth+" height:"+fullHeight);
		int right_x = Integer.parseInt(config.getCut_left());
		int right_y = Integer.parseInt(config.getCut_top());
		int right_width = fullWidth-(left+right);
		int right_height = fullHeight/2-(top+b02);
		System.out.println("orig right_x:"+right_x+" right_y:"+right_y+" right_width:"+right_width+" right_height:"+right_height);
		if(Boolean.parseBoolean(config.getScale_z())){
			int temp;
			temp = right_x;
			right_x = right_y;
			right_y = temp;
			temp = right_width;
			right_width = right_height;
			right_height = temp;
		}
		if(Boolean.parseBoolean(config.getScale_x())){
			right_x = bufImage.getWidth()-right_x-right_width;
		}
		if(Boolean.parseBoolean(config.getScale_y())){
			right_y = bufImage.getHeight()-right_y-right_height;
		}
		
		int left_x = Integer.parseInt(config.getCut_left());
		int left_y = fullHeight/2+b03 ;
		int left_width = fullWidth-(left+right);
		int left_height = fullHeight/2-(bottom+b03);
		System.out.println("orig left_x:"+left_x+" left_y:"+left_y+" left_width:"+left_width+" left_height:"+left_height);
		if(Boolean.parseBoolean(config.getScale_z())){
			int temp;
			temp = left_x;
			left_x = left_y;
			left_y = temp;
			temp = left_width;
			left_width = left_height;
			left_height = temp;
		}
		if(Boolean.parseBoolean(config.getScale_x())){
			left_x = bufImage.getWidth()-left_x-left_width;
		}
		if(Boolean.parseBoolean(config.getScale_y())){
			left_y = bufImage.getHeight()-left_y-left_height;
		}
		
		System.out.println("changed right_x:"+right_x+" right_y:"+right_y+" right_width:"+right_width+" right_height:"+right_height);
		System.out.println("changed left_x:"+left_x+" left_y:"+left_y+" left_width:"+left_width+" left_height:"+left_height);
		
		int[] right_pixels = new int[right_width*right_height];
		bufImage.getRGB(right_x,right_y,right_width,right_height,right_pixels,0,right_width);
		BufferedImage rightBufImage = new BufferedImage(right_width,right_height,BufferedImage.TYPE_INT_RGB);
		rightBufImage.setRGB(0,0,right_width,right_height,right_pixels,0,right_width);
		if(Boolean.parseBoolean(config.getUse_scan())){
			rightBufImage = scaleFilter(rightBufImage, Double.parseDouble(config.getAve_x()), Double.parseDouble(config.getAve_y()));
		}
		
		saveJpgFile(rightBufImage,rightFile);
		try {
			setJpgDpi(rightFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int[] left_pixels = new int[left_width*left_height];
		bufImage.getRGB(left_x,left_y,left_width,left_height,left_pixels,0,left_width);
		BufferedImage leftBufImage = new BufferedImage(left_width,left_height,BufferedImage.TYPE_INT_RGB);
		leftBufImage.setRGB(0,0,left_width,left_height,left_pixels,0,left_width);
		if(Boolean.parseBoolean(config.getUse_scan())){
			leftBufImage = scaleFilter(leftBufImage, Double.parseDouble(config.getAve_x()), Double.parseDouble(config.getAve_y()));
		}
		
		saveJpgFile(leftBufImage,leftFile);
		try {
			setJpgDpi(leftFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private BufferedImage scaleFilter(BufferedImage scale_image,double scaleX,double scaleY) {
		BufferedImage filteredBufImage = new BufferedImage((int)(scale_image.getWidth()*scaleX),(int)(scale_image.getHeight()*scaleY),BufferedImage.TYPE_INT_RGB); 
		AffineTransform transform = new AffineTransform();
		transform.setToScale(scaleX, scaleY);
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);
		imageOp.filter(scale_image, filteredBufImage);
		return filteredBufImage;
	}
    
    private void saveJpgFile(BufferedImage bufImage,String filename){
		ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufImage, "jpg", imageStream);
			//ImageIO.write(BufferedImage,formatName,File);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] tagInfo = imageStream.toByteArray();
		try {
			File tofile = new File(filename);
			FileOutputStream fos = new FileOutputStream(tofile);
			fos.write(tagInfo);
			fos.close();
			imageStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}
	
    private void setJpgDpi(String filename)throws Exception{
        File infile = new File(filename);
        ImageReader reader = ImageIO.getImageReadersByFormatName("jpeg").next();
        FileImageInputStream fiis = new FileImageInputStream(infile);
        reader.setInput(fiis, true, false);
        IIOMetadata data = reader.getImageMetadata(0);
		BufferedImage image = reader.read(0);
		BufferedImage bufImage = ImageIO.read(infile); 
		int width = bufImage.getWidth();
		int height = bufImage.getHeight();
	    Image rescaled = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
	    BufferedImage output = new BufferedImage(rescaled.getWidth(null), rescaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = output.createGraphics();
        g2d.drawImage(rescaled, 0, 0, null);
        g2d.dispose();
	    
		Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
        Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
        for (int i = 0; i < jfif.getAttributes().getLength(); i++) {
            Node attribute = jfif.getAttributes().item(i);
            System.out.println(attribute.getNodeName() + "=" + attribute.getNodeValue());
        }
        fiis.close();

        File outfile = new File(filename);
 		FileOutputStream fos = new FileOutputStream(outfile);
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fos);
        JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(output);
        jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
        jpegEncodeParam.setXDensity(Integer.parseInt(config.getDpi()));
        jpegEncodeParam.setYDensity(Integer.parseInt(config.getDpi()));
        jpegEncoder.encode(output, jpegEncodeParam);
		fos.close();
		output = null;
		System.out.println("JPG图形DPI转换结束...");
    }
//end...AcquireHelper.java
}