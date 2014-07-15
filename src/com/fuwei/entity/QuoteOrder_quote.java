package com.fuwei.entity;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;


//该类是报价单的一条记录，如淘宝中订单与商品的关系
@Table("tb_quoteorder_quote")
public class QuoteOrder_quote {
	@IdentityId
	private Integer quoteId;//报价ID
	private Integer quoteOrderId;//报价单ID
	private double price;//报价
	private String memo;//价格的备注
	private int sampleId;//样品ID
	
	//接下来的Sample的属性
	private String name;//样品名称
	
	private String img;//图片
	private String material;//材料
	private double weight;//克重
	private String size;//尺寸

	private double cost;//成本
	private String productNumber;//产品编号
	private String machine;//机织
	private Integer charge_user;//打样人
	private String detail;//报价详情
	
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public Integer getQuoteOrderId() {
		return quoteOrderId;
	}
	public void setQuoteOrderId(Integer quoteOrderId) {
		this.quoteOrderId = quoteOrderId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getSampleId() {
		return sampleId;
	}
	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public Integer getCharge_user() {
		return charge_user;
	}
	public void setCharge_user(Integer charge_user) {
		this.charge_user = charge_user;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
	
}
