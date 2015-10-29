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
import com.fuwei.entity.ordergrid.StoreOrderDetail;
import com.fuwei.entity.producesystem.HalfCurrentStock;
import com.fuwei.entity.producesystem.HalfCurrentStockDetail;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.entity.producesystem.HalfStoreInOutDetail;
import com.fuwei.entity.producesystem.HalfStoreReturn;
import com.fuwei.entity.producesystem.HalfStoreReturnDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.GongxuProducingOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.producesystem.HalfCurrentStockService;
import com.fuwei.service.producesystem.HalfStoreInOutService;
import com.fuwei.service.producesystem.HalfStoreReturnService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/half_store_return")
@Controller
/* 半成品退货单 */
public class HalfStoreReturnController extends BaseController {
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
	HalfStoreReturnService halfStoreReturnService;

	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "half_store_return/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看半成品退货列表的权限", null);
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

		pager = halfStoreReturnService.getList(pager, start_time_d, end_time_d,
				companyId, factoryId, charge_employee, number, sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		return new ModelAndView("half_store_return/index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少半成品退货单ID");
		}
		String lcode = "half_store_return/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看半成品退货单详情的权限",
					null);
		}
		HalfStoreReturn storeReturn = halfStoreReturnService.get(id);
		if (storeReturn == null) {
			throw new Exception("找不到ID为" + id + "的半成品退货单");
		}
		request.setAttribute("storeReturn", storeReturn);
		return new ModelAndView("half_store_return/detail");	
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String orderNumber,Integer factoryId,Integer gongxuId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 Order order =  orderService.get(orderNumber);
		 if(order == null){
			 throw new Exception("找不到订单号为" + orderNumber + "的订单");
		 }
		 return addbyorder(order.getId(),factoryId,gongxuId, session, request, response);
	}

	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer orderId,Integer factoryId,Integer gongxuId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (orderId == null) {
			throw new Exception("订单ID不能为空");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_return/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半成品退货单的权限",
					null);
		}
		try {
			Order order = orderService.get(orderId);
			if (order == null) {
				throw new Exception(
						"该订单不存在或已被删除", null);
			}
		
			
			//找到可以半成品退货的工厂：该订单有过入库的工厂
			Map<Integer,String> factoryMap = new HashMap<Integer, String>();
			List<HalfStoreInOut> tempstoreInList = halfStoreInOutService.getByOrder(orderId, true);
			for(HalfStoreInOut temp : tempstoreInList){
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
			for(HalfStoreInOut temp : tempstoreInList){
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
			
			
			if(factoryId!=null && factoryId!=0 && gongxuId!=null && gongxuId!=0){
				//获取未退库列表 ， 包括总入库，已退货，实际入库
				//获取已开的入库单
				List<HalfStoreInOut> storeInList = halfStoreInOutService.getByFactoryGongxu(orderId,factoryId, gongxuId,true);
				List<HalfStoreReturn> storeReturnList = halfStoreReturnService.getByFactoryGongxu(orderId,factoryId, gongxuId);
				List<Map<String,Object>> detaillist =  getInStoreQuantity(storeInList,storeReturnList);
				boolean flag = true;
				for(Map<String,Object> tMap : detaillist){
					if((Integer)tMap.get("actual_in_quantity") > 0){//实际入库数量大于0，则表示有货可退			
						flag = false;
					}
				}
				if(flag){
					throw new Exception("实际入库数量为0，已全部退货或者还未入库，无法再创建退货单！！！");
				}
				request.setAttribute("detaillist", detaillist);
			}

			request.setAttribute("order", order);
			return new ModelAndView("half_store_return/add");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(HalfStoreReturn halfStoreReturn,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_return/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半成品入库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer orderId = halfStoreReturn.getOrderId();
			if (orderId == null) {
				throw new Exception("订单ID不能为空");
			}
			Integer factoryId = halfStoreReturn.getFactoryId();
			if (factoryId == null|| factoryId == 0) {
				throw new Exception("必须指定送货单位", null);
			}
			Integer gongxuId = halfStoreReturn.getGongxuId();
			if (gongxuId == null || gongxuId == 0) {
				throw new Exception("工序ID不能为空");
			}
			if (halfStoreReturn.getDate() == null) {
				throw new Exception("退货日期不能为空", null);
			}
			Order order = orderService.get(orderId);
			if (order == null) {
				throw new Exception(
						"该订单不存在或已被删除", null);
			}
			/* 判断有些数据不能为空 */
			

			if (halfStoreReturn.getId() == null) {// 添加半成品退货单
				Map<String,Object> data = new HashMap<String, Object>();
				halfStoreReturn.setCreated_at(DateTool.now());// 设置创建时间
				halfStoreReturn.setUpdated_at(DateTool.now());// 设置更新时间
				halfStoreReturn.setCreated_user(user.getId());// 设置创建人

				// 以下是order的属性
				halfStoreReturn.setOrderId(order.getId());
				halfStoreReturn.setImg(order.getImg());
				halfStoreReturn.setImg_s(order.getImg_s());
				halfStoreReturn.setImg_ss(order.getImg_ss());
				halfStoreReturn.setProductNumber(order.getProductNumber());
				halfStoreReturn.setName(order.getName());
				halfStoreReturn.setCompanyId(order.getCompanyId());
				halfStoreReturn.setCustomerId(order.getCustomerId());
				halfStoreReturn.setSampleId(order.getSampleId());
				halfStoreReturn.setOrderNumber(order.getOrderNumber());
				halfStoreReturn.setCharge_employee(order.getCharge_employee());
				halfStoreReturn.setCompany_productNumber(order
						.getCompany_productNumber());

				List<HalfStoreReturnDetail> detaillist = SerializeTool
						.deserializeList(details, HalfStoreReturnDetail.class);
				
				//判断是否有数量为0的明细项
				Iterator<HalfStoreReturnDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					HalfStoreReturnDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				    if(detail.getPlanOrderDetailId()<=0){//填了数量，但planOrderDetailId为空则也不行
				    	throw new Exception("计划单对应明细ID不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次退货数量均为0，无法创建退货单");
				}
				//判断是否有数量为0的明细项
				
				//设置退货明细id
				for(int count = 0 ; count < detaillist.size() ; ++count){
					HalfStoreReturnDetail detail = detaillist.get(count);
					detail.setId(count+1);
				}
				halfStoreReturn.setDetaillist(detaillist);

				
				/*判断半成品退货总数大于实际入库数量*/
				List<HalfStoreInOut> storeInList = halfStoreInOutService.getByFactoryGongxu(orderId,factoryId, gongxuId,true);
				List<HalfStoreReturn> storeReturnList = halfStoreReturnService.getByFactoryGongxu(orderId,factoryId, gongxuId);
				storeReturnList.add(halfStoreReturn);
				List<Map<String,Object>> returnlist = getInStoreQuantity(storeInList,storeReturnList);
				
				for(Map<String,Object> item:returnlist){
					int actual_in_quantity = (Integer)item.get("actual_in_quantity");
					if(actual_in_quantity<0){//半成品退货总数大于实际入库数量
						throw new Exception( "半成品退货总数大于实际入库数量");
					}
				}
				/*判断半成品退货总数大于实际入库数量*/
				
				int tableOrderId = halfStoreReturnService.add(halfStoreReturn);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			} else {// 编辑
				Map<String,Object> data = new HashMap<String, Object>();
				halfStoreReturn.setUpdated_at(DateTool.now());
				List<HalfStoreReturnDetail> detaillist = SerializeTool
						.deserializeList(details, HalfStoreReturnDetail.class);
				//判断是否有数量为0的明细项
				Iterator<HalfStoreReturnDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					HalfStoreReturnDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				    if(detail.getPlanOrderDetailId()<=0){//填了数量，但planOrderDetailId为空则也不行
				    	throw new Exception("计划单对应明细ID不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次退货数量均为0，无法创建退货单");
				}
				//判断是否有数量为0的明细项
				
				//设置退货明细id
				int maxId = 0 ; 
				for(HalfStoreReturnDetail detail : detaillist){
					if(detail.getId() <= 0){
						continue;
					}else if(detail.getId() > maxId){
						maxId = detail.getId();
					}
				}
				for(int i = 0,count = 0 ; i < detaillist.size() ; ++i){
					HalfStoreReturnDetail detail = detaillist.get(count);
					if(detail.getId() <= 0){
						detail.setId(count+1 + maxId);
						++count;
					}
					
				}
				
				halfStoreReturn.setDetaillist(detaillist);
				
				/*判断入库量总和是否大于计划单总量*/
				List<HalfStoreInOut> halfstoreInList = halfStoreInOutService.getByFactoryGongxu(orderId,factoryId, gongxuId, true);
				List<HalfStoreReturn> storeReturnList = halfStoreReturnService.getByFactoryGongxu(orderId,factoryId, gongxuId);
				for(int i = 0 ; i < storeReturnList.size() ; ++i){
					HalfStoreReturn item= storeReturnList.get(i);
					if(item.getId() == halfStoreReturn.getId()){
						storeReturnList.set(i, halfStoreReturn);
					}
				}
				List<Map<String,Object>> returnlist = getInStoreQuantity(halfstoreInList,storeReturnList);
				for(Map<String,Object> item:returnlist){
					int actual_in_quantity = (Integer)item.get("actual_in_quantity");
					if(actual_in_quantity<0){//半成品退货总数大于实际入库数量
						throw new Exception("半成品退货总数大于实际入库数量");
					}
				}
				/*判断入库量总和是否大于计划单总量*/
				
				int tableOrderId = halfStoreReturnService.update(halfStoreReturn);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	// 2015-3-31 删除
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_return/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除半成品退货单的权限", null);
		}
		HalfStoreReturn storeReturn = halfStoreReturnService.get(id);
		
//		/*判断实际入库量是否小于退货量，若是，则不可以删除*/
//		List<HalfStoreInOut> halfstoreInList = halfStoreInOutService.getByFactoryGongxu(storeReturn.getOrderId(),storeReturn.getFactoryId(), storeReturn.getGongxuId(), true);
//		List<HalfStoreReturn> storeReturnList = halfStoreReturnService.getByFactoryGongxu(storeReturn.getOrderId(),storeReturn.getFactoryId(), storeReturn.getGongxuId());
//		Iterator<HalfStoreReturn> iter = storeReturnList.iterator();
//		while(iter.hasNext()){
//			HalfStoreReturn temp = iter.next();
//			if(temp.getId() == storeReturn.getId()){
//				iter.remove();
//			}
//		}
//		List<Map<String,Object>> returnlist = getInStoreQuantity(halfstoreInList,storeReturnList);
//		for(Map<String,Object> item:returnlist){
//			int actual_in_quantity = (Integer)item.get("actual_in_quantity");
//			if(actual_in_quantity<0){//删除后实际入库数量<0， 则不可删除
//				throw new Exception("删除后实际入库数量小于0， 无法删除");
//			}
//		}
//		/*判断实际入库量是否小于退货量，若是，则不可以删除*/
		
		int success = halfStoreReturnService.remove(storeReturn);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("half_store_return/scan");
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
			throw new Exception("缺少半成品退货单ID");
		}
		String lcode = "half_store_return/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印半成品退货单的权限", null);
		}
		HalfStoreReturn halfstoreReturn = halfStoreReturnService.get(id);
		if (halfstoreReturn == null) {
			throw new Exception("找不到ID为" + id + "的半成品退货单");
		}
		halfstoreReturn.setHas_print(true);
		halfStoreReturnService.updatePrint(halfstoreReturn);
		request.setAttribute("halfstoreReturn", halfstoreReturn);
		return new ModelAndView("half_store_return/print");
	}

	
	public List<Map<String,Object>> getInStoreQuantity(List<HalfStoreInOut> halfstoreInList , List<HalfStoreReturn> halfstoreReturnList) throws Exception{	
		HashMap<Integer, HalfStoreInOutDetail> colorSizeMap = new HashMap<Integer, HalfStoreInOutDetail>();
		//根据 【planOrderDetailId】获取已入库 数量
		HashMap<Integer, Integer> inmap = new HashMap<Integer, Integer>();
		for (HalfStoreInOut storeIn : halfstoreInList) {
			for (HalfStoreInOutDetail temp : storeIn.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(inmap.containsKey(key)){
					int temp_total_quantity = inmap.get(key);
					inmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					inmap.put(key, temp.getQuantity());
				}
				//获取【planOrderDetailId对应的颜色尺寸等属性值】
				if(!colorSizeMap.containsKey(key)){
					colorSizeMap.put(key, temp);
				}
			}
		}
		
		//根据 【planOrderDetailId】 统计已退货 数量
		HashMap<Integer, Integer> total_returnmap = new HashMap<Integer, Integer>();
		for (HalfStoreReturn storereturn : halfstoreReturnList) {
			for (HalfStoreReturnDetail temp : storereturn.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(total_returnmap.containsKey(key)){
					int temp_total_quantity = total_returnmap.get(key);
					total_returnmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_returnmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//获取未退货列表，包括 总入库数量， 已退货数量，实际入库数量等
		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		for(Integer key : inmap.keySet()){
			int in_quantity = inmap.get(key) ;
			int return_quantity = 0 ;
			if(total_returnmap.containsKey(key)){
				return_quantity = total_returnmap.get(key);
			}
			int actual_in_quantity = in_quantity - return_quantity;
			HashMap<String, Object> tempHash = new HashMap<String, Object>();
			tempHash.put("planOrderDetailId", key);
			tempHash.put("in_quantity", in_quantity);//入库
			tempHash.put("return_quantity", return_quantity);//退货
			tempHash.put("actual_in_quantity", actual_in_quantity);//实际入库 = 总入库 - 退货
			resultlist.add(tempHash);
		}
		for(Map<String,Object> map : resultlist){
			int planOrderDetailId = (Integer)map.get("planOrderDetailId");			
			if(colorSizeMap.containsKey(planOrderDetailId)){
				map.put("color", colorSizeMap.get(planOrderDetailId).getColor());
				map.put("weight", colorSizeMap.get(planOrderDetailId).getWeight());
				map.put("size", colorSizeMap.get(planOrderDetailId).getSize());
				map.put("yarn", colorSizeMap.get(planOrderDetailId).getYarn());
			}
		}
		return resultlist;
		
	}

}
