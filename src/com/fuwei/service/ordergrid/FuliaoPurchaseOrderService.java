package com.fuwei.service.ordergrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.ordergrid.CheckRecordOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.HeadBankOrderDetail;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
//import com.fuwei.entity.ProducingOrderDetail;
//import com.fuwei.entity.ProducingOrderMaterialDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.util.SerializeTool;

@Component
public class FuliaoPurchaseOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoPurchaseOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加辅料采购单
	@Transactional
	public int add(FuliaoPurchaseOrder tableOrder) throws Exception {
		try {
			if (tableOrder.getDetaillist() == null
					|| tableOrder.getDetaillist().size() <= 0) {
				throw new Exception("辅料采购单中至少得有一条采购记录");
			} else {
				tableOrder.setDetail_json(SerializeTool
							.serialize(tableOrder.getDetaillist()));

					Integer tableOrderId = this.insert(tableOrder);

					tableOrder.setId(tableOrderId);

					return tableOrderId;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑辅料采购单
	@Transactional
	public int update(FuliaoPurchaseOrder tableOrder) throws Exception {
		try {
			if (tableOrder.getDetaillist() == null
					|| tableOrder.getDetaillist().size() <= 0) {
				throw new Exception("辅料采购单中至少得有一条采购记录");
			} else {
					String details = SerializeTool.serialize(tableOrder
							.getDetaillist());
					tableOrder.setDetail_json(details);

					// 更新表
					this.update(tableOrder, "id",
							"created_user,created_at,orderId", true);

					return tableOrder.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取辅料采购单
	public FuliaoPurchaseOrder getByOrder(int orderId) throws Exception {
		try {
			FuliaoPurchaseOrder order = dao.queryForBean(
					"select * from tb_fuliaopurchaseorder where orderId = ?",
					FuliaoPurchaseOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取辅料采购单
	public FuliaoPurchaseOrder get(int id) throws Exception {
		try {
			FuliaoPurchaseOrder order = dao.queryForBean(
					"select * from tb_fuliaopurchaseorder where id = ?",
					FuliaoPurchaseOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}


}
