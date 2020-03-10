package com.br.ott.client.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseApiController;
import com.br.ott.client.api.service.UserOrderApiService;
import com.br.ott.common.exception.OTTException;

/* 
 *  
 *  文件名：UserOrderApiController.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-16 - 上午10:10:27
 */
@Controller
@RequestMapping(value = "/api")
public class UserOrderApiController extends BaseApiController {

	@Autowired
	private UserOrderApiService userOrderApiService;

	/**
	 * 节目订购
	 * 
	 * 创建人：pananz 创建时间：2015-3-19 - 下午4:51:45
	 * 
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "orderProgram", method = RequestMethod.POST)
	public @ResponseBody
	String orderProgram(HttpServletRequest request, HttpServletResponse response) {
		try {
			JSONObject jsonObject = getJSONObj(request);
			getStringFromJsonObject(jsonObject, "account", false);
			getStringFromJsonObject(jsonObject, "type", false);
			getStringFromJsonObject(jsonObject, "programId", false);
			return success(response,
					userOrderApiService.orderProgram(request, jsonObject));
		} catch (OTTException e) {
			return error(response, e.getCode(), e.getMessage());
		}
	}

	/**
	 * 节目鉴权
	 * 
	 * 创建人：pananz 创建时间：2015-3-19 - 下午4:57:27
	 * 
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "authentication", method = RequestMethod.POST)
	public @ResponseBody
	String authentication(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JSONObject jsonObject = getJSONObj(request);
			getStringFromJsonObject(jsonObject, "account", false);
			getStringFromJsonObject(jsonObject, "type", false);
			getStringFromJsonObject(jsonObject, "programId", false);
			return success(response,
					userOrderApiService.authentication(request, jsonObject));
		} catch (OTTException e) {
			return error(response, e.getCode(), e.getMessage());
		}
	}
}
