package com.fuwei.entity.producesystem.plan;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
//@Table("tb_plandetail")
public class PlanDetail {
//	@IdentityId
	private int id;
	private int planId;//计划ID
	private int orderId;//订单ID
	private int orderDetailId;//订单明细ID
	private Date date;//计划时间
	private int gongxuId;//工序ID
	private int plan_quantity;//计划这一天完成多少数量
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getGongxuId() {
		return gongxuId;
	}
	public void setGongxuId(int gongxuId) {
		this.gongxuId = gongxuId;
	}
	public int getPlan_quantity() {
		return plan_quantity;
	}
	public void setPlan_quantity(int plan_quantity) {
		this.plan_quantity = plan_quantity;
	}
	
}
