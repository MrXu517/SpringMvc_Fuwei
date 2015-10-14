package com.fuwei.entity;

import java.io.Serializable;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

/**
 * 
 * 潘通色号对象属性类
 *四个属性：潘通色号,页号,行号,列号
 */
@Table("tb_pantongcolor")
public class PantongColor implements Serializable{
	@IdentityId
	private String panTongName;
	/**
	 * int
	 * 潘通色号坐标中的页号
	 */
	private int sheetNum;
	/**
	 * int
	 * 潘通色号坐标中的行号
	 */
	private int rowNum;
	/**
	 * int 
	 * 潘通色号坐标中的列号
	 */
	private int columnNum;

	public PantongColor(){
		
	}
	
	public PantongColor(String panTongName,int sheetNum,int rowNum,int columnNum){
		this.panTongName=panTongName;
		this.sheetNum=sheetNum;
		this.rowNum=rowNum;
		this.columnNum=columnNum;
	}

	public String getPanTongName() {
		return panTongName;
	}

	public void setPanTongName(String panTongName) {
		this.panTongName = panTongName;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	
	
	
}
