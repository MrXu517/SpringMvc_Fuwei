package com.fuwei.controller.financial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.financial.Expense_income_invoice;
import com.fuwei.entity.financial.Invoice;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.Expense_incomeService;
import com.fuwei.service.financial.Expense_income_invoiceService;
import com.fuwei.service.financial.InvoiceService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/expense_income_invoice")
@Controller
public class Expense_income_invoiceController extends BaseController {
	
	@Autowired
	Expense_income_invoiceService expense_income_invoiceService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	Expense_incomeService expense_incomeService;
	
	//自动匹配
	@RequestMapping(value = "/match", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> match(String invoice_ids,String expense_income_ids,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将销项发票匹配收入 和 进项发票匹配支出 的权限", null);
		}
		
		List<Invoice> invoiceList = invoiceService.getByIds(invoice_ids);
		if(invoiceList==null || invoiceList.size()<=0){
			throw new Exception("匹配发票时，请至少提供一张发票");
		}
		Boolean invoices_in_out = invoiceList.get(0).getIn_out();
		for(Invoice invoice : invoiceList){
			if(invoice.getIn_out()!= invoices_in_out){
				throw new Exception("发票类型不统一， 请统一提交进项发票或销项发票");
			}
		}
		
		Boolean expense_or_income = !invoices_in_out;//!invoice.getIn_out();
		
		List<Expense_income> Expense_incomeList = expense_incomeService.getByIds(expense_income_ids);
		if(Expense_incomeList==null || Expense_incomeList.size()<=0){
			throw new Exception("匹配发票时，请至少提供一项支出项或收入项");
		}
		if(invoiceList.size() > 1 && Expense_incomeList.size() > 1){
			throw new Exception("暂不支持【多张发票匹配多项支出或收入】");
		}
		
		List<Expense_income_invoice> resultList = new ArrayList<Expense_income_invoice>();
		
		double invoice_total = 0 ;
		double expense_income_total = 0 ;
		for(Expense_income expense_income : Expense_incomeList){
			expense_income_total += expense_income.getAmount() - expense_income.getInvoice_amount();
		}
		expense_income_total = NumberUtil.formateDouble(expense_income_total, 2);
		
		//若一张发票对应 一项或多项支出
		Boolean one_many = false;
		if(invoiceList.size() == 1){
			one_many = true;
		}
		//否则是多张发票对应一项支出，则 one_many = false;
		for(Invoice invoice : invoiceList){
			invoice_total += invoice.getAmount() - invoice.getMatch_amount();
			for(Expense_income expense_income : Expense_incomeList){
				Expense_income_invoice item = new Expense_income_invoice();
				item.setCreated_at(DateTool.now());
				item.setUpdated_at(DateTool.now());
				item.setCreated_user(user.getId());
				item.setInvoice_id(invoice.getId());
				item.setExpense_income_id(expense_income.getId());
//				double amount = Math.min(expense_income.getAmount() - expense_income.getInvoice_amount(), invoice.getAmount() - invoice.getMatch_amount());
				double amount = 0 ;
				if(one_many){//若一张发票对应 一项或多项支出 ， 则取支出的amount值
					amount = expense_income.getAmount() - expense_income.getInvoice_amount();
				}
				else{//否则，取发票的值
					amount = invoice.getAmount() - invoice.getMatch_amount();
				}
				item.setAmount(amount);
				resultList.add(item);
			}
		}
		invoice_total = NumberUtil.formateDouble(invoice_total, 2);
		if(invoice_total!=expense_income_total){
			throw new Exception("匹配失败：发票金额之和 不等于 收入支出项金额之和 ");
		}
		String[] t_expense_income_ids = expense_income_ids.split(",");
		String[] t_invoice_ids = invoice_ids.split(",");
		
		//判断 支出的公司和科目是否一致
		Integer companyId = Expense_incomeList.get(0).getCompany_id();
		Integer subjectId = Expense_incomeList.get(0).getSubject_id();
		for(Expense_income temp :  Expense_incomeList){
			if(temp.getIn_out()!=expense_or_income){
				throw new Exception("收入支出项类型不统一， 请统一提交" + (expense_or_income?"收入项":"支出项"));
			}
			if(temp.getCompany_id()!=companyId){
				throw new Exception("匹配失败：收入支出项公司不一致 ");
			}
			if(temp.getSubject_id()!=subjectId){
				throw new Exception("匹配失败：收入支出项科目不一致 ");
			}
		}

		String subject_name = SystemCache.getSubjectName(subjectId);
		expense_income_invoiceService.batch_add(companyId,subjectId,subject_name,t_expense_income_ids,t_invoice_ids,resultList);
		
		
		
		return this.returnSuccess();
		
	}
		
