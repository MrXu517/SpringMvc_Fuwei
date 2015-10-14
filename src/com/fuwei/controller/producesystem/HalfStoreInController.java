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
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.producesystem.HalfCurrentStockService;
import com.fuwei.service.producesystem.HalfStoreInOutService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/half_store_in")
@Controller
/* 半成品入库单 */
public class HalfStoreInController extends BaseController {
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

	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "half_store_in_out/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看入库列表的权限", null);
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
				companyId, factoryId, charge_employee, number, true, sortList);
		
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
		return new ModelAndView("half_store_in_out/in_index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少半成品入库单ID");
		}
		String lcode = "half_store_in_out/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看半成品入库单详情的权限",
					null);
		}
		HalfStoreInOut storeInOut = halfStoreInOutService.get(id, true);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的半成品入库单");
		}
		request.setAttribute("storeInOut", storeInOut);
		return new ModelAndView("half_store_in_out/in_detail");	
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String orderId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//storeOrderId可能为原材料仓库ID， 或者订单 orderNumber	
		Integer new_OrderId = null ;
		 try{
		      new_OrderId = Integer.parseInt(orderId);
		 }catch(Exception e){
			 Order order =  orderService.get(orderId);
			 if(order == null){
				 throw new Exception("找不到订单ID或订单号为" + orderId + "的订单");
			 }
			 new_OrderId = order.getId();
		 }
		 return addbyorder(new_OrderId, session, request, response);
	}

	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (orderId == null) {
			throw new Exception("订单ID不能为空");
		}

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半成品入库单的权限",
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
			
			
			
			//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
			//获取已开的入库单
			List<HalfStoreInOut> storeInList = halfStoreInOutService.getByOrder(orderId,true);
			List<HalfStoreInOut> halfstoreOutList = halfStoreInOutService.getByOrder(orderId,false);
			List<Map<String,Object>> detaillist = getInStoreQuantity(planOrder,storeInList,halfstoreOutList);
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			boolean flag = true;
			for(Map<String,Object> tMap : detaillist){
				if((Integer)tMap.get("not_in_quantity") > 0){				
					flag = false;
				}
			}
			if(flag){
				request.setAttribute("message", "未入库数量为0，半成品已全部入库 ，无需再创建入库单， 请确认是否超量！！！");
			}
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			
			request.setAttribute("detaillist", detaillist);

			request.setAttribute("order", order);
			return new ModelAndView("half_store_in_out/add_in");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(HalfStoreInOut halfStoreInOut,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑半成品入库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer orderId = halfStoreInOut.getOrderId();
			if (orderId == null) {
				throw new Exception("订单ID不能为空");
			}
			if (halfStoreInOut.getFactoryId() == null
					|| halfStoreInOut.getFactoryId() == 0) {
				throw new Exception("必须指定送货单位", null);
			}
			// if (storeInOut.getEmployee_id() == null
			// || storeInOut.getEmployee_id() == 0) {
			// throw new Exception("经办人不能为空", null);
			// }
			if (halfStoreInOut.getDate() == null) {
				throw new Exception("入库日期不能为空", null);
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
			

			if (halfStoreInOut.getId() == null) {// 添加半成品入库单
				Map<String,Object> data = new HashMap<String, Object>();
				halfStoreInOut.setCreated_at(DateTool.now());// 设置创建时间
				halfStoreInOut.setUpdated_at(DateTool.now());// 设置更新时间
				halfStoreInOut.setCreated_user(user.getId());// 设置创建人

				// 以下是order的属性
				halfStoreInOut.setOrderId(order.getId());
				halfStoreInOut.setImg(order.getImg());
				halfStoreInOut.setImg_s(order.getImg_s());
				halfStoreInOut.setImg_ss(order.getImg_ss());
				halfStoreInOut.setProductNumber(order.getProductNumber());
				halfStoreInOut.setMaterialId(order.getMaterialId());
				halfStoreInOut.setSize(order.getSize());
				halfStoreInOut.setWeight(order.getWeight());
				halfStoreInOut.setName(order.getName());
				halfStoreInOut.setCompanyId(order.getCompanyId());
				halfStoreInOut.setCustomerId(order.getCustomerId());
				halfStoreInOut.setSampleId(order.getSampleId());
				halfStoreInOut.setOrderNumber(order.getOrderNumber());
				halfStoreInOut.setCharge_employee(order.getCharge_employee());
				halfStoreInOut.setCompany_productNumber(order
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
				    if(detail.getPlanOrderDetailId()<=0){//填了数量，但planOrderDetailId为空则也不行
				    	throw new Exception("计划单对应明细ID不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建入库单");
				}
				//判断是否有数量为0的明细项
				
				//设置入库明细id
				for(int count = 0 ; count < detaillist.size() ; ++count){
					HalfStoreInOutDetail detail = detaillist.get(count);
					detail.setId(count+1);
				}
				halfStoreInOut.setDetaillist(detaillist);

				halfStoreInOut.setIn_out(true);
				
				
				/*判断入库量总和是否大于计划单总量*/
				List<HalfStoreInOut> storeInList = halfStoreInOutService.getByOrder(orderId,true);
				storeInList.add(halfStoreInOut);
				List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByOrder(orderId,false);
				List<Map<String,Object>> not_in_quantityList = getInStoreQuantity(planOrder,storeInList,storeOutList);
				for(Map<String,Object> item:not_in_quantityList){
					int not_in_quantity = (Integer)item.get("not_in_quantity");
					if(not_in_quantity<0){//入库总数大于计划单的数量
//						throw new Exception("入库总数大于计划单的数量");
						data.put("message", "半成品入库总数大于计划单的数量");
					}
				}
				/*判断入库量总和是否大于计划单总量*/
				
				int tableOrderId = halfStoreInOutService.add(halfStoreInOut);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			} else {// 编辑
				Map<String,Object> data = new HashMap<String, Object>();
				halfStoreInOut.setUpdated_at(DateTool.now());
				List<HalfStoreInOutDetail> detaillist = SerializeTool
						.deserializeList(details, HalfStoreInOutDetail.class);
				//判断是否有数量为0的明细项
				Iterator<HalfStoreInOutDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					HalfStoreInOutDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				    if(detail.getPlanOrderDetailId()<=0){//填了数量，但planOrderDetailId为空则也不行
				    	throw new Exception("计划单对应明细ID不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建入库单");
				}
				//判断是否有数量为0的明细项
				
				//设置入库明细id
				int maxId = 0 ; 
				for(HalfStoreInOutDetail detail : detaillist){
					if(detail.getId() <= 0){
						continue;
					}else if(detail.getId() > maxId){
						maxId = detail.getId();
					}
				}
				for(int i = 0,count = 0 ; i < detaillist.size() ; ++i){
					HalfStoreInOutDetail detail = detaillist.get(count);
					if(detail.getId() <= 0){
						detail.setId(count+1 + maxId);
						++count;
					}
					
				}
				
				halfStoreInOut.setDetaillist(detaillist);
				halfStoreInOut.setIn_out(true);
				
				/*判断入库量总和是否大于计划单总量*/
				List<HalfStoreInOut> halfstoreInList = halfStoreInOutService.getByOrder(orderId,true);
				for(int i = 0 ; i < halfstoreInList.size() ; ++i){
					HalfStoreInOut item= halfstoreInList.get(i);
					if(item.getId() == halfStoreInOut.getId()){
						halfstoreInList.set(i, halfStoreInOut);
					}
				}
				List<HalfStoreInOut> halfstoreOutList = halfStoreInOutService.getByOrder(orderId,false);
				List<Map<String,Object>> not_in_quantityList = getInStoreQuantity(planOrder,halfstoreInList,halfstoreOutList);
				for(Map<String,Object> item:not_in_quantityList){
					int not_in_quantity = (Integer)item.get("not_in_quantity");
					if(not_in_quantity<0){//入库总数大于原材料仓库单的数量
//						throw new Exception("入库总数大于计划单的数量");
						data.put("message", "入库总数大于计划单的数量");
					}
					if((Integer)item.get("in_quantity")<0){
						throw new Exception("入库单总量总和小于出库单总量，修改半成品入库单失败");
					}
				}
				/*判断入库量总和是否大于计划单总量*/
//				
//				/*判断入库量总和是否大于出库单总量，即判断若改变当前入库量，是否会导致入库总量 < 出库总量*/
//				//获取已开的出库单
//				List<Map<String,Object>> stocklist =  getStockQuantity(halfstoreInList,halfstoreOutList);
//				for(Map<String,Object> stockMap : stocklist){
//					if((Integer)stockMap.get("stock_quantity")<0){
//						throw new Exception("入库单总量总和小于出库单总量，修改半成品入库单失败");
//					}
//				}
//				/*判断入库量总和是否大于出库单总量，即判断若改变当前入库量，是否会导致入库总量 < 出库总量*/
				
				int tableOrderId = halfStoreInOutService.update(halfStoreInOut);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "half_store_in_out/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑半成品入库单的权限", null);
		}
		try {
			if (id != null) {
				HalfStoreInOut halfStoreInOut = halfStoreInOutService.get(id, true);
				if(halfStoreInOut == null){
					throw new Exception("找不到ID为" + id + "的半成品入库单");
				}
				request.setAttribute("halfStoreInOut", halfStoreInOut);
				//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
				//获取已开的入库单
				int orderId = halfStoreInOut.getOrderId();
				PlanOrder planOrder = planOrderService.getByOrder(orderId);
				if (planOrder == null) {
					throw new PermissionDeniedDataAccessException(
							"计划单不存在或已被删除", null);
				}
				if (planOrder.getDetaillist() == null
						|| planOrder.getDetaillist().size() <= 0) {
					throw new PermissionDeniedDataAccessException(
							"计划单缺少颜色及数量列表，请先修改 ", null);
				}
				request.setAttribute("planOrder", planOrder);
				
				List<HalfStoreInOut> storeInList = halfStoreInOutService.getByOrder(orderId,true);
				List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByOrder(orderId,false);
				List<Map<String,Object>> detaillist = getInStoreQuantity(planOrder,storeInList,storeOutList);
				
				List<Map<String,Object>> detaillist_result = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> not_outMap : detaillist){
					not_outMap.put("quantity",0);
					for(HalfStoreInOutDetail detail : halfStoreInOut.getDetaillist()){
						if(detail.getPlanOrderDetailId() == (Integer)not_outMap.get("planOrderDetailId")){
							not_outMap.put("quantity",detail.getQuantity());
							not_outMap.put("id", detail.getId());
						}
						
					}
					
					detaillist_result.add(not_outMap);
				}
				
				request.setAttribute("detaillist", detaillist_result);

				return new ModelAndView("half_store_in_out/edit_in");
			}
			throw new Exception("缺少半成品入库单ID");

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
		String lcode = "half_store_in_out/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除半成品入库单的权限", null);
		}
		/*判断删除后入库量是否小于出库量，若是，则不可以删除*/
		HalfStoreInOut storeIn = halfStoreInOutService.get(id);
		HalfCurrentStock currentStock = halfCurrentStockService.get(storeIn.getOrderId());
		for(HalfStoreInOutDetail detail : storeIn.getDetaillist()){
			int to_be_deleted_quantity = detail.getQuantity();
			int stock_quantity = 0 ;
			if(currentStock == null){
				throw new Exception("发生错误：库存量未知");
			}
			for(HalfCurrentStockDetail tempD:currentStock.getDetaillist()){
				if(tempD.getPlanOrderDetailId() == detail.getPlanOrderDetailId()){
					stock_quantity = tempD.getStock_quantity();
				}
			}
			if(stock_quantity < to_be_deleted_quantity){//若库存量 < 需删除，则无法删除
				throw new Exception("库存量小于删除的入库单数量，无法删除");
			}
		}
		
		/*判断删除后入库量是否小于出库量，若是，则不可以删除*/
		
		int success = halfStoreInOutService.remove(storeIn);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("half_store_in_out/in_scan");
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
			throw new Exception("缺少半成品入库单ID");
		}
		String lcode = "half_store_in_out/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印半成品入库单的权限", null);
		}
		HalfStoreInOut halfstoreInOut = halfStoreInOutService.get(id, true);
		if (halfstoreInOut == null) {
			throw new Exception("找不到ID为" + id + "的半成品入库单");
		}
		halfstoreInOut.setHas_print(true);
		halfStoreInOutService.updatePrint(halfstoreInOut);
		request.setAttribute("halfStoreInOut", halfstoreInOut);
		return new ModelAndView("half_store_in_out/in_print");
	}

	
	public List<Map<String,Object>> getInStoreQuantity(PlanOrder planOrder , List<HalfStoreInOut> halfstoreInList , List<HalfStoreInOut> halfstoreOutList) throws Exception{	
		if(planOrder.getDetaillist() == null){
			return null;
		}
		
		//根据  【planOrderDetailId】  统计总数量 , key = planOrderDetailId
		HashMap<Integer, Integer> totalmap = new HashMap<Integer, Integer>();
		for (PlanOrderDetail detail : planOrder.getDetaillist()) {
			int key = detail.getId(); 
			if(totalmap.containsKey(key)){
				int temp_total_quantity = totalmap.get(key);
				totalmap.put(key, temp_total_quantity + detail.getQuantity());
			}else{
				totalmap.put(key, detail.getQuantity());
			}
		}
		
		
		
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
			}
		}
		
		//根据 【planOrderDetailId】 统计已出库 数量
		HashMap<Integer, Integer> total_outmap = new HashMap<Integer, Integer>();
		for (HalfStoreInOut storeOut : halfstoreOutList) {
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
		
		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		for(Integer key : totalmap.keySet()){
			int total_quantity = totalmap.get(key);
			int in_quantity = 0 ;
			if(inmap.containsKey(key)){
				in_quantity = inmap.get(key);//库存数量
			}
			if(total_outmap.containsKey(key)){
				in_quantity = in_quantity - total_outmap.get(key);
			}
			int not_in_quantity = total_quantity - in_quantity;
			HashMap<String, Object> tempHash = new HashMap<String, Object>();
			tempHash.put("planOrderDetailId", key);
			tempHash.put("total_quantity", total_quantity);//计划单总量
			tempHash.put("in_quantity", in_quantity);//库存
			tempHash.put("not_in_quantity", not_in_quantity);//未入库
			resultlist.add(tempHash);
		}
		
		for(Map<String,Object> map : resultlist){
			int planOrderDetailId = (Integer)map.get("planOrderDetailId");
			for(PlanOrderDetail detail : planOrder.getDetaillist()){
				if(planOrderDetailId == detail.getId()){
					map.put("color", detail.getColor());
					map.put("weight", detail.getProduce_weight());
					map.put("size", detail.getSize());
				}
			}
		}
		
		
		return resultlist;
	}
