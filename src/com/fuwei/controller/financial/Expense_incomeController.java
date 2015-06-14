package com.fuwei.controller.financial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.financial.Subject;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.BankService;
import com.fuwei.service.financial.Expense_incomeService;
import com.fuwei.service.financial.SubjectService;
import com.fuwei.util.DateTool;

@RequestMapping("/expense_income")
@Controller
public class Expense_incomeController extends BaseController {
	
	@Autowired
	Expense_incomeService expense_incomeService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	BankService bankService;
	@Autowired
	SubjectService subjectService;
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除支出项的权限", null);
		}
		int success = expense_incomeService.remove(id);
		return this.returnSuccess();
		
	}
	
	// 下载导入模板
	@RequestMapping(value = "/import_download", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> import_download(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createModuleFile(os);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName = "收入支出批量导入模板";

		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
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

	// 批量导入
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView batch_add(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入收入支出的权限", null);
		}
		return new ModelAndView("financial/expense_income/import");
	}

	// 批量导入账户
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> batch_add(
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入收入支出的权限", null);
		}
		List<Expense_income> list = readFile(file);
		List<Bank> banklist = bankService.getList();
		List<Subject> subjectlist = subjectService.getList();
		Map<String,Integer> subject_name_id_map = new HashMap<String, Integer>();
		Map<String,Integer> bank_name_id_map = new HashMap<String, Integer>();
		for(Subject item : subjectlist){
			subject_name_id_map.put(item.getName(), item.getId());
		}
		for(Bank item : banklist){
			bank_name_id_map.put(item.getName(), item.getId());
		}
		
		
		for (Expense_income item : list) {
			//根据银行name,获取id
			if(item.getBank_name()!=null){
				Integer bank_id = bank_name_id_map.get(item.getBank_name());
				if(bank_id != null){
					item.setBank_id(bank_id);
				}else{
					throw new Exception("不存在的银行名称：" + item.getBank_name());
				}
			}
			//根据公司name，获取id
			if(item.getCompany_name()!=null){
				Integer company_id = SystemCache.getCompanyIdByName(item.getCompany_name());
				if(company_id!=null){
					item.setCompany_id(company_id);
				}else{
					throw new Exception("不存在的公司名称：" + item.getCompany_name());
				}
			}
			
			//根据业务员name,获取id
			if(item.getSalesman_name()!=null){
				Integer salesman_id = SystemCache.getSalesmanIdByName(item.getSalesman_name());
				if(salesman_id!=null){
					item.setSalesman_id(salesman_id);
				}else{
					throw new Exception("不存在的业务员名称：" + item.getSalesman_name());
				}
			}
			//根据科目name,获取id
			if(item.getSubject_name()!=null){
				Integer subject_id = subject_name_id_map.get(item.getSubject_name());
				if(subject_id != null){
					item.setSubject_id(subject_id);
				}else{
					throw new Exception("不存在的科目名称：" + item.getSubject_name());
				}
			}
			item.setCreated_at(DateTool.now());
			item.setUpdated_at(DateTool.now());
			item.setCreated_user(user.getId());
		}
		if (list == null || list.size() <= 0) {
			throw new Exception("请至少上传一条记录");
		}
		expense_incomeService.batch_add(list);
		return this.returnSuccess();
	}

	public void createModuleFile(OutputStream os) throws Exception {
		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称
		// 设置Excel字体
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		String[] title = {"年","月","日", "对方账户","公司","业务员","科目","收入", "支出","备注"};
		// 设置Excel表头
		for (int i = 0; i < title.length; i++) {			
			Label excelTitle = new Label(i, 0, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		wsheet.setRowView(1, 400);
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}

	public List<Expense_income> readFile(CommonsMultipartFile file) throws Exception {
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

		List<Expense_income> list = new ArrayList<Expense_income>();

		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {// 从第二行开始
				Cell[] cells = rs.getRow(j);
				// 每行增加一条记录
				Expense_income expense_income = new Expense_income();
				String year = "";
				if (cells[0].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					year = cells[0].getContents().trim();
					if (year.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				String month = "";
				if (cells[1].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					month = cells[1].getContents().trim();
					if (month.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				String day = "";
				if (cells[2].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					day = cells[2].getContents().trim();
					if (day.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				Calendar c = Calendar.getInstance();
				c.set(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day));
				expense_income.setExpense_at(c.getTime());
				
				String bank_name = "";
				if (cells[3].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					bank_name = chinese2English(cells[3].getContents().trim());
					if (bank_name.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				expense_income.setBank_name(bank_name);
				
				String company_name = "";
				if (cells[4].getType() == CellType.EMPTY) {
					company_name = null;// 收款方名称为空的直接跳过
				} else {
					company_name = cells[4].getContents().trim();
					if (company_name.equals("")) {
						company_name = null;
					}
				}
				expense_income.setCompany_name(company_name);
				
				String salesman_name = "";
				if (cells[5].getType() == CellType.EMPTY) {
					salesman_name = null;// 收款方名称为空的直接跳过
				} else {
					salesman_name = cells[5].getContents().trim();
					if (salesman_name.equals("")) {
						salesman_name = null;
					}
				}
				expense_income.setSalesman_name(salesman_name);
				
				String subject_name = "";
				if (cells[6].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					subject_name = cells[6].getContents().trim();
					if (subject_name.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				expense_income.setSubject_name(subject_name);
				
				String incomeStr = "";
				if (cells[7].getType() == CellType.EMPTY) {
					//continue;// 收款方名称为空的直接跳过
				} else {
					incomeStr = cells[7].getContents().trim();
					if (incomeStr.equals("")) {
						//continue;// 收款方名称为空的直接跳过
					}
				}
				
				String expenseStr = "";
				if (cells[8].getType() == CellType.EMPTY) {
					//continue;// 收款方名称为空的直接跳过
				} else {
					expenseStr = cells[8].getContents().trim();
					if (expenseStr.equals("")) {
						//continue;// 收款方名称为空的直接跳过
					}
				}
				if(incomeStr.equals("") && expenseStr.equals("")){//若既没有支出价格，也没有收入价格，则该行跳过
					continue;
				}
				if(!incomeStr.equals("") && !expenseStr.equals("")){//若既有支出价格，也有收入价格，则该行跳过
					continue;
				}
				if(!incomeStr.equals("")){//如果是收入
					expense_income.setIn_out(true);
					expense_income.setAmount(Double.valueOf(incomeStr));
					expense_income.setInvoice_amount(0);
				}else{
					expense_income.setIn_out(false);
					expense_income.setAmount(Double.valueOf(expenseStr));
					expense_income.setInvoice_amount(0);
				}
				
				String memo = "";
				if(cells.length>=10){
					if (cells[9].getType() == CellType.EMPTY) {
						//continue;// 收款方名称为空的直接跳过
					} else {
						memo = cells[9].getContents().trim();
						if (memo.equals("")) {
							//continue;// 收款方名称为空的直接跳过
						}
					}
				}
				
				expense_income.setMemo(memo);
				

				list.add(expense_income);
			}
		}
		is.close();
		return list;

	}
	
	public String chinese2English(String str){
		String[] regs = { "！", "，", "。 ","；","（","）", "!", ",", ".", ";" ,"(",")"};
		for ( int i = 0; i < regs.length / 2; i++ )
		{
		    str = str.replaceAll (regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
}
