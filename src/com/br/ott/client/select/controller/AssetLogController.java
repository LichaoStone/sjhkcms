package com.br.ott.client.select.controller;

import java.util.Arrays;
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
import com.br.ott.client.select.entity.AssetLog;
import com.br.ott.client.select.service.AssetLogsService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.StringUtil;

/**
 * 文件名：AssetLogController.java
 *  版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：xiaojxiang
 *  创建时间：2016-9-12
 */
@Controller
@RequestMapping(value = "/log")
public class AssetLogController extends BaseController{
	@Autowired
	private AssetLogsService assetLogService;
	@Autowired
	private OperaLogService operaLogService;
	
	private static final String BUSI_NAME = "资产同步日志管理";
	
	@RequestMapping(value = "findAssetLog", method = RequestMethod.GET)
	public String findAssetLog(HttpServletRequest request,
			AssetLog assetLog, Model model) {
		assetLog.setCurrentPage(getPageNo(request));
		assetLog.setShowCount(getPageRow(request));
		List<AssetLog> list = assetLogService
				.findAssetLogByPage(assetLog);
		model.addAttribute("list", list);
		model.addAttribute("type", assetLog);
		list = null;
		return "select/listAssetLog";
	}
	/**
	 * 去资产同步详情页面
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-12
		 * 
		 * @param @param request
		 * @param @param model
		 * @param @return
		 * 返回类型：String
		 * @exception throws
	 */
	@RequestMapping(value = "toAssetLog", method = RequestMethod.GET)
	public String toAssetLog(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		AssetLog type = new AssetLog();
		if (StringUtil.isNotEmpty(id)) {
			type = assetLogService.getAssetLogById(id);
		}
		model.addAttribute("type", type);
		return "select/assetLogInfo";
	}
	
	/**
	 * 通过check 选中多个数据删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delAssetLog", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delAssetLog(@RequestParam("ids") String ids, HttpServletRequest request) {
		if (StringUtil.isEmpty(ids)) {
			return Feedback.fail("资产同步日志删除失败,请选择要删除的资产同步日志");
		}
		try {
			String[] logids = ids.split(",");
			assetLogService.delAssetLogList(Arrays.asList(logids));
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"资产同步日志编号是：" + ids);
			return Feedback.success("资产同步日志删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("资产同步日志删除失败");
		}
	}
}
