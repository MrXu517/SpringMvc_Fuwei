package com.fuwei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;

import com.fuwei.entity.User;

@Component
public class UserService extends BaseService {
	@Autowired
	JdbcTemplate jdbc;
	
	//用户登录
	public User login(String uname, String pwd) throws Exception{
		try{
			//登录验证用户名与密码
			User user = dao.queryForBean("SELECT * FROM tb_user WHERE username = ?", User.class, uname);
			if(user == null){
				throw new Exception("用户名错误");
			}else if(!user.getInUse()){
				throw new Exception("用户已注销");
			}else if(!(user.getPassword().equals(pwd))){
				throw new Exception("密码错误，请重新登录");
			}
			return user;
		}catch(Exception e){
			throw e;
		}
	}
	
	//添加用户
	public int insert(User user)throws Exception{
		try{
			return this.insert(user);
		}catch(Exception e){
			throw e;
		}
	}
	//注销用户
	public int cancel(int id)throws Exception{
		try{
			return dao.update("UPDATE tb_user SET inUse = false WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
	
	//删除用户
	public int remove(int id)throws Exception{
		try{
			return dao.update("delete form tb_user WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
	
	//编辑用户
	public int update(User user)throws Exception{
		try{
			return this.update(user,"id",null);
		}catch(Exception e){
			throw e;
		}
		
	}
}
