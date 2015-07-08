//package com.fuwei.service.ordergrid;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fuwei.entity.financial.Expense_income_invoice;
//import com.fuwei.entity.ordergrid.StoreOrderDetail;
//import com.fuwei.service.BaseService;
//
//
//@Component
//public class StoreOrderDetailService extends BaseService {
//	private Logger log = org.apache.log4j.LogManager.getLogger(StoreOrderDetailService.class);
//	@Autowired
//	JdbcTemplate jdbc;
//	
//	// 获取
//	public StoreOrderDetail get(int id) throws Exception {
//		try {
//			StoreOrderDetail OrderDetail = dao.queryForBean(
//					"select * from tb_storeorder_detail where id = ?", StoreOrderDetail.class,
//					id);
//			return OrderDetail;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	// 获取订单详情列表
//	public List<StoreOrderDetail> getByStoreOrder(int storeOrderId) throws Exception {
//		try {
//			List<StoreOrderDetail> list = dao.queryForBeanList(
//					"SELECT * FROM tb_storeorder_detail WHERE storeOrderId=?", StoreOrderDetail.class,storeOrderId);
//			return list;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	// 添加详情
//	@Transactional
//	public int add(StoreOrderDetail detail) throws Exception {
//		try{
//			return this.insert(detail);
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	// 编辑
//	@Transactional
//	public int update(StoreOrderDetail detail) throws Exception {
//		try{
//			return this.update(detail,"id","storeOrderId,created_user,created_at",true);
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	@Transactional
//	public int addBatch(List<StoreOrderDetail> detailList) throws Exception {
//		try{
//			String sql = "INSERT INTO tb_storeorder_detail(storeOrderId,color,material,quantity,yarn,factoryId,created_at,updated_at,created_user) VALUES(?,?,?,?,?,?,?,?,?)";
//
//	        List<Object[]> batchArgs = new ArrayList<Object[]>();
//	        for(StoreOrderDetail item :detailList) {
//	            batchArgs.add(new Object[]{item.getStoreOrderId(),item.getColor(),item.getMaterial(),item.getQuantity(),item.getYarn(),item.getFactoryId(),item.getCreated_at(),item.getUpdated_at(),item.getCreated_user()});
//	        }
//
//	        int result[]= jdbc.batchUpdate(sql, batchArgs);
//			
//			return 1;
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	@Transactional
//	public int deleteBatch(List<StoreOrderDetail> detailList) throws Exception {
//		try{
//			String sql = "delete from tb_storeorder_detail WHERE id=?";
//
//	        List<Object[]> batchArgs = new ArrayList<Object[]>();
//	        for(StoreOrderDetail item :detailList) {
//	            batchArgs.add(new Object[]{item.getId()});
//	        }
//
//	        int result[]= jdbc.batchUpdate(sql, batchArgs);
//			
//			return 1;
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	@Transactional
//	public int deleteByStoreOrder(int storeOrderId) throws Exception {
//		try{
//			return dao.update("delete from tb_storeorder_detail WHERE  storeOrderId =?", storeOrderId);
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//}