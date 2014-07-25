package com.fuwei.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fuwei.entity.Authority;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.Role_Authority;
@Component
public class AuthorityService extends BaseService {
	private Logger log = org.apache.log4j.LogManager.getLogger(AuthorityService.class);
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	UserService userService;
	
	//获取某模块详情
	public Authority get(int id) throws Exception{
		try{
			Authority authority = dao.queryForBean("SELECT * FROM tb_authority WHERE id = ?", Authority.class, id);
			if(authority == null){
				throw new Exception("没有该模块");
			}
			return authority;
		}catch(Exception e){
			throw e;
		}
	}
	
	//根据角色roleID获取模块
	public List<Authority> getList(int roleId) throws Exception{
		try{
			List<Authority> authorityList = dao.queryForBeanList("SELECT * FROM tb_authority WHERE id in (SELECT authorityId FROM tb_role_authority WHERE roleId = ?)", Authority.class, roleId);
			return authorityList;
		}catch(Exception e){
			throw e;
		}
	}
	
	//获取所有权限
	public List<Authority> getList() throws Exception{
		try{
			List<Authority> authorityList = dao.queryForBeanList("SELECT * FROM tb_authority", Authority.class);
			return authorityList;
		}catch(Exception e){
			throw e;
		}
	}
	
	//修改某角色权限
	public int update(int roleId,List<Role_Authority> role_authoritylist)throws Exception{
		try{
			//先删除再增加
			this.delete(roleId);
			for(Role_Authority role_authority : role_authoritylist){
				this.insert(role_authority);
			}
			//修改该角色下所有用户锁定
			userService.lockByRole(roleId);
			return 1;
		}catch(Exception e){
			throw e;
		}
	}
	
	//删除某角色的权限
	public int delete(int roleId)throws Exception{
		try{
			return dao.update("delete from tb_role_authority WHERE  roleId = ?", roleId);
		}catch(Exception e){
			throw e;
		}
	}
	
	public Boolean checkLcode(int userId,String lcode) throws Exception{
		try{
			Authority entity= dao.queryForBean("select au.* from tb_role_authority ra,tb_user us,tb_authority au WHERE ra.roleId = us.roleId AND ra.authorityId = au.id AND us.id = ? AND au.lcode=?",Authority.class, userId,lcode);
			if(entity!=null){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			throw e;
		}
	}
}
