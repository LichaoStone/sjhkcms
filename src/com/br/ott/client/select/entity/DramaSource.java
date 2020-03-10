package com.br.ott.client.select.entity;

import com.br.ott.common.util.Pagination;
import com.br.ott.common.util.StringUtil;

/* 
 *  资产子节目源信息
 *  文件名：DramaSource.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-10 - 下午3:57:10
 */
public class DramaSource extends Pagination {

	// 编号
	private String id;
	// 子资产编号
	private String childId;
	// 码率
	private String bitrate;
	// 码率类型 1.极速，2高清，3蓝光，4 4K，5扩展，6，扩展
	private String bitrateType;
	// 播放源地址
	private String playUrl;
	// md5
	private String md5;
	// 状态：1启用，0停用
	private String status;
	// ftp地址
	private String ftpUrl;
	// 文件ID
	private String fileId;
	// 排序字段
	private String orderColumn;
	// 码率等级
	private String grade;

	//播放源
	private String source;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStatusName() {
		if ("1".equals(this.status)) {
			return "启用";
		} else if ("0".equals(this.status)) {
			return "停用";
		}
		return "";
	}

	public String getBitrateType() {
		return bitrateType;
	}

	public void setBitrateType(String bitrateType) {
		this.bitrateType = bitrateType;
	}

	public String getBitrateTypeStr() {
		if ("1".equals(bitrateType)) {
			return "急速";
		} else if ("2".equals(bitrateType)) {
			return "高清";
		} else if ("3".equals(bitrateType)) {
			return "蓝光";
		} else if ("4".equals(bitrateType)) {
			return "4K";
		} else {
			return "其他";
		}
	}

	/**
	 * 急速 950-1450
	 * 
	 * 高清 2000-2501
	 * 
	 * 蓝光 5000-7999
	 * 
	 * 4K 8000-15000
	 * 
	 * 
	 * 创建人：pananz 创建时间：2016-10-25
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public void setBitrateTypeStr() {
		if (StringUtil.isEmpty(bitrate)) {
			return;
		}
		try {
			int bit = Integer.parseInt(bitrate);
			if (bit >= 950 && bit <= 1450) {
				this.bitrateType = "1";
			} else if (bit >= 2000 && bit <= 2501) {
				this.bitrateType = "2";
			} else if (bit >= 5000 && bit <= 7999) {
				this.bitrateType = "3";
			} else if (bit >= 8000 && bit <= 15000) {
				this.bitrateType = "4";
			} else {
				this.bitrateType = "5";
			}
		} catch (NumberFormatException e) {
			return;
		}
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}
