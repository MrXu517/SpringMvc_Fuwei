package com.fuwei.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.fuwei.entity.Authority;
import com.fuwei.entity.Company;
import com.fuwei.entity.Customer;
import com.fuwei.entity.Department;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Factory;
import com.fuwei.entity.FuliaoType;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Material;
import com.fuwei.entity.Message;
import com.fuwei.entity.Role;
import com.fuwei.entity.Salesman;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Subject;
import com.fuwei.entity.producesystem.Location;
import com.fuwei.service.CompanyService;
import com.fuwei.service.CustomerService;
import com.fuwei.service.DepartmentService;
import com.fuwei.service.EmployeeService;
import com.fuwei.service.FactoryService;
import com.fuwei.service.FuliaoTypeService;
import com.fuwei.service.GongXuService;
import com.fuwei.service.MaterialService;
import com.fuwei.service.RoleService;
import com.fuwei.service.SalesmanService;
import com.fuwei.service.UserService;
import com.fuwei.service.financial.SubjectService;
import com.fuwei.service.producesystem.LocationService;

public class SystemCache {

	static CompanyService companyService;

	static UserService userService;

	static SalesmanService salesmanService;

	static GongXuService gongXuService;

	static RoleService roleService;

	static FactoryService factoryService;

	static MaterialService materialService;

	static CustomerService customerService;

	static DepartmentService departmentService;

	static EmployeeService employeeService;
	
	static SubjectService subjectService;
	
	static FuliaoTypeService fuliaoTypeService;
	
