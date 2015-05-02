package com.fuwei.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.ordergrid.CarFixRecordOrderDetail;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_order")
public class Order implements Serializable {
	@IdentityId
	private int id;// 主键
	private String orderNumber;// 订单号
	private int status;// 订单状态 -1刚创建
	private String state;// 订单状态描述
	private double amount;// 订单总金额
	private String memo;// 订单备注
	private String info;// 订单信息（样品名称(样品克重)）
	private Date start_at;// 订单生效开始时间
	private Date end_at;// 订单截止时间
	private Date delivery_at;// 发货时间
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private int created_user;// 创建用户
	private Integer salesmanId;// 业务员ID
	private Integer companyId;// 公司ID
//	private String kehu;// 客户
	private Integer customerId;

	// 以下为2014-11-3 添加的，假定一个订单只有一个样品，因此去除了以前的orderDetailList
//	private Integer factoryId;// 生产单位
	private double price;// 报价单价
	private int quantity;// 数量
	// private double amount;//总价
	// private String memo;//价格的备注
	private Integer sampleId;// 样品ID
	private String cproductN; // 公司款号

	// 接下来的Sample的属性
	private String name;// 样品名称

	private String img;// 图片
	private Integer materialId;// 材料
	private double weight;// 克重
	private String size;// 尺寸

	private double cost;// 成本
	private String productNumber;// 产品编号
	//private Integer machineId;// 机织
//	private Integer charge_user;// 打样人 ，跟单人
	private String detail;// 报价详情

	private String img_s;// 中等缩略图
	private String img_ss;// 缩略图

	// 动态的生产步骤
	private Integer stepId;

	private String step_state;
	@Temporary
	private List<OrderStep> stepList;
	// 动态的生产步骤

	private String detail_json;
	@Temporary
	private List<OrderDetail> detaillist;
	
	//2015-3-4增加
	private Boolean in_use;//是否取消
	
	
	//2015-4-4 添加 样品的公司货号
	private String company_productNumber;//样品的公司货号
	
	//2015-5-2修改
	private Integer charge_employee;// 打样人 ，跟单人
	
	public String getCompany_productNumber() {
		return company_productNumber;
	}

	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}

	public Boolean getIn_use() {
		return in_use;
	}

	public void setIn_use(Boolean in_use) {
		this.in_use = in_use;
	}

//	public String getKehu() {
//		return kehu;
//	}
//
//	public void setKehu(String kehu) {
//		this.kehu = kehu;
//	}
	
	

	public List<OrderStep> getStepList() {
		return stepList;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getDetail_json() {
		return detail_json;
	}

	public void setDetail_json(String detail_json) throws Exception {
		if(detail_json != null && !detail_json.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(detail_json,OrderDetail.class));
		}
		this.detail_json = detail_json;
	}

	public List<OrderDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<OrderDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public void setStepList(List<OrderStep> stepList) {
		this.stepList = stepList;
	}

	public Integer getStepId() {
		return stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	public String getStep_state() {
		return step_state;
	}

	public void setStep_state(String step_state) {
		this.step_state = step_state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getStart_at() {
		return start_at;
	}

	public void setStart_at(Date start_at) {
		this.start_at = start_at;
	}

	public Date getEnd_at() {
		return end_at;
	}

	public void setEnd_at(Date end_at) {
		this.end_at = end_at;
	}

	public Date getDelivery_at() {
		return delivery_at;
	}

	public void setDelivery_at(Date delivery_at) {
		this.delivery_at = delivery_at;
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

	public int getCreated_user() {
		return created_user;
	}

	public void setCreated_user(int created_user) {
		this.created_user = created_user;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	// public List<OrderDetail> getDetaillist() {
	// return detaillist;
	// }
	// public void setDetaillist(List<OrderDetail> detaillist) {
	// this.detaillist = detaillist;
	// }

	

	// 获取当前状态描述
	public String getCNState() {
		if (this.stepId == null) {
			return this.state;
		} else {
			return this.step_state;
		}
	}

	// 获取发货时间
	public String getDevelivery() {
		if (this.delivery_at == null) {
			return "未发货";
		} else {
			try {
				return DateTool.formateDate(this.delivery_at);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}

	// 2014-11-10 删除该方法
	// //是否下一步要开始生产
	// public Boolean startProduce(){
	// if(this.status == OrderStatus.BEFOREPRODUCESAMPLE.ordinal()){
	// return true;
	// }
	// return false;
	// }
//	public Integer getFactoryId() {
//		return factoryId;
//	}
//
//	public void setFactoryId(Integer factoryId) {
//		this.factoryId = factoryId;
//	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	public String getCproductN() {
		return cproductN;
	}

	public void setCproductN(String cproductN) {
		this.cproductN = cproductN;
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

	

//	public Integer getMachineId() {
//		return machineId;
//	}
//
//	public void setMachineId(Integer machineId) {
//		this.machineId = machineId;
//	}

	

	public String getDetail() {
		return detail;
	}

	public Integer getCharge_employee() {
		return charge_employee;
	}

	public void setCharge_employee(Integer charge_employee) {
		this.charge_employee = charge_employee;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
	
	// 是否可编辑
	public Boolean isEdit() {
		// return this.status < OrderStatus.COLORING.ordinal();
		return this.status < OrderStatus.DELIVERED.ordinal();// 2014-11-10修改：
																// 在已发货之前都可以修改订单
	}
	public Boolean isDelivered() {
		return this.status >= OrderStatus.DELIVERED.ordinal();// 2014-11-10修改：
																// 在已发货之前都可以创建单据
	}
	
	//是否可取消订单
	public Boolean isCancelable(){
		return this.status < OrderStatus.DELIVERED.ordinal();
	}
	public Boolean isCompleted(){
		return this.status >= OrderStatus.COMPLETED.ordinal();
	}
	
	//2015-4-25添加是否超过截止日期未发货
	public Boolean isOverEnded() throws ParseException{
		return !this.isDelivered() && this.end_at.before(DateTool.nowDate());
	}
	
	//2015-4-25添加是否最近30天内要发货
	public Boolean isPre30() throws ParseException{
		return !this.isDelivered() && !this.isOverEnded() && !this.end_at.after(DateTool.addDay(DateTool.nowDate(), 30));
	}

}
