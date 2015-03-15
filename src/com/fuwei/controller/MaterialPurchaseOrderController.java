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
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.MaterialPurchaseOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/material_purchase_order")
@Controller
public class MaterialPurchaseOrderController extends BaseController {
	
	@Autowired
	MaterialPurchaseOrderService materialPurchaseOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	SampleService sampleService;
	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "material_purchase_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料采购单列表的权限", null);
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
		pager = materialPurchaseOrderService.getList(pager, start_time_d, end_time_d, sortList);
		if (pager != null & pager.getResult() != null) {
			List<MaterialPurchaseOrder> orderlist = (List<MaterialPurchaseOrder>) pager.getResult();
		}

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		return new ModelAndView("material_purchase_order/index");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addproducingorder(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "material_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加原材料采购单的权限", null);
		}
		try {
			return new ModelAndView("material_purchase_order/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addproducingorder(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "material_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加原材料采购单的权限", null);
		}
		try {
			if(orderId!=null){
				Order order = orderService.get(orderId);
				request.setAttribute("order", order);
				return new ModelAndView("material_purchase_order/addbyorder");
			}
			throw new Exception("缺少订单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(MaterialPurchaseOrder materialPurchaseOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "material_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加原材料采购单的权限", null);
		}
		try {	
			materialPurchaseOrder.setCreated_at(DateTool.now());// 设置创建时间
			materialPurchaseOrder.setUpdated_at(DateTool.now());// 设置更新时间
			materialPurchaseOrder.setCreated_user(user.getId());// 设置创建人
			Integer sampleId = materialPurchaseOrder.getSampleId();
			if(sampleId != null){
				Sample sample = sampleService.get(sampleId);
				materialPurchaseOrder.setImg(sample.getImg());
				materialPurchaseOrder.setProductNumber(sample.getProductNumber());
				materialPurchaseOrder.setMaterial(sample.getMaterial());
				materialPurchaseOrder.setSize(sample.getSize());
				materialPurchaseOrder.setWeight(sample.getWeight());
				materialPurchaseOrder.setName(sample.getName());
			}
			List<MaterialPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								MaterialPurchaseOrderDetail.class);
			materialPurchaseOrder.setDetaillist(detaillist);
			Integer tableOrderId = materialPurchaseOrderService.add(materialPurchaseOrder);
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	// 添加或保存原材料采购单
	@RequestMapping(value = "/addbyorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(
			MaterialPurchaseOrder tableOrder, String details,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/materialpurchase";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料采购单的权限",
					null);
		}
		try {
			Integer tableOrderId = tableOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (tableOrder.getOrderId() == null
						|| tableOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"原材料采购单必须属于一张订单", null);
				}
				Order order = orderService.get(tableOrder.getOrderId());
				if(order == null){
					throw new PermissionDeniedDataAccessException(
							"订单不存在", null);
				}
				
				tableOrder.setCreated_at(DateTool.now());// 设置创建时间
				tableOrder.setUpdated_at(DateTool.now());// 设置更新时间
				tableOrder.setCreated_user(user.getId());// 设置创建人
				
				tableOrder.setImg(order.getImg());
				tableOrder.setProductNumber(order.getProductNumber());
				tableOrder.setMaterial(order.getMaterial());
				tableOrder.setSize(order.getSize());
				tableOrder.setWeight(order.getWeight());
				tableOrder.setName(order.getName());
				tableOrder.setCompanyId(order.getCompanyId());
				tableOrder.setKehu(order.getKehu());
				tableOrder.setSampleId(order.getSampleId());
				
				List<MaterialPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								MaterialPurchaseOrderDetail.class);
				tableOrder.setDetaillist(detaillist);
				tableOrderId = materialPurchaseOrderService.add(tableOrder);
			} else {// 编辑
				tableOrder.setUpdated_at(DateTool.now());
				List<MaterialPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								MaterialPurchaseOrderDetail.class);
				tableOrder.setDetaillist(detaillist);
				tableOrderId = materialPurchaseOrderService.update(tableOrder);
			}
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
		String lcode = "material_purchase_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除原材料采购单的权限", null);
		}
		int success = materialPurchaseOrderService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public MaterialPurchaseOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "material_purchase_order/get";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看原材料采购单详情的权限", null);
		}
		MaterialPurchaseOrder materialPurchaseOrder = materialPurchaseOrderService.get(id);
		return materialPurchaseOrder;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(MaterialPurchaseOrder materialPurchaseOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "material_purchase_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑原材料采购单的权限", null);
		}
		materialPurchaseOrder.setUpdated_at(DateTool.now());
		List<MaterialPurchaseOrderDetail> detaillist = SerializeTool
				.deserializeList(details,
						MaterialPurchaseOrderDetail.class);
		materialPurchaseOrder.setDetaillist(detaillist);
		Integer tableOrderId = materialPurchaseOrderService.update(materialPurchaseOrder);
		return this.returnSuccess("id", tableOrderId);
		
	}
	
	
}
