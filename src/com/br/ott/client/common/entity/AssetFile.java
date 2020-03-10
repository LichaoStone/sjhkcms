package com.br.ott.client.common.entity;

import java.io.Serializable;

/**
 * 文件名：AssetFile.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
public class AssetFile implements Serializable {
	private static final long serialVersionUID = -8157255641625775143L;
	// 文件ID
	private String fileId;
	// 码率assetBitrate
	private String assetBitrate;
	// 文件FTP地址
	private String ftpUrl;
	// 播放地址
	private String playUrl;
	// MD5(供FTP下载使用)
	private String md5;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getAssetBitrate() {
		return assetBitrate;
	}

	public void setAssetBitrate(String assetBitrate) {
		this.assetBitrate = assetBitrate;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
