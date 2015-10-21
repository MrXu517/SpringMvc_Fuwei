package com.fuwei.entity.producesystem;

import java.util.Date;
import java.util.List;

import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.Temporary;

//半成品出入库、退货记录 , 只暂时用于半成品出入库、退货记录报表
public class HalfInOut {
	private Integer id;
	private String number;//半成品出入库单编号
	private int gongxuId;//工序Id
	private Integer orderId;
	
	private Boolean in_out ;//入库/出库  ， true:入库 , false：出库

	
	private Date date;//入库、出库日期
	private Integer factoryId;//领取工厂、入库工厂
	private String sign;//领取人签字
	private String memo;//备注
	private String detail_json;
	private Integer created_user;//创建人
	private Date created_at;
	@Temporary
	private List<HalfInOutDetail> detaillist ;
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
	public int getGongxuId() {
		return gongxuId;
	}
	public void setGongxuId(int gongxuId) {
		this.gongxuId = gongxuId;
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
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
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
	public String getDetail_json() {
		return detail_json;
	}
	public void setDetail_json(String detail_json) throws Exception {
		if(detail_json != null && !detail_json.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(detail_json,HalfInOutDetail.class));
		}
		
		this.detail_json = detail_json;
	}
	public List<HalfInOutDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<HalfInOutDetail> detaillist) {
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
