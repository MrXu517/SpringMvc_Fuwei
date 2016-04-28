package com.fuwei.service.producesystem;

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
import com.fuwei.entity.Order;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.entity.producesystem.FuliaoOut;
import com.fuwei.entity.producesystem.FuliaoOutDetail;
import com.fuwei.entity.producesystem.Location;
import com.fuwei.entity.producesystem.SelfFuliaoOut;
import com.fuwei.entity.producesystem.SelfFuliaoOutDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.DataCorrectRecordService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderDetailService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.util.DateTool;

@Component
public class SelfFuliaoOutService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(SelfFuliaoOutService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	SelfFuliaoOutDetailService selfFuliaoOutDetailService;
	@Autowired
	LocationService locationService;
	@Autowired
	DataCorrectRecordService dataCorrectRecordService;
	@Autowired
	FuliaoPurchaseOrderDetailService fuliaoPurchaseOrderDetailService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
	
	// 获取大货辅料入库列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			String orderNumber, Integer charge_employee,
			String number, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_selffuliaoout ");

			StringBuffer sql_condition = new StringBuffer();
			if (start_time != null) {// 出入库时间
				sql_condition.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " created_at<'"
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
					SelfFuliaoOut.class, pager);
			List<SelfFuliaoOut> list = (List<SelfFuliaoOut>)pager.getResult();
			if(list==null || list.size()<=0){
				return pager;
			}else{
				String ids = "";
				for(SelfFuliaoOut in : list){
					ids += in.getId()+ ",";
				}
				ids = ids.substring(0,ids.length()-1);
				String tempsql = "select * from tb_selffuliaoout_detail  where selfFuliaoOutId in (" + ids + ") ";
				List<SelfFuliaoOutDetail> totaldetaillist = dao.queryForBeanList(tempsql, SelfFuliaoOutDetail.class, null);
				Map<Integer,List<SelfFuliaoOutDetail>> map = new HashMap<Integer, List<SelfFuliaoOutDetail>>();
				for(SelfFuliaoOutDetail detail : totaldetaillist){
					int selfFuliaoOutId = detail.getSelfFuliaoOutId();
					if(map.containsKey(selfFuliaoOutId)){
						List<SelfFuliaoOutDetail> tempL = map.get(selfFuliaoOutId);
						tempL.add(detail);
						map.put(selfFuliaoOutId, tempL);
					}else{
						List<SelfFuliaoOutDetail> tempL = new ArrayList<SelfFuliaoOutDetail>();
						tempL.add(detail);
						map.put(selfFuliaoOutId, tempL);
					}
				}
				
				for(SelfFuliaoOut in : list){
					in.setDetaillist(map.get(in.getId()));
				}
			}
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	//获取某订单的自购辅料入库单
	public List<SelfFuliaoOut> getList(int orderId){
		return dao.queryForBeanList("select * from tb_selffuliaoout where orderId=? order by created_at desc", SelfFuliaoOut.class,orderId);
	}
	public List<SelfFuliaoOut> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_selffuliaoout where orderNumber=?", SelfFuliaoOut.class,orderNumber);
	}
	//获取某采购单的自购辅料入库单
	public List<SelfFuliaoOut> getByPurchase(int fuliaoPurchaseOrderId){
		return dao.queryForBeanList("select * from tb_selffuliaoout where fuliaoPurchaseOrderId=?", SelfFuliaoOut.class,fuliaoPurchaseOrderId);
	}
	//获取指定ID的多个自购辅料出库单
	public List<SelfFuliaoOut> getListByIds(String ids){
		List<SelfFuliaoOut> list = dao.queryForBeanList("select * from tb_selffuliaoout where id in("+ids+")", SelfFuliaoOut.class);
		String tempsql = "select * from tb_selffuliaoout_detail  where selfFuliaoOutId in (" + ids + ") ";
		List<SelfFuliaoOutDetail> totaldetaillist = dao.queryForBeanList(tempsql, SelfFuliaoOutDetail.class, null);
		Map<Integer,List<SelfFuliaoOutDetail>> map = new HashMap<Integer, List<SelfFuliaoOutDetail>>();
		for(SelfFuliaoOutDetail detail : totaldetaillist){
			int selffuliaooutId = detail.getSelfFuliaoOutId();
			if(map.containsKey(selffuliaooutId)){
				List<SelfFuliaoOutDetail> tempL = map.get(selffuliaooutId);
				tempL.add(detail);
				map.put(selffuliaooutId, tempL);
			}else{
				List<SelfFuliaoOutDetail> tempL = new ArrayList<SelfFuliaoOutDetail>();
				tempL.add(detail);
				map.put(selffuliaooutId, tempL);
			}
		}
		
		for(SelfFuliaoOut in : list){
			in.setDetaillist(map.get(in.getId()));
		}
		return list;
	}

	// 添加,返回主键
	@Transactional(rollbackFor=Exception.class)
	public int add(SelfFuliaoOut selfFuliaoOut) throws Exception {
		try {

			if (selfFuliaoOut.getFuliaoPurchaseOrderId() == 0) {
				throw new Exception("辅料采购单ID不能为空");
			} 
			if (selfFuliaoOut.getOrderNumber() == null || selfFuliaoOut.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				if(selfFuliaoOut.getDetaillist()==null || selfFuliaoOut.getDetaillist().size()<=0){
					throw new Exception("请至少填写一条出库明细");
				}
				selfFuliaoOut.setHas_tagprint(false);
				selfFuliaoOut.setHas_print(false);
				selfFuliaoOut.setIs_cleaning(false);
				selfFuliaoOut.setStatus(6);
				selfFuliaoOut.setState("执行完成");
				Integer selfFuliaoOutId = this.insert(selfFuliaoOut);
				selfFuliaoOut.setId(selfFuliaoOutId);
				selfFuliaoOut.setNumber(selfFuliaoOut.createNumber());
				this.update(selfFuliaoOut, "id", null);
				for(SelfFuliaoOutDetail detail : selfFuliaoOut.getDetaillist()){
					detail.setSelfFuliaoOutId(selfFuliaoOutId);
					//出库后，减数量  若出光，设置相应的库位为空
					locationService.deleteQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
				}
				selfFuliaoOutDetailService.addBatch(selfFuliaoOut.getDetaillist());
				
				return selfFuliaoOutId;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加清库存的辅料出库单,返回出库单主键
	@Transactional(rollbackFor=Exception.class)
	public int addByLocationId(Location location,int userId) throws Exception {
		try {
			int locationId = location.getId();
			if(location.getFuliaoPurchaseOrderDetailId()==null){
				return 0;
			}
			int fuliaoPurchaseOrderDetailId = location.getFuliaoPurchaseOrderDetailId();
			FuliaoPurchaseOrderDetail fuliaoPurchaseOrderDetail = fuliaoPurchaseOrderDetailService.get(fuliaoPurchaseOrderDetailId);
			FuliaoPurchaseOrder fuliaoPurchaseOrder = fuliaoPurchaseOrderService.get(fuliaoPurchaseOrderDetail.getFuliaoPurchaseOrderId());
			//1.添加辅料出库单
			SelfFuliaoOut selffuliaoOut = new SelfFuliaoOut();
			selffuliaoOut.setIs_cleaning(true);
			selffuliaoOut.setReceiver_employee(null);
			selffuliaoOut.setHas_print(false);
			selffuliaoOut.setHas_tagprint(false);
			selffuliaoOut.setCreated_at(DateTool.now());// 设置创建时间
			selffuliaoOut.setCreated_user(userId);// 设置创建人
			selffuliaoOut.setStatus(6);
			selffuliaoOut.setState("执行完成");
			selffuliaoOut.setDate(DateTool.now());//设置出库时间
			
			selffuliaoOut.setName(fuliaoPurchaseOrder.getName());
			selffuliaoOut.setCompany_productNumber(fuliaoPurchaseOrder.getCompany_productNumber());
			selffuliaoOut.setOrderId(fuliaoPurchaseOrder.getOrderId());
			selffuliaoOut.setOrderNumber(fuliaoPurchaseOrder.getOrderNumber());
			selffuliaoOut.setCharge_employee(fuliaoPurchaseOrder.getCharge_employee());
			selffuliaoOut.setCompanyId(fuliaoPurchaseOrder.getCompanyId());
			selffuliaoOut.setCustomerId(fuliaoPurchaseOrder.getCustomerId());
			selffuliaoOut.setFuliaoPurchaseOrderId(fuliaoPurchaseOrder.getId());
			selffuliaoOut.setFuliaoPurchaseOrder_number(fuliaoPurchaseOrder.getNumber());
			
			Integer newselffuliaoOutId = this.insert(selffuliaoOut);
			selffuliaoOut.setId(newselffuliaoOutId);
			selffuliaoOut.setNumber(selffuliaoOut.createNumber());
			this.update(selffuliaoOut, "id", null);
			
			//2.添加出库单明细
			List<SelfFuliaoOutDetail> detaillist = new ArrayList<SelfFuliaoOutDetail>();
			SelfFuliaoOutDetail detail = new SelfFuliaoOutDetail();
			
			detail.setFuliaoPurchaseOrderDetailId(fuliaoPurchaseOrderDetailId);
			detail.setSelfFuliaoOutId(newselffuliaoOutId);
			detail.setStyle(fuliaoPurchaseOrderDetail.getStyle());
			detail.setLocationId(locationId);
			detail.setMemo(fuliaoPurchaseOrderDetail.getMemo() + " [清空库存]");
			//清空库存，将出库数量设为库位的库存数量
			detail.setQuantity(location.getQuantity());
			
			detaillist.add(detail);
					
			//出库后，若库位内数量为0，则库位设为空库位
			locationService.deleteQuantity_purchase(detail.getLocationId(), detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			selfFuliaoOutDetailService.addBatch(detaillist);
			selffuliaoOut.setDetaillist(detaillist);
			return newselffuliaoOutId;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public SelfFuliaoOut get(int id) throws Exception {
		try {
			SelfFuliaoOut selfFuliaoOut = dao.queryForBean("select * from tb_selffuliaoout where id = ?", SelfFuliaoOut.class, id);
			return selfFuliaoOut;
		} catch (Exception e) {
			throw e;
		}
	}
	public SelfFuliaoOut getAndDetail(int id) throws Exception {
		try {
			SelfFuliaoOut selfFuliaoOut = dao.queryForBean("select * from tb_selffuliaoout where id = ?", SelfFuliaoOut.class, id);
			if(selfFuliaoOut!=null){
				List<SelfFuliaoOutDetail> detaillist = selfFuliaoOutDetailService.getList(selfFuliaoOut.getId());
				selfFuliaoOut.setDetaillist(detaillist);
			}
			return selfFuliaoOut;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public SelfFuliaoOut get(String number) throws Exception {
		try {
			SelfFuliaoOut selfFuliaoOut = dao.queryForBean("select * from tb_selffuliaoout where number = ?", SelfFuliaoOut.class, number);
			return selfFuliaoOut;
		} catch (Exception e) {
			throw e;
		}
	}
	//更新是否打印属性
	@Transactional
	public int updatePrint(SelfFuliaoOut object) throws Exception {
		// 更新表
		dao.update("update tb_selffuliaoout set has_print=? where id=?", object.getHas_print(),object.getId());

		return object.getId();
	}
	//更新是否打印辅料标签属性
	@Transactional
	public Boolean updatePrint_batch(List<SelfFuliaoOut> list) throws Exception {
		if(list == null || list.size()<=0){
			return true;
		}
		String sql = "update tb_selffuliaoout set has_print=? where id=?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (SelfFuliaoOut item : list) {
			batchArgs.add(new Object[] { 
					item.getHas_print(),item.getId()
			});
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//更新是否打印辅料标签属性
	@Transactional
	public int updateTagPrint(SelfFuliaoOut object) throws Exception {
		// 更新表
		dao.update("update tb_selffuliaoout set has_tagprint=? where id=?", object.getHas_tagprint(),object.getId());

		return object.getId();
	}
	//更新是否打印辅料标签属性
	@Transactional
	public Boolean updateTagPrint_batch(List<SelfFuliaoOut> list) throws Exception {
		if(list == null || list.size()<=0){
			return true;
		}
		String sql = "update tb_selffuliaoout set has_tagprint=? where id=?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (SelfFuliaoOut item : list) {
			batchArgs.add(new Object[] { 
					item.getHas_tagprint(),item.getId()
			});
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	// 数据纠正_删除
	@Transactional(rollbackFor=Exception.class)
	public int remove_datacorrect(SelfFuliaoOut object,DataCorrectRecord datacorrect) throws Exception {
		try {
			int id = object.getId();
			if(object.isDeletable()){
				throw new Exception("该辅料出库单未执行，无需进行数据纠正");
			}
			//1.修改库位库存信息
			List<SelfFuliaoOutDetail> detaillist = selfFuliaoOutDetailService.getList(id);
			for(SelfFuliaoOutDetail detail : detaillist){
				//出库单删除后，设置库位不为空
				locationService.addQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			}
			//3.添加数据纠正记录
			dataCorrectRecordService.add(datacorrect);
			
			return dao.update("delete from tb_selffuliaoout WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	// 删除
	@Transactional
	public int remove(int id) throws Exception {
		try {
			SelfFuliaoOut selfFuliaoOut = this.get(id);
			if(!selfFuliaoOut.isDeletable()){
				if(selfFuliaoOut.getStatus() == 6){
					throw new Exception("已执行出库，无法删除");
				}else if(selfFuliaoOut.getStatus() == -1){
					throw new Exception("已执行出库失败，无法删除");
				}
				
			}
			//1.修改库位库存信息
			List<SelfFuliaoOutDetail> detaillist = selfFuliaoOutDetailService.getList(id);
			for(SelfFuliaoOutDetail detail : detaillist){
				//出库单删除后，设置库位不为空
				locationService.addQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			}
			return dao.update("delete from tb_selffuliaoout WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	@Transactional
	public int remove(SelfFuliaoOut selfFuliaoOut) throws Exception {
		try {
			int id = selfFuliaoOut.getId();
			if(!selfFuliaoOut.isDeletable()){
				if(selfFuliaoOut.getStatus() == 6){
					throw new Exception("已执行出库，无法删除");
				}else if(selfFuliaoOut.getStatus() == -1){
					throw new Exception("已执行出库失败，无法删除");
				}
				
			}
			//1.修改库位库存信息
			List<SelfFuliaoOutDetail> detaillist = selfFuliaoOutDetailService.getList(id);
			for(SelfFuliaoOutDetail detail : detaillist){
				//入库单删除后，若库位内数量为0，则库位设为空库位
				locationService.addQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			}
			return dao.update("delete from tb_selffuliaoout WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	

	@Transactional
	public int complete(int id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_selffuliaoout SET status=?,state=? WHERE id = ?",
							6, "执行完成", id);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public int fail(int id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_selffuliaoout SET status=?,state=? WHERE id = ?",
							-1, "执行失败", id);
		} catch (Exception e) {
			throw e;
		}
	}
}
