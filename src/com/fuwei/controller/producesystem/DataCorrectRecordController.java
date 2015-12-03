package com.fuwei.controller.producesystem;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.DataCorrectRecordService;

@RequestMapping("/datacorrectrecord")
@Controller
public class DataCorrectRecordController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	DataCorrectRecordService dataCorrectRecordService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(HttpSession session, HttpServletRequest request) throws Exception {
		String lcode = "data/correct";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看数据纠正记录的权限", null);
		}
		List<DataCorrectRecord> list = dataCorrectRecordService.getList();
		if (list == null) {
			list = new ArrayList<DataCorrectRecord>();
		}
		request.setAttribute("list", list);
		return new ModelAndView("datacorrect/index");
	}
}
