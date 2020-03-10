package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaRecommend;

/**
 * 文件名：RecommendPositionMapper.java
 *  版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：xiaojxiang
 *  创建时间：2016-9-19
 */
public interface DramaRecommendMapper {

	/**
	 * 按分页查询推荐位数据
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-19
		 * 
		 * @param @param recommendPosition
		 * @param @return
		 * 返回类型：List<RecommendPosition>
		 * @exception throws
	 */
	List<DramaRecommend> findRecommendPstByPage(
			DramaRecommend dramaRecommend);
	
	int getCountRecommendPst(DramaRecommend dramaRecommend);

	DramaRecommend getRecommendPstById(String id);

	void updateRecommendPst(DramaRecommend dramaRecommend);

	void delRecommend(String[] ids);

	void addRecommendPst(DramaRecommend dramaRecommend);

	DramaRecommend getMaxSortByPrType(String prType);

	List<DramaRecommend> findDramaRecByDramaId(String dramaId);
	
	List<DramaRecommend> findDramaRecByPrType(String prType);
	
	void delDramaRecByDramaId(String dramaId);
	
	/**
	 * 获取比当前排序值大的最小值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param dramaRecommend
		 * @param @return
		 * 返回类型：List<DramaRecommend>
		 * @exception throws
	 */
	List<DramaRecommend> findDramaRecommendBySortUp(
			DramaRecommend dramaRecommend);

	/**
	 * 跟新排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-29
		 * 
		 * @param @param dramaRecommend
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaRecommendBySort(DramaRecommend dramaRecommend);

	List<DramaRecommend> findDramaRecommendBySortDown(
			DramaRecommend dramaRecommend);

	/**
	 * 删除推荐资产
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-18
		 * 
		 * @param @param id
		 * 返回类型：void
		 * @exception throws
	 */
	void delRecommendPstById(@Param("id")String id);
}
