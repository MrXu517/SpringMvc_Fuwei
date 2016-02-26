package com.fuwei.controller.finishstore;

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
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.entity.finishstore.PackProperty;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.finishstore.PackPropertyService;
import com.fuwei.util.DateTool;

@RequestMapping("/packproperty")
@Controller
public class PackPropertyController extends BaseController {
	
	@Autowired
	PackPropertyService packPropertyService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "packproperty";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有装箱单列属性管理的权限", null);
		}
		request.setAttribute("packpropertylist", SystemCache.packpropertylist);
		return new ModelAndView("finishstore/packproperty");

	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(PackProperty object,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packproperty/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加装箱单列属性的权限", null);
		}
		object.setCreated_at(DateTool.now());
		object.setCreated_user(user.getId());
		int success = packPropertyService.add(object);
		
		//更新缓存
		SystemCache.initPackPropertyList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packproperty/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除装箱单列属性的权限", null);
		}
		int success = packPropertyService.remove(id);
		
		//更新缓存
		SystemCache.initPackPropertyList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public PackProperty get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "packproperty/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看装箱单列属性列表的权限", null);
		}
		PackProperty object = packPropertyService.get(id);
		return object;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(PackProperty object,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packproperty/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑装箱单列属性的权限", null);
		};
		int success = packPropertyService.update(object);
		
		//更新缓存
		SystemCache.initPackPropertyList();
		
		return this.returnSuccess();
		
	}
	
	
}
