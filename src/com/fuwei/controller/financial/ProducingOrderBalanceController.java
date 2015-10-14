package com.fuwei.controller.financial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

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
import com.fuwei.entity.financial.ProducingOrderBalance;
import com.fuwei.entity.financial.ProducingOrderBalanceDetail;
import com.fuwei.entity.financial.Subject;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.entity.producesystem.StoreInOutDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.financial.ProducingOrderBalanceService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

/*生产单对账单*/
@RequestMapping("/producing_order_balance")
@Controller
public class ProducingOrderBalanceController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	ProducingOrderBalanceService producingOrderBalanceService;
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	OrderService orderService;
	
	//创建一个对账单
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producing_order_balance/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有生产单对账的权限", null);
		}
		return new ModelAndView("financial/producing_order_balance/add");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(ProducingOrderBalance producingOrderBalance,String details , HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producing_order_balance/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有生产单对账的权限", null);
		}
		//计算总金额、总扣款、总数量
		List<ProducingOrderBalanceDetail> detaillist = SerializeTool
		.deserializeList(details,
				ProducingOrderBalanceDetail.class);
		for(ProducingOrderBalanceDetail detail : detaillist){
			
		}
		
		
		//判断生产单中是否有已对账的
		producingOrderBalance.setAmount(NumberUtil.formateDouble(producingOrderBalance.getAmount(), 2));
		producingOrderBalance.setCreated_at(DateTool.now());
		producingOrderBalance.setUpdated_at(DateTool.now());
		producingOrderBalance.setCreated_user(user.getId());
		//计算总数量
		//计算总金额
		//计算地税
		//设置应付款
		int id = producingOrderBalanceService.add(producingOrderBalance);
		
		return this.returnSuccess("id",id);
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producing_order_balance/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除对账单的权限", null);
		}
		int success = producingOrderBalanceService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ProducingOrderBalance get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "producing_order_balance/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看对账单的权限", null);
		}
		ProducingOrderBalance producingOrderBalance = producingOrderBalanceService.get(id);
		return producingOrderBalance;
	}
	
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView put(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(id == null){
			throw new Exception("ID不能为空");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producing_order_balance/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑对账单的权限", null);
		}
		ProducingOrderBalance producingOrderBalance = producingOrderBalanceService.get(id);
		if(producingOrderBalance == null ){
			throw new Exception("找不到ID="+id + "的对账项");
		}
		List<Subject> subjectlist = SystemCache.getSubjectList(true);
		request.setAttribute("subjectlist", subjectlist);	
		request.setAttribute("producingOrderBalance", producingOrderBalance);	
		
		return new ModelAndView("producingOrderBalance/edit");
	}
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(ProducingOrderBalance producingOrderBalance,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producing_order_balance/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑支出项的权限", null);
		}
		producingOrderBalance.setUpdated_at(DateTool.now());
		producingOrderBalanceService.update(producingOrderBalance);
		
		return this.returnSuccess("id",producingOrderBalance.getId());
		
	}
	
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		String lcode = "producing_order_balance/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看对账单详情的权限", null);
		}

		if (id == null) {
			throw new Exception("缺少对账单ID");
		}
		ProducingOrderBalance producingOrderBalance = producingOrderBalanceService.get(id);
		
		
		
		request.setAttribute("producingOrderBalance", producingOrderBalance);
		return new ModelAndView("financial/producingOrderBalance/detail");
	}
	//获取一个生产单的信息
	
	
	@RequestMapping(value = "/scan_check", method = RequestMethod.POST)
	@ResponseBody
	public ProducingOrderBalanceDetail scan_check(Integer producingOrderId, Integer factoryId, HttpSession session,
			HttpServletRequest request) throws Exception {
		//producingOrderId为生产单ID
		if (producingOrderId == null) {
			throw new Exception("缺少生产单ID");
		}
		
		ProducingOrder producingOrder = producingOrderService.get(producingOrderId);
		if (producingOrder == null) {
			throw new Exception("找不到ID为" + producingOrder + "的生产单");
		}
		if((int)producingOrder.getFactoryId() != (int)factoryId){
			throw new Exception("机织单位不一致");
		}
		Order order = orderService.get(producingOrder.getOrderId());
		//判断该生产单是否已结账
		
		//生成一个detail
		ProducingOrderBalanceDetail detail = new ProducingOrderBalanceDetail();
		detail.setProducingOrderId(producingOrderId);
		detail.setProducingOrder_createdAt(producingOrder.getCreated_at());
		detail.setProducingOrder_number(producingOrder.getNumber());
		detail.setOrderId(producingOrder.getOrderId());
		detail.setOrderNumber(producingOrder.getOrderNumber());
		detail.setCharge_employee(producingOrder.getCharge_employee());
		detail.setCompany_productNumber(order.getCompany_productNumber());
		detail.setCompanyId(producingOrder.getCompanyId());
		detail.setCustomerId(producingOrder.getCustomerId());
		detail.setSampleId(producingOrder.getSampleId());
		detail.setSample_name(producingOrder.getName());
		detail.setFactoryId(producingOrder.getFactoryId());
		detail.setFactory_name(SystemCache.getFactoryName(producingOrder.getFactoryId()));
		detail.setCompany_name(SystemCache.getCompanyShortName(producingOrder.getCompanyId()));
		detail.setCharge_employee_name(SystemCache.getEmployeeName(producingOrder.getCharge_employee()));
		detail.setDetaillist(producingOrder.getDetaillist());
		//设置total_amount
		double total_amount = 0.0;
		if(detail.getDetaillist()!=null){
			for(ProducingOrderDetail item : detail.getDetaillist()){
				total_amount+= NumberUtil.formateDouble(item.getQuantity()*item.getPrice(), 2);
			}
		}
		detail.setTotal_amount(total_amount);
		return detail;
	}
}
