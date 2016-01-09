package com.fuwei.service.financial;


import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.financial.ProduceBillDetail;
import com.fuwei.entity.financial.ProduceBillDetail_Detail;
import com.fuwei.service.BaseService;


@Component
public class ProduceBillDetail_DetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(ProduceBillDetail_DetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取列表
	public List<ProduceBillDetail_Detail> getList(int produceBillId) throws Exception {
		try {
			List<ProduceBillDetail_Detail> List = dao.queryForBeanList(
					"SELECT * FROM tb_producebilldetail_detail WHERE produceBillId=?", ProduceBillDetail_Detail.class,produceBillId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	@Transactional
	public boolean addBatch(List<ProduceBillDetail_Detail> detailList) throws Exception {
		String sql = "INSERT INTO tb_producebilldetail_detail(planOrderDetailId,produceBillId,produceBillDetailId,plan_quantity,quantity,color,weight,produce_weight,yarn,size,price,amount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (ProduceBillDetail_Detail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getPlanOrderDetailId(),item.getProduceBillId(),
					item.getProduceBillDetailId(),item.getPlan_quantity(), item.getQuantity(),
					item.getColor(),item.getWeight(),item.getProduce_weight(),
					item.getYarn(),item.getSize(),item.getPrice(),
					item.getAmount()
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
	public int deleteBatch(int produceBillId) throws Exception {
		try{
			return dao.update("delete from tb_producebilldetail_detail WHERE  produceBillId =?", produceBillId);
		}catch(Exception e){
			throw e;
		}
	}
}