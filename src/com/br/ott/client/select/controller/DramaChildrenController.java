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
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.select.entity.DramaChildren;
import com.br.ott.client.select.entity.Tariff;
import com.br.ott.client.select.service.DramaChildrenService;
import com.br.ott.client.select.service.TariffService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;

/**
 * 文件名：DramaChildrenController.java 版权：BoRuiCubeNet. Copyright 2014-2015,All
 * rights reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-20
 */
@Controller
@RequestMapping(value = "/select")
public class DramaChildrenController extends BaseController {

	@Autowired
	private DramaChildrenService dramaChildrenService;
	@Autowired
	private TariffService tariffService;

	@Autowired
	private OperaLogService operaLogService;

	private static final String BUSI_NAME = "剧集管理";

	/**
	 * 分页查询剧集
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param request
	 * @param @param dramaChildren
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findDramaChild", method = RequestMethod.GET)
	public String findDramaChild(HttpServletRequest request,
			DramaChildren dramaChildren, Model model) {
		dramaChildren.setDramaId(request.getParameter("dramaId"));
		dramaChildren.setDramaName(request.getParameter("dramaName"));
		dramaChildren.setCurrentPage(getPageNo(request));
		dramaChildren.setShowCount(getPageRow(request));
		List<DramaChildren> list = dramaChildrenService
				.findDramaChildrenByPage(dramaChildren);
		model.addAttribute("list", list);
		model.addAttribute("dramaChildren", dramaChildren);
		list = null;
		return "select/listDChild";
	}

	/**
	 * 转到剧集
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDramaChild", method = RequestMethod.GET)
	public String toDramaChild(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		DramaChildren dramaChildren = new DramaChildren();
		dramaChildren.setDramaId(request.getParameter("dramaId"));
		if (StringUtil.isNotEmpty(id)) {
			dramaChildren = dramaChildrenService.getDramaChildrenById(id);
		}
		dramaChildren.setDramaName(request.getParameter("dramaName"));
		model.addAttribute("dramaChildren", dramaChildren);

		Tariff tariff = new Tariff();
		tariff.setStatus("1");
		List<Tariff> tariffs = tariffService.findTariffByCond(tariff);
		model.addAttribute("tariffs", tariffs);
		tariffs = null;
		return "select/dChildInfo";
	}

	/**
	 * 增加剧集
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param request
	 * @param @param dramaChildren
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDramaChild")
	public @ResponseBody
	Feedback addDramaChild(HttpServletRequest request,
			DramaChildren dramaChildren) {
		try {
			dramaChildren.setStatus("0");
			dramaChildren.setOperator(SessionUtil.getLoginName());
			dramaChildrenService.addDramaChildren(dramaChildren);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("增加剧集失败");
		}
		operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
				"新增剧集信息名称为：" + dramaChildren.getId());
		return Feedback.success("增加剧集成功");
	}

	/**
	 * 修改剧集
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param request
	 * @param @param dramaChildren
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateDramaChild")
	public @ResponseBody
	Feedback updateDramaChild(HttpServletRequest request,
			DramaChildren dramaChildren) {
		DramaChildren old = dramaChildrenService
				.getDramaChildrenById(dramaChildren.getId());
		try {
			dramaChildren.setOperator(SessionUtil.getLoginName());
			dramaChildrenService.updateDramaChildren(dramaChildren);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改剧集信息失败");
		}
		try {
			String diffStr = ObjectUtil.getDiffColumnDescript(old,
					dramaChildren);
			if (null != diffStr) {
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {

		}
		return Feedback.success("修改剧集信息成功");
	}

	/**
	 * 剧集停用
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "childClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback childClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要停用的剧集");
		}
		try {
			dramaChildrenService.updateChildrenStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("剧集停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"停用剧集信息编号为：" + id);
		}
		return Feedback.success("剧集停用成功");
	}

	/**
	 * 剧集启用
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "childReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback childReverse(@RequestParam("id") String id, HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要启用的剧集");
		}
		try {
			dramaChildrenService.updateChildrenStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("剧集启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用剧集信息编号为：" + id);
		}
		return Feedback.success("剧集启用成功");
	}
	
	@RequestMapping(value = "childListClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback childListClose(@RequestParam("ids") String ids,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(ids)) {
			return Feedback.fail("请选择要停用的批量剧集");
		}
		try {
			dramaChildrenService.updateChildrenStatus("0", ids.split(","));
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("剧集批量停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"剧集批量停用信息编号为：" + ids);
		}
		return Feedback.success("剧集批量停用成功");
	}
	
	/**
	 * 剧集启用
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "childrenReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback childrenReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要启用的剧集");
		}
		try {
			dramaChildrenService.updateChildrenStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("剧集启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用剧集信息编号为：" + id);
		}
		return Feedback.success("启用剧集成功");
	}
	
	@RequestMapping(value = "childListReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback childListReverse(@RequestParam("ids") String ids,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(ids)) {
			return Feedback.fail("请选择要批量启用的剧集");
		}
		try {
			dramaChildrenService.updateChildrenStatus("1", ids.split(","));
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("剧集批量启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"剧集批量启用信息编号为：" + ids);
		}
		return Feedback.success("剧集批量启用成功");
	}
}
