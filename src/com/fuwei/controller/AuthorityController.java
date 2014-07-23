package com.fuwei.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.LoginedUser;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Company;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/authority")
@Controller
public class AuthorityController extends BaseController{
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		//判断该用户有没打开打开该页面的权限
		String lcode = "authority/index";
		if(SystemCache.hasAuthority(loginUser, lcode)){
			return new ModelAndView("authority/index");
		}
		else{
			throw new PermissionDeniedDataAccessException("没有访问权限", null);
			//return new ModelAndView("authority/error");
		}
		
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Error(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String message = request.getParameter("message");
		request.setAttribute("message", message);
		return new ModelAndView("authority/error");
	}
//	
//	//修改某用户的权限页面
//	@RequestMapping(value = "/get", method = RequestMethod.POST)
//	@ResponseBody
//	public ModelAndView add(List<Authority> authorityList,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		company.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(company.getFullname())) ;
//		company.setCreated_at(DateTool.now());
//		company.setUpdated_at(DateTool.now());
//		company.setCreated_user(user.getId());
//		int success = companyService.add(company);
//		
//		//更新缓存
//		new SystemCache().initCompanyList();
//		
//		return this.returnSuccess();
//		
//	}
//	
//	//修改某用户的权限
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> add(List<Authority> authorityList,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		company.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(company.getFullname())) ;
//		company.setCreated_at(DateTool.now());
//		company.setUpdated_at(DateTool.now());
//		company.setCreated_user(user.getId());
//		int success = companyService.add(company);
//		
//		//更新缓存
//		new SystemCache().initCompanyList();
//		
//		return this.returnSuccess();
//		
//	}
}
