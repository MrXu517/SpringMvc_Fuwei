package com.fuwei.constant;

import java.util.Calendar;
import java.util.Date;

public class Holiday {
	public static final MyDate[] holidays = {new MyDate(2014,4,5),
		new MyDate(2014,5,1), new MyDate(2014,6,2),
		new MyDate(2014,9,8), 
		new MyDate(2014,10,1), new MyDate(2014,10,2),
		new MyDate(2014,10,3),
		new MyDate(2015, 1, 1),
		new MyDate(2015,2,19), new MyDate(2015,2,20),
		new MyDate(2015,2,21), new MyDate(2015,4,5),
		new MyDate(2015,5,1), new MyDate(2015,6,20),
		new MyDate(2015,9,3), new MyDate(2015,9,27),
		new MyDate(2015,10,1), new MyDate(2015,10,2),
		new MyDate(2015,10,3) ,
		new MyDate(2016,1,1),new MyDate(2016,2,7),new MyDate(2016,2,8),new MyDate(2016,2,9),new MyDate(2016,2,10),new MyDate(2016,2,11),new MyDate(2016,2,12),new MyDate(2016,2,13),
		new MyDate(2016,4,4),new MyDate(2016,5,1),new MyDate(2016,6,9),new MyDate(2016,9,15),
		new MyDate(2016,10,1),new MyDate(2016,10,2),new MyDate(2016,10,3),new MyDate(2017,1,1),new MyDate(2017,1,27),new MyDate(2017,1,28),new MyDate(2017,1,29),new MyDate(2017,1,30),
		new MyDate(2017,1,31),new MyDate(2017,2,1),new MyDate(2017,2,2),new MyDate(2017,4,4),new MyDate(2017,5,1),new MyDate(2017,5,30),new MyDate(2017,10,1),new MyDate(2017,10,2),new MyDate(2017,10,3),new MyDate(2017,10,4),
		new MyDate(2018,1,1)
	};


	public static int getHoliday(int year , int month) {
		int count = 0 ;
		for(MyDate date : holidays){
			if(date.getYear() == year && date.getMonth() == month){
				count++;
			}
		}
		return count;
	}

	public static int getHoliday(int year ,int month , Date leave_at){
		if(leave_at == null){
			return getHoliday(year,month);
		}
		int count = 0 ;
		Calendar cal = Calendar.getInstance();
		cal.setTime(leave_at);
		int leave_year = cal.get(Calendar.YEAR); 
		int leave_month = cal.get(Calendar.MONTH) + 1; 
		int leave_day = cal.get(Calendar.DAY_OF_MONTH); 
		
		if(leave_year != year || leave_month!=month){
			return getHoliday(year,month);
		}
		for(MyDate date : holidays){
			if(date.getYear() == year && date.getMonth() == month && leave_day>=date.getDay()){
				count++;
			}
		}
		return count;
	}
	
	public static boolean isHoliday(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		for(MyDate mydate : holidays){
			if(mydate.getYear() == cal.get(Calendar.YEAR) && mydate.getMonth() == cal.get(Calendar.MONTH) && mydate.getDay() == cal.get(Calendar.DAY_OF_MONTH)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isHoliday(Calendar cal){
		for(MyDate mydate : holidays){
			if(mydate.getYear() == cal.get(Calendar.YEAR) && mydate.getMonth() == cal.get(Calendar.MONTH)+1 && mydate.getDay() == cal.get(Calendar.DAY_OF_MONTH)){
				return true;
			}
		}
		return false;
	}
}

class MyDate {
	private Integer year;
	private int month;
	private int day;

	public MyDate(Integer year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
