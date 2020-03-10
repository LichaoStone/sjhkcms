package com.br.ott.client.select.entity;

import java.io.Serializable;

import com.br.ott.common.util.Pagination;

/**
 * 文件名：DramaChildren.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
public class DramaChildren extends Pagination implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 编号
	private String id;
	// 资产编号
	private String dramaId;
	// 资产名称
	private String dramaName;
	// 第N集
	private String title;
	// 播放时长
	private String timeLongth;
	// 点播价格
	private float price;
	// 录入时间
	private String createTime;
	// 操作人
	private String operator;
	// 资产描述
	private String dramaDesc;
	// 状态：1启用，0停用
	private String status;
	// 排序字段
	private String orderColumn;

	//播放地址playUrl
	private String playUrl;
	
	public String getStatusName() {
		if ("1".equals(this.status)) {
			return "启用";
		} else if ("0".equals(this.status)) {
			return "停用";
		}
		return "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDramaId() {
		return dramaId;
	}

	public void setDramaId(String dramaId) {
		this.dramaId = dramaId;
	}

	public String getDramaName() {
		return dramaName;
	}

	public void setDramaName(String dramaName) {
		this.dramaName = dramaName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTimeLongth() {
		return timeLongth;
	}

	public void setTimeLongth(String timeLongth) {
		this.timeLongth = timeLongth;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDramaDesc() {
		return dramaDesc;
	}

	public void setDramaDesc(String dramaDesc) {
		this.dramaDesc = dramaDesc;
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

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

}
