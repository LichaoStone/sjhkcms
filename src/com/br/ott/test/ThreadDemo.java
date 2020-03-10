package com.br.ott.test;

import java.util.HashMap;

import net.sf.json.JSONObject;

class MyThread extends Thread {

	public void run() {
		testEhcache();
	}

	public void testEhcache() {
		JSONObject json = new JSONObject();
		json.put("operators", "hngd.yd");
		HashMap<String, Object> map = ApiTest.callMethod(
				"http://localhost/HNTV2/api/findNewTVType.action",
				json.toString(), "mac:AC5CFE29E146");
		System.out.println(map.get("jsonObject"));
	}
}

public class ThreadDemo {
	public static void main(String[] args) {
		for(int i=0;i<1000;i++){
			new MyThread().start();
		}
	}
}