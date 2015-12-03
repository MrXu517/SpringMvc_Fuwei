package com.fuwei.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.DataCorrectRecord;
@Component
public class DataCorrectRecordService  extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(DataCorrectRecordService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取所有数据纠正的操作记录
	public List<DataCorrectRecord> getList() throws Exception {
		try {
			List<DataCorrectRecord> list = dao.queryForBeanList(
					"SELECT * FROM tb_datacorrectrecord order by created_at desc", DataCorrectRecord.class);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 根据操作用户获取数据纠正的操作记录
	public List<DataCorrectRecord> getList(Integer handle_user_id) throws Exception {
		try {
			if(handle_user_id == null){
				return getList();
			}
			List<DataCorrectRecord> list = dao.queryForBeanList(
					"SELECT * FROM tb_datacorrectrecord WHERE created_user=?", DataCorrectRecord.class,handle_user_id);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//增加操作
	@Transactional
	public int add(DataCorrectRecord dataCorrectRecord) throws Exception {
		try{
			return this.insert(dataCorrectRecord);
		}catch(Exception e){
			throw e;
		}
	}

}
