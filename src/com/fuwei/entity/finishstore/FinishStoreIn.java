package com.fuwei.entity.finishstore;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//成品出入库
@Table("tb_finishstore_in")
public class FinishStoreIn {
	@IdentityId
	private Integer id;
	private String number;//成品出入库单编号
	private Integer orderId;
	private String orderNumber;
	private Integer packingOrderId;//装箱单ID
	private String name;//样品名称
	private int charge_employee;
	private String company_productNumber;//样品的公司货号
	private Integer companyId;// 公司ID
	private Integer customerId;// 客户ID
	private String img;// 图片
	private String img_s;// 中等缩略图
	private String img_ss;// 缩略图

	
	private Date date;//入库、出库日期
	private String sign;//领取人签字
	
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建人
	private String memo;//备注
	@Temporary
	private List<FinishStoreInDetail> detaillist ;
	
	/*2015-7-8添加 是否打印、 是否打印属性*/
	private Boolean has_print;
	
	private Integer status;// 订单状态 -1刚创建  , 6执行完成 ， 7取消
	private String state;// 订单状态描述
	
	@Temporary
	private Integer col1_id;//装箱单中动态列1的列属性ID ， 根据ID可以在缓存中获取name
	@Temporary
	private Integer col2_id;//装箱单中动态列2的列属性ID ， 根据ID可以在缓存中获取name
	@Temporary
	private Integer col3_id;//装箱单中动态列3的列属性ID ， 根据ID可以在缓存中获取name
	@Temporary
	private Integer col4_id;//装箱单中动态列4的列属性ID ， 根据ID可以在缓存中获取name
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharge_employee() {
		return charge_employee;
	}

	public void setCharge_employee(int charge_employee) {
		this.charge_employee = charge_employee;
	}

	public String getCompany_productNumber() {
		return company_productNumber;
	}

	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getPackingOrderId() {
		return packingOrderId;
	}

	public void setPackingOrderId(Integer packingOrderId) {
		this.packingOrderId = packingOrderId;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<FinishStoreInDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<FinishStoreInDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public Boolean getHas_print() {
		return has_print;
	}

	public void setHas_print(Boolean has_print) {
		this.has_print = has_print;
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
		return DateTool.getYear2() + "FRK" + NumberUtil.appendZero(this.id, 4);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
	public String getType() throws ParseException{
		return "入库";
	}
}
