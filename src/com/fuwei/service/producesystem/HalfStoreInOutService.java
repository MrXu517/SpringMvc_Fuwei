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
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.entity.producesystem.FuliaoIn;
import com.fuwei.entity.producesystem.FuliaoInDetail;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.service.BaseService;
import com.fuwei.service.DataCorrectRecordService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class HalfStoreInOutService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(HalfStoreInOutService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	HalfCurrentStockService halfCurrentStockService;
	@Autowired
	DataCorrectRecordService dataCorrectRecordService;

	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, Boolean in_out, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_half_store_in_out");

			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " companyId='" + companyId + "'");
				seq = " AND ";
			}

			if (start_time != null) {// 出入库时间
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
			if (in_out != null) {
				sql_condition.append(seq + " in_out='"
						+ (in_out == true ? "1" : 0) + "'");
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
					HalfStoreInOut.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(HalfStoreInOut object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("半成品出、入库单至少得有一条颜色及数量记录");
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
				//更新半成品库存表
				halfCurrentStockService.reStock(object.getOrderId());
				return id;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 更新是否打印属性
	@Transactional
	public int updatePrint(HalfStoreInOut object) throws Exception {
		// 更新表
		dao.update("update tb_half_store_in_out set has_print=? where id=?", object
				.getHas_print(), object.getId());

		return object.getId();
	}


	// 编辑
	@Transactional
	public int update(HalfStoreInOut object) throws Exception {
		try {
			object.setHas_print(false);
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("半成品出、入库单至少得有一条颜色及数量记录");
			} else {
				HalfStoreInOut temp = this.get(object.getId());
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
								"number,created_user,created_at,orderId,producing_order_id,companyId,customerId,sampleId,name,img,img_s,img_ss,materialId,weight,size,productNumber,orderNumber,charge_employee,company_productNumber,producing_order_number",
								true);
				//更新半成品库存表
				halfCurrentStockService.reStock(object.getOrderId());

				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}


//	// 获取
//	public List<HalfStoreInOut> getByProducingOrder(int producingOrderId, Boolean in_out)
//			throws Exception {
//		try {
//			List<HalfStoreInOut> orderlist = dao
//					.queryForBeanList(
//							"select * from tb_half_store_in_out where producing_order_id = ? and in_out=?",
//							HalfStoreInOut.class, producingOrderId, in_out);
//			return orderlist;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	// 获取
	public List<HalfStoreInOut> getByOrder(int orderId, Boolean in_out)
			throws Exception {
		try {
			List<HalfStoreInOut> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_in_out where orderId = ? and in_out=?",
							HalfStoreInOut.class, orderId, in_out);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public List<HalfStoreInOut> getByOrderDESC(int orderId)
			throws Exception {
		try {
			List<HalfStoreInOut> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_in_out where orderId = ? order by date desc",
							HalfStoreInOut.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public HalfStoreInOut get(int id, boolean in_out) throws Exception {
		try {
			HalfStoreInOut order = dao.queryForBean(
					"select * from tb_half_store_in_out where id = ? and in_out=?",
					HalfStoreInOut.class, id, in_out);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public HalfStoreInOut get(int id) throws Exception {
		try {
			HalfStoreInOut order = dao.queryForBean(
					"select * from tb_half_store_in_out where id = ?",
					HalfStoreInOut.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByStoreOrder(int producing_order_id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_half_store_in_out SET status=?,state=? WHERE producing_order_id = ?",
							6, "执行完成", producing_order_id);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int updateStatus(int tableOrderId, int status, String state)
			throws Exception {
		try {
			return dao.update(
					"UPDATE tb_half_store_in_out SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	// 数据纠正_删除
	@Transactional
	public int remove_datacorrect(HalfStoreInOut temp,DataCorrectRecord datacorrect) throws Exception {
		try {
			int id = temp.getId();
			//如果单据并未打印，且并未执行完成，则无需数据纠正，正常删除单据即可
			if (!temp.getHas_print() && temp.deletable()) {// 
				throw new Exception("半成品入库单并未打印且并未执行完成，无需数据纠正，正常删除单据即可");
			}
			
			int result = dao.update("delete from tb_half_store_in_out WHERE  id = ?", id);
			//更新半成品库存表
			halfCurrentStockService.reStock(temp.getOrderId());
			//3.添加数据纠正记录
			dataCorrectRecordService.add(datacorrect);
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与半成品出入库单有关的引用");
			}
			throw e;
		}
	}
	@Transactional
	public int remove_datacorrect(int id,DataCorrectRecord datacorrect) throws Exception {
		HalfStoreInOut temp = this.get(id);
		return remove_datacorrect(temp,datacorrect);
	}
	
	// 删除
	public int remove(int id) throws Exception {
		try {
			HalfStoreInOut temp = this.get(id);
			//如果单据已打印则不可以删除
			if (temp.getHas_print()) {// 如果出库单已打印，
																	// 则不能再删除
				throw new Exception("单据已打印，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_half_store_in_out WHERE  id = ?", id);
			//更新半成品库存表
			halfCurrentStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与半成品出入库单有关的引用");
			}
			throw e;
		}
	}
	
	// 删除
	public int remove(HalfStoreInOut temp) throws Exception {
		try {
			if (temp.getHas_print()) {// 如果出库单已打印，
																	// 则不能再删除
				throw new Exception("单据已打印，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_half_store_in_out WHERE  id = ?", temp.getId());
			//更新半成品库存表
			halfCurrentStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与半成品出入库单有关的引用");
			}
			throw e;
		}
	}

	
	// 获取
	public List<HalfStoreInOut> getByFactoryGongxu(int orderId,int factoryId,int gongxuId, Boolean in_out)
			throws Exception {
		try {
			List<HalfStoreInOut> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_in_out where orderId=? and factoryId = ? and gongxuId=? and in_out=?",
							HalfStoreInOut.class, orderId,factoryId, gongxuId,in_out);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
