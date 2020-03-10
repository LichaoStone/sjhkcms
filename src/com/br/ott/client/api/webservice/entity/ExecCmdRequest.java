package com.br.ott.client.api.webservice.entity;

/* 
 *  文件名：ExecCmdRequest.java
 *  版权：BoRuiCube. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方
 *  创建人：李政宏
 *  创建时间：2016-12-15 - 下午6:00:29
 */

public class ExecCmdRequest {
	
	private String cspid;
	
	private String lspid;
	
	private String correlateID;
	
	private String cmdFileURL;

	public String getCspid() {
		return cspid;
	}

	public void setCspid(String cspid) {
		this.cspid = cspid;
	}

	public String getLspid() {
		return lspid;
	}

	public void setLspid(String lspid) {
		this.lspid = lspid;
	}

	public String getCorrelateID() {
		return correlateID;
	}

	public void setCorrelateID(String correlateID) {
		this.correlateID = correlateID;
	}

	public String getCmdFileURL() {
		return cmdFileURL;
	}

	public void setCmdFileURL(String cmdFileURL) {
		this.cmdFileURL = cmdFileURL;
	}
		
}
