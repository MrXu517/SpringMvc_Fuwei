package com.fuwei.service.producesystem;


import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.SelfFuliaoOutDetail;
import com.fuwei.service.BaseService;


@Component
public class SelfFuliaoOutDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(SelfFuliaoOutDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<SelfFuliaoOutDetail> getList(int selfFuliaoOutId) throws Exception {
		try {
			List<SelfFuliaoOutDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_selffuliaoout_detail WHERE selfFuliaoOutId=?", SelfFuliaoOutDetail.class,selfFuliaoOutId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<SelfFuliaoOutDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_selffuliaoout_detail(fuliaoPurchaseOrderDetailId,selfFuliaoOutId,style,memo,locationId,quantity) VALUES(?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (SelfFuliaoOutDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFuliaoPurchaseOrderDetailId(),item.getSelfFuliaoOutId(),item.getStyle(),item.getMemo(),item.getLocationId(),item.getQuantity()
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
	public int deleteBatch(int selfFuliaoOutId) throws Exception {
		try{
			return dao.update("delete from tb_selffuliaoout_detail WHERE  selfFuliaoOutId =?", selfFuliaoOutId);
		}catch(Exception e){
			throw e;
		}
	}
}