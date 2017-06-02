package org.liuhe.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;

public class ScanUtil {
	public static boolean testScan(String sourceName){
		try {
			//Source source = SourceManager.instance().getDefaultSource();
			//Source source = SourceManager.instance().selectSourceByName("Founder AnyScan Z3000");
			//String default_source = SourceManager.instance().getDefaultSource().getSourceName();
			Source source = null;
			if(sourceName!=null){
				source = SourceManager.instance().selectSourceByName(sourceName);
			}else{
				source = SourceManager.instance().selectSourceUI();
			}
			if(source == null) {
				return false;
			}
			System.out.println("sourcename:"+source.getSourceName());
			source.open();
			source.setUIEnabled(false);
			//source.setRegion(0, 0, source.getPhyscialWidth()/2, source.getPhysicalHeight()/2);//…®√Ë≤ø∑÷“≥√Ê
			System.out.println("physcial width:"+source.getPhyscialWidth()+",height:"+source.getPhysicalHeight());

			source.setXResolution(100);
			source.setYResolution(100);
			source.setPixelType(2);
			
			BufferedImage buf = source.acquireImageAsBufferedImage();
	        try {
	            ImageIO.write(buf,"jpg",new File(System.getProperty("user.dir")+"/picture/"+"scan_test.jpg")); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        }
	        return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally{  
			SourceManager.closeSourceManager();
		}
	}
}