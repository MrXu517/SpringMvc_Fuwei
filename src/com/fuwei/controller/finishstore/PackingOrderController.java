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
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/packing_order")
@Controller
public class PackingOrderController extends BaseController {
	@Autowired
	OrderService orderService;
	@Autowired
	PackingOrderService packingOrderService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/list/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer orderId ,HttpSession session, HttpServletRequest request)
			throws Exception {
		if(orderId == null){
			throw new Exception("订单号不能为空");
		}
		request.setAttribute("orderId", orderId);
		List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
		request.setAttribute("packingOrderList", packingOrderList);
		return new ModelAndView("packing_order/order");
		
	}
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, String orderNumber,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "packing_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看装箱单列表的权限", null);
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
		pager = packingOrderService.getList(pager, start_time_d, end_time_d,companyId,orderNumber, sortList);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("orderNumber", orderNumber);//订单号
		request.setAttribute("pager", pager);
		return new ModelAndView("packing_order/index");
	}
	
	
	
	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
		}
		try {
			if(orderId!=null){
				Order order = orderService.get(orderId);
				request.setAttribute("order", order);
				return new ModelAndView("packing_order/add");
			}
			throw new Exception("缺少订单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(PackingOrder packingOrder,String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
		}
		try {	
			if (packingOrder.getOrderId() == null
					|| packingOrder.getOrderId() == 0) {
				throw new PermissionDeniedDataAccessException(
						"装箱单必须属于一张订单", null);
			}
			Order order = orderService.get(packingOrder.getOrderId());
			
			if(order == null){
				throw new PermissionDeniedDataAccessException(
						"订单不存在", null);
			}
			packingOrder.setCreated_at(DateTool.now());// 设置创建时间
			packingOrder.setUpdated_at(DateTool.now());// 设置更新时间
			packingOrder.setCreated_user(user.getId());// 设置创建人
			
			packingOrder.setCharge_employee(order.getCharge_employee());
			packingOrder.setCompany_productNumber(order.getCompany_productNumber());
			packingOrder.setCompanyId(order.getCompanyId());
			packingOrder.setName(order.getName());
			packingOrder.setOrderNumber(order.getOrderNumber());
			
			//判断是否有数量为0的明细项 、 设置总数量、总箱数、总立方
			int total_quantity = 0;
			int total_cartons = 0 ;
			double total_capacity = 0.0;
			List<PackingOrderDetail> detaillist = SerializeTool
			.deserializeList(details, PackingOrderDetail.class);
			Iterator<PackingOrderDetail> iter = detaillist.iterator();  
			while(iter.hasNext()){  
				PackingOrderDetail detail = iter.next();  
			    int quantity = detail.getQuantity();
			    int per_carton_quantity = detail.getPer_carton_quantity();
			    if(quantity == 0){  
			        iter.remove();  
			    } 
			    if(per_carton_quantity ==0){
			    	iter.remove(); 
			    }
			    int cartons = quantity%per_carton_quantity==0?quantity/per_carton_quantity:quantity/per_carton_quantity+1;;  
				double L = detail.getBox_L();
				double W = detail.getBox_W();
				double H = detail.getBox_H();
				double capacity = L/100 * W/100 * H/100 * cartons;
				capacity = NumberUtil.formateDouble(capacity, 2);
				detail.setCapacity(capacity);
			    detail.setCartons(cartons);
			    total_quantity += quantity;
				total_cartons += cartons;
				total_capacity += capacity;
			    	
			}  
			if(detaillist.size() <=0){
				throw new Exception("本次装箱数量均为0，无法创建装箱单");
			}
			//判断是否有数量为0的明细项			
			packingOrder.setDetaillist(detaillist);
    		// 设置总数量、总箱数、总立方
			total_capacity = NumberUtil.formateDouble(total_capacity, 2);
			packingOrder.setCapacity(total_capacity);
			packingOrder.setCartons(total_cartons);
			packingOrder.setQuantity(total_quantity);
			
			
			
			Integer tableOrderId = packingOrderService.add(packingOrder);
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
		String lcode = "packing_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除装箱单的权限", null);
		}
		int success = packingOrderService.remove(id);	
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public PackingOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "packing_order/get";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
		}
		PackingOrder packingOrder = packingOrderService.get(id);
		return packingOrder;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		
		if (id == null) {
			throw new Exception("缺少装箱单ID");
		}		
		String lcode = "packing_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
		}
		
		PackingOrder packingOrder = packingOrderService.getAndDetail(id);
		Order order = orderService.get(packingOrder.getOrderId());
		request.setAttribute("packingOrder", packingOrder);
		request.setAttribute("order", order);
		return new ModelAndView("packing_order/detail");
	}
	
	@RequestMapping(value = "/put/{tableOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer tableOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
		}
		try {
			if(tableOrderId!=null){
				PackingOrder packingOrder = packingOrderService.getAndDetail(tableOrderId);
				request.setAttribute("packingOrder", packingOrder);
				return new ModelAndView("packing_order/edit");			
			}
			throw new Exception("缺少装箱单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(PackingOrder packingOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑装箱单的权限", null);
		}
		packingOrder.setUpdated_at(DateTool.now());
		//判断是否有数量为0的明细项 、 设置总数量、总箱数、总立方
		int total_quantity = 0;
		int total_cartons = 0 ;
		double total_capacity = 0.0;
		List<PackingOrderDetail> detaillist = SerializeTool
		.deserializeList(details, PackingOrderDetail.class);
		Iterator<PackingOrderDetail> iter = detaillist.iterator();  
		while(iter.hasNext()){  
			PackingOrderDetail detail = iter.next();  
		    int quantity = detail.getQuantity();
		    int per_carton_quantity = detail.getPer_carton_quantity();
		    if(quantity == 0){  
		        iter.remove();  
		    } 
		    if(per_carton_quantity ==0){
		    	iter.remove(); 
		    }
		    int cartons = quantity%per_carton_quantity==0?quantity/per_carton_quantity:quantity/per_carton_quantity+1;;  
			double L = detail.getBox_L();
			double W = detail.getBox_W();
			double H = detail.getBox_H();
			double capacity = L/100 * W/100 * H/100 * cartons;
			capacity = NumberUtil.formateDouble(capacity, 2);
			detail.setCapacity(capacity);
		    detail.setCartons(cartons);
		    total_quantity += quantity;
			total_cartons += cartons;
			total_capacity += capacity;
		}   
		if(detaillist.size() <=0){
			throw new Exception("本次装箱数量均为0，无法创建装箱单");
		}
		//判断是否有数量为0的明细项			
		packingOrder.setDetaillist(detaillist);
		// 设置总数量、总箱数、总立方
		total_capacity = NumberUtil.formateDouble(total_capacity, 2);
		packingOrder.setCapacity(total_capacity);
		packingOrder.setCartons(total_cartons);
		packingOrder.setQuantity(total_quantity);
		Integer tableOrderId = packingOrderService.update(packingOrder);
		return this.returnSuccess("id", tableOrderId);
		
	}
}



