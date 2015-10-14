package com.fuwei.service.ordergrid;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.entity.Factory;
import com.fuwei.entity.Material;
import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrder;
import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class FuliaoPurchaseOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoPurchaseOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加辅料采购单
	@Transactional
	public int add(FuliaoPurchaseOrder tableOrder) throws Exception {
		try {
			if (tableOrder.getDetaillist() == null
					|| tableOrder.getDetaillist().size() <= 0) {
				throw new Exception("辅料采购单中至少得有一条采购记录");
			} else {
				tableOrder.setStatus(0);
				tableOrder.setState("新建");
				tableOrder.setDetail_json(SerializeTool.serialize(tableOrder
						.getDetaillist()));

				Integer tableOrderId = this.insert(tableOrder);
				
				tableOrder.setId(tableOrderId);
				tableOrder.setNumber(tableOrder.createNumber());
				this.update(tableOrder, "id", null);

				return tableOrderId;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑辅料采购单
	@Transactional
	public int update(FuliaoPurchaseOrder tableOrder) throws Exception {
		try {
			if (tableOrder.getDetaillist() == null
					|| tableOrder.getDetaillist().size() <= 0) {
				throw new Exception("辅料采购单中至少得有一条采购记录");
			} else {
				FuliaoPurchaseOrder temp = this.get(tableOrder.getId());
				if (!temp.isEdit()) {
					throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
				}
				String details = SerializeTool.serialize(tableOrder
						.getDetaillist());
				tableOrder.setDetail_json(details);

				// 更新表
				this.update(tableOrder, "id",
						"created_user,created_at,orderId,number", true);

				return tableOrder.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取辅料采购单
	public List<FuliaoPurchaseOrder> getByOrder(int orderId) throws Exception {
		try {
			List<FuliaoPurchaseOrder> order = dao.queryForBeanList(
					"select * from tb_fuliaopurchaseorder where orderId = ?",
					FuliaoPurchaseOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取辅料采购单
	public FuliaoPurchaseOrder get(int id) throws Exception {
		try {
			FuliaoPurchaseOrder order = dao.queryForBean(
					"select * from tb_fuliaopurchaseorder where id = ?",
					FuliaoPurchaseOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取辅料采购单列表
	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId, Integer factoryId,String number,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_fuliaopurchaseorder ");

			if (number != null && !number.equals("")) {
				sql.append(seq + " number='"
						+ number + "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (companyId != null) {
				sql.append(seq + " companyId='" + companyId + "'");
				seq = " AND ";
			}
			if (factoryId != null) {
				sql.append(seq + " factoryId='" + factoryId + "'");
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
			return findPager_T(sql.toString(), FuliaoPurchaseOrder.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除辅料采购单
	public int remove(int id) throws Exception {
		try {
			FuliaoPurchaseOrder temp = this.get(id);
			if(!temp.deletable()){
				throw new Exception("单据已执行完成，无法删除 ");
			}
			return dao.update(
					"delete from tb_fuliaopurchaseorder WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与辅料采购单有关的引用");
			}
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_fuliaopurchaseorder SET status=?,state=? WHERE orderId = ?",
							6, "执行完成", orderId);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int updateStatus(int tableOrderId, int status, String state)
			throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_fuliaopurchaseorder SET status=?,state=? WHERE id = ?",
							status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	
	
	// 获取辅料采购报表数据
	public HashMap<Factory,HashMap<Material,Double> > fuliao_purchase_report(Date start_time, Date end_time, Integer factoryId,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_fuliaopurchaseorder ");
			
			if (start_time != null) {
				sql.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (factoryId != null) {
				sql.append(seq + " factoryId='" + factoryId + "'");
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
			 
			List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = dao.queryForBeanList(
					sql.toString(), FuliaoPurchaseOrder.class);
			
			HashMap<Integer,HashMap<Integer,Double> > temp_hashmap = new HashMap<Integer,HashMap<Integer,Double> >();
			
			for(FuliaoPurchaseOrder fuliaoPurchaseOrder : fuliaoPurchaseOrderList){
				if(fuliaoPurchaseOrder.getDetail_json() == null || fuliaoPurchaseOrder.getDetail_json().equals("")){
					continue;
				}
				
				List<FuliaoPurchaseOrderDetail> detailList = SerializeTool
				.deserializeList(fuliaoPurchaseOrder.getDetail_json(),
						FuliaoPurchaseOrderDetail.class);;
				
				Integer temp_factoryId = fuliaoPurchaseOrder.getFactoryId();
				if(temp_factoryId == null){
					continue;
				}
				for(FuliaoPurchaseOrderDetail detail : detailList){
					Integer materialId = detail.getStyle();
					if(materialId==null){
						continue;
					}
					Double quantity = detail.getQuantity();
					
					if(temp_hashmap.containsKey(temp_factoryId)){
						HashMap<Integer,Double> factoryTemp = temp_hashmap.get(temp_factoryId);
						if(factoryTemp.containsKey(materialId)){
							temp_hashmap.get(temp_factoryId).put(materialId, quantity + temp_hashmap.get(temp_factoryId).get(materialId));
						}else{
							temp_hashmap.get(temp_factoryId).put(materialId, quantity);
						}
						
					}else{
						HashMap<Integer,Double> temp = new HashMap<Integer,Double>();
						temp.put(materialId,quantity);
						temp_hashmap.put(fuliaoPurchaseOrder.getFactoryId(), temp);
					}
				}
			}
		
			
			
			HashMap<Factory,HashMap<Material,Double> > result = new HashMap<Factory,HashMap<Material,Double> >();
			if(factoryId == null){
				for(Factory factory : SystemCache.purchase_factorylist){
					result.put(factory, new HashMap<Material,Double>());
					for(Material material : SystemCache.materiallist){	
						if(temp_hashmap.containsKey(factory.getId()) && temp_hashmap.get(factory.getId()).containsKey(material.getId())){
							result.get(factory).put(material,temp_hashmap.get(factory.getId()).get(material.getId()));
						}
					}
				}
			}else{
				Factory factory = SystemCache.getFactory(factoryId);
					result.put(factory, new HashMap<Material,Double>());
					for(Material material : SystemCache.materiallist){	
						if(temp_hashmap.containsKey(factory.getId()) && temp_hashmap.get(factory.getId()).containsKey(material.getId())){
							result.get(factory).put(material,temp_hashmap.get(factory.getId()).get(material.getId()));
						}
					}
			}
			
			
			return result;
			
		} catch (Exception e) {
			throw e;
		}
	}

	
	//原材料采购明细报表
	public List<FuliaoPurchaseOrder> fuliao_purchase_detail_report(Date start_time, Date end_time, Integer factoryId,List<Sort> sortlist) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_fuliaopurchaseorder ");
			
			if (start_time != null) {
				sql.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (factoryId != null) {
				sql.append(seq + " factoryId='" + factoryId + "'");
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
			
			
			List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = dao.queryForBeanList(
					sql.toString(), FuliaoPurchaseOrder.class);
			
			
          
			return fuliaoPurchaseOrderList;
			
		} catch (Exception e) {
			throw e;
		}
	}
}
