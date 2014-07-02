package com.fuwei.entity;

import java.io.Serializable;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_module")
public class Module implements Serializable{

	/**
	 * 
	 */

	
	@IdentityId
	private int id;
	
	private String name;//模块名称
	private String url;//模块url
	private String decription;//模块描述
	private boolean inUse;//是否启用
	
	private int created_user;//创建用户
	
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	
	
}
