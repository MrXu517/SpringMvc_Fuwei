package com.fuwei.entity.producesystem;

public class HalfCurrentStockDetail {
	private int planOrderDetailId;//同一个订单中唯一
	private String color;//颜色
	private double produce_weight;//机织克重
	private Integer yarn;//纱线种类
	private String size;//尺寸
	private int stock_quantity;//库存数量
	
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
	
	public double getProduce_weight() {
		return produce_weight;
	}
	public void setProduce_weight(double produce_weight) {
		this.produce_weight = produce_weight;
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
	public int getStock_quantity() {
		return stock_quantity;
	}
	public void setStock_quantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}
	
}
