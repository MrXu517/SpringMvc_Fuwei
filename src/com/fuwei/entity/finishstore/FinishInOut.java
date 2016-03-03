package com.fuwei.entity.finishstore;


import java.util.Date;
import java.util.List;

import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.Temporary;

//成品出入库、退货记录 , 只暂时用于成品出入库、退货记录报表
public class FinishInOut {
	private Integer id;
	private String number;//成品出入库单编号
	private Integer orderId;
	private String orderNumber;
	private int packingOrderId;//装箱单ID
	private Boolean in_out ;//入库/出库  ， true:入库 , false：出库
	
	private String name;//样品名称
	private int charge_employee;
	private String company_productNumber;//样品的公司货号
	private Integer companyId;// 公司ID
	private Integer customerId;// 客户ID
	private String img;// 图片
	private String img_s;// 中等缩略图
	private String img_ss;// 缩略图
	private Integer col1_id;//装箱单中动态列1的列属性ID ， 根据ID可以在缓存中获取name
	private Integer col2_id;//装箱单中动态列2的列属性ID ， 根据ID可以在缓存中获取name
	private Integer col3_id;//装箱单中动态列3的列属性ID ， 根据ID可以在缓存中获取name
	private Integer col4_id;//装箱单中动态列4的列属性ID ， 根据ID可以在缓存中获取name
	
	
	private Date date;//入库、出库日期
	private String sign;//领取人签字
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建人
	private String memo;//备注
	private List<FinishInOutDetail> detaillist ;
	private Boolean has_print;
	private String type;
	
	
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Integer getCreated_user() {
		return created_user;
	}
	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Boolean getIn_out() {
		return in_out;
	}
	public void setIn_out(Boolean in_out) {
		this.in_out = in_out;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getPackingOrderId() {
		return packingOrderId;
	}
	public void setPackingOrderId(int packingOrderId) {
		this.packingOrderId = packingOrderId;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public List<FinishInOutDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<FinishInOutDetail> detaillist) {
		this.detaillist = detaillist;
	}
	public Boolean getHas_print() {
		return has_print;
	}
	public void setHas_print(Boolean has_print) {
		this.has_print = has_print;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCharge_employee() {
		return charge_employee;
	}
	public void setCharge_employee(int charge_employee) {
		this.charge_employee = charge_employee;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
	public Integer getCol1_id() {
		return col1_id;
	}
	public void setCol1_id(Integer col1_id) {
		this.col1_id = col1_id;
	}
	public Integer getCol2_id() {
		return col2_id;
	}
	public void setCol2_id(Integer col2_id) {
		this.col2_id = col2_id;
	}
	public Integer getCol3_id() {
		return col3_id;
	}
	public void setCol3_id(Integer col3_id) {
		this.col3_id = col3_id;
	}
	public Integer getCol4_id() {
		return col4_id;
	}
	public void setCol4_id(Integer col4_id) {
		this.col4_id = col4_id;
	}
	public String getTypeString(){
		if(this.type.equals("in")){
			return "入库";
		}
		else if(this.type.equals("out")){
			return "出货";
		}
		else if(this.type.equals("return")){
			return "退货";
		}else{
			return "未知";
		}
	}
	
	public Integer getInt(){
		if(this.type.equals("in")){
			return 1;
		}
		else if(this.type.equals("out")){
			return 0;
		}
		else if(this.type.equals("return")){
			return -1;
		}else{
			return null;
		}
	}
}
