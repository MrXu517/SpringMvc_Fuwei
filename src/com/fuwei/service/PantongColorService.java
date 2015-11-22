package com.fuwei.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.PantongColor;


@Component
public class PantongColorService extends BaseService  {
	private Logger log = org.apache.log4j.LogManager.getLogger(PantongColorService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<PantongColor> getList() throws Exception {
		try {
			List<PantongColor> list = dao.queryForBeanList(
					"SELECT * FROM tb_pantongcolor", PantongColor.class);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	

	// 重新建立潘通色卡
	@Transactional
	public boolean reBuild(List<PantongColor> list) throws Exception {
		
		//先清空 tb_pantongcolor表
		jdbc.execute("truncate table tb_pantongcolor");
		//批量填充数据
		String sql = "INSERT INTO tb_pantongcolor(panTongName,sheetNum,rowNum,columnNum) VALUES(?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (PantongColor item : list) {
			batchArgs.add(new Object[] { 
					item.getPanTongName(),item.getSheetNum(),
					item.getRowNum(), item.getColumnNum()});
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("潘通色号必须唯一");
			}
			throw e;
		}
	}

	
	// 获取
	public PantongColor get(String panTongName) throws Exception {
		try {
			PantongColor object = dao.queryForBean(
					"select * from tb_pantongcolor where panTongName = ?", PantongColor.class,
					panTongName);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
