package com.fuwei.service.financial;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Expense_income_invoice;
import com.fuwei.service.BaseService;

@Component
public class BankService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(BankService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取账户列表
	public List<Bank> getList(Boolean is_enterprise) throws Exception {
		try {
			String sql = "SELECT * FROM tb_bank";
			if(is_enterprise!=null){
				sql = sql + " WHERE is_enterprise='"+(is_enterprise == true?"1":0 )+"'";
			}
			List<Bank> bankList = dao.queryForBeanList(sql,
					Bank.class);
			return bankList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取账户列表
	public List<Bank> getList() throws Exception {
		try {
			List<Bank> bankList = dao.queryForBeanList("SELECT * FROM tb_bank",
					Bank.class);
			return bankList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加账户
	@Transactional
	public int add(Bank bank) throws Exception {
		try {
			return this.insert(bank);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除账户
	public int remove(int id) throws Exception {
		try {
			return dao.update("delete from tb_bank WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("账户已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑账户
	public int update(Bank bank) throws Exception {
		try {
			return this.update(bank, "id", "created_at,created_user", true);
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取账户
	public Bank get(int id) throws Exception {
		try {
			Bank bank = dao.queryForBean("select * from tb_bank where id = ?",
					Bank.class, id);
			return bank;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public boolean batch_add(List<Bank> list) throws Exception {
		String sql = "INSERT INTO tb_bank(name,number,is_enterprise,bank_name,bank_no,created_at,updated_at,address,created_user) VALUES(?,?,?,?,?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Bank item : list) {
			batchArgs.add(new Object[] { item.getName(), item.getNumber(),
					item.getIs_enterprise(), item.getBank_name(),
					item.getBank_no(), item.getCreated_at(),
					item.getUpdated_at(), item.getAddress(),
					item.getCreated_user() });
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1062) {// 外键约束
				log.error(e);
				throw new Exception("账户名称必须唯一");
			}
			throw e;
		}

	}
}
