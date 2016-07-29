package com.fuwei.entity.producesystem.plan;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;

//订单明细分批交期
public class OrderDetailDeliveyBatch {
//	@IdentityId
	private int id;
	private int OrderId;//订单ID
	private int OrderDetailId;//订单明细ID
	private int delivery_quantity;//发货数量
	private Date delivery_date;//分批发货的交期，若不是分批发货，则就是全部发货的交期
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return OrderId;
	}
	public void setOrderId(int orderId) {
		OrderId = orderId;
	}
	public int getOrderDetailId() {
		return OrderDetailId;
	}
	public void setOrderDetailId(int orderDetailId) {
		OrderDetailId = orderDetailId;
	}
	public int getDelivery_quantity() {
		return delivery_quantity;
	}
	public void setDelivery_quantity(int delivery_quantity) {
		this.delivery_quantity = delivery_quantity;
	}
	public Date getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}
	
}
