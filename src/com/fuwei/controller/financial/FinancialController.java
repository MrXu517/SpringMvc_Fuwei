package com.fuwei.controller.financial;

import java.util.ArrayList;
import java.util.Date;
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
import com.fuwei.entity.financial.Subject;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.BankService;
import com.fuwei.service.financial.Expense_incomeService;
import com.fuwei.service.financial.SubjectService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@RequestMapping("/financial")
@Controller
public class FinancialController extends BaseController {
	
	@Autowired
	Expense_incomeService expense_incomeService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	BankService bankService;
	
	@RequestMapping(value = "/workspace", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,Double amount_from , Double amount_to,
			String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "financial/workspace";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有收入支出明细的权限", null);
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
				companyId, salesmanId,in_out,bank_id,subject_id,amount_from,amount_to, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if (companyId == null & salesmanId != null) {
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("in_out", in_out);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("subject_id", subject_id);
		request.setAttribute("amount_from", amount_from);
		request.setAttribute("amount_to", amount_to);
		request.setAttribute("pager", pager);
		List<Subject> subjectlist = subjectService.getList(true);
		request.setAttribute("subjectlist", subjectlist);	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("financial/workspace");

	}
	
	@RequestMapping(value = "/workspace/expense_income", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView workspace_expense_income(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,Double amount_from , Double amount_to,
			String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "financial/workspace";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有收入支出明细的权限", null);
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
				companyId, salesmanId,in_out,bank_id,subject_id,amount_from,amount_to, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if (companyId == null & salesmanId != null) {
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("in_out", in_out);
		request.setAttribute("bank_id", bank_id);
		request.setAttribute("subject_id", subject_id);
		request.setAttribute("amount_from", amount_from);
		request.setAttribute("amount_to", amount_to);
		request.setAttribute("pager", pager);
		List<Subject> subjectlist = subjectService.getList(true);
		request.setAttribute("subjectlist", subjectlist);	
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("financial/workspace/expense_income");

	}
	
	
	
	@RequestMapping(value = "/expense_income/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> expense_income_delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除收入支出项的权限", null);
		}
		int success = expense_incomeService.remove(id);
		return this.returnSuccess();
		
	}
	
	
	
}
