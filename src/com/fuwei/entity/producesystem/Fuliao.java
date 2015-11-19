package com.fuwei.entity.producesystem;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_fuliao")
public class Fuliao {
	@IdentityId
	private int id;
	private int orderId;//厂订单ID
	private String orderNumber;//厂订单号
	private String company_orderNumber;//公司订单号
	private String company_productNumber;//公司货号
	private String country;//国家/城市
	private int fuliaoTypeId;//辅料类型
	private String color;//颜色
	private String size;//尺寸
	private String batch;//批次
	private String img;//辅料图片
	private String img_s;//中等缩略图
	private String img_ss;//缩略图
	private int location_size;//库位容量大小
	
	private String memo;
	private Integer created_user;//创建人
	private Date created_at;
	private Date updated_at;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCompany_orderNumber() {
		return company_orderNumber;
	}
	public void setCompany_orderNumber(String company_orderNumber) {
		this.company_orderNumber = company_orderNumber;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getFuliaoTypeId() {
		return fuliaoTypeId;
	}
	public void setFuliaoTypeId(int fuliaoTypeId) {
		this.fuliaoTypeId = fuliaoTypeId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public int getLocation_size() {
		return location_size;
	}
	public void setLocation_size(int location_size) {
		this.location_size = location_size;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getCreated_user() {
		return created_user;
	}
	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
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
	public String getImg_s() {
		return img_s;
	}
	public void setImg_s(String img_s) {
		this.img_s = img_s;
	}
	public String getImg_ss() {
		return img_ss;
	}
	public void setImg_ss(String img_ss) {
		this.img_ss = img_ss;
	}
	
	public String getLocationSizeString() {
		if(this.location_size == 3){
			return "大";
		}else if(this.location_size == 2){
			return "中";
		}else if(this.location_size == 1){
			return "小";
		}else{
			return "其他";
		}
	}
	
}
