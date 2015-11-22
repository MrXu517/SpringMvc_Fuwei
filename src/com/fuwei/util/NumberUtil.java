package com.fuwei.util;

public class NumberUtil {
	public static double formateDouble(double value,int fixed){
		int sep = 1;
		for(int i = 0 ; i < fixed ; ++i){
			sep *= 10;
		}
		return (Math.round(value*100)/100.0);
	}
//	public static double percent(double value,int fixed){
//		double temp = value * 100;
//		int sep = 1;
//		for(int i = 0 ; i < fixed ; ++i){
//			sep *= 10;
//		}
//		return (double) (Math.round(temp*sep)/(double)sep);
//	}
	
	public static int ceil(double value){
		return (int)Math.ceil(value);
	}
	
	public static String appendZero(int num,int total){
		return String.format("%0"+total+"d",num);
	}
}
