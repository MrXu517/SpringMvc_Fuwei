package com.fuwei.service.ordergrid;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail;
import com.fuwei.service.BaseService;


@Component
public class FuliaoPurchaseOrderDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(FuliaoPurchaseOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public FuliaoPurchaseOrderDetail get(int id) throws Exception {
		try {
			FuliaoPurchaseOrderDetail result = dao.queryForBean("SELECT * FROM tb_fuliaopurchaseorder_detail WHERE id=?", FuliaoPurchaseOrderDetail.class,id);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取详情列表
	public List<FuliaoPurchaseOrderDetail> getList(int fuliaoPurchaseOrderId) throws Exception {
		try {
			List<FuliaoPurchaseOrderDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_fuliaopurchaseorder_detail WHERE fuliaoPurchaseOrderId=?", FuliaoPurchaseOrderDetail.class,fuliaoPurchaseOrderId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<FuliaoPurchaseOrderDetail> detailList) throws Exception {
		if(detailList == null || detailList.size()==0){
			return true;
		}
		String sql = "INSERT INTO tb_fuliaopurchaseorder_detail(fuliaoPurchaseOrderId,style,quantity,memo,location_size) VALUES(?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoPurchaseOrderDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getFuliaoPurchaseOrderId(), item.getStyle(),item.getQuantity(),item.getMemo(),item.getLocation_size()
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
	public boolean updateBatch(List<FuliaoPurchaseOrderDetail> detailList) throws Exception {
		String sql = "update tb_fuliaopurchaseorder_detail set style=?,quantity=?,memo=? where id=?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (FuliaoPurchaseOrderDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getStyle(),item.getQuantity(),item.getMemo(),
					item.getId()
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
	public int deleteBatch(int fuliaoPurchaseOrderId) throws Exception {
		try{
			return dao.update("delete from tb_fuliaopurchaseorder_detail WHERE fuliaoPurchaseOrderId =?", fuliaoPurchaseOrderId);
		}catch(Exception e){
			throw e;
		}
	}
	
	@Transactional
	public int deleteBatch(List<Integer> ids) throws Exception {
		if(ids==null || ids.size()==0){
			return 0;
		}
		String strids = "";
		for(Integer id : ids){
			strids += id + ",";
		}
		strids = strids.substring(0,strids.length()-1);
		try{
			return dao.update("delete from tb_fuliaopurchaseorder_detail WHERE id in('"+strids+"')");
		}catch(Exception e){
			throw e;
		}
	}
}