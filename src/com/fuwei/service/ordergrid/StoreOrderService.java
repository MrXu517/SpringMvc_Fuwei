package com.fuwei.service.ordergrid;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.ordergrid.StoreOrder; //import com.fuwei.entity.ProducingOrderDetail;
import com.fuwei.entity.ordergrid.StoreOrderDetail;
//import com.fuwei.entity.ProducingOrderMaterialDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.producesystem.StoreInOutService;
import com.fuwei.util.SerializeTool;

@Component
public class StoreOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(StoreOrderService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	StoreInOutService storeInOutService;
//	@Autowired
//	StoreOrderDetailService storeOrderDetailService;

	// 添加
	@Transactional
	public int add(StoreOrder storeOrder) throws Exception {
		try {
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new Exception("原材料仓库中至少得有一条材料列表记录");
			} else {
				storeOrder.setStatus(0);
				storeOrder.setState("新建");
				storeOrder.setDetail_json(SerializeTool.serialize(storeOrder.getDetaillist()));
				Integer storeOrderId = this.insert(storeOrder);

				storeOrder.setId(storeOrderId);
				
//				//添加明细
//				storeOrderDetailService.addBatch(storeOrder.getDetaillist());	

				return storeOrderId;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑
	@Transactional
	public int update(StoreOrder storeOrder) throws Exception {
		try {
			if (storeOrder.getDetaillist() == null
					|| storeOrder.getDetaillist().size() <= 0) {
				throw new Exception("原材料仓库中至少得有一条材料列表记录");
			} else {
				StoreOrder temp = this.get(storeOrder.getId());
				if (!temp.isEdit()) {
					throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
				}
				List<StoreOrderDetail> detaillist = storeOrder
				.getDetaillist();
				String details = SerializeTool.serialize(detaillist);
				storeOrder.setDetail_json(details);

				// 更新表
				this.update(storeOrder, "id",
						"created_user,created_at,orderId,companyId,customerId,sampleId,name,img,img_s,img_ss,materialId,weight,size,productNumber,orderNumber,charge_employee,company_productNumber", true);
//
//				//更新明细表
//				List<StoreOrderDetail> deletelist = new ArrayList<StoreOrderDetail>();
//				List<StoreOrderDetail> addlist = new ArrayList<StoreOrderDetail>();
//				List<StoreOrderDetail> updatelist = new ArrayList<StoreOrderDetail>();
//				for(StoreOrderDetail detail : detaillist){
//					if(detail.getId() <= 0){//新增的
//						addlist.add(detail);
//					}
//					else{//修改的
//						updatelist.add(detail);
//					}
//				}
//				//新增
				
				return storeOrder.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}
	
	// 获取
	public StoreOrder getByOrderNumber(String orderNumber) throws Exception {
		try {
			StoreOrder order = dao.queryForBean(
					"select * from tb_storeorder where orderNumber = ?",
					StoreOrder.class, orderNumber);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public StoreOrder getByOrder(int orderId) throws Exception {
		try {
			StoreOrder order = dao.queryForBean(
					"select * from tb_storeorder where orderId = ?",
					StoreOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取生产单
	public StoreOrder get(int id) throws Exception {
		try {
			StoreOrder order = dao.queryForBean(
					"select * from tb_storeorder where id = ?",
					StoreOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			//2015-7-1添加   原材料仓库单执行完成时，将所有的原材料入库单、出库单状态修改为执行完成
			storeInOutService.completeByStoreOrder(orderId);
			return dao
					.update(
							"UPDATE tb_storeorder SET status=?,state=? WHERE orderId = ?",
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
					"UPDATE tb_storeorder SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	//获取所有原材料仓库单
	public List<StoreOrder> getList(){
		return dao.queryForBeanList("select * from tb_storeorder", StoreOrder.class);
	}
	
}
