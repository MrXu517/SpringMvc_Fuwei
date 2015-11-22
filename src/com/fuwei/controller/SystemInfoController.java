package com.fuwei.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;

@RequestMapping("/systeminfo")
@Controller
public class SystemInfoController extends BaseController {
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "systeminfo";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有系统信息管理的权限", null);
		}
		request.setAttribute("companylist", SystemCache.companylist);
		request.setAttribute("gongxulist", SystemCache.gongxulist);
		request.setAttribute("salesmanlist", SystemCache.salesmanlist);
		request.setAttribute("userlist", SystemCache.userlist);
		request.setAttribute("rolelist", SystemCache.rolelist);
		request.setAttribute("factorylist", SystemCache.factorylist);
		return new ModelAndView("systeminfo/index");

	}

}