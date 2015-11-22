package com.fuwei.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fuwei.entity.Message;
import com.fuwei.entity.User;
import com.fuwei.service.MessageService;
import com.fuwei.util.SerializeTool;

@RequestMapping("/message")
@Controller
public class MessageController extends BaseController {
	
	@Autowired
	MessageService messageService;
	
	/*获取当前登录用户的消息*/
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page,String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
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
		pager = messageService.getReceiveList(user.getId(),pager,sortList);
		
		request.setAttribute("pager", pager);
		return new ModelAndView("user/message");
	}
	
	
	@RequestMapping(value = "/unread", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView unread(Integer page,String sortJSON, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
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
		pager = messageService.getReceiveList_UnRead(user.getId(),pager,sortList);
		
		request.setAttribute("pager", pager);
		return new ModelAndView("user/unread");
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Message get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Message message = messageService.get(id);
		if(message.getTo_user_id() != user.getId() && message.getFrom_user_id() != user.getId()){
			throw new Exception("您不是该消息的发送人或者接收人，无法查看");
		}
		return message;
	}
	
	@RequestMapping(value = "/read/{messageId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView read(@PathVariable int messageId, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		Message message = messageService.get(messageId);
		if(message.getTo_user_id() != user.getId()){
			throw new Exception("您不是该消息的接收人，无法读取");
		}
		if(!message.getHas_read()){
			messageService.read(messageId);
			SystemCache.setUserCacheUpdate(message.getTo_user_id(),true);
		}		
		return new ModelAndView("redirect:/" + message.getTo_url());
	}
	

	
	
}
