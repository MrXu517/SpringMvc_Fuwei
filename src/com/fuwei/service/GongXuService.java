package com.fuwei.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Company;
import com.fuwei.entity.GongXu;

@Component
public class GongXuService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(GongXuService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取工序列表
	public List<GongXu> getList() throws Exception {
		try {
			List<GongXu> gongxuList = dao.queryForBeanList(
					"SELECT * FROM tb_gongxu", GongXu.class);
			return gongxuList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加工序
	@Transactional
	public int add(GongXu gongxu) throws Exception {
		try{
			gongxu.setIsProducingOrder(false);
			return this.insert(gongxu);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除工序
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_gongxu WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑工序
	public int update(GongXu gongxu) throws Exception {
		try{
			return dao.update("UPDATE tb_gongxu SET name=?,updated_at=? WHERE  id = ?",
					gongxu.getName(),gongxu.getUpdated_at(),
					gongxu.getId());
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取工序
	public GongXu get(int id) throws Exception {
		try {
			GongXu gongxu = dao.queryForBean(
					"select * from tb_gongxu where id = ?", GongXu.class,
					id);
			return gongxu;
		} catch (Exception e) {
			throw e;
		}
	}
}
