package com.fuwei.controller.financial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Company;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.financial.Expense_income_invoice;
import com.fuwei.entity.financial.Invoice;
import com.fuwei.entity.financial.SelfAccount;
import com.fuwei.entity.financial.Subject;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.BankService;
import com.fuwei.service.financial.Expense_incomeService;
import com.fuwei.service.financial.Expense_income_invoiceService;
import com.fuwei.service.financial.InvoiceService;
import com.fuwei.service.financial.SubjectService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/sale_invoice")
@Controller
public class SaleInvoiceController extends BaseController {
	
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	Expense_incomeService expense_incomeService;
	@Autowired
	Expense_income_invoiceService expense_income_invoiceService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	BankService bankService;
	@Autowired
	SubjectService subjectService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer page, String start_time, String end_time,Integer companyId, Integer subject_id,Integer bank_id ,Double amount_from , Double amount_to,
			String number,String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "invoice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看发票列表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}

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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);
		
		pager = invoiceService.getList(pager, null,start_time_d, end_time_d,companyId,subject_id,
				false,bank_id,amount_from,amount_to,number, sortList);

		request.setAttribute("companyId", companyId);
		request.setAttribute("subject_id", subject_id);
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("amount_from", amount_from);
		request.setAttribute("amount_to", amount_to);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		List<Subject> subjectlist = SystemCache.getSubjectList(true);
		request.setAttribute("subjectlist", subjectlist);	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		
		return new ModelAndView("financial/workspace/sale_invoice");

	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建销项发票的权限", null);
		}	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("financial/sale_invoice/add");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Invoice invoice,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有创建销项发票的权限", null);
		}
		if(invoice.getCompany_id() == null){
			throw new Exception("公司不能为空");
		}
		if(invoice.getSubject_id() == null){
			throw new Exception("科目不能为空");
		}
		if(invoice.getNumber() == null || invoice.getNumber().equals("")){
			throw new Exception("发票号不能为空");
		}
		invoice.setAmount(NumberUtil.formateDouble(invoice.getAmount(), 2));
		invoice.setCreated_at(DateTool.now());
		invoice.setUpdated_at(DateTool.now());
		invoice.setCreated_user(user.getId());
		invoice.setIn_out(false);
		int id = invoiceService.add(invoice);
		return this.returnSuccess("id", id);
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除发票的权限", null);
		}
		
		int success = invoiceService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Invoice get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "invoice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看发票明细的权限", null);
		}
		Invoice invoice = invoiceService.get(id,false);
		return invoice;
	}
	
