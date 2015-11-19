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
//import com.fuwei.entity.ordergrid.ColoringProcessOrder;
//import com.fuwei.service.BaseService;
//
//import com.fuwei.util.SerializeTool;
//
//@Component
//public class ColoringProcessOrderService extends BaseService {
//	private Logger log = org.apache.log4j.LogManager
//			.getLogger(ColoringProcessOrderService.class);
//	@Autowired
//	JdbcTemplate jdbc;
//
//	// 添加生产进度单
//	@Transactional
//	public int add(ColoringProcessOrder tableOrder) throws Exception {
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
//	// 编辑生产进度单
//	@Transactional
//	public int update(ColoringProcessOrder tableOrder) throws Exception {
//		try {
//
//			ColoringProcessOrder temp = this.get(tableOrder.getId());
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
//	// 获取生产进度单
//	public ColoringProcessOrder getByOrder(int orderId) throws Exception {
//		try {
//			ColoringProcessOrder order = dao.queryForBean(
//					"select * from tb_coloringprocessorder where orderId = ?",
//					ColoringProcessOrder.class, orderId);
//			return order;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	// 获取生产进度单
//	public ColoringProcessOrder get(int id) throws Exception {
//		try {
//			ColoringProcessOrder order = dao.queryForBean(
//					"select * from tb_coloringprocessorder where id = ?",
//					ColoringProcessOrder.class, id);
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
//							"UPDATE tb_coloringprocessorder SET status=?,state=? WHERE orderId = ?",
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
//							"UPDATE tb_coloringprocessorder SET status=?,state=? WHERE id = ?",
//							status, state, tableOrderId);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//}
