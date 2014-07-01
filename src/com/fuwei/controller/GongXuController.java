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
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Salesman;
import com.fuwei.service.CompanyService;
import com.fuwei.service.GongXuService;
import com.fuwei.service.SalesmanService;
import com.fuwei.util.DateTool;

@RequestMapping("/gongxu")
@Controller
public class GongXuController extends BaseController {
	
	@Autowired
	GongXuService gongxuService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(GongXu gongxu, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		gongxu.setCreated_at(DateTool.now());
		gongxu.setUpdated_at(DateTool.now());
		int success = gongxuService.add(gongxu);
		
		//更新缓存
		new SystemCache().initGongxuList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = gongxuService.remove(id);
		
		//更新缓存
		new SystemCache().initGongxuList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public GongXu get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		GongXu gongxu = gongxuService.get(id);
		return gongxu;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(GongXu gongxu, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		gongxu.setUpdated_at(DateTool.now());
		int success = gongxuService.update(gongxu);
		
		//更新缓存
		new SystemCache().initGongxuList();
		
		return this.returnSuccess();
		
	}
	
	
}
