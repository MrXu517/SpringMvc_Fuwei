package com.fuwei.entity.financial;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_subject")
public class Subject implements Serializable {
	@IdentityId
	private int id;// 科目ID
	
	private String name;//科目名称
	
	private Boolean in_out;//收入还是支出 ，1 收入 ， 0 支出
	
	private Date created_at;// 创建时间

	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户

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

	public Boolean getIn_out() {
		return in_out;
	}

	public void setIn_out(Boolean in_out) {
		this.in_out = in_out;
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
	
	public String getInorOutString(){
		return this.in_out?"收入":"支出";
	}
}
