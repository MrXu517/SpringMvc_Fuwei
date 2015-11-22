package com.fuwei.controller.producesystem;

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
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.GongxuProducingOrder;
import com.fuwei.entity.ordergrid.GongxuProducingOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.producesystem.HalfCurrentStock;
import com.fuwei.entity.producesystem.HalfCurrentStockDetail;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.entity.producesystem.HalfStoreInOutDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.GongxuProducingOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.producesystem.HalfCurrentStockService;
import com.fuwei.service.producesystem.HalfStoreInOutService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/half_store_out")
@Controller
/* 半成品出库单 */
public class HalfStoreOutController extends BaseController {
	@Autowired
	HalfStoreInOutService halfStoreInOutService;
	@Autowired
	PlanOrderService planOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	MessageService messageService;
	@Autowired
	HalfCurrentStockService halfCurrentStockService;
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	GongxuProducingOrderService gongxuProducingOrderService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer factoryId,Integer charge_employee, String number,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "half_store_in_out/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看半成品出库列表的权限", null);
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
		sort.setProperty("date");
		sortList.add(sort);
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);
		pager = halfStoreInOutService.getList(pager, start_time_d, end_time_d,
				companyId, factoryId,charge_employee, number,false, sortList);
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for(Employee temp : SystemCache.employeelist){
			if(temp.getIs_charge_employee()){
				employeelist.add(temp);
			}		
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		return new ModelAndView("half_store_in_out/out_index");
	}

	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少半成品出库单ID");
		}
		String lcode = "half_store_in_out/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看半成品出库单详情的权限",
					null);
		}
		HalfStoreInOut halfStoreInOut = halfStoreInOutService.get(id, false);
		if (halfStoreInOut == null) {
			throw new Exception("找不到ID为" + id + "的半成品出库单");
		}
		request.setAttribute("halfStoreInOut", halfStoreInOut);
		return new ModelAndView("half_store_in_out/out_detail");
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String orderNumber,Integer factoryId,Integer gongxuId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Order order =  orderService.get(orderNumber);
		 if(order == null){
			 throw new Exception("找不到订单号为" + orderNumber + "的订单");
		 }
		 return addbyorder(order.getId(),factoryId,gongxuId, session, request, response);
	}
	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer orderId,Integer factoryId,Integer gongxuId,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (orderId == null) {
			throw new Exception("订单ID不能为空");
		}

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半成品出库单的权限",
					null);
		}
		try {
			Order order = orderService.get(orderId);
			if (order == null) {
				throw new Exception(
						"该订单不存在或已被删除", null);
			}
			PlanOrder planOrder = planOrderService.getByOrder(orderId);
			if (planOrder == null) {
				throw new Exception(
						"计划单不存在或已被删除", null);
			}
			if (planOrder.getDetaillist() == null
					|| planOrder.getDetaillist().size() <= 0) {
				throw new Exception(
						"计划单缺少颜色及数量列表，请先修改 ", null);
			}
			request.setAttribute("planOrder", planOrder);
			request.setAttribute("order", order);
			
			
			/*找到可以半成品入库的工厂：生产单的工厂、工序加工单的工厂*/
			Map<Integer,String> factoryMap = new HashMap<Integer, String>();
			List<ProducingOrder> producingOrderlist = producingOrderService.getByOrder(orderId);		
			for(ProducingOrder temp : producingOrderlist){
				int tempfactoryId = temp.getFactoryId();
				if(!factoryMap.containsKey(tempfactoryId)){
					factoryMap.put(tempfactoryId, SystemCache.getFactoryName(tempfactoryId));
				}
				
			}
			List<GongxuProducingOrder> gongxuProducingOrderlist = gongxuProducingOrderService.getByOrder(orderId);		
			for(GongxuProducingOrder temp : gongxuProducingOrderlist){
				int tempfactoryId = temp.getFactoryId();
				if(!factoryMap.containsKey(tempfactoryId)){
					factoryMap.put(tempfactoryId, SystemCache.getFactoryName(tempfactoryId));
				}
			}
			if(factoryMap.size()==1){
				for(Integer temp:factoryMap.keySet()){
					factoryId = temp;
				}
			}
			request.setAttribute("factoryMap", factoryMap);
			
			Map<Integer,String> gongxuMap = new HashMap<Integer, String>();
			for(ProducingOrder temp : producingOrderlist){
				int tempfactoryId = temp.getFactoryId();
				if(factoryId != null && tempfactoryId == factoryId){
					int tempGongxuId = SystemCache.producing_GONGXU.getId();
					if(!gongxuMap.containsKey(tempGongxuId)){
						gongxuMap.put(tempGongxuId, SystemCache.getGongxuName(tempGongxuId));
					}
				}
				
			}	
			for(GongxuProducingOrder temp : gongxuProducingOrderlist){
				int tempfactoryId = temp.getFactoryId();
				if(factoryId != null && tempfactoryId == factoryId){
					int tempGongxuId = temp.getGongxuId();
					if(!gongxuMap.containsKey(tempGongxuId)){
						gongxuMap.put(tempGongxuId, SystemCache.getGongxuName(tempGongxuId));
					}
				}
			}
			request.setAttribute("gongxuMap", gongxuMap);
			if(gongxuMap.size()==1){
				for(Integer temp:gongxuMap.keySet()){
					gongxuId = temp;
				}
			}
			
			request.setAttribute("factoryId", factoryId);
			request.setAttribute("gongxuId", gongxuId);
			/*找到可以半成品入库的工厂：生产单的工厂、工序加工单的工厂*/
			
			if(factoryId!=null && factoryId!=0 && gongxuId!=null && gongxuId!=0){
				/*半成品出库单的限制条件：1、出库数量<=仓库中当前库存  ， 2、出库数量大于加工单中数量时，给提醒，但依然可保存*/
				/*1.获取当前库存*/
				HalfCurrentStock currentStock = halfCurrentStockService.get(orderId);
				/*1.获取当前库存*/
				
				/*2.获取加工单未出库数量  = 加工单中的数量 - 已出库数量*/
				//获取已开的出库单
				List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByFactoryGongxu(orderId,factoryId,gongxuId,false);
				List<Map<String,Object>> detaillist = new ArrayList<Map<String,Object>>();
				if(gongxuId == SystemCache.producing_GONGXU.getId()){//若是生产单的半成品入库
					List<ProducingOrder> templist = producingOrderService.getByFactory(orderId,factoryId);
					detaillist = getOutStoreQuantity(currentStock,templist,storeOutList,planOrder);
				}else{
					List<GongxuProducingOrder> gongxuProducingOrderList = gongxuProducingOrderService.getByFactoryGongXu(orderId,factoryId,gongxuId);
					detaillist = getGongxuOutStoreQuantity(currentStock,gongxuProducingOrderList,storeOutList,planOrder);
				}
//				List<Map<String,Object>> stocklist = getOutStoreQuantity(planOrder,storeOutList);
				/*2.获取加工单未出库数量*/
				
				/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
				boolean flag = true;
				boolean flag_not_in = true;
				for(Map<String,Object> tMap : detaillist){
					if((Integer)tMap.get("stock_quantity") > 0){				
						flag = false;
					}
					if((Integer)tMap.get("not_out_quantity") > 0){				
						flag_not_in = false;
					}
				}
				if(flag_not_in){
					request.setAttribute("message", "加工单所需半成品已全部出库，请确认出库量是否过大");
				}
				if(flag){
					request.setAttribute("message", "库存数量为0，无法再创建出库单！！！");
				}
				
				/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
				
				request.setAttribute("detaillist", detaillist);
			}
			
			
			return new ModelAndView("half_store_in_out/add_out");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(HalfStoreInOut storeInOut,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半成品出库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer orderId = storeInOut.getOrderId();
			if (orderId == null) {
				throw new Exception("订单ID不能为空");
			}
			Integer factoryId = storeInOut.getFactoryId();
			if (factoryId == null || factoryId == 0) {
				throw new Exception("必须指定领取单位", null);
			}
			Integer gongxuId = storeInOut.getGongxuId();
			if (gongxuId == null || gongxuId == 0) {
				throw new Exception("工序ID不能为空");
			}
			if (storeInOut.getDate() == null) {
				throw new Exception("出库日期不能为空", null);
			}
			Order order = orderService.get(orderId);
			if (order == null) {
				throw new Exception(
						"该订单不存在或已被删除", null);
			}
			PlanOrder planOrder = planOrderService.getByOrder(orderId);
			if (planOrder == null) {
				throw new Exception(
						"计划单不存在或已被删除", null);
			}
			if (planOrder.getDetaillist() == null
					|| planOrder.getDetaillist().size() <= 0) {
				throw new Exception(
						"计划单缺少颜色及数量列表，请先修改 ", null);
			}
			/* 判断有些数据不能为空 */

			if (storeInOut.getId() == null) {// 添加半成品出库单
				storeInOut.setCreated_at(DateTool.now());// 设置创建时间
				storeInOut.setUpdated_at(DateTool.now());// 设置更新时间
				storeInOut.setCreated_user(user.getId());// 设置创建人

				// 以下是order的属性
				storeInOut.setOrderId(order.getId());
				storeInOut.setImg(order.getImg());
				storeInOut.setImg_s(order.getImg_s());
				storeInOut.setImg_ss(order.getImg_ss());
				storeInOut.setProductNumber(order.getProductNumber());
				storeInOut.setMaterialId(order.getMaterialId());
				storeInOut.setSize(order.getSize());
				storeInOut.setWeight(order.getWeight());
				storeInOut.setName(order.getName());
				storeInOut.setCompanyId(order.getCompanyId());
				storeInOut.setCustomerId(order.getCustomerId());
				storeInOut.setSampleId(order.getSampleId());
				storeInOut.setOrderNumber(order.getOrderNumber());
				storeInOut.setCharge_employee(order.getCharge_employee());
				storeInOut.setCompany_productNumber(order
						.getCompany_productNumber());

				List<HalfStoreInOutDetail> detaillist = SerializeTool
						.deserializeList(details, HalfStoreInOutDetail.class);
				
				//判断是否有数量为0的明细项
				Iterator<HalfStoreInOutDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					HalfStoreInOutDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次出库数量均为0，无法创建出库单，请至少出库一种材料");
				}
				//判断是否有数量为0的明细项
				
				storeInOut.setDetaillist(detaillist);

				storeInOut.setIn_out(false);
				
				/*半成品出库单的限制条件：1、出库数量<=仓库中当前库存  ， 2、出库数量大于加工单中数量时，给提醒，但依然可保存*/
				/*1.出库数量<=仓库中当前库存*/
				HalfCurrentStock currentStock = halfCurrentStockService.get(orderId);
				for(HalfStoreInOutDetail temp : storeInOut.getDetaillist()){
					for(HalfCurrentStockDetail dt: currentStock.getDetaillist()){
						if(dt.getPlanOrderDetailId() == temp.getPlanOrderDetailId()){
							dt.setStock_quantity(dt.getStock_quantity() - temp.getQuantity());
							if(dt.getStock_quantity() < 0){//出库总数大于仓库库存的数量
								throw new Exception("颜色：" +  dt.getColor() + "," 
										+ "尺寸：" +  dt.getSize()+
										"的出库总数大于仓库库存");
							}
						}
					}
				}
				/*1.出库数量<=仓库中当前库存*/
				
				/*2.获取加工单未出库数量  = 加工单中的数量 - 已出库数量*/
				List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByFactoryGongxu(orderId,factoryId,gongxuId,false);
				storeOutList.add(storeInOut);
				List<Map<String,Object>> stocklist = new ArrayList<Map<String,Object>>();
				if(gongxuId == SystemCache.producing_GONGXU.getId()){//若是生产单的半成品入库
					List<ProducingOrder> templist = producingOrderService.getByFactory(orderId,factoryId);
					stocklist = getOutStoreQuantity(currentStock,templist,storeOutList,planOrder);
				}else{
					List<GongxuProducingOrder> gongxuProducingOrderList = gongxuProducingOrderService.getByFactoryGongXu(orderId,factoryId,gongxuId);
					stocklist = getGongxuOutStoreQuantity(currentStock,gongxuProducingOrderList,storeOutList,planOrder);
				}
				/*2.获取加工单未出库数量*/
				boolean flag = true;
				for(Map<String,Object> tMap : stocklist){
					if((Integer)tMap.get("not_out_quantity") > 0){				
						flag = false;
					}
				}
				if(flag){
					request.setAttribute("message", "出库数量大于加工单数量，请仔细确认数量是否正确");
				}
				/*半成品出库单的限制条件：1、出库数量<=仓库中当前库存  ， 2、出库数量大于加工单中数量时，给提醒，但依然可保存*/

				int tableOrderId = halfStoreInOutService.add(storeInOut);
				return this.returnSuccess("id", tableOrderId);
			} else {// 编辑
				storeInOut.setUpdated_at(DateTool.now());
				List<HalfStoreInOutDetail> detaillist = SerializeTool
						.deserializeList(details, HalfStoreInOutDetail.class);
				//判断是否有数量为0的明细项
				Iterator<HalfStoreInOutDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					HalfStoreInOutDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次出库数量均为0，无法创建出库单，请至少出库一种材料");
				}
				//判断是否有数量为0的明细项
				storeInOut.setDetaillist(detaillist);
				storeInOut.setIn_out(false);
				
				
				/*半成品出库单的限制条件：1、出库数量<=仓库中当前库存  ， 2、出库数量大于加工单中数量时，给提醒，但依然可保存*/
				/*1.出库数量<=仓库中当前库存*/
				HalfCurrentStock currentStock = halfCurrentStockService.get(orderId);
				for(HalfStoreInOutDetail temp : storeInOut.getDetaillist()){
					for(HalfCurrentStockDetail dt: currentStock.getDetaillist()){
						if(dt.getPlanOrderDetailId() == temp.getPlanOrderDetailId()){
							dt.setStock_quantity(dt.getStock_quantity() - temp.getQuantity());
							if(dt.getStock_quantity() < 0){//出库总数大于仓库库存的数量
								throw new Exception("颜色：" +  dt.getColor() + "," 
										+ "尺寸：" +  dt.getSize()+
										"的出库总数大于仓库库存");
							}
						}
					}
				}
				/*1.出库数量<=仓库中当前库存*/
				
				/*2.获取加工单未出库数量  = 加工单中的数量 - 已出库数量*/
				List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByFactoryGongxu(orderId,factoryId,gongxuId,false);
				Iterator<HalfStoreInOut> iterator = storeOutList.iterator();  
				while(iterator.hasNext()){  
					HalfStoreInOut temp = iterator.next();  
					if(temp.getId() == storeInOut.getId()){
						iterator.remove();  
				    }  
				} 
				storeOutList.add(storeInOut);
				List<Map<String,Object>> stocklist = new ArrayList<Map<String,Object>>();
				if(gongxuId == SystemCache.producing_GONGXU.getId()){//若是生产单的半成品入库
					List<ProducingOrder> templist = producingOrderService.getByFactory(orderId,factoryId);
					stocklist = getOutStoreQuantity(currentStock,templist,storeOutList,planOrder);
				}else{
					List<GongxuProducingOrder> gongxuProducingOrderList = gongxuProducingOrderService.getByFactoryGongXu(orderId,factoryId,gongxuId);
					stocklist = getGongxuOutStoreQuantity(currentStock,gongxuProducingOrderList,storeOutList,planOrder);
				}
				/*2.获取加工单未出库数量*/
				boolean flag = true;
				for(Map<String,Object> tMap : stocklist){
					if((Integer)tMap.get("not_out_quantity") > 0){				
						flag = false;
					}
				}
				if(flag){
					request.setAttribute("message", "出库数量大于加工单数量，请仔细确认数量是否正确");
				}
				/*半成品出库单的限制条件：1、出库数量<=仓库中当前库存  ， 2、出库数量大于加工单中数量时，给提醒，但依然可保存*/
				
				int tableOrderId = halfStoreInOutService.update(storeInOut);
				return this.returnSuccess("id", tableOrderId);
			}

		} catch (Exception e) {
			throw e;
		}

	}

