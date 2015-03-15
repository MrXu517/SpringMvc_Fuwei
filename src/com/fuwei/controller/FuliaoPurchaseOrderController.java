package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/fuliao_purchase_order")
@Controller
public class FuliaoPurchaseOrderController extends BaseController {
	
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "fuliao_purchase_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料采购单列表的权限", null);
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
		pager = fuliaoPurchaseOrderService.getList(pager, start_time_d, end_time_d, sortList);
		if (pager != null & pager.getResult() != null) {
			List<FuliaoPurchaseOrder> orderlist = (List<FuliaoPurchaseOrder>) pager.getResult();
		}

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliao_purchase_order/index");
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FuliaoPurchaseOrder fuliaoPurchaseOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料采购单的权限", null);
		}
		try {	
			fuliaoPurchaseOrder.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoPurchaseOrder.setUpdated_at(DateTool.now());// 设置更新时间
			fuliaoPurchaseOrder.setCreated_user(user.getId());// 设置创建人
			List<FuliaoPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoPurchaseOrderDetail.class);
			fuliaoPurchaseOrder.setDetaillist(detaillist);
			Integer tableOrderId = fuliaoPurchaseOrderService.add(fuliaoPurchaseOrder);
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除辅料采购单的权限", null);
		}
		int success = fuliaoPurchaseOrderService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FuliaoPurchaseOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "fuliao_purchase_order/get";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看辅料采购单详情的权限", null);
		}
		FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(id);
		return fuliaoPurchaseOrder;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FuliaoPurchaseOrder fuliaoPurchaseOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑辅料采购单的权限", null);
		}
		fuliaoPurchaseOrder.setUpdated_at(DateTool.now());
		List<FuliaoPurchaseOrderDetail> detaillist = SerializeTool
				.deserializeList(details,
						FuliaoPurchaseOrderDetail.class);
		fuliaoPurchaseOrder.setDetaillist(detaillist);
		Integer tableOrderId = fuliaoPurchaseOrderService.update(fuliaoPurchaseOrder);
		return this.returnSuccess("id", tableOrderId);
		
	}
	
	
}
