package com.fuwei.entity.ordergrid;

public class StoreOrderDetail {
	private String color;//色号
	private Integer material;//材料
	private double quantity;//总数量
	private String yarn;//标准样纱
	private Integer factoryId;//领取人 ，必填 （工厂）
	
	
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
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
	
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getYarn() {
		return yarn;
	}
	public void setYarn(String yarn) {
		this.yarn = yarn;
	}
	
	
}
