package com.fuwei.entity.financial;

import java.util.Date;
import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_expense_income")
public class Expense_income {
	@IdentityId
	private int id;// 明细ID
	
	private int subject_id;//科目ID
	
	private String subject_name;
	
	private Integer bank_id;
	
	private String bank_name;
	
	private Integer company_id;
	
	private String company_name;//公司
	
	private Integer salesman_id;
	
	private String salesman_name;//业务员
	
	private double amount;//支出金额
	
	private String memo;//备注
	
	private Date expense_at;//支出时间
	
	private Date created_at;// 创建时间

	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
	private Boolean in_out = false;//收入还是支出
	
	private double invoice_amount;//已收发票金额
	
	
		 
	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public double getInvoice_amount() {
		return invoice_amount;
	}

	public void setInvoice_amount(double invoice_amount) {
		this.invoice_amount = invoice_amount;
	}

	public Boolean getIn_out() {
		return in_out;
	}

	public void setIn_out(Boolean in_out) {
		this.in_out = in_out;
	}

//	@Temporary
//	private List<PurchaseInvoice> invoiceList = new ArrayList<PurchaseInvoice>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}

//	public String getSubject_name() {
//		return subject_name;
//	}
//
//	public void setSubject_name(String subject_name) {
//		this.subject_name = subject_name;
//	}

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

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public Integer getSalesman_id() {
		return salesman_id;
	}

	public void setSalesman_id(Integer salesman_id) {
		this.salesman_id = salesman_id;
	}

	public String getSalesman_name() {
		return salesman_name;
	}

	public void setSalesman_name(String salesman_name) {
		this.salesman_name = salesman_name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

//	public List<PurchaseInvoice> getInvoiceList() {
//		return invoiceList;
//	}
//
//	public void setInvoiceList(List<PurchaseInvoice> invoiceList) {
//		this.invoiceList = invoiceList;
//	}

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

	public Date getExpense_at() {
		return expense_at;
	}

	public void setExpense_at(Date expense_at) {
		this.expense_at = expense_at;
	}
	
	public String getIn_outString(){
		return this.in_out == true ?"收入":"支出";
	}
	public boolean isInvoiced(){
		return this.amount == this.invoice_amount;
	}
}
