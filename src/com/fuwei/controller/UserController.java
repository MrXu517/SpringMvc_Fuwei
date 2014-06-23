package com.fuwei.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fuwei.commons.LoginedUser;
import com.fuwei.constant.Constants;
import com.fuwei.entity.User;
import com.fuwei.service.UserService;
import com.fuwei.util.SerializeTool;

@RequestMapping("/user")
@Controller
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 登录
	 * @throws Exception 
	 * @throws Exception
	 * 
	 */
	@ExceptionHandler
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String Login (String username, String password,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		System.out.println(SerializeTool.serialize("fdsa"));
//		return SerializeTool.serialize("fdsa");
		try{
			User user = userService.login(username, password);
			LoginedUser loginUser = new LoginedUser();		
			loginUser.setLoginedUser(user);
			
			session.setAttribute(Constants.LOGIN_SESSION_NAME, loginUser);
//			return this.returnSuccess();
			return "";
		} catch (Exception e) {
			response.getWriter().append("fdddd");
			throw new Exception(e.getMessage(),e);
			
//			e.printStackTrace();
//			Map<String,String> t = new HashMap<String,String>();
//			t.put("cc", e.getMessage());
//			return t;
//			throw e;
//			throw new Exception(e.getMessage());
//			e.printStackTrace();
//			return this.returnSuccess(e.getMessage());
		}
		
	}
	
}
