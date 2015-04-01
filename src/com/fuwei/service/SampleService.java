package com.fuwei.service;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.Sample;
import com.fuwei.util.CreateNumberUtil;
import com.fuwei.util.DateTool;
import com.fuwei.util.SerializeTool;

@Component
public class SampleService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(SampleService.class);
	@Autowired
	JdbcTemplate jdbc;

	public Pager getList(Pager pager, Date start_time, Date end_time,Integer charge_user,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from tb_sample ");
			String seq = " where ";
			if (start_time != null) {
				sql.append(seq + "created_at>='" + DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				
				sql.append(seq + "created_at<='" +  DateTool.formateDate(DateTool.addDay(end_time, 1))+"'");
				seq = " AND ";
			}
			if(charge_user!=null){
				sql.append(seq + "charge_user='" +  charge_user +"'");
				seq = " AND ";
			}
			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql.append("order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return findPager_T(sql.toString(),Sample.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}

	// 设置样品的成本与报价详情
	public int setCost_Detail(int sampleId, double cost, String detail)
			throws Exception {
		try {
			return dao
					.update(
							"UPDATE tb_sample SET has_detail=1,cost=?, detail=? WHERE  id = ?",
							cost, detail, sampleId);
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取待核价列表 has_detail = false
	public List<Sample> getUnDetailList(Integer charge_user) throws Exception {
		try {
			List<Sample> sampleList = new ArrayList<Sample>();
			if (charge_user == null) {
				sampleList = dao.queryForBeanList(
						"SELECT * FROM tb_sample WHERE has_detail=0 order by created_at desc",
						Sample.class);
			} else {
				sampleList = dao
						.queryForBeanList(
								"SELECT * FROM tb_sample WHERE has_detail=0 and charge_user=? order by created_at desc",
								Sample.class, charge_user);
			}
			return sampleList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 获取首页的样品列表 has_detail = true
	public List<Sample> getList() throws Exception {
		try {
			List<Sample> sampleList = dao.queryForBeanList(
					"SELECT * FROM tb_sample order by created_at desc", Sample.class);
			return sampleList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加样品
	@Transactional
	public int add(Sample sample) throws Exception {
		try {
			Integer sampleId = this.insert(sample);
			String productNumber = CreateNumberUtil.createSampleProductNumber(sampleId);
			sample.setProductNumber(productNumber);
			sample.setId(sampleId);
			return this.update(sample, "id", null);
		} catch (Exception e) {
			throw e;
		}
	}

	// 删除样品
	public int remove(int id) throws Exception {
		try {
			return dao.update("delete from tb_sample WHERE  id = ?", id);
		} catch (Exception e) {
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("样品已被引用，无法删除，请先删除与样品有关的引用，包括但不仅限于公司价格、报价、报价单等");
			}
			throw e;
		}
	}

	// 编辑样品
	public int update(Sample sample) throws Exception {
		try {
			return this.update(sample, "id",
					"created_user,detail,has_detail,created_at,productNumber", true);
		} catch (Exception e) {
			throw e;
		}

	}

	// 获取样品
	public Sample get(int id) throws Exception {
		try {
			Sample sample = dao.queryForBean(
					"select * from tb_sample where id = ?", Sample.class, id);
			return sample;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//根据名称等搜索样品
	public Pager searchList(Pager pager, String name,Integer charge_user,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from tb_sample ");
			String seq = " where ";
			if (name != null ) {
				if(!name.equals("")){
					sql.append(seq + "name like %'" + name + "'");
					seq = " AND ";
				}
			}
			if(charge_user!=null){
				sql.append(seq + "charge_user='" +  charge_user +"'");
				seq = " AND ";
			}
			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql.append("order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return findPager_T(sql.toString(),Sample.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
}
