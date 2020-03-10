package com.br.ott.client.common.entity;

import java.io.Serializable;

/* 
 *  
 *  文件名：Area.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2013-1-17 - 下午4:00:25
 */
public class District implements Serializable {
	/** */
	private static final long serialVersionUID = 2379437775267981054L;
	private int id;
	private String name;
	private int level;
	private String areacode;// 区号
	private String postcode;// 邮政编码
	private int upid;
	private int displayorder;// 排序

	public int getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public int getUpid() {
		return upid;
	}

	public void setUpid(int upid) {
		this.upid = upid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + upid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof District) {
			District dist = (District) obj;
			if (this.name.equals(dist.name) && this.upid == dist.upid) {
				return true;
			}
		}
		return false;
	}
}
