package com.br.ott.client.select.controller;

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
import com.br.ott.client.select.entity.Tariff;
import com.br.ott.client.select.service.TariffService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;

/* 
 *  点播资费
 *  文件名：TariffController.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 上午11:32:56
 */
@Controller
@RequestMapping(value = "/select")
public class TariffController extends BaseController {

	@Autowired
	private TariffService tariffService;

	@Autowired
	private OperaLogService operaLogService;

	private static final String BUSI_NAME = "点播资费管理";

	/**
	 * 按分页查询点播资费
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:35:38
	 * 
	 * @param request
	 * @param tariff
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findTariff", method = RequestMethod.GET)
	public String findTariff(HttpServletRequest request, Tariff tariff,
			Model model) {
		tariff.setCurrentPage(getPageNo(request));
		tariff.setShowCount(getPageRow(request));
		List<Tariff> list = tariffService.findTariffByPage(tariff);
		model.addAttribute("list", list);
		model.addAttribute("tariff", tariff);
		list = null;
		return "select/listTariff";
	}

	/**
	 * 转到点播资费页面
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:41:54
	 * 
	 * @param request
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toTariff", method = RequestMethod.GET)
	public String toTariff(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		Tariff tariff = new Tariff();
		if (StringUtil.isNotEmpty(id)) {
			tariff = tariffService.getTariffById(id);
		}
		model.addAttribute("tariff", tariff);
		return "select/tariffInfo";
	}

	/**
	 * 校验点播资费名称是否已使用
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:42:05
	 * 
	 * @param response
	 * @param request
	 *            返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkTariffName", method = RequestMethod.GET)
	public void checkTariffName(HttpServletResponse response,
			HttpServletRequest request) {
		String name = request.getParameter("name");
		String oldName = request.getParameter("oldName");
		if (StringUtil.isEmpty(name) || oldName.equals(name)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = tariffService.checkTariffName(name);
		writeAjaxResult(response, String.valueOf(result));
	}

	/**
	 * 增加点播资费
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:44:38
	 * 
	 * @param request
	 * @param tariff
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addTariff")
	public @ResponseBody
	Feedback addTariff(HttpServletRequest request, Tariff tariff) {
		try {
			boolean bool = tariffService
					.checkTariffName(tariff.getTariffName());
			if (!bool) {
				return Feedback.fail("点播资费名称已使用");
			}
			tariffService.addTariff(tariff);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("增加点播资费失败");
		}
		operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
				"新增点播资费信息名称为：" + tariff.getTariffName());
		return Feedback.success("增加点播资费信息成功");
	}

	/**
	 * 修改点播资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:48:32
	 * 
	 * @param request
	 * @param tariff
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateTariff")
	public @ResponseBody
	Feedback updateTariff(HttpServletRequest request, Tariff tariff) {
		Tariff old = tariffService.getTariffById(tariff.getId());
		try {
			if (!old.getTariffName().equals(tariff.getTariffName())) {
				boolean bool = tariffService.checkTariffName(tariff
						.getTariffName());
				if (!bool) {
					return Feedback.fail("点播资费名称已使用");
				}
			}
			tariffService.updateTariff(tariff);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改点播资费信息失败");
		}
		try {
			String diffStr = ObjectUtil.getDiffColumnDescript(old, tariff);
			if (null != diffStr) {
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {

		}
		return Feedback.success("修改点播资费信息成功");
	}

	/**
	 * 点播资费停用
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:51:05
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "tariffClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback tariffClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要停用的点播资费");
		}
		try {
			tariffService.updateTariffStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("点播资费停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"停用点播资费信息编号为：" + id);
		}
		return Feedback.success("点播资费停用成功");
	}

	/**
	 * 点播资费启用
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:50:45
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "tariffReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback tariffReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要启用的点播资费");
		}
		try {
			tariffService.updateTariffStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("点播资费启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用点播资费信息编号为：" + id);
		}
		return Feedback.success("点播资费启用成功");
	}
}
