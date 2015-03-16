package com.fuwei.entity.ordergrid;

import java.util.Date;
import java.util.List;

import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//原材料采购单
@Table("tb_coloringorder")
public class ColoringOrder extends BaseTableOrder{
	@IdentityId
	private int id;
	private Integer orderId;//订单ID
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	
	private Integer created_user;//创建用户
	
	private String detail_json;
	
	@Temporary
	private List<ColoringOrderDetail> detaillist ;
	
	private int factoryId;//染色单位

	private Integer companyId;// 公司ID
	private String kehu;// 客户
	// 接下来的Sample的属性
	private Integer sampleId;// 样品ID
	
	private String name;// 样品名称

	private String img;// 图片
	private String material;// 材料
	private double weight;// 克重
	private String size;// 尺寸
	private String productNumber;// 产品编号
	private String orderNumber;//订单编号
	
	private String img_s;// 中等缩略图
	private String img_ss;// 缩略图
	
	
	
	
	
	
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getKehu() {
		return kehu;
	}

	public void setKehu(String kehu) {
		this.kehu = kehu;
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
			this.setDetaillist(SerializeTool.deserializeList(detail_json,ColoringOrderDetail.class));
		}
		
		this.detail_json = detail_json;
	}

	

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public List<ColoringOrderDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<ColoringOrderDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	
}
