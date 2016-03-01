package com.fuwei.entity.finishstore;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_packingorder_detail")
public class PackingOrderDetail {
	@IdentityId
	private int id;
	private int packingOrderId;
	private int orderId;
	private String color;//颜色
	private int quantity;//数量
	private int per_carton_quantity;//每箱数量
	private double box_L;//外箱尺寸L
	private double box_W;//外箱尺寸W
	private double box_H;//外箱尺寸H
	private double gross_weight;//毛重
	private double net_weight;//净重
	private int cartons;//箱数
	private int box_number_start;//箱号从。。开始
	private int box_number_end;//箱号到。。结束
	private int per_pack_quantity;//每包几件
	private double capacity;//立方
	
	private String col1_value;//动态属性1的值
	
	private String col2_value;//动态属性2的值
	
	private String col3_value;//动态属性3的值
	
	private String col4_value;//动态属性4的值
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPackingOrderId() {
		return packingOrderId;
	}
	public void setPackingOrderId(int packingOrderId) {
		this.packingOrderId = packingOrderId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPer_carton_quantity() {
		return per_carton_quantity;
	}
	public void setPer_carton_quantity(int per_carton_quantity) {
		this.per_carton_quantity = per_carton_quantity;
	}
	public double getBox_L() {
		return box_L;
	}
	public void setBox_L(double box_L) {
		this.box_L = box_L;
	}
	public double getBox_W() {
		return box_W;
	}
	public void setBox_W(double box_W) {
		this.box_W = box_W;
	}
	public double getBox_H() {
		return box_H;
	}
	public void setBox_H(double box_H) {
		this.box_H = box_H;
	}
	public double getGross_weight() {
		return gross_weight;
	}
	public void setGross_weight(double gross_weight) {
		this.gross_weight = gross_weight;
	}
	public double getNet_weight() {
		return net_weight;
	}
	public void setNet_weight(double net_weight) {
		this.net_weight = net_weight;
	}
	public int getCartons() {
		return cartons;
	}
	public void setCartons(int cartons) {
		this.cartons = cartons;
	}
	public int getBox_number_start() {
		return box_number_start;
	}
	public void setBox_number_start(int box_number_start) {
		this.box_number_start = box_number_start;
	}
	public int getBox_number_end() {
		return box_number_end;
	}
	public void setBox_number_end(int box_number_end) {
		this.box_number_end = box_number_end;
	}
	public int getPer_pack_quantity() {
		return per_pack_quantity;
	}
	public void setPer_pack_quantity(int per_pack_quantity) {
		this.per_pack_quantity = per_pack_quantity;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
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
	
	
}
