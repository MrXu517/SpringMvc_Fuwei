package com.fuwei.service.finishstore;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.FinishInOutDetail;
import com.fuwei.entity.finishstore.FinishStoreOutDetail;
import com.fuwei.service.BaseService;


@Component
public class FinishStoreOutDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FinishStoreOutDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<FinishStoreOutDetail> getList(int finishStoreInOutId) throws Exception {
		try {
			List<FinishStoreOutDetail> List = dao.queryForBeanList(
					"SELECT a.*,b.color,b.per_carton_quantity,b.per_pack_quantity,b.col1_value,b.col2_value,b.col3_value,b.col4_value FROM tb_finishstore_out_detail a,tb_packingorder_detail b WHERE a.finishStoreInOutId=? and a.packingOrderDetailId=b.id", FinishStoreOutDetail.class,finishStoreInOutId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取详情列表
	public List<FinishInOutDetail> getListToFinishInOut(int finishStoreInOutId) throws Exception {
		try {
			List<FinishInOutDetail> List = dao.queryForBeanList(
					"SELECT a.*,b.color,b.per_carton_quantity,b.per_pack_quantity,b.col1_value,b.col2_value,b.col3_value,b.col4_value FROM tb_finishstore_out_detail a,tb_packingorder_detail b WHERE a.finishStoreInOutId=? and a.packingOrderDetailId=b.id", FinishInOutDetail.class,finishStoreInOutId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FinishStoreOutDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_finishstore_out_detail(finishStoreInOutId,packingOrderDetailId,quantity,cartons,notice_quantity,notice_cartons) VALUES(?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FinishStoreOutDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFinishStoreInOutId(), item.getPackingOrderDetailId(),item.getQuantity(),item.getCartons(),
					item.getNotice_quantity(),item.getNotice_cartons()
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
			return dao.update("delete from tb_finishstore_out_detail WHERE finishStoreInOutId =?", finishStoreInOutId);
		}catch(Exception e){
			throw e;
		}
	}
}