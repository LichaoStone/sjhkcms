/*
 * @# SessionInterceptor.java Aug 30, 2012 4:35:51 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.interceptor;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.br.ott.Global;
import com.br.ott.client.SessionUtil;

/*
 * @author pananz
 * TODO
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = Logger
			.getLogger(SessionInterceptor.class);
	private String mappingURL;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (null == SessionUtil.getCurrentUser()) {
			String url = request.getRequestURL().toString();
			if (mappingURL == null || url.matches(mappingURL)) {
				LOGGER.error("-------登录session过期-----");
				request.getRequestDispatcher("../shiro/logout.htm").forward(
						request, response);
				return false;
			}

		}
		Map<String, String[]> params = request.getParameterMap();
		if (params != null && params.size() > 0) {
			Set<String> keys = params.keySet();

			LOGGER.debug("----------------------请求参数------------------------");
			for (String key : keys) {
				LOGGER.debug(key + "=" + params.get(key)[0]);
			}
			LOGGER.debug("------------------------------------------------------");
		}
		// 设置当前时间到请求中
		request.setAttribute(Global.SESSION_REQ_SYSTEM_TIME,
				System.currentTimeMillis());
		return true;
	}

	public void setMappingURL(String mappingURL) {
		this.mappingURL = mappingURL;
	}
	
}
