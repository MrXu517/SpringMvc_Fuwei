package com.fuwei.entity.ordergrid;

import java.util.Date;

import com.fuwei.entity.Order;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;
@Table("tb_packingorder")
public class PackingOrder {
	@IdentityId
	private int id;
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	private String filepath;
	
	private String pdfpath;
	
	private String memo;
	
	@Temporary
	private Order order = new Order();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPdfpath() {
		return pdfpath;
	}

	public void setPdfpath(String pdfpath) {
		this.pdfpath = pdfpath;
	}
	
	// 是否可编辑
	public Boolean isEdit() {
		if(this.status == null){
			return true;
		}
		return this.status != 6 && this.status != 7;
	}
	
	public Boolean deletable(){
		if(this.status == null){
			return true;
		}
		return this.status != 6;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setCompanyId(Integer companyId) {
		order.setCompanyId(companyId);
	}
	public void setSalesmanId(Integer salesmanId) {
		order.setSalesmanId(salesmanId);
	}

	public void setCustomerId(Integer customerId) {
		order.setCustomerId(customerId);
	}

	public void setSampleId(Integer sampleId) {
		order.setSampleId(sampleId);
	}
	public void setName(String name) {
		order.setName(name);
	}
	public void setImg(String img) {
		order.setImg(img);
	}
	public void setMaterialId(Integer materialId) {
		order.setMaterialId(materialId);
	}
	public void setWeight(double weight) {
		order.setWeight(weight);
	}
	public void setSize(String size) {
		order.setSize(size);
	}
	public void setProductNumber(String productNumber) {
		order.setProductNumber(productNumber);
	}
	public void setOrderNumber(String orderNumber) {
		order.setOrderNumber(orderNumber);
	}
	public void setImg_s(String img_s) {
		order.setImg_s(img_s);
	}
	public void setImg_ss(String img_ss) {
		order.setImg_ss(img_ss);
	}
	public void setCharge_employee(Integer charge_employee) {
		order.setCharge_employee(charge_employee);
	}
	public void setCompany_productNumber(String company_productNumber) {
		order.setCompany_productNumber(company_productNumber);
	}
	
	
	
	
}
