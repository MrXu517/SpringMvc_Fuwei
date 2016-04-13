package com.fuwei.controller.producesystem;

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
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.entity.producesystem.FuliaoOutNotice;
import com.fuwei.entity.producesystem.FuliaoOutNoticeDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.producesystem.FuliaoCurrentStockService;
import com.fuwei.service.producesystem.FuliaoOutNoticeDetailService;
import com.fuwei.service.producesystem.FuliaoOutNoticeService;
import com.fuwei.service.producesystem.FuliaoService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/fuliaoout_notice")
@Controller
public class FuliaoOutNoticeController extends BaseController {
	
	@Autowired
	FuliaoOutNoticeService fuliaoOutNoticeService;
	@Autowired
	FuliaoOutNoticeDetailService fuliaoOutNoticeDetailService;
	@Autowired
	FuliaoService fuliaoService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	FuliaoCurrentStockService fuliaoCurrentStockService;

	@RequestMapping(value = "/list/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "fuliaoinout_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看出库通知单列表的权限", null);
		}
		List<FuliaoOutNotice> resultlist = fuliaoOutNoticeService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<FuliaoOutNotice>();
		}
		request.setAttribute("resultlist", resultlist);
		request.setAttribute("orderId", OrderId);
		return new ModelAndView("fuliaoout_notice/listbyorder");
	}
	
	//通用辅料入库通知单列表
	@RequestMapping(value = "/list_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView list_common(HttpSession session, HttpServletRequest request) throws Exception {
		String lcode = "fuliao_workspace/commonfuliao";		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看出库通知单列表的权限", null);
		}
		List<FuliaoOutNotice> resultlist = fuliaoOutNoticeService.getList_common();
		if (resultlist == null) {
			resultlist = new ArrayList<FuliaoOutNotice>();
		}
		request.setAttribute("resultlist", resultlist);
		return new ModelAndView("fuliaoout_notice/list_common");
	}
	
	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable Integer orderId, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(orderId == null || orderId == 0){
			throw new Exception("缺少订单ID");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
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
//			List<Fuliao> fuliaolist = fuliaoService.getList(orderId);
//			if(fuliaolist == null){
//				fuliaolist = new ArrayList<Fuliao>();
//			}
//			request.setAttribute("fuliaolist", fuliaolist);
			List<Map<String,Object>> detaillist = fuliaoCurrentStockService.getByOrder(orderId);
			request.setAttribute("detaillist", detaillist);
			return new ModelAndView("fuliaoout_notice/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add_common(Integer companyId,Integer salesmanId,Integer customerId,String memo, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料预出库通知单的权限", null);
		}
		try {
			List<Integer> fuliaoIdList = fuliaoService.getIdList_Common(companyId,salesmanId,customerId,memo);
			if (fuliaoIdList == null) {
				fuliaoIdList = new ArrayList<Integer>();
			}
			String ids = "";
			for(Integer id : fuliaoIdList){
				ids += id+",";
			}
			if(ids.length()>0){
				ids = ids.substring(0,ids.length()-1);
			}
			//获取指定辅料id的属性和库存信息
			List<Map<String,Object>> detaillist = fuliaoCurrentStockService.getByOrder_Common(ids);
			request.setAttribute("detaillist", detaillist);
			request.setAttribute("companyId", companyId);
			request.setAttribute("salesmanId", salesmanId);
			request.setAttribute("customerId", customerId);
			request.setAttribute("memo", memo);
			return new ModelAndView("fuliaoout_notice/add_common");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	@RequestMapping(value = "/add_common", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add_common(FuliaoOutNotice fuliaoOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料预出库通知单的权限", null);
		}
		try {	
			fuliaoOutNotice.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoOutNotice.setUpdated_at(DateTool.now());// 设置更新时间
			fuliaoOutNotice.setCreated_user(user.getId());// 设置创建人
			
			List<FuliaoOutNoticeDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoOutNoticeDetail.class);
			Iterator<FuliaoOutNoticeDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FuliaoOutNoticeDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条出库明细");
			}
			fuliaoOutNotice.setDetaillist(detaillist);
			if(fuliaoOutNotice.getId() == 0){//添加
				Integer tableOrderId = fuliaoOutNoticeService.add_common(fuliaoOutNotice);
				return this.returnSuccess("id", tableOrderId);
			}else{//编辑
				Integer fuliaoInOutNoticeId = fuliaoOutNoticeService.update(fuliaoOutNotice);
				return this.returnSuccess("id", fuliaoInOutNoticeId);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FuliaoOutNotice fuliaoOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料预出库通知单的权限", null);
		}
		try {	
			fuliaoOutNotice.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoOutNotice.setUpdated_at(DateTool.now());// 设置更新时间
			fuliaoOutNotice.setCreated_user(user.getId());// 设置创建人
			
			List<FuliaoOutNoticeDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoOutNoticeDetail.class);
			Iterator<FuliaoOutNoticeDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FuliaoOutNoticeDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条出库明细");
			}
			fuliaoOutNotice.setDetaillist(detaillist);
			if(fuliaoOutNotice.getId() == 0){//添加
				Order order = orderService.get(fuliaoOutNotice.getOrderId());
				fuliaoOutNotice.setName(order.getName());
				fuliaoOutNotice.setCompany_productNumber(order.getCompany_productNumber());
				fuliaoOutNotice.setOrderNumber(order.getOrderNumber());
				fuliaoOutNotice.setCharge_employee(order.getCharge_employee());
				Integer tableOrderId = fuliaoOutNoticeService.add(fuliaoOutNotice);
				return this.returnSuccess("id", tableOrderId);
			}else{//编辑
				Integer fuliaoInOutNoticeId = fuliaoOutNoticeService.update(fuliaoOutNotice);
				return this.returnSuccess("id", fuliaoInOutNoticeId);
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
		String lcode = "fuliaoinout_notice/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除辅料预出库通知单的权限", null);
		}
		int success = fuliaoOutNoticeService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FuliaoOutNotice get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "fuliaoinout_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看辅料预出库通知单详情的权限", null);
		}
		FuliaoOutNotice fuliaoOutNotice = fuliaoOutNoticeService.get(id);
		return fuliaoOutNotice;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料预出库通知单ID");
		}
		String lcode = "fuliaoinout_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料预出库通知单详情的权限", null);
		}	
		FuliaoOutNotice fuliaoOutNotice = fuliaoOutNoticeService.getAndDetail(id);
		request.setAttribute("fuliaoOutNotice", fuliaoOutNotice);	
		if(fuliaoOutNotice.getOrderId()!=null && fuliaoOutNotice.getOrderId()!=0){
			Order order = orderService.get(fuliaoOutNotice.getOrderId());
			request.setAttribute("order", order);
			return new ModelAndView("fuliaoout_notice/detail");
		}else{
			return new ModelAndView("fuliaoout_notice/detail_common");
		}
	}
	
	@RequestMapping(value = "/put/{fuliaoInOutNoticeId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer fuliaoInOutNoticeId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料预入库通知单的权限", null);
		}
		try {
			if(fuliaoInOutNoticeId!=null){
				FuliaoOutNotice fuliaoOutNotice = fuliaoOutNoticeService.getAndDetail(fuliaoInOutNoticeId);
				request.setAttribute("fuliaoOutNotice", fuliaoOutNotice);
				if(fuliaoOutNotice.getOrderId()!=null && fuliaoOutNotice.getOrderId()!=0){
					Order order = orderService.get(fuliaoOutNotice.getOrderId());
					request.setAttribute("order", order);
					return new ModelAndView("fuliaoout_notice/edit");
				}else{
					return new ModelAndView("fuliaoout_notice/edit_common");
				}
				
				
			}
			throw new Exception("缺少辅料预出库通知单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FuliaoOutNotice fuliaoInOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑辅料预出库通知单的权限", null);
		}
		fuliaoInOutNotice.setUpdated_at(DateTool.now());
		List<FuliaoOutNoticeDetail> detaillist = SerializeTool
				.deserializeList(details,
						FuliaoOutNoticeDetail.class);
		Iterator<FuliaoOutNoticeDetail> iter = detaillist.iterator();
		while(iter.hasNext()){
			FuliaoOutNoticeDetail detail = iter.next();
			if(detail.getQuantity() == 0){
				iter.remove();
			}
		}
		fuliaoInOutNotice.setDetaillist(detaillist);
		Integer fuliaoInOutNoticeId = fuliaoOutNoticeService.update(fuliaoInOutNotice);
		return this.returnSuccess("id", fuliaoInOutNoticeId);
		
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料预入库通知单ID");
		}
		String lcode = "fuliaoinout_notice/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看打印辅料预出库通知单的权限", null);
		}	
		FuliaoOutNotice fuliaoOutNotice = fuliaoOutNoticeService.getAndDetail(id);
		request.setAttribute("fuliaoOutNotice", fuliaoOutNotice);
		if(fuliaoOutNotice.getOrderId()!=null && fuliaoOutNotice.getOrderId()!=0){
			Order order = orderService.get(fuliaoOutNotice.getOrderId());
			request.setAttribute("order", order);
			return new ModelAndView("fuliaoout_notice/print");
		}else{
			return new ModelAndView("fuliaoout_notice/print_common");
		}
	}
}
