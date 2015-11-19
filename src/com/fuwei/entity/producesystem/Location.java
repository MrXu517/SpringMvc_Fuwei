package com.fuwei.entity.producesystem;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_location")
public class Location {
	@IdentityId
	private int id;
	private String number;
	private String location;
	private int size;//size = 3大、size = 2中、 size=1小
	private Boolean isempty;
	private Integer fuliaoId;
	private int quantity ;
	
	
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Integer getFuliaoId() {
		return fuliaoId;
	}
	public void setFuliaoId(Integer fuliaoId) {
		this.fuliaoId = fuliaoId;
	}
	public Boolean getIsempty() {
		return isempty;
	}
	public void setIsempty(Boolean isempty) {
		this.isempty = isempty;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getSizeString() {
		if(this.size == 3){
			return "大";
		}else if(this.size == 2){
			return "中";
		}else if(this.size == 1){
			return "小";
		}else{
			return "其他";
		}
	}
}
