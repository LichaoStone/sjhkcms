package com.br.ott.client.select.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.DramaSource;
import com.br.ott.client.select.mapper.DramaSourceMapper;

/* 
 *  
 *  文件名：DramaSourceService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 下午2:12:21
 */
@Service
public class DramaSourceService {

	@Autowired
	private DramaSourceMapper dramaSourceMapper;

	/**
	 * 增加资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:54:25
	 * 
	 * @param dramaSource
	 *            返回类型：void
	 * @exception throws
	 */
	public void addDramaSource(DramaSource dramaSource) {
		dramaSourceMapper.addDramaSource(dramaSource);
	}

	/**
	 * 修改资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:54:51
	 * 
	 * @param dramaSource
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateDramaSource(DramaSource dramaSource) {
		dramaSourceMapper.updateDramaSource(dramaSource);
	}

	/**
	 * 修改资产子节目源状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:54:58
	 * 
	 * @param status
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateSourceStatus(String status, String id) {
		dramaSourceMapper.updateSourceStatus(status, id);
	}

	public void updateSourceStatus(String status, String[] ids) {
		for (String id : ids) {
			dramaSourceMapper.updateSourceStatus(status, id);
		}
	}

	/**
	 * 按ID删除
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 下午2:09:53
	 * 
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	public void deleteSourceById(String id) {
		dramaSourceMapper.deleteSourceById(id);
	}

	/**
	 * 按资产编号删除
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 下午2:10:03
	 * 
	 * @param dramaId
	 *            返回类型：void
	 * @exception throws
	 */
	public void deleteSourceByDramaId(String dramaId) {
		dramaSourceMapper.deleteSourceByDramaId(dramaId);
	}

	/**
	 * 按ID查询资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:55:14
	 * 
	 * @param id
	 * @return 返回类型：DramaSource
	 * @exception throws
	 */
	public DramaSource getDramaSourceById(String id) {
		return dramaSourceMapper.getDramaSourceById(id);
	}

	/**
	 * 按分页查询资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:55:24
	 * 
	 * @param dramaSource
	 * @return 返回类型：List<DramaSource>
	 * @exception throws
	 */
	public List<DramaSource> findDramaSourceByPage(DramaSource dramaSource) {
		int totalResult = dramaSourceMapper.getCountDramaSource(dramaSource);
		dramaSource.setTotalResult(totalResult);
		return dramaSourceMapper.findDramaSourceByPage(dramaSource);
	}

	/**
	 * 按条件查询资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:55:37
	 * 
	 * @param dramaSource
	 * @return 返回类型：List<DramaSource>
	 * @exception throws
	 */
	public List<DramaSource> findDramaSourceByCond(DramaSource dramaSource) {
		return dramaSourceMapper.findDramaSourceByCond(dramaSource);
	}
}
