package com.fuwei.service.financial;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.financial.Bank;
import com.fuwei.service.BaseService;

@Component
public class BankService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(BankService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取账户列表
	public List<Bank> getList() throws Exception {
		try {
			List<Bank> bankList = dao.queryForBeanList(
					"SELECT * FROM tb_bank", Bank.class);
			return bankList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加账户
	@Transactional
	public int add(Bank bank) throws Exception {
		try{
			return this.insert(bank);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除账户
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_bank WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("账户已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑账户
	public int update(Bank bank) throws Exception {
		try{
			return this.update(bank, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取账户
	public Bank get(int id) throws Exception {
		try {
			Bank bank = dao.queryForBean(
					"select * from tb_bank where id = ?", Bank.class,
					id);
			return bank;
		} catch (Exception e) {
			throw e;
		}
	}
}
