package com.fuwei.util;

public class NumberUtil {
	public static double formateDouble(double value,int fixed){
		int sep = 1;
		for(int i = 0 ; i < fixed ; ++i){
			sep *= 10;
		}
		return (double) (Math.round(value*100)/100.0);
	}
}
