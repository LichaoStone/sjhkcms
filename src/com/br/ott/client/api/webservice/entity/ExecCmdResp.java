package com.br.ott.client.api.webservice.entity;

/* 
 *  文件名：ExecCmdResponse.java
 *  版权：BoRuiCube. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方
 *  创建人：李政宏
 *  创建时间：2016-12-15 - 下午6:00:29
 */

public class ExecCmdResp {
	
	private String result;
	
	private String errorDescription;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
}
