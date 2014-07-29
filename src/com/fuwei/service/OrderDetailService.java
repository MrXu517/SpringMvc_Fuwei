package com.fuwei.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.OrderDetail;


@Component
public class OrderDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(QuoteOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取报价单详情列表
	public List<OrderDetail> getListByOrder(int orderId) throws Exception {
		try {
			List<OrderDetail> quoteorder_quoteList = dao.queryForBeanList(
					"SELECT * FROM tb_order_detail WHERE orderId=?", OrderDetail.class,orderId);
			return quoteorder_quoteList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加详情
	@Transactional
	public int add(OrderDetail detail) throws Exception {
		try{
			return this.insert(detail);
		}catch(Exception e){
			throw e;
		}
	}
	
	@Transactional
	public int addBatch(List<OrderDetail> detailList) throws Exception {
		try{
			for(OrderDetail detail : detailList){
				this.insert(detail);
			}
			return 1;
		}catch(Exception e){
			throw e;
		}
	}
	
	@Transactional
	public int deleteBatch(int orderId) throws Exception {
		try{
			return dao.update("delete from tb_order_detail WHERE  orderId =?", orderId);
		}catch(Exception e){
			throw e;
		}
	}
}