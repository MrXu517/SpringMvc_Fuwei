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
import com.fuwei.entity.User;
import com.fuwei.service.CompanyService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/company")
@Controller
public class CompanyController extends BaseController {
	
	@Autowired
	CompanyService companyService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Company company,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
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
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = companyService.remove(id);
		
		//更新缓存
		new SystemCache().initCompanyList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Company get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Company company = companyService.get(id);
		return company;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Company company, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		company.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(company.getFullname())) ;
		company.setUpdated_at(DateTool.now());
		int success = companyService.update(company);
		
		//更新缓存
		new SystemCache().initCompanyList();
		
		return this.returnSuccess();
		
	}
	
	
}
