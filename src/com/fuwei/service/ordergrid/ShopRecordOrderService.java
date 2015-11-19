//package com.fuwei.service.ordergrid;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fuwei.entity.ordergrid.ShopRecordOrder;
//import com.fuwei.service.BaseService;
//
//
//@Component
//public class ShopRecordOrderService extends BaseService {
//	private Logger log = org.apache.log4j.LogManager
//			.getLogger(FinalStoreOrderService.class);
//	@Autowired
//	JdbcTemplate jdbc;
//
//	// 添加车间记录单
//	@Transactional
//	public int add(ShopRecordOrder tableOrder) throws Exception {
//		try {
//			tableOrder.setStatus(0);
//			tableOrder.setState("新建");
//			Integer tableOrderId = this.insert(tableOrder);
//
//			tableOrder.setId(tableOrderId);
//
//			return tableOrderId;
//			// }
//		} catch (Exception e) {
//
//			throw e;
//		}
//	}
//
//	// 编辑车间记录单
//	@Transactional
//	public int update(ShopRecordOrder tableOrder) throws Exception {
//		try {
//
//			ShopRecordOrder temp = this.get(tableOrder.getId());
//			if (!temp.isEdit()) {
//				throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
//			}
//			// 更新表
//			this.update(tableOrder, "id", "created_user,created_at,orderId",
//					true);
//
//			return tableOrder.getId();
//			// }
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}
//
//	// 获取车间记录单
//	public ShopRecordOrder getByOrder(int orderId) throws Exception {
//		try {
//			ShopRecordOrder order = dao.queryForBean(
//					"select * from tb_shoprecordorder where orderId = ?",
//					ShopRecordOrder.class, orderId);
//			return order;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	// 获取车间记录单
//	public ShopRecordOrder get(int id) throws Exception {
//		try {
//			ShopRecordOrder order = dao.queryForBean(
//					"select * from tb_shoprecordorder where id = ?",
//					ShopRecordOrder.class, id);
//			return order;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Transactional
//	public int completeByOrder(int orderId) throws Exception {
//		try {
//			return dao
//					.update(
//							"UPDATE tb_shoprecordorder SET status=?,state=? WHERE orderId = ?",
//							6, "执行完成", orderId);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Transactional
//	public int updateStatus(int tableOrderId, int status, String state)
//			throws Exception {
//		try {
//			return dao
//					.update(
//							"UPDATE tb_shoprecordorder SET status=?,state=? WHERE id = ?",
//							status, state, tableOrderId);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//}
//
