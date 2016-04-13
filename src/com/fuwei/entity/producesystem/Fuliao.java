package com.fuwei.entity.producesystem;

import java.text.ParseException;
import java.util.Date;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_fuliao")
public class Fuliao {
	@IdentityId
	private int id;
	private Integer orderId;//厂订单ID
	private String orderNumber;//厂订单号
	private String sample_name;//样品名称
	private String company_orderNumber;//公司订单号
	private String company_productNumber;//公司货号
	private String country;//国家/城市
	private int fuliaoTypeId;//辅料类型
	private String color;//颜色
	private String size;//尺寸
	private String batch;//批次
	private String img;//辅料图片
	private String img_s;//中等缩略图
	private String img_ss;//缩略图
	private int location_size;//库位容量大小
	private int plan_quantity;
	private String memo;
	private Integer created_user;//创建人
	private Date created_at;
	private Date updated_at;
	
	private String fnumber;//辅料number
	private Integer charge_employee;// 打样人 ，跟单人
	
	//2016-4-11 若是通用辅料，则添加下面3个属性
	private Integer companyId;//外贸公司
	private Integer salesmanId;//业务员
	private Integer customerId;//客户
	private int type;//1表示大货辅料， 2表示通用辅料
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getSalesmanId() {
		return salesmanId;
	}
	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getCharge_employee() {
		return charge_employee;
	}
	public void setCharge_employee(Integer charge_employee) {
		this.charge_employee = charge_employee;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public int getPlan_quantity() {
		return plan_quantity;
	}
	public void setPlan_quantity(int plan_quantity) {
		this.plan_quantity = plan_quantity;
	}
	public String getSample_name() {
		return sample_name;
	}
	public void setSample_name(String sample_name) {
		this.sample_name = sample_name;
	}
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
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCompany_orderNumber() {
		return company_orderNumber;
	}
	public void setCompany_orderNumber(String company_orderNumber) {
		this.company_orderNumber = company_orderNumber;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getFuliaoTypeId() {
		return fuliaoTypeId;
	}
	public void setFuliaoTypeId(int fuliaoTypeId) {
		this.fuliaoTypeId = fuliaoTypeId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public int getLocation_size() {
		return location_size;
	}
	public void setLocation_size(int location_size) {
		this.location_size = location_size;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getImg_s() {
		return img_s;
	}
	public void setImg_s(String img_s) {
		this.img_s = img_s;
	}
	public String getImg_ss() {
		return img_ss;
	}
	public void setImg_ss(String img_ss) {
		this.img_ss = img_ss;
	}
	
	public String getLocationSizeString() {
		if(this.location_size == 3){
			return "大";
		}else if(this.location_size == 2){
			return "中";
		}else if(this.location_size == 1){
			return "小";
		}else{
			return "其他";
		}
	}
	public String createNumber() throws ParseException{	
		return DateTool.getYear2() + "FL" + NumberUtil.appendZero(this.id%9999, 4);
		
	}
	public String typeString(){
		if(this.type==1){
			return "大货辅料";
		}else if(this.type==2){
			return "通用辅料";
		}else{
			return "未知";
		}
	}
	
}
