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
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.ordergrid.StoreOrderDetail;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.entity.producesystem.StoreInOutDetail;
import com.fuwei.entity.producesystem.StoreReturn;
import com.fuwei.entity.producesystem.StoreReturnDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.service.producesystem.StoreInOutService;
import com.fuwei.service.producesystem.StoreReturnService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/store_in")
@Controller
/* 原材料入库单 */
public class StoreInController extends BaseController {
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
	StoreReturnService storeReturnService;
	@Autowired
	ColoringOrderService coloringOrderService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

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
				companyId, factoryId, charge_employee, number, true, sortList);
		if (pager != null & pager.getResult() != null) {
			List<StoreInOut> orderlist = (List<StoreInOut>) pager.getResult();
		}
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
		return new ModelAndView("store_in_out/in_index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少原材料入库单ID");
		}
		String lcode = "store_in_out/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料入库单详情的权限",
					null);
		}
		StoreInOut storeInOut = storeInOutService.get(id, true);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的原材料入库单");
		}
		request.setAttribute("storeInOut", storeInOut);
		if(storeInOut.getStore_order_id()!=null){
			return new ModelAndView("store_in_out/in_detail");	
		}else{
			return new ModelAndView("store_in_out/in_detail_coloring");	
		}
		
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String orderNumber, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StoreOrder storeOrder = storeOrderService.getByOrderNumber(orderNumber);
		if(storeOrder == null){
			throw new Exception("找不到订单号为" + orderNumber + "的原材料仓库单");
		}
		 return addbyorder(storeOrder.getId(), session, request, response);
	}

	
	@RequestMapping(value = "/{storeOrderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer storeOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (storeOrderId == null) {
			throw new Exception("原材料仓库单ID不能为空");
		}

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料入库单的权限",
					null);
		}
		try {
			StoreOrder storeOrder = storeOrderService.get(storeOrderId);
			if (storeOrder == null) {
				throw new Exception(
						"该原材料仓库单不存在或已被删除", null);
			}
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new Exception(
						"原材料仓库单缺少材料列表，请先修改原材料仓库的材料列表 ", null);
			}
			
			
			
			//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
			//获取已开的入库单
			List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
			List<StoreReturn> storeReturnList = storeReturnService.getByStoreOrder(storeOrderId);
			List<Map<String,Object>> detaillist = getInStoreQuantity(storeOrder,storeInList,storeReturnList);
			
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			boolean flag = true;
			for(Map<String,Object> tMap : detaillist){
				if((Double)tMap.get("not_in_quantity") > 0){				
					flag = false;
				}
			}
			if(flag){
				request.setAttribute("message", "未入库数量为0，原材料已全部入库 ，无需再创建入库单， 请确认材料是否超出！！！");
			}
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			
			
			request.setAttribute("detaillist", detaillist);

			request.setAttribute("storeOrder", storeOrder);
			return new ModelAndView("store_in_out/add_in");
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
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料入库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer storeOrderId = storeInOut.getStore_order_id();
			if (storeOrderId == null) {
				throw new Exception("原材料仓库单ID不能为空");
			}
			if (storeInOut.getFactoryId() == null
					|| storeInOut.getFactoryId() == 0) {
				throw new Exception("必须指定送货单位", null);
			}
			// if (storeInOut.getEmployee_id() == null
			// || storeInOut.getEmployee_id() == 0) {
			// throw new Exception("经办人不能为空", null);
			// }
			if (storeInOut.getDate() == null) {
				throw new Exception("入库日期不能为空", null);
			}
			StoreOrder storeOrder = storeOrderService.get(storeOrderId);
			if (storeOrder == null) {
				throw new Exception(
						"该原材料仓库单不存在或已被删除", null);
			}
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new Exception(
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
				    detail.setLot_no(detail.getLot_no().trim());
				    if(detail.getQuantity()!=0 && detail.getLot_no().equals("")){//填了数量，但没填缸号则无效
				    	throw new Exception("本次入库 数量 不为 0 时，缸号也不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建入库单，请至少入库一种材料");
				}
				//判断是否有数量为0的明细项
				
				//设置入库明细id
				for(int count = 0 ; count < detaillist.size() ; ++count){
					StoreInOutDetail detail = detaillist.get(count);
					detail.setId(count+1);
				}
				storeInOut.setDetaillist(detaillist);

				storeInOut.setIn_out(true);
				
				Map<String ,Object> data = new HashMap<String, Object>();
				
				/*判断入库量总和是否大于原材料仓库总量*/
				List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
				storeInList.add(storeInOut);
				List<StoreReturn> storeReturnList = storeReturnService.getByStoreOrder(storeOrderId);
				List<Map<String,Object>> not_in_quantityList = getInStoreQuantity(storeOrder,storeInList,storeReturnList);
				for(Map<String,Object> item:not_in_quantityList){
					double not_in_quantity = (Double)item.get("not_in_quantity");
					if(not_in_quantity<0){//入库总数大于原材料仓库单的数量
						data.put("message", "入库总数大于原材料仓库单的数量");
					}
				}
				/*判断入库量总和是否大于原材料仓库总量*/
				
				
				
				int tableOrderId = storeInOutService.add(storeInOut);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			} else {// 编辑
				Map<String ,Object> data = new HashMap<String, Object>();
				
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
				    detail.setLot_no(detail.getLot_no().trim());
				    if(detail.getQuantity()!=0 && detail.getLot_no().equals("")){//填了数量，但没填缸号则无效
				    	throw new Exception("本次入库 数量 不为 0 时，缸号也不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建入库单，请至少入库一种材料");
				}
				//判断是否有数量为0的明细项
				
				//设置入库明细id
				int maxId = 0 ; 
				for(StoreInOutDetail detail : detaillist){
					if(detail.getId() == null || detail.getId() == 0){
						continue;
					}else if(detail.getId() > maxId){
						maxId = detail.getId();
					}
				}
				for(int i = 0,count = 0 ; i < detaillist.size() ; ++i){
					StoreInOutDetail detail = detaillist.get(count);
					if(detail.getId() == null || detail.getId() == 0){
						detail.setId(count+1 + maxId);
						++count;
					}
					
				}
				
				storeInOut.setDetaillist(detaillist);
				storeInOut.setIn_out(true);
				
				/*判断入库量总和是否大于原材料仓库总量*/
				List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
				for(int i = 0 ; i < storeInList.size() ; ++i){
					StoreInOut item= storeInList.get(i);
					if(item.getId() == storeInOut.getId()){
						storeInList.set(i, storeInOut);
					}
				}
				List<StoreReturn> storeReturnList = storeReturnService.getByStoreOrder(storeOrderId);
				List<Map<String,Object>> not_in_quantityList = getInStoreQuantity(storeOrder,storeInList,storeReturnList);
				for(Map<String,Object> item:not_in_quantityList){
					double not_in_quantity = (Double)item.get("not_in_quantity");
					if(not_in_quantity<0){//入库总数大于原材料仓库单的数量
//						throw new Exception("入库总数大于原材料仓库单的数量");
						data.put("message", "入库总数大于原材料仓库单的数量");
					}
				}
				/*判断入库量总和是否大于原材料仓库总量*/
				
//				/*判断入库数量和缸号 修改过后， 是否 大于出库单缸号出库数量之和    ， 若是，则报异常，编辑出错*/
//				List<StoreInOut> storeOutList = storeInOutService.getByStoreOrder(storeOrderId, false);
//				//根据 【色号】 和 【材料】 和 【缸号】 统计已出库 数量
//				HashMap<String, HashMap<String, Double>> outmap = new HashMap<String, HashMap<String, Double>>();
//				for (StoreInOut storeOut : storeOutList) {
//					for (StoreInOutDetail temp : storeOut.getDetaillist()) {
//						String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
//						if(outmap.containsKey(key)){
//							HashMap<String, Double> lotno_quantity = outmap.get(key);
//							String key_lotno = temp.getLot_no();
//							if(lotno_quantity.containsKey(key_lotno)){
//								double temp_total_quantity = lotno_quantity.get(key_lotno);
//								lotno_quantity.put(key_lotno, temp_total_quantity + temp.getQuantity());
//								outmap.put(key, lotno_quantity);
//							}else{
//								lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
//								outmap.put(key, lotno_quantity);
//							}
//						}else{
//							HashMap<String, Double> lotno_quantity = new HashMap<String, Double>();
//							lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
//							outmap.put(key, lotno_quantity);
//						}
//					}
//				}
//				//根据  【色号】 和 【材料】 和 【缸号】  统计入库总数量 , key = material + : + color + : + lot_no
//				HashMap<String, HashMap<String, Double>> total_inmap = new HashMap<String, HashMap<String, Double>>();
//				for (StoreInOut storeIn : storeInList) {
//					for (StoreInOutDetail temp : storeIn.getDetaillist()) {
//						String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
//						if(total_inmap.containsKey(key)){
//							HashMap<String, Double> lotno_quantity = total_inmap.get(key);
//							String key_lotno = temp.getLot_no();
//							if(lotno_quantity.containsKey(key_lotno)){
//								double temp_total_quantity = lotno_quantity.get(key_lotno);
//								lotno_quantity.put(key_lotno, temp_total_quantity + temp.getQuantity());
//								total_inmap.put(key, lotno_quantity);
//							}else{
//								lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
//								total_inmap.put(key, lotno_quantity);
//							}
//						}else{
//							HashMap<String, Double> lotno_quantity = new HashMap<String, Double>();
//							lotno_quantity.put(temp.getLot_no(), temp.getQuantity());
//							total_inmap.put(key, lotno_quantity);
//						}
//					}
//				}
//				
//				//获取 【色号】、【材料】、【缸号】未出库列表
//				for(String key : outmap.keySet()){
//					HashMap<String, Double> lot_outquantityMap = outmap.get(key);
//					for(String lot_no : lot_outquantityMap.keySet()){
//						Double out_quantity = lot_outquantityMap.get(lot_no);//获取已入库数量
//						double in_quantity = 0 ;
//						if(total_inmap.containsKey(key)){
//							if(total_inmap.get(key).containsKey(lot_no)){
//								in_quantity = total_inmap.get(key).get(lot_no);
//							}
//						}
//						double out_minus_in_quantity = out_quantity - in_quantity;
//						
//						if(out_minus_in_quantity > 0){
//							throw new Exception("色号、材料、缸号入库数量总和小于已出库数量，无法修改");
//						}
//					}
//				}
//				/*判断入库数量和缸号 修改过后， 是否 大于出库单缸号出库数量之和    ， 若是，则报异常，编辑出错*/
//				
				
				int tableOrderId = storeInOutService.update(storeInOut);
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
		String lcode = "store_in_out/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑原材料入库单的权限", null);
		}
		try {
			if (id != null) {
				StoreInOut storeInOut = storeInOutService.get(id, true);
				if(storeInOut == null){
					throw new Exception("找不到ID为" + id + "的原材料入库单");
				}
				request.setAttribute("storeInOut", storeInOut);
				//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
				//获取已开的入库单
				
				List<StoreInOut> storeInList = null;
				List<StoreReturn> storeReturnList = null;
				List<Map<String,Object>> detaillist = null;
				if(storeInOut.getStore_order_id()!=null){//若是大货纱入库
					int storeOrderId = storeInOut.getStore_order_id();
					StoreOrder storeOrder = storeOrderService.get(storeOrderId);
					request.setAttribute("storeOrder", storeOrder);
					storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
					storeReturnList = storeReturnService.getByStoreOrder(storeOrderId);
					detaillist = getInStoreQuantity(storeOrder,storeInList,storeReturnList);
				}
				else{
					int coloringOrderId = storeInOut.getColoring_order_id();
					ColoringOrder coloringOrder = coloringOrderService.get(coloringOrderId);
					request.setAttribute("coloringOrder", coloringOrder);
					storeInList = storeInOutService.getByColoringOrder(coloringOrderId,true);
//					storeReturnList = storeReturnService.getByColoringOrder(coloringOrderId);
					storeReturnList = new ArrayList<StoreReturn>();//2016-2-23暂时没有样纱退货的功能
					detaillist = getInStoreQuantity(coloringOrder,storeInList,storeReturnList);
				}
				
				
				List<Map<String,Object>> detaillist_result = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> not_outMap : detaillist){
					not_outMap.put("quantity",0.0);
					not_outMap.put("lot_no","");
					not_outMap.put("packages","1");
					for(StoreInOutDetail detail : storeInOut.getDetaillist()){
						if(detail.getColor().trim().equals(not_outMap.get("color")) && detail.getMaterial() == (Integer)not_outMap.get("material")){
							not_outMap.put("quantity",detail.getQuantity());
							not_outMap.put("lot_no", detail.getLot_no());
							not_outMap.put("packages", detail.getPackages());
							not_outMap.put("id", detail.getId());
						}
						
					}
					
					detaillist_result.add(not_outMap);
				}
				
				request.setAttribute("detaillist", detaillist_result);

				if(storeInOut.getStore_order_id()!=null){//若是大货纱入库
					return new ModelAndView("store_in_out/edit_in");
				}else{
					return new ModelAndView("store_in_out/edit_in_coloring");
				}
				
			}
			throw new Exception("缺少原材料入库单ID");

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
			throw new PermissionDeniedDataAccessException("没有删除原材料入库单的权限", null);
		}
		StoreInOut storeIn = storeInOutService.get(id);
		int success = storeInOutService.remove(id);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("store_in_out/in_scan");
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
			throw new Exception("缺少原材料入库单ID");
		}
		String lcode = "store_in_out/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印原材料入库单的权限", null);
		}
		StoreInOut storeInOut = storeInOutService.get(id, true);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的原材料入库单");
		}
		storeInOut.setHas_print(true);
		storeInOutService.updatePrint(storeInOut);
		request.setAttribute("storeInOut", storeInOut);
		return new ModelAndView("store_in_out/in_print");
	}
	
	/*打印纱线标签*/
	@RequestMapping(value = "/print/{id}/tag", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_tag(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少原材料入库单ID");
		}
		StoreInOut storeInOut = storeInOutService.get(id, true);
		if (storeInOut == null) {
			throw new Exception("找不到ID为" + id + "的原材料入库单");
		}
		storeInOut.setHas_tagprint(true);
		storeInOutService.updateTagPrint(storeInOut);
		request.setAttribute("storeInOut", storeInOut);
		return new ModelAndView("store_in_out/in_tag_print");
	}
	
	
	//实际入库数量
	@RequestMapping(value = "/actual_in/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView actual_in(@PathVariable Integer orderId, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (orderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "order/progress";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看订单原材料生产进度的权限",
					null);
		}
		StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
		if(storeOrder == null){
			throw new Exception("找不到订单ID为" + orderId + "的原材料仓库单");
		}
		int storeOrderId = storeOrder.getId();
		
		/*1.获取工序 以及 工序对应工厂 Map*/
		//Map<材料：色号，Map<工厂,入库进度数据Map>>
		Map<String,Map<String,Object>> color_materialTotalMap = new HashMap<String,Map<String,Object>>();
		//Map<材料：色号，Map<工厂,实际入库数量>>
		List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
		List<StoreReturn> storeReturnList = storeReturnService.getByStoreOrder(storeOrderId);
		Map<String,Map<Integer,Double>> actualInMap =  getFactoryActualInMap(storeOrder,storeInList,storeReturnList);	
		//根据  【色号】 和 【材料】  统计总数量 , key = material + : + color
		HashMap<String, Double> planmap = new HashMap<String, Double>();
		for (StoreOrderDetail storeOrderDetail : storeOrder.getDetaillist()) {
			String key = storeOrderDetail.getMaterial() + ":"+ storeOrderDetail.getColor().trim(); 
			if(planmap.containsKey(key)){
				double temp_total_quantity = planmap.get(key);
				planmap.put(key, temp_total_quantity + storeOrderDetail.getQuantity());
			}else{
				planmap.put(key, storeOrderDetail.getQuantity());
			}
		}
		
		for(String key : planmap.keySet()){
			double total_plan_quantity = planmap.get(key);
			double total_actual_in_quantity = 0;
			if(actualInMap.containsKey(key)){
				Map<Integer,Double> tempMap = actualInMap.get(key);
				for(Integer factoryId : tempMap.keySet()){
					total_actual_in_quantity = total_actual_in_quantity + tempMap.get(factoryId);
				}
			}
			Map<String,Object> detailMap = new HashMap<String, Object>();
			detailMap.put("total_plan_quantity", total_plan_quantity);
			detailMap.put("total_actual_in_quantity", total_actual_in_quantity);
			color_materialTotalMap.put(key, detailMap);
		}
		
		request.setAttribute("storeOrder", storeOrder);
		request.setAttribute("color_materialTotalMap", color_materialTotalMap);
		request.setAttribute("actualInMap", actualInMap);
		return new ModelAndView("store_in_out/actual_in");	
	}
	
	
	//各个订单的原材料生产进度
	@RequestMapping(value = "/order_progress", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView order_progress(Integer page,String orderNumber,HttpSession session,HttpServletRequest request) throws Exception {
		String lcode = "order/progress";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看订单原材料生产进度的权限",
					null);
		}
		//Map<orderId,List<Map<String,Object>>(包括工序id，计划数量、实际数量等)>
		Map<Integer,List<Map<String,Object>>> resultMap = new HashMap<Integer, List<Map<String,Object>>>();
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager = orderService.getUnDeliveryList(pager,orderNumber);
		List<Order> orderlist = new ArrayList<Order>();
		if(pager.getResult()!=null){
			orderlist = (List<Order>)pager.getResult();
		}
		for(Order order : orderlist){
			int orderId = order.getId();
			StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
			if(storeOrder == null){
				resultMap.put(orderId, new ArrayList<Map<String,Object>>());
				continue;
			}
			int storeOrderId = storeOrder.getId();
			List<StoreInOut> storeInList = storeInOutService.getByStoreOrder(storeOrderId,true);
			List<StoreReturn> storeReturnList = storeReturnService.getByStoreOrder(storeOrderId);
			
			List<Map<String,Object>> detaillist = getInStoreQuantity(storeOrder,storeInList,storeReturnList);
	
			resultMap.put(orderId, detaillist);
		}
		
		request.setAttribute("resultMap", resultMap);
		request.setAttribute("pager", pager);
		request.setAttribute("orderlist",orderlist);
		request.setAttribute("orderNumber", orderNumber);
		return new ModelAndView("store_in_out/order_progress");	
	}
	public List<Map<String,Object>> getInStoreQuantity(StoreOrder storeOrder , List<StoreInOut> storeInList,List<StoreReturn> storeReturnList) throws Exception{	
		//根据  【色号】 和 【材料】  统计总数量 , key = material + : + color
		HashMap<String, Double> totalmap = new HashMap<String, Double>();
		for (StoreOrderDetail storeOrderDetail : storeOrder.getDetaillist()) {
			String key = storeOrderDetail.getMaterial() + ":"+ storeOrderDetail.getColor().trim(); 
			if(totalmap.containsKey(key)){
				double temp_total_quantity = totalmap.get(key);
				totalmap.put(key, temp_total_quantity + storeOrderDetail.getQuantity());
			}else{
				totalmap.put(key, storeOrderDetail.getQuantity());
			}
		}
		
		
		
		//根据 【色号】 和 【材料】获取已入库 数量
		HashMap<String, Double> inmap = new HashMap<String, Double>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(inmap.containsKey(key)){
					double temp_total_quantity = inmap.get(key);
					inmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					inmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//根据 【色号】 和 【材料】获取退货 数量
		HashMap<String, Double> returnmap = new HashMap<String, Double>();
		for (StoreReturn storeReturn : storeReturnList) {
			for (StoreReturnDetail temp : storeReturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(returnmap.containsKey(key)){
					double temp_total_quantity = returnmap.get(key);
					returnmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					returnmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		for(String key : totalmap.keySet()){
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			double total_quantity = totalmap.get(key);
			double in_quantity = 0 ;
			if(inmap.containsKey(key)){
				in_quantity = inmap.get(key);
			}
			double return_quantity = 0 ;
			if(returnmap.containsKey(key)){
				return_quantity = returnmap.get(key);
			}
			double actual_in_quantity = in_quantity-return_quantity;
			double not_in_quantity = total_quantity - actual_in_quantity;
			HashMap<String, Object> tempHash = new HashMap<String, Object>();
			tempHash.put("material", material);
			tempHash.put("color", color);
			tempHash.put("total_quantity", total_quantity);
			tempHash.put("in_quantity", in_quantity);
			tempHash.put("actual_in_quantity", actual_in_quantity);
			tempHash.put("not_in_quantity", not_in_quantity);
			resultlist.add(tempHash);
		}
		return resultlist;
	}
	
	public List<Map<String,Object>> getInStoreQuantity(ColoringOrder coloringOrder , List<StoreInOut> storeInList,List<StoreReturn> storeReturnList) throws Exception{	
		//根据  【色号】 和 【材料】  统计总数量 , key = material + : + color
		HashMap<String, Double> totalmap = new HashMap<String, Double>();
		for (ColoringOrderDetail detail : coloringOrder.getDetaillist()) {
			String key = detail.getMaterial() + ":"+ detail.getColor().trim(); 
			if(totalmap.containsKey(key)){
				double temp_total_quantity = totalmap.get(key);
				totalmap.put(key, temp_total_quantity + detail.getQuantity());
			}else{
				totalmap.put(key, detail.getQuantity());
			}
		}
		
		
		
		//根据 【色号】 和 【材料】获取已入库 数量
		HashMap<String, Double> inmap = new HashMap<String, Double>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(inmap.containsKey(key)){
					double temp_total_quantity = inmap.get(key);
					inmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					inmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//根据 【色号】 和 【材料】获取退货 数量
		HashMap<String, Double> returnmap = new HashMap<String, Double>();
		for (StoreReturn storeReturn : storeReturnList) {
			for (StoreReturnDetail temp : storeReturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(returnmap.containsKey(key)){
					double temp_total_quantity = returnmap.get(key);
					returnmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					returnmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		for(String key : totalmap.keySet()){
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			double total_quantity = totalmap.get(key);
			double in_quantity = 0 ;
			if(inmap.containsKey(key)){
				in_quantity = inmap.get(key);
			}
			double return_quantity = 0 ;
			if(returnmap.containsKey(key)){
				return_quantity = returnmap.get(key);
			}
			double actual_in_quantity = in_quantity-return_quantity;
			double not_in_quantity = total_quantity - actual_in_quantity;
			HashMap<String, Object> tempHash = new HashMap<String, Object>();
			tempHash.put("material", material);
			tempHash.put("color", color);
			tempHash.put("total_quantity", total_quantity);
			tempHash.put("in_quantity", in_quantity);
			tempHash.put("actual_in_quantity", actual_in_quantity);
			tempHash.put("not_in_quantity", not_in_quantity);
			resultlist.add(tempHash);
		}
		return resultlist;
	}
	
	public List<Map<String,Object>> getActualInStoreQuantity(List<StoreInOut> storeInList,List<StoreReturn> storeReturnList) throws Exception{	
		//根据 【色号】 和 【材料】获取已入库 数量
		HashMap<String, Double> inmap = new HashMap<String, Double>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(inmap.containsKey(key)){
					double temp_total_quantity = inmap.get(key);
					inmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					inmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//根据 【色号】 和 【材料】获取退货 数量
		HashMap<String, Double> returnmap = new HashMap<String, Double>();
		for (StoreReturn storeReturn : storeReturnList) {
			for (StoreReturnDetail temp : storeReturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(returnmap.containsKey(key)){
					double temp_total_quantity = returnmap.get(key);
					returnmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					returnmap.put(key, temp.getQuantity());
				}
			}
		}
		
		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
		if(returnmap.size()>inmap.size()){
			for(String key : returnmap.keySet()){
				int indexOf = key.indexOf(":");
				if(indexOf <= -1){
					throw new Exception("分割色号与材料出错");
				}
				Integer material = Integer.parseInt(key.substring(0,indexOf));
				String color = key.substring(indexOf+1);
				double return_quantity = returnmap.get(key);
				double in_quantity = 0 ;
				if(inmap.containsKey(key)){
					in_quantity = inmap.get(key);
				}
				double actual_in_quantity = in_quantity-return_quantity;
				HashMap<String, Object> tempHash = new HashMap<String, Object>();
				tempHash.put("material", material);
				tempHash.put("color", color);
				tempHash.put("in_quantity", in_quantity);
				tempHash.put("actual_in_quantity", actual_in_quantity);
				resultlist.add(tempHash);
			}
		}else{
			for(String key : inmap.keySet()){
				int indexOf = key.indexOf(":");
				if(indexOf <= -1){
					throw new Exception("分割色号与材料出错");
				}
				Integer material = Integer.parseInt(key.substring(0,indexOf));
				String color = key.substring(indexOf+1);
				double in_quantity = inmap.get(key);
				double return_quantity = 0 ;
				if(returnmap.containsKey(key)){
					return_quantity = returnmap.get(key);
				}
				double actual_in_quantity = in_quantity-return_quantity;
				HashMap<String, Object> tempHash = new HashMap<String, Object>();
				tempHash.put("material", material);
				tempHash.put("color", color);
				tempHash.put("in_quantity", in_quantity);
				tempHash.put("actual_in_quantity", actual_in_quantity);
				resultlist.add(tempHash);
			}
		}
		
		return resultlist;
	}

	public Map<String, Map<Integer,Double>> getFactoryActualInMap(StoreOrder storeOrder , List<StoreInOut> storeInList,List<StoreReturn> storeReturnList) throws Exception{	
		//根据  【色号】 和 【材料】  统计总数量 , key = material + : + color
		HashMap<String, Double> totalmap = new HashMap<String, Double>();
		for (StoreOrderDetail storeOrderDetail : storeOrder.getDetaillist()) {
			String key = storeOrderDetail.getMaterial() + ":"+ storeOrderDetail.getColor().trim(); 
			if(totalmap.containsKey(key)){
				double temp_total_quantity = totalmap.get(key);
				totalmap.put(key, temp_total_quantity + storeOrderDetail.getQuantity());
			}else{
				totalmap.put(key, storeOrderDetail.getQuantity());
			}
		}
		
		
		
		//根据 【色号】 、【材料】、【工厂】获取已入库 数量
		HashMap<String, Map<Integer,Double>> inmap = new HashMap<String, Map<Integer,Double>>();
		for (StoreInOut storeIn : storeInList) {
			int factoryId = storeIn.getFactoryId();
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(inmap.containsKey(key)){
					HashMap<Integer,Double> factoryMap = (HashMap<Integer,Double>)inmap.get(key);
					if(factoryMap.containsKey(factoryId)){
						double temp_total_quantity = factoryMap.get(factoryId);
						factoryMap.put(factoryId, temp_total_quantity + temp.getQuantity());
						inmap.put(key, factoryMap);
					}else{
						factoryMap.put(factoryId, temp.getQuantity());
						inmap.put(key, factoryMap);
					}
				}else{
					HashMap<Integer,Double> tempM = new HashMap<Integer, Double>();
					tempM.put(factoryId, temp.getQuantity());
					inmap.put(key, tempM);
				}
			}
		}
		
		//根据 【色号】 、【材料】、【工厂】获取退货 数量
		HashMap<String, Map<Integer,Double>> returnmap = new HashMap<String, Map<Integer,Double>>();
		for (StoreReturn storeReturn : storeReturnList) {
			int factoryId = storeReturn.getFactoryId();
			for (StoreReturnDetail temp : storeReturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(returnmap.containsKey(key)){
					HashMap<Integer,Double> factoryMap = (HashMap<Integer,Double>)returnmap.get(key);
					if(factoryMap.containsKey(factoryId)){
						double temp_total_quantity = factoryMap.get(factoryId);
						factoryMap.put(factoryId, temp_total_quantity + temp.getQuantity());
						returnmap.put(key, factoryMap);
					}else{
						factoryMap.put(factoryId, temp.getQuantity());
						returnmap.put(key, factoryMap);
					}
				}else{
					HashMap<Integer,Double> tempM = new HashMap<Integer, Double>();
					tempM.put(factoryId, temp.getQuantity());
					returnmap.put(key, tempM);
				}
			}
		}
		
		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
		Map<String,Map<Integer,Double>> actualInMap = new HashMap<String, Map<Integer,Double>>();
		for(String key : totalmap.keySet()){
			Map<Integer,Double> actualInDetailMap = new HashMap<Integer, Double>();
			if(actualInMap.containsKey(key)){
				actualInDetailMap = actualInMap.get(key);
			}
			
			Map<Integer,Double> factoryInM = inmap.get(key);
			if(factoryInM != null){
				for(Integer factoryId : factoryInM.keySet()){
					double in_quantity = factoryInM.get(factoryId);
					if(actualInDetailMap.containsKey(factoryId)){
						double value = actualInDetailMap.get(factoryId);
						actualInDetailMap.put(factoryId, in_quantity + value);
					}else{
						actualInDetailMap.put(factoryId, in_quantity);
					}
				}
			}
			Map<Integer,Double> factoryReturnM = returnmap.get(key);
			if(factoryReturnM != null){
				for(Integer factoryId : factoryReturnM.keySet()){
					double return_quantity = factoryReturnM.get(factoryId);
					if(actualInDetailMap.containsKey(factoryId)){
						double value = actualInDetailMap.get(factoryId);
						actualInDetailMap.put(factoryId, value-return_quantity);
					}else{
						actualInDetailMap.put(factoryId, -return_quantity);
					}
				}
			}
			actualInMap.put(key, actualInDetailMap);
		}
		return actualInMap;
	}

	
	// 样纱入库
	@RequestMapping(value = "/add_coloring", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add_coloring(String coloringOrderNumber, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(coloringOrderNumber == null || coloringOrderNumber.equals("")){
			throw new Exception("染色单号不能为空");
		}
		ColoringOrder coloringOrder = coloringOrderService.getByNumber(coloringOrderNumber);
		if(coloringOrder == null){
			throw new Exception("找不到单号为" + coloringOrder + "的染色单");
		}
		if(coloringOrder.getOrderId() != null){
			throw new Exception("大货纱线请扫描原材料仓库单入库");
		}
		 return addbyorder_coloring(coloringOrder.getId(), session, request, response);
	}

	//样纱入库
	@RequestMapping(value = "/{coloringOrderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder_coloring(@PathVariable Integer coloringOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (coloringOrderId == null) {
			throw new Exception("染色单ID不能为空");
		}

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料入库单的权限",
					null);
		}
		try {
			ColoringOrder coloringOrder = coloringOrderService.get(coloringOrderId);
			if(coloringOrder == null){
				throw new Exception("找不到单号为" + coloringOrder + "的染色单");
			}
			if (coloringOrder.getDetaillist() == null
					|| coloringOrder.getDetaillist().size() <= 0) {
				throw new Exception(
						"染色单缺少材料列表，请先修改染色单的材料列表 ", null);
			}
			
			
			
			
			//获取已开的入库单
			List<StoreInOut> storeInList = storeInOutService.getByColoringOrder(coloringOrderId,true);
//			List<StoreReturn> storeReturnList = storeReturnService.getByColoringOrder(coloringOrderId);
			List<StoreReturn> storeReturnList = new ArrayList<StoreReturn>();//2016-2-23暂时没有样纱退货的功能
			//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
			List<Map<String,Object>> detaillist = getInStoreQuantity(coloringOrder,storeInList,storeReturnList);
			
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			boolean flag = true;
			for(Map<String,Object> tMap : detaillist){
				if((Double)tMap.get("not_in_quantity") > 0){				
					flag = false;
				}
			}
			if(flag){
				request.setAttribute("message", "未入库数量为0，原材料已全部入库 ，无需再创建入库单， 请确认材料是否超出！！！");
			}
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			
			
			request.setAttribute("detaillist", detaillist);

			request.setAttribute("coloringOrder", coloringOrder);
			return new ModelAndView("store_in_out/add_in_coloring");
		} catch (Exception e) {
			throw e;
		}
	}
	// 样纱入库 添加或保存
	@RequestMapping(value = "/add_coloring", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder_coloring(StoreInOut storeInOut,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "store_in_out/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑原材料入库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer coloring_order_id = storeInOut.getColoring_order_id();
			if (coloring_order_id == null) {
				throw new Exception("染色单ID不能为空");
			}
			if (storeInOut.getDate() == null) {
				throw new Exception("入库日期不能为空", null);
			}
			ColoringOrder coloringOrder = coloringOrderService.get(coloring_order_id);
			if (coloringOrder == null) {
				throw new Exception(
						"该染色单不存在或已被删除", null);
			}
			if(coloringOrder.getOrderId() != null){
				throw new Exception("大货纱线请扫描原材料仓库单入库");
			}
			if (coloringOrder.getDetaillist() == null
					|| coloringOrder.getDetaillist().size() <= 0) {
				throw new Exception(
						"染色单缺少材料列表，请先修改染色单的材料列表 ", null);
			}
			/* 判断有些数据不能为空 */
			

			if (storeInOut.getId() == null) {// 添加原材料入库单

				storeInOut.setCreated_at(DateTool.now());// 设置创建时间
				storeInOut.setUpdated_at(DateTool.now());// 设置更新时间
				storeInOut.setCreated_user(user.getId());// 设置创建人

				// 以下是coloringOrder的属性
				storeInOut.setColoring_order_number(coloringOrder.getNumber());
				storeInOut.setName(coloringOrder.getName());
				storeInOut.setCompanyId(coloringOrder.getCompanyId());
				storeInOut.setCustomerId(coloringOrder.getCustomerId());
				storeInOut.setCharge_employee(coloringOrder.getCharge_employee());
				storeInOut.setCompany_productNumber(coloringOrder.getCompany_productNumber());
				storeInOut.setFactoryId(coloringOrder.getFactoryId());
				
				List<StoreInOutDetail> detaillist = SerializeTool
						.deserializeList(details, StoreInOutDetail.class);
				
				//判断是否有数量为0的明细项
				Iterator<StoreInOutDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					StoreInOutDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				    detail.setLot_no(detail.getLot_no().trim());
				    if(detail.getQuantity()!=0 && detail.getLot_no().equals("")){//填了数量，但没填缸号则无效
				    	throw new Exception("本次入库 数量 不为 0 时，缸号也不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建入库单，请至少入库一种材料");
				}
				//判断是否有数量为0的明细项
				
				//设置入库明细id
				for(int count = 0 ; count < detaillist.size() ; ++count){
					StoreInOutDetail detail = detaillist.get(count);
					detail.setId(count+1);
				}
				storeInOut.setDetaillist(detaillist);

				storeInOut.setIn_out(true);
				
				Map<String ,Object> data = new HashMap<String, Object>();
				
				/*判断入库量总和是否大于样纱染色总量*/
				List<StoreInOut> storeInList = storeInOutService.getByColoringOrder(coloring_order_id,true);
				storeInList.add(storeInOut);
//				List<StoreReturn> storeReturnList = storeReturnService.getByColoringOrder(coloring_order_id);
				List<StoreReturn> storeReturnList = new ArrayList<StoreReturn>();//2016-2-23暂时没有样纱退货的功能
				List<Map<String,Object>> not_in_quantityList = getInStoreQuantity(coloringOrder,storeInList,storeReturnList);
				for(Map<String,Object> item:not_in_quantityList){
					double not_in_quantity = (Double)item.get("not_in_quantity");
					if(not_in_quantity<0){//入库总数大于原材料仓库单的数量
						data.put("message", "入库总数大于样纱染色单的数量");
					}
				}
				/*判断入库量总和是否大于样纱染色单总量*/
				
				
				
				int tableOrderId = storeInOutService.add(storeInOut);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			} else {// 编辑
				Map<String ,Object> data = new HashMap<String, Object>();
				
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
				    detail.setLot_no(detail.getLot_no().trim());
				    if(detail.getQuantity()!=0 && detail.getLot_no().equals("")){//填了数量，但没填缸号则无效
				    	throw new Exception("本次入库 数量 不为 0 时，缸号也不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建入库单，请至少入库一种材料");
				}
				//判断是否有数量为0的明细项
				
				//设置入库明细id
				int maxId = 0 ; 
				for(StoreInOutDetail detail : detaillist){
					if(detail.getId() == null || detail.getId() == 0){
						continue;
					}else if(detail.getId() > maxId){
						maxId = detail.getId();
					}
				}
				for(int i = 0,count = 0 ; i < detaillist.size() ; ++i){
					StoreInOutDetail detail = detaillist.get(count);
					if(detail.getId() == null || detail.getId() == 0){
						detail.setId(count+1 + maxId);
						++count;
					}
					
				}
				
				storeInOut.setDetaillist(detaillist);
				storeInOut.setIn_out(true);
				
				/*判断入库量总和是否大于样纱染色单总量*/
				List<StoreInOut> storeInList = storeInOutService.getByColoringOrder(coloring_order_id,true);
				for(int i = 0 ; i < storeInList.size() ; ++i){
					StoreInOut item= storeInList.get(i);
					if(item.getId() == storeInOut.getId()){
						storeInList.set(i, storeInOut);
					}
				}
//				List<StoreReturn> storeReturnList = storeReturnService.getByColoringOrder(coloring_order_id);
				List<StoreReturn> storeReturnList = new ArrayList<StoreReturn>();//2016-2-23暂时没有样纱退货的功能
				List<Map<String,Object>> not_in_quantityList = getInStoreQuantity(coloringOrder,storeInList,storeReturnList);
				for(Map<String,Object> item:not_in_quantityList){
					double not_in_quantity = (Double)item.get("not_in_quantity");
					if(not_in_quantity<0){//入库总数大于原材料仓库单的数量
						data.put("message", "入库总数大于染色单的数量");
					}
				}
				/*判断入库量总和是否大于样纱染色单总量*/
				
				int tableOrderId = storeInOutService.update(storeInOut);
				data.put("id", tableOrderId);
				return this.returnSuccess(data);
			}

		} catch (Exception e) {
			throw e;
		}

	}
}
