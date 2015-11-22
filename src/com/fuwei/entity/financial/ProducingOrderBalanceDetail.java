package com.fuwei.entity.financial;

import java.util.Date;
import java.util.List;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.util.SerializeTool;

//生产单对账单明细
@Table("tb_producing_order_balance_detail")
public class ProducingOrderBalanceDetail {
	@IdentityId
	private int id;// 明细ID
	private Integer producingOrderId; //生产单ID
	private Date producingOrder_createdAt;//生产单日期
	private String producingOrder_number; //生产单号
	private Integer orderId;//订单ID
	private String orderNumber;//订单编号
	private String company_productNumber;//公司货号
	private Integer factoryId;// 生产单位
	@Temporary
	private String factory_name;// 生产单位
	private Integer companyId;// 公司ID
	@Temporary
	private String company_name;// 
	private Integer customerId;
	private Integer sampleId;// 样品ID
	private String sample_name;// 样品名称
	private Integer charge_employee;// 打样人 
	@Temporary
	private String charge_employee_name;//
	
	private double total_amount;//总金额
	
	private double deduct_money;  //扣款金额
	private String deduct_memo;//扣款理由
	private String memo ; //备注
	private String detail_json;
	private int status = 0;
	@Temporary
	private List<ProducingOrderDetail> detaillist ;
	
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDetail_json() {
		return detail_json;
	}

	public void setDetail_json(String detail_json) throws Exception {
		if(detail_json != null && !detail_json.equals("")){
			this.setDetaillist(SerializeTool.deserializeList(detail_json,ProducingOrderDetail.class));
		}
		
		this.detail_json = detail_json;
	}

	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getFactory_name() {
		return factory_name;
	}
	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}
	public String getCharge_employee_name() {
		return charge_employee_name;
	}
	public void setCharge_employee_name(String charge_employee_name) {
		this.charge_employee_name = charge_employee_name;
	}
	public Date getProducingOrder_createdAt() {
		return producingOrder_createdAt;
	}
	public void setProducingOrder_createdAt(Date producingOrder_createdAt) {
		this.producingOrder_createdAt = producingOrder_createdAt;
	}
	public String getProducingOrder_number() {
		return producingOrder_number;
	}
	public void setProducingOrder_number(String producingOrder_number) {
		this.producingOrder_number = producingOrder_number;
	}
	public String getCompany_productNumber() {
		return company_productNumber;
	}
	public void setCompany_productNumber(String company_productNumber) {
		this.company_productNumber = company_productNumber;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getProducingOrderId() {
		return producingOrderId;
	}
	public void setProducingOrderId(Integer producingOrderId) {
		this.producingOrderId = producingOrderId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
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
	
	public String getSample_name() {
		return sample_name;
	}
	public void setSample_name(String sample_name) {
		this.sample_name = sample_name;
	}
	public Integer getCharge_employee() {
		return charge_employee;
	}
	public void setCharge_employee(Integer charge_employee) {
		this.charge_employee = charge_employee;
	}
	
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	public double getDeduct_money() {
		return deduct_money;
	}
	public void setDeduct_money(double deduct_money) {
		this.deduct_money = deduct_money;
	}
	public String getDeduct_memo() {
		return deduct_memo;
	}
	public void setDeduct_memo(String deduct_memo) {
		this.deduct_memo = deduct_memo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<ProducingOrderDetail> getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(List<ProducingOrderDetail> detaillist) {
		this.detaillist = detaillist;
	}
	
	
	
	
}
