package com.br.ott.client.select.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.select.entity.DramaSource;
import com.br.ott.client.select.entity.Tariff;
import com.br.ott.client.select.service.DramaSourceService;
import com.br.ott.client.select.service.TariffService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;

/* 
 *  
 *  文件名：DramaSourceController.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 下午2:15:04
 */
@Controller
@RequestMapping(value = "/select")
public class DramaSourceController extends BaseController {

	@Autowired
	private DramaSourceService dramaSourceService;
	@Autowired
	private OperaLogService operaLogService;
	@Autowired
	private TariffService tariffService;

	private static final String BUSI_NAME = "资产资源管理";

	/**
	 * 按分页查询资产资源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:35:38
	 * 
	 * @param request
	 * @param DramaSource
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findDramaSource", method = RequestMethod.GET)
	public String findDramaSource(HttpServletRequest request,
			DramaSource dramaSource, Model model) {
		dramaSource.setChildId(request.getParameter("childId"));
		dramaSource.setCurrentPage(getPageNo(request));
		dramaSource.setShowCount(getPageRow(request));
		List<DramaSource> list = dramaSourceService
				.findDramaSourceByPage(dramaSource);
		model.addAttribute("list", list);
		model.addAttribute("dramaSource", dramaSource);
		list = null;
		return "select/listDSource";
	}

	/**
	 * 转到资产资源页面
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:41:54
	 * 
	 * @param request
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDramaSource", method = RequestMethod.GET)
	public String toDramaSource(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		DramaSource dramaSource = new DramaSource();
		dramaSource.setChildId(request.getParameter("childId"));
		if (StringUtil.isNotEmpty(id)) {
			dramaSource = dramaSourceService.getDramaSourceById(id);
		}
		model.addAttribute("dramaSource", dramaSource);
		Tariff tariff = new Tariff();
		tariff.setStatus("1");
		List<Tariff> tariffs = tariffService.findTariffByCond(tariff);
		model.addAttribute("tariffs", tariffs);
		tariffs = null;
		return "select/dSourceInfo";
	}

	/**
	 * 增加资产资源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:44:38
	 * 
	 * @param request
	 * @param DramaSource
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDramaSource")
	public @ResponseBody
	Feedback addDramaSource(HttpServletRequest request, DramaSource dramaSource) {
		try {
			dramaSource.setStatus("0");
			dramaSource.setBitrateTypeStr();
			dramaSourceService.addDramaSource(dramaSource);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("增加资产资源失败");
		}
		operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
				"新增资产资源信息为：" + dramaSource.getId());
		return Feedback.success("增加资产资源信息成功");
	}

	/**
	 * 修改资产资源信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:48:32
	 * 
	 * @param request
	 * @param DramaSource
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateDramaSource")
	public @ResponseBody
	Feedback updateDramaSource(HttpServletRequest request,
			DramaSource dramaSource) {
		DramaSource old = dramaSourceService.getDramaSourceById(dramaSource
				.getId());
		try {
			dramaSource.setBitrateTypeStr();
			dramaSourceService.updateDramaSource(dramaSource);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改资产资源信息失败");
		}
		try {
			String diffStr = ObjectUtil.getDiffColumnDescript(old, dramaSource);
			if (null != diffStr) {
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {

		}
		return Feedback.success("修改资产资源信息成功");
	}

	/**
	 * 资产资源停用
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:51:05
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "sourceClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback sourceClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要停用的资产资源");
		}
		try {
			dramaSourceService.updateSourceStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产资源停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"停用资产资源信息编号为：" + id);
		}
		return Feedback.success("资产资源停用成功");
	}

	@RequestMapping(value = "sourceListClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback sourceListClose(@RequestParam("ids") String ids,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(ids)) {
			return Feedback.fail("请选择要停用的批量资产资源");
		}
		try {
			dramaSourceService.updateSourceStatus("0", ids.split(","));
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产资源批量停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"批量资产资源停用信息编号为：" + ids);
		}
		return Feedback.success("资产资源批量停用成功");
	}
	
	/**
	 * 资产资源启用
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:50:45
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "sourceReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback sourceReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要启用的资产资源");
		}
		try {
			dramaSourceService.updateSourceStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产资源启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用资产资源信息编号为：" + id);
		}
		return Feedback.success("资产资源启用成功");
	}

	@RequestMapping(value = "sourceListReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback sourceListReverse(@RequestParam("ids") String ids,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(ids)) {
			return Feedback.fail("请选择要启用的批量资产资源");
		}
		try {
			dramaSourceService.updateSourceStatus("1", ids.split(","));
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产资源批量启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"资产资源批量启用信息编号为：" + ids);
		}
		return Feedback.success("资产资源批量启用成功");
	}
	
	/**
	 * 资产资源删除
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 下午2:20:08
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "deleteSource", method = RequestMethod.POST)
	public @ResponseBody
	Feedback deleteSource(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要删除的资产资源");
		}
		try {
			dramaSourceService.deleteSourceById(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产资源删除失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"删除资产资源信息编号为：" + id);
		}
		return Feedback.success("资产资源删除成功");
	}
}
