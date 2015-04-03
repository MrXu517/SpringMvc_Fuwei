package com.fuwei.entity.ordergrid;

public class FuliaoPurchaseOrderDetail {
	private Integer style ; //辅料类型
	private String memo;//备注
	private double quantity;//数量
	public Integer getStyle() {
		return style;
	}
	public void setStyle(Integer style) {
		this.style = style;
	}
	
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	
}
