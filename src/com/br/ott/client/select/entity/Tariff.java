package com.br.ott.client.select.entity;

import com.br.ott.common.annotation.FieldName;
import com.br.ott.common.util.Constants.State;
import com.br.ott.common.util.Pagination;
import com.br.ott.common.util.StringUtil;

/* 
 *  资费实体信息
 *  文件名：Tariff.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 上午11:01:51
 */
public class Tariff extends Pagination {
	private String id;
	@FieldName("资费名称")
	private String tariffName;
	@FieldName("资费值")
	private float tariffValue;
	// 资费类型1，按次；2，包月；3，时长
	@FieldName("资费类型")
	private String tariffType;
	@FieldName("资费说明")
	private String tariffDesc;
	// 状态 0，停用；1，可用
	@FieldName("资费状态")
	private String status;
	private String orderColumn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTariffName() {
		return tariffName;
	}

	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}

	public float getTariffValue() {
		return tariffValue;
	}

	public void setTariffValue(float tariffValue) {
		this.tariffValue = tariffValue;
	}

	public String getTariffType() {
		return tariffType;
	}

	public void setTariffType(String tariffType) {
		this.tariffType = tariffType;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
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

	public String getStatusName() {
		if (StringUtil.isEmpty(this.status)
				|| State.INVALID.equals(this.status)) {
			return "停用";
		}
		return "启用";
	}

	public String getTariffTypeName() {
		if (StringUtil.isEmpty(this.tariffType)) {
			return "";
		} else if ("1".equals(this.tariffType)) {
			return "按次";
		} else if ("2".equals(this.tariffType)) {
			return "包月";
		} else if ("3".equals(this.tariffType)) {
			return "时常";
		} else {
			return "";
		}

	}
}
