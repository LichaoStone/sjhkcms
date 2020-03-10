package com.br.ott.client.select.mapper;

import java.util.List;

import com.br.ott.client.select.entity.AssetLog;

/**
 * 文件名：AssetLogMapper.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
public interface AssetLogMapper {

	void addAssetLog(AssetLog assetLog);

	void addAssetLogList(List<AssetLog> list);

	List<AssetLog> findAssetLogByPage(AssetLog assetLog);

	int getCountAssetLog(AssetLog assetLog);

	AssetLog getAssetLogById(String id);

	void delAssetLogList(List<String> ids);
}
