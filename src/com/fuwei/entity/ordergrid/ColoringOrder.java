package com.fuwei.entity.ordergrid;

import java.util.Date;
import java.util.List;

import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//原材料采购单
@Table("tb_coloringorder")
public class ColoringOrder {
	@IdentityId
	private int id;
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
	private String detail_json;
	
	@Temporary
	private List<ColoringOrderDetail> detaillist ;
	
	private int factoryId;//染色单位

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

	public String getDetail_json() {
		return detail_json;
	}

	public void setDetail_json(String detail_json) throws Exception {
		if(detail_json != null && !detail_json.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(detail_json,ColoringOrderDetail.class));
		}
		
		this.detail_json = detail_json;
	}

	

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public List<ColoringOrderDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<ColoringOrderDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	
}
