/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.client.shiro.entity;

import java.io.Serializable;

public class GroupUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6060771486853467556L;
	private String id; // 记录Id
	private String userid; // 用户id
	private String groupid; // 组id
	private String userName;
	private String groupName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public GroupUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupUser(String id, String userid, String groupid) {
		super();
		this.id = id;
		this.userid = userid;
		this.groupid = groupid;
	}

}
