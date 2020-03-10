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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.br.ott.client.common.DistrictCache;
import com.br.ott.client.common.entity.District;
import com.br.ott.common.util.StringUtil;

public class UserInfo implements Serializable {

	/**
	 * 用户实体 
	 */
	private static final long serialVersionUID = -1691370977360113750L;
	private String userId;
	private String userName; // 用户名
	private String password; // 密码
	private String loginname; // 真实姓名
	private String areaid; // 地区id
	private String status; // 状态 0:停用 1:正常
	//	private String type;		// 类型 :后台用户管理，商户用户管理，商品添加员，客服人员，短信发送员，财务统计员;合作商，取字典表
	private String groupId; // 组id
	private String partnerid; // 合作伙伴id
	private String phone; // 手机号
	private Date createdate; // 加入时间
	private String operators;//所属运营商编码

	private String dicname;

	private String cdate;

	private String nodeIds;

	private boolean isSuperman;
	
	public boolean isSuperman() {
		return isSuperman;
	}

	public void setSuperman(boolean isSuperman) {
		this.isSuperman = isSuperman;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	private String partnername;

	public String getPartnername() {
		return partnername;
	}

	public void setPartnername(String partnername) {
		this.partnername = partnername;
	}

	public String getDicname() {
		return dicname;
	}

	public void setDicname(String dicname) {
		this.dicname = dicname;
	}

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

	private Set<Role> roles = new HashSet<Role>();

	public Set<Groups> getGroups() {
		return groups;
	}

	public void setGroups(Set<Groups> groups) {
		this.groups = groups;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getAreaName() {
		if (StringUtil.isEmpty(this.areaid)) {
			return "";
		} else {
			District district = DistrictCache.getCityByAreaCode(areaid);
			return null == district ? "" : district.getName();
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public UserInfo() {
	}

	public String getNodeIds() {
		return nodeIds;
	}

	public void setNodeIds(String nodeIds) {
		this.nodeIds = nodeIds;
	}

	public String getOperators() {
		return operators;
	}
	
	public void setOperators(String operators) {
		this.operators = operators;
	}

}
