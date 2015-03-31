package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Customer;

@Component
public class CustomerService extends BaseService{
	private Logger log = org.apache.log4j.LogManager.getLogger(CustomerService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Customer> getList() throws Exception {
		try {
			List<Customer> customerList = dao.queryForBeanList(
					"SELECT * FROM tb_customer", Customer.class);
			return customerList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Customer customer) throws Exception {
		try{
			return this.insert(customer);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_customer WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("客户已被引用，无法删除，请先删除与客户有关的引用等");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Customer customer) throws Exception {
		try{
			return this.update(customer, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取
	public Customer get(int id) throws Exception {
		try {
			Customer Factory = dao.queryForBean(
					"select * from tb_customer where id = ?", Customer.class,
					id);
			return Factory;
		} catch (Exception e) {
			throw e;
		}
	}
}
