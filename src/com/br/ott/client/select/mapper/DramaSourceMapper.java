package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaSource;

/* 
 *  
 *  文件名：DramaSourceMapper.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 上午11:52:27
 */
public interface DramaSourceMapper {

	/**
	 * 增加资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:54:25
	 * 
	 * @param dramaSource
	 *            返回类型：void
	 * @exception throws
	 */
	void addDramaSource(DramaSource dramaSource);

	/**
	 * 修改资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:54:51
	 * 
	 * @param dramaSource
	 *            返回类型：void
	 * @exception throws
	 */
	void updateDramaSource(DramaSource dramaSource);

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
	void updateSourceStatus(@Param("status") String status,
			@Param("id") String id);

	/**
	 * 按ID删除
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 下午2:09:53
	 * 
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	void deleteSourceById(String id);

	/**
	 * 按资产编号删除
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 下午2:10:03
	 * 
	 * @param dramaId
	 *            返回类型：void
	 * @exception throws
	 */
	void deleteSourceByDramaId(String dramaId);

	/**
	 * 按ID查询资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:55:14
	 * 
	 * @param id
	 * @return 返回类型：DramaSource
	 * @exception throws
	 */
	DramaSource getDramaSourceById(String id);

	/**
	 * 按分页查询资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:55:24
	 * 
	 * @param dramaSource
	 * @return 返回类型：List<DramaSource>
	 * @exception throws
	 */
	List<DramaSource> findDramaSourceByPage(DramaSource dramaSource);
	int getCountDramaSource(DramaSource dramaSource);
	
	/**
	 * 按条件查询资产子节目源
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:55:37
	 * 
	 * @param dramaSource
	 * @return 返回类型：List<DramaSource>
	 * @exception throws
	 */
	List<DramaSource> findDramaSourceByCond(DramaSource dramaSource);
	
	/**
	 * 资产码率列表信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-14
	 * 
	 * @param DramaSource
	 * @return 返回类型：List<DramaSource>
	 * @exception throws
	 */
	List<DramaSource> findBitrateList(@Param("childId") String childId);
	
	/**
	 * 根据API的相应childId和播放源source获取DramaSource
	 * @author lizhenghg   2017-7-23
	 * @return DramaSource
	 */
	DramaSource findDramaSourceForAPI(@Param("zcId") String zcId, @Param("title") String title, @Param("pId") String pId, @Param("source") String source);
}
