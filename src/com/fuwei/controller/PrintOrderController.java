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
			
			Boolean printAll = false;
			if(gridName == null){
				printAll = true;
			}
			String grids = "";
			
			
			//获取头带质量记录单
			if(printAll || gridName.indexOf("headbankorder") > -1){
				HeadBankOrder headBankOrder = headBankOrderService.getByOrder(orderId);
				if(headBankOrder!=null){
					grids += "headbankorder,";
					request.setAttribute("headBankOrder", headBankOrder);
				}
				
			}
			
			//获取生产单
			if(printAll || gridName.indexOf("producingorder") > -1){
				ProducingOrder producingOrder = producingOrderService.getByOrder(orderId);		
				if(producingOrder!=null){
					grids += "producingorder,";
					request.setAttribute("producingOrder", producingOrder);
				}				
			}
			
			
			//获取计划单
			if(printAll || gridName.indexOf("planorder") > -1){
				PlanOrder planOrder = planOrderService.getByOrder(orderId);
				if(planOrder!=null){
					grids += "planorder,";
					request.setAttribute("planOrder", planOrder);
				}	
			}
			
			//获取原材料仓库单
			if(printAll || gridName.indexOf("storeorder") > -1){
				StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
				if(storeOrder!=null){
					grids += "storeorder,";
					request.setAttribute("storeOrder", storeOrder);
				}	
			}
			
			
			//获取半检记录单
			if(printAll || gridName.indexOf("halfcheckrecordorder") > -1){
				HalfCheckRecordOrder halfCheckRecordOrder = halfCheckRecordOrderService.getByOrder(orderId);
				if(halfCheckRecordOrder!=null){
					grids += "halfcheckrecordorder,";
					request.setAttribute("halfCheckRecordOrder", halfCheckRecordOrder);
				}	
			}
			
			
			//获取原材料采购单
			if(printAll || gridName.indexOf("materialpurchaseorder") > -1){
				MaterialPurchaseOrder materialPurchaseOrder = materialPurchaseOrderService.getByOrder(orderId);
				if(materialPurchaseOrder!=null){
					grids += "materialpurchaseorder,";
					request.setAttribute("materialPurchaseOrder", materialPurchaseOrder);
				}	
			}
			
			
			//获取染色单
			if(printAll || gridName.indexOf("coloringorder") > -1){
				ColoringOrder coloringOrder = coloringOrderService.getByOrder(orderId);
				if(coloringOrder!=null){
					grids += "coloringorder,";
					request.setAttribute("coloringOrder", coloringOrder);
				}	
			}
			
			
			//获取抽检记录单
			if(printAll || gridName.indexOf("checkrecordorder") > -1){
				CheckRecordOrder checkRecordOrder = checkRecordOrderService.getByOrder(orderId);
				if(checkRecordOrder!=null){
					grids += "checkrecordorder,";
					request.setAttribute("checkRecordOrder", checkRecordOrder);
				}	
			}
			
			
			//获取辅料采购单
			if(printAll || gridName.indexOf("fuliaopurchaseorder") > -1){
				FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.getByOrder(orderId);
				if(fuliaoPurchaseOrder!=null){
					grids += "fuliaopurchaseorder,";
					request.setAttribute("fuliaoPurchaseOrder", fuliaoPurchaseOrder);
				}	
			}
			
			
			//获取车缝记录单
			if(printAll || gridName.indexOf("carfixrecordorder") > -1){
				CarFixRecordOrder carFixRecordOrder = carFixRecordOrderService.getByOrder(orderId);
				if(carFixRecordOrder!=null){
					grids += "carfixrecordorder,";
					request.setAttribute("carFixRecordOrder", carFixRecordOrder);
				}	
			}
			
			
			//获取整烫记录单
			if(printAll || gridName.indexOf("ironingrecordorder") > -1){
				IroningRecordOrder ironingRecordOrder = ironingRecordOrderService.getByOrder(orderId);
				if(ironingRecordOrder!=null){
					grids += "ironingrecordorder,";
					request.setAttribute("ironingRecordOrder", ironingRecordOrder);
				}	
			}
			if(grids.indexOf(",") <= -1){
				throw new Exception("请先创建表格，再打印");
			}
			request.setAttribute("gridName", grids);
			return new ModelAndView("printorder/print");
//			throw new Exception("没有该表格");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
}
