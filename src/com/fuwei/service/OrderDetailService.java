package com.fuwei.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.OrderDetail;


//@Component
public class OrderDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(OrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取订单样品
	public Map<String,Object> getDetail(int id) throws Exception {
		try {
			Map<String,Object> result = dao.queryForMap(
					"select d.*,o.orderNumber,o.salesmanId,o.companyId,o.end_at from tb_order_detail d ,tb_order o where o.id=d.orderId AND d.id = ?", id);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取订单样品
	public OrderDetail get(int id) throws Exception {
		try {
			OrderDetail OrderDetail = dao.queryForBean(
					"select * from tb_order_detail where id = ?", OrderDetail.class,
					id);
			return OrderDetail;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取订单详情列表
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
	
//	@Transactional
//	public int addHeadBankOrder() throws Exception {
//		try{
//			return dao.update("delete from tb_order_detail WHERE  orderId =?", orderId);
//		}catch(Exception e){
//			throw e;
//		}
//	}
}