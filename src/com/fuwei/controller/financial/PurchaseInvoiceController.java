package com.fuwei.controller.financial;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
			String sortJSON,HttpSession session, HttpServletRequest request,
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
				true,bank_id,amount_from,amount_to, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("amount_from", amount_from);
		request.setAttribute("amount_to", amount_to);
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
	
	
	
}
