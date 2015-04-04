//package com.fuwei.service;
//
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fuwei.commons.Pager;
//import com.fuwei.commons.Sort;
//import com.fuwei.constant.Constants;
//import com.fuwei.entity.Quote;
//import com.fuwei.entity.QuoteOrder;
//import com.fuwei.entity.QuoteOrderDetail;
//import com.fuwei.entity.Sample;
//import com.fuwei.util.CreateNumberUtil;
//import com.fuwei.util.DateTool;
//import com.fuwei.util.ExportExcel;
//
//
//@Component
//public class QuoteOrderService extends BaseService {
//	private Logger log = org.apache.log4j.LogManager.getLogger(QuoteOrderService.class);
//	@Autowired
//	JdbcTemplate jdbc;
//	
//	@Autowired
//	QuoteOrderDetailService  quoteOrderDetailService;
//	
//	@Autowired
//	QuoteService quoteService;
//	
//	// 获取报价单列表
//	public Pager getList(Pager pager, Date start_time, Date end_time,Integer companyId,Integer salesmanId,
//			List<Sort> sortlist) throws Exception {
//		try {
//			StringBuffer sql = new StringBuffer();
//			String seq = "WHERE ";
//			if (companyId != null & salesmanId == null) {
//				sql.append("select q.* from tb_quoteorder q,tb_salesman s where q.salesmanId=s.id AND s.companyId='" + companyId + "'");
//				seq = " AND ";
//			}else{
//				sql.append("select * from tb_quoteorder q ");
//			}
//			
//			
//			if (start_time != null) {
//				sql.append(seq + " q.created_at>='" + DateTool.formateDate(start_time) + "'");
//				seq = " AND ";
//			}
//			if (end_time != null) {
//				sql.append(seq + " q.created_at<='" +  DateTool.formateDate(DateTool.addDay(end_time, 1))+"'");
//				seq = " AND ";
//			}
//			if (salesmanId != null) {
//				sql.append(seq + " q.salesmanId='" +  salesmanId +"'");
//			}
//			
//			if (sortlist != null && sortlist.size() > 0) {
//
//				for (int i = 0; i < sortlist.size(); ++i) {
//					if (i == 0) {
//						sql.append(" order by q." + sortlist.get(i).getProperty()
//								+ " " + sortlist.get(i).getDirection() + " ");
//					} else {
//						sql.append(",q." + sortlist.get(i).getProperty() + " "
//								+ sortlist.get(i).getDirection() + " ");
//					}
//
//				}
//			}
//			return findPager_T(sql.toString(),QuoteOrder.class, pager);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	// 添加报价单,返回主键
//	@Transactional
//	public int add(QuoteOrder quoteorder,String ids,String appPath) throws Exception {
//		try{
//			
//			if(quoteorder.getDetaillist()==null || quoteorder.getDetaillist().size()<=0){
//				throw new Exception("报价单中至少得有一条报价记录");
//			}else{
//				Integer quoteOrderId = this.insert(quoteorder);
//				String quotationNumber = CreateNumberUtil.createQuoteOrderNumber(quoteOrderId);
//				quoteorder.setExcelUrl(Constants.UPLOADEXCEL_QuoteOrder + quoteOrderId+"_"+quoteorder.getExcelUrl());
//				quoteorder.setQuotationNumber(quotationNumber);
//				quoteorder.setId(quoteOrderId);
//				this.update(quoteorder, "id", null);
//				for(QuoteOrderDetail detail : quoteorder.getDetaillist()){
//					detail.setQuoteOrderId(quoteOrderId);
//				}
//				quoteOrderDetailService.addBatch(quoteorder.getDetaillist());
//				//删除报价
//				quoteService.batch_remove(ids);
//				
//				//生成excel文件
//				ExportExcel.exportExcel(quoteorder.getExcelUrl(), appPath, quoteorder ,appPath);
//				return quoteOrderId;
//			}
//		}catch(Exception e){
//			
//			throw e;
//		}
//	}
//	
//	public int updateExcel(String excelUrl,int quoteOrderId){
//		return dao.update("update tb_quoteorder set excelUrl=? where id=? ",excelUrl,quoteOrderId);
//	}
//
//	// 删除报价单
//	public int remove(int id) throws Exception {
//		try{
//			return dao.update("delete from tb_quoteorder WHERE  id = ?", id);
//		}catch(Exception e){
//			SQLException sqlException = (java.sql.SQLException)e.getCause();
//			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
//				log.error(e);
//				throw new Exception("报价单已被引用，无法删除，请先删除订单");
//			}
//			throw e;
//		}
//	}
////
////	// 编辑报价
////	public int update(Quote quote) throws Exception {
////		try{
////			//UPDATE tb_user SET inUse = true WHERE  id = ?
////			return this.update(quote, "id", null,true);
////		}catch(Exception e){
////			throw e;
////		}
////	}
////	
//	// 获取报价单
//	public QuoteOrder get(int id) throws Exception {
//		try {
//			QuoteOrder quoteOrder = dao.queryForBean(
//					"select * from tb_quoteorder where id = ?", QuoteOrder.class,
//					id);
//			return quoteOrder;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//}