//	@RequestMapping(value = "/put", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> update(Invoice invoice,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "invoice/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有编辑发票的权限", null);
//		}
//		invoice.setUpdated_at(DateTool.now());
//		invoice.setIn_out(false);
//		int success = invoiceService.update(invoice);
//		
//		return this.returnSuccess();
//		
//	}
//	
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		String lcode = "invoice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看发票详情的权限", null);
		}

		if (id == null) {
			throw new Exception("缺少发票明细ID");
		}
		Invoice invoice = invoiceService.get(id,false);
		if(invoice == null){
			throw new Exception("找不到ID为" + id + "的销项发票");
		}
		Map<Expense_income,Expense_income_invoice> map = new HashMap<Expense_income, Expense_income_invoice>();
		List<Expense_income_invoice> eiilist = expense_income_invoiceService.getListByInvoiceId(invoice.getId());
		if(eiilist.size()>0){
			String expense_income_ids = "";
			for(Expense_income_invoice temp:eiilist){
				expense_income_ids += temp.getExpense_income_id()+",";			
			}
			
			expense_income_ids = expense_income_ids.substring(0, expense_income_ids.length()-1);
			
			List<Expense_income> eilist = expense_incomeService.getByIds(expense_income_ids);
			
			for(Expense_income Expense_income : eilist){
				
				for(Expense_income_invoice temp:eiilist){
					if(temp.getExpense_income_id() == Expense_income.getId()){
						map.put(Expense_income, temp);
						break;
					}
				}
			}
		}		
		request.setAttribute("invoice", invoice);	
		request.setAttribute("map", map);
		return new ModelAndView("financial/sale_invoice/detail");
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

		String fileName = "发票批量导入模板";

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
		String lcode = "invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入销项发票的权限", null);
		}
		return new ModelAndView("financial/sale_invoice/import");
	}

	// 批量导入
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> batch_add(
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入销项发票的权限", null);
		}
		List<Invoice> list = readFile(file);
		List<Bank> banklist = bankService.getList();
		Map<String,Integer> bank_name_id_map = new HashMap<String, Integer>();
		for(Bank item : banklist){
			bank_name_id_map.put(item.getName(), item.getId());
		}
		
		
		for (Invoice item : list) {
			//根据银行name,获取id
			if(item.getBank_name()!=null){
				Integer bank_id = bank_name_id_map.get(item.getBank_name());
				if(bank_id != null){
					item.setBank_id(bank_id);
				}else{
					throw new Exception("不存在的银行名称：" + item.getBank_name());
				}
			}
			if(item.getSubject_id()==null){
				throw new Exception("科目不能为空：" + item.getBank_name()+","+item.getAmount());
			}
			item.setIn_out(false);
			item.setMatch_amount(0);
			item.setCreated_at(DateTool.now());
			item.setUpdated_at(DateTool.now());
			item.setCreated_user(user.getId());
		}
		if (list == null || list.size() <= 0) {
			throw new Exception("请至少上传一条记录");
		}
		invoiceService.batch_add(list);
		return this.returnSuccess();
	}
	
	// 批量导入
	@RequestMapping(value = "/import_new", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView batch_add_new(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入销项发票的权限", null);
		}
		return new ModelAndView("financial/sale_invoice/import_new");
	}

	// 批量导入
	@RequestMapping(value = "/import_new", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView batch_add_new(
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入销项发票的权限", null);
		}
		List<Invoice> list = readFile_new(file);
		List<Bank> banklist = bankService.getList();
		Map<String,Integer> bank_name_id_map = new HashMap<String, Integer>();
		for(Bank item : banklist){
			bank_name_id_map.put(item.getName(), item.getId());
		}
		
		
		for (Invoice item : list) {
			//根据银行name,获取id
			if(item.getBank_name()!=null){
				Integer bank_id = bank_name_id_map.get(item.getBank_name());
				if(bank_id != null){
					item.setBank_id(bank_id);
				}else{
					item.setBank_id(null);
				}
			}
		}
		if (list == null || list.size() <= 0) {
			throw new Exception("请至少上传一条记录");
		}
		request.setAttribute("list", list);
		return new ModelAndView("financial/sale_invoice/import_new_list");
//		invoiceService.batch_add(list);
//		return this.returnSuccess();
	}
	//银行账户对接
	@RequestMapping(value = "/import_new_list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> import_new_list(
			String details,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入销项发票的权限", null);
		}	
		//判断是否重复导入 （根据流水号、帐号、金额、账户名、日期判断）
		
		List<Invoice> list = SerializeTool.deserializeList(details, Invoice.class);
		if (list == null || list.size() <= 0) {
			throw new Exception("请至少上传一条记录");
		}
		for(Invoice temp : list){
			if(temp.getBank_id()==null || temp.getBank_id()==0){
				throw new Exception("对方开票账户不能为空");
			}
			if(temp.getCompany_id()== null || temp.getCompany_id()==0){
				throw new Exception("公司不能为空");
			}
			if(temp.getSubject_id()== null || temp.getSubject_id()==0){
				throw new Exception("科目不能为空");
			}
			if(temp.getPrint_date()==null){
				throw new Exception("开票日期不能为空");
			}
			temp.setIn_out(false);
			temp.setMatch_amount(0);
			temp.setCreated_at(DateTool.now());
			temp.setUpdated_at(DateTool.now());
			temp.setCreated_user(user.getId());
			temp.setType(3);
		}
		request.setAttribute("list", list);
		invoiceService.batch_add(list);
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

		String[] title = {"年","月","日","公司","科目", "对方账户","发票号","发票类型(1普通发票、2增值税普通发票、3增值税专用发票)","金额", "备注"};
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

	public List<Invoice> readFile(CommonsMultipartFile file) throws Exception {
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

		List<Invoice> list = new ArrayList<Invoice>();
		Map<String,Integer> subject_name_id_map = new HashMap<String, Integer>();
		Map<String,Integer> company_name_id_map = new HashMap<String, Integer>();
		for(Subject item : SystemCache.subjectlist){
			subject_name_id_map.put(item.getName(), item.getId());
		}
		for(Company item : SystemCache.companylist){
			company_name_id_map.put(item.getShortname(), item.getId());
		}
		
		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {// 从第二行开始
				Cell[] cells = rs.getRow(j);
				// 每行增加一条记录
				Invoice invoice = new Invoice();
				String year = "";
				if (cells[0].getType() == CellType.EMPTY) {
					continue;// YEAR为空的直接跳过
				} else {
					year = cells[0].getContents().trim();
					if (year.equals("")) {
						continue;// YEAR名称为空的直接跳过
					}
				}
				String month = "";
				if (cells[1].getType() == CellType.EMPTY) {
					continue;// MONTH为空的直接跳过
				} else {
					month = cells[1].getContents().trim();
					if (month.equals("")) {
						continue;// MONTH为空的直接跳过
					}
				}
				String day = "";
				if (cells[2].getType() == CellType.EMPTY) {
					continue;// DAY为空的直接跳过
				} else {
					day = cells[2].getContents().trim();
					if (day.equals("")) {
						continue;// DAY为空的直接跳过
					}
				}
				Calendar c = Calendar.getInstance();
				c.set(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day));
				invoice.setPrint_date(c.getTime());
				
				String company_name = "";
				if (cells[3].getType() == CellType.EMPTY) {
//					continue;// 公司名称为空的直接跳过
				} else {
					company_name = chinese2English(cells[3].getContents().trim());
					if (!company_name.equals("")) {
						invoice.setCompany_id(company_name_id_map.get(company_name));
					}
				}
				
				
				String subject_name = "";
				if (cells[4].getType() == CellType.EMPTY) {
					continue;// 科目名称为空的直接跳过
				} else {
					subject_name = chinese2English(cells[4].getContents().trim());
					if (subject_name.equals("")) {
						continue;// 科目名称为空的直接跳过
					}
				}
				invoice.setSubject_id(subject_name_id_map.get(subject_name));
				
				String bank_name = "";
				if (cells[5].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					bank_name = chinese2English(cells[5].getContents().trim());
					if (bank_name.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				invoice.setBank_name(bank_name);
				
				String number = "";
				if (cells[6].getType() == CellType.EMPTY) {
					continue;
				} else {
					number = cells[6].getContents().trim();
					if (number.equals("")) {
						continue;
					}
				}
				invoice.setNumber(number);
				
				String type = "";
				if (cells[7].getType() == CellType.EMPTY) {
					continue;
				} else {
					type = cells[7].getContents().trim();
					if (type.equals("")) {
						continue;
					}
				}
				invoice.setType(Integer.valueOf(type));
			
				String amount = "";
				if (cells[8].getType() == CellType.EMPTY) {
					continue;// 直接跳过
				} else {
					amount = cells[8].getContents().trim();
					if (amount.equals("")) {
						continue;// 直接跳过
					}
				}
				invoice.setAmount(Double.valueOf(amount));
				
				String memo = "";
				if(cells.length>=10){
					if (cells[9].getType() == CellType.EMPTY) {
						
					} else {
						memo = cells[9].getContents().trim();
					}
				}
				
				invoice.setMemo(memo);
				

				list.add(invoice);
			}
		}
		is.close();
		return list;

	}
	
	public List<Invoice> readFile_new(CommonsMultipartFile file) throws Exception {
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

		List<Invoice> list = new ArrayList<Invoice>();
		
		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {// 从第二行开始
				Cell[] cells = rs.getRow(j);
				// 每行增加一条记录
				Invoice invoice = new Invoice();

				//22作废标志
				String canceld = "";
				if (cells[22].getType() == CellType.EMPTY) {
				} else {
					canceld = chinese2English(cells[22].getContents().trim());
					if (canceld.equals("是")) {
						continue;// 已作废的记录不导入
					}
				}
				
				//2发票号码
				String number = "";
				if (cells[2].getType() == CellType.EMPTY) {
					continue;
				} else {
					number = cells[2].getContents().trim();
					if (number.equals("")) {
						continue;
					}
					Integer numberd = Integer.parseInt(number);
					number = String.format("%08d",numberd);
				}
				invoice.setNumber(number);
				
				//4对方账户名称
				String bank_name = "";
				if (cells[4].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					bank_name = chinese2English(cells[4].getContents().trim());
					if (bank_name.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				invoice.setBank_name(bank_name);
				
				//8开票日期
				String print_date = "";
				if (cells[8].getType() == CellType.EMPTY) {
					continue;// 开票日期为空的直接跳过
				} else {
					print_date = cells[8].getContents().trim();
					if (!print_date.equals("")) {
						SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.CHINA); 
						Date d = sf.parse(print_date);
						invoice.setPrint_date(d);
					}
				}
				
				//12不含税价、14税额  ， 12+14=总金额
				String unrax_amount_str = "";
				BigDecimal unrax_amount = new BigDecimal(0);
				if (cells[12].getType() == CellType.EMPTY) {
					continue;// 直接跳过
				} else {
					unrax_amount_str = cells[12].getContents().trim();
					if (unrax_amount_str.equals("")) {
						continue;// 直接跳过
					}
					unrax_amount = new BigDecimal(unrax_amount_str);
				}
				String rax_amount_str = "";
				BigDecimal rax_amount = new BigDecimal(0);
				if (cells[14].getType() == CellType.EMPTY) {
					continue;// 直接跳过
				} else {
					rax_amount_str = cells[14].getContents().trim();
					if (rax_amount_str.equals("")) {
						continue;// 直接跳过
					}
					rax_amount = new BigDecimal(rax_amount_str);
				}
				
				invoice.setAmount((unrax_amount.add(rax_amount)).doubleValue());
				
				//17备注
				String memo = "";
				if(cells.length>=10){
					if (cells[17].getType() == CellType.EMPTY) {
						
					} else {
						memo = cells[17].getContents().trim();
					}
				}
				invoice.setMemo(memo);
				
				
				list.add(invoice);
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
