package com.fuwei.service.financial;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.financial.Income;
import com.fuwei.service.BaseService;

@Component
public class IncomeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(IncomeService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Income> getList() throws Exception {
		try {
			List<Income> incomeList = dao.queryForBeanList(
					"SELECT * FROM tb_income", Income.class);
			return incomeList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Income income) throws Exception {
		try{
			return this.insert(income);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_income WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("科目已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Income income) throws Exception {
		try{
			return this.update(income, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Income get(int id) throws Exception {
		try {
			Income income = dao.queryForBean(
					"select * from tb_income where id = ?", Income.class,
					id);
			return income;
		} catch (Exception e) {
			throw e;
		}
	}
}
