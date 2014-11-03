package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Factory;

@Component
public class FactoryService extends BaseService{
	private Logger log = org.apache.log4j.LogManager.getLogger(CompanyService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Factory> getList() throws Exception {
		try {
			List<Factory> FactoryList = dao.queryForBeanList(
					"SELECT * FROM tb_factory", Factory.class);
			return FactoryList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Factory Factory) throws Exception {
		try{
			return this.insert(Factory);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_factory WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("加工工厂已被引用，无法删除，请先删除与工厂有关的生产单等");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Factory Factory) throws Exception {
		try{
			return this.update(Factory, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取
	public Factory get(int id) throws Exception {
		try {
			Factory Factory = dao.queryForBean(
					"select * from tb_factory where id = ?", Factory.class,
					id);
			return Factory;
		} catch (Exception e) {
			throw e;
		}
	}
}
