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
import java.util.HashSet;
import java.util.Set;

import com.br.ott.common.util.Constants.State;
import com.br.ott.common.util.StringUtil;

/**
 * 资源信息实体类 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-1
 */
public class Resources implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3580102697353440729L;

	private String id; // 资源记录id
	private Resources parent;
	private String name; // 资源名称
	private String enname; // 英文名称
	private String systemid; // 所属系统id
	private String parentId; // 上级资源记录id
	private String resourcetype; // 资源类型
	private String link; // 链接
	private String icon; // 图标
	private String iconopen; // 打开图标
	private String isopen; // 是否打开
	private String isleaf; // 是否节点
	private String status; // 状态
	private String orderid; // 排序
	private String memo; // 备注

	private String parnetName;
	private String systemName;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getParnetName() {
		return parnetName;
	}

	public void setParnetName(String parnetName) {
		this.parnetName = parnetName;
	}

	private Set<Resources> children = new HashSet<Resources>(0);

	public Set<Resources> getChildren() {
		return children;
	}

	public void setChildren(Set<Resources> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Resources getParent() {
		return parent;
	}

	public void setParent(Resources parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getSystemid() {
		return systemid;
	}

	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getResourcetype() {
		return resourcetype;
	}

	public void setResourcetype(String resourcetype) {
		this.resourcetype = resourcetype;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconopen() {
		return iconopen;
	}

	public void setIconopen(String iconopen) {
		this.iconopen = iconopen;
	}

	public String getIsopen() {
		return isopen;
	}

	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Resources() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Resources(String id, Resources parent, String name, String enname,
			String systemid, String parentId, String resourcetype, String link,
			String icon, String iconopen, String isopen, String isleaf,
			String status, String orderid, String memo, String parnetName,
			Set<Resources> children) {
		super();
		this.id = id;
		this.parent = parent;
		this.name = name;
		this.enname = enname;
		this.systemid = systemid;
		this.parentId = parentId;
		this.resourcetype = resourcetype;
		this.link = link;
		this.icon = icon;
		this.iconopen = iconopen;
		this.isopen = isopen;
		this.isleaf = isleaf;
		this.status = status;
		this.orderid = orderid;
		this.memo = memo;
		this.parnetName = parnetName;
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Resources) {
			Resources res = (Resources) obj;
			if (this.name.equals(res.name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符集转布尔值 创建人：pananz 创建时间：2012-11-12 - 上午11:40:20
	 * 
	 * @return 返回类型：boolean
	 * @exception throws
	 */
	public boolean changeIsOpen() {
		if (StringUtil.isEmpty(this.isopen)) {
			return true;
		}
		if (State.INVALID.equals(this.isopen)) {
			return true;
		} else {
			return false;
		}
	}
}
