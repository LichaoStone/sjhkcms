package com.br.ott.client.select.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.common.DramaPositionCache;
import com.br.ott.client.select.entity.DramaPosition;
import com.br.ott.client.select.mapper.DramaPositionMapper;

/**
 * 文件名：DramaPositionService.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：xiaojxiang 创建时间：2016-9-29
 */
@Service
public class DramaPositionService {

	@Autowired
	private DramaPositionMapper dramaPositionMapper;

	/**
	 * 获取推荐位页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param dramaPosition
	 * @param @return 返回类型：List<DramaPosition>
	 * @exception throws
	 */
	public List<DramaPosition> findDramaPositionByPage(
			DramaPosition dramaPosition) {
		int totalResult = dramaPositionMapper
				.getCountDramaPosition(dramaPosition);
		dramaPosition.setTotalResult(totalResult);
		return dramaPositionMapper.findDramaPositionByPage(dramaPosition);
	}

	/**
	 * 修改或新增
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param id
	 * @param @return 返回类型：DramaPosition
	 * @exception throws
	 */
	public DramaPosition getDramaPositionById(String id) {
		return dramaPositionMapper.getDramaPositionById(id);
	}

	/**
	 * 停用或启用推荐位
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param status
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	public void updateDramaPositionStatus(String status, String id) {
		dramaPositionMapper.updateDramaPositionStatus(status, id);
		reloadPosition();
	}

	/**
	 * 新增推荐位
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param dramaPosition 返回类型：void
	 * @exception throws
	 */
	public void addDramaPosition(DramaPosition dramaPosition) {
		dramaPositionMapper.addDramaPosition(dramaPosition);
		reloadPosition();
	}

	/**
	 * 修改推荐位
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param dramaPosition 返回类型：void
	 * @exception throws
	 */
	public void updateDramaPosition(DramaPosition dramaPosition) {
		dramaPositionMapper.updateDramaPosition(dramaPosition);
		reloadPosition();
	}

	/**
	 * 按条件查询推荐位
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param @param dramaPosition
	 * @param @return 返回类型：List<DramaPosition>
	 * @exception throws
	 */
	public List<DramaPosition> findDramaPositionByCond(
			DramaPosition dramaPosition) {
		return dramaPositionMapper.findDramaPositionByCond(dramaPosition);
	}

	
	/**
	 * 校验编码
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param code
		 * @param @return
		 * 返回类型：boolean
		 * @exception throws
	 */
	public boolean checkPositionByCode(String code) {
		DramaPosition position = new DramaPosition();
		position.setCode(code);
		List<DramaPosition> list = findDramaPositionByCond(position);
		if (CollectionUtils.isNotEmpty(list)) {
			return false;
		}
		return true;
	}

	/**
	 * 更新推荐位
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param 返回类型：void
	 * @exception throws
	 */
	public void reloadPosition() {
		DramaPosition dp = new DramaPosition();
		dp.setStatus("1");
		List<DramaPosition> list = findDramaPositionByCond(dp);
		DramaPositionCache.relocadPosition(list);
		list = null;
	}
}
