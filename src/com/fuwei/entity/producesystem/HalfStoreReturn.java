package com.fuwei.entity.producesystem;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//半成品出入库
@Table("tb_half_store_return")
public class HalfStoreReturn {
	@IdentityId
	private Integer id;
	private String number;//半成品退货单编号
	private int gongxuId;//工序Id
	private Integer orderId;
	
	private Date date;//退货日期
	private Integer factoryId;//加工工厂
	private String sign;//领取人签字
	
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建人
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	private String memo;//备注
	private String detail_json;
	@Temporary
	private List<HalfStoreReturnDetail> detaillist ;
	
	
	// 接下来是Order的属性
	private Integer companyId;// 公司ID
	private Integer customerId;
	private Integer sampleId;// 样品ID
	
	private String name;// 样品名称

	private String img;// 图片
//	private Integer materialId;// 材料
//	private double weight;// 克重
//	private String size;// 尺寸
	private String productNumber;// 产品编号
	private String orderNumber;//订单编号
	
	private String img_s;// 中等缩略图
	private String img_ss;// 缩略图
	private Integer charge_employee;// 打样人 
	private String company_productNumber;//样品的公司货号
	
	/*2015-7-8添加 是否打印、 是否打印纱线标签 属性*/
	private Boolean has_print;
	
	
	
	public int getGongxuId() {
		return gongxuId;
	}
	public void setGongxuId(int gongxuId) {
		this.gongxuId = gongxuId;
	}
	public Boolean getHas_print() {
		return has_print;
	}
	public void setHas_print(Boolean has_print) {
		this.has_print = has_print;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
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
//	public Integer getMaterialId() {
//		return materialId;
//	}
//	public void setMaterialId(Integer materialId) {
//		this.materialId = materialId;
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
	
	public String getDetail_json() {
		return detail_json;
	}
	public void setDetail_json(String detail_json) throws Exception {
		if(detail_json != null && !detail_json.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(detail_json,HalfStoreReturnDetail.class));
		}
		
		this.detail_json = detail_json;
	}
	public List<HalfStoreReturnDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<HalfStoreReturnDetail> detaillist) {
		this.detaillist = detaillist;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}
//	public Integer getEmployee_id() {
//		return employee_id;
//	}
//	public void setEmployee_id(Integer employee_id) {
//		this.employee_id = employee_id;
//	}
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
	
	public Boolean deletable(){
		if(this.has_print){
			return false;
		}
		if(this.status == null){
			return true;
		}
		return this.status != 6;
	}
	
	public String createNumber() throws ParseException{		
			return DateTool.getYear2() + "HR" + NumberUtil.appendZero(this.id, 4);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String printStr(){
		if(this.has_print!=null && this.has_print){
			return "是";
		}
		else{
			return "否";
		}
	}
}

