package com.br.ott.client.select.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.br.ott.Global;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.common.annotation.FieldName;
import com.br.ott.common.util.Constants.DicType;
import com.br.ott.common.util.Pagination;
import com.br.ott.common.util.StringUtil;

/* 
 *  资产信息
 *  文件名：DramaProgram.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-9 - 下午5:37:33
 */
public class DramaProgram extends Pagination implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 编号
	private String id;
	@FieldName("资产名称")
	private String name;
	@FieldName("其他名称")
	private String otherName;
	@FieldName("内容提供商")
	private String cProvider;
	@FieldName("内容提供商资产id")
	private String assetId;
	// 资产简拼（首字母）
	@FieldName("资产简拼")
	private String pcode;
	@FieldName("同步栏目类型")
	private String oldType;
	@FieldName("看点")
	private String summary;;
	// 产地（中国大陆，中国香港，中国台湾，美国等）
	@FieldName("产地")
	private String origin;
	// 语言(中文，英语，韩文等)
	@FieldName("语言")
	private String language;
	@FieldName("上映年份")
	private String playyear;
	@FieldName("导演")
	private String director;
	@FieldName("主演")
	private String actor;
	@FieldName("主持人")
	private String compere;
	// 资产分类 :基础或专题
	@FieldName("资产分类 :基础或专题")
	private String ptype;
	@FieldName("关键字")
	private String keyword;
	@FieldName("是否已注入图片 1:已下载，0:未下载,2注入失败")
	private String isLoad;
	@FieldName("播放源地址")
	private String playUrl;
	@FieldName("资产介绍")
	private String remark;
	@FieldName("录入时间")
	private String createTime;
	@FieldName("操作人")
	private String operator;
	// 状态：1上线，0待上线，2下线
	@FieldName("状态")
	private String status;
	@FieldName("封面小图地址 列表展示用")
	private String coverXImg;
	@FieldName("封面大图地址 详情和推荐用")
	private String coverDImg;
	@FieldName("推荐大图地址 ")
	private String recDImg;
	@FieldName("推荐小图地址 ")
	private String recXImg;
	// 免费播放（开始提示订购）
	@FieldName(" 免费播放集数")
	private int limitFree;
	@FieldName("点播一次价格")
	private float price;
	// 视频的md5值
	private String md5;
	// 是否连播剧 1是，0否
	@FieldName("资产是否连播剧")
	private String pId;
	// 跳转地址
	private String linkUrl;
	
	// 不包含的资产ID（用于查询）
	private String noContainId;
	
	//包含的资产ID（用于查询）
	private String containId;
	// 排序字段
	private String orderColumn;

	// 二级栏目ID（动作片，科幻片，篮球，足球等）
	private String ttype;
	// 一级栏目ID
	private String parentId;
	// 修改时间
	private String updateTime;
	private List<DramaType> dtList;

	//资产是否合辑1是0否
	private String isAlbum;
	
	// 顺序
	private String queue;
	// 关联ID
	private String daId;
	
	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getContainId() {
		return containId;
	}

	public void setContainId(String containId) {
		this.containId = containId;
	}

	public String getDaId() {
		return daId;
	}

	public void setDaId(String daId) {
		this.daId = daId;
	}

	// 所属专辑ID
	private String albumId;
	
	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public String getIsAlbum() {
		return isAlbum;
	}

	public void setIsAlbum(String isAlbum) {
		this.isAlbum = isAlbum;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCoverImgUrl() {
		if (StringUtil.isEmpty(this.coverXImg)) {
			return "";
		}
		if (this.coverXImg.indexOf("http") > -1) {
			return this.coverXImg;
		} else {
			return Global.SERVER_SOURCE_URL + this.coverXImg;
		}
	}

	public String getNoContainId() {
		return noContainId;
	}

	public void setNoContainId(String noContainId) {
		this.noContainId = noContainId;
	}

	/**
	 * 取得内容提供商名称
	 * 
	 * 创建人：pananz 创建时间：2016-10-17
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public String getProviderName() {
		if (StringUtil.isEmpty(this.cProvider)) {
			return "";
		} else {
			Dictionary dict = DictCache.getDictValue(DicType.LRTGS,
					this.cProvider);
			if (dict != null) {
				return dict.getDicName();
			} else {
				return "";
			}
		}

	}

	/**
	 * 取得资产所属栏目名称(一级名称-二级名称)
	 * 
	 * 创建人：pananz 创建时间：2016-10-17
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public String getTypeName() {
		if (CollectionUtils.isEmpty(dtList)) {
			return "";
		}
		List<String> pList = new ArrayList<String>();
		for (DramaType dt : dtList) {
			pList.add(dt.getParentName() + "-" + dt.getNavName());
		}
		return org.apache.commons.lang.StringUtils.join(pList, "/");
	}

	/**
	 * 取得资产所属栏目名称
	 * 
	 * 创建人：pananz 创建时间：2016-10-17
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public String getTypeName2() {
		if (CollectionUtils.isEmpty(dtList)) {
			return "";
		}
		List<String> pList = new ArrayList<String>();
		for (DramaType dt : dtList) {
			pList.add(dt.getNavName());
		}
		return org.apache.commons.lang.StringUtils.join(pList, "/");
	}

	/**
	 * 取得资产所属栏目ID
	 * 
	 * 创建人：pananz 创建时间：2016-10-17
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public String getDType() {
		if (CollectionUtils.isEmpty(dtList)) {
			return "";
		}
		List<String> pList = new ArrayList<String>();
		for (DramaType dt : dtList) {
			pList.add(dt.getId());
		}
		return org.apache.commons.lang.StringUtils.join(pList, ",");
	}

	public List<DramaType> getDtList() {
		return dtList;
	}

	public void setDtList(List<DramaType> dtList) {
		this.dtList = dtList;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getStatusName() {
		// 1上线，0待上线，2下线
		if ("1".equals(this.status)) {
			return "已上线";
		} else if ("0".equals(this.status)) {
			return "待上线";
		} else if ("2".equals(this.status)) {
			return "已下线";
		}
		return "";
	}

	public String getLoadName() {
		// 1注入成功，0待注入，2注入失败
		if ("1".equals(this.isLoad)) {
			return "注入成功";
		} else if ("0".equals(this.isLoad)) {
			return "待注入";
		} else if ("2".equals(this.isLoad)) {
			return "注入失败";
		}
		return "";
	}

	/**
	 * 取得资产分类名称
	 * 
	 * 创建人：pananz 创建时间：2016-10-17
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public String getPtypeName() {
		if (StringUtil.isEmpty(this.ptype)) {
			return "";
		} else {
			Dictionary dict = DictCache.getDictValue(DicType.JMLX, this.ptype);
			if (dict != null) {
				return dict.getDicName();
			} else {
				return "";
			}
		}

	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
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

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getcProvider() {
		return cProvider;
	}

	public void setcProvider(String cProvider) {
		this.cProvider = cProvider;
	}

	public String getOldType() {
		return oldType;
	}

	public void setOldType(String oldType) {
		this.oldType = oldType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPlayyear() {
		return playyear;
	}

	public void setPlayyear(String playyear) {
		this.playyear = playyear;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getCompere() {
		return compere;
	}

	public void setCompere(String compere) {
		this.compere = compere;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getIsLoad() {
		return isLoad;
	}

	public void setIsLoad(String isLoad) {
		this.isLoad = isLoad;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getCoverDImg() {
		return coverDImg;
	}

	public void setCoverDImg(String coverDImg) {
		this.coverDImg = coverDImg;
	}

	public String getRealCoverDImg() {
		if (StringUtil.isEmpty(this.coverDImg)) {
			return "";
		}
		if (this.coverDImg.indexOf("http") > -1) {
			return this.coverDImg;
		} else {
			return Global.SERVER_SOURCE_URL + this.coverDImg;
		}
	}
	
	public String getCoverXImg() {
		if (StringUtil.isEmpty(this.coverXImg)) {
			return "";
		}
		if (this.coverXImg.indexOf("http") > -1) {
			return this.coverXImg;
		} else {
			return Global.SERVER_SOURCE_URL + this.coverXImg;
		}
	}

	public void setCoverXImg(String coverXImg) {
		this.coverXImg = coverXImg;
	}

	public String getRealCoverXImg() {
		if (StringUtil.isEmpty(this.coverXImg)) {
			return "";
		}
		if (this.coverXImg.indexOf("http") > -1) {
			return this.coverXImg;
		} else {
			return Global.SERVER_SOURCE_URL + this.coverXImg;
		}
	}
	
	public String getRecDImg() {
		return recDImg;
	}

	public void setRecDImg(String recDImg) {
		this.recDImg = recDImg;
	}
	
	public String getRealRecDImg() {
		if (StringUtil.isEmpty(this.recDImg)) {
			return "";
		}
		if (this.recDImg.indexOf("http") > -1) {
			return this.recDImg;
		} else {
			return Global.SERVER_SOURCE_URL + this.recDImg;
		}
	}

	public String getRecXImg() {
		return recXImg;
	}

	public void setRecXImg(String recXImg) {
		this.recXImg = recXImg;
	}

	public String getRealRecXImg() {
		if (StringUtil.isEmpty(recXImg)) {
			return "";
		}
		return Global.SERVER_SOURCE_URL + recXImg;
	}
	
	public int getLimitFree() {
		return limitFree;
	}

	public void setLimitFree(int limitFree) {
		this.limitFree = limitFree;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	// 处理批量导入excel文件时，把所属栏目的值暂时先附在该属性上
	private Set<String> dramaTypeNames;

	public Set<String> getDramaTypeNames() {
		return dramaTypeNames;
	}

	public void setDramaTypeNames(Set<String> dramaTypeNames) {
		this.dramaTypeNames = dramaTypeNames;
	}

	// 推荐资产信息排序使用
	private String sort;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	// 推荐资产信息一级栏目名称
	private String level1NavName;

	public String getLevel1NavName() {
		return level1NavName;
	}

	public void setLevel1NavName(String level1NavName) {
		this.level1NavName = level1NavName;
	}

}
