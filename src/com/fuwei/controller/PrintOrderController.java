package com.fuwei.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.CheckRecordOrder;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrder;
import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.IroningRecordOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.CarFixRecordOrderService;
import com.fuwei.service.ordergrid.CheckRecordOrderService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.HeadBankOrderService;
import com.fuwei.service.ordergrid.IroningRecordOrderService;
import com.fuwei.service.ordergrid.MaterialPurchaseOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;

@RequestMapping("/printorder")
@Controller
public class PrintOrderController extends BaseController {
	@Autowired
	OrderService orderService;
	
	@Autowired
	HeadBankOrderService headBankOrderService;
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	PlanOrderService planOrderService;
	@Autowired
	StoreOrderService storeOrderService;
	@Autowired
	HalfCheckRecordOrderService halfCheckRecordOrderService;
	@Autowired
	CheckRecordOrderService checkRecordOrderService;
	@Autowired
	ColoringOrderService coloringOrderService;
	@Autowired
	MaterialPurchaseOrderService materialPurchaseOrderService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	@Autowired
	CarFixRecordOrderService carFixRecordOrderService;
	@Autowired
	IroningRecordOrderService ironingRecordOrderService;
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(Integer orderId,String gridName, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try {
			Order order = orderService.get(orderId);
			
			request.setAttribute("order", order);
			if(gridName == null){
				throw new Exception("请输入需要打印的表格名称");
			}
			
			request.setAttribute("gridName", gridName);
			
			//获取头带质量记录单
			if(gridName.indexOf("headbankorder") > -1){
				HeadBankOrder headBankOrder = headBankOrderService.getByOrder(orderId);
				request.setAttribute("headBankOrder", headBankOrder);
			}
			
			//获取生产单
			if(gridName.indexOf("producingorder") > -1){
				ProducingOrder producingOrder = producingOrderService.getByOrder(orderId);		
				request.setAttribute("producingOrder", producingOrder);
			}
			
			
			//获取计划单
			if(gridName.indexOf("planorder") > -1){
				PlanOrder planOrder = planOrderService.getByOrder(orderId);
				request.setAttribute("planOrder", planOrder);
			}
			
			//获取原材料仓库单
			if(gridName.indexOf("storeorder") > -1){
				StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
				request.setAttribute("storeOrder", storeOrder);
			}
			
			
			//获取半检记录单
			if(gridName.indexOf("halfcheckrecordorder") > -1){
				HalfCheckRecordOrder halfCheckRecordOrder = halfCheckRecordOrderService.getByOrder(orderId);
				request.setAttribute("halfCheckRecordOrder", halfCheckRecordOrder);
			}
			
			
			//获取原材料采购单
			if(gridName.indexOf("materialpurchaseorder") > -1){
				MaterialPurchaseOrder materialPurchaseOrder = materialPurchaseOrderService.getByOrder(orderId);
				request.setAttribute("materialPurchaseOrder", materialPurchaseOrder);
			}
			
			
			//获取染色单
			if(gridName.indexOf("coloringorder") > -1){
				ColoringOrder coloringOrder = coloringOrderService.getByOrder(orderId);
				request.setAttribute("coloringOrder", coloringOrder);
			}
			
			
			//获取抽检记录单
			if(gridName.indexOf("checkrecordorder") > -1){
				CheckRecordOrder checkRecordOrder = checkRecordOrderService.getByOrder(orderId);
				request.setAttribute("checkRecordOrder", checkRecordOrder);
			}
			
			
			//获取辅料采购单
			if(gridName.indexOf("fuliaopurchaseorder") > -1){
				FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.getByOrder(orderId);
				request.setAttribute("fuliaoPurchaseOrder", fuliaoPurchaseOrder);
			}
			
			
			//获取车缝记录单
			if(gridName.indexOf("carfixrecordorder") > -1){
				CarFixRecordOrder carFixRecordOrder = carFixRecordOrderService.getByOrder(orderId);
				request.setAttribute("carFixRecordOrder", carFixRecordOrder);
			}
			
			
			//获取整烫记录单
			if(gridName.indexOf("ironingrecordorder") > -1){
				IroningRecordOrder ironingRecordOrder = ironingRecordOrderService.getByOrder(orderId);
				request.setAttribute("ironingRecordOrder", ironingRecordOrder);
			}
			return new ModelAndView("printorder/print");
//			throw new Exception("没有该表格");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
}
