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
import com.br.ott.client.select.entity.DramaPosition;
import com.br.ott.client.select.service.DramaPositionService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;

/**
 * 文件名：DramaPositionController.java 版权：BoRuiCubeNet. Copyright 2014-2015,All
 * rights reserved 公司名称：青岛博瑞立方科技有限公司 创建人：xiaojxiang 创建时间：2016-9-29
 */
@Controller
@RequestMapping(value = "/position")
public class DramaPositionController extends BaseController {

	@Autowired
	private OperaLogService operaLogService;

	@Autowired
	private DramaPositionService dramaPositionService;

	private static final String BUSI_NAME = "推荐位管理";

	/**
	 * 推荐位管理页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param request
	 * @param @param dramaPosition
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findPosition", method = RequestMethod.GET)
	public String findPosition(HttpServletRequest request,
			DramaPosition dramaPosition, Model model) {
		dramaPosition.setCurrentPage(getPageNo(request));
		dramaPosition.setShowCount(getPageRow(request));
		List<DramaPosition> list = dramaPositionService
				.findDramaPositionByPage(dramaPosition);
		model.addAttribute("list", list);
		model.addAttribute("type", dramaPosition);
		list = null;
		return "select/listPosition";
	}

	/**
	 * 新增or修改推荐位
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toPosition", method = RequestMethod.GET)
	public String toPosition(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		DramaPosition type = new DramaPosition();
		if (StringUtil.isNotEmpty(id)) {
			type = dramaPositionService.getDramaPositionById(id);
		}
		model.addAttribute("type", type);
		return "select/positionInfo";
	}

	/**
	 * 推荐位启用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "dramaPositionReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dramaPositionReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("推荐位启用失败,请选择要启用的推荐位");
		}
		try {
			dramaPositionService.updateDramaPositionStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("推荐位启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用推荐位编号为：" + id);
		}
		return Feedback.success("推荐位启用成功");
	}

	/**
	 * 推荐位停用
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "dramaPositionClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dramaPositionClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("推荐位停用失败,请选择要停用的推荐位");
		}
		try {
			dramaPositionService.updateDramaPositionStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("推荐位停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"停用推荐位编号为：" + id);
		}
		return Feedback.success("推荐位停用成功");
	}

	/**
	 * 增加推荐位
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param request
	 * @param @param dramaPosition
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDramaPosition")
	public @ResponseBody
	Feedback addDramaPosition(HttpServletRequest request,
			DramaPosition dramaPosition) {
		boolean flag = false;
		try {
			boolean bool = dramaPositionService
					.checkPositionByCode(dramaPosition.getCode());
			if (!bool) {
				return Feedback.fail("推荐位保存失败,推荐位编码已使用");
			}
			dramaPositionService.addDramaPosition(dramaPosition);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("增加推荐位失败");
		}
		if (flag) {
			operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
					"新增推荐位为：" + dramaPosition.getName());
		}
		return Feedback.success("增加推荐位成功");
	}

	/**
	 * 修改推荐位
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param request
	 * @param @param dramaPosition
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateDramaPosition")
	public @ResponseBody
	Feedback updateDramaPosition(HttpServletRequest request,
			DramaPosition dramaPosition) {
		boolean flag = false;
		DramaPosition old = dramaPositionService
				.getDramaPositionById(dramaPosition.getId());
		try {
			/*boolean bool = dramaPositionService
					.checkPositionByCode(dramaPosition.getCode());
			if (!bool) {
				return Feedback.fail("推荐位保存失败,推荐位编码已使用");
			}*/
			dramaPositionService.updateDramaPosition(dramaPosition);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改推荐位失败");
		}
		if (flag) {
			try {
				dramaPosition.setStatus(old.getStatus());
				String diffStr = ObjectUtil.getDiffColumnDescript(old,
						dramaPosition);
				if (null != diffStr) {
					// 写入系统操作日志
					operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
							BUSI_NAME, diffStr);
				}
			} catch (Exception e) {
			}
		}
		return Feedback.success("修改推荐位成功");
	}

	/**
	 * 校验编码
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkPositionByCode", method = RequestMethod.GET)
	public void checkPositionByCode(HttpServletResponse response,
			HttpServletRequest request) {
		String code = request.getParameter("code");
		String oldCode = request.getParameter("oldCode");
		if (StringUtil.isEmpty(code) || oldCode.equals(code)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dramaPositionService.checkPositionByCode(code);
		writeAjaxResult(response, String.valueOf(result));
	}
}
