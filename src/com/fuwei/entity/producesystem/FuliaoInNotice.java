package com.fuwei.entity.producesystem;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_fuliaoin_notice")
public class FuliaoInNotice {
	@IdentityId
	private int id;
	private String number;//通知单号
	private int orderId;
	private String orderNumber;
	private int charge_employee;
	//当通知实际已出入库或已取消时，通知单不能修改，不能删除。  
	//status != 6 && status !=-1 时 ， 可以修改，可以删除
	private int status;//0创建，6表示已确认入库或出库,-1表示入库失败
	private String state;//状态描述
	private Integer created_user;//创建人
	private Date updated_at;//最近更新时间
	private Date created_at;
	private String fail_memo;
	@Temporary
	private List<FuliaoInNoticeDetail> detaillist;
	
	private String name;// 样品名称
	private String company_productNumber;//样品的公司货号
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}
	public String getFail_memo() {
		return fail_memo;
	}
	public void setFail_memo(String fail_memo) {
		this.fail_memo = fail_memo;
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
	public List<FuliaoInNoticeDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<FuliaoInNoticeDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public boolean isEditable(){
		if(this.status!=6 && this.status !=-1){
			return true;
		}else{
			return false;
		}
	}
	public boolean isDeletable(){
		if(this.status!=6 && this.status !=-1){
			return true;
		}else{
			return false;
		}
	}
	public String createNumber() throws ParseException{	
		return DateTool.getYear2() + "FRT" + NumberUtil.appendZero(this.id, 4);
		
	}
	
	public String getStateString(){
		if(this.status == 6){
			return "已入库";
		}else if(this.status == -1){
			return "入库失败";
		}else{
			return "等待入库";
		}
	}
	
}
