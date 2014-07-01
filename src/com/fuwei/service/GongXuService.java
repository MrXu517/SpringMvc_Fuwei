package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.GongXu;

@Component
public class GongXuService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;

	// 获取公司列表
	public List<GongXu> getList() throws Exception {
		try {
			List<GongXu> gongxuList = dao.queryForBeanList(
					"SELECT * FROM tb_gongxu", GongXu.class);
			return gongxuList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加公司
	@Transactional
	public int add(GongXu gongxu) throws Exception {
		try{
			return this.insert(gongxu);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除公司
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete form tb_gongxu WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑公司
	public int update(GongXu gongxu) throws Exception {
		try{
			return this.update(gongxu, "id", null);
		}catch(Exception e){
			throw e;
		}

	}
}
