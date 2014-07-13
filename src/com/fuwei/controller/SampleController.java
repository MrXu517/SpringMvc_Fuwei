package com.fuwei.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.service.QuotePriceService;
import com.fuwei.service.QuoteService;
import com.fuwei.service.SampleService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;



@RequestMapping("/sample")
@Controller
public class SampleController extends BaseController {
	
	@Autowired
	SampleService sampleService;
	@Autowired
	QuotePriceService quotePriceService;
	
	//样品详情
	@RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Detail(@PathVariable Integer id,HttpSession session,HttpServletRequest request) throws Exception{
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
		sampleService.setCost_Detail(id, cost, detail);
		return this.returnSuccess();
	}
	
	//待核价样品列表
	@RequestMapping(value="/undetailedindex",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView undetailedindex(Integer charge_user ,HttpSession session,HttpServletRequest request) throws Exception{
		List<Sample> samplelist = sampleService.getUnDetailList(charge_user);
		request.setAttribute("samplelist", samplelist);
		request.setAttribute("userlist", SystemCache.userlist);
		request.setAttribute("gongxulist", SystemCache.gongxulist);
		return new ModelAndView("sample/undetailedindex");
	}
	
	//样品管理列表
	@RequestMapping(value="/index",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(HttpSession session,HttpServletRequest request) throws Exception{
		List<Sample> samplelist = sampleService.getList();
		request.setAttribute("samplelist", samplelist);
		return new ModelAndView("sample/index");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		request.setAttribute("userlist", SystemCache.userlist);
		return new ModelAndView("sample/add");
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Sample sample,@RequestParam("file") CommonsMultipartFile file,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String img = fileUpload(request,file);
		sample.setImg(img);
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
	public Map<String,Object> delete(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		int success = sampleService.remove(id);
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Sample get(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Sample sample = sampleService.get(id);
		return sample;
		
	}
	
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Sample sample = sampleService.get(id);
		request.setAttribute("userlist", SystemCache.userlist);
		request.setAttribute("sample", sample);
		return new ModelAndView("sample/edit");
		
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Sample sample, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		// 转型为MultipartHttpRequest  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
        // 获得上传的文件（根据前台的name名称得到上传的文件）  
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();  
        List<MultipartFile> file = multiValueMap.get("file");  
        if(file!=null && !file.isEmpty()){  
        	String img = fileUpload(request,(CommonsMultipartFile)file.get(0));
    		sample.setImg(img);
        }  
	
		sample.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(sample.getName())) ;
		sample.setUpdated_at(DateTool.now());
		int success = sampleService.update(sample);
		return this.returnSuccess();
		
	}
	
	/*
     * 采用file.Transto 来保存上传的文件
     */
	
    public String fileUpload(HttpServletRequest request , CommonsMultipartFile file) throws Exception {
         long  startTime=System.currentTimeMillis();
        
        String path = Constants.UPLOADIMGPATH + new Date().getTime() +file.getOriginalFilename();
        
        java.io.File pathFile=new java.io.File(request.getSession().getServletContext().getRealPath("/") + Constants.UPLOADIMGPATH);
        if(!pathFile.exists()){
        	pathFile.mkdir();
        }
        System.out.println("path："+path);
        java.io.File newFile=new java.io.File(request.getSession().getServletContext().getRealPath("/")  + path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);
        long  endTime=System.currentTimeMillis();
        System.out.println("方法二的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return path;  
    }
}
