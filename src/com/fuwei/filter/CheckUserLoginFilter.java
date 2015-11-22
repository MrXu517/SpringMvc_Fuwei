/**
 * 
 */
package com.fuwei.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fuwei.commons.LoginedUser;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.constant.ERROR;

/**
 * @author zxd
 * 
 */
public class CheckUserLoginFilter implements Filter {

	private static final Log LOG = LogFactory
			.getLog(CheckUserLoginFilter.class);

	private List<String> notFilterURIs;

	private List<Pattern> notFilterPatterns = new ArrayList<Pattern>();

	private String LOGIN_URL;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		String path = request.getContextPath();
		String URI = request.getRequestURI();
		String query = request.getQueryString();
		String full_url = URI;
		if(query!=null && !query.equals("")){
			full_url = URI + "?" + query;
		}
		String requestURI = URI.substring(URI.indexOf(path) + path.length());
		
		if (this.isNeedFilter(requestURI)) {// 是需要验证登录的URI
			LoginedUser sessionUser = SystemContextUtils
					.getCurrentUser(session);
			if (sessionUser == null) {
				redirectTo(request, response, LOGIN_URL,false,full_url);
				return;
			} else {
				//判断该用户是否被锁定
				for(com.fuwei.entity.User user : SystemCache.userlist){
					if(user.getId() == sessionUser.getLoginedUser().getId()){
						if(user.getLocked()){//被锁定
							//去除session的用户信息
							session.removeAttribute(Constants.LOGIN_SESSION_NAME);
							redirectTo(request, response, LOGIN_URL,true,full_url);
							return;
						}
						//2015-4-4添加 用户是否需要更新缓存 未读信息
						if(user.getNeed_message_cache_update()){
							try {
								sessionUser.reloadMessage();
								//SystemCache.setUserCacheUpdate(sessionUser.getLoginedUser().getId(),false);
							} catch (Exception e) {
								session.removeAttribute(Constants.LOGIN_SESSION_NAME);
								redirectTo(request, response, LOGIN_URL,false,full_url);
								return;
							}
						}
					}
					
				}
				chain.doFilter(servletRequest, servletResponse);
			}
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	@SuppressWarnings("deprecation")
	private void redirectTo(HttpServletRequest request,
			HttpServletResponse response, String url,Boolean locked,String referer) throws IOException {
		String request_path = request.getContextPath();
		String request_URI = request.getRequestURI();
		String requestURI = request_URI.substring(request_URI.indexOf(request_path) + request_path.length());
		
		String message =  ERROR.RELOGIN;
		if(locked){
			message = ERROR.LOCKED;
		}
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			JSONObject json = new JSONObject();
			json.put("message",message);
			if(locked){
				json.put("locked", true);
			}else{
				json.put("relogin", true);
			}
			
			PrintWriter pw = response.getWriter();
			pw.print(json.toString());

			response.setContentType("text/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			pw.close();
		} else {
			if(requestURI.equals("/") || requestURI.equals("")){//第一次登录无需提示信息
				message = null;
				referer = null;
			}
			String querystring = "";
			String seq = "?";
			if(message != null && !message.equals("")){
				String me = URLEncoder.encode(message, "utf-8");
				querystring = seq + "message=" + me;
				seq = "&";
			}
			if(referer != null && !referer.equals("")){
				referer = URLEncoder.encode(referer, "utf-8");
				querystring = querystring + seq + "referer=" + referer;
			}
			
			
			if (url.startsWith("http")) {	
				response.sendRedirect(url + querystring);
			} else {
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://"
						+ request.getServerName() + ":"
						+ request.getServerPort() + path;
				
				response.sendRedirect(basePath + url + querystring);
			}
		}

	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		this.notFilterURIs = new ArrayList<String>();// notFilterURIs不需要过滤的uri
		String notFilterURIStr = cfg.getInitParameter("notFilterURIs");
		LOGIN_URL = cfg.getInitParameter("loginURL");
		if (LOGIN_URL != null) {
			this.notFilterURIs.add(LOGIN_URL);
			this.notFilterPatterns.add(Pattern.compile(LOGIN_URL));
		} else {
			LOG.info("没有配置loginURL......");
		}
		if (notFilterURIStr != null) {
			String[] strs = notFilterURIStr.split(",");
			for (String str : strs) {
				this.notFilterURIs.add(str);
				this.notFilterPatterns.add(Pattern.compile(str));
			}
		}
	}

	/**
	 * 是否需要过滤
	 * 
	 * @param requestURI
	 * @return true则需要过滤
	 */
	private boolean isNeedFilter(String requestURI) {
		for (Pattern pattern : notFilterPatterns) {
			if (pattern.matcher(requestURI).matches()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * private static boolean hasAuthorityURL(String url,List<String> listUrl) {
	 * 
	 * try { if (url.startsWith("/")) { url = url.substring(1); }
	 * 
	 * if (CollectionUtils.isNotEmpty(listUrl) && listUrl.contains(url)) {
	 * return true; } } catch (Exception e) { return false; }
	 * 
	 * return false; }
	 */

}
