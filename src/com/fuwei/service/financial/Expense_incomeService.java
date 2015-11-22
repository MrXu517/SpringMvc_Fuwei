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
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class Expense_incomeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(Expense_incomeService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	public List<Expense_income> getByIds(String ids) throws Exception{
		if(ids == null || ids.equals("")){
			return null;
		}
		try {
			List<Expense_income> Expense_incomeList = dao.queryForBeanList(
					"SELECT * FROM tb_expense_income WHERE id in ("+ids+")", Expense_income.class);
			return Expense_incomeList;
		} catch (Exception e) {
			throw e;
		}
	}
	public List<Expense_income> getExpenseIncomeList(Integer bank_id,boolean in_out) throws Exception{
		try {
			List<Expense_income> Expense_incomeList = dao.queryForBeanList(
					"SELECT * FROM tb_expense_income WHERE bank_id=? and amount<>invoice_amount and in_out=?", Expense_income.class,bank_id,in_out);
			return Expense_incomeList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Expense_income> getList(Integer bank_id) throws Exception{
		try {
			List<Expense_income> Expense_incomeList = dao.queryForBeanList(
					"SELECT * FROM tb_expense_income WHERE bank_id=?", Expense_income.class,bank_id);
			return Expense_incomeList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Expense_income getByInvoice(int invoiceId) throws Exception {
		try {
			Expense_income expense = dao.queryForBean(
					"select * from tb_expense_income e , tb_invoice b where b.id = ? and b.bank_id = e.bank_id and b.amount = e.amount", Expense_income.class,
					invoiceId);
			return expense;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//获取列表  - 不分页， 收支报表
	public List<Expense_income> getList_export( Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_expense_income ");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}

			if (start_time != null) {
				sql_condition.append(seq + " expense_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " expense_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql_condition.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (in_out != null) {
				sql_condition.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			if (subject_id != null) {
				sql_condition.append(seq + " subject_id='" + subject_id+ "'");
				seq = " AND ";
			}
		

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return dao.queryForBeanList(sql.append(sql_condition).toString(), Expense_income.class);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,Double amount_from , Double amount_to, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_expense_income ");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}

			if (start_time != null) {
				sql_condition.append(seq + " expense_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " expense_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql_condition.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (in_out != null) {
				sql_condition.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			if (subject_id != null) {
				sql_condition.append(seq + " subject_id='" + subject_id+ "'");
				seq = " AND ";
			}
			if (amount_from != null) {
				sql_condition.append(seq + " amount>='" + amount_from+ "'");
				seq = " AND ";
			}
			if (amount_to != null) {
				sql_condition.append(seq + " amount<='" + amount_to+ "'");
				seq = " AND ";
			}

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return findPager_T(sql.append(sql_condition).toString(), Expense_income.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Map<String,Object> getTotal(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Boolean in_out, Integer bank_id , Integer subject_id,Double amount_from , Double amount_to, List<Sort> sortlist) throws Exception {
		//获取统计数据
		String [] total_colnames = pager.getTotal_colnames();
		if(total_colnames == null || total_colnames.length <= 0){
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for(String colname : total_colnames){
			sql.append("IFNULL(sum(IFNULL(" + colname+",0)),0) " + colname + ",");
		}	
		sql = new StringBuffer(sql.substring(0, sql.length()-1));
		sql.append(" from tb_expense_income");	
		String seq = " WHERE ";
		if (companyId != null) {
			sql.append(seq + " company_id='" + companyId+ "'");
			seq = " AND ";
		}

		if (start_time != null) {
			sql.append(seq + " expense_at>='"
					+ DateTool.formateDate(start_time) + "'");
			seq = " AND ";
		}
		if (end_time != null) {
			sql.append(seq + " expense_at<'"
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
		Map<String,Object> total_map = dao.queryForMap(sql.toString(), null);
		return total_map;
	}

	// 添加
	@Transactional
	public int add(Expense_income expense) throws Exception {
		try{
			return this.insert(expense);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_expense_income WHERE  id = ?", id);
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
	public int update(Expense_income expense) throws Exception {
		try{
			return this.update(expense, "id", "created_at,created_user,amount,expense_at,bank_id,company_id,salesman_id",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Expense_income get(int id) throws Exception {
		try {
			Expense_income expense = dao.queryForBean(
					"select * from tb_expense_income where id = ?", Expense_income.class,
					id);
			return expense;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean batch_add(List<Expense_income> list) throws Exception {
		String sql = "INSERT INTO tb_expense_income(subject_id,subject_name,bank_id,bank_name,company_id,company_name,salesman_id,salesman_name,amount,memo,expense_at,created_at,updated_at,created_user,in_out,invoice_amount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Expense_income item : list) {
			batchArgs.add(new Object[] { item.getSubject_id(), item.getSubject_name(),
					item.getBank_id(), item.getBank_name(),
					item.getCompany_id(), item.getCompany_name(),
					item.getSalesman_id(), item.getSalesman_name(),
					item.getAmount(),item.getMemo(),item.getExpense_at(),
					item.getCreated_at(),item.getUpdated_at(),
					item.getCreated_user(),item.getIn_out(),
					item.getInvoice_amount() });
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			throw e;
		}

	}
}
