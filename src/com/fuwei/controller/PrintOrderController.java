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
			
			//获取头带质量记录单
			if(gridName.equals("headbankorder")){
				HeadBankOrder headBankOrder = headBankOrderService.getByOrder(orderId);
				request.setAttribute("headBankOrder", headBankOrder);
				return new ModelAndView("printorder/headbankorder");
			}
			
			//获取生产单
			if(gridName.equals("producingorder")){
				ProducingOrder producingOrder = producingOrderService.getByOrder(orderId);		
				request.setAttribute("producingOrder", producingOrder);
				return new ModelAndView("printorder/producingorder");
			}
			
			
			//获取计划单
			if(gridName.equals("planorder")){
				PlanOrder planOrder = planOrderService.getByOrder(orderId);
				request.setAttribute("planOrder", planOrder);
				return new ModelAndView("printorder/planorder");
			}
			
			//获取原材料仓库单
			if(gridName.equals("storeorder")){
				StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
				request.setAttribute("storeOrder", storeOrder);
				return new ModelAndView("printorder/storeorder");
			}
			
			
			//获取半检记录单
			if(gridName.equals("halfcheckrecordorder")){
				HalfCheckRecordOrder halfCheckRecordOrder = halfCheckRecordOrderService.getByOrder(orderId);
				request.setAttribute("halfCheckRecordOrder", halfCheckRecordOrder);
				return new ModelAndView("printorder/halfcheckrecordorder");
			}
			
			
			//获取原材料采购单
			if(gridName.equals("materialpurchaseorder")){
				MaterialPurchaseOrder materialPurchaseOrder = materialPurchaseOrderService.getByOrder(orderId);
				request.setAttribute("materialPurchaseOrder", materialPurchaseOrder);
				return new ModelAndView("printorder/materialpurchaseorder");
			}
			
			
			//获取染色单
			if(gridName.equals("coloringorder")){
				ColoringOrder coloringOrder = coloringOrderService.getByOrder(orderId);
				request.setAttribute("coloringOrder", coloringOrder);
				return new ModelAndView("printorder/coloringorder");
			}
			
			
			//获取抽检记录单
			if(gridName.equals("checkrecordorder")){
				CheckRecordOrder checkRecordOrder = checkRecordOrderService.getByOrder(orderId);
				request.setAttribute("checkRecordOrder", checkRecordOrder);
				return new ModelAndView("printorder/checkrecordorder");
			}
			
			
			//获取辅料采购单
			if(gridName.equals("fuliaopurchaseorder")){
				FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.getByOrder(orderId);
				request.setAttribute("fuliaoPurchaseOrder", fuliaoPurchaseOrder);
				return new ModelAndView("printorder/fuliaopurchaseorder");
			}
			
			
			//获取车缝记录单
			if(gridName.equals("carfixrecordorder")){
				CarFixRecordOrder carFixRecordOrder = carFixRecordOrderService.getByOrder(orderId);
				request.setAttribute("carFixRecordOrder", carFixRecordOrder);
				return new ModelAndView("printorder/carfixrecordorder");
			}
			
			
			//获取整烫记录单
			if(gridName.equals("ironingrecordorder")){
				IroningRecordOrder ironingRecordOrder = ironingRecordOrderService.getByOrder(orderId);
				request.setAttribute("ironingRecordOrder", ironingRecordOrder);
				return new ModelAndView("printorder/ironingrecordorder");
			}
			
			throw new Exception("没有该表格");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
}
