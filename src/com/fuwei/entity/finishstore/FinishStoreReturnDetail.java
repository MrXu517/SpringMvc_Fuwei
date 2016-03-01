package com.fuwei.entity.finishstore;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_finishstore_return_detail")
public class FinishStoreReturnDetail {
	@IdentityId
	private int id;
	private int finishStoreReturnId;
	private int packingOrderDetailId;//装箱单明细ID
	private int quantity;//出入库数量
	private int cartons;//出入库箱数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFinishStoreReturnId() {
		return finishStoreReturnId;
	}
	public void setFinishStoreReturnId(int finishStoreReturnId) {
		this.finishStoreReturnId = finishStoreReturnId;
	}
	public int getPackingOrderDetailId() {
		return packingOrderDetailId;
	}
	public void setPackingOrderDetailId(int packingOrderDetailId) {
		this.packingOrderDetailId = packingOrderDetailId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getCartons() {
		return cartons;
	}
	public void setCartons(int cartons) {
		this.cartons = cartons;
	}
	
	
}
