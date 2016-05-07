package com.fuwei.controller.ordergrid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.constant.ERROR;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Factory;
import com.fuwei.entity.FuliaoType;
import com.fuwei.entity.Material;
import com.fuwei.entity.Order;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.FactoryService;
import com.fuwei.service.FuliaoTypeService;
import com.fuwei.service.MaterialService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderDetailService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/fuliao_purchase_order")
@Controller
public class FuliaoPurchaseOrderController extends BaseController {
	
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	@Autowired
	FuliaoPurchaseOrderDetailService fuliaoPurchaseOrderDetailService;
	@Autowired
	OrderService orderService;
	@Autowired
	SampleService sampleService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	MaterialService materialService;
	@Autowired
	FuliaoTypeService fuliaoTypeService;
	@Autowired
	FactoryService factoryService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, Integer factoryId,String number,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "fuliao_purchase_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料采购单列表的权限", null);
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
		pager = fuliaoPurchaseOrderService.getList(pager, start_time_d, end_time_d,companyId,factoryId,number, sortList);
		if (pager != null & pager.getResult() != null) {
			List<FuliaoPurchaseOrder> orderlist = (List<FuliaoPurchaseOrder>) pager.getResult();
		}

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliao_purchase_order/index");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addproducingorder(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料采购单的权限", null);
		}
		try {
			return new ModelAndView("fuliao_purchase_order/add");	
			
		} catch (Exception e) {
			throw e;
		}
	}
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("fuliao_purchase_order/scan");	
	}
	
	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addproducingorder(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料采购单的权限", null);
		}
		try {
			if(orderId!=null){
				Order order = orderService.get(orderId);
				request.setAttribute("order", order);
				return new ModelAndView("fuliao_purchase_order/addbyorder");
			}
			throw new Exception("缺少订单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/addbyorder", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addproducingorder(String orderNumber,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加辅料采购单的权限", null);
		}
		try {
			if(orderNumber!=null){
				Order order = orderService.get(orderNumber);
				request.setAttribute("order", order);
				return new ModelAndView("fuliao_purchase_order/addbyorder");
			}
			throw new Exception("缺少订单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(FuliaoPurchaseOrder fuliaoPurchaseOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加辅料采购单的权限", null);
		}
		try {	
			
			fuliaoPurchaseOrder.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoPurchaseOrder.setUpdated_at(DateTool.now());// 设置更新时间
			fuliaoPurchaseOrder.setCreated_user(user.getId());// 设置创建人
			Integer sampleId = fuliaoPurchaseOrder.getSampleId();
			if(sampleId != null){
				Sample sample = sampleService.get(sampleId);
				fuliaoPurchaseOrder.setImg(sample.getImg());
				fuliaoPurchaseOrder.setProductNumber(sample.getProductNumber());
				fuliaoPurchaseOrder.setMaterialId(sample.getMaterialId());
				fuliaoPurchaseOrder.setSize(sample.getSize());
				fuliaoPurchaseOrder.setWeight(sample.getWeight());
				fuliaoPurchaseOrder.setName(sample.getName());
				fuliaoPurchaseOrder.setImg_s(sample.getImg_s());
				fuliaoPurchaseOrder.setImg_ss(sample.getImg_ss());
			}
			List<FuliaoPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoPurchaseOrderDetail.class);
			if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
				throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
			}
			fuliaoPurchaseOrder.setDetaillist(detaillist);
			Integer tableOrderId = fuliaoPurchaseOrderService.add(fuliaoPurchaseOrder);
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	// 添加或保存原材料采购单
	@RequestMapping(value = "/addbyorder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(
			FuliaoPurchaseOrder tableOrder, String details,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "order/fuliaopurchase";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑辅料采购单的权限",
					null);
		}
		try {
			Integer tableOrderId = tableOrder.getId();

			if (tableOrderId == null || tableOrderId == 0) {
				// 添加
				if (tableOrder.getOrderId() == null
						|| tableOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"辅料采购单必须属于一张订单", null);
				}
				Order order = orderService.get(tableOrder.getOrderId());
				if(order == null){
					throw new PermissionDeniedDataAccessException(
							"订单不存在", null);
				}
				
				tableOrder.setCreated_at(DateTool.now());// 设置创建时间
				tableOrder.setUpdated_at(DateTool.now());// 设置更新时间
				tableOrder.setCreated_user(user.getId());// 设置创建人
				
				tableOrder.setImg(order.getImg());
				tableOrder.setImg_s(order.getImg_s());
				tableOrder.setImg_ss(order.getImg_ss());
				tableOrder.setProductNumber(order.getProductNumber());
				tableOrder.setMaterialId(order.getMaterialId());
				tableOrder.setSize(order.getSize());
				tableOrder.setWeight(order.getWeight());
				tableOrder.setName(order.getName());
				tableOrder.setCompanyId(order.getCompanyId());
				tableOrder.setCustomerId(order.getCustomerId());
				tableOrder.setSampleId(order.getSampleId());
				tableOrder.setOrderNumber(order.getOrderNumber());
				tableOrder.setCharge_employee(order.getCharge_employee());//2015/3/17 添加跟单人
				
				List<FuliaoPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoPurchaseOrderDetail.class);
				if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
					throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
				}
				tableOrder.setDetaillist(detaillist);
				tableOrderId = fuliaoPurchaseOrderService.add(tableOrder);
			} else {// 编辑
				if (tableOrder.getOrderId() == null
						|| tableOrder.getOrderId() == 0) {
					throw new PermissionDeniedDataAccessException(
							"缺少订单ID", null);
				}
				tableOrder.setUpdated_at(DateTool.now());
				List<FuliaoPurchaseOrderDetail> detaillist = SerializeTool
						.deserializeList(details,
								FuliaoPurchaseOrderDetail.class);
				if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
					throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
				}
				tableOrder.setDetaillist(detaillist);
				tableOrderId = fuliaoPurchaseOrderService.update(tableOrder);
			}
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
		String lcode = "fuliao_purchase_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除辅料采购单的权限", null);
		}
		int success = fuliaoPurchaseOrderService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FuliaoPurchaseOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "fuliao_purchase_order/get";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看辅料采购单详情的权限", null);
		}
		FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(id);
		return fuliaoPurchaseOrder;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料采购单ID");
		}
		String lcode = "fuliao_purchase_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料采购单详情的权限", null);
		}	
		FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(id);
		
		List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = new ArrayList<FuliaoPurchaseOrder>();
		fuliaoPurchaseOrderList.add(fuliaoPurchaseOrder);
		request.setAttribute("fuliaoPurchaseOrderList", fuliaoPurchaseOrderList);
		Map<String,Object> data = new HashMap<String,Object>();  
	    data.put("gridName","fuliaopurchaseorder");  
		return new ModelAndView("printorder/preview",data);
	}
	
	@RequestMapping(value = "/put/{tableOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer tableOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加原材料采购单的权限", null);
		}
		try {
			if(tableOrderId!=null){
				FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(tableOrderId);
				request.setAttribute("fuliaoPurchaseOrder", fuliaoPurchaseOrder);
				if(fuliaoPurchaseOrder.getOrderId()!=null){
					return new ModelAndView("fuliao_purchase_order/editbyorder");
				}else{
					return new ModelAndView("fuliao_purchase_order/edit");
				}
				
			}
			throw new Exception("缺少原材料采购单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(FuliaoPurchaseOrder fuliaoPurchaseOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_purchase_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑辅料采购单的权限", null);
		}
		fuliaoPurchaseOrder.setUpdated_at(DateTool.now());
		List<FuliaoPurchaseOrderDetail> detaillist = SerializeTool
				.deserializeList(details,
						FuliaoPurchaseOrderDetail.class);
		if(detaillist.size() >Constants.MAX_DETAIL_LENGTH ){
			throw new Exception(ERROR.MAX_DETAIL_LENGTH_ERROR);
		}
		fuliaoPurchaseOrder.setDetaillist(detaillist);
		Integer tableOrderId = fuliaoPurchaseOrderService.update(fuliaoPurchaseOrder);
		return this.returnSuccess("id", tableOrderId);
		
	}
	
	@RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料采购单ID");
		}
		String lcode = "fuliao_purchase_order/detail";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看辅料采购单详情的权限", null);
