package com.fuwei.controller;

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

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Company;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.CompanyService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/company")
@Controller
public class CompanyController extends BaseController {
	
	@Autowired
	CompanyService companyService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Company company,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "company/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加公司的权限", null);
		}
		company.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(company.getFullname())) ;
		company.setCreated_at(DateTool.now());
		company.setUpdated_at(DateTool.now());
		company.setCreated_user(user.getId());
		int success = companyService.add(company);
		
		//更新缓存
		new SystemCache().initCompanyList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "company/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除公司的权限", null);
		}
		int success = companyService.remove(id);
		
		//更新缓存
		new SystemCache().initCompanyList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Company get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "company/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看公司列表的权限", null);
		}
		Company company = companyService.get(id);
		return company;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Company company,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "company/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑公司的权限", null);
		}
		company.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(company.getFullname())) ;
		company.setUpdated_at(DateTool.now());
		int success = companyService.update(company);
		
		//更新缓存
		new SystemCache().initCompanyList();
		
		return this.returnSuccess();
		
	}
	
	
}
