package com.fuwei.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.constant.OrderStatus;
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderHandle;
import com.fuwei.entity.OrderProduceStatus;
import com.fuwei.entity.ProductionNotification;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.service.ordergrid.ColoringOrderService;
import com.fuwei.service.ordergrid.FuliaoPurchaseOrderService;
import com.fuwei.service.ordergrid.GongxuProducingOrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.MaterialPurchaseOrderService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.ordergrid.StoreOrderService;
import com.fuwei.util.CreateNumberUtil;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;
import com.fuwei.constant.OrderStatusUtil;
import com.mysql.jdbc.Statement;

@Component
public class OrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(OrderService.class);
	@Autowired
	JdbcTemplate jdbc;

//	@Autowired
//	OrderDetailService orderDetailService;
	@Autowired
	OrderHandleService orderHandleService;
	@Autowired
	OrderProduceStatusService orderProduceStatusService;
	@Autowired
	ProductionNotificationService productionNotificationService;
	
	//2015-3-18添加
//	@Autowired
//	HeadBankOrderService headBankOrderService;
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	PlanOrderService planOrderService;
	@Autowired
	StoreOrderService storeOrderService;
	@Autowired
	HalfCheckRecordOrderService halfCheckRecordOrderService;
//	@Autowired
//	CheckRecordOrderService checkRecordOrderService;
	@Autowired
	ColoringOrderService coloringOrderService;
	@Autowired
	MaterialPurchaseOrderService materialPurchaseOrderService;
	@Autowired
	FuliaoPurchaseOrderService fuliaoPurchaseOrderService;
//	@Autowired
//	CarFixRecordOrderService carFixRecordOrderService;
//	@Autowired
//	IroningRecordOrderService ironingRecordOrderService;
	//2015-3-18添加
	
	/*2015-3-31添加 新表格*/
