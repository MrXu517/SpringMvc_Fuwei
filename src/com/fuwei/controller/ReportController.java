package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Factory;
import com.fuwei.entity.Material;
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.User;
import com.fuwei.service.ordergrid.CarFixRecordOrderService;
import com.fuwei.service.ordergrid.CheckRecordOrderService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.ColoringProcessOrderService;
import com.fuwei.service.ordergrid.FinalStoreOrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.HeadBankOrderService;
import com.fuwei.service.ordergrid.IroningRecordOrderService;
import com.fuwei.service.ordergrid.MaterialPurchaseOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.ordergrid.ProductionScheduleOrderService;
import com.fuwei.service.ordergrid.ShopRecordOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/report")
@Controller
public class ReportController {
	@Autowired
	HeadBankOrderService headBankOrderService;

	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	PlanOrderService planOrderService;
	@Autowired
	StoreOrderService storeOrderService;
	@Autowired
	HalfCheckRecordOrderService halfCheckRecordOrderService;
	@Autowired
	CheckRecordOrderService checkRecordOrderService;
	@Autowired
	ColoringOrderService coloringOrderService;
	@Autowired
	MaterialPurchaseOrderService materialPurchaseOrderService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	@Autowired
	CarFixRecordOrderService carFixRecordOrderService;
	@Autowired
	IroningRecordOrderService ironingRecordOrderService;
	
	/*2015-3-23添加 新表格*/
	@Autowired
	ProductionScheduleOrderService productionScheduleOrderService;
	@Autowired
	FinalStoreOrderService finalStoreOrderService;
	@Autowired
	ShopRecordOrderService shopRecordOrderService;
	@Autowired
	ColoringProcessOrderService coloringProcessOrderService;
	
	
	@RequestMapping(value = "/material_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView material_purchase(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		
		String lcode = "report/material_purchase";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料采购报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			Sort sort_factory = new Sort();
			sort_factory.setDirection("desc");
			sort_factory.setProperty("factoryId");
			sortList.add(sort_factory);
			
			
			HashMap<Factory,HashMap<Material,Double> > result = materialPurchaseOrderService.material_purchase_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
//			request.setAttribute("pager", pager);
			return new ModelAndView("report/material_purchase");
		} catch (Exception e) {
			throw e;
		}
	}
	
	

}
