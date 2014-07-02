package com.fuwei.entity;

import java.io.Serializable;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_role_module")
public class Role_Module implements Serializable {
	
	/**
	 * 
	 */


	@IdentityId
	private int id;
	
	private int roleId;
	
	private int moduleId;
	
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	
	
}
