package com.fuwei.controller.finishstore;

import java.util.ArrayList;
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

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.finishstore.FinishStoreIn;
import com.fuwei.entity.finishstore.FinishStoreInDetail;
import com.fuwei.entity.finishstore.FinishStoreStock;
import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.finishstore.FinishStoreInDetailService;
import com.fuwei.service.finishstore.FinishStoreInService;
import com.fuwei.service.finishstore.FinishStoreStockService;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/finishstore_in")
@Controller
public class FinishStoreInController extends BaseController {
	
	@Autowired
	FinishStoreInService finishStoreInService;
	@Autowired
	FinishStoreInDetailService finishStoreInDetailService;
	@Autowired
	FinishStoreStockService finishStoreStockService;
	@Autowired
	PackingOrderService packingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;

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
			throw new PermissionDeniedDataAccessException("没有查看成品入库单列表的权限", null);
		}
		List<FinishStoreIn> resultlist = finishStoreInService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<FinishStoreIn>();
		}
		request.setAttribute("resultlist", resultlist);
		request.setAttribute("orderId", OrderId);
		return new ModelAndView("finishstore/in/listbyorder");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable String orderNumber, HttpSession session, HttpServletRequest request,
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
			throw new PermissionDeniedDataAccessException("没有添加成品入库单的权限", null);
		}
		request.setAttribute("order", order);
		try {
			PackingOrder packingOrder = packingOrderService.getByOrder(orderId);
			request.setAttribute("packingOrder", packingOrder);
			Map<Integer,FinishStoreStockDetail> stockMap = finishStoreStockService.getStockMapByOrder(orderId);
			request.setAttribute("stockMap", stockMap);
			return new ModelAndView("finishstore/in/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FinishStoreIn finishStoreIn, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加成品入库单的权限", null);
		}
		try {	
			if(finishStoreIn.getOrderId() == null){
				throw new Exception("订单ID不能为空");
			}
			if(finishStoreIn.getPackingOrderId() == null){
				throw new Exception("装箱单号不能为空");
			}
			if(finishStoreIn.getOrderNumber() == null){
				throw new Exception("订单号不能为空");
			}
			finishStoreIn.setCreated_at(DateTool.now());// 设置创建时间
			finishStoreIn.setUpdated_at(DateTool.now());// 设置更新时间
			finishStoreIn.setCreated_user(user.getId());// 设置创建人
			
			List<FinishStoreInDetail> detaillist = SerializeTool
						.deserializeList(details,
								FinishStoreInDetail.class);
			Iterator<FinishStoreInDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FinishStoreInDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条入库明细");
			}
			finishStoreIn.setDetaillist(detaillist);
			if(finishStoreIn.getId() == 0){//添加
//				Order order = orderService.get(finishStoreIn.getOrderId());
//				fuliaoInOutNotice.setName(order.getName());
//				fuliaoInOutNotice.setCompany_productNumber(order.getCompany_productNumber());
//				fuliaoInOutNotice.setOrderNumber(order.getOrderNumber());
//				fuliaoInOutNotice.setCharge_employee(order.getCharge_employee());
				Integer tableOrderId = finishStoreInService.add(finishStoreIn);
				return this.returnSuccess("id", tableOrderId);
			}else{//编辑
				Integer tableOrderId = finishStoreInService.update(finishStoreIn);
				return this.returnSuccess("id", tableOrderId);
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
		String lcode = "finishstore/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除成品入库单的权限", null);
		}
		int success = finishStoreInService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FinishStoreIn get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "finishstore/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看成品入库单详情的权限", null);
		}
		FinishStoreIn finishStoreIn = finishStoreInService.getAndDetail(id);
		return finishStoreIn;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少成品入库单ID");
		}
		String lcode = "finishstore/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看成品入库单详情的权限", null);
		}	
		FinishStoreIn finishStoreIn = finishStoreInService.getAndDetail(id);
		request.setAttribute("finishStoreIn", finishStoreIn);
		Order order = orderService.get(finishStoreIn.getOrderId());
		request.setAttribute("order", order);
		return new ModelAndView("finishstore/in/detail");
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
			throw new PermissionDeniedDataAccessException("没有添加成品入库单的权限", null);
		}
		try {
			if(finishStoreInId!=null){
				FinishStoreIn finishStoreIn = finishStoreInService.getAndDetail(finishStoreInId);
				request.setAttribute("finishStoreIn", finishStoreIn);
				Order order = orderService.get(finishStoreIn.getOrderId());
				request.setAttribute("order", order);
				return new ModelAndView("finishstore/in/edit");
				
			}
			throw new Exception("缺少成品入库单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FinishStoreIn finishStoreIn, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑成品入库单的权限", null);
		}
		finishStoreIn.setUpdated_at(DateTool.now());
		List<FinishStoreInDetail> detaillist = SerializeTool
				.deserializeList(details,
						FinishStoreInDetail.class);
		Iterator<FinishStoreInDetail> iter = detaillist.iterator();
		while(iter.hasNext()){
			FinishStoreInDetail detail = iter.next();
			if(detail.getQuantity() == 0){
				iter.remove();
			}
		}
		if(detaillist==null || detaillist.size()<=0){
			throw new Exception("请至少填写一条入库明细");
		}
		finishStoreIn.setDetaillist(detaillist);
		Integer id = finishStoreInService.update(finishStoreIn);
		return this.returnSuccess("id", id);
		
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少成品入库单ID");
		}
		String lcode = "finishstore/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看打印成品入库单的权限", null);
		}	
		FinishStoreIn finishStoreIn = finishStoreInService.getAndDetail(id);
		request.setAttribute("finishStoreIn", finishStoreIn);
		Order order = orderService.get(finishStoreIn.getOrderId());
		request.setAttribute("order", order);
		return new ModelAndView("finishstore/in/print");
	}
}
