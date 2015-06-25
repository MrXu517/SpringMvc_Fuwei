package com.fuwei.service.ordergrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.Order;
import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.HeadBankOrder;
import com.fuwei.entity.ordergrid.HeadBankOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.StoreOrder; //import com.fuwei.entity.ProducingOrderDetail;
//import com.fuwei.entity.ProducingOrderMaterialDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.util.SerializeTool;

@Component
public class StoreOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(StoreOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加生产单
	@Transactional
	public int add(StoreOrder storeOrder) throws Exception {
		try {
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new Exception("原材料仓库中至少得有一条材料列表记录");
			} else {
				storeOrder.setStatus(0);
				storeOrder.setState("新建");
				storeOrder.setDetail_json(SerializeTool.serialize(storeOrder
						.getDetaillist()));
				// storeOrder
				// .setDetail_2_json(SerializeTool
				// .serialize(storeOrder
				// .getDetail_2_list()));

				Integer storeOrderId = this.insert(storeOrder);

				storeOrder.setId(storeOrderId);

				return storeOrderId;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑生产单
	@Transactional
	public int update(StoreOrder storeOrder) throws Exception {
		try {
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new Exception("原材料仓库中至少得有一条材料列表记录");
			} else {
				StoreOrder temp = this.get(storeOrder.getId());
				if (!temp.isEdit()) {
					throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
				}
				String details = SerializeTool.serialize(storeOrder
						.getDetaillist());
				storeOrder.setDetail_json(details);

				// storeOrder.setDetail_2_json(SerializeTool
				// .serialize(storeOrder
				// .getDetail_2_list()));

				// 更新表
				this.update(storeOrder, "id",
						"created_user,created_at,orderId", true);

				return storeOrder.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取生产单
	public StoreOrder getByOrder(int orderId) throws Exception {
		try {
			StoreOrder order = dao.queryForBean(
					"select * from tb_storeorder where orderId = ?",
					StoreOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取生产单
	public StoreOrder get(int id) throws Exception {
		try {
			StoreOrder order = dao.queryForBean(
					"select * from tb_storeorder where id = ?",
					StoreOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_storeorder SET status=?,state=? WHERE orderId = ?",
							6, "执行完成", orderId);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int updateStatus(int tableOrderId, int status, String state)
			throws Exception {
		try {
			return dao.update(
					"UPDATE tb_storeorder SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	//获取所有原材料仓库单
	public List<StoreOrder> getList(){
		return dao.queryForBeanList("select * from tb_storeorder", StoreOrder.class);
	}
	
}
