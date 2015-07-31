package com.fuwei.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

@Table("tb_employee")
public class Employee implements Serializable {

	/**
	 * 
	 */

	@IdentityId
	private int id;
	private String name;//姓名,一般是中文名称
	private String help_code;//拼音简称

	private Boolean inUse;//是否离职

	private Date created_at;//创建时间

	private Date updated_at;//最近更新时间
	
	private Integer created_user;//创建用户

	private String tel;//手机

	private String email;//邮箱

	private String qq;//QQ

	private String sex;//性别
	
	private String number;//员工编号
	
	private Date enter_at;//入厂日期
	
	private String id_card;//身份证
	
	private int departmentId; //部门
	
	private String job; //岗位
	
	private String address_home;//家庭地址
	
	private String address;//现居住地
	
	private Date agreement_at;//合同开始时间
	
	private Date agreement_end; //合同结束时间
	
	private String employee_type;//用工形式
	
	private Date leave_at;//离职时间
	
	private Double year_salary;//年薪
	
	private Double hour_salary;//时薪
	
	private String nation;//民族
	
	private String education;//学历
	
	private Boolean married;//是否已婚
	
	private String bank_name;//开户行
	
	private String bank_no;//银行卡号
	
	private Date birthday;//出生年月
	
	private Boolean is_charge_employee = false;//是否跟单人
	
	


	public Boolean getIs_charge_employee() {
		return is_charge_employee;
	}

	public void setIs_charge_employee(Boolean is_charge_employee) {
		this.is_charge_employee = is_charge_employee;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getId() {
		return id;
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

	public String getHelp_code() {
		return help_code;
	}

	public void setHelp_code(String help_code) {
		this.help_code = help_code;
	}

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getEnter_at() {
		return enter_at;
	}

	public void setEnter_at(Date enter_at) {
		this.enter_at = enter_at;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getAddress_home() {
		return address_home;
	}

	public void setAddress_home(String address_home) {
		this.address_home = address_home;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getAgreement_at() {
		return agreement_at;
	}
	
	public void setAgreement_at(Date agreement_at) {
		this.agreement_at = agreement_at;
	}

	public Date getAgreement_end() {
		return agreement_end;
	}

	public void setAgreement_end(Date agreement_end) {
		this.agreement_end = agreement_end;
	}

	public String getEmployee_type() {
		return employee_type;
	}

	public void setEmployee_type(String employee_type) {
		this.employee_type = employee_type;
	}

	public Date getLeave_at() {
		return leave_at;
	}

	public void setLeave_at(Date leave_at) {
		this.leave_at = leave_at;
	}

	public Double getYear_salary() {
		return year_salary;
	}

	public void setYear_salary(Double year_salary) {
		this.year_salary = year_salary;
	}

	public Double getHour_salary() {
		return hour_salary;
	}

	public void setHour_salary(Double hour_salary) {
		this.hour_salary = hour_salary;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public Boolean getMarried() {
		return married;
	}

	public void setMarried(Boolean married) {
		this.married = married;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_no() {
		return bank_no;
	}

	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}

	public Integer getCreated_user() {
		return created_user;
	}

	public void setCreated_user(Integer created_user) {
		this.created_user = created_user;
	}
	
	public String createNumber() throws ParseException{
		return "FW1" + NumberUtil.appendZero(this.id, 3);
	}
	
	


}
