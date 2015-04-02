package com.fuwei.entity.ordergrid;

public class FuliaoPurchaseOrderDetail {
	private Integer style ; //辅料类型
	private String standardsample;//标准样
	private double quantity;//数量
	private double price;//价格
	private String end_at;//交期
	public Integer getStyle() {
		return style;
	}
	public void setStyle(Integer style) {
		this.style = style;
	}
	public String getStandardsample() {
		return standardsample;
	}
	public void setStandardsample(String standardsample) {
		this.standardsample = standardsample;
	}
	
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getEnd_at() {
		return end_at;
	}
	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}
	
}
