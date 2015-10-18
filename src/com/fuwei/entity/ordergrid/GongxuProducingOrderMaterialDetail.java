package com.fuwei.entity.ordergrid;

import java.io.Serializable;

public class GongxuProducingOrderMaterialDetail implements Serializable {
	private Integer material;//材料
	private String color;//色号
	private double quantity;//数量
	private String colorsample;//标准色样
	public Integer getMaterial() {
		return material;
	}
	public void setMaterial(Integer material) {
		this.material = material;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getColorsample() {
		return colorsample;
	}
	public void setColorsample(String colorsample) {
		this.colorsample = colorsample;
	}
	
}