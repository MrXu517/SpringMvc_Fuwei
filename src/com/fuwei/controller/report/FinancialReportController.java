package com.fuwei.controller.report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
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

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.financial.Subject;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.report.Payable;
import com.fuwei.entity.report.Receivable;
import com.fuwei.service.financial.BankService;
import com.fuwei.service.financial.Expense_incomeService;
import com.fuwei.service.financial.FinancialReportService;
import com.fuwei.service.financial.InvoiceService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/report/financial")
@Controller
public class FinancialReportController extends BaseController {

	@Autowired
	InvoiceService invoiceService;
	@Autowired
	BankService bankService;
	@Autowired
	FinancialReportService financialReportService;
	@Autowired
	Expense_incomeService expense_incomeService;

	/* 进项发票报表 */
	@RequestMapping(value = "/purchase_invoice", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView invoice(Integer page, String start_time,
			String end_time, Integer companyId, Integer subject_id,
			Boolean un_paid, Integer bank_id, String sortJSON,
			HttpSession session, HttpServletRequest request) throws Exception {
		String lcode = "report/financial/purchase_invoice";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看进项发票报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = { "amount", "match_amount" };
		pager.setTotal_colnames(total_colnames);

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
		pager = invoiceService.getList(pager, un_paid, start_time_d,
				end_time_d, companyId, subject_id, true, bank_id, null, null,
				null, sortList);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("un_paid", un_paid);
		request.setAttribute("companyId", companyId);
		request.setAttribute("subject_id", subject_id);
		List<Subject> subjectlist = SystemCache.getSubjectList(false);
		request.setAttribute("subjectlist", subjectlist);
		List<Bank> banklist = bankService.getList(true);
		request.setAttribute("banklist", banklist);
		return new ModelAndView("report/financial/purchase_invoice");
	}

	/* 进项发票报表 */

	/* 应付报表 */
	@RequestMapping(value = "/payable", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView payable(Integer page, String start_time,
			String end_time, Integer companyId, Integer salesmanId,
			Boolean in_out, Integer bank_id, Integer subject_id,
			Double amount_from, Double amount_to, String sortJSON,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "report/financial/payable";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看应付报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = { "payable", "pay", "un_pay", "un_invoiced" };
		pager.setTotal_colnames(total_colnames);

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("record_at");
		sortList.add(sort);
		pager = financialReportService.getPayableList(pager, start_time_d, end_time_d,
				companyId, salesmanId, subject_id, bank_id, sortList);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("salesmanId", salesmanId);
		request.setAttribute("companyId", companyId);
		request.setAttribute("subject_id", subject_id);
		List<Subject> subjectlist = SystemCache.getSubjectList(false);
		request.setAttribute("subjectlist", subjectlist);
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("report/financial/payable");
	}

	

	/* 收支报表 */
	@RequestMapping(value = "/expense_income", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView workspace_expense_income(Integer page,
			String start_time, String end_time, Integer companyId,
			Integer salesmanId, Boolean in_out, Integer bank_id,
			Integer subject_id, String sortJSON, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String lcode = "report/financial/expense_income";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看收支报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = { "amount" };
		pager.setTotal_colnames(total_colnames);

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("expense_at");
		sortList.add(sort);

		pager = expense_incomeService.getList(pager, start_time_d, end_time_d,
				companyId, salesmanId, in_out, bank_id, subject_id, null, null,
				sortList);
		Map<String, Object> total_map = new HashMap<String, Object>();

		if (in_out == null) {
			Map<String, Object> income_map = expense_incomeService.getTotal(
					pager, start_time_d, end_time_d, companyId, salesmanId,
					true, bank_id, subject_id, null, null, sortList);
			total_map.put("income_amount", income_map.get("amount"));
			Map<String, Object> expense_map = expense_incomeService.getTotal(
					pager, start_time_d, end_time_d, companyId, salesmanId,
					false, bank_id, subject_id, null, null, sortList);

			total_map.put("expense_amount", expense_map.get("amount"));
		} else if (in_out) {// 收入
			Map<String, Object> income_map = expense_incomeService.getTotal(
					pager, start_time_d, end_time_d, companyId, salesmanId,
					true, bank_id, subject_id, null, null, sortList);
			total_map.put("income_amount", income_map.get("amount"));
			total_map.put("expense_amount", 0.0);
		} else {// 支出
			Map<String, Object> expense_map = expense_incomeService.getTotal(
					pager, start_time_d, end_time_d, companyId, salesmanId,
					false, bank_id, subject_id, null, null, sortList);
			total_map.put("expense_amount", expense_map.get("amount"));
			total_map.put("income_amount", 0.0);
		}
		pager.setTotal(total_map);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		request.setAttribute("companyId", companyId);
		request.setAttribute("in_out", in_out);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("subject_id", subject_id);

		request.setAttribute("pager", pager);
		List<Subject> subjectlist = SystemCache.subjectlist;
		request.setAttribute("subjectlist", subjectlist);
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("report/financial/expense_income");

	}
	/* 收支报表 导出 */
	@RequestMapping(value = "/expense_income/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> workspace_expense_income_export(Integer page,
			String start_time, String end_time, Integer companyId,
			Integer salesmanId, Boolean in_out, Integer bank_id,
			Integer subject_id, String sortJSON, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String lcode = "report/financial/expense_income";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看收支报表的权限", null);
		}
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
		sort.setProperty("expense_at");
		sortList.add(sort);

		List<Expense_income> list = expense_incomeService.getList_export(start_time_d, end_time_d,
				companyId, salesmanId, in_out, bank_id, subject_id,sortList);

		String companyName = companyId==null?"": SystemCache.getCompanyName(companyId);
		String salesmanName = salesmanId==null?"": SystemCache.getSalesmanName(salesmanId);
		String subjectName = subject_id == null?"":SystemCache.getSubjectName(subject_id);
		String bank_name = "";
		if (bank_id != null) {
			Bank bank = bankService.get(bank_id);
			bank_name = bank.getName();
		}
		String nameTemp = "";
		if(!companyName.equals("")){
			nameTemp = nameTemp + companyName + "_";
		}
		if(!salesmanName.equals("")){
			nameTemp = nameTemp + salesmanName + "_";
		}
		if(!subjectName.equals("")){
			nameTemp = nameTemp + subjectName + "_";
		}
		if(!bank_name.equals("")){
			nameTemp = nameTemp + bank_name + "_";
		}
		if(!nameTemp.equals("")){
			nameTemp = nameTemp.substring(0, nameTemp.length()-1);
		}
		
		if(start_time_d!=null){
			nameTemp = "从" + nameTemp + DateTool.formatDateYMD(start_time_d);
		}
		if(end_time_d!=null){
			nameTemp = "到" + nameTemp + DateTool.formatDateYMD(end_time_d);
		}
		
		if(in_out!=null){
			if(in_out){
				nameTemp = nameTemp + "_收入";
			}else{
				nameTemp = nameTemp + "_支出";
			}
		}
		
		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createExpenseIncome_detailFile(os, list, nameTemp);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName = nameTemp + "__收支报表";

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
	public void createExpenseIncome_detailFile(OutputStream os,
			List<Expense_income> list, String nameTemp) throws Exception {

		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称

		// 设置公司名
		WritableFont companyfont = new WritableFont(WritableFont
				.createFont("宋体"), 18, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont);
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		String line0_text = "桐庐富伟针织厂收支报表";
		Label excelCompany = new Label(0, 0, line0_text, companyFormat);
		wsheet.addCell(excelCompany);
		wsheet.mergeCells(0, 0, 8, 0);
		wsheet.setRowView(0, 800);

		// 设置Excel字体
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"),
				11, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2);
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat2.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框

		String[] title = { "序号", "收入", "支出", "对方账户", "科目", "公司", "业务员", "收付款时间","备注"};

		// 设置Excel表头
		int columnBestWidth[] = new int[title.length]; // 保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) {
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i, 1, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		wsheet.setRowView(3, 400);

		int c = 2; // 用于循环时Excel的行号

		int count = 1;
		for (Expense_income item : list) {
			wsheet.setRowView(c, 400);

			String expense_at = DateTool.formatDateYMD(item.getExpense_at());
			String bank_name = item.getBank_name();
			String subject_name = item.getSubject_name();
			String company_name = SystemCache.getCompanyShortName(item
					.getCompany_id());
			String salesman_name = SystemCache.getSalesmanName(item
					.getSalesman_id());
			String income_amount = item.getIn_out() == true ? String.valueOf(item.getAmount()) :"";
			String expense_amount = item.getIn_out() == false?  String.valueOf(item.getAmount()):"";
			String memo = item.getMemo() == null ? "":item.getMemo();
			
			Label content1 = new Label(0, c, count + "", titleFormat2);
			Label content2 = new Label(1, c, income_amount, titleFormat2);
			Label content3 = new Label(2, c, expense_amount, titleFormat2);
			Label content4 = new Label(3, c, bank_name, titleFormat2);
			Label content5 = new Label(4, c, subject_name, titleFormat2);
			Label content6 = new Label(5, c, company_name, titleFormat2);
			Label content7 = new Label(6, c, salesman_name, titleFormat2);
			Label content8 = new Label(7, c, expense_at, titleFormat2);
			Label content9 = new Label(8, c, memo, titleFormat2);

			
			wsheet.addCell(content1);
			wsheet.addCell(content2);
			wsheet.addCell(content3);
			wsheet.addCell(content4);
			wsheet.addCell(content5);
			wsheet.addCell(content6);
			wsheet.addCell(content7);
			wsheet.addCell(content8);
			wsheet.addCell(content9);
			int width1 = content1.getContents().getBytes().length;
			int width2 = content2.getContents().getBytes().length;
			int width3 = content3.getContents().getBytes().length;
			int width4 = content4.getContents().getBytes().length;
			int width5 = content5.getContents().getBytes().length;
			int width6 = content6.getContents().getBytes().length;
			int width7 = content7.getContents().getBytes().length;
			int width8 = content8.getContents().getBytes().length;
			int width9 = content9.getContents().getBytes().length;
			if (columnBestWidth[0] < width1) {
				columnBestWidth[0] = width1;
			}
			if (columnBestWidth[1] < width2) {
				columnBestWidth[1] = width2;
			}
			if (columnBestWidth[2] < width3) {
				columnBestWidth[2] = width3;
			}
			if (columnBestWidth[3] < width4) {
				columnBestWidth[3] = width4;
			}
			if (columnBestWidth[4] < width5) {
				columnBestWidth[4] = width5;
			}
			if (columnBestWidth[5] < width6) {
				columnBestWidth[5] = width6;
			}
			if (columnBestWidth[6] < width7) {
				columnBestWidth[6] = width7;
			}
			if (columnBestWidth[7] < width8) {
				columnBestWidth[7] = width8;
			}
			if (columnBestWidth[8] < width9) {
				columnBestWidth[8] = width9;
			}
			
			++count;
			c++;

		}
		for (int p = 0; p < columnBestWidth.length; ++p) {
			wsheet.setColumnView(p, columnBestWidth[p] + 2);
		}
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}

	/*收支报表 -- 导出*/
	
	/*应付报表 -- 导出*/
	@RequestMapping(value = "/payable/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> payable_export(Integer page, String start_time,
			String end_time, Integer companyId, Integer salesmanId,
			Boolean in_out, Integer bank_id, Integer subject_id,
			Double amount_from, Double amount_to, String sortJSON,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "report/financial/payable";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看应付报表的权限", null);
		}
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
		sort.setProperty("record_at");
		sortList.add(sort);
		List<Payable> list = financialReportService.getPayableList_export(
				start_time_d, end_time_d, companyId, salesmanId, subject_id,
				bank_id, sortList);

		String companyName = companyId==null?"": SystemCache.getCompanyName(companyId);
		String salesmanName = salesmanId==null?"": SystemCache.getSalesmanName(salesmanId);
		String subjectName = subject_id == null?"":SystemCache.getSubjectName(subject_id);
		String bank_name = "";
		if (bank_id != null) {
			Bank bank = bankService.get(bank_id);
			bank_name = bank.getName();
		}
		String nameTemp = "";
		if(!companyName.equals("")){
			nameTemp = nameTemp + companyName + "_";
		}
		if(!salesmanName.equals("")){
			nameTemp = nameTemp + salesmanName + "_";
		}
		if(!subjectName.equals("")){
			nameTemp = nameTemp + subjectName + "_";
		}
		if(!bank_name.equals("")){
			nameTemp = nameTemp + bank_name + "_";
		}
		if(!nameTemp.equals("")){
			nameTemp = nameTemp.substring(0, nameTemp.length()-1);
		}
		if(start_time_d!=null){
			nameTemp = "从" + nameTemp + DateTool.formatDateYMD(start_time_d);
		}
		if(end_time_d!=null){
			nameTemp = "到" + nameTemp + DateTool.formatDateYMD(end_time_d);
		}
		
		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createPayble_detailFile(os, list, nameTemp);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName = nameTemp + "__应付报表";
		
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

	public void createPayble_detailFile(OutputStream os,
			List<Payable> list, String nameTemp) throws Exception {

		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称

		// 设置公司名
		WritableFont companyfont = new WritableFont(WritableFont
				.createFont("宋体"), 18, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont);
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		String line0_text = "桐庐富伟针织厂应付报表";
		Label excelCompany = new Label(0, 0, line0_text, companyFormat);
		wsheet.addCell(excelCompany);
		wsheet.mergeCells(0, 0, 8, 0);
		wsheet.setRowView(0, 800);

		// 设置Excel字体
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"),
				11, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2);
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat2.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框

		String[] title = { "序号", "类型", "对方账户", "应付", "已付", "未匹配", "日期", "备注" };

		// 设置Excel表头
		int columnBestWidth[] = new int[title.length]; // 保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) {
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i, 1, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		wsheet.setRowView(3, 400);

		int c = 2; // 用于循环时Excel的行号

		int count = 1;
		for (Payable item : list) {
			wsheet.setRowView(c, 400);
			
			String type = item.getTypeString();
			String bank_name = item.getBank_name();
			String date = DateTool.formatDateYMD(item.getRecord_at());//付款时间or收发票时间
			String payable = "";//应付
			String pay = "";//已付
			String un_invoice = "";//未付
			if(item.getType().equals("invoice")){
				payable = String.valueOf(item.getPayable()) ;
			}else{
				pay = String.valueOf(item.getPay()) ;
				un_invoice = String.valueOf(item.getUn_invoiced()) ;
			}
			String memo = item.getMemo() == null? "":item.getMemo();
			
			Label content1 = new Label(0, c, count + "", titleFormat2);
			Label content2 = new Label(1, c, type, titleFormat2);
			Label content3 = new Label(2, c, bank_name, titleFormat2);
			Label content4 = new Label(3, c, payable, titleFormat2);
			Label content5 = new Label(4, c, pay, titleFormat2);
			Label content6 = new Label(5, c, un_invoice, titleFormat2);
			Label content7 = new Label(6, c, date, titleFormat2);
			Label content8 = new Label(7, c, memo, titleFormat2);
			wsheet.addCell(content1);
			wsheet.addCell(content2);
			wsheet.addCell(content3);
			wsheet.addCell(content4);
			wsheet.addCell(content5);
			wsheet.addCell(content6);
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
			if (columnBestWidth[0] < width1) {
				columnBestWidth[0] = width1;
			}
			if (columnBestWidth[6] < width7) {
				columnBestWidth[6] = width7;
			}
			if (columnBestWidth[7] < width8) {
				columnBestWidth[7] = width8;
			}
			
			if (columnBestWidth[1] < width2) {
				columnBestWidth[1] = width2;
			}
			if (columnBestWidth[2] < width3) {
				columnBestWidth[2] = width3;
			}
			if (columnBestWidth[3] < width4) {
				columnBestWidth[3] = width4;
			}
			if (columnBestWidth[4] < width5) {
				columnBestWidth[4] = width5;
			}
			if (columnBestWidth[5] < width6) {
				columnBestWidth[5] = width6;
			}
			++count;
			c++;
		}
		for (int p = 0; p < columnBestWidth.length; ++p) {
			wsheet.setColumnView(p, columnBestWidth[p] + 2);
		}
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}

	/* 应付报表导出 */
	
	
	/* 销项发票报表 */
	@RequestMapping(value = "/sale_invoice", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView sale_invoice(Integer page, String start_time,
			String end_time, Integer companyId, Integer subject_id,
			Boolean un_received, Integer bank_id, String sortJSON,
			HttpSession session, HttpServletRequest request) throws Exception {
		String lcode = "report/financial/sale_invoice";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看销项发票报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = { "amount", "match_amount" };
		pager.setTotal_colnames(total_colnames);

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("print_date");
		sortList.add(sort);
		pager = invoiceService.getList(pager, un_received, start_time_d,
				end_time_d, companyId, subject_id, false, bank_id, null, null,
				null, sortList);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("un_received", un_received);
		request.setAttribute("companyId", companyId);
		request.setAttribute("subject_id", subject_id);
		List<Subject> subjectlist = SystemCache.getSubjectList(true);
		request.setAttribute("subjectlist", subjectlist);
		List<Bank> banklist = bankService.getList(true);
		request.setAttribute("banklist", banklist);
		return new ModelAndView("report/financial/sale_invoice");
	}

	/* 销项发票报表 */

	/* 应收报表 */
	@RequestMapping(value = "/receivable", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView receivable(Integer page, String start_time,
			String end_time, Integer companyId, Integer salesmanId,
			Boolean in_out, Integer bank_id, Integer subject_id,
			Double amount_from, Double amount_to, String sortJSON,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "report/financial/receivable";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看应收报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = { "receivable", "received", "un_received", "un_invoiced" };
		pager.setTotal_colnames(total_colnames);

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("happen_at");
		sortList.add(sort);
		pager = financialReportService.getReceivableList(pager, start_time_d, end_time_d,
				companyId, salesmanId, subject_id, bank_id, sortList);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("salesmanId", salesmanId);
		request.setAttribute("companyId", companyId);
		request.setAttribute("subject_id", subject_id);
		List<Subject> subjectlist = SystemCache.getSubjectList(true);
		request.setAttribute("subjectlist", subjectlist);
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("report/financial/receivable");
	}
	/*应收报表 -- 导出*/
	@RequestMapping(value = "/receivable/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> receivable_export(Integer page, String start_time,
			String end_time, Integer companyId, Integer salesmanId,
			Boolean in_out, Integer bank_id, Integer subject_id,
			Double amount_from, Double amount_to, String sortJSON,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "report/financial/receivable";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有导出应收报表的权限", null);
		}
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
		sort.setProperty("happen_at");
		sortList.add(sort);
		List<Receivable> list = financialReportService.getReceivableList_export(
				start_time_d, end_time_d, companyId, salesmanId, subject_id,
				bank_id, sortList);

		String companyName = companyId==null?"": SystemCache.getCompanyName(companyId);
		String salesmanName = salesmanId==null?"": SystemCache.getSalesmanName(salesmanId);
		String subjectName = subject_id == null?"":SystemCache.getSubjectName(subject_id);
		String bank_name = "";
		if (bank_id != null) {
			Bank bank = bankService.get(bank_id);
			bank_name = bank.getName();
		}
		String nameTemp = "";
		if(!companyName.equals("")){
			nameTemp = nameTemp + companyName + "_";
		}
		if(!salesmanName.equals("")){
			nameTemp = nameTemp + salesmanName + "_";
		}
		if(!subjectName.equals("")){
			nameTemp = nameTemp + subjectName + "_";
		}
		if(!bank_name.equals("")){
			nameTemp = nameTemp + bank_name + "_";
		}
		if(!nameTemp.equals("")){
			nameTemp = nameTemp.substring(0, nameTemp.length()-1);
		}
		if(start_time_d!=null){
			nameTemp = "从" + nameTemp + DateTool.formatDateYMD(start_time_d);
		}
		if(end_time_d!=null){
			nameTemp = "到" + nameTemp + DateTool.formatDateYMD(end_time_d);
		}
		
		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createReceivable_detailFile(os, list, nameTemp);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName = nameTemp + "__应收报表";
		
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

	public void createReceivable_detailFile(OutputStream os,
			List<Receivable> list, String nameTemp) throws Exception {

		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称

		// 设置公司名
		WritableFont companyfont = new WritableFont(WritableFont
				.createFont("宋体"), 18, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont);
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		String line0_text = "桐庐富伟针织厂应收报表";
		Label excelCompany = new Label(0, 0, line0_text, companyFormat);
		wsheet.addCell(excelCompany);
		wsheet.mergeCells(0, 0, 8, 0);
		wsheet.setRowView(0, 800);

		// 设置Excel字体
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"),
				11, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2);
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat2.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框

		String[] title = { "序号", "类型", "对方账户", "应收", "已收", "未匹配", "日期", "备注" };

		// 设置Excel表头
		int columnBestWidth[] = new int[title.length]; // 保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) {
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i, 1, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		wsheet.setRowView(3, 400);

		int c = 2; // 用于循环时Excel的行号

		int count = 1;
		for (Receivable item : list) {
			wsheet.setRowView(c, 400);
			
			String type = item.getTypeString();
			String bank_name = item.getBank_name();
			String date = DateTool.formatDateYMD(item.getHappen_at());//付款时间or收发票时间
			String receivable = "";//应收
			String received = "";//已收
			String un_invoice = "";//未付
			if(item.getType().equals("invoice")){
				receivable = String.valueOf(item.getReceivable()) ;
			}else{
				received = String.valueOf(item.getReceived()) ;
				un_invoice = String.valueOf(item.getUn_invoiced()) ;
			}
			String memo = item.getMemo() == null? "":item.getMemo();
			
			Label content1 = new Label(0, c, count + "", titleFormat2);
			Label content2 = new Label(1, c, type, titleFormat2);
			Label content3 = new Label(2, c, bank_name, titleFormat2);
			Label content4 = new Label(3, c, receivable, titleFormat2);
			Label content5 = new Label(4, c, received, titleFormat2);
			Label content6 = new Label(5, c, un_invoice, titleFormat2);
			Label content7 = new Label(6, c, date, titleFormat2);
			Label content8 = new Label(7, c, memo, titleFormat2);
			wsheet.addCell(content1);
			wsheet.addCell(content2);
			wsheet.addCell(content3);
			wsheet.addCell(content4);
			wsheet.addCell(content5);
			wsheet.addCell(content6);
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
			if (columnBestWidth[0] < width1) {
				columnBestWidth[0] = width1;
			}
			if (columnBestWidth[6] < width7) {
				columnBestWidth[6] = width7;
			}
			if (columnBestWidth[7] < width8) {
				columnBestWidth[7] = width8;
			}
			
			if (columnBestWidth[1] < width2) {
				columnBestWidth[1] = width2;
			}
			if (columnBestWidth[2] < width3) {
				columnBestWidth[2] = width3;
			}
			if (columnBestWidth[3] < width4) {
				columnBestWidth[3] = width4;
			}
			if (columnBestWidth[4] < width5) {
				columnBestWidth[4] = width5;
			}
			if (columnBestWidth[5] < width6) {
				columnBestWidth[5] = width6;
			}
			++count;
			c++;
		}
		for (int p = 0; p < columnBestWidth.length; ++p) {
			wsheet.setColumnView(p, columnBestWidth[p] + 2);
		}
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}

	/* 应收报表导出 */
	/*应收报表*/
	
}
