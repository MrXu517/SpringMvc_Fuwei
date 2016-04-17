package com.fuwei.service.finishstore;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.FinishStoreOutNoticeDetail;
import com.fuwei.service.BaseService;


@Component
public class FinishStoreOutNoticeDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FinishStoreOutNoticeDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	
	// 获取详情列表
	public List<FinishStoreOutNoticeDetail> getList(int finishStoreOutNoticeId) throws Exception {
		try {
			List<FinishStoreOutNoticeDetail> List = dao.queryForBeanList(
					"SELECT a.*,b.color,b.per_carton_quantity,b.per_pack_quantity,b.col1_value,b.col2_value,b.col3_value,b.col4_value FROM tb_finishstore_out_notice_detail a,tb_packingorder_detail b WHERE a.finishStoreOutNoticeId=? and a.packingOrderDetailId=b.id", FinishStoreOutNoticeDetail.class,finishStoreOutNoticeId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FinishStoreOutNoticeDetail> detailList) throws Exception {
		String sql = "INSERT INTO tb_finishstore_out_notice_detail(finishStoreOutNoticeId,packingOrderDetailId,quantity,cartons) VALUES(?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FinishStoreOutNoticeDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFinishStoreOutNoticeId(),item.getPackingOrderDetailId(),item.getQuantity()
					,item.getCartons()
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
	public int deleteBatch(int finishStoreOutNoticeId) throws Exception {
		try{
			return dao.update("delete from tb_finishstore_out_notice_detail WHERE  finishStoreOutNoticeId =?", finishStoreOutNoticeId);
		}catch(Exception e){
			throw e;
		}
	}
}