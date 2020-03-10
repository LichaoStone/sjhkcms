package com.br.ott.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class APIUtils {

	public static Map<String, String> callMethod(String url, String params, String mac) {
		DataOutputStream output = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		String returnMsg = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			// 请求url
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("mac", mac);
			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");
			// 是否有参数请求
			if (params != null) {
				// 显式指定编码格式为
				byte[] paramsByte = params.getBytes("UTF-8");
				conn.setRequestProperty("Content-Length",
						String.valueOf(paramsByte.length));
				output = new DataOutputStream(conn.getOutputStream());
				output.write(paramsByte);
				output.flush();
			}
			// 提取结果
			int responseCode = conn.getResponseCode();
			String returnCode = conn.getHeaderField("ERRORCODE");
			returnMsg = conn.getHeaderField("ERRORMESSAGE");

			System.out.println(responseCode + "------returnCode---------"
					+ returnCode);
			System.out.println("returnMsg-----------" + returnMsg);

			String reString = streamToString(conn.getInputStream());
			System.out.println("res:---" + reString);
			map.put("returnCode", returnCode);
			map.put("returnMsg", returnMsg);
			map.put("reString", reString);

		} catch (Exception e) {
			e.printStackTrace();
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

	public static String streamToString(InputStream input) {

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input,
					"UTF-8"));
			String temp = br.readLine();
			while (null != temp && !"".equals(temp)) {
				sb.append(temp);
				temp = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("流转换string错误！");
			return "";
		}
		return sb.toString();
	}

}
