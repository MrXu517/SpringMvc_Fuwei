package com.fuwei.entity.producesystem;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
@Table("tb_fuliao_changelocation")
public class FuliaoChangeLocation {
	@IdentityId
	private int id;
	private int fuliaoId;
	private int locationId;//新的库位
	private String memo;
	private int created_user;//操作用户
	private Date created_at;//操作时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFuliaoId() {
		return fuliaoId;
	}
	public void setFuliaoId(int fuliaoId) {
		this.fuliaoId = fuliaoId;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getCreated_user() {
		return created_user;
	}
	public void setCreated_user(int created_user) {
		this.created_user = created_user;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	
}
