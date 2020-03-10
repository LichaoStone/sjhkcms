/* 
* Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
* 
* This software is the confidential and proprietary information of 
* Founder. You shall not disclose such Confidential Information 
* and shall use it only in accordance with the terms of the agreements 
* you entered into with Founder. 
* 
*/ 

package com.br.ott.client.shiro.entity;

import java.io.Serializable;


/**
 * 系统信息实体类
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-1
 */
public class Systems implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9062223678917681592L;
	
	private String id;				// 记录id
	private String name;			// 系统名
	private String ename; 			// 英文名称
	private String builddate;		// 构建日期
	private String contextPath;		// 上下文
	private String icon;			// 系统图标
	private String logo;			// 系统标志
	private String memo;			// 备注
	
	private String order;			// 排序
	private String status;			// 状态
	private String tablePrefix;		// 表名前缀
	private String version;			// 版本
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getBuilddate() {
		return builddate;
	}
	public void setBuilddate(String builddate) {
		this.builddate = builddate;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Systems() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Systems(String id, String name, String ename, String builddate,
			String contextPath, String icon, String logo, String memo,
			String order, String status, String tablePrefix, String version) {
		super();
		this.id = id;
		this.name = name;
		this.ename = ename;
		this.builddate = builddate;
		this.contextPath = contextPath;
		this.icon = icon;
		this.logo = logo;
		this.memo = memo;
		this.order = order;
		this.status = status;
		this.tablePrefix = tablePrefix;
		this.version = version;
	}
	
	

}
