package com.fuwei.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.CheckRecordOrder;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.ordergrid.ColoringProcessOrder;
import com.fuwei.entity.ordergrid.ColoringProcessOrderDetail;
import com.fuwei.entity.ordergrid.FinalStoreOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrder;
import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.IroningRecordOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProductionScheduleOrder;
import com.fuwei.entity.ordergrid.ShopRecordOrder;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.CarFixRecordOrderService;
import com.fuwei.service.ordergrid.CheckRecordOrderService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.ColoringProcessOrderService;
import com.fuwei.service.ordergrid.FinalStoreOrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.HeadBankOrderService;
import com.fuwei.service.ordergrid.IroningRecordOrderService;
import com.fuwei.service.ordergrid.MaterialPurchaseOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.ordergrid.ProductionScheduleOrderService;
import com.fuwei.service.ordergrid.ShopRecordOrderService;
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
	
	/*2015-3-23添加 新表格*/
	@Autowired
	ProductionScheduleOrderService productionScheduleOrderService;
	@Autowired
	FinalStoreOrderService finalStoreOrderService;
	@Autowired
	ShopRecordOrderService shopRecordOrderService;
	@Autowired
	ColoringProcessOrderService coloringProcessOrderService;

	
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView print(Integer orderId,String gridName, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try {
			Order order = orderService.get(orderId);
			PlanOrder planOrder = null;
			List<ColoringOrder> coloringOrderList = null;
			
			List<ProducingOrder> producingOrderList = producingOrderService.getByOrder(orderId);	
			String productfactoryStr = "";
			String seq = "";
			if(producingOrderList!=null){
				for(ProducingOrder producingOrder : producingOrderList){
					productfactoryStr += seq + SystemCache.getFactoryName(producingOrder.getFactoryId());
					seq = " | ";
				}
			}
			request.setAttribute("productfactoryStr", productfactoryStr);
			
			
			
			request.setAttribute("order", order);
			
			Boolean printAll = false;
			if(gridName == null){
				printAll = true;
			}
			String grids = "";
			
			
			//获取质量记录单
			if(printAll || gridName.indexOf("headbankorder") > -1){
				HeadBankOrder headBankOrder = headBankOrderService.getByOrder(orderId);
				if(headBankOrder!=null){
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					headBankOrder.setDetaillist(planOrder.getDetaillist());
					grids += "headbankorder,";
					request.setAttribute("headBankOrder", headBankOrder);
					
				}
				
			}
			
			//获取生产单
			if(printAll || gridName.indexOf("producingorder") > -1){
//				List<ProducingOrder> producingOrderList = producingOrderService.getByOrder(orderId);		
				if(producingOrderList!=null){
					grids += "producingorder,";
					request.setAttribute("producingOrderList", producingOrderList);
				}				
			}
			
			
			//获取计划单
			if(printAll || gridName.indexOf("planorder") > -1){
				planOrder = planOrderService.getByOrder(orderId);
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
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					halfCheckRecordOrder.setDetaillist(planOrder.getDetaillist());
					grids += "halfcheckrecordorder,";
					request.setAttribute("halfCheckRecordOrder", halfCheckRecordOrder);			
					
				}	
			}
			
			
			//获取原材料采购单
			//2015-4-2 修改打印所有的单据时， 不打印 染色单、采购单、辅料单
			if(!printAll && gridName.indexOf("materialpurchaseorder") > -1){
				List<MaterialPurchaseOrder> materialPurchaseOrderList = materialPurchaseOrderService.getByOrder(orderId);
				if(materialPurchaseOrderList!=null && materialPurchaseOrderList.size() > 0){
					grids += "materialpurchaseorder,";
					request.setAttribute("materialPurchaseOrderList", materialPurchaseOrderList);
				}	
			}
			
			
			//获取染色单
			//2015-4-2 修改打印所有的单据时， 不打印 染色单、采购单、辅料单
			if(!printAll && gridName.indexOf("coloringorder") > -1){
				coloringOrderList = coloringOrderService.getByOrder(orderId);
				if(coloringOrderList!=null && coloringOrderList.size() > 0){
					grids += "coloringorder,";
					request.setAttribute("coloringOrderList", coloringOrderList);
				}	
			}
			
			
			//获取抽检记录单
			if(printAll || (gridName.indexOf(",checkrecordorder") > -1 || gridName.indexOf("checkrecordorder")==0)){
				CheckRecordOrder checkRecordOrder = checkRecordOrderService.getByOrder(orderId);
				if(checkRecordOrder!=null){
					grids += "checkrecordorder,";
					request.setAttribute("checkRecordOrder", checkRecordOrder);
				}	
			}
			
			
			//获取辅料采购单
			//2015-4-2 修改打印所有的单据时， 不打印 染色单、采购单、辅料单
			if(!printAll && gridName.indexOf("fuliaopurchaseorder") > -1){
				List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = fuliaoPurchaseOrderService.getByOrder(orderId);
				if(fuliaoPurchaseOrderList!=null && fuliaoPurchaseOrderList.size() > 0){
					grids += "fuliaopurchaseorder,";
					request.setAttribute("fuliaoPurchaseOrderList", fuliaoPurchaseOrderList);
				}	
			}
			
			
			//获取车缝记录单
			if(printAll || gridName.indexOf("carfixrecordorder") > -1){
				CarFixRecordOrder carFixRecordOrder = carFixRecordOrderService.getByOrder(orderId);
				if(carFixRecordOrder!=null){
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					carFixRecordOrder.setDetaillist(planOrder.getDetaillist());
					grids += "carfixrecordorder,";
					request.setAttribute("carFixRecordOrder", carFixRecordOrder);
				}	
			}
			
			
			//获取整烫记录单
			if(printAll || gridName.indexOf("ironingrecordorder") > -1){
				IroningRecordOrder ironingRecordOrder = ironingRecordOrderService.getByOrder(orderId);
				if(ironingRecordOrder!=null){
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					ironingRecordOrder.setDetaillist(planOrder.getDetaillist());
					grids += "ironingrecordorder,";
					request.setAttribute("ironingRecordOrder", ironingRecordOrder);
				}	
			}
			
			/*2015-3-23添加 新表格*/
			//获取生产进度单
			if(printAll || gridName.indexOf("productionscheduleorder") > -1){
				ProductionScheduleOrder productionScheduleOrder = productionScheduleOrderService.getByOrder(orderId);
				if(productionScheduleOrder!=null){
					grids += "productionscheduleorder,";
					request.setAttribute("productionScheduleOrder", productionScheduleOrder);
				}	
			}
			//获取成品仓库记录单
			if(printAll || gridName.indexOf("finalstorerecordorder") > -1){
				FinalStoreOrder finalStoreOrder = finalStoreOrderService.getByOrder(orderId);
				if(finalStoreOrder!=null){
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					finalStoreOrder.setDetaillist(planOrder.getDetaillist());
					grids += "finalstorerecordorder,";
					request.setAttribute("finalStoreOrder", finalStoreOrder);
				}
			}
			//2015-5-12添加获取成品检验记录单
			if(printAll || gridName.indexOf("finalcheckrecordorder") > -1){		
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					grids += "finalcheckrecordorder,";
					request.setAttribute("planOrder", planOrder);
			}
			//获取车间记录单
			if(printAll || gridName.indexOf("shoprecordorder") > -1){
				ShopRecordOrder shopRecordOrder = shopRecordOrderService.getByOrder(orderId);
				if(shopRecordOrder!=null){
					if(planOrder==null){
						planOrder = planOrderService.getByOrder(orderId);
					}
					shopRecordOrder.setDetaillist(planOrder.getDetaillist());
					grids += "shoprecordorder,";
					request.setAttribute("shopRecordOrder", shopRecordOrder);
				}
			}
			//获取染色进度单
			if(printAll || gridName.indexOf("coloringprocessorder") > -1){
				ColoringProcessOrder coloringProcessOrder = coloringProcessOrderService.getByOrder(orderId);
				if(coloringProcessOrder!=null){
					if(coloringOrderList==null){
						coloringOrderList = coloringOrderService.getByOrder(orderId);
					}
					List<ColoringProcessOrderDetail> coloringProcessOrderDetailList = new ArrayList<ColoringProcessOrderDetail>();
					for(ColoringOrder coloringOrder : coloringOrderList){
						Integer factoryId = coloringOrder.getFactoryId();
						List<ColoringOrderDetail> detaillist = coloringOrder.getDetaillist() == null ?  new ArrayList<ColoringOrderDetail>() : coloringOrder.getDetaillist();
						for(ColoringOrderDetail detail : detaillist){
							ColoringProcessOrderDetail temp = new ColoringProcessOrderDetail();
							temp.setColor(detail.getColor());
							temp.setFactoryId(factoryId);
							temp.setMaterial(detail.getMaterial());
							temp.setQuantity(detail.getQuantity());
							coloringProcessOrderDetailList.add(temp);
						}
					}
					coloringProcessOrder.setDetaillist(coloringProcessOrderDetailList);
					grids += "coloringprocessorder,";
					request.setAttribute("coloringProcessOrder", coloringProcessOrder);
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
