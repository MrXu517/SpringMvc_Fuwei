package com.fuwei.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * 
 * @author huchao
 *
 */
public class BaseController extends MultiActionController{

	/**
	 * 返回结果成功结果
	 * 
	 * @return
	 */
	protected Map<String, Object> returnSuccess() {
		return returnSuccess(true);
	}

	protected Map<String, String> returnSuccess(String type, String value) {
		Map<String, String> result = new HashMap<String, String>();
		result.put(type, value);
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
	public Map<String,Object> exception(Exception e){
		System.out.println(e.getMessage());
//		e.printStackTrace();
		return this.returnFail(e.getMessage());
	}
}