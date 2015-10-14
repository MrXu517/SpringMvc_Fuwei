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
import com.fuwei.entity.ordergrid.CarFixRecordOrder;
import com.fuwei.entity.ordergrid.MaterialPurchaseOrder;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.StoreOrder;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class ProducingOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(ProducingOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加生产单
	@Transactional
	public int add(ProducingOrder producingOrder) throws Exception {
		try {
			if (producingOrder.getDetaillist() == null
					|| producingOrder.getDetaillist().size() <= 0) {
				throw new Exception("生产单中至少得有一条颜色及数量详情记录");
			} else {
				if (producingOrder.getDetail_2_list() == null
						|| producingOrder.getDetail_2_list().size() <= 0) {
					throw new Exception("生产单中至少得有一条生产材料详情记录");
				} else {
					producingOrder.setStatus(0);
					producingOrder.setState("新建");
					producingOrder.setDetail_json(SerializeTool
							.serialize(producingOrder.getDetaillist()));
					producingOrder
							.setDetail_2_json(SerializeTool
									.serialize(producingOrder
											.getDetail_2_list()));

					Integer producingOrderId = this.insert(producingOrder);

					producingOrder.setId(producingOrderId);
					producingOrder.setNumber(producingOrder.createNumber());
					this.update(producingOrder, "id", null);

					return producingOrderId;
				}
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑生产单
	@Transactional
	public int update(ProducingOrder producingOrder) throws Exception {
		try {
			if (producingOrder.getDetaillist() == null
					|| producingOrder.getDetaillist().size() <= 0) {
				throw new Exception("生产单中至少得有一条颜色及数量详情记录");
			} else {
				if (producingOrder.getDetail_2_list() == null
						|| producingOrder.getDetail_2_list().size() <= 0) {
					throw new Exception("生产单中至少得有一条生产材料详情记录");
				} else {
					ProducingOrder temp = this.get(producingOrder.getId());
					if (!temp.isEdit()) {
						throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
					}
					String details = SerializeTool.serialize(producingOrder
							.getDetaillist());
					producingOrder.setDetail_json(details);
					
					producingOrder.setDetail_2_json(SerializeTool
							.serialize(producingOrder
									.getDetail_2_list()));

					// 更新表
					this.update(producingOrder, "id",
							"created_user,created_at,orderId,factoryId,number", true);

					return producingOrder.getId();
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取生产单
	public List<ProducingOrder> getByOrder(int orderId) throws Exception {
		try {
			List<ProducingOrder> list = dao.queryForBeanList("select * from tb_producingorder where orderId = ?", ProducingOrder.class, orderId);
//			if (order.getDetail_json() == null) {
//				order.setDetaillist(new ArrayList<ProducingOrderDetail>());
//				return order;
//			}else{
//				order.setDetaillist(SerializeTool.deserializeList(order
//						.getDetail_json(), ProducingOrderDetail.class));
//			}
//			if (order.getDetail_material_json() == null) {
//				order.setDetail_material_list(new ArrayList<ProducingOrderMaterialDetail>());
//				return order;
//			}else{
//				order.setDetail_material_list(SerializeTool.deserializeList(order
//						.getDetail_json(), ProducingOrderMaterialDetail.class));
//			}
			
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取生产单
	public ProducingOrder get(int id) throws Exception {
		try {
			ProducingOrder order = dao.queryForBean(
					"select * from tb_producingorder where id = ?",
					ProducingOrder.class, id);
//			if (order.getDetail_json() == null) {
//				order.setDetaillist(new ArrayList<HeadBankOrderDetail>());
//				return order;
//			}
//			order.setDetaillist(SerializeTool.deserializeList(order
//					.getDetail_json(), HeadBankOrderDetail.class));
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	//获取未划价生产单列表
	public List<ProducingOrder> getUnpriceList(String orderNumber,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			
			sql.append("select * from tb_producingorder ");
			if (orderNumber != null && !orderNumber.equals("")) {
				sql.append(seq + " orderNumber='" + orderNumber+ "'");
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
			return dao.queryForBeanList(sql.toString(), ProducingOrder.class);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取生产单列表
	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId, Integer factoryId,String orderNumber,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			
			sql.append("select * from tb_producingorder ");

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
			if (orderNumber != null && !orderNumber.equals("")) {
				sql.append(seq + " orderNumber='"
						+ orderNumber + "'");
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
			return findPager_T(sql.toString(), ProducingOrder.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 删除生产单
	public int remove(int id) throws Exception {
		try{
			ProducingOrder temp = this.get(id);
			if(!temp.deletable()){
				throw new Exception("单据已执行完成，无法删除 ");
			}
			return dao.update("delete from tb_producingorder WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与生产单有关的引用");
			}
			throw e;
		}
	}
	
	@Transactional 
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao.update("UPDATE tb_producingorder SET status=?,state=? WHERE orderId = ?", 6,"执行完成", orderId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional 
	public int updateStatus(int tableOrderId ,int status,String state) throws Exception {
		try {
			return dao.update("UPDATE tb_producingorder SET status=?,state=? WHERE id = ?", status,state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public ProducingOrder getByNumber(String number) throws Exception {
		try {
			ProducingOrder order = dao.queryForBean(
					"select * from tb_producingorder where number = ?",
					ProducingOrder.class, number);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

}
