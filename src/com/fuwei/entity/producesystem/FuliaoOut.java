package com.fuwei.entity.producesystem;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_fuliaoout")
public class FuliaoOut {
	@IdentityId
	private int id;
	private String number;//单号
	private int fuliaoout_noticeId;
	private int orderId;
	private String orderNumber;
	private int charge_employee;
	private int status;//一旦创建，status = 6
	private String state;//状态描述
	private Integer created_user;//创建人
	private Date created_at;
	private Integer receiver_employee;
	@Temporary
	private List<FuliaoOutDetail> detaillist;
	
	
	
	public int getFuliaoout_noticeId() {
		return fuliaoout_noticeId;
	}
	public void setFuliaoout_noticeId(int fuliaoout_noticeId) {
		this.fuliaoout_noticeId = fuliaoout_noticeId;
	}
	
	public Integer getReceiver_employee() {
		return receiver_employee;
	}
	public void setReceiver_employee(Integer receiver_employee) {
		this.receiver_employee = receiver_employee;
	}
	public List<FuliaoOutDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<FuliaoOutDetail> detaillist) {
		this.detaillist = detaillist;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getCharge_employee() {
		return charge_employee;
	}
	public void setCharge_employee(int charge_employee) {
		this.charge_employee = charge_employee;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getCreated_user() {
		return created_user;
	}
	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	public boolean isEditable(){
		if(this.status!=6){
			return true;
		}else{
			return false;
		}
	}
	public boolean isDeletable(){
		if(this.status!=6){
			return true;
		}else{
			return false;
		}
	}
	public String createNumber() throws ParseException{	
		return DateTool.getYear2() + "FC" + NumberUtil.appendZero(this.id, 4);
		
	}
	
}
