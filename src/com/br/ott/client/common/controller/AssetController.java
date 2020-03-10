package com.br.ott.client.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseApiController;
import com.br.ott.client.common.service.AssetLogService;
import com.br.ott.common.exception.OTTException;

/**
 * 文件名：AssetController.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
@Controller
@RequestMapping(value = "/tvod")
public class AssetController extends BaseApiController {

	@Autowired
	private AssetLogService assetLogService;

	@RequestMapping(value = "syncTvodsInfo")
	public @ResponseBody
	String syncTvodsInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			return assetLogService.syncTvodsInfo(request);
		} catch (OTTException e) {
			return error(response, e.getCode(), e.getMessage());
		}
	}
	
}
