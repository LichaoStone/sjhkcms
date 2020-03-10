/*
 * @# UploadSourceMapper.java Oct 17, 2012 2:04:46 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.common.entity.UploadSource;

/*
 * @author pananz
 * TODO
 */
public interface UploadSourceMapper {

	/**
	 * 增加上传内容资源
	 * 
	 * @param uploadSource
	 */
	void addUploadSource(UploadSource uploadSource);

	/**
	 * 修改内容资源状态
	 * 
	 * @param status
	 * @param id
	 */
	void updateUploadSourceStatus(@Param("status") String status,
			@Param("id") String id);

	/**
	 * 删除上传内容资源
	 * 
	 * @param uploadSource
	 */
	void delUploadSource(String[] ids);

	/**
	 * 按产品ID查询上传内容资源(有效)
	 * 
	 * @param id
	 * @return
	 */
	List<UploadSource> getUploadSourceByPid(String pid);

	/**
	 * SOURCEVERSION 按ID查询
	 * 
	 * @param id
	 * @return
	 */
	UploadSource getUploadSourceById(String id);

	List<UploadSource> findUploadSourceByPidAndStart(@Param("pid") String pid,
			@Param("sourceType") String sourceType);

	void updateUploadSavePath(@Param("savePath") String savePath,
			@Param("id") String id);
}
