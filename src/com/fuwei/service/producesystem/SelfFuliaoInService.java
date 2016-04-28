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
import com.fuwei.entity.producesystem.SelfFuliaoIn;
import com.fuwei.entity.producesystem.SelfFuliaoInDetail;
import com.fuwei.service.BaseService;
import com.fuwei.service.DataCorrectRecordService;
import com.fuwei.util.DateTool;

@Component
public class SelfFuliaoInService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(SelfFuliaoInService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	SelfFuliaoInDetailService selfFuliaoInDetailService;
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
			String seq = " WHERE ";
			sql.append("select * from tb_selffuliaoin ");

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
					SelfFuliaoIn.class, pager);
			List<SelfFuliaoIn> list = (List<SelfFuliaoIn>)pager.getResult();
			if(list==null || list.size()<=0){
				return pager;
			}else{
				String ids = "";
				for(SelfFuliaoIn in : list){
					ids += in.getId()+ ",";
				}
				ids = ids.substring(0,ids.length()-1);
				String tempsql = "select * from tb_selffuliaoin_detail  where selfFuliaoInId in (" + ids + ") ";
				List<SelfFuliaoInDetail> totaldetaillist = dao.queryForBeanList(tempsql, SelfFuliaoInDetail.class, null);
				Map<Integer,List<SelfFuliaoInDetail>> map = new HashMap<Integer, List<SelfFuliaoInDetail>>();
				for(SelfFuliaoInDetail detail : totaldetaillist){
					int selfFuliaoInId = detail.getSelfFuliaoInId();
					if(map.containsKey(selfFuliaoInId)){
						List<SelfFuliaoInDetail> tempL = map.get(selfFuliaoInId);
						tempL.add(detail);
						map.put(selfFuliaoInId, tempL);
					}else{
						List<SelfFuliaoInDetail> tempL = new ArrayList<SelfFuliaoInDetail>();
						tempL.add(detail);
						map.put(selfFuliaoInId, tempL);
					}
				}
				
				for(SelfFuliaoIn in : list){
					in.setDetaillist(map.get(in.getId()));
				}
			}
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}
	//获取某订单的自购辅料入库单
	public List<SelfFuliaoIn> getList(int orderId){
		return dao.queryForBeanList("select * from tb_selffuliaoin where orderId=? order by created_at desc", SelfFuliaoIn.class,orderId);
	}
	public List<SelfFuliaoIn> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_selffuliaoin where orderNumber=?", SelfFuliaoIn.class,orderNumber);
	}
	//获取某采购单的自购辅料入库单
	public List<SelfFuliaoIn> getByPurchase(int fuliaoPurchaseOrderId){
		return dao.queryForBeanList("select * from tb_selffuliaoin where fuliaoPurchaseOrderId=?", SelfFuliaoIn.class,fuliaoPurchaseOrderId);
	}

	// 添加,返回主键
	@Transactional(rollbackFor=Exception.class)
	public int add(SelfFuliaoIn selfFuliaoIn) throws Exception {
		try {
			
			if (selfFuliaoIn.getOrderNumber() == null || selfFuliaoIn.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				if(selfFuliaoIn.getDetaillist()==null || selfFuliaoIn.getDetaillist().size()<=0){
					throw new Exception("请至少填写一条入库明细");
				}
				selfFuliaoIn.setHas_tagprint(false);
				selfFuliaoIn.setHas_print(false);
				selfFuliaoIn.setStatus(6);
				selfFuliaoIn.setState("执行完成");
				Integer selfFuliaoInId = this.insert(selfFuliaoIn);
				selfFuliaoIn.setId(selfFuliaoInId);
				selfFuliaoIn.setNumber(selfFuliaoIn.createNumber());
				this.update(selfFuliaoIn, "id", null);
				for(SelfFuliaoInDetail detail : selfFuliaoIn.getDetaillist()){
					detail.setSelfFuliaoInId(selfFuliaoInId);
					//入库后，设置相应的库位不为空
					locationService.addQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
				}
				selfFuliaoInDetailService.addBatch(selfFuliaoIn.getDetaillist());
				
				return selfFuliaoInId;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	//匹配库位
	public List<Map<String,Object>> matchlocation(int fuliaoPurchaseOrderDetailId) throws Exception{
		//辅料 的 type必须与location的type一致， 比如通用辅料type=2， 必须放在type=2的库位中
		List<Map<String,Object>> locationMap = locationService.getMapByPurchase(fuliaoPurchaseOrderDetailId);//找当前已存放的辅料
		if(locationMap == null || locationMap.size()<=0){//辅料若之前未入库，则寻找一个新的空库位放置
			locationMap = dao.queryForListMap("select b.id as fuliaoPurchaseOrderDetailId,a.id as locationId from tb_location a, tb_fuliaopurchaseorder_detail b where a.size = b.location_size and b.id=? and a.isempty=1 and a.type=1",fuliaoPurchaseOrderDetailId);
			if(locationMap == null ||  locationMap.size()<=0){
				throw new Exception("找不到已存放该辅料或空的库位");
			}
			return locationMap;
		}else{//若已入库，则从已入库的库位的挑选一个
			return locationMap;
		}
	}
	
	// 获取
	public SelfFuliaoIn get(int id) throws Exception {
		try {
			SelfFuliaoIn selfFuliaoIn = dao.queryForBean("select * from tb_selffuliaoin where id = ?", SelfFuliaoIn.class, id);
			return selfFuliaoIn;
		} catch (Exception e) {
			throw e;
		}
	}
	public SelfFuliaoIn getAndDetail(int id) throws Exception {
		try {
			SelfFuliaoIn selfFuliaoIn = dao.queryForBean("select * from tb_selffuliaoin where id = ?", SelfFuliaoIn.class, id);
			if(selfFuliaoIn!=null){
				List<SelfFuliaoInDetail> detaillist = selfFuliaoInDetailService.getList(selfFuliaoIn.getId());
				selfFuliaoIn.setDetaillist(detaillist);
			}
			return selfFuliaoIn;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public SelfFuliaoIn get(String number) throws Exception {
		try {
			SelfFuliaoIn selfFuliaoIn = dao.queryForBean("select * from tb_selffuliaoin where number = ?", SelfFuliaoIn.class, number);
			return selfFuliaoIn;
		} catch (Exception e) {
			throw e;
		}
	}
	//更新是否打印属性
	@Transactional
	public int updatePrint(SelfFuliaoIn object) throws Exception {
		// 更新表
		dao.update("update tb_selffuliaoin set has_print=? where id=?", object.getHas_print(),object.getId());

		return object.getId();
	}
	//更新是否打印辅料标签属性
	@Transactional
	public int updateTagPrint(SelfFuliaoIn object) throws Exception {
		// 更新表
		dao.update("update tb_selffuliaoin set has_tagprint=? where id=?", object.getHas_tagprint(),object.getId());

		return object.getId();
	}

	// 数据纠正_删除
	@Transactional(rollbackFor=Exception.class)
	public int remove_datacorrect(SelfFuliaoIn object,DataCorrectRecord datacorrect) throws Exception {
		try {
			int id = object.getId();
			if(object.isDeletable()){
				throw new Exception("该辅料入库单未执行，无需进行数据纠正");
			}
			//1.修改库位库存信息
			List<SelfFuliaoInDetail> detaillist = selfFuliaoInDetailService.getList(id);
			for(SelfFuliaoInDetail detail : detaillist){
				//入库单删除后，若库位内数量为0，则库位设为空库位
				locationService.deleteQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			}
			//3.添加数据纠正记录
			dataCorrectRecordService.add(datacorrect);
			
			return dao.update("delete from tb_selffuliaoin WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	// 删除
	@Transactional
	public int remove(int id) throws Exception {
		try {
			SelfFuliaoIn selfFuliaoIn = this.get(id);
			if(!selfFuliaoIn.isDeletable()){
				if(selfFuliaoIn.getStatus() == 6){
					throw new Exception("已执行入库，无法删除");
				}else if(selfFuliaoIn.getStatus() == -1){
					throw new Exception("已执行入库失败，无法删除");
				}
				
			}
			//1.修改库位库存信息
			List<SelfFuliaoInDetail> detaillist = selfFuliaoInDetailService.getList(id);
			for(SelfFuliaoInDetail detail : detaillist){
				//入库单删除后，若库位内数量为0，则库位设为空库位
				locationService.deleteQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			}
			return dao.update("delete from tb_selffuliaoin WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	@Transactional
	public int remove(SelfFuliaoIn selfFuliaoIn) throws Exception {
		try {
			int id = selfFuliaoIn.getId();
			if(!selfFuliaoIn.isDeletable()){
				if(selfFuliaoIn.getStatus() == 6){
					throw new Exception("已执行入库，无法删除");
				}else if(selfFuliaoIn.getStatus() == -1){
					throw new Exception("已执行入库失败，无法删除");
				}
				
			}
			//1.修改库位库存信息
			List<SelfFuliaoInDetail> detaillist = selfFuliaoInDetailService.getList(id);
			for(SelfFuliaoInDetail detail : detaillist){
				//入库单删除后，若库位内数量为0，则库位设为空库位
				locationService.deleteQuantity_purchase(detail.getLocationId(),detail.getFuliaoPurchaseOrderDetailId(),detail.getQuantity());
			}
			return dao.update("delete from tb_selffuliaoin WHERE  id = ?", id);
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
//	@Transactional(rollbackFor=Exception.class)
//	public int update(SelfFuliaoIn selfFuliaoIn) throws Exception {
//		try {
//			int selfFuliaoInId = selfFuliaoIn.getId();
//			SelfFuliaoIn temp = this.get(selfFuliaoInId);
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
//			if(selfFuliaoIn.getDetaillist()==null || selfFuliaoIn.getDetaillist().size()<=0){
//				throw new Exception("请至少填写一条入库明细");
//			}
//			selfFuliaoInDetailService.deleteBatch(selfFuliaoInId);
//			for(SelfFuliaoInDetail detail : selfFuliaoIn.getDetaillist()){
//				detail.setSelfFuliaoInId(selfFuliaoInId);
//			}
//			selfFuliaoInDetailService.addBatch(selfFuliaoIn.getDetaillist());
//			this.update(selfFuliaoIn,"id","fuliaoPurchaseOrderId,created_user,number,created_at,orderNumber,orderId,status,state,charge_employee,name,company_productNumber,companyId,customerId",
//								true);
//			return selfFuliaoIn.getId();
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}

	@Transactional
	public int complete(int id) throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_selffuliaoin SET status=?,state=? WHERE id = ?",
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
							"UPDATE tb_selffuliaoin SET status=?,state=? WHERE id = ?",
							-1, "执行失败", id);
		} catch (Exception e) {
			throw e;
		}
	}
}
