package com.fuwei.service.financial;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.financial.Expense;
import com.fuwei.service.BaseService;

@Component
public class ExpenseService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(ExpenseService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Expense> getList() throws Exception {
		try {
			List<Expense> expenseList = dao.queryForBeanList(
					"SELECT * FROM tb_expense", Expense.class);
			return expenseList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Expense expense) throws Exception {
		try{
			return this.insert(expense);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_expense WHERE  id = ?", id);
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
	public int update(Expense expense) throws Exception {
		try{
			return this.update(expense, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Expense get(int id) throws Exception {
		try {
			Expense expense = dao.queryForBean(
					"select * from tb_expense where id = ?", Expense.class,
					id);
			return expense;
		} catch (Exception e) {
			throw e;
		}
	}
}
