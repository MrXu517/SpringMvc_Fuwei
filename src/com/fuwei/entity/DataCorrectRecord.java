package com.fuwei.entity;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_datacorrectrecord")
public class DataCorrectRecord {
	@IdentityId
	private int id;
	private String operation;//操作类型：删除、编辑
	private String tb_table;//纠正的单据
	private String description;//操作描述
	private int created_user;//操作用户
	private Date created_at;//操作时间
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreated_user() {
		return created_user;
	}
	public void setCreated_user(int created_user) {
		this.created_user = created_user;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getTb_table() {
		return tb_table;
	}
	public void setTb_table(String tb_table) {
		this.tb_table = tb_table;
	}
	
	
}