//	
//	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView put(@PathVariable Integer id,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		if(id == null){
//			throw new Exception("半成品出库单ID不能为null");
//		}
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "half_store_in_out/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有编辑半成品出库单的权限", null);
//		}
//		try {
//			HalfStoreInOut object = halfStoreInOutService.get(id, false);
//			if(object == null){
//				throw new Exception("找不到ID为" + id + "的半成品出库单");
//			}
//			request.setAttribute("halfStoreInOut", object);
//			int orderId = object.getOrderId();
//			PlanOrder planOrder = planOrderService.getByOrder(orderId);
//			request.setAttribute("planOrder", planOrder);
//			
//			/*获取当前库存*/
//			//获取已开的入库单
//			List<HalfStoreInOut> storeInList = halfStoreInOutService.getByOrder(orderId,true);
//			//获取已开的出库单
//			List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByOrder(orderId,false);
//			List<Map<String,Object>> stocklist =  getOutStoreQuantity(planOrder,storeInList,storeOutList);
//			/*获取当前库存*/
//				
//			List<Map<String,Object>> detaillist_result = new ArrayList<Map<String,Object>>();
//			for(Map<String,Object> stockMap : stocklist){
//				stockMap.put("quantity",0);
//				for(HalfStoreInOutDetail detail : object.getDetaillist()){
//					if(detail.getPlanOrderDetailId() == (Integer)stockMap.get("planOrderDetailId")){
//						stockMap.put("quantity",detail.getQuantity());
//						stockMap.put("id", detail.getId());
//					}
//					
//				}
//				
//				detaillist_result.add(stockMap);
//			}
//			
//			request.setAttribute("detaillist", detaillist_result);
//			
//			request.setAttribute("stocklist", stocklist);
//			return new ModelAndView("half_store_in_out/edit_out");
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	// 2015-3-31 删除
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_in_out/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除半成品出库单的权限", null);
		}
		int success = halfStoreInOutService.remove(id);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("half_store_in_out/out_scan");
	}

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
			throw new Exception("缺少半成品出库单ID");
		}
		String lcode = "half_store_in_out/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印半成品出库单的权限", null);
		}
		HalfStoreInOut halfStoreInOut = halfStoreInOutService.get(id, false);
		if (halfStoreInOut == null) {
			throw new Exception("找不到ID为" + id + "的半成品出库单");
		}
		halfStoreInOut.setHas_print(true);
		halfStoreInOutService.updatePrint(halfStoreInOut);
		request.setAttribute("halfStoreInOut", halfStoreInOut);
		return new ModelAndView("half_store_in_out/out_print");
	}

	//获取某加工工厂某工序的未出库数量
	public List<Map<String,Object>> getOutStoreQuantity(HalfCurrentStock currentStock,List<ProducingOrder> list,List<HalfStoreInOut> storeOutList,PlanOrder planOrder) throws Exception{
		//根据  【planOrderDetailId】  统计加工单总数量 , key = planOrderDetailId
		HashMap<Integer, Integer> total_producingmap = new HashMap<Integer, Integer>();
		for (ProducingOrder producingorder : list) {
			for (ProducingOrderDetail temp : producingorder.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(total_producingmap.containsKey(key)){
					int quantity = total_producingmap.get(key) + temp.getQuantity();
					total_producingmap.put(key, quantity);
				}else{
					total_producingmap.put(key, temp.getQuantity());
				}
			}
		}
		List<Map<String,Object>> resultlist = _getOutStoreQuantity(currentStock,total_producingmap, storeOutList,planOrder);
		return resultlist;
	}
	
	//获取某加工工厂某工序的未出库数量
	public List<Map<String,Object>> getGongxuOutStoreQuantity(HalfCurrentStock currentStock, List<GongxuProducingOrder> list,List<HalfStoreInOut> storeOutList,PlanOrder planOrder) throws Exception{
		//根据  【planOrderDetailId】  统计加工单总数量 , key = planOrderDetailId
		HashMap<Integer, Integer> total_producingmap = new HashMap<Integer, Integer>();
		for (GongxuProducingOrder producingorder : list) {
			for (GongxuProducingOrderDetail temp : producingorder.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(total_producingmap.containsKey(key)){
					int quantity = total_producingmap.get(key) + temp.getQuantity();
					total_producingmap.put(key, quantity);
				}else{
					total_producingmap.put(key, temp.getQuantity());
				}
			}
		}
		List<Map<String,Object>> resultlist = _getOutStoreQuantity(currentStock,total_producingmap, storeOutList,planOrder);
		return resultlist;
	}
	
	//获取某订单的当前库存
	public List<Map<String,Object>> _getOutStoreQuantity(HalfCurrentStock currentStock,HashMap<Integer, Integer> total_producingmap,List<HalfStoreInOut> storeOutList,PlanOrder planOrder) throws Exception{
		//根据 【planOrderDetailId】 统计已出库 数量
		HashMap<Integer, Integer> total_outmap = new HashMap<Integer, Integer>();
		for (HalfStoreInOut storeOut : storeOutList) {
			for (HalfStoreInOutDetail temp : storeOut.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(total_outmap.containsKey(key)){
					int temp_total_quantity = total_outmap.get(key);
					total_outmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_outmap.put(key, temp.getQuantity());
				}
			}
		}

		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		//获取加工单未出库列表
		for(int key : total_producingmap.keySet()){
			int total_quantity = total_producingmap.get(key);
			Integer out_quantity = total_outmap.get(key);
			if(out_quantity == null){
				out_quantity = 0 ;
			}
			//未出库数量 = 加工单数量  - 已出库数量
			int not_out_quantity = out_quantity == null? total_quantity:total_quantity - out_quantity;
			
			HashMap<String, Object> tempHash = new HashMap<String, Object>();
			tempHash.put("planOrderDetailId", key);
			tempHash.put("total_quantity", total_quantity);//应生产数量
			tempHash.put("out_quantity", out_quantity);//已出库
			tempHash.put("not_out_quantity", not_out_quantity);
			resultlist.add(tempHash);
		}
		//添加planOrderDetailId对应的颜色尺寸等属性
		for(Map<String,Object> map : resultlist){
			int planOrderDetailId = (Integer)map.get("planOrderDetailId");
			for(PlanOrderDetail detail : planOrder.getDetaillist()){
				if(planOrderDetailId == detail.getId()){
					map.put("color", detail.getColor());
					map.put("weight", detail.getProduce_weight());
					map.put("size", detail.getSize());
					map.put("yarn", detail.getYarn());
				}
			}
			boolean flag = true;
			for(HalfCurrentStockDetail detail : currentStock.getDetaillist()){
				if(detail.getPlanOrderDetailId() == planOrderDetailId){
					flag=false;
					map.put("stock_quantity", detail.getStock_quantity());
				}
			}
			if(flag){
				map.put("stock_quantity", 0);
			}
		}
		
		return resultlist;
	}
}
