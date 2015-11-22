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

import com.fuwei.commons.SystemCache;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.SystemSettingService;
import com.fuwei.util.SerializeTool;

@RequestMapping("/systemsetting")
@Controller
public class SystemSettingsController extends BaseController {
	
	@Autowired
	SystemSettingService systemSettingService;
	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = "/set", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView set(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "systemsetting";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有系统设置的权限", null);
		}
		return new ModelAndView("systemsetting/set");

	}
	
	@RequestMapping(value = "/set", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> set_post(String data,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "systemsetting";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有系统设置的权限", null);
		}
		Map<String,Object> params = SerializeTool.deserializeMap(data);
		systemSettingService.setSettings(params);
		systemSettingService.initSettings();
		return this.returnSuccess();

	}
}
