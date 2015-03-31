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
import com.fuwei.entity.Customer;
import com.fuwei.entity.Factory;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Material;
import com.fuwei.entity.Role;
import com.fuwei.entity.Salesman;
import com.fuwei.entity.User;
import com.fuwei.service.CompanyService;
import com.fuwei.service.CustomerService;
import com.fuwei.service.FactoryService;
import com.fuwei.service.GongXuService;
import com.fuwei.service.MaterialService;
import com.fuwei.service.RoleService;
import com.fuwei.service.SalesmanService;
import com.fuwei.service.UserService;

public class SystemCache {

	CompanyService companyService;

	UserService userService;

	SalesmanService salesmanService;

	GongXuService gongXuService;

	RoleService roleService;
	
	FactoryService factoryService;
	
	MaterialService materialService;
	
	CustomerService customerService;
	
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
		factoryService = (FactoryService)SystemContextUtils
		.getBean(FactoryService.class);
		materialService = (MaterialService)SystemContextUtils
		.getBean(MaterialService.class);
		customerService = (CustomerService)SystemContextUtils
		.getBean(CustomerService.class);
	}


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

	//缓存加工工厂
	public static List<Factory> factorylist = new ArrayList<Factory>();
	public static List<Factory> purchase_factorylist = new ArrayList<Factory>();
	public static List<Factory> coloring_factorylist = new ArrayList<Factory>();
	public static List<Factory> produce_factorylist = new ArrayList<Factory>();
	
	//缓存材料
	public static List<Material> materiallist = new ArrayList<Material>();
	
	//缓存客户
	public static List<Customer> customerlist = new ArrayList<Customer>();

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
		initFactoryList();
		initMaterialList();
		initCustomerList();
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
	
	public void initFactoryList() throws Exception {
		SystemCache.factorylist = factoryService.getList(); // userlist;
		for (int i = 0; i < SystemCache.factorylist.size(); ++i) {
			Factory temp = SystemCache.factorylist.get(i);
			Integer type = temp.getType();
			if(type == null || type == 0){
				SystemCache.produce_factorylist.add(temp);
			}
			if(type == 1){
				SystemCache.purchase_factorylist.add(temp);
			}
			if(type == 2){
				SystemCache.coloring_factorylist.add(temp);
			}
		}
		
	}
	public void initMaterialList() throws Exception {
		SystemCache.materiallist = materialService.getList(); // userlist;
	}
	public void initCustomerList() throws Exception {
		SystemCache.customerlist = customerService.getList(); // customerlist;
	}
	
	public static String getCustomerName(Integer customerId) {
		if(customerId == null){
			return "";
		}
		for (int i = 0; i < SystemCache.customerlist.size(); ++i) {
			Customer temp = SystemCache.customerlist.get(i);
			if (temp.getId() == customerId) {
				return temp.getName();
			}
		}
		return "";
	}
	
	public static Customer getCustomer(Integer customerId) {
		if(customerId == null){
			return null;
		}
		for (int i = 0; i < SystemCache.customerlist.size(); ++i) {
			Customer temp = SystemCache.customerlist.get(i);
			if (temp.getId() == customerId) {
				return temp;
			}
		}
		return null;
	}
	
	public static String getMaterialName(Integer materialId) {
		if(materialId==null){
			return "";
		}
		for (int i = 0; i < SystemCache.materiallist.size(); ++i) {
			Material temp = SystemCache.materiallist.get(i);
			if (temp.getId() == materialId) {
				return temp.getName();
			}
		}
		return "";
	}
	
	public static Material getMaterial(Integer materialId) {
		if(materialId==null){
			return null;
		}
		for (int i = 0; i < SystemCache.materiallist.size(); ++i) {
			Material temp = SystemCache.materiallist.get(i);
			if (temp.getId() == materialId) {
				return temp;
			}
		}
		return null;
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
	
	public static Factory getFactory(int factoryId) {

		for (int i = 0; i < SystemCache.factorylist.size(); ++i) {
			Factory temp = SystemCache.factorylist.get(i);
			if (temp.getId() == factoryId) {
				return temp;
			}
		}
		return null;
	}
	
	public static String getFactoryName(int factoryId) {

		for (int i = 0; i < SystemCache.factorylist.size(); ++i) {
			Factory temp = SystemCache.factorylist.get(i);
			if (temp.getId() == factoryId) {
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
	
	public static String getRoleName(int roleId) {

		for (int i = 0; i < SystemCache.rolelist.size(); ++i) {
			Role temp = SystemCache.rolelist.get(i);
			if (temp.getId() == roleId) {
				return temp.getName();
			}
		}
		return "";
	}
	
	public static Role getRole(int roleId) {

		for (int i = 0; i < SystemCache.rolelist.size(); ++i) {
			Role temp = SystemCache.rolelist.get(i);
			if (temp.getId() == roleId) {
				return temp;
			}
		}
		return null;
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
