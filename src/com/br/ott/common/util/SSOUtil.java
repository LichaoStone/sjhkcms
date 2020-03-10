package com.br.ott.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* 
 *  
 *  文件名：SSOUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：pananz
 *  创建时间：2013-4-2 - 上午11:01:22
 */
public final class SSOUtil {
	private static final Logger log = Logger.getLogger(SSOUtil.class);

	private static final int TIMEOUT = 10000;
	private static final String CHARSET = "utf-8"; // 编码格式
	private static final int READ_TIMEOUT = 60000; // 接收超时时间

	/**
	 * 上传用户行为数据
	 * 
	 * 创建人：pananz 创建时间：2016-8-7 - 上午7:07:32
	 * 
	 * @param url
	 * @param params
	 * @param headersstr
	 * @return 返回类型：String
	 * @exception throws
	 */
	public static String uploadBehavior(String url, String params,
			String headersstr) {
		if (StringUtil.isEmpty(url))
			return null;
		DataOutputStream output = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		try {
			// 请求url
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(3000);
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
			if (StringUtil.isNotEmpty(params)) {
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
			log.debug("返回结果：" + responseCode);
			return String.valueOf(responseCode);
		} catch (MalformedURLException e) {
			// URL地址错误
			return "APITest URL地址错误";
		} catch (SocketTimeoutException e) {
			// 请求超时
			return "请求超时";
		} catch (IOException e) {
			e.printStackTrace();
			// 网络连接失败
			return "网络连接失败";
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			return "公用请求参数异常！";
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
	}

	public static String postRequestByUTF8(String url, String params) {
		DataOutputStream output = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		StringBuffer sb = new StringBuffer();
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
			int responseCode = conn.getResponseCode();
			log.debug("responseCode is:" + responseCode);
			if (conn.getResponseCode() != 200) {
				return "";
			}
			is = conn.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {

				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			if (conn != null)
				conn.disconnect();
			urlAddress = null;
		}
		return sb.toString();
	}

	/**
	 * 取得接口状态及信息<一句话功能简述>*
	 */
	public static Map<String, Object> execute(String url, String method) {
		URL urlAddress = null;
		HttpURLConnection conn = null;
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream is = null;
		try {
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", "SH_IPTV_BoRuiCube");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type", "text/xml; charset="
					+ CHARSET);
			log.info("结果:" + conn.getResponseCode());
			if (conn.getResponseCode() != 200) {
				return map;
			}
			is = conn.getInputStream();
			DataOutputStream output = new DataOutputStream(
					conn.getOutputStream());
			byte[] bytes = new byte[1024 * 1024];
			int len = 0;
			while ((len = is.read(bytes, 0, 1024 * 1024)) > 0) {
				output.write(bytes, 0, len);
			}
			output.flush();
			output.close();
			// 提取结果
			map.put("returnMsg", conn.getResponseCode());
			log.info("结果:" + conn.getResponseCode());
		} catch (MalformedURLException e) {
			log.info("URL地址错误");
			map.put("returnMsg", " URL地址错误");
			e.printStackTrace();
		} catch (ProtocolException e) {
			log.info("请求超时");
			map.put("returnMsg", "请求超时");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("网络连接失败");
			map.put("returnMsg", "网络连接失败");
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			if (conn != null)
				conn.disconnect();
			urlAddress = null;
		}
		return map;
	}

	public static String getHTMLByGBK(String url) {
		URL urlAddress = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		StringBuffer sb = new StringBuffer();
		try {
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "TVSOU_IPTV");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "GBK");
			conn.setRequestProperty("Content-Type", "text/xml; charset=GBK");
			log.info("结果:" + conn.getResponseCode());
			if (conn.getResponseCode() != 200) {
				return "";
			}
			is = conn.getInputStream();
			isr = new InputStreamReader(is, "GBK");
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			// 提取结果
		} catch (MalformedURLException e) {
			log.error("URL地址错误");
		} catch (ProtocolException e) {
			log.error("请求超时");
		} catch (IOException e) {
			log.error("网络连接失败");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {

				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			if (conn != null)
				conn.disconnect();
			urlAddress = null;
		}
		return sb.toString();

	}

