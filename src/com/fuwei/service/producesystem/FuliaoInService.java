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
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.entity.producesystem.FuliaoIn;
import com.fuwei.entity.producesystem.FuliaoInDetail;
import com.fuwei.entity.producesystem.FuliaoOutDetail;
import com.fuwei.entity.producesystem.StoreInOut;
import com.fuwei.service.BaseService;
import com.fuwei.service.DataCorrectRecordService;
import com.fuwei.util.DateTool;

@Component
public class FuliaoInService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoInService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FuliaoInDetailService fuliaoInOutDetailService;
	@Autowired
	FuliaoInNoticeService fuliaoInNoticeService;
	@Autowired
	LocationService locationService;
	@Autowired
	DataCorrectRecordService dataCorrectRecordService;
	
	// 获取大货辅料入库列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			String orderNumber, Integer charge_employee,
			String number, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select * from tb_fuliaoin where orderId is not null");

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
					FuliaoIn.class, pager);
			List<FuliaoIn> list = (List<FuliaoIn>)pager.getResult();
			if(list==null || list.size()<=0){
				return pager;
			}else{
				String ids = "";
				for(FuliaoIn in : list){
					ids += in.getId()+ ",";
				}
				ids = ids.substring(0,ids.length()-1);
				String tempsql = "select * from tb_fuliaoin_detail  where fuliaoInOutId in (" + ids + ") ";
				List<FuliaoInDetail> totaldetaillist = dao.queryForBeanList(tempsql, FuliaoInDetail.class, null);
				Map<Integer,List<FuliaoInDetail>> map = new HashMap<Integer, List<FuliaoInDetail>>();
				for(FuliaoInDetail detail : totaldetaillist){
					int fuliaoInId = detail.getFuliaoInOutId();
					if(map.containsKey(fuliaoInId)){
						List<FuliaoInDetail> tempL = map.get(fuliaoInId);
						tempL.add(detail);
						map.put(fuliaoInId, tempL);
					}else{
						List<FuliaoInDetail> tempL = new ArrayList<FuliaoInDetail>();
						tempL.add(detail);
						map.put(fuliaoInId, tempL);
					}
				}
				
				for(FuliaoIn in : list){
					in.setDetaillist(map.get(in.getId()));
				}
			}
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取通用辅料入库列表
	public Pager getList_common(Pager pager, Date start_time, Date end_time,
			String number, List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select * from tb_fuliaoin where orderId is null ");//orderId=null则是通用辅料

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
			if (number != null && !number.equals("")) {
				sql_condition.append(seq + " number='" + number + "'");
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
					FuliaoIn.class, pager);
			List<FuliaoIn> list = (List<FuliaoIn>)pager.getResult();
			if(list==null || list.size()<=0){
				return pager;
			}else{
				String ids = "";
				for(FuliaoIn in : list){
					ids += in.getId()+ ",";
				}
				ids = ids.substring(0,ids.length()-1);
				String tempsql = "select * from tb_fuliaoin_detail  where fuliaoInOutId in (" + ids + ") ";
				List<FuliaoInDetail> totaldetaillist = dao.queryForBeanList(tempsql, FuliaoInDetail.class, null);
				Map<Integer,List<FuliaoInDetail>> map = new HashMap<Integer, List<FuliaoInDetail>>();
				for(FuliaoInDetail detail : totaldetaillist){
					int fuliaoInId = detail.getFuliaoInOutId();
					if(map.containsKey(fuliaoInId)){
						List<FuliaoInDetail> tempL = map.get(fuliaoInId);
						tempL.add(detail);
						map.put(fuliaoInId, tempL);
					}else{
						List<FuliaoInDetail> tempL = new ArrayList<FuliaoInDetail>();
						tempL.add(detail);
						map.put(fuliaoInId, tempL);
					}
				}
				
				for(FuliaoIn in : list){
					in.setDetaillist(map.get(in.getId()));
				}
			}
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}

	
	//获取辅料的入库记录
	public List<Map<String,Object>> getByFuliao(int fuliaoId){
		List<Map<String,Object>> result = dao.queryForListMap("select a.*,b.number,b.created_at,b.status from tb_fuliaoin_detail a , tb_fuliaoin b where fuliaoId=? and a.fuliaoInOutId = b.id", fuliaoId);
		return result;
	}
	
	//获取某订单的辅料预入库通知单
	public List<FuliaoIn> getList(int orderId){
		return dao.queryForBeanList("select * from tb_fuliaoin where orderId=?", FuliaoIn.class,orderId);
	}
	public List<FuliaoIn> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_fuliaoin where orderNumber=?", FuliaoIn.class,orderNumber);
	}

	// 添加,返回主键
	@Transactional(rollbackFor=Exception.class)
	public int add(FuliaoIn object) throws Exception {
		try {
			if (object.getFuliaoin_noticeId() == 0) {
				throw new Exception("入库通知单ID不能为空");
			} 
			if (object.getOrderNumber() == null || object.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				if(object.getDetaillist()==null || object.getDetaillist().size()<=0){
					throw new Exception("请至少填写一条入库明细");
				}
				object.setHas_tagprint(false);
				object.setHas_print(false);
				object.setStatus(6);
				object.setState("执行完成");
				Integer fuliaoInId = this.insert(object);
				object.setId(fuliaoInId);
				object.setNumber(object.createNumber());
				this.update(object, "id", null);
				for(FuliaoInDetail detail : object.getDetaillist()){
					if(detail.getFuliaoPurchaseFactoryId()==0){
						detail.setFuliaoPurchaseFactoryId(null);
					}
					detail.setFuliaoInOutId(fuliaoInId);
					//入库后，设置相应的库位不为空
					locationService.addQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
				}
				fuliaoInOutDetailService.addBatch(object.getDetaillist());
				
				
				//入库后，将入库通知单status设为6，表示执行完成，不可再进行删除或编辑
				fuliaoInNoticeService.complete(object.getFuliaoin_noticeId());
				
				
				return fuliaoInId;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public int add_common(FuliaoIn object) throws Exception {
		try {
			if (object.getFuliaoin_noticeId() == 0) {
				throw new Exception("入库通知单ID不能为空");
			} 
			
				if(object.getDetaillist()==null || object.getDetaillist().size()<=0){
					throw new Exception("请至少填写一条入库明细");
				}
				object.setHas_tagprint(false);
				object.setHas_print(false);
				object.setStatus(6);
				object.setState("执行完成");
				Integer fuliaoInId = this.insert(object);
				object.setId(fuliaoInId);
				object.setNumber(object.createNumber());
				this.update(object, "id", null);
				for(FuliaoInDetail detail : object.getDetaillist()){
					detail.setFuliaoInOutId(fuliaoInId);
					//入库后，设置相应的库位不为空
					locationService.addQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
				}
				fuliaoInOutDetailService.addBatch(object.getDetaillist());
				
				
				//入库后，将入库通知单status设为6，表示执行完成，不可再进行删除或编辑
				fuliaoInNoticeService.complete(object.getFuliaoin_noticeId());
				
				
				return fuliaoInId;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	//匹配库位
	public List<Map<String,Object>> matchlocation(int fuliaoId) throws Exception{
		//辅料 的 type必须与location的type一致， 比如通用辅料type=2， 必须放在type=2的库位中
		List<Map<String,Object>> locationMap = locationService.getMapByFuliao(fuliaoId);//找当前已存放的辅料
		if(locationMap == null || locationMap.size()<=0){//辅料若之前未入库，则寻找一个新的空库位放置
			locationMap = dao.queryForListMap("select b.id as fuliaoId,a.id as locationId from tb_location a, tb_fuliao b where a.size = b.location_size and b.id=? and a.isempty=1 and a.type=b.type",fuliaoId);
			if(locationMap == null ||  locationMap.size()<=0){
				throw new Exception("找不到已存放该辅料或空的库位");
			}
			return locationMap;
		}else{//若已入库，则从已入库的库位的挑选一个
			return locationMap;
		}
	}
	// 获取
	public FuliaoIn getAndDetail(int id) throws Exception {
		try {
			FuliaoIn object = dao.queryForBean("select * from tb_fuliaoin where id = ?", FuliaoIn.class, id);
			List<FuliaoInDetail> detaillist = fuliaoInOutDetailService.getList(id);
			object.setDetaillist(detaillist);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取
	public FuliaoIn get(int id) throws Exception {
		try {
			FuliaoIn object = dao.queryForBean("select * from tb_fuliaoin where id = ?", FuliaoIn.class, id);
			List<FuliaoInDetail> detaillist = fuliaoInOutDetailService.getList(id);
			object.setDetaillist(detaillist);
			return object;
		} catch (Exception e) {
			throw e;
		}
	}

	//更新是否打印属性
	@Transactional
	public int updatePrint(FuliaoIn object) throws Exception {
		// 更新表
		dao.update("update tb_fuliaoin set has_print=? where id=?", object.getHas_print(),object.getId());

		return object.getId();
	}
	
	//更新是否打印辅料标签属性
	@Transactional
	public int updateTagPrint(FuliaoIn object) throws Exception {
		// 更新表
		dao.update("update tb_fuliaoin set has_tagprint=? where id=?", object.getHas_tagprint(),object.getId());

		return object.getId();
	}
	
	// 数据纠正_删除
	@Transactional(rollbackFor=Exception.class)
	public int remove_datacorrect(FuliaoIn object,DataCorrectRecord datacorrect) throws Exception {
		try {
			int id = object.getId();
			if(object.isDeletable()){
				throw new Exception("该辅料入库单未执行，无需进行数据纠正");
			}
//			fuliaoInOutDetailService.deleteBatch(id);数据库会自动删除
			//1.修改库位库存信息
			for(FuliaoInDetail detail : object.getDetaillist()){
				//入库单删除后，若库位内数量为0，则库位设为空库位
				locationService.deleteQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
			}
			//2.修改辅料入库通知单为执行失败状态
			fuliaoInNoticeService.fail(object.getFuliaoin_noticeId());
			//3.添加数据纠正记录
			dataCorrectRecordService.add(datacorrect);
			
			return dao.update("delete from tb_fuliaoin WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public int remove(FuliaoIn object) throws Exception {
		try {
			int id = object.getId();
			if(!object.isDeletable()){
				if(object.getStatus() == 6){
					throw new Exception("已执行入库，无法删除");
				}else if(object.getStatus() == -1){
					throw new Exception("已执行入库失败，无法删除");
				}
				
			}
			//1.修改库位库存信息
			for(FuliaoInDetail detail : object.getDetaillist()){
				//入库单删除后，若库位内数量为0，则库位设为空库位
				locationService.deleteQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
			}
			//2.修改辅料入库通知单为执行失败状态
			fuliaoInNoticeService.fail(object.getFuliaoin_noticeId());
			
			return dao.update("delete from tb_fuliaoin WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	
	

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
