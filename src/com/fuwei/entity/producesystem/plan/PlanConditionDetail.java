package com.fuwei.entity.producesystem.plan;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

//@Table("tb_planconditiondetail")
public class PlanConditionDetail {
//	@IdentityId
	private int id;
	private int planConditionId;
	private int gongxuId;//工序ID
	private int gongxuProduction_per_hour;//该工序每小时生产量
	private int gongxuProduction_per_day;//该工序每天生产量
	private int gongxuWorkhour_per_day;//该工序每天工作时间
	private Date start_date;//该工序可以开始运作的时间， 受辅料等的约束
	private double pass_rate;//该工序产品合格率
	private int quantity;//需完成数量
	private int complete_quantity;//已完成数量
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlanConditionId() {
		return planConditionId;
	}
	public void setPlanConditionId(int planConditionId) {
		this.planConditionId = planConditionId;
	}
	public int getGongxuId() {
		return gongxuId;
	}
	public void setGongxuId(int gongxuId) {
		this.gongxuId = gongxuId;
	}
	public int getGongxuProduction_per_hour() {
		return gongxuProduction_per_hour;
	}
	public void setGongxuProduction_per_hour(int gongxuProduction_per_hour) {
		this.gongxuProduction_per_hour = gongxuProduction_per_hour;
	}
	public int getGongxuProduction_per_day() {
		return gongxuProduction_per_day;
	}
	public void setGongxuProduction_per_day(int gongxuProduction_per_day) {
		this.gongxuProduction_per_day = gongxuProduction_per_day;
	}
	public int getGongxuWorkhour_per_day() {
		return gongxuWorkhour_per_day;
	}
	public void setGongxuWorkhour_per_day(int gongxuWorkhour_per_day) {
		this.gongxuWorkhour_per_day = gongxuWorkhour_per_day;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public double getPass_rate() {
		return pass_rate;
	}
	public void setPass_rate(double pass_rate) {
		this.pass_rate = pass_rate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getComplete_quantity() {
		return complete_quantity;
	}
	public void setComplete_quantity(int complete_quantity) {
		this.complete_quantity = complete_quantity;
	}
	
	
}