	//自动匹配
	@RequestMapping(value = "/match/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView match(@PathVariable Integer id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将销项发票匹配收入 和 进项发票匹配支出 的权限", null);
		}
		
		Map<Invoice , List<Expense_income>> map = new HashMap<Invoice , List<Expense_income>>();
		
		Invoice invoice = invoiceService.get(id);
		Boolean expense_or_income = !invoice.getIn_out();
		//发票待匹配金额
		double to_be_match_invoice_amount = invoice.getAmount() - invoice.getMatch_amount();
		Integer bank_id = invoice.getBank_id();
		List<Expense_income> Expense_incomeList = expense_incomeService.getExpenseIncomeList(bank_id,expense_or_income);
		
		Collections.sort(Expense_incomeList, new Comparator() {
			  @Override
		      public int compare(Object o1, Object o2) {
				  Expense_income a1 = (Expense_income) o1;
				  Expense_income a2 = (Expense_income) o2;
		    	  Double a1_d = new Double(a1.getAmount()-a1.getInvoice_amount());
		    	  Double a2_d = new Double(a2.getAmount()-a2.getInvoice_amount());
		        return a1_d.compareTo(a2_d);
		      }
		    });
		
//		//把收入中负数的item放到前面
//		List<Expense_income> Expense_incomeList = new ArrayList<Expense_income>();
//		for(Expense_income item : temp_Expense_incomeList){
//			if(item.getAmount() - item.getInvoice_amount() < 0){
//				Expense_incomeList.add(0, item);
//			}else{
//				Expense_incomeList.add(item);
//			}
//		}
		
		
		//存放 一张发票 -> 一项或多项支出
		List<List<Expense_income>> one_to_many_Result = new ArrayList<List<Expense_income>>();
		for(int i = 0 ; i < Expense_incomeList.size() ; ++i){
			List<Expense_income> tempList = new ArrayList<Expense_income>();
			double value = NumberUtil.formateDouble(to_be_match_invoice_amount,2);
			Expense_income temp = Expense_incomeList.get(i);
			double value_i = NumberUtil.formateDouble(temp.getAmount() - temp.getInvoice_amount(),2);
//			if( value_i > value){
//				continue;
//			}
			Expense_incomeNSum(Expense_incomeList,i,0,value,tempList,one_to_many_Result);
		}
		
		//多张发票 -> 一项支出
		Map<List<Invoice> , List<Expense_income>> many_to_one_map = new HashMap<List<Invoice> , List<Expense_income>>();
		List<Invoice> invoiceList = invoiceService.getInvoiceList(bank_id,invoice.getIn_out());	

		Collections.sort(invoiceList, new Comparator() {
			  @Override
		      public int compare(Object o1, Object o2) {
		    	  Invoice a1 = (Invoice) o1;
		    	  Invoice a2 = (Invoice) o2;
		    	  Double a1_d = new Double(a1.getAmount()-a1.getMatch_amount());
		    	  Double a2_d = new Double(a2.getAmount()-a2.getMatch_amount());
		        return a1_d.compareTo(a2_d);
		      }
		    });
