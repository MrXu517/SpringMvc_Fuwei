package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_quote")
public class Quote implements Serializable{
	@IdentityId
	private int id;
	private double price;
	private String memo;
	private int sampleId;
	private int salesmanId;
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private int created_user;//创建用户
	
	private Boolean has_sure = false;//是否确认报价（如是否提交订单） 
	private Boolean has_quoted = false;//是否已生成报价单
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getSampleId() {
		return sampleId;
	}
	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}
	public int getSalesmanId() {
		return salesmanId;
	}
	public void setSalesmanId(int salesmanId) {
		this.salesmanId = salesmanId;
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
	public int getCreated_user() {
		return created_user;
	}
	public void setCreated_user(int created_user) {
		this.created_user = created_user;
	}
	public Boolean getHas_sure() {
		return has_sure;
	}
	public void setHas_sure(Boolean has_sure) {
		this.has_sure = has_sure;
	}
	public Boolean getHas_quoted() {
		return has_quoted;
	}
	public void setHas_quoted(Boolean has_quoted) {
		this.has_quoted = has_quoted;
	}
	
	
}

