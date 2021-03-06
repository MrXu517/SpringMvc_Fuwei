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
	private Logger log = org.apache.log4j.LogManager.getLogger(FactoryService.class);
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
			Factory.setInUse(true);
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
			return this.update(Factory, "id", "created_at,created_user,inUse",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 停用
	public int disable(int factoryId) throws Exception {
		try{
			return dao.update("update tb_factory set inUse=0 WHERE  id = ?", factoryId);
		}catch(Exception e){
			throw e;
		}
	}
	// 启用
	public int enable(int factoryId) throws Exception {
		try{
			return dao.update("update tb_factory set inUse=1 WHERE  id = ?", factoryId);
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
	
	// 获取
	public Factory getByNameTYPE3(String name) throws Exception {
		try {
			Factory Factory = dao.queryForBean(
					"select * from tb_factory where type=3 and name = ?", Factory.class,
					name);
			return Factory;
		} catch (Exception e) {
			throw e;
		}
	}
}
