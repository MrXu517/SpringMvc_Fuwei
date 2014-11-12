package com.fuwei.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.entity.HeadBankOrder;
import com.fuwei.entity.HeadBankOrderDetail;

@Component
public class HeadBankOrderService extends BaseService {
	private Logger log = org.apache.log4j.LogManager
			.getLogger(QuoteOrderDetailService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 添加头带质量记录单
	@Transactional
	public int add(HeadBankOrder headbankOrder) throws Exception {
		try {
			if (headbankOrder.getDetaillist() == null
					|| headbankOrder.getDetaillist().size() <= 0) {
				throw new Exception("头带质量记录单中至少得有一条详情记录");
			} else {
				Integer headbankOrderId = this.insert(headbankOrder);

				headbankOrder.setId(headbankOrderId);

				for (HeadBankOrderDetail detail : headbankOrder.getDetaillist()) {
					detail.setHeadBankOrderId(headbankOrderId);
				}
				addDetailList(headbankOrder.getDetaillist());

				return headbankOrderId;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	// 编辑单子
	@Transactional
	public int update(HeadBankOrder headBankOrder) throws Exception {
		try {
			if (headBankOrder.getDetaillist() == null
					|| headBankOrder.getDetaillist().size() <= 0) {
				throw new Exception("头带质量记录单中至少得有一条详情记录");
			} else {
				// 更新表
				this.update(headBankOrder, "id",
						"created_user,created_at,orderId", true);

				// 删除原来单子的detail
				deleteDetailList(headBankOrder.getId());
				// 再添加新的detail]
				for (HeadBankOrderDetail detail : headBankOrder.getDetaillist()) {
					detail.setHeadBankOrderId(headBankOrder.getId());
				}
				addDetailList(headBankOrder.getDetaillist());

				return headBankOrder.getId();
			}
		} catch (Exception e) {
			throw e;
		}

	}
	// 获取头套记录单
	public HeadBankOrder getByOrder(int orderId) throws Exception {
		try {
			HeadBankOrder order = dao.queryForBean(
					"select * from tb_headbankorder where orderId = ?",
					HeadBankOrder.class, orderId);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取头套记录单
	public HeadBankOrder get(int id) throws Exception {
		try {
			HeadBankOrder order = dao.queryForBean(
					"select * from tb_headbankorder where id = ?",
					HeadBankOrder.class, id);
			return order;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取订单详情列表
	public List<HeadBankOrderDetail> getDetailList(int headBankOrderId)
			throws Exception {
		try {
			List<HeadBankOrderDetail> detailList = dao
					.queryForBeanList(
							"SELECT * FROM tb_headbankorder_detail WHERE headBankOrderId=?",
							HeadBankOrderDetail.class, headBankOrderId);
			return detailList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加头套质量记录单详情
	@Transactional
	public int addDetailList(List<HeadBankOrderDetail> detailList)
			throws Exception {
		try {
			for (HeadBankOrderDetail detail : detailList) {
				this.insert(detail);
			}
			return 1;
		} catch (Exception e) {
			throw e;
		}
	}

	// 清空头套质量记录单详情
	@Transactional
	public int deleteDetailList(int headBankOrderId) throws Exception {
		try {
			return dao
					.update(
							"delete from tb_headbankorder_detail WHERE  headBankOrderId =?",
							headBankOrderId);
		} catch (Exception e) {
			throw e;
		}
	}
}
