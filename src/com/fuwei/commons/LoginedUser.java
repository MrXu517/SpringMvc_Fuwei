package com.fuwei.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fuwei.entity.Authority;
import com.fuwei.entity.Module;
import com.fuwei.entity.Role;
import com.fuwei.entity.User;

public class LoginedUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7402368945074884653L;

	private User loginedUser;
	private Role role;
	private List<Authority> authoritylist = new ArrayList<Authority>();
	
	
//	// 1:个人用户 2：企业用户 3：企业 4：admin
//	private int role;

//	private Company company;
//
//	public Company getCompany() {
//		return company;
//	}
//
//	public void setCompany(Company company) {
//		this.company = company;
//	}

//	public int getRole() {
//		return role;
//	}
//
//	public void setRole(int role) {
//		this.role = role;
//	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}



	public List<Authority> getAuthoritylist() {
		return authoritylist;
	}

	public void setAuthoritylist(List<Authority> authoritylist) {
		this.authoritylist = authoritylist;
	}

	public User getLoginedUser() {
		return loginedUser;
	}

	public void setLoginedUser(User loginedUser) {
		this.loginedUser = loginedUser;
	}

}
