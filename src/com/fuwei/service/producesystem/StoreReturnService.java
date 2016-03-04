package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.entity.producesystem.StoreReturn;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class StoreReturnService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(StoreReturnService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	MaterialCurrentStockService materialCurrentStockService;

	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_store_return");

			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " companyId='" + companyId + "'");
				seq = " AND ";
			}

			if (start_time != null) {// 退货时间
				sql_condition.append(seq + " date>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " date<'"
						+ DateTool.formateDate(DateTool.addDay(end_time,1))
						+ "'");
				seq = " AND ";
			}
			if (factoryId != null) {
				sql_condition.append(seq + " factoryId='" + factoryId + "'");
				seq = " AND ";
			}
			if (charge_employee != null) {
				sql_condition.append(seq + " charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number + "'");
				seq = " AND ";
			}

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by "
								+ sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append(","
								+ sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}

			return findPager_T(sql.append(sql_condition).toString(),
					StoreReturn.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional(rollbackFor=Exception.class)
	public int add(StoreReturn object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("原材料退货单至少得有一条明细记录");
			} else {
				object.setStatus(0);
				object.setState("新建");
				object.setHas_print(false);// 未打印
				object.setDetail_json(SerializeTool.serialize(object
						.getDetaillist()));

				Integer id = this.insert(object);
				object.setId(id);
				object.setNumber(object.createNumber());
				this.update(object, "id", null);

				//如果是样纱退库单
				if(object.getColoring_order_id()!=null){
					//更新样纱库存表
					materialCurrentStockService.reStock_Coloring(object.getColoring_order_id());
				}else{
					//更新库存表
					materialCurrentStockService.reStock(object.getOrderId());
				}
				return id;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 更新是否打印属性
	@Transactional
	public int updatePrint(StoreReturn object) throws Exception {
		// 更新表
		dao.update("update tb_store_return set has_print=? where id=?", object
				.getHas_print(), object.getId());

		return object.getId();
	}


	// 编辑
	@Transactional(rollbackFor=Exception.class)
	public int update(StoreReturn object) throws Exception {
		try {
			object.setHas_print(false);
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("原材料退货单至少得有一条明细记录");
			} else {
				StoreReturn temp = this.get(object.getId());
				if (!temp.isEdit()) {
					throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
				}
				String details = SerializeTool
						.serialize(object.getDetaillist());
				object.setDetail_json(details);
				
				// 更新表
				this
						.update(
								object,
								"id",
								"number,created_user,created_at,store_order_id,orderId,companyId,customerId,sampleId,name,img,img_s,img_ss,productNumber,orderNumber,charge_employee,company_productNumber",
								true);

				//如果是样纱退库单
				if(object.getColoring_order_id()!=null){
					//更新样纱库存表
					materialCurrentStockService.reStock_Coloring(object.getColoring_order_id());
				}else{
					//更新库存表
					materialCurrentStockService.reStock(object.getOrderId());
				}

				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}
	
	// 获取
	public List<StoreReturn> getByOrder(int orderId)
			throws Exception {
		try {
			List<StoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_store_return where orderId = ?",
							StoreReturn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 根据染色单获取
	public List<StoreReturn> getByColoringOrder(int coloringOrderId) throws Exception {
		try {
			List<StoreReturn> orderlist = dao.queryForBeanList(
					"select * from tb_store_return where coloring_order_id = ?",
					StoreReturn.class, coloringOrderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 根据原材料仓库单获取
	public List<StoreReturn> getByStoreOrder(int store_order_id)
			throws Exception {
		try {
			List<StoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_store_return where store_order_id = ?",
							StoreReturn.class, store_order_id);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
//	// 根据染色单获取//2016-2-23暂时没有样纱退货的功能
//	public List<StoreReturn> getByColoringOrder(int coloring_order_id)
//			throws Exception {
//		try {
//			List<StoreReturn> orderlist = dao
//					.queryForBeanList(
//							"select * from tb_store_return where store_order_id is null and  coloring_order_id = ?",
//							StoreReturn.class, coloring_order_id);
//			return orderlist;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	// 获取
	public List<StoreReturn> getByOrderDESC(int orderId)
			throws Exception {
		try {
			List<StoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_store_return where orderId = ? order by date desc",
							StoreReturn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public StoreReturn get(int id) throws Exception {
		try {
			StoreReturn order = dao.queryForBean(
					"select * from tb_store_return where id = ?",
					StoreReturn.class, id);
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
							"UPDATE tb_store_return SET status=?,state=? WHERE orderId = ?",
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
					"UPDATE tb_store_return SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除
	@Transactional(rollbackFor=Exception.class)
	public int remove(int id) throws Exception {
		try {
			StoreReturn temp = this.get(id);
			if (temp.getHas_print()) {// 如果已打印，则不能再删除
				throw new Exception("已打印，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_store_return WHERE  id = ?", id);

			//如果是样纱退库单
			if(temp.getColoring_order_id()!=null){
				//更新样纱库存表
				materialCurrentStockService.reStock_Coloring(temp.getColoring_order_id());
			}else{
				//更新库存表
				materialCurrentStockService.reStock(temp.getOrderId());
			}
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与原材料退货单有关的引用");
			}
			throw e;
		}
	}
	
	// 删除
	@Transactional(rollbackFor=Exception.class)
	public int remove(StoreReturn temp) throws Exception {
		try {
			if (temp.getHas_print()) {// 如果出库单已打印，
																	// 则不能再删除
				throw new Exception("已打印，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_store_return WHERE  id = ?", temp.getId());
			//如果是样纱退库单
			if(temp.getColoring_order_id()!=null){
				//更新样纱库存表
				materialCurrentStockService.reStock_Coloring(temp.getColoring_order_id());
			}else{
				//更新库存表
				materialCurrentStockService.reStock(temp.getOrderId());
			}
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与原材料退货单有关的引用");
			}
			throw e;
		}
	}

	
	// 获取
	public List<StoreReturn> getByFactory(int storeOrderId,int factoryId)
			throws Exception {
		try {
			List<StoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_store_return where store_order_id=? and factoryId = ?",
							StoreReturn.class, storeOrderId,factoryId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
}
