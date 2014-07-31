package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.OrderHandle;
import com.fuwei.entity.OrderProduceStatus;
import com.fuwei.entity.OrderStep;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderDetailService;
import com.fuwei.service.OrderHandleService;
import com.fuwei.service.OrderProduceStatusService;
import com.fuwei.service.OrderService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.service.QuoteOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/order")
@Controller
public class OrderController extends BaseController {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderDetailService orderDetailService;
	@Autowired
	OrderHandleService orderHandleService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	QuoteOrderService quoteOrderService;
	@Autowired
	QuoteOrderDetailService quoteOrderDetailService;
	@Autowired
	OrderProduceStatusService orderProduceStatusService;
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(Integer quoteOrderId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Order order = new Order();
		try {
			order.setCreated_at(DateTool.now());//设置订单创建时间
			order.setUpdated_at(DateTool.now());//设置订单更新时间
			order.setCreated_user(user.getId());//设置订单创建人
			order.setStatus(OrderStatus.CREATE.ordinal());//设置订单状态
			order.setState(OrderStatus.CREATE.getName());//设置订单状态描述			
			order.setAmount(0);//设置订单总金额
			List<OrderDetail> orderDetaillist = new ArrayList<OrderDetail>();//设置订单详情
			double amount = 0;
			if(quoteOrderId!=null){
				QuoteOrder quoteOrder = quoteOrderService.get(quoteOrderId);
				List<QuoteOrderDetail> quoteOrderDetaillist = quoteOrderDetailService.getListByQuoteOrder(quoteOrderId);
				order.setCompanyId(SystemCache.getSalesman(quoteOrder.getSalesmanId()).getCompanyId());//设置订单公司
				order.setSalesmanId(quoteOrder.getSalesmanId());//设置订单业务员
				if (quoteOrderDetaillist == null) {
					quoteOrderDetaillist = new ArrayList<QuoteOrderDetail>();
				}
				
				for (QuoteOrderDetail quoteOrderDetail : quoteOrderDetaillist) {
					OrderDetail detail = new OrderDetail();
					detail.setCproductN(quoteOrderDetail.getCproductN());
					detail.setPrice(NumberUtil.formateDouble(quoteOrderDetail.getPrice(),3));//保留三位小数
					detail.setQuantity(1000);
					detail.setAmount(NumberUtil.formateDouble(detail.getQuantity() * detail.getPrice(), 3));//保留三位小数
					amount += detail.getAmount();
					detail.setMemo(quoteOrderDetail.getMemo());
					detail.setSampleId(quoteOrderDetail.getSampleId());
					detail.setName(quoteOrderDetail.getName());
					detail.setImg(quoteOrderDetail.getImg());
					detail.setImg_s(quoteOrderDetail.getImg_s());
					detail.setImg_ss(quoteOrderDetail.getImg_ss());
					detail.setMaterial(quoteOrderDetail.getMaterial());
					detail.setMachine(quoteOrderDetail.getMachine());
					detail.setWeight(quoteOrderDetail.getWeight());
					detail.setSize(quoteOrderDetail.getSize());
					detail.setCost(quoteOrderDetail.getCost());
					detail.setProductNumber(quoteOrderDetail.getProductNumber());
					detail.setMachine(quoteOrderDetail.getMachine());
					detail.setCharge_user(quoteOrderDetail.getCharge_user());
					detail.setDetail(quoteOrderDetail.getDetail());
					orderDetaillist.add(detail);
				}
				
			}
			order.setAmount(NumberUtil.formateDouble(amount,3));//设置订单总金额
			order.setDetaillist(orderDetaillist);//设置订单详情
			request.setAttribute("order", order);
			return new ModelAndView("order/add");
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(Order order,String order_details, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加订单的权限", null);
		}
		try {
			order.setCreated_at(DateTool.now());//设置订单创建时间
			order.setUpdated_at(DateTool.now());//设置订单更新时间
			order.setCreated_user(user.getId());//设置订单创建人
			order.setStatus(OrderStatus.CONFIRMSAMPLE.ordinal());//设置订单状态,订单创建时，默认打确认样状态
			order.setState(OrderStatus.CONFIRMSAMPLE.getName());//设置订单状态描述
			
			if(order.getSalesmanId() == null){
				throw new Exception("业务员不能为空");
			}
			order.setCompanyId(SystemCache.getSalesman(order.getSalesmanId()).getCompanyId());//设置订单公司
			if(order.getCompanyId() == null){
				throw new Exception("公司不能为空");
			}
			
			List<OrderDetail> orderDetaillist = SerializeTool.deserializeList(order_details, OrderDetail.class);
			if (orderDetaillist == null || orderDetaillist.size() <= 0) {
				throw new Exception("订单中至少得有一条样品记录");
			}
			double amount = 0;
			String info = "";
			for (OrderDetail orderDetail : orderDetaillist) {
				orderDetail.setPrice(NumberUtil.formateDouble(orderDetail.getPrice(),3));
				orderDetail.setAmount(NumberUtil.formateDouble(orderDetail.getQuantity() * orderDetail.getPrice(), 3));//保留三位小数
				amount += orderDetail.getAmount();
				info += orderDetail.getName()+"(" + orderDetail.getWeight() + "克)";
			}
			order.setAmount(NumberUtil.formateDouble(amount,3));//设置订单总金额
			order.setInfo(info);//设置订单信息
			order.setDetaillist(orderDetaillist);//设置订单详情
			
			//添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setName("创建订单");
			handle.setState(order.getState());
			handle.setStatus(order.getStatus());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());
			
			int orderId = orderService.add(order,handle);
			return this.returnSuccess("id",orderId);
		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId,Integer salesmanId,
			Integer status,String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		
		String lcode = "order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
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
		if(sortList == null){
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		pager = orderService.getList(pager, start_time_d, end_time_d,companyId,salesmanId,status,
				sortList);
		if (pager != null & pager.getResult() != null) {
			List<Order> orderlist = (List<Order>) pager
			.getResult();
		}
	
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if(companyId==null & salesmanId!=null){
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("status", status);
		request.setAttribute("pager", pager);
		return new ModelAndView("order/index");
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id,HttpSession session, HttpServletRequest request) throws Exception{
		String lcode = "order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看订单详情的权限", null);
		}
		
		if(id == null){
			throw new Exception("缺少订单ID");
		}
		Order order = orderService.get(id);
		List<OrderDetail> detaillist = orderDetailService.getListByOrder(id);//获取订单详情
		order.setDetaillist(detaillist);//设置订单详情
		//获取订单步骤列表
		
		List<OrderProduceStatus> db_steplist = orderProduceStatusService.getListByOrder(order.getId());
		OrderStatus[] statuses = OrderStatus.values();
		
		List<OrderStep> stepList = new ArrayList<OrderStep>();
		for(OrderStatus status : statuses){
			
			if(status.ordinal() >= OrderStatus.CANCEL.ordinal()){
				break;
			}
			OrderStep temp = new OrderStep();
			temp.setOrderId(order.getId());
			temp.setState(status.getName());
			temp.setStatus(status.ordinal());
			temp.setStepId(null);
			if(order.getStepId() == null && order.getStatus() == status.ordinal()){
				temp.setChecked(true);
			}else{
				temp.setChecked(false);
			}
			stepList.add(temp);
			if(status == OrderStatus.MACHINING){//当是机织时，填充动态生产步骤
				for(OrderProduceStatus orderProduceStatus : db_steplist){
					OrderStep temp2 = new OrderStep();
					temp2.setOrderId(order.getId());
					temp2.setState(orderProduceStatus.getName());
					temp2.setStatus(null);
					temp2.setStepId(orderProduceStatus.getId());
					if(order.getStepId() != null && order.getStepId() == orderProduceStatus.getId()){
						temp2.setChecked(true);
					}else{
						temp2.setChecked(false);
					}
					stepList.add(temp2);
				}
			}
		}
		order.setStepList(stepList);//设置订单步骤
		
		request.setAttribute("order", order);
		return new ModelAndView("order/detail");
	}
	
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Order order = orderService.get(id);
		List<OrderDetail> detaillist = orderDetailService.getListByOrder(id);
		order.setDetaillist(detaillist);
		request.setAttribute("order", order);
		return new ModelAndView("order/edit");
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(Order order,String order_details, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑订单的权限", null);
		}
		try {
			Order db_order = orderService.get(order.getId());
			if(db_order.getStatus() >= OrderStatus.COLORING.ordinal()){
				throw new Exception("订单已进入生产阶段，无法编辑");
			}
			order.setUpdated_at(DateTool.now());//设置订单更新时间			
			if(order.getSalesmanId() == null){
				throw new Exception("业务员不能为空");
			}
			order.setCompanyId(SystemCache.getSalesman(order.getSalesmanId()).getCompanyId());//设置订单公司
			if(order.getCompanyId() == null){
				throw new Exception("公司不能为空");
			}
			
			List<OrderDetail> orderDetaillist = SerializeTool.deserializeList(order_details, OrderDetail.class);
			if (orderDetaillist == null || orderDetaillist.size() <= 0) {
				throw new Exception("订单中至少得有一条样品记录");
			}
			double amount = 0;
			String info = "";
			for (OrderDetail orderDetail : orderDetaillist) {
				orderDetail.setPrice(NumberUtil.formateDouble(orderDetail.getPrice(),3));
				orderDetail.setAmount(NumberUtil.formateDouble(orderDetail.getQuantity() * orderDetail.getPrice(), 3));//保留三位小数
				amount += orderDetail.getAmount();
				info += orderDetail.getName()+"(" + orderDetail.getWeight() + "克)";
				orderDetail.setOrderId(order.getId());
			}
			order.setAmount(NumberUtil.formateDouble(amount,3));//设置订单总金额
			order.setInfo(info);//设置订单信息
			order.setDetaillist(orderDetaillist);//设置订单详情
			
			//添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setOrderId(order.getId());
			handle.setName("修改订单");
			handle.setState(order.getState());
			handle.setStatus(order.getStatus());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());
			
			int orderId = orderService.update(order,handle);
			return this.returnSuccess("id",orderId);
		} catch (Exception e) {
			throw e;
		}

	}
	
	
	//添加订单步骤
	@RequestMapping(value = "addstep", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(OrderProduceStatus orderProduceStatus, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/addstep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加订单步骤的权限", null);
		}
		try {
			Integer orderId = orderProduceStatus.getOrderId();
			Order order = orderService.get(orderId);
			if(order.getStatus() == OrderStatus.DELIVERING.ordinal()){
				throw new Exception("订单已完成生产，不能添加步骤");
			}
			if(order.getStatus() == OrderStatus.DELIVERED.ordinal()){
				throw new Exception("订单已发货，不能添加步骤");
			}
			if(order.getStatus() == OrderStatus.COMPLETED.ordinal()){
				throw new Exception("订单已交易完成，不能添加步骤");
			}
			if(order.getStatus() == OrderStatus.CANCEL.ordinal()){
				throw new Exception("订单已取消，不能添加步骤");
			}
			
			orderProduceStatus.setCreated_at(DateTool.now());//设置订单创建时间
			orderProduceStatus.setUpdated_at(DateTool.now());//设置订单更新时间
			orderProduceStatus.setCreated_user(user.getId());//设置订单创建人
			orderProduceStatus.setOrderId(orderId);		
			
			//添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setName("添加生产步骤");
			handle.setOrderId(orderProduceStatus.getOrderId());
			handle.setState(orderProduceStatus.getName());
			handle.setStatus(orderProduceStatus.getId());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());
			
			int stepId = orderService.addstep(orderProduceStatus, handle);
			return this.returnSuccess("id",stepId);
		} catch (Exception e) {
			throw e;
		}
	} 
	
	//修改订单步骤
	@RequestMapping(value = "/putstep", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(OrderProduceStatus orderProduceStatus, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Integer stepId = orderProduceStatus.getId();
		if(stepId == null){
			throw new Exception("缺少步骤ID");
		}
		String lcode = "order/editstep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑订单步骤的权限", null);
		}
		try {
			if(orderProduceStatus.getOrderId() == null){
				throw new Exception("缺少订单ID");
			}
			orderProduceStatus.setId(stepId);
			orderProduceStatus.setUpdated_at(DateTool.now());//设置步骤更新时间			

			
			//添加操作记录
			OrderHandle handle = new OrderHandle();
			handle.setOrderId(orderProduceStatus.getOrderId());
			handle.setName("修改订单步骤信息");
			handle.setState(orderProduceStatus.getName());
			handle.setStatus(orderProduceStatus.getId());
			handle.setCreated_at(DateTool.now());
			handle.setCreated_user(user.getId());
			
			orderService.updatestep(orderProduceStatus,handle);
			return this.returnSuccess();
		} catch (Exception e) {
			throw e;
		}
	}
	
	//删除订单步骤
	@RequestMapping(value = "/deletestep/{stepId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable Integer stepId,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		if(stepId == null){
			throw new Exception("缺少步骤ID");
		}
		String lcode = "order/deletestep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除订单步骤的权限", null);
		}
		
		//删除时要做判断，若当前步骤已执行，则无法删除此步骤
		OrderProduceStatus orderProduceStatus = orderProduceStatusService.get(stepId);
		Order order = orderService.get(orderProduceStatus.getOrderId());
		if(order.getStatus() > OrderStatus.MACHINING.ordinal()){
			throw new Exception("该步骤已执行，不能删除");
		}
		if(order.getStepId()!=null && order.getStepId() > orderProduceStatus.getId() ){
			throw new Exception("该步骤已执行，不能删除");
		}
		
		//添加操作记录
		
		OrderHandle handle = new OrderHandle();
		handle.setOrderId(orderProduceStatus.getOrderId());
		handle.setName("删除订单步骤信息");
		handle.setState(orderProduceStatus.getName());
		handle.setStatus(orderProduceStatus.getId());
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());
		
		orderService.deletestep(stepId,handle);
		
		return this.returnSuccess();		
	}
	
	//获取步骤详情
	@RequestMapping(value = "/getstep/{id}", method = RequestMethod.GET)
	@ResponseBody
	public OrderProduceStatus get(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		String lcode = "order/detailstep";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看步骤详情的权限", null);
		}
		
		OrderProduceStatus OrderProduceStatus = orderProduceStatusService.get(id);
		return OrderProduceStatus;
		
	}
	
	//执行订单步骤
	@RequestMapping(value = "/exestep/{orderId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> exeStep(@PathVariable Integer orderId,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		if(orderId == null){
			throw new Exception("缺少订单ID");
		}
		String lcode = "order/exestep";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有执行订单的权限", null);
		}
		
		//添加操作记录
		OrderHandle handle = new OrderHandle();
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());
		handle.setName("执行订单步骤");
		//修改订单信息
		orderService.exestep(orderId,handle);
		
		return this.returnSuccess();		
	}
}
