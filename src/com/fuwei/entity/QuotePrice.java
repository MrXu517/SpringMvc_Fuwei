package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_quoteprice")
public class QuotePrice implements Serializable{
	@IdentityId
	private int id;
	private double price;
	private String memo;
	private int sampleId;
	private int salesmanId;
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
	
	private String cproductN ; //公司款号
	@Temporary
	private Sample sample = new Sample();
	@Temporary
	private int companyId;
	
	
	
	public String getCproductN() {
		return cproductN;
	}
	public void setCproductN(String cproductN) {
		this.cproductN = cproductN;
	}
	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
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
	
}

