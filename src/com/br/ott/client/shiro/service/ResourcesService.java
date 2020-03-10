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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.ott.client.shiro.entity.Resources;
import com.br.ott.client.shiro.entity.Role;
import com.br.ott.client.shiro.entity.RoleAuth;
import com.br.ott.client.shiro.mapper.ResourcesMapper;
import com.br.ott.client.shiro.mapper.RoleAuthMapper;
import com.br.ott.common.jackson.TinyTreeBean;
import com.br.ott.common.jackson.TinyTreeUtils;
import com.br.ott.common.util.PagedModelList;

@Component
public class ResourcesService {

	@Autowired
	private ResourcesMapper resourMapper;

	@Autowired
	private RoleAuthMapper roleAuthMapper;

	/**
	 * 分页查询
	 * 
	 * @author zhuw
	 * @param res
	 * @param skip
	 * @return
	 */
	public PagedModelList<Resources> findAllResour(Resources res, int skip,
			int row) {
		int totalCount = resourMapper.getCountResour(res);
		PagedModelList<Resources> pml = new PagedModelList<Resources>(skip,
				row, totalCount);
		pml.addModels(resourMapper.findAllResour(res, new RowBounds((skip - 1)
				* row, row)));
		return pml;
	}

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param res
	 */
	public void addResour(Resources res) {
		resourMapper.addResour(res);
	}

	/**
	 * 根据ID查找信息
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public Resources getResourById(String id) {
		return resourMapper.getResourById(id);
	}

	/**
	 * 修改
	 * 
	 * @author zhuw
	 * @param res
	 */
	public void modifyResour(Resources res) {
		resourMapper.modifyResour(res);
	}

	public boolean getResourByName(String rname) {
		List<Resources> resources = resourMapper.checkName(rname);
		if (CollectionUtils.isNotEmpty(resources)) {
			return false;
		}
		return true;
	}

	public boolean getChekEnname(String rname) {
		List<Resources> resources = resourMapper.getChekEnname(rname);
		if (CollectionUtils.isNotEmpty(resources)) {
			return false;
		}
		return true;
	}

	/**
	 * 删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	public void delResour(String[] ids) {
		for (String id : ids) {
			resourMapper.delResour(id);
		}
	}

	/**
	 * 获得指定角色的所有资源操作对应的集合，用于给指定角色的资源分配操作时使用
	 * 
	 * @author zhuw
	 * @param roleId
	 * @param resources
	 *            角色所拥有的资源集合
	 * @return
	 */
	public List<Map<String, Object>> findRoleMapResour(String roleId,
			Set<Resources> resources) {

		// resourMapper.delResour(id);
		return null;

	}

	/**
	 * 获得指定角色的可选资源操作对应的集合
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Resources> findAllResourByRoleId(String id) {
		return resourMapper.findAllResourByRoleId(id);
	}

	/**
	 * 查询所有资源
	 * 
	 * @author zhuw
	 * @return
	 */
	public List<Resources> findAllResourList() {
		return resourMapper.findAllResourList();
	}

	/**
	 * 指定模块下所有的资源
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<Resources> findAllResourByIdList(String id, String resId) {
		return resourMapper.findAllResourByIdList(id, resId);
	}

	/**
	 * 根据系统ID查找对应的资源ID
	 * 
	 * @author zhuw
	 * @param systemId
	 * @return
	 */
	public String getResourId(String systemId) {
		return resourMapper.getResourId(systemId);
	}

	/**
	 * 构建菜单权限树
	 * 
	 * @return
	 */
	public String buildTreeResource(String roleId) {
		List<Resources> list = findAllResourList();
		if (CollectionUtils.isEmpty(list)) {
			TinyTreeBean root = new TinyTreeBean();
			root.setName("暂无菜单权限");
			return TinyTreeUtils.toJson(root);
		}
		List<RoleAuth> roleAuths = roleAuthMapper.findRoleAuthByRoleId(roleId);
		return TinyTreeUtils.toJson(buildTinyTreeBean(list, roleAuths));
	}

	private static final String ROOT_RESOURCE_ID = "0";

	private List<TinyTreeBean> buildTinyTreeBean(List<Resources> list,
			List<RoleAuth> roleAuths) {
		// TODO 此方法后续需优化(当前只能用于查询数据量不大的情况)
		TinyTreeBean root = new TinyTreeBean(ROOT_RESOURCE_ID, null);
		Map<String, TinyTreeBean> treeMap = new HashMap<String, TinyTreeBean>();
		treeMap.put(ROOT_RESOURCE_ID, root);
		for (Resources res : list) {
			TinyTreeBean node = new TinyTreeBean(res.getId(), res.getName());
			if (CollectionUtils.isNotEmpty(roleAuths)) {
				for (RoleAuth ra : roleAuths) {
					if (res.getId().equals(ra.getResourceId())) {
						node.setChecked(true);
					}
				}
			}
			treeMap.put(res.getId(), node);
		}
		for (Resources res : list) {
			TinyTreeBean parent = treeMap.get(res.getParentId());
			if (null == parent) {
				continue;
			}
			parent.addChild(treeMap.get(res.getId()));
		}
		return treeMap.get(ROOT_RESOURCE_ID).getChildren();
	}

	List<Resources> findResourcesByRoleIds(List<Role> roles) {
		if (CollectionUtils.isEmpty(roles)) {
			return null;
		}
		List<String> roleIds = new ArrayList<String>();
		Iterator<Role> iterator = roles.iterator();
		while (iterator.hasNext()) {
			Role role = iterator.next();
			roleIds.add(role.getOid());
		}
		return resourMapper.findResourcesByRoleIds(roleIds);
	}
}
