package com.fuwei.service.producesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.FuliaoInDetail;
import com.fuwei.entity.producesystem.FuliaoOutDetail;
import com.fuwei.service.BaseService;


@Component
public class FuliaoOutDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FuliaoOutDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<FuliaoOutDetail> getList(int fuliaoOutNoticeId) throws Exception {
		try {
			List<FuliaoOutDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_fuliaoout_detail WHERE fuliaoInOutId=?", FuliaoOutDetail.class,fuliaoOutNoticeId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FuliaoOutDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_fuliaoout_detail(locationId,fuliaoInOutId,fuliaoId,quantity,img,img_s,img_ss,color,size,batch,fuliaoTypeId,company_orderNumber,company_productNumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoOutDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getLocationId(), item.getFuliaoInOutId(),item.getFuliaoId(),item.getQuantity()
					,item.getImg(),item.getImg_s(),item.getImg_ss()
					,item.getColor(),item.getSize(),item.getBatch()
					,item.getFuliaoTypeId(),item.getCompany_orderNumber(),item.getCompany_productNumber()
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
	public int deleteBatch(int fuliaoInOutId) throws Exception {
		try{
			return dao.update("delete from tb_fuliaoout_detail WHERE  fuliaoInOutId =?", fuliaoInOutId);
		}catch(Exception e){
			throw e;
		}
	}
}