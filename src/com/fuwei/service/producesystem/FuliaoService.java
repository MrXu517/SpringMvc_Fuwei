package com.fuwei.service.producesystem;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.producesystem.Fuliao;
import com.fuwei.service.BaseService;

@Component
public class FuliaoService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(FuliaoService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//获取某订单的辅料
	public List<Fuliao> getList(int orderId){
		return dao.queryForBeanList("select * from tb_fuliao where orderId=?", Fuliao.class,orderId);
	}
	public List<Fuliao> getList(String orderNumber){
		return dao.queryForBeanList("select * from tb_fuliao where orderNumber=?", Fuliao.class,orderNumber);
	}
	//获取某订单的指定辅料的辅料列表
	public List<Fuliao> getList(int orderId,String fuliaoIds){
		if(fuliaoIds == null){
			return getList(orderId);
		}else{
			return dao.queryForBeanList("select * from tb_fuliao where orderId=? and id in("+fuliaoIds+")", Fuliao.class,orderId);
		}
	}

	// 添加,返回主键
	@Transactional
	public int add(Fuliao fuliao) throws Exception {
		try {

			if (fuliao.getOrderNumber() == null || fuliao.getOrderNumber().equals("")) {
				throw new Exception("订单号不能为空");
			} else {
				Integer fuliaoId = this.insert(fuliao);
				fuliao.setId(fuliaoId);
				fuliao.setFnumber(fuliao.createNumber());
				this.update(fuliao, "id", null);
				return fuliaoId;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try {
			return dao.update("delete from tb_fuliao WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException) e.getCause();
			if (sqlException != null && sqlException.getErrorCode() == 1451) {// 外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除相关引用");
			}
			throw e;
		}
	}
	

	// 获取订单
	public Fuliao get(int id) throws Exception {
		try {
			Fuliao fuliao = dao.queryForBean("select * from tb_fuliao where id = ?", Fuliao.class, id);
			return fuliao;
		} catch (Exception e) {
			throw e;
		}
	}
	

	// 编辑
	@Transactional
	public int update(Fuliao fuliao) throws Exception {
		try {
			// 更新
			this.update(fuliao,"id","created_user,created_at,orderNumber,orderId,fnumber,sample_name",
								true);
			return fuliao.getId();
		} catch (Exception e) {
			throw e;
		}

	}

}
