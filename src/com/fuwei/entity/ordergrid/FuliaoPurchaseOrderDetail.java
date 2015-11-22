package com.fuwei.entity.ordergrid;

import com.fuwei.commons.SystemCache;
import com.fuwei.entity.Factory;

public class FuliaoPurchaseOrderDetail {
	private Integer style ; //辅料类型
	private String memo;//备注
	private double quantity;//数量
	public Integer getStyle() {
		return style;
	}
	public void setStyle(Integer style) {
		this.style = style;
	}
	
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
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
	
}
