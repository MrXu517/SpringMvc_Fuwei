package com.fuwei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
}
