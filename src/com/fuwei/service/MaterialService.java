package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Material;


@Component
public class MaterialService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(CompanyService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取材料列表
	public List<Material> getList() throws Exception {
		try {
			List<Material> materialList = dao.queryForBeanList(
					"SELECT * FROM tb_material", Material.class);
			return materialList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加材料
	@Transactional
	public int add(Material material) throws Exception {
		try{
			return this.insert(material);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除材料
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_material WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("材料已被引用，无法删除，请先删除与材料有关的引用");
			}
			throw e;
		}
	}

	// 编辑材料
	public int update(Material material) throws Exception {
		try{
			return this.update(material, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取材料
	public Material get(int id) throws Exception {
		try {
			Material material = dao.queryForBean(
					"select * from tb_material where id = ?", Material.class,
					id);
			return material;
		} catch (Exception e) {
			throw e;
		}
	}
}
