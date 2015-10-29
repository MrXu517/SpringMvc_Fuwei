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
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.entity.producesystem.StoreInOutDetail;
import com.fuwei.entity.producesystem.StoreReturn;
import com.fuwei.entity.producesystem.StoreReturnDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.service.producesystem.MaterialCurrentStockService;
import com.fuwei.service.producesystem.StoreInOutService;
import com.fuwei.service.producesystem.StoreReturnService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/store_return")
@Controller
/* 原材料退货单 */
public class StoreReturnController extends BaseController {
	@Autowired
	StoreInOutService storeInOutService;
	@Autowired
	StoreOrderService storeOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	MessageService messageService;
	@Autowired
	MaterialCurrentStockService materialCurrentStockService;
	@Autowired
	StoreReturnService storeReturnService;

	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "store_return/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料退货列表的权限", null);
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

		pager = storeReturnService.getList(pager, start_time_d, end_time_d,
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
		return new ModelAndView("store_return/index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少原材料退货单ID");
		}
		String lcode = "store_return/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料退货单详情的权限",
					null);
		}
		StoreReturn storeReturn = storeReturnService.get(id);
		if (storeReturn == null) {
			throw new Exception("找不到ID为" + id + "的原材料退货单");
		}
		request.setAttribute("storeReturn", storeReturn);
		return new ModelAndView("store_return/detail");	
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String orderNumber,Integer factoryId,HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StoreOrder storeOrder = storeOrderService.getByOrderNumber(orderNumber);
		 if(storeOrder == null){
			 throw new Exception("找不到订单号为" + orderNumber + "的订单");
		 }
		 return addbyorder(storeOrder.getId(),factoryId, session, request, response);
	}

	@RequestMapping(value = "/{storeOrderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer storeOrderId,Integer factoryId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (storeOrderId == null) {
			throw new Exception("原材料仓库单ID不能为空");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_return/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料退货单的权限",
					null);
		}
		try {
			StoreOrder storeOrder = storeOrderService.get(storeOrderId);
			if (storeOrder == null) {
				throw new Exception(
						"该原材料仓库单不存在或已被删除", null);
			}
		
			
			//找到可以半成品退货的工厂：该订单有过入库的染色单位
			Map<Integer,String> factoryMap = new HashMap<Integer, String>();
			List<StoreInOut> tempstoreInList = storeInOutService.getByStoreOrder(storeOrderId, true);
			for(StoreInOut temp : tempstoreInList){
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
			request.setAttribute("factoryId", factoryId);
			
			
			if(factoryId!=null && factoryId!=0){
				//获取未退库列表 ， 包括总入库，已退货，实际入库
				//获取已开的入库单
				List<StoreInOut> storeInList = storeInOutService.getByFactory(storeOrderId,factoryId,true);
				//获取已开的退货单
				List<StoreReturn> storeReturnList = storeReturnService.getByFactory(storeOrderId,factoryId);
				//获取当前库存
				List<Map<String,Object>> detaillist = getInStoreQuantity(storeInList,storeReturnList);
				boolean flag = true;
				for(Map<String,Object> tMap : detaillist){
					if((Double)tMap.get("actual_in_quantity") > 0){//实际入库数量大于0，则表示有货可退			
						flag = false;
					}
				}
				if(flag){
					throw new Exception("实际入库数量为0，已全部退货或者还未入库，无法再创建退货单！！！");
				}
				request.setAttribute("detaillist", detaillist);
			}

			request.setAttribute("storeOrder", storeOrder);
			return new ModelAndView("store_return/add");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(StoreReturn storeReturn,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_return/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料退货单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer storeOrderId = storeReturn.getStore_order_id();
			Integer orderId = storeReturn.getOrderId();
			if (orderId == null) {
				throw new Exception("订单ID不能为空");
			}
			Integer factoryId = storeReturn.getFactoryId();
			if (factoryId == null|| factoryId == 0) {
				throw new Exception("必须指定染色单位", null);
			}
			if (storeReturn.getDate() == null) {
				throw new Exception("退货日期不能为空", null);
			}
			Order order = orderService.get(orderId);
			if (order == null) {
				throw new Exception(
						"该订单不存在或已被删除", null);
			}
			/* 判断有些数据不能为空 */
			

			if (storeReturn.getId() == null) {// 添加原材料退货单
				Map<String,Object> data = new HashMap<String, Object>();
				storeReturn.setCreated_at(DateTool.now());// 设置创建时间
				storeReturn.setUpdated_at(DateTool.now());// 设置更新时间
				storeReturn.setCreated_user(user.getId());// 设置创建人

				// 以下是order的属性
				storeReturn.setOrderId(order.getId());
				storeReturn.setImg(order.getImg());
				storeReturn.setImg_s(order.getImg_s());
				storeReturn.setImg_ss(order.getImg_ss());
				storeReturn.setProductNumber(order.getProductNumber());
				storeReturn.setName(order.getName());
				storeReturn.setCompanyId(order.getCompanyId());
				storeReturn.setCustomerId(order.getCustomerId());
				storeReturn.setSampleId(order.getSampleId());
				storeReturn.setOrderNumber(order.getOrderNumber());
				storeReturn.setCharge_employee(order.getCharge_employee());
				storeReturn.setCompany_productNumber(order
						.getCompany_productNumber());

				List<StoreReturnDetail> detaillist = SerializeTool
						.deserializeList(details, StoreReturnDetail.class);
				
				//判断是否有数量为0的明细项
				Iterator<StoreReturnDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					StoreReturnDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次退货数量均为0，无法创建退货单");
				}
				//判断是否有数量为0的明细项
				
				//设置退货明细id
				for(int count = 0 ; count < detaillist.size() ; ++count){
					StoreReturnDetail detail = detaillist.get(count);
					detail.setId(count+1);
				}
				storeReturn.setDetaillist(detaillist);

				
				/*判断半成品退货总数大于实际入库数量*/
				List<StoreInOut> storeInList = storeInOutService.getByFactory(storeOrderId,factoryId,true);
				List<StoreReturn> storeReturnList = storeReturnService.getByFactory(storeOrderId,factoryId);
				storeReturnList.add(storeReturn);
				List<Map<String,Object>> returnlist = getInStoreQuantity(storeInList,storeReturnList);
				
				for(Map<String,Object> item:returnlist){
					double actual_in_quantity = (Double)item.get("actual_in_quantity");
					if(actual_in_quantity<0){//半成品退货总数大于实际入库数量
						throw new Exception( "原材料退货总数大于实际入库数量");
					}
				}
				/*判断半成品退货总数大于实际入库数量*/
				
				int tableOrderId = storeReturnService.add(storeReturn);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			} else {// 编辑
				Map<String,Object> data = new HashMap<String, Object>();
				storeReturn.setUpdated_at(DateTool.now());
				List<StoreReturnDetail> detaillist = SerializeTool
						.deserializeList(details, StoreReturnDetail.class);
				//判断是否有数量为0的明细项
				Iterator<StoreReturnDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					StoreReturnDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次退货数量均为0，无法创建退货单");
				}
				//判断是否有数量为0的明细项
				
				//设置退货明细id
				int maxId = 0 ; 
				for(StoreReturnDetail detail : detaillist){
					if(detail.getId() <= 0){
						continue;
					}else if(detail.getId() > maxId){
						maxId = detail.getId();
					}
				}
				for(int i = 0,count = 0 ; i < detaillist.size() ; ++i){
					StoreReturnDetail detail = detaillist.get(count);
					if(detail.getId() <= 0){
						detail.setId(count+1 + maxId);
						++count;
					}
					
				}
				
				storeReturn.setDetaillist(detaillist);
				
				/*判断入库量总和是否大于计划单总量*/
				List<StoreInOut> storeInList = storeInOutService.getByFactory(storeOrderId,factoryId, true);
				List<StoreReturn> storeReturnList = storeReturnService.getByFactory(storeOrderId,factoryId);
				for(int i = 0 ; i < storeReturnList.size() ; ++i){
					StoreReturn item= storeReturnList.get(i);
					if(item.getId() == storeReturn.getId()){
						storeReturnList.set(i, storeReturn);
					}
				}
				List<Map<String,Object>> returnlist = getInStoreQuantity(storeInList,storeReturnList);
				for(Map<String,Object> item:returnlist){
					double actual_in_quantity = (Double)item.get("actual_in_quantity");
					if(actual_in_quantity<0){//半成品退货总数大于实际入库数量
						throw new Exception("原材料退货总数大于实际入库数量");
					}
				}
				/*判断入库量总和是否大于计划单总量*/
				
				int tableOrderId = storeReturnService.update(storeReturn);
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
		String lcode = "store_return/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除原材料退货单的权限", null);
		}
		StoreReturn storeReturn = storeReturnService.get(id);	
		int success = storeReturnService.remove(storeReturn);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("store_return/scan");
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
			throw new Exception("缺少原材料退货单ID");
		}
		String lcode = "store_return/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印原材料退货单的权限", null);
		}
		StoreReturn storeReturn = storeReturnService.get(id);
		if (storeReturn == null) {
			throw new Exception("找不到ID为" + id + "的原材料退货单");
		}
		storeReturn.setHas_print(true);
		storeReturnService.updatePrint(storeReturn);
		request.setAttribute("storeReturn", storeReturn);
		return new ModelAndView("store_return/print");
	}

	
	public List<Map<String,Object>> getInStoreQuantity(List<StoreInOut> storeInList , List<StoreReturn> storeReturnList) throws Exception{	
		//storeInList，storeReturnList 已锁定某个工厂
		//根据 【色号、材料、缸号】获取已入库 数量
		HashMap<String, HashMap<String, Double>> inmap = new HashMap<String, HashMap<String, Double>>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 				
				if(inmap.containsKey(key)){
					HashMap<String, Double> lotno_quantity = inmap.get(key);
					String key_lotno = temp.getLot_no();
					if(lotno_quantity.containsKey(key_lotno)){
						double temp_total_quantity = lotno_quantity.get(key_lotno);
						lotno_quantity.put(key_lotno, temp_total_quantity + temp.getQuantity());
						inmap.put(key, lotno_quantity);
					}else{
						lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
						inmap.put(key, lotno_quantity);
					}
				}else{
					HashMap<String, Double> lotno_quantity = new HashMap<String, Double>();
					lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
					inmap.put(key, lotno_quantity);
				}
			}
		}
		
		//根据 【key】 统计已退货 数量
		HashMap<String, HashMap<String, Double>> total_returnmap = new HashMap<String, HashMap<String, Double>>();
		for (StoreReturn storereturn : storeReturnList) {
			for (StoreReturnDetail temp : storereturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_returnmap.containsKey(key)){
					HashMap<String, Double> lotno_quantity = total_returnmap.get(key);
					String key_lotno = temp.getLot_no();
					if(lotno_quantity.containsKey(key_lotno)){
						double temp_total_quantity = lotno_quantity.get(key_lotno);
						lotno_quantity.put(key_lotno, temp_total_quantity + temp.getQuantity());
						total_returnmap.put(key, lotno_quantity);
					}else{
						lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
						total_returnmap.put(key, lotno_quantity);
					}
				}else{
					HashMap<String, Double> lotno_quantity = new HashMap<String, Double>();
					lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
					total_returnmap.put(key, lotno_quantity);
				}
			}
		}
		
		//获取未退货列表，包括 总入库数量， 已退货数量，实际入库数量等
		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		for(String key : inmap.keySet()){
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			
			HashMap<String, Double> lot_inquantityMap = inmap.get(key);
			for(String lot_no : lot_inquantityMap.keySet()){
				Double in_quantity = lot_inquantityMap.get(lot_no);//获取已入库数量
				double return_quantity = 0 ;
				if(total_returnmap.containsKey(key)){
					if(total_returnmap.get(key).containsKey(lot_no)){
						return_quantity = total_returnmap.get(key).get(lot_no);
					}
				}
				double actual_in_quantity = in_quantity - return_quantity;
				
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("material", material);
				itemMap.put("color", color);
				itemMap.put("lot_no", lot_no);
				itemMap.put("in_quantity", in_quantity);//入库
				itemMap.put("return_quantity", return_quantity);//退货
				itemMap.put("actual_in_quantity", actual_in_quantity);//实际入库 = 总入库 - 退货
				
				resultlist.add(itemMap);
			}
		}
		return resultlist;
		
	}

}
