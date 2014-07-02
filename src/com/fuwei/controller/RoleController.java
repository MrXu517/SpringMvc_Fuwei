package com.fuwei.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Company;
import com.fuwei.entity.Role;
import com.fuwei.entity.User;
import com.fuwei.service.CompanyService;
import com.fuwei.service.RoleService;
import com.fuwei.util.DateTool;

@RequestMapping("/role")
@Controller
public class RoleController extends BaseController {
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Role role,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		role.setCreated_at(DateTool.now());
		role.setUpdated_at(DateTool.now());
		role.setCreated_user(user.getId());
		int success = roleService.add(role);
		
		//更新缓存
		new SystemCache().initRoleList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = roleService.remove(id);
		
		//更新缓存
		new SystemCache().initRoleList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Role get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Role role = roleService.get(id);
		return role;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Role role, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		role.setUpdated_at(DateTool.now());
		int success = roleService.update(role);
		
		//更新缓存
		new SystemCache().initRoleList();
		
		return this.returnSuccess();
		
	}
	
	
}
