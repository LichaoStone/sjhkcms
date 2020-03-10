package com.br.ott.common.util.ip;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
	/**
	 * youdao获取地址
	 */
	private static final String API_URL = "http://www.youdao.com/smartresult-xml/search.s?type=ip&q=%s";

	private static final String CHARSET = "UTF-8";

	public static String doGet(String strURL) {
		return doGet(strURL, CHARSET);
	}

	public static String doPost(String strURL, Map<String, String> map) {
		return doPost(strURL, map, CHARSET);
	}

	public static String doPost(String strURL, Map<String, String> map,
			String encoding) {
		URL url;
		try {
			url = new URL(strURL);
			return doPost(url, map, encoding);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getAddressByYouDao(String ip) {
		String url = String.format(API_URL, ip);
		String text = HttpUtils.doGet(url, "GBK");
		int start = text.indexOf("<location>");
		int end = text.indexOf("</location>");
		if(start < 0 || end < 0){
			return "";
		}
		return text.substring(start + 10, end);
	}

	public static String doGet(URL url, String encoding) {
		InputStream in = null;
		InputStreamReader insr = null;
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		try {
			// 打开连接
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("accept-language", "zh_CN");
			conn.setRequestProperty("Charset", encoding);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.connect();

			// 读取数据
			in = conn.getInputStream();
			insr = new InputStreamReader(in, encoding);
			reader = new BufferedReader(insr);
			StringBuffer buff = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				buff.append(line);
				line = reader.readLine();
			}
			// 关闭连接
			conn.disconnect();
			return buff.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
			try {
				if (insr != null)
					insr.close();
			} catch (IOException e) {
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
			if (conn != null)
				conn.disconnect();
		}
	}

	public static String buildParams(Map<String, String> map) {
		if (map == null || map.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (i > 0)
				sb.append("&");
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			i++;
		}

		return sb.toString();
	}

	public static String doPost(URL url, Map<String, String> map,
			String encoding) {
		BufferedReader reader = null;
		DataOutputStream out = null;
		HttpURLConnection conn = null;
		try {

			// 打开连接
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", encoding);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.connect();

			// 发送数据
			if (map != null && !map.isEmpty()) {
				out = new DataOutputStream(conn.getOutputStream());
				String params = buildParams(map);
				out.write(params.getBytes());
				out.flush();
				out.close();
				out = null;
			}

			// 读取数据
			reader = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), encoding));
			StringBuffer buff = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				buff.append("\n" + line);
				line = reader.readLine();
			}
			// 关闭资源
			reader.close();
			reader = null;
			conn.disconnect();
			conn = null;
			return buff.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
			if (conn != null)
				conn.disconnect();
		}
	}

	public static final String doGet(String strURL, String encoding) {
		try {
			URL url = new URL(strURL);
			return doGet(url, encoding);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public static final String encode(String param) {
		return encode(param, CHARSET);
	}

	public static final String decode(String param) {
		return decode(param, CHARSET);
	}

	public static final String encode(String param, String encoding) {
		try {
			return URLEncoder.encode(param, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String decode(String param, String encoding) {
		try {
			return URLDecoder.decode(param, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
