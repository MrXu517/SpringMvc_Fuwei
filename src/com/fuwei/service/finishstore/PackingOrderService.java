package com.fuwei.service.finishstore;

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
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.entity.producesystem.FuliaoInNotice;
import com.fuwei.entity.producesystem.FuliaoInNoticeDetail;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class PackingOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(PackingOrderService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	PackingOrderDetailService packingOrderDetailService;

	//根据OrderId获取所有
	public List<PackingOrder> getListByOrder(int orderId) throws Exception{
		List<PackingOrder> packingOrderList = dao.queryForBeanList("select * from tb_packingorder where orderId=?", PackingOrder.class,orderId);
		if(packingOrderList!=null){
			for(PackingOrder item : packingOrderList){
				List<PackingOrderDetail> detaillist = packingOrderDetailService.getList(item.getId());
				item.setDetaillist(detaillist);
			}
		}
		return packingOrderList;
	}
//	public PackingOrder getByOrderAndDetail(int orderId) throws Exception {
//		try {
//			PackingOrder order = dao.queryForBean(
//					"select * from tb_packingorder where orderId = ?",
//					PackingOrder.class, orderId);
//			if(order == null){
//				return null;
//			}
//			List<PackingOrderDetail> detaillist = packingOrderDetailService.getList(order.getId());
//			order.setDetaillist(detaillist);
//			return order;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	//根据OrderId获取所有
	public List<PackingOrder> getListByOrderNumber(String orderNumber) throws Exception{
		List<PackingOrder> packingOrderList = dao.queryForBeanList("select * from tb_packingorder where orderNumber=?", PackingOrder.class,orderNumber);
		if(packingOrderList!=null){
			for(PackingOrder item : packingOrderList){
				List<PackingOrderDetail> detaillist = packingOrderDetailService.getList(item.getId());
				item.setDetaillist(detaillist);
			}
		}
		return packingOrderList;
	}
	
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId, String number,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";

			sql.append("select aT.*,img,img_s,img_ss from tb_packingorder aT , tb_order bT where aT.orderId=bT.id ");
			if (number != null && !number.equals("")) {
				sql.append(seq + " aT.orderNumber='"
						+ number + "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql.append(seq + " aT.created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " aT.created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (companyId != null) {
				sql.append(seq + " aT.companyId='" + companyId + "'");
				seq = " AND ";
			}
			

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return findPager_T(sql.toString(), PackingOrder.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加
	@Transactional(rollbackFor=Exception.class)
	public int add(PackingOrder packingOrder) throws Exception {
		try {
			if (packingOrder.getOrderId() == null) {
				throw new Exception("订单ID不能为空");
			}
			if(packingOrder.getDetaillist()==null || packingOrder.getDetaillist().size()<=0){
				throw new Exception("请至少填写一条装箱单明细");
			}
			//2016-5-4修改 一个订单可以有多个装箱单
//			PackingOrder temp = this.getByOrder(packingOrder.getOrderId());
//			if(temp!=null){
//				throw new Exception("每个订单只能有一个装箱单");
//			}
			packingOrder.setStatus(0);
			packingOrder.setState("新建");
			Integer packingOrderId = this.insert(packingOrder);
			packingOrder.setId(packingOrderId);
			packingOrder.setNumber(packingOrder.createNumber());
			this.update(packingOrder, "id", null);
			for(PackingOrderDetail detail : packingOrder.getDetaillist()){
				detail.setPackingOrderId(packingOrderId);
				detail.setOrderId(packingOrder.getOrderId());
			}
			packingOrderDetailService.addBatch(packingOrder.getDetaillist());
			return packingOrderId;
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑
	@Transactional(rollbackFor=Exception.class)
	public int update(PackingOrder packingOrder) throws Exception {
		try {
			if(packingOrder.getDetaillist()==null || packingOrder.getDetaillist().size()<=0){
				throw new Exception("请至少填写一条装箱单明细");
			}
			PackingOrder temp = this.getAndDetail(packingOrder.getId());
			if (!temp.isEdit()) {
				throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
			}
			// 更新表
			int packingOrderId = packingOrder.getId();
			//1.更新原有的id，删除删掉的id，添加新的id
			int old_ids[]=new int[temp.getDetaillist().size()];
			for(int i = 0 ; i <temp.getDetaillist().size();++i){
				PackingOrderDetail detail = temp.getDetaillist().get(i);
				old_ids[i] = detail.getId();
			}
			
			//更新的列表
			List<PackingOrderDetail>  to_updatelist = new ArrayList<PackingOrderDetail>();
			//新增的列表
			List<PackingOrderDetail>  to_addlist = new ArrayList<PackingOrderDetail>();
			//删除的装箱单明细ids
			List<Integer> to_deletelist = new ArrayList<Integer>();
			
			for(PackingOrderDetail detail : packingOrder.getDetaillist()){
				detail.setPackingOrderId(packingOrderId);
				detail.setOrderId(temp.getOrderId());
				if(detail.getId() > 0){
					to_updatelist.add(detail);
				}else{
					to_addlist.add(detail);
				}
			}
			
			for(Integer id : old_ids){
				boolean flag = false;
				for(PackingOrderDetail detail :to_updatelist){
					if(detail.getId() == id){
						flag = true;//表示这个id只是更新，不是删除
					}
				}
				if(!flag){//若是删除的id
					to_deletelist.add(id);
				}
			}
			//更新明细
			packingOrderDetailService.updateBatch(to_updatelist);
			//增加明细
			packingOrderDetailService.addBatch(to_addlist);
			//删除明细
			packingOrderDetailService.deleteBatch(to_deletelist);
			
			this.update(packingOrder, "id",
					"number,customerId,created_user,created_at,orderId,status,state,orderNumber,name,company_productNumber,charge_employee,companyId,customerId", false);

			return packingOrder.getId();
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取
	public PackingOrder getByOrder(int orderId) throws Exception {
		try {
			PackingOrder order = dao.queryForBean(
					"select * from tb_packingorder where orderId = ?",
					PackingOrder.class, orderId);
			if(order == null){
				return null;
			}
			return order;
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	// 获取
	public PackingOrder get(int id) throws Exception {
		try {
			PackingOrder packingOrder = dao.queryForBean(
					"select * from tb_packingorder where id = ?",
					PackingOrder.class, id);
			return packingOrder;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public PackingOrder getAndDetail(int id) throws Exception {
		try {
			PackingOrder packingOrder = dao.queryForBean("select * from tb_packingorder where id = ?", PackingOrder.class, id);
			if(packingOrder == null){
				return null;
			}
			List<PackingOrderDetail> detaillist = packingOrderDetailService.getList(id);
			packingOrder.setDetaillist(detaillist);
			return packingOrder;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_packingorder SET status=?,state=? WHERE orderId = ?",
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
					"UPDATE tb_packingorder SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	//获取所有
	public List<PackingOrder> getList(){
		return dao.queryForBeanList("select * from tb_packingorder", PackingOrder.class);
	}
	
	// 删除
	public int remove(int id) throws Exception {
		try {
			PackingOrder temp = this.get(id);
			if(!temp.deletable()){
				throw new Exception("单据已执行完成，无法删除 ");
			}
			return dao.update("delete from tb_packingorder WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与装箱单有关的引用");
			}
			throw e;
		}
	}
}


//package com.fuwei.service.finishstore;
//
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fuwei.commons.Pager;
//import com.fuwei.commons.Sort;
//import com.fuwei.entity.finishstore.PackingOrder;
//import com.fuwei.service.BaseService;
//import com.fuwei.util.DateTool;
//
//@Component
//public class PackingOrderService extends BaseService {
//	private Logger log = org.apache.log4j.LogManager
//			.getLogger(PackingOrderService.class);
//	@Autowired
//	JdbcTemplate jdbc;
//
//	//根据OrderId获取所有
//	public List<PackingOrder> getListByOrder(int orderId){
//		return dao.queryForBeanList("select * from tb_packingorder where orderId=?", PackingOrder.class,orderId);
//	}
//	
//	// 获取列表
//	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId, String number,
//			List<Sort> sortlist) throws Exception {
//		try {
//			StringBuffer sql = new StringBuffer();
//			String seq = " AND ";
//
//			sql.append("select aT.*,companyId,salesmanId,customerId,sampleId,name,img,materialId,weight,size,productNumber,orderNumber,img_s,img_ss,charge_employee,company_productNumber from tb_packingorder aT , tb_order bT where aT.orderId=bT.id ");
//			if (number != null && !number.equals("")) {
//				sql.append(seq + " orderNumber='"
//						+ number + "'");
//				seq = " AND ";
//			}
//			if (start_time != null) {
//				sql.append(seq + " aT.created_at>='"
//						+ DateTool.formateDate(start_time) + "'");
//				seq = " AND ";
//			}
//			if (end_time != null) {
//				sql.append(seq + " aT.created_at<'"
//						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
//						+ "'");
//				seq = " AND ";
//			}
//			if (companyId != null) {
//				sql.append(seq + " companyId='" + companyId + "'");
//				seq = " AND ";
//			}
//			
//
//			if (sortlist != null && sortlist.size() > 0) {
//
//				for (int i = 0; i < sortlist.size(); ++i) {
//					if (i == 0) {
//						sql.append(" order by " + sortlist.get(i).getProperty()
//								+ " " + sortlist.get(i).getDirection() + " ");
//					} else {
//						sql.append("," + sortlist.get(i).getProperty() + " "
//								+ sortlist.get(i).getDirection() + " ");
//					}
//
//				}
//			}
//			return findPager_T(sql.toString(), PackingOrder.class, pager);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	// 添加
//	@Transactional
//	public int add(PackingOrder packingOrder) throws Exception {
//		try {
//			if (packingOrder.getOrderId() == null) {
//				throw new Exception("订单ID不能为空");
//			}
//			if (packingOrder.getFilepath() == null) {
//				throw new Exception("装箱单EXCEL文件路径不能为空");
//			}
//			if (packingOrder.getPdfpath() == null) {
//				throw new Exception("装箱单PDF文件路径不能为空");
//			}
//			packingOrder.setStatus(0);
//			packingOrder.setState("新建");
//			Integer id = this.insert(packingOrder);
//			packingOrder.setId(id);
//			return id;
//		} catch (Exception e) {
//
//			throw e;
//		}
//	}
//
//	// 编辑
//	@Transactional
//	public int update(PackingOrder packingOrder) throws Exception {
//		try {
//			if (packingOrder.getOrderId() == null) {
//				throw new Exception("订单ID不能为空");
//			}
//			if (packingOrder.getFilepath() == null) {
//				throw new Exception("装箱单EXCEL文件路径不能为空");
//			}
//			if (packingOrder.getPdfpath() == null) {
//				throw new Exception("装箱单PDF文件路径不能为空");
//			}
//			PackingOrder temp = this.get(packingOrder.getId());
//			if (!temp.isEdit()) {
//				throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
//			}
//			// 更新表
//			this.update(packingOrder, "id",
//					"created_user,created_at,orderId", true);
//
//			return packingOrder.getId();
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}
//
//	// 获取
//	public PackingOrder getByOrder(int orderId) throws Exception {
//		try {
//			PackingOrder order = dao.queryForBean(
//					"select * from tb_packingorder where orderId = ?",
//					PackingOrder.class, orderId);
//			return order;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	// 获取
//	public PackingOrder get(int id) throws Exception {
//		try {
//			PackingOrder packingOrder = dao.queryForBean(
//					"select * from tb_packingorder where id = ?",
//					PackingOrder.class, id);
//			return packingOrder;
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
//							"UPDATE tb_packingorder SET status=?,state=? WHERE orderId = ?",
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
//			return dao.update(
//					"UPDATE tb_packingorder SET status=?,state=? WHERE id = ?",
//					status, state, tableOrderId);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	//获取所有
//	public List<PackingOrder> getList(){
//		return dao.queryForBeanList("select * from tb_packingorder", PackingOrder.class);
//	}
//	
//	// 删除
//	public int remove(int id) throws Exception {
//		try {
//			PackingOrder temp = this.get(id);
//			if(!temp.deletable()){
//				throw new Exception("单据已执行完成，无法删除 ");
//			}
//			return dao.update("delete from tb_packingorder WHERE  id = ?", id);
//		} catch (Exception e) {
//			SQLException sqlException = (java.sql.SQLException) e.getCause();
//			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
//				log.error(e);
//				throw new Exception("已被引用，无法删除，请先删除与装箱单有关的引用");
//			}
//			throw e;
//		}
//	}
//}
