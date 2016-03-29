package com.fuwei.controller.producesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.producesystem.FuliaoCurrentStockService;
import com.fuwei.service.producesystem.FuliaoInNoticeService;
import com.fuwei.service.producesystem.FuliaoOutNoticeService;
import com.fuwei.service.producesystem.FuliaoService;
import com.fuwei.util.CompressUtil;
import com.fuwei.util.DateTool;

@RequestMapping("/fuliao")
@Controller
public class FuliaoController extends BaseController {
	@Autowired
	FuliaoService fuliaoService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	FuliaoCurrentStockService fuliaoCurrentStockService;
	@Autowired
	FuliaoInNoticeService fuliaoInNoticeService;
	@Autowired
	FuliaoOutNoticeService fuliaoOutNoticeService;

	@RequestMapping(value = "/list/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "fuliao/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料列表的权限", null);
		}
		List<Fuliao> fuliaoList = fuliaoService.getList(OrderId);
		if (fuliaoList == null) {
			fuliaoList = new ArrayList<Fuliao>();
		}
		request.setAttribute("fuliaoList", fuliaoList);
		Order order = orderService.get(OrderId);
		request.setAttribute("order", order);
		
		//获取某订单的各辅料总当前库存,只返回fuliaoId和stock_quantity
		Map<Integer,Integer> stockMap = fuliaoCurrentStockService.getStockMapByOrder(OrderId);
		request.setAttribute("stockMap", stockMap);
		
