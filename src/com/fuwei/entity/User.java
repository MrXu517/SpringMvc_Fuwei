package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_user")
public class User implements Serializable {

	/**
	 * 
	 */

	@IdentityId
	private int id;
	private String username ; //用户名（登录时使用）
	private String name;//姓名,一般是中文名称
	private String help_code;//拼音简称
	
	private Integer roleId;//角色（决定有哪些权限）

	private Boolean inUse;//是否启用

	private Date created_at;//创建时间

	private Date updated_at;//最近更新时间

	private String tel;//手机

	private String email;//邮箱

	private String qq;//QQ

	private String password;
	
	//2014-6-24晚添加  是否被锁定字段，表明当前用户是否权限已修改。 重新登录后，则字段归false
	private Boolean locked = false;
	
	private Boolean built_in = false;//是否是内建用户，内建用户（比如：系统管理员是不能被删或者注销的）
	
	@Temporary
	private Boolean need_message_cache_update = false;//是否需要更新 消息缓存,暂时数据，不存放在数据库
	
	private Boolean isyanchang;//是否在验厂状态时需要做数据可见的调整
	
	//2016-6-12添加用户对应员工
	private Integer employeeId;
	
	
	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Boolean getIsyanchang() {
		return isyanchang;
	}

	public void setIsyanchang(Boolean isyanchang) {
		this.isyanchang = isyanchang;
	}

	public Boolean getNeed_message_cache_update() {
		return need_message_cache_update;
	}

	public void setNeed_message_cache_update(Boolean need_message_cache_update) {
		this.need_message_cache_update = need_message_cache_update;
	}

	public Boolean getBuilt_in() {
		return built_in;
	}

	public void setBuilt_in(Boolean built_in) {
		this.built_in = built_in;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHelp_code() {
		return help_code;
	}

	public void setHelp_code(String help_code) {
		this.help_code = help_code;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


}
