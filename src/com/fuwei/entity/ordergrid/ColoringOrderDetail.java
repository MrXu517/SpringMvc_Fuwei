package com.fuwei.entity.ordergrid;

public class ColoringOrderDetail {
	private String color;//色号
	private Integer material ; //材料
	private double quantity;//数量
	private String standardyarn;//标准样纱
	public Integer getMaterial() {
		return material;
	}
	public void setMaterial(Integer material) {
		this.material = material;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getStandardyarn() {
		return standardyarn;
	}
	public void setStandardyarn(String standardyarn) {
		this.standardyarn = standardyarn;
	}
	
	
	
}
