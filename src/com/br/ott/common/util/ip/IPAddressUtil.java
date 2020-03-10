/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.common.util.ip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 根据IP 获取IP所属城市
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-10-18
 */
public class IPAddressUtil {
	
	/**
	 * 根据IP 获取IP所属城市
	 * @author zhuw
	 * @param args
	 */
	public static void main(String[] args) {
		String IP = "14.151.136.181";
		String strURL = "http://www.ip138.com/ips1388.asp?ip="+IP+"&action=2";
		String str = getURLContents(strURL);
		int start = str.indexOf("<li>");
		int end = str.indexOf("</li>");
		System.out.println(str.substring(start + 4, end));
	}

	public static String getURLContents(String strURL) {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(strURL);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			System.setProperty("sun.net.client.defaultConnectTimeout", "150000");
			System.setProperty("sun.net.client.defaultReadTimeout", "150000");
			httpConn.setRequestMethod("GET");// 设置请求为POST方法
			connection.setDoOutput(true);// 可以输出
			InputStreamReader isr = new InputStreamReader(
					httpConn.getInputStream(), "GBK");
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				temp = temp.trim();
				if (temp != null && temp.length() > 0) {
					sb.append(temp);
				}
			}
			br.close();
			isr.close();
		} catch (Exception e) {
			System.out.println("Error 1" + e.getMessage());
		}
		return sb.toString();
	}

}
