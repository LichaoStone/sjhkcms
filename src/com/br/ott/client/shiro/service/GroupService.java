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

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.mapper.GroupMapper;
import com.br.ott.common.util.PagedModelList;

@Component
public class GroupService {

	@Autowired
	private GroupMapper groupMapper;

	/**
	 * 分页查询
	 * 
	 * @author zhuw
	 * @param groups
	 * @param skip
	 * @return
	 */
	public PagedModelList<Groups> findAllGroup(Groups groups, int skip, int row) {
		int totalCount = groupMapper.getCountGroup(groups);
		PagedModelList<Groups> pml = new PagedModelList<Groups>(skip, row,
				totalCount);
		pml.addModels(groupMapper.findAllGroup(groups, new RowBounds((skip - 1)
				* row, row)));
		return pml;
	}

	/**
	 * 查询所有
	 * 
	 * @author zhuw
	 * @return
	 */
	public List<Groups> findAllGroupList() {
		return groupMapper.getAllGroup();
	}

	public boolean getGroupByName(String rname) {
		List<Groups> resources = groupMapper.checkName(rname);
		if (CollectionUtils.isNotEmpty(resources)) {
			return false;
		}
		return true;
	}

	public boolean getGroupByenName(String enName) {
		List<Groups> resources = groupMapper.checkenName(enName);
		if (CollectionUtils.isNotEmpty(resources)) {
			return false;
		}
		return true;
	}

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param groups
	 */
	public void addGroup(Groups groups) {
		groupMapper.addGroup(groups);
	}

	/**
	 * 根据ID查找信息
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public Groups getGroupById(String id) {
		return groupMapper.getGroupById(id);
	}

	/**
	 * 修改
	 * 
	 * @author zhuw
	 * @param groups
	 */
	public void modifyGroup(Groups groups) {
		groupMapper.modifyGroup(groups);
	}

	/**
	 * 删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	public void delGroup(String[] ids) {
		for (String id : ids) {
			groupMapper.delGroup(id);
		}
	}

	/**
	 * 根据组ID查询组所有权限
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Groups> findAllGroupByIdList(String id) {
		return groupMapper.getRoleByIdList(id);
	}

	/**
	 * 根据角色ID查询可选组
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Groups> findAllGroupByRoleId(String id) {
		return groupMapper.getGroupByIdList(id);
	}

	/**
	 * 根据用户ID查询可选组
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Groups> findAllGroupByUserId(String id) {
		return groupMapper.getGroupByUserId(id);
	}

	/**
	 * 根据组ID查找角色
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public Groups getRoleByGroupId(String id) {
		return groupMapper.getRoleByGroupId(id);
	}

	/**
	 * 根据名称查找分组
	 * 
	 * @param name
	 * @return
	 */
	public Groups findGroupByEnName(String name) {
		return groupMapper.findGroupByEnName(name);
	}

}
