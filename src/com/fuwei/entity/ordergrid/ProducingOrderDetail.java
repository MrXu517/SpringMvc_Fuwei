package com.fuwei.entity.ordergrid;

import java.io.Serializable;

public class ProducingOrderDetail implements Serializable {
	private int planOrderDetailId;
	private String color;//颜色
	private double weight;//克重
	private Integer yarn;//纱线种类
	private String size;//尺寸
	private int quantity;//生产数量
	private double price;//单价
	
	
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
