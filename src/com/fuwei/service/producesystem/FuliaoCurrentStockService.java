package com.fuwei.service.producesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.service.BaseService;

@Component
public class FuliaoCurrentStockService  extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FuliaoCurrentStockService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	LocationService locationService;
	
	// 获取列表
	public Pager getList(Pager pager,Integer charge_employee,String locationNumber,String orderNumber, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select f.*,l.number as number,l.fuliaoId,l.quantity,l.size l_size,l.id as locationId from tb_location l,tb_fuliao f where l.fuliaoId=f.id and l.type=1 ");

			StringBuffer sql_condition = new StringBuffer();
			if (charge_employee != null) {
				sql_condition.append(seq + " f.charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (orderNumber != null && !orderNumber.equals("")) {
				sql_condition.append(seq + " f.orderNumber='" + orderNumber + "'");
				seq = " AND ";
			}
			if (locationNumber != null && !locationNumber.equals("")) {
				sql_condition.append(seq + " l.number='" + locationNumber + "'");
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
			
			return findPager_T_Map(sql.append(sql_condition).toString(), pager);
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取自购辅料库存列表
	public Pager getList_purchase(Pager pager,Integer charge_employee,String locationNumber,String orderNumber, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select a.style,a.memo,a.quantity as plan_quantity,b.charge_employee,b.orderId,b.orderNumber,b.name,l.quantity,l.size l_size, l.id as locationId,l.number  from tb_location l , tb_fuliaopurchaseorder_detail a ,tb_fuliaopurchaseorder b  where l.fuliaoPurchaseOrderDetailId=a.id and l.type=1 and a.fuliaoPurchaseOrderId=b.id");

			StringBuffer sql_condition = new StringBuffer();
			if (charge_employee != null) {
				sql_condition.append(seq + " b.charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (orderNumber != null && !orderNumber.equals("")) {
				sql_condition.append(seq + " b.orderNumber='" + orderNumber + "'");
				seq = " AND ";
			}
			if (locationNumber != null && !locationNumber.equals("")) {
				sql_condition.append(seq + " l.number='" + locationNumber + "'");
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
			
			return findPager_T_Map(sql.append(sql_condition).toString(), pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取通用辅料库存列表
	public Pager getList_common(Pager pager ,String locationNumber, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select f.*,l.number as number,l.fuliaoId,l.quantity,l.size l_size,l.id as locationId from tb_location l,tb_fuliao f where l.fuliaoId=f.id and l.type=2 ");

			StringBuffer sql_condition = new StringBuffer();
			if (locationNumber != null && !locationNumber.equals("")) {
				sql_condition.append(seq + " l.number='" + locationNumber + "'");
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
			
			return findPager_T_Map(sql.append(sql_condition).toString(), pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//获取辅料出入库记录
	public List<Map<String,Object>> inoutByFuliao(int fuliaoId){
		List<Map<String,Object>> result = dao.queryForListMap("(select 'in' as type ,fuliaoId,quantity,fuliaoInOutId,locationId,fuliaoPurchaseFactoryId,b.number,b.created_at,b.created_user,null as is_cleaning from tb_fuliaoin_detail a , tb_fuliaoin b where a.fuliaoId=? and a.fuliaoInOutId = b.id and b.status = 6 )"
				+" union all (select 'out' as type ,fuliaoId,quantity,fuliaoInOutId,locationId,null,b.number,b.created_at,b.created_user,b.is_cleaning from tb_fuliaoout_detail a , tb_fuliaoout b where a.fuliaoId=? and a.fuliaoInOutId = b.id and b.status = 6 ) order by created_at", fuliaoId,fuliaoId);
		return result;
	}
	
	//获取辅料的总当前库存数量，只有数量，没有库位分布
	public Map<String,Object> getByFuliao(int fuliaoId){
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> in_map = dao.queryForMap("select sum(quantity) in_quantity from tb_fuliaoin_detail where fuliaoId=?", fuliaoId);
		Map<String,Object> out_map = dao.queryForMap("select sum(quantity) out_quantity from tb_fuliaoout_detail where fuliaoId=?", fuliaoId);
//		Map<String,Object> in_return_map = dao.queryForMap("select sum(quantity) in_return_quantity from tb_fuliaoin_return_detail where fuliaoId=?", fuliaoId);
//		Map<String,Object> out_return_map = dao.queryForMap("select sum(quantity) out_return_quantity from tb_fuliaoout_detail where fuliaoId=?", fuliaoId);
		int in_quantity = 0 ;
		if(in_map.get("in_quantity")!=null){
			in_quantity = Integer.valueOf(in_map.get("in_quantity").toString());
		}
		result.put("in_quantity",in_quantity);
		int out_quantity = 0 ;
		if(out_map.get("out_quantity")!=null){
			out_quantity = Integer.valueOf(out_map.get("out_quantity").toString());
		}
		result.put("out_quantity",out_quantity);
//		result.put("in_return_quantity",in_return_map.get("in_return_quantity"));
//		result.put("out_return_quantity",out_return_map.get("out_return_quantity"));
		result.put("stock_quantity",in_quantity - out_quantity);
		return result;
	}
	
	//获取某订单的各辅料总当前库存数量，只有数量，没有库位分布
	public List<Map<String,Object>> getByOrder(int orderId){
		List<Map<String,Object>> result = dao.queryForListMap("select f.* ,newtable.in_quantity from tb_fuliao f left join (select sum(quantity) in_quantity,fuliaoId from tb_fuliaoin_detail group by fuliaoId)  newtable on f.id = newtable.fuliaoId where  f.orderId = ?",orderId);
		List<Map<String,Object>> out_map = dao.queryForListMap("select sum(quantity) out_quantity,fuliaoId from tb_fuliaoout_detail a ,tb_fuliao b where a.fuliaoId=b.id and b.orderId=? group by fuliaoId", orderId);
//		List<Map<String,Object>> in_return_map = dao.queryForListMap("select sum(quantity) in_return_quantity,fuliaoId from tb_fuliaoin_return_detail a ,tb_fuliao b where a.fuliaoId=b.id and b.orderId=? group by fuliaoId", orderId);
//		List<Map<String,Object>> out_return_map = dao.queryForListMap("select sum(quantity) out_return_quantity,fuliaoId from tb_fuliaoout_return_detail a ,tb_fuliao b where a.fuliaoId=b.id and b.orderId=? group by fuliaoId", orderId);
		
		for(Map<String,Object> item : result){
			int fuliaoId = (Integer)item.get("id");
			int in_quantity = 0;
			if(item.get("in_quantity")!=null){
				in_quantity = Integer.valueOf(item.get("in_quantity").toString());
			}
			int out_quantity = 0;
			for(Map<String,Object> temp_item : out_map){
				int tempfuliaoId = (Integer)temp_item.get("fuliaoId");
				if(tempfuliaoId == fuliaoId){
					out_quantity = Integer.valueOf(temp_item.get("out_quantity").toString());
				}
			}
			item.put("out_quantity",out_quantity);
			
//			int in_return_quantity = 0;
//			for(Map<String,Object> temp_item : in_return_map){
//				int tempfuliaoId = (Integer)temp_item.get("fuliaoId");
//				if(tempfuliaoId == fuliaoId){
//					in_return_quantity = (Integer)temp_item.get("in_return_quantity");
//				}
//			}
//			item.put("in_return_quantity",in_return_quantity);
//			
//			int out_return_quantity = 0;
//			for(Map<String,Object> temp_item : out_return_map){
//				int tempfuliaoId = (Integer)temp_item.get("fuliaoId");
//				if(tempfuliaoId == fuliaoId){
//					out_return_quantity = (Integer)temp_item.get("out_return_quantity");
//				}
//			}
//			item.put("out_return_quantity",out_return_quantity);
			
//			int stock_quantity = in_quantity - out_quantity - in_return_quantity + out_return_quantity; //当前库存
			int stock_quantity = in_quantity - out_quantity; //当前库存
			item.put("stock_quantity",stock_quantity);
		}
		return result;
	}
	
	//同上类似，获取所有通用辅料的各辅料总当前库存数量、辅料各属性，只有数量，没有库位分布
	public List<Map<String,Object>> getByOrder_Common(){
		List<Map<String,Object>> result = dao.queryForListMap("select f.* ,newtable.in_quantity from tb_fuliao f left join (select sum(quantity) in_quantity,fuliaoId from tb_fuliaoin_detail group by fuliaoId)  newtable on f.id = newtable.fuliaoId where  f.orderId is null");
		List<Map<String,Object>> out_map = dao.queryForListMap("select sum(quantity) out_quantity,fuliaoId from tb_fuliaoout_detail a ,tb_fuliao b where a.fuliaoId=b.id and b.orderId is null group by fuliaoId");
		for(Map<String,Object> item : result){
			int fuliaoId = (Integer)item.get("id");
			int in_quantity = 0;
			if(item.get("in_quantity")!=null){
				in_quantity = Integer.valueOf(item.get("in_quantity").toString());
			}
			int out_quantity = 0;
			for(Map<String,Object> temp_item : out_map){
				int tempfuliaoId = (Integer)temp_item.get("fuliaoId");
				if(tempfuliaoId == fuliaoId){
					out_quantity = Integer.valueOf(temp_item.get("out_quantity").toString());
				}
			}
			item.put("out_quantity",out_quantity);
			int stock_quantity = in_quantity - out_quantity; //当前库存
			item.put("stock_quantity",stock_quantity);
		}
		return result;
	}
	//同上类似，参数不同 ids：辅料的id
	public List<Map<String,Object>> getByOrder_Common(String ids){
		if(ids == null || ids.equals("")){
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String,Object>> result = dao.queryForListMap("select f.* ,newtable.in_quantity from tb_fuliao f left join (select sum(quantity) in_quantity,fuliaoId from tb_fuliaoin_detail group by fuliaoId)  newtable on f.id = newtable.fuliaoId where f.orderId is null and f.id in (" + ids + ")");
		List<Map<String,Object>> out_map = dao.queryForListMap("select sum(quantity) out_quantity,fuliaoId from tb_fuliaoout_detail a ,tb_fuliao b where a.fuliaoId=b.id and b.orderId is null and b.id in (" + ids + ") group by fuliaoId");
		for(Map<String,Object> item : result){
			int fuliaoId = (Integer)item.get("id");
			int in_quantity = 0;
			if(item.get("in_quantity")!=null){
				in_quantity = Integer.valueOf(item.get("in_quantity").toString());
			}
			int out_quantity = 0;
			for(Map<String,Object> temp_item : out_map){
				int tempfuliaoId = (Integer)temp_item.get("fuliaoId");
				if(tempfuliaoId == fuliaoId){
					out_quantity = Integer.valueOf(temp_item.get("out_quantity").toString());
				}
			}
			item.put("out_quantity",out_quantity);
			int stock_quantity = in_quantity - out_quantity; //当前库存
			item.put("stock_quantity",stock_quantity);
		}
		return result;
	}
	
	//获取某辅料采购单的各自购辅料总当前库存数量，只有数量，没有库位分布
	public Map<Integer,Map<String,Object>> getByPurchaseOrder(int fuliaoPurchaseOrderId){
		List<Map<String,Object>> in_map = dao.queryForListMap("select f.* ,newtable.in_quantity from tb_fuliaopurchaseorder_detail f left join (select sum(quantity) in_quantity,fuliaoPurchaseOrderDetailId from tb_selffuliaoin_detail group by fuliaoPurchaseOrderDetailId)  newtable on f.id = newtable.fuliaoPurchaseOrderDetailId where  f.fuliaoPurchaseOrderId = ?",fuliaoPurchaseOrderId);
		List<Map<String,Object>> out_map = dao.queryForListMap("select sum(a.quantity) out_quantity,fuliaoPurchaseOrderDetailId from tb_selffuliaoout_detail a ,tb_fuliaopurchaseorder_detail b where a.fuliaoPurchaseOrderDetailId=b.id and b.fuliaoPurchaseOrderId=? group by fuliaoPurchaseOrderDetailId", fuliaoPurchaseOrderId);
		//Map<采购单明细ID，出入库数据>
		Map<Integer,Map<String,Object>> result = new HashMap<Integer, Map<String,Object>>();
		for(Map<String,Object> item : in_map){
			Map<String,Object> resultItem = new HashMap<String, Object>();
			int fuliaoPurchaseOrderDetailId = (Integer)item.get("id");
			int plan_quantity = (Integer)item.get("quantity");
			int in_quantity = 0;
			if(item.get("in_quantity")!=null){
				in_quantity = Integer.valueOf(item.get("in_quantity").toString());
			}
			int out_quantity = 0;
			for(Map<String,Object> temp_item : out_map){
				int tempfuliaoPurchaseOrderDetailId = (Integer)temp_item.get("fuliaoPurchaseOrderDetailId");
				if(tempfuliaoPurchaseOrderDetailId == fuliaoPurchaseOrderDetailId){
					out_quantity = Integer.valueOf(temp_item.get("out_quantity").toString());
				}
			}
			int stock_quantity = in_quantity - out_quantity; //当前库存		

			resultItem.put("plan_quantity", plan_quantity);
			resultItem.put("in_quantity", in_quantity);
			resultItem.put("not_in_quantity", plan_quantity-in_quantity);
			resultItem.put("out_quantity", out_quantity);
			resultItem.put("not_out_quantity", plan_quantity-out_quantity);
			resultItem.put("stock_quantity", stock_quantity);
			result.put(fuliaoPurchaseOrderDetailId, resultItem);
		}
		return result;
	}
	
	//获取某订单的各辅料总当前库存,只返回fuliaoId和stock_quantity
	public Map<Integer,Integer> getStockMapByOrder(int orderId){
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		List<Map<String,Object>> maplist = dao.queryForListMap("select sum(quantity) stock_quantity,fuliaoId from tb_location a ,tb_fuliao b where b.orderId = ? and a.fuliaoId = b.id group by fuliaoId", orderId);
			//这个list里的每一个map的fuliaoId都不相同
		for(Map<String,Object> item : maplist){
			int fuliaoId = Integer.valueOf(item.get("fuliaoId").toString());
			if(!result.containsKey(fuliaoId)){
				result.put(fuliaoId,Integer.valueOf(item.get("stock_quantity").toString()));
			}
		}
	
		return result;
	}
	
	//获取各个通用辅料总当前库存,只返回fuliaoId和stock_quantity
	public Map<Integer,Integer> getStockMapByOrder_Common(){
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		List<Map<String,Object>> maplist = dao.queryForListMap("select sum(quantity) stock_quantity,fuliaoId from tb_location a ,tb_fuliao b where b.orderId is null  and a.fuliaoId = b.id group by fuliaoId");
			//这个list里的每一个map的fuliaoId都不相同
		for(Map<String,Object> item : maplist){
			int fuliaoId = Integer.valueOf(item.get("fuliaoId").toString());
			if(!result.containsKey(fuliaoId)){
				result.put(fuliaoId,Integer.valueOf(item.get("stock_quantity").toString()));
			}
		}
	
		return result;
	}
	//获取各个通用辅料总当前库存,只返回fuliaoId和stock_quantity
	public Map<Integer,Integer> getStockMapByOrder_Common(String ids){
		if(ids.equals("")){
			return new HashMap<Integer, Integer>();
		}
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		List<Map<String,Object>> maplist = dao.queryForListMap("select sum(quantity) stock_quantity,fuliaoId from tb_location a ,tb_fuliao b where b.orderId is null and a.fuliaoId = b.id and b.id in (" + ids + ") group by fuliaoId");
			//这个list里的每一个map的fuliaoId都不相同
		for(Map<String,Object> item : maplist){
			int fuliaoId = Integer.valueOf(item.get("fuliaoId").toString());
			if(!result.containsKey(fuliaoId)){
				result.put(fuliaoId,Integer.valueOf(item.get("stock_quantity").toString()));
			}
		}
	
		return result;
	}
	
	//获取某辅料当前存放的 库位 以及各库位分别存放了多少
	public Map<Integer,Integer> locationByFuliao(int fuliaoId){
//		List<Map<String,Object>> map = dao.queryForListMap(
//				"select sum(quantity) stock_quantity,locationId from" 
//				+" ((select 'in' as type ,fuliaoId,quantity,fuliaoInOutId,locationId,fuliaoPurchaseFactoryId,b.number,b.created_at,b.created_user from tb_fuliaoin_detail a , tb_fuliaoin b where a.fuliaoId=? and a.fuliaoInOutId = b.id and b.status = 6 )"
//				+" union all (select 'out' as type ,fuliaoId,-quantity,fuliaoInOutId,locationId,null,b.number,b.created_at,b.created_user from tb_fuliaoout_detail a , tb_fuliaoout b where a.fuliaoId=? and a.fuliaoInOutId = b.id and b.status = 6 )) as tempTb"
//				+" group by locationId", fuliaoId,fuliaoId);
//		//Map<locationId,stock_quantity>
//		Map<Integer,Integer> result = new HashMap<Integer, Integer>();
//		for(Map<String,Object> item : map){
//			result.put(Integer.valueOf(item.get("locationId").toString()), Integer.valueOf(item.get("stock_quantity").toString()));
//		}
//		return result;
		
		List<Map<String,Object>> map = dao.queryForListMap(
				"select quantity,id from tb_location where fuliaoId=?", fuliaoId);
		if(map == null){
			return new HashMap<Integer, Integer>();
		}
		//Map<locationId,stock_quantity>
		Map<Integer,Integer> result = new HashMap<Integer, Integer>();
		for(Map<String,Object> item : map){
			result.put(Integer.valueOf(item.get("id").toString()), Integer.valueOf(item.get("quantity").toString()));
		}
		return result;
	}
	
	//获取某自购辅料当前存放的 库位 以及各库位分别存放了多少
	public Map<Integer,Integer> locationByPurchaseDetail(int fuliaoPurchaseOrderDetailId){	
		List<Map<String,Object>> map = dao.queryForListMap(
				"select quantity,id from tb_location where fuliaoPurchaseOrderDetailId=?", fuliaoPurchaseOrderDetailId);
		if(map == null){
			return new HashMap<Integer, Integer>();
		}
		//Map<locationId,stock_quantity>
		Map<Integer,Integer> result = new HashMap<Integer, Integer>();
		for(Map<String,Object> item : map){
			result.put(Integer.valueOf(item.get("id").toString()), Integer.valueOf(item.get("quantity").toString()));
		}
		return result;
	}
}
