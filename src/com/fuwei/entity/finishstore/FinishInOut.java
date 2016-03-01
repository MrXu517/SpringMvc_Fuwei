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
	
	public String getTypeString(){
		if(this.type.equals("store")){
			if(this.in_out){
				return "入库";
			}else{
				return "出库";
			}
		}
		else if(this.type.equals("return")){
			return "退货";
		}else{
			return "未知";
		}
	}
	
	public Integer getInt(){
		if(this.type.equals("store")){
			if(this.in_out){
				return 1;
			}else{
				return 0;
			}
		}
		else if(this.type.equals("return")){
			return -1;
		}else{
			return null;
		}
	}
}
