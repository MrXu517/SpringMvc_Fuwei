package com.fuwei.entity.ordergrid;

import java.util.Date;
import java.util.List;

import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//抽检记录单
@Table("tb_checkrecordorder")
public class CheckRecordOrder  extends BaseTableOrder{
	@IdentityId
	private int id;
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
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
		if(this.status == null){
			return true;
		}
		return this.status != 6;
	}
//	private String detail_json;
	
//	@Temporary
//	private List<CheckRecordOrderDetail> detaillist ;
	
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

//	public String getDetail_json() {
//		return detail_json;
//	}
//
//	public void setDetail_json(String detail_json) throws Exception {
//		if(detail_json != null && !detail_json.equals("")){
//			this.setDetaillist(SerializeTool.deserializeList(detail_json,CheckRecordOrderDetail.class));
//		}
//		
//		this.detail_json = detail_json;
//	}

//	public List<CheckRecordOrderDetail> getDetaillist() {
//		return detaillist;
//	}
//
//	public void setDetaillist(List<CheckRecordOrderDetail> detaillist) {
//		this.detaillist = detaillist;
//	}
	
	
	
	
}
