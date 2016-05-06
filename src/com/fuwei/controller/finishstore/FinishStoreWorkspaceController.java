package com.fuwei.controller.finishstore;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.finishstore.FinishInOut;
import com.fuwei.entity.finishstore.FinishStoreStock;
import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.producesystem.HalfInOut;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.finishstore.FinishStoreStockService;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.util.SerializeTool;

/*原材料工作台*/
@RequestMapping("/finishstore_workspace")
@Controller
public class FinishStoreWorkspaceController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	FinishStoreStockService finishStoreStockService;
	@Autowired
	OrderService orderService;
	@Autowired
	PackingOrderService packingOrderService;
	
	@RequestMapping(value = "/workspace", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView half_workspace(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producesystem/finishstore_workspace";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有成品工作台的权限",
					null);
		}
		return new ModelAndView("finishstore/workspace");
	}
	
	
	@RequestMapping(value = "/current_stock", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView current_stock(Integer page,Integer charge_employee,String orderNumber, Boolean not_zero,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "producesystem/finishstore_workspace";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有成品工作台的权限", null);
		}

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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = finishStoreStockService.getListAndDetail(pager, null,charge_employee, orderNumber,not_zero, sortList);
		
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("not_zero", not_zero);
		request.setAttribute("pager", pager);
		return new ModelAndView("finishstore/current_stock");
	}
	
	@RequestMapping(value = "/in_out/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView in_out(@PathVariable Integer orderId, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (orderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "finishstore/in_out";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		String lcode2 = "order/progress";
		Boolean hasAuthority2 = SystemCache.hasAuthority(session, lcode2);
		if (!hasAuthority && !hasAuthority2) {
			throw new PermissionDeniedDataAccessException("没有查看订单成品出入库记录的权限",
					null);
		}
		Order order = orderService.get(orderId);
		if(order == null){
			throw new Exception("找不到ID为" + orderId + "的订单");
		}
		List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
		if(packingOrderList == null || packingOrderList.size()<=0){
			throw new Exception("该订单没有创建装箱单，请先创建装箱单 点击此处创建 <a href='packing_order/add/"+ orderId + "'>添加装箱单</a>");
		}
		request.setAttribute("packingOrderList", packingOrderList);
		List<FinishInOut> detailInOutlist = finishStoreStockService.inoutDetail(orderId);
		//Map<packingOrderId,List<FinishInOut>>
		Map<Integer,List<FinishInOut>> detailInOutMap = new HashMap<Integer,List<FinishInOut>>();
		for(FinishInOut temp : detailInOutlist){
			int tempPackingOrderId = temp.getPackingOrderId();
			if(detailInOutMap.containsKey(tempPackingOrderId)){
				List<FinishInOut> templist = detailInOutMap.get(tempPackingOrderId);
				templist.add(temp);
				detailInOutMap.put(tempPackingOrderId, templist);
			}else{
				List<FinishInOut> templist = new ArrayList<FinishInOut>();
				templist.add(temp);
				detailInOutMap.put(tempPackingOrderId, templist);
			}
		}
		
		if (detailInOutlist == null) {
			throw new Exception("找不到订单ID为" + orderId + "的成品出入库、退货记录");
		}
		FinishStoreStock storeStock = finishStoreStockService.getAndDetail(orderId);
		Map<Integer,List<FinishStoreStockDetail>> storeStockMap = new HashMap<Integer, List<FinishStoreStockDetail>>();
		if(storeStock.getDetaillist()!=null && storeStock.getDetaillist().size()>0){
			for(FinishStoreStockDetail temp : storeStock.getDetaillist()){
				int tempPackingOrderId = temp.getPackingOrderId();
				if(storeStockMap.containsKey(tempPackingOrderId)){
					List<FinishStoreStockDetail> templist = storeStockMap.get(tempPackingOrderId);
					templist.add(temp);
					storeStockMap.put(tempPackingOrderId, templist);
				}else{
					List<FinishStoreStockDetail> templist = new ArrayList<FinishStoreStockDetail>();
					templist.add(temp);
					storeStockMap.put(tempPackingOrderId, templist);
				}
			}
		}
		
		
		request.setAttribute("order", order);
//		request.setAttribute("storeStock", storeStock);
		request.setAttribute("storeStockMap", storeStockMap);
//		request.setAttribute("detailInOutlist", detailInOutlist);
		request.setAttribute("detailInOutMap", detailInOutMap);
		return new ModelAndView("finishstore/order_in_out");	
	}

}
