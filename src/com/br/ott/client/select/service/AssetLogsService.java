package com.br.ott.client.select.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.AssetLog;
import com.br.ott.client.select.mapper.AssetLogMapper;

/**
 * 文件名：AssetLogService.java
 *  版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：xiaojxiang
 *  创建时间：2016-9-12
 */
@Service
public class AssetLogsService {
	@Autowired
	private AssetLogMapper assetLogMapper;
	/**
	 * 获取分页页面
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param assetLog
		 * @param @return
		 * 返回类型：List<AssetLog>
		 * @exception throws
	 */
	public List<AssetLog> findAssetLogByPage(AssetLog assetLog) {
		int totalResult = assetLogMapper.getCountAssetLog(assetLog);
		assetLog.setTotalResult(totalResult);
		return assetLogMapper.findAssetLogByPage(assetLog);
	}
	
	/**
	 *跳转详情页面
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param id
		 * @param @return
		 * 返回类型：AssetLog
		 * @exception throws
	 */
	public AssetLog getAssetLogById(String id) {
		return assetLogMapper.getAssetLogById(id);
	}

	public void delAssetLogList(List<String> asList) {
		assetLogMapper.delAssetLogList(asList);
	}
	
}
