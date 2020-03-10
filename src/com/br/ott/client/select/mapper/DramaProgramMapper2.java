package com.br.ott.client.select.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.select.entity.DramaProgram;

/* 
 *  
 *  文件名：DramaProgramMapper.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  专为mybatis的二级缓存而使用
 *  创建人：lizhenghg
 *  创建时间：2018-01-28 - 下午5:16:39
 */
public interface DramaProgramMapper2 {

	/**
	 * 按ID和STATUS查询资产点播节目单
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-28 - 上午10:07:08
	 * 
	 * @param id
	 * @return 返回类型：dramaProgram
	 * @exception throws
	 */
	DramaProgram getDramaProgramByIdAndStatus(@Param("id")String id, @Param("status")String status);
	
	/**
	 * 修改资产点播节目单状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:19
	 * 
	 * @param status
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	void updateDramaProgramStatus(@Param("status") String status, @Param("id") String id);
	
	
	int getCountDramaProgram(DramaProgram dramaProgram);
	
	/**
	 * 按分页查询资产点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:12
	 * 
	 * @param dramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	List<DramaProgram> findDramaProgram(DramaProgram dramaProgram, RowBounds rowBounds);	
	
	List<DramaProgram> findDramaProgram2(DramaProgram dramaProgram);
}
