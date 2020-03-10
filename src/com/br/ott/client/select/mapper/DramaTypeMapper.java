package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaType;

/**
 * 文件名：DramaTypeMapper.java
 *  版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2016-9-19
 */
public interface DramaTypeMapper {

	void addDramaType(DramaType dramaType);
	
	void delDramaTypeByNavId(String navId);
	
	void delDramaTypeByDramaId(String dramaId);
	
	List<DramaType> findDramaTypeByCond(DramaType dramaType);
	
	List<DramaType> findDramaTypeByDramaId(String dramaId);
	
	List<DramaType> findNavByDramaId(String dramaId);
	
	DramaType findDramaTypeByDramaId2(@Param("dramaId") String dramaId);
	
	List<DramaType> findDramaTypeByNavId(@Param("navId") String navId);
}


