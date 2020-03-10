package com.br.ott.client.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.common.entity.District;

/* 
 *  地区
 *  文件名：DistrictMapper.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2013-1-18 - 下午2:34:16
 */
public interface DistrictMapper {
	public List<District> getAllDistrict(@Param("maxlevl")int maxLevel);
	public List<District> getDistrictByUpid(@Param("upid")int upid);
}
