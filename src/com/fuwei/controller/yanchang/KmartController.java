package com.fuwei.controller.yanchang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.controller.BaseController;
import com.fuwei.util.DateTool;

@RequestMapping("/kmartyanchang")
@Controller
public class KmartController extends BaseController {
	
	@RequestMapping(value = "/plan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView list(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("yanchang/kmart_plan");

	}
	
	//车缝
	@RequestMapping(value = "/sewing_plan", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> fake_salarys(
			@RequestParam("file") CommonsMultipartFile file,String year_month,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//Map<款号，Map<小订单号，Map<生产日期，当日生产数量>>>
		Map<String,Map<String,Map<Date,Integer>>> resultMap = readKmartFile(file);
		//Map<款号，Map<生产日期，当日生产数量>>
		Map<String,Map<Date,Integer>> newMap = new HashMap<String, Map<Date,Integer>>() ;
		for(String styleNo : resultMap.keySet()){
			Map<String,Map<Date,Integer>> detailM = resultMap.get(styleNo);
			Map<Date,Integer> newMap_detail = new HashMap<Date, Integer>();
			for(String orderno : detailM.keySet()){
				Map<Date,Integer> resultMap_detail = detailM.get(orderno);
				for(Date date : resultMap_detail.keySet()){
					int quantity = resultMap_detail.get(date);
					if(newMap_detail.containsKey(date)){
						newMap_detail.put(date, quantity + newMap_detail.get(date));
					}else{
						newMap_detail.put(date, quantity);
					}
				}
			}
			newMap.put(styleNo, newMap_detail);
		}
		
		//导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createSewingFile(os,newMap);
		
		String fileName="桐庐富伟针织厂KMART客人车缝计划表";
		 byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
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

	}
	
	
	
	public Map<String,Map<String,Map<Date,Integer>>> readKmartFile(CommonsMultipartFile file) throws Exception {
		String nameString = file.getOriginalFilename();
		if (nameString.lastIndexOf(".") == -1
				|| nameString.lastIndexOf(".") == 0) {
			throw new Exception("请上传有效的97-2003版Excel文件，包括 以.xls为扩展名的文件");
		} else {
			String extString = nameString.substring(
					nameString.lastIndexOf(".") + 1, nameString.length());
			extString = extString.toLowerCase();
			if (!extString.equals("xls") && !extString.equals("xlsx")) {
				throw new Exception("请上传有效的97-2003版Excel文件，包括 以.xls为扩展名的文件");
			}
		}

		InputStream is = file.getInputStream();
		Workbook rb = Workbook.getWorkbook(is); // 从文件流中获取Excel工作区对象（WorkBook）
		Sheet[] sheet = rb.getSheets();

//		List<Salary> salaryList = new ArrayList<Salary>();
		//Map<款号，Map<小订单号，Map<生产日期，当日生产数量>>>
		Map<String,Map<String,Map<Date,Integer>>> resultMap= new HashMap<String, Map<String,Map<Date,Integer>>>();
		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 5; j < rows; j++) {//从第6行开始
				try{
				Cell[] cells = rs.getRow(j);
				if(cells.length==0){
					break;
				}
				//每行增加一条记录
				//c列是款号
				String styleNo = cells[2].getContents().trim();//款号
				if(styleNo.equals("")){
					continue;
				}
				String detailorderNo = cells[5].getContents().trim();//小订单号
				int planQuantity = Integer.valueOf(cells[9].getContents().trim());//数量
				String sewingstart =  cells[19].getContents().trim();//缝纫开始时间
				if(sewingstart.equals("") || sewingstart.toUpperCase().equals("NO SEWING")){
					continue;
				}
				Date sewingstartdate = DateTool.parse(sewingstart, "yyyy.MM.dd");
				//int sewingmachine = Integer.valueOf(cells[19].getContents().trim());//机器数量
				int sewingQuantityPerDay =  Integer.valueOf(cells[22].getContents().trim());//每台机器每天产量
				double temp = Math.ceil((double)planQuantity/sewingQuantityPerDay);
				int sewingdays = (int)temp;
				
				Map<Date,Integer> dayProduceMap = new HashMap<Date, Integer>();
				for(int k = 0 ; k < sewingdays;++k){
					if(k<sewingdays-1){
						dayProduceMap.put(sewingstartdate , sewingQuantityPerDay);
					}
					if(k==sewingdays-1){
						dayProduceMap.put(sewingstartdate , planQuantity-sewingQuantityPerDay*(k));
					}
					sewingstartdate = DateTool.getNextDayNotHoliday(sewingstartdate);
				}
				if(resultMap.containsKey(styleNo)){
					Map<String,Map<Date,Integer>> detailMap = resultMap.get(styleNo);
					if(detailMap.containsKey(detailorderNo)){
						Map<Date,Integer> tempM = detailMap.get(detailorderNo);
						for(Date date : dayProduceMap.keySet()){
							if(tempM.containsKey(date)){
								tempM.put(date, dayProduceMap.get(date) + tempM.get(date));
							}else{
								tempM.put(date, dayProduceMap.get(date));
							}
						}
						detailMap.put(detailorderNo, tempM);
						resultMap.put(styleNo, detailMap);
						
					}else{
						detailMap.put(detailorderNo, dayProduceMap);
						resultMap.put(styleNo, detailMap);
					}
					
					
				}else{
					Map<String,Map<Date,Integer>> detailMap = new HashMap<String, Map<Date,Integer>>();
					detailMap.put(detailorderNo, dayProduceMap);
					resultMap.put(styleNo, detailMap);
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}
		is.close();
		return resultMap;

	}

	public void createSewingFile(OutputStream os,Map<String,Map<Date,Integer>> resultMap)throws Exception {	
		WritableWorkbook wbook = Workbook.createWorkbook(os); //建立excel文件 
		
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); //工作表名称 
		wsheet.setPageSetup(PageOrientation.LANDSCAPE);//设置打印横向
		wsheet.getSettings().setLeftMargin(0.4);//设置打印边距
		wsheet.getSettings().setRightMargin(0.4);
		wsheet.getSettings().setTopMargin(0.4);
		wsheet.getSettings().setBottomMargin(0.4);
		wsheet.getSettings().setFooterMargin(0);
		wsheet.getSettings().setHeaderMargin(0);
		//设置公司名
		WritableFont companyfont = new WritableFont(WritableFont.createFont("宋体"), 18, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont); 
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		String line0_text = "桐庐富伟针织厂KMART生产计划表——车缝";
		
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.mergeCells(0,0,10,0);
		wsheet.setRowView(0, 800);
		
		
		
		//设置Excel字体 
		// 星期六行的颜色
        WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
                10, WritableFont.BOLD, false, 
        		jxl.format.UnderlineStyle.NO_UNDERLINE,
        		
        		jxl.format.Colour.BLACK); // 字体样式
        WritableCellFormat saturdayformat = new WritableCellFormat(wfont);
        saturdayformat.setBackground(Colour.RED);
        
		//普通字体
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		titleFormat.setWrap(true);		
		wsheet.setRowView(1,400);		

		//总数
		Label totalQuantity = new Label(0, 1,"计划总数" , titleFormat); 
		wsheet.addCell(totalQuantity); 
		
		//Map<日期，行数>
		Map<Date,Integer> rowMap = new HashMap<Date, Integer>();
		Date startDate = DateTool.parse("2016/10/18");
		Date endDate = DateTool.parse("2017/03/20");
		for(int c = 2;startDate.before(endDate);++c ){//写好日期列
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTime(startDate);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){//如果是星期六
				Label content1 = new Label(0, c, DateTool.formatDateYMD(startDate),saturdayformat);
				wsheet.addCell(content1);
			}else{
				Label content1 = new Label(0, c, DateTool.formatDateYMD(startDate),titleFormat);
				wsheet.addCell(content1);
			}
			rowMap.put(startDate, c);
			startDate = DateTool.addDay(startDate, 1);
		}
		
