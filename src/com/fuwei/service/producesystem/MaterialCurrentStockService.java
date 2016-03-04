package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.ColoringOrderDetail;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.entity.ordergrid.StoreOrderDetail;
import com.fuwei.entity.producesystem.MaterialCurrentStock;
import com.fuwei.entity.producesystem.MaterialCurrentStockDetail;
import com.fuwei.entity.producesystem.MaterialInOut;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.entity.producesystem.StoreInOutDetail;
import com.fuwei.entity.producesystem.StoreReturn;
import com.fuwei.entity.producesystem.StoreReturnDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.util.SerializeTool;

@Component
public class MaterialCurrentStockService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(MaterialCurrentStockService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	StoreInOutService storeInOutService;
	@Autowired
	StoreReturnService storeReturnService;
	@Autowired
	StoreOrderService storeOrderService;
	@Autowired
	ColoringOrderService coloringOrderService;
	
	// 获取列表
	public Pager getList(Pager pager,Integer companyId,Integer charge_employee,String orderNumber,Boolean not_zero, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select a.*,b.companyId,b.sampleId,b.name,b.img,b.img_s,b.img_ss,b.orderNumber,b.charge_employee,b.company_productNumber from tb_material_current_stock a, tb_order b where a.orderId=b.id ");

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
					MaterialCurrentStock.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 样纱，染色单 获取列表
	public Pager getList_coloring(Pager pager,Integer companyId,Integer charge_employee,String coloringOrderNumber,Boolean not_zero, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select a.*,b.companyId,b.name,b.number,b.charge_employee,b.company_productNumber from tb_material_current_stock a, tb_coloringorder b where a.coloring_order_Id=b.id ");

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
			if (coloringOrderNumber != null && !coloringOrderNumber.equals("")) {
				sql_condition.append(seq + " b.number='" + coloringOrderNumber + "'");
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
					MaterialCurrentStock.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	//获取某订单的原材料出入库记录
	// 获取
	public List<MaterialInOut> inOutdetail(int orderId)
			throws Exception {
		try {
			List<MaterialInOut> list = dao.queryForBeanList("select * from (select created_at,created_user,'store' as type ,id ,number, store_order_id,orderId,date,factoryId,sign,has_print,detail_json,in_out,memo  from tb_store_in_out where orderId = ? union all select created_at,created_user,'return' as type,id ,number, store_order_id, orderId,date,factoryId,sign,has_print,detail_json,null,memo  from tb_store_return where orderId = ?)  c order by date desc,created_at desc",
					MaterialInOut.class,orderId,orderId);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//样纱    获取某染色单的原材料出入库记录
	// 获取
	public List<MaterialInOut> inOutdetail_coloring(int coloringOrderId)
			throws Exception {
		try {
			List<MaterialInOut> list = dao.queryForBeanList("select * from (select created_at,created_user,'store' as type ,id ,number, coloring_order_id,date,factoryId,sign,has_print,detail_json,in_out,memo  from tb_store_in_out where coloring_order_id = ? union all select created_at,created_user,'return' as type,id ,number, coloring_order_id,date,factoryId,sign,has_print,detail_json,null,memo  from tb_store_return where coloring_order_id = ?)  c order by date desc,created_at desc",
					MaterialInOut.class,coloringOrderId,coloringOrderId);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//创建
	@Transactional
	public int add(MaterialCurrentStock object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("明细不能为空");
			} else {
				object.setDetail_json(SerializeTool.serialize(object
						.getDetaillist()));
				//获取total_stock_quantity
				int total_stock_quantity = 0 ;
				for(MaterialCurrentStockDetail detail : object.getDetaillist()){
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
	public int update(MaterialCurrentStock object) throws Exception {
		try {
			if (object.getDetaillist() == null
					|| object.getDetaillist().size() <= 0) {
				throw new Exception("明细不可为空");
			} else {
				String details = SerializeTool.serialize(object
						.getDetaillist());
				object.setDetail_json(details);
				//获取total_stock_quantity
				int total_stock_quantity = 0 ;
				for(MaterialCurrentStockDetail detail : object.getDetaillist()){
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
	public MaterialCurrentStock get(int orderId) throws Exception {
		try {
			MaterialCurrentStock object = dao.queryForBean(
					"select * from tb_material_current_stock where orderId = ?",
					MaterialCurrentStock.class, orderId);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public MaterialCurrentStock getByColoringOrder(int coloringOrderId) throws Exception {
		try {
			MaterialCurrentStock object = dao.queryForBean(
					"select * from tb_material_current_stock where coloring_order_id = ?",
					MaterialCurrentStock.class, coloringOrderId);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//重新计算某订单的原材料库存
	@Transactional
	public void reStock(int orderId) throws Exception{
		StoreOrder storeOrder = storeOrderService.getByOrder(orderId);
		MaterialCurrentStock currentStock = this.get(orderId);
		List<MaterialCurrentStockDetail> detaillist = new ArrayList<MaterialCurrentStockDetail>();
		
		//获取已开的入库单
		List<StoreInOut> storeInList = storeInOutService.getByOrder(orderId,true);
		//获取已开的出库单
		List<StoreInOut> storeOutList = storeInOutService.getByOrder(orderId,false);
		//获取已开的半成品退货单
		List<StoreReturn> storeReturnList = storeReturnService.getByOrder(orderId);
		
		//根据  【色号】 和 【材料】  统计计划纱线数量 , key = material + : + color
		HashMap<String, Double> totalmap = new HashMap<String, Double>();
		for (StoreOrderDetail storeOrderDetail : storeOrder.getDetaillist()) {
			String key = storeOrderDetail.getMaterial() + ":"+ storeOrderDetail.getColor().trim(); 
			if(totalmap.containsKey(key)){
				double temp_total_quantity = totalmap.get(key);
				totalmap.put(key, temp_total_quantity + storeOrderDetail.getQuantity());
			}else{
				totalmap.put(key, storeOrderDetail.getQuantity());
			}
		}
		
		//根据  【planOrderDetailId】  统计入库总数量 , key = planOrderDetailId
		HashMap<String, Double> total_inmap = new HashMap<String, Double>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_inmap.containsKey(key)){
					double temp_total_quantity = total_inmap.get(key);
					total_inmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_inmap.put(key, temp.getQuantity());
				}
			}
		}
		

		//根据 【planOrderDetailId】 统计已出库 数量
		HashMap<String, Double> total_outmap = new HashMap<String, Double>();
		for (StoreInOut storeOut : storeOutList) {
			for (StoreInOutDetail temp : storeOut.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_outmap.containsKey(key)){
					double temp_total_quantity = total_outmap.get(key);
					total_outmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_outmap.put(key, temp.getQuantity());
				}
			}
		}
		
		HashMap<String, Double> total_returnmap = new HashMap<String, Double>();
		for (StoreReturn storereturn : storeReturnList) {
			for (StoreReturnDetail temp : storereturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_returnmap.containsKey(key)){
					double temp_total_quantity = total_returnmap.get(key);
					total_returnmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_returnmap.put(key, temp.getQuantity());
				}
			}
		}
		

		//获取当前库存列表
		for(String key : total_inmap.keySet()){
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			double plan_quantity = totalmap.get(key);
			double in_quantity = total_inmap.get(key);
			double out_quantity = 0;
			if(total_outmap.containsKey(key)){
				out_quantity = total_outmap.get(key);
			}
			double return_quantity = 0;
			if(total_returnmap.containsKey(key)){
				return_quantity = total_returnmap.get(key);
			}
			//库存数量 = 入库数量 - 出库数量
			double stock_quantity = in_quantity - return_quantity - out_quantity;
			if(stock_quantity<0){
				throw new Exception("库存数量不能小于0，入库数："+in_quantity+"，出库数："+out_quantity + "，退货数：" + return_quantity);
			}
			MaterialCurrentStockDetail detail = new MaterialCurrentStockDetail();
			detail.setColor(color);
			detail.setMaterial(material);
			detail.setIn_quantity(in_quantity);
			detail.setReturn_quantity(return_quantity);
			detail.setPlan_quantity(plan_quantity);
			detail.setStock_quantity(stock_quantity);
			detaillist.add(detail);
		}
		
		//根据缸号判断库位是否小于0,若是则报异常，库存数量异常
		if(isLotStockNull(storeInList,storeOutList,storeReturnList) == true){
			throw new Exception("色号、材料、缸号入库数量总和小于已出库数量，无法修改");
		}
	
		
		if(currentStock == null){//添加一个
			currentStock = new MaterialCurrentStock();
			currentStock.setOrderId(orderId);
			currentStock.setDetaillist(detaillist);
			if(detaillist.size()>0){
				this.add(currentStock);
			}
		}
		else{//编辑
			currentStock.setDetaillist(detaillist);
			if(detaillist.size()<=0){
				this.remove(currentStock);
			}else{
				this.update(currentStock);
			}
		}
	}
	
	
	//重新计算某染色单的样纱库存
	@Transactional
	public void reStock_Coloring(int coloringOrderId) throws Exception{
		ColoringOrder coloringOrder = coloringOrderService.get(coloringOrderId);
		MaterialCurrentStock currentStock = this.getByColoringOrder(coloringOrderId);
		List<MaterialCurrentStockDetail> detaillist = new ArrayList<MaterialCurrentStockDetail>();
		
		//获取已开的入库单
		List<StoreInOut> storeInList = storeInOutService.getByColoringOrder(coloringOrderId,true);
		//获取已开的出库单
		List<StoreInOut> storeOutList = storeInOutService.getByColoringOrder(coloringOrderId,false);
//		//获取已开的半成品退货单
//		List<StoreReturn> storeReturnList = storeReturnService.getByColoringOrder(coloringOrderId);
		List<StoreReturn> storeReturnList = new ArrayList<StoreReturn>();//2016-2-23暂时没有样纱退货的功能
		
		//根据  【色号】 和 【材料】  统计计划纱线数量 , key = material + : + color
		HashMap<String, Double> totalmap = new HashMap<String, Double>();
		for (ColoringOrderDetail coloringOrderDetail : coloringOrder.getDetaillist()) {
			String key = coloringOrderDetail.getMaterial() + ":"+ coloringOrderDetail.getColor().trim(); 
			if(totalmap.containsKey(key)){
				double temp_total_quantity = totalmap.get(key);
				totalmap.put(key, temp_total_quantity + coloringOrderDetail.getQuantity());
			}else{
				totalmap.put(key, coloringOrderDetail.getQuantity());
			}
		}
		
		//根据  【planOrderDetailId】  统计入库总数量 , key = planOrderDetailId
		HashMap<String, Double> total_inmap = new HashMap<String, Double>();
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_inmap.containsKey(key)){
					double temp_total_quantity = total_inmap.get(key);
					total_inmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_inmap.put(key, temp.getQuantity());
				}
			}
		}
		

		//根据 【planOrderDetailId】 统计已出库 数量
		HashMap<String, Double> total_outmap = new HashMap<String, Double>();
		for (StoreInOut storeOut : storeOutList) {
			for (StoreInOutDetail temp : storeOut.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_outmap.containsKey(key)){
					double temp_total_quantity = total_inmap.get(key);
					total_outmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_outmap.put(key, temp.getQuantity());
				}
			}
		}
		
		HashMap<String, Double> total_returnmap = new HashMap<String, Double>();
		for (StoreReturn storereturn : storeReturnList) {
			for (StoreReturnDetail temp : storereturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim(); 
				if(total_returnmap.containsKey(key)){
					double temp_total_quantity = total_returnmap.get(key);
					total_returnmap.put(key, temp_total_quantity + temp.getQuantity());
				}else{
					total_returnmap.put(key, temp.getQuantity());
				}
			}
		}
		

		//获取当前库存列表
		for(String key : total_inmap.keySet()){
			int indexOf = key.indexOf(":");
			if(indexOf <= -1){
				throw new Exception("分割色号与材料出错");
			}
			Integer material = Integer.parseInt(key.substring(0,indexOf));
			String color = key.substring(indexOf+1);
			double plan_quantity = totalmap.get(key);
			double in_quantity = total_inmap.get(key);
			double out_quantity = 0;
			if(total_outmap.containsKey(key)){
				out_quantity = total_outmap.get(key);
			}
			double return_quantity = 0;
			if(total_returnmap.containsKey(key)){
				return_quantity = total_returnmap.get(key);
			}
			//库存数量 = 入库数量 - 出库数量
			double stock_quantity = in_quantity - return_quantity - out_quantity;
			if(stock_quantity<0){
				throw new Exception("库存数量不能小于0，入库数："+in_quantity+"，出库数："+out_quantity + "，退货数：" + return_quantity);
			}
			MaterialCurrentStockDetail detail = new MaterialCurrentStockDetail();
			detail.setColor(color);
			detail.setMaterial(material);
			detail.setIn_quantity(in_quantity);
			detail.setReturn_quantity(return_quantity);
			detail.setPlan_quantity(plan_quantity);
			detail.setStock_quantity(stock_quantity);
			detaillist.add(detail);
		}
		//根据缸号判断库位是否小于0,若是则报异常，库存数量异常
		if(isLotStockNull(storeInList,storeOutList,storeReturnList) == true){
			throw new Exception("色号、材料、缸号入库数量总和小于已出库数量，无法修改");
		}
	
		
		if(currentStock == null){//添加一个
			currentStock = new MaterialCurrentStock();
			currentStock.setColoring_order_id(coloringOrderId);
			currentStock.setDetaillist(detaillist);
			if(detaillist.size()>0){
				this.add(currentStock);
			}
		}
		else{//编辑
			currentStock.setDetaillist(detaillist);
			if(detaillist.size()<=0){
				this.remove(currentStock);
			}else{
				this.update(currentStock);
			}
		}
	}
	
	//根据缸号判断库位是否小于0
	public Boolean isLotStockNull(List<StoreInOut> storeInList,List<StoreInOut> storeOutList,List<StoreReturn> storeReturnList){
		HashMap<String, Double> resultMap = new HashMap<String, Double>();
		
		//根据  【色号】 和 【材料】 和 【缸号】  统计入库总数量
		for (StoreInOut storeIn : storeInList) {
			for (StoreInOutDetail temp : storeIn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim() + ":" + temp.getLot_no().trim(); 
				if(resultMap.containsKey(key)){
					double temp_quantity = resultMap.get(key);
					resultMap.put(key, temp_quantity + temp.getQuantity());
				}else{
					resultMap.put(key, temp.getQuantity());
				}
			}
		}
		
		//根据  【色号】 和 【材料】 和 【缸号】  减去出库数量
		for (StoreInOut storeOut : storeOutList) {
			for (StoreInOutDetail temp : storeOut.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim()+ ":" + temp.getLot_no().trim(); 
				if(resultMap.containsKey(key)){
					double temp_quantity = resultMap.get(key);
					resultMap.put(key, temp_quantity - temp.getQuantity());
				}else{
					resultMap.put(key, -temp.getQuantity());
				}
			}
		}
		//根据  【色号】 和 【材料】 和 【缸号】  减去退货数量
		for (StoreReturn storereturn : storeReturnList) {
			for (StoreReturnDetail temp : storereturn.getDetaillist()) {
				String key = temp.getMaterial() + ":"+ temp.getColor().trim()+ ":" + temp.getLot_no().trim(); 
				if(resultMap.containsKey(key)){
					double temp_quantity = resultMap.get(key);
					resultMap.put(key, temp_quantity - temp.getQuantity());
				}else{
					resultMap.put(key, -temp.getQuantity());
				}
			}
		}
				
		//获取 【色号】、【材料】、【缸号】 库存
		for(String key : resultMap.keySet()){
			if(resultMap.get(key) < 0){
				return true;
				//throw new Exception("色号、材料、缸号入库数量总和小于已出库数量，无法修改");
			}
		}
		return false;
	}
	
	// 删除
	public int remove(MaterialCurrentStock temp) throws Exception {
		try {
			int result = dao.update("delete from tb_material_current_stock WHERE  id = ?", temp.getId());
			return result;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与半成品库存有关的引用");
			}
			throw e;
		}
	}

}
