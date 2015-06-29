package com.fuwei.service.ordergrid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.ordergrid.PackingOrder;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class PackingOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(PackingOrderService.class);
	@Autowired
	JdbcTemplate jdbc;

	//根据OrderId获取所有
	public List<PackingOrder> getListByOrder(int orderId){
		return dao.queryForBeanList("select * from tb_packingorder where orderId=?", PackingOrder.class,orderId);
	}
	
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId, String number,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";

			sql.append("select aT.*,companyId,salesmanId,customerId,sampleId,name,img,materialId,weight,size,productNumber,orderNumber,img_s,img_ss,charge_employee,company_productNumber from tb_packingorder aT , tb_order bT where aT.orderId=bT.id ");
			if (number != null && !number.equals("")) {
				sql.append(seq + " orderNumber='"
						+ number + "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql.append(seq + " aT.created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " aT.created_at<='"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (companyId != null) {
				sql.append(seq + " companyId='" + companyId + "'");
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
			return findPager_T(sql.toString(), PackingOrder.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加
	@Transactional
	public int add(PackingOrder packingOrder) throws Exception {
		try {
			if (packingOrder.getOrderId() == null) {
				throw new Exception("订单ID不能为空");
			}
			if (packingOrder.getFilepath() == null) {
				throw new Exception("装箱单EXCEL文件路径不能为空");
			}
			if (packingOrder.getPdfpath() == null) {
				throw new Exception("装箱单PDF文件路径不能为空");
			}
			packingOrder.setStatus(0);
			packingOrder.setState("新建");
			Integer id = this.insert(packingOrder);
			packingOrder.setId(id);
			return id;
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑
	@Transactional
	public int update(PackingOrder packingOrder) throws Exception {
		try {
			if (packingOrder.getOrderId() == null) {
				throw new Exception("订单ID不能为空");
			}
			if (packingOrder.getFilepath() == null) {
				throw new Exception("装箱单EXCEL文件路径不能为空");
			}
			if (packingOrder.getPdfpath() == null) {
				throw new Exception("装箱单PDF文件路径不能为空");
			}
			PackingOrder temp = this.get(packingOrder.getId());
			if (!temp.isEdit()) {
				throw new Exception("单据已执行完成，或已被取消，无法编辑 ");
			}
			// 更新表
			this.update(packingOrder, "id",
					"created_user,created_at,orderId", true);

			return packingOrder.getId();
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取
	public PackingOrder getByOrder(int orderId) throws Exception {
		try {
			PackingOrder order = dao.queryForBean(
					"select * from tb_packingorder where orderId = ?",
					PackingOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取
	public PackingOrder get(int id) throws Exception {
		try {
			PackingOrder packingOrder = dao.queryForBean(
					"select * from tb_packingorder where id = ?",
					PackingOrder.class, id);
			return packingOrder;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public int completeByOrder(int orderId) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_packingorder SET status=?,state=? WHERE orderId = ?",
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
					"UPDATE tb_packingorder SET status=?,state=? WHERE id = ?",
					status, state, tableOrderId);
		} catch (Exception e) {
			throw e;
		}
	}

	//获取所有
	public List<PackingOrder> getList(){
		return dao.queryForBeanList("select * from tb_packingorder", PackingOrder.class);
	}
	
	// 删除
	public int remove(int id) throws Exception {
		try {
			PackingOrder temp = this.get(id);
			if(!temp.deletable()){
				throw new Exception("单据已执行完成，无法删除 ");
			}
			return dao.update("delete from tb_packingorder WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与装箱单有关的引用");
			}
			throw e;
		}
	}
}
