package com.fuwei.entity.finishstore;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_finishstore_in_detail")
public class FinishStoreInDetail {
	@IdentityId
	private int id;
	private int finishStoreInOutId;
	private int packingOrderDetailId;//装箱单明细ID
	private int quantity;//入库数量
	private int cartons;//入库箱数
	
	public int getFinishStoreInOutId() {
		return finishStoreInOutId;
	}
	public void setFinishStoreInOutId(int finishStoreInOutId) {
		this.finishStoreInOutId = finishStoreInOutId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
