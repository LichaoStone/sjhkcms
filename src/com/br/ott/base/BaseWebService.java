package com.br.ott.base;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.ApiCode;


/**
 * 对外接口控制器父类
 * 
 * @author lizhenghg 2016-11-4
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class BaseWebService {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
		this.session = this.request.getSession();
	}
	
	public void write2Json(HttpServletResponse response, Object obj){
		PrintWriter pt = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try{
			pt = response.getWriter();
			pt.print(obj);
		}catch(Exception e){
			
		}finally{
			if(pt != null)
				pt.close();
		}
	}
	
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
	
	public String getStringFromReq(HttpServletRequest request, String key) throws OTTException{
		String content = request.getParameter(key);
		if(null == content || "".equals(content))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + key + "缺失");
		return content;
	}
	
	public String success(String returnCode, String result){
		return "{\"returnCode\":\"" + returnCode + "\",\"result\":" + result + "}";
	}
	
	public String error(String returnCode, String returnMsg){
		return "{\"returnCode\":\"" + returnCode + "\",\"returnMsg\":\"" + returnMsg + "\"}";
	}
	
}
