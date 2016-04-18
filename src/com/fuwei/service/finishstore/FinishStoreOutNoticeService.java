package com.fuwei.service.finishstore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.FinishStoreIn;
import com.fuwei.entity.finishstore.FinishStoreInDetail;
import com.fuwei.entity.finishstore.FinishStoreOutNotice;
import com.fuwei.entity.finishstore.FinishStoreOutNoticeDetail;
import com.fuwei.service.BaseService;

@Component
public class FinishStoreOutNoticeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FinishStoreOutNoticeService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FinishStoreOutNoticeDetailService finishStoreOutNoticeDetailService;
	
	
	//获取某订单的成品预出库通知单
	public List<FinishStoreOutNotice> getList(int orderId){
		List<FinishStoreOutNotice> list = dao.queryForBeanList("select * from tb_finishstore_out_notice where orderId=?  order by created_at desc", FinishStoreOutNotice.class,orderId);
		if(list!=null && list.size()>0){
			String ids = "";
			for(FinishStoreOutNotice temp : list){
				ids += temp.getId()+ ",";
			}
			ids = ids.substring(0,ids.length()-1);
			String tempsql = "SELECT a.*,b.color,b.per_carton_quantity,b.per_pack_quantity,b.col1_value,b.col2_value,b.col3_value,b.col4_value FROM tb_finishstore_out_notice_detail a,tb_packingorder_detail b WHERE a.packingOrderDetailId=b.id and  a.finishStoreOutNoticeId in (" + ids + ") ";
			List<FinishStoreOutNoticeDetail> totaldetaillist = dao.queryForBeanList(tempsql, FinishStoreOutNoticeDetail.class, null);
			Map<Integer,List<FinishStoreOutNoticeDetail>> map = new HashMap<Integer, List<FinishStoreOutNoticeDetail>>();
			for(FinishStoreOutNoticeDetail detail : totaldetaillist){
				int finishStoreId = detail.getFinishStoreOutNoticeId();
				if(map.containsKey(finishStoreId)){
					List<FinishStoreOutNoticeDetail> tempL = map.get(finishStoreId);
					tempL.add(detail);
					map.put(finishStoreId, tempL);
				}else{
					List<FinishStoreOutNoticeDetail> tempL = new ArrayList<FinishStoreOutNoticeDetail>();
					tempL.add(detail);
					map.put(finishStoreId, tempL);
				}
			}
			
			for(FinishStoreOutNotice in : list){
				in.setDetaillist(map.get(in.getId()));
			}
		}
		return list;
	}
	public List<FinishStoreOutNotice> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_finishstore_out_notice where orderNumber=?", FinishStoreOutNotice.class,orderNumber);
	}

	// 添加,返回主键
	@Transactional
	public int add(FinishStoreOutNotice notice) throws Exception {
		try {
			
			if (notice.getOrderNumber() == null || notice.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				if(notice.getDetaillist()==null || notice.getDetaillist().size()<=0){
					throw new Exception("请至少填写一条出库明细");
				}
				notice.setStatus(0);
				notice.setState("创建");
				Integer noticeId = this.insert(notice);
				notice.setId(noticeId);
				notice.setNumber(notice.createNumber());
				this.update(notice, "id", null);
				for(FinishStoreOutNoticeDetail detail : notice.getDetaillist()){
					detail.setFinishStoreOutNoticeId(noticeId);
				}
				finishStoreOutNoticeDetailService.addBatch(notice.getDetaillist());
				
				return noticeId;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 删除
	@Transactional
	public int remove(int id) throws Exception {
		try {
			FinishStoreOutNotice notice = this.get(id);
			if(!notice.deletable()){
				if(notice.getStatus() == 6){
					throw new Exception("已执行出库，无法删除");
				}else if(notice.getStatus() == -1){
					throw new Exception("已执行出库失败，无法删除");
				}else{
					throw new Exception("未知原因，无法删除");
				}
				
			}
			return dao.update("delete from tb_finishstore_out_notice WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	

	// 获取
	public FinishStoreOutNotice getAndDetail(int id) throws Exception {
		try {
			FinishStoreOutNotice notice = this.get(id);
			if(notice == null){
				return null;
			}
			List<FinishStoreOutNoticeDetail> detaillist = finishStoreOutNoticeDetailService.getList(id);
			notice.setDetaillist(detaillist);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public FinishStoreOutNotice getAndDetail(String number) throws Exception {
		try {
			FinishStoreOutNotice notice = this.get(number);
			if(notice == null){
				return null;
			}
			List<FinishStoreOutNoticeDetail> detaillist = finishStoreOutNoticeDetailService.getList(notice.getId());
			notice.setDetaillist(detaillist);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取
	public FinishStoreOutNotice get(int id) throws Exception {
		try {
			FinishStoreOutNotice notice = dao.queryForBean(
					"select a.*,b.col1_id,b.col2_id,b.col3_id,b.col4_id from tb_finishstore_out_notice a,tb_packingorder b where a.packingOrderId=b.id and a.id = ?",
					FinishStoreOutNotice.class, id);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}
	public FinishStoreOutNotice get(String number) throws Exception {
		try {
			FinishStoreOutNotice notice = dao.queryForBean("select a.*,b.col1_id,b.col2_id,b.col3_id,b.col4_id from tb_finishstore_out_notice a,tb_packingorder b where a.packingOrderId=b.id and a.number = ?", FinishStoreOutNotice.class, number);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}

	// 编辑
	@Transactional
	public int update(FinishStoreOutNotice notice) throws Exception {
		try {
			FinishStoreOutNotice temp = this.get(notice.getId());
			if(!temp.isEdit()){
				if(temp.getStatus() == 6){
					throw new Exception("已执行出库，无法编辑");
				}else if(temp.getStatus() == -1){
					throw new Exception("已执行出库失败，无法编辑");
				}else{
					throw new Exception("未知原因，无法编辑");
				}
			}
			// 更新
			if(notice.getDetaillist()==null || notice.getDetaillist().size()<=0){
				throw new Exception("请至少填写一条出库明细");
			}
			finishStoreOutNoticeDetailService.deleteBatch(notice.getId());
			for(FinishStoreOutNoticeDetail detail : notice.getDetaillist()){
				detail.setFinishStoreOutNoticeId(notice.getId());
			}
			finishStoreOutNoticeDetailService.addBatch(notice.getDetaillist());
			this.update(notice,"id","charge_employee,name,company_productNumber,companyId,customerId,img,img_s,img_ss,number,packingOrderId,created_user,created_at,orderId,has_print,status,state",
								true);
			return notice.getId();
		} catch (Exception e) {
			throw e;
		}

	}

	@Transactional
	public int complete(int id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_finishstore_out_notice SET status=?,state=? WHERE id = ?",
							6, "执行完成", id);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public int fail(int id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_finishstore_out_notice SET status=?,state=? WHERE id = ?",
							-1, "执行失败", id);
		} catch (Exception e) {
			throw e;
		}
	}
}
