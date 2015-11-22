package com.fuwei.entity;

import java.util.Date;
import java.util.List;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_production_notification")
public class ProductionNotification {
	@IdentityId
	private int id;
	private Integer orderDetailId;// 订单ID，外键
	private String notificationNumber;// 生产通知单号
	private Integer factoryId;// 加工单位

	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;// 创建用户
	
	private int quantity;//生产数量
	private Date end_at;//交货日期
	
	//内容json字符串  json数组， json对象内容：色号，色别，尺寸，生产数量，材料名称，材料数量，损耗，总材料
	private String details;
	
	@Temporary
	private List<ProductionNotificationDetail> detaillist;
	
	
	
	public List<ProductionNotificationDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<ProductionNotificationDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getEnd_at() {
		return end_at;
	}

	public void setEnd_at(Date end_at) {
		this.end_at = end_at;
	}

	public int getId() {
		return id;
	}

	public String getNotificationNumber() {
		return notificationNumber;
	}

	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}

	

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Integer getCreated_user() {
		return created_user;
	}

	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

}
