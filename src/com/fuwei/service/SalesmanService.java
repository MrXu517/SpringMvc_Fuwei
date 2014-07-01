package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Salesman;

@Component
public class SalesmanService extends BaseService  {
	@Autowired
	JdbcTemplate jdbc;

	// 获取公司列表
	public List<Salesman> getList() throws Exception {
		try {
			List<Salesman> salesmanList = dao.queryForBeanList(
					"SELECT * FROM tb_salesman", Salesman.class);
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
			return dao.update("delete form tb_salesman WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑公司
	public int update(Salesman salesman) throws Exception {
		try{
			return this.update(salesman, "id", null);
		}catch(Exception e){
			throw e;
		}

	}
}
