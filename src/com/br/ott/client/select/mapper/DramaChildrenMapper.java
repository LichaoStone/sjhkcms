package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaChildren;

/**
 * 文件名：DramaChildrenMapper.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
public interface DramaChildrenMapper {

	/**
	 * 增加子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren 返回类型：void
	 * @exception throws
	 */
	void addDramaChildren(DramaChildren dramaChildren);

	/**
	 * 按ID查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param id
	 * @param @return 返回类型：DramaChildren
	 * @exception throws
	 */
	DramaChildren getDramaChildrenById(String id);

	/**
	 * 按条件查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren
	 * @param @return 返回类型：List<DramaChildren>
	 * @exception throws
	 */
	List<DramaChildren> findDramaChildrenByCond(DramaChildren dramaChildren);

	/**
	 * 按条件分页查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren
	 * @param @return 返回类型：List<DramaChildren>
	 * @exception throws
	 */
	List<DramaChildren> findDramaChildrenByPage(DramaChildren dramaChildren);

	int getCountDramaChildren(DramaChildren dramaChildren);

	/**
	 * 更新子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren 返回类型：void
	 * @exception throws
	 */
	void updateDramaChildren(DramaChildren dramaChildren);

	/**
	 * 信息子资产状态
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param status
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	void updateChildrenStatus(@Param("status") String status,
			@Param("id") String id);

	/**
	 * 获取子资产全部信息
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-2
	 * 
	 * @param @param status
	 * @param @param 返回类型：List<DramaChildren>
	 * @exception throws
	 */
	List<DramaChildren> findDramaChildren(DramaChildren dramaChildren);
}
