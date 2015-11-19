package com.fuwei.constant;

public class Constants {

	public static final String COOKIE_RecentViewGoods = "RecentViewGoods";

	public static final int RecentViewGoodsNum = 5;

	public final static String LOGIN_SESSION_NAME = "B2C_USER";

	public final static String CART_SESSION_NAME = "B2C_CART";
	
	public final static String CODE_SESSION_NAME = "B2C_CODE";

	/**
	 * Session scope attribute that holds the locale set by the user. By setting
	 * this key to the same one that Struts uses, we get synchronization in
	 * Struts w/o having to do extra work or have two session-level variables.
	 */
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";

	// 个人用户
	public static final int ROLE_B2C = 1;
	// 个人用户
	public static final int ROLE_B2B = 2;
	// 企业
	public static final int ROLE_COMPANY = 3;

	public static final int ORDER_STATUS_QR = 1;

	public static final int ORDER_STATUS_QRZFFS = 2;

	public static final int ORDER_STATUS_FH = 3;

	public static final int ORDER_STATUS_FK = 4;

	public static final int ORDER_STATUS_SH = 5;

	public static final int ORDER_STATUS_PJ = 6;

	public static final int ORDER_STATUS_GB = 7;

	public static final int ORDER_TYPE_BUY = 1;

	public static final int ORDER_TYPE_TH = 2;

	public static final int ORDER_TYPE_HH = 3;
	
	public static final String UPLOADSite = "c:/";//上传样品图片的路径
	public static final String UPLOADIMGPATH = "resource.fuwei.com/images/";//上传样品图片的路径
	public static final String UPLOADEXCEL = "resource.fuwei.com/excel/";//上传样品图片的路径
	
	public static final String UPLOADEXCEL_TEMP = "resource.fuwei.com/excel/temp/";//excel临时生成路径
	
	public static final String UPLOADEXCEL_QuoteOrder = "resource.fuwei.com/excel/quote/";
	public static final String UPLOADEXCEL_Sample = "resource.fuwei.com/excel/sample/";
	public static final String UPLOADEXCEL_Sample_temp = "resource.fuwei.com/excel/sample/temp/";
	public static final String UPLOADIMGPATH_Sample = "resource.fuwei.com/images/sample/";
	public static final String UPLOADIMGPATH_Sample_SS = "resource.fuwei.com/images/sample/ss/";
	public static final String UPLOADIMGPATH_Sample_S = "resource.fuwei.com/images/sample/s/";
	
	public static final String UPLOADEXCEL_Order_temp = "resource.fuwei.com/excel/order/temp/";
	
	public static final String LOGIN_URL = "/login.jsp";
	
	public static final int MAX_DETAIL_LENGTH = 6;//辅料单、采购单、染色单最多可填一条明细
	
	//2015-6-27添加 装箱单存放的path
	public static final String UPLOADEXCEL_Packing = "resource.fuwei.com/excel/packing/";
	public static final String UPLOADPDF_Packing = "resource.fuwei.com/pdf/packing/";
	
	//2015-11-6添加存放辅料图片
	public static final String UPLOADIMGPATH_fuliao = "resource.fuwei.com/images/fuliao/";
	public static final String UPLOADIMGPATH_fuliao_SS = "resource.fuwei.com/images/fuliao/ss/";
	public static final String UPLOADIMGPATH_fuliao_S = "resource.fuwei.com/images/fuliao/s/";
}
