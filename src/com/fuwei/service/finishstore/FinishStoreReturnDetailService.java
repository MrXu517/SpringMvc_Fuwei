package com.fuwei.service.finishstore;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.FinishStoreReturnDetail;
import com.fuwei.service.BaseService;


@Component
public class FinishStoreReturnDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FinishStoreReturnDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<FinishStoreReturnDetail> getList(int finishStoreReturnId) throws Exception {
		try {
			List<FinishStoreReturnDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_finishstore_return_detail WHERE finishStoreReturnId=?", FinishStoreReturnDetail.class,finishStoreReturnId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FinishStoreReturnDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_finishstore_return_detail(finishStoreReturnId,packingOrderDetailId,quantity,cartons) VALUES(?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FinishStoreReturnDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFinishStoreReturnId(), item.getPackingOrderDetailId(),item.getQuantity(),item.getCartons()
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
	public int deleteBatch(int finishStoreReturnId) throws Exception {
		try{
			return dao.update("delete from tb_finishstore_return_detail WHERE finishStoreReturnId =?", finishStoreReturnId);
		}catch(Exception e){
			throw e;
		}
	}
}