		int cell = 1;
		for(String styleNo : resultMap.keySet()){
			wsheet.setRowView(0,400);
			Label styleNoLabel = new Label(cell, 2, styleNo,titleFormat); //款号行
			wsheet.addCell(styleNoLabel);
			int total_quantity = 0 ;
			Map<Date,Integer> quantityMap = resultMap.get(styleNo);
			for(Date producedate : quantityMap.keySet()){
				int row = rowMap.get(producedate);
				total_quantity = total_quantity + quantityMap.get(producedate);
				Label quantityLabelTemp = new Label(cell, row, quantityMap.get(producedate)+"",titleFormat); //生产数量
				wsheet.addCell(quantityLabelTemp);
				
			}
			Label totalquantityLabel = new Label(cell, 1, total_quantity+"",titleFormat); //款号行
			wsheet.addCell(totalquantityLabel);
			int width = styleNo.getBytes().length;
			wsheet.setColumnView(cell, width+4);
			++cell;
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}
	
	
	//机织
	@RequestMapping(value = "/knitting_plan", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> knitting_plan(
			@RequestParam("file") CommonsMultipartFile file,String year_month,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//Map<款号，Map<生产日期，当日生产数量>>
		Map<String,Map<Date,Integer>> resultMap = null;
		try{
			resultMap = readKmartKnittingFile(file);
		}catch (Exception e) {
			throw e;
		}
		
		//导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createKnittingFile(os,resultMap);
		
		String fileName="桐庐富伟针织厂KMART客人机织计划表";
		 byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
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

	}
	
	
	
	public Map<String,Map<Date,Integer>> readKmartKnittingFile(CommonsMultipartFile file) throws Exception {
		String nameString = file.getOriginalFilename();
		if (nameString.lastIndexOf(".") == -1
				|| nameString.lastIndexOf(".") == 0) {
			throw new Exception("请上传有效的97-2003版Excel文件，包括 以.xls为扩展名的文件");
		} else {
			String extString = nameString.substring(
					nameString.lastIndexOf(".") + 1, nameString.length());
			extString = extString.toLowerCase();
			if (!extString.equals("xls") && !extString.equals("xlsx")) {
				throw new Exception("请上传有效的97-2003版Excel文件，包括 以.xls为扩展名的文件");
			}
		}

		InputStream is = file.getInputStream();
		Workbook rb = Workbook.getWorkbook(is); // 从文件流中获取Excel工作区对象（WorkBook）
		Sheet[] sheet = rb.getSheets();

//		List<Salary> salaryList = new ArrayList<Salary>();
		//Map<款号，Map<生产日期，当日生产数量>>
		Map<String,Map<Date,Integer>> resultMap= new HashMap<String,Map<Date,Integer>>();
		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {//从第2行开始
				try{
					Cell[] cells = rs.getRow(j);
					if(cells.length==0){
						break;
					}
					if(cells.length==1){
						continue;
					}
					//生产日期
					String date_str = cells[0].getContents().trim();
					if(date_str.equals("")){
						continue;
					}
					Date produce_date = DateTool.parse(date_str);
//					//列数必定是 3的备倍数+1 
//					if((cells.length-1)%3!=0){
//						is.close();
//						throw new Exception("列数出错，必须是3的倍数+1");
//					}
					//从第2列开始
					int cell=1;
					for(;cell<cells.length;++cell){
						++cell;//滤去第一个订单明细号
						String styleNo = cells[cell].getContents().trim();//获取款号
						++cell;//下一个是数量
						String quantity_str = cells[cell].getContents().trim();//获取数量
						if(styleNo.equals("")){
							continue;
						}
						if(quantity_str.equals("")){
							continue;
						}
						int quantity = Integer.valueOf(quantity_str);//数量
						if(resultMap.containsKey(styleNo)){
							Map<Date,Integer> tempMap = resultMap.get(styleNo);
							if(tempMap.containsKey(produce_date)){
								tempMap.put(produce_date, quantity+tempMap.get(produce_date));
							}else{
								tempMap.put(produce_date, quantity);
							}
							resultMap.put(styleNo, tempMap);
						}else{
							Map<Date,Integer> tempMap = new HashMap<Date, Integer>();
							tempMap.put(produce_date, quantity);
							resultMap.put(styleNo, tempMap);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}
		is.close();
		return resultMap;

	}

	public void createKnittingFile(OutputStream os,Map<String,Map<Date,Integer>> resultMap)throws Exception {	
		WritableWorkbook wbook = Workbook.createWorkbook(os); //建立excel文件 
		
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); //工作表名称 
		wsheet.setPageSetup(PageOrientation.LANDSCAPE);//设置打印横向
		wsheet.getSettings().setLeftMargin(0.4);//设置打印边距
		wsheet.getSettings().setRightMargin(0.4);
		wsheet.getSettings().setTopMargin(0.4);
		wsheet.getSettings().setBottomMargin(0.4);
		wsheet.getSettings().setFooterMargin(0);
		wsheet.getSettings().setHeaderMargin(0);
		//设置公司名
		WritableFont companyfont = new WritableFont(WritableFont.createFont("宋体"), 18, 
				WritableFont.BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont); 
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		String line0_text = "桐庐富伟针织厂KMART生产计划表——机织";
		
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.mergeCells(0,0,10,0);
		wsheet.setRowView(0, 800);
		
		
		
		
		//设置Excel字体 
		// 星期六行的颜色
        WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
                10, WritableFont.BOLD, false, 
        		jxl.format.UnderlineStyle.NO_UNDERLINE,
        		
        		jxl.format.Colour.BLACK); // 字体样式
        WritableCellFormat saturdayformat = new WritableCellFormat(wfont);
        saturdayformat.setBackground(Colour.RED);
        
		//普通字体
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		titleFormat.setWrap(true);		
		wsheet.setRowView(1,400);	
		
		//总数
		Label totalQuantity = new Label(0, 1,"计划总数" , titleFormat); 
		wsheet.addCell(totalQuantity); 
		
		//Map<日期，行数>
		Map<Date,Integer> rowMap = new HashMap<Date, Integer>();
		Date startDate = DateTool.parse("2016/10/18");
		Date endDate = DateTool.parse("2017/03/20");
		for(int c = 3;startDate.before(endDate);++c ){//写好日期列
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTime(startDate);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){//如果是星期六
				Label content1 = new Label(0, c, DateTool.formatDateYMD(startDate),saturdayformat);
				wsheet.addCell(content1);
			}else{
				Label content1 = new Label(0, c, DateTool.formatDateYMD(startDate),titleFormat);
				wsheet.addCell(content1);
			}
			rowMap.put(startDate, c);
			startDate = DateTool.addDay(startDate, 1);
		}
		
		int cell = 1;
		for(String styleNo : resultMap.keySet()){
			wsheet.setRowView(0,400);
			Label styleNoLabel = new Label(cell, 2, styleNo,titleFormat); //款号行
			wsheet.addCell(styleNoLabel);
			int total_quantity = 0 ;
			Map<Date,Integer> quantityMap = resultMap.get(styleNo);
			for(Date producedate : quantityMap.keySet()){
				int row = rowMap.get(producedate);
				total_quantity = total_quantity + quantityMap.get(producedate);
				Label quantityLabelTemp = new Label(cell, row, quantityMap.get(producedate)+"",titleFormat); //生产数量
				wsheet.addCell(quantityLabelTemp);
				
			}
			Label totalquantityLabel = new Label(cell, 1, total_quantity+"",titleFormat); //款号行
			wsheet.addCell(totalquantityLabel);
			int width = styleNo.getBytes().length;
			wsheet.setColumnView(cell, width+4);
			++cell;
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}
}