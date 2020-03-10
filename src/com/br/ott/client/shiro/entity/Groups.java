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
import java.util.HashSet;
import java.util.Set;

import com.br.ott.common.util.Constants;

/**
 * 组实体类 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-1
 */
public class Groups implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3840193805795529596L;

	private String id; // 组记录id
	private String name; // 组名称
	private String parentId; // 上级组Id
	private String enName; // 英文名称
	private String groupType; // 组类别
	private String status; // 状态
	private String orderId; // 排序
	private String memo; // 备注

	private String roleId; // 角色ID

	private String parnetName;

	private String orderColumn;

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getParnetName() {
		return parnetName;
	}

	public void setParnetName(String parnetName) {
		this.parnetName = parnetName;
	}

	/**
	 * 关联用户
	 */
	private Set<UserInfo> users = new HashSet<UserInfo>();
	/**
	 * 关联权限
	 */
	private Set<Role> roles = new HashSet<Role>();

	public Set<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(Set<UserInfo> users) {
		this.users = users;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
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

	public Groups() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Groups(String id, String name, String parentId, String enName,
			String groupType, String status, String orderId, String memo) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.enName = enName;
		this.groupType = groupType;
		this.status = status;
		this.orderId = orderId;
		this.memo = memo;
	}
	
	/**
	 * 组状态
	 */
	public String getStatusStr(){
		if(null == status){
			return null;
		}
		if(Constants.GroupState.STATE1.equals(status)){
			return "启用";
		}
		if(Constants.GroupState.STATE2.equals(status)){
			return "隐藏";
		}
		if(Constants.GroupState.STATE3.equals(status)){
			return "冻结";
		}
		if(Constants.GroupState.STATE4.equals(status)){
			return "弃用";
		}
		return null;
	}

}
