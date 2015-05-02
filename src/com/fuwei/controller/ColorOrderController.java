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
import com.fuwei.constant.Constants;
import com.fuwei.constant.ERROR;
import com.fuwei.entity.Order;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/coloring_order")
@Controller
public class ColorOrderController extends BaseController {
	
	@Autowired
	ColoringOrderService coloringOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	SampleService sampleService;
	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, Integer factoryId,String number,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "coloring_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看染色单列表的权限", null);
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
		pager = coloringOrderService.getList(pager, start_time_d, end_time_d,companyId,factoryId,number, sortList);
		if (pager != null & pager.getResult() != null) {
			List<ColoringOrder> orderlist = (List<ColoringOrder>) pager.getResult();
		}

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		return new ModelAndView("coloring_order/index");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addproducingorder(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "coloring_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加染色单的权限", null);
		}
		try {
			return new ModelAndView("coloring_order/add");	
			
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
		String lcode = "coloring_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加染色单的权限", null);
		}
		try {
			if(orderId!=null){
				Order order = orderService.get(orderId);
				request.setAttribute("order", order);
				return new ModelAndView("coloring_order/addbyorder");
			}
			throw new Exception("缺少订单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(ColoringOrder coloringOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "coloring_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加染色单的权限", null);
		}
		try {	
			coloringOrder.setCreated_at(DateTool.now());// 设置创建时间
			coloringOrder.setUpdated_at(DateTool.now());// 设置更新时间
			coloringOrder.setCreated_user(user.getId());// 设置创建人
			Integer sampleId = coloringOrder.getSampleId();
			if(sampleId != null){
				Sample sample = sampleService.get(sampleId);
				coloringOrder.setImg(sample.getImg());
				coloringOrder.setProductNumber(sample.getProductNumber());
				coloringOrder.setMaterialId(sample.getMaterialId());
				coloringOrder.setSize(sample.getSize());
				coloringOrder.setWeight(sample.getWeight());
				coloringOrder.setName(sample.getName());
				coloringOrder.setImg_s(sample.getImg_s());
				coloringOrder.setImg_ss(sample.getImg_ss());
			}
			List<ColoringOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								ColoringOrderDetail.class);
			if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
				throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
			}
			coloringOrder.setDetaillist(detaillist);
			Integer tableOrderId = coloringOrderService.add(coloringOrder);
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	// 添加或保存原材料采购单
	@RequestMapping(value = "/addbyorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(
			ColoringOrder tableOrder, String details,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/coloring";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑染色单的权限",
					null);
		}
		try {
			Integer tableOrderId = tableOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (tableOrder.getOrderId() == null
						|| tableOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"染色单必须属于一张订单", null);
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
				tableOrder.setImg_s(order.getImg_s());
				tableOrder.setImg_ss(order.getImg_ss());
				tableOrder.setProductNumber(order.getProductNumber());
				tableOrder.setMaterialId(order.getMaterialId());
				tableOrder.setSize(order.getSize());
				tableOrder.setWeight(order.getWeight());
				tableOrder.setName(order.getName());
				tableOrder.setCompanyId(order.getCompanyId());
				tableOrder.setCustomerId(order.getCustomerId());
				tableOrder.setSampleId(order.getSampleId());
				tableOrder.setOrderNumber(order.getOrderNumber());
				tableOrder.setCharge_employee(order.getCharge_employee());//2015/3/17 添加跟单人
				
				List<ColoringOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								ColoringOrderDetail.class);
				if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
					throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
				}
				tableOrder.setDetaillist(detaillist);
				tableOrderId = coloringOrderService.add(tableOrder);
			} else {// 编辑
				if (tableOrder.getOrderId() == null
						|| tableOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"缺少订单ID", null);
				}
				tableOrder.setUpdated_at(DateTool.now());
				List<ColoringOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								ColoringOrderDetail.class);
				if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
					throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
				}
				tableOrder.setDetaillist(detaillist);
				tableOrderId = coloringOrderService.update(tableOrder);
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
		String lcode = "coloring_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除染色单的权限", null);
		}
		int success = coloringOrderService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ColoringOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "coloring_order/get";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看染色单详情的权限", null);
		}
		ColoringOrder coloringOrder = coloringOrderService.get(id);
		return coloringOrder;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		
		if (id == null) {
			throw new Exception("缺少染色单ID");
		}		
		String lcode = "fuliao_purchase_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看染色单详情的权限", null);
		}
		
		ColoringOrder coloringOrder = coloringOrderService.get(id);
		
		List<ColoringOrder> coloringOrderList = new ArrayList<ColoringOrder>();
		coloringOrderList.add(coloringOrder);
		request.setAttribute("coloringOrderList", coloringOrderList);
		Map<String,Object> data = new HashMap<String,Object>();  
	    data.put("gridName","coloringorder");  
		return new ModelAndView("printorder/preview",data);
	}
	
	@RequestMapping(value = "/put/{tableOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer tableOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "coloring_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加原材料采购单的权限", null);
		}
		try {
			if(tableOrderId!=null){
				ColoringOrder coloringOrder = coloringOrderService.get(tableOrderId);
				request.setAttribute("coloringOrder", coloringOrder);
				if(coloringOrder.getOrderId()!=null){
					return new ModelAndView("coloring_order/editbyorder");
				}else{
					return new ModelAndView("coloring_order/edit");
				}
				
			}
			throw new Exception("缺少原材料采购单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(ColoringOrder coloringOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "coloring_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑染色单的权限", null);
		}
		coloringOrder.setUpdated_at(DateTool.now());
		List<ColoringOrderDetail> detaillist = SerializeTool
				.deserializeList(details,
						ColoringOrderDetail.class);
		if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
			throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
		}
		coloringOrder.setDetaillist(detaillist);
		Integer tableOrderId = coloringOrderService.update(coloringOrder);
		return this.returnSuccess("id", tableOrderId);
		
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		
		if (id == null) {
			throw new Exception("缺少染色单ID");
		}		
//		String lcode = "fuliao_purchase_order/detail";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看染色单详情的权限", null);
//		}
		
		ColoringOrder coloringOrder = coloringOrderService.get(id);
		
		List<ColoringOrder> coloringOrderList = new ArrayList<ColoringOrder>();
		coloringOrderList.add(coloringOrder);
		request.setAttribute("coloringOrderList", coloringOrderList);
		Map<String,Object> data = new HashMap<String,Object>();  
	    data.put("gridName","coloringorder");  
		return new ModelAndView("printorder/print",data);
	}
	
}
