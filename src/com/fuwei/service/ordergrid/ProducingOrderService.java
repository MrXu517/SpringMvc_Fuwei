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
import com.fuwei.entity.ordergrid.ProducingOrder;
//import com.fuwei.entity.ProducingOrderDetail;
//import com.fuwei.entity.ProducingOrderMaterialDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.util.SerializeTool;

@Component
public class ProducingOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(ProducingOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加生产单
	@Transactional
	public int add(ProducingOrder producingOrder) throws Exception {
		try {
			if (producingOrder.getDetaillist() == null
					|| producingOrder.getDetaillist().size() <= 0) {
				throw new Exception("生产单中至少得有一条颜色及数量详情记录");
			} else {
				if (producingOrder.getDetail_2_list() == null
						|| producingOrder.getDetail_2_list().size() <= 0) {
					throw new Exception("生产单中至少得有一条生产材料详情记录");
				} else {
					producingOrder.setDetail_json(SerializeTool
							.serialize(producingOrder.getDetaillist()));
					producingOrder
							.setDetail_2_json(SerializeTool
									.serialize(producingOrder
											.getDetail_2_list()));

					Integer producingOrderId = this.insert(producingOrder);

					producingOrder.setId(producingOrderId);

					return producingOrderId;
				}
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑生产单
	@Transactional
	public int update(ProducingOrder producingOrder) throws Exception {
		try {
			if (producingOrder.getDetaillist() == null
					|| producingOrder.getDetaillist().size() <= 0) {
				throw new Exception("生产单中至少得有一条颜色及数量详情记录");
			} else {
				if (producingOrder.getDetail_2_list() == null
						|| producingOrder.getDetail_2_list().size() <= 0) {
					throw new Exception("生产单中至少得有一条生产材料详情记录");
				} else {
					String details = SerializeTool.serialize(producingOrder
							.getDetaillist());
					producingOrder.setDetail_json(details);
					
					producingOrder.setDetail_2_json(SerializeTool
							.serialize(producingOrder
									.getDetail_2_list()));

					// 更新表
					this.update(producingOrder, "id",
							"created_user,created_at,orderId,factoryId", true);

					return producingOrder.getId();
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取生产单
	public List<ProducingOrder> getByOrder(int orderId) throws Exception {
		try {
			List<ProducingOrder> list = dao.queryForBeanList("select * from tb_producingorder where orderId = ?", ProducingOrder.class, orderId);
//			if (order.getDetail_json() == null) {
//				order.setDetaillist(new ArrayList<ProducingOrderDetail>());
//				return order;
//			}else{
//				order.setDetaillist(SerializeTool.deserializeList(order
//						.getDetail_json(), ProducingOrderDetail.class));
//			}
//			if (order.getDetail_material_json() == null) {
//				order.setDetail_material_list(new ArrayList<ProducingOrderMaterialDetail>());
//				return order;
//			}else{
//				order.setDetail_material_list(SerializeTool.deserializeList(order
//						.getDetail_json(), ProducingOrderMaterialDetail.class));
//			}
			
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取生产单
	public ProducingOrder get(int id) throws Exception {
		try {
			ProducingOrder order = dao.queryForBean(
					"select * from tb_producingorder where id = ?",
					ProducingOrder.class, id);
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

	// // 获取生产单详情列表
	// public List<HeadBankOrderDetail> getDetailList(int headBankOrderId)
	// throws Exception {
	// try {
	// HeadBankOrder order = dao.queryForBean(
	// "select * from tb_headbankorder where id = ?",
	// HeadBankOrder.class, headBankOrderId);
	//			
	// List<HeadBankOrderDetail> detailList =
	// SerializeTool.deserializeList(order.getDetail_json(),
	// HeadBankOrderDetail.class);
	// return detailList;
	// } catch (Exception e) {
	// throw e;
	// }
	// }

}
