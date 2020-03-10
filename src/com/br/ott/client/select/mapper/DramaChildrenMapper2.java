package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import com.br.ott.client.select.entity.DramaChildren;

/**
 * 文件名：DramaChildrenMapper.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
public interface DramaChildrenMapper2 {

	/**
	 * 按条件查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren
	 * @param @return 返回类型：List<DramaChildren>
	 * @exception throws
	 */
	List<DramaChildren> findDramaChildren(DramaChildren dramaChildren);
	
	/**
	 * 信息子资产状态
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param status
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	void updateChildrenStatus(@Param("status") String status, @Param("id") String id);
}
