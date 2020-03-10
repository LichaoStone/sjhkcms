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
 * 权限实体类
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-7-30
 */
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5425737226667295291L;

	private String oid; // 角色记录id
	private String roleName; // 角色名称
	private String description; // 描述
	private String enname; // 英文名称
	private String status; // 状态,包括：启用,隐藏,冻结,弃用
	private String orderid; // 排序
	private String memo; // 备注
	
	private String orderColumn;
	
	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	/**
	 * 关联用户组
	 */
	private Set<Groups> groups = new HashSet<Groups>();
	/**
	 * 关联资源信息
	 */
	private Set<Resources> resources = new HashSet<Resources>();

	public Set<Groups> getGroups() {
		return groups;
	}

	public void setGroups(Set<Groups> groups) {
		this.groups = groups;
	}

	public Set<Resources> getResources() {
		return resources;
	}

	public void setResources(Set<Resources> resources) {
		this.resources = resources;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(String oid, String roleName, String enname, String status, String description, String orderid,
			String memo) {
		super();
		this.oid = oid;
		this.roleName = roleName;
		this.enname = enname;
		this.status = status;
		this.orderid = orderid;
		this.memo = memo;
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Role) {
			Role res = (Role) obj;
			if (this.oid.equals(res.oid)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 角色状态
	 */
	public String getStatusStr(){
		if(null == status){
			return null;
		}
		if(Constants.RoleState.STATE1.equals(status)){
			return "启用";
		}
		if(Constants.RoleState.STATE2.equals(status)){
			return "隐藏";
		}
		if(Constants.RoleState.STATE3.equals(status)){
			return "冻结";
		}
		if(Constants.RoleState.STATE4.equals(status)){
			return "弃用";
		}
		return null;
	}
}
