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
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.producesystem.FuliaoIn;
import com.fuwei.entity.producesystem.FuliaoInDetail;
import com.fuwei.entity.producesystem.FuliaoInNotice;
import com.fuwei.entity.producesystem.FuliaoInNoticeDetail;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.producesystem.FuliaoCurrentStockService;
import com.fuwei.service.producesystem.FuliaoInNoticeDetailService;
import com.fuwei.service.producesystem.FuliaoInNoticeService;
import com.fuwei.service.producesystem.FuliaoInService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/fuliaoin")
@Controller
/* 辅料入库单 */
public class FuliaoInController extends BaseController {
	@Autowired
	FuliaoInService fuliaoInService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	FuliaoCurrentStockService fuliaoCurrentStockService;
	@Autowired
	FuliaoInNoticeService fuliaoInNoticeService;
	@Autowired
	FuliaoInNoticeDetailService fuliaoInNoticeDetailService;

	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			String orderNumber, Integer charge_employee,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料入库列表的权限", null);
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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = fuliaoInService.getList(pager, start_time_d, end_time_d,
				orderNumber, charge_employee, number, sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("orderNumber", orderNumber);
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
		return new ModelAndView("fuliaoinout/in_index");
	}
	
	@RequestMapping(value = "/index_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index_common(Integer page, String start_time, String end_time,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliao_workspace/commonfuliao_workspace";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看通用辅料入库列表的权限", null);
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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = fuliaoInService.getList_common(pager, start_time_d, end_time_d,number, sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliaoinout/in_index_common");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料入库单ID");
		}
		String lcode = "fuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料入库单详情的权限",
					null);
		}
		FuliaoIn object = fuliaoInService.get(id);
		if (object == null) {
			throw new Exception("找不到ID为" + id + "的辅料入库单");
		}
		Order order = orderService.get(object.getOrderId());
		request.setAttribute("order", order);
		request.setAttribute("object", object);
		return new ModelAndView("fuliaoinout/in_detail");	
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String fuliaoNoticeNumber,HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 FuliaoInNotice notice =  fuliaoInNoticeService.get(fuliaoNoticeNumber);
		 if(notice == null){
			 throw new Exception("找不到单号为" + fuliaoNoticeNumber + "的辅料入库通知单");
		 }
		if(notice.getStatus() == 6){
			throw new Exception("该通知单已入库，无法再入库");
		}
		 return addbyorder(notice.getId(), session, request, response);
	}

	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer fuliaoNoticeId,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (fuliaoNoticeId == null) {
			throw new Exception("辅料入库通知单ID不能为空");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑辅料入库单的权限",
					null);
		}
		try {
			FuliaoInNotice notice = fuliaoInNoticeService.getAndDetail(fuliaoNoticeId);
			if (notice == null) {
				throw new Exception(
						"该辅料入库通知单不存在或已被删除", null);
			}
			List<FuliaoInNoticeDetail> detaillist = notice.getDetaillist();
			if (detaillist == null|| detaillist.size() <= 0) {
				throw new Exception(
						"入库通知单缺少入库列表，请先修改 ", null);
			}
			request.setAttribute("notice", notice);
			Order order = orderService.get(notice.getOrderId());
			request.setAttribute("order", order);
			//自动匹配库位locationId
			Map<Integer,List<Map<String,Object>>> locationMap = new HashMap<Integer, List<Map<String,Object>>>();
			for(FuliaoInNoticeDetail detail : detaillist){
				List<Map<String,Object>> tempMap = fuliaoInService.matchlocation(detail.getFuliaoId());
				locationMap.put(detail.getFuliaoId(), tempMap);
			}
			request.setAttribute("detaillist", detaillist);
			request.setAttribute("locationMap", locationMap);
			
			return new ModelAndView("fuliaoinout/in_add");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(FuliaoIn fuliaoIn,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑辅料入库单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer orderId = fuliaoIn.getOrderId();
			if (orderId == null) {
				throw new Exception("订单ID不能为空");
			}
			Integer fuliaoin_noticeId = fuliaoIn.getFuliaoin_noticeId();
			if (fuliaoin_noticeId == null|| fuliaoin_noticeId == 0) {
				throw new Exception("辅料入库通知单ID不能为空", null);
			}
			FuliaoInNotice notice = fuliaoInNoticeService.get(fuliaoin_noticeId);
			if(notice == null){
				throw new Exception("找不到ID为" + fuliaoin_noticeId + "的辅料入库通知单，不存在或已被删除");
			}
			if(notice.getStatus() == 6){
				throw new Exception("该通知单已入库，无法再入库");
			}
			/* 判断有些数据不能为空 */
			

			if (fuliaoIn.getId() == 0) {// 添加辅料入库单
				Map<String,Object> data = new HashMap<String, Object>();
				fuliaoIn.setCreated_at(DateTool.now());// 设置创建时间
				fuliaoIn.setCreated_user(user.getId());// 设置创建人
				fuliaoIn.setCharge_employee(notice.getCharge_employee());
				fuliaoIn.setOrderNumber(notice.getOrderNumber());
				fuliaoIn.setOrderId(notice.getOrderId());
				fuliaoIn.setName(notice.getName());
				fuliaoIn.setCompany_productNumber(notice.getCompany_productNumber());

				List<FuliaoInDetail> detaillist = SerializeTool
						.deserializeList(details, FuliaoInDetail.class);
				
				//判断是否有数量为0的明细项
				Iterator<FuliaoInDetail> iter = detaillist.iterator();  
				while(iter.hasNext()){  
					FuliaoInDetail detail = iter.next();  
				    if(detail.getQuantity() == 0){  
				        iter.remove();  
				    }  
				    if(detail.getLocationId() == 0){
				    	throw new Exception("库位不能为空");
				    }
				}  
				if(detaillist.size() <=0){
					throw new Exception("本次入库数量均为0，无法创建辅料入库单");
				}
				//判断是否有数量为0的明细项			
				fuliaoIn.setDetaillist(detaillist);			
				int fuliaoInId = fuliaoInService.add(fuliaoIn);
				data.put("id", fuliaoInId);
				return this.returnSuccess(data);
			} else{
				throw new Exception("id错误，创建时id只能为空");
			}

		} catch (Exception e) {
			throw e;
		}

	}


	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("fuliaoinout/in_scan");
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
			throw new Exception("缺少辅料入库单ID");
		}
		String lcode = "fuliaoinout/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印辅料入库单的权限", null);
		}
		FuliaoIn fuliaoIn = fuliaoInService.get(id);
		if (fuliaoIn == null) {
			throw new Exception("找不到ID为" + id + "的辅料入库单");
		}
		fuliaoIn.setHas_print(true);
		fuliaoInService.updatePrint(fuliaoIn);
		Order order = orderService.get(fuliaoIn.getOrderId());
		request.setAttribute("order", order);
		request.setAttribute("fuliaoIn", fuliaoIn);
		return new ModelAndView("fuliaoinout/in_print");
	}
	
	/*打印纱线标签*/
	@RequestMapping(value = "/print/{id}/tag", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_tag(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {	
		if (id == null) {
			throw new Exception("缺少辅料入库单ID");
		}
		FuliaoIn fuliaoIn = fuliaoInService.get(id);
		if (fuliaoIn == null) {
			throw new Exception("找不到ID为" + id + "的辅料入库单");
		}
		fuliaoIn.setHas_tagprint(true);
		fuliaoInService.updateTagPrint(fuliaoIn);
		request.setAttribute("fuliaoIn", fuliaoIn);
		return new ModelAndView("fuliaoinout/in_tag_print");
	}
	
	
	// 数据纠正：删除
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		FuliaoIn fuliaoIn = fuliaoInService.get(id);	
		if(fuliaoIn.isDeletable()){
			String lcode = "fuliaoinout/delete";
			Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
			if (!hasAuthority) {
				throw new PermissionDeniedDataAccessException("没有删除辅料入库单的权限", null);
			}
			//删除
			int success = fuliaoInService.remove(fuliaoIn);
			return this.returnSuccess();
		}else{//若单据已打印
			String lcode = "data/correct";//数据纠正权限
			Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
			if (!hasAuthority) {
				throw new PermissionDeniedDataAccessException("辅料入库单已打印，且没有数据纠正的权限，无法删除", null);
			}
			//删除
			DataCorrectRecord dataCorrectRecord = new DataCorrectRecord();
			dataCorrectRecord.setCreated_at(DateTool.now());
			dataCorrectRecord.setCreated_user(user.getId());
			dataCorrectRecord.setOperation("删除");
			dataCorrectRecord.setTb_table("辅料入库单");
			dataCorrectRecord.setDescription("辅料入库单" + fuliaoIn.getNumber()+"已打印，因数据错误进行数据纠正删除");
			int success = fuliaoInService.remove_datacorrect(fuliaoIn,dataCorrectRecord);
			return this.returnSuccess();
		}
		
	}
//	public List<Map<String,Object>> getInStoreQuantity(List<ProducingOrder> list , List<HalfStoreInOut> halfstoreInList , List<HalfStoreReturn> halfstoreReturnList,PlanOrder planOrder) throws Exception{	
//		if(list == null){
//			return null;
//		}
//		
//		//根据  【planOrderDetailId】  统计总数量 , key = planOrderDetailId
//		HashMap<Integer, Integer> totalmap = new HashMap<Integer, Integer>();
//		for(ProducingOrder object : list){
//			for (ProducingOrderDetail detail : object.getDetaillist()) {
//				int key = detail.getPlanOrderDetailId(); 
//				if(totalmap.containsKey(key)){
//					int temp_total_quantity = totalmap.get(key);
//					totalmap.put(key, temp_total_quantity + detail.getQuantity());
//				}else{
//					totalmap.put(key, detail.getQuantity());
//				}
//			}
//		}
//		List<Map<String,Object>> resultlist = _getInStoreQuantity(totalmap,halfstoreInList,halfstoreReturnList);
//		for(Map<String,Object> map : resultlist){
//			int planOrderDetailId = (Integer)map.get("planOrderDetailId");
//			for(PlanOrderDetail detail : planOrder.getDetaillist()){
//				if(planOrderDetailId == detail.getId()){
//					map.put("color", detail.getColor());
//					map.put("weight", detail.getProduce_weight());
//					map.put("size", detail.getSize());
//					map.put("yarn", detail.getYarn());
//				}
//			}
//		}
//		return resultlist;
//	}
//	
//	public List<Map<String,Object>> _getInStoreQuantity(HashMap<Integer, Integer> totalmap , List<HalfStoreInOut> halfstoreInList , List<HalfStoreReturn> halfstoreReturnList){
//		//根据 【planOrderDetailId】获取已入库 数量
//		HashMap<Integer, Integer> inmap = new HashMap<Integer, Integer>();
//		for (HalfStoreInOut storeIn : halfstoreInList) {
//			for (HalfStoreInOutDetail temp : storeIn.getDetaillist()) {
//				int key = temp.getPlanOrderDetailId(); 
//				if(inmap.containsKey(key)){
//					int temp_total_quantity = inmap.get(key);
//					inmap.put(key, temp_total_quantity + temp.getQuantity());
//				}else{
//					inmap.put(key, temp.getQuantity());
//				}
//			}
//		}
//		
//		//根据 【planOrderDetailId】 统计已出库 数量
//		HashMap<Integer, Integer> total_returnmap = new HashMap<Integer, Integer>();
//		for (HalfStoreReturn storereturn : halfstoreReturnList) {
//			for (HalfStoreReturnDetail temp : storereturn.getDetaillist()) {
//				int key = temp.getPlanOrderDetailId(); 
//				if(total_returnmap.containsKey(key)){
//					int temp_total_quantity = total_returnmap.get(key);
//					total_returnmap.put(key, temp_total_quantity + temp.getQuantity());
//				}else{
//					total_returnmap.put(key, temp.getQuantity());
//				}
//			}
//		}
//		
//		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
//		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
//		for(Integer key : totalmap.keySet()){
//			int total_quantity = totalmap.get(key);
//			int in_quantity = 0 ;
//			if(inmap.containsKey(key)){
//				in_quantity = inmap.get(key);//库存数量
//			}
//			if(total_returnmap.containsKey(key)){
//				in_quantity = in_quantity - total_returnmap.get(key);
//			}
//			int not_in_quantity = total_quantity - in_quantity;
//			HashMap<String, Object> tempHash = new HashMap<String, Object>();
//			tempHash.put("planOrderDetailId", key);
//			tempHash.put("total_quantity", total_quantity);//计划单总量
//			tempHash.put("in_quantity", in_quantity);//实际入库数量 = 入库 - 退货
//			tempHash.put("not_in_quantity", not_in_quantity);//实际未入库
//			resultlist.add(tempHash);
//		}
//		return resultlist;
//	}
//	
//	public List<Map<String,Object>> getActualInStoreQuantity(List<HalfStoreInOut> halfstoreInList , List<HalfStoreReturn> halfstoreReturnList){
//		//根据 【planOrderDetailId】获取已入库 数量
//		HashMap<Integer, Integer> inmap = new HashMap<Integer, Integer>();
//		for (HalfStoreInOut storeIn : halfstoreInList) {
//			for (HalfStoreInOutDetail temp : storeIn.getDetaillist()) {
//				int key = temp.getPlanOrderDetailId(); 
//				if(inmap.containsKey(key)){
//					int temp_total_quantity = inmap.get(key);
//					inmap.put(key, temp_total_quantity + temp.getQuantity());
//				}else{
//					inmap.put(key, temp.getQuantity());
//				}
//			}
//		}
//		
//		//根据 【planOrderDetailId】 统计已出库 数量
//		HashMap<Integer, Integer> total_returnmap = new HashMap<Integer, Integer>();
//		for (HalfStoreReturn storereturn : halfstoreReturnList) {
//			for (HalfStoreReturnDetail temp : storereturn.getDetaillist()) {
//				int key = temp.getPlanOrderDetailId(); 
//				if(total_returnmap.containsKey(key)){
//					int temp_total_quantity = total_returnmap.get(key);
//					total_returnmap.put(key, temp_total_quantity + temp.getQuantity());
//				}else{
//					total_returnmap.put(key, temp.getQuantity());
//				}
//			}
//		}
//		
//		//获取未入库列表 ， 包括 色号、材料、总数量、已入库数量、未入库数量
//		List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
//
//		if(total_returnmap.size()>inmap.size()){
//			for(Integer key : total_returnmap.keySet()){
//				int return_quantity = total_returnmap.get(key);//
//				int in_quantity = 0;
//				if(inmap.containsKey(key)){
//					in_quantity = inmap.get(key);
//				}
//				int actual_in_quantity = in_quantity - return_quantity;
//				HashMap<String, Object> tempHash = new HashMap<String, Object>();
//				tempHash.put("planOrderDetailId", key);
//				tempHash.put("in_quantity", in_quantity);//计划单总量
//				tempHash.put("return_quantity", return_quantity);//实际入库数量 = 入库 - 退货
//				tempHash.put("actual_in_quantity", actual_in_quantity);//实际未入库
//				resultlist.add(tempHash);
//			}
//		}else{
//			for(Integer key : inmap.keySet()){
//				int in_quantity = inmap.get(key);//库存数量
//				int return_quantity = 0;
//				if(total_returnmap.containsKey(key)){
//					return_quantity = total_returnmap.get(key);
//				}
//				int actual_in_quantity = in_quantity - return_quantity;
//				HashMap<String, Object> tempHash = new HashMap<String, Object>();
//				tempHash.put("planOrderDetailId", key);
//				tempHash.put("in_quantity", in_quantity);//计划单总量
//				tempHash.put("return_quantity", return_quantity);//实际入库数量 = 入库 - 退货
//				tempHash.put("actual_in_quantity", actual_in_quantity);//实际未入库
//				resultlist.add(tempHash);
//			}
//		}
//		
//		return resultlist;
//	}

}
