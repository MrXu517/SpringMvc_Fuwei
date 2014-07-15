package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_sample")
public class Sample implements Serializable {
	@IdentityId
	private int id;
	
	private String name;//样品名称
	
	private String img;//图片
	private String material;//材料
	private double weight;//克重
	private String size;//尺寸

	private double cost;//成本
	private String productNumber;//产品编号
	private String machine;//机织
	private String memo;
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
	
	private Integer created_user;//创建用户
	
	
	private Integer charge_user;//打样人
	
	private String detail;//报价详情
	
	private Boolean has_detail = false;//是否已核价
	
	private String help_code;//名称的拼音
	
	public String getHelp_code() {
		return help_code;
	}
	public void setHelp_code(String help_code) {
		this.help_code = help_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
	
	
	public Boolean getHas_detail() {
		return has_detail;
	}
	public void setHas_detail(Boolean has_detail) {
		this.has_detail = has_detail;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public Integer getCreated_user() {
		return created_user;
	}
	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	public Integer getCharge_user() {
		return charge_user;
	}
	public void setCharge_user(Integer charge_user) {
		this.charge_user = charge_user;
	}

}


