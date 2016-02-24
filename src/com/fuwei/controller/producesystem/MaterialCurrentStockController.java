package com.fuwei.controller.producesystem;

import java.util.ArrayList;
import java.util.List;

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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.producesystem.MaterialCurrentStock;
import com.fuwei.entity.producesystem.MaterialInOut;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.service.producesystem.MaterialCurrentStockService;
import com.fuwei.service.producesystem.StoreInOutService;
import com.fuwei.util.SerializeTool;

@RequestMapping("/material_current_stock")
@Controller
/* 原材料库存*/
public class MaterialCurrentStockController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	MaterialCurrentStockService materialCurrentStockService;
	@Autowired
	StoreInOutService storeInOutService;
	@Autowired
	OrderService orderService;
	@Autowired
	StoreOrderService storeOrderService;	
	@Autowired
	ColoringOrderService coloringOrderService;
	
	//大货纱原材料库存
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, Integer companyId, Integer charge_employee,
			String orderNumber, Boolean not_zero,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "material_current_stock/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料库存列表的权限", null);
		}

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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = materialCurrentStockService.getList(pager, companyId, charge_employee, orderNumber,not_zero, sortList);
		
		request.setAttribute("companyId", companyId);
		request.setAttribute("not_zero", not_zero);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("store_in_out/current_stock");
	}
	
	//样纱原材料库存
	@RequestMapping(value = "/index_coloring", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index_coloring(Integer page, Integer companyId, Integer charge_employee,
			String coloringOrderNumber, Boolean not_zero,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "material_current_stock/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料库存列表的权限", null);
		}

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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = materialCurrentStockService.getList_coloring(pager, companyId, charge_employee, coloringOrderNumber,not_zero, sortList);
		
		request.setAttribute("companyId", companyId);
		request.setAttribute("not_zero", not_zero);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("coloringOrderNumber", coloringOrderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("store_in_out/current_stock_coloring");
	}
	
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView report(Integer page, Integer companyId, Integer charge_employee,
			String orderNumber, Boolean not_zero,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "report/material_current_stock";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料库存报表的权限", null);
		}

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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = materialCurrentStockService.getList(pager, companyId, charge_employee, orderNumber,not_zero, sortList);
		
		request.setAttribute("companyId", companyId);
		request.setAttribute("not_zero", not_zero);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("report/material_current_stock");
	}
	
	
	@RequestMapping(value = "/in_out/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView in_out(@PathVariable Integer orderId, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (orderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "material_current_stock/in_out";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		String lcode2 = "order/progress";
		Boolean hasAuthority2 = SystemCache.hasAuthority(session, lcode2);
		if (!hasAuthority && !hasAuthority2) {
			throw new PermissionDeniedDataAccessException("没有查看订单原材料出入库记录的权限",
					null);
		}
		Order order = orderService.get(orderId);
		if(order == null){
			throw new Exception("找不到ID为" + orderId + "的订单");
		}
		List<MaterialInOut> detailInOutlist = materialCurrentStockService.inOutdetail(orderId);
		if (detailInOutlist == null) {
			throw new Exception("找不到订单ID为" + orderId + "的原材料出入库、退货记录");
		}
		StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
		request.setAttribute("order", order);
		request.setAttribute("storeOrder", storeOrder);
		request.setAttribute("detailInOutlist", detailInOutlist);
		MaterialCurrentStock materialCurrentStock = materialCurrentStockService.get(orderId);
		request.setAttribute("materialCurrentStock", materialCurrentStock);
		
		return new ModelAndView("store_in_out/order_in_out");	
	}
	
	@RequestMapping(value = "/in_out_coloring/{coloringOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView in_out_coloring(@PathVariable Integer coloringOrderId, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (coloringOrderId == null) {
			throw new Exception("缺少染色单ID");
		}
		String lcode = "material_current_stock/in_out";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		String lcode2 = "order/progress";
		Boolean hasAuthority2 = SystemCache.hasAuthority(session, lcode2);
		if (!hasAuthority && !hasAuthority2) {
			throw new PermissionDeniedDataAccessException("没有查看订单原材料出入库记录的权限",
					null);
		}
		ColoringOrder coloringOrder = coloringOrderService.get(coloringOrderId);
		if(coloringOrder == null){
			throw new Exception("找不到ID为" + coloringOrderId + "的染色单");
		}
		List<MaterialInOut> detailInOutlist = materialCurrentStockService.inOutdetail_coloring(coloringOrderId);
		if (detailInOutlist == null) {
			throw new Exception("找不到染色单ID为" + coloringOrderId + "的原材料出入库、退货记录");
		}
		MaterialCurrentStock materialCurrentStock = materialCurrentStockService.getByColoringOrder(coloringOrderId);
		request.setAttribute("materialCurrentStock", materialCurrentStock);
		request.setAttribute("coloringOrder", coloringOrder);
		request.setAttribute("detailInOutlist", detailInOutlist);
		return new ModelAndView("store_in_out/order_in_out_coloring");	
	}
	
	@RequestMapping(value = "/in_out2/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView in_out2(@PathVariable Integer orderId, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (orderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "order/progress";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看订单原材料出入库记录的权限",
					null);
		}
		Order order = orderService.get(orderId);
		if(order == null){
			throw new Exception("找不到ID为" + orderId + "的订单");
		}
		List<MaterialInOut> detailInOutlist = materialCurrentStockService.inOutdetail(orderId);
		if (detailInOutlist == null) {
			throw new Exception("找不到订单ID为" + orderId + "的原材料出入库、退货记录");
		}
		StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
		request.setAttribute("order", order);
		request.setAttribute("storeOrder", storeOrder);
		request.setAttribute("detailInOutlist", detailInOutlist);
		return new ModelAndView("order/progress/material_in_out");	
	}
}
