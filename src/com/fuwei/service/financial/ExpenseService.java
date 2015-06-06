package com.fuwei.service.financial;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.financial.Expense_income;
import com.fuwei.service.BaseService;

@Component
public class ExpenseService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(ExpenseService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Expense_income> getList() throws Exception {
		try {
			List<Expense_income> expenseList = dao.queryForBeanList(
					"SELECT * FROM tb_expense_income WHERE in_out=?", Expense_income.class,false);
			return expenseList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Expense_income expense) throws Exception {
		expense.setIn_out(false);
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
			expense.setIn_out(false);
			return this.update(expense, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Expense_income get(int id) throws Exception {
		try {
			Expense_income expense = dao.queryForBean(
					"select * from tb_expense_income where id = ? and in_out=?", Expense_income.class,
					id,false);
			return expense;
		} catch (Exception e) {
			throw e;
		}
	}
}
