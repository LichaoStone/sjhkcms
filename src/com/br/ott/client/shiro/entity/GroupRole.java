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

/**
 * 组,角色 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-9
 */
public class GroupRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8925575492678008377L;

	private String id;

	private String groupid; // 组ID

	private String roleid; // 角色ID

	private String roleName;

	private String groupName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public GroupRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupRole(String id, String groupid, String roleid) {
		super();
		this.id = id;
		this.groupid = groupid;
		this.roleid = roleid;
	}

}
