package com.br.ott.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.br.ott.Global;
import com.br.ott.client.JsonFormat;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.ApiCode;
import com.br.ott.common.util.StringUtil;

/**
 * 对外接口控制器父类
 * 
 * @author 陈登民
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class BaseApiController {
	private static final Logger LOGGER = Logger
			.getLogger(BaseApiController.class);

	/**
	 * 日志操作类型：删除
	 */
	protected final static String OPERA_TYPE_DELETE = "3";
	/**
	 * 日志操作类型：修改
	 */
	protected final static String OPERA_TYPE_MODIFY = "2";
	/**
	 * 日志操作类型：新增
	 */
	protected final static String OPERA_TYPE_ADD = "1";
	/**
	 * 日志操作类型：批量导入
	 */
	protected final static String OPERA_TYPE_BATCH_ADD = "4";

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

	protected JSONObject getJSONObj2(HttpServletRequest request)
			throws OTTException {
		try {
			Map<String, String[]> params = request.getParameterMap();
			Map<String, String> map = new HashMap<String, String>();
			if (params != null && params.size() > 0) {
				Set<Entry<String, String[]>> set = params.entrySet();
				Iterator<Entry<String, String[]>> it = set.iterator();
				while (it.hasNext()) {
					Entry<String, String[]> entry = it.next();
					String key = entry.getKey();
					String[] val = entry.getValue();
					LOGGER.debug("key==" + key + "val==" + val[0]);
					map.put(key, val[0]);
				}
			}else{
				
			}
			return JSONObject.fromObject(map);
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
	 * 成功返回JSON数据
	 * 
	 * 创建人：pananz 创建时间：2016-8-2 - 上午10:17:17
	 * 
	 * @param response
	 * @param jsonStr
	 * @return 返回类型：String
	 * @exception throws
	 */
	protected String liveSuccess(HttpServletResponse response, String jsonStr) {
		response.setHeader(Global.ERROR_CODE, ApiCode.SUCCESS);
		return jsonStr;
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

	protected void writeJson(HttpServletResponse response, Object obj) {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
	 * 对request对象进行遍历，把获取到的所有参数封装到Map<String, Object>中
	 * 创建人：lizhenghg 创建时间：2016-11-4 
	 * @param request
	 *
	 */
	public Map<String, Object> commonDealBefore(HttpServletRequest request){
		if(null == request)
			throw new IllegalArgumentException("Request must not be null"); 
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap();
		while(paramNames != null && paramNames.hasMoreElements()){
			String paramName = (String)paramNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			if(values != null && values.length > 0){
				if(values.length > 1)
					params.put(paramName, values);
				else
					params.put(paramName, values[0]);
			}	
		}
		return params;
	}
	
	/**
	 * 对request对象进行遍历，把获取到的所有参数封装到JSONObject中
	 * 创建人：lizhenghg 创建时间：2016-11-4 
	 * @param request
	 *
	 */
	public JSONObject commonDealBefore3(HttpServletRequest request){
		if(null == request)
			throw new IllegalArgumentException("Request must not be null"); 
		Map<String, String[]> params = request.getParameterMap();
		JSONObject result = new JSONObject();
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String[]> entry : params.entrySet()) {
				String key = entry.getKey();
				String[] values = entry.getValue();
				if (values.length > 1) {
					result.put(key, values);
				} else {
					result.put(key, values[0]);
				}
			}
		}
		return result;
	}
	
	/**
	 * 流转换成JSONObject
	 * 创建人：lizhenghg 创建时间：2016-11-4
	 * @param input
	 *            [输入流]
	 * @return [参数说明]
	 * @return String [字符串]
	 */
	public static JSONObject stream2JSON(InputStream input) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader br = null;
		JSONObject result = null;
		try {
			isr = new InputStreamReader(input, "UTF-8");
			br = new BufferedReader(isr);
			String temp = br.readLine();
			while (null != temp && !"".equals(temp)) {
				sb.append(temp);
				temp = br.readLine();
			}
			if (sb.length() > 0) {
				result = JSONObject.fromObject(sb.toString());
				return result;
			} else {
				return new JSONObject();
			}
		} catch (IOException e) {
			LOGGER.error("流转换string错误！", e);
			return new JSONObject();
		} catch (Exception ex) {
			LOGGER.error("String转换JSONObject错误！", ex);
			return new JSONObject();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/**
	 * 对request对象进行遍历，把获取到的所有参数封装到JSONObject中,只处理一个参数且存放在body情况(参数的value作为表单的name来处理)
	 * 创建人：lizhenghg 创建时间：2017-7-5 
	 * @param request
	 *
	 */
	public static JSONObject commonDealBefore2(HttpServletRequest request){
		if(null == request)
			throw new IllegalArgumentException("Request must not be null"); 
		Enumeration<String> paramNames = request.getParameterNames();
		JSONObject jsonObject = null;
		String paramName = null;
		while(paramNames != null && paramNames.hasMoreElements()){
			paramName = (String)paramNames.nextElement();
			jsonObject = JSONObject.fromObject(paramName);
			break;
		}
		if (jsonObject == null) return new JSONObject();
		return jsonObject;
	}

	/**
	 * 判断传入参数是否缺失
	 * 创建人：lizhenghg 创建时间：2016-11-4 
	 * @param Map<String, Object> map, String str
	 *
	 */
	public Object getStringFromMap(Map<String, Object> map, String str) throws OTTException{
		if(CollectionUtils.isEmpty(map))
			throw new OTTException(ApiCode.NULL_PARAM, "全部参数缺失");
		if(StringUtils.isBlank(str))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + str + "缺失");
		String paramName = null;
		Object value = null;
		for(Map.Entry<String, Object> entry : map.entrySet()){
			paramName = entry.getKey();
			if(str.equals(paramName)){
				value = entry.getValue();
				break;
			}
		}
		if(null == value)
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + str + "缺失");
		return value;
	}
	
	/**
	 * 判断传入参数是否缺失
	 * 创建人：lizhenghg 创建时间：2016-11-4 
	 * @param HttpServletRequest request, String key
	 *
	 */
	public String getStringFromReq(HttpServletRequest request, String key) throws OTTException{
		String content = request.getParameter(key);
		if(null == content || "".equals(content))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + key + "缺失");
		return content;
	}
	
	/**
	 * 返回正确格式的json(针对接口)
	 * 创建人：lizhenghg 创建时间：2016-11-29
	 * @param String returnCode, String result
	 */
	public String success(String returnCode, String result){
		return "{\"returnCode\":\"" + returnCode + "\",\"result\":" + result + "}";
	}
	
	/**
	 * 返回错误格式的json(针对接口)
	 * 创建人：lizhenghg 创建时间：2016-11-29
	 * @param String returnCode, String returnMsg
	 */
	public String error(String returnCode, String returnMsg){
		return "{\"returnCode\":\"" + returnCode + "\",\"returnMsg\":\"" + returnMsg + "\"}";
	}
}
