package com.br.ott.client.select.entity;

import com.br.ott.client.common.DramaPositionCache;
import com.br.ott.common.util.Pagination;
import com.br.ott.common.util.StringUtil;

/**
 * 文件名：RecommendPosition.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：xiaojxiang 创建时间：2016-9-19
 */
public class DramaRecommend extends Pagination {
	private String id;
	// 资产id
	private String dramaId;
	// 资产名称
	private String dramaName;
	// 最后修改时间
	private String lasttime;
	// 最后操作人
	private String lastopt;
	// 排序值
	private String sort;
	// 推荐位ID
	private String prType;
	// 排序字段
	private String orderColumn;
	private String cList;

	public String getcList() {
		return cList;
	}

	public void setcList(String cList) {
		this.cList = cList;
	}

	public String getDramaName() {
		return dramaName;
	}

	public void setDramaName(String dramaName) {
		this.dramaName = dramaName;
	}

	public String getPrTypeName() {
		if (StringUtil.isEmpty(this.prType)) {
			return "";
		}
		return DramaPositionCache.getNameById(prType);
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

	public String getDramaId() {
		return dramaId;
	}

	public void setDramaId(String dramaId) {
		this.dramaId = dramaId;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getLastopt() {
		return lastopt;
	}

	public void setLastopt(String lastopt) {
		this.lastopt = lastopt;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getPrType() {
		return prType;
	}

	public void setPrType(String PrType) {
		prType = PrType;
	}

}
