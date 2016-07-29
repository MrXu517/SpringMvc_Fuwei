package com.fuwei.service.finishstore;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.BaseService;


@Component
public class PackingOrderDetailService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(PackingOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	// 获取详情列表
	public List<PackingOrderDetail> getList(int packingOrderId) throws Exception {
		try {
			List<PackingOrderDetail> List = dao.queryForBeanList(
					"SELECT * FROM tb_packingorder_detail WHERE packingOrderId=?", PackingOrderDetail.class,packingOrderId);
			return List;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public boolean addBatch(List<PackingOrderDetail> detailList) throws Exception {
		if(detailList == null || detailList.size()==0){
			return true;
		}
		String sql = "INSERT INTO tb_packingorder_detail(packingOrderId,color,quantity,per_carton_quantity,box_L,box_W,box_H,gross_weight,net_weight,cartons,box_number_start,box_number_end,per_pack_quantity,capacity,col1_value,col2_value,col3_value,col4_value,orderId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (PackingOrderDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getPackingOrderId(), item.getColor(),item.getQuantity(),item.getPer_carton_quantity(),
					item.getBox_L(),item.getBox_W(),item.getBox_H(),
					item.getGross_weight(),item.getNet_weight(),item.getCartons(),
					item.getBox_number_start(),item.getBox_number_end(),
					item.getPer_pack_quantity(),item.getCapacity(),
					item.getCol1_value(),item.getCol2_value(),item.getCol3_value(),item.getCol4_value(),
					item.getOrderId()
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
	public boolean updateBatch(List<PackingOrderDetail> detailList) throws Exception {
		String sql = "update tb_packingorder_detail set color=?,quantity=?,per_carton_quantity=?,box_L=?,box_W=?,box_H=?,gross_weight=?,net_weight=?,cartons=?,box_number_start=?,box_number_end=?,per_pack_quantity=?,capacity=?,col1_value=?,col2_value=?,col3_value=?,col4_value=? where id=?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (PackingOrderDetail item : detailList) {
			batchArgs.add(new Object[] { 
					item.getColor(),item.getQuantity(),item.getPer_carton_quantity(),
					item.getBox_L(),item.getBox_W(),item.getBox_H(),
					item.getGross_weight(),item.getNet_weight(),item.getCartons(),
					item.getBox_number_start(),item.getBox_number_end(),
					item.getPer_pack_quantity(),item.getCapacity(),
					item.getCol1_value(),item.getCol2_value(),item.getCol3_value(),item.getCol4_value(),
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
	public int deleteBatch(int packingOrderId) throws Exception {
		try{
			return dao.update("delete from tb_packingorder_detail WHERE packingOrderId =?", packingOrderId);
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
			return dao.update("delete from tb_packingorder_detail WHERE id in("+strids+")");
		}catch(Exception e){
			throw e;
		}
	}
}