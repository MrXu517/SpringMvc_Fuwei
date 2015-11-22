package com.fuwei.service.producesystem;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.FuliaoChangeLocation;
import com.fuwei.service.BaseService;

@Component
public class FuliaoChangeLocationService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoChangeLocationService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//获取
	public List<FuliaoChangeLocation> getList(int fuliaoId){
		return dao.queryForBeanList("select * from tb_fuliao_changelocation where fuliaoId=?", FuliaoChangeLocation.class,fuliaoId);
	}

	// 添加,返回主键
	@Transactional
	public int add(FuliaoChangeLocation object) throws Exception {
		try {
			Integer id = this.insert(object);
			return id;
		} catch (Exception e) {
			throw e;
		}
	}

	

	// 获取订单
	public FuliaoChangeLocation get(int id) throws Exception {
		try {
			FuliaoChangeLocation fuliao = dao.queryForBean("select * from tb_fuliao_changelocation where id = ?", FuliaoChangeLocation.class, id);
			return fuliao;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
