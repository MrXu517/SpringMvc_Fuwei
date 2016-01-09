package com.fuwei.service.financial;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.financial.ProduceBillDetail;
import com.fuwei.service.BaseService;


@Component
public class ProduceBillDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(ProduceBillDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取列表
	public List<ProduceBillDetail> getList(int produceBillId) throws Exception {
		try {
			List<ProduceBillDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_producebilldetail WHERE produceBillId=?", ProduceBillDetail.class,produceBillId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	@Transactional
	public boolean addBatch(List<ProduceBillDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_producebilldetail(produceBillId,producingOrderId,deduct,amount,payable_amount,memo) VALUES(?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (ProduceBillDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getProduceBillId(), item.getProducingOrderId(),item.getDeduct(),
					item.getAmount(),item.getPayable_amount(),item.getMemo()
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
	public int add(ProduceBillDetail object) throws Exception {
		try {
			return this.insert(object);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public int deleteBatch(int produceBillId) throws Exception {
		try{
			return dao.update("delete from tb_producebilldetail WHERE  produceBillId =?", produceBillId);
		}catch(Exception e){
			throw e;
		}
	}
}