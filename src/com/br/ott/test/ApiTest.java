/*
 * @# ApiTest.java Sep 3, 2012 5:39:53 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.br.ott.client.JsonFormat;
import com.br.ott.common.util.JsonUtils;
import com.br.ott.common.util.StringUtil;

/*
 * @author pananz
 * TODO
 */
public class ApiTest {
	private static final int TIMEOUT = 10000;
	private static final String CHARSET = "utf-8"; // 编码格式
	private static final int READ_TIMEOUT = 30000; // 接收超时时间

	public static HashMap<String, Object> callMethod(String url, String params,
			String headersstr) {
		if (StringUtil.isEmpty(url))
			return null;
		DataOutputStream output = null;
		String result = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			// 请求url
			urlAddress = new URL(url);

			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			if (StringUtil.isNotEmpty(headersstr)) {
				String[] headers = headersstr.split(",");
				if (headers.length >= 1) {
					for (int i = 0; i < headers.length; i++) {
						conn.setRequestProperty(headers[i].split(":")[0],
								headers[i].split(":")[1]);
					}
				}
			}

			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type",
					"application/json; charset=" + CHARSET);
			// 是否有参数请求
			if (params != null) {
				// 显式指定编码格式为
				byte[] paramsByte = params.getBytes(CHARSET);
				conn.setRequestProperty("Content-Length",
						String.valueOf(paramsByte.length));
				output = new DataOutputStream(conn.getOutputStream());
				output.write(paramsByte);
				output.flush();
			}
			// 提取结果
			int responseCode = conn.getResponseCode();
			String returnCode = conn.getHeaderField("ERRORCODE");
			String returnMsg = conn.getHeaderField("ERRORMESSAGE");

