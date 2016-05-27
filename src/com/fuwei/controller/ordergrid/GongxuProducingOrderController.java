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
import com.fuwei.entity.Factory;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.GongxuProducingOrder;
import com.fuwei.entity.ordergrid.GongxuProducingOrderDetail;
import com.fuwei.entity.ordergrid.GongxuProducingOrderMaterialDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.GongxuProducingOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/gongxu_producing_order")
@Controller
public class GongxuProducingOrderController extends BaseController {
	@Autowired
	GongxuProducingOrderService gongxuProducingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	PlanOrderService planOrderService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, Integer factoryId,String orderNumber,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "gongxu_producing_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看工序加工单列表的权限", null);
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
		pager = gongxuProducingOrderService.getList(pager, start_time_d, end_time_d,companyId,factoryId,orderNumber, 
				sortList);
		if (pager != null & pager.getResult() != null) {
			List<GongxuProducingOrder> orderlist = (List<GongxuProducingOrder>) pager
					.getResult();
		}
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("gongxu_producing_order/index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少工序加工单ID");
		}
		String lcode = "gongxu_producing_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看工序加工单详情的权限",
					null);
		}
		GongxuProducingOrder producingOrder = gongxuProducingOrderService.get(id);
		if(producingOrder == null){
			throw new Exception("找不到ID为" + id + "的工序加工单");
		}
		//去掉工序加工单为数量为0的行
		Iterator iterator = producingOrder.getDetaillist().iterator();
	    while(iterator.hasNext()){
	    	GongxuProducingOrderDetail item = (GongxuProducingOrderDetail)iterator.next();
	           if(item.getQuantity() == 0){
	               iterator.remove();
	            }
	    }

		List<GongxuProducingOrder> producingOrderList = new ArrayList<GongxuProducingOrder>();
		producingOrderList.add(producingOrder);
		request.setAttribute("gongxuProducingOrderList", producingOrderList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gridName", "gongxuproduceorder");
		return new ModelAndView("printorder/preview", data);
	}	
	
	// 添加或保存工序加工单

	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "gongxu_producing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑工序加工单的权限", null);
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
//			List<GongxuProducingOrder> producingOrderList = gongxuProducingOrderService
//					.getByOrder(orderId);
			
