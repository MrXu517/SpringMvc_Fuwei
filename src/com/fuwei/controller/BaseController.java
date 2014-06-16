package com.fuwei.controller;

import java.util.HashMap;
import java.util.Map;

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
	protected Map<String, String> returnSuccess() {
		return returnSuccess("success");
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
	protected Map<String, String> returnCacl(String str) {
		Map<String, String> result = new HashMap<String, String>();
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
	protected Map<String, String> returnSuccess(String isSuccess) {
		Map<String, String> result = new HashMap<String, String>();
		// if(StringUtils.isNotBlank(isSuccess)&&(!"success".equals(isSuccess))&&isSuccess.indexOf("#id#")==-1&&Tools.isABC(isSuccess))
		// isSuccess="更新失败,请检查数据是否合法！";
		result.put("success", isSuccess);
		return result;
	}

}