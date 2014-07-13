package com.fuwei.controller;

import java.util.List;
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


import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.User;
import com.fuwei.service.QuotePriceService;
import com.fuwei.util.DateTool;

@RequestMapping("/quoteprice")
@Controller
public class QuotePriceController extends BaseController{
	@Autowired
	QuotePriceService quotePriceService;
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(QuotePrice QuotePrice,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		QuotePrice.setCreated_at(DateTool.now());
		QuotePrice.setUpdated_at(DateTool.now());
		QuotePrice.setCreated_user(user.getId());
		int success = quotePriceService.add(QuotePrice);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = quotePriceService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public QuotePrice get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		QuotePrice QuotePrice = quotePriceService.get(id);
		return QuotePrice;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(QuotePrice QuotePrice, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		QuotePrice.setUpdated_at(DateTool.now());
		int success = quotePriceService.update(QuotePrice);
		return this.returnSuccess();
		
	}
}
