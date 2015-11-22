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
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.QuotePriceService;
import com.fuwei.util.DateTool;

@RequestMapping("/quoteprice")
@Controller
public class QuotePriceController extends BaseController{
	@Autowired
	QuotePriceService quotePriceService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(QuotePrice QuotePrice,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "quoteprice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加公司价格的权限", null);
		}
		
		QuotePrice.setCreated_at(DateTool.now());
		QuotePrice.setUpdated_at(DateTool.now());
		QuotePrice.setCreated_user(user.getId());
		int success = quotePriceService.add(QuotePrice);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "quoteprice/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除公司价格的权限", null);
		}
		
		int success = quotePriceService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public QuotePrice get(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		String lcode = "quoteprice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看公司价格列表的权限", null);
		}
		
		QuotePrice QuotePrice = quotePriceService.get(id);
		QuotePrice.setCompanyId(SystemCache.getSalesman(QuotePrice.getSalesmanId()).getCompanyId());
		return QuotePrice;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(QuotePrice QuotePrice,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "quoteprice/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑公司价格的权限", null);
		}
		
		QuotePrice.setUpdated_at(DateTool.now());
		int success = quotePriceService.update(QuotePrice);
		return this.returnSuccess();
		
	}
}
