package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_quoteorder")
public class QuoteOrder implements Serializable{
	@IdentityId
	private int id;//主键
	//private quoteId;//报价ID
	private String quote_ids;//报价IDs
	
	private String excelUrl;
	private String quotationNumber;
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private int created_user;//创建用户
	
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuote_ids() {
		return quote_ids;
	}
	public void setQuote_ids(String quote_ids) {
		this.quote_ids = quote_ids;
	}
	public String getExcelUrl() {
		return excelUrl;
	}
	public void setExcelUrl(String excelUrl) {
		this.excelUrl = excelUrl;
	}
	public String getQuotationNumber() {
		return quotationNumber;
	}
	public void setQuotationNumber(String quotationNumber) {
		this.quotationNumber = quotationNumber;
	}
	
	
}
