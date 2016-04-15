package com.fuwei.entity.finishstore;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;
@Table("tb_packingorder")
public class PackingOrder {
	@IdentityId
	private int id;
	private String number;//装箱单号
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	private String orderNumber;//订单号
	private String name;//样品名称
	private String company_productNumber;//样品的公司货号
	private Integer charge_employee;// 打样人 
	private Integer companyId;// 公司ID
	private Integer customerId;// 公司ID
	private String memo;
	
	private int quantity ;//总数量
	private int cartons;//总箱数
	private double capacity;//总立方数
	private Integer col1_id;//装箱单中动态列1的列属性ID ， 根据ID可以在缓存中获取name
	private Integer col2_id;//装箱单中动态列2的列属性ID ， 根据ID可以在缓存中获取name
	private Integer col3_id;//装箱单中动态列3的列属性ID ， 根据ID可以在缓存中获取name
	private Integer col4_id;//装箱单中动态列4的列属性ID ， 根据ID可以在缓存中获取name
	
	@Temporary
	private String img;// 图片
	@Temporary
	private String img_s;// 中等缩略图
	@Temporary
	private String img_ss;// 缩略图
	
	@Temporary
	private List<PackingOrderDetail> detaillist ;
	
	
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImg_s() {
		return img_s;
	}

	public void setImg_s(String img_s) {
		this.img_s = img_s;
	}

	public String getImg_ss() {
		return img_ss;
	}

	public void setImg_ss(String img_ss) {
		this.img_ss = img_ss;
	}

	public Integer getCol1_id() {
		return col1_id;
	}

	public void setCol1_id(Integer col1_id) {
		this.col1_id = col1_id;
	}

	public Integer getCol2_id() {
		return col2_id;
	}

	public void setCol2_id(Integer col2_id) {
		this.col2_id = col2_id;
	}

	public Integer getCol3_id() {
		return col3_id;
	}

	public void setCol3_id(Integer col3_id) {
		this.col3_id = col3_id;
	}

	public Integer getCol4_id() {
		return col4_id;
	}

	public void setCol4_id(Integer col4_id) {
		this.col4_id = col4_id;
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

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public List<PackingOrderDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<PackingOrderDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Integer getCreated_user() {
		return created_user;
	}

	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Boolean deletable(){
		if(this.status == null){
			return true;
		}
		return this.status != 6;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany_productNumber() {
		return company_productNumber;
	}

	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}

	public Integer getCharge_employee() {
		return charge_employee;
	}

	public void setCharge_employee(Integer charge_employee) {
		this.charge_employee = charge_employee;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	// 是否可编辑
	public Boolean isEdit() {
		if(this.status == null){
			return true;
		}
		return this.status != 6 && this.status != 7;
	}
	public String createNumber() throws ParseException{	
		return DateTool.getYear2() + "PA" + NumberUtil.appendZero(this.id%9999, 4);
		
	}
}



//package com.fuwei.entity.finishstore;
//
//import java.util.Date;
//
//import com.fuwei.entity.Order;
//
//import net.keepsoft.commons.annotation.IdentityId;
//import net.keepsoft.commons.annotation.Table;
//import net.keepsoft.commons.annotation.Temporary;
//@Table("tb_packingorder")
//public class PackingOrder {
//	@IdentityId
//	private int id;
//	private Integer orderId;//订单ID
//	private Date created_at;// 创建时间
//	private Date updated_at;// 最近更新时间
//	private Integer created_user;//创建用户
//	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
//	private String state;// 订单状态描述
//	
//	private String filepath;
//	
//	private String pdfpath;
//	
//	private String memo;
//	
//	@Temporary
//	private Order order = new Order();
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public Integer getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(Integer orderId) {
//		this.orderId = orderId;
//	}
//
//	public Date getCreated_at() {
//		return created_at;
//	}
//
//	public void setCreated_at(Date created_at) {
//		this.created_at = created_at;
//	}
//
//	public Date getUpdated_at() {
//		return updated_at;
//	}
//
//	public void setUpdated_at(Date updated_at) {
//		this.updated_at = updated_at;
//	}
//
//	public Integer getCreated_user() {
//		return created_user;
//	}
//
//	public void setCreated_user(Integer created_user) {
//		this.created_user = created_user;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getFilepath() {
//		return filepath;
//	}
//
//	public void setFilepath(String filepath) {
//		this.filepath = filepath;
//	}
//
//	public String getMemo() {
//		return memo;
//	}
//
//	public void setMemo(String memo) {
//		this.memo = memo;
//	}
//
//	public String getPdfpath() {
//		return pdfpath;
//	}
//
//	public void setPdfpath(String pdfpath) {
//		this.pdfpath = pdfpath;
//	}
//	
//	// 是否可编辑
//	public Boolean isEdit() {
//		if(this.status == null){
//			return true;
//		}
//		return this.status != 6 && this.status != 7;
//	}
//	
//	public Boolean deletable(){
//		if(this.status == null){
//			return true;
//		}
//		return this.status != 6;
//	}
//
//	public Order getOrder() {
//		return order;
//	}
//
//	public void setOrder(Order order) {
//		this.order = order;
//	}
//
//	public void setCompanyId(Integer companyId) {
//		order.setCompanyId(companyId);
//	}
//	public void setSalesmanId(Integer salesmanId) {
//		order.setSalesmanId(salesmanId);
//	}
//
//	public void setCustomerId(Integer customerId) {
//		order.setCustomerId(customerId);
//	}
//
//	public void setSampleId(Integer sampleId) {
//		order.setSampleId(sampleId);
//	}
//	public void setName(String name) {
//		order.setName(name);
//	}
//	public void setImg(String img) {
//		order.setImg(img);
//	}
//	public void setMaterialId(Integer materialId) {
//		order.setMaterialId(materialId);
//	}
//	public void setWeight(double weight) {
//		order.setWeight(weight);
//	}
//	public void setSize(String size) {
//		order.setSize(size);
//	}
//	public void setProductNumber(String productNumber) {
//		order.setProductNumber(productNumber);
//	}
//	public void setOrderNumber(String orderNumber) {
//		order.setOrderNumber(orderNumber);
//	}
//	public void setImg_s(String img_s) {
//		order.setImg_s(img_s);
//	}
//	public void setImg_ss(String img_ss) {
//		order.setImg_ss(img_ss);
//	}
//	public void setCharge_employee(Integer charge_employee) {
//		order.setCharge_employee(charge_employee);
//	}
//	public void setCompany_productNumber(String company_productNumber) {
//		order.setCompany_productNumber(company_productNumber);
//	}
//	
//	
//	
//	
//}
