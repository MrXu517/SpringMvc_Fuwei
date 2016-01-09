package com.fuwei.service.financial;

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
import com.fuwei.commons.SystemCache;
import com.fuwei.entity.financial.ProduceBill;
import com.fuwei.entity.financial.ProduceBillDetail;
import com.fuwei.entity.financial.ProduceBillDetail_Detail;
import com.fuwei.service.BaseService;
import com.fuwei.service.ordergrid.GongxuProducingOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.util.DateTool;

@Component
public class ProduceBillService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(ProduceBillService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	ProduceBillDetailService produceBillDetailService;
	@Autowired
	ProduceBillDetail_DetailService produceBillDetail_DetailService;
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	GongxuProducingOrderService gongxuProducingOrderService;
	
	// 获取列表
	public List<ProduceBill> getList() throws Exception {
		try {
			String sql = "SELECT * FROM tb_producebill";
			List<ProduceBill> list = dao.queryForBeanList(sql,
					ProduceBill.class);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 根据factoryId、年份获取列表
	public Pager getList(Pager pager, Integer year, Integer factoryId,List<Sort> sortlist)
			throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			String seq = " WHERE ";
			sql.append("select * from tb_producebill");

			StringBuffer sql_condition = new StringBuffer();
			if(year!=null && year!=0){
				sql_condition.append(seq + " year='"+ year + "'");
				seq = " AND ";
			}
			if(factoryId!=null && factoryId!=0){
				sql_condition.append(seq + " factoryId='"+ factoryId + "'");
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
					ProduceBill.class, pager);
			return pager;
		} catch (Exception e) {
			throw e;
		}
	}

	

	// 添加
	@Transactional(rollbackFor=Exception.class)
	public int add(ProduceBill bill) throws Exception {
		try {
			//1.添加对账单
			int produceBillId = this.insert(bill);
			//2.设置对账单明细的对账单ID，添加对账单明细
			for(ProduceBillDetail detail : bill.getDetaillist()){
				detail.setProduceBillId(produceBillId);
				//3.设置生产单inbill属性为true
				if(detail.getGongxuId() == SystemCache.producing_GONGXU.getId()){
					producingOrderService.updateInBill(detail.getProducingOrderId(), true);
				}else{
					gongxuProducingOrderService.updateInBill(detail.getProducingOrderId(), true);
				}
				//添加对账单明细
				int detailId = produceBillDetailService.add(detail);	
				//4.设置对账单明细_明细的明细ID，添加对账单明细_明细
				for(ProduceBillDetail_Detail detail_detail : detail.getDetaillist()){
					detail_detail.setProduceBillDetailId(detailId);
					detail_detail.setProduceBillId(produceBillId);
				}
				produceBillDetail_DetailService.addBatch(detail.getDetaillist());
			}
			return produceBillId;
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除
	@Transactional(rollbackFor=Exception.class)
	public int remove(int produceBillId) throws Exception {
		try {
			ProduceBill bill = this.get(produceBillId);
			//将生产单的inbill改为false
			for(ProduceBillDetail detail : bill.getDetaillist()){
				if(detail.getGongxuId() == SystemCache.producing_GONGXU.getId()){
					producingOrderService.updateInBill(detail.getProducingOrderId(), false);
				}else{
					gongxuProducingOrderService.updateInBill(detail.getProducingOrderId(), false);
				}
			}
			return dao.update("delete from tb_producebill WHERE  id = ?", produceBillId);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑
	@Transactional(rollbackFor=Exception.class)
	public int update(ProduceBill bill) throws Exception {
		try {
			int produceBillId = bill.getId();
			
			//1.将原来的生产单inbill改为false
			dao.update("update tb_producingOrder a, tb_producebilldetail b set a.inbill=0 where b.producebillId=? and a.id=b.producingOrderId", produceBillId);
			dao.update("update tb_gongxu_producingOrder a, tb_producebilldetail b set a.inbill=0 where b.producebillId=? and a.id=b.producingOrderId", produceBillId);
			//2.删除原来的明细
			produceBillDetailService.deleteBatch(produceBillId);
			//3.添加新的明细
			//2.设置对账单明细的对账单ID，添加对账单明细
			for(ProduceBillDetail detail : bill.getDetaillist()){
				detail.setProduceBillId(produceBillId);
				//3.设置生产单inbill属性为true
				//添加对账单明细
				int detailId = produceBillDetailService.add(detail);	
				//4.设置对账单明细_明细的明细ID，添加对账单明细_明细
				for(ProduceBillDetail_Detail detail_detail : detail.getDetaillist()){
					detail_detail.setProduceBillDetailId(detailId);
					detail_detail.setProduceBillId(produceBillId);
				}
				produceBillDetail_DetailService.addBatch(detail.getDetaillist());
			}
			//4.将新的生产单inbill改为true
			dao.update("update tb_producingOrder a, tb_producebilldetail b set a.inbill=1 where b.producebillId=? and a.id=b.producingOrderId", produceBillId);
			dao.update("update tb_gongxu_producingOrder a, tb_producebilldetail b set a.inbill=1 where b.producebillId=? and a.id=b.producingOrderId", produceBillId);
			//5.修改对账单
			return this.update(bill, "id", "factoryId,created_at,created_user", true);
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取
	public ProduceBill get(int produceBillId) throws Exception {
		try {
			ProduceBill obj = dao.queryForBean("select * from tb_producebill where id = ?",
					ProduceBill.class, produceBillId);
			List<ProduceBillDetail> detaillist = produceBillDetailService.getList(produceBillId);
			List<ProduceBillDetail_Detail> detail_detaillist = produceBillDetail_DetailService.getList(produceBillId);
			Map<Integer,List<ProduceBillDetail_Detail>> map = new HashMap<Integer, List<ProduceBillDetail_Detail>>();
			for(ProduceBillDetail_Detail temp : detail_detaillist){
				int producebilldetailId = temp.getProduceBillDetailId();
				if(map.containsKey(producebilldetailId)){
					List<ProduceBillDetail_Detail> tempL = map.get(producebilldetailId);
					tempL.add(temp);
					map.put(producebilldetailId,tempL );
				}else{
					List<ProduceBillDetail_Detail> tempL = new ArrayList<ProduceBillDetail_Detail>();
					tempL.add(temp);
					map.put(producebilldetailId,tempL);
				}
			}
			for(ProduceBillDetail detail : detaillist){
				detail.setDetaillist(map.get(detail.getId()));
			}
			obj.setDetaillist(detaillist);
			
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}
}
