package com.fuwei.service.finishstore;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.fuwei.commons.SystemCache;
import com.fuwei.entity.Order;
import com.fuwei.entity.finishstore.FinishInOut;
import com.fuwei.entity.finishstore.FinishStoreIn;
import com.fuwei.entity.finishstore.FinishStoreInDetail;
import com.fuwei.entity.finishstore.FinishStoreReturn;
import com.fuwei.entity.finishstore.FinishStoreReturnDetail;
import com.fuwei.entity.finishstore.FinishStoreStock;
import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.ordergrid.GongxuProducingOrder;
import com.fuwei.entity.ordergrid.GongxuProducingOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.producesystem.HalfCurrentStock;
import com.fuwei.entity.producesystem.HalfCurrentStockDetail;
import com.fuwei.entity.producesystem.HalfInOut;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.entity.producesystem.HalfStoreInOutDetail;
import com.fuwei.entity.producesystem.HalfStoreReturn;
import com.fuwei.entity.producesystem.HalfStoreReturnDetail;
import com.fuwei.service.BaseService;
import com.fuwei.util.SerializeTool;

@Component
public class FinishStoreStockService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FinishStoreStockService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FinishStoreInService finishStoreInOutService;
	@Autowired
	FinishStoreStockDetailService finishStoreStockDetailService;
	@Autowired
	FinishStoreReturnService finishStoreReturnService;
	
	// 获取列表
	public Pager getList(Pager pager,Integer companyId,Integer charge_employee,String orderNumber,Boolean not_zero, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select a.*,b.companyId,b.name,b.img,b.img_s,b.img_ss,b.orderNumber,b.charge_employee,b.company_productNumber from tb_finishstorestock a, tb_order b where a.orderId=b.id ");

			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " b.companyId='" + companyId + "'");
				seq = " AND ";
			}
			if (charge_employee != null) {
				sql_condition.append(seq + " b.charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (orderNumber != null && !orderNumber.equals("")) {
				sql_condition.append(seq + " b.orderNumber='" + orderNumber + "'");
				seq = " AND ";
			}
			if (not_zero != null) {
				sql_condition.append(seq + " a.total_stock_quantity>0 ");
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
					FinishStoreStock.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//获取某订单的半成品出入库记录
	// 获取
	public List<FinishInOut> halfDetail(int orderId)
			throws Exception {
		try {
			List<FinishInOut> list = dao.queryForBeanList("select * from (select created_at,created_user,'store' as type ,id ,number,orderId,orderNumber,date,sign,has_print,in_out,memo  from tb_finishstore_in_out where orderId = ? union all select created_at,created_user,'return' as type,id ,number, orderId,orderNumber,date,sign,has_print,null,memo  from tb_finishstore_return where orderId = ?)  c order by date desc,created_at desc",
					FinishInOut.class,orderId,orderId);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	//创建
	@Transactional(rollbackFor=Exception.class)
	public int add(FinishStoreStock object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("明细不可为空");
			} else {
				//获取total_stock_quantity
				int total_stock_quantity = 0 ;
				for(FinishStoreStockDetail detail : object.getDetaillist()){
					total_stock_quantity += detail.getStock_quantity();
				}
				object.setTotal_stock_quantity(total_stock_quantity);
				Integer id = this.insert(object);
				object.setId(id);
				for(FinishStoreStockDetail detail : object.getDetaillist()){
					detail.setFinishStoreStockId(id);
				}
				finishStoreStockDetailService.addBatch(object.getDetaillist());
				return id;
			}
		} catch (Exception e) {

			throw e;
		}
	}
	
	//编辑
	@Transactional(rollbackFor=Exception.class)
	public int update(FinishStoreStock object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("明细不可为空");
			} else {
				finishStoreStockDetailService.deleteBatch(object.getId());
				//获取total_stock_quantity
				int total_stock_quantity = 0 ;
				for(FinishStoreStockDetail detail : object.getDetaillist()){
					total_stock_quantity += detail.getStock_quantity();
					detail.setFinishStoreStockId(object.getId());
				}
				object.setTotal_stock_quantity(total_stock_quantity);
				
				finishStoreStockDetailService.addBatch(object.getDetaillist());
				// 更新表
				this.update(object, "id","orderId", true);
				
				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取
	public FinishStoreStock get(int orderId) throws Exception {
		try {
			FinishStoreStock object = dao.queryForBean(
					"select * from tb_finishstorestock where orderId = ?",
					FinishStoreStock.class, orderId);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public FinishStoreStock getAndDetail(int orderId) throws Exception {
		try {
			FinishStoreStock object = dao.queryForBean(
					"select * from tb_finishstorestock where orderId = ?",
					FinishStoreStock.class, orderId);
			if(object == null){
				return null;
			}
			List<FinishStoreStockDetail> detaillist = finishStoreStockDetailService.getList(object.getId());
			object.setDetaillist(detaillist);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//重新计算某订单的成品库存
	@Transactional
	public void reStock(int orderId) throws Exception{
		FinishStoreStock finishStoreStock = this.get(orderId);
		List<FinishStoreStockDetail> detaillist = new ArrayList<FinishStoreStockDetail>();
		List<Map<String,Object>> stockMap = this.getByOrder(orderId);
		//获取当前库存列表
		
		int total_stock_quantity = 0;
		int total_stock_cartons = 0;
		for(Map<String,Object> item: stockMap){
			int in_quantity = Integer.valueOf(item.get("in_quantity").toString());
			int plan_quantity = Integer.valueOf(item.get("plan_quantity").toString());
			int out_quantity = Integer.valueOf(item.get("out_quantity").toString());
			int return_quantity = Integer.valueOf(item.get("return_quantity").toString());
			int stock_quantity = Integer.valueOf(item.get("stock_quantity").toString());
			int in_cartons = Integer.valueOf(item.get("in_cartons").toString());
			int out_cartons = Integer.valueOf(item.get("out_cartons").toString());
			int plan_cartons = Integer.valueOf(item.get("plan_cartons").toString());
			int return_cartons = Integer.valueOf(item.get("return_cartons").toString());
			int stock_cartons = Integer.valueOf(item.get("stock_cartons").toString());
			
			int packingOrderDetailId = Integer.valueOf(item.get("packingOrderDetailId").toString());
			FinishStoreStockDetail detail = new FinishStoreStockDetail();
			detail.setPackingOrderDetailId(packingOrderDetailId);
			detail.setStock_quantity(stock_quantity);
			detail.setStock_cartons(stock_cartons);
			detail.setPlan_quantity(plan_quantity);
			detail.setPlan_cartons(plan_cartons);
			detail.setIn_quantity(in_quantity);
			detail.setIn_cartons(in_cartons);
			detail.setOut_quantity(out_quantity);
			detail.setOut_cartons(out_cartons);
			detail.setReturn_quantity(return_quantity);
			detail.setReturn_cartons(return_cartons);
			detaillist.add(detail);

			total_stock_quantity +=stock_quantity;
			total_stock_cartons += stock_cartons;
		}
		
		
		if(finishStoreStock == null){//添加一个
			finishStoreStock = new FinishStoreStock();
			finishStoreStock.setOrderId(orderId);
			finishStoreStock.setDetaillist(detaillist);
			finishStoreStock.setTotal_stock_cartons(total_stock_cartons);
			finishStoreStock.setTotal_stock_quantity(total_stock_quantity);
			if(detaillist.size()>0){
				this.add(finishStoreStock);
			}
		}
		else{//编辑
			finishStoreStock.setTotal_stock_cartons(total_stock_cartons);
			finishStoreStock.setTotal_stock_quantity(total_stock_quantity);
			finishStoreStock.setDetaillist(detaillist);
			if(detaillist.size()<=0){
				this.remove(finishStoreStock);
			}else{
				this.update(finishStoreStock);
			}
		}
	}
	
	// 删除
	public int remove(FinishStoreStock temp) throws Exception {
		try {
			int result = dao.update("delete from tb_finishstorestock WHERE  id = ?", temp.getId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与成品库存有关的引用");
			}
			throw e;
		}
	}

	//获取某订单的成品当前库存数量，只有数量
	public List<Map<String,Object>> getByOrder(int orderId){
		List<Map<String,Object>> in_map = dao.queryForListMap("select f.* ,IFNULL(newtable.in_quantity,0) as in_quantity ,IFNULL(newtable.in_cartons,0) as in_cartons  from tb_packingorder_detail f left join (select sum(quantity) in_quantity, sum(cartons) in_cartons ,packingOrderDetailId from tb_finishstore_in_detail group by packingOrderDetailId)  newtable on f.id = newtable.packingOrderDetailId where  f.orderId =?",orderId);
		List<Map<String,Object>> out_map = dao.queryForListMap("select sum(a.quantity) out_quantity,sum(a.cartons) out_cartons,packingOrderDetailId from tb_finishstore_out_detail a ,tb_packingorder_detail b where a.packingOrderDetailId=b.id and b.orderId=? group by packingOrderDetailId", orderId);
		List<Map<String,Object>> in_return_map = dao.queryForListMap("select sum(a.quantity) return_quantity,sum(a.cartons) return_cartons,packingOrderDetailId from tb_finishstore_return_detail a ,tb_packingorder_detail b where a.packingOrderDetailId=b.id and b.orderId=? group by packingOrderDetailId", orderId);
	
		for(Map<String,Object> item : in_map){
			int packingOrderDetailId = (Integer)item.get("id");
			item.put("packingOrderDetailId",packingOrderDetailId);
			int plan_quantity = (Integer)item.get("quantity");
			int plan_cartons = (Integer)item.get("cartons");
			item.put("plan_quantity",plan_quantity);
			item.put("plan_cartons",plan_cartons);
			
			int in_quantity = 0;
			if(item.get("in_quantity")!=null){
				in_quantity = Integer.valueOf(item.get("in_quantity").toString());
			}
			item.put("in_quantity",in_quantity);
			
			int in_cartons = 0;
			if(item.get("in_cartons")!=null){
				in_cartons = Integer.valueOf(item.get("in_cartons").toString());
			}
			item.put("in_cartons",in_cartons);
			
			int out_quantity = 0;
			int out_cartons = 0;
			for(Map<String,Object> temp_item : out_map){
				int temppackingOrderDetailId = (Integer)temp_item.get("packingOrderDetailId");
				if(temppackingOrderDetailId == packingOrderDetailId){
					out_quantity = Integer.valueOf(temp_item.get("out_quantity").toString());
					out_cartons = Integer.valueOf(temp_item.get("out_cartons").toString());
				}
			}
			item.put("out_quantity",out_quantity);
			item.put("out_cartons",out_cartons);
			
			int return_quantity = 0;
			int return_cartons = 0;
			for(Map<String,Object> temp_item : in_return_map){
				int temppackingOrderDetailId = (Integer)temp_item.get("packingOrderDetailId");
				if(temppackingOrderDetailId == packingOrderDetailId){
					return_quantity = Integer.valueOf(temp_item.get("return_quantity").toString());
					return_cartons = Integer.valueOf(temp_item.get("return_cartons").toString());
				}
			}
			item.put("return_quantity",return_quantity);
			item.put("return_cartons",return_cartons);
			
			int stock_quantity = in_quantity - out_quantity - return_quantity; //当前库存
			int stock_cartons = in_cartons - out_cartons - return_cartons;
			item.put("stock_quantity",stock_quantity);
			item.put("stock_cartons",stock_cartons);
		}
		return in_map;
	}
	
	//获取某订单的成品当前库存数量，只有数量
	public Map<Integer,FinishStoreStockDetail> getStockMapByOrder(int orderId) throws Exception{
		Map<Integer,FinishStoreStockDetail> result = new HashMap<Integer, FinishStoreStockDetail>();
		//1、查看这个订单是否已有库存数据， 若有，则直接获取
		FinishStoreStock finishStoreStock = this.getAndDetail(orderId);
		//2、若没有，则需要通过sql语句得到数据
		if(finishStoreStock == null){
			List<Map<String,Object>> in_map = dao.queryForListMap("select f.id as packingOrderDetailId, IFNULL(newtable.in_quantity,0) as in_quantity ,IFNULL(newtable.in_cartons,0) as in_cartons  from tb_packingorder_detail f left join (select sum(quantity) in_quantity, sum(cartons) in_cartons ,packingOrderDetailId from tb_finishstore_in_detail group by packingOrderDetailId)  newtable on f.id = newtable.packingOrderDetailId where  f.orderId =?",orderId);
			List<Map<String,Object>> out_map = dao.queryForListMap("select sum(a.quantity) out_quantity,sum(a.cartons) out_cartons,packingOrderDetailId from tb_finishstore_out_detail a ,tb_packingorder_detail b where a.packingOrderDetailId=b.id and b.orderId=? group by packingOrderDetailId", orderId);
			List<Map<String,Object>> in_return_map = dao.queryForListMap("select sum(a.quantity) return_quantity,sum(a.cartons) return_cartons,packingOrderDetailId from tb_finishstore_return_detail a ,tb_packingorder_detail b where a.packingOrderDetailId=b.id and b.orderId=? group by packingOrderDetailId", orderId);
			for(Map<String,Object> item : in_map){
				FinishStoreStockDetail detail = new FinishStoreStockDetail();
				
				int packingOrderDetailId = (Integer)item.get("packingOrderDetailId");
				int plan_quantity = (Integer)item.get("quantity");
				int plan_cartons = (Integer)item.get("cartons");
				int in_quantity = 0;
				if(item.get("in_quantity")!=null){
					in_quantity = Integer.valueOf(item.get("in_quantity").toString());
				}				
				int in_cartons = 0;
				if(item.get("in_cartons")!=null){
					in_cartons = Integer.valueOf(item.get("in_cartons").toString());
				}
				int out_quantity = 0;
				int out_cartons = 0;
				for(Map<String,Object> temp_item : out_map){
					int temppackingOrderDetailId = (Integer)temp_item.get("packingOrderDetailId");
					if(temppackingOrderDetailId == packingOrderDetailId){
						out_quantity = Integer.valueOf(temp_item.get("out_quantity").toString());
						out_cartons = Integer.valueOf(temp_item.get("out_cartons").toString());
					}
				}
				int return_quantity = 0;
				int return_cartons = 0;
				for(Map<String,Object> temp_item : in_return_map){
					int temppackingOrderDetailId = (Integer)temp_item.get("packingOrderDetailId");
					if(temppackingOrderDetailId == packingOrderDetailId){
						return_quantity = Integer.valueOf(temp_item.get("return_quantity").toString());
						return_cartons = Integer.valueOf(temp_item.get("return_cartons").toString());
					}
				}
				int stock_quantity = in_quantity - out_quantity - return_quantity; //当前库存
				int stock_cartons = in_cartons - out_cartons - return_cartons;
				
				detail.setPackingOrderDetailId(packingOrderDetailId);
				detail.setStock_quantity(stock_quantity);
				detail.setStock_cartons(stock_cartons);
				detail.setPlan_quantity(plan_quantity);
				detail.setPlan_cartons(plan_cartons);
				detail.setIn_quantity(in_quantity);
				detail.setIn_cartons(in_cartons);
				detail.setOut_quantity(out_quantity);
				detail.setOut_cartons(out_cartons);
				detail.setReturn_quantity(return_quantity);
				detail.setReturn_cartons(return_cartons);
				result.put(packingOrderDetailId, detail);
				
			}
		}else{
			if(finishStoreStock.getDetaillist()!=null){
				for(FinishStoreStockDetail detail : finishStoreStock.getDetaillist()){
					int packingOrderDetailId = detail.getPackingOrderDetailId();
					result.put(packingOrderDetailId, detail);
				}
			}
		}
		return result;
	}
}
