package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_salary")
public class Salary implements Serializable, Comparable {
	@IdentityId
	private int id;
	private String name;// 姓名,一般是中文名称
	private String number;// 员工编号
	private int departmentId; // 部门
	private double hour_salary;// 时薪
	private double work_hour;// 白班工作时间
	private double over_normal;// 平时加班
	private double over_weekend;// 周末加班
	private double over_holiday;// 节假日加班
	
	private double sick_leave; //病假时间（单位：小时）
	private double compassionate_leave;//事假(单位：小时)
	private double year_leave ; // 年假(单位：小时)
	
	private int year;// 年份
	private int month;// 月份
	private double payable_salary; // 应发工资

	private double work_money;
	private double over_normal_money;
	private double over_weekend_money;
	private double over_holiday_money;

	private double holiday_reback;// 假日补贴
	private double insurance_deduction;// 保险
	private double personal_tax; // 个税

	private double real_salary;// 实发工资

	private Date leave_at;

	public int compareTo(Object o) {
		if (o instanceof Salary) {
			Salary s = (Salary) o;
			return s.name.compareTo(this.name);
		}
		return -1;
	}
	
	

	public double getSick_leave() {
		return sick_leave;
	}



	public void setSick_leave(double sick_leave) {
		this.sick_leave = sick_leave;
	}



	public double getCompassionate_leave() {
		return compassionate_leave;
	}



	public void setCompassionate_leave(double compassionate_leave) {
		this.compassionate_leave = compassionate_leave;
	}



	public double getYear_leave() {
		return year_leave;
	}



	public void setYear_leave(double year_leave) {
		this.year_leave = year_leave;
	}



	public Date getLeave_at() {
		return leave_at;
	}

	public void setLeave_at(Date leave_at) {
		this.leave_at = leave_at;
	}

	public Double getHoliday_reback() {
		return holiday_reback;
	}

	public void setHoliday_reback(Double holiday_reback) {
		this.holiday_reback = NumberUtil.formateDouble(holiday_reback, 2);
	}

	public Double getInsurance_deduction() {
		return insurance_deduction;
	}

	public void setInsurance_deduction(double insurance_deduction) {
		this.insurance_deduction = insurance_deduction;
	}

	public Double getPersonal_tax() {
		return personal_tax;
	}

	public void setPersonal_tax(double personal_tax) {
		this.personal_tax = personal_tax;
	}

	public Double getWork_money() {
		return this.work_money;
	}

	public void setWork_money(Double work_money) {
		this.work_money = NumberUtil.formateDouble(this.work_hour
				* this.hour_salary, 2);
	}

	public Double getOver_normal_money() {
		return this.over_normal_money;
	}

	public void setOver_normal_money(Double over_normal_money) {
		this.over_normal_money = NumberUtil.formateDouble(this.over_normal
				* this.hour_salary * 1.5, 2);
	}

	public Double getOver_weekend_money() {
		return this.over_weekend_money;
	}

	public void setOver_weekend_money(Double over_weekend_money) {
		this.over_weekend_money = NumberUtil.formateDouble(this.over_weekend
				* this.hour_salary * 2, 2);
	}

	public Double getOver_holiday_money() {
		return this.over_holiday_money;
	}

	public void setOver_holiday_money(Double over_holiday_money) {
		this.over_holiday_money = NumberUtil.formateDouble(this.over_holiday
				* this.hour_salary * 3, 2);
	}

	public int getId() {
		return id;
	}

	public Double getPayable_salary() {
		return this.payable_salary;
	}

	public void setPayable_salary(double payable_salary) {
		this.payable_salary = NumberUtil.formateDouble(this.hour_salary
				* (this.work_hour + this.over_normal * 1.5 + this.over_weekend
						* 2 + this.over_holiday * 3) + holiday_reback, 2);
	}

	public Double getReal_salary() {
		return this.real_salary;
	}

	public void setReal_salary(Double real_salary) {
		this.real_salary = NumberUtil.formateDouble(getPayable_salary()
				- insurance_deduction - personal_tax, 2);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public Double getHour_salary() {
		return hour_salary;
	}

	public void setHour_salary(Double hour_salary) {
		this.hour_salary = hour_salary;
	}

	public Double getWork_hour() {
		return work_hour;
	}

	public void setWork_hour(Double work_hour) {
		this.work_hour = work_hour;
	}

	public Double getOver_normal() {
		return over_normal;
	}

	public void setOver_normal(Double over_normal) {
		this.over_normal = over_normal;
	}

	public Double getOver_weekend() {
		return over_weekend;
	}

	public void setOver_weekend(Double over_weekend) {
		this.over_weekend = over_weekend;
	}

	public Double getOver_holiday() {
		return over_holiday;
	}

	public void setOver_holiday(Double over_holiday) {
		this.over_holiday = over_holiday;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public double personal_tax() { // 计算个人所得税
		// java中switch的case变量只支持int char string，而此处是double,所以不能使用switch
		double shouru = this.getPayable_salary() - 3500;//构造函数中本来已经有了，所以再次使用的使用，
														// 用成money即可
		if (shouru <= 0) {
			this.personal_tax = 0;
		} else if (shouru <= 1500) {
			this.personal_tax = shouru * 0.03;
		} else if (1500 < shouru && shouru <= 4500) {
			this.personal_tax = 1500 * 0.03 + (shouru - 1500) * 0.1;
		} else if (4500 < shouru && shouru <= 9000) {
			this.personal_tax = 1500 * 0.03 + (4500 - 1500) * 0.1
					+ (shouru - 4500) * 0.2;
		} else if (9000 < shouru && shouru <= 35000) {
			this.personal_tax = 1500 * 0.03 + (4500 - 1500) * 0.1
					+ (9000 - 4500) * 0.2 + (shouru - 9000) * 0.25;
		}
		this.personal_tax = NumberUtil.formateDouble(this.personal_tax, 2);
		this.setReal_salary(this.real_salary);
		return this.personal_tax;
	}
}
