package com.fuwei.entity.ordergrid;

//@Table("tb_storeorder_detail")
public class StoreOrderDetail extends BaseTableOrder{
//	@IdentityId
//	private int id;
//	private int storeOrderId;
	private String color;//色号
	private Integer material;//材料
	private double quantity;//总数量
	private String yarn;//标准样纱
	private Integer factoryId;//领取人 ，必填 （工厂）
//	private Date created_at;// 创建时间
//	private Date updated_at;// 最近更新时间
//	
//	private Integer created_user;//创建用户
	
	
	
//	public int getStoreOrderId() {
//		return storeOrderId;
//	}
//	public void setStoreOrderId(int storeOrderId) {
//		this.storeOrderId = storeOrderId;
//	}
//	public Date getCreated_at() {
//		return created_at;
//	}
//	public void setCreated_at(Date created_at) {
//		this.created_at = created_at;
//	}
//	public Date getUpdated_at() {
//		return updated_at;
//	}
//	public void setUpdated_at(Date updated_at) {
//		this.updated_at = updated_at;
//	}
//	public Integer getCreated_user() {
//		return created_user;
//	}
//	public void setCreated_user(Integer created_user) {
//		this.created_user = created_user;
//	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
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
	public String getYarn() {
		return yarn;
	}
	public void setYarn(String yarn) {
		this.yarn = yarn;
	}
	
	
}
