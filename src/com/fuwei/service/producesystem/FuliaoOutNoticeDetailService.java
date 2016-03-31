package com.fuwei.service.producesystem;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.FuliaoOutNoticeDetail;
import com.fuwei.service.BaseService;


@Component
public class FuliaoOutNoticeDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FuliaoOutNoticeDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	
	// 获取详情列表
	public List<FuliaoOutNoticeDetail> getList(int fuliaoInOutNoticeId) throws Exception {
		try {
			List<FuliaoOutNoticeDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_fuliaoout_notice_detail WHERE fuliaoInOutNoticeId=?", FuliaoOutNoticeDetail.class,fuliaoInOutNoticeId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FuliaoOutNoticeDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_fuliaoout_notice_detail(fuliaoInOutNoticeId,fuliaoId,quantity,img,img_s,img_ss,color,size,batch,fuliaoTypeId,company_orderNumber,company_productNumber,country,memo,fnumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoOutNoticeDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFuliaoInOutNoticeId(),item.getFuliaoId(),item.getQuantity()
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
	public int deleteBatch(int fuliaoInOutNoticeId) throws Exception {
		try{
			return dao.update("delete from tb_fuliaoout_notice_detail WHERE  fuliaoInOutNoticeId =?", fuliaoInOutNoticeId);
		}catch(Exception e){
			throw e;
		}
	}
}