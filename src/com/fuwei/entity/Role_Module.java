package com.fuwei.entity;

import java.io.Serializable;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_role_module")
public class Role_Module implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2779983463985608830L;

	@IdentityId
	private int id;
	
	private int roleId;
	
	private int moduleId;

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
