package com.fuwei.service.financial;

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
import com.fuwei.entity.financial.Invoice;
import com.fuwei.entity.financial.ProducingOrderBalance;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;
@Component
public class ProducingOrderBalanceService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(ProducingOrderBalanceService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	public List<ProducingOrderBalance> getList() throws Exception{
		try {
			List<ProducingOrderBalance> list = dao.queryForBeanList(
					"SELECT * FROM tb_producing_order_balance", ProducingOrderBalance.class);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取分页列表
	public Pager getList(Pager pager, Date start_time, Date end_time, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_producing_order_balance");
			
			StringBuffer sql_condition = new StringBuffer();
			
			if (start_time != null) {
				sql_condition.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			
			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			
			pager = findPager_T(sql.append(sql_condition).toString(), ProducingOrderBalance.class, pager);
//			//获取统计数据
//			String [] total_colnames = pager.getTotal_colnames();
//			if(total_colnames == null || total_colnames.length <= 0){
//				return pager;
//			}
//			StringBuffer sql_total = new StringBuffer();
//			sql_total.append("select ");
//			for(String colname : total_colnames){
//				sql_total.append("IFNULL(sum(IFNULL(" + colname+",0)),0) " + colname + ",");
//			}
//			sql_total = new StringBuffer(sql_total.substring(0, sql_total.length()-1));
//			sql_total.append(" from tb_invoice");			
//			Map<String,Object> total_map = dao.queryForMap(sql_total.append(sql_condition).toString(),null);
//			pager.setTotal(total_map);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加
	@Transactional
	public int add(ProducingOrderBalance producingOrderBalance) throws Exception {
		try{
			Integer Id = this.insert(producingOrderBalance);
			producingOrderBalance.setId(Id);
			producingOrderBalance.setNumber(producingOrderBalance.createNumber());
			this.update(producingOrderBalance, "id", null);
			return Id;
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("对账单必须唯一");
			}
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_producing_order_balance WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑
	public int update(ProducingOrderBalance producingOrderBalance) throws Exception {
		try{
			return this.update(producingOrderBalance, "id", "created_at,created_user,number",true);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("对账单号必须唯一");
			}
			throw e;
		}

	}

	// 获取
	public ProducingOrderBalance get(int id) throws Exception {
		try {
			ProducingOrderBalance producingOrderBalance = dao.queryForBean(
					"select * from tb_producing_order_balance where id = ?", ProducingOrderBalance.class,
					id);
			return producingOrderBalance;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean batch_add(List<Invoice> list) throws Exception {
		String sql = "INSERT INTO tb_producing_order_balance(company_id,subject_id,number,print_date,amount,tax,tax_amount,bank_id,bank_name,type,memo,created_at,updated_at,created_user,in_out,match_amount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Invoice item : list) {
			batchArgs.add(new Object[] { 
					item.getCompany_id(),item.getSubject_id(),
					item.getNumber(), item.getPrint_date(),
					item.getAmount(), item.getTax(),
					item.getTax_amount(), item.getBank_id(),
					item.getBank_name(), item.getType(),
					item.getMemo(),
					item.getCreated_at(),item.getUpdated_at(),
					item.getCreated_user(),item.getIn_out(),
					item.getMatch_amount() });
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("发票号必须唯一");
			}
			throw e;
		}

	}
	
}
