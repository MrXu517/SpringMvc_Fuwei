package com.fuwei.controller.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.fuwei.entity.financial.Subject;
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
	
	/*进项发票报表*/
	@RequestMapping(value = "/purchase_invoice", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView invoice(Integer page,String start_time, String end_time,Integer companyId, Integer subject_id,Boolean un_paid,
			Integer bank_id,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		String lcode = "report/financial/purchase_invoice";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看进项发票报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if(page!=null && page > 0){
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = {"amount","match_amount"};
		pager.setTotal_colnames(total_colnames);
		
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
		pager = invoiceService.getList(pager,un_paid, start_time_d, end_time_d,companyId,subject_id, true, bank_id, null, null, null, sortList);
		
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
	/*进项发票报表*/
	
	
	/*应付报表*/
	@RequestMapping(value = "/payable", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView payable(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,Double amount_from , Double amount_to,
			String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "report/financial/payable";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看应付报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if(page!=null && page > 0){
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = {"payable","pay","un_pay","un_invoiced"};
		pager.setTotal_colnames(total_colnames);
		
		List<Sort> sortList = null;
		if(sortJSON!=null){
			sortList = SerializeTool.deserializeList(sortJSON,Sort.class);
		}
		if(sortList == null){
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("record_at");
		sortList.add(sort);
		pager = financialReportService.getList(pager, start_time_d, end_time_d,companyId,subject_id, bank_id,sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("pager", pager);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("companyId", companyId);
		request.setAttribute("subject_id", subject_id);
		List<Subject> subjectlist = SystemCache.getSubjectList(false);
		request.setAttribute("subjectlist", subjectlist);	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("report/financial/payable");	
	}
	/*应付报表*/
	@RequestMapping(value = "/expense_income", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView workspace_expense_income(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,
			String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "report/financial/expense_income";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看收支报表的权限", null);
		}
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}
		pager.setPageSize(50);
		String[] total_colnames = {"amount"};
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
				companyId, salesmanId,in_out,bank_id,subject_id,null,null, sortList);
		Map<String,Object> total_map = new HashMap<String, Object>();
		
		if(in_out == null){
			Map<String,Object> income_map = expense_incomeService.getTotal(pager, start_time_d, end_time_d,
					companyId, salesmanId,true,bank_id,subject_id,null,null, sortList);
			total_map.put("income_amount", income_map.get("amount"));
			Map<String,Object> expense_map = expense_incomeService.getTotal(pager, start_time_d, end_time_d,
					companyId, salesmanId,false,bank_id,subject_id,null,null, sortList);
			
			total_map.put("expense_amount", expense_map.get("amount"));
		}else if(in_out){//收入
			Map<String,Object> income_map = expense_incomeService.getTotal(pager, start_time_d, end_time_d,
					companyId, salesmanId,true,bank_id,subject_id,null,null, sortList);
			total_map.put("income_amount", income_map.get("amount"));
		}else{//支出
			Map<String,Object> expense_map = expense_incomeService.getTotal(pager, start_time_d, end_time_d,
					companyId, salesmanId,false,bank_id,subject_id,null,null, sortList);
			total_map.put("expense_amount", expense_map.get("amount"));
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
	/*收支报表*/
	
	/*收支报表*/
}
