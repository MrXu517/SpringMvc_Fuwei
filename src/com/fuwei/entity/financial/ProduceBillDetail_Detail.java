package com.fuwei.entity.financial;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

//生产单对账单明细的明细
@Table("tb_producebilldetail_detail")
public class ProduceBillDetail_Detail {
	@IdentityId
	private int id;// ID
	private int planOrderDetailId;//同一个生产单中唯一
	private int produceBillId;//生产对账单ID，外键
	private int produceBillDetailId;//生产对账单明细ID
	private int plan_quantity;//计划生产数量
	private int quantity;//实际生产数量
	
	private String color;//颜色
	private double weight;//克重
	private double produce_weight;//机织克重
	private Integer yarn;//纱线种类
	private String size;//尺寸
	private double price;//单价
	private double amount;//金额
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
	public int getProduceBillId() {
		return produceBillId;
	}
	public void setProduceBillId(int produceBillId) {
		this.produceBillId = produceBillId;
	}
	public int getProduceBillDetailId() {
		return produceBillDetailId;
	}
	public void setProduceBillDetailId(int produceBillDetailId) {
		this.produceBillDetailId = produceBillDetailId;
	}
	public int getPlan_quantity() {
		return plan_quantity;
	}
	public void setPlan_quantity(int plan_quantity) {
		this.plan_quantity = plan_quantity;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
