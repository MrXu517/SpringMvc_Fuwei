package com.fuwei.controller.producesystem;

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

import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;

/*原材料工作台*/
@RequestMapping("/workspace")
@Controller
public class WorkspaceController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/material_workspace", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView workspace(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producesystem/material_workspace";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有原材料工作台的权限",
					null);
		}
		return new ModelAndView("store_in_out/workspace");
	}
	
	@RequestMapping(value = "/half_workspace", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView half_workspace(HttpSession session,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producesystem/halfstoreorder_workspace";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有半成品工作台的权限",
					null);
		}
		return new ModelAndView("half_store_in_out/workspace");
	}
}
