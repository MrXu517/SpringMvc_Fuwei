package com.fuwei.entity.financial;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_producing_order_balance")
public class ProducingOrderBalance implements Serializable {
	/**
	 * 
	 */
	@IdentityId
	private int id;// ID
	private String number;//对账单号码
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
	private String balance_employee;//本厂对账人
	private Date balance_at; //对账时间
	private Integer factoryId;//对账的生产单位
	private String balance_factory_employee;//机织单位对账人
	private int quantity;//总数量
	private double amount;//总金额，已减去扣款金额
	private String memo ; //备注
	private double rate_deduct ; //地税扣款
	private double payable_amount;//应付款
	
	
	@Temporary
	private List<ProducingOrderBalanceDetail> detaillist;



	public double getRate_deduct() {
		return rate_deduct;
	}



	public void setRate_deduct(double rate_deduct) {
		this.rate_deduct = rate_deduct;
	}


	public String getMemo() {
		return memo;
	}



	public void setMemo(String memo) {
		this.memo = memo;
	}



	public double getPayable_amount() {
		return payable_amount;
	}



	public void setPayable_amount(double payable_amount) {
		this.payable_amount = payable_amount;
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



	public String getBalance_employee() {
		return balance_employee;
	}



	public void setBalance_employee(String balance_employee) {
		this.balance_employee = balance_employee;
	}



	public Date getBalance_at() {
		return balance_at;
	}



	public void setBalance_at(Date balance_at) {
		this.balance_at = balance_at;
	}



	public Integer getFactoryId() {
		return factoryId;
	}



	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}



	public String getBalance_factory_employee() {
		return balance_factory_employee;
	}



	public void setBalance_factory_employee(String balance_factory_employee) {
		this.balance_factory_employee = balance_factory_employee;
	}



	public double getAmount() {
		return amount;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public List<ProducingOrderBalanceDetail> getDetaillist() {
		return detaillist;
	}



	public void setDetaillist(List<ProducingOrderBalanceDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	
	public String createNumber() throws ParseException{
		return DateTool.getYear2() + "DZ" + NumberUtil.appendZero(this.id, 4);
	}
	
}