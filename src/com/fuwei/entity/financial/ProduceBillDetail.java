package com.fuwei.entity.financial;

import java.util.Date;
import java.util.List;

import com.fuwei.util.SerializeTool;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//生产单对账单明细
@Table("tb_producebilldetail")
public class ProduceBillDetail {
	@IdentityId
	private int id;// 明细ID
	private int produceBillId;//生产对账单ID，外键
	private int quantity;//总数量
	private double deduct;//扣款
	private double amount;//金额，未减扣款
	private double payable_amount;// 总金额 - 总扣款
	private String memo;
	@Temporary
	List<ProduceBillDetail_Detail> detaillist;
	
	private int gongxuId;//工序ID
	private int orderId;
	private String orderNumber;
	private String name;// 样品名称
	private int producingOrderId;//生产单ID或者工序加工单ID
	private String producingOrderNumber;//生产单Number或者工序加工单Number
	private String company_productNumber;//大货货号
	private Integer charge_employee;// 跟单人
	private Integer companyId;// 公司ID
	private Integer sampleId;// 样品ID
	private Date producingOrder_created_at;// 创建时间
	
	@Temporary
	private String details;
	
	
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) throws Exception {
		if(details != null && !details.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(details,ProduceBillDetail_Detail.class));
		}
		
		this.details = details;
	}
	public int getGongxuId() {
		return gongxuId;
	}
	public void setGongxuId(int gongxuId) {
		this.gongxuId = gongxuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProducingOrderNumber() {
		return producingOrderNumber;
	}
	public void setProducingOrderNumber(String producingOrderNumber) {
		this.producingOrderNumber = producingOrderNumber;
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
	public Integer getSampleId() {
		return sampleId;
	}
	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	public Date getProducingOrder_created_at() {
		return producingOrder_created_at;
	}
	public void setProducingOrder_created_at(Date producingOrder_created_at) {
		this.producingOrder_created_at = producingOrder_created_at;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
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
	public int getProduceBillId() {
		return produceBillId;
	}
	public void setProduceBillId(int produceBillId) {
		this.produceBillId = produceBillId;
	}
	public int getProducingOrderId() {
		return producingOrderId;
	}
	public void setProducingOrderId(int producingOrderId) {
		this.producingOrderId = producingOrderId;
	}
	public double getDeduct() {
		return deduct;
	}
	public void setDeduct(double deduct) {
		this.deduct = deduct;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getPayable_amount() {
		return payable_amount;
	}
	public void setPayable_amount(double payable_amount) {
		this.payable_amount = payable_amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<ProduceBillDetail_Detail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<ProduceBillDetail_Detail> detaillist) {
		this.detaillist = detaillist;
	}
	
	
}
