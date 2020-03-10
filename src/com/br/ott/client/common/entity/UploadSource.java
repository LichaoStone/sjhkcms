/*
 * @# UploadSource.java Aug 7, 2012 4:00:58 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.common.entity;

import com.br.ott.Global;
import com.br.ott.common.util.StringUtil;

/*
 * @author pananz
 * TODO
 * 上传内容资源
 */
public class UploadSource {

	public static final String SOURCE_AUTO_IMG = "AUTO_IMG";
	public static final String SOURCE_VIDEO = "VIDEO";
	public static final String SOURCE_APP_APK = "APP_APK";
	public static final String SOURCE_APP_J2ME = "APP_J2ME";

	private String id;
	// 资源名称
	private String sourceName;
	// 上传日期
	private String uploadDate;
	// 文件保存路径
	private String savePath;
	// 上传人
	private String uploadUser;
	// 资源类型 图片：IMG,应用程序等：APP,视频：VIDEO
	private String sourceType;
	private String pid;
	// 资源是否失效(1：有效，0失效）
	private String status;
	// 排序
	private String sequence;

	private String orderColumn;

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSourceTypeName() {
		if (StringUtil.isEmpty(this.sourceType)) {
			return "暂无此类型";
		}
		if (SOURCE_APP_APK.equals(this.sourceType)) {
			return "ANDROID";
		} else if (SOURCE_APP_J2ME.equals(this.sourceType)) {
			return "J2ME";
		} else if (SOURCE_AUTO_IMG.equals(this.sourceType)) {
			return "开机图片";
		} else if (SOURCE_VIDEO.equals(this.sourceType)) {
			return "视频";
		} else {
			return "图片";
		}
	}

	public String getSourceUrl() {
		return Global.SERVER_SOURCE_URL + this.savePath;
	}
}
