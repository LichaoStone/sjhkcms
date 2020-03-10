package com.br.ott.client.operasset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.operasset.entity.Assets;
import com.br.ott.client.operasset.service.HandleAssetsService;
import com.br.ott.common.util.Feedback;

@Controller
@RequestMapping("/oper")
public class HandleAssetsController extends BaseController{
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(HandleAssetsController.class);
	
	@Autowired
	private HandleAssetsService handleAssetsService;
	
	/**查询资产列表
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param asset
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/findAssets")
	public String findAssets(HttpServletRequest request, Assets asset, Model model){
		asset.setCurrentPage(getPageNo(request));
		asset.setShowCount(getPageRow(request));
		List<Assets> assetList = handleAssetsService.findAssetsByPage(asset);
		model.addAttribute("list", assetList);
		model.addAttribute("asset", asset);
		assetList = null;
		return "operassets/listAssetsInfo";
	}
	
	/**查询资产详情
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping("/toAssets")
	public String toAssets(HttpServletRequest request, Model model){
		Assets asset = new Assets();
		String correlateID = request.getParameter("correlateID");
		if(StringUtils.isNotBlank(correlateID))
			asset = this.handleAssetsService.getAssetsByCorrelateID(correlateID);
		model.addAttribute("asset", asset);
		return "operassets/dProgramInfo";
	}
	
	/**(批量)生成资产文件
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping(value="/toCreateFileForBatch", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Feedback toCreateFileForBatch(HttpServletRequest request){
		String assetlist = null;
		if((assetlist = request.getParameter("assetlist")) == null || "".equals((assetlist = request.getParameter("assetlist"))))
			return new Feedback(false, "传递参数不能为空");
		return this.handleAssetsService.toCreateFileForBatch(assetlist);
	}
	
	/**(批量)处理资产文件
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping(value="/toDealFileForBatch")
	@ResponseBody
	public Feedback toDealFileForBatch(HttpServletRequest request){
		String assetlist = null;
		if((assetlist = request.getParameter("assetlist")) == null || "".equals((assetlist = request.getParameter("assetlist"))))
			return new Feedback(false, "传递参数不能为空");
		return this.handleAssetsService.toDealFileForBatch(assetlist);
	}
}
