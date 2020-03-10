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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.shiro.entity.Resources;
import com.br.ott.client.shiro.entity.Systems;
import com.br.ott.client.shiro.service.ResourcesService;
import com.br.ott.client.shiro.service.SystemsService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 分组 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-2
 */
@Controller
@RequestMapping(value = "resour")
public class ResourceController extends BaseController {
	/**
	 * 资源信息服务接口
	 */
	@Autowired
	private ResourcesService resourcesService;

	@Autowired
	private SystemsService systemsService;

	@Autowired
	private OperaLogService operaLogService;

	private static final String BUSI_NAME = "资源菜单管理";

	/**
	 * 获得资源信息列表
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "findResourList", method = RequestMethod.GET)
	public String findResourList(HttpServletRequest request, Model model,
			Resources res) {
		PagedModelList<Resources> pml = resourcesService.findAllResour(res,
				getPageNo(request), getPageRow(request));
		model.addAttribute("pml", pml);
		model.addAttribute("res", res);
		pml = null;
		return "shiro/listResour";
	}

	/**
	 * 去资源页面
	 * 
	 * @author zhuw
	 * @修改 pananz
	 * @return
	 */
	@RequestMapping(value = "toResour", method = RequestMethod.GET)
	public String toResour(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		Resources resour = new Resources();
		if (!StringUtil.isEmpty(id)) {
			resour = resourcesService.getResourById(id);
			if (resour != null) {
				String resId = resourcesService.getResourId(resour
						.getSystemid());
				List<Resources> resourList = resourcesService
						.findAllResourByIdList(resour.getSystemid(), resId);
				model.addAttribute("resourList", resourList);
				resourList = null;
			}
		}
		model.addAttribute("resour", resour);

		List<Systems> systems = systemsService.findAllList();
		model.addAttribute("systems", systems);
		systems = null;
		return "shiro/resourInfo";
	}

	/**
	 * 指定模块下所有的资源
	 * 
	 * @author zhuw
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getResources")
	public @ResponseBody
	List<Resources> getResources(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String systemId = request.getParameter("systemId");
		String resId = resourcesService.getResourId(systemId);
		List<Resources> resources = new ArrayList<Resources>();
		if (!StringUtil.isEmpty(resId)) {
			Resources sysRes = resourcesService.getResourById(resId);
			resources.add(sysRes);// 把模块本身也加上
			List<Resources> list = resourcesService.findAllResourByIdList(
					systemId, resId);
			resources.addAll(list);
		}
		return resources;
	}

	/**
	 * 保存资源信息记录
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "saveResour", method = RequestMethod.POST)
	public @ResponseBody
	Feedback saveResour(HttpServletRequest request, Resources res) {
		try {
			if (!StringUtil.isEmpty(res.getId())) {
				Resources old = resourcesService.getResourById(res.getId());
				if (!old.getName().equals(res.getName())) {
					boolean bool = resourcesService.getResourByName(res
							.getName());
					if (!bool) {
						return Feedback.fail("资源保存失败,资源名称已使用");
					}
				}
				if (!old.getEnname().equals(res.getEnname())) {
					boolean bool = resourcesService.getChekEnname(res
							.getEnname());
					if (!bool) {
						return Feedback.fail("资源保存失败,资源编码已使用");
					}
				}
				resourcesService.modifyResour(res);
				String diffStr = ObjectUtil.getDiffColumnDescript(old, res);
				if (null != diffStr) {
					// 写入系统操作日志
					operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
							BUSI_NAME, diffStr);
				}

			} else {
				boolean bool = resourcesService.getResourByName(res.getName());
				if (!bool) {
					return Feedback.fail("资源保存失败,资源名称已使用");
				}
				boolean flag = resourcesService.getChekEnname(res.getName());
				if (!flag) {
					return Feedback.fail("资源保存失败,资源编码已使用");
				}
				resourcesService.addResour(res);
				operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
						"新增资源名称为：" + res.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("保存资源失败");
		}
		return Feedback.success("保存资源成功");
	}

	/**
	 * 检验用户名是否已存在
	 * 
	 * @author zhuw
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "checkName", method = RequestMethod.GET)
	public void checkName(HttpServletRequest request,
			HttpServletResponse response) {
		String rname = request.getParameter("name");
		String oldName = request.getParameter("oldName");
		if (StringUtil.isEmpty(rname)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		if (rname.equals(oldName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}

		boolean bool = resourcesService.getResourByName(rname);
		writeAjaxResult(response, String.valueOf(bool));
	}

	/**
	 * 检查资源编码是否已经存在
	 * 
	 * @author zhuw
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "checkEnName", method = RequestMethod.GET)
	public void checkEnName(HttpServletRequest request,
			HttpServletResponse response) {
		String enname = request.getParameter("enname");
		String oldEnName = request.getParameter("oldEnName");
		if (StringUtil.isEmpty(enname)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		if (enname.equals(oldEnName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean bool = resourcesService.getChekEnname(enname);
		writeAjaxResult(response, String.valueOf(bool));
	}

	/**
	 * 删除指定id的资源信息
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "delResour", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delResour(@RequestParam("oid") String ids) {
		try {
			resourcesService.delResour(new String[] { ids });
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("删除失败!");
		}
		return Feedback.success("删除成功!");
	}

	/**
	 * 去查看页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toViewResour", method = RequestMethod.GET)
	public String toViewResour(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		if (!StringUtil.isEmpty(id)) {
			Resources resour = resourcesService.getResourById(id);
			model.addAttribute("resour", resour);
			resour = null;
		}

		List<Resources> resourList = resourcesService.findAllResourList();
		model.addAttribute("resourList", resourList);
		resourList = null;
		List<Systems> systems = systemsService.findAllList();
		model.addAttribute("systems", systems);
		systems = null;
		return "shiro/viewResour";
	}

}
