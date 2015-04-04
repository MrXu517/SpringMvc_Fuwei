package com.fuwei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import java.util.Calendar;

public class DateTool {

	public static Date now() {
		return new Date();
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
	
	public static Date addDay(Date date,int day) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	public static String formatDateYMD(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	public static String formatDateYMD(Date date,String sep) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+sep+"MM"+sep+"dd");
		return sdf.format(date);
	}
	
	public static String formateDate(Date date,String format)throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
