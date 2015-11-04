package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.Location;
import com.fuwei.service.BaseService;


@Component
public class LocationService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(LocationService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Location> getList() throws Exception {
		try {
			List<Location> locationList = dao.queryForBeanList(
					"SELECT * FROM tb_location", Location.class);
			return locationList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加材料
	@Transactional
	public int add(Location location) throws Exception {
		try{
			return this.insert(location);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除材料
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_location WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与库位有关的引用");
			}
			throw e;
		}
	}

	// 编辑材料
	public int update(Location location) throws Exception {
		try{
			return this.update(location, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取材料
	public Location get(int id) throws Exception {
		try {
			Location location = dao.queryForBean(
					"select * from tb_location where id = ?", Location.class,
					id);
			return location;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取材料
	public Location get(String number) throws Exception {
		try {
			Location location = dao.queryForBean(
					"select * from tb_location where number = ?", Location.class,
					number);
			return location;
		} catch (Exception e) {
			throw e;
		}
	}
}
