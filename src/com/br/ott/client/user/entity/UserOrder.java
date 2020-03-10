package com.br.ott.client.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.br.ott.common.util.DateUtil;
import com.br.ott.common.util.Pagination;

/* 
 *  用户订单实体
 *  文件名：UserOrder.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-9 - 上午10:26:19
 */
public class UserOrder extends Pagination implements Serializable {

	/** */
	private static final long serialVersionUID = 1447840973273764850L;

	private String id;
	// 订单流水号
	private String orderId;
	// 用户宽带账号
	private String account;
	// 盒子MAC
	private String mac;
	// 订购节目编号(节目基础表)
	private String programId;
	// 订购节目名称
	private String programName;
	// 节目类型（1：单点播，2，资产点播）
	private String type;
	// 订购价格
	private float price;
	// 订购时间
	private Date orderTime;
	// 订购状态(取消订购0, 订购成功1，订购过期-1)
	private String status;
	private String orderColumn;

	public String getStatusName() {
		int n = Integer.parseInt(status);
		String s = "";
		switch (n) {
		case 0:
			s = "取消订购";
			break;
		case 1:
			s = "订购成功";
			break;
		case -1:
			s = "订购过期";
			break;
		default:
			break;
		}
		return s;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderTimeStr() {
		return DateUtil.toString(orderTime);
	}
}
