package com.fuwei.entity;

import java.io.Serializable;
import java.util.Date;

import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.Table;

@Table("tb_message")
public class Message implements Serializable {

	/**
	 * 
	 */
	
	@IdentityId
	private int id;
	private int from_user_id;//谁发送出来的
	private int to_user_id;// 发送给谁
	private Boolean has_read;// 是否已读
	private String topic;// 创建时间
	private String content;// 创建时间
	private String to_url;// 创建时间
	private Date created_at;// 创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFrom_user_id() {
		return from_user_id;
	}
	public void setFrom_user_id(int from_user_id) {
		this.from_user_id = from_user_id;
	}
	public int getTo_user_id() {
		return to_user_id;
	}
	public void setTo_user_id(int to_user_id) {
		this.to_user_id = to_user_id;
	}
	public Boolean getHas_read() {
		return has_read;
	}
	public void setHas_read(Boolean has_read) {
		this.has_read = has_read;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTo_url() {
		return to_url;
	}
	public void setTo_url(String to_url) {
		this.to_url = to_url;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	
	
}
