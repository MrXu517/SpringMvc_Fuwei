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
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.producesystem.FuliaoOut;
import com.fuwei.entity.producesystem.FuliaoOutNoticeDetail;
import com.fuwei.entity.producesystem.SelfFuliaoIn;
import com.fuwei.entity.producesystem.SelfFuliaoInDetail;
import com.fuwei.entity.producesystem.SelfFuliaoOut;
import com.fuwei.entity.producesystem.SelfFuliaoOutDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.producesystem.FuliaoCurrentStockService;
import com.fuwei.service.producesystem.SelfFuliaoOutDetailService;
import com.fuwei.service.producesystem.SelfFuliaoOutService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/selffuliaoout")
@Controller
public class SelfFuliaoOutController extends BaseController {
	
	@Autowired
	SelfFuliaoOutService selfFuliaoOutService;
	@Autowired
	SelfFuliaoOutDetailService selfFuliaoOutDetailService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	@Autowired
	FuliaoCurrentStockService fuliaoCurrentStockService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			String orderNumber, Integer charge_employee,
			String number, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "selffuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看自购辅料出库列表的权限", null);
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

		pager = selfFuliaoOutService.getList(pager, start_time_d, end_time_d,
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
		return new ModelAndView("selffuliao/out/index");
	}
	@RequestMapping(value = "/list/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "selffuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看自购辅料出库单列表的权限", null);
		}
		List<SelfFuliaoOut> resultlist = selfFuliaoOutService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<SelfFuliaoOut>();
		}
		request.setAttribute("resultlist", resultlist);
		request.setAttribute("orderId", OrderId);
		return new ModelAndView("selffuliao/out/listbyorder");
	}
	// 添加或保存
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return new ModelAndView("selffuliao/out/scan");
	}
	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder2(String fuliaoPurchaseOrderNumber,HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 FuliaoPurchaseOrder fuliaoPurchaseOrder =  fuliaoPurchaseOrderService.get(fuliaoPurchaseOrderNumber);
		 if(fuliaoPurchaseOrder == null){
			 throw new Exception("找不到单号为" + fuliaoPurchaseOrderNumber + "的辅料采购单");
		 }
		 return addbyorder(fuliaoPurchaseOrder.getId(), session, request, response);
	}
	
	@RequestMapping(value = "/add/{fuliaoPurchaseOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer fuliaoPurchaseOrderId, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(fuliaoPurchaseOrderId == null || fuliaoPurchaseOrderId == 0){
			throw new Exception("缺少辅料采购单ID");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selffuliaoinout/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加自购辅料出库单的权限", null);
		}
		try {
			FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(fuliaoPurchaseOrderId);
			if(fuliaoPurchaseOrder == null){
				throw new Exception("辅料采购单不存在");
			}
			request.setAttribute("fuliaoPurchaseOrder", fuliaoPurchaseOrder);
			//获取当前库存
			Map<Integer,Map<String,Object>> stockMap = fuliaoCurrentStockService.getByPurchaseOrder(fuliaoPurchaseOrderId);
			request.setAttribute("stockMap", stockMap);
			//自动匹配库位locationId
			//Map<FuliaoPurchaseOrderDetailID,Map<locationId,stock_quantity>>
			Map<Integer,List<Map<String,Object>>> locationMap = new HashMap<Integer, List<Map<String,Object>>>();
			for(FuliaoPurchaseOrderDetail detail : fuliaoPurchaseOrder.getDetaillist()){
				int fuliaoPurchaseOrderDetailId = detail.getId();
				//获取该辅料 存放在哪些库位，各存放了多少 。 Map<locationId,stock_quantity>
				Map<Integer,Integer> locationTempMap = fuliaoCurrentStockService.locationByPurchaseDetail(fuliaoPurchaseOrderDetailId);
				List<Map<String,Object>> tempL = new ArrayList<Map<String,Object>>();
				for(Integer locationId : locationTempMap.keySet()){
					Map<String,Object> tempM = new HashMap<String, Object>();
					tempM.put("locationId", locationId);
					tempM.put("stock_quantity", locationTempMap.get(locationId));
					tempL.add(tempM);
				}
				locationMap.put(fuliaoPurchaseOrderDetailId, tempL);
			}
			request.setAttribute("locationMap", locationMap);
			//获取管理人员列表
			List<Employee> employeelist = new ArrayList<Employee>();
			for (Employee temp : SystemCache.employeelist) {
				if (temp.getIsmanager()) {
					employeelist.add(temp);
				}
			}
			request.setAttribute("employeelist", employeelist);
			//自动匹配库位locationId
			return new ModelAndView("selffuliao/out/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(SelfFuliaoOut selfFuliaoOut, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selffuliaoinout/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加自购辅料出库单的权限", null);
		}
		try {	
			if(selfFuliaoOut.getFuliaoPurchaseOrderId() == 0){
			    throw new Exception("采购单ID不能为空");
			}
			if(selfFuliaoOut.getReceiver_employee()==null || selfFuliaoOut.getReceiver_employee()== 0){
			    throw new Exception("领取人不能为空");
			}
			if(selfFuliaoOut.getDate() == null){
			    throw new Exception("出库日期不能为空");
			}
			selfFuliaoOut.setCreated_at(DateTool.now());// 设置创建时间
			selfFuliaoOut.setUpdated_at(DateTool.now());// 设置更新时间
			selfFuliaoOut.setCreated_user(user.getId());// 设置创建人
			
			List<SelfFuliaoOutDetail> detaillist = SerializeTool
						.deserializeList(details,
								SelfFuliaoOutDetail.class);
			Iterator<SelfFuliaoOutDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				SelfFuliaoOutDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			    if(detail.getLocationId() == 0){
			    	throw new Exception("库位不能为空");
			    }
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("本次出库数量均为0，无法创建自购辅料出库单");
			}
			selfFuliaoOut.setDetaillist(detaillist);
			if(selfFuliaoOut.getId() == 0){//添加
				FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(selfFuliaoOut.getFuliaoPurchaseOrderId());
				selfFuliaoOut.setName(fuliaoPurchaseOrder.getName());
				selfFuliaoOut.setCompany_productNumber(fuliaoPurchaseOrder.getCompany_productNumber());
				selfFuliaoOut.setOrderId(fuliaoPurchaseOrder.getOrderId());
				selfFuliaoOut.setOrderNumber(fuliaoPurchaseOrder.getOrderNumber());
				selfFuliaoOut.setCharge_employee(fuliaoPurchaseOrder.getCharge_employee());
				selfFuliaoOut.setCompanyId(fuliaoPurchaseOrder.getCompanyId());
				selfFuliaoOut.setCustomerId(fuliaoPurchaseOrder.getCustomerId());
				selfFuliaoOut.setFuliaoPurchaseOrderId(fuliaoPurchaseOrder.getId());
				selfFuliaoOut.setFuliaoPurchaseOrder_number(fuliaoPurchaseOrder.getNumber());
				Integer tableOrderId = selfFuliaoOutService.add(selfFuliaoOut);
				return this.returnSuccess("id", tableOrderId);
			}else{//编辑
				throw new Exception("id错误，创建时id只能为空");
			}
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	

	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();	
		SelfFuliaoOut selfFuliaoOut = selfFuliaoOutService.get(id);	
		if(selfFuliaoOut.isDeletable()){
			String lcode = "selffuliaoinout/delete";
			Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
			if (!hasAuthority) {
				throw new PermissionDeniedDataAccessException("没有删除自购辅料出库单的权限", null);
			}
			//删除
			int success = selfFuliaoOutService.remove(selfFuliaoOut);
			return this.returnSuccess();
		}else{//若单据已打印
			String lcode = "data/correct";//数据纠正权限
			Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
			if (!hasAuthority) {
				throw new PermissionDeniedDataAccessException("自购辅料出库单已打印，且没有数据纠正的权限，无法删除", null);
			}
			//删除
			DataCorrectRecord dataCorrectRecord = new DataCorrectRecord();
			dataCorrectRecord.setCreated_at(DateTool.now());
			dataCorrectRecord.setCreated_user(user.getId());
			dataCorrectRecord.setOperation("删除");
			dataCorrectRecord.setTb_table("自购辅料出库单");
			dataCorrectRecord.setDescription("自购辅料出库单" + selfFuliaoOut.getNumber()+"已打印，因数据错误进行数据纠正删除");
			int success = selfFuliaoOutService.remove_datacorrect(selfFuliaoOut,dataCorrectRecord);
			return this.returnSuccess();
		}	
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public SelfFuliaoOut get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "selffuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看自购辅料出库单详情的权限", null);
		}
		SelfFuliaoOut selfFuliaoOut = selfFuliaoOutService.getAndDetail(id);
		return selfFuliaoOut;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少自购辅料出库单ID");
		}
		String lcode = "selffuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看自购辅料出库单详情的权限", null);
		}	
		SelfFuliaoOut selfFuliaoOut = selfFuliaoOutService.getAndDetail(id);
		request.setAttribute("selfFuliaoOut", selfFuliaoOut);
		return new ModelAndView("selffuliao/out/detail");
	}
	//查看多个自购辅料出库单的详情，参数为ID
	@RequestMapping(value = "/detail_batch", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail_batch(String ids, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (ids == null) {
			throw new Exception("缺少自购辅料出库单ID");
		}
		List<SelfFuliaoOut> result = selfFuliaoOutService.getListByIds(ids);
		if (result == null|| result.size()==0) {
			throw new Exception("找不到ID为" + ids + "的自购辅料出库单");
		}
		request.setAttribute("result", result);
		request.setAttribute("ids", ids);
		return new ModelAndView("selffuliao/out/detail_batch");	
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少自购辅料出库单ID");
		}
		String lcode = "selffuliaoinout/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看打印自购辅料出库单的权限", null);
		}	
		SelfFuliaoOut selfFuliaoOut = selfFuliaoOutService.getAndDetail(id);
		selfFuliaoOut.setHas_print(true);
		selfFuliaoOutService.updatePrint(selfFuliaoOut);
		request.setAttribute("selfFuliaoOut", selfFuliaoOut);
		return new ModelAndView("selffuliao/out/print");
	}
	//批量打印出库单
	@RequestMapping(value = "/print_batch", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_batch(String ids, HttpSession session,
			HttpServletRequest request) throws Exception {	
		if (ids == null) {
			throw new Exception("缺少辅料出库单ID");
		}
		List<SelfFuliaoOut> result = selfFuliaoOutService.getListByIds(ids);
		if (result == null|| result.size()==0) {
			throw new Exception("找不到ID为" + ids + "的自购辅料出库单");
		}
		for(SelfFuliaoOut item : result){
			item.setHas_print(true);
		}
		selfFuliaoOutService.updatePrint_batch(result);
		request.setAttribute("result", result);
		return new ModelAndView("selffuliao/out/print_batch");
		
	}
	/*批量打印纱线标签*/
	@RequestMapping(value = "/print_batch/tag", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_tag(String ids, HttpSession session,
			HttpServletRequest request) throws Exception {	
		if (ids == null) {
			throw new Exception("缺少辅料出库单ID");
		}
		List<SelfFuliaoOut> result = selfFuliaoOutService.getListByIds(ids);
		if (result == null|| result.size()==0) {
			throw new Exception("找不到ID为" + ids + "的自购辅料出库单");
		}
		for(SelfFuliaoOut item : result){
			item.setHas_tagprint(true);
		}
		selfFuliaoOutService.updateTagPrint_batch(result);
		request.setAttribute("result", result);
		return new ModelAndView("selffuliao/out/tag_print_batch");
	}
	/*打印纱线标签*/
	@RequestMapping(value = "/print/{id}/tag", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_tag(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {	
		if (id == null) {
			throw new Exception("缺少自购辅料出库单ID");
		}
		SelfFuliaoOut selfFuliaoOut = selfFuliaoOutService.getAndDetail(id);
		if (selfFuliaoOut == null) {
			throw new Exception("找不到ID为" + id + "的自购辅料出库单");
		}
		selfFuliaoOut.setHas_tagprint(true);
		selfFuliaoOutService.updateTagPrint(selfFuliaoOut);
		request.setAttribute("selfFuliaoOut", selfFuliaoOut);
		return new ModelAndView("selffuliao/out/tag_print");
	}
}
