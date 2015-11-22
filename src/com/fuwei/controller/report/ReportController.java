package com.fuwei.controller.report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Factory;
import com.fuwei.entity.FuliaoType;
import com.fuwei.entity.Material;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.MaterialPurchaseOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/report")
@Controller
public class ReportController extends BaseController {
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	PlanOrderService planOrderService;
	@Autowired
	StoreOrderService storeOrderService;
	@Autowired
	HalfCheckRecordOrderService halfCheckRecordOrderService;
	@Autowired
	ColoringOrderService coloringOrderService;
	@Autowired
	MaterialPurchaseOrderService materialPurchaseOrderService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	
	
	@RequestMapping(value = "/material_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView material_purchase(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		
		String lcode = "report/material_purchase";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料采购汇总报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			Sort sort_factory = new Sort();
			sort_factory.setDirection("desc");
			sort_factory.setProperty("factoryId");
			sortList.add(sort_factory);
			
			
			HashMap<Factory,HashMap<Material,Double> > result = materialPurchaseOrderService.material_purchase_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
//			request.setAttribute("pager", pager);
			return new ModelAndView("report/material_purchase");
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	@RequestMapping(value = "/material_purchase_detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView material_purchase_detail(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		if(factoryId == null){
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			List<MaterialPurchaseOrder> result = new ArrayList<MaterialPurchaseOrder>();
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("report/material_purchase_detail");
		}
		String lcode = "report/material_purchase_detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料采购明细报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			
			
			List<MaterialPurchaseOrder> result = materialPurchaseOrderService.material_purchase_detail_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("report/material_purchase_detail");
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/material_purchase_detail/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> material_purchase_detail_export(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		if(factoryId == null){
			return this.returnFail("供货单位ID不能为空");
		}
		String lcode = "report/material_purchase_detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看原材料采购明细报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			
			
			List<MaterialPurchaseOrder> result = materialPurchaseOrderService.material_purchase_detail_report(start_time_d, end_time_d,
					factoryId, sortList);
			
			String factoryName = SystemCache.getFactoryName(factoryId);
			//导出
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			createMaterialPurchase_detailFile(os,result,factoryName,start_time_d,end_time_d);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			
			String fileName= factoryName+"__材料采购明细表" + DateTool.formatDateYMD(start_time_d) +"至"+ DateTool.formatDateYMD(end_time_d);
			
			 // 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        try {
	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(response.getOutputStream());
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (final IOException e) {
	            throw e;
	        } finally {
	            if (bis != null)
	                bis.close();
	            if (bos != null)
	                bos.close();
	        }
			return this.returnSuccess();
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	public void createMaterialPurchase_detailFile(OutputStream os,List<MaterialPurchaseOrder> list,String factory_name,Date start_date ,Date end_date)
			throws Exception {
		
		WritableWorkbook wbook = Workbook.createWorkbook(os); //建立excel文件 
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); //工作表名称 
		
		//设置公司名
		WritableFont companyfont = new WritableFont(WritableFont.createFont("宋体"), 18, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont); 
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		String line0_text = "桐庐富伟针织厂材料采购明细";
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.mergeCells(0,0,7,0);
		wsheet.setRowView(0, 800);
		
		WritableFont dateFont = new WritableFont(WritableFont.createFont("宋体"), 14, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat dateFormat = new WritableCellFormat(dateFont); 
		dateFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		dateFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		String line1_text = DateTool.formatDateYMD(start_date) + "至" + DateTool.formatDateYMD(end_date);
		Label excelDate = new Label(0,1,line1_text , dateFormat);
		wsheet.addCell(excelDate); 
		wsheet.mergeCells(0,1,7,1);
		wsheet.setRowView(0, 800);
		

		WritableCellFormat timeFormat = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 12, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK)); 
		timeFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		Label excelFactory1 = new Label(0,2, "供货单位：" + factory_name, timeFormat); 
		wsheet.addCell(excelFactory1);  
		wsheet.mergeCells(0,2,2,2);
		wsheet.setRowView(2, 600);
		
		//设置Excel字体 
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 12, 
		WritableFont.BOLD, false, 
		jxl.format.UnderlineStyle.NO_UNDERLINE, 
		jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"), 11, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2); 
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		
		String[] title = {"序号", "日期", "采购单号", "品名","公司","跟单","材料","数量" }; 
		
		//设置Excel表头 
		int columnBestWidth[]=new  int[title.length];    //保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) { 	
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i,3, title[i], titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		wsheet.setRowView(3,400);
		
		
		int c = 4; //用于循环时Excel的行号 			
		
		int count = 1 ;
		for(MaterialPurchaseOrder item : list){
			if(item.getDetaillist()==null || item.getDetaillist().size()<=0){
				continue;
			}
			wsheet.setRowView(c,400);
			
			String created_at = DateTool.formatDateYMD(item.getCreated_at());
			String number = item.getNumber();
			String name = item.getName();
			String company_name = SystemCache.getCompanyShortName(item.getCompanyId());
			String employee_name = SystemCache.getEmployeeName(item.getCharge_employee());
			
			
			int kk = 0 ;
			for(MaterialPurchaseOrderDetail detail : item.getDetaillist()){
				Label content2 = new Label(1, c,"",titleFormat2); 
				Label content3 = new Label(2, c,"",titleFormat2); 
				Label content4 = new Label(3, c,"",titleFormat2); 
				Label content5 = new Label(4, c,"" ,titleFormat2); 
				Label content6 = new Label(5, c,"" ,titleFormat2); 
				if(kk==0){
					content2 = new Label(1, c,created_at,titleFormat2); 
					content3 = new Label(2, c,number,titleFormat2); 
					content4 = new Label(3, c,name,titleFormat2); 
					content5 = new Label(4, c,company_name ,titleFormat2); 
					content6 = new Label(5, c,employee_name ,titleFormat2); 
				}
				wsheet.setRowView(c,400);
				Label content1 = new Label(0, c, count+"",titleFormat2); 
				Label content7 = new Label(6, c, SystemCache.getMaterialName(detail.getMaterial()),titleFormat2); 
				Label content8 = new Label(7, c, String.valueOf(new Double(detail.getQuantity()).intValue()),titleFormat2); 
				wsheet.addCell(content2); 
				wsheet.addCell(content3); 
				wsheet.addCell(content4); 
				wsheet.addCell(content5); 
				wsheet.addCell(content6);
				wsheet.addCell(content1); 
				wsheet.addCell(content7); 
				wsheet.addCell(content8); 
				int width2 = content2.getContents().getBytes().length;
				int width3 = content3.getContents().getBytes().length;
				int width4 = content4.getContents().getBytes().length;
				int width5 = content5.getContents().getBytes().length;
				int width6 = content6.getContents().getBytes().length;
				int width1 = content1.getContents().getBytes().length;
				int width7 = content7.getContents().getBytes().length;
				int width8 = content8.getContents().getBytes().length;
				if(columnBestWidth[0] < width1){
					columnBestWidth[0] = width1;
				}
				if(columnBestWidth[6] < width7){
					columnBestWidth[6] = width7;
				}if(columnBestWidth[7] < width8){
					columnBestWidth[7] = width8;
				}
				if(columnBestWidth[1] < width2){
					columnBestWidth[1] = width2;
				}if(columnBestWidth[2] < width3){
					columnBestWidth[2] = width3;
				}if(columnBestWidth[3] < width4){
					columnBestWidth[3] = width4;
				}if(columnBestWidth[4] < width5){
					columnBestWidth[4] = width5;
				}if(columnBestWidth[5] < width6){
					columnBestWidth[5] = width6;
				}
				++kk;
				++count;
				c++; 
			}
			
		} 
		for(int p = 0 ; p < columnBestWidth.length ; ++p){
			wsheet.setColumnView(p, columnBestWidth[p]+3);
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}

	
	
	/*染色报表*/
	//染色汇总报表
	@RequestMapping(value = "/coloring_summary", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView coloring_summary(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		
		String lcode = "report/coloring_summary";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看染色汇总报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			Sort sort_factory = new Sort();
			sort_factory.setDirection("desc");
			sort_factory.setProperty("factoryId");
			sortList.add(sort_factory);
			
			
			HashMap<Factory,HashMap<Material,Double> > result = coloringOrderService.coloring_summary_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
//			request.setAttribute("pager", pager);
			return new ModelAndView("report/coloring_summary");
		} catch (Exception e) {
			throw e;
		}
	}
	@RequestMapping(value = "/coloring_detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView coloring_detail(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		if(factoryId == null){
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			List<MaterialPurchaseOrder> result = new ArrayList<MaterialPurchaseOrder>();
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("report/coloring_detail");
		}
		String lcode = "report/coloring_detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看染色明细报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			
			
			List<ColoringOrder> result = coloringOrderService.coloring_detail_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("report/coloring_detail");
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/coloring_detail/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> coloring_detail_export(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		if(factoryId == null){
			return this.returnFail("供货单位ID不能为空");
		}
		String lcode = "report/coloring_detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看染色明细报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			
			
			List<ColoringOrder> result = coloringOrderService.coloring_detail_report(start_time_d, end_time_d,
					factoryId, sortList);
			
			String factoryName = SystemCache.getFactoryName(factoryId);
			//导出
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			createColoring_detailFile(os,result,factoryName,start_time_d,end_time_d);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			
			String fileName= factoryName+"__染色明细表" + DateTool.formatDateYMD(start_time_d) +"至"+ DateTool.formatDateYMD(end_time_d);
			
			 // 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        try {
	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(response.getOutputStream());
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (final IOException e) {
	            throw e;
	        } finally {
	            if (bis != null)
	                bis.close();
	            if (bos != null)
	                bos.close();
	        }
			return this.returnSuccess();
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	public void createColoring_detailFile(OutputStream os,List<ColoringOrder> list,String factory_name,Date start_date ,Date end_date)
			throws Exception {
		
		WritableWorkbook wbook = Workbook.createWorkbook(os); //建立excel文件 
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); //工作表名称 
		
		//设置公司名
		WritableFont companyfont = new WritableFont(WritableFont.createFont("宋体"), 18, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont); 
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		String line0_text = "桐庐富伟针织厂染色明细";
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.mergeCells(0,0,8,0);
		wsheet.setRowView(0, 800);
		
		WritableFont dateFont = new WritableFont(WritableFont.createFont("宋体"), 14, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat dateFormat = new WritableCellFormat(dateFont); 
		dateFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		dateFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		String line1_text = DateTool.formatDateYMD(start_date) + "至" + DateTool.formatDateYMD(end_date);
		Label excelDate = new Label(0,1,line1_text , dateFormat);
		wsheet.addCell(excelDate); 
		wsheet.mergeCells(0,1,8,1);
		wsheet.setRowView(0, 800);
		

		WritableCellFormat timeFormat = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 12, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK)); 
		timeFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		Label excelFactory1 = new Label(0,2, "染色单位：" + factory_name, timeFormat); 
		wsheet.addCell(excelFactory1);  
		wsheet.mergeCells(0,2,2,2);
		wsheet.setRowView(2, 600);
		
		//设置Excel字体 
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 12, 
		WritableFont.BOLD, false, 
		jxl.format.UnderlineStyle.NO_UNDERLINE, 
		jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"), 11, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2); 
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		
		String[] title = {"序号", "日期", "染色单号", "品名","公司","跟单","材料","数量","颜色" }; 
		
		//设置Excel表头 
		int columnBestWidth[]=new  int[title.length];    //保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) { 	
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i,3, title[i], titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		wsheet.setRowView(3,400);
		
		
		int c = 4; //用于循环时Excel的行号 			
		
		int count = 1 ;
		for(ColoringOrder item : list){
			if(item.getDetaillist()==null || item.getDetaillist().size()<=0){
				continue;
			}
			wsheet.setRowView(c,400);
			
			String created_at = DateTool.formatDateYMD(item.getCreated_at());
			String number = item.getNumber();
			String name = item.getName();
			String company_name = SystemCache.getCompanyShortName(item.getCompanyId());
			String employee_name = SystemCache.getEmployeeName(item.getCharge_employee());
			
			
			int kk = 0 ;
			for(ColoringOrderDetail detail : item.getDetaillist()){
				Label content2 = new Label(1, c,"",titleFormat2); 
				Label content3 = new Label(2, c,"",titleFormat2); 
				Label content4 = new Label(3, c,"",titleFormat2); 
				Label content5 = new Label(4, c,"" ,titleFormat2); 
				Label content6 = new Label(5, c,"" ,titleFormat2); 
				if(kk==0){
					content2 = new Label(1, c,created_at,titleFormat2); 
					content3 = new Label(2, c,number,titleFormat2); 
					content4 = new Label(3, c,name,titleFormat2); 
					content5 = new Label(4, c,company_name ,titleFormat2); 
					content6 = new Label(5, c,employee_name ,titleFormat2); 
				}
				wsheet.setRowView(c,400);
				Label content1 = new Label(0, c, count+"",titleFormat2); 
				Label content7 = new Label(6, c, SystemCache.getMaterialName(detail.getMaterial()),titleFormat2); 
				Label content8 = new Label(7, c, String.valueOf(new Double(detail.getQuantity()).intValue()),titleFormat2); 
				Label content9 = new Label(8, c, detail.getColor(),titleFormat2); 
				wsheet.addCell(content2); 
				wsheet.addCell(content3); 
				wsheet.addCell(content4); 
				wsheet.addCell(content5); 
				wsheet.addCell(content6);
				wsheet.addCell(content1); 
				wsheet.addCell(content7); 
				wsheet.addCell(content8); 
				wsheet.addCell(content9); 
				int width2 = content2.getContents().getBytes().length;
				int width3 = content3.getContents().getBytes().length;
				int width4 = content4.getContents().getBytes().length;
				int width5 = content5.getContents().getBytes().length;
				int width6 = content6.getContents().getBytes().length;
				int width1 = content1.getContents().getBytes().length;
				int width7 = content7.getContents().getBytes().length;
				int width8 = content8.getContents().getBytes().length;
				int width9 = content9.getContents().getBytes().length;
				if(columnBestWidth[0] < width1){
					columnBestWidth[0] = width1;
				}
				if(columnBestWidth[6] < width7){
					columnBestWidth[6] = width7;
				}if(columnBestWidth[7] < width8){
					columnBestWidth[7] = width8;
				}if(columnBestWidth[8] < width9){
					columnBestWidth[8] = width9;
				}
				if(columnBestWidth[1] < width2){
					columnBestWidth[1] = width2;
				}if(columnBestWidth[2] < width3){
					columnBestWidth[2] = width3;
				}if(columnBestWidth[3] < width4){
					columnBestWidth[3] = width4;
				}if(columnBestWidth[4] < width5){
					columnBestWidth[4] = width5;
				}if(columnBestWidth[5] < width6){
					columnBestWidth[5] = width6;
				}
				++kk;
				++count;
				c++; 
			}
			
		} 
		for(int p = 0 ; p < columnBestWidth.length ; ++p){
			wsheet.setColumnView(p, columnBestWidth[p]+2);
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}

	/*染色报表*/
	
	/*辅料采购单报表*/

	@RequestMapping(value = "/fuliao_purchase", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView fuliao_purchase(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		
		String lcode = "report/fuliao_purchase";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料采购汇总报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			Sort sort_factory = new Sort();
			sort_factory.setDirection("desc");
			sort_factory.setProperty("factoryId");
			sortList.add(sort_factory);
			
			
			HashMap<Factory,HashMap<FuliaoType,Double> > result = fuliaoPurchaseOrderService.fuliao_purchase_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
//			request.setAttribute("pager", pager);
			return new ModelAndView("report/fuliao_purchase");
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	@RequestMapping(value = "/fuliao_purchase_detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView fuliao_purchase_detail(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		if(factoryId == null){
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			List<FuliaoPurchaseOrder> result = new ArrayList<FuliaoPurchaseOrder>();
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("report/fuliao_purchase_detail");
		}
		String lcode = "report/fuliao_purchase_detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料采购明细报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			
			
			List<FuliaoPurchaseOrder> result = fuliaoPurchaseOrderService.fuliao_purchase_detail_report(start_time_d, end_time_d,
					factoryId, sortList);
			request.setAttribute("result", result);
			request.setAttribute("start_time", start_time_d);
			request.setAttribute("end_time", end_time_d);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("report/fuliao_purchase_detail");
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/fuliao_purchase_detail/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> fuliao_purchase_detail_export(String start_time, String end_time,
			Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		if(factoryId == null){
			return this.returnFail("供货单位ID不能为空");
		}
		String lcode = "report/fuliao_purchase_detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看辅料采购明细报表的权限", null);
		}
		
		try {
			Date start_time_d = DateTool.parse(start_time);
			Date end_time_d = DateTool.parse(end_time);
			
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
			
			
			List<FuliaoPurchaseOrder> result = fuliaoPurchaseOrderService.fuliao_purchase_detail_report(start_time_d, end_time_d,
					factoryId, sortList);
			
			String factoryName = SystemCache.getFactoryName(factoryId);
			//导出
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			createFuliaoPurchase_detailFile(os,result,factoryName,start_time_d,end_time_d);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			
			String fileName= factoryName+"__辅料采购明细表" + DateTool.formatDateYMD(start_time_d) +"至"+ DateTool.formatDateYMD(end_time_d);
			
			 // 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        try {
	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(response.getOutputStream());
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (final IOException e) {
	            throw e;
	        } finally {
	            if (bis != null)
	                bis.close();
	            if (bos != null)
	                bos.close();
	        }
			return this.returnSuccess();
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	public void createFuliaoPurchase_detailFile(OutputStream os,List<FuliaoPurchaseOrder> list,String factory_name,Date start_date ,Date end_date)
			throws Exception {
		
		WritableWorkbook wbook = Workbook.createWorkbook(os); //建立excel文件 
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); //工作表名称 
		
		//设置公司名
		WritableFont companyfont = new WritableFont(WritableFont.createFont("宋体"), 18, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont); 
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		String line0_text = "桐庐富伟针织厂辅料采购明细";
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.mergeCells(0,0,7,0);
		wsheet.setRowView(0, 800);
		
		WritableFont dateFont = new WritableFont(WritableFont.createFont("宋体"), 14, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat dateFormat = new WritableCellFormat(dateFont); 
		dateFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		dateFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		String line1_text = DateTool.formatDateYMD(start_date) + "至" + DateTool.formatDateYMD(end_date);
		Label excelDate = new Label(0,1,line1_text , dateFormat);
		wsheet.addCell(excelDate); 
		wsheet.mergeCells(0,1,7,1);
		wsheet.setRowView(0, 800);
		

		WritableCellFormat timeFormat = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 12, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK)); 
		timeFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		Label excelFactory1 = new Label(0,2, "供货单位：" + factory_name, timeFormat); 
		wsheet.addCell(excelFactory1);  
		wsheet.mergeCells(0,2,2,2);
		wsheet.setRowView(2, 600);
		
		//设置Excel字体 
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 12, 
		WritableFont.BOLD, false, 
		jxl.format.UnderlineStyle.NO_UNDERLINE, 
		jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"), 11, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2); 
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		
		String[] title = {"序号", "日期", "采购单号", "品名","公司","跟单","材料","数量" }; 
		
		//设置Excel表头 
		int columnBestWidth[]=new  int[title.length];    //保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) { 	
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i,3, title[i], titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		wsheet.setRowView(3,400);
		
		
		int c = 4; //用于循环时Excel的行号 			
		
		int count = 1 ;
		for(FuliaoPurchaseOrder item : list){
			if(item.getDetaillist()==null || item.getDetaillist().size()<=0){
				continue;
			}
			wsheet.setRowView(c,400);
			
			String created_at = DateTool.formatDateYMD(item.getCreated_at());
			String number = item.getNumber();
			String name = item.getName();
			String company_name = SystemCache.getCompanyShortName(item.getCompanyId());
			String employee_name = SystemCache.getEmployeeName(item.getCharge_employee());
			
			
			int kk = 0 ;
			for(FuliaoPurchaseOrderDetail detail : item.getDetaillist()){
				Label content2 = new Label(1, c,"",titleFormat2); 
				Label content3 = new Label(2, c,"",titleFormat2); 
				Label content4 = new Label(3, c,"",titleFormat2); 
				Label content5 = new Label(4, c,"" ,titleFormat2); 
				Label content6 = new Label(5, c,"" ,titleFormat2); 
				if(kk==0){
					content2 = new Label(1, c,created_at,titleFormat2); 
					content3 = new Label(2, c,number,titleFormat2); 
					content4 = new Label(3, c,name,titleFormat2); 
					content5 = new Label(4, c,company_name ,titleFormat2); 
					content6 = new Label(5, c,employee_name ,titleFormat2); 
				}
				wsheet.setRowView(c,400);
				Label content1 = new Label(0, c, count+"",titleFormat2); 
				Label content7 = new Label(6, c, SystemCache.getMaterialName(detail.getStyle()),titleFormat2); 
				Label content8 = new Label(7, c, String.valueOf(new Double(detail.getQuantity()).intValue()),titleFormat2); 
				wsheet.addCell(content2); 
				wsheet.addCell(content3); 
				wsheet.addCell(content4); 
				wsheet.addCell(content5); 
				wsheet.addCell(content6);
				wsheet.addCell(content1); 
				wsheet.addCell(content7); 
				wsheet.addCell(content8); 
				int width2 = content2.getContents().getBytes().length;
				int width3 = content3.getContents().getBytes().length;
				int width4 = content4.getContents().getBytes().length;
				int width5 = content5.getContents().getBytes().length;
				int width6 = content6.getContents().getBytes().length;
				int width1 = content1.getContents().getBytes().length;
				int width7 = content7.getContents().getBytes().length;
				int width8 = content8.getContents().getBytes().length;
				if(columnBestWidth[0] < width1){
					columnBestWidth[0] = width1;
				}
				if(columnBestWidth[6] < width7){
					columnBestWidth[6] = width7;
				}if(columnBestWidth[7] < width8){
					columnBestWidth[7] = width8;
				}
				if(columnBestWidth[1] < width2){
					columnBestWidth[1] = width2;
				}if(columnBestWidth[2] < width3){
					columnBestWidth[2] = width3;
				}if(columnBestWidth[3] < width4){
					columnBestWidth[3] = width4;
				}if(columnBestWidth[4] < width5){
					columnBestWidth[4] = width5;
				}if(columnBestWidth[5] < width6){
					columnBestWidth[5] = width6;
				}
				++kk;
				++count;
				c++; 
			}
			
		} 
		for(int p = 0 ; p < columnBestWidth.length ; ++p){
			wsheet.setColumnView(p, columnBestWidth[p]+3);
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}
	/*辅料采购单报表*/
}
