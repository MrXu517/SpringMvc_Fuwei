package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.Department;

@Component
public class DepartmentService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(DepartmentService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取部门列表
	public List<Department> getList() throws Exception {
		try {
			List<Department> departmentList = dao.queryForBeanList(
					"SELECT * FROM tb_department", Department.class);
			return departmentList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加部门
	@Transactional
	public int add(Department department) throws Exception {
		try{
			return this.insert(department);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除部门
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_department WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("部门已被引用，无法删除，请先删除与部门有关的员工等");
			}
			throw e;
		}
	}

	// 编辑部门
	public int update(Department department) throws Exception {
		try{
			return this.update(department, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取部门
	public Department get(int id) throws Exception {
		try {
			Department department = dao.queryForBean(
					"select * from tb_department where id = ?", Department.class,
					id);
			return department;
		} catch (Exception e) {
			throw e;
		}
	}
}
