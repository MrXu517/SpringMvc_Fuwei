package com.fuwei.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.constant.OrderStatus;
import com.fuwei.util.DateTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_order")
public class Order implements Serializable{
	@IdentityId
	private int id;//主键
	private String orderNumber;//订单号
	private int status;//订单状态 -1刚创建
	private String state;//订单状态描述
	private double amount;//订单总金额
	private String memo;//订单备注
	private String info;//订单信息（样品名称(样品克重)）
	private Date start_at;//订单生效开始时间
	private Date end_at;//订单截止时间
	private Date delivery_at;//发货时间
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private int created_user;//创建用户
	private Integer salesmanId;//业务员ID
	private Integer companyId;//公司ID
	
	//动态的生产步骤
	private Integer stepId;
	
	private String step_state;
	@Temporary
	private List<OrderStep> stepList ;
	//动态的生产步骤
	
	@Temporary
	private List<OrderDetail> detaillist ;
	
	public List<OrderStep> getStepList() {
		return stepList;
	}
	public void setStepList(List<OrderStep> stepList) {
		this.stepList = stepList;
	}
	public Integer getStepId() {
		return stepId;
	}
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}
	

	
	public String getStep_state() {
		return step_state;
	}
	public void setStep_state(String step_state) {
		this.step_state = step_state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Date getStart_at() {
		return start_at;
	}
	public void setStart_at(Date start_at) {
		this.start_at = start_at;
	}
	public Date getEnd_at() {
		return end_at;
	}
	public void setEnd_at(Date end_at) {
		this.end_at = end_at;
	}
	public Date getDelivery_at() {
		return delivery_at;
	}
	public void setDelivery_at(Date delivery_at) {
		this.delivery_at = delivery_at;
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
	public int getCreated_user() {
		return created_user;
	}
	public void setCreated_user(int created_user) {
		this.created_user = created_user;
	}
	public Integer getSalesmanId() {
		return salesmanId;
	}
	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public List<OrderDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<OrderDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	//是否可编辑
	public Boolean isEdit(){
		return this.status < OrderStatus.COLORING.ordinal();
	}
	//获取当前状态描述
	public String getCNState(){
		if(this.stepId == null){
			return this.state;
		}else{
			return this.step_state;
		}
	}
	//获取发货时间
	public String getDevelivery(){
		if(this.delivery_at == null){
			return "未发货";
		}else{
			try {
				return DateTool.formateDate(this.delivery_at);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}
	
}