//			HashMap<Integer,Integer> hashmap = new HashMap<Integer,Integer>();
//			for (GongxuProducingOrder producingorder : producingOrderList) {
//				for (GongxuProducingOrderDetail temp : producingorder.getDetaillist()) {
//					int planOrderDetailId = temp.getPlanOrderDetailId();
//					Integer value = hashmap.get(planOrderDetailId);
//					if(value!=null){
//						hashmap.put(planOrderDetailId, value + temp.getQuantity());
//					}else{
//						hashmap.put(planOrderDetailId, temp.getQuantity());
//					}
//				}
//			}

			List<GongxuProducingOrderDetail> detaillist = new ArrayList<GongxuProducingOrderDetail>();
			for (PlanOrderDetail planOrderDetail : planOrder.getDetaillist()) {
				GongxuProducingOrderDetail producingorderdetail = new GongxuProducingOrderDetail();
				producingorderdetail.setColor(planOrderDetail.getColor());
				producingorderdetail.setPlanOrderDetailId(planOrderDetail.getId());
				producingorderdetail.setSize(planOrderDetail.getSize());
				producingorderdetail.setPrice(0);
				producingorderdetail.setWeight(planOrderDetail.getWeight());
				producingorderdetail.setYarn(planOrderDetail.getYarn());
				producingorderdetail.setProduce_weight(planOrderDetail.getProduce_weight());
//				Integer quantity = hashmap.get(planOrderDetail.getId());
//				quantity = quantity == null? 0 : quantity;
//				producingorderdetail.setQuantity(planOrderDetail.getQuantity()- quantity);
				producingorderdetail.setQuantity(0);
				detaillist.add(producingorderdetail);
			}
			request.setAttribute("order", order);
			request.setAttribute("detaillist", detaillist);
			List<GongXu> gongxulist = new ArrayList<GongXu>();
			for(GongXu temp : SystemCache.gongxulist){
				if(!temp.getIsProducingOrder()){  
					gongxulist.add(temp);
				}		
			}
			request.setAttribute("gongxulist", gongxulist);
			
			List<Factory> produce_factorylist = new ArrayList<Factory>();
			for(int i=0;i<SystemCache.produce_factorylist.size();++i){
				Factory temp = SystemCache.produce_factorylist.get(i);
				if(temp.getInUse()){
					produce_factorylist.add(temp);
				}
			}
			request.setAttribute("produce_factorylist", produce_factorylist);
			return new ModelAndView("gongxu_producing_order/addbyorder");
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(GongxuProducingOrder producingOrder,
			String details, String details_2, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "gongxu_producing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑工序加工单的权限", null);
		}
		try {
			Integer tableOrderId = producingOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (producingOrder.getOrderId() == null
						|| producingOrder.getOrderId() == 0) {
					throw new Exception(
							"工序加工单必须属于一张订单", null);
				}
				if (producingOrder.getFactoryId() == null
						|| producingOrder.getFactoryId() == 0) {
					throw new Exception(
							"工序加工单必须指定生产单位", null);
				} else{
					if(!SystemCache.getFactory(producingOrder.getFactoryId()).getInUse()){
						throw new Exception(
								"该生产单位已被停用", null);
					}
				}
				if (producingOrder.getGongxuId() == 0) {
					throw new Exception(
							"工序加工单必须指定工序", null);
				} 
				//判断工序是否是机织专用工序，若是则报错
				GongXu gongxu = SystemCache.getGongxu(producingOrder.getGongxuId());
				if(gongxu==null){
					throw new Exception(
							"该工序不存在", null);
				}else{
					if(gongxu.getIsProducingOrder()){
						throw new Exception(
								gongxu.getName() + " 工序是生产通知单专用工序，不能用于工序加工单", null);
					}
				}
				Order order = orderService.get(producingOrder.getOrderId());
				if(order == null){
					throw new Exception(
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
				producingOrder.setSize(order.getSize());
				producingOrder.setWeight(order.getWeight());
				producingOrder.setName(order.getName());
				producingOrder.setCompanyId(order.getCompanyId());
				producingOrder.setCustomerId(order.getCustomerId());
				producingOrder.setSampleId(order.getSampleId());
				producingOrder.setOrderNumber(order.getOrderNumber());
				producingOrder.setCharge_employee(order.getCharge_employee());
			
				producingOrder.setCompany_productNumber(order.getCompany_productNumber());
				
				List<GongxuProducingOrderDetail> detaillist = SerializeTool
						.deserializeList(details, GongxuProducingOrderDetail.class);
				List<GongxuProducingOrderMaterialDetail> detaillist2 = SerializeTool
						.deserializeList(details_2,
								GongxuProducingOrderMaterialDetail.class);
				producingOrder.setDetaillist(detaillist);
				producingOrder.setDetail_2_list(detaillist2);
				tableOrderId = gongxuProducingOrderService.add(producingOrder);
			} else {// 编辑
				//判断生产数量是否超出了计划数量
				//判断生产数量是否超出了计划数量
				
//				if (producingOrder.getFactoryId() == null
//						|| producingOrder.getFactoryId() == 0) {
//					throw new Exception(
//							"工序加工单必须指定生产单位", null);
//				} 
//				if (producingOrder.getGongxuId() == 0) {
//					throw new Exception(
//							"工序加工单必须指定工序", null);
//				} 
//				//判断工序是否是机织专用工序，若是则报错
//				GongXu gongxu = SystemCache.getGongxu(producingOrder.getGongxuId());
//				if(gongxu==null){
//					throw new Exception(
//							"该工序不存在", null);
//				}else{
//					if(gongxu.getIsProducingOrder()){
//						throw new Exception(
//								gongxu.getName() + " 工序是生产通知单专用工序，不能用于工序加工单", null);
//					}
//				}
				producingOrder.setUpdated_at(DateTool.now());
				List<GongxuProducingOrderDetail> detaillist = SerializeTool
						.deserializeList(details, GongxuProducingOrderDetail.class);
				List<GongxuProducingOrderMaterialDetail> detaillist2 = SerializeTool
						.deserializeList(details_2,
								GongxuProducingOrderMaterialDetail.class);
				producingOrder.setDetaillist(detaillist);
				producingOrder.setDetail_2_list(detaillist2);
				tableOrderId = gongxuProducingOrderService.update(producingOrder);
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
		String lcode = "gongxu_producing_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除工序加工单的权限", null);
		}
		int success = gongxuProducingOrderService.remove(id);
		
		return this.returnSuccess();
	}

	@RequestMapping(value = "/put/{gongxuProducingOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer gongxuProducingOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "gongxu_producing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑工序加工单的权限", null);
		}
		try {
			if(gongxuProducingOrderId!=null){
				List<GongXu> gongxulist = new ArrayList<GongXu>();
				for(GongXu temp : SystemCache.gongxulist){
					if(!temp.getIsProducingOrder()){  
						gongxulist.add(temp);
					}	
				}
				request.setAttribute("gongxulist", gongxulist);
				GongxuProducingOrder gongxuProducingOrder = gongxuProducingOrderService.get(gongxuProducingOrderId);
				request.setAttribute("gongxuProducingOrder",gongxuProducingOrder);
				if(gongxuProducingOrder.getOrderId()!=null){
					return new ModelAndView("gongxu_producing_order/editbyorder");
				}else{
					throw new Exception("发生错误：工序加工单缺少订单ID");
					//return new ModelAndView("producing_order/edit");
				}
				
			}
			throw new Exception("缺少工序加工单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
//	@RequestMapping(value = "/scan", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView scan(HttpSession session,
//			HttpServletRequest request) throws Exception {
//		return new ModelAndView("gongxu_producing_order/scan");
//	}
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail2(Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		return detail(id, session, request);
	}

	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少工序加工单ID");
		}	
		GongxuProducingOrder producingOrder = gongxuProducingOrderService.get(id);
		if(producingOrder == null){
			throw new Exception("找不到ID为" + id + "的工序加工单");
		}
		//去掉生产单为数量为0的行
		Iterator iterator = producingOrder.getDetaillist().iterator();
	    while(iterator.hasNext()){
	    	GongxuProducingOrderDetail item = (GongxuProducingOrderDetail)iterator.next();
	           if(item.getQuantity() == 0){
	               iterator.remove();
	            }
	    }
//		Order order = orderService.get(producingOrder.getOrderId());
//		request.setAttribute("order", order);

		List<GongxuProducingOrder> producingOrderList = new ArrayList<GongxuProducingOrder>();
		producingOrderList.add(producingOrder);
		request.setAttribute("gongxuProducingOrderList", producingOrderList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("gridName", "gongxuproduceorder");
		return new ModelAndView("printorder/print", data);
	}
	
}
