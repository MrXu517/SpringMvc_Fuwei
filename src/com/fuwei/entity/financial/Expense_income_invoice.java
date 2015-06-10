package com.fuwei.entity.financial;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_expense_income_invoice")
public class Expense_income_invoice {
	
	@IdentityId
	private int id;
	
	private int expense_income_id;
	
	private int invoice_id;
	
	private Date created_at;// 创建时间

	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
	private double amount;//匹配金额
	
	

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExpense_income_id() {
		return expense_income_id;
	}

	public void setExpense_income_id(int expense_income_id) {
		this.expense_income_id = expense_income_id;
	}

	public int getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(int invoice_id) {
		this.invoice_id = invoice_id;
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
	
	
}
