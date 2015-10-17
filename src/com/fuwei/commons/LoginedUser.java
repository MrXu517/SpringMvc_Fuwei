package com.fuwei.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fuwei.entity.Authority;
import com.fuwei.entity.Message;
import com.fuwei.entity.Module;
import com.fuwei.entity.Role;
import com.fuwei.entity.User;
import com.fuwei.service.CompanyService;
import com.fuwei.service.CustomerService;
import com.fuwei.service.FactoryService;
import com.fuwei.service.GongXuService;
import com.fuwei.service.MaterialService;
import com.fuwei.service.MessageService;
import com.fuwei.service.RoleService;
import com.fuwei.service.SalesmanService;
import com.fuwei.service.UserService;

public class LoginedUser implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7402368945074884653L;
	
	public LoginedUser() {
		
	}
	public void reloadMessage() throws Exception{
		MessageService messageService= (MessageService) SystemContextUtils
		.getBean(MessageService.class);	
		this.message_count = messageService.getReceiveList_UnRead_Count(this.loginedUser.getId());
		SystemCache.setUserCacheUpdate(this.loginedUser.getId(), false);
	}

	private User loginedUser;
	private Role role;
//	private List<Authority> authoritylist = new ArrayList<Authority>();
	private Map<String,Authority> authorityMap = new HashMap<String, Authority>();
	private int message_count = 0;//未读信息列表
	
	
	
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

	public Map<String, Authority> getAuthorityMap() {
		return authorityMap;
	}
	public void setAuthorityMap(Map<String, Authority> authorityMap) {
		this.authorityMap = authorityMap;
	}
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}



//	public List<Authority> getAuthoritylist() {
//		return authoritylist;
//	}
//
//	public void setAuthoritylist(List<Authority> authoritylist) {
//		this.authoritylist = authoritylist;
//	}

	public User getLoginedUser() {
		return loginedUser;
	}

	public void setLoginedUser(User loginedUser) {
		this.loginedUser = loginedUser;
	}

	public int getMessage_count() {
		return message_count;
	}
	public void setMessage_count(int message_count) {
		this.message_count = message_count;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
