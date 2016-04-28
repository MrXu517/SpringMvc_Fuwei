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
import com.fuwei.entity.producesystem.HalfStoreReturn;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class HalfStoreReturnService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(HalfStoreReturnService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	HalfCurrentStockService halfCurrentStockService;

	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number,Boolean isyanchang, List<Sort> sortlist)
			throws Exception {
		try {
			if(isyanchang){
				return getList_yanchang(pager, start_time, end_time, companyId, factoryId, charge_employee, number, sortlist);
			}
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_half_store_return");

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
					HalfStoreReturn.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取列表
	public Pager getList_yanchang(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer factoryId, Integer charge_employee,
			String number, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select a.* from tb_half_store_return a ,tb_factory b where a.factoryId=b.id and b.isyanchang=1  ");

			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " a.companyId='" + companyId + "'");
				seq = " AND ";
			}

			if (start_time != null) {// 退货时间
				sql_condition.append(seq + " a.date>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " a.date<'"
						+ DateTool.formateDate(DateTool.addDay(end_time,1))
						+ "'");
				seq = " AND ";
			}
			if (factoryId != null) {
				sql_condition.append(seq + " a.factoryId='" + factoryId + "'");
				seq = " AND ";
			}
			if (charge_employee != null) {
				sql_condition.append(seq + " a.charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " a.number='" + number + "'");
				seq = " AND ";
			}

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by a."
								+ sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append(",a."
								+ sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}

			return findPager_T(sql.append(sql_condition).toString(),
					HalfStoreReturn.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(HalfStoreReturn object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("半成品退货单至少得有一条颜色及数量记录");
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
	public int updatePrint(HalfStoreReturn object) throws Exception {
		// 更新表
		dao.update("update tb_half_store_return set has_print=? where id=?", object
				.getHas_print(), object.getId());

		return object.getId();
	}


	// 编辑
	@Transactional
	public int update(HalfStoreReturn object) throws Exception {
		try {
			object.setHas_print(false);
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("半成品退货单至少得有一条颜色及数量记录");
			} else {
				HalfStoreReturn temp = this.get(object.getId());
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
								"number,created_user,created_at,orderId,companyId,customerId,sampleId,name,img,img_s,img_ss,productNumber,orderNumber,charge_employee,company_productNumber",
								true);
				//更新半成品库存表
				halfCurrentStockService.reStock(object.getOrderId());

				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}
	
	// 获取
	public List<HalfStoreReturn> getByOrder(int orderId,Boolean isyanchang)
			throws Exception {
		try {
			if(isyanchang){
				return getByOrder_yanchang(orderId);
			}
			List<HalfStoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_return where orderId = ?",
							HalfStoreReturn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public List<HalfStoreReturn> getByOrderDESC(int orderId,Boolean isyanchang)
			throws Exception {
		try {
			if(isyanchang){
				return getByOrderDESC_yanchang(orderId);
			}
			List<HalfStoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_return where orderId = ? order by date desc",
							HalfStoreReturn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public List<HalfStoreReturn> getByOrder_yanchang(int orderId)
			throws Exception {
		try {
			List<HalfStoreReturn> orderlist = dao
					.queryForBeanList(
							"select a.* from tb_half_store_return a,tb_factory b where a.factoryId=b.id and b.isyanchang=1 and orderId = ?",
							HalfStoreReturn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public List<HalfStoreReturn> getByOrderDESC_yanchang(int orderId)
			throws Exception {
		try {
			List<HalfStoreReturn> orderlist = dao
					.queryForBeanList(
							"select a.* from tb_half_store_return a,tb_factory b where a.factoryId=b.id and b.isyanchang=1 and orderId = ? order by date desc",
							HalfStoreReturn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public List<HalfStoreReturn> getByFactory(int factoryId)
			throws Exception {
		try {
			List<HalfStoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_return where factoryId = ?",
							HalfStoreReturn.class, factoryId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	


	// 获取
	public HalfStoreReturn get(int id) throws Exception {
		try {
			HalfStoreReturn order = dao.queryForBean(
					"select * from tb_half_store_return where id = ?",
					HalfStoreReturn.class, id);
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
							"UPDATE tb_half_store_return SET status=?,state=? WHERE orderId = ?",
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
					"UPDATE tb_half_store_return SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try {
			HalfStoreReturn temp = this.get(id);
			if (temp.getHas_print()) {// 如果已打印，则不能再删除
				throw new Exception("已打印出库，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_half_store_return WHERE  id = ?", id);
			//更新半成品库存表
			halfCurrentStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与半成品退货单有关的引用");
			}
			throw e;
		}
	}
	
	// 删除
	public int remove(HalfStoreReturn temp) throws Exception {
		try {
			if (temp.getHas_print()) {// 如果出库单已打印，
																	// 则不能再删除
				throw new Exception("已打印出库，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_half_store_return WHERE  id = ?", temp.getId());
			//更新半成品库存表
			halfCurrentStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与半成品退货单有关的引用");
			}
			throw e;
		}
	}

	
	// 获取
	public List<HalfStoreReturn> getByFactoryGongxu(int orderId,int factoryId,int gongxuId)
			throws Exception {
		try {
			List<HalfStoreReturn> orderlist = dao
					.queryForBeanList(
							"select * from tb_half_store_return where orderId=? and factoryId = ? and gongxuId=?",
							HalfStoreReturn.class, orderId,factoryId, gongxuId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
}
