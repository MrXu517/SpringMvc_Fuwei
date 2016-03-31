package com.fuwei.service.producesystem;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.FuliaoInDetail;
import com.fuwei.service.BaseService;


@Component
public class FuliaoInDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FuliaoInDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<FuliaoInDetail> getList(int fuliaoInNoticeId) throws Exception {
		try {
			List<FuliaoInDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_fuliaoin_detail WHERE fuliaoInOutId=?", FuliaoInDetail.class,fuliaoInNoticeId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
//	public List<Map<String,Object>> getMapList(int fuliaoInOutNoticeId) throws Exception {
//		try {
//			List<Map<String,Object>> List = dao.queryForListMap("SELECT * FROM tb_fuliaoin_detail a , tb_fuliao b WHERE a.fuliaoId=b.id and a.fuliaoInOutId=?", fuliaoInOutNoticeId);
//			return List;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	@Transactional
	public boolean addBatch(List<FuliaoInDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_fuliaoin_detail(locationId,fuliaoInOutId,fuliaoId,quantity,fuliaoPurchaseFactoryId,img,img_s,img_ss,color,size,batch,fuliaoTypeId,company_orderNumber,company_productNumber,country,memo,fnumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoInDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getLocationId(), item.getFuliaoInOutId(),item.getFuliaoId(),item.getQuantity(),item.getFuliaoPurchaseFactoryId()
					,item.getImg(),item.getImg_s(),item.getImg_ss()
					,item.getColor(),item.getSize(),item.getBatch()
					,item.getFuliaoTypeId(),item.getCompany_orderNumber(),item.getCompany_productNumber(),
					item.getCountry(),item.getMemo(),item.getFnumber()
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
			return dao.update("delete from tb_fuliaoin_detail WHERE  fuliaoInOutId =?", fuliaoInOutId);
		}catch(Exception e){
			throw e;
		}
	}
}