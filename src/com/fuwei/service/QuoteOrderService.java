package com.fuwei.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.Quote;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.Sample;
import com.fuwei.util.CreateNumberUtil;
import com.fuwei.util.DateTool;


@Component
public class QuoteOrderService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	QuoteOrderDetailService  quoteOrderDetailService;
	
	@Autowired
	QuoteService quoteService;
	
	// 获取报价单列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from tb_quoteorder ");
			String seq = "WHERE ";
			if (start_time != null) {
				sql.append(seq + " created_at>='" + DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				
				sql.append(seq + " created_at<='" +  DateTool.formateDate(DateTool.addDay(end_time, 1))+"'");
			}

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql.append("order by " + sortlist.get(i).getProperty()
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
	
	// 添加报价单,返回主键
	@Transactional
	public int add(QuoteOrder quoteorder,String ids) throws Exception {
		try{
			
			if(quoteorder.getDetaillist()==null || quoteorder.getDetaillist().size()<=0){
				throw new Exception("报价单中至少得有一条报价记录");
			}else{
				Integer quoteOrderId = this.insert(quoteorder);
				String quotationNumber = CreateNumberUtil.createQuoteOrderNumber(quoteOrderId);
				quoteorder.setQuotationNumber(quotationNumber);
				quoteorder.setId(quoteOrderId);
				this.update(quoteorder, "id", null);
				for(QuoteOrderDetail detail : quoteorder.getDetaillist()){
					detail.setQuoteOrderId(quoteOrderId);
				}
				quoteOrderDetailService.addBatch(quoteorder.getDetaillist());
				//删除报价
				quoteService.batch_remove(ids);
				return quoteOrderId;
			}
		}catch(Exception e){
			throw e;
		}
	}
	

//	// 删除报价
//	public int remove(int id) throws Exception {
//		try{
//			return dao.update("delete from tb_quote WHERE  id = ?", id);
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	// 编辑报价
//	public int update(Quote quote) throws Exception {
//		try{
//			//UPDATE tb_user SET inUse = true WHERE  id = ?
//			return this.update(quote, "id", null,true);
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
	// 获取报价单
	public QuoteOrder get(int id) throws Exception {
		try {
			QuoteOrder quoteOrder = dao.queryForBean(
					"select * from tb_quoteorder where id = ?", QuoteOrder.class,
					id);
			return quoteOrder;
		} catch (Exception e) {
			throw e;
		}
	}
}
