package com.fuwei.controller.financial;

import java.math.BigDecimal;
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
		String lcode = "invoice/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有将发票匹配支出的权限", null);
		}
//		Invoice invoice = invoiceService.get(invoiceId);
//		Integer bank_id = invoice.getBank_id();
		
//		String[] t_invoice_ids = invoice_ids.split(",");
//		String[] t_expense_income_ids = expense_income_ids.split(",");
		
		
		List<Invoice> invoiceList = invoiceService.getByIds(invoice_ids);
		if(invoiceList==null || invoiceList.size()<=0){
			throw new Exception("匹配发票时，请至少提供一张发票");
		}
		List<Expense_income> Expense_incomeList = expense_incomeService.getByIds(expense_income_ids);
		if(Expense_incomeList==null || Expense_incomeList.size()<=0){
			throw new Exception("匹配发票时，请至少提供一项支出项");
		}
		if(invoiceList.size() > 1 && Expense_incomeList.size() > 1){
			throw new Exception("暂不支持【多张发票匹配多项支出】");
		}
		
		List<Expense_income_invoice> resultList = new ArrayList<Expense_income_invoice>();
		
		double invoice_total = 0 ;
		double expense_income_total = 0 ;
		for(Expense_income expense_income : Expense_incomeList){
			expense_income_total += expense_income.getAmount() - expense_income.getInvoice_amount();
		}
		for(Invoice invoice : invoiceList){
			invoice_total += invoice.getAmount() - invoice.getMatch_amount();
			for(Expense_income expense_income : Expense_incomeList){
				Expense_income_invoice item = new Expense_income_invoice();
				item.setCreated_at(DateTool.now());
				item.setUpdated_at(DateTool.now());
				item.setCreated_user(user.getId());
				item.setInvoice_id(invoice.getId());
				item.setExpense_income_id(expense_income.getId());
				double amount = Math.min(expense_income.getAmount() - expense_income.getInvoice_amount(), invoice.getAmount() - invoice.getMatch_amount());
				item.setAmount(amount);
				resultList.add(item);
			}
		}
		if(invoice_total!=expense_income_total){
			throw new Exception("匹配失败：发票金额之和 不等于 支出项金额之和 ");
		}
		String[] t_expense_income_ids = expense_income_ids.split(",");
		String[] t_invoice_ids = invoice_ids.split(",");
		expense_income_invoiceService.batch_add(t_expense_income_ids,t_invoice_ids,resultList);
		
		
		
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
			throw new PermissionDeniedDataAccessException("没有将发票匹配支出的权限", null);
		}
		
		Map<Invoice , List<Expense_income>> map = new HashMap<Invoice , List<Expense_income>>();
		
		Invoice invoice = invoiceService.get(id);
		//发票待匹配金额
		double to_be_match_invoice_amount = invoice.getAmount() - invoice.getMatch_amount();
		Integer bank_id = invoice.getBank_id();
		List<Expense_income> Expense_incomeList = expense_incomeService.getInvoiceList(bank_id,false);
//		//一张发票 -> 对一项支出
//		for(Expense_income expense_income : Expense_incomeList){
//			double un_match_amount = expense_income.getAmount() - expense_income.getInvoice_amount();//支出未匹配金额
//			if(un_match_amount == to_be_match_invoice_amount){
//				List<Expense_income> temp= new ArrayList<Expense_income>();
//				temp.add(expense_income);
//				map.put(invoice, temp);
//			}
//		}
		
		//存放 一张发票 -> 一项或多项支出
		List<List<Expense_income>> one_to_many_Result = new ArrayList<List<Expense_income>>();
		for(int i = 0 ; i < Expense_incomeList.size() ; ++i){
			List<Expense_income> tempList = new ArrayList<Expense_income>();
			double value = NumberUtil.formateDouble(to_be_match_invoice_amount,2);
			Expense_income temp = Expense_incomeList.get(i);
			double value_i = NumberUtil.formateDouble(temp.getAmount() - temp.getInvoice_amount(),2);
			if( value_i > value){
				continue;
			}
			Expense_incomeNSum(Expense_incomeList,i,0,value,tempList,one_to_many_Result);
		}
		
		//多张发票 -> 一项支出
		Map<List<Invoice> , List<Expense_income>> many_to_one_map = new HashMap<List<Invoice> , List<Expense_income>>();
		List<Invoice> invoiceList = invoiceService.getInvoiceList(bank_id);	
		
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
		
//		for(List<Expense_income> templist : mapResult){
////			for(Expense_income t : templist){
//////				System.out.print(t+",");
////			}
////			System.out.println("");
//		}
		
		return new ModelAndView("financial/expense_income_invoice/match");
		
		
	}
	
	public static void invoiceNSum(List<Invoice> list ,int i, double sum , double value,List<Invoice> templist,List<List<Invoice>> mapResult){
		for(int j = i ; j < list.size();++j){
			Invoice item = list.get(j);
			double un_invoiced_amount = item.getAmount() - item.getMatch_amount();
			un_invoiced_amount = NumberUtil.formateDouble(un_invoiced_amount,2);
			double temp_sum = sum + un_invoiced_amount;
			temp_sum = NumberUtil.formateDouble(temp_sum,2);
			
			
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
				invoiceNSum(list,j+1,temp_sum, value ,templist,mapResult);
				
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
				continue;
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
				continue;
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
	
	
}
