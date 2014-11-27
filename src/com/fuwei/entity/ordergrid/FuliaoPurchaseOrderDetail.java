package com.fuwei.entity.ordergrid;

public class FuliaoPurchaseOrderDetail {
	private String style ; //辅料类型
	private String standardsample;//标准样
	private int quantity;//数量
	private double price;//价格
	private String end_at;//交期
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getStandardsample() {
		return standardsample;
	}
	public void setStandardsample(String standardsample) {
		this.standardsample = standardsample;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
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
