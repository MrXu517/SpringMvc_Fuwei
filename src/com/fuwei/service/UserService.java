package com.fuwei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;

import com.fuwei.entity.Company;
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
	//锁定用户
	public int lock(int id) throws Exception{
		try{
			return dao.update("UPDATE tb_user SET locked = true WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
	//解锁用户
	public int unlock(int id) throws Exception{
		try{
			return dao.update("UPDATE tb_user SET locked = false WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
	
	//锁定用户
	public int lock(int[] ids) throws Exception{
		try{
			return dao.update("UPDATE tb_user SET locked = true WHERE  id in ( ? )", ids);
		}catch(Exception e){
			throw e;
		}
	}
	//解锁用户
	public int unlock(int[] ids) throws Exception{
		try{
			return dao.update("UPDATE tb_user SET locked = false WHERE  id in ( ? )", ids);
		}catch(Exception e){
			throw e;
		}
	}
	
	//添加用户
	public int add(User user)throws Exception{
		try{
			user.setPassword("123456");
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
			return dao.update("delete from tb_user WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
	
	//编辑用户
	public int update(User user)throws Exception{
		try{
			return dao.update("UPDATE tb_user SET name=?,username=?,updated_at=?,help_code=?,roleId=?,tel=?,email=?,qq=? WHERE  id = ?",
					user.getName(),user.getUsername(),user.getUpdated_at(),user.getHelp_code(),user.getRoleId(),user.getTel(),user.getEmail(),user.getQq(),
					user.getId());
		}catch(Exception e){
			throw e;
		}
	}
	
	// 获取用户列表
	public List<User> getList() throws Exception {
		try {
			List<User> userList = dao.queryForBeanList(
					"SELECT * FROM tb_user", User.class);
			return userList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 获取用户
	public User get(int id) throws Exception {
		try {
			User user = dao.queryForBean(
					"select * from tb_user where id = ?", User.class,
					id);
			return user;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//启用用户
	public int enable(int id)throws Exception{
		try{
			return dao.update("UPDATE tb_user SET inUse = true WHERE  id = ?", id);
		}catch(Exception e){
			throw e;
		}
	}
}
