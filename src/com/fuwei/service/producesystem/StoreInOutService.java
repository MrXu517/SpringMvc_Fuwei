package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.financial.Expense_income;
import com.fuwei.entity.ordergrid.PackingOrder;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.service.BaseService;
import com.fuwei.util.CreateNumberUtil;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;


/*原材料入库单*/
@Component
public class StoreInOutService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(StoreInOutService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	MaterialCurrentStockService materialCurrentStockService;

	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId, Integer factoryId,Integer charge_employee,String number,Boolean in_out, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_store_in_out");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " companyId='" + companyId+ "'");
				seq = " AND ";
			}

			if (start_time != null) {//出入库时间
				sql_condition.append(seq + " date>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " date<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (factoryId != null) {
				sql_condition.append(seq + " factoryId='" + factoryId + "'");
				seq = " AND ";
			}
			if (in_out != null) {
				sql_condition.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (charge_employee != null) {
				sql_condition.append(seq + " charge_employee='" + charge_employee+ "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number+ "'");
				seq = " AND ";
			}
			

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
		
			return findPager_T(sql.append(sql_condition).toString(), StoreInOut.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加
	@Transactional
	public int add(StoreInOut object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("原材料出、入库单中至少得有一条材料出入库列表记录");
			} else {
				object.setStatus(0);
				object.setState("新建");
				object.setHas_print(false);//未打印
				object.setHas_tagprint(false);//未打印标签
				object.setDetail_json(SerializeTool.serialize(object
						.getDetaillist()));

				Integer id = this.insert(object);
				object.setId(id);
				object.setNumber(object.createNumber());
				this.update(object, "id", null);
				//更新库存表
				materialCurrentStockService.reStock(object.getOrderId());

				return id;
			}
		} catch (Exception e) {

			throw e;
		}
	}
	
	//更新是否打印属性
	@Transactional
	public int updatePrint(StoreInOut object) throws Exception {
		// 更新表
		dao.update("update tb_store_in_out set has_print=? where id=?", object.getHas_print(),object.getId());

		return object.getId();
	}
	
	//更新是否打印纱线标签属性
	@Transactional
	public int updateTagPrint(StoreInOut object) throws Exception {
		// 更新表
		dao.update("update tb_store_in_out set has_tagprint=? where id=?", object.getHas_tagprint(),object.getId());

		return object.getId();
	}
	// 编辑
	@Transactional
	public int update(StoreInOut object) throws Exception {
		try {
			object.setHas_print(false);
			object.setHas_tagprint(false);
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("原材料出、入库单至少得有一条材料列表记录");
			} else {
				StoreInOut temp = this.get(object.getId());
				if (!temp.isEdit()) {
					throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
				}
				String details = SerializeTool.serialize(object
						.getDetaillist());
				object.setDetail_json(details);

				// 更新表
				this.update(object, "id",
						"number,created_user,created_at,orderId,store_order_id,companyId,customerId,sampleId,name,img,img_s,img_ss,materialId,weight,size,productNumber,orderNumber,charge_employee,company_productNumber", true);

				//更新库存表
				materialCurrentStockService.reStock(temp.getOrderId());
				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取
	public List<StoreInOut> getByStoreOrder(int storeOrderId) throws Exception {
		try {
			List<StoreInOut> orderlist = dao.queryForBeanList(
					"select * from tb_store_in_out where store_order_id = ?",
					StoreInOut.class, storeOrderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public List<StoreInOut> getByStoreOrder(int storeOrderId,Boolean in_out) throws Exception {
		try {
			List<StoreInOut> orderlist = dao.queryForBeanList(
					"select * from tb_store_in_out where store_order_id = ? and in_out=?",
					StoreInOut.class, storeOrderId,in_out);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public List<StoreInOut> getByOrder(int orderId) throws Exception {
		try {
			List<StoreInOut> orderlist = dao.queryForBeanList(
					"select * from tb_store_in_out where orderId = ?",
					StoreInOut.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public List<StoreInOut> getByOrder(int OrderId,Boolean in_out) throws Exception {
		try {
			List<StoreInOut> orderlist = dao.queryForBeanList(
					"select * from tb_store_in_out where orderId = ? and in_out=?",
					StoreInOut.class, OrderId,in_out);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public StoreInOut get(int id,boolean in_out) throws Exception {
		try {
			StoreInOut order = dao.queryForBean(
					"select * from tb_store_in_out where id = ? and in_out=?",
					StoreInOut.class, id,in_out);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public StoreInOut get(int id) throws Exception {
		try {
			StoreInOut order = dao.queryForBean(
					"select * from tb_store_in_out where id = ?",
					StoreInOut.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByStoreOrder(int store_order_id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_store_in_out SET status=?,state=? WHERE store_order_id = ?",
							6, "执行完成", store_order_id);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int updateStatus(int tableOrderId, int status, String state)
			throws Exception {
		try {
			return dao.update(
					"UPDATE tb_store_in_out SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 删除
	public int remove(int id) throws Exception {
		try {
			StoreInOut temp = this.get(id);
			if(temp.getIn_out() == false && temp.getHas_print()){//如果出库单已打印，则不能再删除
				throw new Exception("已打印出库，无法删除 ");
			}
			if(!temp.deletable()){
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_store_in_out WHERE  id = ?", id);
			//更新库存表
			materialCurrentStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与原材料出入库单有关的引用");
			}
			throw e;
		}
	}
	
	// 获取
	public List<StoreInOut> getByFactory(int storeOrderId,int factoryId, Boolean in_out)
			throws Exception {
		try {
			List<StoreInOut> orderlist = dao
					.queryForBeanList(
							"select * from tb_store_in_out where store_order_id=? and factoryId = ? and in_out=?",
							StoreInOut.class, storeOrderId,factoryId,in_out);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
}
