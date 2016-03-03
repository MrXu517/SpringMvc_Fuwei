package com.fuwei.entity.finishstore;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_finishstorestock_detail")
public class FinishStoreStockDetail {
	@IdentityId
	private int id;
	private int finishStoreStockId;//库存类ID
	private int packingOrderDetailId;//装箱单明细ID
	private int stock_quantity;//库存数量
	private int stock_cartons;//库存箱数
	private int plan_quantity;//计划数量
	private int plan_cartons;//计划箱数
	private int in_quantity;//入库数量
	private int in_cartons;//入库箱数
	private int out_quantity;//发库数量
	private int out_cartons;//发货箱数
	private int return_quantity;//退回数量
	private int return_cartons;//退回箱数
	
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
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
	public int getStock_cartons() {
		return stock_cartons;
	}
	public void setStock_cartons(int stock_cartons) {
		this.stock_cartons = stock_cartons;
	}
	public int getPlan_cartons() {
		return plan_cartons;
	}
	public void setPlan_cartons(int plan_cartons) {
		this.plan_cartons = plan_cartons;
	}
	public int getIn_cartons() {
		return in_cartons;
	}
	public void setIn_cartons(int in_cartons) {
		this.in_cartons = in_cartons;
	}
	public int getOut_cartons() {
		return out_cartons;
	}
	public void setOut_cartons(int out_cartons) {
		this.out_cartons = out_cartons;
	}
	public int getReturn_cartons() {
		return return_cartons;
	}
	public void setReturn_cartons(int return_cartons) {
		this.return_cartons = return_cartons;
	}
	public int getFinishStoreStockId() {
		return finishStoreStockId;
	}
	public void setFinishStoreStockId(int finishStoreStockId) {
		this.finishStoreStockId = finishStoreStockId;
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
	public int getStock_quantity() {
		return stock_quantity;
	}
	public void setStock_quantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}
	public int getPlan_quantity() {
		return plan_quantity;
	}
	public void setPlan_quantity(int plan_quantity) {
		this.plan_quantity = plan_quantity;
	}
	public int getIn_quantity() {
		return in_quantity;
	}
	public void setIn_quantity(int in_quantity) {
		this.in_quantity = in_quantity;
	}
	public int getOut_quantity() {
		return out_quantity;
	}
	public void setOut_quantity(int out_quantity) {
		this.out_quantity = out_quantity;
	}
	public int getReturn_quantity() {
		return return_quantity;
	}
	public void setReturn_quantity(int return_quantity) {
		this.return_quantity = return_quantity;
	}

	
	
	
}
