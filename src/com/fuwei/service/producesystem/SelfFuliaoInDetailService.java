package com.fuwei.service.producesystem;


import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.SelfFuliaoInDetail;
import com.fuwei.service.BaseService;


@Component
public class SelfFuliaoInDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(SelfFuliaoInDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<SelfFuliaoInDetail> getList(int selfFuliaoInId) throws Exception {
		try {
			List<SelfFuliaoInDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_selffuliaoin_detail WHERE selfFuliaoInId=?", SelfFuliaoInDetail.class,selfFuliaoInId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<SelfFuliaoInDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_selffuliaoin_detail(fuliaoPurchaseOrderDetailId,selfFuliaoInId,style,memo,locationId,quantity) VALUES(?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (SelfFuliaoInDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFuliaoPurchaseOrderDetailId(),item.getSelfFuliaoInId(),item.getStyle(),item.getMemo(),item.getLocationId(),item.getQuantity()
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
	public int deleteBatch(int selfFuliaoInId) throws Exception {
		try{
			return dao.update("delete from tb_selffuliaoin_detail WHERE  selfFuliaoInId =?", selfFuliaoInId);
		}catch(Exception e){
			throw e;
		}
	}
}