package com.fuwei.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.User;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.util.DateTool;

@RequestMapping("/quoteorder")
@Controller
public class QuoteOrderController {
	
	@Autowired
	QuoteOrderDetailService quoteOrderDetailService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(String ids,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		QuoteOrder quoteOrder = new QuoteOrder();
		quoteOrder.setCreated_at(DateTool.now());
		quoteOrder.setUpdated_at(DateTool.now());
		quoteOrder.setCreated_user(user.getId());
		
		List<QuoteOrderDetail> detaillist = new ArrayList<QuoteOrderDetail>();
		
		
		int success = quotePriceService.add(QuotePrice);
		return this.returnSuccess();
		
	}
}
