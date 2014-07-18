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
	
	public static final String UPLOADIMGPATH = "upload/images/";//上传样品图片的路径
	public static final String UPLOADEXCEL = "upload/excel/";//上传样品图片的路径
	public static final String UPLOADIMGPATH_SS = "upload/images/ss/";
	public static final String UPLOADIMGPATH_S = "upload/images/s/";
}
