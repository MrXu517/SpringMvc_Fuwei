package com.fuwei.service.producesystem;

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
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.producesystem.HalfCurrentStock;
import com.fuwei.entity.producesystem.HalfCurrentStockDetail;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.entity.producesystem.HalfStoreInOutDetail;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class HalfCurrentStockService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(StoreInOutService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	HalfStoreInOutService halfStoreInOutService;
	
	// 获取列表
	public Pager getList(Pager pager,Integer companyId,Integer charge_employee,String orderNumber,Boolean not_zero, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select a.*,b.companyId,b.sampleId,b.name,b.img,b.img_s,b.img_ss,b.orderNumber,b.charge_employee,b.company_productNumber from tb_half_current_stock a, tb_order b where a.orderId=b.id ");

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
					HalfCurrentStock.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//创建
	@Transactional
	public int add(HalfCurrentStock object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("颜色及数量不可为空");
			} else {
				object.setDetail_json(SerializeTool.serialize(object
						.getDetaillist()));
				//获取total_stock_quantity
				int total_stock_quantity = 0 ;
				for(HalfCurrentStockDetail detail : object.getDetaillist()){
					total_stock_quantity += detail.getStock_quantity();
				}
				object.setTotal_stock_quantity(total_stock_quantity);
				Integer id = this.insert(object);
				this.update(object, "id", null);
				return id;
			}
		} catch (Exception e) {

			throw e;
		}
	}
	
	//编辑
	@Transactional
	public int update(HalfCurrentStock object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("颜色及数量不可为空");
			} else {
				String details = SerializeTool.serialize(object
						.getDetaillist());
				object.setDetail_json(details);
				//获取total_stock_quantity
				int total_stock_quantity = 0 ;
				for(HalfCurrentStockDetail detail : object.getDetaillist()){
					total_stock_quantity += detail.getStock_quantity();
				}
				object.setTotal_stock_quantity(total_stock_quantity);
				// 更新表
				this.update(object, "id","orderId", true);

				return object.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取
	public HalfCurrentStock get(int orderId) throws Exception {
		try {
			HalfCurrentStock object = dao.queryForBean(
					"select * from tb_half_current_stock where orderId = ?",
					HalfCurrentStock.class, orderId);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	//重新计算某订单的半成品库存
	@Transactional
	public void reStock(int orderId) throws Exception{
		HalfCurrentStock halfCurrentStock = this.get(orderId);
		List<HalfCurrentStockDetail> detaillist = new ArrayList<HalfCurrentStockDetail>();
		
		//获取已开的入库单
		List<HalfStoreInOut> storeInList = halfStoreInOutService.getByOrder(orderId,true);
		//获取已开的出库单
		List<HalfStoreInOut> storeOutList = halfStoreInOutService.getByOrder(orderId,false);
		
		//根据  【planOrderDetailId】  统计入库总数量 , key = planOrderDetailId
		HashMap<Integer, HalfStoreInOutDetail> tempMap = new HashMap<Integer, HalfStoreInOutDetail>();
		HashMap<Integer, Integer> total_inmap = new HashMap<Integer, Integer>();
		for (HalfStoreInOut storeIn : storeInList) {
			for (HalfStoreInOutDetail temp : storeIn.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(total_inmap.containsKey(key)){
					int quantity = total_inmap.get(key) + temp.getQuantity();
					total_inmap.put(key, quantity);
				}else{
					total_inmap.put(key, temp.getQuantity());
				}
				if(!tempMap.containsKey(key)){
					tempMap.put(key, temp);
				}
			}
		}
		

		//根据 【planOrderDetailId】 统计已出库 数量
		HashMap<Integer, Integer> total_outmap = new HashMap<Integer, Integer>();
		for (HalfStoreInOut storeOut : storeOutList) {
			for (HalfStoreInOutDetail temp : storeOut.getDetaillist()) {
				int key = temp.getPlanOrderDetailId(); 
				if(total_outmap.containsKey(key)){
					int temp_total_quantity = total_outmap.get(key);
					total_outmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_outmap.put(key, temp.getQuantity());
				}
			}
		}

		//获取当前库存列表
		for(int key : total_inmap.keySet()){
			int in_quantity = total_inmap.get(key);
			Integer out_quantity = total_outmap.get(key);
			//库存数量 = 入库数量 - 出库数量
			int stock_quantity = out_quantity == null? in_quantity:in_quantity - out_quantity;
			
			HalfCurrentStockDetail detail = new HalfCurrentStockDetail();
			detail.setPlanOrderDetailId(key);
			detail.setStock_quantity(stock_quantity);
			detaillist.add(detail);
		}
		
		//添加planOrderDetailId对应的颜色尺寸等属性
		for(HalfCurrentStockDetail detail : detaillist){
			int planOrderDetailId = detail.getPlanOrderDetailId();
			if(tempMap.containsKey(planOrderDetailId)){
				HalfStoreInOutDetail temp = tempMap.get(planOrderDetailId);
				detail.setColor(temp.getColor());
				detail.setProduce_weight(temp.getWeight());
				detail.setSize(temp.getSize());
				detail.setYarn(temp.getYarn());
			}
		}
		
	
		
		if(halfCurrentStock == null){//添加一个
			halfCurrentStock = new HalfCurrentStock();
			halfCurrentStock.setOrderId(orderId);
			halfCurrentStock.setDetaillist(detaillist);
			this.add(halfCurrentStock);
		}
		else{//编辑
			halfCurrentStock.setDetaillist(detaillist);
			this.update(halfCurrentStock);
		}
	}

}
