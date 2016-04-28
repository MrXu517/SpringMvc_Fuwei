package com.fuwei.controller.producesystem;

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
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.entity.producesystem.FuliaoChangeLocation;
import com.fuwei.entity.producesystem.FuliaoOut;
import com.fuwei.entity.producesystem.Location;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderDetailService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.producesystem.FuliaoCurrentStockService;
import com.fuwei.service.producesystem.FuliaoService;
import com.fuwei.service.producesystem.LocationService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/fuliao_workspace")
@Controller
public class FuliaoStoreWorkspaceController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	LocationService locationService;
	@Autowired
	FuliaoCurrentStockService fuliaoCurrentStockService;
	@Autowired
	FuliaoService fuliaoService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	@Autowired
	FuliaoPurchaseOrderDetailService fuliaoPurchaseOrderDetailService;
	
	
	@RequestMapping(value = "/workspace", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView workspace(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_workspace/workspace";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有辅料工作台的权限",
					null);
		}
		return new ModelAndView("fuliaoinout/workspace");
	}
	
	@RequestMapping(value = "/workspace_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView workspace_purchase(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selffuliaoinout";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有自购辅料工作台的权限",
					null);
		}
		return new ModelAndView("selffuliao/workspace");
	}
	
	@RequestMapping(value = "/commonfuliao_workspace", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView commonfuliao_workspace(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_workspace/commonfuliao_workspace";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有通用辅料工作台的权限",
					null);
		}
		return new ModelAndView("commonfuliao/commonfuliao_workspace");
	}
	
	@RequestMapping(value = "/commonfuliao", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView commonfuliao(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao_workspace/commonfuliao";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有通用辅料的权限",
					null);
		}
		return new ModelAndView("commonfuliao/commonfuliao");
	}
	
	@RequestMapping(value = "/current_stock", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView current_stock(Integer page,Integer charge_employee,String locationNumber,String orderNumber,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliao_workspace/workspace";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有辅料工作台的权限", null);
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

		pager = fuliaoCurrentStockService.getList(pager, charge_employee,locationNumber, orderNumber, sortList);
		
		request.setAttribute("locationNumber", locationNumber);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliaoinout/current_stock");
	}
	
	@RequestMapping(value = "/current_stock_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView current_stock_common(Integer page,String locationNumber ,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliao_workspace/commonfuliao_workspace";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有通用辅料工作台的权限", null);
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

		pager = fuliaoCurrentStockService.getList_common(pager,locationNumber, sortList);
		
		request.setAttribute("locationNumber", locationNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliaoinout/current_stock_common");
	}
	
	@RequestMapping(value = "/current_stock_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView current_stock_purchase(Integer page,Integer charge_employee,String locationNumber,String orderNumber,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "selffuliaoinout";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有自购辅料工作台的权限", null);
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
		sort2.setProperty("l.id");
		sortList.add(sort2);

		pager = fuliaoCurrentStockService.getList_purchase(pager, charge_employee,locationNumber, orderNumber, sortList);
		
		request.setAttribute("locationNumber", locationNumber);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("selffuliao/current_stock");
	}
	
	//手动清辅料库存
	@RequestMapping(value = "/cleaningstock", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView cleaningstock(Integer page,Integer charge_employee,String locationNumber,String orderNumber,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliao_workspace/cleaningstock";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有手动清辅料库存的权限", null);
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

		pager = fuliaoCurrentStockService.getList(pager, charge_employee,locationNumber, orderNumber, sortList);
		
		request.setAttribute("locationNumber", locationNumber);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliaoinout/cleaningstock");
	}
	
	//手动清辅料库存 -- 通用
	@RequestMapping(value = "/cleaningstock_common", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView cleaningstock_common(Integer page,String locationNumber,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliao_workspace/cleaningstock";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有手动清辅料库存的权限", null);
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

		pager = fuliaoCurrentStockService.getList_common(pager,locationNumber, sortList);
		
		request.setAttribute("locationNumber", locationNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("fuliaoinout/cleaningstock_common");
	}
	
	//手动清辅料库存 -- 自购
	@RequestMapping(value = "/cleaningstock_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView cleaningstock_purchase(Integer page,Integer charge_employee,String locationNumber,String orderNumber,String sortJSON, HttpSession session,
			HttpServletRequest request) throws Exception {

		String lcode = "fuliao_workspace/cleaningstock";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有手动清辅料库存的权限", null);
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
		sort2.setProperty("l.id");
		sortList.add(sort2);

		pager = fuliaoCurrentStockService.getList_purchase(pager, charge_employee,locationNumber, orderNumber, sortList);
		
		request.setAttribute("locationNumber", locationNumber);
		request.setAttribute("charge_employee", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for (Employee temp : SystemCache.employeelist) {
			if (temp.getIs_charge_employee()) {
				employeelist.add(temp);
			}
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("selffuliao/cleaningstock");
	}
	// 清空辅料库存
	@RequestMapping(value = "/cleaningstock", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cleaningstock(String ids,Date step_time,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		if (ids == null) {
			throw new Exception("缺少库位ID");
		}
		String lcode = "fuliao_workspace/cleaningstock";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有手动清辅料库存的权限", null);
		}

		
		// 修改订单信息
		String[] tempids = ids.split(",");
		int[] int_ids = new int[tempids.length];
		
		for(int i = 0 ;i < tempids.length ; ++i){
			int_ids[i] = Integer.parseInt(tempids[i]);
		}
		String fuliaoOutIds = locationService.cleanstock_batch(int_ids, user.getId());
		
		//若成功，则返回显示此次清空生成的辅料出库单
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("ids", fuliaoOutIds);
		return this.returnSuccess(data);
	}

	
	@RequestMapping(value = "/changelocation/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView changelocation_scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("fuliaoinout/changelocation/scan");
	}
	
	@RequestMapping(value = "/changelocation/scan_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView changelocation_scan_purchase(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("selffuliao/changelocation/scan");
	}
	
	@RequestMapping(value = "/changelocation/scan_confirm", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView changelocation_scan_confirm(String locationNumber, HttpSession session, HttpServletRequest request)
			throws Exception {
		if(locationNumber==null || locationNumber.equals("")){
			throw new Exception("请输入库位编号");
		}
		//1.获取该库位的辅料
		Location location = locationService.get(locationNumber);
		if(location == null){
			throw new Exception("找不到编号为" + locationNumber + "的库位");
		}
		if(location.getIsempty()){
			throw new Exception("该库位是空库位，无法更改");
		}
		if(location.getFuliaoId()!=null && location.getFuliaoId()!=0){//大货辅料
			Integer fuliaoId = location.getFuliaoId();
			Map<Integer,Integer> locationMap = fuliaoCurrentStockService.locationByFuliao(fuliaoId);
			Fuliao fuliao = fuliaoService.get(fuliaoId);
			request.setAttribute("fuliao", fuliao);
			request.setAttribute("locationMap", locationMap);
			//获取可以更改的库位,条件1.空或本身 
			List<Location> locationlist = locationService.getChangeLocationList(fuliaoId,location.getType());
			request.setAttribute("locationlist", locationlist);
			return new ModelAndView("fuliaoinout/changelocation/scan_confirm");
		}else{//自购辅料
			int fuliaoPurchaseOrderDetailId = location.getFuliaoPurchaseOrderDetailId();
			Map<Integer,Integer> locationMap = fuliaoCurrentStockService.locationByPurchaseDetail(fuliaoPurchaseOrderDetailId);
			request.setAttribute("locationMap", locationMap);
			FuliaoPurchaseOrderDetail fuliaoPurchaseOrderDetail = fuliaoPurchaseOrderDetailService.get(fuliaoPurchaseOrderDetailId);
			FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(fuliaoPurchaseOrderDetail.getFuliaoPurchaseOrderId());
			request.setAttribute("fuliaoPurchaseOrderDetail", fuliaoPurchaseOrderDetail);
			request.setAttribute("fuliaoPurchaseOrder", fuliaoPurchaseOrder);
			//获取可以更改的库位,条件1.空或本身 
			List<Location> locationlist = locationService.getChangeLocationList_purchase(fuliaoPurchaseOrderDetailId,location.getType());
			request.setAttribute("locationlist", locationlist);
			return new ModelAndView("selffuliao/changelocation/scan_confirm_purchase");
		}
		
	}
	
	@RequestMapping(value = "/changelocation", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String,Object> changelocation(int fuliaoId, Integer locationId, HttpSession session, HttpServletRequest request)
			throws Exception {
		if(locationId == null){
			throw new Exception("更改库位ID不能为空");
		}
		String lcode = "fuliao/changelocation";
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有更改库位的权限", null);
		}
		FuliaoChangeLocation handle = new FuliaoChangeLocation();
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());
		handle.setFuliaoId(fuliaoId);
		handle.setLocationId(locationId);
		handle.setMemo("更改库位为【" + SystemCache.getLocationNumber(locationId) + "】");
		locationService.changeLocation(fuliaoId, locationId,handle);
		return this.returnSuccess();
	}
	
	@RequestMapping(value = "/changelocation_purchase", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String,Object> changelocation_purchase(int fuliaoPurchaseOrderDetailId, Integer locationId, HttpSession session, HttpServletRequest request)
			throws Exception {
		if(locationId == null){
			throw new Exception("更改库位ID不能为空");
		}
		String lcode = "selffuliaoinout/changelocation";
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有更改库位的权限", null);
		}
		FuliaoChangeLocation handle = new FuliaoChangeLocation();
		handle.setCreated_at(DateTool.now());
		handle.setCreated_user(user.getId());
		handle.setFuliaoPurchaseOrderDetailId(fuliaoPurchaseOrderDetailId);
		handle.setLocationId(locationId);
		handle.setMemo("更改库位为【" + SystemCache.getLocationNumber(locationId) + "】");
		locationService.changelocation_purchase(fuliaoPurchaseOrderDetailId, locationId,handle);
		return this.returnSuccess();
	}
}
