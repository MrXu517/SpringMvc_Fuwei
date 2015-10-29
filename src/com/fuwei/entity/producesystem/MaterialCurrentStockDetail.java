package com.fuwei.entity.producesystem;

public class MaterialCurrentStockDetail {
	private int id;
	private String color;//色号
	private Integer material;//材料
	private double stock_quantity;//库存数量
	private double plan_quantity;//计划数量
	private double in_quantity;//计划数量
	private double return_quantity;//计划数量
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getMaterial() {
		return material;
	}
	public void setMaterial(Integer material) {
		this.material = material;
	}
	public double getPlan_quantity() {
		return plan_quantity;
	}
	public void setPlan_quantity(double plan_quantity) {
		this.plan_quantity = plan_quantity;
	}
	public double getIn_quantity() {
		return in_quantity;
	}
	public void setIn_quantity(double in_quantity) {
		this.in_quantity = in_quantity;
	}
	public double getReturn_quantity() {
		return return_quantity;
	}
	public void setReturn_quantity(double return_quantity) {
		this.return_quantity = return_quantity;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getStock_quantity() {
		return stock_quantity;
	}
	public void setStock_quantity(double stock_quantity) {
		this.stock_quantity = stock_quantity;
	}
	
}

