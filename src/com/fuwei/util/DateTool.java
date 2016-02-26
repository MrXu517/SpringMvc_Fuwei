package com.fuwei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fuwei.constant.Holiday;

import java.util.Calendar;

public class DateTool {
	
	public static int nowYear() {
		Calendar a=Calendar.getInstance();
		return a.get(Calendar.YEAR);//得到年
	}
	public static Date now() {
		return new Date();
	}
	public static Date nowDate() throws ParseException {
		return parse(formatDateYMD(new Date())) ;
	}
	public static String getYear(Date date)throws ParseException {
		if(date == null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	public static String getYear2()throws ParseException {
		String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
		return yearLast;
	}
	public static String formateDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static Date parse(String strDate) throws ParseException {
		if(strDate == null || strDate == ""){
			return null;
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(strDate);
		}catch(Exception e){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.parse(strDate);
		}
	}
	
	public static Date parse(String strDate,String format) throws ParseException {
		if(strDate == null || strDate == ""){
			return null;
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(strDate);
		}catch(Exception e){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(strDate);
		}
	}
	
	public static Date addDay(Date date,int day) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	public static String formatDateYMD(Date date) throws ParseException {
		if(date == null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	public static String formatDateYMD(Date date,String sep) throws ParseException {
		if(date == null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+sep+"MM"+sep+"dd");
		return sdf.format(date);
	}
	
	public static String formateDate(Date date,String format)throws ParseException {
		if(date == null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	//验厂日期：往前推找到 最近的 不是周六和节假日的 日子
	public static Date getYanDate(Date date){		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		while(Holiday.isHoliday(cal) || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){//如果是节假日或周六
			//则往前推一天，再进行判断
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return cal.getTime();
	}
}
