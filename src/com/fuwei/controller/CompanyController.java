package com.fuwei.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fuwei.entity.Company;
import com.fuwei.service.CompanyService;

@RequestMapping("/company")
@Controller
public class CompanyController extends BaseController {
	
	@Autowired
	CompanyService companyService;
	
	public Map<String,Object> add(Company company, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = companyService.insert(company);
		return this.returnSuccess();
		
	}
}
