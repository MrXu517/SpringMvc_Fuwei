package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Sample;

@Component
public class SampleService extends BaseService{
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取待核价列表 has_detail = false
	public List<Sample> getUnDetailList() throws Exception {
		try {
			List<Sample> sampleList = dao.queryForBeanList(
					"SELECT * FROM tb_sample WHERE has_detail=0", Sample.class);
			return sampleList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取首页的样品列表 has_detail = true
	public List<Sample> getList() throws Exception {
		try {
			List<Sample> sampleList = dao.queryForBeanList(
					"SELECT * FROM tb_sample WHERE has_detail=1", Sample.class);
			return sampleList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加样品
	@Transactional
	public int add(Sample sample) throws Exception {
		try{
			return this.insert(sample);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除公司
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_sample WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑公司
	public int update(Sample sample) throws Exception {
		try{
			return this.update(sample, "id", "created_user,detail,has_detail,created_at");
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取样品
	public Sample get(int id) throws Exception {
		try {
			Sample sample = dao.queryForBean(
					"select * from tb_sample where id = ?", Sample.class,
					id);
			return sample;
		} catch (Exception e) {
			throw e;
		}
	}
}
