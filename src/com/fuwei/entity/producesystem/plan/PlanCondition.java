package com.fuwei.entity.producesystem.plan;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
//@Table("tb_plancondition")
public class PlanCondition {
//	@IdentityId
	private int id;
	private int orderId;//订单ID
	private int orderDetailId;//订单明细ID
	private int orderDeliveyBatch;//发货交期ID
	private Date delivery_date;//分批发货的交期，若不是分批发货，则就是全部发货的交期
	private int delivery_quantity;//发货数量
	
	private Date start_date;//该计划开始时间
	
	private int orderNumber;//订单号
	private String orderName;//款名
	private String company_productNumber;//公司款号
	/*订单明细属性*/
	private String color;//颜色
	private double weight;//克重
	private Integer yarn;//纱线种类
	private String size;//尺寸
	/*订单明细属性*/
//	private int gongxulineId;//流水线ID
	private Boolean locked;//该计划被锁定，有变化，要报警
	private String locked_reason;//被锁定的原因
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Integer getYarn() {
		return yarn;
	}
	public void setYarn(Integer yarn) {
		this.yarn = yarn;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
//	public int getGongxulineId() {
//		return gongxulineId;
//	}
//	public void setGongxulineId(int gongxulineId) {
//		this.gongxulineId = gongxulineId;
//	}
	public Boolean getLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	public String getLocked_reason() {
		return locked_reason;
	}
	public void setLocked_reason(String locked_reason) {
		this.locked_reason = locked_reason;
	}
	public int getOrderDeliveyBatch() {
		return orderDeliveyBatch;
	}
	public void setOrderDeliveyBatch(int orderDeliveyBatch) {
		this.orderDeliveyBatch = orderDeliveyBatch;
	}
	public Date getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}
	public int getDelivery_quantity() {
		return delivery_quantity;
	}
	public void setDelivery_quantity(int delivery_quantity) {
		this.delivery_quantity = delivery_quantity;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	
	
}
