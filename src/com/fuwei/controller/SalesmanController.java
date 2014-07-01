package com.fuwei.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuwei.commons.SystemCache;
import com.fuwei.entity.Company;
import com.fuwei.entity.Salesman;
import com.fuwei.service.CompanyService;
import com.fuwei.service.SalesmanService;
import com.fuwei.util.DateTool;

@RequestMapping("/salesman")
@Controller
public class SalesmanController extends BaseController {
	
	@Autowired
	SalesmanService salesmanService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Salesman salesman, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		salesman.setCreated_at(DateTool.now());
		salesman.setUpdated_at(DateTool.now());
		int success = salesmanService.add(salesman);
		
		//更新缓存
		new SystemCache().initSalesmanList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = salesmanService.remove(id);
		
		//更新缓存
		new SystemCache().initSalesmanList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Salesman get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Salesman salesman = salesmanService.get(id);
		return salesman;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Salesman salesman, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		salesman.setUpdated_at(DateTool.now());
		int success = salesmanService.update(salesman);
		
		//更新缓存
		new SystemCache().initSalesmanList();
		
		return this.returnSuccess();
		
	}
	
	
}
