package com.fuwei.service.financial;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.financial.Expense_income_invoice;
import com.fuwei.entity.financial.Invoice;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class Expense_income_invoiceService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(Expense_income_invoiceService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//匹配
	@Transactional
	public Boolean batch_add(Integer company_id , Integer subject_id , String[] expense_income_ids, String[] invoice_ids,List<Expense_income_invoice> list){
		String sql = "INSERT INTO tb_expense_income_invoice(amount,created_at,created_user,expense_income_id,invoice_id,updated_at) VALUES(?,?,?,?,?,?)";

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(Expense_income_invoice item :list) {
            batchArgs.add(new Object[]{item.getAmount(),item.getCreated_at(),item.getCreated_user(),item.getExpense_income_id(),item.getInvoice_id(),item.getUpdated_at()});
        }

        int result[]= jdbc.batchUpdate(sql, batchArgs);
        
        //修改支出 和 发票的匹配金额
        
        String sql_invoice = "update tb_invoice set match_amount= (select  ROUND(IFNULL(sum(amount),0),2)  from  tb_expense_income_invoice where invoice_id=?)  where id=?";
        List<Object[]> batchArgs_invoice = new ArrayList<Object[]>();
        for(String item :invoice_ids) {
        	batchArgs_invoice.add(new Object[]{item,item});
        }
        int[] result_invoice= jdbc.batchUpdate(sql_invoice, batchArgs_invoice);
        //设置发票的公司 和 科目
        String sql_invoice_company_subject = "update tb_invoice set company_id=? , subject_id=? where id=?";
        List<Object[]> batchArgs_invoice_company_subject = new ArrayList<Object[]>();
        for(String item :invoice_ids) {
        	batchArgs_invoice_company_subject.add(new Object[]{company_id,subject_id,item});
        }
        int[] result_invoice_company_subject= jdbc.batchUpdate(sql_invoice_company_subject, batchArgs_invoice_company_subject);
        
        
        String sql_expense = "update tb_expense_income set invoice_amount= (select  ROUND(IFNULL(sum(amount),0),2)  from  tb_expense_income_invoice where expense_income_id=?)  where id=?";
        List<Object[]> batchArgs_expense = new ArrayList<Object[]>();
        for(String item :expense_income_ids) {
        	batchArgs_expense.add(new Object[]{item,item});
        }
        int[] result_expense= jdbc.batchUpdate(sql_expense, batchArgs_expense);
        
        
        
        
        return true;
	}
	
	
	//收入匹配发票
	@Transactional
	public Boolean batch_add_incomeMatch(Integer company_id , Integer subject_id , String[] expense_income_ids, String[] invoice_ids,List<Expense_income_invoice> list){
		String sql = "INSERT INTO tb_expense_income_invoice(amount,created_at,created_user,expense_income_id,invoice_id,updated_at) VALUES(?,?,?,?,?,?)";

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for(Expense_income_invoice item :list) {
            batchArgs.add(new Object[]{item.getAmount(),item.getCreated_at(),item.getCreated_user(),item.getExpense_income_id(),item.getInvoice_id(),item.getUpdated_at()});
        }

        int result[]= jdbc.batchUpdate(sql, batchArgs);
        
        //修改支出 和 发票的匹配金额
        
        String sql_invoice = "update tb_invoice set match_amount= (select  ROUND(IFNULL(sum(amount),0),2)  from  tb_expense_income_invoice where invoice_id=?)  where id=?";
        List<Object[]> batchArgs_invoice = new ArrayList<Object[]>();
        for(String item :invoice_ids) {
        	batchArgs_invoice.add(new Object[]{item,item});
        }
        int[] result_invoice= jdbc.batchUpdate(sql_invoice, batchArgs_invoice);
        //设置收入项的公司和科目
        String sql_expense_income_company_subject = "update tb_expense_income set company_id=? , subject_id=? where id=?";
        List<Object[]> batchArgs_expense_income_company_subject = new ArrayList<Object[]>();
        for(String item :expense_income_ids) {
        	batchArgs_expense_income_company_subject.add(new Object[]{company_id,subject_id,item});
        }
        int[] result_expense_income_company_subject= jdbc.batchUpdate(sql_expense_income_company_subject, batchArgs_expense_income_company_subject);
        
        
        String sql_expense = "update tb_expense_income set invoice_amount= (select  ROUND(IFNULL(sum(amount),0),2)  from  tb_expense_income_invoice where expense_income_id=?)  where id=?";
        List<Object[]> batchArgs_expense = new ArrayList<Object[]>();
        for(String item :expense_income_ids) {
        	batchArgs_expense.add(new Object[]{item,item});
        }
        int[] result_expense= jdbc.batchUpdate(sql_expense, batchArgs_expense);
        
        
        
        
        return true;
	}
	
	public List<Expense_income_invoice> getListByExpense_incomeId(int expense_incomeId) throws Exception{
		try {
			List<Expense_income_invoice> result = dao.queryForBeanList("select * from tb_expense_income_invoice where expense_income_id=?", Expense_income_invoice.class,expense_incomeId);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	public List<Expense_income_invoice> getListByInvoiceId(int invoiceId) throws Exception{
		try {
			List<Expense_income_invoice> result = dao.queryForBeanList("select * from tb_expense_income_invoice where invoice_id=?", Expense_income_invoice.class,invoiceId);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,Double amount_from , Double amount_to, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";
			if (companyId != null & salesmanId == null) {
				sql.append("select * from tb_expense_income_invoice where company_id='"
						+ companyId + "'");
				seq = " AND ";
			} else {
				sql.append("select * from tb_expense_income ");
			}

			if (start_time != null) {
				sql.append(seq + " expense_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " expense_at<='"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (in_out != null) {
				sql.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			if (subject_id != null) {
				sql.append(seq + " subject_id='" + subject_id+ "'");
				seq = " AND ";
			}
			if (amount_from != null) {
				sql.append(seq + " amount>='" + amount_from+ "'");
				seq = " AND ";
			}
			if (amount_to != null) {
				sql.append(seq + " amount<='" + amount_to+ "'");
				seq = " AND ";
			}

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return findPager_T(sql.toString(), Expense_income_invoice.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Expense_income_invoice expense_income_invoice) throws Exception {
		try{
			return this.insert(expense_income_invoice);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	@Transactional
	public int remove(int id) throws Exception {
		try{
			Expense_income_invoice temp = this.get(id);
			int result =  dao.update("delete from tb_expense_income_invoice WHERE  id = ?", id);
			//删除关系时，同时要修改 支出项 与 发票的invoice_amount 和 match_amount
			//修改支出 和 发票的匹配金额
	        dao.update("update tb_invoice set match_amount= (select  ROUND(IFNULL(sum(amount),0),2)  from  tb_expense_income_invoice where invoice_id=?)  where id=?", temp.getInvoice_id() , temp.getInvoice_id());    
	        dao.update("update tb_expense_income set invoice_amount= (select  ROUND(IFNULL(sum(amount),0),2)  from  tb_expense_income_invoice where expense_income_id=?)  where id=?", temp.getExpense_income_id(),temp.getExpense_income_id());
			
	        
	        return result;
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Expense_income_invoice expense_income_invoice) throws Exception {
		try{
			return this.update(expense_income_invoice, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Expense_income_invoice get(int id) throws Exception {
		try {
			Expense_income_invoice expense_income_invoice = dao.queryForBean(
					"select * from tb_expense_income_invoice where id = ?", Expense_income_invoice.class,
					id);
			return expense_income_invoice;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}
