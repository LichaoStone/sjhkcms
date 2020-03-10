/*
 * @# PaginationUtil.java Jul 31, 2012 10:03:40 AM
 * 
 * Copyright (C) 2011 - 2012 青岛博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/*
 * @author pananz
 * TODO
 */
public class PagedModelList<T> {
	// 当前第几页
	private int pageId;
	// 每页行数
	private int pageSize;
	// 总页数
	private int pageCount;
	// 总记录数
	private int totalCount;
	// 数据集合
	private List<T> pagedModelList;
	// 每页行数选择
	public static String[] pageRowSelect = { "10", "15", "20", "50", "100" };
	// 是否显示行数选择
	private boolean isShowRowSelect = true;

	public PagedModelList() {
		super();
	}

	public PagedModelList(int totalCount) {
		this.pageId = 1;
		this.pageSize = 15;
		this.totalCount = totalCount;
		this.pageCount = 1;
		this.pagedModelList = new ArrayList<T>();
	}

	public PagedModelList(int pageID, int pageSize, int totalCount) {
		this.pageId = pageID;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.pagedModelList = new ArrayList<T>();
		setPageCount();
	}

	public PagedModelList(int pageSize, int pageCount) {
		super();
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.pagedModelList = new ArrayList<T>();
	}

	public int getPageCount() {
		return pageCount;
	}

	public List<T> getPagedModelList() {
		return pagedModelList;
	}

	public Object[] getPagedModels() {
		return pagedModelList.toArray(new Object[pagedModelList.size()]);
	}

	public void addModel(T t) {
		pagedModelList.add(t);
	}

	public void addModels(List<T> modelList) {
		if (CollectionUtils.isEmpty(modelList))
			return;
		for (T t : modelList) {
			pagedModelList.add(t);
		}
		setPageCount();
	}

	public int getPageId() {
		return pageId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 设置总页面 创建人：pananz 创建时间：2012-11-4 - 下午10:44:36 返回类型：void
	 * 
	 * @exception throws
	 */
	protected void setPageCount() {
		if (pageSize <= 0) {
			pageCount = 1;
		} else {
			int pn = totalCount / pageSize;
			if (totalCount % pageSize != 0)
				pn++;
			if (pn == 0)
				pn++;
			pageCount = pn;
		}
	}

	public int[] getPages() {
		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0)
			count++;
		int pages[] = new int[count];
		for (int i = 0; i < count; i++)
			pages[i] = i;

		return pages;
	}

	public boolean isShowRowSelect() {
		return isShowRowSelect;
	}

	public void setShowRowSelect(boolean isShowRowSelect) {
		this.isShowRowSelect = isShowRowSelect;
	}

	public String[] getPageRowSelect() {
		return pageRowSelect;
	}
}
