package com.br.ott.client.api.controller.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

import com.br.ott.Global;
import com.br.ott.client.JsonFormat;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.ApiCode;
import com.br.ott.common.util.StringUtil;

public abstract class BaseManager {
	private static final Logger LOGGER = Logger
			.getLogger(BaseManager.class);
	

	HttpServletRequest request = null;
	HttpServletResponse response = null;

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	protected String getParameter(HttpServletRequest request, String param) {
		String str = request.getParameter(param);
		if (str == null) {
			str = "";
		}
		return str;
	}

	/**
	 * 获取参数，如果nullable为false ，则直接返回不为空结果 创建人：陈登民 创建时间：2012-11-6 - 下午5:38:38
	 * 
	 * @param request
	 * @param key
	 * @param nullable
	 * @return 返回类型：String
	 * @throws OTTException
	 * @exception throws
	 */
	protected String getParameter(HttpServletRequest request, String key,
			boolean nullable) throws OTTException {
		String str = request.getParameter(key);
		if (nullable) {
			return str;
		}
		if (str == null) {
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + key + "缺失!");
		}
		if ("".equals(str.trim())) {
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + key + "不能为空");
		}
		return str;
	}

	/**
	 * 从请求中解析出JSON对象
	 * 
	 * @param request
	 * @return JSON对象，如果返回为空，解析JSON时出现异常
	 * @throws OTTException
	 */
	protected JSONObject getJSONObj(HttpServletRequest request)
			throws OTTException {
		try {
			String jsonString = StringUtil.requestGetStreamToString(request);
			if (StringUtil.isEmpty(jsonString)) {
				jsonString = JsonFormat.JSON_RESULT_EMPTY;
			}
			LOGGER.debug("客户端JSON：" + jsonString);
			return JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			throw new OTTException(ApiCode.DECODE_JSON_ERROR, "JSON格式不正确");
		}
	}

	protected JSONArray getJSONArr(HttpServletRequest request)
			throws OTTException {
		try {
			String jsonString = StringUtil.requestGetStreamToString(request);
			LOGGER.debug("客户端jsonString：" + jsonString);
			return JSONArray.fromObject(jsonString);
		} catch (Exception e) {
			throw new OTTException(ApiCode.DECODE_JSON_ERROR, "JSON格式不正确");
		}
	}

	/**
	 * 从JSON对象中获取字符串值
	 * 
	 * @param obj
	 * @param key
	 * @return 如果obj为空，或JSON对象中不存在key，则返回NULL
	 */
	protected String getStringFromJsonObject(JSONObject json, String key) {
		try {
			if (json == null) {
				return null;
			}
			return json.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从JSON对象中获取字符串值,如果JSON对象中不存在该字段，则抛出异常
	 * 
	 * @param obj
	 * @param key
	 * @param nullable
	 *            是否可为空
	 * @return 如果obj为空，或JSON对象中不存在key，则返回NULL
	 * @throws OTTException
	 *             如果nullable为false，则返回参数不能为空的异常
	 */
	protected String getStringFromJsonObject(JSONObject json, String key,
			boolean nullable) throws OTTException {
		try {
			if (json == null) {
				return null;
			}
			return json.getString(key);
		} catch (JSONException e) {
			if (nullable) {
				return null;
			}
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + key + "缺失!");
		}
	}

	/**
	 * 从请求头中获取公用请求参数
	 * 
	 * @param request
	 * @param key
	 * @param nullable
	 *            是否可为空
	 * @return
	 * @throws OTTException
	 */
	protected String getStringFromRequestHeader(HttpServletRequest request,
			String key, boolean nullable) throws OTTException {
		if (request == null || StringUtil.isEmpty(key)) {
			return null;
		}
		String value = request.getHeader(key);
		if (StringUtil.isEmpty(value)) {
			if (!nullable) {
				throw new OTTException(ApiCode.NULL_PARAM, "公用请求参数" + key
						+ "不能为空");
			}
		}
		return value;
	}

	/**
	 * 成功，返回JSON
	 * 
	 * @param response
	 * @param jsonStr
	 * @return
	 */
	protected String success(HttpServletResponse response, String jsonStr) {
		response.setHeader(Global.ERROR_CODE, ApiCode.SUCCESS);
		return StringUtil.transformBase64(jsonStr);
	}

	/**
	 * 成功，返回JSON
	 * 
	 * @param response
	 * @param obj
	 * @return
	 */
	protected String success(HttpServletResponse response, Object obj) {
		try {
			response.setHeader(Global.ERROR_CODE, ApiCode.SUCCESS);
			JSON jsonObj = JSONSerializer.toJSON(obj);
			// LOGGER.debug(StringUtil.transformBase64(jsonObj.toString()));
			return StringUtil.transformBase64(jsonObj.toString());
		} catch (Exception e) {
			response.setHeader(Global.ERROR_CODE, ApiCode.ERROR);
			response.setHeader(Global.ERROR_MESSAGE,
					StringUtil.transformBase64(e.getMessage()));
			return JsonFormat.JSON_RESULT_EMPTY;
		}
	}

	/**
	 * 成功，返回JSON
	 * 
	 * @param response
	 * @param obj
	 * @return
	 */
	protected String success(HttpServletResponse response, Object obj,
			JsonConfig jsonConfig) {
		try {
			response.setHeader(Global.ERROR_CODE, ApiCode.SUCCESS);
			JSON jsonObj = JSONSerializer.toJSON(obj, jsonConfig);

			return StringUtil.transformBase64("{\"results\":"
					+ jsonObj.toString() + "}");
		} catch (Exception e) {
			response.setHeader(Global.ERROR_CODE, ApiCode.ERROR);
			response.setHeader(Global.ERROR_MESSAGE,
					StringUtil.transformBase64(e.getMessage()));
			return JsonFormat.JSON_RESULT_EMPTY;
		}
	}

	/**
	 * 出错，返回错误信息
	 * 
	 * @param response
	 * @param errorCode
	 * @param errorMsg
	 * @return
	 */
	protected String error(HttpServletResponse response, String errorCode,
			String errorMsg) {
		response.setHeader(Global.ERROR_CODE, errorCode);
		response.setHeader(Global.ERROR_MESSAGE,
				StringUtil.transformBase64(errorMsg));
		LOGGER.debug("错误信息:" + StringUtil.transformBase64(errorMsg));
		return JsonFormat.JSON_RESULT_EMPTY;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public abstract String execute() throws OTTException;
}
