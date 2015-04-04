package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Message;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/producing_order")
@Controller
public class ProducingOrderController extends BaseController {
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	SampleService sampleService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	MessageService messageService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "producing_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看生产单列表的权限", null);
		}

		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		pager = producingOrderService.getList(pager, start_time_d, end_time_d,
				sortList);
		if (pager != null & pager.getResult() != null) {
			List<ProducingOrder> orderlist = (List<ProducingOrder>) pager
					.getResult();
		}

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		return new ModelAndView("producing_order/index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少生产单ID");
		}
		String lcode = "material_purchase_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料采购单详情的权限",
					null);
		}
		ProducingOrder producingOrder = producingOrderService.get(id);
		Order order = orderService.get(producingOrder.getOrderId());
		request.setAttribute("order", order);

		List<ProducingOrder> producingOrderList = new ArrayList<ProducingOrder>();
		producingOrderList.add(producingOrder);
		request.setAttribute("producingOrderList", producingOrderList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gridName", "producingorder");
		return new ModelAndView("printorder/preview", data);
	}

	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少生产单ID");
		}
		ProducingOrder producingOrder = producingOrderService.get(id);
		Order order = orderService.get(producingOrder.getOrderId());
		request.setAttribute("order", order);

		List<ProducingOrder> producingOrderList = new ArrayList<ProducingOrder>();
		producingOrderList.add(producingOrder);
		request.setAttribute("producingOrderList", producingOrderList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gridName", "producingorder");
		return new ModelAndView("printorder/print", data);
	}

	// 2015-4-4请求划价
	@RequestMapping(value = "/price_request/{orderId}", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> price_request(@PathVariable Integer orderId,
			String orderNumber, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing/price_request";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有请求划价的权限", null);
		}
		// 获取有编辑生产单单价的用户
		List<User> userList = authorityService
				.getListByLcode("order/producing/price_edit");

		for (User temp : userList) {
			Message message = new Message();
			message.setTopic("请求划价");
			message.setFrom_user_id(user.getId());
			message.setTo_user_id(temp.getId());
			message.setCreated_at(DateTool.now());
			message.setTo_url("order/tablelist?orderId=" + orderId
					+ "&tab=producingorder");
			message.setContent("订单 " + orderNumber
					+ " 的<strong> 生产单 <strong> <strong>请求划价<strong>，点击打开链接");
			messageService.add(message);
			SystemCache.setUserCacheUpdate(message.getTo_user_id(),true);
		}

		return this.returnSuccess();
	}

	// 2015-4-4完成划价
	@RequestMapping(value = "/price_complete/{orderId}", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> price_complete(@PathVariable Integer orderId,
			String orderNumber, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing/price_edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有划价的权限", null);
		}
		// 获取有编辑生产单单价的用户
		List<User> userList = authorityService
				.getListByLcode("order/producing/price_request");

		for (User temp : userList) {
			Message message = new Message();
			message.setTopic("完成划价");
			message.setFrom_user_id(user.getId());
			message.setTo_user_id(temp.getId());
			message.setCreated_at(DateTool.now());
			message.setTo_url("order/tablelist?orderId=" + orderId
					+ "&tab=producingorder");
			message.setContent("订单 " + orderNumber
					+ " 的<strong> 生产单 <strong> <strong>已完成划价<strong>，点击打开链接");
			messageService.add(message);
			SystemCache.setUserCacheUpdate(message.getTo_user_id(),true);
		}

		return this.returnSuccess();

	}

}
