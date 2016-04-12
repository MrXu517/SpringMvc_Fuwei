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
import com.fuwei.entity.producesystem.FuliaoInNotice;
import com.fuwei.entity.producesystem.FuliaoInNoticeDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.producesystem.FuliaoInNoticeDetailService;
import com.fuwei.service.producesystem.FuliaoInNoticeService;
import com.fuwei.service.producesystem.FuliaoService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/fuliaoin_notice")
@Controller
public class FuliaoInNoticeController extends BaseController {
	
	@Autowired
	FuliaoInNoticeService fuliaoInOutNoticeService;
	@Autowired
	FuliaoInNoticeDetailService fuliaoInOutNoticeDetailService;
	@Autowired
	FuliaoService fuliaoService;
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
		String lcode = "fuliaoinout_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看入库通知单列表的权限", null);
		}
		List<FuliaoInNotice> resultlist = fuliaoInOutNoticeService.getList(OrderId);
		if (resultlist == null) {
			resultlist = new ArrayList<FuliaoInNotice>();
		}
		request.setAttribute("resultlist", resultlist);
		request.setAttribute("orderId", OrderId);
		return new ModelAndView("fuliaoin_notice/listbyorder");
	}
	
	//通用辅料入库通知单列表
	@RequestMapping(value = "/list_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView list_common(HttpSession session, HttpServletRequest request) throws Exception {
		String lcode = "fuliao_workspace/commonfuliao";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看入库通知单列表的权限", null);
		}
		List<FuliaoInNotice> resultlist = fuliaoInOutNoticeService.getList_common();
		if (resultlist == null) {
			resultlist = new ArrayList<FuliaoInNotice>();
		}
		request.setAttribute("resultlist", resultlist);
		return new ModelAndView("fuliaoin_notice/list_common");
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
			throw new PermissionDeniedDataAccessException("没有添加辅料预入库通知单的权限", null);
		}
		try {
			Order order = orderService.get(orderId);
			if(order == null){
				throw new Exception("订单不存在");
			}
			request.setAttribute("order", order);
			List<Fuliao> fuliaolist = fuliaoService.getList(orderId);
			if(fuliaolist == null){
				fuliaolist = new ArrayList<Fuliao>();
			}
			request.setAttribute("fuliaolist", fuliaolist);
			return new ModelAndView("fuliaoin_notice/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	//创建通用辅料入库通知单
	@RequestMapping(value = "/add_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add_common(Integer companyId,Integer salesmanId,Integer customerId,String memo,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料预入库通知单的权限", null);
		}
		try {
			List<Fuliao> fuliaolist = fuliaoService.getList_Common(companyId,salesmanId,customerId,memo);
			if(fuliaolist == null){
				fuliaolist = new ArrayList<Fuliao>();
			}
			request.setAttribute("fuliaolist", fuliaolist);
			return new ModelAndView("fuliaoin_notice/add_common");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	@RequestMapping(value = "/add_common", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add_common(FuliaoInNotice fuliaoInOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料预入库通知单的权限", null);
		}
		try {	
			fuliaoInOutNotice.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoInOutNotice.setUpdated_at(DateTool.now());// 设置更新时间
			fuliaoInOutNotice.setCreated_user(user.getId());// 设置创建人
			
			List<FuliaoInNoticeDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoInNoticeDetail.class);
			Iterator<FuliaoInNoticeDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FuliaoInNoticeDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条入库明细");
			}
			fuliaoInOutNotice.setDetaillist(detaillist);
			if(fuliaoInOutNotice.getId() == 0){//添加
				Integer tableOrderId = fuliaoInOutNoticeService.add_common(fuliaoInOutNotice);
				return this.returnSuccess("id", tableOrderId);
			}else{//编辑
				Integer fuliaoInOutNoticeId = fuliaoInOutNoticeService.update(fuliaoInOutNotice);
				return this.returnSuccess("id", fuliaoInOutNoticeId);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FuliaoInNotice fuliaoInOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料预入库通知单的权限", null);
		}
		try {	
			fuliaoInOutNotice.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoInOutNotice.setUpdated_at(DateTool.now());// 设置更新时间
			fuliaoInOutNotice.setCreated_user(user.getId());// 设置创建人
			
			List<FuliaoInNoticeDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoInNoticeDetail.class);
			Iterator<FuliaoInNoticeDetail> iter = detaillist.iterator();
			while(iter.hasNext()){
				FuliaoInNoticeDetail detail = iter.next();
				if(detail.getQuantity() == 0){
					iter.remove();
				}
			}
			if(detaillist==null || detaillist.size()<=0){
				throw new Exception("请至少填写一条入库明细");
			}
			fuliaoInOutNotice.setDetaillist(detaillist);
			if(fuliaoInOutNotice.getId() == 0){//添加
				Order order = orderService.get(fuliaoInOutNotice.getOrderId());
				fuliaoInOutNotice.setName(order.getName());
				fuliaoInOutNotice.setCompany_productNumber(order.getCompany_productNumber());
				fuliaoInOutNotice.setOrderNumber(order.getOrderNumber());
				fuliaoInOutNotice.setCharge_employee(order.getCharge_employee());
				Integer tableOrderId = fuliaoInOutNoticeService.add(fuliaoInOutNotice);
				return this.returnSuccess("id", tableOrderId);
			}else{//编辑
				Integer fuliaoInOutNoticeId = fuliaoInOutNoticeService.update(fuliaoInOutNotice);
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
			throw new PermissionDeniedDataAccessException("没有删除辅料预入库通知单的权限", null);
		}
		int success = fuliaoInOutNoticeService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FuliaoInNotice get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "fuliaoinout_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看辅料预入库通知单详情的权限", null);
		}
		FuliaoInNotice fuliaoInOutNotice = fuliaoInOutNoticeService.get(id);
		return fuliaoInOutNotice;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料预入库通知单ID");
		}
		String lcode = "fuliaoinout_notice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料预入库通知单详情的权限", null);
		}	
		FuliaoInNotice fuliaoInNotice = fuliaoInOutNoticeService.getAndDetail(id);
		request.setAttribute("fuliaoInNotice", fuliaoInNotice);
		if(fuliaoInNotice.getOrderId()!=null && fuliaoInNotice.getOrderId()!=0){
			Order order = orderService.get(fuliaoInNotice.getOrderId());
			request.setAttribute("order", order);
			return new ModelAndView("fuliaoin_notice/detail");
		}else{
			return new ModelAndView("fuliaoin_notice/detail_common");
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
				FuliaoInNotice fuliaoInNotice = fuliaoInOutNoticeService.getAndDetail(fuliaoInOutNoticeId);
				request.setAttribute("fuliaoInNotice", fuliaoInNotice);
				if(fuliaoInNotice.getOrderId()!=null && fuliaoInNotice.getOrderId()!=0){
					Order order = orderService.get(fuliaoInNotice.getOrderId());
					request.setAttribute("order", order);
					return new ModelAndView("fuliaoin_notice/edit");
				}else{
					return new ModelAndView("fuliaoin_notice/edit_common");
				}
			}
			throw new Exception("缺少辅料预入库通知单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FuliaoInNotice fuliaoInOutNotice, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliaoinout_notice/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑辅料预入库通知单的权限", null);
		}
		fuliaoInOutNotice.setUpdated_at(DateTool.now());
		List<FuliaoInNoticeDetail> detaillist = SerializeTool
				.deserializeList(details,
						FuliaoInNoticeDetail.class);
		Iterator<FuliaoInNoticeDetail> iter = detaillist.iterator();
		while(iter.hasNext()){
			FuliaoInNoticeDetail detail = iter.next();
			if(detail.getQuantity() == 0){
				iter.remove();
			}
		}
		fuliaoInOutNotice.setDetaillist(detaillist);
		Integer fuliaoInOutNoticeId = fuliaoInOutNoticeService.update(fuliaoInOutNotice);
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
			throw new PermissionDeniedDataAccessException("没有查看打印辅料预入库通知单的权限", null);
		}	
		FuliaoInNotice fuliaoInNotice = fuliaoInOutNoticeService.getAndDetail(id);
		request.setAttribute("fuliaoInNotice", fuliaoInNotice);
		if(fuliaoInNotice.getOrderId()!=null && fuliaoInNotice.getOrderId()!=0){
			Order order = orderService.get(fuliaoInNotice.getOrderId());
			request.setAttribute("order", order);
			return new ModelAndView("fuliaoin_notice/print");
		}else{
			return new ModelAndView("fuliaoin_notice/print_common");
		}
	}
	
