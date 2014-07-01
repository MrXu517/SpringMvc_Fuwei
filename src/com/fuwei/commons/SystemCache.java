package com.fuwei.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fuwei.entity.Company;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Role;
import com.fuwei.entity.Salesman;
import com.fuwei.entity.User;
import com.fuwei.service.CompanyService;
import com.fuwei.service.GongXuService;
import com.fuwei.service.RoleService;
import com.fuwei.service.SalesmanService;
import com.fuwei.service.UserService;

public class SystemCache {
	
	CompanyService companyService;
	
	UserService userService;
	
	SalesmanService salesmanService;
	
	GongXuService gongXuService;
	
	RoleService roleService;
	
	public SystemCache(){
		companyService = (CompanyService)SystemContextUtils.getBean(CompanyService.class);
		userService = (UserService)SystemContextUtils.getBean(UserService.class);
		salesmanService = (SalesmanService)SystemContextUtils.getBean(SalesmanService.class);
		gongXuService = (GongXuService)SystemContextUtils.getBean(GongXuService.class);
		roleService = (RoleService)SystemContextUtils.getBean(RoleService.class);
	}
	//缓存需要重新登录的用户
	public static Map<Integer,String[]> reloginList = new HashMap<Integer,String[]>();//需要重新登录的user，以及错误信息。
	
	//缓存公司
	public static List<Company> companylist = new ArrayList<Company>();
	
	//缓存工艺
	public static List<GongXu> gongxulist = new ArrayList<GongXu>();
	
	//缓存用户
	public static List<User> userlist = new ArrayList<User>();
	
	//缓存业务员
	public static List<Salesman> salesmanlist = new ArrayList<Salesman>();
	
	//缓存角色
	public static List<Role> rolelist = new ArrayList<Role>();
	
	
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
	
	public void init() throws Exception{
		initCompanyList();
		initSalesmanList();
		initGongxuList();
		initUserList();
		initRoleList();
	}
	
	public void reload() throws Exception{
		init();
	}
	
	public void initCompanyList() throws Exception{
		SystemCache.companylist = companyService.getList(); //companylist;
	}
	
	public void initSalesmanList() throws Exception{
		SystemCache.salesmanlist =  salesmanService.getList();//salesmanlist;
	}
	
	public void initGongxuList() throws Exception{
		SystemCache.gongxulist = gongXuService.getList(); //gongxulist;
	}
	
	public void initUserList() throws Exception{
		SystemCache.userlist = userService.getList(); //userlist;
	}
	
	public void initRoleList() throws Exception{
		SystemCache.rolelist = roleService.getList(); //userlist;
	}
}
