/*
 * @# PartnerController.java Aug 1, 2012 2:12:47 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.jackson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.br.ott.common.util.StringUtil;

/**
 * tinytree的数据结构.
 * 
 * @author huang weijian
 */
public class TinyTreeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6434019587853169934L;
	private String id;
	private String name;
	private String url;
	private boolean leaf = true;
	private boolean checked;
	private boolean disabled;
	private boolean hide;
	private String style;
	private String parentId;
	private int orderId;

	private long childCount = 0L;
	private List<TinyTreeBean> children = new ArrayList<TinyTreeBean>();

	public TinyTreeBean() {
		// empty!
	}

	public TinyTreeBean(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<TinyTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<TinyTreeBean> children) {
		this.children = children;
		if (CollectionUtils.isEmpty(children)) {
			setLeaf(true);
		} else {
			setLeaf(false);
			childCount += children.size();
		}
	}

	public void addChild(TinyTreeBean child) {
		children.add(child);
		setLeaf(false);
		childCount++;
	}

	public long getChildCount() {
		return childCount;
	}

	public void setChildCount(long childCount) {
		this.childCount = childCount;
		if (childCount > 0L) {
			setLeaf(false);
		} else {
			setLeaf(true);
		}
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 设置一级菜单样式（根据汉字长度）
	 * 创建人：pananz
	 * 创建时间：2012-11-12 - 上午10:25:17
	 * 返回类型：void
	 * @exception throws
	 */
	public void selectStyle() {
		if (StringUtil.isEmpty(this.name)) {
			return;
		}
		if (this.name.length() == 2) {
			this.style = "mm";
		} else if (this.name.length() == 3) {
			this.style = "mm3";
		} else if (this.name.length() == 4) {
			this.style = "mm2";
		} else {
			this.style = "mm";
		}
	}

}
