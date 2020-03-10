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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.entity.Role;
import com.br.ott.client.shiro.mapper.RoleMapper;
import com.br.ott.common.util.PagedModelList;

@Component
public class RoleService {

	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 分页查询
	 * 
	 * @author zhuw
	 * @param role
	 * @param skip
	 * @return
	 */
	public PagedModelList<Role> findAllRole(Role role, int skip, int row) {
		int totalCount = roleMapper.getCountRole(role);
		PagedModelList<Role> pml = new PagedModelList<Role>(skip, row,
				totalCount);
		pml.addModels(roleMapper.findAllRole(role, new RowBounds((skip - 1)
				* row, row)));
		return pml;
	}

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param role
	 */
	public void addRole(Role role) {
		roleMapper.addRole(role);
	}

	/**
	 * 修改
	 * 
	 * @author zhuw
	 * @param role
	 */
	public void modifyRole(Role role) {
		roleMapper.modifyRole(role);
	}

	/**
	 * 根据ID修改
	 * 
	 * @author zhuw
	 * @param id
	 */
	public void modifyRole(String id) {
		roleMapper.modifyRoleById(id);
	}

	/**
	 * 根据ID删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	public void delRole(String[] ids) {
		for (String id : ids) {
			roleMapper.delRole(id);
		}
	}

	/**
	 * 根据ID查找Role信息
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public Role getRoleById(String id) {
		return roleMapper.getRoleById(id);
	}

	/**
	 * 查询所有
	 * 
	 * @author zhuw
	 * @return
	 */
	public List<Role> findAllRoleList() {
		return roleMapper.getRoleList();
	}

	/**
	 * 根据用户ID查询用户所有权限
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Role> findAllRoleByIdList(String id) {
		return roleMapper.getRoleByIdList(id);
	}

	/**
	 * 根据组ID 查找系统中可选权限
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Role> findAllRoleByGroupId(String id) {
		return roleMapper.findAllRoleByGroupId(id);
	}

	/**
	 * 根据ID查找资源 信息
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public Role getResourByRoleId(String id) {
		return roleMapper.getResourByRoleId(id);
	}

	/**
	 * 按组集合查询角色信息
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Role> findRoleByGroupIds(Set<Groups> groups) {
		if (CollectionUtils.isEmpty(groups)) {
			return null;
		}
		List<String> groupIds = new ArrayList<String>();
		Iterator<Groups> iterator = groups.iterator();
		while (iterator.hasNext()) {
			Groups group = iterator.next();
			groupIds.add(group.getId());
		}
		return roleMapper.findRoleByGroupIds(groupIds);
	}

	/**
	 * 检验角色名称
	 * 
	 * @author zhuw
	 * @param rname
	 * @return
	 */
	public boolean getRoleByName(String rname) {
		List<Role> roles = roleMapper.checkName(rname);
		if (CollectionUtils.isNotEmpty(roles)) {
			return false;
		}
		return true;
	}

	public boolean getRoleByenName(String enName) {
		List<Role> roles = roleMapper.checkEname(enName);
		if (CollectionUtils.isNotEmpty(roles)) {
			return false;
		}
		return true;
	}

}
