package com.fuwei.controller.producesystem.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Order;
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;

@RequestMapping("/plancondition")
@Controller
public class PlanConditionController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value = "/orderdetail/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "plancondition/add";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有排生产计划的权限", null);
		}
		Order order = orderService.get(OrderId);
		request.setAttribute("order", order);		
		return new ModelAndView("producesystemplan/plancondition/orderdetail");
	}
}
