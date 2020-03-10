package com.br.ott.client.select.entity;

import com.br.ott.Global;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.common.util.Pagination;
import com.br.ott.common.util.StringUtil;

/* 
 *  
 *  文件名：ChannelType.java
 *  创建人：pananz
 *  创建时间：2014-7-4 - 下午6:19:58
 */
public class DramaNavigation extends Pagination {

	private String id;
	// 频道
	private String name;
	// 上一级栏目
	private String parentId;
	// 类型编码
	private String code;
	// 状态
	private String status;
	// 公共排序值
	private int sequence;
	// 类型属性运营商
	private String operators;
	// 公共排序属性
	private String orderColumn;
	private boolean isCheck;
	// 是否推荐
	private String recommend;

	// logo图片地址
	private String imgurl;

	//是否独立展示（0否1是）
	private String isdl_display;
	
	//本地栏目对应的csp(内容提供商)的栏目id
	private String categoryId;
	
	private int oldSequence;
	
	public String getSortName() {
		String sort = String.valueOf(sequence);
		if ("1".equals(sort)) {
			return "左1";
		} else if ("2".equals(sort)) {
			return "右1";
		} else if ("3".equals(sort)) {
			return "左2";
		} else if ("4".equals(sort)) {
			return "右2";
		} else if ("5".equals(sort)) {
			return "左3";
		} else if ("6".equals(sort)) {
			return "右3";
		} else if ("7".equals(sort)) {
			return "左4";
		} else if ("8".equals(sort)) {
			return "右4";
		} else if ("9".equals(sort)) {
			return "左5";
		} else if ("10".equals(sort)) {
			return "右5";
		} else {
			return sort;
		}
	}
	
	public int getOldSequence() {
		return oldSequence;
	}

	public void setOldSequence(int oldSequence) {
		this.oldSequence = oldSequence;
	}

	public String getParentName() {
		DramaNavigation dNavigation = NavigationCache
				.getNavigationById(this.parentId);
		if (dNavigation != null) {
			return dNavigation.getName();
		}
		return "";
	}

	/**
	 * 图片HTTP地址
	 * 
	 * 创建人：pananz 创建时间：2016-10-19
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public String getImg() {
		if (StringUtil.isEmpty(this.imgurl)) {
			return "";
		}
		if (this.imgurl.indexOf("http") > -1) {
			return this.imgurl;
		} else {
			return Global.SERVER_SOURCE_URL + this.imgurl;
		}
	}

	public String getOperators() {
		return operators;
	}

	public void setOperators(String operators) {
		this.operators = operators;
	}

	public DramaNavigation() {
		super();
	}

	public DramaNavigation(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getImgurl() {
		return imgurl;
	}

	public String getRealImgurl() {
		if (StringUtil.isEmpty(this.imgurl)) {
			return "";
		}
		if (this.imgurl.indexOf("http") > -1) {
			return this.imgurl;
		} else {
			return Global.SERVER_SOURCE_URL + this.imgurl;
		}
		
	}
	
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getIsdl_display() {
		return isdl_display;
	}

	public void setIsdl_display(String isdl_display) {
		this.isdl_display = isdl_display;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