//		//把发票中负数的item放到前面
//		List<Invoice> invoiceList = new ArrayList<Invoice>();
//		for(Invoice item : temp_invoiceList){
//			if(item.getAmount() - item.getMatch_amount() < 0){
//				invoiceList.add(0, item);
//			}else{
//				invoiceList.add(item);
//			}
//		}
		
		for(Invoice tempinvoice : invoiceList){
			if(tempinvoice.getId() == invoice.getId()){
				invoiceList.remove(tempinvoice);
				break;
			}
		}
		for(int i = 0 ; i < Expense_incomeList.size() ; ++i){
			Expense_income ett = Expense_incomeList.get(i);
			List<Expense_income> e_templist = new ArrayList<Expense_income>();
			double to_be_invoiced_amount = ett.getAmount() - ett.getInvoice_amount();
			e_templist.add(ett);
			List<List<Invoice>> tempResult = new ArrayList<List<Invoice>>();
			for(int k = 0 ; k < invoiceList.size() ; ++k){
				List<Invoice> tempList = new ArrayList<Invoice>();
				double value = NumberUtil.formateDouble(to_be_invoiced_amount - to_be_match_invoice_amount, 2);
				Invoice temp = invoiceList.get(k);
				double value_k = NumberUtil.formateDouble(temp.getAmount() - temp.getMatch_amount(),2);
				if( value_k > value){
					continue;
				}
				invoiceNSum(invoiceList,k,0,value,tempList,tempResult);
			}
			for(List<Invoice> templist : tempResult){
				templist.add(invoice);
				many_to_one_map.put(templist,e_templist);
			}
		}
		request.setAttribute("invoice", invoice);
		request.setAttribute("many_to_one_map", many_to_one_map);
		request.setAttribute("one_to_many_Result", one_to_many_Result);
		
		if(expense_or_income){//true = 收入
			return new ModelAndView("financial/expense_income_invoice/match_income");
		}else{
			return new ModelAndView("financial/expense_income_invoice/match");
		}
		
		
		
	}
	
	
	//手动匹配
	@RequestMapping(value = "/match_manual/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView match_manual(@PathVariable Integer id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将销项发票匹配收入 和 进项发票匹配支出 的权限", null);
		}
		
		Map<Invoice , List<Expense_income>> map = new HashMap<Invoice , List<Expense_income>>();
		
		Invoice invoice = invoiceService.get(id);
		Boolean expense_or_income = !invoice.getIn_out();
		//发票待匹配金额
		Integer bank_id = invoice.getBank_id();
		
		List<Expense_income> Expense_incomeList = expense_incomeService.getExpenseIncomeList(bank_id,expense_or_income);

		request.setAttribute("invoice", invoice);
		request.setAttribute("Expense_incomeList", Expense_incomeList);
		
		if(expense_or_income){
			return new ModelAndView("financial/expense_income_invoice/match_manual_income");			
		}
		else{
			return new ModelAndView("financial/expense_income_invoice/match_manual");			
		}
	}
	
	//手动匹配
	@RequestMapping(value = "/match_manual", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> match_manual(Integer invoice_id,Integer expense_income_id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将销项发票匹配收入 和 进项发票匹配支出 的权限", null);
		}
		
		Invoice invoice = invoiceService.get(invoice_id);
		Boolean expense_or_income = !invoice.getIn_out();
		
		if(invoice==null){
			throw new Exception("匹配发票时，请至少提供一张发票");
		}
		Expense_income expense_income = expense_incomeService.get(expense_income_id);
		if(expense_income==null){
			throw new Exception("匹配发票时，请至少提供一项支出项");
		}
		if(expense_income.getIn_out()!=expense_or_income){
			throw new Exception("收入支出项类型与发票不一致， 销项发票对应收入，进项发票对应支出");
		}
		
		
		List<Expense_income_invoice> resultList = new ArrayList<Expense_income_invoice>();
		
		Expense_income_invoice item = new Expense_income_invoice();
		item.setCreated_at(DateTool.now());
		item.setUpdated_at(DateTool.now());
		item.setCreated_user(user.getId());
		item.setInvoice_id(invoice.getId());
		item.setExpense_income_id(expense_income.getId());
		double amount = Math.min(expense_income.getAmount() - expense_income.getInvoice_amount(), invoice.getAmount() - invoice.getMatch_amount());
		item.setAmount(amount);
		resultList.add(item);
		
		Integer companyId = expense_income.getCompany_id();
		Integer subjectId = expense_income.getSubject_id();
		

		String subject_name = SystemCache.getSubjectName(subjectId);
		expense_income_invoiceService.batch_add(companyId,subjectId,subject_name,new String[]{""+expense_income_id},new String[]{""+invoice_id},resultList);
		
		
		
		return this.returnSuccess();
		
	}
	
	public static void invoiceNSum(List<Invoice> list ,int i, double sum , double value,List<Invoice> templist,List<List<Invoice>> mapResult){		
		for(int j = i ; j < list.size();++j){
			Invoice item = list.get(j);
			double un_invoiced_amount = item.getAmount() - item.getMatch_amount();			
			un_invoiced_amount = NumberUtil.formateDouble(un_invoiced_amount,2);
			double temp_sum = sum + un_invoiced_amount;
			temp_sum = NumberUtil.formateDouble(temp_sum,2);
//			System.out.println(un_invoiced_amount+","+temp_sum);
			if(temp_sum == value){//若相等，则找到了
				templist.add(item);
				mapResult.add(templist);
				if(templist.size() <= 1){
					return ;
				}
				List<Invoice> alist  = new ArrayList<Invoice>();
				alist.addAll(templist);
				alist.remove(alist.size()-1);
				templist = alist;
				continue;
			}
			if(temp_sum < value){//若值<value,则继续往下找
				templist.add(item);
				int indexOf = templist.size()-1;
				invoiceNSum(list,j+1,temp_sum , value ,templist,mapResult);
				
				if(indexOf <= 0){
					return ; 
				}else{
					List<Invoice> alist  = new ArrayList<Invoice>();

					for(int k = 0; k < indexOf;++k){
						alist.add(templist.get(k));
					}
					templist = alist;
				}
				
				continue;
			}
			if(temp_sum > value){//若值>value,则返回 false
//				templist.add(item);
//				int indexOf = templist.size()-1;
//				invoiceNSum(list,j+1,temp_sum , value ,templist,mapResult);
//				
//				if(indexOf <= 0){
//					return ; 
//				}else{
//					List<Invoice> alist  = new ArrayList<Invoice>();
//
//					for(int k = 0; k < indexOf;++k){
//						alist.add(templist.get(k));
//					}
//					templist = alist;
//				}
//				
//				continue;
				
				return;
			}
		}
	}
	
	
	public static void Expense_incomeNSum(List<Expense_income> list ,int i, double sum ,double value,List<Expense_income> templist,List<List<Expense_income>> mapResult){
		for(int j = i ; j < list.size();++j){
			Expense_income item = list.get(j);
			double un_invoiced_amount = item.getAmount() - item.getInvoice_amount();			
			un_invoiced_amount = NumberUtil.formateDouble(un_invoiced_amount,2);
			double temp_sum = sum + un_invoiced_amount;
			temp_sum = NumberUtil.formateDouble(temp_sum,2);
			if(temp_sum == value){//若相等，则找到了
				templist.add(item);
				mapResult.add(templist);
				if(templist.size() <= 1){
					return ;
				}
				List<Expense_income> alist  = new ArrayList<Expense_income>();
				alist.addAll(templist);
				alist.remove(alist.size()-1);
				templist = alist;
				continue;
			}
			if(temp_sum < value){//若值<value,则继续往下找
				templist.add(item);
				int indexOf = templist.size()-1;
				Expense_incomeNSum(list,j+1,temp_sum , value ,templist,mapResult);
				
				if(indexOf <= 0){
					return ; 
				}else{
					List<Expense_income> alist  = new ArrayList<Expense_income>();

					for(int k = 0; k < indexOf;++k){
						alist.add(templist.get(k));
					}
					templist = alist;
				}
				
				continue;
			}
			if(temp_sum > value){//若值>value,则返回 false
//				templist.add(item);
//				int indexOf = templist.size()-1;
//				Expense_incomeNSum(list,j+1,temp_sum , value ,templist,mapResult);
//				
//				if(indexOf <= 0){
//					return ; 
//				}else{
//					List<Expense_income> alist  = new ArrayList<Expense_income>();
//
//					for(int k = 0; k < indexOf;++k){
//						alist.add(templist.get(k));
//					}
//					templist = alist;
//				}
//				continue;
				
				return;
			}
		}
	}
	
	
	
	//匹配列表
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(Integer page, String start_time, String end_time,
			Integer companyId, Integer salesmanId,
			String sortJSON,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "expense_income_invoice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看发票与支出项匹配关系的权限", null);
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
		
		pager = expense_income_invoiceService.getList(pager, start_time_d, end_time_d,
				companyId, salesmanId,false,null,null,null,null, sortList);

		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if (companyId == null & salesmanId != null) {
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("pager", pager);
		
		return new ModelAndView("expense_income_invoice/list");

	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除发票与支出项匹配关系的权限", null);
		}
		int success = expense_income_invoiceService.remove(id);
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Expense_income_invoice get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "expense_income_invoice/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看发票与支出项匹配关系的权限", null);
		}
		Expense_income_invoice expense_income_invoice = expense_income_invoiceService.get(id);
		return expense_income_invoice;
	}
	
	
	
	/*收入匹配销项发票*/
	//自动匹配
	@RequestMapping(value = "/income_match_saleinvoice/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView income_match_saleinvoice(@PathVariable Integer id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有收入匹配销项发票的权限", null);
		}
		
		Map<Invoice , List<Expense_income>> map = new HashMap<Invoice , List<Expense_income>>();
		Expense_income income=  expense_incomeService.get(id);//id为收入ID
		if(income.getIn_out()!=true){//如果不是收入
			throw new Exception("找不到ID为" + id + "的收入项");
		}
		//收入待匹配金额
		double to_be_match_income_amount = income.getAmount() - income.getInvoice_amount();
		Integer bank_id = income.getBank_id();
		List<Invoice> invoiceList = invoiceService.getBeforeDateInvoiceList(bank_id,false,income.getSubject_id(),income.getExpense_at());
		
		Collections.sort(invoiceList, new Comparator() {
			  @Override
		      public int compare(Object o1, Object o2) {
		    	  Invoice a1 = (Invoice) o1;
		    	  Invoice a2 = (Invoice) o2;
		    	  Double a1_d = new Double(a1.getAmount()-a1.getMatch_amount());
		    	  Double a2_d = new Double(a2.getAmount()-a2.getMatch_amount());
		        return a1_d.compareTo(a2_d);
		      }
		    });
		
