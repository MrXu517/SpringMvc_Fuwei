package com.fuwei.service.financial;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fuwei.entity.financial.Subject;
import com.fuwei.service.BaseService;

@Component
public class SubjectService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(SubjectService.class);
	@Autowired
	JdbcTemplate jdbc;

	// 获取列表
	public List<Subject> getList() throws Exception {
		try {
			List<Subject> subjectList = dao.queryForBeanList(
					"SELECT * FROM tb_subject", Subject.class);
			return subjectList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取列表
	public List<Subject> getList(boolean in_out) throws Exception {
		try {
			List<Subject> subjectList = dao.queryForBeanList(
					"SELECT * FROM tb_subject WHERE in_out=?", Subject.class,in_out);
			return subjectList;
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加
	@Transactional
	public int add(Subject subject) throws Exception {
		try{
			return this.insert(subject);
		}catch(Exception e){
			throw e;
		}
	}

	// 删除
	public int remove(int id) throws Exception {
		try{
			return dao.update("delete from tb_subject WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("科目已被引用，无法删除");
			}
			throw e;
		}
	}

	// 编辑
	public int update(Subject subject) throws Exception {
		try{
			return this.update(subject, "id", "created_at,created_user",true);
		}catch(Exception e){
			throw e;
		}

	}

	// 获取
	public Subject get(int id) throws Exception {
		try {
			Subject subject = dao.queryForBean(
					"select * from tb_subject where id = ?", Subject.class,
					id);
			return subject;
		} catch (Exception e) {
			throw e;
		}
	}
}
