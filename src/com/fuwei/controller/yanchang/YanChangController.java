package com.fuwei.controller.yanchang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.constant.Holiday;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Factory;
import com.fuwei.entity.Order;
import com.fuwei.entity.Salary;
import com.fuwei.entity.User;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.service.OrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.util.FileUtil;

@RequestMapping("/yanchang")
@Controller
public class YanChangController extends BaseController {
	@Autowired
	OrderService orderService;
	@Autowired
	PlanOrderService planOrderService;
	@Autowired
	HalfCheckRecordOrderService halfCheckRecordOrderService;
	
	//半检记录单打印
	@RequestMapping(value = "/halfcheck_scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView halcheck_scan(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "yanchang";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有扫描半检记录单验厂的权限", null);
		}
		return new ModelAndView("yanchang/halfcheck_scan");

	}
	//半检记录单打印
	@RequestMapping(value = "/halfcheck_print", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView halcheck_print(String number ,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(number == null || number.equals("")){
			throw new Exception("订单号不能为空");
		}
		String lcode = "yanchang";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有打印半检记录单验厂的权限", null);
		}
		Order order = orderService.get(number);
		if(order == null){
			throw new Exception("找不到相应的订单");
		}
		int orderId = order.getId();
		PlanOrder planOrder = planOrderService.getByOrder(orderId);
		HalfCheckRecordOrder halfCheckRecordOrder = halfCheckRecordOrderService.getByOrder(orderId);
		halfCheckRecordOrder.setDetaillist(planOrder.getDetaillist());
		request.setAttribute("order", order);
		request.setAttribute("halfCheckRecordOrder", halfCheckRecordOrder);
		return new ModelAndView("yanchang/halfcheck_print");

	}
	
	
	@RequestMapping(value = "/systemstatus", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView systemstatus(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "yanchang/systemstatus";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有当前验厂状态的权限", null);
		}
		List<User> users = SystemCache.userlist;
		List<User> yanchangUserlist = new ArrayList<User>();
		for(User user : users){
			if(user.getIsyanchang()){
				yanchangUserlist.add(user);
			}
		}
		request.setAttribute("yanchangUserlist", yanchangUserlist);
		

		List<Factory> factorys = SystemCache.factorylist;
		List<Factory> yanchangFactorylist = new ArrayList<Factory>();
		for(Factory item : factorys){
			if(item.getIsyanchang()){
				yanchangFactorylist.add(item);
			}
		}
		request.setAttribute("yanchangFactorylist", yanchangFactorylist);
		
		return new ModelAndView("yanchang/systemstatus");

	}

	@RequestMapping(value = "/yan_salarys", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView list(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "yanchang/fake_salary";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有获取计时工资表的权限", null);
		}
		return new ModelAndView("yanchang/salary_fake");

	}
	
	// 上传考勤统计记录，计算员工工资,并导出
	@RequestMapping(value = "/fake_salarys", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> fake_salarys(
			@RequestParam("file") CommonsMultipartFile file,String year_month,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<Salary> tempList = readKaoqinFile(file);
		
		int year = Integer.valueOf(year_month.substring(0, year_month.indexOf("/")));
		int month = Integer.valueOf(year_month.substring(year_month.indexOf("/")+1));
		List<Salary> salaryList = new ArrayList<Salary>();
		for(Salary salary : tempList){
			Employee tempE = SystemCache.getEmployee(salary.getName());
			if(tempE == null){
				salary.setHour_salary(0.00);
			}else{
				if(tempE.getHour_salary() == null){
					tempE.setHour_salary(0.00);
				}				
				salary.setHour_salary(tempE.getHour_salary());
				salary.setLeave_at(tempE.getLeave_at());
				salary.setDepartmentId(tempE.getDepartmentId());
				if(!tempE.getInUse() && beforeNextSalaryTime(year,month,salary.getLeave_at())){//若该员工已离职，   且在下月发工资日之前离职
					continue;
				}
			}
			salaryList.add(salary);
		}
		
		
		
		for(Salary salary : salaryList){
			//设置加班工资，平时工资，应发工资等
			salary.setWork_money(salary.getWork_hour() * salary.getHour_salary());
			salary.setOver_holiday_money(salary.getOver_holiday() * salary.getHour_salary()*3);
			salary.setOver_weekend_money(salary.getOver_weekend() * salary.getHour_salary()*2);
			salary.setOver_normal_money(salary.getOver_normal() * salary.getHour_salary()*1.5);
			//设置假日补贴
			int holiday_day = Holiday.getHoliday(year, month, salary.getLeave_at()) ;
			salary.setHoliday_reback(holiday_day * 8 * salary.getHour_salary() + salary.getSick_leave() * salary.getHour_salary() + salary.getYear_leave() * salary.getHour_salary());
			salary.setPayable_salary(salary.getOver_holiday_money() + salary.getOver_normal_money() + salary.getOver_weekend_money() + salary.getWork_money()+salary.getHoliday_reback());
			//设置个税	
			salary.personal_tax();
			salary.setReal_salary(salary.getPayable_salary() - salary.getPayable_salary() - salary.getInsurance_deduction());
		}
		
		//导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createSalaryFile(os,salaryList,year,month,null);
		
		String fileName="" + year+"年" + month + "月"+"工资表_yan";
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

	// 上传考勤统计记录，计算离职员工工资,并导出
	@RequestMapping(value = "/fake_salarys_leave", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> fake_salarys_leave(
			@RequestParam("file") CommonsMultipartFile file,@RequestParam("ealier_month") CommonsMultipartFile ealier_monthfile,String year_month,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int year = Integer.valueOf(year_month.substring(0, year_month.indexOf("/")));
		int month = Integer.valueOf(year_month.substring(year_month.indexOf("/")+1));
		
		List<Salary> tempList = readKaoqinFile(file);		
		List<Salary> tempealier_monthList = readKaoqinFile(ealier_monthfile);
		
		HashMap<Integer,List<Salary>>  map = new HashMap<Integer,List<Salary>>();
		 
		for(Salary salary : tempList){
			Employee tempE = SystemCache.getEmployee(salary.getName());
			if(tempE == null){
				continue;
			}else{
				if(tempE.getInUse()){
					continue;
				}
				Calendar leave_at = Calendar.getInstance();
				leave_at.setTime(tempE.getLeave_at());				
				if(leave_at.get(Calendar.MONTH)+1 != month){//如果不是当月离职，则本月无需做该员工的离职工资表
					continue;
				}
				if(tempE.getHour_salary() == null){
					tempE.setHour_salary(0.00);
				}				
				salary.setHour_salary(tempE.getHour_salary());
				salary.setLeave_at(tempE.getLeave_at());
				salary.setDepartmentId(tempE.getDepartmentId());
				salary.setYear(year);
				salary.setMonth(month);
				int day = leave_at.get(Calendar.DAY_OF_MONTH);
				if(map.containsKey(day)){
					List<Salary> salaryList = map.get(day);
					salaryList.add(salary);
					map.put(day, salaryList);
				}else{
					List<Salary> salaryList = new ArrayList<Salary>();
					salaryList.add(salary);
					map.put(day, salaryList);
				}
				
						
			}		
		}
		for(Salary salary : tempealier_monthList){
			Employee tempE = SystemCache.getEmployee(salary.getName());
			if(tempE == null){
				continue;
			}else{
				if(tempE.getInUse()){
					continue;
				}
				Calendar leave_at = Calendar.getInstance();
				leave_at.setTime(tempE.getLeave_at());				
				if(leave_at.get(Calendar.MONTH)+1 != month){//如果不是当月离职，则本月无需做该员工的离职工资表
					continue;
				}
				if(afterEalierMonthSalaryTime(year,month,tempE.getLeave_at())){//如果在上月发工资后离职，则无需计算上个月的工资。 否则，需计算
					continue;
				}
				if(tempE.getHour_salary() == null){
					tempE.setHour_salary(0.00);
				}				
				salary.setHour_salary(tempE.getHour_salary());
				salary.setLeave_at(tempE.getLeave_at());
				salary.setDepartmentId(tempE.getDepartmentId());
				if(month==1){
					salary.setMonth(12);
					salary.setYear(year-1);
				}else{
					salary.setYear(year);
					salary.setMonth(month-1);
				}
								
			
				int day = leave_at.get(Calendar.DAY_OF_MONTH);
				if(map.containsKey(day)){
					List<Salary> salaryList = map.get(day);
					salaryList.add(salary);
					map.put(day, salaryList);
				}else{
					List<Salary> salaryList = new ArrayList<Salary>();
					salaryList.add(salary);
					map.put(day, salaryList);
				}
			}		
		}
		
		if(map.size() == 0){
			return this.returnFail( year + "年" + month + "月无离职记录");
		}
		
//		for(Integer day : map.keySet()){
//			List<Salary> salaryList= map.get(day);
////			Comparator<Salary> com=Collator.getInstance(java.util.Locale.CHINA);
//			java.util.Collections.sort(salaryList);
//			for(Salary salary : salaryList){
//				//设置加班工资，平时工资，应发工资等
//				salary.setWork_money(salary.getWork_hour() * salary.getHour_salary());
//				salary.setOver_holiday_money(salary.getOver_holiday() * salary.getHour_salary()*3);
//				salary.setOver_weekend_money(salary.getOver_weekend() * salary.getHour_salary()*2);
//				salary.setOver_normal_money(salary.getOver_normal() * salary.getHour_salary()*1.5);
//				//设置假日补贴
//				int holiday_day = Holiday.getHoliday(salary.getYear(), salary.getMonth(), salary.getLeave_at()) ;
//				salary.setHoliday_reback(holiday_day * 8 * salary.getHour_salary() + salary.getSick_leave() * salary.getHour_salary() + salary.getYear_leave() * salary.getHour_salary());
//				salary.setPayable_salary(salary.getOver_holiday_money() + salary.getOver_normal_money() + salary.getOver_weekend_money() + salary.getWork_money()+salary.getHoliday_reback());
//				//设置个税	
//				salary.personal_tax();
//				salary.setReal_salary(salary.getPayable_salary() - salary.getPayable_salary() - salary.getInsurance_deduction());
//			}
//			
//			
//			//导出
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			createSalaryFile(os,salaryList,year,month,day);
//			byte[] content = os.toByteArray();
//			InputStream is = new ByteArrayInputStream(content);
//			
//			String fileName="" + year+"年" + month + "月"+"离职工资表_" + day + "号_yan";
//			
//			 // 设置response参数，可以打开下载页面
//	        response.reset();
//	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
//	        BufferedInputStream bis = null;
//	        BufferedOutputStream bos = null;
//	        try {
//	            bis = new BufferedInputStream(is);
//	            bos = new BufferedOutputStream(response.getOutputStream());
//	            byte[] buff = new byte[2048];
//	            int bytesRead;
//	            // Simple read/write loop.
//	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//	                bos.write(buff, 0, bytesRead);
//	            }
//	        } catch (final IOException e) {
//	            throw e;
//	        } finally {
//	            if (bis != null)
//	                bis.close();
//	            if (bos != null)
//	                bos.close();
//	        }
//		}
//	    return this.returnSuccess();
		
		
		Map<InputStream,String> inputStreams = new HashMap<InputStream, String>();
		String zipfileName="" + year+"年" + month + "月"+"离职工资表";
		for(Integer day : map.keySet()){
			List<Salary> salaryList= map.get(day);
//			Comparator<Salary> com=Collator.getInstance(java.util.Locale.CHINA);
			java.util.Collections.sort(salaryList);
			for(Salary salary : salaryList){
				//设置加班工资，平时工资，应发工资等
				salary.setWork_money(salary.getWork_hour() * salary.getHour_salary());
				salary.setOver_holiday_money(salary.getOver_holiday() * salary.getHour_salary()*3);
				salary.setOver_weekend_money(salary.getOver_weekend() * salary.getHour_salary()*2);
				salary.setOver_normal_money(salary.getOver_normal() * salary.getHour_salary()*1.5);
				//设置假日补贴
				int holiday_day = Holiday.getHoliday(salary.getYear(), salary.getMonth(), salary.getLeave_at()) ;
				salary.setHoliday_reback(holiday_day * 8 * salary.getHour_salary() + salary.getSick_leave() * salary.getHour_salary() + salary.getYear_leave() * salary.getHour_salary());
				salary.setPayable_salary(salary.getOver_holiday_money() + salary.getOver_normal_money() + salary.getOver_weekend_money() + salary.getWork_money()+salary.getHoliday_reback());
				//设置个税	
				salary.personal_tax();
				salary.setReal_salary(salary.getPayable_salary() - salary.getPayable_salary() - salary.getInsurance_deduction());
			}
			
			
			//导出
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			createSalaryFile(os,salaryList,year,month,day);
			
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			
			String fileName="" + year+"年" + month + "月"+"离职工资表_" + day + "号_yan.xls";
			inputStreams.put(is, fileName);
			
//			
//			byte[] content = os.toByteArray();
//			InputStream is = new ByteArrayInputStream(content);
//			
//			String fileName="" + year+"年" + month + "月"+"离职工资表_" + day + "号_yan";
//			
//			 // 设置response参数，可以打开下载页面
//	        response.reset();
//	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
//	        BufferedInputStream bis = null;
//	        BufferedOutputStream bos = null;
//	        try {
//	            bis = new BufferedInputStream(is);
//	            bos = new BufferedOutputStream(response.getOutputStream());
//	            byte[] buff = new byte[2048];
//	            int bytesRead;
//	            // Simple read/write loop.
//	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//	                bos.write(buff, 0, bytesRead);
//	            }
//	        } catch (final IOException e) {
//	            throw e;
//	        } finally {
//	            if (bis != null)
//	                bis.close();
//	            if (bos != null)
//	                bos.close();
//	        }
		}
		FileUtil.downLoadFiles(zipfileName,inputStreams, request, response);
	    return this.returnSuccess();

	}
	
	public List<Salary> readKaoqinFile(CommonsMultipartFile file) throws Exception {
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

		List<Salary> salaryList = new ArrayList<Salary>();

		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {//从第二行开始
				Cell[] cells = rs.getRow(j);
				//每行增加一条记录
				Salary salary = new Salary();
				salary.setNumber(cells[0].getContents().trim());//第1个 编号
				salary.setName(cells[1].getContents().trim());//第2个姓名
				
				double work_hour = 0.00;
				if(cells[2].getType() != CellType.EMPTY){
					work_hour = Double.parseDouble(cells[2].getContents().trim());
				}
				salary.setWork_hour(work_hour);//第3个正常白班工作时间
				
				double over_normal = 0.00;
				if(cells[6].getType() != CellType.EMPTY){
					over_normal = Double.parseDouble(cells[6].getContents().trim());
				}
				double over_weekend = 0.00;
				if(cells[7].getType() != CellType.EMPTY){
					over_weekend = Double.parseDouble(cells[7].getContents().trim());
				}
				double over_holiday = 0.00;
				if(cells[8].getType() != CellType.EMPTY){
					over_holiday = Double.parseDouble(cells[8].getContents().trim());
				}
				
				//事假
				double compassionate_leave = 0.00;
				if(cells[9].getType() != CellType.EMPTY){
					compassionate_leave = Double.parseDouble(cells[9].getContents().trim());
				}
				
				//病假
				double sick_leave = 0.00;
				if(cells[10].getType() != CellType.EMPTY){
					sick_leave = Double.parseDouble(cells[10].getContents().trim());
				}
				
				//年假
				double year_leave = 0.00;
				if(cells[11].getType() != CellType.EMPTY){
					year_leave = Double.parseDouble(cells[11].getContents().trim());
				}
				
				salary.setOver_normal(over_normal);//第7个平时加班时间
				salary.setOver_weekend(over_weekend);//第8个周末加班时间
				salary.setOver_holiday(over_holiday);//第9个节假日加班时间
				
				salary.setCompassionate_leave(compassionate_leave);
				salary.setSick_leave(sick_leave);
				salary.setYear_leave(year_leave);
				
				salaryList.add(salary);
			}
		}
		is.close();
		return salaryList;

	}

	public void createSalaryFile(OutputStream os,List<Salary> salaryList,int year ,int month,Integer leave_day)
			throws Exception {
		
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
		
		String line0_text = "桐庐富伟针织厂" + year+"年" + month + "月"+"工资表";
		if(leave_day!=null){
			line0_text =  "桐庐富伟针织厂" + year+"年" + month + "月"+"离职工资表";
		}
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.mergeCells(0,0,17,0);
		wsheet.setRowView(0, 800);
		
		
		
		//设置Excel字体 
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 10, 
		WritableFont.BOLD, false, 
		jxl.format.UnderlineStyle.NO_UNDERLINE, 
		jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		titleFormat.setWrap(true);
		
		//添加 工资发放日期
		String salarytime = "";
		if(leave_day==null){
			Calendar cal = getSalaryTime(year,month);
			salarytime = cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH);
		}else{
			salarytime = year+"/" + month+ "/" + leave_day;
		}
		
		WritableCellFormat timeFormat = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 10, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK)); 
		Label excelSalaryTime1 = new Label(0,1, "发放时间：", timeFormat); 
		Label excelSalaryTime3 = new Label(2,1, salarytime , timeFormat); 
		wsheet.addCell(excelSalaryTime1);  
		wsheet.addCell(excelSalaryTime3); 
		wsheet.mergeCells(0,1,1,1);
		
		
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"), 10, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2); 
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		
		String[] title = {"序号", "姓名", "部门", "时薪","正常出勤工时","正常出勤工资","加班工时","","","加班工资","","","假日补贴","应发工资","代扣税","","实发工资","员工签名" }; 
		String []title2 = {"","","","","","","平时","周末","节假日","平时","周末","节假日","","","保险税","个税","",""};
		//设置Excel表头 
		int columnBestWidth[]=new  int[title.length];    //保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) { 	
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i,2, title[i], titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		for (int i = 0; i < title2.length; i++) { 	
			int tempLength = title2[i].getBytes().length;
			if(tempLength>columnBestWidth[i]){
				columnBestWidth[i] = tempLength;
			}
			Label excelTitle = new Label(i,3, title2[i], titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		
		wsheet.setRowView(1,400);
		wsheet.mergeCells(0,2,0,3);
		wsheet.mergeCells(1,2,1,3);
		wsheet.mergeCells(2,2,2,3);
		wsheet.mergeCells(3,2,3,3);
		wsheet.mergeCells(4,2,4,3);
		wsheet.mergeCells(5,2,5,3);
		wsheet.mergeCells(6,2,8,2);
		wsheet.mergeCells(9,2,11,2);
		wsheet.mergeCells(14,2,15,2);
		wsheet.mergeCells(12,2,12,3);
		wsheet.mergeCells(13,2,13,3);
		wsheet.mergeCells(16,2,16,3);
		wsheet.mergeCells(17,2,17,3);
		
		int c = 4; //用于循环时Excel的行号 			
		
		int count = 1 ;
		//数字字体格式
		jxl.write.NumberFormat nf_double = new jxl.write.NumberFormat("0.0");    //设置数字格式：小数保留两位小数
		jxl.write.WritableCellFormat wcfN_double = new jxl.write.WritableCellFormat(nf_double); //设置表单格式    
		wcfN_double.setAlignment(jxl.format.Alignment.CENTRE);  
		wcfN_double.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		wcfN_double.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfN_double.setFont(wfont2);
		
		WritableFont wfont_sign = new WritableFont(WritableFont.createFont("宋体"), 11, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.GREY_25_PERCENT); 
		WritableCellFormat cellFot_sign = new WritableCellFormat(wfont_sign); 
		cellFot_sign.setAlignment(jxl.format.Alignment.CENTRE);   
		cellFot_sign.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		
		for(Salary salary : salaryList){
			wsheet.setRowView(c,400);
			Label content1 = new Label(0, c, count+"",titleFormat2); 
			++count;
			Label content2 = new Label(1, c, salary.getName(),titleFormat2); 
			Label content3 = new Label(2, c, SystemCache.getDepartmentName(salary.getDepartmentId()),titleFormat2); 
			jxl.write.Number content4 = new jxl.write.Number(3, c, salary.getHour_salary(),wcfN_double); 
			jxl.write.Number content5 = new jxl.write.Number(4, c, salary.getWork_hour(),wcfN_double); 
			Formula content6 = new Formula(5, c, "D" +(c+1)+"*E"+(c+1),wcfN_double); 
			jxl.write.Number content7 = new jxl.write.Number(6, c, salary.getOver_normal(),wcfN_double); 
			jxl.write.Number content8 = new jxl.write.Number(7, c, salary.getOver_weekend(),wcfN_double); 
			jxl.write.Number content9 = new jxl.write.Number(8, c, salary.getOver_holiday(),wcfN_double); 
			Formula content10 = new Formula(9, c, "1.5*D" +(c+1)+"*G"+(c+1),wcfN_double); 
			Formula content11 = new Formula(10, c, "2*D" +(c+1)+"*H"+(c+1),wcfN_double); 
			Formula content12 = new Formula(11, c, "3*D" +(c+1)+"*I"+(c+1),wcfN_double); 
			jxl.write.Number content13 = new jxl.write.Number(12, c, salary.getHoliday_reback(),wcfN_double); 
			Formula content14 = new Formula(13, c, "F" +(c+1)+"+J"+(c+1)+"+K"+(c+1)+"+L"+(c+1)+"+M"+(c+1),wcfN_double); 
			jxl.write.Number content15 = new jxl.write.Number(14, c, salary.getInsurance_deduction(),wcfN_double); 
			jxl.write.Number content16 = new jxl.write.Number(15, c, salary.getPersonal_tax(),wcfN_double); 
			Formula content17 = new Formula(16, c, "N" +(c+1)+"-O"+(c+1)+"-P"+(c+1),wcfN_double); 
			Formula content18 = new Formula(17, c, "B" +(c+1),cellFot_sign); 
			
			wsheet.addCell(content1); 
			wsheet.addCell(content2); 
			wsheet.addCell(content3); 
			wsheet.addCell(content4); 
			wsheet.addCell(content5); 
			wsheet.addCell(content6); 
			wsheet.addCell(content7); 
			wsheet.addCell(content8); 
			wsheet.addCell(content9); 
			wsheet.addCell(content10); 
			wsheet.addCell(content11); 
			wsheet.addCell(content12); 
			wsheet.addCell(content13);
			wsheet.addCell(content14);
			wsheet.addCell(content15);
			wsheet.addCell(content16);
			wsheet.addCell(content17);
			wsheet.addCell(content18);
			
			int width1 = content1.getContents().getBytes().length;
			int width2 = content2.getContents().getBytes().length;
			int width3 = content3.getContents().getBytes().length;
			int width4 = content4.getContents().getBytes().length;
			int width5 = 8;//content5.getContents().getBytes().length;
			int width6 = 8;//content6.getContents().getBytes().length;
			int width7 = 5;//content7.getContents().getBytes().length;
			int width8 = 5;//content8.getContents().getBytes().length;
			int width9 = 6;//content9.getContents().getBytes().length;
			int width10 = 8;//content10.getContents().getBytes().length;
			int width11 = 8;//content11.getContents().getBytes().length;
			int width12 = 8;//content12.getContents().getBytes().length;
			int width13 = content13.getContents().getBytes().length;
			int width14 = 8;//content14.getContents().getBytes().length;
			int width15 = content15.getContents().getBytes().length;
			int width16 = content16.getContents().getBytes().length;
			int width17 = 8;//content17.getContents().getBytes().length;
			int width18 = 8;//content18.getContents().getBytes().length;
			if(columnBestWidth[0] < width1){
				columnBestWidth[0] = width1;
			}if(columnBestWidth[1] < width2){
				columnBestWidth[1] = width2;
			}if(columnBestWidth[2] < width3){
				columnBestWidth[2] = width3;
			}if(columnBestWidth[3] < width4){
				columnBestWidth[3] = width4;
			}
//			if(columnBestWidth[4] < width5){
				columnBestWidth[4] = width5;
//			}
//			if(columnBestWidth[5] < width6){
				columnBestWidth[5] = width6;
//			}
//			if(columnBestWidth[6] < width7){
				columnBestWidth[6] = width7;
//			}if(columnBestWidth[7] < width8){
				columnBestWidth[7] = width8;
//			}if(columnBestWidth[8] < width9){
				columnBestWidth[8] = width9;
//			}
			if(columnBestWidth[9] < width10){
				columnBestWidth[9] = width10;
			}if(columnBestWidth[10] < width11){
				columnBestWidth[10] = width11;
			}if(columnBestWidth[11] < width12){
				columnBestWidth[11] = width12;
			}if(columnBestWidth[12] < width13){
				columnBestWidth[12] = width13;
			}if(columnBestWidth[13] < width14){
				columnBestWidth[13] = width14;
			}if(columnBestWidth[14] < width15){
				columnBestWidth[14] = width15;
			}if(columnBestWidth[15] < width16){
				columnBestWidth[15] = width16;
			}if(columnBestWidth[16] < width17){
				columnBestWidth[16] = width17;
			}if(columnBestWidth[17] < width18){
				columnBestWidth[17] = width18;
			}
			c++; 
		}
		//合计行
		jxl.write.NumberFormat nf_double_noborder = new jxl.write.NumberFormat("0.0");    //设置数字格式：小数保留两位小数
		jxl.write.WritableCellFormat wcfN_double_noborder = new jxl.write.WritableCellFormat(nf_double_noborder); //设置表单格式    
		wcfN_double_noborder.setAlignment(jxl.format.Alignment.CENTRE);  
		wcfN_double_noborder.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfN_double_noborder.setFont(wfont2);

		Label content19 = new Label(15, c, "合计",timeFormat); 
		Formula content20 = new Formula(16, c, "SUM(Q5:Q" +c+")",wcfN_double_noborder); 
		wsheet.addCell(content19);
		wsheet.addCell(content20);
		int width19 = content19.getContents().getBytes().length;
		int width20 = content20.getContents().getBytes().length;
		if(columnBestWidth[15] < width19){
			columnBestWidth[15] = width19;
		}if(columnBestWidth[16] < width20){
			columnBestWidth[16] = width20;
		}
		for(int p = 0 ; p < columnBestWidth.length ; ++p){
			wsheet.setColumnView(p, columnBestWidth[p]+1);
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}

	
	public boolean afterEalierMonthSalaryTime(int year , int month , Date leave_at){
		if(leave_at == null){
			return false;
		}
		if(month==1){
			month = 12;
			year = year-1;
		}else{
			month = month-1;
		}
		Calendar cal = getSalaryTime(year,month);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(leave_at);
		return !cal2.before(cal);//[>=],在上月发工资[后或发工资当天]离职
	}
	
	public boolean beforeNextSalaryTime(int year , int month , Date leave_at){
		if(leave_at == null){
			return false;
		}
		
		Calendar cal = getSalaryTime(year,month);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(leave_at);
		return cal2.before(cal); // [<]
	}
	public Calendar getSalaryTime(int year , int month){		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		int tempMon = month + 1;
		int tempY = year;
		if(month == 12){
			month = 1;
			tempY = tempY + 1;
		}
		cal.set(tempY, tempMon-1, 28);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		if(year <= 2015 && month <=2){//2015年2月及之前 是周日放假
			if(week == Calendar.SUNDAY){//如果是星期天，则工资发放日期 往前移一天
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
		}else{
			if(week == Calendar.SATURDAY){//星期六
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
		}	
		return cal;
	}
}