//		//把发票中负数的item放到前面
//		List<Invoice> invoiceList = new ArrayList<Invoice>();
//		for(Invoice invoice : temp_invoiceList){
//			if(invoice.getAmount() - invoice.getMatch_amount() < 0){
//				invoiceList.add(0, invoice);
//			}else{
//				invoiceList.add(invoice);
//			}
//		}
		
		
		//存放 一项收入 -> 一张或多张销项发票
		List<List<Invoice>> one_to_many_Result = new ArrayList<List<Invoice>>();
		for(int i = 0 ; i < invoiceList.size() ; ++i){
			List<Invoice> tempList = new ArrayList<Invoice>();
			double value = NumberUtil.formateDouble(to_be_match_income_amount,2);
//			Invoice temp = invoiceList.get(i);
//			double value_i = NumberUtil.formateDouble(temp.getAmount() - temp.getMatch_amount(),2);
			invoiceNSum(invoiceList,i,0,value,tempList,one_to_many_Result);
		}
		
		//多项收入 -> 一张发票
		Map<List<Expense_income> , List<Invoice>> many_to_one_map = new HashMap<List<Expense_income> , List<Invoice>>();
		List<Expense_income> expense_incomeList = expense_incomeService.getExpenseIncomeList(bank_id, true);

		Collections.sort(expense_incomeList, new Comparator() {
			  @Override
		      public int compare(Object o1, Object o2) {
				  Expense_income a1 = (Expense_income) o1;
				  Expense_income a2 = (Expense_income) o2;
		    	  Double a1_d = new Double(a1.getAmount()-a1.getInvoice_amount());
		    	  Double a2_d = new Double(a2.getAmount()-a2.getInvoice_amount());
		        return a1_d.compareTo(a2_d);
		      }
		    });
