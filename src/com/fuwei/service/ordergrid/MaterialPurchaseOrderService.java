package com.fuwei.service.ordergrid;

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
import com.fuwei.commons.SystemCache;
import com.fuwei.entity.Factory;
import com.fuwei.entity.Material;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;
import java.util.HashMap;
@Component
public class MaterialPurchaseOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(MaterialPurchaseOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加原材料采购单
	@Transactional
	public int add(MaterialPurchaseOrder tableOrder) throws Exception {
		try {
			if (tableOrder.getDetaillist() == null
					|| tableOrder.getDetaillist().size() <= 0) {
				throw new Exception("原材料采购单中至少得有一条采购记录");
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

	// 编辑原材料采购单
	@Transactional
	public int update(MaterialPurchaseOrder tableOrder) throws Exception {
		try {
			if (tableOrder.getDetaillist() == null
					|| tableOrder.getDetaillist().size() <= 0) {
				throw new Exception("原材料采购单中至少得有一条采购记录");
			} else {
				MaterialPurchaseOrder temp = this.get(tableOrder.getId());
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

	// 获取原材料采购单
	public List<MaterialPurchaseOrder> getByOrder(int orderId) throws Exception {
		try {
			List<MaterialPurchaseOrder> order = dao.queryForBeanList(
					"select * from tb_materialpurchaseorder where orderId = ?",
					MaterialPurchaseOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取原材料采购单
	public MaterialPurchaseOrder get(int id) throws Exception {
		try {
			MaterialPurchaseOrder order = dao.queryForBean(
					"select * from tb_materialpurchaseorder where id = ?",
					MaterialPurchaseOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取采购单列表
	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId, Integer factoryId,String number,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_materialpurchaseorder ");
			
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
			return findPager_T(sql.toString(), MaterialPurchaseOrder.class,
					pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除原材料采购单
	public int remove(int id) throws Exception {
		try {
			MaterialPurchaseOrder temp = this.get(id);
			if(!temp.deletable()){
				throw new Exception("单据已执行完成，无法删除 ");
			}
			return dao.update(
					"delete from tb_materialpurchaseorder WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与原材料采购单有关的引用");
			}
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_materialpurchaseorder SET status=?,state=? WHERE orderId = ?",
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
							"UPDATE tb_materialpurchaseorder SET status=?,state=? WHERE id = ?",
							status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	
	// 获取原材料采购报表数据
	public HashMap<Factory,HashMap<Material,Double> > material_purchase_report(Date start_time, Date end_time, Integer factoryId,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_materialpurchaseorder ");
			
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
			
			List<MaterialPurchaseOrder> materialPurchaseOrderList = dao.queryForBeanList(
					sql.toString(), MaterialPurchaseOrder.class);
			
			HashMap<Integer,HashMap<Integer,Double> > temp_hashmap = new HashMap<Integer,HashMap<Integer,Double> >();
			
			for(MaterialPurchaseOrder materialPurchaseOrder : materialPurchaseOrderList){
				if(materialPurchaseOrder.getDetail_json() == null || materialPurchaseOrder.getDetail_json().equals("")){
					continue;
				}
				
				List<MaterialPurchaseOrderDetail> detailList = SerializeTool
				.deserializeList(materialPurchaseOrder.getDetail_json(),
						MaterialPurchaseOrderDetail.class);;
				
				Integer temp_factoryId = materialPurchaseOrder.getFactoryId();
				if(temp_factoryId == null){
					continue;
				}
				for(MaterialPurchaseOrderDetail detail : detailList){
					Integer materialId = detail.getMaterial();
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
						temp_hashmap.put(materialPurchaseOrder.getFactoryId(), temp);
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
	public List<MaterialPurchaseOrder> material_purchase_detail_report(Date start_time, Date end_time, Integer factoryId,List<Sort> sortlist) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_materialpurchaseorder ");
			
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
			
			
			List<MaterialPurchaseOrder> materialPurchaseOrderList = dao.queryForBeanList(
					sql.toString(), MaterialPurchaseOrder.class);
			
			
          
			return materialPurchaseOrderList;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取白胚报表数据 //Map<染厂Id,Map<材料ID，数量和>>
	//参数：开始时间、结束时间、染厂ID，排序顺序
	public HashMap<Integer,HashMap<Integer,Double> > muslin(Date start_time, Date end_time, Integer factoryId,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";

			sql.append("select * from tb_materialpurchaseorder ");
			
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
//			if (factoryId != null) {
//				sql.append(seq + " factoryId='" + factoryId + "'");
//				seq = " AND ";
//			}
		
			
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
			
			List<MaterialPurchaseOrder> materialPurchaseOrderList = dao.queryForBeanList(
					sql.toString(), MaterialPurchaseOrder.class);
			//Map<染厂Id,Map<材料ID，数量和>>
			HashMap<Integer,HashMap<Integer,Double> > resultMap = new HashMap<Integer,HashMap<Integer,Double> >();
			
			for(MaterialPurchaseOrder materialPurchaseOrder : materialPurchaseOrderList){
				if(materialPurchaseOrder.getDetail_json() == null || materialPurchaseOrder.getDetail_json().equals("")){
					continue;
				}
				
				List<MaterialPurchaseOrderDetail> detailList = SerializeTool
				.deserializeList(materialPurchaseOrder.getDetail_json(),
						MaterialPurchaseOrderDetail.class);;
				
				
				for(MaterialPurchaseOrderDetail detail : detailList){
					Integer temp_coloring_factoryId = detail.getFactoryId();
					if(temp_coloring_factoryId == null){
						continue;
					}
					Integer materialId = detail.getMaterial();
					if(materialId==null){
						continue;
					}
					if(factoryId!=null && factoryId!=temp_coloring_factoryId){
						continue;
					}
					Double quantity = detail.getQuantity();
					
					if(resultMap.containsKey(temp_coloring_factoryId)){
						HashMap<Integer,Double> factoryTemp = resultMap.get(temp_coloring_factoryId);
						if(factoryTemp.containsKey(materialId)){
							resultMap.get(temp_coloring_factoryId).put(materialId, quantity + resultMap.get(temp_coloring_factoryId).get(materialId));
						}else{
							resultMap.get(temp_coloring_factoryId).put(materialId, quantity);
						}
						
					}else{
						HashMap<Integer,Double> temp = new HashMap<Integer,Double>();
						temp.put(materialId,quantity);
						resultMap.put(temp_coloring_factoryId, temp);
					}
				}
			}
		
			if(factoryId == null){
				for(Factory factory : SystemCache.coloring_factorylist){
					if(!resultMap.containsKey(factory.getId())){
						resultMap.put(factory.getId(), new HashMap<Integer,Double>());
					}
					
				}
			}
			return resultMap;
			
		} catch (Exception e) {
			throw e;
		}
	}
}
