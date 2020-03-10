package com.br.ott.test;

import java.util.HashMap;

import net.sf.json.JSONObject;

/**
 * 文件名：TestNativeOutOfMemoryError.java 版权：BoRuiCubeNet. Copyright 2014-2015,All
 * rights reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-4-6
 */
public class TestNativeOutOfMemoryError {
	public static void main(String[] args) {
		for (int i = 0;i<1000; i++) {
			System.out.println("i = " + i);
			new Thread(new HoldThread()).start();
		}
	}

}

/**
 * 测试应用服务器最大支持创建线程数
 * 
 * @author Administrator
 * 
 */
class HoldThread extends Thread {

	public HoldThread() {
	}

	public void run() {
		try {
			testEhcache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testEhcache() {
		JSONObject json = new JSONObject();
		json.put("operators", "hngd.yd");
		HashMap<String, Object> map = ApiTest.callMethod(
				"http://localhost:8080/HNTV2/live/findNewTVType.action",
				json.toString(), "mac:AC5CFE29E146");
		System.out.println(map.get("jsonObject"));
	}
}
