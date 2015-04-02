package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Sort;
import com.fuwei.entity.Company;
import com.fuwei.entity.Salesman;

@Component
public class SalesmanService extends BaseService  {
	private Logger log = org.apache.log4j.LogManager.getLogger(SalesmanService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取公司列表
	public List<Salesman> getList() throws Exception {
		try {
			List<Salesman> salesmanList = dao.queryForBeanList(
					"SELECT * FROM tb_salesman order by companyId desc , created_at desc", Salesman.class);
			return salesmanList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加公司
	@Transactional
	public int add(Salesman salesman) throws Exception {
		try{
			return this.insert(salesman);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除公司
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_salesman WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("业务员已被引用，无法删除，请先删除与业务员有关的引用，包括但不仅限于公司价格、报价、报价单等");
			}
			throw e;
		}
	}

	// 编辑公司
	public int update(Salesman salesman) throws Exception {
		try{
			return dao.update("UPDATE tb_salesman SET name=?,companyId=?, tel=?,updated_at=?,help_code=? WHERE  id = ?",
					salesman.getName(),salesman.getCompanyId(),salesman.getTel(),salesman.getUpdated_at(),salesman.getHelp_code(),
					salesman.getId());
		}catch(Exception e){
			throw e;
		}

	}
	// 获取公司
	public Salesman get(int id) throws Exception {
		try {
			Salesman salesman = dao.queryForBean(
					"select * from tb_salesman where id = ?", Salesman.class,
					id);
			return salesman;
		} catch (Exception e) {
			throw e;
		}
	}
}
