package com.fuwei.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuwei.entity.Company;
import com.fuwei.service.CompanyService;
import com.fuwei.util.DateTool;

@RequestMapping("/company")
@Controller
public class CompanyController extends BaseController {
	
	@Autowired
	CompanyService companyService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Company company, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		company.setCreated_at(DateTool.now());
		company.setUpdated_at(DateTool.now());
		int success = companyService.add(company);
		return this.returnSuccess();
		
	}
}
