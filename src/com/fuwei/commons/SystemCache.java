package com.fuwei.commons;

import java.util.HashMap;
import java.util.Map;

public class SystemCache {
	private static Map<Integer,String[]> reloginList = new HashMap<Integer,String[]>();//需要重新登录的user，以及错误信息。
	
	public static Boolean checkRelogin(Integer user_id){
		String[] errorcodes = reloginList.get(user_id);
		if(errorcodes!=null && errorcodes.length>0){
			return true;
		}
		return false;
	}
	
	public static void pushRelogin(Integer user_id,String error_code){
		String[] errorcodes = reloginList.get(user_id);
		if(errorcodes==null){
			errorcodes = new String[]{};
		}
		errorcodes[error_code.length()] = error_code;
		reloginList.put(user_id, errorcodes);
	}
	
	public static void removeRelogin(Integer user_id){
		String[] errorcodes = reloginList.get(user_id);
		if(errorcodes!=null && errorcodes.length>0){
			reloginList.remove(user_id);
		}
	}
}
