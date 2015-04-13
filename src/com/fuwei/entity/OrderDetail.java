package com.fuwei.entity;

//import net.keepsoft.commons.annotation.IdentityId;
//import net.keepsoft.commons.annotation.Table;
//import net.keepsoft.commons.annotation.Temporary;


//订单明细
//@Table("tb_order_detail")
public class OrderDetail {
	private int id;
	private String color;//颜色
	private double weight;//克重
	private Integer yarn;//纱线种类
	private String size;//尺寸
	private int quantity;//生产数量
	private double price;//单价
	
	private double produce_weight;//机织克重
	
	
	
	
	public double getProduce_weight() {
		return produce_weight;
	}
	public void setProduce_weight(double produce_weight) {
		this.produce_weight = produce_weight;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Integer getYarn() {
		return yarn;
	}
	public void setYarn(Integer yarn) {
		this.yarn = yarn;
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
	
	
//	@IdentityId
//	private Integer id;//主键
//	private Integer orderId;//订单ID
//	private double price;//报价单价
//	private int quantity;//数量
//	private double amount;//总价
//	private String memo;//价格的备注
//	private int sampleId;//样品ID
//	private String cproductN ; //公司款号
//	
//	//接下来的Sample的属性
//	private String name;//样品名称
//	
//	private String img;//图片
//	private String material;//材料
//	private double weight;//克重
//	private String size;//尺寸
//
//	private double cost;//成本
//	private String productNumber;//产品编号
//	private String machine;//机织
//	private Integer charge_user;//打样人 ，跟单人
//	private String detail;//报价详情
//	
//	private String img_s;//中等缩略图
//	private String img_ss;//缩略图
//	
//	private Integer factoryId;// 生产单位
//	
//	public String getImg_s() {
//		return img_s;
//	}
//	public void setImg_s(String img_s) {
//		this.img_s = img_s;
//	}
//	public String getImg_ss() {
//		return img_ss;
//	}
//	public void setImg_ss(String img_ss) {
//		this.img_ss = img_ss;
//	}
//	public String getCproductN() {
//		return cproductN;
//	}
//	public void setCproductN(String cproductN) {
//		this.cproductN = cproductN;
//	}
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
//	
//	public Integer getOrderId() {
//		return orderId;
//	}
//	public void setOrderId(Integer orderId) {
//		this.orderId = orderId;
//	}
//	public int getQuantity() {
//		return quantity;
//	}
//	public void setQuantity(int quantity) {
//		this.quantity = quantity;
//	}
//	public double getAmount() {
//		return amount;
//	}
//	public void setAmount(double amount) {
//		this.amount = amount;
//	}
//	public double getPrice() {
//		return price;
//	}
//	public void setPrice(double price) {
//		this.price = price;
//	}
//	public String getMemo() {
//		return memo;
//	}
//	public void setMemo(String memo) {
//		this.memo = memo;
//	}
//	public int getSampleId() {
//		return sampleId;
//	}
//	public void setSampleId(int sampleId) {
//		this.sampleId = sampleId;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getImg() {
//		return img;
//	}
//	public void setImg(String img) {
//		this.img = img;
//	}
//	public String getMaterial() {
//		return material;
//	}
//	public void setMaterial(String material) {
//		this.material = material;
//	}
//	public double getWeight() {
//		return weight;
//	}
//	public void setWeight(double weight) {
//		this.weight = weight;
//	}
//	public String getSize() {
//		return size;
//	}
//	public void setSize(String size) {
//		this.size = size;
//	}
//	public double getCost() {
//		return cost;
//	}
//	public void setCost(double cost) {
//		this.cost = cost;
//	}
//	public String getProductNumber() {
//		return productNumber;
//	}
//	public void setProductNumber(String productNumber) {
//		this.productNumber = productNumber;
//	}
//	public String getMachine() {
//		return machine;
//	}
//	public void setMachine(String machine) {
//		this.machine = machine;
//	}
//	public Integer getCharge_user() {
//		return charge_user;
//	}
//	public void setCharge_user(Integer charge_user) {
//		this.charge_user = charge_user;
//	}
//	public String getDetail() {
//		return detail;
//	}
//	public void setDetail(String detail) {
//		this.detail = detail;
//	}
//	public Integer getFactoryId() {
//		return factoryId;
//	}
//	public void setFactoryId(Integer factoryId) {
//		this.factoryId = factoryId;
//	}
//	
	
	
}
