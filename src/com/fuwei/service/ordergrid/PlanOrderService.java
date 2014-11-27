package com.fuwei.service.ordergrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.HeadBankOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
//import com.fuwei.entity.ProducingOrderDetail;
//import com.fuwei.entity.ProducingOrderMaterialDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.util.SerializeTool;

@Component
public class PlanOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(PlanOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加生产单
	@Transactional
	public int add(PlanOrder planOrder) throws Exception {
		try {
			if (planOrder.getDetaillist() == null
					|| planOrder.getDetaillist().size() <= 0) {
				throw new Exception("计划单中至少得有一条颜色及数量详情记录");
			} else {
//				if (planOrder.getDetail_2_list() == null
//						|| planOrder.getDetail_2_list().size() <= 0) {
//					throw new Exception("计划单中至少得有一条生产计划详情记录");
//				} else {
					planOrder.setDetail_json(SerializeTool
							.serialize(planOrder.getDetaillist()));
//					planOrder
//							.setDetail_2_json(SerializeTool
//									.serialize(planOrder
//											.getDetail_2_list()));

					Integer planOrderId = this.insert(planOrder);

					planOrder.setId(planOrderId);

					return planOrderId;
//				}
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑生产单
	@Transactional
	public int update(PlanOrder planOrder) throws Exception {
		try {
			if (planOrder.getDetaillist() == null
					|| planOrder.getDetaillist().size() <= 0) {
				throw new Exception("计划单中至少得有一条颜色及数量详情记录");
			} else {
//				if (planOrder.getDetail_2_list() == null
//						|| planOrder.getDetail_2_list().size() <= 0) {
//					throw new Exception("计划单中至少得有一条生产计划详情记录");
//				} else {
					String details = SerializeTool.serialize(planOrder
							.getDetaillist());
					planOrder.setDetail_json(details);
					
//					planOrder.setDetail_2_json(SerializeTool
//							.serialize(planOrder
//									.getDetail_2_list()));

					// 更新表
					this.update(planOrder, "id",
							"created_user,created_at,orderId", true);

					return planOrder.getId();
//				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取生产单
	public PlanOrder getByOrder(int orderId) throws Exception {
		try {
			PlanOrder order = dao.queryForBean(
					"select * from tb_planorder where orderId = ?",
					PlanOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取生产单
	public PlanOrder get(int id) throws Exception {
		try {
			PlanOrder order = dao.queryForBean(
					"select * from tb_planorder where id = ?",
					PlanOrder.class, id);
//			if (order.getDetail_json() == null) {
//				order.setDetaillist(new ArrayList<HeadBankOrderDetail>());
//				return order;
//			}
//			order.setDetaillist(SerializeTool.deserializeList(order
//					.getDetail_json(), HeadBankOrderDetail.class));
			return order;
		} catch (Exception e) {
			throw e;
		}
	}


}
