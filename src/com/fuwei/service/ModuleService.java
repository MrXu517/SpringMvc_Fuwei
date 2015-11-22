package com.fuwei.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.entity.Module;

@Component
public class ModuleService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(ModuleService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//获取某模块详情
	public Module get(int id) throws Exception{
		try{
			Module module = dao.queryForBean("SELECT * FROM tb_module WHERE id = ?", Module.class, id);
			if(module == null){
				throw new Exception("没有该模块");
			}
			return module;
		}catch(Exception e){
			throw e;
		}
	}
	
	//根据角色roleID获取模块
	public List<Module> getList(int roleId) throws Exception{
		try{
			List<Module> moduleList = dao.queryForBeanList("SELECT * FROM tb_module WHERE id in (SELECT moduleId FROM tb_role_module WHERE roleId = ?)", Module.class, roleId);
			return moduleList;
		}catch(Exception e){
			throw e;
		}
	}
}
