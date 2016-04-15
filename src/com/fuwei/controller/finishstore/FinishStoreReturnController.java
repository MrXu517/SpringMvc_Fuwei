package com.fuwei.controller.finishstore;

import java.util.ArrayList;
import java.util.Date;
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
import com.fuwei.entity.finishstore.FinishStoreIn;
import com.fuwei.entity.finishstore.FinishStoreInDetail;
import com.fuwei.entity.finishstore.FinishStoreReturn;
import com.fuwei.entity.finishstore.FinishStoreReturnDetail;
import com.fuwei.entity.finishstore.FinishStoreStock;
import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.finishstore.FinishStoreInDetailService;
import com.fuwei.service.finishstore.FinishStoreInService;
import com.fuwei.service.finishstore.FinishStoreReturnDetailService;
import com.fuwei.service.finishstore.FinishStoreReturnService;
import com.fuwei.service.finishstore.FinishStoreStockService;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/finishstore_return")
@Controller
public class FinishStoreReturnController extends BaseController {
	
	@Autowired
	FinishStoreReturnService finishStoreReturnService;
	@Autowired
	FinishStoreReturnDetailService finishStoreReturnDetailService;
	@Autowired
	FinishStoreStockService finishStoreStockService;
	@Autowired
	PackingOrderService packingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			String orderNumber, String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "finishstore/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看成品退货列表的权限", null);
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

		pager = finishStoreReturnService.getListAndDetail(pager, start_time_d, end_time_d,
				orderNumber,null,null, sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("finishstore/return/index");
	}
	
	@RequestMapping(value = "/list/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "finishstore/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看成品退货单列表的权限", null);
		}
		List<FinishStoreReturn> resultlist = finishStoreReturnService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<FinishStoreReturn>();
		}
		request.setAttribute("resultlist", resultlist);
		request.setAttribute("orderId", OrderId);
		return new ModelAndView("finishstore/return/listbyorder");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(String orderNumber, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Order order =  orderService.get(orderNumber);
		 if(order == null){
			 throw new Exception("找不到订单号为" + orderNumber + "的订单");
		 }
		int orderId = order.getId();
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加成品退货单的权限", null);
		}
		request.setAttribute("order", order);
		try {
			
			PackingOrder packingOrder = packingOrderService.getByOrderAndDetail(orderId);
			if(packingOrder == null){
				throw new Exception("该订单没有创建装箱单，请先创建装箱单 点击此处创建 <a href='packing_order/add/"+ orderId + "'>添加装箱单</a>");
			}
			request.setAttribute("packingOrder", packingOrder);
			Map<Integer,FinishStoreStockDetail> stockMap = finishStoreStockService.getStockMapByOrder(orderId);
			boolean flag = true;
			for(int packingOrderDetailId:stockMap.keySet()){
				FinishStoreStockDetail temp = stockMap.get(packingOrderDetailId);
				int actual_in_quantity = temp.getIn_quantity() - temp.getReturn_quantity();
				if(actual_in_quantity>0){//若实际入库数量>0，则表示有货可退
					flag = false;
				}
			}
			if(flag){
				throw new Exception("实际入库数量为0，已全部退货或者还未入库，无法再创建退货单！！！ <br> <a href='finishstore_workspace/workspace'>点击此处返回成品工作台</a>");
			}
			
			
			request.setAttribute("stockMap", stockMap);
			return new ModelAndView("finishstore/return/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FinishStoreReturn finishStoreReturn, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加成品退货单的权限", null);
		}
		try {	
			if(finishStoreReturn.getOrderId() == null){
				throw new Exception("订单ID不能为空");
			}
			if(finishStoreReturn.getPackingOrderId() == null){
				throw new Exception("装箱单号不能为空");
			}
			finishStoreReturn.setCreated_at(DateTool.now());// 设置创建时间
			finishStoreReturn.setUpdated_at(DateTool.now());// 设置更新时间
			finishStoreReturn.setCreated_user(user.getId());// 设置创建人
			
			Order order = orderService.get(finishStoreReturn.getOrderId());
			if(order == null){
				throw new Exception("不存在ID="+finishStoreReturn.getOrderId()+"的订单" );
			}
			finishStoreReturn.setCharge_employee(order.getCharge_employee());
			finishStoreReturn.setCompany_productNumber(order.getCompany_productNumber());
			finishStoreReturn.setName(order.getName());
			finishStoreReturn.setOrderNumber(order.getOrderNumber());
			finishStoreReturn.setCompanyId(order.getCompanyId());
			finishStoreReturn.setCustomerId(order.getCustomerId());
			finishStoreReturn.setImg(order.getImg());
			finishStoreReturn.setImg_s(order.getImg_s());
			finishStoreReturn.setImg_ss(order.getImg_ss());
			List<FinishStoreReturnDetail> detaillist = SerializeTool
						.deserializeList(details,
								FinishStoreReturnDetail.class);
			Iterator<FinishStoreReturnDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FinishStoreReturnDetail detail = iter.next();
				if(detail.getCartons() == 0){
					iter.remove();
				}
				detail.setQuantity(detail.getCartons() * detail.getPer_carton_quantity());
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条入库明细");
			}
			finishStoreReturn.setDetaillist(detaillist);		
			Integer tableOrderId = finishStoreReturnService.add(finishStoreReturn);
			return this.returnSuccess("id", tableOrderId);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	

	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除成品退货单的权限", null);
		}
		int success = finishStoreReturnService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FinishStoreReturn get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "finishstore/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看成品退货单详情的权限", null);
		}
		FinishStoreReturn finishStoreReturn = finishStoreReturnService.getAndDetail(id);
		return finishStoreReturn;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少成品退货单ID");
		}
		String lcode = "finishstore/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看成品退货单详情的权限", null);
		}	
		FinishStoreReturn finishStoreReturn = finishStoreReturnService.getAndDetail(id);
		request.setAttribute("finishStoreReturn", finishStoreReturn);
		return new ModelAndView("finishstore/return/detail");
	}
	
	@RequestMapping(value = "/put/{finishStoreInId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer finishStoreInId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑成品退货单的权限", null);
		}
		try {
			if(finishStoreInId!=null){
				FinishStoreReturn finishStoreReturn = finishStoreReturnService.getAndDetail(finishStoreInId);
				request.setAttribute("finishStoreReturn", finishStoreReturn);
				Map<Integer,FinishStoreStockDetail> stockMap = finishStoreStockService.getStockMapByOrder(finishStoreReturn.getOrderId());
				request.setAttribute("stockMap", stockMap);
				return new ModelAndView("finishstore/return/edit");
				
			}
			throw new Exception("缺少成品退货单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FinishStoreReturn finishStoreReturn, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑成品退货单的权限", null);
		}
		finishStoreReturn.setUpdated_at(DateTool.now());
		
		Order order = orderService.get(finishStoreReturn.getOrderId());
		if(order == null){
			throw new Exception("不存在ID="+finishStoreReturn.getOrderId()+"的订单" );
		}
		finishStoreReturn.setCharge_employee(order.getCharge_employee());
		finishStoreReturn.setCompany_productNumber(order.getCompany_productNumber());
		finishStoreReturn.setName(order.getName());
		finishStoreReturn.setOrderNumber(order.getOrderNumber());
		finishStoreReturn.setCompanyId(order.getCompanyId());
		finishStoreReturn.setCustomerId(order.getCustomerId());
		finishStoreReturn.setImg(order.getImg());
		finishStoreReturn.setImg_s(order.getImg_s());
		finishStoreReturn.setImg_ss(order.getImg_ss());
		
		List<FinishStoreReturnDetail> detaillist = SerializeTool
				.deserializeList(details,
						FinishStoreReturnDetail.class);
		Iterator<FinishStoreReturnDetail> iter = detaillist.iterator();
		while(iter.hasNext()){
			FinishStoreReturnDetail detail = iter.next();
			if(detail.getCartons() == 0){
				iter.remove();
			}
			detail.setQuantity(detail.getCartons() * detail.getPer_carton_quantity());
			if(detail.getQuantity() == 0){
				iter.remove();
			}
		}
		if(detaillist==null || detaillist.size()<=0){
			throw new Exception("请至少填写一条入库明细");
		}
		finishStoreReturn.setDetaillist(detaillist);
		Integer id = finishStoreReturnService.update(finishStoreReturn);
		return this.returnSuccess("id", id);
		
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少成品退货单ID");
		}
		String lcode = "finishstore/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看打印成品退货单的权限", null);
		}	
		FinishStoreReturn finishStoreReturn = finishStoreReturnService.getAndDetail(id);
		request.setAttribute("finishStoreReturn", finishStoreReturn);
		return new ModelAndView("finishstore/return/print");
	}

	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("finishstore/return/scan");
	}
}
