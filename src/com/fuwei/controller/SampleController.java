package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.print.PrintExcel;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.QuotePriceService;
import com.fuwei.service.QuoteService;
import com.fuwei.service.SampleService;
import com.fuwei.util.CompressUtil;
import com.fuwei.util.DateTool;
//import com.fuwei.util.ExportExcel;
import com.fuwei.util.HanyuPinyinUtil;
import com.fuwei.util.SerializeTool;



@RequestMapping("/sample")
@Controller
public class SampleController extends BaseController {
	
	@Autowired
	SampleService sampleService;
	@Autowired
	QuotePriceService quotePriceService;
	@Autowired
	AuthorityService authorityService;
	
//	//生成样品标签
//	@RequestMapping(value="/print_sign/{id}",method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String,Object> printSign(@PathVariable Integer id,HttpSession session,HttpServletRequest request) throws Exception{
//		String lcode = "sample/print_sign";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有生成样品标签的权限", null);
//		}
//		
//		Sample sample = sampleService.get(id);
//		String excelfile_name = Constants.UPLOADEXCEL_Sample_temp + "样品标签" + sample.getId() + "_"
//		+ DateTool.formateDate(new Date(), "yyyyMMddHHmmss") + ".xls";
//		String uploadSite = Constants.UPLOADSite;
//		List<Sample> samplelist = new ArrayList<Sample>();
//		samplelist.add(sample);
//		ExportExcel.exportSampleSignExcel(samplelist,excelfile_name, uploadSite );
//		PrintExcel.printExcel(uploadSite + excelfile_name, true);
//		return this.returnSuccess();
//	}
	
//	//打印样品价格详情
//	@RequestMapping(value="/printDetail/{quotePriceId}",method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String,Object> printDetail(@PathVariable Integer quotePriceId,HttpSession session,HttpServletRequest request) throws Exception{
//		String lcode = "quoteprice/print";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有打印价格详情的权限", null);
//		}
//		
//		QuotePrice quotePrice = quotePriceService.get(quotePriceId);
//		Sample sample = sampleService.get(quotePrice.getSampleId());
//		String excelfile_name = Constants.UPLOADEXCEL_Sample_temp + "样品详情" + sample.getId() + "_"
//		+ DateTool.formateDate(new Date(), "yyyyMMddHHmmss") + ".xls";
//		String uploadSite = Constants.UPLOADSite;
//
//		ExportExcel.exportSampleDetailExcel(sample,quotePrice,uploadSite,excelfile_name,uploadSite );
//		PrintExcel.printExcel(uploadSite + excelfile_name, true);
//		return this.returnSuccess();
//	}
	
