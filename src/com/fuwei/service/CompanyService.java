package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Company;
import com.fuwei.entity.User;

@Component
public class CompanyService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;

	// 获取公司列表
	public List<Company> getList(int roleId) throws Exception {
		try {
			List<Company> companyList = dao.queryForBeanList(
					"SELECT * FROM tb_company", Company.class, roleId);
			return companyList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加公司
	@Transactional
	public int add(Company company) throws Exception {
		try{
			return this.insert(company);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除公司
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete form tb_company WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑公司
	public int update(Company company) throws Exception {
		try{
			return this.update(company, "id", null);
		}catch(Exception e){
			throw e;
		}

	}
}
