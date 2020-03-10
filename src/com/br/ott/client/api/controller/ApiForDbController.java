package com.br.ott.client.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseApiController;
import com.br.ott.client.api.service.ApiForDbService;
import com.br.ott.common.exception.OTTException;

@Controller
@RequestMapping("/tp/drama")
public class ApiForDbController extends BaseApiController {

	private Logger logger = Logger.getLogger(ApiForDbController.class);

	@Autowired
	private ApiForDbService apiForDbService;

	/**
	 * 获取一级推荐栏目接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-11
	 * 
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findRecType", method = { RequestMethod.GET, RequestMethod.POST })
	public void findRecType(HttpServletRequest request, HttpServletResponse response) {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		try {
			writeJson(response, callback + apiForDbService.findRecType(request) + (callback.equals("") ? "" : ")"));
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取一级推荐栏目失败" + e.getMessage());
			e.printStackTrace();
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}

	/**
	 * 获取栏目信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try
	 *                {}catch
	 */
	@RequestMapping(value = "findType")
	public void findType(HttpServletRequest request, HttpServletResponse response) {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		try {
			JSONObject content = commonDealBefore3(request);
			JSONObject result = this.apiForDbService.findType(content);
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
			result = null;
			content = null;
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取栏目信息失败" + e.getMessage());
			e.printStackTrace();
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}

	/**
	 * 获取推荐资产信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param request
	 * @param response
	 * @return 返回类型：void
	 * @exception try
	 *                {}catch
	 */
	@RequestMapping(value = "findRecDrama")
	public void findRecDrama(HttpServletRequest request, HttpServletResponse response) {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		JSONObject content = null;
		JSONObject result = null;
		try {
			content = commonDealBefore3(request);
			result = apiForDbService.findRecDrama(request, content);
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
			result = null;
			content = null;
		} catch (OTTException ot) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取推荐资产信息失败" + ot.getMessage());
			writeJson(response, callback + error("1", ot.getMessage()) + (callback.equals("") ? "" : ")"));
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取推荐资产信息失败" + e.getMessage());
			e.printStackTrace();
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}

	/**
	 * 资产列表查询接口(B端)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try
	 *                {}catch
	 */
	@RequestMapping(value = "findDrama")
	public void findDrama(HttpServletRequest request, HttpServletResponse response) {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		JSONObject content = null;
		JSONObject result = null;
		try {
			content = commonDealBefore3(request);
			result = apiForDbService.findDramaByPage(content);
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
			result = null;
			content = null;
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取资产列表查询失败" + e.getMessage());
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}

	/**
	 * 资产详细信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @param request
	 * @param response
	 * @param dramaId
	 * @return 返回类型：String
	 * @exception try
	 *                {}catch
	 *//*
	@RequestMapping(value = "getDramaInfo")
	public void getDramaInfo(HttpServletRequest request, HttpServletResponse response) {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		String header = request.getHeader("type");  //作为json还是jsonp的判断依据，假如是json，则存在type="json"，参数存放在body里。假如是jsonp，参数则附带在url上
		JSONObject content = null;
		if(StringUtils.isNotBlank(header)){
			content = commonDealBefore2(request);
		}
		String result = null;
		try {
			result = apiForDbService.getDramaInfo(request, content, header);
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
		} catch (OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取资产详细信息失败" + ott.getMessage());
			writeJson(response, callback + error("1", ott.getMessage()) + (callback.equals("") ? "" : ")"));
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取资产详细信息失败" + e.getMessage());
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		} finally {
			if(result != null)
				result = null;
			if(content != null && content.size() > 0)
				content = null;
		}
	}*/

	/**
	 * 点播首页面信息推荐栏目接口 (B端)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @param request
	 * @param response
	 * @param dramaId
	 * @return 返回类型：void
	 * @throws OTTException 
	 * @exception try
	 *                {}catch
	 */
	@RequestMapping(value = "dramaIndexBanner")
	public void dramaIndexBanner(HttpServletRequest request, HttpServletResponse response) throws OTTException {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		JSONObject result = new JSONObject();
		try {
			JSONObject temp = new JSONObject();
			request.setAttribute("sign", "ok");
			JSONArray tjBanner = (JSONArray)this.apiForDbService.findRecType(request); // {"tjBanner":[{},{}...]}
			temp.put("tjBanner", tjBanner);
			result.put("returnCode", "0");
			result.put("result", temp);
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
			result = null;
			tjBanner = null;
			temp = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ApiForDbController.class.getName() + "业务操作：点播首页面组合信息失败" + ex.getMessage());
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}

	/**
	 * 点播首页面信息滚动栏接口 (B端)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @param request
	 * @param response
	 * @param dramaId
	 * @return 返回类型：void
	 * @throws OTTException 
	 * @exception try
	 *                {}catch
	 */
	@RequestMapping(value = "dramaIndexTJ")
	public void dramaIndexTJ(HttpServletRequest request, HttpServletResponse response) throws OTTException {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		JSONObject content = null;
		try {
			content = commonDealBefore3(request);
			request.setAttribute("sign", "ok");
			JSONObject result = this.apiForDbService.findRecDrama(request, content);         // {"tj":[{},{}...]}
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
			result = null;
			content = null;
		} catch (OTTException e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：点播首页面组合信息失败" + e.getMessage());
			writeJson(response, callback + error("1", e.getMessage()) + (callback.equals("") ? "" : ")"));
		} catch (Exception ex) {
			logger.error(ApiForDbController.class.getName() + "业务操作：点播首页面组合信息失败" + ex.getMessage());
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}
	
	/**
	 * 详情页面信息组合接口(C端接口)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @param request
	 * @param response
	 * @param dramaId
	 * @return 返回类型：String
	 * @exception try
	 *                {}catch
	 */
	@RequestMapping(value = "dramaDetail")
	public void dramaDetail(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = null;
		try {
			result = apiForDbService.dealDramaDetail(request);
			writeJson(response, result);
			result = null;
		} catch (OTTException e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：详情页面组合信息失败" + e.getMessage());
			writeJson(response, error("1", e.getMessage()));
		} catch (Exception ex) {
			logger.error(ApiForDbController.class.getName() + "业务操作：详情页面组合信息失败" + ex.getMessage());
			ex.printStackTrace();
			writeJson(response, error("2", "系统异常，请稍后重试"));
		}
	}
	
	/**
	 * 添加用户收藏/最近收看资产接口(C端接口)
	 * 创建人：lizhenghg 创建时间：2016-11-4
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try{}catch
	 */
	@RequestMapping("addZClogForColAndRec")
	@ResponseBody
	public String addZClogForColAndRec(HttpServletRequest request, HttpServletResponse response) {		
		try {
			Map<String, Object> paramsMap = commonDealBefore(request);
			this.apiForDbService.addZClogForColAndRec(paramsMap);
			return success("0", JSONObject.fromObject(paramsMap).toString());
		} catch (OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：添加用户收藏/最近收看资产失败" + ott.getMessage());
			return error("1", ott.getMessage());
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：添加用户收藏/最近收看资产失败" + e.getMessage());
			e.printStackTrace();
			return error("2", "系统异常，请稍后重试");
		}
	}
	
	/**
	 * 获取用户收藏/最近收看资产接口
	 * 创建人：lizhenghg 创建时间：2016-11-3
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try{}catch
	 */
	@RequestMapping("findZClogForColAndRec")
	@ResponseBody
	public String findZClogForColAndRec(HttpServletRequest request, HttpServletResponse response){
		String callback = request.getParameter("callback") == null ? "" : ((String)request.getParameter("callback") + "(");
		JSONObject content = null;
		List<Map<String, Object>> result = null;
		try {
			content = commonDealBefore3(request);
			result = this.apiForDbService.findZClogForColAndRec(content);
			content = null;
			return callback + success("0", JSONArray.fromObject(result).toString()) + (callback.equals("") ? "" : ")");
		} catch (OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取用户收藏/最近收看资产失败" + ott.getMessage());
			return callback + error("1", ott.getMessage()) + (callback.equals("") ? "" : ")"); 
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取用户收藏/最近收看资产失败" + e.getMessage());
			e.printStackTrace();
			return callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")");
		}
	}
	
	/**
	 * IPTV首页全部点播信息组合接口
	 * 创建人：lizhenghg 创建时间：2016-11-14
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try{}catch
	 */
	@RequestMapping(value="IPTVDramaIndex")
	@ResponseBody
	public String IPTVDramaIndex(HttpServletRequest request, HttpServletResponse response){
		String callback = request.getParameter("callback") == null ? "" : request.getParameter("callback") + "(";
		JSONObject result = null;
		try {
			//返回格式null({"returnCode":"0","result"[{"name":"大片","id":"1","categoryId":"xx","children":[{},{},{}],"navChildren":[]},{},{}]})
			result = this.apiForDbService.IPTVDramaIndex();
			return callback + result + (callback.equals("") ? "" : ")");
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(ApiForDbController.class.getName() + "业务操作：IPTV首页全部点播信息组合失败" + e.getMessage());
			return callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")");
		}
	}
	
	/**
	 * 取消用户收藏资产接口(C端接口)
	 * 创建人：lizhenghg 创建时间：2016-11-16
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try{}catch
	 */
	@RequestMapping("cancelZClogForColAndRec")
	@ResponseBody
	public String cancelZClogForColAndRec(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> paramsMap = commonDealBefore(request);
			this.apiForDbService.cancelZClogForColAndRec(paramsMap);
			return success("0", JSONObject.fromObject(paramsMap).toString());
		} catch (OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：取消用户收藏/最近收看资产失败" + ott.getMessage());
			return error("1", ott.getMessage()); 
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：取消用户收藏/最近收看资产失败" + e.getMessage());
			return error("2", "系统异常，请稍后重试");
		}
	}
	
