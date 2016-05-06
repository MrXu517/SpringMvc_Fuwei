package com.fuwei.service.finishstore;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.BaseService;


@Component
public class FinishStoreStockDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FinishStoreStockDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<FinishStoreStockDetail> getList(int finishStoreStockId) throws Exception {
		try {
			List<FinishStoreStockDetail> List = dao.queryForBeanList(
					"SELECT a.*,b.color,b.per_carton_quantity,b.per_pack_quantity,b.col1_value,b.col2_value,b.col3_value,b.col4_value FROM tb_finishstorestock_detail a,tb_packingorder_detail b WHERE a.finishStoreStockId=? and a.packingOrderDetailId=b.id", FinishStoreStockDetail.class,finishStoreStockId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FinishStoreStockDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_finishstorestock_detail(finishStoreStockId,packingOrderDetailId,packingOrderId, stock_quantity,plan_quantity,in_quantity,out_quantity,return_quantity,stock_cartons,plan_cartons,in_cartons,return_cartons,out_cartons) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FinishStoreStockDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFinishStoreStockId(), item.getPackingOrderDetailId(),item.getPackingOrderId(), item.getStock_quantity(),item.getPlan_quantity(),
					item.getIn_quantity(),item.getOut_quantity(),item.getReturn_quantity(),
					item.getStock_cartons(),item.getPlan_cartons(),item.getIn_cartons(),item.getReturn_cartons(),item.getOut_cartons()
			});
		}
		try {
			int result[] = jdbc.batchUpdate(sql, batchArgs);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public int deleteBatch(int finishStoreStockId) throws Exception {
		try{
			return dao.update("delete from tb_finishstorestock_detail WHERE finishStoreStockId =?", finishStoreStockId);
		}catch(Exception e){
			throw e;
		}
	}
}