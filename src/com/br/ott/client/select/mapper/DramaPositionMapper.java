package com.br.ott.client.select.mapper;

import java.util.List;

import com.br.ott.client.select.entity.DramaPosition;

/**
 * 文件名：DramaPositionMapper.java
 *  版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：xiaojxiang
 *  创建时间：2016-9-29
 */
public interface DramaPositionMapper {

	/**
	 * 统计数量
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param dramaPosition
		 * @param @return
		 * 返回类型：int
		 * @exception throws
	 */
	int getCountDramaPosition(DramaPosition dramaPosition);

	/**
	 * 根据分页查询推荐位数据
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param dramaPosition
		 * @param @return
		 * 返回类型：List<DramaPosition>
		 * @exception throws
	 */
	List<DramaPosition> findDramaPositionByPage(DramaPosition dramaPosition);

	/**
	 * 修改or新增推荐位
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param id
		 * @param @return
		 * 返回类型：DramaPosition
		 * @exception throws
	 */
	DramaPosition getDramaPositionById(String id);

	/**
	 * 启用推荐位
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param status
		 * @param @param id
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaPositionStatus(String status, String id);

	/**
	 * 新增推荐位
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param dramaPosition
		 * 返回类型：void
		 * @exception throws
	 */
	void addDramaPosition(DramaPosition dramaPosition);

	/**
	 * 修改推荐位
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param dramaPosition
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaPosition(DramaPosition dramaPosition);
	
	/**
	 * 按条件查询推荐位
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param @param dramaPosition
	 * @param @return 返回类型：List<DramaPosition>
	 * @exception throws
	 */
	List<DramaPosition> findDramaPositionByCond(DramaPosition dramaPosition);
}
