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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.LoginedUser;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Authority;
import com.fuwei.entity.Role;
import com.fuwei.entity.Role_Authority;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.RoleService;
import com.fuwei.util.SerializeTool;

@RequestMapping("/authority")
@Controller
public class AuthorityController extends BaseController{
	@Autowired
	AuthorityService authorityService;
	@Autowired
	RoleService roleService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		//判断该用户有没打开打开该页面的权限
		String lcode = "authority/index";
		if(SystemCache.hasAuthority(loginUser, lcode)){
			List<Role> rolelist =  roleService.getList();
			if(rolelist == null){
				rolelist = new ArrayList<Role>();
			}
			request.setAttribute("rolelist", rolelist);
			return new ModelAndView("authority/index");
		}
		else{
			throw new PermissionDeniedDataAccessException("没有查看权限列表的权限", null);
			//return new ModelAndView("authority/error");
		}
		
	}
	
	/*获取某角色的所有权限*/
	@RequestMapping(value = "/get/{roleId}", method = RequestMethod.GET)
	@ResponseBody
	public List<Authority> Index(@PathVariable Integer roleId, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LoginedUser loginUser = SystemContextUtils.getCurrentUser(session);
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		//判断该用户有没打开打开该页面的权限
		String lcode = "authority/index";
		if(SystemCache.hasAuthority(loginUser, lcode)){
			List<Authority> authoritylist = authorityService.getList();
			List<Authority> checkedAuthoritylist =  authorityService.getList(roleId);
			for(Authority authority : authoritylist){
				int authority_id = authority.getId();
				for(Authority checkedItem : checkedAuthoritylist){
					if(checkedItem.getId() == authority_id){
						authority.setChecked(true);
						break;
					}
				}
				
			}
			
			if(authoritylist == null){
				authoritylist = new ArrayList<Authority>();
			}
			return authoritylist;
		}
		else{
			throw new PermissionDeniedDataAccessException("没有查看权限列表的权限", null);
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
	
	//修改某用户的权限
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> put(Integer roleId, String authoritys,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		if(roleId == null){
			throw new Exception("缺少角色");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		//判断该用户有没修改权限的权限
		//判断是否有修改权限的权限
		String lcode = "authority/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		List<Authority> authorityList = SerializeTool.deserializeList(authoritys, Authority.class);
		if(hasAuthority){
			List<Role_Authority> role_authoritylist = new ArrayList<Role_Authority>();
			for(Authority authority:authorityList){
				Role_Authority temp = new Role_Authority();
				temp.setAuthorityId(authority.getId());
				temp.setCreated_user(user.getId());
				temp.setRoleId(roleId);
				role_authoritylist.add(temp);
			}
			authorityService.update(roleId, role_authoritylist);
			//更新User缓存
			SystemCache.initUserList();
			return this.returnSuccess();
		}
		else{
			throw new PermissionDeniedDataAccessException("没有查看权限列表的权限", null);
		}
		
	}
}
