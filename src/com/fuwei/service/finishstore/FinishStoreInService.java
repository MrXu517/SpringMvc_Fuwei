package com.fuwei.service.finishstore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.entity.finishstore.FinishStoreIn;
import com.fuwei.entity.finishstore.FinishStoreInDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.DataCorrectRecordService;
import com.fuwei.util.DateTool;

@Component
public class FinishStoreInService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FinishStoreInService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FinishStoreStockService finishStoreStockService;
	@Autowired
	DataCorrectRecordService dataCorrectRecordService;
	@Autowired
	FinishStoreInDetailService finishStoreInOutDetailService;

	// 获取列表
	// 获取列表
	public Pager getListAndDetail(Pager pager, Date start_time, Date end_time,
			String orderNumber, Integer charge_employee,
			String number,List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_finishstore_in");

			StringBuffer sql_condition = new StringBuffer();
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
			if (charge_employee != null) {
				sql_condition.append(seq + " charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number + "'");
				seq = " AND ";
			}
			if (orderNumber != null && !orderNumber.equals("")) {
				sql_condition.append(seq + " orderNumber='" + orderNumber + "'");
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
			pager = findPager_T(sql.append(sql_condition).toString(),
					FinishStoreIn.class, pager);
			List<FinishStoreIn> list = (List<FinishStoreIn>)pager.getResult();
			if(list==null || list.size()<=0){
				return pager;
			}else{
				String ids = "";
				for(FinishStoreIn in : list){
					ids += in.getId()+ ",";
				}
				ids = ids.substring(0,ids.length()-1);
				String tempsql = "SELECT a.*,b.color,b.per_carton_quantity,b.per_pack_quantity,b.col1_value,b.col2_value,b.col3_value,b.col4_value FROM tb_finishstore_in_detail a,tb_packingorder_detail b WHERE a.packingOrderDetailId=b.id and  a.finishStoreInOutId in (" + ids + ") ";
				List<FinishStoreInDetail> totaldetaillist = dao.queryForBeanList(tempsql, FinishStoreInDetail.class, null);
				Map<Integer,List<FinishStoreInDetail>> map = new HashMap<Integer, List<FinishStoreInDetail>>();
				for(FinishStoreInDetail detail : totaldetaillist){
					int finishStoreId = detail.getFinishStoreInOutId();
					if(map.containsKey(finishStoreId)){
						List<FinishStoreInDetail> tempL = map.get(finishStoreId);
						tempL.add(detail);
						map.put(finishStoreId, tempL);
					}else{
						List<FinishStoreInDetail> tempL = new ArrayList<FinishStoreInDetail>();
						tempL.add(detail);
						map.put(finishStoreId, tempL);
					}
				}
				
				for(FinishStoreIn in : list){
					in.setDetaillist(map.get(in.getId()));
				}
			}
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Pager getList(Pager pager, Date start_time, Date end_time,
			String orderNumber, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_finishstore_in");

			StringBuffer sql_condition = new StringBuffer();
			

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
			
			if (orderNumber != null && !orderNumber.equals("")) {
				sql_condition.append(seq + " orderNumber='" + orderNumber + "'");
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
					FinishStoreIn.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	//获取某订单的成品入库单
	public List<FinishStoreIn> getList(int orderId){
		return dao.queryForBeanList("select * from tb_finishstore_in where orderId=? order by created_at desc", FinishStoreIn.class,orderId);
	}
	public List<FinishStoreIn> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_finishstore_in where orderNumber=?", FinishStoreIn.class,orderNumber);
	}
	
	// 添加
	@Transactional(rollbackFor=Exception.class)
	public int add(FinishStoreIn object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("成品出、入库单至少得有一条详情记录");
			} else {
				object.setStatus(0);
				object.setState("新建");
				object.setHas_print(false);// 未打印
				

				Integer id = this.insert(object);
				object.setId(id);
				object.setNumber(object.createNumber());
				this.update(object, "id", null);

				for(FinishStoreInDetail detail : object.getDetaillist()){
					detail.setFinishStoreInOutId(id);
				}
				finishStoreInOutDetailService.addBatch(object.getDetaillist());
				//更新成品库存表
				finishStoreStockService.reStock(object.getOrderId());
				return id;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 更新是否打印属性
	@Transactional
	public int updatePrint(FinishStoreIn object) throws Exception {
		// 更新表
		dao.update("update tb_finishstore_in set has_print=? where id=?", object
				.getHas_print(), object.getId());

		return object.getId();
	}


	// 编辑
	@Transactional(rollbackFor=Exception.class)
	public int update(FinishStoreIn object) throws Exception {
		try {
			object.setHas_print(false);
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("成品出、入库单至少得有一条详情记录");
			} else {
				FinishStoreIn temp = this.get(object.getId());
				if (!temp.isEdit()) {
					throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
				}
				finishStoreInOutDetailService.deleteBatch(object.getId());
				for(FinishStoreInDetail detail : object.getDetaillist()){
					detail.setFinishStoreInOutId(object.getId());
				}
				finishStoreInOutDetailService.addBatch(object.getDetaillist());
				// 更新表
				this.update(object,"id","number,packingOrderId,created_user,created_at,orderId,has_print,status,state",
								true);
				//更新成品库存表
				finishStoreStockService.reStock(object.getOrderId());

				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	
	// 获取
	public List<FinishStoreIn> getByOrder(int orderId)
			throws Exception {
		try {
			List<FinishStoreIn> orderlist = dao
					.queryForBeanList(
							"select * from tb_finishstore_in where orderId = ?",
							FinishStoreIn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public List<FinishStoreIn> getByOrderDESC(int orderId)
			throws Exception {
		try {
			List<FinishStoreIn> orderlist = dao
					.queryForBeanList(
							"select * from tb_finishstore_in where orderId = ? order by date desc",
							FinishStoreIn.class, orderId);
			return orderlist;
		} catch (Exception e) {
			throw e;
		}
	}


	// 获取
	public FinishStoreIn get(int id) throws Exception {
		try {
			FinishStoreIn order = dao.queryForBean(
					"select a.*,b.col1_id,b.col2_id,b.col3_id,b.col4_id from tb_finishstore_in a,tb_packingorder b where a.packingOrderId=b.id and a.id = ?",
					FinishStoreIn.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public FinishStoreIn getAndDetail(int id) throws Exception {
		try {
			FinishStoreIn finishStoreIn = this.get(id);
			if(finishStoreIn == null){
				return null;
			}
			List<FinishStoreInDetail> detaillist = finishStoreInOutDetailService.getList(finishStoreIn.getId());
			finishStoreIn.setDetaillist(detaillist);
			return finishStoreIn;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_finishstore_in SET status=?,state=? WHERE orderId = ?",
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
					"UPDATE tb_finishstore_in SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	// 数据纠正_删除
	@Transactional(rollbackFor=Exception.class)
	public int remove_datacorrect(FinishStoreIn temp,DataCorrectRecord datacorrect) throws Exception {
		try {
			int id = temp.getId();
			//如果单据并未打印，且并未执行完成，则无需数据纠正，正常删除单据即可
			if (!temp.getHas_print() && temp.deletable()) {// 
				throw new Exception("成品入库单并未打印且并未执行完成，无需数据纠正，正常删除单据即可");
			}
			
			int result = dao.update("delete from tb_finishstore_in WHERE  id = ?", id);
			//更新成品库存表
			finishStoreStockService.reStock(temp.getOrderId());
			//3.添加数据纠正记录
			dataCorrectRecordService.add(datacorrect);
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与成品出入库单有关的引用");
			}
			throw e;
		}
	}
	@Transactional
	public int remove_datacorrect(int id,DataCorrectRecord datacorrect) throws Exception {
		FinishStoreIn temp = this.get(id);
		return remove_datacorrect(temp,datacorrect);
	}
	
	// 删除
	@Transactional(rollbackFor=Exception.class)
	public int remove(int id) throws Exception {
		try {
			FinishStoreIn temp = this.get(id);
			//如果单据已打印则不可以删除
			if (temp.getHas_print()) {// 如果出库单已打印，
																	// 则不能再删除
				throw new Exception("单据已打印，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_finishstore_in WHERE  id = ?", id);
			//更新成品库存表
			finishStoreStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与成品出入库单有关的引用");
			}
			throw e;
		}
	}
	
	// 删除
	@Transactional(rollbackFor=Exception.class)
	public int remove(FinishStoreIn temp) throws Exception {
		try {
			if (temp.getHas_print()) {// 如果出库单已打印，
																	// 则不能再删除
				throw new Exception("单据已打印，无法删除 ");
			}
			if (!temp.deletable()) {
				throw new Exception("单据已执行完成，无法删除 ");
			}
			int result = dao.update("delete from tb_finishstore_in WHERE  id = ?", temp.getId());
			//更新成品库存表
			finishStoreStockService.reStock(temp.getOrderId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与成品出入库单有关的引用");
			}
			throw e;
		}
	}
	
}