	//样品详情
	@RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Detail(@PathVariable Integer id,HttpSession session,HttpServletRequest request) throws Exception{
		
		String lcode = "sample/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看样品详情的权限", null);
		}
		
		Sample sample = sampleService.get(id);
		request.setAttribute("sample", sample);
		List<QuotePrice> quotepricelist = quotePriceService.getList(id);
		request.setAttribute("quotepricelist", quotepricelist);
		return new ModelAndView("sample/detail");
	}
	
	//核价
	@RequestMapping(value="/setDetail",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setDetail(Integer id,Double cost,String detail ,HttpSession session,HttpServletRequest request) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "sample/set_detail";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有样品核价的权限", null);
		}
		
		sampleService.setCost_Detail(id, cost, detail);
		return this.returnSuccess();
	}
	
	//待核价样品列表
	@RequestMapping(value="/undetailedindex",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView undetailedindex(Integer charge_user ,HttpSession session,HttpServletRequest request) throws Exception{
		
		String lcode = "sample/undetailedindex";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看待核价样品列表的权限", null);
		}
		
		List<Sample> samplelist = sampleService.getUnDetailList(charge_user);
		request.setAttribute("samplelist", samplelist);
		request.setAttribute("userlist", SystemCache.userlist);
		request.setAttribute("gongxulist", SystemCache.gongxulist);
		return new ModelAndView("sample/undetailedindex");
	}
	
	//样品管理列表
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/index",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page,String start_time,String end_time,String sortJSON,Integer charge_user,  HttpSession session,HttpServletRequest request) throws Exception{
		String lcode = "sample/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看样品列表的权限", null);
		}
		
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
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
		pager = sampleService.getList(pager,start_time_d,end_time_d,charge_user,sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		request.setAttribute("charge_user", charge_user);
		request.setAttribute("userlist", SystemCache.userlist);
		return new ModelAndView("sample/index");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		return new ModelAndView("sample/add");
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Sample sample,@RequestParam("file") CommonsMultipartFile file,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "sample/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加样品的权限", null);
		}
		
		JSONObject jObject = fileUpload(request,file);
		sample.setImg((String)jObject.get("img"));
		sample.setImg_s((String)jObject.get("img_s"));
		sample.setImg_ss((String)jObject.get("img_ss"));
		
		sample.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(sample.getName())) ;
		sample.setCreated_at(DateTool.now());
		sample.setUpdated_at(DateTool.now());
		sample.setCreated_user(user.getId());
		sample.setHas_detail(false);
		int success = sampleService.add(sample);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "sample/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除样品的权限", null);
		}
		
		int success = sampleService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Sample get(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		String lcode = "sample/detail";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有查看样品详情的权限", null);
//		}
		
		Sample sample = sampleService.get(id);
		return sample;
		
	}
	
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Sample sample = sampleService.get(id);
		request.setAttribute("sample", sample);
		return new ModelAndView("sample/edit");
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Sample sample, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "sample/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑样品的权限", null);
		}
		
		// 转型为MultipartHttpRequest  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
        // 获得上传的文件（根据前台的name名称得到上传的文件）  
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();  
        List<MultipartFile> file = multiValueMap.get("file");  
        if(file!=null && !file.isEmpty()){  
        	JSONObject jObject = fileUpload(request,(CommonsMultipartFile)file.get(0));
    		sample.setImg((String)jObject.get("img"));
    		sample.setImg_s((String)jObject.get("img_s"));
    		sample.setImg_ss((String)jObject.get("img_ss"));
        }  
	
		sample.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(sample.getName())) ;
		sample.setUpdated_at(DateTool.now());
		int success = sampleService.update(sample);
		return this.returnSuccess();
		
	}
	
	/*
     * 采用file.Transto 来保存上传的文件
     */
	
    public JSONObject fileUpload(HttpServletRequest request , CommonsMultipartFile file) throws Exception {
    	String nameString = file.getOriginalFilename();
    	if(nameString.lastIndexOf(".") == -1 || nameString.lastIndexOf(".") == 0){
    		throw new Exception("请上传有效的图片文件，包括 以.bmp,.png,.jpg,.jpeg,.gif为扩展名的文件");
    	}
    	else{
    		String extString = nameString.substring(nameString.lastIndexOf(".")+1,nameString.length());
    		extString = extString.toLowerCase();
    		if(!extString.equals("bmp")  && !extString.equals("png") && !extString.equals("jpg") && !extString.equals("jpeg") && !extString.equals("gif")){
    			throw new Exception("请上传有效的图片文件，包括 以.bmp,.png,.jpg,.jpeg,.gif为扩展名的文件");
    		}
    	}
         long  startTime=System.currentTimeMillis();
        String fileName = new Date().getTime() +file.getOriginalFilename();
        String path = Constants.UPLOADIMGPATH_Sample + fileName;
        
        java.io.File pathFile=new java.io.File(Constants.UPLOADSite + Constants.UPLOADIMGPATH_Sample);
        
        if(!pathFile.exists()){
        	pathFile.mkdirs();
        }
       
        java.io.File newFile=new java.io.File(Constants.UPLOADSite + path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);
        long  endTime=System.currentTimeMillis();
        
        //上传原图后，上传中等缩略图 与 缩略图
        //中等缩略图：样品详情
		String s_filename = CompressUtil.compressPic(Constants.UPLOADSite + Constants.UPLOADIMGPATH_Sample, Constants.UPLOADSite+ Constants.UPLOADIMGPATH_Sample_S, fileName, fileName, 350, 350,"png");
		
		//缩略图：列表
		String ss_filename = CompressUtil.compressPic(Constants.UPLOADSite + Constants.UPLOADIMGPATH_Sample, Constants.UPLOADSite+ Constants.UPLOADIMGPATH_Sample_SS, fileName, fileName, 120, 120,"png");
		
		JSONObject jObject = new JSONObject();
		jObject.put("img",Constants.UPLOADIMGPATH_Sample + fileName );
		jObject.put("img_s", Constants.UPLOADIMGPATH_Sample_S + s_filename);
		jObject.put("img_ss", Constants.UPLOADIMGPATH_Sample_SS + ss_filename);
		
        System.out.println("方法二的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return jObject;  
    }
    
    
    //搜索样品
    @SuppressWarnings("deprecation")
	@RequestMapping(value="/search",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView search(Integer page,String name,String sortJSON,Integer charge_user,  HttpSession session,HttpServletRequest request) throws Exception{
//		String lcode = "sample/index";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有查看样品列表的权限", null);
//		}
		
//		Date start_time_d = DateTool.parse(start_time);
//		Date end_time_d = DateTool.parse(end_time);
    	
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
		pager = sampleService.searchList(pager,name,charge_user,sortList);
		
		request.setAttribute("name", name);
		request.setAttribute("pager", pager);
		request.setAttribute("charge_user", charge_user);
		request.setAttribute("userlist", SystemCache.userlist);
		return new ModelAndView("sample/search");
	}
	
    
    @RequestMapping(value = "/util_price", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView util_price(HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
    	String lcode = "sample/util/price";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有报价工具的权限", null);
		}
		return new ModelAndView("sample/util_price");
		
	}
}
