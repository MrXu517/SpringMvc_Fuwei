package com.fuwei.service.ordergrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.CheckRecordOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.HeadBankOrderDetail;
import com.fuwei.entity.ordergrid.IroningRecordOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
//import com.fuwei.entity.ProducingOrderDetail;
//import com.fuwei.entity.ProducingOrderMaterialDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.util.SerializeTool;

@Component
public class IroningRecordOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(IroningRecordOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加整烫记录单
	@Transactional
	public int add(IroningRecordOrder tableOrder) throws Exception {
		try {
//			if (tableOrder.getDetaillist() == null
//					|| tableOrder.getDetaillist().size() <= 0) {
//				throw new Exception("整烫记录单中至少得有一条颜色及数量记录");
//			} else {
//				tableOrder.setDetail_json(SerializeTool
//							.serialize(tableOrder.getDetaillist()));

					Integer tableOrderId = this.insert(tableOrder);

					tableOrder.setId(tableOrderId);

					return tableOrderId;
//			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑整烫记录单
	@Transactional
	public int update(IroningRecordOrder tableOrder) throws Exception {
		try {
//			if (tableOrder.getDetaillist() == null
//					|| tableOrder.getDetaillist().size() <= 0) {
//				throw new Exception("整烫记录单中至少得有一条颜色及数量记录");
//			} else {
//					String details = SerializeTool.serialize(tableOrder
//							.getDetaillist());
//					tableOrder.setDetail_json(details);

					// 更新表
					this.update(tableOrder, "id",
							"created_user,created_at,orderId", true);

					return tableOrder.getId();
//			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取整烫记录单
	public IroningRecordOrder getByOrder(int orderId) throws Exception {
		try {
			IroningRecordOrder order = dao.queryForBean(
					"select * from tb_ironingrecordorder where orderId = ?",
					IroningRecordOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取整烫记录单
	public IroningRecordOrder get(int id) throws Exception {
		try {
			IroningRecordOrder order = dao.queryForBean(
					"select * from tb_ironingrecordorder where id = ?",
					IroningRecordOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}


}
