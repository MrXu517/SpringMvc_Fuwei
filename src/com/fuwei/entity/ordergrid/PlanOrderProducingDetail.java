package com.fuwei.entity.ordergrid;

public class PlanOrderProducingDetail {
	private String project ; //生产项目
	private Integer quantity ; //计划数量
	private String complete_at;//计划完成时间
	private Integer actual_quantity;//截止期实际完成数量
	private String sign;//签字
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getComplete_at() {
		return complete_at;
	}
	public void setComplete_at(String complete_at) {
		this.complete_at = complete_at;
	}
	public Integer getActual_quantity() {
		return actual_quantity;
	}
	public void setActual_quantity(Integer actual_quantity) {
		this.actual_quantity = actual_quantity;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
