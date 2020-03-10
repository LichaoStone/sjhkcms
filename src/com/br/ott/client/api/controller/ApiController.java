package com.br.ott.client.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.Global;
import com.br.ott.base.BaseApiController;
import com.br.ott.client.JsonFormat;
import com.br.ott.client.api.controller.manager.BaseManager;
import com.br.ott.client.api.controller.manager.TestManager;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.util.StringUtil;

@RequestMapping("/api")
@Controller
public class ApiController extends BaseApiController {
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> mapping = new HashMap<String, Class>();
	static {
		mapping.put("test", TestManager.class);
	}

	@RequestMapping(value = "/v1/{apis}")
	public @ResponseBody
	String v1(@PathVariable String apis, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtil.isEmpty(apis) || "null".equals(apis)) {
			response.setHeader(Global.ERROR_CODE, JsonFormat.JSON_RESULT_EMPTY);
			return JsonFormat.JSON_RESULT_EMPTY;
		}

		if (apis.contains(",")) {// 多个接口
			String[] names = apis.split(",");
			return execute(names, request, response);
		} else {// 单个接口
			return execute(new String[] { apis }, request, response);
		}
	}

	private String execute(String[] apis, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> mapData = new HashMap<String, String>();
		Map<String, String> header = new HashMap<String, String>();
		for (String api : apis) {
			if (!mapping.containsKey(api)) {
				header.put(api, "api is not exists");
				continue;
			}
			BaseManager iManager = null;
			try {
				iManager = (BaseManager) Class.forName(mapping.get(api).getName()).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				header.put(api,  "api instance error");
				continue;
			}
			iManager.setRequest(request);
			iManager.setResponse(response);

			String res = null;
			try {
				res = iManager.execute();
				mapData.put(api, res);
			} catch (OTTException ott) {
				header.put(api, ott.getMessage());
				continue;
			}
			header.put(api, "SUCCESS");
		}
		mapData.put("time", String.valueOf(System.currentTimeMillis()));

		return success(response, mapData, header);
	}

	/**
	 * 成功，返回JSON
	 */
	protected String success(HttpServletResponse response, Object obj,
			Map<String, String> header) {
		try {
			JSON jsonHeaderCodeMsg = JSONSerializer.toJSON(header);
			response.setHeader("result", jsonHeaderCodeMsg.toString());
			JSON jsonObj = JSONSerializer.toJSON(obj);
			return StringUtil.transformBase64(jsonObj.toString());
		} catch (Exception e) {
			response.setHeader("result", JsonFormat.JSON_RESULT_EMPTY);
			return JsonFormat.JSON_RESULT_EMPTY;
		}
	}
}