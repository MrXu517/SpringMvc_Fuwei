package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.FuliaoOutNotice;
import com.fuwei.entity.producesystem.FuliaoOutNoticeDetail;
import com.fuwei.service.BaseService;

@Component
public class FuliaoOutNoticeService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoOutNoticeService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FuliaoOutNoticeDetailService fuliaoOutNoticeDetailService;
	
	
	//获取辅料的预出库记录
	public List<Map<String,Object>> getByFuliao(int fuliaoId){
		List<Map<String,Object>> result = dao.queryForListMap("select a.*,b.number,b.created_at,b.status from tb_fuliaoout_notice_detail a , tb_fuliaoout_notice b where fuliaoId=? and a.fuliaoInOutNoticeId = b.id", fuliaoId);
		return result;
	}
	
	//获取某订单的辅料预入库通知单
	public List<FuliaoOutNotice> getList(int orderId){
		return dao.queryForBeanList("select * from tb_fuliaoout_notice where orderId=?  order by created_at desc", FuliaoOutNotice.class,orderId);
	}
	public List<FuliaoOutNotice> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_fuliaoout_notice where orderNumber=?", FuliaoOutNotice.class,orderNumber);
	}

	// 添加,返回主键
	@Transactional
	public int add(FuliaoOutNotice notice) throws Exception {
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
				for(FuliaoOutNoticeDetail detail : notice.getDetaillist()){
					detail.setFuliaoInOutNoticeId(noticeId);
				}
				fuliaoOutNoticeDetailService.addBatch(notice.getDetaillist());
				
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
			FuliaoOutNotice notice = this.get(id);
			if(!notice.isDeletable()){
				if(notice.getStatus() == 6){
					throw new Exception("已执行出库，无法删除");
				}else if(notice.getStatus() == -1){
					throw new Exception("已执行出库失败，无法删除");
				}else{
					throw new Exception("未知原因，无法删除");
				}
				
			}
//			fuliaoOutNoticeDetailService.deleteBatch(id);
			return dao.update("delete from tb_fuliaoout_notice WHERE  id = ?", id);
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
	public FuliaoOutNotice getAndDetail(int id) throws Exception {
		try {
			FuliaoOutNotice notice = dao.queryForBean("select * from tb_fuliaoout_notice where id = ?", FuliaoOutNotice.class, id);
			List<FuliaoOutNoticeDetail> detaillist = fuliaoOutNoticeDetailService.getList(id);
			notice.setDetaillist(detaillist);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public FuliaoOutNotice get(int id) throws Exception {
		try {
			FuliaoOutNotice notice = dao.queryForBean("select * from tb_fuliaoout_notice where id = ?", FuliaoOutNotice.class, id);
//			List<FuliaoOutNoticeDetail> detaillist = fuliaoOutNoticeDetailService.getList(id);
//			notice.setDetaillist(detaillist);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}
	public FuliaoOutNotice get(String number) throws Exception {
		try {
			FuliaoOutNotice notice = dao.queryForBean("select * from tb_fuliaoout_notice where number = ?", FuliaoOutNotice.class, number);
			return notice;
		} catch (Exception e) {
			throw e;
		}
	}

	// 编辑
	@Transactional
	public int update(FuliaoOutNotice notice) throws Exception {
		try {
			FuliaoOutNotice temp = this.get(notice.getId());
			if(!temp.isEditable()){
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
			fuliaoOutNoticeDetailService.deleteBatch(notice.getId());
			for(FuliaoOutNoticeDetail detail : notice.getDetaillist()){
				detail.setFuliaoInOutNoticeId(notice.getId());
			}
			fuliaoOutNoticeDetailService.addBatch(notice.getDetaillist());
			this.update(notice,"id","created_user,number,created_at,orderNumber,orderId,status,state,charge_employee,name,company_productNumber",
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
							"UPDATE tb_fuliaoout_notice SET status=?,state=? WHERE id = ?",
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
							"UPDATE tb_fuliaoout_notice SET status=?,state=? WHERE id = ?",
							-1, "执行失败", id);
		} catch (Exception e) {
			throw e;
		}
	}
}
