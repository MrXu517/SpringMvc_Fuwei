package com.fuwei.service.financial;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.financial.SelfAccount;
import com.fuwei.service.BaseService;

@Component
public class SelfAccountService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(SelfAccountService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取列表
	public List<SelfAccount> getList() throws Exception {
		try {
			List<SelfAccount> bankList = dao.queryForBeanList("SELECT * FROM tb_selfaccount",
					SelfAccount.class);
			return bankList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加账户
	@Transactional
	public int add(SelfAccount selfAccount) throws Exception {
		try {
			return this.insert(selfAccount);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除账户
	public int remove(int id) throws Exception {
		try {
			return dao.update("delete from tb_selfaccount WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("账号已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑账户
	public int update(SelfAccount selfAccount) throws Exception {
		try {
			return this.update(selfAccount, "id", "created_at,created_user", true);
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取账户
	public SelfAccount get(int id) throws Exception {
		try {
			SelfAccount selfAccount = dao.queryForBean("select * from tb_selfaccount where id = ?",
					SelfAccount.class, id);
			return selfAccount;
		} catch (Exception e) {
			throw e;
		}
	}
}
