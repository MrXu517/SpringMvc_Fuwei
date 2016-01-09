package com.fuwei.entity.financial;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;
//生产对账单
@Table("tb_producebill")
public class ProduceBill implements Serializable {
	@IdentityId
	private int id;// 对账单ID
	private int factoryId;//工厂Id 
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间	
	private Integer created_user;//创建用户
	private int quantity;//总数量
	private double deduct;//总扣款
	private double amount;//总金额，未减扣款
	private double rate_deduct ; //地税扣款 (总金额 - 总扣款)*0.013
	private double payable_amount;// 总金额 - 总扣款 - 地税扣款
	private int year;//对账年份
	private String memo;

	@Temporary
	private List<ProduceBillDetail> detaillist;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public Integer getCreated_user() {
		return created_user;
	}
	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getDeduct() {
		return deduct;
	}
	public void setDeduct(double deduct) {
		this.deduct = deduct;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getRate_deduct() {
		return rate_deduct;
	}
	public void setRate_deduct(double rate_deduct) {
		this.rate_deduct = rate_deduct;
	}
	public double getPayable_amount() {
		return payable_amount;
	}
	public void setPayable_amount(double payable_amount) {
		this.payable_amount = payable_amount;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<ProduceBillDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<ProduceBillDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	
	
}
