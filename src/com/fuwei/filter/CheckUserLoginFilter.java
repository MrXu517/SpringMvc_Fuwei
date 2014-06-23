/**
 * 
 */
package com.fuwei.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fuwei.commons.LoginedUser;
import com.fuwei.commons.SystemContextUtils;

/**
 * @author zxd
 *
 */
public class CheckUserLoginFilter implements Filter {

	private static final Log LOG = LogFactory.getLog(CheckUserLoginFilter.class);
	
	private List<String> notFilterURIs;
	
	private List<Pattern> notFilterPatterns = new ArrayList<Pattern>();
	
	private  String LOGIN_URL; 
	
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		String path = request.getContextPath();
		String requestURI = request.getRequestURI();
		
		requestURI = requestURI.substring(requestURI.indexOf(path)+ path.length());
		if (this.isNeedFilter(requestURI)) {// 是需要验证登录的URI
			LoginedUser sessionUser =SystemContextUtils.getCurrentUser(session);
			if (sessionUser == null) {
				redirectTo(request, response, LOGIN_URL);
				return;
			}else{
				
				//若该用户已登录，则判断该用户的权限是否已改变（用户角色的权限是否已改变）
				
				//若该用户已登录，则判断该用户的权限是否已改变（用户角色的权限是否已改变）
				
				chain.doFilter(servletRequest, servletResponse);
			}
		}else{
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	private void redirectTo(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		if (url.startsWith("http")) {
			response.sendRedirect(url);
		} else {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;
			response.sendRedirect(basePath + url);
		}
	}
	
	@Override
	public void init(FilterConfig cfg) throws ServletException {
		this.notFilterURIs = new ArrayList<String>();//notFilterURIs不需要过滤的uri
		String notFilterURIStr = cfg.getInitParameter("notFilterURIs");
		LOGIN_URL =  cfg.getInitParameter("loginURL");
		if (LOGIN_URL != null) {
			this.notFilterURIs.add(LOGIN_URL);
			this.notFilterPatterns.add(Pattern.compile(LOGIN_URL));
		}else {
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
	

	
	/*private static boolean hasAuthorityURL(String url,List<String> listUrl) {
		
		try {
			if (url.startsWith("/")) {
				url = url.substring(1);
			}
			
			if (CollectionUtils.isNotEmpty(listUrl) && listUrl.contains(url)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}*/
	
}
