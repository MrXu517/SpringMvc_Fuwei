package com.fuwei.entity;

public class ProductionNotificationDetail {
	//内容json字符串  json数组， json对象内容：色号，色别，尺寸，生产数量，材料名称，材料数量，损耗，总材料
	private String colorCode;
	private String colorStyle;
	private String size;
	private int quantity;
	private String material;
	private String material_quantity;
	private String waste;
	private String total_material;
	private String memo;
	
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getColorStyle() {
		return colorStyle;
	}
	public void setColorStyle(String colorStyle) {
		this.colorStyle = colorStyle;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getMaterial_quantity() {
		return material_quantity;
	}
	public void setMaterial_quantity(String material_quantity) {
		this.material_quantity = material_quantity;
	}
	public String getWaste() {
		return waste;
	}
	public void setWaste(String waste) {
		this.waste = waste;
	}
	public String getTotal_material() {
		return total_material;
	}
	public void setTotal_material(String total_material) {
		this.total_material = total_material;
	}
	
	

}
