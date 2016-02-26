package com.fuwei.service.finishstore;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.Department;
import com.fuwei.entity.finishstore.PackProperty;
import com.fuwei.service.BaseService;

@Component
public class PackPropertyService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(PackPropertyService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<PackProperty> getList() throws Exception {
		try {
			List<PackProperty> list = dao.queryForBeanList(
					"SELECT * FROM tb_pack_property", PackProperty.class);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加部门
	@Transactional
	public int add(PackProperty packProperty) throws Exception {
		try{
			return this.insert(packProperty);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除部门
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_pack_property WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与之有关的元素等");
			}
			throw e;
		}
	}

	// 编辑
	public int update(PackProperty packProperty) throws Exception {
		try{
			return this.update(packProperty, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public PackProperty get(int id) throws Exception {
		try {
			PackProperty packProperty = dao.queryForBean(
					"select * from tb_pack_property where id = ?", PackProperty.class,
					id);
			return packProperty;
		} catch (Exception e) {
			throw e;
		}
	}
}
