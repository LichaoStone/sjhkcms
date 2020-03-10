package com.br.ott.client.select.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.br.ott.Global;
import com.br.ott.base.BaseController;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaType;
import com.br.ott.client.select.service.DramaProgramService;
import com.br.ott.client.select.service.DramaTypeService;
import com.br.ott.common.jackson.TinyTreeBean;
import com.br.ott.common.jackson.TinyTreeUtils;
import com.br.ott.common.util.Constants.DicType;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.JsonUtils;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.StringUtil;

/* 
 *  
 *  文件名：DramaProgramController.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-10 - 下午3:26:22
 */
@Controller
@RequestMapping(value = "/select")
public class DramaProgramController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(DramaProgramController.class);
	@Autowired
	private DramaProgramService dramaProgramService;
	@Autowired
	private DramaTypeService dramaTypeService;
	@Autowired
	private OperaLogService operaLogService;

	private static final String BUSI_NAME = "资产管理";

	/**
	 * 按分页查询资产基础信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:37:27
	 * 
	 * @param request
	 * @param DP
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findDProgram", method = RequestMethod.GET)
	public String findDProgram(HttpServletRequest request,
			DramaProgram dprogram, Model model) {
		dprogram.setCurrentPage(getPageNo(request));
		dprogram.setShowCount(getPageRow(request));
		if (StringUtil.isEmpty(dprogram.getStatus())) {
			dprogram.setStatus("0");
		} else if ("all".equals(dprogram.getStatus())) {
			dprogram.setStatus("");
		}
		List<DramaProgram> list = dramaProgramService
				.findDramaProgramByPage(dprogram);
		model.addAttribute("list", list);
		model.addAttribute("dprogram", dprogram);
		list = null;

		Map<String, Dictionary> ptypes = DictCache
				.getDictValueList(DicType.JMLX);
		model.addAttribute("ptypes", ptypes);

		Map<String, Dictionary> cProviders = DictCache
				.getDictValueList(DicType.LRTGS);
		model.addAttribute("cProviders", cProviders);

		List<DramaNavigation> dnList = NavigationCache.getNavigationList1("0");
		model.addAttribute("dnList", dnList);
		dnList = null;
		if (StringUtil.isNotEmpty(dprogram.getParentId())) {
			List<DramaNavigation> dnList2 = NavigationCache
					.getNavigationList(dprogram.getParentId());
			model.addAttribute("dnList2", dnList2);
		}
		return "select/listDProgram";
	}

	/**
	 * 按分页查询资产基础信息（上线下线操作）
	 * 
	 * 创建人：pananz 创建时间：2016-9-26
	 * 
	 * @param @param request
	 * @param @param dprogram
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findProgram", method = RequestMethod.GET)
	public String findProgram(HttpServletRequest request,
			DramaProgram dprogram, Model model) {
		dprogram.setCurrentPage(getPageNo(request));
		dprogram.setShowCount(getPageRow(request));
		if (StringUtil.isEmpty(dprogram.getStatus())) {
			dprogram.setStatus("0");
		} else if ("all".equals(dprogram.getStatus())) {
			dprogram.setStatus("");
		}
		List<DramaProgram> list = dramaProgramService
				.findDramaProgramByPage(dprogram);
		model.addAttribute("list", list);
		model.addAttribute("dprogram", dprogram);
		list = null;

		Map<String, Dictionary> ptypes = DictCache
				.getDictValueList(DicType.JMLX);
		model.addAttribute("ptypes", ptypes);

		Map<String, Dictionary> cProviders = DictCache
				.getDictValueList(DicType.LRTGS);
		model.addAttribute("cProviders", cProviders);

		List<DramaNavigation> dnList = NavigationCache.getNavigationList1("0");
		model.addAttribute("dnList", dnList);
		dnList = null;
		if (StringUtil.isNotEmpty(dprogram.getParentId())) {
			List<DramaNavigation> dnList2 = NavigationCache
					.getNavigationList(dprogram.getParentId());
			model.addAttribute("dnList2", dnList2);
		}
		return "select/listProgram";
	}

	/**
	 * 转到编辑资产页面
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:37:18
	 * 
	 * @param request
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDProgram", method = RequestMethod.GET)
	public String toDProgram(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		DramaProgram dp = new DramaProgram();
		if (StringUtil.isNotEmpty(id)) {
			dp = dramaProgramService.getDramaProgramById(id);
			model.addAttribute("sourceUrl", Global.SERVER_SOURCE_URL);
		}
		model.addAttribute("dp", dp);

		// 获取字典类型信息
		Map<String, Dictionary> ptypes = DictCache
				.getDictValueList(DicType.JMLX);
		Map<String, Dictionary> cProviders = DictCache
				.getDictValueList(DicType.LRTGS);
		Map<String, Dictionary> languages = DictCache
				.getDictValueList(DicType.YUYAN);
		Map<String, Dictionary> bitrates = DictCache
				.getDictValueList(DicType.MALV);
		Map<String, Dictionary> origins = DictCache
				.getDictValueList(DicType.JMZYCD);
		model.addAttribute("ptypes", ptypes);
		model.addAttribute("cProviders", cProviders);
		model.addAttribute("languages", languages);
		model.addAttribute("bitrates", bitrates);
		model.addAttribute("origins", origins);
		return "select/dProgramInfo";
	}

	/**
	 * 查询资产详情
	 * 
	 * 创建人：pananz 创建时间：2016-9-19
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDProgramDetail", method = RequestMethod.GET)
	public String toDProgramDetail(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		DramaProgram dp = new DramaProgram();
		if (StringUtil.isNotEmpty(id)) {
			dp = dramaProgramService.getDramaProgramById(id);
			model.addAttribute("sourceUrl", Global.SERVER_SOURCE_URL);
		}
		model.addAttribute("dp", dp);
		// 获取字典类型信息
		Map<String, Dictionary> ptypes = DictCache
				.getDictValueList(DicType.JMLX);
		Map<String, Dictionary> cProviders = DictCache
				.getDictValueList(DicType.LRTGS);
		Map<String, Dictionary> languages = DictCache
				.getDictValueList(DicType.YUYAN);
		Map<String, Dictionary> origins = DictCache
				.getDictValueList(DicType.JMZYCD);
		model.addAttribute("ptypes", ptypes);
		model.addAttribute("cProviders", cProviders);
		model.addAttribute("languages", languages);
		model.addAttribute("origins", origins);

		return "select/dProgramDetail";

	}

	private static final String ROOT_NAVIGATION_ID = "0";

	private List<TinyTreeBean> buildTinyTreeBean(List<DramaNavigation> navList,
			List<String> typeList) {
		if (CollectionUtils.isEmpty(navList)) {
			return null;
		}
		TinyTreeBean root = new TinyTreeBean(ROOT_NAVIGATION_ID, null);
		Map<String, TinyTreeBean> treeMap = new HashMap<String, TinyTreeBean>();
		treeMap.put(ROOT_NAVIGATION_ID, root);
		for (DramaNavigation channel : navList) {
			TinyTreeBean node = new TinyTreeBean(channel.getId(),
					channel.getName());
			node.setOrderId(channel.getSequence());
			if (CollectionUtils.isNotEmpty(typeList)) {
				if (typeList.contains(channel.getId())) {
					node.setChecked(true);
				} else {
					node.setChecked(false);
				}
			}
			treeMap.put(channel.getId(), node);
		}
		for (DramaNavigation pt : navList) {
			TinyTreeBean parent = treeMap.get(pt.getParentId());
			if (null == parent) {
				continue;
			}
			parent.addChild(treeMap.get(pt.getId()));
		}
		return treeMap.get(ROOT_NAVIGATION_ID).getChildren();
	}

	/**
	 * 查询资产所在栏目
	 * 
	 * 创建人：pananz 创建时间：2016-9-21
	 * 
	 * @param @param request
	 * @param @param response 返回类型：void
	 * @exception throws
	 */

	@RequestMapping(value = "buildDramaTypeTree", method = RequestMethod.POST)
	public void buildDramaTypeTree(HttpServletRequest request,
			HttpServletResponse response) {
		String dramaId = request.getParameter("dramaId");
		List<DramaType> dtList = dramaTypeService.findNavByDramaId(dramaId);
		List<String> cList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(dtList)) {
			for (DramaType dt : dtList) {
				cList.add(dt.getNavId());
			}
		}
		List<TinyTreeBean> list = buildTinyTreeBean(NavigationCache.navList,
				cList);
		sortType(list);
		dtList = null;
		cList = null;
		writeAjaxResult(response, TinyTreeUtils.toJson(list));
		list = null;
	}

	@SuppressWarnings("unchecked")
	private void sortType(List<TinyTreeBean> typeList) {
		if (CollectionUtils.isEmpty(typeList))
			return;
		BeanComparator menuBC = new BeanComparator("orderId");// 排序
		Collections.sort(typeList, menuBC);
		for (TinyTreeBean bean : typeList) {
			sortType(bean.getChildren());
		}
	}

	/**
	 * 转到资产栏目编辑页面
	 * 
	 * 创建人：pananz 创建时间：2016-9-21
	 * 
	 * @param @param request
	 * @param @param model
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toDramaTypeList", method = RequestMethod.GET)
	public String toDramaTypeList(HttpServletRequest request, Model model) {
		String dramaId = request.getParameter("dramaId");
		String dramaName = request.getParameter("dramaName");
		String oldType = request.getParameter("oldType");
		List<DramaType> dtList = dramaTypeService.findNavByDramaId(dramaId);
		List<String> cList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(dtList)) {
			for (DramaType dt : dtList) {
				cList.add(dt.getNavId());
			}
		}
		DramaType dramaType = new DramaType();
		dramaType.setDramaId(dramaId);
		dramaType.setDramaName(dramaName);
		model.addAttribute("dramaType", dramaType);
		model.addAttribute("oldType", oldType);

		model.addAttribute("dType",
				org.apache.commons.lang.StringUtils.join(cList, ","));
		dtList = null;
		cList = null;
		return "select/dramaTypeList";
	}

	/**
	 * 增加资产栏目
	 * 
	 * 创建人：pananz 创建时间：2016-9-22
	 * 
	 * @param @param request
	 * @param @param dramaType
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDramaTypeList")
	public @ResponseBody
	Feedback addCTypeList(HttpServletRequest request) {
		boolean flag = false;
		String dramaId = request.getParameter("dramaId");
		String dtList = request.getParameter("dtList");
		String dType = request.getParameter("dType");
		try {
			dramaTypeService.addDramaTypeList(dramaId, dtList, dType);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产栏目更新失败");
		}
		if (flag) {
			operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
					"资产更新栏目为：从" + dType + "更新为" + dtList);
		}
		return Feedback.success("资产栏目更新成功");
	}

	/**
	 * 校验资产节目基础信息名称是否存在
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:37:04
	 * 
	 * @param reDPonse
	 * @param request
	 *            返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkDPName", method = RequestMethod.GET)
	public void checkDPName(HttpServletResponse response,
			HttpServletRequest request) {
		String name = request.getParameter("name");
		String oldName = request.getParameter("oldName");
		if (StringUtil.isEmpty(name) || oldName.equals(name)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dramaProgramService.checkDPName(name);
		writeAjaxResult(response, String.valueOf(result));
	}

	/**
	 * 资产名称是否存在(集合)
	 * 
	 * 创建人：pananz 创建时间：2016-11-15
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@SuppressWarnings({"static-access" })
	@RequestMapping(value = "checkNameList")
	public void checkNameList(HttpServletResponse response,
			HttpServletRequest request) {
		try {
			String jsonString = StringUtil.requestGetStreamToString(request);
			logger.debug("同步请求信息：" + jsonString);
			if (StringUtil.isEmpty(jsonString)) {
				writeAjaxResult(response, "");
			}
			Object[] arr = JsonUtils.getObjectArrayFromJson(jsonString);
			Map<String, String> map = new HashMap<String, String>();
			for (Object name : arr) {
				boolean result = dramaProgramService.checkDPName(name.toString());
				map.put(name.toString(), String.valueOf(result));
			}
			JSONObject json = new JSONObject();
			writeAjaxResult(response, json.fromObject(map).toString());
		} catch (Exception e) {
			logger.debug("同步请求处理异常" + e.getMessage());
			writeAjaxResult(response, "{}");
		}
	}

	/**
	 * 校验assetId是否存在
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param response
	 * @param @param request 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkAssetId", method = RequestMethod.GET)
	public void checkAssetId(HttpServletResponse response,
			HttpServletRequest request) {
		String assetId = request.getParameter("assetId");
		if (StringUtil.isEmpty(assetId)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dramaProgramService.checkAssetId(assetId);
		writeAjaxResult(response, String.valueOf(result));
	}

	/**
	 * 修改资产节目基础信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:36:58
	 * 
	 * @param request
	 * @param DP
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "updateDProgram")
	public @ResponseBody
	Feedback updateDProgram(HttpServletRequest request, DramaProgram dp) {
		DramaProgram old = dramaProgramService.getDramaProgramById(dp.getId());
		try {
			// if (!old.getName().equals(dp.getName())) {
			// boolean bool = dramaProgramService.checkDPName(dp.getName());
			// if (!bool) {
			// return Feedback.fail("资产基础信息修改失败,资产名称已使用");
			// }
			// }
			dp.setOperator(SessionUtil.getLoginName());
			dramaProgramService.updateDramaProgram(dp,
					request.getParameter("dType2"));
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产基础信息失败");
		}
		try {
			dp.setStatus(old.getStatus());
			String diffStr = ObjectUtil.getDiffColumnDescript(old, dp);
			if (null != diffStr) {
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {

		}
		return Feedback.success("修改资产基础信息成功");
	}

	/**
	 * 增加资产节目基础信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:39:03
	 * 
	 * @param request
	 * @param DP
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "addDProgram")
	public @ResponseBody
	Feedback addDProgram(HttpServletRequest request, DramaProgram dp) {
		try {
			// boolean bool = dramaProgramService.checkDPName(dp.getName());
			// if (!bool) {
			// return Feedback.fail("资产基础信息保存失败,资产名称已使用");
			// }
			dp.setOperator(SessionUtil.getLoginName());
			dp.setIsLoad("1");
			dramaProgramService.addDramaProgram(dp);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("增加资产基础信息失败");
		}
		operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
				"新增资产基础信息名称为：" + dp.getName());
		return Feedback.success("增加资产基础信息成功");
	}

	@RequestMapping(value = "toCloseProgram", method = RequestMethod.GET)
	public String toCloseProgram(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		DramaProgram dp = dramaProgramService.getDramaProgramById(id);
		model.addAttribute("dp", dp);
		return "select/closeProgram";
	}

	/**
	 * 下线资产节目
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:41:33
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "dProgramClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dProgramClose(HttpServletRequest request) {
		String id = request.getParameter("id");
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要下线的资产");
		}
		String assetId = request.getParameter("assetId");
		String msg = request.getParameter("msg");
		try {
			dramaProgramService.closeDramaProgram("2", id, assetId, msg);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产下线失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"下线资产编号为：" + id + "资产id" + assetId + "下线原因：" + msg);
		}
		return Feedback.success("资产下线成功");
	}

	/**
	 * 待上线资产节目
	 * 
	 * 创建人：pananz 创建时间：2015-3-10 - 下午3:41:21
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedbacd
	 * @exception throws
	 */
	@RequestMapping(value = "dProgramReady", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dProgramready(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要待上线的资产");
		}
		try {
			dramaProgramService.updateDramaProgramStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产待上线失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用资产待上线编号为：" + id);
		}
		return Feedback.success("资产待上线成功");
	}

	/**
	 * 上线资产
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param id
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "dProgramReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dProgramReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("请选择要上线的资产");
		}
		try {
			dramaProgramService.updateDramaProgramStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产上线失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用资产上线编号为：" + id);
		}
		return Feedback.success("资产上线成功");
	}

	/**
	 * 重置资产简拼
	 * 
	 * 创建人：pananz 创建时间：2016-10-24
	 * 
	 * @param @param request
	 * @param @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "resetProgramJP", method = RequestMethod.POST)
	public @ResponseBody
	Feedback resetProgramJP(HttpServletRequest request) {
		try {
			dramaProgramService.resetDramaProgramOfJP();
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("重置资产简拼失败");
		}
		return Feedback.success("重置资产简拼成功");
	}

	/**
	 * 使用execl批量导入资产
	 * 
	 * 创建人：lizhenghg 创建时间：2016-9-28
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @exception throws
	 */
	@RequestMapping(value = "getDPInfosByImportExecl", method = RequestMethod.POST)
	public void getDPInfosByImportExecl(HttpServletRequest request,
			HttpServletResponse response) {
		String msg = null;
		try {
			MultipartFile file = null;
			MultipartHttpServletRequest mr = null;
			if (request instanceof MultipartHttpServletRequest)
				mr = (MultipartHttpServletRequest) request;
			if (null != mr)
				file = mr.getFile("fileApp");
			msg = this.dramaProgramService.insertDPForExecl(file, request);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "读取文件失败";
		}
		writeAjaxResult(response, "{\"msg\":\"" + msg + "\"}");
	}

	/**
	 * 批量上线资产审核
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-09
	 * 
	 * @param ids
	 *            ,sign,request
	 * @param model
	 * @return Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "batchOnlineForDP", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Feedback batchOnlineForDP(@RequestParam("ids") String ids,
			@RequestParam("sign") String sign,
			@RequestParam("theResponse") String theResponse,
			HttpServletRequest request) {

		boolean flag = false;
		String msg = null;
		String statusSign = (sign == null ? "" : sign);
		if ("1".equals(statusSign))
			msg = "上线";
		if ("2".equals(statusSign))
			msg = "下线";
		if (StringUtil.isEmpty(ids))
			return Feedback.fail("资产批量" + msg + "失败");
		String[] idStrs = ids.split(",");
		List id_array = new ArrayList();
		for (String id : idStrs) {
			id_array.add(id);
		}
		Map paramsMap = new HashMap();
		paramsMap.put("id_array", id_array);
		if (!"".equals(statusSign))
			paramsMap.put("status", statusSign);
		if (!"".equals(theResponse))
			paramsMap.put("", theResponse);
		try {
			dramaProgramService.updateDPStatusForBatch(paramsMap);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产批量" + msg + "失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, BUSI_NAME,
					"启用资产" + msg + "编号为：" + ids);
		}
		return Feedback.success("资产批量" + msg + "成功");
	}
	
}
