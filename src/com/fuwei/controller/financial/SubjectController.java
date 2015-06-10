package com.fuwei.controller.financial;

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

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Subject;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.SubjectService;
import com.fuwei.util.DateTool;

@RequestMapping("/subject")
@Controller
public class SubjectController extends BaseController {
	
	@Autowired
	SubjectService subjectService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "subject/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有账户管理的权限", null);
		}
		List<Subject> subjectlist = subjectService.getList();
		request.setAttribute("subjectlist", subjectlist);
		return new ModelAndView("financial/subject/list");

	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Subject subject,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "subject/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加科目的权限", null);
		}
		subject.setCreated_at(DateTool.now());
		subject.setUpdated_at(DateTool.now());
		subject.setCreated_user(user.getId());
		int success = subjectService.add(subject);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "subject/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除科目的权限", null);
		}
		int success = subjectService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Subject get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "subject/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看科目列表的权限", null);
		}
		Subject subject = subjectService.get(id);
		return subject;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Subject subject,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "subject/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑科目的权限", null);
		}
		subject.setUpdated_at(DateTool.now());
		int success = subjectService.update(subject);
		
		return this.returnSuccess();
		
	}
	
	
}
