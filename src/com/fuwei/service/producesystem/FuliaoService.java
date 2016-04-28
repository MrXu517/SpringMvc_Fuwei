package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class FuliaoService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//获取某订单的辅料
	public List<Fuliao> getList(int orderId){
		return dao.queryForBeanList("select * from tb_fuliao where orderId=? and fuliaoPurchaseOrderDetailId is null", Fuliao.class,orderId);
	}
	public List<Fuliao> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_fuliao where orderNumber=? and fuliaoPurchaseOrderDetailId is null", Fuliao.class,orderNumber);
	}
	//获取某订单的指定辅料的辅料列表
	public List<Fuliao> getList(int orderId,String fuliaoIds){
		if(fuliaoIds == null){
			return getList(orderId);
		}else{
			return dao.queryForBeanList("select * from tb_fuliao where fuliaoPurchaseOrderDetailId is null and orderId=? and id in("+fuliaoIds+")", Fuliao.class,orderId);
		}
	}
	//获取通用辅料列表
	public List<Fuliao> getList_Common(Integer companyId,Integer salesmanId,Integer customerId,String memo){
		StringBuffer sql = new StringBuffer();
		String seq = " AND ";
		sql.append("select * from tb_fuliao where orderId is null and  and fuliaoPurchaseOrderDetailId is null ");

		if (companyId != null) {// 
			sql.append(seq + " companyId='"+companyId+ "'");
			seq = " AND ";
		}
		if (salesmanId != null) {// 
			sql.append(seq + " salesmanId='"+salesmanId+ "'");
			seq = " AND ";
		}
		if (customerId != null) {// 
			sql.append(seq + " customerId='"+customerId+ "'");
			seq = " AND ";
		}
		if (memo != null && !memo.equals("")) {// 出入库时间
			sql.append(seq + " memo like '%"+memo+ "%'");
			seq = " AND ";
		}
		return dao.queryForBeanList(sql.toString(), Fuliao.class);
	}
	//获取通用辅料列表
	public List<Integer> getIdList_Common(Integer companyId,Integer salesmanId,Integer customerId,String memo){
 		StringBuffer sql = new StringBuffer();
		String seq = " AND ";
		sql.append("select id from tb_fuliao where orderId is null and  and fuliaoPurchaseOrderDetailId is null ");

		if (companyId != null) {// 
			sql.append(seq + " companyId='"+companyId+ "'");
			seq = " AND ";
		}
		if (salesmanId != null) {// 
			sql.append(seq + " salesmanId='"+salesmanId+ "'");
			seq = " AND ";
		}
		if (customerId != null) {// 
			sql.append(seq + " customerId='"+customerId+ "'");
			seq = " AND ";
		}
		if (memo != null && !memo.equals("")) {// 出入库时间
			sql.append(seq + " memo like '%"+memo+ "%'");
			seq = " AND ";
		}
		List<Integer> result = new ArrayList<Integer>();
		List<Map<String,Object>> listmap = dao.queryForListMap(sql.toString());
		for(Map<String,Object> item : listmap){
			result.add((Integer)item.get("id"));
		}
		return result;
	}

	// 添加,返回主键
	@Transactional
	public int add(Fuliao fuliao) throws Exception {
		try {

			if (fuliao.getOrderNumber() == null || fuliao.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				fuliao.setType(1);
				Integer fuliaoId = this.insert(fuliao);
				fuliao.setId(fuliaoId);
				fuliao.setFnumber(fuliao.createNumber());
				this.update(fuliao, "id", null);
				return fuliaoId;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加,返回主键
	@Transactional
	public int add_common(Fuliao fuliao) throws Exception {
		try {
			fuliao.setType(2);//通用辅料
			Integer fuliaoId = this.insert(fuliao);
			fuliao.setId(fuliaoId);
			fuliao.setFnumber(fuliao.createNumber());
			this.update(fuliao, "id", null);
			return fuliaoId;
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try {
			return dao.update("delete from tb_fuliao WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	

	// 获取订单
	public Fuliao get(int id) throws Exception {
		try {
			Fuliao fuliao = dao.queryForBean("select * from tb_fuliao where id = ?", Fuliao.class, id);
			return fuliao;
		} catch (Exception e) {
			throw e;
		}
	}
	

	// 编辑
	@Transactional
	public int update(Fuliao fuliao) throws Exception {
		try {
			// 更新
			this.update(fuliao,"id","created_user,created_at,orderNumber,orderId,fnumber,sample_name,charge_employee,type",
								true);
			return fuliao.getId();
		} catch (Exception e) {
			throw e;
		}

	}

}
