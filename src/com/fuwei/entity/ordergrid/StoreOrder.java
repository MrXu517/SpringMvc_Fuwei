package com.fuwei.entity.ordergrid;

import java.util.Date;
import java.util.List;

import com.fuwei.entity.producesystem.StoreInOutDetail;
import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//原材料仓库
@Table("tb_storeorder")
public class StoreOrder extends BaseTableOrder{
	@IdentityId
	private int id;
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
	private String detail_json;
	
	private String detail_2_json;
	
	@Temporary
	private List<StoreOrderDetail> detaillist ;
	
	@Temporary
	private List<StoreInOutDetail> detail_2_list ;
	
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	
	
	
	// 接下来是Order的属性
	private Integer companyId;// 公司ID
	private Integer customerId;
	private Integer sampleId;// 样品ID
	
	private String name;// 样品名称

	private String img;// 图片
	private Integer materialId;// 材料
	private double weight;// 克重
	private String size;// 尺寸
	private String productNumber;// 产品编号
	private String orderNumber;//订单编号
	
	private String img_s;// 中等缩略图
	private String img_ss;// 缩略图
	private Integer charge_employee;// 打样人 
	private String company_productNumber;//样品的公司货号
	
	
	
	
	
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getSampleId() {
		return sampleId;
	}
	public void setSampleId(Integer sampleId) {
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
	public Integer getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
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
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	public Integer getCharge_employee() {
		return charge_employee;
	}
	public void setCharge_employee(Integer charge_employee) {
		this.charge_employee = charge_employee;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
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
	
	// 是否可编辑
	public Boolean isEdit() {
		if(this.status == null){
			return true;
		}
		return this.status != 6 && this.status != 7;
	}
	
	public Boolean deletable(){
		if(this.status == null){
			return true;
		}
		return this.status != 6;
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

	public String getDetail_json() {
		return detail_json;
	}

	public void setDetail_json(String detail_json) throws Exception {
		if(detail_json != null && !detail_json.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(detail_json,StoreOrderDetail.class));
		}
		
		this.detail_json = detail_json;
	}

	public String getDetail_2_json() {
		return detail_2_json;
	}

	public void setDetail_2_json(String detail_2_json) throws Exception {
		if(detail_2_json != null && !detail_2_json.equals("")){
			this.setDetail_2_list(SerializeTool.deserializeList(detail_2_json,StoreInOutDetail.class));
		}
		
		this.detail_2_json = detail_2_json;
	}

	public List<StoreOrderDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<StoreOrderDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public List<StoreInOutDetail> getDetail_2_list() {
		return detail_2_list;
	}

	public void setDetail_2_list(List<StoreInOutDetail> detail_2_list) {
		this.detail_2_list = detail_2_list;
	}
	
	
}
