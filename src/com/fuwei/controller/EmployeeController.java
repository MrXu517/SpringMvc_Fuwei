package com.fuwei.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Employee;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.EmployeeService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/employee")
@Controller
public class EmployeeController extends BaseController {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer departmentId,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "employee";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有员工管理的权限", null);
		}
		
		request.setAttribute("departmentlist", SystemCache.departmentlist);
		if(departmentId == null){
			request.setAttribute("employeelist", SystemCache.employeelist);
		} 
		else {
			List<Employee> list = new ArrayList<Employee>();
			for(Employee e : SystemCache.employeelist){
				if(e.getDepartmentId() == departmentId){
					list.add(e);
				}
			}
			request.setAttribute("employeelist", list);
		}
		
//		String tabname = request.getParameter("tab");
//		Map<String,Object> model = new HashMap<String,Object>();
//		model.put("tab", tabname);
		return new ModelAndView("systeminfo/employee");

	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Employee employee,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加员工的权限", null);
		}
		employee.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(employee.getName())) ;
		employee.setCreated_at(DateTool.now());
		employee.setUpdated_at(DateTool.now());
		employee.setCreated_user(user.getId());
		int success = employeeService.add(employee);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除员工的权限", null);
		}
		int success = employeeService.remove(id);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Employee get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "employee/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看员工列表的权限", null);
		}
		Employee employee = employeeService.get(id);
		return employee;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Employee employee,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑员工的权限", null);
		}
		employee.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(employee.getName())) ;
		employee.setUpdated_at(DateTool.now());
		int success = employeeService.update(employee);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	
}