//	
//	public List<Map<String,Object>> getStockQuantity(List<HalfStoreInOut> halfstoreInList,List<HalfStoreInOut> halfstoreOutList) throws Exception{	
//		//根据  【planOrderDetailId】  统计入库总数量 , key = planOrderDetailId
//		HashMap<Integer, Integer> total_inmap = new HashMap<Integer, Integer>();
//		for (HalfStoreInOut storeIn : halfstoreInList) {
//			for (HalfStoreInOutDetail temp : storeIn.getDetaillist()) {
//				int key = temp.getPlanOrderDetailId(); 
//				if(total_inmap.containsKey(key)){
//					int quantity = total_inmap.get(key) + temp.getQuantity();
//					total_inmap.put(key, quantity);
//				}else{
//					total_inmap.put(key, temp.getQuantity());
//				}
//			}
//		}
//		
//
//		//根据 【planOrderDetailId】 统计已出库 数量
//		HashMap<Integer, Integer> total_outmap = new HashMap<Integer, Integer>();
//		for (HalfStoreInOut storeOut : halfstoreOutList) {
//			for (HalfStoreInOutDetail temp : storeOut.getDetaillist()) {
//				int key = temp.getPlanOrderDetailId(); 
//				if(total_outmap.containsKey(key)){
//					int temp_total_quantity = total_outmap.get(key);
//					total_outmap.put(key, temp_total_quantity + temp.getQuantity());
//				}else{
//					total_outmap.put(key, temp.getQuantity());
//				}
//			}
//		}
//
//		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
//		//获取当前库存（未出库）列表
//		for(int key : total_inmap.keySet()){
//			int in_quantity = total_inmap.get(key);
//			Integer out_quantity = total_outmap.get(key);
//			//库存数量 = 入库数量 - 出库数量
//			int stock_quantity = out_quantity == null? in_quantity:in_quantity - out_quantity;
//			
//			HashMap<String, Object> tempHash = new HashMap<String, Object>();
//			tempHash.put("planOrderDetailId", key);
//			tempHash.put("stock_quantity", stock_quantity);
//			resultlist.add(tempHash);
//		}
//		
//		return resultlist;
//	}

}
