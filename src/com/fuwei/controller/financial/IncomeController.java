package com.fuwei.controller.financial;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

@RequestMapping("/income")
@Controller
public class IncomeController extends BaseController {
	
	@Autowired
	Expense_income_invoiceService expense_income_invoiceService;
	@Autowired
	Expense_incomeService expense_incomeService;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	BankService bankService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,
			String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "expense_income/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看收入支出明细的权限", null);
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
		sort.setProperty("expense_at");
		sortList.add(sort);
		
		pager = expense_incomeService.getList(pager, start_time_d, end_time_d,
				companyId, salesmanId,true,null,null,null,null, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if (companyId == null & salesmanId != null) {
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("pager", pager);
		
		return new ModelAndView("financial/expense_income/income_list");

	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有出纳的权限", null);
		}
		List<Subject> subjectlist = SystemCache.getSubjectList(false);
		request.setAttribute("subjectlist", subjectlist);	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("financial/expense_income/income_add");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(Expense_income expense,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有出纳的权限", null);
		}
		expense.setAmount(NumberUtil.formateDouble(expense.getAmount(), 2));
		expense.setCreated_at(DateTool.now());
		expense.setUpdated_at(DateTool.now());
		expense.setCreated_user(user.getId());
		expense.setIn_out(true);
		int id = expense_incomeService.add(expense);
		
		return this.returnSuccess("id",id);
		
	}
	
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
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Expense_income get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "expense_income/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看支出明细的权限", null);
		}
		Expense_income expense = expense_incomeService.get(id);
		return expense;
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(Expense_income expense,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑支出项的权限", null);
		}
		expense.setUpdated_at(DateTool.now());
		expense.setIn_out(true);
		int success = expense_incomeService.update(expense);
		
		return this.returnSuccess();
		
	}
	
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		String lcode = "expense_income/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看支出详情的权限", null);
		}

		if (id == null) {
			throw new Exception("缺少收入明细ID");
		}
		Expense_income income = expense_incomeService.get(id);
		
		Map<Invoice,Expense_income_invoice> map = new HashMap<Invoice, Expense_income_invoice>();
		List<Expense_income_invoice> eiilist = expense_income_invoiceService.getListByExpense_incomeId(income.getId());
		if(eiilist.size()>0){
			String invoice_ids = "";
			for(Expense_income_invoice temp:eiilist){
				invoice_ids += temp.getInvoice_id()+",";			
			}
			
			invoice_ids = invoice_ids.substring(0, invoice_ids.length()-1);
			
			List<Invoice> eilist = invoiceService.getByIds(invoice_ids);
			
			for(Invoice invoice : eilist){
				
				for(Expense_income_invoice temp:eiilist){
					if(temp.getInvoice_id() == invoice.getId()){
						map.put(invoice, temp);
						break;
					}
				}
			}
		}		
		request.setAttribute("map", map);
		request.setAttribute("expense_income", income);
		
		
		request.setAttribute("income", income);
		return new ModelAndView("financial/expense_income/detail");
	}
	
	
}
