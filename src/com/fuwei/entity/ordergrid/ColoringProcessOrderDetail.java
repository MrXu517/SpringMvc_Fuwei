package com.fuwei.entity.ordergrid;

public class ColoringProcessOrderDetail {
	private String color;//色号
	private Integer material ; //材料
	private int quantity;//数量
	private int factoryId;//染色单位
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getMaterial() {
		return material;
	}
	public void setMaterial(Integer material) {
		this.material = material;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	
	
}
