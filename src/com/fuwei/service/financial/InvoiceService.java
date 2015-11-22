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
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class InvoiceService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(InvoiceService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	public List<Invoice> getInvoiceList(Integer bank_id,boolean in_out) throws Exception{
		try {
			List<Invoice> invoiceList = dao.queryForBeanList(
					"SELECT * FROM tb_invoice WHERE bank_id=? and amount<>match_amount and in_out=?", Invoice.class,bank_id,in_out);
			return invoiceList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Invoice> getInvoiceList(Integer bank_id,boolean in_out,Integer subject_id) throws Exception{
		try {
			if(subject_id == null){
				return getInvoiceList(bank_id,in_out);
			}else{
				List<Invoice> invoiceList = dao.queryForBeanList(
						"SELECT * FROM tb_invoice WHERE bank_id=? and amount<>match_amount and in_out=? and subject_id=?", Invoice.class,bank_id,in_out,subject_id);
				return invoiceList;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Invoice> getBeforeDateInvoiceList(Integer bank_id,boolean in_out,Integer subject_id,Date date) throws Exception{
		try {
			if(date == null){
				return getInvoiceList(bank_id,in_out,subject_id);
			}else{
				List<Invoice> invoiceList = dao.queryForBeanList(
						"SELECT * FROM tb_invoice WHERE bank_id=? and amount<>match_amount and in_out=? and subject_id=? and print_date<?", Invoice.class,bank_id,in_out,subject_id,date);
				return invoiceList;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Invoice> getByIds(String ids) throws Exception{
		if(ids == null || ids.equals("")){
			return null;
		}
		
		try {
			List<Invoice> invoiceList = dao.queryForBeanList(
					"SELECT * FROM tb_invoice WHERE id in ("+ids+")", Invoice.class);
			return invoiceList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Invoice> getList(Integer bank_id) throws Exception{
		try {
			List<Invoice> invoiceList = dao.queryForBeanList(
					"SELECT * FROM tb_invoice WHERE bank_id=?", Invoice.class,bank_id);
			return invoiceList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 根据开票日期获取分页列表
	public Pager getListByPrint_date(Pager pager,Boolean unpaid, Date start_time, Date end_time,
			Integer companyId, Integer subjectId,
			Boolean in_out, Integer bank_id ,Double amount_from , Double amount_to,String number, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_invoice");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql_condition.append(seq + " print_date>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " print_date<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (unpaid != null) {
				if(unpaid){
					sql_condition.append(seq + " amount>match_amount");
					seq = " AND ";
				}else{
					sql_condition.append(seq + " amount=match_amount");
					seq = " AND ";
				}
			}
			if (in_out != null) {
				sql_condition.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			
			if (amount_from != null) {
				sql_condition.append(seq + " amount>='" + amount_from+ "'");
				seq = " AND ";
			}
			if (amount_to != null) {
				sql_condition.append(seq + " amount<='" + amount_to+ "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number+ "'");
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
			
			pager = findPager_T(sql.append(sql_condition).toString(), Invoice.class, pager);
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
			sql_total.append(" from tb_invoice");			
			Map<String,Object> total_map = dao.queryForMap(sql_total.append(sql_condition).toString(),null);
			pager.setTotal(total_map);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取分页列表
	public Pager getList(Pager pager,Boolean unpaid, Date start_time, Date end_time,
			Integer companyId, Integer subjectId,
			Boolean in_out, Integer bank_id ,Double amount_from , Double amount_to,String number, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_invoice");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
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
			if (unpaid != null) {
				if(unpaid){
					sql_condition.append(seq + " amount>match_amount");
					seq = " AND ";
				}else{
					sql_condition.append(seq + " amount=match_amount");
					seq = " AND ";
				}
			}
			if (in_out != null) {
				sql_condition.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			
			if (amount_from != null) {
				sql_condition.append(seq + " amount>='" + amount_from+ "'");
				seq = " AND ";
			}
			if (amount_to != null) {
				sql_condition.append(seq + " amount<='" + amount_to+ "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number+ "'");
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
			
			pager = findPager_T(sql.append(sql_condition).toString(), Invoice.class, pager);
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
			sql_total.append(" from tb_invoice");			
			Map<String,Object> total_map = dao.queryForMap(sql_total.append(sql_condition).toString(),null);
			pager.setTotal(total_map);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 添加
	@Transactional
	public int add(Invoice invoice) throws Exception {
		try{
			return this.insert(invoice);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("发票号必须唯一");
			}
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_invoice WHERE  id = ?", id);
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
	public int update(Invoice invoice) throws Exception {
		try{
			return this.update(invoice, "id", "created_at,created_user",true);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1062){//外键约束
				log.error(e);
				throw new Exception("发票号必须唯一");
			}
			throw e;
		}

	}

	// 获取
	public Invoice get(int id) throws Exception {
		try {
			Invoice invoice = dao.queryForBean(
					"select * from tb_invoice where id = ?", Invoice.class,
					id);
			return invoice;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public Invoice get(int id,Boolean in_out) throws Exception {
		try {
			Invoice invoice = dao.queryForBean(
					"select * from tb_invoice where id = ? and in_out=?", Invoice.class,
					id,in_out);
			return invoice;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean batch_add(List<Invoice> list) throws Exception {
		String sql = "INSERT INTO tb_invoice(company_id,subject_id,number,print_date,amount,tax,tax_amount,bank_id,bank_name,type,memo,created_at,updated_at,created_user,in_out,match_amount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
	
	
	// 获取分页列表
	public Pager getSaleInvoiceReport(Pager pager,Boolean unpaid, Date start_time, Date end_time,
			Integer companyId, Integer subjectId,
			Boolean in_out, Integer bank_id ,Double amount_from , Double amount_to,String number, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_invoice");
			
			StringBuffer sql_condition = new StringBuffer();
			if (companyId != null) {
				sql_condition.append(seq + " company_id='" + companyId+ "'");
				seq = " AND ";
			}
			if (subjectId != null) {
				sql_condition.append(seq + " subject_id='" + subjectId+ "'");
				seq = " AND ";
			}
			if (start_time != null) {
				sql_condition.append(seq + " print_date>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " print_date<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (unpaid != null) {
				if(unpaid){
					sql_condition.append(seq + " amount>match_amount");
					seq = " AND ";
				}else{
					sql_condition.append(seq + " amount=match_amount");
					seq = " AND ";
				}
			}
			if (in_out != null) {
				sql_condition.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql_condition.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			
			if (amount_from != null) {
				sql_condition.append(seq + " amount>='" + amount_from+ "'");
				seq = " AND ";
			}
			if (amount_to != null) {
				sql_condition.append(seq + " amount<='" + amount_to+ "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number+ "'");
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
			
			pager = findPager_T(sql.append(sql_condition).toString(), Invoice.class, pager);
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
			sql_total.append(" from tb_invoice");			
			Map<String,Object> total_map = dao.queryForMap(sql_total.append(sql_condition).toString(),null);
			pager.setTotal(total_map);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
