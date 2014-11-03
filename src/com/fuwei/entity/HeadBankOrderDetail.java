package com.fuwei.entity;

import java.io.Serializable;
import java.util.List;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//头带质量记录单详情
@Table("tb_headbankorder_detail")
public class HeadBankOrderDetail implements Serializable {
	
	@IdentityId
	private int id;
	private Integer headBankOrderId;//头带质量记录单ID
	
	private String color;//颜色
	private double weight;//克重
	private String yarn;//纱线种类
	private String size;//尺寸
	private int quantity;//生产数量
	private double price;//单价
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getHeadBankOrderId() {
		return headBankOrderId;
	}

	public void setHeadBankOrderId(Integer headBankOrderId) {
		this.headBankOrderId = headBankOrderId;
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

	public String getYarn() {
		return yarn;
	}

	public void setYarn(String yarn) {
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
