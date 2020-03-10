package com.br.ott.client.select.entity;

import java.io.Serializable;

import com.br.ott.client.common.NavigationCache;

/**
 * 文件名：DramaType.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
public class DramaType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String dramaId;
	private String dramaName;
	private String navId;
	
	private String cList;
	
	
	public DramaType(String dramaId, String navId) {
		super();
		this.dramaId = dramaId;
		this.navId = navId;
	}

	public DramaType() {
		super();
	}

	public String getcList() {
		return cList;
	}

	public void setcList(String cList) {
		this.cList = cList;
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

	public String getDramaName() {
		return dramaName;
	}

	public void setDramaName(String dramaName) {
		this.dramaName = dramaName;
	}

	public String getNavName() {
		return NavigationCache.getNameById(this.navId);
	}

	public String getParentName() {
		return NavigationCache.getParentNameById(this.navId);
	}
	
	public String getNavId() {
		return navId;
	}

	public void setNavId(String navId) {
		this.navId = navId;
	}

}
