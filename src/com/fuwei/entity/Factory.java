package com.fuwei.entity;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_factory")
public class Factory {
	@IdentityId
	private int id;
	private String name;
	private String address;
	private String help_code;//拼音简称
	
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
	private Integer type = 0 ;
	private Boolean isyanchang;//是否在验厂状态下可见
	private Boolean inUse ; //是否启用
	
	public Boolean getInUse() {
		return inUse;
	}
	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}
	public Boolean getIsyanchang() {
		return isyanchang;
	}
	public void setIsyanchang(Boolean isyanchang) {
		this.isyanchang = isyanchang;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHelp_code() {
		return help_code;
	}
	public void setHelp_code(String help_code) {
		this.help_code = help_code;
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
	
	public String getTypeName() {
		Integer type = this.type;
		if(type == 0){
			return "机织、加工";
		}
		if(type == 1){
			return "原材料采购";
		}
		if(type == 2){
			return "染色";
		}
		if(type == 3){
			return "辅料采购";
		}
		return "";
	}
	
	public boolean ismaterialFactory(){
		if(this.type == 1){
			return true;
		}
		return false;
	}
	
}
