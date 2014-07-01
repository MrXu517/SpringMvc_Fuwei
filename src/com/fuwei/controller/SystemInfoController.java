package com.fuwei.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.LoginedUser;
import com.fuwei.commons.SystemCache;
import com.fuwei.constant.Constants;
import com.fuwei.entity.Module;
import com.fuwei.entity.Role;
import com.fuwei.entity.User;

@RequestMapping("/systeminfo")
@Controller
public class SystemInfoController extends BaseController {
	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index (HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.setAttribute("companylist", SystemCache.companylist);
		request.setAttribute("gongxulist", SystemCache.gongxulist);
		request.setAttribute("salesmanlist", SystemCache.salesmanlist);
		request.setAttribute("userlist", SystemCache.userlist);
		request.setAttribute("rolelist", SystemCache.rolelist);
		return new ModelAndView("systeminfo/index");
		
	} 
}
