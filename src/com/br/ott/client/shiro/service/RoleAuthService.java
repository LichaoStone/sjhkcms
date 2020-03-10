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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.br.ott.client.shiro.entity.RoleAuth;
import com.br.ott.client.shiro.mapper.RoleAuthMapper;

@Component
public class RoleAuthService {

	@Autowired
	private RoleAuthMapper roleAuthMapper;

	/**
	 * 根据角色和资源ID查询
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午10:39:34
	 * 
	 * @param roleId
	 * @param resId
	 * @return 返回类型：RoleAuth
	 * @exception throws
	 */
	public RoleAuth findRoleAuth(String roleId, String resId) {
		return roleAuthMapper.findRoleAuth(roleId, resId);
	}

	/**
	 * 删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	@Transactional
	public void delResourToRole(String[] ids, String roleId) {
		for (String id : ids) {
			roleAuthMapper.delResourToRole(id, roleId);
		}
	}

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param groups
	 */
	@Transactional
	public void addAuthToRole(RoleAuth gr) {
		roleAuthMapper.addAuthToRole(gr);
	}

	/**
	 * 给角色授予菜单
	 * 
	 * @param resIds
	 * @param roleId
	 */
	@Transactional
	public void addAuthToRole(String[] resIds, String roleId) {
		roleAuthMapper.deleteRoleAuthByRoleId(roleId);
		if (resIds.length <= 0) {
			return;
		}
		for (String resourceId : resIds) {
			RoleAuth roleAuth = new RoleAuth();
			roleAuth.setResourceId(resourceId);
			roleAuth.setRoleId(roleId);
			addAuthToRole(roleAuth);
		}
	}
}
