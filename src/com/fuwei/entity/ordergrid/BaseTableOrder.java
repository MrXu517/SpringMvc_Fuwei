package com.fuwei.entity.ordergrid;

public class BaseTableOrder {
	private Integer status;// 订单状态 -1刚创建
	private String state;// 订单状态描述
	public int getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	// 是否可编辑
	public Boolean isEdit() {
		return true;
	}
	
}