//		}	
		FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(id);
		
		List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = new ArrayList<FuliaoPurchaseOrder>();
		fuliaoPurchaseOrderList.add(fuliaoPurchaseOrder);
		request.setAttribute("fuliaoPurchaseOrderList", fuliaoPurchaseOrderList);
		Map<String,Object> data = new HashMap<String,Object>();  
	    data.put("gridName","fuliaopurchaseorder");  
		return new ModelAndView("printorder/print",data);
	}
	
	//特殊用法，只在2016-4-24使用，其他时候请不要使用该方法
	@RequestMapping(value = "/setdetail", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public Map<String, Object> setdetail() throws Exception{
		List<FuliaoPurchaseOrder> list = fuliaoPurchaseOrderService.getAll();
		for(FuliaoPurchaseOrder item : list){
			List<FuliaoPurchaseOrderDetail> itemDetaillist = item.getDetaillist();
			List<FuliaoPurchaseOrderDetail> dbdetaillist = fuliaoPurchaseOrderDetailService.getList(item.getId());
			if(dbdetaillist!=null){//若数据库里没有
				for(FuliaoPurchaseOrderDetail detail : item.getDetaillist()){
					detail.setLocation_size(1);
				}
//				fuliaoPurchaseOrderDetailService.addBatch(itemDetaillist);
//				List<FuliaoPurchaseOrderDetail> detaillist = fuliaoPurchaseOrderDetailService.getList(temp.getId());
//				item.setDetail_json(SerializeTool.serialize(detaillist));
				fuliaoPurchaseOrderService.update(item);
			}
		}
		return this.returnSuccess();
		
	}
	
//	@RequestMapping(value = "/setwrong", method = RequestMethod.GET)
//	@ResponseBody
//	@Transactional
//	public Map<String,Object> setWrongFuliaoOrder(String details,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		
//		try {	
//			
//			List<FuliaoPurchaseOrder> list = fuliaoPurchaseOrderService.getListFactory1();
//			for(FuliaoPurchaseOrder item : list){
//				int factoryId = item.getFactoryId();
//				String factoryName = SystemCache.getFactoryName(factoryId);
//				Factory factory = factoryService.getByNameTYPE3(factoryName);
//				Factory factory2 = factoryService.getByNameTYPE3(factoryName + "_辅料");
//				if(factory == null && factory2 == null){
//					factory = new Factory();
//					factory.setType(3);
//					factory.setName(factoryName + "_辅料");
//					factory.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(factory
//							.getName()));
//					factory.setCreated_at(DateTool.now());
//					factory.setUpdated_at(DateTool.now());
//					factory.setCreated_user(user.getId());
//					int newfactoryId = factoryService.add(factory);
//					item.setFactoryId(newfactoryId);
//				}else{
//					if(factory!=null){
//						item.setFactoryId(factory.getId());
//					}else{
//						item.setFactoryId(factory2.getId());
//						
//					}
//				}
//				
//				
//				List<FuliaoPurchaseOrderDetail> detaillist = item.getDetaillist();
//				for(FuliaoPurchaseOrderDetail detail : detaillist){
//					int styleId = detail.getStyle();
//					//将此style 材料ID设为对应的辅料类型Id
//					Material material = materialService.get(styleId);
//					FuliaoType fuliaoType = fuliaoTypeService.getName(material.getName());
//					if(fuliaoType == null){//则自动添加一个
//						fuliaoType = new FuliaoType();
//						fuliaoType.setCreated_at(DateTool.now());
//						fuliaoType.setUpdated_at(DateTool.now());
//						fuliaoType.setCreated_user(user.getId());
//						fuliaoType.setName(material.getName());
//						int fuliaoTypeId = fuliaoTypeService.add(fuliaoType);
//						detail.setStyle(fuliaoTypeId);
//					}else{
//						detail.setStyle(fuliaoType.getId());
//					}
//				}
//				
//				fuliaoPurchaseOrderService.update(item);
//			}
//			SystemCache.initFactoryList();
//			SystemCache.initFuliaoTypeList();
//			return this.returnSuccess("result","纠正辅料采购单错误数据成功");
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
}

