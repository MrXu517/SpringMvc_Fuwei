package com.fuwei.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.entity.Employee;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.EmployeeService;
import com.fuwei.util.DateTool;
import com.fuwei.util.HanyuPinyinUtil;

@RequestMapping("/employee")
@Controller
public class EmployeeController extends BaseController {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	AuthorityService authorityService;
	
	//2015-5-10花名册
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView list(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "renshi/employees";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看花名册的权限", null);
		}
		List<Employee> list = employeeService.getInUseList();
		request.setAttribute("employeelist", list);
		return new ModelAndView("employee/list");

	}
	
	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer departmentId,Boolean inUse, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "employee";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);		
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有员工管理的权限", null);
		}
		
		request.setAttribute("departmentlist", SystemCache.departmentlist);
		if(departmentId == null){
			if(inUse == null){
				request.setAttribute("employeelist", SystemCache.employeelist);
			}
			else{
				List<Employee> list = new ArrayList<Employee>();
				for(Employee e : SystemCache.employeelist){
					if(inUse == e.getInUse()){
						list.add(e);
					}
				}
				request.setAttribute("employeelist", list);
			}
		} 
		else {
			List<Employee> list = new ArrayList<Employee>();
			for(Employee e : SystemCache.employeelist){
				if(inUse == null){
					if(e.getDepartmentId() == departmentId ){
						list.add(e);
					}
				}else{
					if(e.getDepartmentId() == departmentId && inUse == e.getInUse()){
						list.add(e);
					}
				}
				
			}
			request.setAttribute("employeelist", list);
		}
		
