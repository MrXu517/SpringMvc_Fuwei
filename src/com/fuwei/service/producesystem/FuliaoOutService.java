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
import com.fuwei.entity.Order;
import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.entity.producesystem.FuliaoIn;
import com.fuwei.entity.producesystem.FuliaoInDetail;
import com.fuwei.entity.producesystem.FuliaoOut;
import com.fuwei.entity.producesystem.FuliaoOutDetail;
import com.fuwei.entity.producesystem.Location;
import com.fuwei.service.BaseService;
import com.fuwei.service.DataCorrectRecordService;
import com.fuwei.service.OrderService;
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
	@Autowired
	DataCorrectRecordService dataCorrectRecordService;
	@Autowired
	FuliaoService fuliaoService;
	@Autowired
	OrderService orderService;
	
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			String orderNumber, Integer charge_employee,
			String number,List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select * from tb_fuliaoout where orderId is not null ");

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

	// 获取列表
	public Pager getList_common(Pager pager, Date start_time, Date end_time,
			String number,List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " AND ";
			sql.append("select * from tb_fuliaoout where orderId is null ");

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
	//获取指定ID的多个辅料出库单
	public List<FuliaoOut> getListByIds(String ids){
		List<FuliaoOut> list = dao.queryForBeanList("select * from tb_fuliaoout where id in("+ids+")", FuliaoOut.class);
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
		return list;
	}

	// 添加,返回主键
	@Transactional(rollbackFor=Exception.class)
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
	
	// 添加清库存的辅料出库单,返回出库单主键
	@Transactional(rollbackFor=Exception.class)
	public int addByLocationId(int locationId,int userId) throws Exception {
		try {
			Location location = locationService.get(locationId);
			if(location.getFuliaoId()==null){
				return 0;
			}
			Fuliao fuliao = fuliaoService.get(location.getFuliaoId());
			Order order = orderService.get(fuliao.getOrderId());
			//1.添加辅料出库单
			FuliaoOut fuliaoOut = new FuliaoOut();
			fuliaoOut.setIs_cleaning(true);
			fuliaoOut.setReceiver_employee(null);
			fuliaoOut.setCreated_at(DateTool.now());// 设置创建时间
			fuliaoOut.setCreated_user(userId);// 设置创建人
			fuliaoOut.setFuliaoout_noticeId(null);
			fuliaoOut.setStatus(6);
			fuliaoOut.setState("执行完成");
			if(order!=null){
				fuliaoOut.setCharge_employee(order.getCharge_employee());
				fuliaoOut.setOrderNumber(order.getOrderNumber());
				fuliaoOut.setOrderId(order.getId());
				fuliaoOut.setName(order.getName());
				fuliaoOut.setCompany_productNumber(order.getCompany_productNumber());		
				
			}else{
				fuliaoOut.setOrderNumber(fuliao.getOrderNumber());
				fuliaoOut.setOrderId(fuliao.getOrderId());
				fuliaoOut.setName(fuliao.getSample_name());
				fuliaoOut.setCompany_productNumber(fuliao.getCompany_productNumber());
			}
			
			Integer newFuliaoOutId = this.insert(fuliaoOut);
			fuliaoOut.setId(newFuliaoOutId);
			fuliaoOut.setNumber(fuliaoOut.createNumber());
			this.update(fuliaoOut, "id", null);
			
			//2.添加出库单明细
			List<FuliaoOutDetail> detaillist = new ArrayList<FuliaoOutDetail>();
			FuliaoOutDetail detail = new FuliaoOutDetail();
			detail.setBatch(fuliao.getBatch());
			detail.setColor(fuliao.getColor());
			detail.setCompany_orderNumber(fuliao.getCompany_orderNumber());
			detail.setCompany_productNumber(fuliao.getCompany_productNumber());
			detail.setCountry(fuliao.getCountry());
			detail.setFnumber(fuliao.getFnumber());
			detail.setFuliaoId(fuliao.getId());
			detail.setFuliaoInOutId(newFuliaoOutId);
			detail.setFuliaoTypeId(fuliao.getFuliaoTypeId());
			detail.setImg(fuliao.getImg());
			detail.setImg_s(fuliao.getImg_s());
			detail.setImg_ss(fuliao.getImg_ss());
			detail.setLocationId(locationId);
			detail.setMemo("清空库存");
			detail.setSize(fuliao.getSize());
			//清空库存，将出库数量设为库位的库存数量
			detail.setQuantity(location.getQuantity());
			
			detaillist.add(detail);
					
			//出库后，若库位内数量为0，则库位设为空库位
			locationService.deleteQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
			fuliaoInOutDetailService.addBatch(detaillist);
			fuliaoOut.setDetaillist(detaillist);
			return newFuliaoOutId;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//更新是否打印属性
	@Transactional
	public int updatePrint(FuliaoOut object) throws Exception {
		// 更新表
		dao.update("update tb_fuliaoout set has_print=? where id=?", object.getHas_print(),object.getId());

		return object.getId();
	}
	
	//更新是否打印辅料标签属性
	@Transactional
	public Boolean updatePrint_batch(List<FuliaoOut> list) throws Exception {
		if(list == null || list.size()<=0){
			return true;
		}
		String sql = "update tb_fuliaoout set has_print=? where id=?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoOut item : list) {
			batchArgs.add(new Object[] { 
					item.getHas_print(),item.getId()
			});
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//更新是否打印辅料标签属性
	@Transactional
	public int updateTagPrint(FuliaoOut object) throws Exception {
		// 更新表
		dao.update("update tb_fuliaoout set has_tagprint=? where id=?", object.getHas_tagprint(),object.getId());

		return object.getId();
	}
	
	//更新是否打印辅料标签属性
	@Transactional
	public Boolean updateTagPrint_batch(List<FuliaoOut> list) throws Exception {
		if(list == null || list.size()<=0){
			return true;
		}
		String sql = "update tb_fuliaoout set has_tagprint=? where id=?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoOut item : list) {
			batchArgs.add(new Object[] { 
					item.getHas_tagprint(),item.getId()
			});
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
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

	// 数据纠正_删除
	@Transactional(rollbackFor=Exception.class)
	public int remove_datacorrect(FuliaoOut object,DataCorrectRecord datacorrect) throws Exception {
		try {
			int id = object.getId();
			if(object.isDeletable()){
				throw new Exception("该辅料出库单未执行，无需进行数据纠正");
			}
//			fuliaoInOutDetailService.deleteBatch(id);数据库会自动删除
			//1.修改库位库存信息
			for(FuliaoOutDetail detail : object.getDetaillist()){
				//出库单删除后，设置相应的库位不为空
				locationService.addQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
			}
			//2.修改辅料出库通知单为执行失败状态
			if(object.getFuliaoout_noticeId()!=null){
				fuliaoOutNoticeService.fail(object.getFuliaoout_noticeId());
			}
			//3.添加数据纠正记录
			dataCorrectRecordService.add(datacorrect);
			
			return dao.update("delete from tb_fuliaoout WHERE  id = ?", id);
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
	public int remove(FuliaoOut object) throws Exception {
		try {
			int id = object.getId();
			if(!object.isDeletable()){
				if(object.getStatus() == 6){
					throw new Exception("已执行出库，无法删除");
				}else if(object.getStatus() == -1){
					throw new Exception("已执行出库失败，无法删除");
				}
				
			}
			//1.修改库位库存信息
			for(FuliaoOutDetail detail : object.getDetaillist()){
				//出库单删除后，设置相应的库位不为空
				locationService.addQuantity(detail.getLocationId(),detail.getFuliaoId(),detail.getQuantity());
			}
			//2.修改辅料出库通知单为执行失败状态
			fuliaoOutNoticeService.fail(object.getFuliaoout_noticeId());
			
			return dao.update("delete from tb_fuliaoout WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}

}
