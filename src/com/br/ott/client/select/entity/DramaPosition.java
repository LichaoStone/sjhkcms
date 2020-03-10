package com.br.ott.client.select.entity;

import com.br.ott.common.util.Pagination;

/**
 * 文件名：DramaPosition.java
 *  版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：xiaojxiang
 *  创建时间：2016-9-29
 */
public class DramaPosition  extends Pagination {

	private String id;
	
	//推荐位名称
	private String name;
	
	//推荐位编码
	private String code;
	
	//停用or启用状态值
	private String status;
	
	// 公共排序属性
	private String orderColumn;
	// 是否选 中
	private String check;
	
	//排序值
	private int sort ;
	
	//推荐位类型
	private String type ;

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
