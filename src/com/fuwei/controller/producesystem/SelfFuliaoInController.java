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
import com.fuwei.entity.producesystem.SelfFuliaoIn;
import com.fuwei.entity.producesystem.SelfFuliaoInDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.producesystem.FuliaoCurrentStockService;
import com.fuwei.service.producesystem.SelfFuliaoInDetailService;
import com.fuwei.service.producesystem.SelfFuliaoInService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/selffuliaoin")
@Controller
public class SelfFuliaoInController extends BaseController {
	
	@Autowired
	SelfFuliaoInService selfFuliaoInService;
	@Autowired
	SelfFuliaoInDetailService selfFuliaoInDetailService;
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
			throw new PermissionDeniedDataAccessException("没有查看自购辅料入库列表的权限", null);
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

		pager = selfFuliaoInService.getList(pager, start_time_d, end_time_d,
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
		return new ModelAndView("selffuliao/in/index");
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
			throw new PermissionDeniedDataAccessException("没有查看自购辅料入库单列表的权限", null);
		}
		List<SelfFuliaoIn> resultlist = selfFuliaoInService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<SelfFuliaoIn>();
		}
		request.setAttribute("resultlist", resultlist);
		request.setAttribute("orderId", OrderId);
		return new ModelAndView("selffuliao/in/listbyorder");
	}
	// 添加或保存
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return new ModelAndView("selffuliao/in/scan");
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
			throw new PermissionDeniedDataAccessException("没有添加自购辅料入库单的权限", null);
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
			/*判断若stockMap 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			boolean flag = true;
			for(int fuliaoPurchaseOrderdetailId : stockMap.keySet()){
				if((Integer)stockMap.get(fuliaoPurchaseOrderdetailId).get("not_in_quantity") > 0){				
					flag = false;
				}
			}
			if(flag){
				request.setAttribute("message", "未入库数量为0，自购辅料已全部入库 ，无需再创建入库单， 请确认辅料是否超出！！！");
			}
			/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
			
			//自动匹配库位locationId
			Map<Integer,List<Map<String,Object>>> locationMap = new HashMap<Integer, List<Map<String,Object>>>();
			for(FuliaoPurchaseOrderDetail detail : fuliaoPurchaseOrder.getDetaillist()){
				List<Map<String,Object>> tempMap = selfFuliaoInService.matchlocation(detail.getId());
				locationMap.put(detail.getId(), tempMap);
			}
			request.setAttribute("locationMap", locationMap);
			return new ModelAndView("selffuliao/in/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(SelfFuliaoIn selfFuliaoIn, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selffuliaoinout/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加自购辅料入库单的权限", null);
		}
		try {	
			if(selfFuliaoIn.getFuliaoPurchaseOrderId() == 0){
			    throw new Exception("采购单ID不能为空");
			}
			if(selfFuliaoIn.getDate() == null){
			    throw new Exception("入库日期不能为空");
			}
			selfFuliaoIn.setCreated_at(DateTool.now());// 设置创建时间
			selfFuliaoIn.setUpdated_at(DateTool.now());// 设置更新时间
			selfFuliaoIn.setCreated_user(user.getId());// 设置创建人
			
			List<SelfFuliaoInDetail> detaillist = SerializeTool
						.deserializeList(details,
								SelfFuliaoInDetail.class);
			Iterator<SelfFuliaoInDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				SelfFuliaoInDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			    if(detail.getLocationId() == 0){
			    	throw new Exception("库位不能为空");
			    }
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条入库明细");
			}
			selfFuliaoIn.setDetaillist(detaillist);
			if(selfFuliaoIn.getId() == 0){//添加
				FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(selfFuliaoIn.getFuliaoPurchaseOrderId());
				selfFuliaoIn.setName(fuliaoPurchaseOrder.getName());
				selfFuliaoIn.setCompany_productNumber(fuliaoPurchaseOrder.getCompany_productNumber());
				selfFuliaoIn.setOrderId(fuliaoPurchaseOrder.getOrderId());
				selfFuliaoIn.setOrderNumber(fuliaoPurchaseOrder.getOrderNumber());
				selfFuliaoIn.setCharge_employee(fuliaoPurchaseOrder.getCharge_employee());
				selfFuliaoIn.setCompanyId(fuliaoPurchaseOrder.getCompanyId());
				selfFuliaoIn.setCustomerId(fuliaoPurchaseOrder.getCustomerId());
				selfFuliaoIn.setFuliaoPurchaseOrderId(fuliaoPurchaseOrder.getId());
				selfFuliaoIn.setFuliaoPurchaseOrder_number(fuliaoPurchaseOrder.getNumber());
				selfFuliaoIn.setFactoryId(fuliaoPurchaseOrder.getFactoryId());
				Integer tableOrderId = selfFuliaoInService.add(selfFuliaoIn);
				
				//获取当前库存
				Map<Integer,Map<String,Object>> stockMap = fuliaoCurrentStockService.getByPurchaseOrder(selfFuliaoIn.getFuliaoPurchaseOrderId());
				request.setAttribute("stockMap", stockMap);
				/*判断若stockMap 的not_in_quantity < 0 ， 则表示入库超出*/
				boolean flag = true;
				for(int fuliaoPurchaseOrderdetailId : stockMap.keySet()){
					if((Integer)stockMap.get(fuliaoPurchaseOrderdetailId).get("not_in_quantity") < 0){				
						flag = false;
					}
				}
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("id", tableOrderId);
				if(!flag){
					data.put("message", "辅料超数！！！入库总数大于辅料采购单的数量");
				}
				/*判断若detaillist 的not_in_quantity 均 == 0 ， 则表示已全部入库，无需再创建入库单*/
				return this.returnSuccess(data);
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
		SelfFuliaoIn selfFuliaoIn = selfFuliaoInService.get(id);	
		if(selfFuliaoIn.isDeletable()){
			String lcode = "selffuliaoinout/delete";
			Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
			if (!hasAuthority) {
				throw new PermissionDeniedDataAccessException("没有删除自购辅料入库单的权限", null);
			}
			//删除
			int success = selfFuliaoInService.remove(selfFuliaoIn);
			return this.returnSuccess();
		}else{//若单据已打印
			String lcode = "data/correct";//数据纠正权限
			Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
			if (!hasAuthority) {
				throw new PermissionDeniedDataAccessException("自购辅料入库单已打印，且没有数据纠正的权限，无法删除", null);
			}
			//删除
			DataCorrectRecord dataCorrectRecord = new DataCorrectRecord();
			dataCorrectRecord.setCreated_at(DateTool.now());
			dataCorrectRecord.setCreated_user(user.getId());
			dataCorrectRecord.setOperation("删除");
			dataCorrectRecord.setTb_table("自购辅料入库单");
			dataCorrectRecord.setDescription("自购辅料入库单" + selfFuliaoIn.getNumber()+"已打印，因数据错误进行数据纠正删除");
			int success = selfFuliaoInService.remove_datacorrect(selfFuliaoIn,dataCorrectRecord);
			return this.returnSuccess();
		}	
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public SelfFuliaoIn get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "selffuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看自购辅料入库单详情的权限", null);
		}
		SelfFuliaoIn fuliaoInOutNotice = selfFuliaoInService.getAndDetail(id);
		return fuliaoInOutNotice;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少自购辅料入库单ID");
		}
		String lcode = "selffuliaoinout/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看自购辅料入库单详情的权限", null);
		}	
		SelfFuliaoIn selfFuliaoIn = selfFuliaoInService.getAndDetail(id);
		request.setAttribute("selfFuliaoIn", selfFuliaoIn);
		return new ModelAndView("selffuliao/in/detail");
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少自购辅料入库单ID");
		}
		String lcode = "selffuliaoinout/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看打印自购辅料入库单的权限", null);
		}	
		SelfFuliaoIn selfFuliaoIn = selfFuliaoInService.getAndDetail(id);
		selfFuliaoIn.setHas_print(true);
		selfFuliaoInService.updatePrint(selfFuliaoIn);
		request.setAttribute("selfFuliaoIn", selfFuliaoIn);
		return new ModelAndView("selffuliao/in/print");
	}
	/*打印纱线标签*/
	@RequestMapping(value = "/print/{id}/tag", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print_tag(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {	
		if (id == null) {
			throw new Exception("缺少自购辅料入库单ID");
		}
		SelfFuliaoIn selfFuliaoIn = selfFuliaoInService.getAndDetail(id);
		if (selfFuliaoIn == null) {
			throw new Exception("找不到ID为" + id + "的自购辅料入库单");
		}
		selfFuliaoIn.setHas_tagprint(true);
		selfFuliaoInService.updateTagPrint(selfFuliaoIn);
		request.setAttribute("selfFuliaoIn", selfFuliaoIn);
		return new ModelAndView("selffuliao/in/tag_print");
	}
//	@RequestMapping(value = "/put/{selfFuliaoInId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView update(@PathVariable Integer selfFuliaoInId,
//			HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "selffuliaoinout/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有添加自购辅料入库单的权限", null);
//		}
//		try {
//			if(selfFuliaoInId!=null){
//				SelfFuliaoIn selfFuliaoIn = selfFuliaoInService.getAndDetail(selfFuliaoInId);	
//				//1、库存
//				//2、库位
//				request.setAttribute("selfFuliaoIn", selfFuliaoIn);
//				return new ModelAndView("selffuliaoin/edit");
//			}
//			throw new Exception("缺少自购辅料入库单ID");
//			
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	@RequestMapping(value = "/put", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> update(SelfFuliaoIn fuliaoInOutNotice, String details,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "selffuliaoinout/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有编辑自购辅料入库单的权限", null);
//		}
//		fuliaoInOutNotice.setUpdated_at(DateTool.now());
//		List<SelfFuliaoInDetail> detaillist = SerializeTool
//				.deserializeList(details,
//						SelfFuliaoInDetail.class);
//		Iterator<SelfFuliaoInDetail> iter = detaillist.iterator();
//		while(iter.hasNext()){
//			SelfFuliaoInDetail detail = iter.next();
//			if(detail.getQuantity() == 0){
//				iter.remove();
//			}
//		    if(detail.getLocationId() == 0){
//		    	throw new Exception("库位不能为空");
//		    }
//		}
//		fuliaoInOutNotice.setDetaillist(detaillist);
//		Integer fuliaoInOutNoticeId = selfFuliaoInService.update(fuliaoInOutNotice);
//		return this.returnSuccess("id", fuliaoInOutNoticeId);
//		
//	}
	

}
