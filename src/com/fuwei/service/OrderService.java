package com.fuwei.service;

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
import com.fuwei.entity.Order;
import com.fuwei.entity.OrderDetail;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.util.CreateNumberUtil;
import com.fuwei.util.DateTool;


@Component
public class OrderService extends BaseService{
	private Logger log = org.apache.log4j.LogManager.getLogger(QuoteOrderService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	OrderDetailService  orderDetailService;
	
	// 获取订单列表
	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId,Integer salesmanId,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";
			if (companyId != null & salesmanId == null) {
				sql.append("select * from tb_order where companyId='" + companyId + "'");
				seq = " AND ";
			}else{
				sql.append("select * from tb_order ");
			}
			
			
			if (start_time != null) {
				sql.append(seq + " created_at>='" + DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " created_at<='" +  DateTool.formateDate(DateTool.addDay(end_time, 1))+"'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql.append(seq + " salesmanId='" +  salesmanId +"'");
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
			return findPager_T(sql.toString(),QuoteOrder.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加订单,返回主键
	@Transactional
	public int add(Order order) throws Exception {
		try{
			
			if(order.getDetaillist()==null || order.getDetaillist().size()<=0){
				throw new Exception("订单单中至少得有一条样品记录");
			}else{
				Integer orderId = this.insert(order);
				String orderNumber = CreateNumberUtil.createFWStyleNumber(orderId);
				order.setOrderNumber(orderNumber);
				order.setId(orderId);
				this.update(order, "id", null);
				for(OrderDetail detail : order.getDetaillist()){
					detail.setOrderId(orderId);
				}
				orderDetailService.addBatch(order.getDetaillist());
				return orderId;
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	// 删除订单
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_order WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("订单已被引用，无法删除，请先删除引用");
			}
			throw e;
		}
	}
	
	// 获取报价单
	public Order get(int id) throws Exception {
		try {
			Order order = dao.queryForBean(
					"select * from tb_quoteorder where id = ?", Order.class,
					id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}
}