//		String tabname = request.getParameter("tab");
//		Map<String,Object> model = new HashMap<String,Object>();
//		model.put("tab", tabname);
		return new ModelAndView("systeminfo/employee");

	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Employee employee,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加员工的权限", null);
		}
		employee.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(employee.getName())) ;
		employee.setCreated_at(DateTool.now());
		employee.setUpdated_at(DateTool.now());
		employee.setCreated_user(user.getId());
		int success = employeeService.add(employee);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除员工的权限", null);
		}
		int success = employeeService.remove(id);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Employee get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "employee/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看员工列表的权限", null);
		}
		Employee employee = employeeService.get(id);
		return employee;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Employee employee,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑员工的权限", null);
		}
		employee.setHelp_code(HanyuPinyinUtil.getFirstSpellByString(employee.getName())) ;
		employee.setUpdated_at(DateTool.now());
		int success = employeeService.update(employee);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cancel(@PathVariable int id,Date leave_at ,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "employee/cancel";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有设置员工离职的权限", null);
		}
		int success = employeeService.cancel(id,leave_at);
		
		//更新缓存
		new SystemCache().initEmployeeList();
		
		return this.returnSuccess();
		
	}
	
	//2015-5-10导出花名册
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public void export(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		   String fileName="花名册";
	        //填充projects数据	       
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        try {
	        	createEmployeesFile(os);
	        } catch (IOException e) {
	            throw e;
	        }
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
	        
	        
		//String rootpath = request.getSession().getServletContext().getRealPath(
		//"/");
		//String filename = "花名册.xls";
		//File file = new File(rootpath+filename);
//		FileOutputStream os = new FileOutputStream();
//		createEmployeesFile(os);
//		
//		response.setContentType("text/html;charset=UTF-8"); 
//		BufferedInputStream in = null;
//		BufferedOutputStream out = null;
//		request.setCharacterEncoding("UTF-8");
//			
//		try {
//			File f = new File(rootpath + filename);
//			response.setContentType("application/x-excel");
//			response.setCharacterEncoding("UTF-8");
//			response.setHeader("Content-Disposition", "attachment; filename="+filename);
//			response.setHeader("Content-Length",String.valueOf(f.length()));
//			in = new BufferedInputStream(new FileInputStream(f));
//			out = new BufferedOutputStream(response.getOutputStream());
//			byte[] data = new byte[1024];
//			int len = 0;
//			while (-1 != (len=in.read(data, 0, data.length))) {
//				out.write(data, 0, len);
//			}
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			if (in != null) {
//				in.close();
//			}
//			if (out != null) {
//				out.close();
//			}
//		}
	}
	
	public void createEmployeesFile(OutputStream os) throws Exception{
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
			Label excelCompany = new Label(0, 0, "桐庐富伟针织有限公司员工花名册", companyFormat); 
			wsheet.addCell(excelCompany); 
			wsheet.mergeCells(0,0,12,0);
			wsheet.setRowView(0, 800);
			
			//设置Excel字体 
			WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 10, 
			WritableFont.BOLD, false, 
			jxl.format.UnderlineStyle.NO_UNDERLINE, 
			jxl.format.Colour.BLACK); 
			WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
			titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
			titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
			
			
			WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"), 10, 
					WritableFont.NO_BOLD, false, 
					jxl.format.UnderlineStyle.NO_UNDERLINE, 
					jxl.format.Colour.BLACK); 
			WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2); 
			titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);   
			titleFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
			
			String[] title = { "编号", "姓名", "性别", "入厂日期","身份证号码","联系方式","岗位","部门","家庭住址","现居住地","合同期限","用工形式" }; 
			//设置Excel表头 
			int col = 0;
			int merge_col = 0;
			int columnBestWidth[]=new  int[title.length+1];    //保存最佳列宽数据的数组
			for (int i = 0; i < title.length; i++,col++) { 
				columnBestWidth[col] = title[i].getBytes().length;
				Label excelTitle = new Label(col,1, title[i], titleFormat); 
				
				if(title[i].equals("合同期限") ){
					merge_col = col;
					col++;		
					columnBestWidth[col] = title[i].getBytes().length;
					
				}
				wsheet.addCell(excelTitle); 
			} 
			
			wsheet.setRowView(1,400);
			wsheet.mergeCells(merge_col,1,merge_col+1,1);
			
			
			int c = 2; //用于循环时Excel的行号 			
			List<Employee> list = employeeService.getInUseList(); //这个是从数据库中取得要导出的数据 
			
			
			for(Employee employee : list){
				wsheet.setRowView(c,400);
				Label content1 = new Label(0, c, employee.getNumber(),titleFormat2); 
				Label content2 = new Label(1, c, employee.getName(),titleFormat2); 
				Label content3 = new Label(2, c, employee.getSex(),titleFormat2); 
				Label content4 = new Label(3, c, DateTool.formatDateYMD(employee.getEnter_at()),titleFormat2); 
				Label content5 = new Label(4, c, employee.getId_card(),titleFormat2); 
				Label content6 = new Label(5, c, employee.getTel(),titleFormat2); 
				Label content7 = new Label(6, c, employee.getJob(),titleFormat2); 
				Label content8 = new Label(7, c, SystemCache.getDepartmentName(employee.getDepartmentId()),titleFormat2); 
				Label content9 = new Label(8, c, employee.getAddress_home(),titleFormat2); 
				Label content10 = new Label(9, c, employee.getAddress(),titleFormat2); 
				Label content11 = new Label(10, c, DateTool.formatDateYMD(employee.getAgreement_at()),titleFormat2); 
				Label content12 = new Label(11, c, DateTool.formatDateYMD(employee.getAgreement_end()),titleFormat2); 
				Label content13 = new Label(12, c, employee.getEmployee_type(),titleFormat2); 
				
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
				
				int width1 = content1.getContents().getBytes().length;
				int width2 = content2.getContents().getBytes().length;
				int width3 = content3.getContents().getBytes().length;
				int width4 = content4.getContents().getBytes().length;
				int width5 = content5.getContents().getBytes().length;
				int width6 = content6.getContents().getBytes().length;
				int width7 = content7.getContents().getBytes().length;
				int width8 = content8.getContents().getBytes().length;
				int width9 = content9.getContents().getBytes().length;
				int width10 = content10.getContents().getBytes().length;
				int width11 = content11.getContents().getBytes().length;
				int width12 = content12.getContents().getBytes().length;
				int width13 = content13.getContents().getBytes().length;
				if(columnBestWidth[0] < width1){
					columnBestWidth[0] = width1;
				}if(columnBestWidth[1] < width2){
					columnBestWidth[1] = width2;
				}if(columnBestWidth[2] < width3){
					columnBestWidth[2] = width3;
				}if(columnBestWidth[3] < width4){
					columnBestWidth[3] = width4;
				}if(columnBestWidth[4] < width5){
					columnBestWidth[4] = width5;
				}if(columnBestWidth[5] < width6){
					columnBestWidth[5] = width6;
				}if(columnBestWidth[6] < width7){
					columnBestWidth[6] = width7;
				}if(columnBestWidth[7] < width8){
					columnBestWidth[7] = width8;
				}if(columnBestWidth[8] < width9){
					columnBestWidth[8] = width9;
				}if(columnBestWidth[9] < width10){
					columnBestWidth[9] = width10;
				}if(columnBestWidth[10] < width11){
					columnBestWidth[10] = width11;
				}if(columnBestWidth[11] < width12){
					columnBestWidth[11] = width12;
				}if(columnBestWidth[12] < width13){
					columnBestWidth[12] = width13;
				}
				c++; 
			} 
			for(int p = 0 ; p < columnBestWidth.length ; ++p){
				wsheet.setColumnView(p, columnBestWidth[p]+2);
			}
			wbook.write(); //写入文件 
			wbook.close(); 
			os.close(); 
			 
	}
	 public int getChineseNum(String context){    ///统计context中是汉字的个数
	        int lenOfChinese=0;
	        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");    //汉字的Unicode编码范围
	        Matcher m = p.matcher(context);
	        while(m.find()){
	            lenOfChinese++;
	        }
	        return lenOfChinese;
	    }
}
