package com.fuwei.entity;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_production_notification")
public class ProductionNotification {
	@IdentityId
	private int id;
	private Integer orderId;//订单ID，外键
	private String notificationNumber;//生产通知单号
	private String processfactory;//加工单位
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
	public int getId() {
		return id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public String getNotificationNumber() {
		return notificationNumber;
	}
	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}
	public String getProcessfactory() {
		return processfactory;
	}
	public void setProcessfactory(String processfactory) {
		this.processfactory = processfactory;
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
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	
}
