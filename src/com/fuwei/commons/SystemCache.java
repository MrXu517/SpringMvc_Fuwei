package com.fuwei.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fuwei.entity.Company;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Salesman;

public class SystemCache {
	//缓存需要重新登录的用户
	private static Map<Integer,String[]> reloginList = new HashMap<Integer,String[]>();//需要重新登录的user，以及错误信息。
	
	//缓存公司
	private static List<Company> companylist = new ArrayList<Company>();
	
	//缓存工艺
	private static List<GongXu> gongxulist = new ArrayList<GongXu>();
	
	//缓存用户
	//private static List<User> userlist = new ArrayList<GongXu>();
	
	//缓存业务员
	private static List<Salesman> salesmanlist = new ArrayList<Salesman>();
	
	
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

	public static void addCompany(Company company){
		companylist.add(company);
	}
	public static void updateCompany(Company company){
		companylist.add(company);
	}
	public static void removeCompany(Company company){
		companylist.remove(company);
	}
	
}
