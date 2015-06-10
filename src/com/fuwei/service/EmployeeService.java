package com.fuwei.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.util.DateTool;

@Component
public class EmployeeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(EmployeeService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//获取花名册 ： 在职的员工
	public List<Employee> getInUseList() throws Exception {
		try {
			List<Employee> employeeList = dao.queryForBeanList(
					"SELECT * FROM tb_employee WHERE inUse=1 order by departmentId asc,created_at asc", Employee.class);
			return employeeList;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取列表
	public List<Employee> getList(Boolean inUse) throws Exception {
		try {
			if(inUse == null){
				List<Employee> employeeList = dao.queryForBeanList(
						"SELECT * FROM tb_employee", Employee.class);
				return employeeList;
			}else{
				List<Employee> employeeList = dao.queryForBeanList(
						"SELECT * FROM tb_employee WHERE inUse=?", Employee.class,inUse);
				return employeeList;
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
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
			employee.setInUse(true);
			return this.insert(employee);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("员工姓名必须唯一");
			}
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
			return this.update(employee, "id", "created_at,created_user,inUse",true);
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
	
	//员工离职
	public int cancel(int id,Date leave_at) throws Exception{
		try{
			Employee employee = this.get(id);
			if(!employee.getInUse()){
				throw new Exception("员工已离职，离职时间：" + DateTool.formateDate(employee.getLeave_at(), "yyyy/MM/dd hh:mm:ss") );
			}
			employee.setUpdated_at(DateTool.now());
			employee.setInUse(false);
			employee.setLeave_at(leave_at);
			this.update(employee, "id", null);
			return employee.getId();
		}catch(Exception e){
			throw e;
		}
	}
}
