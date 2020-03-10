package com.br.ott.client.select.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.entity.DramaPosition;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaRecommend;
import com.br.ott.client.select.service.DramaPositionService;
import com.br.ott.client.select.service.DramaProgramService;
import com.br.ott.client.select.service.DramaRecommendService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.Constants.DicType;

/**
 * 文件名：RecommendPositionController.java 版权：BoRuiCubeNet. Copyright 2014-2015,All
 * rights reserved 公司名称：青岛博瑞立方科技有限公司 创建人：xiaojxiang 创建时间：2016-9-19
 */
@Controller
@RequestMapping(value = "/recommend")
public class DramaRecommendController extends BaseController {
	@Autowired
	private DramaRecommendService dramaRecommendService;

	@Autowired
	private DramaPositionService dramaPositionService;

	@Autowired
	private DramaProgramService dramaProgramService;

	@Autowired
	private OperaLogService operaLogService;
	private static final Logger logger = Logger
			.getLogger(DramaRecommendController.class);
	private static final String BUSI_NAME = "资产管理";

	/**
	 * 按分页查询推荐位列表
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-19
	 * 
	 * @param @param request
	 * @param @param dramaSource
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findRecommendPst", method = RequestMethod.GET)
	public String findRecommendPst(HttpServletRequest request,
			DramaRecommend recommendPosition, Model model) {
		recommendPosition.setDramaId(request.getParameter("dramaId"));
		recommendPosition.setCurrentPage(getPageNo(request));
		recommendPosition.setShowCount(getPageRow(request));
		if (request.getParameter("id") != null) {
			recommendPosition.setPrType(request.getParameter("id"));
		} else if (request.getParameter("prType") != null) {
			recommendPosition.setPrType(request.getParameter("prType"));
		}
		List<DramaRecommend> list = dramaRecommendService
				.findRecommendPstByPage(recommendPosition);
		model.addAttribute("list", list);
		model.addAttribute("recommendPosition", recommendPosition);
		list = null;
		return "select/listRPosition";
	}

	/**
	 * 编辑推荐位页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-19
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toRecommendPst", method = RequestMethod.GET)
	public String toRecommendPst(HttpServletRequest request, Model model) {
		String dramaId = request.getParameter("dramaId");
		String dramaName = request.getParameter("dramaName");
		DramaRecommend recommendPosition = new DramaRecommend();
		recommendPosition.setDramaId(dramaId);
		List<DramaRecommend> list = dramaRecommendService
				.findRecommendPstByPage(recommendPosition);
		recommendPosition.setDramaName(dramaName);
		model.addAttribute("rp", recommendPosition);
		model.addAttribute("list", list);
		list = null;
		return "select/listDPosition";
	}

	/**
	 * 编辑排序值页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-20
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDRecommend", method = RequestMethod.GET)
	public String toDRecommend(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		String dramaName = request.getParameter("dramaName");
		DramaRecommend recommendPosition = new DramaRecommend();
		if (StringUtil.isNotEmpty(id)) {
			recommendPosition = dramaRecommendService.getRecommendPstById(id);
		} else {
			String dramaId = request.getParameter("dramaId");
			String prType = request.getParameter("prType");
			recommendPosition = dramaRecommendService
					.getMaxSortByPrType(prType);
			if (recommendPosition == null) {
				recommendPosition = new DramaRecommend();
				recommendPosition.setSort("1");
			}
			recommendPosition.setPrType(prType);
			recommendPosition.setDramaId(dramaId);
		}
		recommendPosition.setDramaName(dramaName);
		model.addAttribute("recommendPosition", recommendPosition);
		return "select/rPositionInfo";
	}

	/**
	 * 转到维护资产推荐页面
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDramaRec", method = RequestMethod.GET)
	public String toDramaRec(HttpServletRequest request, Model model) {
		String dramaId = request.getParameter("dramaId");
		String dramaName = request.getParameter("dramaName");
		List<DramaRecommend> list = dramaRecommendService
				.findDramaRecByDramaId(dramaId);

		DramaPosition dp = new DramaPosition();
		dp.setStatus("1");
		List<DramaPosition> dpList = dramaPositionService
				.findDramaPositionByCond(dp);
		List<String> recList = new ArrayList<String>();
		for (DramaRecommend rec : list) {
			recList.add(rec.getPrType());
		}
		for (DramaPosition position : dpList) {
			if (recList.contains(position.getId())) {
				position.setCheck("true");
			} else {
				position.setCheck("false");
			}
		}

		model.addAttribute("dpList", dpList);
		DramaRecommend dr = new DramaRecommend();
		dr.setDramaId(dramaId);
		dr.setDramaName(dramaName);
		if (CollectionUtils.isNotEmpty(list)) {
			DramaRecommend dd = list.get(0);
			if (dd != null) {
				dr.setSort(dd.getSort());
			}
		}
		model.addAttribute("dr", dr);
		model.addAttribute("oldType",
				org.apache.commons.lang.StringUtils.join(recList, ","));
		dpList = null;
		recList = null;
		return "select/dramaRecInfo";
	}

	/**
	 * 资产推荐
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param @param request
	 * @param @param dr
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDramaRecList")
	public @ResponseBody
	Feedback addDramaRecList(HttpServletRequest request, DramaRecommend dr) {
		boolean flag = false;
		try {
			dr.setLastopt(SessionUtil.getLoginName());
			dramaRecommendService.addDramaRecList(dr,
					request.getParameter("oldType"));
			//及时更新缓存数据
			dramaProgramService.reloadDramaProgram();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产推荐保存失败");
		}
		if (flag) {
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"更新资产" + dr.getDramaName() + "推荐位为：" + dr.getcList());
		}
		return Feedback.success("资产推荐保存成功");
	}

	/**
	 * 更新推荐位数据
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-19
	 * 
	 * @param @param request
	 * @param @param dramaSource
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateRecommendPst")
	public @ResponseBody
	Feedback updateRecommendPst(HttpServletRequest request,
			DramaRecommend recommendPosition) {
		DramaRecommend old = dramaRecommendService
				.getRecommendPstById(recommendPosition.getId());
		try {
			recommendPosition.setLastopt(SessionUtil.getLoginName());
			dramaRecommendService.updateRecommendPst(recommendPosition);
			//及时更新缓存数据
			dramaProgramService.reloadDramaProgram();
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改资产推荐信息失败");
		}
		try {
			String diffStr = ObjectUtil.getDiffColumnDescript(old,
					recommendPosition);
			if (null != diffStr) {
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {

		}
		return Feedback.success("修改资产推荐信息成功");
	}

	/**
	 * 删除资产推荐
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-20
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "delRecommend", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delRecommend(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("资产推荐删除失败，提交参数不完整");
		}
		try {
			dramaRecommendService.delRecommend(id.split(","));
			//及时更新缓存数据
			dramaProgramService.reloadDramaProgram();
			flag = true;
		} catch (Exception e) {
			return Feedback.fail("资产推荐删除失败");
		}

		if (flag) {
			// 加入日记
			try {
				operaLogService.addOperaLog(OPERA_TYPE_DELETE, request,
						BUSI_NAME, "删除资产推荐编号为：" + id);
			} catch (RuntimeException e) {
				e.printStackTrace();
				logger.error("添加日记出错" + e);
			}
		}
		return Feedback.success("资产推荐删除成功");
	}

	/**
	 * 增加分类信息
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-20
	 * 
	 * @param @param productRecommend
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addRecommendPst", method = RequestMethod.POST)
	public @ResponseBody
	Feedback addRecommendPst(DramaRecommend dramaRecommend,
			HttpServletRequest request) {
		try {
			dramaRecommend.setLastopt(SessionUtil.getLoginName());
			String[] sort = dramaRecommend.getSort().split(",");
			String[] dramaid = dramaRecommend.getDramaId().split(",");
			for (int j = 0; j < dramaid.length; j++) {
				DramaRecommend dramaRecommend2 = new DramaRecommend();
				dramaRecommend2.setDramaId(dramaid[j]);
				dramaRecommend2.setPrType(dramaRecommend.getPrType());
				List<DramaRecommend> list = dramaRecommendService
						.findRecommendPstByPage(dramaRecommend2);
				if (CollectionUtils.isNotEmpty(list)) {
					return Feedback.fail("资产推荐添加失败,"
							+ request.getParameter("name") + "已存在");
				}
			}
			for (int i = 0; i < dramaid.length; i++) {
				dramaRecommend.setSort(sort[i]);
				dramaRecommend.setDramaId(dramaid[i]);
				dramaRecommendService.addRecommendPst(dramaRecommend);
				//及时更新缓存数据
				dramaProgramService.reloadDramaProgram();
				operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
						"添加资产推荐编号为：" + dramaRecommend.getId());
			}
		} catch (Exception e) {
			logger.error("资产推荐添加失败" + e.getMessage());
			return Feedback.fail("资产推荐添加失败");
		}
		return Feedback.success("资产推荐添加成功");
	}

	/**
	 * 排序值向上移动
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param id
	 * @param @param seq
	 * @param @param parentId
	 * @param @param request
	 * @param @param dramaNavigation
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "changeUpSequence", method = RequestMethod.POST)
	public @ResponseBody
	Feedback changeUpSequence(@RequestParam("id") String id,
			@RequestParam("sort") String sort,
			@RequestParam("prType") String prType, HttpServletRequest request,
			DramaRecommend dramaRecommend) {
		dramaRecommend.setSort(sort);
		dramaRecommend.setPrType(prType);
		List<DramaRecommend> list = dramaRecommendService
				.findDramaRecommendBySortUp(dramaRecommend);
		if (!CollectionUtils.isEmpty(list)) {
			dramaRecommend = new DramaRecommend();
			dramaRecommend.setId(id);
			dramaRecommend.setSort(list.get(0).getSort());
			dramaRecommendService.updateDramaRecommendBySort(dramaRecommend);
			dramaRecommend = new DramaRecommend();
			dramaRecommend.setId(list.get(0).getId());
			dramaRecommend.setSort(sort);
			dramaRecommendService.updateDramaRecommendBySort(dramaRecommend);
			//及时更新缓存数据
			dramaProgramService.reloadDramaProgram();
		} else {
			return Feedback.fail("移动失败,已是最大值");
		}
		return Feedback.success("排序值上移成功");
	}

	/**
	 * 排序值向下移动
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param id
	 * @param @param seq
	 * @param @param parentId
	 * @param @param request
	 * @param @param dramaNavigation
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "changeDownSequence", method = RequestMethod.POST)
	public @ResponseBody
	Feedback changeDownSequence(@RequestParam("id") String id,
			@RequestParam("sort") String sort,
			@RequestParam("prType") String prType, HttpServletRequest request,
			DramaRecommend dramaRecommend) {
		dramaRecommend.setSort(sort);
		dramaRecommend.setPrType(prType);
		List<DramaRecommend> list = dramaRecommendService
				.findDramaRecommendBySortDown(dramaRecommend);
		if (!CollectionUtils.isEmpty(list)) {
			// 循环将当前值向下调整并且将下个值往下调整
			dramaRecommend = new DramaRecommend();
			dramaRecommend.setId(id);
			dramaRecommend.setSort(list.get(0).getSort());
			dramaRecommendService.updateDramaRecommendBySort(dramaRecommend);
			dramaRecommend = new DramaRecommend();
			dramaRecommend.setId(list.get(0).getId());
			dramaRecommend.setSort(sort);
			dramaRecommendService.updateDramaRecommendBySort(dramaRecommend);
			//及时更新缓存数据
			dramaProgramService.reloadDramaProgram();
		} else {
			return Feedback.fail("移动失败,已是最小值");
		}
		return Feedback.success("排序值下移成功");
	}

	/**
	 * 增加推荐资产
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-30
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "addRecommendPst", method = RequestMethod.GET)
	public String addRecommendPst(HttpServletRequest request, Model model) {
		String prType = request.getParameter("prType");
		DramaRecommend recommendPosition = new DramaRecommend();
		recommendPosition.setPrType(prType);
		model.addAttribute("rp", recommendPosition);
		List<DramaNavigation> dnList = NavigationCache.getNavigationList1("0");
		model.addAttribute("dnList", dnList);

		Map<String, Dictionary> ptypes = DictCache
				.getDictValueList(DicType.JMLX);
		model.addAttribute("ptypes", ptypes);
		return "select/zjPositionInfo";
	}

	/**
	 * 获取资产列表
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-8
	 * 
	 * @param @param request
	 * @param @param response 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "findPositionTest", method = RequestMethod.GET)
	public void findPositionTest(HttpServletRequest request,
			HttpServletResponse response) {
		int skip = 1;
		String page = request.getParameter("page");
		if (!StringUtil.isEmpty(page)) {
			skip = Integer.valueOf(page);
		}
		String prType = request.getParameter("prType");
		String selectId = request.getParameter("selectId");
		DramaProgram dramaProgram = new DramaProgram();
		if (StringUtil.isNotEmpty(prType)) {
			List<DramaRecommend> drList = dramaRecommendService
					.findDramaRecByPrType(prType);
			List<String> dramaIds = new ArrayList<String>();
			for (DramaRecommend dr : drList) {
				dramaIds.add(dr.getDramaId());
			}
			if (StringUtil.isNotEmpty(selectId)) {
				String[] arr = selectId.split("c");
				for (String dramaId : arr) {
					dramaIds.add(dramaId);
				}
			}
			if (CollectionUtils.isNotEmpty(dramaIds)) {
				dramaProgram.setNoContainId(StringUtils.join(dramaIds, ","));
			}
		}

		dramaProgram.setName(request.getParameter("name"));
		dramaProgram.setTtype(request.getParameter("ttype"));
		dramaProgram.setParentId(request.getParameter("parentId"));
		dramaProgram.setPtype(request.getParameter("ptype"));
		dramaProgram.setCurrentPage(skip);
		dramaProgram.setStatus("1");
		List<DramaProgram> list = dramaProgramService
				.findDramaProgramByPage(dramaProgram);
		JSONObject json = new JSONObject();
		json.put("pageCount", dramaProgram.getTotalPage());
		json.put("total", dramaProgram.getTotalResult());
		JSONArray array = (JSONArray) JSONSerializer.toJSON(list);
		json.put("result", array.toArray());
		list = null;
		writeJson(response, json);
	}

	/**
	 * 获取当前分类最大排序值
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-10-9
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "getMaxSort", method = RequestMethod.GET)
	public void changeParentId(HttpServletResponse response,
			HttpServletRequest request) {
		DramaRecommend dramaRecommend = dramaRecommendService
				.getMaxSortByPrType(request.getParameter("prType"));
		if (dramaRecommend != null) {
			writeAjaxResult(response, dramaRecommend.getSort());
		} else {
			writeAjaxResult(response, "1");
		}
	}

	@RequestMapping(value = "del")
	public @ResponseBody
	Feedback del(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			DramaRecommend dramaRecommend = dramaRecommendService
					.getRecommendPstById(id);
			dramaRecommendService.delRecommendPstById(id);
			//及时更新缓存数据
			dramaProgramService.reloadDramaProgram();
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"删除推荐资产名：" + dramaRecommend.getDramaName());
			return Feedback.success("删除推荐资产成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("删除推荐资产失败");
		}
	}
}
