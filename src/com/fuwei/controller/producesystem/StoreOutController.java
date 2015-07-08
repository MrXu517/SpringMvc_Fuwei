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

import net.sf.json.JSONObject;

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
import com.fuwei.entity.Employee;
import com.fuwei.entity.Message;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail;
import com.fuwei.entity.ordergrid.StoreInOut;
import com.fuwei.entity.ordergrid.StoreInOutDetail;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.ordergrid.StoreOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.ordergrid.StoreInOutService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;
import com.google.gson.JsonObject;

@RequestMapping("/store_out")
@Controller
/* 原材料出库单 */
public class StoreOutController extends BaseController {
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
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer factoryId,Integer charge_employee, String number,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "store_in_out/index";
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
		pager = storeInOutService.getList(pager, start_time_d, end_time_d,
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
		return new ModelAndView("store_in_out/out_index");
	}

	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少原材料出库单ID");
		}
		String lcode = "store_in_out/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料出库单详情的权限",
					null);
		}
		StoreInOut storeInOut = storeInOutService.get(id, false);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的原材料出库单");
		}
		request.setAttribute("storeInOut", storeInOut);
		return new ModelAndView("store_in_out/out_detail");
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String storeOrderId,Integer factoryId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//storeOrderId可能为原材料仓库ID， 或者订单 orderNumber	
		 try{
		      int new_storeOrderId = Integer.parseInt(storeOrderId);
		      return addbyorder(new_storeOrderId,factoryId, session, request, response);
		 }catch(Exception e){
			 StoreOrder storeOrder =  storeOrderService.getByOrderNumber(storeOrderId);
			 return addbyorder(storeOrder.getId(),factoryId, session, request, response);
		 }
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
		String lcode = "store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料出库单的权限",
					null);
		}
		try {
			StoreOrder storeOrder = storeOrderService.get(storeOrderId);
			if (storeOrder == null) {
				throw new PermissionDeniedDataAccessException(
						"该原材料仓库单不存在或已被删除", null);
			}
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new PermissionDeniedDataAccessException(
						"原材料仓库单缺少材料列表，请先修改原材料仓库的材料列表 ", null);
			}
			request.setAttribute("storeOrder", storeOrder);
			
			//获取可以领取材料的所有工厂
			Map<Integer,String> factoryMap = new HashMap<Integer, String>();
			for(StoreOrderDetail detail : storeOrder.getDetaillist()){
				int tempfactoryId = detail.getFactoryId();
				if(!factoryMap.containsKey(tempfactoryId)){
					factoryMap.put(tempfactoryId, SystemCache.getFactoryName(tempfactoryId));
				}
				
			}
			request.setAttribute("factoryMap", factoryMap);
			
			List<Map<String,Object>> factory_not_outlist = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lot_not_outlist = new ArrayList<Map<String,Object>>();
			if(factoryId!=null && factoryId >0){
				//获取已开的入库单
				List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
				//获取已开的出库单
				List<StoreInOut> storeOutList = storeInOutService.getByStoreOrder(storeOrderId,false);
				getOutStoreQuantity(factory_not_outlist,lot_not_outlist,factoryId,storeOrder,storeInList,storeOutList);
				request.setAttribute("factoryId", factoryId);
			}
			
			request.setAttribute("factory_not_outlist", factory_not_outlist);
			request.setAttribute("lot_not_outlist", lot_not_outlist);
			return new ModelAndView("store_in_out/add_out");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(StoreInOut storeInOut,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料出库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer storeOrderId = storeInOut.getStore_order_id();
			if (storeOrderId == null) {
				throw new Exception("原材料仓库单ID不能为空");
			}
			Integer factoryId = storeInOut.getFactoryId();
			if (factoryId == null || factoryId == 0) {
				throw new Exception("必须指定领取单位", null);
			}
//			if (storeInOut.getEmployee_id() == null
//					|| storeInOut.getEmployee_id() == 0) {
//				throw new Exception("经办人不能为空", null);
//			}
			if (storeInOut.getDate() == null) {
				throw new Exception("出库日期不能为空", null);
			}
			StoreOrder storeOrder = storeOrderService.get(storeOrderId);
			if (storeOrder == null) {
				throw new PermissionDeniedDataAccessException(
						"该原材料仓库单不存在或已被删除", null);
			}
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new PermissionDeniedDataAccessException(
						"原材料仓库单缺少材料列表，请先修改原材料仓库的材料列表 ", null);
			}
			/* 判断有些数据不能为空 */

			if (storeInOut.getId() == null) {// 添加原材料入库单
				int orderId = storeOrder.getOrderId();
				Order order = orderService.get(orderId);

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

				List<StoreInOutDetail> detaillist = SerializeTool
						.deserializeList(details, StoreInOutDetail.class);
				
				//判断是否有数量为0的明细项
				Iterator<StoreInOutDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					StoreInOutDetail detail = iter.next();  
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
				
				/*判断出库量总和是否大于原材料仓库总量，判断出库量总和是否大于入库总数*/
				List<Map<String,Object>> factory_not_outlist = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> lot_not_outlist = new ArrayList<Map<String,Object>>();
				//获取已开的入库单
				List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
				//获取已开的出库单
				List<StoreInOut> storeOutList = storeInOutService.getByStoreOrder(storeOrderId,false);
				storeOutList.add(storeInOut);
				getOutStoreQuantity(factory_not_outlist,lot_not_outlist,factoryId,storeOrder,storeInList,storeOutList);
				for(Map<String,Object> item:factory_not_outlist){
					double not_out_quantity = (Double)item.get("not_out_quantity");
					if(not_out_quantity<0){//出库总数大于原材料仓库单的数量
						throw new Exception("色号：" +  item.get("color") + "," 
								+ "材料：" +  SystemCache.getMaterialName((Integer)item.get("material"))+
								"的出库总数大于原材料仓库单的数量");
					}
				}
				for(Map<String,Object> item:lot_not_outlist){
					double not_out_quantity = (Double)item.get("not_out_quantity");
					if(not_out_quantity<0){//出库总数大于入库数量
						throw new Exception( "色号：" +  item.get("color") + "," 
								+ "材料：" +  SystemCache.getMaterialName((Integer)item.get("material")) + ","
								+ "缸号：" + item.get("lot_no") +  "的出库总数大于入库数量");
					}
				}
				/*判断出库量总和是否大于原材料仓库总量，判断出库量总和是否大于入库总数*/
				int tableOrderId = storeInOutService.add(storeInOut);
				return this.returnSuccess("id", tableOrderId);
			} else {// 编辑
				storeInOut.setUpdated_at(DateTool.now());
				List<StoreInOutDetail> detaillist = SerializeTool
						.deserializeList(details, StoreInOutDetail.class);
				//判断是否有数量为0的明细项
				Iterator<StoreInOutDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					StoreInOutDetail detail = iter.next();  
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
				
				/*判断出库量总和是否大于原材料仓库总量，判断出库量总和是否大于入库总数*/
				List<Map<String,Object>> factory_not_outlist = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> lot_not_outlist = new ArrayList<Map<String,Object>>();
				//获取已开的入库单
				List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
				//获取已开的出库单
				List<StoreInOut> storeOutList = storeInOutService.getByStoreOrder(storeOrderId,false);
				for(int i = 0 ; i < storeOutList.size() ; ++i){
					StoreInOut item= storeOutList.get(i);
					if(item.getId() == storeInOut.getId()){
						storeOutList.set(i, storeInOut);
					}
				}
				getOutStoreQuantity(factory_not_outlist,lot_not_outlist,factoryId,storeOrder,storeInList,storeOutList);
				for(Map<String,Object> item:factory_not_outlist){
					double not_out_quantity = (Double)item.get("not_out_quantity");
					if(not_out_quantity<0){//出库总数大于原材料仓库单的数量
						throw new Exception("色号：" +  item.get("color") + "," 
								+ "材料：" +  SystemCache.getMaterialName((Integer)item.get("material"))+
								"的出库总数大于原材料仓库单的数量");
					}
				}
				for(Map<String,Object> item:lot_not_outlist){
					double not_out_quantity = (Double)item.get("not_out_quantity");
					if(not_out_quantity<0){//出库总数大于入库数量
						throw new Exception( "色号：" +  item.get("color") + "," 
								+ "材料：" +  SystemCache.getMaterialName((Integer)item.get("material")) + ","
								+ "缸号：" + item.get("lot_no") +  "的出库总数大于入库数量");
					}
				}
				/*判断出库量总和是否大于原材料仓库总量，判断出库量总和是否大于入库总数*/
				
				int tableOrderId = storeInOutService.update(storeInOut);
				return this.returnSuccess("id", tableOrderId);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView put(@PathVariable Integer id,Integer factoryId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(id == null){
			throw new Exception("原材料出库单ID不能为null");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_in_out/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑原材料出库单的权限", null);
		}
		try {
			StoreInOut object = storeInOutService.get(id, false);
			if(object == null){
				throw new Exception("找不到ID为" + id + "的原材料出库单");
			}
			if(factoryId == null){
				factoryId = object.getFactoryId();
			}
			request.setAttribute("storeInOut", object);
			int storeOrderId = object.getStore_order_id();
			StoreOrder storeOrder = storeOrderService.get(storeOrderId);
			request.setAttribute("storeOrder", storeOrder);
			
			//获取可以领取材料的所有工厂
			Map<Integer,String> factoryMap = new HashMap<Integer, String>();
			for(StoreOrderDetail detail : storeOrder.getDetaillist()){
				int tempfactoryId = detail.getFactoryId();
				if(!factoryMap.containsKey(tempfactoryId)){
					factoryMap.put(tempfactoryId, SystemCache.getFactoryName(tempfactoryId));
				}
				
			}
			request.setAttribute("factoryMap", factoryMap);
			
			List<Map<String,Object>> factory_not_outlist = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lot_not_outlist = new ArrayList<Map<String,Object>>();
			if(factoryId!=null && factoryId >0){
				//获取已开的入库单
				List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
				//获取已开的出库单
				List<StoreInOut> storeOutList = storeInOutService.getByStoreOrder(storeOrderId,false);
				getOutStoreQuantity(factory_not_outlist,lot_not_outlist,factoryId,storeOrder,storeInList,storeOutList);
				request.setAttribute("factoryId", factoryId);
			}
			
			List<Map<String,Object>> lot_outlist = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> not_outMap : lot_not_outlist){
				not_outMap.put("quantity",0);
				for(StoreInOutDetail detail : object.getDetaillist()){
					if(detail.getColor().trim().equals(not_outMap.get("color")) && detail.getMaterial() == (Integer)not_outMap.get("material") && detail.getLot_no().trim().equals(not_outMap.get("lot_no"))){
						not_outMap.put("quantity",detail.getQuantity());
					}
				}
				lot_outlist.add(not_outMap);
			}
			
			request.setAttribute("factory_not_outlist", factory_not_outlist);
			request.setAttribute("lot_outlist", lot_outlist);
			return new ModelAndView("store_in_out/edit_out");
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
		String lcode = "store_in_out/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除原材料出库单的权限", null);
		}
		int success = storeInOutService.remove(id);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("store_in_out/out_scan");
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
			throw new Exception("缺少原材料出库单ID");
		}
		String lcode = "store_in_out/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印原材料出库单的权限", null);
		}
		StoreInOut storeInOut = storeInOutService.get(id, false);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的原材料出库单");
		}
		storeInOut.setHas_print(true);
		storeInOutService.updatePrint(storeInOut);
		request.setAttribute("storeInOut", storeInOut);
		return new ModelAndView("store_in_out/out_print");
	}
	
	@RequestMapping(value = "/print_scan/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_scan(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少原材料出库单ID");
		}
		StoreInOut storeInOut = storeInOutService.get(id, false);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的原材料出库单");
		}
		request.setAttribute("storeInOut", storeInOut);
		return new ModelAndView("store_in_out/out_print_scan");
	}
	
	@RequestMapping(value = "/print_scan_check", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> print_scan_check(Integer storeOutId, String storeInId_detailId, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (storeOutId == null) {
			throw new Exception("缺少原材料出库单ID");
		}
		StoreInOut storeOut = storeInOutService.get(storeOutId, false);
		if (storeOut == null) {
			throw new Exception("找不到ID为" + storeOutId + "的原材料出库单");
		}
		int indexOf = storeInId_detailId.indexOf("_");
		if(indexOf <= -1){
			throw new Exception("分割原材料入库单ID与明细ID出错");
		}
		Integer storeInID = Integer.parseInt(storeInId_detailId.substring(0,indexOf));
		Integer detailId = Integer.parseInt(storeInId_detailId.substring(indexOf+1));
		

		StoreInOut storeIn = storeInOutService.get(storeInID,true);
		if(storeIn == null){
			throw new Exception("找不到ID为" + storeInID + "的原材料入库单");
		}
		StoreInOutDetail check_detail = null;
		for(StoreInOutDetail detail : storeIn.getDetaillist()){
			if(detail.getId()!=null && detail.getId() == detailId){
				check_detail = detail;
			}
		}
		if(check_detail == null){
			throw new Exception("找不到ID为" + detailId + "的原材料明细");
		}
		
		//判断订单号是否一致
		if((int)storeIn.getOrderId() != (int)storeOut.getOrderId()){
			throw new Exception("当前出库单 的订单号是 ：" +  storeOut.getOrderNumber() + " , 而这袋纱线属于订单：" + storeIn.getOrderNumber());
		}
		
		//判断色号、材料、缸号是否一致
		Boolean result = false;
		for(StoreInOutDetail detail : storeOut.getDetaillist()){
			if(detail.getColor().trim().equals(check_detail.getColor().trim()) 
					&& (int)detail.getMaterial() == (int)check_detail.getMaterial() 
					&& detail.getLot_no().trim().equals(check_detail.getLot_no().trim())){
				result = true;
			}
		}
		if(!result){//检测失败
			throw new Exception("色号、材料或者缸号不一致。 不能出库");
		}else{//检测成功
			JSONObject jsObject = new JSONObject();
			jsObject.put("storeInId_detailId",storeInId_detailId);
			jsObject.put("orderNumber",storeIn.getOrderNumber());
			jsObject.put("orderId",storeIn.getOrderId());
			jsObject.put("color",check_detail.getColor());
			jsObject.put("material",check_detail.getMaterial());
			jsObject.put("material_name",SystemCache.getMaterialName(check_detail.getMaterial()));
			jsObject.put("lot_no",check_detail.getLot_no());
			return this.returnSuccess("data",jsObject);
		}
	}
	
	
	@RequestMapping(value = "/print_scan", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> print_scan(Integer storeOutId, String details, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (storeOutId == null) {
			throw new Exception("缺少原材料出库单ID");
		}
		if(details == null){
			throw new Exception("出库明细为空");
		}
		StoreInOut storeOut = storeInOutService.get(storeOutId, false);
		if (storeOut == null) {
			throw new Exception("找不到ID为" + storeOutId + "的原材料出库单");
		}
		
		
		List<Map<String,Object>> detaillist = SerializeTool.deserializeListMap(details,Object.class);
		
	
		for(Map<String,Object> check_detail : detaillist){
			String storeInId_detailId = (String)check_detail.get("storeInId_detailId");
			Integer orderId = (Integer) check_detail.get("orderId");
			//判断订单号是否一致
			if(orderId!=null && (int)orderId != (int)storeOut.getOrderId()){
				throw new Exception("入库ID_明细ID = " + storeInId_detailId +"的订单号与出库单不一致");
			}
			//判断色号、材料、缸号是否一致
			String color = String.valueOf(check_detail.get("color"));
			Integer material = (Integer)check_detail.get("material");
			String lot_no = String.valueOf(check_detail.get("lot_no"));
			Boolean result = false;
			for(StoreInOutDetail detail : storeOut.getDetaillist()){
				if(detail.getColor().trim().equals(color.trim()) 
						&& (int)detail.getMaterial() == (int)material
						&& detail.getLot_no().trim().equals(lot_no.trim())){
					result = true;
				}
			}
			if(!result){//检测失败
				throw new Exception( storeInId_detailId + "的色号、材料或者缸号与出库单不一致。 不能出库");
			}
		}
		if(detaillist.size() != storeOut.getDetaillist().size()){
			throw new Exception( "纱线未扫描");
		}
		return this.returnSuccess("id", storeOut.getId());
		
	}

	//获取各个缸号的库存量  和  某工厂未出库量  
	public void getOutStoreQuantity(List<Map<String,Object>> factory_not_outlist,List<Map<String,Object>> lot_not_outlist, int factoryId, StoreOrder storeOrder , List<StoreInOut> storeInList , List<StoreInOut> storeOutList) throws Exception{	
		//根据【工厂】  【色号】 和 【材料】  统计总数量 , key = material + : + color
		HashMap<String, Double> factory_totalmap = new HashMap<String, Double>();
		for (StoreOrderDetail storeOrderDetail : storeOrder.getDetaillist()) {
			if(storeOrderDetail.getFactoryId() != factoryId){
				continue;
			}
			String key = storeOrderDetail.getMaterial() + ":"+ storeOrderDetail.getColor().trim(); 
			if(factory_totalmap.containsKey(key)){
				double temp_total_quantity = factory_totalmap.get(key);
				factory_totalmap.put(key, temp_total_quantity + storeOrderDetail.getQuantity());
			}else{
				factory_totalmap.put(key, storeOrderDetail.getQuantity());
			}
		}
		
		
		//根据  【色号】 和 【材料】 和 【缸号】  统计入库总数量 , key = material + : + color + : + lot_no
		HashMap<String, HashMap<String, Double>> total_inmap = new HashMap<String, HashMap<String, Double>>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(!factory_totalmap.containsKey(key)){//色号、材料不一致的不需统计
					continue;
				}
				if(total_inmap.containsKey(key)){
					HashMap<String, Double> lotno_quantity = total_inmap.get(key);
					String key_lotno = temp.getLot_no();
					if(lotno_quantity.containsKey(key_lotno)){
						double temp_total_quantity = lotno_quantity.get(key_lotno);
						lotno_quantity.put(key_lotno, temp_total_quantity + temp.getQuantity());
						total_inmap.put(key, lotno_quantity);
					}else{
						lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
						total_inmap.put(key, lotno_quantity);
					}
				}else{
					HashMap<String, Double> lotno_quantity = new HashMap<String, Double>();
					lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
					total_inmap.put(key, lotno_quantity);
				}
			}
		}
		

		//根据 【色号】 和 【材料】 和 【工厂】 统计已出库 数量
		HashMap<String, Double> factory_outmap = new HashMap<String, Double>();
		for (StoreInOut storeOut : storeOutList) {
			if(storeOut.getFactoryId() != factoryId){
				continue;
			}
			for (StoreInOutDetail temp : storeOut.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(factory_outmap.containsKey(key)){
					double temp_total_quantity = factory_outmap.get(key);
					factory_outmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					factory_outmap.put(key, temp.getQuantity());
				}
			}
		}
		
		
		//根据 【色号】 和 【材料】 和 【缸号】 统计已出库 数量
		HashMap<String, HashMap<String, Double>> outmap = new HashMap<String, HashMap<String, Double>>();
		for (StoreInOut storeOut : storeOutList) {
			for (StoreInOutDetail temp : storeOut.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(outmap.containsKey(key)){
					HashMap<String, Double> lotno_quantity = outmap.get(key);
					String key_lotno = temp.getLot_no();
					if(lotno_quantity.containsKey(key_lotno)){
						double temp_total_quantity = lotno_quantity.get(key_lotno);
						lotno_quantity.put(key_lotno, temp_total_quantity + temp.getQuantity());
						outmap.put(key, lotno_quantity);
					}else{
						lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
						outmap.put(key, lotno_quantity);
					}
				}else{
					HashMap<String, Double> lotno_quantity = new HashMap<String, Double>();
					lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
					outmap.put(key, lotno_quantity);
				}
			}
		}
		
		
		//获取工厂未出库列表
		
		for(String key : factory_totalmap.keySet()){
			//存色号、材料 、总数量
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			double total_quantity = factory_totalmap.get(key);
			HashMap<String, Object> material_color_totalquantity_Map = new HashMap<String, Object>();
			material_color_totalquantity_Map.put("material", material);
			material_color_totalquantity_Map.put("color", color);
			material_color_totalquantity_Map.put("total_quantity", total_quantity);
			
			//存已出库数量、未出库数量
			double out_quantity = 0 ;
			if(factory_outmap.containsKey(key)){
				out_quantity = factory_outmap.get(key);
			}
			double not_out_quantity = total_quantity - out_quantity;
			
			material_color_totalquantity_Map.put("out_quantity", out_quantity);
			material_color_totalquantity_Map.put("not_out_quantity", not_out_quantity);
			factory_not_outlist.add(material_color_totalquantity_Map);
		}
		
		//获取 【色号】、【材料】、【缸号】未出库列表
		
		for(String key : total_inmap.keySet()){
			//存色号、材料 、总数量
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			
			HashMap<String, Double> lot_outquantityMap = total_inmap.get(key);
			for(String lot_no : lot_outquantityMap.keySet()){
				Double total_quantity = lot_outquantityMap.get(lot_no);//获取已入库数量
				double out_quantity = 0 ;
				if(outmap.containsKey(key)){
					if(outmap.get(key).containsKey(lot_no)){
						out_quantity = outmap.get(key).get(lot_no);
					}
				}
				double not_out_quantity = total_quantity - out_quantity;
				
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("material", material);
				itemMap.put("color", color);
				itemMap.put("lot_no", lot_no);
				itemMap.put("total_quantity", total_quantity);
				itemMap.put("out_quantity", out_quantity);
				itemMap.put("not_out_quantity", not_out_quantity);
				
				lot_not_outlist.add(itemMap);
			}
		}
		
	}
}
