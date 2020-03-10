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
 * 角色权限实体
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-1
 */
public class RoleAuth implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8151490047348907551L;
	
	private String id;
	private String roleId;      // 角色id
	private String resourceId;	// 资源id
	private String actions;		// 操作id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public RoleAuth() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RoleAuth(String id, String roleId, String resourceId, String actions) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.resourceId = resourceId;
		this.actions = actions;
	}
	
}
