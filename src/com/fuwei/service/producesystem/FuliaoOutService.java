package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.producesystem.FuliaoOut;
import com.fuwei.entity.producesystem.FuliaoOutDetail;
import com.fuwei.service.BaseService;
import com.fuwei.util.DateTool;

@Component
public class FuliaoOutService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoOutService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FuliaoOutDetailService fuliaoInOutDetailService;
	@Autowired
	FuliaoOutNoticeService fuliaoOutNoticeService;
	@Autowired
	LocationService locationService;
	
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			String orderNumber, Integer charge_employee,
			String number,List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_fuliaoout");

			StringBuffer sql_condition = new StringBuffer();
			if (start_time != null) {// 出入库时间
				sql_condition.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql_condition.append(seq + " created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time,1))
						+ "'");
				seq = " AND ";
			}
			if (charge_employee != null) {
				sql_condition.append(seq + " charge_employee='"
						+ charge_employee + "'");
				seq = " AND ";
			}
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number + "'");
				seq = " AND ";
			}
			if (orderNumber != null && !orderNumber.equals("")) {
				sql_condition.append(seq + " orderNumber='" + orderNumber + "'");
				seq = " AND ";
			}

			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql_condition.append(" order by "
								+ sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					} else {
						sql_condition.append(","
								+ sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			pager = findPager_T(sql.append(sql_condition).toString(),
					FuliaoOut.class, pager);
			List<FuliaoOut> list = (List<FuliaoOut>)pager.getResult();
			if(list==null || list.size()<=0){
				return pager;
			}else{
				String ids = "";
				for(FuliaoOut in : list){
					ids += in.getId()+ ",";
				}
				ids = ids.substring(0,ids.length()-1);
				String tempsql = "select * from tb_fuliaoout_detail  where fuliaoInOutId in (" + ids + ") ";
				List<FuliaoOutDetail> totaldetaillist = dao.queryForBeanList(tempsql, FuliaoOutDetail.class, null);
				Map<Integer,List<FuliaoOutDetail>> map = new HashMap<Integer, List<FuliaoOutDetail>>();
				for(FuliaoOutDetail detail : totaldetaillist){
					int fuliaoInId = detail.getFuliaoInOutId();
					if(map.containsKey(fuliaoInId)){
						List<FuliaoOutDetail> tempL = map.get(fuliaoInId);
						tempL.add(detail);
						map.put(fuliaoInId, tempL);
					}else{
						List<FuliaoOutDetail> tempL = new ArrayList<FuliaoOutDetail>();
						tempL.add(detail);
						map.put(fuliaoInId, tempL);
					}
				}
				
				for(FuliaoOut in : list){
					in.setDetaillist(map.get(in.getId()));
				}
			}
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}

	
	//获取辅料的出库记录
	public List<Map<String,Object>> getByFuliao(int fuliaoId){
		List<Map<String,Object>> result = dao.queryForListMap("select a.*,b.number,b.created_at,b.status from tb_fuliaoout_detail a , tb_fuliaoout b where fuliaoId=? and a.fuliaoInOutId = b.id", fuliaoId);
		return result;
	}
	
	//获取某订单的辅料出库单
	public List<FuliaoOut> getList(int orderId){
		return dao.queryForBeanList("select * from tb_fuliaoout where orderId=?", FuliaoOut.class,orderId);
	}
	public List<FuliaoOut> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_fuliaoout where orderNumber=?", FuliaoOut.class,orderNumber);
	}

	// 添加,返回主键
	@Transactional
	public int add(FuliaoOut object) throws Exception {
		try {
			if (object.getFuliaoout_noticeId() == 0) {
				throw new Exception("出库通知单ID不能为空");
			} 
			if (object.getOrderNumber() == null || object.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				if(object.getDetaillist()==null || object.getDetaillist().size()<=0){
					throw new Exception("请至少填写一条出库明细");
				}
				object.setStatus(6);
				object.setState("执行完成");
				Integer noticeId = this.insert(object);
				object.setId(noticeId);
				object.setNumber(object.createNumber());
				this.update(object, "id", null);
				for(FuliaoOutDetail detail : object.getDetaillist()){
					detail.setFuliaoInOutId(noticeId);
					//出库后，若库位内数量为0，则库位设为空库位
					locationService.deleteQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
				}
				fuliaoInOutDetailService.addBatch(object.getDetaillist());
				
				
				//出库后，将出库通知单status设为6，表示执行完成，不可再进行删除或编辑
				fuliaoOutNoticeService.complete(object.getFuliaoout_noticeId());
				
				
				return noticeId;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
//	// 获取
//	public FuliaoOut getAndDetail(int id) throws Exception {
//		try {
//			FuliaoOut object = dao.queryForBean("select * from tb_fuliaoout where id = ?", FuliaoOut.class, id);
//			List<FuliaoOutDetail> detaillist = fuliaoInOutDetailService.getList(id);
//			object.setDetaillist(detaillist);
//			return object;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	// 获取
	public FuliaoOut get(int id) throws Exception {
		try {
			FuliaoOut object = dao.queryForBean("select * from tb_fuliaoout where id = ?", FuliaoOut.class, id);
			List<FuliaoOutDetail> detaillist = fuliaoInOutDetailService.getList(id);
			object.setDetaillist(detaillist);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}

//	// 删除
//	@Transactional
//	public int remove(int id) throws Exception {
//		try {
//			FuliaoIn object = this.get(id);
//			if(!object.isDeletable()){
//				if(object.getStatus() == 6){
//					throw new Exception("已执行入库，无法删除");
//				}else if(object.getStatus() == -1){
//					throw new Exception("已执行入库失败，无法删除");
//				}
//				
//			}
//			fuliaoInOutDetailService.deleteBatch(id);
//			return dao.update("delete from tb_fuliaoin WHERE  id = ?", id);
//		} catch (Exception e) {
//			SQLException sqlException = (java.sql.SQLException) e.getCause();
//			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
//				log.error(e);
//				throw new Exception("已被引用，无法删除，请先删除相关引用");
//			}
//			throw e;
//		}
//	}
//	
	

//	// 编辑
//	@Transactional
//	public int update(FuliaoIn object) throws Exception {
//		try {
//			FuliaoIn temp = this.get(object.getId());
//			if(!temp.isEditable()){
//				if(temp.getStatus() == 6){
//					throw new Exception("已执行入库，无法编辑");
//				}else if(temp.getStatus() == -1){
//					throw new Exception("已执行入库失败，无法编辑");
//				}else{
//					throw new Exception("未知原因，无法编辑");
//				}
//			}
//			// 更新
//			if(object.getDetaillist()==null || object.getDetaillist().size()<=0){
//				throw new Exception("请至少填写一条入库明细");
//			}
//			fuliaoInOutDetailService.deleteBatch(object.getId());
//			for(FuliaoInDetail detail : object.getDetaillist()){
//				detail.setFuliaoInOutId(object.getId());
//			}
//			fuliaoInOutDetailService.addBatch(object.getDetaillist());
//			this.update(object,"id","created_user,fuliaoin_noticeId,number,created_at,orderNumber,orderId,status,state,charge_employee",
//								true);
//			return object.getId();
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}

//	@Transactional
//	public int complete(int id) throws Exception {
//		try {
//			return dao
//					.update(
//							"UPDATE tb_fuliaoin SET status=?,state=? WHERE id = ?",
//							6, "执行完成", id);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
//	@Transactional
//	public int fail(int id) throws Exception {
//		try {
//			return dao
//					.update(
//							"UPDATE tb_fuliaoin_notice SET status=?,state=? WHERE id = ?",
//							-1, "执行失败", id);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
}
