package com.fuwei.entity.financial;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

//进项发票
@Table("tb_invoice")
public class Invoice implements Serializable {
	@IdentityId
	private int id;// 发票ID
	
	private String number;//发票号
	
	private Date print_date;//开票日期
	
	private double amount;//含税总价
	
	private double tax;//税率
	
	private double tax_amount;//税价
	
	private Integer bank_id;
	
	private String bank_name;//对方账户名称
	
	private int type ;//发票类型:1普通发票、2增值税普通发票、3增值税专用发票
	
	private String memo;//备注
	
	private Date created_at;// 创建时间

	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户

	private Boolean in_out = false;//进项还是销项发票，  1：进项， 0：销项
	
	private double match_amount;//已匹配金额
	
	private Integer company_id;
	
	private Integer subject_id;
	
	
	
	
	public Integer getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public double getMatch_amount() {
		return match_amount;
	}

	public void setMatch_amount(double match_amount) {
		this.match_amount = match_amount;
	}

	public Boolean getIn_out() {
		return in_out;
	}

	public void setIn_out(Boolean in_out) {
		this.in_out = in_out;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getPrint_date() {
		return print_date;
	}

	public void setPrint_date(Date print_date) {
		this.print_date = print_date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(double tax_amount) {
		this.tax_amount = tax_amount;
	}

	

	public Integer getBank_id() {
		return bank_id;
	}

	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
	
	public String getTypeString(){
		if(this.type == 1){
			return "普通发票";
		}
		if(this.type == 2){
			return "增值税普通发票";
		}
		if(this.type == 3){
			return "增值税专用发票";
		}
		return "";
	}
	
	public boolean isMatched(){
		return this.amount == this.match_amount;
	}
}
