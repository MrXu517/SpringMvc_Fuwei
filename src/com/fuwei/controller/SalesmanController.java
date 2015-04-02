package com.fuwei.controller;

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
import com.fuwei.entity.Company;
import com.fuwei.entity.Salesman;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.CompanyService;
import com.fuwei.service.SalesmanService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/salesman")
@Controller
public class SalesmanController extends BaseController {
	
	@Autowired
	SalesmanService salesmanService;
	@Autowired
	AuthorityService authorityService;
	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "salesman";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有业务员管理的权限", null);
		}
		request.setAttribute("companylist", SystemCache.companylist);
		request.setAttribute("salesmanlist", SystemCache.salesmanlist);
		return new ModelAndView("systeminfo/salesman");

	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Salesman salesman,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "salesman/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加业务员的权限", null);
		}
		
		salesman.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(salesman.getName())) ;
		salesman.setCreated_at(DateTool.now());
		salesman.setUpdated_at(DateTool.now());
		salesman.setCreated_user(user.getId());
		int success = salesmanService.add(salesman);
		
		//更新缓存
		new SystemCache().initSalesmanList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "salesman/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除业务员的权限", null);
		}
		
		int success = salesmanService.remove(id);
		
		//更新缓存
		new SystemCache().initSalesmanList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Salesman get(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "salesman/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看业务员列表的权限", null);
		}
		Salesman salesman = salesmanService.get(id);
		return salesman;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Salesman salesman,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "salesman/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑业务员的权限", null);
		}
		
		salesman.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(salesman.getName())) ;
		salesman.setUpdated_at(DateTool.now());
		int success = salesmanService.update(salesman);
		
		//更新缓存
		new SystemCache().initSalesmanList();
		
		return this.returnSuccess();
		
	}
	
	
}
