package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_gongxu")
public class GongXu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6704578104018531898L;
	
	@IdentityId
	private int id;
	private String name;
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
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
	
}
