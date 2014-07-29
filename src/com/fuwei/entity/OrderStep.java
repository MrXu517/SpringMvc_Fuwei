package com.fuwei.entity;

public class OrderStep {
	private int orderId;
	private Integer stepId;//步骤ID，OrderProduceStatus有，OrderStatus没有
	private String state;//生产步骤中文描述
	private Integer status;//当前状态，所有生产步骤的status一样
	private Boolean checked;//订单是否处于当前这个步骤
	
	
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public Integer getStepId() {
		return stepId;
	}
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
