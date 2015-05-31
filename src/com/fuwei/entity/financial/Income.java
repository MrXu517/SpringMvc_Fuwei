package com.fuwei.entity.financial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;

//收入明细
@Table("tb_income")
public class Income implements Serializable {
	@IdentityId
	private int id;// 明细ID
	
	private int subject_id;//科目ID
	
	private String subject_name;
	
	private int partner_id;
	
	private String partner_name;
	
	private double amount;//收入金额
	
	private String memo;//备注
	
	private 
	@Temporary
	List<SaleInvoice> invoiceList = new ArrayList<SaleInvoice>();
	
	
	
}