	static LocationService locationService;

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
		factoryService = (FactoryService) SystemContextUtils
				.getBean(FactoryService.class);
		materialService = (MaterialService) SystemContextUtils
				.getBean(MaterialService.class);
		customerService = (CustomerService) SystemContextUtils
				.getBean(CustomerService.class);
		departmentService = (DepartmentService) SystemContextUtils
				.getBean(DepartmentService.class);
		employeeService = (EmployeeService) SystemContextUtils
				.getBean(EmployeeService.class);
		subjectService = (SubjectService) SystemContextUtils
		.getBean(SubjectService.class);
		fuliaoTypeService = (FuliaoTypeService) SystemContextUtils
		.getBean(FuliaoTypeService.class);
		locationService = (LocationService) SystemContextUtils
		.getBean(LocationService.class);

	}

	// 缓存公司
	public static List<Company> companylist = new ArrayList<Company>();
	public static Map<Integer,String> companyMap = new HashMap<Integer, String>();
	// 缓存工艺
	public static List<GongXu> gongxulist = new ArrayList<GongXu>();
	public static GongXu producing_GONGXU = null;
	public static Map<Integer,String> gongxuMap = new HashMap<Integer, String>();

	// 缓存用户
	public static List<User> userlist = new ArrayList<User>();
	public static Map<Integer,String> userMap = new HashMap<Integer, String>();

	// 缓存业务员
	public static List<Salesman> salesmanlist = new ArrayList<Salesman>();
	public static Map<Integer,String> salesmanMap = new HashMap<Integer, String>();

	// 缓存角色
	public static List<Role> rolelist = new ArrayList<Role>();
	public static Map<Integer,String> roleMap = new HashMap<Integer, String>();

	// 缓存加工工厂
	public static List<Factory> factorylist = new ArrayList<Factory>();
	public static Map<Integer,String> factoryMap = new HashMap<Integer, String>();
	public static List<Factory> purchase_factorylist = new ArrayList<Factory>();
	public static List<Factory> coloring_factorylist = new ArrayList<Factory>();
	public static List<Factory> produce_factorylist = new ArrayList<Factory>();

	// 缓存材料
	public static List<Material> materiallist = new ArrayList<Material>();
	public static Map<Integer,String> materialMap = new HashMap<Integer, String>();

	// 缓存客户
	public static List<Customer> customerlist = new ArrayList<Customer>();
	public static Map<Integer,String> customerMap = new HashMap<Integer, String>();

	// 缓存部门
	public static List<Department> departmentlist = new ArrayList<Department>();
	public static Map<Integer,String> departmentMap = new HashMap<Integer, String>();

	// 缓存员工
	public static List<Employee> employeelist = new ArrayList<Employee>();
	public static Map<Integer,String> employeeMap = new HashMap<Integer, String>();
	
	// 缓存科目
	public static List<Subject> subjectlist = new ArrayList<Subject>();
	public static Map<Integer,String> subjectMap = new HashMap<Integer, String>();
	
	// 缓存辅料类型
	public static List<FuliaoType> fuliaotypelist = new ArrayList<FuliaoType>();
	public static Map<Integer,String> fuliaotypeMap = new HashMap<Integer, String>();
	
	// 缓存库位
	public static List<Location> locationlist = new ArrayList<Location>();
	public static Map<Integer,String> locationMap = new HashMap<Integer, String>();

	

	public static void init() throws Exception {
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
		factoryService = (FactoryService) SystemContextUtils
				.getBean(FactoryService.class);
		materialService = (MaterialService) SystemContextUtils
				.getBean(MaterialService.class);
		customerService = (CustomerService) SystemContextUtils
				.getBean(CustomerService.class);
		departmentService = (DepartmentService) SystemContextUtils
				.getBean(DepartmentService.class);
		employeeService = (EmployeeService) SystemContextUtils
				.getBean(EmployeeService.class);
		subjectService = (SubjectService) SystemContextUtils
		.getBean(SubjectService.class);
		fuliaoTypeService = (FuliaoTypeService) SystemContextUtils
		.getBean(FuliaoTypeService.class);
		locationService = (LocationService) SystemContextUtils
		.getBean(LocationService.class);
		initCompanyList();
		initSalesmanList();
		initGongxuList();
		initUserList();
		initRoleList();
		initFactoryList();
		initMaterialList();
		initCustomerList();
		initDepartmentList();
		initEmployeeList();
		initSubjectList();
		initFuliaoTypeList();
		initLocationList();
	}

	public static void reload() throws Exception {
		init();
	}
	public static void initSubjectList() throws Exception {
		SystemCache.subjectlist = subjectService.getList(); 
		SystemCache.subjectMap = new HashMap<Integer, String>();
		if(SystemCache.subjectlist != null){
			for(Subject obj : SystemCache.subjectlist){
				subjectMap.put(obj.getId(), obj.getName());
			}
		}
	}
	public static void initCompanyList() throws Exception {
		SystemCache.companylist = companyService.getList(); // companylist;
		SystemCache.companyMap = new HashMap<Integer, String>();
		if(SystemCache.companylist != null){
			for(Company obj : SystemCache.companylist){
				companyMap.put(obj.getId(), obj.getShortname());
			}
		}
	}

	public static void initSalesmanList() throws Exception {
		SystemCache.salesmanlist = salesmanService.getList();// salesmanlist;
		SystemCache.salesmanMap = new HashMap<Integer, String>();
		if(SystemCache.salesmanlist != null){
			for(Salesman obj : SystemCache.salesmanlist){
				salesmanMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initGongxuList() throws Exception {
		SystemCache.gongxulist = gongXuService.getList(); // gongxulist;
		SystemCache.producing_GONGXU = null;
		SystemCache.gongxuMap = new HashMap<Integer, String>();
		if(SystemCache.gongxulist != null){
			for(GongXu temp : SystemCache.gongxulist){
				if(temp.getIsProducingOrder()){
					SystemCache.producing_GONGXU = temp;
				}
				gongxuMap.put(temp.getId(), temp.getName());
			}
		}
	}

	public static void initUserList() throws Exception {
		SystemCache.userlist = userService.getList(); // userlist;
		SystemCache.userMap = new HashMap<Integer, String>();
		if(SystemCache.userlist != null){
			for(User obj : SystemCache.userlist){
				userMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initRoleList() throws Exception {
		SystemCache.rolelist = roleService.getList(); // userlist;
		SystemCache.roleMap = new HashMap<Integer, String>();
		if(SystemCache.rolelist != null){
			for(Role obj : SystemCache.rolelist){
				roleMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initFactoryList() throws Exception {
		SystemCache.factorylist = factoryService.getList(); // userlist;
		SystemCache.factoryMap = new HashMap<Integer, String>();
		purchase_factorylist = new ArrayList<Factory>();
		coloring_factorylist = new ArrayList<Factory>();
		produce_factorylist = new ArrayList<Factory>();
		for (int i = 0; i < SystemCache.factorylist.size(); ++i) {
			Factory temp = SystemCache.factorylist.get(i);
			Integer type = temp.getType();
			if (type == 0) {
				SystemCache.produce_factorylist.add(temp);
			}
			if (type == 1) {
				SystemCache.purchase_factorylist.add(temp);
			}
			if (type == 2) {
				SystemCache.coloring_factorylist.add(temp);
			}
			factoryMap.put(temp.getId(), temp.getName());
		}

	}
	
	public static void initFuliaoTypeList() throws Exception {
		SystemCache.fuliaotypelist = fuliaoTypeService.getList(); // 
		SystemCache.fuliaotypeMap = new HashMap<Integer, String>();
		if(SystemCache.fuliaotypelist != null){
			for(FuliaoType obj : SystemCache.fuliaotypelist){
				fuliaotypeMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initMaterialList() throws Exception {
		SystemCache.materiallist = materialService.getList(); // userlist;
		SystemCache.materialMap = new HashMap<Integer, String>();
		if(SystemCache.materiallist != null){
			for(Material obj : SystemCache.materiallist){
				materialMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initCustomerList() throws Exception {
		SystemCache.customerlist = customerService.getList(); // customerlist;
		SystemCache.customerMap = new HashMap<Integer, String>();
		if(SystemCache.customerlist != null){
			for(Customer obj : SystemCache.customerlist){
				customerMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initDepartmentList() throws Exception {
		SystemCache.departmentlist = departmentService.getList(); // departmentlist
		SystemCache.departmentMap = new HashMap<Integer, String>();
		if(SystemCache.departmentlist != null){
			for(Department obj : SystemCache.departmentlist){
				departmentMap.put(obj.getId(), obj.getName());
			}
		}
	}

	public static void initEmployeeList() throws Exception {
		SystemCache.employeelist = employeeService.getList(); // employeelist;
		SystemCache.employeeMap = new HashMap<Integer, String>();
		if(SystemCache.employeelist != null){
			for(Employee obj : SystemCache.employeelist){
				employeeMap.put(obj.getId(), obj.getName());
			}
		}
	}
	public static void initLocationList() throws Exception {
		SystemCache.locationlist = locationService.getList(); 
		SystemCache.locationMap = new HashMap<Integer, String>();
		if(SystemCache.locationlist != null){
			for(Location obj : SystemCache.locationlist){
				locationMap.put(obj.getId(), obj.getNumber());
			}
		}
	}
	
	public static List<Subject> getSubjectList(Boolean in_out){
		List<Subject> templist = new ArrayList<Subject>();
		for(Subject temp : subjectlist){
			if(temp.getIn_out() == in_out){
				templist.add(temp);
			}
		}
		return templist;
	}
	
	public static String getSubjectName(Integer id) {
		if (id == null) {
			return "";
		}
		if(subjectMap.containsKey(id)){
			return subjectMap.get(id);
		}else{
			return "";
		}
//		for (int i = 0; i < SystemCache.subjectlist.size(); ++i) {
//			Subject temp = SystemCache.subjectlist.get(i);
//			if (temp.getId() == id) {
//				return temp.getName();
//			}
//		}

	}
	public static String getDepartmentName(Integer departmentId) {
		if (departmentId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.departmentlist.size(); ++i) {
//			Department temp = SystemCache.departmentlist.get(i);
//			if (temp.getId() == departmentId) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(departmentMap.containsKey(departmentId)){
			return departmentMap.get(departmentId);
		}else{
			return "";
		}
	}

	public static Department getDepartment(Integer departmentId) {
		if (departmentId == null) {
			return null;
		}
		for (int i = 0; i < SystemCache.departmentlist.size(); ++i) {
			Department temp = SystemCache.departmentlist.get(i);
			if (temp.getId() == departmentId) {
				return temp;
			}
		}
		return null;
	}

	public static String getEmployeeName(Integer employeeId) {
		if (employeeId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.employeelist.size(); ++i) {
//			Employee temp = SystemCache.employeelist.get(i);
//			if (temp.getId() == employeeId) {
//				return temp.getName();
//			}
//		}
//		return "";

		if(employeeMap.containsKey(employeeId)){
			return employeeMap.get(employeeId);
		}else{
			return "";
		}
	}

	public static Employee getEmployee(String name) {
		if (name == null) {
			return null;
		}
		name = name.trim();
		for (int i = 0; i < SystemCache.employeelist.size(); ++i) {
			Employee temp = SystemCache.employeelist.get(i);
			if (temp.getName().trim().equals(name)) {
				return temp;
			}
		}
		return null;
	}

	public static Employee getEmployee(Integer employeeId) {
		if (employeeId == null) {
			return null;
		}
		for (int i = 0; i < SystemCache.employeelist.size(); ++i) {
			Employee temp = SystemCache.employeelist.get(i);
			if (temp.getId() == employeeId) {
				return temp;
			}
		}
		return null;
	}

	public static String getCustomerName(Integer customerId) {
		if (customerId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.customerlist.size(); ++i) {
//			Customer temp = SystemCache.customerlist.get(i);
//			if (temp.getId() == customerId) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(customerMap.containsKey(customerId)){
			return customerMap.get(customerId);
		}else{
			return "";
		}
	}

	public static Customer getCustomer(Integer customerId) {
		if (customerId == null) {
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
		if (materialId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.materiallist.size(); ++i) {
//			Material temp = SystemCache.materiallist.get(i);
//			if (temp.getId() == materialId) {
//				return temp.getName();
//			}
//		}
//		return "";

		if(materialMap.containsKey(materialId)){
			return materialMap.get(materialId);
		}else{
			return "";
		}
	}

	public static Material getMaterial(Integer materialId) {
		if (materialId == null) {
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
	
	public static String getFuliaoTypeName(Integer fuliaotypeId) {
		if (fuliaotypeId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.fuliaotypelist.size(); ++i) {
//			FuliaoType temp = SystemCache.fuliaotypelist.get(i);
//			if (temp.getId() == fuliaotypeId) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(fuliaotypeMap.containsKey(fuliaotypeId)){
			return fuliaotypeMap.get(fuliaotypeId);
		}else{
			return "";
		}
	}

	public static FuliaoType getFuliaoType(Integer fuliaotypeId) {
		if (fuliaotypeId == null) {
			return null;
		}
		for (int i = 0; i < SystemCache.fuliaotypelist.size(); ++i) {
			FuliaoType temp = SystemCache.fuliaotypelist.get(i);
			if (temp.getId() == fuliaotypeId) {
				return temp;
			}
		}
		return null;
	}

	public static String getUserName(int userid) {

//		for (int i = 0; i < SystemCache.userlist.size(); ++i) {
//			User temp = SystemCache.userlist.get(i);
//			if (temp.getId() == userid) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(userMap.containsKey(userid)){
			return userMap.get(userid);
		}else{
			return "";
		}
	}

	public static String getSalesmanName(Integer salesmanid) {
		if(salesmanid == null){
			return "";
		}
//		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
//			Salesman temp = SystemCache.salesmanlist.get(i);
//			if (temp.getId() == salesmanid) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(salesmanMap.containsKey(salesmanid)){
			return salesmanMap.get(salesmanid);
		}else{
			return "";
		}
	}

	public static Salesman getSalesman(Integer salesmanid) {
		if(salesmanid == null){
			return null;
		}
		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
			Salesman temp = SystemCache.salesmanlist.get(i);
			if (temp.getId() == salesmanid) {
				return temp;
			}
		}
		return null;
	}
	public static Integer getSalesmanIdByName(String name) {
		if(name == null){
			return null;
		}
		if(name.equals("")){
			return null;
		}
		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
			Salesman temp = SystemCache.salesmanlist.get(i);
			if (temp.getName().equals(name)) {
				return temp.getId();
			}
		}
		return null;
	}
	public static Integer getCompanyIdByName(String name) {
		if(name == null){
			return null;
		}
		if(name.equals("")){
			return null;
		}
		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			if (temp.getFullname().equals(name)) {
				return temp.getId();
			}
		}
		return null;
	}
	
	public static String getCompanyName(Integer companyId) {
		if(companyId == null){
			return "";
		}
		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			if (temp.getId() == companyId) {
				return temp.getFullname();
			}
		}
		return "";
	}

	public static String getCompanyShortName(Integer companyId) {
		if(companyId == null){
			return "";
		}
//		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
//			Company temp = SystemCache.companylist.get(i);
//			if (temp.getId() == companyId) {
//				return temp.getShortname();
//			}
//		}
//		return "";

		if(companyMap.containsKey(companyId)){
			return companyMap.get(companyId);
		}else{
			return "";
		}
	}

	public static Company getCompanyById(Integer companyId) {

		for (int i = 0; i < SystemCache.companylist.size(); ++i) {
			Company temp = SystemCache.companylist.get(i);
			if (temp.getId() == companyId) {
				return temp;
			}
		}
		return null;
	}

	public static String getGongxuName(int gongxuId) {

//		for (int i = 0; i < SystemCache.gongxulist.size(); ++i) {
//			GongXu temp = SystemCache.gongxulist.get(i);
//			if (temp.getId() == gongxuId) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(gongxuMap.containsKey(gongxuId)){
			return gongxuMap.get(gongxuId);
		}else{
			return "";
		}
	}
	public static GongXu getGongxu(int gongxuId) {

		for (int i = 0; i < SystemCache.gongxulist.size(); ++i) {
			GongXu temp = SystemCache.gongxulist.get(i);
			if (temp.getId() == gongxuId) {
				return temp;
			}
		}
		return null;
	}

	public static Factory getFactory(Integer factoryId) {
		if (factoryId == null) {
			return null;
		}
		for (int i = 0; i < SystemCache.factorylist.size(); ++i) {
			Factory temp = SystemCache.factorylist.get(i);
			if (temp.getId() == factoryId) {
				return temp;
			}
		}
		return null;
	}

	public static String getFactoryName(Integer factoryId) {
		if (factoryId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.factorylist.size(); ++i) {
//			Factory temp = SystemCache.factorylist.get(i);
//			if (temp.getId() == factoryId) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(factoryMap.containsKey(factoryId)){
			return factoryMap.get(factoryId);
		}else{
			return "";
		}
	}

	public static List<Salesman> getSalesmanList(int companyId) {
		List<Salesman> salemanlist = new ArrayList<Salesman>();
		for (int i = 0; i < SystemCache.salesmanlist.size(); ++i) {
			Salesman temp = SystemCache.salesmanlist.get(i);
			if (temp.getCompanyId() == companyId) {
				salemanlist.add(temp);
			}
		}
		return salemanlist;
	}

	public static HashMap<Company, List<Salesman>> getCompanySalesmanMap() {
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

	public static HashMap<String, List<Salesman>> getCompanySalesmanMap_ID() {
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

//		for (int i = 0; i < SystemCache.rolelist.size(); ++i) {
//			Role temp = SystemCache.rolelist.get(i);
//			if (temp.getId() == roleId) {
//				return temp.getName();
//			}
//		}
//		return "";
		if(roleMap.containsKey(roleId)){
			return roleMap.get(roleId);
		}else{
			return "";
		}
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
	
	public static Location getLocation(Integer locationId) {
		if (locationId == null) {
			return null;
		}
		for (int i = 0; i < SystemCache.locationlist.size(); ++i) {
			Location temp = SystemCache.locationlist.get(i);
			if (temp.getId() == locationId) {
				return temp;
			}
		}
		return null;
	}

	public static String getLocationNumber(Integer locationId) {
		if (locationId == null) {
			return "";
		}
//		for (int i = 0; i < SystemCache.locationlist.size(); ++i) {
//			Location temp = SystemCache.locationlist.get(i);
//			if (temp.getId() == locationId) {
//				return temp.getNumber();
//			}
//		}
//		return "";
		if(locationMap.containsKey(locationId)){
			return locationMap.get(locationId);
		}else{
			return "";
		}
	}

	/* 权限相关 */
//	public static Boolean hasAuthority(HttpSession session, int authorityId) {
//		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
//		return SystemCache.hasAuthority(loginUser, authorityId);
//	}

	public static Boolean hasAuthority(HttpSession session, String lcode) {
		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
		return SystemCache.hasAuthority(loginUser, lcode);
	}

//	public static Boolean hasAuthority(LoginedUser loginUser, int authorityId) {
//		if (loginUser.getLoginedUser().getBuilt_in()) {
//			return true;
//		}
////		List<Authority> authorityList = loginUser.getAuthoritylist();
//		private Map<String,Authority> authorityMap = loginUser.getAuthorityMap();
//		if (authorityMap == null || authorityMap.size() <= 0) {
//			return false;
//		}
////		for (Authority authority : authorityList) {
////			if (authority.getId() == authorityId) {
////				return true;
////			}
////		}
//		
//		return false;
//	}

	public static Boolean hasAuthority(LoginedUser loginUser, String lcode) {
		if (loginUser.getLoginedUser().getBuilt_in()) {
			return true;
		}
		Map<String,Authority> authorityMap = loginUser.getAuthorityMap();
		if (authorityMap == null || authorityMap.size() <= 0) {
			return false;
		}
		return authorityMap.containsKey(lcode);
	}

	/* 权限相关 */

	// 2015-4-4添加设置某用户的数据
	public static void setUserCacheUpdate(int userId,
			Boolean need_message_cache_update) {
		for (com.fuwei.entity.User user : SystemCache.userlist) {
			if (user.getId() == userId) {
				user.setNeed_message_cache_update(need_message_cache_update);
			}
		}
	}
}
