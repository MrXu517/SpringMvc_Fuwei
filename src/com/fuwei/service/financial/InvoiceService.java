package com.fuwei.service.financial;
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
import com.fuwei.entity.financial.Invoice;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class InvoiceService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(InvoiceService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			Boolean in_out, Integer bank_id ,Double amount_from , Double amount_to, List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = "WHERE ";
			sql.append("select * from tb_invoice");
			
			if (start_time != null) {
				sql.append(seq + " print_date>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " print_date<='"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			
			if (in_out != null) {
				sql.append(seq + " in_out='" + (in_out == true?"1":0 )+ "'");
				seq = " AND ";
			}
			if (bank_id != null) {
				sql.append(seq + " bank_id='" + bank_id+ "'");
				seq = " AND ";
			}
			
			if (amount_from != null) {
				sql.append(seq + " amount>='" + amount_from+ "'");
				seq = " AND ";
			}
			if (amount_to != null) {
				sql.append(seq + " amount<='" + amount_to+ "'");
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
			return findPager_T(sql.toString(), Invoice.class, pager);
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
}
