package com.fuwei.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.Announcement;
import com.fuwei.util.DateTool;


@Component
public class AnnouncementService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(AnnouncementService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Announcement> getList() throws Exception {
		try {
			List<Announcement> announcementList = dao.queryForBeanList(
					"SELECT * FROM tb_announcement", Announcement.class);
			return announcementList;
		} catch (Exception e) {
			throw e;
		}
	}
	// 获取列表
	public Pager getList(Pager pager, Date start_time, Date end_time,
			List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM tb_announcement ");
			String seq = " WHERE ";
			if (start_time != null) {
				sql.append(seq + " created_at>='"
						+ DateTool.formateDate(start_time) + "'");
				seq = " AND ";
			}
			if (end_time != null) {
				sql.append(seq + " created_at<'"
						+ DateTool.formateDate(DateTool.addDay(end_time, 1))
						+ "'");
				seq = " AND ";
			}
			if (sortlist != null && sortlist.size() > 0) {

				for (int i = 0; i < sortlist.size(); ++i) {
					if (i == 0) {
						sql.append(" order by " + sortlist.get(i).getProperty()
								+ " " + sortlist.get(i).getDirection() + " ");
					} else {
						sql.append("," + sortlist.get(i).getProperty() + " "
								+ sortlist.get(i).getDirection() + " ");
					}

				}
			}
			return findPager_T(sql.toString(), Announcement.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}


	// 添加
	@Transactional
	public int add(Announcement announcement) throws Exception {
		try{
			announcement.setHomepage(false);
			return this.insert(announcement);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_announcement WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("已被引用，无法删除，请先删除与通知有关的关系");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Announcement announcement) throws Exception {
		try{
			return this.update(announcement, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}
	
	// 获取
	public Announcement get(int id) throws Exception {
		try {
			Announcement announcement = dao.queryForBean(
					"select * from tb_announcement where id = ?", Announcement.class,
					id);
			return announcement;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取显示在首页的通知栏
	public Announcement getHomePage() throws Exception {
		try {
			Announcement announcement = dao.queryForBean(
					"select * from tb_announcement where homepage = 1", Announcement.class);
			return announcement;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 设置为首页置顶显示
	@Transactional
	public int setHomepage(int id) throws Exception {
		try{
			dao.update("update tb_announcement set homepage=0 WHERE  homepage=1");
			return dao.update("update tb_announcement set homepage=1 WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
}
