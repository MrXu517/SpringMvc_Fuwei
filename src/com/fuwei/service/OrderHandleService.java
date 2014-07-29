package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.OrderHandle;
import com.fuwei.util.CreateNumberUtil;

@Component
public class OrderHandleService  extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(QuoteOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取订单操作列表
	public List<OrderHandle> getListByOrder(int orderId) throws Exception {
		try {
			List<OrderHandle> order_handleList = dao.queryForBeanList(
					"SELECT * FROM tb_order_handle WHERE orderId=?", OrderHandle.class,orderId);
			return order_handleList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//增加操作
	@Transactional
	public int add(OrderHandle orderHandle) throws Exception {
		try{
			return this.insert(orderHandle);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除操作
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_order_handle WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("操作已被引用，无法删除，请先删除引用");
			}
			throw e;
		}
	}

}
