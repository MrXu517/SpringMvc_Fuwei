package com.fuwei.entity.ordergrid;

public class MaterialPurchaseOrderDetail {
	private Integer material ; //材料品种
	private double quantity;//数量
	private String memo;//备注
	public Integer getMaterial() {
		return material;
	}
	public void setMaterial(Integer material) {
		this.material = material;
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

}
