package com.br.ott.client.operasset.entity;


import com.br.ott.common.util.Pagination;

public class Assets extends Pagination{
	//id
	private String id;
	//工单号
	private String correlateID;
	//指定文件url下载地址
	private String cmdFileURL;
	//保存文件名
	private String localFileName;
	//交接时间
	private String jointTime;
	//生成文件情况
	private String isCreateFile;
	//处理文件情况
	private String isDealFile;
	//生成文件异常
	private String cerror;
	//处理文件异常
	private String derror;
	//信息反馈给供应商状态
	private String sendStatus;
	//反馈时间
	private String sendTime;
	// 排序字段
	private String orderColumn;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getLocalFileName() {
		return localFileName;
	}
	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}
	public String getJointTime() {
		return jointTime;
	}
	public void setJointTime(String jointTime) {
		this.jointTime = jointTime;
	}
	public String getIsCreateFile() {
		return isCreateFile;
	}
	public void setIsCreateFile(String isCreateFile) {
		this.isCreateFile = isCreateFile;
	}
	public String getIsDealFile() {
		return isDealFile;
	}
	public void setIsDealFile(String isDealFile) {
		this.isDealFile = isDealFile;
	}
	public String getCerror() {
		return cerror;
	}
	public void setCerror(String cerror) {
		this.cerror = cerror;
	}
	public String getDerror() {
		return derror;
	}
	public void setDerror(String derror) {
		this.derror = derror;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
}
