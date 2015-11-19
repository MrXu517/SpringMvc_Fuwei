package com.fuwei.controller.producesystem;

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
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.entity.producesystem.FuliaoChangeLocation;
import com.fuwei.entity.producesystem.Location;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.producesystem.FuliaoChangeLocationService;
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
	
	
	@RequestMapping(value = "/changelocation/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView changelocation_scan(HttpSession session, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("fuliaoinout/changelocation/scan");
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
		Integer fuliaoId = location.getFuliaoId();
		if(fuliaoId == null){
			throw new Exception("该库位是空库位，无法更改");
		}
		Map<Integer,Integer> locationMap = fuliaoCurrentStockService.locationByFuliao(fuliaoId);
		Fuliao fuliao = fuliaoService.get(fuliaoId);
		request.setAttribute("fuliao", fuliao);
		request.setAttribute("locationMap", locationMap);
		//获取可以更改的库位,条件1.空或本身 
		List<Location> locationlist = locationService.getChangeLocationList(fuliaoId);
		request.setAttribute("locationlist", locationlist);
		return new ModelAndView("fuliaoinout/changelocation/scan_confirm");
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
}
