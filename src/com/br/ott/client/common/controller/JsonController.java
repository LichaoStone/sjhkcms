/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.client.common.controller;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.ott.base.BaseController;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.util.DateUtil;
import com.br.ott.common.util.StringUtil;
import com.br.ott.test.ApiTest;

@Controller
@RequestMapping(value = "json")
public class JsonController extends BaseController {
	private static Logger log = Logger.getLogger(JsonController.class);

	@RequestMapping(value = "jsonTest")
	public String jsonTest(HttpServletRequest request,
			HttpServletResponse response, Model view) throws OTTException {
		Date date = new Date();
		String urlValue = request.getParameter("url");
		String pub = request.getParameter("publicReq");
		String businessReq = request.getParameter("businessReq");
		if (StringUtil.isEmpty(urlValue))
			urlValue = "";
		String returnjson = "";
		String returnCode = "";
		String returnMsg = "";
		try {
			HashMap<String, Object> map = ApiTest.callMethod(urlValue,
					businessReq, pub);
			if (map == null) {
				urlValue = "";
				pub = "";
				businessReq = "";
			} else {
				returnCode = (String) map.get("returnCode");
				returnMsg = (String) map.get("returnMsg");
				if (map.get("jsonObject") != null)
					returnjson = map.get("jsonObject").toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		view.addAttribute("returnCode", returnCode);
		view.addAttribute("returnMsg", returnMsg);
		view.addAttribute("url", urlValue);
		view.addAttribute("publicReq", pub);
		view.addAttribute("businessReq", businessReq);
		view.addAttribute("returnjson", returnjson);
		long longTime = DateUtil.dateDiff(date, new Date());
		view.addAttribute("longTime", longTime);
		log.debug("查询接口消耗时间：" + longTime);
		return "jsonTest";
	}

	@RequestMapping(value = "jsonTest2")
	public String jsonTest2(HttpServletRequest request,
			HttpServletResponse response, Model view) throws OTTException {
		Date date = new Date();
		String urlValue = request.getParameter("url");
		String pub = request.getParameter("publicReq");
		String businessReq = request.getParameter("businessReq");
		if (StringUtil.isEmpty(urlValue))
			urlValue = "";
		String returnjson = "";
		String returnCode = "";
		String returnMsg = "";
		try {
			HashMap<String, Object> map = ApiTest.callMethod3(urlValue,
					businessReq, pub);
			if (map == null) {
				urlValue = "";
				pub = "";
				businessReq = "";
			} else {
				returnCode = (String) map.get("returnCode");
				returnMsg = (String) map.get("returnMsg");
				if (map.get("jsonObject") != null)
					returnjson = map.get("jsonObject").toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		view.addAttribute("returnCode", returnCode);
		view.addAttribute("returnMsg", returnMsg);
		view.addAttribute("url", urlValue);
		view.addAttribute("publicReq", pub);
		view.addAttribute("businessReq", businessReq);
		view.addAttribute("returnjson", returnjson);
		long longTime = DateUtil.dateDiff(date, new Date());
		view.addAttribute("longTime", longTime);
		log.debug("查询接口消耗时间：" + longTime);
		return "jsonTest2";
	}
}
