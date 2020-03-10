package com.br.ott.client.select.entity;

import com.br.ott.common.util.Pagination;

/**
 * 文件名：AssetLog.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
public class AssetLog extends Pagination {
	private String id;
	// 资产id
	private String assetId;
	// 资产名称
	private String assetTitle;
	// 操作动作 ON，上线 OFF，下线
	private String action;
	// 同步报文
	private String assetInfo;
	// 同步记录时间
	private String recordTime;
	// 同步结果
	private String result;
	// 同步返回报文
	private String returnInfo;

	private String orderColumn;
	private String recordTimeMin;
	private String recordTimeMax;
	// 临时变量(结束时间)
	private String endTime;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultName() {
		if ("0".equals(result)) {
			return "成功";
		} else if ("1".equals(result)) {
			return "失败";
		}
		return result;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(String assetTitle) {
		this.assetTitle = assetTitle;
	}

	public String getAssetInfo() {
		return assetInfo;
	}

	public void setAssetInfo(String assetInfo) {
		this.assetInfo = assetInfo;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getRecordTimeMin() {
		return recordTimeMin;
	}

	public void setRecordTimeMin(String recordTimeMin) {
		this.recordTimeMin = recordTimeMin;
	}

	public String getRecordTimeMax() {
		return recordTimeMax;
	}

	public void setRecordTimeMax(String recordTimeMax) {
		this.recordTimeMax = recordTimeMax;
	}

}
