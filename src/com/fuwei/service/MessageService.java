package com.fuwei.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.entity.Message;
import com.fuwei.entity.Order;
import com.fuwei.util.DateTool;


@Component
public class MessageService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(MessageService.class);
	@Autowired
	JdbcTemplate jdbc;
	
	//添加消息
	public int add(Message message)throws Exception{
		try{
			message.setHas_read(false);
			return this.insert(message);
		}catch(Exception e){
			throw e;
		}
	}
	//消息设置为已读
	public int read(int id)throws Exception{
		try{
			return dao.update("UPDATE tb_message SET has_read = true WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}

	//删除消息
	public int delete(int id)throws Exception{
		try{
			Message message = this.get(id);
			if(!message.getHas_read()){
				throw new Exception("消息还未读取，无法删除，请先读取");
			}
			return dao.update("delete from tb_message WHERE  id = ?", id);
		}catch(Exception e){
			SQLException sqlException = (java.sql.SQLException)e.getCause();
			if(sqlException!=null && sqlException.getErrorCode() == 1451){//外键约束
				log.error(e);
				throw new Exception("消息已被引用，无法删除");
			}
			throw e;
		}
	}

	//编辑消息
	public int update(Message message)throws Exception{
		try{
			if(message.getHas_read()){
				throw new Exception("消息已读，无法修改");
			}
			return this.update(message, "id", "created_at",true);
		}catch(Exception e){
			throw e;
		}
	}
	

	// 获取消息列表
	public List<Message> getList() throws Exception {
		try {
			List<Message> messageList = dao.queryForBeanList(
					"SELECT * FROM tb_message", Message.class);
			return messageList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//某用户发出去的消息
	public List<Message> getSendList(int userId) throws Exception {
		try {
			List<Message> messageList = dao.queryForBeanList(
					"SELECT * FROM tb_message where from_user_id=?", Message.class,userId);
			return messageList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//某用户接收到的消息
	public Pager getReceiveList(int userId,Pager pager,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM tb_message where to_user_id=" + userId);
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
			return findPager_T(sql.toString(), Message.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//某用户接收到的未读消息
	public Pager getReceiveList_UnRead(int userId,Pager pager,List<Sort> sortlist) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM tb_message where has_read=false and to_user_id=" + userId);
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
			return findPager_T(sql.toString(), Message.class, pager);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//某用户接收到的未读消息条数
	public int getReceiveList_UnRead_Count(int userId)throws Exception {
		try {
			Integer result = dao.queryForObject("SELECT count(*) as total FROM tb_message where has_read=false and to_user_id=?",Integer.class, userId);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取消息
	public Message get(int id) throws Exception {
		try {
			Message message = dao.queryForBean(
					"select * from tb_message where id = ?", Message.class,
					id);
			return message;
		} catch (Exception e) {
			throw e;
		}
	}

}
