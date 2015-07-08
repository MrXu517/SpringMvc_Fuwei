package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.fuwei.constant.Constants;
import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.ProductionNotification;
import com.fuwei.entity.ProductionNotificationDetail;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.User;
import com.fuwei.print.PrintExcel;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderDetailService;
import com.fuwei.service.OrderService;
import com.fuwei.service.ProductionNotificationService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/notification")
@Controller
public class ProductionNotificationController extends BaseController{
	@Autowired
	ProductionNotificationService productionNotificationService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	OrderService orderService;
//	@Autowired
//	OrderDetailService orderDetailService;
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(Integer orderDetailId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(orderDetailId == null){
			throw new Exception("缺少订单样品ID");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		ProductionNotification ProductionNotification = new ProductionNotification();
		try {
//			Map<String,Object> orderDetail = orderDetailService.getDetail(orderDetailId);
			ProductionNotification.setCreated_at(DateTool.now());//设置创建时间
			ProductionNotification.setUpdated_at(DateTool.now());//设置更新时间
			ProductionNotification.setCreated_user(user.getId());//设置创建人
			ProductionNotification.setQuantity(0);
			ProductionNotification.setOrderDetailId(orderDetailId);
			
			List<ProductionNotificationDetail> Detaillist = new ArrayList<ProductionNotificationDetail>();
			ProductionNotification.setDetaillist(Detaillist);//设置详情
//			request.setAttribute("orderDetail", orderDetail);
			request.setAttribute("productionNotification", ProductionNotification);
			return new ModelAndView("notification/add");
		} catch (Exception e) {
			throw e;
		}
	}

	//创建生产单
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(ProductionNotification productionNotification, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Integer orderDetailId = productionNotification.getOrderDetailId();
		if(orderDetailId == null){
			throw new Exception("缺少订单详情ID");
		}
		String lcode = "notification/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有创建生产单的权限", null);
		}
		//判断订单当前状态：在 打确认样 和打产前样 阶段 不能创建生产通知单
		Order order = orderService.getByDetailId(orderDetailId);
//		2014-11-10 去掉下面三行，因为没有了打确认样和产前样两个阶段
//		if(order.getStatus() <= OrderStatus.BEFOREPRODUCESAMPLE.ordinal()){
//			throw new Exception("在打确认样 和打产前样阶段 不能创建生产通知单");
//		}

		//创建生产单
		productionNotification.setCreated_at(DateTool.now());
		productionNotification.setUpdated_at(DateTool.now());
		productionNotification.setCreated_user(user.getId());
		List<ProductionNotificationDetail> detaillist = SerializeTool.deserializeList(productionNotification.getDetails(), ProductionNotificationDetail.class);
		int quantity = 0 ;
		for(ProductionNotificationDetail detail : detaillist){
			quantity += detail.getQuantity();
		}
		productionNotification.setQuantity(quantity);
		orderService.addNotification(productionNotification);
		
		return this.returnSuccess();		
	}
	
	//打印生产通知单
	@RequestMapping(value="/print/{orderDetailId}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> printNotification(@PathVariable Integer orderDetailId,HttpSession session,HttpServletRequest request) throws Exception{
		String lcode = "notification/print";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有打印生产通知单的权限", null);
		}
		
		List<ProductionNotification> productionNotificationList = productionNotificationService.getByOrderDetailId(orderDetailId);
		if(productionNotificationList == null || productionNotificationList.size()==0){
			throw new Exception("此订单样品还未生成生产通知单");
		}
		
		String excelfile_name = Constants.UPLOADEXCEL_Order_temp + "生产通知单" + orderDetailId + "_"
		+ DateTool.formateDate(new Date(), "yyyyMMddHHmmss") + ".xls";
		String uploadSite = Constants.UPLOADSite;

//		ExportExcel.exportSampleDetailExcel(sample,quotePrice,uploadSite,excelfile_name,uploadSite );
		PrintExcel.printExcel(uploadSite + excelfile_name, true);
		return this.returnSuccess();
	}
	
	@RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id,HttpSession session, HttpServletRequest request) throws Exception{
		String lcode = "notification/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看生产通知单详情的权限", null);
		}
		
		if(id == null){
			throw new Exception("缺少生产通知单ID");
		}
		ProductionNotification productionNotification = productionNotificationService.get(id);	
		List<ProductionNotificationDetail> detaillist = SerializeTool.deserializeList(productionNotification.getDetails(),ProductionNotificationDetail.class); 
		productionNotification.setDetaillist(detaillist);//设置生产单详情列表
		request.setAttribute("productionNotification", productionNotification);
		return new ModelAndView("notification/detail");
	}
}