		return new ModelAndView("fuliao/listbyorder");
	}
	
	@RequestMapping(value = "/card/{OrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView card(@PathVariable Integer OrderId,
			HttpSession session, HttpServletRequest request) throws Exception {
		if (OrderId == null) {
			throw new Exception("缺少订单ID");
		}
		String lcode = "fuliao/card";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有打印辅料卡的权限", null);
		}
		List<Fuliao> fuliaoList = fuliaoService.getList(OrderId);
		if (fuliaoList == null) {
			fuliaoList = new ArrayList<Fuliao>();
		}
		request.setAttribute("fuliaoList", fuliaoList);
		Order order = orderService.get(OrderId);
		request.setAttribute("order", order);	
		return new ModelAndView("fuliao/card");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少辅料ID");
		}
		String lcode = "fuliao/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料详情的权限", null);
		}
		Fuliao fuliao = fuliaoService.get(id);
		if (fuliao == null) {
			throw new Exception("找不到ID为" + id + "的辅料");
		}
		//获取辅料当前库存
		Map<String,Object> stockMap = fuliaoCurrentStockService.getByFuliao(id);
		request.setAttribute("stockMap", stockMap);
		//获取预入库、出库记录
		List<Map<String,Object>> inNoticeMap = fuliaoInNoticeService.getByFuliao(id);
		List<Map<String,Object>> outNoticeMap = fuliaoOutNoticeService.getByFuliao(id);
		request.setAttribute("inNoticeMap", inNoticeMap);
		request.setAttribute("outNoticeMap", outNoticeMap);
		//获取辅料仓库出入库记录
		List<Map<String,Object>> storeInOutMap = fuliaoCurrentStockService.inoutByFuliao(id);
		request.setAttribute("storeInOutMap", storeInOutMap);
		//获取当前库位分布
		Map<Integer,Integer> locationMap = fuliaoCurrentStockService.locationByFuliao(id);
		request.setAttribute("locationMap", locationMap);
		
		Order order = orderService.get(fuliao.getOrderId());
		request.setAttribute("fuliao", fuliao);
		request.setAttribute("order", order);

		return new ModelAndView("fuliao/detail");
	}

	// 添加或保存

	@RequestMapping(value = "/{orderId}/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑辅料的权限", null);
		}
		try {
			Order order = orderService.get(orderId);
			request.setAttribute("order", order);
			return new ModelAndView("fuliao/addbyorder");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(Fuliao fuliao,
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑辅料的权限", null);
		}
		try {

			Integer location_size = fuliao.getLocation_size();
			if (location_size == null || location_size == 0) {
				throw new Exception("必须指定库位容量");
			}
			if (location_size != 1 && location_size != 2 && location_size != 3) {
				throw new Exception("不存在的库位容量标志值");
			}

			// 添加
			if (fuliao.getOrderId() == 0) {
				throw new Exception("辅料必须属于一张订单", null);
			}

			Order order = orderService.get(fuliao.getOrderId());
			if (order == null) {
				throw new Exception("订单不存在", null);
			}

			fuliao.setCreated_at(DateTool.now());// 设置创建时间
			fuliao.setCreated_user(user.getId());// 设置创建人

			// 上传图片
			JSONObject jObject = fileUpload(request, file);
			fuliao.setImg((String) jObject.get("img"));
			fuliao.setImg_s((String) jObject.get("img_s"));
			fuliao.setImg_ss((String) jObject.get("img_ss"));
			fuliao.setOrderNumber(order.getOrderNumber());

			int fuliaoId = fuliaoService.add(fuliao);

			int orderId = fuliao.getOrderId();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", fuliaoId);
			data.put("orderId", orderId);
			return this.returnSuccess(data);
		} catch (Exception e) {
			throw e;
		}

	}

	// 2015-3-31 删除
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除辅料的权限", null);
		}
		//删除原先的图片
		Fuliao fuliao = fuliaoService.get(id);
		String filepath = Constants.UPLOADSite + fuliao.getImg();
		File file = new File(filepath);
		if(file.exists()){
		    file.delete();
		}
		String filepath_s = Constants.UPLOADSite + fuliao.getImg_s();
		File file_s = new File(filepath_s);
		if(file_s.exists()){
		    file_s.delete();
		}
		String filepath_ss = Constants.UPLOADSite + fuliao.getImg_ss();
		File file_ss = new File(filepath_ss);
		if(file_ss.exists()){
		    file_ss.delete();
		}
		int success = fuliaoService.remove(id);

		return this.returnSuccess();
	}

	@RequestMapping(value = "/put/{fuliaoId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer fuliaoId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑辅料的权限", null);
		}
		try {
			if (fuliaoId != null) {
				Fuliao fuliao = fuliaoService.get(fuliaoId);
				if (fuliao.getOrderId() != 0) {
					request.setAttribute("fuliao", fuliao);
					return new ModelAndView("fuliao/editbyorder");
				} else {
					throw new Exception("发生错误：辅料缺少订单ID");
					// return new ModelAndView("producing_order/edit");
				}

			}
			throw new Exception("缺少辅料ID");

		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Fuliao fuliao, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "fuliao/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑辅料的权限", null);
		}
		Integer location_size = fuliao.getLocation_size();
		if (location_size == null || location_size == 0) {
			throw new Exception("必须指定库位容量");
		}
		if (location_size != 1 && location_size != 2 && location_size != 3) {
			throw new Exception("不存在的库位容量标志值");
		}
		
		// 转型为MultipartHttpRequest  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
        // 获得上传的文件（根据前台的name名称得到上传的文件）  
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();  
        List<MultipartFile> file = multiValueMap.get("file");  
        if(file!=null && !file.isEmpty()){  
        	JSONObject jObject = fileUpload(request,(CommonsMultipartFile)file.get(0));
    		fuliao.setImg((String)jObject.get("img"));
    		fuliao.setImg_s((String)jObject.get("img_s"));
    		fuliao.setImg_ss((String)jObject.get("img_ss"));
        }  
	
		fuliao.setUpdated_at(DateTool.now());
		int success = fuliaoService.update(fuliao);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", fuliao.getId());
		data.put("orderId", fuliao.getOrderId());
		return this.returnSuccess(data);
		
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail2(Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		return detail(id, session, request);
	}

	public JSONObject fileUpload(HttpServletRequest request,
			CommonsMultipartFile file) throws Exception {
		String nameString = file.getOriginalFilename();
		if (nameString.lastIndexOf(".") == -1
				|| nameString.lastIndexOf(".") == 0) {
			throw new Exception(
					"请上传有效的图片文件，包括 以.bmp,.png,.jpg,.jpeg,.gif为扩展名的文件");
		} else {
			String extString = nameString.substring(
					nameString.lastIndexOf(".") + 1, nameString.length());
			extString = extString.toLowerCase();
			if (!extString.equals("bmp") && !extString.equals("png")
					&& !extString.equals("jpg") && !extString.equals("jpeg")
					&& !extString.equals("gif")) {
				throw new Exception(
						"请上传有效的图片文件，包括 以.bmp,.png,.jpg,.jpeg,.gif为扩展名的文件");
			}
		}
		String fileName = new Date().getTime() + file.getOriginalFilename();
		String path = Constants.UPLOADIMGPATH_fuliao + fileName;

		java.io.File pathFile = new java.io.File(Constants.UPLOADSite
				+ Constants.UPLOADIMGPATH_fuliao);

		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}

		java.io.File newFile = new java.io.File(Constants.UPLOADSite + path);
		// 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
		file.transferTo(newFile);
		long endTime = System.currentTimeMillis();

		// 上传原图后，上传中等缩略图 与 缩略图
		// 中等缩略图：样品详情
		String s_filename = CompressUtil.compressPic(Constants.UPLOADSite
				+ Constants.UPLOADIMGPATH_fuliao, Constants.UPLOADSite
				+ Constants.UPLOADIMGPATH_fuliao_S, fileName, fileName, 350,
				350, "png");

		// 缩略图：列表
		String ss_filename = CompressUtil.compressPic(Constants.UPLOADSite
				+ Constants.UPLOADIMGPATH_fuliao, Constants.UPLOADSite
				+ Constants.UPLOADIMGPATH_fuliao_SS, fileName, fileName, 120,
				120, "png");

		JSONObject jObject = new JSONObject();
		jObject.put("img", Constants.UPLOADIMGPATH_fuliao + fileName);
		jObject.put("img_s", Constants.UPLOADIMGPATH_fuliao_S + s_filename);
		jObject.put("img_ss", Constants.UPLOADIMGPATH_fuliao_SS + ss_filename);

		return jObject;
	}
}
