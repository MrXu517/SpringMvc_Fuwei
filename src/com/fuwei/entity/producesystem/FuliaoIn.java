package com.fuwei.entity.producesystem;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_fuliaoin")
public class FuliaoIn {
	@IdentityId
	private int id;
	private String number;//单号
	private int fuliaoin_noticeId;
	private int orderId;
	private String orderNumber;
	private String name;// 样品名称
	private String company_productNumber;//样品的公司货号
	private int charge_employee;
	private int status;//一旦创建，status = 6
	private String state;//状态描述
	private Integer created_user;//创建人
	private Date created_at;
	/*是否打印、 是否打印辅料标签 属性*/
	private Boolean has_print;
	private Boolean has_tagprint;
	@Temporary
	private List<FuliaoInDetail> detaillist;
	
	
	
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
	public List<FuliaoInDetail> getDetaillist() {
		return detaillist;
	}
	public int getFuliaoin_noticeId() {
		return fuliaoin_noticeId;
	}
	public void setFuliaoin_noticeId(int fuliaoin_noticeId) {
		this.fuliaoin_noticeId = fuliaoin_noticeId;
	}
	public void setDetaillist(List<FuliaoInDetail> detaillist) {
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
		return DateTool.getYear2() + "FR" + NumberUtil.appendZero(this.id, 4);
		
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
