package com.fuwei.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;

@RequestMapping("/util")
@Controller
public class UtilController extends BaseController {
	
	@RequestMapping(value = "/barcode", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView barcode(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "util/barcode";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有条形码生成器的权限", null);
		}
		return new ModelAndView("util/barcode");

	}
	
	@RequestMapping(value = "/box", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView box(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "util/box";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有装箱工具的权限", null);
		}
		return new ModelAndView("util/box");

	}
	
	@RequestMapping(value = "/box", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView box_print(String ASOS_ORDER_NUMBER,String ASOS_MDA_NUMBER , String ASOS_Style_Number,String UNITS_PER_CARTON,String CARTON_NUMBER,String ASOS_SKU_Number,Integer total_number, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(total_number == null){
			throw new Exception("总箱数不能为空");
		}
		session.setAttribute("ASOS_ORDER_NUMBER", ASOS_ORDER_NUMBER);
		session.setAttribute("ASOS_MDA_NUMBER", ASOS_MDA_NUMBER);
		session.setAttribute("ASOS_Style_Number", ASOS_Style_Number);
		session.setAttribute("UNITS_PER_CARTON", UNITS_PER_CARTON);
		session.setAttribute("CARTON_NUMBER", CARTON_NUMBER);
		session.setAttribute("ASOS_SKU_Number", ASOS_SKU_Number);
		session.setAttribute("total_number", total_number);
		
		return new ModelAndView("util/box_print");

	}
	
	@RequestMapping(value = "/box_german", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView box_german_print(String ASOS_ORDER_NUMBER,String ASOS_MDA_NUMBER , String ASOS_Style_Number,String UNITS_PER_CARTON,String CARTON_NUMBER,String ASOS_SKU_Number,Integer total_number, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(total_number == null){
			throw new Exception("总箱数不能为空");
		}
		session.setAttribute("ASOS_ORDER_NUMBER", ASOS_ORDER_NUMBER);
		session.setAttribute("ASOS_MDA_NUMBER", ASOS_MDA_NUMBER);
		session.setAttribute("ASOS_Style_Number", ASOS_Style_Number);
		session.setAttribute("UNITS_PER_CARTON", UNITS_PER_CARTON);
		session.setAttribute("CARTON_NUMBER", CARTON_NUMBER);
		session.setAttribute("ASOS_SKU_Number", ASOS_SKU_Number);
		session.setAttribute("total_number", total_number);
		
		return new ModelAndView("util/box_print_german");

	}
}
