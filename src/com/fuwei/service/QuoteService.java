
package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Quote;


@Component
public class QuoteService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取预报价列表（所有报价）(含报价价格与样品详情）
	public List<Quote> getDetailList() throws Exception {
		try {
			List<Quote> quoteList = dao.queryForBeanList(
					"SELECT t.*,s.id as sid,s.name as sname,s.img as simg,s.material as smaterial,s.weight as sweight,s.size as ssize,s.cost as scost,s.productNumber as sproductNumber,s.machine as smachine,s.memo as smemo,s.created_at as screated_at, s.updated_at as supdated_at , s.created_user as screated_user , s.charge_user as scharge_user, s.detail as sdetail,s.has_detail as shas_detail,s.help_code as shelp_code,q.id as qid,q.price as qprice,q.memo as qmemo,q.sampleId as qsampleId,q.salesmanId as qsalesmanId ,q.created_at as qcreated_at,q.updated_at as qupdated_at , q.created_user as qcreated_user FROM tb_quote t, tb_sample s,tb_quoteprice q WHERE t.sampleId = s.id AND t.quotePriceId=q.id", Quote.class);
			return quoteList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取预报价列表（所有报价不含报价价格与样品详情）
	public List<Quote> getList() throws Exception {
		try {
			List<Quote> quoteList = dao.queryForBeanList(
					"SELECT * FROM tb_quote", Quote.class);
			return quoteList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加报价
	@Transactional
	public int add(Quote quote) throws Exception {
		try{
			return this.insert(quote);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除报价
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_quote WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	// 编辑报价
	public int update(Quote quote) throws Exception {
		try{
			//UPDATE tb_user SET inUse = true WHERE  id = ?
			return this.update(quote, "id", null,true);
		}catch(Exception e){
			throw e;
		}
	}
	
	// 获取报价
	public Quote get(int id) throws Exception {
		try {
			Quote quote = dao.queryForBean(
					"select * from tb_quote where id = ?", Quote.class,
					id);
			return quote;
		} catch (Exception e) {
			throw e;
		}
	}
}