	/**
	 * 判断某资产是否已经收藏接口(C端接口)
	 * 创建人：lizhenghg 创建时间：2016-11-22
	 * @param request
	 * @param response
	 * @return 返回类型：String
	 * @exception try{}catch
	 */
	@RequestMapping("checkForCol")
	@ResponseBody
	public String checkForCol(HttpServletRequest request, HttpServletResponse response){
		try {
			Map<String, Object> paramsMap = commonDealBefore(request);
			List<Map<String, Object>> result = null;
			paramsMap.put("dramaType", "0");
			result = this.apiForDbService.findZCInfoForCol(paramsMap);
			if(CollectionUtils.isEmpty(result))
				return error("3", "该资产尚未收藏");
			return success("0", JSONObject.fromObject(paramsMap).toString());
		} catch (OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：判断某资产是否已经收藏失败" + ott.getMessage());
			return error("1", ott.getMessage()); 
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：判断某资产是否已经收藏失败" + e.getMessage());
			return error("2", "系统异常，请稍后重试");
		}
	}
	
	/**
	 * 获取资产播放URL (C端接口)
	 * @author lizhenghg  2017-7-23
	 * @param request
	 * @param response
	 * @return String
	 */
	@RequestMapping("getUrlAddress")
	@ResponseBody
	public String getUrlAddress(HttpServletRequest request, HttpServletResponse response){
		JSONObject content = null;
		JSONArray result;
		try {
			content = commonDealBefore3(request);
			result = this.apiForDbService.getUrlAddress(content);
			return success("0", result.toString());
		} catch (OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取资产播放URL失败" + ott.getMessage());
			return error("1", ott.getMessage()); 
		} catch (Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：获取资产播放URL失败" + e.getMessage());
			e.printStackTrace();
			return error("2", "系统异常，请稍后重试");
		}
	}
	/**
	 * 根据资产id获取该资产所对应的supercid (C端接口)
	 * @author lizhenghg  2017-09-14
	 * @param request
	 * @param response
	 * @return String
	 */
	@RequestMapping("getSupercidByID")
	@ResponseBody
	public String getSupercidByID(HttpServletRequest request, HttpServletResponse response) {
		JSONObject content = null;
		try {
		    content = commonDealBefore3(request);
			return success("0", this.apiForDbService.getSpuercidByID(content));
		} catch(OTTException ott) {
			logger.error(ApiForDbController.class.getName() + "业务操作：根据资产id获取该资产所对应的supercid失败" + ott.getMessage());
			return error("1", ott.getMessage());
		} catch(Exception e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：根据资产id获取该资产所对应的supercid失败" + e.getMessage());
			e.printStackTrace();
			return error("2", "系统异常，请稍后重试");
		}
	}
	
	
	/**
	 * 
	 * @Title:             dramaIndexTJ2
	 * @Description:     获取首页大片、动漫、剧场等推荐位以及推荐资源
	 * @param:             @param request
	 * @param:             @param response   
	 * @return:         void   
	 * @throws
	 */
	@RequestMapping("dramaIndexTJ2")
	@ResponseBody
	public void dramaIndexTJ2(HttpServletRequest request, HttpServletResponse response) {
		String callback = request.getParameter("callback") == null ? "" : (request.getParameter("callback") + "(");
		JSONObject content = null;
		try {
			content = commonDealBefore3(request);
			request.setAttribute("sign", "ok");
			JSONObject result = this.apiForDbService.findRecDrama2(request, content);         // {"tj":[{},{}...]}
			writeJson(response, callback + result + (callback.equals("") ? "" : ")"));
			result = null;
			content = null;
		} catch (OTTException e) {
			logger.error(ApiForDbController.class.getName() + "业务操作：点播首页面组合信息失败" + e.getMessage());
			writeJson(response, callback + error("1", e.getMessage()) + (callback.equals("") ? "" : ")"));
		} catch (Exception ex) {
			logger.error(ApiForDbController.class.getName() + "业务操作：点播首页面组合信息失败" + ex.getMessage());
			writeJson(response, callback + error("2", "系统异常，请稍后重试") + (callback.equals("") ? "" : ")"));
		}
	}
}