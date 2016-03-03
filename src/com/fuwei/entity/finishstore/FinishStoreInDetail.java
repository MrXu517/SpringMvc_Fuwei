package com.fuwei.entity.finishstore;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_finishstore_in_detail")
public class FinishStoreInDetail {
	@IdentityId
	private int id;
	private int finishStoreInOutId;
	private int packingOrderDetailId;//装箱单明细ID
	private int quantity;//入库数量
	private int cartons;//入库箱数
	
	//以下是装箱单明细的属性
	@Temporary
	private String color;//颜色
	@Temporary
	private int per_carton_quantity;//每箱数量
	@Temporary
	private int per_pack_quantity;//每包几件
	@Temporary
	private String col1_value;//动态属性1的值
	@Temporary
	private String col2_value;//动态属性2的值
	@Temporary
	private String col3_value;//动态属性3的值
	@Temporary
	private String col4_value;//动态属性4的值
	
	public int getFinishStoreInOutId() {
		return finishStoreInOutId;
	}
	public void setFinishStoreInOutId(int finishStoreInOutId) {
		this.finishStoreInOutId = finishStoreInOutId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPackingOrderDetailId() {
		return packingOrderDetailId;
	}
	public void setPackingOrderDetailId(int packingOrderDetailId) {
		this.packingOrderDetailId = packingOrderDetailId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getCartons() {
		return cartons;
	}
	public void setCartons(int cartons) {
		this.cartons = cartons;
	}
	public int getPer_carton_quantity() {
		return per_carton_quantity;
	}
	public void setPer_carton_quantity(int per_carton_quantity) {
		this.per_carton_quantity = per_carton_quantity;
	}
	public int getPer_pack_quantity() {
		return per_pack_quantity;
	}
	public void setPer_pack_quantity(int per_pack_quantity) {
		this.per_pack_quantity = per_pack_quantity;
	}
	public String getCol1_value() {
		return col1_value;
	}
	public void setCol1_value(String col1_value) {
		this.col1_value = col1_value;
	}
	public String getCol2_value() {
		return col2_value;
	}
	public void setCol2_value(String col2_value) {
		this.col2_value = col2_value;
	}
	public String getCol3_value() {
		return col3_value;
	}
	public void setCol3_value(String col3_value) {
		this.col3_value = col3_value;
	}
	public String getCol4_value() {
		return col4_value;
	}
	public void setCol4_value(String col4_value) {
		this.col4_value = col4_value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
}
