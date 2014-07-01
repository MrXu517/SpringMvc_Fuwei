package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Company;
import com.fuwei.entity.Role;

@Component
public class RoleService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;
	
	//获取某角色详情
	public Role get(int id) throws Exception{
		try{
			Role role = dao.queryForBean("SELECT * FROM tb_role WHERE id = ?", Role.class, id);
			if(role == null){
				throw new Exception("没有该角色");
			}
			return role;
		}catch(Exception e){
			throw e;
		}
	}
	
	// 获取角色列表
	public List<Role> getList() throws Exception {
		try {
			List<Role> roleList = dao.queryForBeanList(
					"SELECT * FROM tb_role", Role.class);
			return roleList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加角色
	@Transactional
	public int add(Role role) throws Exception {
		try{
			return this.insert(role);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除角色
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_role WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑角色
	public int update(Role role) throws Exception {
		try{
			return this.update(role, "id", null);
		}catch(Exception e){
			throw e;
		}

	}

}
