package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.FuliaoType;



@Component
public class FuliaoTypeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FuliaoTypeService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取材料列表
	public List<FuliaoType> getList() throws Exception {
		try {
			List<FuliaoType> materialList = dao.queryForBeanList(
					"SELECT * FROM tb_fuliaotype", FuliaoType.class);
			return materialList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加材料
	@Transactional
	public int add(FuliaoType material) throws Exception {
		try{
			return this.insert(material);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除材料
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_fuliaotype WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("类型已被引用，无法删除，请先删除与辅料类型有关的引用");
			}
			throw e;
		}
	}

	// 编辑材料
	public int update(FuliaoType material) throws Exception {
		try{
			return this.update(material, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取材料
	public FuliaoType get(int id) throws Exception {
		try {
			FuliaoType material = dao.queryForBean(
					"select * from tb_fuliaotype where id = ?", FuliaoType.class,
					id);
			return material;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public FuliaoType getName(String name) throws Exception {
		try {
			FuliaoType obj = dao.queryForBean(
					"select * from tb_fuliaotype where name = ?", FuliaoType.class,
					name);
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}
}