//	@RequestMapping(value = "/index", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, Integer factoryId,String number,
//			String sortJSON, HttpSession session, HttpServletRequest request)
//			throws Exception {
//
//		String lcode = "material_purchase_order/index";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看原材料采购单列表的权限", null);
//		}
//
//		Date start_time_d = DateTool.parse(start_time);
//		Date end_time_d = DateTool.parse(end_time);
//		Pager pager = new Pager();
//		if (page != null && page > 0) {
//			pager.setPageNo(page);
//		}
//
//		List<Sort> sortList = null;
//		if (sortJSON != null) {
//			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
//		}
//		if (sortList == null) {
//			sortList = new ArrayList<Sort>();
//		}
//		Sort sort = new Sort();
//		sort.setDirection("desc");
//		sort.setProperty("created_at");
//		sortList.add(sort);
//		pager = materialPurchaseOrderService.getList(pager, start_time_d, end_time_d,companyId,factoryId,number, sortList);
//		if (pager != null & pager.getResult() != null) {
//			List<MaterialPurchaseOrder> orderlist = (List<MaterialPurchaseOrder>) pager.getResult();
//		}
//
//		request.setAttribute("start_time", start_time_d);
//		request.setAttribute("end_time", end_time_d);
//		request.setAttribute("companyId", companyId);
//		request.setAttribute("factoryId", factoryId);
//		request.setAttribute("number", number);
//		request.setAttribute("pager", pager);
//		return new ModelAndView("material_purchase_order/index");
//	}
	
}
