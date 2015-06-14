package com.fuwei.entity.report;

import java.util.Date;

public class Payable {
	private Integer type_id;
	private String type;
	private String company_name;
	private String salesman_name;
	private String subject_name;
	private Double payable;//应付
	private Double pay;//已付
	private Double un_pay;//未付
	private Double un_invoiced ; //支出未收发票
	private Date record_at;//发票记录时间或付款时间
	
	private Integer company_id; //公司
	private Integer subject_id ; //科目
	private Integer bank_id;//对方账户
	private String bank_name;
	private String number ; 
	private String memo;
	
	
	
	
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	public Integer getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}
	public Integer getBank_id() {
		return bank_id;
	}
	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getSalesman_name() {
		return salesman_name;
	}
	public void setSalesman_name(String salesman_name) {
		this.salesman_name = salesman_name;
	}
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	public Double getPayable() {
		return payable;
	}
	public void setPayable(Double payable) {
		this.payable = payable;
	}
	public Double getPay() {
		return pay;
	}
	public void setPay(Double pay) {
		this.pay = pay;
	}
	public Double getUn_pay() {
		return un_pay;
	}
	public void setUn_pay(Double un_pay) {
		this.un_pay = un_pay;
	}
	public Double getUn_invoiced() {
		return un_invoiced;
	}
	public void setUn_invoiced(Double un_invoiced) {
		this.un_invoiced = un_invoiced;
	}
	public Date getRecord_at() {
		return record_at;
	}
	public void setRecord_at(Date record_at) {
		this.record_at = record_at;
	}
	
	public String getTypeString(){
		if(this.type.equals("invoice")){
			return "发票";
		}else if(this.type.equals("expense")){
			return "支出";
		}
		return "";
	}
	
	
}
