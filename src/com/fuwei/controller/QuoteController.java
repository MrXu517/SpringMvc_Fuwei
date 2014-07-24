package com.fuwei.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.fuwei.entity.Quote;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.QuotePriceService;
import com.fuwei.service.QuoteService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/quote")
@Controller
public class QuoteController extends BaseController{
	@Autowired
	QuoteService quoteService;
	@Autowired
	QuotePriceService quotePriceService;
	@Autowired
	AuthorityService authorityService;
	
	//获取未确认提交报价列表
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "quote/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看报价列表的权限", null);
		}
		List<Quote> quotelist = quoteService.getDetailList();
		HashMap<Integer, List<Quote>> quoteMap=new HashMap<Integer, List<Quote>>();
		for (Quote quote : quotelist) {
			Integer salesmanId = quote.getQuotePrice().getSalesmanId();
			if(quoteMap.containsKey(salesmanId)){
				quoteMap.get(salesmanId).add(quote);
			}else {
				List<Quote> tList=new ArrayList<Quote>();
				tList.add(quote);
				quoteMap.put(salesmanId, tList);
			}
		}
		
		request.setAttribute("quoteMap", quoteMap);
		return new ModelAndView("quote/index");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Integer quotePriceId,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "quote/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加报价的权限", null);
		}
		
		
		QuotePrice quoteprice = quotePriceService.get(quotePriceId);
		if(quoteprice == null){
			throw new Exception("没有相关的公司价格");
		}
		Quote quote = new Quote();
		quote.setSampleId(quoteprice.getSampleId());
		quote.setCreated_at(DateTool.now());
		quote.setUpdated_at(DateTool.now());
		quote.setCreated_user(user.getId());
		quote.setQuotePriceId(quotePriceId);
		int success = quoteService.add(quote);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "quote/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除报价的权限", null);
		}
		
		int success = quoteService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Quote get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Quote quote = quoteService.get(id);
		return quote;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Quote quote, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		quote.setUpdated_at(DateTool.now());
		int success = quoteService.update(quote);
		return this.returnSuccess();
		
	}
}
