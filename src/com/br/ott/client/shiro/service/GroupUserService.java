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

import com.br.ott.client.shiro.entity.GroupUser;
import com.br.ott.client.shiro.mapper.GroupUserMapper;
import com.br.ott.common.util.StringUtil;

@Component
public class GroupUserService {

	@Autowired
	private GroupUserMapper gUserMapper;

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param groups
	 */
	public void addUserToGroup(GroupUser guUser) {
		gUserMapper.addUserToGroup(guUser);
	}

	/**
	 * 删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	@Transactional
	public void delUserToGroup(String[] ids, String gid) {
		for (String id : ids) {
			if (StringUtil.isNotEmpty(id)) {
				gUserMapper.delUserToGroup(id, gid);
			}
		}
	}

	/**
	 * 删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	@Transactional
	public void delGroupToUser(String[] ids, String uId) {
		for (String id : ids) {
			gUserMapper.delGroupToUser(id, uId);
		}
	}

	/**
	 * 按组查询关联用户信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午10:51:20
	 * 
	 * @param groupId
	 * @return 返回类型：List<GroupUser>
	 * @exception throws
	 */
	public List<GroupUser> findGroupUserByGroupId(String groupId) {
		return gUserMapper.findGroupUserByGroupId(groupId);
	}

	/**
	 * 按用户查询关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午10:51:38
	 * 
	 * @param userId
	 * @return 返回类型：List<GroupUser>
	 * @exception throws
	 */
	public List<GroupUser> findGroupUserByUserId(String userId) {
		return gUserMapper.findGroupUserByUserId(userId);
	}

	public GroupUser getGroupUserByGIdAndUId(String groupId, String userId) {
		return gUserMapper.getGroupUserByGIdAndUId(groupId, userId);
	}
}
