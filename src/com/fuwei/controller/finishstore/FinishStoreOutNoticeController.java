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
import com.fuwei.entity.finishstore.FinishStoreOutNotice;
import com.fuwei.entity.finishstore.FinishStoreOutNoticeDetail;
import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.finishstore.FinishStoreOutNoticeDetailService;
import com.fuwei.service.finishstore.FinishStoreOutNoticeService;
import com.fuwei.service.finishstore.FinishStoreStockService;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/finishstoreout_notice")
@Controller
public class FinishStoreOutNoticeController extends BaseController {
	
	@Autowired
	FinishStoreOutNoticeService finishStoreOutNoticeService;
	@Autowired
	FinishStoreOutNoticeDetailService finishStoreOutNoticeDetailService;
	@Autowired
	PackingOrderService packingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	FinishStoreStockService finishStoreStockService;

	@RequestMapping(value = "/list/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "finishstore_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看成品出库通知单列表的权限", null);
		}
		List<FinishStoreOutNotice> resultlist = finishStoreOutNoticeService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<FinishStoreOutNotice>();
		}
		request.setAttribute("resultlist", resultlist);
		Order order = orderService.get(OrderId);
		request.setAttribute("order", order);
		return new ModelAndView("finishstoreout_notice/listbyorder");
	}
	
	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable Integer orderId, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(orderId == null || orderId == 0){
			throw new Exception("缺少订单ID");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料预出库通知单的权限", null);
		}
		try {
			Order order = orderService.get(orderId);
			if(order == null){
				throw new Exception("订单不存在");
			}
			request.setAttribute("order", order);
//			PackingOrder packingOrder = packingOrderService.getByOrderAndDetail(orderId);
//			request.setAttribute("packingOrder", packingOrder);
			List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
			if(packingOrderList == null || packingOrderList.size()<=0){
				throw new Exception("该订单没有创建装箱单，请先创建装箱单 点击此处创建 <a href='packing_order/add/"+ orderId + "'>添加装箱单</a>");
			}
			request.setAttribute("packingOrderList", packingOrderList);
			Map<Integer,FinishStoreStockDetail> stockMap = finishStoreStockService.getStockMapByOrder(orderId);
			request.setAttribute("stockMap", stockMap);
			return new ModelAndView("finishstoreout_notice/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FinishStoreOutNotice finishStoreOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料预出库通知单的权限", null);
		}
		try {	
			finishStoreOutNotice.setCreated_at(DateTool.now());// 设置创建时间
			finishStoreOutNotice.setUpdated_at(DateTool.now());// 设置更新时间
			finishStoreOutNotice.setCreated_user(user.getId());// 设置创建人
			if(finishStoreOutNotice.getDate() == null){
				throw new Exception("发货日期不能为空");
			}
			List<FinishStoreOutNoticeDetail> detaillist = SerializeTool
						.deserializeList(details,
								FinishStoreOutNoticeDetail.class);
			Iterator<FinishStoreOutNoticeDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FinishStoreOutNoticeDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条出库明细");
			}
			finishStoreOutNotice.setDetaillist(detaillist);
			Order order = orderService.get(finishStoreOutNotice.getOrderId());
			finishStoreOutNotice.setCharge_employee(order.getCharge_employee());
			finishStoreOutNotice.setCompany_productNumber(order.getCompany_productNumber());
			finishStoreOutNotice.setName(order.getName());
			finishStoreOutNotice.setOrderNumber(order.getOrderNumber());
			finishStoreOutNotice.setCompanyId(order.getCompanyId());
			finishStoreOutNotice.setCustomerId(order.getCustomerId());
			finishStoreOutNotice.setImg(order.getImg());
			finishStoreOutNotice.setImg_s(order.getImg_s());
			finishStoreOutNotice.setImg_ss(order.getImg_ss());
			Integer tableOrderId = finishStoreOutNoticeService.add(finishStoreOutNotice);
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
		String lcode = "finishstore_notice/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除辅料预出库通知单的权限", null);
		}
		int success = finishStoreOutNoticeService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FinishStoreOutNotice get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "finishstore_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看辅料预出库通知单详情的权限", null);
		}
		FinishStoreOutNotice finishStoreOutNotice = finishStoreOutNoticeService.get(id);
		return finishStoreOutNotice;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料预出库通知单ID");
		}
		String lcode = "finishstore_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料预出库通知单详情的权限", null);
		}	
		FinishStoreOutNotice finishStoreOutNotice = finishStoreOutNoticeService.getAndDetail(id);
		request.setAttribute("finishStoreOutNotice", finishStoreOutNotice);	
		Order order = orderService.get(finishStoreOutNotice.getOrderId());
		request.setAttribute("order", order);
		return new ModelAndView("finishstoreout_notice/detail");
	}
	
	@RequestMapping(value = "/put/{finishStoreOutNoticeId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer finishStoreOutNoticeId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore_notice/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料预出库通知单的权限", null);
		}
		try {
			if(finishStoreOutNoticeId!=null){
				FinishStoreOutNotice finishStoreOutNotice = finishStoreOutNoticeService.getAndDetail(finishStoreOutNoticeId);
				request.setAttribute("finishStoreOutNotice", finishStoreOutNotice);
				Order order = orderService.get(finishStoreOutNotice.getOrderId());
				request.setAttribute("order", order);
				Map<Integer,FinishStoreStockDetail> stockMap = finishStoreStockService.getStockMapByOrder(finishStoreOutNotice.getOrderId());
				request.setAttribute("stockMap", stockMap);
				return new ModelAndView("finishstoreout_notice/edit");
			}
			throw new Exception("缺少辅料预出库通知单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FinishStoreOutNotice finishStoreOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "finishstore_notice/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑辅料预出库通知单的权限", null);
		}
		if(finishStoreOutNotice.getDate() == null){
			throw new Exception("发货日期不能为空");
		}
		finishStoreOutNotice.setUpdated_at(DateTool.now());
		List<FinishStoreOutNoticeDetail> detaillist = SerializeTool
				.deserializeList(details,
						FinishStoreOutNoticeDetail.class);
		Iterator<FinishStoreOutNoticeDetail> iter = detaillist.iterator();
		while(iter.hasNext()){
			FinishStoreOutNoticeDetail detail = iter.next();
			if(detail.getQuantity() == 0){
				iter.remove();
			}
		}
		if(detaillist==null || detaillist.size()<=0){
			throw new Exception("请至少填写一条出库明细");
		}
		finishStoreOutNotice.setDetaillist(detaillist);
		Integer finishStoreOutNoticeId = finishStoreOutNoticeService.update(finishStoreOutNotice);
		return this.returnSuccess("id", finishStoreOutNoticeId);
		
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料预出库通知单ID");
		}
		String lcode = "finishstore_notice/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看打印辅料预出库通知单的权限", null);
		}	
		FinishStoreOutNotice finishStoreOutNotice = finishStoreOutNoticeService.getAndDetail(id);
		request.setAttribute("finishStoreOutNotice", finishStoreOutNotice);
		Order order = orderService.get(finishStoreOutNotice.getOrderId());
		request.setAttribute("order", order);
		return new ModelAndView("finishstoreout_notice/print");
	}
}
