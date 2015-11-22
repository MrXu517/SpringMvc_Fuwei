package com.fuwei.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;


import org.apache.log4j.Logger;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * 
 * @author huchao
 *
 */
public class BaseController extends MultiActionController{
	private Logger log = org.apache.log4j.LogManager.getLogger(BaseController.class);
	
	/**
	 * 返回结果成功结果
	 * 
	 * @return
	 */
	protected Map<String, Object> returnSuccess() {
		return returnSuccess(true);
	}

	protected Map<String, Object> returnSuccess(String type,  Object value) {
		Map<String,  Object> result = new HashMap<String,  Object>();
		result.put("success", true);
		result.put(type, value);
		return result;
	}
	protected Map<String, Object> returnSuccess(Map<String ,Object> data) {
		Map<String,  Object> result = new HashMap<String,  Object>();
		result.put("success", true);
		for(String key : data.keySet()){
			result.put(key, data.get(key));
		}
		return result;
	}

	/**
	 * 返回值
	 * 
	 * @param str
	 * @return
	 */
	protected Map<String, Object> returnCacl(String str) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", str);
		return result;
	}

	/**
	 * 返回结果
	 * 
	 * @param isSuccess
	 *           值
	 * @return
	 */
	protected Map<String, Object> returnSuccess(boolean isSuccess) {
		Map<String, Object> result = new HashMap<String, Object>();
		// if(StringUtils.isNotBlank(isSuccess)&&(!"success".equals(isSuccess))&&isSuccess.indexOf("#id#")==-1&&Tools.isABC(isSuccess))
		// isSuccess="更新失败,请检查数据是否合法！";
		result.put("success", isSuccess);
		return result;
	}
	
	/*失败的返回值*/
	protected Map<String, Object> returnFail(String message) {
		Map<String, Object> result = returnSuccess(false);
		result.put("message", message);
		return result;
	}
	
	/*捕捉异常的通用方法*/
	@ExceptionHandler
	@ResponseBody
	public void exception(HttpServletRequest request,HttpServletResponse response ,Exception e) throws IOException{
		log.error(e);
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":"
				+ request.getServerPort() + path + "/";
		String errorUrl = "error.jsp";
		String message = e.getMessage();
		if(e instanceof PermissionDeniedDataAccessException){//如果捕获到权限异常
			//则跳到异常页面
			errorUrl = "authority/error";
		}
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			JSONObject json = new JSONObject();
			PrintWriter pw = response.getWriter();
			pw.print(message);
			response.setContentType("text/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			pw.close();
		}else{
			message = URLEncoder.encode(message, "utf-8");
			response.sendRedirect(basePath + errorUrl + "?message=" + message);
		}
	}
}