package org.liuhe.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	
	
//	private static String dateToStr(String format) {
//		//format: "yyyyMMddHHmmss" "yyyy-MM-dd HH:mm:ss"
//		SimpleDateFormat formatter = new SimpleDateFormat(format);
//		String dateString = formatter.format(new Date());
//		return dateString;
//	}
	/*******************************************
	     函数名			作用	     
	     dateToStr    	将时间格式时间转换为字符串
	     strToDate		将时间格式字符串转换为时间
	     getDays		字符串格式时间相减得天数
	     getDevDay		字符串格式时间减天数	     
	 *********************************************/

	/**
	 * 将时间格式时间转换为字符串
	 * @param dateDate  时间 例：dateDate="2009-07-24"  datestyle="yyyy-MM-dd"
	 * @param datestyle 日期显示样式例如 yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * //format: "yyyyMMddHHmmss" "yyyy-MM-dd HH:mm:ss"
	 * @return 字符串格式时间
	 */
	public static String dateToStr(Date dateDate, String datestyle) {
		SimpleDateFormat formatter = new SimpleDateFormat(datestyle);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将时间格式字符串转换为时间 
	 *
	 * @param strDate 时间格式字符串
	 * @param datestyle 日期显示样式例如 yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return 时间
	 */
	public static Date strToDate(String strDate, String datestyle) {
		SimpleDateFormat formatter = new SimpleDateFormat(datestyle);
		Date strtodate = null;
		try {
			strtodate = formatter.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strtodate;
	}

	/**
	 * 字符串格式时间相减得天数 
	 * @param sdate  被减数 
	 * @param edate  减数
	 * @param style  日期显示样式例如 yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return 天数
	 */
	public static long getDays(String sdate, String edate, String style) throws Exception {
		Date sd = strToDate(sdate, style);
		Date ed = strToDate(edate, style);
		long ret = (ed.getTime() - sd.getTime()) / (3600 * 24 * 1000);
		return ret;
	}
	
	/**
	 * 字符串格式时间减天数 
	 * @param strdate  时间 
	 * @param dd  天数
	 * @param style  时间显示样式例如 yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return 减了天数后的日期
	 */
	public static String getDevDay(String strdate, String style, int dd) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat(style);
		Date sd = strToDate(strdate, style);
		Calendar c = Calendar.getInstance();
		c.setTime(sd);//取当前时间
		c.add(Calendar.DAY_OF_YEAR, -dd); //日期减天数
		String str = format1.format(c.getTime());
		return str;
	}
	
	/**
	 * 字符串格式时间加天数 
	 * @param strdate  时间 
	 * @param dd  天数
	 * @param style  时间显示样式例如 yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return 加了天数后的日期
	 */
	public static String getAddDay(String strdate, String style, int dd) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat(style);
		Date sd = strToDate(strdate, style);
		Calendar c = Calendar.getInstance();
		c.setTime(sd);//取当前时间
		c.add(Calendar.DAY_OF_YEAR, dd); //日期加天数
		String str = format1.format(c.getTime());
		return str;
	}
	
	/**
	 * 获得当前时间的年月日
	 * 返回——年-月-日
	 * @return
	 */
	public String getDate() {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
		return getYear(calendar) + "-" + getStrMonth(calendar) + "-"
				+ getStrDayOfMonth(calendar);
	}

	/**
	 * 返回当前时间的年
	 * @return 年
	 */
	public int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回当前时间的月份，格式为2为，不足在前面补0
	 * @param calendar 日历实例
	 * @return 月份字符串
	 */
	public String getStrMonth(Calendar calendar) {
		int m = getMonthInt(calendar);
		String[] months = new String[] { "01", "02", "03", "04", "05", "06",
				"07", "08", "09", "10", "11", "12" };
		if (m > 12) {
			return "Unknown to Man";
		}
		return months[m - 1];
	}

	/**
	 * 返回当前时间的月值
	 * @param calendar 日历实例
	 * @return 月
	 */
	public int getMonthInt(Calendar calendar) {
		return 1 + calendar.get(Calendar.MONTH);
	}

	/**
	 * 返回当前时间是一月中的哪一天
	 * @param calendar 日历实例
	 * @return 日
	 */
	public String getStrDayOfMonth(Calendar calendar) {
		int i = calendar.get(Calendar.DAY_OF_MONTH);
		String s = i + "";
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

}
