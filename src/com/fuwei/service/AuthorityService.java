package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.entity.Authority;
@Component
public class AuthorityService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;
	
	//获取某模块详情
	public Authority get(int id) throws Exception{
		try{
			Authority authority = dao.queryForBean("SELECT * FROM tb_authority WHERE id = ?", Authority.class, id);
			if(authority == null){
				throw new Exception("没有该模块");
			}
			return authority;
		}catch(Exception e){
			throw e;
		}
	}
	
	//根据角色roleID获取模块
	public List<Authority> getList(int roleId) throws Exception{
		try{
			List<Authority> moduleList = dao.queryForBeanList("SELECT * FROM tb_authority WHERE id in (SELECT authorityId FROM tb_role_authority WHERE roleId = ?)", Authority.class, roleId);
			return moduleList;
		}catch(Exception e){
			throw e;
		}
	}
}
