package com.fuwei.entity.producesystem;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
@Table("tb_fuliaoin_detail")
public class FuliaoInDetail {
	@IdentityId
	private int id;
	private int fuliaoInOutId;
	private int fuliaoId;
	private int locationId;
	private int quantity;
	private Integer fuliaoPurchaseFactoryId;
	
	private String company_orderNumber;//公司订单号
	
	private String company_productNumber;//公司货号
	
	private int fuliaoTypeId;//辅料类型
	
	private String color;//颜色
	
	private String size;//尺寸
	
	private String batch;//批次
	
	private String img;//辅料图片
	
	private String img_s;//中等缩略图
	
	private String img_ss;//缩略图

	private String country;//国家/城市
	private String memo;
	private String fnumber;
	
	
	
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	

	
	public Integer getFuliaoPurchaseFactoryId() {
		return fuliaoPurchaseFactoryId;
	}
	public void setFuliaoPurchaseFactoryId(Integer fuliaoPurchaseFactoryId) {
		this.fuliaoPurchaseFactoryId = fuliaoPurchaseFactoryId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getFuliaoInOutId() {
		return fuliaoInOutId;
	}
	public void setFuliaoInOutId(int fuliaoInOutId) {
		this.fuliaoInOutId = fuliaoInOutId;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getFuliaoId() {
		return fuliaoId;
	}
	public void setFuliaoId(int fuliaoId) {
		this.fuliaoId = fuliaoId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
