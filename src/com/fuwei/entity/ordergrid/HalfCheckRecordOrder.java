package com.fuwei.entity.ordergrid;

import java.util.Date;
import java.util.List;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

import com.fuwei.util.SerializeTool;

//半检记录单
@Table("tb_halfcheckrecordorder")
public class HalfCheckRecordOrder extends BaseTableOrder{
	@IdentityId
	private int id;
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
//	private String detail_json;
	
	private String detail_2_json;
	
//	@Temporary
//	private List<HalfCheckRecordOrderDetail> detaillist ;
	
	@Temporary
	private List<PlanOrderDetail> detaillist ;//2015-3-31添加
	
	@Temporary
	private List<HalfCheckRecordOrderDetail2> detail_2_list ;

	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	
	
	public List<PlanOrderDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<PlanOrderDetail> detaillist) {
		this.detaillist = detaillist;
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
		if(this.status == null){
			return true;
		}
		return this.status != 6;
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
//			this.setDetaillist(SerializeTool.deserializeList(detail_json,HalfCheckRecordOrderDetail.class));
//		}
//		
//		this.detail_json = detail_json;
//	}

	public String getDetail_2_json() {
		return detail_2_json;
	}

	public void setDetail_2_json(String detail_2_json) throws Exception {
		if(detail_2_json != null && !detail_2_json.equals("") ){
			this.setDetail_2_list(SerializeTool.deserializeList(detail_2_json,HalfCheckRecordOrderDetail2.class));
		}
		this.detail_2_json = detail_2_json;
	}

//	public List<HalfCheckRecordOrderDetail> getDetaillist() {
//		return detaillist;
//	}
//
//	public void setDetaillist(List<HalfCheckRecordOrderDetail> detaillist) {
//		this.detaillist = detaillist;
//	}

	public List<HalfCheckRecordOrderDetail2> getDetail_2_list() {
		return detail_2_list;
	}

	public void setDetail_2_list(
			List<HalfCheckRecordOrderDetail2> detail_2_list) {
		this.detail_2_list = detail_2_list;
	}
}
