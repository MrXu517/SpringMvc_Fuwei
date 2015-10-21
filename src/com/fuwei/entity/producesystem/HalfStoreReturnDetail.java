package com.fuwei.entity.producesystem;

//半成品退货单明细
public class HalfStoreReturnDetail {
	private int id;
	private int planOrderDetailId;//同一个生产单中唯一
	private String color;//颜色
	private double weight;//机织克重
	private Integer yarn;//纱线种类
	private String size;//尺寸
	private int quantity;//退货数量
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlanOrderDetailId() {
		return planOrderDetailId;
	}
	public void setPlanOrderDetailId(int planOrderDetailId) {
		this.planOrderDetailId = planOrderDetailId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Integer getYarn() {
		return yarn;
	}
	public void setYarn(Integer yarn) {
		this.yarn = yarn;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	
	
}