	public static String getResultByUTF8(String url) {
		URL urlAddress = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		StringBuffer sb = new StringBuffer();
		try {
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "TVMAO_IPTV");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			log.info("结果:" + conn.getResponseCode());
			if (conn.getResponseCode() != 200) {
				return "";
			}
			is = conn.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			// 提取结果
			log.info("结果:" + conn.getResponseCode());
		} catch (MalformedURLException e) {
			log.error("URL地址错误");
		} catch (ProtocolException e) {
			log.error("请求超时");
		} catch (IOException e) {
			log.error("网络连接失败");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {

				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			if (conn != null)
				conn.disconnect();
			urlAddress = null;
		}
		return sb.toString();
	}

	/**
	 * 获取表单input内容
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Map<String, String> getFormDataByHmtl(String html)
			throws ParseException, IOException {
		Document document = Jsoup.parse(html);
		Elements form = document.getElementsByTag("form");// 找出所有form表单
		Element element = form.select("[method=post]").first();// 筛选出提交方法为post的表单
		Elements elements = element.select("input[name]");// 把表单中带有name属性的input标签取出
		Map<String, String> parmas = new HashMap<String, String>();
		for (Element temp : elements) {
			log.info(temp.attr("name") + ":" + temp.attr("value"));
			parmas.put(temp.attr("name"), temp.attr("value"));// 把所有取出的input，取出其name，放入Map中
		}
		return parmas;
	}

	/**
	 * 订购成功后，通过解析HTML内容，取得提示页面中的回调URL<一句话功能简述>* onClick=
	 * "location.href='http://222.66.209.188:8088/SH_iptv_Server/coin_returnConfirm.do?gameid=P10056&Result=0&Description=%B4%FA%BC%C6%B7%D1%B3%C9%B9%A6%2C%C7%EB%B5%E3%BB%F7%B7%B5%BB%D8%21&UserID=50006537@iptv2&UserToken=50006537@iptv2***237720765d80c2a&PayType=1&ServiceID=&ProductID=1000704427&PurchaseType=1&Fee=300&SPID=02100005&TransactionID=10002013041114160600000026926974&ExpiredTime=null'"
	 */
	public static String getReturnURL(String html, String url) {
		Document doc = Jsoup.parse(html);
		Element link = doc.select("a").first();
		String linkHref = link.attr("onClick");
		int beginIndex = linkHref.indexOf(url);
		log.info(linkHref + ":" + beginIndex);
		if (beginIndex < 0) {
			return "";
		}
		return linkHref.substring(beginIndex);
	}

	/**
	 * 通过URl取得表单数据<一句话功能简述>*
	 */
	public static Map<String, String> getFormDataByUrl(String url) {
		Document document = null;
		try {
			document = Jsoup.connect(url).timeout(READ_TIMEOUT).get();
		} catch (IOException e) {
			log.info("取得内容失败");
			e.printStackTrace();
		}

		if (document == null) {
			log.info("无内容返回");
			return null;
		}
		Elements form = document.getElementsByTag("form");// 找出所有form表单
		if (form == null) {
			log.info("无form表单返回");
			return null;
		}
		Element element = form.first();// 筛选出提交方法为第一个的表单
		if (element == null) {
			log.info("无第一个form[method=post]或form[method=get]表单返回");
			return null;
		}
		Elements elements = element.select("input[name]");// 把表单中带有name属性的input标签取出
		if (elements.isEmpty()) {
			log.info("无input[name]表单属性返回");
			return null;
		}
		Map<String, String> parmas = new HashMap<String, String>();
		for (Element temp : elements) {
			log.info(temp.attr("name") + ":" + temp.attr("value"));
			parmas.put(temp.attr("name"), temp.attr("value"));// 把所有取出的input，取出其name，放入Map中
		}
		return parmas;
	}

