package com.fuwei.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Company;
import com.fuwei.entity.ProductionNotification;
import com.fuwei.util.CreateNumberUtil;

@Component
public class ProductionNotificationService extends BaseService{
	private Logger log = org.apache.log4j.LogManager.getLogger(CompanyService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 根据OrderId获取生产单
	public ProductionNotification getByOrderId(int orderId) throws Exception {
		try {
			ProductionNotification ProductionNotification = dao.queryForBean(
					"select * from tb_production_notification where orderId = ? limit 1", ProductionNotification.class,
					orderId);
			return ProductionNotification;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取生产单
	public ProductionNotification get(int id) throws Exception {
		try {
			ProductionNotification ProductionNotification = dao.queryForBean(
					"select * from tb_production_notification where id = ?", ProductionNotification.class,
					id);
			return ProductionNotification;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 编辑公司
	public int update(ProductionNotification ProductionNotification) throws Exception {
		try{
			return this.update(ProductionNotification, "id", "created_user,orderId,created_at,notificationNumber",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 添加生产单
	@Transactional
	public int add(ProductionNotification ProductionNotification) throws Exception {
		try{
			if(ProductionNotification.getOrderId() == null){
				throw new Exception("创建生产单时，订单ID不能为空");
			}
			Integer ProductionNotificationId = this.insert(ProductionNotification);
			String notificationNumber = CreateNumberUtil.createProductNotificationNumber(ProductionNotificationId);
			ProductionNotification.setNotificationNumber(notificationNumber);
			ProductionNotification.setId(ProductionNotificationId);
			this.update(ProductionNotification, "id", null);
			return ProductionNotificationId;
		}catch(Exception e){
			throw e;
		}
	}
}
