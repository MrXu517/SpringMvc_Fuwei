package com.fuwei.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fuwei.entity.Authority;
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

	public SystemCache() {
		companyService = (CompanyService) SystemContextUtils
				.getBean(CompanyService.class);
		userService = (UserService) SystemContextUtils
				.getBean(UserService.class);
		salesmanService = (SalesmanService) SystemContextUtils
				.getBean(SalesmanService.class);
		gongXuService = (GongXuService) SystemContextUtils
				.getBean(GongXuService.class);
		roleService = (RoleService) SystemContextUtils
				.getBean(RoleService.class);
	}

	// 缓存需要重新登录的用户
	public static Map<Integer, String[]> reloginList = new HashMap<Integer, String[]>();// 需要重新登录的user
																						// ，
																						// 以及错误信息
																						// 。

	// 缓存公司
	public static List<Company> companylist = new ArrayList<Company>();

	// 缓存工艺
	public static List<GongXu> gongxulist = new ArrayList<GongXu>();

	// 缓存用户
	public static List<User> userlist = new ArrayList<User>();

	// 缓存业务员
	public static List<Salesman> salesmanlist = new ArrayList<Salesman>();

	// 缓存角色
	public static List<Role> rolelist = new ArrayList<Role>();

	public static Boolean checkRelogin(Integer user_id) {
		String[] errorcodes = reloginList.get(user_id);
		if (errorcodes != null && errorcodes.length > 0) {
			return true;
		}
		return false;
	}

	public static void pushRelogin(Integer user_id, String error_code) {
		String[] errorcodes = reloginList.get(user_id);
		if (errorcodes == null) {
			errorcodes = new String[] {};
		}
		errorcodes[error_code.length()] = error_code;
		reloginList.put(user_id, errorcodes);
	}

	public static void removeRelogin(Integer user_id) {
		String[] errorcodes = reloginList.get(user_id);
		if (errorcodes != null && errorcodes.length > 0) {
			reloginList.remove(user_id);
		}
	}

	public static void addCompany(Company company) {
		companylist.add(company);
	}

	public static void updateCompany(Company company) {
		companylist.add(company);
	}

	public static void removeCompany(Company company) {
		companylist.remove(company);
	}

	public void init() throws Exception {
		initCompanyList();
		initSalesmanList();
		initGongxuList();
		initUserList();
		initRoleList();
	}

	public void reload() throws Exception {
		init();
	}

	public void initCompanyList() throws Exception {
		SystemCache.companylist = companyService.getList(); // companylist;
	}

	public void initSalesmanList() throws Exception {
		SystemCache.salesmanlist = salesmanService.getList();// salesmanlist;
	}

	public void initGongxuList() throws Exception {
		SystemCache.gongxulist = gongXuService.getList(); // gongxulist;
	}

	public void initUserList() throws Exception {
		SystemCache.userlist = userService.getList(); // userlist;
	}

	public void initRoleList() throws Exception {
		SystemCache.rolelist = roleService.getList(); // userlist;
	}

	public static String getUserName(int userid) {

		for (int i = 0; i < SystemCache.userlist.size(); ++i) {
			User temp = SystemCache.userlist.get(i);
			if (temp.getId() == userid) {
				return temp.getName();
			}
		}
		return "";
	}

	public static String getSalesmanName(int salesmanid) {

		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
			Salesman temp = SystemCache.salesmanlist.get(i);
			if (temp.getId() == salesmanid) {
				return temp.getName();
			}
		}
		return "";
	}
	
	public static Salesman getSalesman(int salesmanid) {

		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
			Salesman temp = SystemCache.salesmanlist.get(i);
			if (temp.getId() == salesmanid) {
				return temp;
			}
		}
		return null;
	}
	
	public static String getCompanyName(int companyId) {

		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			if (temp.getId() == companyId) {
				return temp.getFullname();
			}
		}
		return "";
	}
	
	public static Company getCompanyById(int companyId) {

		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			if (temp.getId() == companyId) {
				return temp;
			}
		}
		return null;
	}
	
	public static String getGongxuName(int gongxuId) {

		for (int i = 0; i < SystemCache.gongxulist.size(); ++i) {
			GongXu temp = SystemCache.gongxulist.get(i);
			if (temp.getId() == gongxuId) {
				return temp.getName();
			}
		}
		return "";
	}
	
	public static List<Salesman> getSalesmanList(int companyId){
		List<Salesman> salemanlist = new ArrayList<Salesman>();
		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
			Salesman temp = SystemCache.salesmanlist.get(i);
			if (temp.getCompanyId() == companyId) {
				salemanlist.add(temp);
			}
		}
		return salemanlist;
	}
	
	public static HashMap<Company, List<Salesman>> getCompanySalesmanMap(){
		HashMap<Company, List<Salesman>> map = new HashMap<Company, List<Salesman>>();
		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			List<Salesman> templist = new ArrayList<Salesman>();
			for (int k = 0; k < SystemCache.salesmanlist.size(); ++k) {
				Salesman s_temp = SystemCache.salesmanlist.get(k);
				if (s_temp.getCompanyId() == temp.getId()) {
					templist.add(s_temp);
				}
			}
			map.put(temp, templist);
		}
		return map;
	}
	
	
	public static HashMap<String, List<Salesman>> getCompanySalesmanMap_ID(){
		HashMap<String, List<Salesman>> map = new HashMap<String, List<Salesman>>();
		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			List<Salesman> templist = new ArrayList<Salesman>();
			for (int k = 0; k < SystemCache.salesmanlist.size(); ++k) {
				Salesman s_temp = SystemCache.salesmanlist.get(k);
				if (s_temp.getCompanyId() == temp.getId()) {
					templist.add(s_temp);
				}
			}
			map.put(String.valueOf(temp.getId()), templist);
		}
		return map;
	}
	
	/*权限相关*/
	public static Boolean hasAuthority(HttpSession session, int authorityId){
		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
		return SystemCache.hasAuthority(loginUser, authorityId);
	}
	
	public static Boolean hasAuthority(HttpSession session,String lcode){
		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
		return SystemCache.hasAuthority(loginUser, lcode);
	}
	
	public static Boolean hasAuthority(LoginedUser loginUser, int authorityId){
		List<Authority> authorityList = loginUser.getAuthoritylist();
		if(authorityList == null || authorityList.size()<=0){
			return false;
		}
		for(Authority authority : authorityList){
			if(authority.getId() == authorityId){
				return true;
			}
		}
		return false;
	}
	
	public static Boolean hasAuthority(LoginedUser loginUser,String lcode){
		List<Authority> authorityList = loginUser.getAuthoritylist();
		if(authorityList == null || authorityList.size()<=0){
			return false;
		}
		for(Authority authority : authorityList){
			if(authority.getLcode().trim().equals(lcode.trim())){
				return true;
			}
		}
		return false;
	}
	/*权限相关*/
}
