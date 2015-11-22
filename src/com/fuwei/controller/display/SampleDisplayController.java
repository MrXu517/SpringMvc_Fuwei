package com.fuwei.controller.display;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.fuwei.commons.SystemSettings;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Sample;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.SampleService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;



@RequestMapping("/sample_display")
@Controller
public class SampleDisplayController extends BaseController {
	
	@Autowired
	SampleService sampleService;
	@Autowired
	AuthorityService authorityService;
	
	//样品详情
	@RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Detail(@PathVariable Integer id,HttpSession session,HttpServletRequest request) throws Exception{
		
		String lcode = "sample_display/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看样品详情的权限", null);
		}
		Sample sample = sampleService.get(id);
		request.setAttribute("sample", sample);
		return new ModelAndView("display/sample_display/detail");
	}
	
	//样品管理列表
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/index",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page,String productNumber,String sortJSON,Integer charge_employee,  HttpSession session,HttpServletRequest request) throws Exception{
		String lcode = "sample_display/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看样品展示列表的权限", null);
		}
		
		Date start_time_d = DateTool.parse(SystemSettings.sample_display_start_at) ;
		Date end_time_d = DateTool.parse(SystemSettings.sample_display_end_at);
		Pager pager = new Pager();
		if(page!=null && page > 0){
			pager.setPageNo(page);
		}
		
		List<Sort> sortList = null;
		if(sortJSON!=null){
			sortList = SerializeTool.deserializeList(sortJSON,Sort.class);
		}
		if(sortList == null){
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		pager = sampleService.getList(pager,start_time_d,end_time_d,productNumber,sortList);
		

		request.setAttribute("pager", pager);
		request.setAttribute("productNumber", productNumber);
		return new ModelAndView("display/sample_display/index");
	}
}
