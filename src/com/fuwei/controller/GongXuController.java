package com.fuwei.controller;

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

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Company;
import com.fuwei.entity.GongXu;
import com.fuwei.entity.Salesman;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.CompanyService;
import com.fuwei.service.GongXuService;
import com.fuwei.service.SalesmanService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/gongxu")
@Controller
public class GongXuController extends BaseController {
	
	@Autowired
	GongXuService gongxuService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "gongxu";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有工序管理的权限", null);
		}
		request.setAttribute("gongxulist", SystemCache.gongxulist);
		return new ModelAndView("systeminfo/gongxu");

	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(GongXu gongxu,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "gongxu/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加工序的权限", null);
		}
		
		gongxu.setCreated_at(DateTool.now());
		gongxu.setUpdated_at(DateTool.now());
		gongxu.setCreated_user(user.getId());
		int success = gongxuService.add(gongxu);
		
		//更新缓存
		new SystemCache().initGongxuList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "gongxu/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除工序的权限", null);
		}
		int success = gongxuService.remove(id);
		//更新缓存
		new SystemCache().initGongxuList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public GongXu get(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "gongxu/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看公司列表的权限", null);
		}
		GongXu gongxu = gongxuService.get(id);
		return gongxu;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(GongXu gongxu,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "gongxu/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑工序的权限", null);
		}
		gongxu.setUpdated_at(DateTool.now());
		int success = gongxuService.update(gongxu);
		
		//更新缓存
		new SystemCache().initGongxuList();
		
		return this.returnSuccess();
		
	}
	
	
}
