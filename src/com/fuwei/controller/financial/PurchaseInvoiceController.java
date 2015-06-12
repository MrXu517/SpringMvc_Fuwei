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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.financial.Expense_income_invoice;
import com.fuwei.entity.financial.Invoice;
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

@RequestMapping("/purchase_invoice")
@Controller
public class PurchaseInvoiceController extends BaseController {
	
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
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer page, String start_time, String end_time,Integer bank_id ,Double amount_from , Double amount_to,
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
		
		pager = invoiceService.getList(pager, start_time_d, end_time_d,
				true,bank_id,amount_from,amount_to,number, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("amount_from", amount_from);
		request.setAttribute("amount_to", amount_to);
		request.setAttribute("number", number);
		request.setAttribute("pager", pager);
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		
		return new ModelAndView("financial/workspace/purchase_invoice");

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
			throw new PermissionDeniedDataAccessException("没有收取发票的权限", null);
		}	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("financial/purchase_invoice/add");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Invoice invoice,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有收取发票的权限", null);
		}
		invoice.setAmount(NumberUtil.formateDouble(invoice.getAmount(), 2));
		invoice.setCreated_at(DateTool.now());
		invoice.setUpdated_at(DateTool.now());
		invoice.setCreated_user(user.getId());
		invoice.setIn_out(true);
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
		Invoice invoice = invoiceService.get(id);
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
		Invoice invoice = invoiceService.get(id);
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
		return new ModelAndView("financial/purchase_invoice/detail");
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
		String lcode = "purchase_invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入发票的权限", null);
		}
		return new ModelAndView("financial/purchase_invoice/import");
	}

	// 批量导入
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> batch_add(
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "purchase_invoice/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入发票的权限", null);
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
			item.setIn_out(true);
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

		String[] title = {"年","月","日", "对方账户","发票号","发票类型(1普通发票、2增值税普通发票、3增值税专用发票)","金额", "备注"};
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

		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {// 从第二行开始
				Cell[] cells = rs.getRow(j);
				// 每行增加一条记录
				Invoice invoice = new Invoice();
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
				invoice.setPrint_date(c.getTime());
				
				String bank_name = "";
				if (cells[3].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					bank_name = chinese2English(cells[3].getContents().trim());
					if (bank_name.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				invoice.setBank_name(bank_name);
				
				String number = "";
				if (cells[4].getType() == CellType.EMPTY) {
					continue;
				} else {
					number = cells[4].getContents().trim();
					if (number.equals("")) {
						continue;
					}
				}
				invoice.setNumber(number);
				
				String type = "";
				if (cells[5].getType() == CellType.EMPTY) {
					continue;
				} else {
					type = cells[5].getContents().trim();
					if (type.equals("")) {
						continue;
					}
				}
				invoice.setType(Integer.valueOf(type));
			
				String amount = "";
				if (cells[6].getType() == CellType.EMPTY) {
					continue;// 直接跳过
				} else {
					amount = cells[6].getContents().trim();
					if (amount.equals("")) {
						continue;// 直接跳过
					}
				}
				invoice.setAmount(Double.valueOf(amount));
				
				String memo = "";
				if(cells.length>=8){
					if (cells[7].getType() == CellType.EMPTY) {
						
					} else {
						memo = cells[7].getContents().trim();
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
