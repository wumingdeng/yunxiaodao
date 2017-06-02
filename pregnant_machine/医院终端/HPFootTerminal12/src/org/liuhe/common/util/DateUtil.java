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
	     ������			����	     
	     dateToStr    	��ʱ���ʽʱ��ת��Ϊ�ַ���
	     strToDate		��ʱ���ʽ�ַ���ת��Ϊʱ��
	     getDays		�ַ�����ʽʱ�����������
	     getDevDay		�ַ�����ʽʱ�������	     
	 *********************************************/

	/**
	 * ��ʱ���ʽʱ��ת��Ϊ�ַ���
	 * @param dateDate  ʱ�� ����dateDate="2009-07-24"  datestyle="yyyy-MM-dd"
	 * @param datestyle ������ʾ��ʽ���� yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * //format: "yyyyMMddHHmmss" "yyyy-MM-dd HH:mm:ss"
	 * @return �ַ�����ʽʱ��
	 */
	public static String dateToStr(Date dateDate, String datestyle) {
		SimpleDateFormat formatter = new SimpleDateFormat(datestyle);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * ��ʱ���ʽ�ַ���ת��Ϊʱ�� 
	 *
	 * @param strDate ʱ���ʽ�ַ���
	 * @param datestyle ������ʾ��ʽ���� yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return ʱ��
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
	 * �ַ�����ʽʱ����������� 
	 * @param sdate  ������ 
	 * @param edate  ����
	 * @param style  ������ʾ��ʽ���� yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return ����
	 */
	public static long getDays(String sdate, String edate, String style) throws Exception {
		Date sd = strToDate(sdate, style);
		Date ed = strToDate(edate, style);
		long ret = (ed.getTime() - sd.getTime()) / (3600 * 24 * 1000);
		return ret;
	}
	
	/**
	 * �ַ�����ʽʱ������� 
	 * @param strdate  ʱ�� 
	 * @param dd  ����
	 * @param style  ʱ����ʾ��ʽ���� yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return ���������������
	 */
	public static String getDevDay(String strdate, String style, int dd) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat(style);
		Date sd = strToDate(strdate, style);
		Calendar c = Calendar.getInstance();
		c.setTime(sd);//ȡ��ǰʱ��
		c.add(Calendar.DAY_OF_YEAR, -dd); //���ڼ�����
		String str = format1.format(c.getTime());
		return str;
	}
	
	/**
	 * �ַ�����ʽʱ������� 
	 * @param strdate  ʱ�� 
	 * @param dd  ����
	 * @param style  ʱ����ʾ��ʽ���� yyyyMMdd,yyyy-MM-dd,yyyy/MM/dd  hh:mm:ss
	 * @return ���������������
	 */
	public static String getAddDay(String strdate, String style, int dd) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat(style);
		Date sd = strToDate(strdate, style);
		Calendar c = Calendar.getInstance();
		c.setTime(sd);//ȡ��ǰʱ��
		c.add(Calendar.DAY_OF_YEAR, dd); //���ڼ�����
		String str = format1.format(c.getTime());
		return str;
	}
	
	/**
	 * ��õ�ǰʱ���������
	 * ���ء�����-��-��
	 * @return
	 */
	public String getDate() {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
		return getYear(calendar) + "-" + getStrMonth(calendar) + "-"
				+ getStrDayOfMonth(calendar);
	}

	/**
	 * ���ص�ǰʱ�����
	 * @return ��
	 */
	public int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * ���ص�ǰʱ����·ݣ���ʽΪ2Ϊ��������ǰ�油0
	 * @param calendar ����ʵ��
	 * @return �·��ַ���
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
	 * ���ص�ǰʱ�����ֵ
	 * @param calendar ����ʵ��
	 * @return ��
	 */
	public int getMonthInt(Calendar calendar) {
		return 1 + calendar.get(Calendar.MONTH);
	}

	/**
	 * ���ص�ǰʱ����һ���е���һ��
	 * @param calendar ����ʵ��
	 * @return ��
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
