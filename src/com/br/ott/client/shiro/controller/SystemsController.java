/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.client.shiro.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.shiro.entity.Systems;
import com.br.ott.client.shiro.service.SystemsService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 系统控制类
 * 
 * @author zhuw
 * @version 1.0 2012-9-3
 */
@Controller
@RequestMapping(value = "system")
public class SystemsController extends BaseController {

	@Autowired
	private SystemsService systemsService;

	/**
	 * 获得系统列表
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "findSystemList", method = RequestMethod.GET)
	public String findSystemList(HttpServletRequest request, Model model,
			Systems syste) {
		PagedModelList<Systems> pml = systemsService.findAllSystem(syste,
				getPageNo(request), getPageRow(request));
		model.addAttribute("pml", pml);
		model.addAttribute("syste", syste);
		pml = null;
		return "shiro/listSystem";
	}

	/**
	 * 去编辑页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toSystem", method = RequestMethod.GET)
	public String toAddSystem(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		Systems systems = new Systems();
		if (!StringUtil.isEmpty(id)) {
			systems = systemsService.getSystemById(id);
		}
		model.addAttribute("systems", systems);
		return "shiro/systemInfo";
	}

	/**
	 * 保存系统
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "saveSystem", method = RequestMethod.POST)
	public @ResponseBody
	Feedback saveSystem(HttpServletRequest request, Systems systems) {
		try {
			if (!StringUtil.isEmpty(systems.getId())) {
				systemsService.modifySystem(systems);
			} else {
				SimpleDateFormat bartDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				Date date = new Date();
				String builddate = bartDateFormat.format(date).toString();
				systems.setBuilddate(builddate);
				systemsService.addSystem(systems);
			}
			systems = null;
			return Feedback.success("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("保存失败");
		}
	}

	/**
	 * 去查看页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toViewSystem", method = RequestMethod.GET)
	public String toViewSystem(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		if (!StringUtil.isEmpty(id)) {
			Systems systems = systemsService.getSystemById(id);
			model.addAttribute("systems", systems);
		}

		return "shiro/viewSystems";
	}

	/**
	 * 删除指定id的角色
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "delSystem", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delSystem(@RequestParam("oid") String id) {
		try {
			systemsService.delSystem(new String[] { id });
		} catch (Exception e) {
			return Feedback.fail("删除失败!");
		}
		return Feedback.success("删除成功!");
	}

}
