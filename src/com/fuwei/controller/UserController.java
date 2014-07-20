package com.fuwei.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fuwei.commons.LoginedUser;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.entity.Module;
import com.fuwei.entity.Role;
import com.fuwei.entity.Salesman;
import com.fuwei.entity.User;
import com.fuwei.service.ModuleService;
import com.fuwei.service.RoleService;
import com.fuwei.service.UserService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/user")
@Controller
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ModuleService moduleService;
	
	/**
	 * 登录
	 * @throws Exception 
	 * @throws Exception
	 * 
	 */
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> Login (String username, String password,
			HttpSession session,HttpServletResponse response) throws Exception {
		try{
			User user = userService.login(username, password);
			LoginedUser loginUser = new LoginedUser();		
			loginUser.setLoginedUser(user);
			//获取登录用户的角色与权限
			Role role = null;
			List<Module> moduleList = null;
			Integer roleId = user.getRoleId();
			if(roleId != null){
				role = roleService.get(roleId);
				moduleList = moduleService.getList(roleId);
			}
			loginUser.setModulelist(moduleList);
			loginUser.setRole(role);
			//登录成功，若该用户的locked为true,则改为false，且从缓存列表中删除
			if(user.getLocked()){
				userService.unlock(user.getId());		
				//从缓存列表中删除
				SystemCache.removeRelogin(user.getId());
			}
			//登录成功，若该用户的locked为true,则改为false
			session.setAttribute(Constants.LOGIN_SESSION_NAME, loginUser);
			return this.returnSuccess();
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	//退出登录
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public void Logout (HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try{
			session.removeAttribute(Constants.LOGIN_SESSION_NAME);
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;
			response.sendRedirect(basePath + Constants.LOGIN_URL);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index (HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try{
			LoginedUser user =  SystemContextUtils.getCurrentUser(session);
			return new ModelAndView("user/index");
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(User user, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		user.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(user.getName())) ;
		user.setInUse(true);
		user.setCreated_at(DateTool.now());
		user.setUpdated_at(DateTool.now());
		int success = userService.add(user);
		
		//更新缓存
		new SystemCache().initUserList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = userService.remove(id);
		
		//更新缓存
		new SystemCache().initUserList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public User get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = userService.get(id);
		return user;
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(User user, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		user.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(user.getName())) ;
		user.setUpdated_at(DateTool.now());
		int success = userService.update(user);
		
		//更新缓存
		new SystemCache().initUserList();
		
		return this.returnSuccess();
		
	}
	
	//注销用户
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cancel(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = userService.cancel(id);
		
		//更新缓存
		new SystemCache().initUserList();
		
		return this.returnSuccess();
		
	}
	
	//启用用户
	@RequestMapping(value = "/enable/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> enable(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = userService.enable(id);
		
		//更新缓存
		new SystemCache().initUserList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "login/cancel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cancel(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		int success = userService.cancel(user.getId());
		
		//更新缓存
		new SystemCache().initUserList();
		
		return this.returnSuccess();
		
	}
}
