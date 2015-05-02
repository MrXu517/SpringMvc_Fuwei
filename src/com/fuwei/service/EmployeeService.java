package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.Employee;

@Component
public class EmployeeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(EmployeeService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Employee> getList() throws Exception {
		try {
			List<Employee> employeeList = dao.queryForBeanList(
					"SELECT * FROM tb_employee", Employee.class);
			return employeeList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Employee employee) throws Exception {
		try{
			return this.insert(employee);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_employee WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("员工已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Employee employee) throws Exception {
		try{
			return this.update(employee, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Employee get(int id) throws Exception {
		try {
			Employee employee = dao.queryForBean(
					"select * from tb_employee where id = ?", Employee.class,
					id);
			return employee;
		} catch (Exception e) {
			throw e;
		}
	}
}
