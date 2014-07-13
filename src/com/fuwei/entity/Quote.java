package com.fuwei.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_quote")
public class Quote implements Serializable{
	@IdentityId
	private int id;

	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private int created_user;//创建用户
	
	private int quotePriceId;
	private int sampleId;
	
	@Temporary
	private Sample sample = new Sample();
	@Temporary
	private QuotePrice quotePrice = new QuotePrice();
	
	
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	public int getSampleId() {
		return sampleId;
	}
	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}
	
	public QuotePrice getQuotePrice() {
		return quotePrice;
	}
	public void setQuotePrice(QuotePrice quotePrice) {
		this.quotePrice = quotePrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getQuotePriceId() {
		return quotePriceId;
	}
	public void setQuotePriceId(int quotePriceId) {
		quotePrice.setId(quotePriceId);
		this.quotePriceId = quotePriceId;
	}
	
	public void setQprice(double price) {
		quotePrice.setPrice(price);
	}
	public void setQmemo(String memo) {
		quotePrice.setMemo(memo);
	}
	public void setQsampleId(int sampleId) {
		quotePrice.setSampleId(sampleId);
	}
	public void setQsalesmanId(int salesmanId) {
		quotePrice.setSalesmanId(salesmanId);
	}
	public void setQcreated_at(Date created_at) {
		quotePrice.setCreated_at(created_at);
	}
	public void setQupdated_at(Date updated_at) {
		quotePrice.setUpdated_at(updated_at);
	}
	public void setQcreated_user(int created_user) {
		quotePrice.setCreated_user(created_user);
	}
	public void setSid(Integer id){
		sample.setId(id);
	}
	public void setSname(String name){
		sample.setName(name);
	}
	public void setSimg(String img){
		sample.setImg(img);
	}
	public void setSmaterial(String material){
		sample.setMaterial(material);
	}
	public void setSweight(Double weight){
		sample.setWeight(weight);
	}
	public void setSsize(String size){
		sample.setSize(size);
	}
	public void setScost(Double cost){
		sample.setCost(cost);
	}
	public void setSproductNumber(String productNumber){
		sample.setProductNumber(productNumber);
	}
	public void setSmachine(String machine){
		sample.setMachine(machine);
	}
	public void setSmemo(String memo){
		sample.setMemo(memo);
	}
	public void setScreated_at(Date created_at){
		sample.setCreated_at(created_at);
	}
	public void setSupdated_at(Date updated_at){
		sample.setUpdated_at(updated_at);
	}
	public void setScreated_user(Integer created_user){
		sample.setCreated_user(created_user);
	}
	public void setScharge_user(Integer charge_user){
		sample.setCharge_user(charge_user);
	}
	public void setSdetail(String detail){
		sample.setDetail(detail);
	}
	public void setShelp_code(String help_code){
		sample.setHelp_code(help_code);
	}
	public void setShas_detail(Boolean has_detail) {
		sample.setHas_detail(has_detail);
	}
}

