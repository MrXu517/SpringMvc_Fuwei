package com.fuwei.controller;


import java.util.ArrayList;
import java.util.Date;
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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Announcement;
import com.fuwei.entity.User;
import com.fuwei.service.AnnouncementService;
import com.fuwei.service.AuthorityService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/announcement")
@Controller
public class AnnouncementController extends BaseController {
	
	@Autowired
	AnnouncementService announcementService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer page, String start_time, String end_time,String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "announcement";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看布告栏通知列表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		
		pager = announcementService.getList(pager, start_time_d, end_time_d,sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		return new ModelAndView("announcement/index");

	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加布告栏通知的权限", null);
		}
		return new ModelAndView("announcement/add");
		
	}
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Announcement announcement,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加布告栏通知的权限", null);
		}
		if(announcement.getTopic()==null || announcement.getTopic().equals("")){
			throw new Exception("主题不能为空");
		}
		if(announcement.getContent()==null || announcement.getContent().equals("")){
			throw new Exception("正文不能为空");
		}
		announcement.setCreated_at(DateTool.now());
		announcement.setUpdated_at(DateTool.now());
		announcement.setCreated_user(user.getId());
		int id = announcementService.add(announcement);	
		return this.returnSuccess("id",id);
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除布告栏通知的权限", null);
		}
		int success = announcementService.remove(id);	
		return this.returnSuccess();
		
	}
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/index";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看布告栏通知的权限", null);
		}
		Announcement announcement = announcementService.get(id);
		request.setAttribute("announcement", announcement);
		return new ModelAndView("announcement/detail");
		
	}
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Announcement get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "announcement/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看布告栏通知列表的权限", null);
		}
		Announcement announcement = announcementService.get(id);
		return announcement;
	}
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView put(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑布告栏通知的权限", null);
		}
		Announcement announcement = announcementService.get(id);
		request.setAttribute("announcement", announcement);
		return new ModelAndView("announcement/edit");	
	}
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Announcement announcement,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑布告栏通知的权限", null);
		}
		if(announcement.getTopic()==null || announcement.getTopic().equals("")){
			throw new Exception("主题不能为空");
		}
		if(announcement.getContent()==null || announcement.getContent().equals("")){
			throw new Exception("正文不能为空");
		}
		announcement.setUpdated_at(DateTool.now());
		int success = announcementService.update(announcement);
		
		return this.returnSuccess("id",announcement.getId());
		
	}
	
	@RequestMapping(value = "/sethomepage/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setHomepage(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "announcement/sethomepage";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有设置首页显示布告栏通知的权限", null);
		}
		int success = announcementService.setHomepage(id);
		
		return this.returnSuccess("id",id);
		
	}
	
	
}