//package com.fuwei.controller.finishstore;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import net.sf.json.JSONObject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.PermissionDeniedDataAccessException;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.fuwei.commons.Pager;
//import com.fuwei.commons.Sort;
//import com.fuwei.commons.SystemCache;
//import com.fuwei.commons.SystemContextUtils;
//import com.fuwei.constant.Constants;
//import com.fuwei.controller.BaseController;
//import com.fuwei.entity.Order;
//import com.fuwei.entity.User;
//import com.fuwei.entity.finishstore.PackingOrder;
//import com.fuwei.service.AuthorityService;
//import com.fuwei.service.OrderService;
//import com.fuwei.service.finishstore.PackingOrderService;
//import com.fuwei.util.Any2PDFUtil;
//import com.fuwei.util.DateTool;
//import com.fuwei.util.SerializeTool;
//
//@RequestMapping("/packing_order")
//@Controller
//public class PackingOrderController extends BaseController {
//	@Autowired
//	OrderService orderService;
//	@Autowired
//	PackingOrderService packingOrderService;
//	@Autowired
//	AuthorityService authorityService;
//	
//	@RequestMapping(value = "/list/{orderId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView listbyorder(@PathVariable Integer orderId ,HttpSession session, HttpServletRequest request)
//			throws Exception {
//		if(orderId == null){
//			throw new Exception("订单号不能为空");
//		}
//		request.setAttribute("orderId", orderId);
//		List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
//		request.setAttribute("packingOrderList", packingOrderList);
//		return new ModelAndView("packing_order/order");
//		
//	}
//	@RequestMapping(value = "/index", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, String orderNumber,
//			String sortJSON, HttpSession session, HttpServletRequest request)
//			throws Exception {
//
//		String lcode = "packing_order/index";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看装箱单列表的权限", null);
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
//		pager = packingOrderService.getList(pager, start_time_d, end_time_d,companyId,orderNumber, sortList);
//		if (pager != null & pager.getResult() != null) {
//			List<PackingOrder> orderlist = (List<PackingOrder>) pager.getResult();
//		}
//
//		request.setAttribute("start_time", start_time_d);
//		request.setAttribute("end_time", end_time_d);
//		request.setAttribute("companyId", companyId);
//		request.setAttribute("orderNumber", orderNumber);//订单号
//		request.setAttribute("pager", pager);
//		return new ModelAndView("packing_order/index");
//	}
//	
//	
//	
//	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView add(@PathVariable Integer orderId,
//			HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/add";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
//		}
//		try {
//			if(orderId!=null){
//				Order order = orderService.get(orderId);
//				request.setAttribute("order", order);
//				return new ModelAndView("packing_order/add");
//			}
//			throw new Exception("缺少订单ID");
//			
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> add(PackingOrder packingOrder,@RequestParam("file") CommonsMultipartFile file,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/add";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
//		}
//		try {	
//			if (packingOrder.getOrderId() == null
//					|| packingOrder.getOrderId() == 0) {
//				throw new PermissionDeniedDataAccessException(
//						"装箱单必须属于一张订单", null);
//			}
//			Order order = orderService.get(packingOrder.getOrderId());
//			
//			if(order == null){
//				throw new PermissionDeniedDataAccessException(
//						"订单不存在", null);
//			}
//			packingOrder.setCreated_at(DateTool.now());// 设置创建时间
//			packingOrder.setUpdated_at(DateTool.now());// 设置更新时间
//			packingOrder.setCreated_user(user.getId());// 设置创建人
//			
//			//上传图片
//			JSONObject jObject = fileUpload(order.getId(), request, file);;
//			packingOrder.setFilepath((String)jObject.get("excel"));
//			packingOrder.setPdfpath((String)jObject.get("pdf"));
//    		
//			Integer tableOrderId = packingOrderService.add(packingOrder);
//			return this.returnSuccess("id", tableOrderId);
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
//	
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/delete";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有删除装箱单的权限", null);
//		}
//		
//		//删除原先的EXCEL和PDF文件
//		PackingOrder packingOrder = packingOrderService.get(id);
//		String filepath = Constants.UPLOADSite + packingOrder.getFilepath();
//		File file = new File(filepath);
//		if(file.exists()){
//		    file.delete();
//		}
//		
//		String pdfpath = Constants.UPLOADSite + packingOrder.getPdfpath();
//		File pdffile = new File(pdfpath);
//		if(pdffile.exists()){
//		    pdffile.delete();
//		}
//		
//		//再删除数据库信息
//		int success = packingOrderService.remove(id);
//		
//		
//		
//		return this.returnSuccess();
//		
//	}
//	
//	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public PackingOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		String lcode = "packing_order/get";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
//		}
//		PackingOrder packingOrder = packingOrderService.get(id);
//		return packingOrder;
//	}
//	
//	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
//			HttpServletRequest request) throws Exception {
//		
//		if (id == null) {
//			throw new Exception("缺少装箱单ID");
//		}		
//		String lcode = "packing_order/detail";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
//		}
//		
//		PackingOrder packingOrder = packingOrderService.get(id);
//		Order order = orderService.get(packingOrder.getOrderId());
//		request.setAttribute("packingOrder", packingOrder);
//		request.setAttribute("order", order);
//		return new ModelAndView("packing_order/detail");
//	}
//	
//	@RequestMapping(value = "/put/{tableOrderId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView update(@PathVariable Integer tableOrderId,
//			HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
//		}
//		try {
//			if(tableOrderId!=null){
//				PackingOrder packingOrder = packingOrderService.get(tableOrderId);
//				request.setAttribute("packingOrder", packingOrder);
//				return new ModelAndView("packing_order/edit");			
//			}
//			throw new Exception("缺少装箱单ID");
//			
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	@RequestMapping(value = "/put", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> update(PackingOrder packingOrder, String details,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有编辑装箱单的权限", null);
//		}
//		if(packingOrder.getOrderId() == null){
//			throw new PermissionDeniedDataAccessException(
//					"订单不存在", null);
//		}
//		packingOrder.setUpdated_at(DateTool.now());
//		//修改filepath 与 pdfpath
//		//上传图片
//		// 转型为MultipartHttpRequest  
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
//        // 获得上传的文件（根据前台的name名称得到上传的文件）  
//        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();  
//        List<MultipartFile> file = multiValueMap.get("file"); 
//        if(file!=null && !file.isEmpty()){  
//        	JSONObject jObject = fileUpload(packingOrder.getOrderId(), request, (CommonsMultipartFile)file.get(0));;
//    		packingOrder.setFilepath((String)jObject.get("excel"));
//    		packingOrder.setPdfpath((String)jObject.get("pdf"));
//        }
//		
//		
//		Integer tableOrderId = packingOrderService.update(packingOrder);
//		return this.returnSuccess("id", tableOrderId);
//		
//	}
//	
//	//预览EXCEL文件转化为PDF
//	@RequestMapping(value = "/preview", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> preview(@RequestParam("file") CommonsMultipartFile file,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		try {	
//			
//			//上传文件
//			JSONObject jObject = fileUpload(0, request, file);;
//			String excelpath = Constants.UPLOADSite + (String)jObject.get("excel");
//			String pdfpath = Constants.UPLOADSite + (String)jObject.get("pdf");
//			
//			
//			
//			// 设置response参数，可以打开下载页面
//	        response.reset();
//	        response.setContentType("application/pdf;charset=utf-8");
////	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
//	        BufferedInputStream bis = null;
//	        BufferedOutputStream bos = null;
//	        try {
//	            bis = new BufferedInputStream(new FileInputStream(new File(pdfpath)));
//	            bos = new BufferedOutputStream(response.getOutputStream());
//	            byte[] buff = new byte[2048];
//	            int bytesRead;
//	            // Simple read/write loop.
//	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//	                bos.write(buff, 0, bytesRead);
//	            }
//	           
//	        } catch (final IOException e) {
//	            throw e;
//	        } finally {
//	            if (bis != null)
//	                bis.close();
//	            if (bos != null)
//	                bos.close();
//	            //删除文件
//				File excelfile = new File(excelpath);
//				if(excelfile.exists()){
//					excelfile.delete();
//				}
//				File pdffile = new File(pdfpath);
//				if(pdffile.exists()){
//				    pdffile.delete();
//				}
//	        }
//	        
//	      
//	        return this.returnSuccess();
//	        
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
//	
//	public JSONObject fileUpload(int orderId , HttpServletRequest request , CommonsMultipartFile file) throws Exception {
//    	String nameString = file.getOriginalFilename();
//    	if(nameString.lastIndexOf(".") == -1 || nameString.lastIndexOf(".") == 0){
//    		throw new Exception("请上传有效的EXCEL文件，包括 以.xls,.xlsx为扩展名的文件");
//    	}
//    	else{
//    		String extString = nameString.substring(nameString.lastIndexOf(".")+1,nameString.length());
//    		extString = extString.toLowerCase();
//    		if(!extString.equals("xls")  && !extString.equals("xlsx")){
//    			throw new Exception("请上传有效的EXCEL文件，包括 以.xls,.xlsx为扩展名的文件");
//    		}
//    	}
//        long  startTime=System.currentTimeMillis();
//        String fileName = "orderId=" + orderId + "_" + DateTool.formatDateYMD(DateTool.now(),"-") +"_" + new Date().getTime();
//        String path = Constants.UPLOADEXCEL_Packing + fileName + ".xls";
//        
//       
//        java.io.File pathFile=new java.io.File(Constants.UPLOADSite + Constants.UPLOADEXCEL_Packing);
//        if(!pathFile.exists()){
//        	pathFile.mkdirs();
//        }
//       
//        java.io.File newFile=new java.io.File(Constants.UPLOADSite + path);
//        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
//        file.transferTo(newFile);
//        
//        long  endTime=System.currentTimeMillis();
//        
//        //上传excel后，上传pdf
//        java.io.File upload_pdf_pathFile=new java.io.File(Constants.UPLOADSite + Constants.UPLOADPDF_Packing);
//        if(!upload_pdf_pathFile.exists()){
//        	upload_pdf_pathFile.mkdirs();
//        }
//        String upload_pdf_full_path = Constants.UPLOADSite + Constants.UPLOADPDF_Packing + fileName + ".pdf";
//        Any2PDFUtil.convert2PDF(Constants.UPLOADSite + path, upload_pdf_full_path);
//      
//		
//		JSONObject jObject = new JSONObject();
//		jObject.put("excel",Constants.UPLOADEXCEL_Packing + fileName +".xls");
//		jObject.put("pdf", Constants.UPLOADPDF_Packing + fileName + ".pdf");
//		
//        return jObject;  
//    }
//	
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	@ResponseBody
//	public void test(HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		//读取excel文件
//		try{
//		File file =new File("C:\\Users\\Administrator\\Desktop\\张明霞正唛1.xls");
//		
//		Any2PDFUtil.convert2PDF("C:\\Users\\Administrator\\Desktop\\张明霞正唛1.xls", "C:\\Users\\Administrator\\Desktop\\text2.pdf");
//		}catch(Exception e){
//			throw e;
////			e.printStackTrace();
//		}
//		//		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
//		
////		// 设置response参数，可以打开下载页面
////        response.reset();
////        response.setContentType("application/pdf;charset=utf-8");
////        response.setHeader("Content-Disposition", "attachment;filename="+ new String(("test.pdf").getBytes(), "iso-8859-1"));
////		
////        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
////		byte[] buff = new byte[2048];
////        int bytesRead;
////        // Simple read/write loop.
////        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
////            bos.write(buff, 0, bytesRead);
////        }
////        bis.close();
////        bos.close();
//	}
//    
//}
