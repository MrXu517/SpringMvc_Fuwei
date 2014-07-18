package com.fuwei.util;

public class CreateNumberUtil {
	//第4位1代表报价单的编号
	/**
	 * 第4位1代表报价单的编号
	 * 
	 */
	public static String createQuoteOrderNumber(int id) {
		StringBuffer stringBuffer = new StringBuffer();
		char c = 'A';
		int number = id / 9999;
		c += number;
		stringBuffer.append("FW");
		stringBuffer.append(c);
		stringBuffer.append("1");
		String result = id % 9999 + "";
		switch (result.length()) {
		case 1:
			stringBuffer.append("000"+result);
			break;
		case 2:
			stringBuffer.append("00"+result);
			break;
		case 3:
			stringBuffer.append("0"+result);
			break;
		case 4:
			stringBuffer.append(result);
			break;

		default:
			stringBuffer.append("0000"+result);
			break;
		}
		return stringBuffer.toString();
	}
	//第4位2代表  订单号
	/**
	 * 第4位2代表  订单号
	 */
	public static String createFWStyleNumber(int id){
		StringBuffer stringBuffer = new StringBuffer();
		char c = 'A';
		int number = id / 9999;
		c += number;
		stringBuffer.append("FW");
		stringBuffer.append(c);
		stringBuffer.append("2");
		String result = id % 9999 + "";
		switch (result.length()) {
		case 1:
			stringBuffer.append("000"+result);
			break;
		case 2:
			stringBuffer.append("00"+result);
			break;
		case 3:
			stringBuffer.append("0"+result);
			break;
		case 4:
			stringBuffer.append(result);
			break;

		default:
			stringBuffer.append("0000"+result);
			break;
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 *第4位为3代表工厂样品款号
	 */
	//第4位为3代表工厂样品款号
	public static String createSampleProductNumber(int id){//id:sample主键
		StringBuffer stringBuffer = new StringBuffer();
		char c = 'A';
		int number = id / 9999;
		c += number;
		stringBuffer.append("FW");
		stringBuffer.append(c);
		stringBuffer.append("3");
		String result = id % 9999 + "";
		switch (result.length()) {
		case 1:
			stringBuffer.append("000"+result);
			break;
		case 2:
			stringBuffer.append("00"+result);
			break;
		case 3:
			stringBuffer.append("0"+result);
			break;
		case 4:
			stringBuffer.append(result);
			break;

		default:
			stringBuffer.append("0000"+result);
			break;
		}
		return stringBuffer.toString();
	}
	
	
	//第4位为4代表生产通知单
	public static String createProductNotificationNumber(int id){
		StringBuffer stringBuffer = new StringBuffer();
		char c = 'A';
		int number = id / 9999;
		c += number;
		stringBuffer.append("FW");
		stringBuffer.append(c);
		stringBuffer.append("3");
		String result = id % 9999 + "";
		switch (result.length()) {
		case 1:
			stringBuffer.append("000"+result);
			break;
		case 2:
			stringBuffer.append("00"+result);
			break;
		case 3:
			stringBuffer.append("0"+result);
			break;
		case 4:
			stringBuffer.append(result);
			break;

		default:
			stringBuffer.append("0000"+result);
			break;
		}
		return stringBuffer.toString();
	}
}