//	@Autowired
//	ProductionScheduleOrderService productionScheduleOrderService;
//	@Autowired
//	FinalStoreOrderService finalStoreOrderService;
//	@Autowired
//	ShopRecordOrderService shopRecordOrderService;
//	@Autowired
//	ColoringProcessOrderService coloringProcessOrderService;
	
	/*2015-7-1添加装箱单*/
	@Autowired
	PackingOrderService packingOrderService;
	/*2015-10-18添加工序加工单*/
	@Autowired
	GongxuProducingOrderService gongxuProducingOrderService;
	
	//获取未发货的订单
	public Pager getUnDeliveryList(Pager pager,String orderNumber) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from tb_order where status<" + OrderStatus.DELIVERED.ordinal());
			if (orderNumber != null && !orderNumber.equals("")) {
				sql.append(" and orderNumber='" + orderNumber + "'");
			}
			sql.append(" order by end_at");
			return findPager_T(sql.toString(), Order.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	//获取所有订单
	public List<Order> getList(){
		return dao.queryForBeanList("select * from tb_order", Order.class);
	}
	// 获取订单列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Integer companyId,Integer charge_employee,String company_productNumber, Integer status,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";
			if (companyId != null) {
				sql.append("select * from tb_order where companyId='"
						+ companyId + "'");
				seq = " AND ";
			} else {
				sql.append("select * from tb_order ");
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
//			if (salesmanId != null) {
//				sql.append(seq + " salesmanId='" + salesmanId + "'");
//				seq = " AND ";
//			}
			if (charge_employee != null) {
				sql.append(seq + " charge_employee='" + charge_employee + "'");
				seq = " AND ";
			}
			if (status != null) {
				sql.append(seq + " status='" + status + "'");
			}
			if (company_productNumber != null && !company_productNumber.equals("")) {
				sql.append(seq + " company_productNumber='" + company_productNumber + "'");
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
			return findPager_T(sql.toString(), Order.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加订单,返回主键
	@Transactional
	public int add(Order order, OrderHandle handle) throws Exception {
		try {

			if (order.getSampleId() == null) {
				throw new Exception("订单必须填写样品");
			} else {
				if (order.getDetaillist() == null
						|| order.getDetaillist().size() <= 0) {
					throw new Exception("订单中至少得有一条颜色及数量记录");
				} else {
					order.setDetail_json(SerializeTool.serialize(order
							.getDetaillist()));
					order.setIn_use(true);
					Integer orderId = this.insert(order);
					String orderNumber = CreateNumberUtil
							.createFWStyleNumber(orderId);
					order.setOrderNumber(orderNumber);
					order.setId(orderId);
					this.update(order, "id", null);
					// for (OrderDetail detail : order.getDetaillist()) {
					// detail.setOrderId(orderId);
					// }
					// orderDetailService.addBatch(order.getDetaillist());

					// 添加操作记录
					handle.setOrderId(orderId);
					orderHandleService.add(handle);

					return orderId;
				}
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 删除订单
	public int remove(int id) throws Exception {
		try {
			return dao.update("delete from tb_order WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("订单已被引用，无法删除，请先删除引用");
			}
			throw e;
		}
	}
	
	//注销订单
	public int cancel(int id,OrderHandle handle)throws Exception{
		try{
			Order order = this.get(id);
			if(!order.isCancelable()){
				throw new Exception("订单已发货，无法取消");
			}
			order.setStepId(null);
			order.setStepId(null);
			order.setStep_state(null);
			order.setStatus(OrderStatus.CANCEL.ordinal());
			order.setState(OrderStatus.CANCEL.getName());
			order.setIn_use(false);
			
			//添加操作记录
			handle.setOrderId(order.getId());
			handle.setName("取消订单");
			handle.setState(order.getState());
			handle.setStatus(order.getStatus());
			// 更新订单表
			this.update(order, "id", null);
			// 添加操作记录
			orderHandleService.add(handle);
			return order.getId();
		}catch(Exception e){
			throw e;
		}
	}
	
	// 根据detailId获取订单
	public Order getByDetailId(int detailId) throws Exception {
		try {
			Order order = dao
					.queryForBean(
							"select o.* from tb_order o ,tb_order_detail d where o.id=d.orderId AND d.id = ?",
							Order.class, detailId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取订单
	public Order get(int id) throws Exception {
		try {
			Order order = dao.queryForBean(
					"select * from tb_order where id = ?", Order.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}
	//获取订单所开的生产单ID, List<工厂ID>
	public List<Integer> getProducingOrderFactoryIds(int orderId) throws Exception {
		try {
			List<Integer> factoryIdlist = new ArrayList<Integer>();
			List<ProducingOrder> producingOrderList = producingOrderService.getByOrder(orderId,false);	
			for(ProducingOrder item : producingOrderList){
				factoryIdlist.add(item.getFactoryId());
			}
			return factoryIdlist;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	// 根据orderNumber获取订单
	public Order get(String orderNumber) throws Exception {
		try {
			Order order = dao.queryForBean(
					"select * from tb_order where orderNumber = ?", Order.class, orderNumber);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 编辑订单
	@Transactional
	public int update(Order order, OrderHandle handle) throws Exception {
		try {
			// 更新订单表
			if (order.getDetaillist() == null
					|| order.getDetaillist().size() <= 0) {
				throw new Exception("订单中至少得有一条颜色及数量记录");
			} else {
				String details = SerializeTool.serialize(order.getDetaillist());
				order.setDetail_json(details);
				this
						.update(order,"id","created_user,status,state,created_at,orderNumber,stepId,setp_state,in_use",
								true);
				// // 删除原来订单的detail
				// orderDetailService.deleteBatch(order.getId());
				// // 再添加新的detail
				// orderDetailService.addBatch(order.getDetaillist());

				// 添加操作记录
				orderHandleService.add(handle);

				return order.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// 添加步骤
	public int addstep(OrderProduceStatus orderProduceStatus, OrderHandle handle)
			throws Exception {
		try {
			int primaryId = orderProduceStatusService.add(orderProduceStatus);
			// 添加操作记录
			orderHandleService.add(handle);
			return primaryId;
		} catch (Exception e) {
			throw e;
		}
	}

	// 修改步骤
	public int updatestep(OrderProduceStatus orderProduceStatus,
			OrderHandle handle) throws Exception {
		try {
			int success = orderProduceStatusService.update(orderProduceStatus);
			// 修改当前订单的step_state描述
			Order order = this.get(orderProduceStatus.getOrderId());
			if (order.getStepId() != null
					&& order.getStepId() == orderProduceStatus.getId()
					&& !order.getStep_state().equals(
							orderProduceStatus.getName())) {
				order.setStep_state(orderProduceStatus.getName());
				this
						.update(
								order,
								"id",
								"created_user,status,state,created_at,orderNumber,stepId,in_use",
								true);
			}
			// 添加操作记录
			orderHandleService.add(handle);
			return success;
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除步骤
	public int deletestep(int stepId, OrderHandle handle) throws Exception {
		try {
			int success = orderProduceStatusService.remove(stepId);
			// 添加操作记录
			orderHandleService.add(handle);
			return success;
		} catch (Exception e) {
			throw e;
		}
	}

	// 执行订单
	@Transactional(rollbackFor=Exception.class)
	public int exestep(int orderId,Date step_time, OrderHandle handle) throws Exception {
		try {
			// 获取当前步骤
			Order order = this.get(orderId);
			int status = order.getStatus();
			// 如果当前交易已完成，则不能再执行步骤
			if (status == OrderStatus.COMPLETED.ordinal()) {
				throw new Exception("交易已完成，无法执行其他步骤");
			}
			if (status == OrderStatus.CANCEL.ordinal()) {
				throw new Exception("交易已取消，无法执行其他步骤");
			}
			Integer step = order.getStepId();

			// 若当前执行发货步骤，则修改订单的发货时间
			if (status == OrderStatus.DELIVERING.ordinal()) {
				if(step_time == null){
					step_time = DateTool.now();
				}
				order.setDelivery_at(step_time);
				
				//2015-3-18添加，若当前执行发货步骤，则将所有表格status设为6
//				headBankOrderService.completeByOrder(orderId);
//				carFixRecordOrderService.completeByOrder(orderId);
//				checkRecordOrderService.completeByOrder(orderId);
				coloringOrderService.completeByOrder(orderId);
				fuliaoPurchaseOrderService.completeByOrder(orderId);
				halfCheckRecordOrderService.completeByOrder(orderId);
//				ironingRecordOrderService.completeByOrder(orderId);
				materialPurchaseOrderService.completeByOrder(orderId);
				planOrderService.completeByOrder(orderId);
				producingOrderService.completeByOrder(orderId);
				storeOrderService.completeByOrder(orderId);
				//2015-3-18添加，若当前执行发货步骤，则将所有表格status设为6
				//2015-3-31添加 新单据
//				productionScheduleOrderService.completeByOrder(orderId);
//				coloringProcessOrderService.completeByOrder(orderId);
//				shopRecordOrderService.completeByOrder(orderId);
//				finalStoreOrderService.completeByOrder(orderId);
				//2015-3-31添加 新单据
				
				//2015-7-1添加 执行订单发货时，将装箱单设置为不可修改
				packingOrderService.completeByOrder(orderId);
				
				//2015-10-18添加工序加工单
				gongxuProducingOrderService.completeByOrder(orderId);
			}

			// 2014-11-10 删除if，因为去掉了动态步骤
			// //获取下一步步骤, 若当前执行机织步骤，则不修改status,但修改step,执行后的状态为动态生产步骤
			// if(status ==
			// OrderStatus.MACHINING.ordinal()){//若当前步骤是机织，则要获取动态生产步骤
			// //获取下一步动态生产步骤
			// OrderProduceStatus nextStep =
			// orderProduceStatusService.getNext(order.getId(), step);
			// if(nextStep == null){//如果获取不到下一个步骤，则跳到下一个status
			// OrderStatus orderstatus = OrderStatusUtil.getNext(status);
			// order.setStepId(null);
			// order.setStep_state(null);
			// order.setStatus(orderstatus.ordinal());
			// order.setState(orderstatus.getName());
			// }else{
			// order.setStepId(nextStep.getId());
			// order.setStep_state(nextStep.getName());
			// }
			// }
			// else{//若不是机织，则status 直接+1
			OrderStatus orderstatus = OrderStatusUtil.getNext(status);
			order.setStepId(null);
			order.setStep_state(null);
			order.setStatus(orderstatus.ordinal());
			order.setState(orderstatus.getName());
			// }

			// 更新订单表
			this.update(order, "id", "created_user,created_at,orderNumber,in_use",
					false);
			// 添加操作记录
			handle.setOrderId(orderId);
			if (order.getStepId() != null) {
				handle.setState(order.getStep_state());
				handle.setStatus(order.getStepId());
			} else {
				handle.setState(order.getState());
				handle.setStatus(order.getStatus());
			}

			orderHandleService.add(handle);
			return 1;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 批量执行订单
	@Transactional(rollbackFor=Exception.class)
	public int exestep_batch(int[] orderIds,Date step_time, int userId) throws Exception {
		try {
			for(int orderId : orderIds){
				// 添加操作记录
				OrderHandle handle = new OrderHandle();
				handle.setCreated_at(DateTool.now());
				handle.setCreated_user(userId);
				handle.setName("执行订单步骤");
				exestep(orderId,step_time,handle);
			}
			return 1;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int addNotification(ProductionNotification ProductionNotification)
			throws Exception {
		try {
			productionNotificationService.add(ProductionNotification);
			return 1;
		} catch (Exception e) {
			throw e;
		}
	}

}
