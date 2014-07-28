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
import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderDetailService;
import com.fuwei.service.OrderHandleService;
import com.fuwei.service.OrderService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.service.QuoteOrderService;
import com.fuwei.util.DateTool;
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
	
	
	@RequestMapping(value = "/add/{quoteOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable Integer quoteOrderId, HttpSession session,
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
			order.setStatus(OrderStatus.CREATE.ordinal());//设置订单状态
			order.setState(OrderStatus.CREATE.getName());//设置订单状态描述
			
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
			int orderId = orderService.add(order);
			return this.returnSuccess("id",orderId);
		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId,Integer salesmanId,
			String sortJSON, HttpSession session, HttpServletRequest request)
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
		pager = orderService.getList(pager, start_time_d, end_time_d,companyId,salesmanId,
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
		List<OrderDetail> detaillist = orderDetailService.getListByOrder(id);
		order.setDetaillist(detaillist);
		request.setAttribute("order", order);
		return new ModelAndView("quoteorder/detail");
	}
	
}
