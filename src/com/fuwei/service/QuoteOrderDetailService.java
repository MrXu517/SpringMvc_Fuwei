package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.QuoteOrderDetail;

@Component
public class QuoteOrderDetailService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取报价单详情列表
	public List<QuoteOrderDetail> getListByQuoteOrder(int quoteOrderId) throws Exception {
		try {
			List<QuoteOrderDetail> quoteorder_quoteList = dao.queryForBeanList(
					"SELECT * FROM tb_quoteorder_quote WHERE quoteOrderId=?", QuoteOrderDetail.class);
			return quoteorder_quoteList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加详情
	@Transactional
	public int add(QuoteOrderDetail detail) throws Exception {
		try{
			return this.insert(detail);
		}catch(Exception e){
			throw e;
		}
	}
	
	@Transactional
	public int addBatch(List<QuoteOrderDetail> detailList) throws Exception {
		try{
			for(QuoteOrderDetail detail : detailList){
				this.insert(detail);
			}
			return 1;
		}catch(Exception e){
			throw e;
		}
	}
//	// 删除报价
//	public int remove(int id) throws Exception {
//		try{
//			return dao.update("delete from tb_quote WHERE  id = ?", id);
//		}catch(Exception e){
//			throw e;
//		}
//	}

//	// 编辑报价
//	public int update(Quote quote) throws Exception {
//		try{
//			//UPDATE tb_user SET inUse = true WHERE  id = ?
//			return this.update(quote, "id", null,true);
//		}catch(Exception e){
//			throw e;
//		}
//	}
	
//	// 获取报价
//	public Quote get(int id) throws Exception {
//		try {
//			Quote quote = dao.queryForBean(
//					"select * from tb_quote where id = ?", Quote.class,
//					id);
//			return quote;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
}