	/**
	 * 通过URL提交表单<一句话功能简述>*
	 */
	public static Document getDocResponse(String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(READ_TIMEOUT).get();
			return doc;
		} catch (IOException e) {
			log.info("提交表单数据返回异常：" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static String getResponseStr(String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(READ_TIMEOUT).get();
			if (doc != null) {
				return doc.text();
			}
		} catch (IOException e) {
			log.info("提交表单数据返回异常：" + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public static String submitFormResponse(String url) {
		try {

			Response response = Jsoup.connect(url).timeout(READ_TIMEOUT)
					.execute();
			if (response != null)
				return response.body();
		} catch (IOException e) {
			log.info("提交表单数据返回异常：" + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public static String submitFormResponse(InputStream is, String url) {
		try {
			return Jsoup.parse(is, "utf-8", url).toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 通过URL及MAP提交表单<一句话功能简述>*
	 */
	public static void submitForm(String url, Map<String, String> map) {
		if (map.isEmpty()) {
			return;
		}
		try {
			Jsoup.connect(url).timeout(READ_TIMEOUT).data(map).execute();
		} catch (IOException e) {
			log.info("提交表单数据返回异常：" + e.getMessage());
		}
	}

	/**
	 * 按MAP方式提交表单，并返回HTML内容<一句话功能简述>*
	 */
	public static String submitFormResponse(String url, Map<String, String> map) {
		try {
			Response response = Jsoup.connect(url).timeout(READ_TIMEOUT)
					.data(map).execute();
			if (response != null)
				// log.info("提交表单的URL" + response.url().getHost() + ":" +
				// response.url().getPort()
				// + response.url().getPath());
				return response.body();
		} catch (IOException e) {
			log.info("提交表单数据返回异常：" + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 按post方式提交表单内容，并取得第一个页面FORM表单的数据<一句话功能简述>*
	 */
	public static String postDataByMap(String url, Map<String, String> map) {
		if (map.isEmpty()) {
			return "";
		}
		Document document = null;
		try {
			document = Jsoup.connect(url).timeout(READ_TIMEOUT).data(map)
					.post();
		} catch (IOException e) {
			log.info("取得内容失败");
			e.printStackTrace();
		}

		if (document == null) {
			log.info("无内容返回");
			return "";
		}
		return document.text();
	}

	/**
	 * 按get方式提交表单内容，并取得第一个页面FORM表单的数据<一句话功能简述>*
	 */
	public static Map<String, String> getFormDataByMap(String url,
			Map<String, String> map) {
		if (map.isEmpty()) {
			return null;
		}
		Document document = null;
		try {
			document = Jsoup.connect(url).timeout(READ_TIMEOUT).data(map).get();
		} catch (IOException e) {
			log.info("取得内容失败:" + e.getMessage());
		}

		if (document == null) {
			log.info("无内容返回");
			return null;
		}
		Elements form = document.getElementsByTag("form");// 找出所有form表单
		if (form == null) {
			log.info("无form表单返回");
			return null;
		}
		Element element = form.first();// 筛选出提交方法为第一个的表单
		if (element == null) {
			log.info("无第一个form[method=post]或form[method=get]表单返回");
			return null;
		}
		log.info("form action is: " + element.attr("action"));
		Elements elements = element.select("input[name]");// 把表单中带有name属性的input标签取出
		if (elements.isEmpty()) {
			log.info("无input[name]表单属性返回");
			return null;
		}
		Map<String, String> parmas = new HashMap<String, String>();
		for (Element temp : elements) {
			log.info(temp.attr("name") + ":" + temp.attr("value"));
			parmas.put(temp.attr("name"), temp.attr("value"));// 把所有取出的input，取出其name，放入Map中
		}
		return parmas;
	}

	/**
	 * 获取表单内容并设置成URL参数方式
	 */
	public static String getUrlData(String url) {
		StringBuffer sb = new StringBuffer();
		try {
			Document document = Jsoup.connect(url).timeout(READ_TIMEOUT).post();
			if (document == null) {
				log.info("无内容返回");
				return "";
			}
			Elements form = document.getElementsByTag("form");// 找出所有form表单
			if (form == null) {
				log.info("无form表单返回");
				return "";
			}
			Element element = form.select("[method=post]").first();// 筛选出提交方法为post的表单
			if (element == null) {
				log.info("无form[method=post]表单返回");
				return "";
			}
			Elements elements = element.select("input[name]");
			if (elements.isEmpty()) {
				log.info("无input[name]表单属性返回");
				return "";
			}
			int i = 1;
			for (Element temp : elements) {
				if (i == 1) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				String name = temp.attr("name");
				log.info(name + ":" + temp.attr("value"));
				if ("newfocus".equals(name))
					continue;
				sb.append(name).append("=").append(temp.attr("value"));
				i++;
			}
		} catch (IOException e) {
			log.info("获得表单内容失败");
			e.printStackTrace();
		}
		return sb.toString();
	}

}
