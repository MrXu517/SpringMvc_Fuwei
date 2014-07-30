package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Order;
import com.fuwei.entity.OrderProduceStatus;

@Component
public class OrderProduceStatusService  extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(QuoteOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取订单额外生产步骤列表
	public List<OrderProduceStatus> getListByOrder(int orderId) throws Exception {
		try {
			List<OrderProduceStatus> order_handleList = dao.queryForBeanList(
					"SELECT * FROM tb_order_produce_status WHERE orderId=?", OrderProduceStatus.class,orderId);
			return order_handleList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//增加步骤
	@Transactional
	public int add(OrderProduceStatus orderProduceStatus) throws Exception {
		try{
			return this.insert(orderProduceStatus);
		}catch(Exception e){
			throw e;
		}
	}
	
	//修改步骤
	@Transactional
	public int update(OrderProduceStatus orderProduceStatus) throws Exception {
		try{
			return this.update(orderProduceStatus, "id",
					"created_user,created_at", true);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除步骤
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_order_produce_status WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("操作已被引用，无法删除，请先删除引用");
			}
			throw e;
		}
	}
	
	// 获取步骤
	public OrderProduceStatus get(int id) throws Exception {
		try {
			OrderProduceStatus OrderProduceStatus = dao.queryForBean(
					"select * from tb_order_produce_status where id = ?", OrderProduceStatus.class, id);
			return OrderProduceStatus;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//获取下一步生产步骤
	public OrderProduceStatus getNext(int orderId,Integer stepId)throws Exception {
		try {
			String sql = "";
			OrderProduceStatus OrderProduceStatus = null;
			if(stepId == null){//若第一次进入动态生产步骤
				OrderProduceStatus = dao.queryForBean(
						 "select * from tb_order_produce_status where orderId=? order by id asc limit 1", OrderProduceStatus.class, orderId);
			}else{
				 OrderProduceStatus = dao.queryForBean(
							"select * from tb_order_produce_status where orderId=? AND id > ? order by id asc limit 1", OrderProduceStatus.class, orderId,stepId);
			}
			return OrderProduceStatus;
		} catch (Exception e) {
			throw e;
		}
	}
}
