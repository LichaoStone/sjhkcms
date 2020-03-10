package com.br.ott.common.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/* 
 *  
 *  文件名：UploadApiUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：pananz
 *  创建时间：2012-11-15 - 下午5:24:36
 */
public class UploadApiUtil {
	private static final String CHARSET = "utf-8"; // 编码格式
	private static final int blockSize = 1024 * 1024;
	private static Logger log = Logger.getLogger(UploadApiUtil.class);

	public static boolean httpDownload(String httpUrl, String saveFile) {
		int nStartPos = 0;
		int nRead = 0;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			URL url = new URL(httpUrl);
			// 打开连接
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "Internet Explorer");
			// 获得文件长度
			long nEndPos = getFileSize(httpConnection);
			fos = new FileOutputStream(saveFile);
			String sProperty = "bytes=" + nStartPos + "-";
			// 告诉服务器book.rar这个文件从nStartPos字节开始传
			httpConnection.setRequestProperty("RANGE", sProperty);
			is = httpConnection.getInputStream();
			byte[] buffer = new byte[1024];
			// 读取网络文件,写入指定的文件中
			while ((nRead = is.read(buffer, 0, 1024)) > 0
					&& nStartPos < nEndPos) {
				fos.write(buffer, 0, nRead);
				nStartPos += nRead;
			}
			httpConnection.disconnect();
			return true;
		} catch (Exception e) {
			log.error("下载HTTP文件失败" + e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 取得文件大小
	 * 
	 * 创建人：pananz 创建时间：2016-9-14
	 * 
	 * @param @param sURL
	 * @param @return 返回类型：long
	 * @exception throws
	 */
	public static long getFileSize(HttpURLConnection httpConnection) {
		int nFileLength = -1;
		try {
			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				log.error("Error Code : " + responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			log.error("HTTP文件取得长度异常" + e.getMessage());
		}
		return nFileLength;
	}

	/**
	 * 调用公用上传测试接口方法 创建人：pananz 创建时间：2012-11-15 - 下午5:25:37
	 * 
	 * @param url
	 *            接口地址
	 * @param params
	 *            JSON格式参数{"MId":"1","bigimage":"","city":"106001"}
	 * @param headersstr
	 *            HEADER 参数("userId:123,userToken:235")
	 * @return 返回类型：HashMap<String,Object>
	 * @exception throws
	 */
	public static HashMap<String, Object> uploadFile(String url, Object object,
			Map<String, String> headers) {
		if (StringUtil.isEmpty(url) || object == null)
			return null;
		MultipartFile file = null;
		File f = null;
		if (object instanceof CommonsMultipartFile)
			file = (MultipartFile) object;
		else
			f = (File) object;
		DataOutputStream output = null;
		DataInputStream in = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 请求url
			urlAddress = new URL(url);

			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setChunkedStreamingMode(blockSize);

			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> entry : set) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Charset", CHARSET);
			String fileName = (file != null ? file.getOriginalFilename() : f.getName());
			log.debug("fileName is :" + fileName);
			conn.setRequestProperty("Content-Type", "multipart/form-data;file="
					+ fileName);
			in = new DataInputStream((file == null ? new FileInputStream(f) : file.getInputStream()));
			output = new DataOutputStream(conn.getOutputStream());
			byte[] bytes = new byte[blockSize];
			int len = 0;
			while ((len = in.read(bytes, 0, blockSize)) > 0) {
				output.write(bytes, 0, len);
			}
			output.flush();
			// 提取结果
			// int responseCode = conn.getResponseCode();
			String returnCode = conn.getHeaderField("ERRORCODE");
			String returnMsg = conn.getHeaderField("ERRORMESSAGE");
			String fileUrl = conn.getHeaderField("FILEURL");

			map.put("returnCode", returnCode);
			map.put("returnMsg", StringUtil.decodeformBase64(returnMsg));
			map.put("fileUrl", StringUtil.decodeformBase64(fileUrl));
		} catch (MalformedURLException e) {
			// URL地址错误
			log.error("上传文件的URL地址错误");
			map.put("returnMsg", "UploadApiUtil URL地址错误");
		} catch (SocketTimeoutException e) {
			// 请求超时
			log.error("上传文件请求超时");
			map.put("returnMsg", "UploadApiUtil 请求超时");
		} catch (IOException e) {
			// 网络连接失败
			log.error("上传文件的网络连接失败");
			map.put("returnMsg", "UploadApiUtil 网络连接失败");
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			log.error("上传文件的公用请求参数异常");
			map.put("returnMsg", "UploadApiUtil 公用请求参数异常！");
		} finally {
			if (conn != null)
				conn.disconnect();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			urlAddress = null;
		}
		return map;
	}

	/**
	 * 上传文件
	 * 
	 * 创建人：pananz 创建时间：2016-9-13
	 * 
	 * @param @param url
	 * @param @param file
	 * @param @param headers
	 * @param @return 返回类型：HashMap<String,Object>
	 * @exception throws
	 */
	public static HashMap<String, Object> uploadFile2(String url, File file,
			Map<String, String> headers) {
		if (StringUtil.isEmpty(url))
			return null;
		DataOutputStream output = null;
		FileInputStream fis = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 请求url
			urlAddress = new URL(url);

			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setChunkedStreamingMode(blockSize);

			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> entry : set) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Charset", CHARSET);
			String fileName = file.getName();
			log.debug("fileName is :" + fileName);
			conn.setRequestProperty("Content-Type", "multipart/form-data;file="
					+ fileName);
			fis = new FileInputStream(file);
			output = new DataOutputStream(conn.getOutputStream());
			byte[] bytes = new byte[blockSize];
			int len = 0;
			while ((len = fis.read(bytes, 0, blockSize)) > 0) {
				output.write(bytes, 0, len);
			}
			output.flush();
			// 提取结果
			// int responseCode = conn.getResponseCode();
			String returnCode = conn.getHeaderField("ERRORCODE");
			String returnMsg = conn.getHeaderField("ERRORMESSAGE");
			String fileUrl = conn.getHeaderField("FILEURL");

			map.put("returnCode", returnCode);
			map.put("returnMsg", StringUtil.decodeformBase64(returnMsg));
			map.put("fileUrl", StringUtil.decodeformBase64(fileUrl));
		} catch (MalformedURLException e) {
			// URL地址错误
			log.error("上传文件的URL地址错误");
			map.put("returnMsg", "UploadApiUtil URL地址错误");
		} catch (SocketTimeoutException e) {
			// 请求超时
			log.error("上传文件请求超时");
			map.put("returnMsg", "UploadApiUtil 请求超时");
		} catch (IOException e) {
			// 网络连接失败
			log.error("上传文件的网络连接失败");
			map.put("returnMsg", "UploadApiUtil 网络连接失败");
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			log.error("上传文件的公用请求参数异常");
			map.put("returnMsg", "UploadApiUtil 公用请求参数异常！");
		} finally {
			if (conn != null)
				conn.disconnect();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			urlAddress = null;
		}
		return map;
	}

	public static HashMap<String, Object> uploadSoft(String url,
			MultipartFile file, Map<String, String> headers) {
		if (StringUtil.isEmpty(url))
			return null;
		DataOutputStream output = null;
		DataInputStream in = null;
		URL urlAddress = null;
		HttpURLConnection conn = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 请求url
			urlAddress = new URL(url);
			// 打开链接
			conn = (HttpURLConnection) urlAddress.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setChunkedStreamingMode(blockSize);

			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> entry : set) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setRequestProperty("User-Agent", "IPTV");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type", "multipart/form-data;file="
					+ file.getOriginalFilename());
			in = new DataInputStream(file.getInputStream());
			output = new DataOutputStream(conn.getOutputStream());
			byte[] bytes = new byte[blockSize];
			int len = 0;
			while ((len = in.read(bytes, 0, blockSize)) > 0) {
				output.write(bytes, 0, len);
			}
			output.flush();
			// 提取结果
			int responseCode = conn.getResponseCode();
			String returnCode = conn.getHeaderField("ERRORCODE");
			String returnMsg = conn.getHeaderField("ERRORMESSAGE");
			String fileUrl = conn.getHeaderField("FILEURL");
			String packageName = conn.getHeaderField("PACKAGENAME");
			String sourceType = conn.getHeaderField("SOURCETYPE");

			map.put("returnCode", returnCode);
			map.put("returnMsg", StringUtil.decodeformBase64(returnMsg));
			map.put("fileUrl", StringUtil.decodeformBase64(fileUrl));
			map.put("packageName", StringUtil.decodeformBase64(packageName));
			map.put("sourceType", StringUtil.decodeformBase64(sourceType));
			log.info(returnCode + ":" + StringUtil.decodeformBase64(returnMsg)
					+ ":" + responseCode + ":"
					+ StringUtil.decodeformBase64(fileUrl) + "packagenName==="
					+ StringUtil.decodeformBase64(packageName) + "sourceType"
					+ StringUtil.decodeformBase64(sourceType));
		} catch (MalformedURLException e) {
			// URL地址错误
			log.error("上传文件的URL地址错误");
			map.put("returnMsg", "UploadApiUtil URL地址错误");
		} catch (SocketTimeoutException e) {
			// 请求超时
			log.error("上传文件请求超时");
			map.put("returnMsg", "UploadApiUtil 请求超时");
		} catch (IOException e) {
			// 网络连接失败
			log.error("上传文件的网络连接失败");
			map.put("returnMsg", "UploadApiUtil 网络连接失败");
		} catch (ArrayIndexOutOfBoundsException e) {
			// 公用请求参数异常！
			log.error("上传文件的公用请求参数异常");
			map.put("returnMsg", "UploadApiUtil 公用请求参数异常！");
		} finally {
			if (conn != null)
				conn.disconnect();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			urlAddress = null;
		}
		return map;
	}
}
