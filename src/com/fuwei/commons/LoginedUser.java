package com.fuwei.commons;

import java.io.Serializable;

import com.fuwei.entity.Company;
import com.fuwei.entity.User;

public class LoginedUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7402368945074884653L;

	private User loginedUser;

	// 1:个人用户 2：企业用户 3：企业 4：admin
	private int role;

	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public User getLoginedUser() {
		return loginedUser;
	}

	public void setLoginedUser(User loginedUser) {
		this.loginedUser = loginedUser;
	}

}
