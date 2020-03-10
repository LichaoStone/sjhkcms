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

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.ott.client.shiro.entity.Systems;
import com.br.ott.client.shiro.mapper.SystemsMapper;
import com.br.ott.common.util.PagedModelList;

@Component
public class SystemsService {

	@Autowired
	private SystemsMapper systemsMapper;

	/**
	 * 分页查询
	 * 
	 * @author zhuw
	 * @param role
	 * @param skip
	 * @return
	 */
	public PagedModelList<Systems> findAllSystem(Systems systems, int skip,
			int row) {
		int totalCount = systemsMapper.getCountSystem(systems);
		PagedModelList<Systems> pml = new PagedModelList<Systems>(skip, row,
				totalCount);
		pml.addModels(systemsMapper.findAllSystem(systems, new RowBounds(
				(skip - 1) * row, row)));
		return pml;
	}

	/**
	 * 添加
	 * 
	 * @author zhuw
	 * @param role
	 */
	public void addSystem(Systems systems) {
		systemsMapper.addSystem(systems);
	}

	/**
	 * 根据ID修改
	 * 
	 * @author zhuw
	 * @param id
	 */
	// public void modifySystem(String id) {
	// systemsMapper.modifySystem(id);
	// }

	/**
	 * 修改
	 * 
	 * @author zhuw
	 * @param systems
	 */
	public void modifySystem(Systems systems) {
		systemsMapper.modifySystem(systems);
	}

	/**
	 * 根据ID删除
	 * 
	 * @author zhuw
	 * @param ids
	 */
	public void delSystem(String[] ids) {
		for (String id : ids) {
			systemsMapper.delSystem(id);
		}
	}

	/**
	 * 根据ID查找
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public Systems getSystemById(String id) {
		return systemsMapper.getSystemById(id);
	}

	/**
	 * 查询所有
	 * 
	 * @author zhuw
	 * @return
	 */
	public List<Systems> findAllList() {
		return systemsMapper.getSystemList();
	}

}
