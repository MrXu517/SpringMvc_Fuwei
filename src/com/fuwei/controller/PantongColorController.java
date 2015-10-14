package com.fuwei.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Company;
import com.fuwei.entity.Factory;
import com.fuwei.entity.PantongColor;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Bank;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.CompanyService;
import com.fuwei.service.FactoryService;
import com.fuwei.service.PantongColorService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;
import com.fuwei.util.PantongUtil;

@RequestMapping("/pantongcolor")
@Controller
public class PantongColorController extends BaseController {
	@Autowired
	PantongColorService pantongColorService;
	@Autowired
	AuthorityService authorityService;

	// 批量导入
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView batch_add(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "pantongcolor/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入潘通色号的权限", null);
		}
		return new ModelAndView("util/pantongcolor/import");
	}

	// 批量导入银行账户
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> batch_add(
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "pantongcolor/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入潘通色号的权限", null);
		}
		List<PantongColor> pantongList = new ArrayList<PantongColor>();
		
		Map<String,PantongColor> map =  PantongUtil.getPantongColorMapByEXCEL(file.getInputStream());
		
		for (String pantongColorName : map.keySet()) {
			pantongList.add(map.get(pantongColorName));
		}
		if (pantongList == null || pantongList.size() <= 0) {
			throw new Exception("请至少上传一条记录");
		}
		pantongColorService.reBuild(pantongList);
		return this.returnSuccess();
	}


	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView search(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return new ModelAndView("util/pantongcolor/search");
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView search(String panTongName,HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PantongColor object = pantongColorService.get(panTongName);
		if(object == null){
			request.setAttribute("message","找不到潘通色号：" + panTongName);
		}else{
			request.setAttribute("pantongColor",object);
		}
		return new ModelAndView("util/pantongcolor/search");
	}
	
//	@RequestMapping(value = "/search", method = RequestMethod.POST)
//	@ResponseBody
//	public PantongColor search(String panTongName,HttpSession session,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		PantongColor object = pantongColorService.get(panTongName);
//		return object;
//	}

//	@RequestMapping(value = "/get/{pantongcolor}", method = RequestMethod.GET)
//	@ResponseBody
//	public Factory get(@PathVariable String pantongcolor, HttpSession session,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		String lcode = "pantongcolor/detail";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看加工工厂列表的权限", null);
//		}
//		Factory Factory = factoryService.get(id);
//		return Factory;
//	}

	
}
