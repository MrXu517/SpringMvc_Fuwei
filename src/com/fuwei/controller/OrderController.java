package com.fuwei.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.OrderHandle;
import com.fuwei.entity.OrderProduceStatus;
import com.fuwei.entity.OrderStep;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.CheckRecordOrder;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.ordergrid.ColoringProcessOrder;
import com.fuwei.entity.ordergrid.ColoringProcessOrderDetail;
import com.fuwei.entity.ordergrid.FinalStoreOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrder;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrderDetail;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrderDetail2;
import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.HeadBankOrderDetail;
import com.fuwei.entity.ordergrid.IroningRecordOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.ordergrid.ProductionScheduleOrder;
import com.fuwei.entity.ordergrid.ShopRecordOrder;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.ordergrid.StoreOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderDetailService;
import com.fuwei.service.OrderHandleService;
import com.fuwei.service.OrderProduceStatusService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ProductionNotificationService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.service.SampleService;
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

@RequestMapping("/order")
@Controller
public class OrderController extends BaseController {
	@Autowired
	OrderService orderService;
//	@Autowired
//	OrderDetailService orderDetailService;
	@Autowired
	OrderHandleService orderHandleService;
	@Autowired
	AuthorityService authorityService;
//	@Autowired
//	QuoteOrderService quoteOrderService;
	@Autowired
	QuoteOrderDetailService quoteOrderDetailService;
	@Autowired
	OrderProduceStatusService orderProduceStatusService;
	@Autowired
	ProductionNotificationService productionNotificationService;

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
	
	
	@Autowired
	SampleService sampleService;
	
	
	@RequestMapping(value = "/undelivery", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView undelivery(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,Integer charge_employee,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "order/undelivery";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看待发货订单列表的权限", null);
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
		sort.setDirection("asc");
		sort.setProperty("end_at");
		sortList.add(sort);
		
		Integer status = OrderStatus.DELIVERING.ordinal();
		pager = orderService.getList(pager, start_time_d, end_time_d,
				companyId,charge_employee, status, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if (companyId == null & salesmanId != null) {
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("status", status);
		request.setAttribute("pager", pager);
		request.setAttribute("charge_employee", charge_employee);
		return new ModelAndView("order/undelivery");
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(Integer quoteOrderId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Order order = new Order();
		try {
			order.setCreated_at(DateTool.now());// 设置订单创建时间
			order.setUpdated_at(DateTool.now());// 设置订单更新时间
			order.setCreated_user(user.getId());// 设置订单创建人
			order.setStatus(OrderStatus.CREATE.ordinal());// 设置订单状态
			order.setState(OrderStatus.CREATE.getName());// 设置订单状态描述
			order.setAmount(0);// 设置订单总金额
			List<OrderDetail> orderDetaillist = new ArrayList<OrderDetail>();// 设置订单详情
			double amount = 0;
//			if (quoteOrderId != null) {
//				QuoteOrder quoteOrder = quoteOrderService.get(quoteOrderId);
//				List<QuoteOrderDetail> quoteOrderDetaillist = quoteOrderDetailService
//						.getListByQuoteOrder(quoteOrderId);
//				order.setCompanyId(SystemCache.getSalesman(
//						quoteOrder.getSalesmanId()).getCompanyId());// 设置订单公司
//				order.setSalesmanId(quoteOrder.getSalesmanId());// 设置订单业务员
//
//			}
			order.setAmount(NumberUtil.formateDouble(amount, 3));// 设置订单总金额
			// order.setDetaillist(orderDetaillist);//设置订单详情
			request.setAttribute("order", order);
			return new ModelAndView("order/add");
		} catch (Exception e) {
			throw e;
		}
	}

	/* 创建订单 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(Order order, String details,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加订单的权限", null);
		}
		Integer sampleId = order.getSampleId();
		if (sampleId == null) {
			throw new PermissionDeniedDataAccessException("您没有填写样品，创建订单失败",
					null);
		}
		try {
			Sample sample = sampleService.get(sampleId);
			order.setCost(sample.getCost());
			order.setImg(sample.getImg());
			order.setImg_s(sample.getImg_s());
			order.setImg_ss(sample.getImg_ss());
			order.setDetail(sample.getDetail());

			order.setCreated_at(DateTool.now());// 设置订单创建时间
			order.setUpdated_at(DateTool.now());// 设置订单更新时间
			order.setCreated_user(user.getId());// 设置订单创建人
			// 2014-11-10 修改 ，将初始状态设为待发货
			//order.setStatus(OrderStatus.CONFIRMSAMPLE.ordinal());//设置订单状态,订单创建时
			// ，默认打确认样状态
			// order.setState(OrderStatus.CONFIRMSAMPLE.getName());//设置订单状态描述
			order.setStatus(OrderStatus.DELIVERING.ordinal());// 设置订单状态,订单创建时，
																// 默认打确认样状态
			order.setState(OrderStatus.DELIVERING.getName());// 设置订单状态描述

			if (order.getSalesmanId() == null) {
				throw new Exception("业务员不能为空");
			}
			order.setCompanyId(SystemCache.getSalesman(order.getSalesmanId())
					.getCompanyId());// 设置订单公司
			if (order.getCompanyId() == null) {
				throw new Exception("公司不能为空");
			}

			double amount = order.getAmount();
			String info = order.getName() + "(" + order.getWeight() + "克)";

			order.setAmount(NumberUtil.formateDouble(amount, 3));// 设置订单总金额
			order.setInfo(info);// 设置订单信息

			// 添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setName("创建订单");
			handle.setState(order.getState());
			handle.setStatus(order.getStatus());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());

			// 2015-2-27添加颜色及数量
			List<OrderDetail> detaillist = SerializeTool.deserializeList(
					details, OrderDetail.class);
			for(int i = 0 ; i<detaillist.size();++i){
				detaillist.get(i).setId(i+1);
			}
			order.setDetaillist(detaillist);
			// 2015-2-27添加颜色及数量

			int orderId = orderService.add(order, handle);

			// 2015-3-2添加 创建订单时 自动创建计划单
			PlanOrder planOrder = new PlanOrder();
			planOrder.setOrderId(orderId);
			planOrder.setCreated_at(DateTool.now());// 设置创建时间
			planOrder.setUpdated_at(DateTool.now());// 设置更新时间
			planOrder.setCreated_user(user.getId());// 设置创建人

			List<PlanOrderDetail> plandetaillist = SerializeTool
					.deserializeList(details, PlanOrderDetail.class);
			for(int i = 0 ; i<plandetaillist.size();++i){
				plandetaillist.get(i).setId(i+1);
			}
			planOrder.setDetaillist(plandetaillist);
			int planOrderId = planOrderService.add(planOrder);
			// 2015-3-2添加 创建订单时 自动创建计划单

			// 2015-3-4创建订单时自动创建抽检记录单
			// 抽检记录单
			CheckRecordOrder checkRecordOrder = new CheckRecordOrder();
			checkRecordOrder.setOrderId(orderId);
			checkRecordOrder.setCreated_at(DateTool.now());// 设置创建时间
			checkRecordOrder.setUpdated_at(DateTool.now());// 设置更新时间
			checkRecordOrder.setCreated_user(user.getId());// 设置创建人
			checkRecordOrderService.add(checkRecordOrder);
			/*2015-3-23添加 新表格*/
			//生产进度单
			ProductionScheduleOrder productionScheduleOrder = new ProductionScheduleOrder();
			productionScheduleOrder.setOrderId(orderId);
			productionScheduleOrder.setCreated_at(DateTool.now());// 设置创建时间
			productionScheduleOrder.setUpdated_at(DateTool.now());// 设置更新时间
			productionScheduleOrder.setCreated_user(user.getId());// 设置创建人
			productionScheduleOrderService.add(productionScheduleOrder);
			
			//染色进度单
			ColoringProcessOrder coloringProcessOrder = new ColoringProcessOrder();
			coloringProcessOrder.setOrderId(orderId);
			coloringProcessOrder.setCreated_at(DateTool.now());// 设置创建时间
			coloringProcessOrder.setUpdated_at(DateTool.now());// 设置更新时间
			coloringProcessOrder.setCreated_user(user.getId());// 设置创建人
			coloringProcessOrderService.add(coloringProcessOrder);
			
			// 2015-3-4创建订单时自动创建半检记录单、抽检记录单

			// 2015-3-4创建计划单后，自动创建 质量记录单、车缝记录单、整烫记录单，2015-3-31添加 计划单创建后，自动创建半检记录单
			// 质量记录单
			HeadBankOrder headBankOrder = new HeadBankOrder();
			headBankOrder.setOrderId(orderId);
			headBankOrder.setCreated_at(DateTool.now());// 设置创建时间
			headBankOrder.setUpdated_at(DateTool.now());// 设置更新时间
			headBankOrder.setCreated_user(user.getId());// 设置创建人
			headBankOrderService.add(headBankOrder);

			// 车缝记录单
			CarFixRecordOrder carFixRecordOrder = new CarFixRecordOrder();
			carFixRecordOrder.setOrderId(orderId);
			carFixRecordOrder.setCreated_at(DateTool.now());// 设置创建时间
			carFixRecordOrder.setUpdated_at(DateTool.now());// 设置更新时间
			carFixRecordOrder.setCreated_user(user.getId());// 设置创建人
			carFixRecordOrderService.add(carFixRecordOrder);

			// 整烫记录单
			IroningRecordOrder ironingRecordOrder = new IroningRecordOrder();
			ironingRecordOrder.setOrderId(orderId);
			ironingRecordOrder.setCreated_at(DateTool.now());// 设置创建时间
			ironingRecordOrder.setUpdated_at(DateTool.now());// 设置更新时间
			ironingRecordOrder.setCreated_user(user.getId());// 设置创建人
			ironingRecordOrderService.add(ironingRecordOrder);
			
			// 半检记录单
			HalfCheckRecordOrder halfCheckRecordOrder = new HalfCheckRecordOrder();
			halfCheckRecordOrder.setOrderId(orderId);
			halfCheckRecordOrder.setCreated_at(DateTool.now());// 设置创建时间
			halfCheckRecordOrder.setUpdated_at(DateTool.now());// 设置更新时间
			halfCheckRecordOrder.setCreated_user(user.getId());// 设置创建人
			List<HalfCheckRecordOrderDetail2> halfCheckRecordOrderDetaillist2 = new ArrayList<HalfCheckRecordOrderDetail2>();
			for (OrderDetail detail : detaillist) {
				HalfCheckRecordOrderDetail2 temp = new HalfCheckRecordOrderDetail2();
				temp.setMaterial(detail.getYarn());
				temp.setColor(detail.getColor());
				temp.setColorsample("");
				halfCheckRecordOrderDetaillist2.add(temp);
			}
			halfCheckRecordOrder
					.setDetail_2_list(halfCheckRecordOrderDetaillist2);
			halfCheckRecordOrderService.add(halfCheckRecordOrder);
			
			// 2015-3-4创建计划单后，自动创建 质量记录单、车缝记录单、整烫记录单，2015-3-31添加 计划单创建后，自动创建半检记录单
			
			/*2015-3-23添加 新表格*/
			//成品仓库记录单
			FinalStoreOrder finalStoreOrder = new FinalStoreOrder();
			finalStoreOrder.setOrderId(orderId);
			finalStoreOrder.setCreated_at(DateTool.now());// 设置创建时间
			finalStoreOrder.setUpdated_at(DateTool.now());// 设置更新时间
			finalStoreOrder.setCreated_user(user.getId());// 设置创建人
			finalStoreOrderService.add(finalStoreOrder);
			
			//车间记录单
			ShopRecordOrder shopRecordOrder = new ShopRecordOrder();
			shopRecordOrder.setOrderId(orderId);
			shopRecordOrder.setCreated_at(DateTool.now());// 设置创建时间
			shopRecordOrder.setUpdated_at(DateTool.now());// 设置更新时间
			shopRecordOrder.setCreated_user(user.getId());// 设置创建人
			shopRecordOrderService.add(shopRecordOrder);
			
			
			return this.returnSuccess("id", orderId);
		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer charge_employeeId, Integer status,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看订单列表的权限", null);
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
		
		pager = orderService.getList(pager, start_time_d, end_time_d,
				companyId,charge_employeeId, status, sortList);
//		if (pager != null & pager.getResult() != null) {
//			List<FuliaoPurchaseOrder> orderlist = (List<FuliaoPurchaseOrder>) pager.getResult();
//		}
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("charge_employeeId", charge_employeeId);
		List<Employee> employeelist = new ArrayList<Employee>();
		for(Employee temp : SystemCache.employeelist){
			if(temp.getIs_charge_employee()){
				employeelist.add(temp);
			}		
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("companyId", companyId);
		request.setAttribute("status", status);
		request.setAttribute("pager", pager);
		return new ModelAndView("order/index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		String lcode = "order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看订单详情的权限", null);
		}

		if (id == null) {
			throw new Exception("缺少订单ID");
		}
		Order order = orderService.get(id);
//		List<OrderDetail> detaillist = orderDetailService.getListByOrder(id);// 获取订单详情
		// order.setDetaillist(detaillist);//设置订单详情
		// 获取订单步骤列表

		List<OrderProduceStatus> db_steplist = orderProduceStatusService
				.getListByOrder(order.getId());
		OrderStatus[] statuses = OrderStatus.values();

		List<OrderStep> stepList = new ArrayList<OrderStep>();
		for (OrderStatus status : statuses) {

			if (status.ordinal() >= OrderStatus.CANCEL.ordinal()) {
				break;
			}
			OrderStep temp = new OrderStep();
			temp.setOrderId(order.getId());
			temp.setState(status.getName());
			temp.setStatus(status.ordinal());
			temp.setStepId(null);
			if (order.getStepId() == null
					&& order.getStatus() == status.ordinal()) {
				temp.setChecked(true);
			} else {
				temp.setChecked(false);
			}
			stepList.add(temp);
			// 2014-11-10 修改，去掉了动态生产步骤
			// if(status == OrderStatus.MACHINING){//当是机织时，填充动态生产步骤
			// for(OrderProduceStatus orderProduceStatus : db_steplist){
			// OrderStep temp2 = new OrderStep();
			// temp2.setOrderId(order.getId());
			// temp2.setState(orderProduceStatus.getName());
			// temp2.setStatus(null);
			// temp2.setStepId(orderProduceStatus.getId());
			// if(order.getStepId() != null && order.getStepId() ==
			// orderProduceStatus.getId()){
			// temp2.setChecked(true);
			// }else{
			// temp2.setChecked(false);
			// }
			// stepList.add(temp2);
			// }
			// }
		}
		order.setStepList(stepList);// 设置订单步骤

		request.setAttribute("order", order);
		return new ModelAndView("order/detail");
	}

	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable int id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Order order = orderService.get(id);
//		List<OrderDetail> detaillist = orderDetailService.getListByOrder(id);
		// order.setDetaillist(detaillist);
		request.setAttribute("order", order);
		return new ModelAndView("order/edit");

	}

	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(Order order, String details,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑订单的权限", null);
		}
		try {
			Order db_order = orderService.get(order.getId());
			// 2014-11-10 修改：订单进入发货阶段，无法编辑
			// if(db_order.getStatus() >= OrderStatus.COLORING.ordinal()){
			// throw new Exception("订单已进入生产阶段，无法编辑");
			// }
			if (db_order.getStatus() >= OrderStatus.DELIVERED.ordinal()) {
				throw new Exception("订单已进入发货阶段，无法编辑");
			}

			order.setUpdated_at(DateTool.now());// 设置订单更新时间
			if (order.getSalesmanId() == null) {
				throw new Exception("业务员不能为空");
			}
			order.setCompanyId(SystemCache.getSalesman(order.getSalesmanId())
					.getCompanyId());// 设置订单公司
			if (order.getCompanyId() == null) {
				throw new Exception("公司不能为空");
			}

			// List<OrderDetail> orderDetaillist =
			// SerializeTool.deserializeList(order_details, OrderDetail.class);
			// if (orderDetaillist == null || orderDetaillist.size() <= 0) {
			// throw new Exception("订单中至少得有一条样品记录");
			// }
			double amount = order.getAmount();
			String info = order.getName() + "(" + order.getWeight() + "克)";
			order.setAmount(NumberUtil.formateDouble(amount, 3));// 设置订单总金额
			order.setInfo(info);// 设置订单信息

			// 添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setOrderId(order.getId());
			handle.setName("修改订单");
			handle.setState(order.getState());
			handle.setStatus(order.getStatus());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());

			// 2015-2-27添加颜色及数量
			List<OrderDetail> detaillist = SerializeTool.deserializeList(
					details, OrderDetail.class);
			order.setDetaillist(detaillist);			
			// 2015-2-27添加颜色及数量
			int orderId = orderService.update(order, handle);
			return this.returnSuccess("id", orderId);
		} catch (Exception e) {
			throw e;
		}

	}

	/* 2015-3-4添加取消订单 */
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cancel(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/cancel";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有取消订单的权限", null);
		}
		OrderHandle handle = new OrderHandle();
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());
		int success = orderService.cancel(id, handle);

		return this.returnSuccess();

	}

	@RequestMapping(value = "/tablelist", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView tablelist(Integer orderId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();

		try {
			Order order = orderService.get(orderId);

			// 获取头带质量记录单
			HeadBankOrder headBankOrder = headBankOrderService
					.getByOrder(orderId);

			// 获取生产单
			List<ProducingOrder> producingOrderList = producingOrderService
					.getByOrder(orderId);
			if(producingOrderList != null){
				for(ProducingOrder producingOrder : producingOrderList){
					//去掉生产单为数量为0的行
					Iterator iterator = producingOrder.getDetaillist().iterator();
					    while(iterator.hasNext()){
					    	ProducingOrderDetail item = (ProducingOrderDetail)iterator.next();
					           if(item.getQuantity() == 0){
					               iterator.remove();
					            }
					    }
					}
			}
			

			// 获取计划单
			PlanOrder planOrder = planOrderService.getByOrder(orderId);

			// 获取原材料仓库单
			StoreOrder storeOrder = storeOrderService.getByOrder(orderId);

			// 获取半检记录单
			HalfCheckRecordOrder halfCheckRecordOrder = halfCheckRecordOrderService
					.getByOrder(orderId);

			// 获取原材料采购单
			List<MaterialPurchaseOrder> materialPurchaseOrderList = materialPurchaseOrderService
					.getByOrder(orderId);

			// 获取染色单
			List<ColoringOrder> coloringOrderList = coloringOrderService
					.getByOrder(orderId);

			// 获取抽检记录单
			CheckRecordOrder checkRecordOrder = checkRecordOrderService
					.getByOrder(orderId);

			// 获取辅料采购单
			List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = fuliaoPurchaseOrderService
					.getByOrder(orderId);

			// 获取车缝记录单
			CarFixRecordOrder carFixRecordOrder = carFixRecordOrderService
					.getByOrder(orderId);

			// 获取整烫记录单
			IroningRecordOrder ironingRecordOrder = ironingRecordOrderService
					.getByOrder(orderId);
			
			//2015-3-24添加新表格
			//生产进度单
			ProductionScheduleOrder productionScheduleOrder = productionScheduleOrderService.getByOrder(orderId);
			request.setAttribute("productionScheduleOrder",productionScheduleOrder);
	
			//成品仓库记录单
			FinalStoreOrder finalStoreOrder = finalStoreOrderService.getByOrder(orderId);
			request.setAttribute("finalStoreOrder",finalStoreOrder);
			
			
			//车间记录单
			ShopRecordOrder shopRecordOrder = shopRecordOrderService.getByOrder(orderId);
			request.setAttribute("shopRecordOrder",shopRecordOrder);
			
			//染色进度单
			ColoringProcessOrder coloringProcessOrder = coloringProcessOrderService.getByOrder(orderId);	
			if(coloringProcessOrder!=null){
				List<ColoringProcessOrderDetail> coloringProcessOrderDetailList = new ArrayList<ColoringProcessOrderDetail>();
				for(ColoringOrder coloringOrder : coloringOrderList){
					Integer factoryId = coloringOrder.getFactoryId();
					List<ColoringOrderDetail> detaillist = coloringOrder.getDetaillist() == null ?  new ArrayList<ColoringOrderDetail>() : coloringOrder.getDetaillist();
					for(ColoringOrderDetail detail : detaillist){
						ColoringProcessOrderDetail temp = new ColoringProcessOrderDetail();
						temp.setColor(detail.getColor());
						temp.setFactoryId(factoryId);
						temp.setMaterial(detail.getMaterial());
						temp.setQuantity(detail.getQuantity());
						coloringProcessOrderDetailList.add(temp);
					}
					
					
				}
				coloringProcessOrder.setDetaillist(coloringProcessOrderDetailList);
				request.setAttribute("coloringProcessOrder",coloringProcessOrder);
			}
			
			

			request.setAttribute("order", order);
			request.setAttribute("headBankOrder", headBankOrder);
			request.setAttribute("producingOrderList", producingOrderList);
			request.setAttribute("planOrder", planOrder);
			request.setAttribute("storeOrder", storeOrder);

			request.setAttribute("halfCheckRecordOrder", halfCheckRecordOrder);
			request.setAttribute("materialPurchaseOrderList",
							materialPurchaseOrderList);
			request.setAttribute("coloringOrderList", coloringOrderList);
			request.setAttribute("checkRecordOrder", checkRecordOrder);
			request.setAttribute("fuliaoPurchaseOrderList", fuliaoPurchaseOrderList);
			request.setAttribute("carFixRecordOrder", carFixRecordOrder);
			request.setAttribute("ironingRecordOrder", ironingRecordOrder);

			return new ModelAndView("order/tablelist");
		} catch (Exception e) {
			throw e;
		}
	}

	
	// 添加或保存计划单

	@RequestMapping(value = "/planorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> planorder(PlanOrder planOrder, String details,/*
																			 * String
																			 * details_2
																			 * ,
																			 */
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/plan";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑计划单的权限", null);
		}
		try {
			Integer tableOrderId = planOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (planOrder.getOrderId() == null
						|| planOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"计划单必须属于一张订单", null);
				} else {
					PlanOrder temp = planOrderService.getByOrder(planOrder
							.getOrderId());
					if (temp != null) {
						throw new PermissionDeniedDataAccessException(
								"该订单已经存在计划单", null);
					}
				}

				planOrder.setCreated_at(DateTool.now());// 设置创建时间
				planOrder.setUpdated_at(DateTool.now());// 设置更新时间
				planOrder.setCreated_user(user.getId());// 设置创建人

				List<PlanOrderDetail> detaillist = SerializeTool
						.deserializeList(details, PlanOrderDetail.class);
				planOrder.setDetaillist(detaillist);
				tableOrderId = planOrderService.add(planOrder);
			} else {// 编辑
				planOrder.setUpdated_at(DateTool.now());
				List<PlanOrderDetail> detaillist = SerializeTool
						.deserializeList(details, PlanOrderDetail.class);
				planOrder.setDetaillist(detaillist);
				tableOrderId = planOrderService.update(planOrder);
			}
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}

	}

	// 添加或保存原材料仓库
	@RequestMapping(value = "/storeorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> storeorder(StoreOrder storeOrder,
			String details,/* String details_2, */HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/store";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料仓库单的权限",
					null);
		}
		try {
			Integer tableOrderId = storeOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (storeOrder.getOrderId() == null
						|| storeOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"原材料仓库单必须属于一张订单", null);
				} else {
					StoreOrder temp = storeOrderService.getByOrder(storeOrder
							.getOrderId());
					if (temp != null) {
						throw new PermissionDeniedDataAccessException(
								"该订单已经存在原材料仓库单", null);
					}
				}
				//2015-7-1添加订单相关属性
				Order order = orderService.get(storeOrder.getOrderId());
				storeOrder.setImg(order.getImg());
				storeOrder.setImg_s(order.getImg_s());
				storeOrder.setImg_ss(order.getImg_ss());
				storeOrder.setProductNumber(order.getProductNumber());
				storeOrder.setMaterialId(order.getMaterialId());
				storeOrder.setSize(order.getSize());
				storeOrder.setWeight(order.getWeight());
				storeOrder.setName(order.getName());
				storeOrder.setCompanyId(order.getCompanyId());
				storeOrder.setCustomerId(order.getCustomerId());
				storeOrder.setSampleId(order.getSampleId());
				storeOrder.setOrderNumber(order.getOrderNumber());
				storeOrder.setCharge_employee(order.getCharge_employee());
				storeOrder.setCompany_productNumber(order.getCompany_productNumber());
				
				storeOrder.setCreated_at(DateTool.now());// 设置创建时间
				storeOrder.setUpdated_at(DateTool.now());// 设置更新时间
				storeOrder.setCreated_user(user.getId());// 设置创建人

				List<StoreOrderDetail> detaillist = SerializeTool
						.deserializeList(details, StoreOrderDetail.class);
				storeOrder.setDetaillist(detaillist);
				tableOrderId = storeOrderService.add(storeOrder);
			} else {// 编辑
				storeOrder.setUpdated_at(DateTool.now());
				List<StoreOrderDetail> detaillist = SerializeTool
						.deserializeList(details, StoreOrderDetail.class);
				storeOrder.setDetaillist(detaillist);
				tableOrderId = storeOrderService.update(storeOrder);
			}
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}

	}


	// 添加订单步骤
	@RequestMapping(value = "addstep", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(OrderProduceStatus orderProduceStatus,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/addstep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加订单步骤的权限", null);
		}
		try {
			Integer orderId = orderProduceStatus.getOrderId();
			Order order = orderService.get(orderId);
			if (order.getStatus() == OrderStatus.DELIVERING.ordinal()) {
				throw new Exception("订单已完成生产，不能添加步骤");
			}
			if (order.getStatus() == OrderStatus.DELIVERED.ordinal()) {
				throw new Exception("订单已发货，不能添加步骤");
			}
			if (order.getStatus() == OrderStatus.COMPLETED.ordinal()) {
				throw new Exception("订单已交易完成，不能添加步骤");
			}
			if (order.getStatus() == OrderStatus.CANCEL.ordinal()) {
				throw new Exception("订单已取消，不能添加步骤");
			}

			orderProduceStatus.setCreated_at(DateTool.now());// 设置订单创建时间
			orderProduceStatus.setUpdated_at(DateTool.now());// 设置订单更新时间
			orderProduceStatus.setCreated_user(user.getId());// 设置订单创建人
			orderProduceStatus.setOrderId(orderId);

			// 添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setName("添加生产步骤");
			handle.setOrderId(orderProduceStatus.getOrderId());
			handle.setState(orderProduceStatus.getName());
			handle.setStatus(orderProduceStatus.getId());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());

			int stepId = orderService.addstep(orderProduceStatus, handle);
			return this.returnSuccess("id", stepId);
		} catch (Exception e) {
			throw e;
		}
	}

	// 修改订单步骤
	@RequestMapping(value = "/putstep", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(OrderProduceStatus orderProduceStatus,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Integer stepId = orderProduceStatus.getId();
		if (stepId == null) {
			throw new Exception("缺少步骤ID");
		}
		String lcode = "order/editstep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑订单步骤的权限", null);
		}
		try {
			if (orderProduceStatus.getOrderId() == null) {
				throw new Exception("缺少订单ID");
			}
			orderProduceStatus.setId(stepId);
			orderProduceStatus.setUpdated_at(DateTool.now());// 设置步骤更新时间

			// 添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setOrderId(orderProduceStatus.getOrderId());
			handle.setName("修改订单步骤信息");
			handle.setState(orderProduceStatus.getName());
			handle.setStatus(orderProduceStatus.getId());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());

			orderService.updatestep(orderProduceStatus, handle);
			return this.returnSuccess();
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除订单步骤
	@RequestMapping(value = "/deletestep/{stepId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable Integer stepId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		if (stepId == null) {
			throw new Exception("缺少步骤ID");
		}
		String lcode = "order/deletestep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除订单步骤的权限", null);
		}

		// 2014-11-10 以下几行删除：因为去掉了动态步骤
		// //删除时要做判断，若当前步骤已执行，则无法删除此步骤
		// OrderProduceStatus orderProduceStatus =
		// orderProduceStatusService.get(stepId);
		// Order order = orderService.get(orderProduceStatus.getOrderId());
		// if(order.getStatus() > OrderStatus.MACHINING.ordinal()){
		// throw new Exception("该步骤已执行，不能删除");
		// }
		// if(order.getStepId()!=null && order.getStepId() >
		// orderProduceStatus.getId() ){
		// throw new Exception("该步骤已执行，不能删除");
		// }
		OrderProduceStatus orderProduceStatus = orderProduceStatusService
				.get(stepId);

		// 添加操作记录

		OrderHandle handle = new OrderHandle();
		handle.setOrderId(orderProduceStatus.getOrderId());
		handle.setName("删除订单步骤信息");
		handle.setState(orderProduceStatus.getName());
		handle.setStatus(orderProduceStatus.getId());
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());

		orderService.deletestep(stepId, handle);

		return this.returnSuccess();
	}

	// 获取步骤详情
	@RequestMapping(value = "/getstep/{id}", method = RequestMethod.GET)
	@ResponseBody
	public OrderProduceStatus get(@PathVariable int id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String lcode = "order/detailstep";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看步骤详情的权限", null);
		}

		OrderProduceStatus OrderProduceStatus = orderProduceStatusService
				.get(id);
		return OrderProduceStatus;

	}

	// 执行订单步骤
	@RequestMapping(value = "/exestep/{orderId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> exeStep(@PathVariable Integer orderId,Date step_time,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		if (orderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "order/exestep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有执行订单的权限", null);
		}

		// 添加操作记录
		OrderHandle handle = new OrderHandle();
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());
		handle.setName("执行订单步骤");
		// 修改订单信息
		orderService.exestep(orderId, step_time,handle);

		return this.returnSuccess();
	}

	


	// 添加或保存半检记录单
	@RequestMapping(value = "/halfcheckrecordorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> halfcheckrecordorder(
			HalfCheckRecordOrder tableOrder, String details_2,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/halfcheckrecord";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半检记录单的权限",
					null);
		}
		try {
			Integer tableOrderId = tableOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (tableOrder.getOrderId() == null
						|| tableOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"半检记录单必须属于一张订单", null);
				} else {
					HalfCheckRecordOrder temp = halfCheckRecordOrderService
							.getByOrder(tableOrder.getOrderId());
					if (temp != null) {
						throw new PermissionDeniedDataAccessException(
								"该订单已经存在半检记录单", null);
					}
				}

				tableOrder.setCreated_at(DateTool.now());// 设置创建时间
				tableOrder.setUpdated_at(DateTool.now());// 设置更新时间
				tableOrder.setCreated_user(user.getId());// 设置创建人

				// List<HalfCheckRecordOrderDetail> detaillist =
				// SerializeTool.deserializeList(details,
				// HalfCheckRecordOrderDetail.class);
				List<HalfCheckRecordOrderDetail2> detaillist2 = SerializeTool
						.deserializeList(details_2,
								HalfCheckRecordOrderDetail2.class);
				// tableOrder.setDetaillist(detaillist);
				tableOrder.setDetail_2_list(detaillist2);
				tableOrderId = halfCheckRecordOrderService.add(tableOrder);
			} else {// 编辑
				tableOrder.setUpdated_at(DateTool.now());
				// List<HalfCheckRecordOrderDetail> detaillist =
				// SerializeTool.deserializeList(details,
				// HalfCheckRecordOrderDetail.class);
				List<HalfCheckRecordOrderDetail2> detaillist2 = SerializeTool
						.deserializeList(details_2,
								HalfCheckRecordOrderDetail2.class);
				// tableOrder.setDetaillist(detaillist);
				tableOrder.setDetail_2_list(detaillist2);
				tableOrderId = halfCheckRecordOrderService.update(tableOrder);
			}
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}

	}
	
	
	@RequestMapping(value = "/edit/memo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> editMemo(Integer id, String memo,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/edit/memo";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有修改订单备注的权限", null);
		}
		if(id == null){
			throw new Exception("订单号为空");
		}
		try {
			Order db_order = orderService.get(id);
			
			if (db_order.getStatus() >= OrderStatus.DELIVERED.ordinal()) {
				throw new Exception("订单已进入发货阶段，无法编辑");
			}

			db_order.setUpdated_at(DateTool.now());// 设置订单更新时间
			if(db_order.getMemo() == null || db_order.getMemo().equals("")){
				memo = memo + "(" + user.getName() + "," + DateTool.formatDateYMD(DateTool.now())+ ")" ;//备注  = 备注 + 修改人 + 修改时间
			}else{
				memo = db_order.getMemo() + "，<br>" + memo + "(" + user.getName() + ",于" + DateTool.formatDateYMD(DateTool.now())+ ")" ;//备注  = 备注 + 修改人 + 修改时间
			}
			
			db_order.setMemo(memo);//设置新的备注
			
			// 添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setOrderId(db_order.getId());
			handle.setName("待发货列表 -- 修改订单备注");
			handle.setState(db_order.getState());
			handle.setStatus(db_order.getStatus());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());

			id = orderService.update(db_order, handle);
			return this.returnSuccess("memo", db_order.getMemo());
		} catch (Exception e) {
			throw e;
		}

	}

	
	//判断是否有 色号、材料、领取人 重复  的  原材料仓库单
	@RequestMapping(value = "/isrepeat", method = RequestMethod.GET)
	@ResponseBody
	public void isRepeat(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<StoreOrder> storeOrderlist = storeOrderService.getList();
		
		List<StoreOrder> result = new ArrayList<StoreOrder>();
		for(StoreOrder order : storeOrderlist){
			List<StoreOrderDetail> detaillist = order.getDetaillist();
			if(detaillist == null){
				continue;
			}
		
			for  ( int  i  =   0 ; i  <  detaillist.size()  -   1 ; i ++ )   { 
				StoreOrderDetail detail_i = detaillist.get(i);
			    for  ( int  j  =  detaillist.size()  -   1 ; j  >  i; j -- )   { 
			    	StoreOrderDetail detail_j = detaillist.get(j);
			    	if(detail_j.getColor().trim().equals(detail_i.getColor().trim()) && detail_j.getMaterial().equals(detail_i.getMaterial())&& detail_j.getFactoryId().equals(detail_i.getFactoryId()))   { 
			    		result.add(order);
			    	} 
			    } 
			}
		}
			PrintWriter printWriter = response.getWriter();
			for(StoreOrder storeOrder : result){
				printWriter.write(storeOrder.getOrderId() + ",");
			}
		
		//request.setAttribute("result", "result");
	}
	/*
	 * @RequestMapping(value = "/headbank", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public Map<String, Object> headbank(HeadBankOrder
	 * headBankOrder, HttpSession session, HttpServletRequest request,
	 * HttpServletResponse response) throws Exception { User user =
	 * SystemContextUtils.getCurrentUser(session).getLoginedUser(); String lcode
	 * = "order/headbank"; Boolean hasAuthority =
	 * authorityService.checkLcode(user.getId(), lcode); if(!hasAuthority){
	 * throw new PermissionDeniedDataAccessException("没有创建或编辑质量记录单的权限", null); }
	 * try { Integer headBankOrderId = headBankOrder.getId();
	 * 
	 * if(headBankOrderId == null || headBankOrderId == 0){ //添加
	 * if(headBankOrder.getOrderId() == null || headBankOrder.getOrderId() ==
	 * 0){ throw new PermissionDeniedDataAccessException("质量记录单必须属于一张订单", null);
	 * }else{ HeadBankOrder temp =
	 * headBankOrderService.getByOrder(headBankOrder.getOrderId());
	 * if(temp!=null){ throw new
	 * PermissionDeniedDataAccessException("该订单已经存在质量记录单", null); } }
	 * 
	 * 
	 * headBankOrder.setCreated_at(DateTool.now());//设置创建时间
	 * headBankOrder.setUpdated_at(DateTool.now());//设置更新时间
	 * headBankOrder.setCreated_user(user.getId());//设置创建人
	 * 
	 * headBankOrderId = headBankOrderService.add(headBankOrder); }else{//编辑
	 * headBankOrder.setUpdated_at(DateTool.now()); headBankOrderId =
	 * headBankOrderService.update(headBankOrder); } return
	 * this.returnSuccess("id",headBankOrderId); } catch (Exception e) { throw
	 * e; }
	 * 
	 * }
	 * 
	 * //添加或保存抽检记录单
	 * 
	 * @RequestMapping(value = "/checkrecordorder", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public Map<String, Object>
	 * checkrecordorder(CheckRecordOrder tableOrder, HttpSession session,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { User user =
	 * SystemContextUtils.getCurrentUser(session).getLoginedUser(); String lcode
	 * = "order/checkrecord"; Boolean hasAuthority =
	 * authorityService.checkLcode(user.getId(), lcode); if(!hasAuthority){
	 * throw new PermissionDeniedDataAccessException("没有创建或编辑抽检记录单的权限", null); }
	 * try { Integer tableOrderId = tableOrder.getId();
	 * 
	 * if(tableOrderId == null || tableOrderId == 0){ //添加
	 * if(tableOrder.getOrderId() == null || tableOrder.getOrderId() == 0){
	 * throw new PermissionDeniedDataAccessException("抽检记录单必须属于一张订单", null);
	 * }else{ CheckRecordOrder temp =
	 * checkRecordOrderService.getByOrder(tableOrder.getOrderId());
	 * if(temp!=null){ throw new
	 * PermissionDeniedDataAccessException("该订单已经存在抽检记录单", null); } }
	 * 
	 * 
	 * tableOrder.setCreated_at(DateTool.now());//设置创建时间
	 * tableOrder.setUpdated_at(DateTool.now());//设置更新时间
	 * tableOrder.setCreated_user(user.getId());//设置创建人
	 * 
	 * // List<CheckRecordOrderDetail> detaillist =
	 * SerializeTool.deserializeList(details, CheckRecordOrderDetail.class); //
	 * tableOrder.setDetaillist(detaillist); tableOrderId =
	 * checkRecordOrderService.add(tableOrder); }else{//编辑
	 * tableOrder.setUpdated_at(DateTool.now()); // List<CheckRecordOrderDetail>
	 * detaillist = SerializeTool.deserializeList(details,
	 * CheckRecordOrderDetail.class); // tableOrder.setDetaillist(detaillist);
	 * tableOrderId = checkRecordOrderService.update(tableOrder); } return
	 * this.returnSuccess("id",tableOrderId); } catch (Exception e) { throw e; }
	 * 
	 * }
	 * 
	 * //添加或保存车缝记录单
	 * 
	 * @RequestMapping(value = "/carfixrecordorder", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public Map<String, Object>
	 * carfixrecordorder(CarFixRecordOrder tableOrder, HttpSession session,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { User user =
	 * SystemContextUtils.getCurrentUser(session).getLoginedUser(); String lcode
	 * = "order/carfixrecord"; Boolean hasAuthority =
	 * authorityService.checkLcode(user.getId(), lcode); if(!hasAuthority){
	 * throw new PermissionDeniedDataAccessException("没有创建或编辑车缝记录单的权限", null); }
	 * try { Integer tableOrderId = tableOrder.getId();
	 * 
	 * if(tableOrderId == null || tableOrderId == 0){ //添加
	 * if(tableOrder.getOrderId() == null || tableOrder.getOrderId() == 0){
	 * throw new PermissionDeniedDataAccessException("车缝记录单必须属于一张订单", null);
	 * }else{ CarFixRecordOrder temp =
	 * carFixRecordOrderService.getByOrder(tableOrder.getOrderId());
	 * if(temp!=null){ throw new
	 * PermissionDeniedDataAccessException("该订单已经存在车缝记录单", null); } }
	 * 
	 * 
	 * tableOrder.setCreated_at(DateTool.now());//设置创建时间
	 * tableOrder.setUpdated_at(DateTool.now());//设置更新时间
	 * tableOrder.setCreated_user(user.getId());//设置创建人
	 * 
	 * // List<CarFixRecordOrderDetail> detaillist =
	 * SerializeTool.deserializeList(details, CarFixRecordOrderDetail.class); //
	 * tableOrder.setDetaillist(detaillist); tableOrderId =
	 * carFixRecordOrderService.add(tableOrder); }else{//编辑
	 * tableOrder.setUpdated_at(DateTool.now()); //
	 * List<CarFixRecordOrderDetail> detaillist =
	 * SerializeTool.deserializeList(details, CarFixRecordOrderDetail.class); //
	 * tableOrder.setDetaillist(detaillist); tableOrderId =
	 * carFixRecordOrderService.update(tableOrder); } return
	 * this.returnSuccess("id",tableOrderId); } catch (Exception e) { throw e; }
	 * 
	 * }
	 * 
	 * //添加或保存整烫记录单
	 * 
	 * @RequestMapping(value = "/ironingrecordorder", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public Map<String, Object>
	 * ironingrecordorder(IroningRecordOrder tableOrder,HttpSession session,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { User user =
	 * SystemContextUtils.getCurrentUser(session).getLoginedUser(); String lcode
	 * = "order/ironingrecord"; Boolean hasAuthority =
	 * authorityService.checkLcode(user.getId(), lcode); if(!hasAuthority){
	 * throw new PermissionDeniedDataAccessException("没有创建或编辑整烫记录单的权限", null); }
	 * try { Integer tableOrderId = tableOrder.getId();
	 * 
	 * if(tableOrderId == null || tableOrderId == 0){ //添加
	 * if(tableOrder.getOrderId() == null || tableOrder.getOrderId() == 0){
	 * throw new PermissionDeniedDataAccessException("整烫记录单必须属于一张订单", null);
	 * }else{ IroningRecordOrder temp =
	 * ironingRecordOrderService.getByOrder(tableOrder.getOrderId());
	 * if(temp!=null){ throw new
	 * PermissionDeniedDataAccessException("该订单已经存在整烫记录单", null); } }
	 * 
	 * 
	 * tableOrder.setCreated_at(DateTool.now());//设置创建时间
	 * tableOrder.setUpdated_at(DateTool.now());//设置更新时间
	 * tableOrder.setCreated_user(user.getId());//设置创建人
	 * 
	 * // List<IroningRecordOrderDetail> detaillist =
	 * SerializeTool.deserializeList(details, IroningRecordOrderDetail.class);
	 * // tableOrder.setDetaillist(detaillist); tableOrderId =
	 * ironingRecordOrderService.add(tableOrder); }else{//编辑
	 * tableOrder.setUpdated_at(DateTool.now()); //
	 * List<IroningRecordOrderDetail> detaillist =
	 * SerializeTool.deserializeList(details, IroningRecordOrderDetail.class);
	 * // tableOrder.setDetaillist(detaillist); tableOrderId =
	 * ironingRecordOrderService.update(tableOrder); } return
	 * this.returnSuccess("id",tableOrderId); } catch (Exception e) { throw e; }
	 * 
	 * }
	 */
}