//		//把收入中负数的item放到前面
//		List<Expense_income> expense_incomeList = new ArrayList<Expense_income>();
//		for(Expense_income item : temp_expense_incomeList){
//			if(item.getAmount() - item.getInvoice_amount() < 0){
//				expense_incomeList.add(0, item);
//			}else{
//				expense_incomeList.add(item);
//			}
//		}
		
		for(Expense_income tempExpenseIncome : expense_incomeList){
			if(tempExpenseIncome.getId() == income.getId()){
				expense_incomeList.remove(tempExpenseIncome);
				break;
			}
		}
		for(int i = 0 ; i < invoiceList.size() ; ++i){
			Invoice ett = invoiceList.get(i);
			List<Invoice> e_templist = new ArrayList<Invoice>();
			double to_be_matched_amount = ett.getAmount() - ett.getMatch_amount();
			e_templist.add(ett);
			List<List<Expense_income>> tempResult = new ArrayList<List<Expense_income>>();
			for(int k = 0 ; k < expense_incomeList.size() ; ++k){
				List<Expense_income> tempList = new ArrayList<Expense_income>();
				double value = NumberUtil.formateDouble(to_be_matched_amount - to_be_match_income_amount, 2);
				Expense_income temp = expense_incomeList.get(k);
				double value_k = NumberUtil.formateDouble(temp.getAmount() - temp.getInvoice_amount(),2);
				if( value_k > value){
					continue;
				}
				Expense_incomeNSum(expense_incomeList,k,0,value,tempList,tempResult);
			}
			for(List<Expense_income> templist : tempResult){
				templist.add(income);
				many_to_one_map.put(templist,e_templist);
			}
		}
		request.setAttribute("income", income);
		request.setAttribute("many_to_one_map", many_to_one_map);
		request.setAttribute("one_to_many_Result", one_to_many_Result);	
		return new ModelAndView("financial/expense_income_invoice/income_match_saleinvoice");		
	}
	
	@RequestMapping(value = "/income_match_saleinvoice", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> income_match_saleinvoice(String invoice_ids,String expense_income_ids,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将收入匹配销项发票 的权限", null);
		}
		List<Expense_income> Expense_incomeList = expense_incomeService.getByIds(expense_income_ids);
		if(Expense_incomeList==null || Expense_incomeList.size()<=0){
			throw new Exception("匹配收入项时，请至少提供一项收入项");
		}
		List<Invoice> invoiceList = invoiceService.getByIds(invoice_ids);
		if(invoiceList==null || invoiceList.size()<=0){
			throw new Exception("匹配收入项时，请至少提供一张发票");
		}
		if(invoiceList.size() > 1 && Expense_incomeList.size() > 1){
			throw new Exception("暂不支持【多项收入匹配多张发票】");
		}
		
		
		Boolean invoices_in_out = invoiceList.get(0).getIn_out();
		if(invoices_in_out){
			throw new Exception("请提交销项发票");
		}
		for(Invoice invoice : invoiceList){
			if(invoice.getIn_out()!= invoices_in_out){
				throw new Exception("发票类型不统一， 请统一提交销项发票");
			}
		}
		
		Boolean expense_or_income = !invoices_in_out;//!invoice.getIn_out();
		
		List<Expense_income_invoice> resultList = new ArrayList<Expense_income_invoice>();
		
		double invoice_total = 0 ;
		double expense_income_total = 0 ;
		for(Expense_income expense_income : Expense_incomeList){
			expense_income_total += expense_income.getAmount() - expense_income.getInvoice_amount();
		}
		expense_income_total = NumberUtil.formateDouble(expense_income_total, 2);
		
		//若一项收入对应 一项或多项发票
		Boolean one_many = false;
		if(Expense_incomeList.size() == 1){
			one_many = true;
		}
		//否则是多项收入对应一张发票，则 one_many = false;
		
		for(Invoice invoice : invoiceList){
			invoice_total += invoice.getAmount() - invoice.getMatch_amount();
			for(Expense_income expense_income : Expense_incomeList){
				Expense_income_invoice item = new Expense_income_invoice();
				item.setCreated_at(DateTool.now());
				item.setUpdated_at(DateTool.now());
				item.setCreated_user(user.getId());
				item.setInvoice_id(invoice.getId());
				item.setExpense_income_id(expense_income.getId());
//				double amount = Math.min(expense_income.getAmount() - expense_income.getInvoice_amount(), invoice.getAmount() - invoice.getMatch_amount());
				double amount = 0 ;
				if(one_many){//若一项收入对应 一项或多项发票 ， 则取发票的amount值
					amount = invoice.getAmount() - invoice.getMatch_amount();
				}
				else{//否则，取收入的值
					amount = expense_income.getAmount() - expense_income.getInvoice_amount();
				}
				item.setAmount(amount);
				resultList.add(item);
			}
		}
		invoice_total = NumberUtil.formateDouble(invoice_total, 2);
		if(invoice_total!=expense_income_total){
			throw new Exception("匹配失败：发票待匹配金额之和 不等于 收入项金额之和 ");
		}
		String[] t_expense_income_ids = expense_income_ids.split(",");
		String[] t_invoice_ids = invoice_ids.split(",");
		
		//判断 发票的公司和科目是否一致
		Integer companyId = invoiceList.get(0).getCompany_id();
		Integer subjectId = invoiceList.get(0).getSubject_id();
		Integer bank_id = invoiceList.get(0).getBank_id();
		for(Invoice temp :  invoiceList){
			if(temp.getIn_out()!= invoices_in_out){
				throw new Exception("发票类型不统一， 请统一提交销项发票");
			}
			if(temp.getCompany_id()!=companyId){
				throw new Exception("匹配失败：发票项公司不一致 ");
			}
			if((int)temp.getSubject_id()!=(int)subjectId){
				throw new Exception("匹配失败：发票项科目不一致 ");
			}
			if((int)temp.getBank_id()!=(int)bank_id){
				throw new Exception("匹配失败：发票项对方账户不一致 ");
			}
		}
		String subject_name = SystemCache.getSubjectName(subjectId);
		expense_income_invoiceService.batch_add_incomeMatch(companyId,subjectId,subject_name,t_expense_income_ids,t_invoice_ids,resultList);
		
		
		
		return this.returnSuccess();
		
	}
	
	//手动匹配
	@RequestMapping(value = "/income_match_manual/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView income_match_manual(@PathVariable Integer id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将收入匹配销项发票", null);
		}
		
		Map<Expense_income , List<Invoice>> map = new HashMap<Expense_income , List<Invoice>>();
		
		Expense_income income = expense_incomeService.get(id);
		Boolean expense_or_income = income.getIn_out();
		Boolean invoice_in_out = !expense_or_income;
		//发票待匹配金额
		Integer bank_id = income.getBank_id();
		
		List<Invoice> invoicelist = invoiceService.getInvoiceList(bank_id, invoice_in_out);

		request.setAttribute("income", income);
		request.setAttribute("invoicelist", invoicelist);
		return new ModelAndView("financial/expense_income_invoice/income_match_manual_saleinvoice");			
		
	}
	
	//手动匹配
	@RequestMapping(value = "/income_match_manual", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> income_match_manual(Integer invoice_id,Integer expense_income_id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "expense_income_invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将收入匹配销项发票", null);
		}
		
		Expense_income income = expense_incomeService.get(expense_income_id);
		Boolean expense_or_income = income.getIn_out();
		Boolean invoice_in_out = !expense_or_income;
		
		if(income==null){
			throw new Exception("匹配收入时，请至少提供一项收入");
		}
		
		Invoice invoice = invoiceService.get(invoice_id,invoice_in_out);
		if(invoice==null){
			throw new Exception("匹配收入时，请至少提供一项销项发票");
		}
		
		
		List<Expense_income_invoice> resultList = new ArrayList<Expense_income_invoice>();
		
		Expense_income_invoice item = new Expense_income_invoice();
		item.setCreated_at(DateTool.now());
		item.setUpdated_at(DateTool.now());
		item.setCreated_user(user.getId());
		item.setInvoice_id(invoice.getId());
		item.setExpense_income_id(income.getId());
		double amount = Math.min(income.getAmount() - income.getInvoice_amount(), invoice.getAmount() - invoice.getMatch_amount());
		item.setAmount(amount);
		resultList.add(item);
		
		
		Integer companyId = invoice.getCompany_id();
		Integer subjectId = invoice.getSubject_id();
		

		String subject_name = SystemCache.getSubjectName(subjectId);
		expense_income_invoiceService.batch_add_incomeMatch(companyId,subjectId,subject_name,new String[]{""+expense_income_id},new String[]{""+invoice_id},resultList);
		
		
		
		return this.returnSuccess();
		
	}
	/*收入匹配销项发票*/
	
	
	
}
