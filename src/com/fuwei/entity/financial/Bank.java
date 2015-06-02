package com.fuwei.entity.financial;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_bank")
public class Bank implements Serializable {

	/**
	 * 
	 */

	@IdentityId
	private int id;// ID

	private String name;// 名称
	
	private String number;//纳税人识别号
	
	private Boolean is_enterprise = true;//个人或企业,默认是企业
	
	private String bank_name;//开户行
	
	private String bank_no;//帐号


	private Date created_at;// 创建时间

	private Date updated_at;// 最近更新时间

	private String address;// 详细地址：区-镇-街道等

	
	private Integer created_user;//创建用户
	
//	private int company_id; //外贸公司id
//	private String company_name; //外贸公司名称

	

//
//	public int getCompany_id() {
//		return company_id;
//	}
//
//	public void setCompany_id(int company_id) {
//		this.company_id = company_id;
//	}
//
//	public String getCompany_name() {
//		return company_name;
//	}
//
//	public void setCompany_name(String company_name) {
//		this.company_name = company_name;
//	}

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Boolean getIs_enterprise() {
		return is_enterprise;
	}

	
	public void setIs_enterprise(Boolean is_enterprise) {
		this.is_enterprise = is_enterprise;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_no() {
		return bank_no;
	}

	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getCreated_user() {
		return created_user;
	}

	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	
	
	public String getIs_enterpriseString(){
		return this.is_enterprise?"企业":"个人";
	}
	
}
