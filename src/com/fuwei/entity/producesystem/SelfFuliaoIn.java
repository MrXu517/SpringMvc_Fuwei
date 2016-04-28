package com.fuwei.entity.producesystem;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_selffuliaoin")
public class SelfFuliaoIn {
	@IdentityId
	private int id;
	private String number ; //单据number属性
	private Integer orderId;//订单ID
	private int fuliaoPurchaseOrderId;//辅料采购单ID
	private String fuliaoPurchaseOrder_number;//辅料采购单ID
	private String orderNumber;//订单编号
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间	
	private Integer created_user;//创建用户
	private int factoryId;//辅料采购单位
	private Integer companyId;// 公司ID
	private Integer customerId;
	private String name;// 样品名称
	private Integer charge_employee;// 打样人 ，跟单人 2015-5-2修改
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描
	private String company_productNumber;//样品的公司货号
	/*是否打印、 是否打印辅料标签 属性*/
	private Boolean has_print;
	private Boolean has_tagprint;
	private Date date;//入库日期
	@Temporary
	private List<SelfFuliaoInDetail> detaillist ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFuliaoPurchaseOrder_number() {
		return fuliaoPurchaseOrder_number;
	}
	public void setFuliaoPurchaseOrder_number(String fuliaoPurchaseOrder_number) {
		this.fuliaoPurchaseOrder_number = fuliaoPurchaseOrder_number;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Boolean getHas_print() {
		return has_print;
	}
	public void setHas_print(Boolean has_print) {
		this.has_print = has_print;
	}
	public Boolean getHas_tagprint() {
		return has_tagprint;
	}
	public void setHas_tagprint(Boolean has_tagprint) {
		this.has_tagprint = has_tagprint;
	}
	public int getFuliaoPurchaseOrderId() {
		return fuliaoPurchaseOrderId;
	}
	public void setFuliaoPurchaseOrderId(int fuliaoPurchaseOrderId) {
		this.fuliaoPurchaseOrderId = fuliaoPurchaseOrderId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCharge_employee() {
		return charge_employee;
	}
	public void setCharge_employee(Integer charge_employee) {
		this.charge_employee = charge_employee;
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
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}
	public List<SelfFuliaoInDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<SelfFuliaoInDetail> detaillist) {
		this.detaillist = detaillist;
	}
	public String createNumber() throws ParseException{	
		return DateTool.getYear2() + "ZR" + NumberUtil.appendZero(this.id%9999, 4);
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

	public String printStr(){
		if(this.has_print!=null && this.has_print){
			return "是";
		}
		else{
			return "否";
		}
	}
}
