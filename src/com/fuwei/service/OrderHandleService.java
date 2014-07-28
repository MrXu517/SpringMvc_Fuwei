package com.fuwei.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.entity.OrderHandle;

@Component
public class OrderHandleService  extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(QuoteOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取报价单详情列表
	public List<OrderHandle> getListByOrder(int orderId) throws Exception {
		try {
			List<OrderHandle> order_handleList = dao.queryForBeanList(
					"SELECT * FROM tb_order_handle WHERE orderId=?", OrderHandle.class,orderId);
			return order_handleList;
		} catch (Exception e) {
			throw e;
		}
	}

}
