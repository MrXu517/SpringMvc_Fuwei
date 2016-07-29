package com.fuwei.entity.producesystem.plan;

import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
//@Table("tb_gongxuline")
public class GongxuLine {
//	@IdentityId
	private int id;
	private String name;//流水线名称
	private String line;//工序流水线描述  例如： 机织，套口，车标，整烫，检验，检针，包装
	private String gongxuIds;//工序ID集合
	private int gongxuId1;
	private int gongxuId2;
	private int gongxuId3;
	private int gongxuId4;
	private int gongxuId5;
	private int gongxuId6;
	private int gongxuId7;
	private int gongxuId8;
	private int gongxuId9;
	private int gongxuId10;
	private Date created_at;// 创建时间
	private Date updated_at;// 最近更新时间
	private Integer created_user;//创建用户
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
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	public String getGongxuIds() {
		return gongxuIds;
	}
	public void setGongxuIds(String gongxuIds) {
		this.gongxuIds = gongxuIds;
	}
	public int getGongxuId1() {
		return gongxuId1;
	}
	public void setGongxuId1(int gongxuId1) {
		this.gongxuId1 = gongxuId1;
	}
	public int getGongxuId2() {
		return gongxuId2;
	}
	public void setGongxuId2(int gongxuId2) {
		this.gongxuId2 = gongxuId2;
	}
	public int getGongxuId3() {
		return gongxuId3;
	}
	public void setGongxuId3(int gongxuId3) {
		this.gongxuId3 = gongxuId3;
	}
	public int getGongxuId4() {
		return gongxuId4;
	}
	public void setGongxuId4(int gongxuId4) {
		this.gongxuId4 = gongxuId4;
	}
	public int getGongxuId5() {
		return gongxuId5;
	}
	public void setGongxuId5(int gongxuId5) {
		this.gongxuId5 = gongxuId5;
	}
	public int getGongxuId6() {
		return gongxuId6;
	}
	public void setGongxuId6(int gongxuId6) {
		this.gongxuId6 = gongxuId6;
	}
	public int getGongxuId7() {
		return gongxuId7;
	}
	public void setGongxuId7(int gongxuId7) {
		this.gongxuId7 = gongxuId7;
	}
	public int getGongxuId8() {
		return gongxuId8;
	}
	public void setGongxuId8(int gongxuId8) {
		this.gongxuId8 = gongxuId8;
	}
	public int getGongxuId9() {
		return gongxuId9;
	}
	public void setGongxuId9(int gongxuId9) {
		this.gongxuId9 = gongxuId9;
	}
	public int getGongxuId10() {
		return gongxuId10;
	}
	public void setGongxuId10(int gongxuId10) {
		this.gongxuId10 = gongxuId10;
	}
	
	
}