			map.put("returnCode", returnCode);
			map.put("returnMsg", returnMsg);
			result = StringUtil.streamToString(conn.getInputStream());
			System.out.println(returnMsg + ":" + responseCode);
			if (!StringUtil.isEmpty(result)
					&& !JsonFormat.JSON_RESULT_EMPTY.equals(result)) {
				map.put("jsonObject", result);
			}

		} catch (MalformedURLException e) {
			// URL地址错误
			map.put("returnMsg", "APITest URL地址错误");
		} catch (SocketTimeoutException e) {
			// 请求超时
			map.put("returnMsg", "APITest 请求超时");
		} catch (IOException e) {
			e.printStackTrace();
			// 网络连接失败
			map.put("returnMsg", "APITest 网络连接失败");
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			map.put("returnMsg", "APITest 公用请求参数异常！");
		} finally {
			if (conn != null)
				conn.disconnect();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			urlAddress = null;
		}
		return map;
	}

	/**
	 * 非base64加密,传参通过URL方式
	 * 
	 * 创建人：pananz 创建时间：2017-1-6
	 * 
	 * @param @param url
	 * @param @param params
	 * @param @param headersstr
	 * @param @return 返回类型：HashMap<String,Object>
	 * @exception throws
	 */
	public static HashMap<String, Object> callMethod2(String url,
			String params, String headersstr) {
		if (StringUtil.isEmpty(url))
			return null;
		String result = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer urlSB = new StringBuffer();
		urlSB.append(url);
		if (StringUtil.isNotEmpty(params)) {
			@SuppressWarnings("unchecked")
			Map<String, String> jsonMap = JsonUtils.getMapFromJson(params);
			Set<Entry<String, String>> set = jsonMap.entrySet();
			for (Iterator<Entry<String, String>> iter = set.iterator(); iter
					.hasNext();) {
				Entry<String, String> entry = iter.next();
				if (urlSB.toString().indexOf("?") > 0) {
					urlSB.append("&" + entry.getKey() + "=" + entry.getValue());
				} else {
					urlSB.append("?" + entry.getKey() + "=" + entry.getValue());
				}
			}
		}
		try {
			// 请求url
			System.out.println("请求地址" + urlSB.toString());
			urlAddress = new URL(urlSB.toString());
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			if (StringUtil.isNotEmpty(headersstr)) {
				String[] headers = headersstr.split(",");
				if (headers.length >= 1) {
					for (int i = 0; i < headers.length; i++) {
						conn.setRequestProperty(headers[i].split(":")[0],
								headers[i].split(":")[1]);
					}
				}
			}
			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type",
					"application/json; charset=" + CHARSET);
			// 提取结果
			int responseCode = conn.getResponseCode();
			result = StringUtil.streamToString(conn.getInputStream());
			System.out.println(result + ":" + responseCode);
			if (!StringUtil.isEmpty(result)
					&& !JsonFormat.JSON_RESULT_EMPTY.equals(result)) {
				map.put("jsonObject", result);
			}

		} catch (MalformedURLException e) {
			// URL地址错误
			map.put("returnMsg", "APITest URL地址错误");
		} catch (SocketTimeoutException e) {
			// 请求超时
			map.put("returnMsg", "APITest 请求超时");
		} catch (IOException e) {
			e.printStackTrace();
			// 网络连接失败
			map.put("returnMsg", "APITest 网络连接失败");
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			map.put("returnMsg", "APITest 公用请求参数异常！");
		} finally {
			if (conn != null)
				conn.disconnect();
			urlAddress = null;
		}
		return map;
	}

	public static HashMap<String, Object> callMethod3(String url,
			String params, String headersstr) {
		if (StringUtil.isEmpty(url))
			return null;
		DataOutputStream output = null;
		String result = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			// 请求url
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			if (StringUtil.isNotEmpty(headersstr)) {
				String[] headers = headersstr.split(",");
				if (headers.length >= 1) {
					for (int i = 0; i < headers.length; i++) {
						conn.setRequestProperty(headers[i].split(":")[0],
								headers[i].split(":")[1]);
					}
				}
			}
			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			if (StringUtil.isNotEmpty(params)) {
				// 显式指定编码格式为
				@SuppressWarnings("unchecked")
				Map<String, String> jsonMap = JsonUtils.getMapFromJson(params);
				Set<Entry<String, String>> set = jsonMap.entrySet();
				StringBuffer content = new StringBuffer();
				int index = 1;
				for (Iterator<Entry<String, String>> iter = set.iterator(); iter
						.hasNext();) {
					Entry<String, String> entry = iter.next();
					if (index > 1) {
						content.append("&" + entry.getKey() + "="
								+ convert(entry.getValue()));
					} else {
						content.append(entry.getKey() + "="
								+ convert(entry.getValue()));
					}
					index++;
				}
				byte[] paramsByte = content.toString().getBytes(CHARSET);
				conn.setRequestProperty("Content-Length",
						String.valueOf(paramsByte.length));
				output = new DataOutputStream(conn.getOutputStream());
				output.write(paramsByte);
				output.flush();
			}
			// 提取结果
			String returnCode = conn.getHeaderField("ERRORCODE");
			String returnMsg = conn.getHeaderField("ERRORMESSAGE");

			map.put("returnCode", returnCode);
			map.put("returnMsg", returnMsg);
			result = StringUtil.streamToString(conn.getInputStream());
			if (!StringUtil.isEmpty(result)
					&& !JsonFormat.JSON_RESULT_EMPTY.equals(result)) {
				map.put("jsonObject", result);
			}

		} catch (MalformedURLException e) {
			// URL地址错误
			map.put("returnMsg", "APITest URL地址错误");
		} catch (SocketTimeoutException e) {
			// 请求超时
			map.put("returnMsg", "APITest 请求超时");
		} catch (IOException e) {
			e.printStackTrace();
			// 网络连接失败
			map.put("returnMsg", "APITest 网络连接失败");
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			map.put("returnMsg", "APITest 公用请求参数异常！");
		} finally {
			if (conn != null)
				conn.disconnect();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			urlAddress = null;
		}
		return map;
	}

	private static String convert(String target) {
		try {
			byte bytes[] = target.getBytes("iso8859-1");
			String result = new String(bytes, CHARSET);
			return result;
		} catch (UnsupportedEncodingException e) {
			return target;
		}

	}
}
