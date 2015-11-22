package com.fuwei.controller.ordergrid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Message;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.PlanOrderService;
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
	@Autowired
	PlanOrderService planOrderService;

	@RequestMapping(value = "/unprice_list", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView unprice_list(String orderNumber,String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "order/producing/price_edit";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		String lcode2 = "order/producing/price_request";
		Boolean hasAuthority2 = SystemCache.hasAuthority(session, lcode2);
		if (!hasAuthority && !hasAuthority2) {
			throw new PermissionDeniedDataAccessException("没有查看待划价生产单列表的权限", null);
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
		List<ProducingOrder> list = producingOrderService.getUnpriceList(orderNumber,sortList);
		
		List<ProducingOrder> resultlist = new ArrayList<ProducingOrder>();
		for(ProducingOrder item : list){
			List<ProducingOrderDetail> detaillist = item.getDetaillist();
			for(ProducingOrderDetail detail : detaillist){
				if(detail.getPrice() == 0){
					resultlist.add(item);
					break;
				}
			}
		}
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("list", resultlist);
		return new ModelAndView("producing_order/unpricelist");
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, Integer factoryId,String orderNumber,
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
		pager = producingOrderService.getList(pager, start_time_d, end_time_d,companyId,factoryId,orderNumber, 
				sortList);
		if (pager != null & pager.getResult() != null) {
			List<ProducingOrder> orderlist = (List<ProducingOrder>) pager
					.getResult();
		}
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("orderNumber", orderNumber);
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
		String lcode = "producing_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看生产单详情的权限",
					null);
		}
		ProducingOrder producingOrder = producingOrderService.get(id);
		if(producingOrder == null){
			throw new Exception("找不到ID为" + id + "的生产单");
		}
		//去掉生产单为数量为0的行
		Iterator iterator = producingOrder.getDetaillist().iterator();
	    while(iterator.hasNext()){
	    	ProducingOrderDetail item = (ProducingOrderDetail)iterator.next();
	           if(item.getQuantity() == 0){
	               iterator.remove();
	            }
	    }
		
//		Order order = orderService.get(producingOrder.getOrderId());
//		request.setAttribute("order", order);

		List<ProducingOrder> producingOrderList = new ArrayList<ProducingOrder>();
		producingOrderList.add(producingOrder);
		request.setAttribute("producingOrderList", producingOrderList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gridName", "producingorder");
		return new ModelAndView("printorder/preview", data);
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

	
	
	// 添加或保存生产单

	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑生产单的权限", null);
		}
		try {
			Order order = orderService.get(orderId);
			PlanOrder planOrder = planOrderService.getByOrder(orderId);

			if (planOrder == null) {
				throw new PermissionDeniedDataAccessException("请先创建计划单", null);
			}
			if (planOrder.getDetaillist() == null
					|| planOrder.getDetaillist().size() <= 0) {
				throw new PermissionDeniedDataAccessException(
						"计划单缺少颜色及数量明细，请先修改计划单 ", null);
			}
			List<ProducingOrder> producingOrderList = producingOrderService
					.getByOrder(orderId);
			
			HashMap<Integer,Integer> hashmap = new HashMap<Integer,Integer>();
			for (ProducingOrder producingorder : producingOrderList) {
				for (ProducingOrderDetail temp : producingorder.getDetaillist()) {
					int planOrderDetailId = temp.getPlanOrderDetailId();
					Integer value = hashmap.get(planOrderDetailId);
					if(value!=null){
						hashmap.put(planOrderDetailId, value + temp.getQuantity());
					}else{
						hashmap.put(planOrderDetailId, temp.getQuantity());
					}
				}
			}

			List<ProducingOrderDetail> detaillist = new ArrayList<ProducingOrderDetail>();
			for (PlanOrderDetail planOrderDetail : planOrder.getDetaillist()) {
				ProducingOrderDetail producingorderdetail = new ProducingOrderDetail();
				producingorderdetail.setColor(planOrderDetail.getColor());
				producingorderdetail.setPlanOrderDetailId(planOrderDetail.getId());
				producingorderdetail.setSize(planOrderDetail.getSize());
				producingorderdetail.setPrice(0);
				producingorderdetail.setWeight(planOrderDetail.getWeight());
				producingorderdetail.setYarn(planOrderDetail.getYarn());
				producingorderdetail.setProduce_weight(planOrderDetail.getProduce_weight());
				Integer quantity = hashmap.get(planOrderDetail.getId());
				quantity = quantity == null? 0 : quantity;
				producingorderdetail.setQuantity(planOrderDetail.getQuantity()- quantity);
				detaillist.add(producingorderdetail);
			}
			request.setAttribute("order", order);
			request.setAttribute("detaillist", detaillist);
			return new ModelAndView("producing_order/addbyorder");
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加或保存生产单
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(ProducingOrder producingOrder,
			String details, String details_2, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑生产单的权限", null);
		}
		try {
			Integer tableOrderId = producingOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (producingOrder.getOrderId() == null
						|| producingOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"生产单必须属于一张订单", null);
				}
				if (producingOrder.getFactoryId() == null
						|| producingOrder.getFactoryId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"生产单必须指定生产单位", null);
				} 
				Order order = orderService.get(producingOrder.getOrderId());
				if(order == null){
					throw new PermissionDeniedDataAccessException(
							"订单不存在", null);
				}
				
				//判断生产数量是否超出了计划数量
				//判断生产数量是否超出了计划数量
				
				producingOrder.setCreated_at(DateTool.now());// 设置创建时间
				producingOrder.setUpdated_at(DateTool.now());// 设置更新时间
				producingOrder.setCreated_user(user.getId());// 设置创建人
				
				//2015-6-18添加订单相关属性
				producingOrder.setImg(order.getImg());
				producingOrder.setImg_s(order.getImg_s());
				producingOrder.setImg_ss(order.getImg_ss());
				producingOrder.setProductNumber(order.getProductNumber());
				producingOrder.setMaterialId(order.getMaterialId());
				producingOrder.setSize(order.getSize());
				producingOrder.setWeight(order.getWeight());
				producingOrder.setName(order.getName());
				producingOrder.setCompanyId(order.getCompanyId());
				producingOrder.setCustomerId(order.getCustomerId());
				producingOrder.setSampleId(order.getSampleId());
				producingOrder.setOrderNumber(order.getOrderNumber());
				producingOrder.setCharge_employee(order.getCharge_employee());
			
				producingOrder.setCompany_productNumber(order.getCompany_productNumber());
				
				List<ProducingOrderDetail> detaillist = SerializeTool
						.deserializeList(details, ProducingOrderDetail.class);
				List<ProducingOrderMaterialDetail> detaillist2 = SerializeTool
						.deserializeList(details_2,
								ProducingOrderMaterialDetail.class);
				producingOrder.setDetaillist(detaillist);
				producingOrder.setDetail_2_list(detaillist2);
				tableOrderId = producingOrderService.add(producingOrder);
			} else {// 编辑
				//判断生产数量是否超出了计划数量
				//判断生产数量是否超出了计划数量
				
				producingOrder.setUpdated_at(DateTool.now());
				List<ProducingOrderDetail> detaillist = SerializeTool
						.deserializeList(details, ProducingOrderDetail.class);
				List<ProducingOrderMaterialDetail> detaillist2 = SerializeTool
						.deserializeList(details_2,
								ProducingOrderMaterialDetail.class);
				producingOrder.setDetaillist(detaillist);
				producingOrder.setDetail_2_list(detaillist2);
				tableOrderId = producingOrderService.update(producingOrder);
			}
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}

	}

	//2015-3-31 删除生产单
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除生产单的权限", null);
		}
		int success = producingOrderService.remove(id);
		
		return this.returnSuccess();
	}

	@RequestMapping(value = "/put/{producingOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer producingOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑生产单的权限", null);
		}
		try {
			if(producingOrderId!=null){
				ProducingOrder producingOrder = producingOrderService.get(producingOrderId);
				request.setAttribute("producingOrder", producingOrder);
				if(producingOrder.getOrderId()!=null){
					Order order = orderService.get(producingOrder.getOrderId());
					request.setAttribute("order", order);
					return new ModelAndView("producing_order/editbyorder");
				}else{
					throw new Exception("发生错误：生产单缺少订单ID");
					//return new ModelAndView("producing_order/edit");
				}
				
			}
			throw new Exception("缺少生产单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/price/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView price(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少生产单ID");
		}
		String lcode = "order/producing/price_edit";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有划价的权限",
					null);
		}
		ProducingOrder producingOrder = producingOrderService.get(id);
		if(producingOrder == null){
			throw new Exception("找不到ID为"+id+ "的生产单");
		}
		Iterator<ProducingOrderDetail> iter = producingOrder.getDetaillist().iterator();
		while(iter.hasNext()){
			ProducingOrderDetail detail = iter.next();
			if(detail.getQuantity() == 0){
				iter.remove();
			}
		}
		request.setAttribute("producingOrder", producingOrder);
		Order order = orderService.get(producingOrder.getOrderId());
		request.setAttribute("order", order);
		return new ModelAndView("producing_order/price");
	}
	// 2015-4-4完成划价 ， 2015-6-18修改
	@RequestMapping(value = "/price", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> price_complete(ProducingOrder producingOrder, String details,String details_2,
			HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/producing/price_edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有划价的权限", null);
		}
		
		//保存生产单数据
		producingOrder.setUpdated_at(DateTool.now());
		List<ProducingOrderDetail> detaillist = SerializeTool
				.deserializeList(details,
						ProducingOrderDetail.class);
		producingOrder.setDetaillist(detaillist);
		
		//检查价格中是否有0
		for(ProducingOrderDetail item : detaillist){
			if(item.getPrice() == 0){
				throw new Exception("价格为0，划价失败");
			}
		}
		
		List<ProducingOrderMaterialDetail> detaillist2 = SerializeTool
		.deserializeList(details_2,
				ProducingOrderMaterialDetail.class);
		producingOrder.setDetail_2_list(detaillist2);
		
		Integer tableOrderId = producingOrderService.update(producingOrder);
		int orderId = producingOrder.getOrderId();
		ProducingOrder producingOrderTemp = producingOrderService.get(tableOrderId);
		
		
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
			message.setContent("订单 " + producingOrderTemp.getOrderNumber()
					+ " 的<strong> 生产单"+ producingOrderTemp.getId() + "<strong> <strong>已完成划价<strong>，点击打开链接");
			messageService.add(message);
			SystemCache.setUserCacheUpdate(message.getTo_user_id(),true);
		}

		return this.returnSuccess();

	}
	
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session,
			HttpServletRequest request) throws Exception {
		return new ModelAndView("producing_order/scan");
	}
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail2(String number, HttpSession session,
			HttpServletRequest request) throws Exception {
		ProducingOrder producingOrder = producingOrderService.getByNumber(number);
		if(producingOrder == null){
			throw new Exception("找不到生产单号为"+number+"的生产单");
		}
		return detail(producingOrder.getId(), session, request);
	}

	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少生产单ID");
		}	
		ProducingOrder producingOrder = producingOrderService.get(id);
		if(producingOrder == null){
			throw new Exception("找不到ID为" + id + "的生产单");
		}
		//去掉生产单为数量为0的行
		Iterator iterator = producingOrder.getDetaillist().iterator();
	    while(iterator.hasNext()){
	    	ProducingOrderDetail item = (ProducingOrderDetail)iterator.next();
	           if(item.getQuantity() == 0){
	               iterator.remove();
	            }
	    }
//		Order order = orderService.get(producingOrder.getOrderId());
//		request.setAttribute("order", order);

		List<ProducingOrder> producingOrderList = new ArrayList<ProducingOrder>();
		producingOrderList.add(producingOrder);
		request.setAttribute("producingOrderList", producingOrderList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gridName", "producingorder");
		return new ModelAndView("printorder/print", data);
	}
	
}
