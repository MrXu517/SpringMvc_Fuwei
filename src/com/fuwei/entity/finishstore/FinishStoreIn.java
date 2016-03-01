package com.fuwei.entity.finishstore;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//成品出入库
@Table("tb_finishstore_in")
public class FinishStoreIn {
	@IdentityId
	private Integer id;
	private String number;//成品出入库单编号
	private Integer orderId;
	private String orderNumber;
	private Integer packingOrderId;//装箱单ID

	
	private Date date;//入库、出库日期
	private String sign;//领取人签字
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建人
	private String memo;//备注
	@Temporary
	private List<FinishStoreInDetail> detaillist ;
	
	/*2015-7-8添加 是否打印、 是否打印属性*/
	private Boolean has_print;
	
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getPackingOrderId() {
		return packingOrderId;
	}

	public void setPackingOrderId(Integer packingOrderId) {
		this.packingOrderId = packingOrderId;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<FinishStoreInDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<FinishStoreInDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public Boolean getHas_print() {
		return has_print;
	}

	public void setHas_print(Boolean has_print) {
		this.has_print = has_print;
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

	// 是否可编辑
	public Boolean isEdit() {
		if(this.status == null){
			return true;
		}
		return this.status != 6 && this.status != 7;
	}
	
	public Boolean deletable(){
		if(this.has_print){
			return false;
		}
		if(this.status == null){
			return true;
		}
		return this.status != 6;
	}
	
	public String createNumber() throws ParseException{
		return DateTool.getYear2() + "FRK" + NumberUtil.appendZero(this.id, 4);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getType() throws ParseException{
		return "入库";
	}
}
