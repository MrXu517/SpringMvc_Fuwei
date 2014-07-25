package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.QuotePrice;


@Component
public class QuotePriceService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(QuotePriceService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	
	// 获取某样品的价格列表
	public List<QuotePrice> getList(int sampleId) throws Exception {
		try {
			List<QuotePrice> quotepriceList = dao.queryForBeanList(
					"SELECT * FROM tb_quoteprice WHERE sampleId=? order by created_at desc", QuotePrice.class,sampleId);
			return quotepriceList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加报价价格
	@Transactional
	public int add(QuotePrice QuotePrice) throws Exception {
		try{
			return this.insert(QuotePrice);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除报价价格
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_quoteprice WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("公司价格已被引用，无法删除，请先删除报价");
			}
			throw e;
		}
	}

	// 编辑报价价格
	public int update(QuotePrice QuotePrice) throws Exception {
		try{
			//UPDATE tb_user SET inUse = true WHERE  id = ?
			return this.update(QuotePrice, "id", null,true);
		}catch(Exception e){
			throw e;
		}
	}
	
	// 获取报价价格
	public QuotePrice get(int id) throws Exception {
		try {
			QuotePrice QuotePrice = dao.queryForBean(
					"select * from tb_quoteprice where id = ?", QuotePrice.class,
					id);
			return QuotePrice;
		} catch (Exception e) {
			throw e;
		}
	}
}
