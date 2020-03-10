/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.client.shiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.br.ott.client.shiro.entity.GroupRole;
import com.br.ott.client.shiro.mapper.GroupRoleMapper;
import com.br.ott.common.util.StringUtil;

@Component
public class GroupRoleService {

	@Autowired
	private GroupRoleMapper gRoleMapper;

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param groups
	 */
	@Transactional
	public void addRoleToGroup(GroupRole gr) {
		gRoleMapper.addRoleToGroup(gr);
	}

	/**
	 * 删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	@Transactional
	public void delGroupRoleByRid(List<String> groupIds, String roleId) {
		for (String groupId : groupIds) {
			if (StringUtil.isNotEmpty(groupId)) {
				gRoleMapper.delGroupRole(groupId, roleId);
			}
		}
	}

	@Transactional
	public void delGroupRoleByGid(List<String> roleIds, String groupId) {
		for (String roleId : roleIds) {
			if (StringUtil.isNotEmpty(roleId)) {
				gRoleMapper.delGroupRole(groupId, roleId);
			}
		}
	}

	/**
	 * 按组查询组角色关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:45:44
	 * 
	 * @param groupid
	 * @return 返回类型：List<GroupRole>
	 * @exception throws
	 */
	public List<GroupRole> findGroupRoleByGroupId(String groupid) {
		return gRoleMapper.findGroupRoleByGroupId(groupid);
	}

	/**
	 * 按角色查询组角色关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:46:07
	 * 
	 * @param roleid
	 * @return 返回类型：List<GroupRole>
	 * @exception throws
	 */
	public List<GroupRole> findGroupRoleByRoleId(String roleid) {
		return gRoleMapper.findGroupRoleByRoleId(roleid);
	}

	/**
	 * 按组与角色查询组角色关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:46:16
	 * 
	 * @param groupid
	 * @param roleid
	 * @return 返回类型：GroupRole
	 * @exception throws
	 */
	public GroupRole getGroupRoleByRidAndGid(String groupid, String roleid) {
		return gRoleMapper.getGroupRoleByRidAndGid(groupid, roleid);
	}
}
