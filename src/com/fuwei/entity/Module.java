package com.fuwei.entity;

import java.io.Serializable;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_module")
public class Module implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7955494533827010207L;
	
	@IdentityId
	private int id;
	
	private String name;//模块名称
	private String url;//模块url
	private String decription;//模块描述
	private boolean inUse;//是否启用
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