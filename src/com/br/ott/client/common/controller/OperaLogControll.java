package com.br.ott.client.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.ott.base.BaseController;
import com.br.ott.client.common.entity.OperaLog;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/* 
 *  系统操作日志
 *  文件名：OperaLogControll.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-28 - 下午1:29:35
 */
@Controller
@RequestMapping(value = "common")
public class OperaLogControll extends BaseController {
	@Autowired
	private OperaLogService operaLogService;

	@RequestMapping("findOperaLogs")
	public String findOperaLogs(HttpServletRequest request, Model model) {
		bindRequestToModel(request, model);

		OperaLog log = new OperaLog();
		log.setCreateUser(getParameter(request, "createUser"));
		log.setBusiname(getParameter(request, "businame"));
		log.setTmp("%" + getParameter(request, "businame") + "%");
		log.setOperType(getParameter(request, "operType"));
		log.setCreateDate(getParameter(request, "startTime"));
		log.setEndTime(getParameter(request, "endTime"));

		String order = getParameter(request, "orderColumn");
		log.setOrderColumn(StringUtil.isEmpty(order) ? "createDate desc"
				: order);

		PagedModelList<OperaLog> pagedModelList = operaLogService
				.findOperaLogs(getPageNo(request), getPageRow(request), log);
		model.addAttribute("pagedModelList", pagedModelList);
		pagedModelList = null;
		log = null;
		return "common/listOperaLogs";
	}
}
