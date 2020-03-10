package com.br.ott.client.select.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.Global;
import com.br.ott.base.BaseController;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.service.DramaNavigationService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;

/* 
 *  
 *  文件名：ChannelTypeController.java
 *  创建人：pananz
 *  创建时间：2014-7-4 - 下午7:40:41
 */
@Controller
@RequestMapping(value = "/type")
public class DramaNavigationController extends BaseController {

	@Autowired
	private DramaNavigationService dramaNavigationService;

	@Autowired
	private OperaLogService operaLogService;

	private static final String BUSI_NAME = "栏目管理";

	/**
	 * 栏目管理页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param request
	 * @param @param dramaNavigation
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findNavigation", method = RequestMethod.GET)
	public String findNavigation(HttpServletRequest request,
			DramaNavigation dramaNavigation, Model model) {
		dramaNavigation.setCurrentPage(getPageNo(request));
		dramaNavigation.setShowCount(getPageRow(request));
		if (StringUtil.isEmpty(dramaNavigation.getOrderColumn())) {
			dramaNavigation.setOrderColumn("c.parentId,c.sequence");
		}
		List<DramaNavigation> list = dramaNavigationService.findDramaNavigationByPage(dramaNavigation);
		List<DramaNavigation> olist = NavigationCache.getNavigationList1("0");
		model.addAttribute("list", list);
		model.addAttribute("oList", olist);
		model.addAttribute("type", dramaNavigation);
		list = null;
		return "select/listNavigation";
	}

	/**
	 * 跳转一级栏目页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-10
	 * 
	 * @param @param request
	 * @param @param dramaNavigation
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findNgByPId", method = RequestMethod.GET)
	public String findNgByPId(HttpServletRequest request,
			DramaNavigation dramaNavigation, Model model) {
		dramaNavigation.setCurrentPage(getPageNo(request));
		dramaNavigation.setShowCount(getPageRow(request));
		dramaNavigation.setParentId("0");
		if (StringUtil.isEmpty(dramaNavigation.getOrderColumn())) {
			dramaNavigation.setOrderColumn("c.sequence");
		}
		List<DramaNavigation> list = dramaNavigationService
				.findDramaNavigationByPage(dramaNavigation);

		model.addAttribute("list", list);
		model.addAttribute("type", dramaNavigation);
		list = null;
		return "select/rNavigation";
	}

	/**
	 * 去添加或修改页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toNavigation", method = RequestMethod.GET)
	public String toNavigation(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		String xz = request.getParameter("xz");
		String tj = request.getParameter("tj");
		String parentId = request.getParameter("parentId");
		if (StringUtil.isNotEmpty(parentId) && (!"0".equals(parentId))) {
			xz = "2";
		}
		if (StringUtil.isNotEmpty(tj)) {
			model.addAttribute("tj", "1");
		}
		DramaNavigation type = new DramaNavigation();
		if (StringUtil.isNotEmpty(id)) {
			type = dramaNavigationService.getDramaNavigationById(id);
			model.addAttribute("sourceUrl", Global.SERVER_SOURCE_URL);
		}
		model.addAttribute("xz", xz);
		model.addAttribute("type", type);
		List<DramaNavigation> olist = NavigationCache.getNavigationList1("0");
		model.addAttribute("olist", olist);
		olist = null;
		return "select/navigationInfo";
	}

	/**
	 * 添加栏目
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param request
	 * @param @param type
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDramaNavigation")
	public @ResponseBody
	Feedback addDramaNavigation(HttpServletRequest request,
			DramaNavigation dramaNavigation) {
		boolean flag = false;
			try {
				boolean bool = dramaNavigationService.checkNavigationByName(
						dramaNavigation.getName(), dramaNavigation.getParentId());
				if (!bool) {
					return Feedback.fail("栏目保存失败,栏目名称已使用");
				}
				bool = dramaNavigationService.checkNavigationByCode(dramaNavigation
						.getCode());
				if (!bool) {
					return Feedback.fail("栏目保存失败,栏目编码已使用");
				}
				dramaNavigation.setStatus("1");
				dramaNavigationService.addNavigationType(dramaNavigation);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				return Feedback.fail("增加栏目失败");
			}
			if (flag) {
				operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
						"新增栏目名称为：" + dramaNavigation.getName());
			}
			return Feedback.success("增加栏目成功");
	}

	/**
	 * 编辑栏目
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param request
	 * @param @param type
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateDramaNavigation")
	public @ResponseBody
	Feedback updateDramaNavigation(HttpServletRequest request,
			DramaNavigation dramaNavigation) {
		boolean flag = false;
		DramaNavigation old = dramaNavigationService
				.getDramaNavigationById(dramaNavigation.getId());
		try {
			if (StringUtil.isNotEmpty(dramaNavigation.getName())) {
				if (!old.getName().equals(dramaNavigation.getName())) {
					boolean bool = dramaNavigationService
							.checkNavigationByName(dramaNavigation.getName(),
									dramaNavigation.getParentId());
					if (!bool) {
						return Feedback.fail("栏目修改失败,栏目名称已使用");
					}
				}
			}
			dramaNavigationService.updateDramaNavigation(dramaNavigation);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改栏目失败");
		}
		if (flag) {
			try {
				dramaNavigation.setStatus(old.getStatus());
				String diffStr = ObjectUtil.getDiffColumnDescript(old,
						dramaNavigation);
				if (null != diffStr) {
					// 写入系统操作日志
					operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
							BUSI_NAME, diffStr);
				}
			} catch (Exception e) {
			}
		}
		return Feedback.success("修改栏目成功");
	}

	/**
	 * 栏目启用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "dramaNavigationReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dramaNavigationReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("栏目启用失败,请选择要启用的栏目");
		}
		try {
			dramaNavigationService.updateDramaNavigationStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("栏目启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用栏目编号为：" + id);
		}
		return Feedback.success("栏目启用成功");
	}

	/**
	 * 栏目停用
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "dramaNavigationClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dramaNavigationClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("栏目停用失败,请选择要停用的栏目");
		}
		try {
			dramaNavigationService.updateDramaNavigationStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("栏目停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"停用栏目分类编号为：" + id);
		}
		return Feedback.success("栏目停用成功");
	}

	/**
	 * 删除
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "delDramaNavigation", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delDramaNavigation(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要删除的栏目");
		}
		try {
			dramaNavigationService.delDramaNavigationById(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("删除栏目类型的栏目");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"栏目的删除栏目编号为：" + id);
		}
		return Feedback.success("删除栏目成功");
	}

	/**
	 * 校验编码
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkNavigationByCode", method = RequestMethod.GET)
	public void checkNavigationByCode(HttpServletResponse response,
			HttpServletRequest request) {
		String code = request.getParameter("code");
		String oldCode = request.getParameter("oldCode");
		if (StringUtil.isEmpty(code) || oldCode.equals(code)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dramaNavigationService.checkNavigationByCode(code);
		writeAjaxResult(response, String.valueOf(result));
	}

	/**
	 * 校验名称
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkNavigationByName", method = RequestMethod.GET)
	public void checkNavigationByName(HttpServletResponse response,
			HttpServletRequest request) {
		String name = request.getParameter("name");
		String oldName = request.getParameter("oldName");
		String parentId = request.getParameter("parentId");
		if (StringUtil.isEmpty(name) || oldName.equals(name)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dramaNavigationService.checkNavigationByName(name,
				parentId);
		writeAjaxResult(response, String.valueOf(result));
	}

	/**
	 * 查询排序值
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-22
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "changeParentId", method = RequestMethod.GET)
	public void changeParentId(HttpServletResponse response,
			HttpServletRequest request) {
		String parentId = request.getParameter("parentId") == null ? "0"
				: request.getParameter("parentId");
		int maxSequence = dramaNavigationService.getMaxSequence(parentId);
		JSONObject json = new JSONObject();
		json.put("sequence", maxSequence);
		writeJson(response, json.toString());
	}

	/**
	 * 排序值向上移动
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-22
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "changeUpSequence", method = RequestMethod.POST)
	public @ResponseBody
	Feedback changeUpSequence(@RequestParam("id") String id,
			@RequestParam("sequence") String seq,
			@RequestParam("parentId") String parentId,
			HttpServletRequest request, DramaNavigation dramaNavigation) {
		dramaNavigation.setSequence(Integer.parseInt(seq));
		dramaNavigation.setParentId(parentId);
		List<DramaNavigation> list = dramaNavigationService
				.findDramaNavigationBySequenceUp(dramaNavigation);
		if (!CollectionUtils.isEmpty(list)) {
			dramaNavigation = new DramaNavigation();
			dramaNavigation.setId(id);
			dramaNavigation.setSequence(list.get(0).getSequence());
			dramaNavigationService
					.updateDramaNavigationBySequence(dramaNavigation);
			dramaNavigation = new DramaNavigation();
			dramaNavigation.setId(list.get(0).getId());
			dramaNavigation.setSequence(Integer.parseInt(seq));
			dramaNavigationService
					.updateDramaNavigationBySequence(dramaNavigation);
		} else {
			return Feedback.fail("移动失败,已是最大值");
		}
		return Feedback.success("排序值上移成功");
	}

	@RequestMapping(value = "changeDownSequence", method = RequestMethod.POST)
	public @ResponseBody
	Feedback changeDownSequence(@RequestParam("id") String id,
			@RequestParam("sequence") String seq,
			@RequestParam("parentId") String parentId,
			HttpServletRequest request, DramaNavigation dramaNavigation) {
		dramaNavigation.setSequence(Integer.parseInt(seq));
		dramaNavigation.setParentId(parentId);
		List<DramaNavigation> list = dramaNavigationService
				.findDramaNavigationBySequenceDown(dramaNavigation);
		if (!CollectionUtils.isEmpty(list)) {
			// 循环将当前值向下调整并且将下个值往下调整
			dramaNavigation = new DramaNavigation();
			dramaNavigation.setId(id);
			dramaNavigation.setSequence(list.get(0).getSequence());
			dramaNavigationService
					.updateDramaNavigationBySequence(dramaNavigation);
			dramaNavigation.setId(list.get(0).getId());
			dramaNavigation.setSequence(Integer.parseInt(seq));
			dramaNavigationService
					.updateDramaNavigationBySequence(dramaNavigation);
		} else {
			return Feedback.fail("移动失败,已是最小值");
		}
		return Feedback.success("排序值下移成功");
	}

	/**
	 * 推荐排序值向上移动
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-10
	 * 
	 * @param @param id
	 * @param @param seq
	 * @param @param parentId
	 * @param @param request
	 * @param @param dramaNavigation
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "changeUpSort", method = RequestMethod.POST)
	public @ResponseBody
	Feedback changeUpSort(@RequestParam("id") String id,
			@RequestParam("sequence") String sequence,
			@RequestParam("parentId") String parentId,
			HttpServletRequest request, DramaNavigation dramaNavigation) {
		dramaNavigation.setSequence(Integer.parseInt(sequence));
		dramaNavigation.setParentId(parentId);
		List<DramaNavigation> list = dramaNavigationService
				.findDramaNavigationBySortUp(dramaNavigation);
		if (!CollectionUtils.isEmpty(list)) {
			dramaNavigation = new DramaNavigation();
			dramaNavigation.setId(id);
			dramaNavigation.setSequence(list.get(0).getSequence());
			dramaNavigationService.updateDramaNavigationBySort(dramaNavigation);
			dramaNavigation = new DramaNavigation();
			dramaNavigation.setId(list.get(0).getId());
			dramaNavigation.setSequence(Integer.parseInt(sequence));
			dramaNavigationService.updateDramaNavigationBySort(dramaNavigation);
		} else {
			return Feedback.fail("移动失败,已是最大值");
		}
		return Feedback.success("排序值上移成功");
	}

	/**
	 * 推荐排序值下移
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-10
	 * 
	 * @param @param id
	 * @param @param seq
	 * @param @param parentId
	 * @param @param request
	 * @param @param dramaNavigation
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "changeDownSort", method = RequestMethod.POST)
	public @ResponseBody
	Feedback changeDownSort(@RequestParam("id") String id,
			@RequestParam("sequence") String sequence,
			@RequestParam("parentId") String parentId,
			HttpServletRequest request, DramaNavigation dramaNavigation) {
		dramaNavigation.setSequence(Integer.parseInt(sequence));
		dramaNavigation.setParentId(parentId);
		List<DramaNavigation> list = dramaNavigationService
				.findDramaNavigationBySortDown(dramaNavigation);
		if (!CollectionUtils.isEmpty(list)) {
			// 循环将当前值向下调整并且将下个值往下调整
			dramaNavigation = new DramaNavigation();
			dramaNavigation.setId(id);
			dramaNavigation.setSequence(list.get(0).getSequence());
			dramaNavigationService.updateDramaNavigationBySort(dramaNavigation);
			dramaNavigation.setId(list.get(0).getId());
			dramaNavigation.setSequence(Integer.parseInt(sequence));
			dramaNavigationService.updateDramaNavigationBySort(dramaNavigation);
		} else {
			return Feedback.fail("移动失败,已是最小值");
		}
		return Feedback.success("排序值下移成功");
	}

	/**
	 * 栏目推荐
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-10
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "navigationRecommend", method = RequestMethod.POST)
	public @ResponseBody
	Feedback navigationRecommend(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("栏目推荐失败,请选择要推荐的栏目");
		}
		try {
			dramaNavigationService.updateDramaNavigationRecommend("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("栏目推荐失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"推荐栏目编号为：" + id);
		}
		return Feedback.success("栏目推荐成功");
	}

	/**
	 * 取消栏目推荐
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-10
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "navigationRecommendClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback navigationRecommendClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("栏目取消推荐失败,请选择要停用的栏目");
		}
		try {
			dramaNavigationService.updateDramaNavigationRecommend("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("栏目取消推荐失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"取消推荐栏目分类编号为：" + id);
		}
		return Feedback.success("栏目取消推荐成功");
	}
	
	/**
	 * 改变推荐位顺序
	 * @author  lizhenghg  2017-03-17
	 * @param request
	 * @return Feedback
	 */
	@RequestMapping(value = "changeDNSequence", method = RequestMethod.POST)
	@ResponseBody
	public Feedback changeDNSequence(HttpServletRequest request) {
		String id = request.getParameter("id");
		String queue = request.getParameter("queue");
		String oldQueue = request.getParameter("oldQueue");
		boolean flag = false;
		try {
			DramaNavigation dn = dramaNavigationService.getDramaNavigationById(id);
			if (dn != null) {
				int maxSort = dramaNavigationService.getMaxSequence(dn.getParentId());
				if (Integer.parseInt(queue) > (maxSort - 1)) {
					return Feedback.fail("只能排序一级菜单，且最大值不能超过" + (maxSort - 1) + ",修改排序值失败");
				}
			}
			dramaNavigationService.changeDNSequence(dn, oldQueue, queue);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改排序值失败");
		}
		if (flag) {
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME, "排序值修改编号为：" + id);
		}
		return Feedback.success("排序值修改成功");
	}
}
