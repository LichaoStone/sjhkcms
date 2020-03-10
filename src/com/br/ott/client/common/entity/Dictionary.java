/*
 * @# Dictionary.java Aug 6, 2012 3:21:26 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.common.entity;

import java.io.Serializable;

import com.br.ott.client.common.DictCache;
import com.br.ott.common.annotation.FieldName;
import com.br.ott.common.util.Constants.State;
import com.br.ott.common.util.StringUtil;

/*
 * @author pananz
 * TODO
 * 字典表实体
 */
public class Dictionary implements Serializable {

	/** */
	private static final long serialVersionUID = -2773946124229268640L;
	private String id;
	// '昵称';
	@FieldName("昵称")
	private String dicnickName;
	// '常量显示名称';
	@FieldName("常量显示名称")
	private String dicName;
	// '常量代码值';
	@FieldName("常量代码值")
	private String dicValue;
	// '创建日期';
	@FieldName("创建日期")
	private String createTime;
	// '常量分类值';
	@FieldName("常量分类值")
	private String dicType;
	// '是否可编辑';
	@FieldName("是否可编辑")
	private String updatable;
	// '创建人';
	@FieldName("创建人")
	private String creator;
	// '状态';
	@FieldName("状态")
	private String status;
	// '排序';
	private String orderId;
	// '备注';
	@FieldName("备注")
	private String memo;

	private String orderColumn;
	private String check;

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getDicTypeName() {
		if (StringUtil.isEmpty(this.dicType)) {
			return "";
		} else {
			Dictionary dictionary = null;
			if (!"0".equals(this.dicType)) {
				dictionary = DictCache.getDictValue("0", this.dicType);
			}
			return null == dictionary ? "" : dictionary.getDicName();
		}
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

	public String getDicnickName() {
		return dicnickName;
	}

	public void setDicnickName(String dicnickName) {
		this.dicnickName = dicnickName;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getDicValue() {
		return dicValue;
	}

	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	public String getUpdatable() {
		return updatable;
	}

	public void setUpdatable(String updatable) {
		this.updatable = updatable;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatusName() {
		if (State.INVALID.equals(this.status)) {
			return "停用";
		} else if (State.VALID.equals(this.status)) {
			return "启用";
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dicName == null) ? 0 : dicName.hashCode());
		result = prime * result
				+ ((dicValue == null) ? 0 : dicValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Dictionary) {
			Dictionary dict = (Dictionary) obj;
			if (this.dicValue.equals(dict.dicValue)
					&& this.dicName.equals(dict.dicName)) {
				return true;
			}
		}
		return false;
	}

}
