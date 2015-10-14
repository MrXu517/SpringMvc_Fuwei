package com.fuwei.service.financial;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.financial.Invoice;
import com.fuwei.entity.ordergrid.ColoringOrder;
import com.fuwei.entity.report.Payable;
import com.fuwei.entity.report.Receivable;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class FinancialReportService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FinancialReportService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 应付报表
	public Pager getPayableList(Pager pager,Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Integer subjectId,Integer bank_id ,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from report_payable");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql_condition.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql_condition.append(seq + " record_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " record_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
		
			
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
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
			
			pager = findPager_T(sql.append(sql_condition).toString(), Payable.class, pager);
			//获取统计数据
			String [] total_colnames = pager.getTotal_colnames();
			if(total_colnames == null || total_colnames.length <= 0){
				return pager;
			}
			StringBuffer sql_total = new StringBuffer();
			sql_total.append("select ");
			for(String colname : total_colnames){
				sql_total.append("IFNULL(sum(IFNULL(" + colname+",0)),0) " + colname + ",");
			}
			sql_total = new StringBuffer(sql_total.substring(0, sql_total.length()-1));
			sql_total.append(" from report_payable");	
			
			String sql_condition_1 = sql_condition.substring(0,sql_condition.indexOf(" order by"));
			String sql_condition_orderby =  sql_condition.substring(sql_condition.indexOf(" order by"));
			Map<String,Object> total_map = dao.queryForMap(sql_total.append(sql_condition).toString(),null);
			
			String pay_personal_sql = "select IFNULL(sum(IFNULL(pay,0)),0) pay_personal from report_payable" + sql_condition_1;
			pay_personal_sql = pay_personal_sql + seq + "is_enterprise=0";
			pay_personal_sql = pay_personal_sql + sql_condition_orderby;
			Map<String,Object> pay_personal_map = dao.queryForMap(pay_personal_sql, null);
			for(String key:pay_personal_map.keySet()){
				total_map.put(key, pay_personal_map.get(key));
			}
			
			pager.setTotal(total_map);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 应付报表  -- 导出
	public List<Payable> getPayableList_export(Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Integer subjectId,Integer bank_id ,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from report_payable");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql_condition.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql_condition.append(seq + " record_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " record_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
		
			
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
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
			
			List<Payable> result = dao.queryForBeanList(sql.append(sql_condition).toString(), Payable.class); 
			
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 应收报表
	public Pager getReceivableList(Pager pager,Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Integer subjectId,Integer bank_id ,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from report_receivable");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql_condition.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql_condition.append(seq + " happen_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " happen_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
		
			
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
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
			
			pager = findPager_T(sql.append(sql_condition).toString(), Receivable.class, pager);
			//获取统计数据
			String [] total_colnames = pager.getTotal_colnames();
			if(total_colnames == null || total_colnames.length <= 0){
				return pager;
			}
			StringBuffer sql_total = new StringBuffer();
			sql_total.append("select ");
			for(String colname : total_colnames){
				sql_total.append("IFNULL(sum(IFNULL(" + colname+",0)),0) " + colname + ",");
			}
			sql_total = new StringBuffer(sql_total.substring(0, sql_total.length()-1));
			sql_total.append(" from report_receivable");	
			
			String sql_condition_1 = sql_condition.substring(0,sql_condition.indexOf(" order by"));
			String sql_condition_orderby =  sql_condition.substring(sql_condition.indexOf(" order by"));
			Map<String,Object> total_map = dao.queryForMap(sql_total.append(sql_condition).toString(),null);
			
			String received_personal_sql = "select IFNULL(sum(IFNULL(received,0)),0) received_personal from report_receivable" + sql_condition_1;
			received_personal_sql = received_personal_sql + seq + "is_enterprise=0";
			received_personal_sql = received_personal_sql + sql_condition_orderby;
			Map<String,Object> pay_personal_map = dao.queryForMap(received_personal_sql, null);
			for(String key:pay_personal_map.keySet()){
				total_map.put(key, pay_personal_map.get(key));
			}
			
			pager.setTotal(total_map);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 应收报表  -- 导出
	public List<Receivable> getReceivableList_export(Date start_time, Date end_time,
			Integer companyId, Integer salesmanId,Integer subjectId,Integer bank_id ,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from report_receivable");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (salesmanId != null) {
				sql_condition.append(seq + " salesman_id='" + salesmanId + "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql_condition.append(seq + " happen_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " happen_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
		
			
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
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
			
			List<Receivable> result = dao.queryForBeanList(sql.append(sql_condition).toString(), Receivable.class); 
			
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
