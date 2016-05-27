package com.fuwei.controller.financial;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.SelfAccount;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.SelfAccountService;
import com.fuwei.util.DateTool;

@RequestMapping("/selfaccount")
@Controller
public class SelfAccountController extends BaseController {
	@Autowired
	SelfAccountService selfAccountService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "selfaccount/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有本厂帐号管理的权限", null);
		}
		List<SelfAccount> selfaccountlist = selfAccountService.getList();
		request.setAttribute("selfaccountlist", selfaccountlist);
		return new ModelAndView("financial/selfaccount/list");

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(SelfAccount selfAccount, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selfaccount/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加账户的权限", null);
		}
		selfAccount.setName(chinese2English(selfAccount.getName()));
		selfAccount.setCreated_at(DateTool.now());
		selfAccount.setUpdated_at(DateTool.now());
		selfAccount.setCreated_user(user.getId());
		int success = selfAccountService.add(selfAccount);
		//更新缓存
		SystemCache.initSelfAccountList();
		return this.returnSuccess();

	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selfaccount/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除账户的权限", null);
		}
		int success = selfAccountService.remove(id);
		//更新缓存
		SystemCache.initSelfAccountList();
		return this.returnSuccess();

	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public SelfAccount get(@PathVariable int id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String lcode = "selfaccount/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看账户列表的权限", null);
		}
		SelfAccount bank = selfAccountService.get(id);
		return bank;
	}

	@RequestMapping(value = "/list_json", method = RequestMethod.GET)
	@ResponseBody
	public List<SelfAccount> get(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "selfaccount/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看账户列表的权限", null);
		}
		List<SelfAccount> banklist = selfAccountService.getList();
		return banklist;
	}

	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(SelfAccount selfAccount, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "selfaccount/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑账户的权限", null);
		}
		selfAccount.setName(chinese2English(selfAccount.getName()));
		selfAccount.setUpdated_at(DateTool.now());
		int success = selfAccountService.update(selfAccount);
		//更新缓存
		SystemCache.initSelfAccountList();

		return this.returnSuccess();

	}
	public String chinese2English(String str){
		String[] regs = { "！", "，", "。 ","；","（","）", "!", ",", ".", ";" ,"(",")"};
		for ( int i = 0; i < regs.length / 2; i++ )
		{
		    str = str.replaceAll (regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
}
