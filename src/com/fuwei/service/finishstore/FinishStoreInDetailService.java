package com.fuwei.service.finishstore;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.FinishStoreInDetail;
import com.fuwei.entity.finishstore.FinishStoreStockDetail;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.BaseService;


@Component
public class FinishStoreInDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FinishStoreInDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<FinishStoreInDetail> getList(int finishStoreInOutId) throws Exception {
		try {
			List<FinishStoreInDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_finishstore_in_detail WHERE finishStoreInOutId=?", FinishStoreInDetail.class,finishStoreInOutId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FinishStoreInDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_finishstore_in_detail(finishStoreInOutId,packingOrderDetailId,quantity,cartons) VALUES(?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FinishStoreInDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFinishStoreInOutId(), item.getPackingOrderDetailId(),item.getQuantity(),item.getCartons()
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
	public int deleteBatch(int finishStoreInOutId) throws Exception {
		try{
			return dao.update("delete from tb_finishstore_in_detail WHERE finishStoreInOutId =?", finishStoreInOutId);
		}catch(Exception e){
			throw e;
		}
	}
}