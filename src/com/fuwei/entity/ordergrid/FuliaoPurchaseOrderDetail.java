package com.fuwei.entity.ordergrid;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

import com.fuwei.commons.SystemCache;

@Table("tb_fuliaopurchaseorder_detail")
public class FuliaoPurchaseOrderDetail {
	@IdentityId
	private int id;
	private int fuliaoPurchaseOrderId;
	private Integer style ; //辅料类型
	private String memo;//备注
	private int quantity;//数量
	private int location_size;//库位容量大小
	
	
	public int getLocation_size() {
		return location_size;
	}
	public void setLocation_size(int location_size) {
		this.location_size = location_size;
	}
	public int getFuliaoPurchaseOrderId() {
		return fuliaoPurchaseOrderId;
	}
	public void setFuliaoPurchaseOrderId(int fuliaoPurchaseOrderId) {
		this.fuliaoPurchaseOrderId = fuliaoPurchaseOrderId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getStyle() {
		return style;
	}
	public void setStyle(Integer style) {
		this.style = style;
	}
	
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

//	public String getStyleString(Integer factoryId){
//		if(factoryId == null){
//			return SystemCache.getFuliaoTypeName(this.getStyle());
//		}else{
//			Factory factory = SystemCache.getFactory(factoryId);
//			if(factory.getType() == 1){
//				return SystemCache.getMaterialName(this.getStyle());
//			}else{
//				return SystemCache.getFuliaoTypeName(this.getStyle());
//			}
//		}
//	}
	public String getStyleString(){
		return SystemCache.getFuliaoTypeName(this.getStyle());
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
	
}
