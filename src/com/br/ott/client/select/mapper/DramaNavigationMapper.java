package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaNavigation;


/**
 * 
 * @author Administrator
 *
 */
public interface DramaNavigationMapper {
	/**
	 * 统计数量
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param dramaNavigation
		 * @param @return
		 * 返回类型：int
		 * @exception throws
	 */
	int getCountDramaNavigation(DramaNavigation dramaNavigation);
	/**
	 * 获取分页数据
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param dramaNavigation
		 * @param @return
		 * 返回类型：List<DramaNavigation>
		 * @exception throws
	 */
	List<DramaNavigation> findDramaNavigationByPage(DramaNavigation dramaNavigation);
	
	/**
	 * 添加栏目信息
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param dramaNavigation
		 * 返回类型：void
		 * @exception throws
	 */
	void addDramaNavigation(DramaNavigation dramaNavigation);

	/**
	 * 根据id获取数据
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param id
		 * @param @return
		 * 返回类型：DramaNavigation
		 * @exception throws
	 */
	DramaNavigation getDramaNavigationById(String id);

	/**
	 * 根据类型和名称获取数据
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param operators
		 * @param @param name
		 * @param @return
		 * 返回类型：List<DramaNavigation>
		 * @exception throws
	 */
	List<DramaNavigation> findDramaNavigationByTypeAndName(String operators,
			String name);

	/**
	 * 跟新栏目信息
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param dramaNavigation
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaNavigation(DramaNavigation dramaNavigation);

	/**
	 * 修改栏目启用或停用状态
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param status
		 * @param @param id
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaNavigationStatus(@Param("status")String status,@Param("id") String id);

	/**
	 * 删除栏目数据
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-13
		 * 
		 * @param @param id
		 * 返回类型：void
		 * @exception throws
	 */
	void delNavigationById(String id);
	
	/**
	 * 按条件查询栏目
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param dramaNavigation
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	List<DramaNavigation> findDramaNavigationByCond(
			DramaNavigation dramaNavigation);
	/**
	 * 获取最大排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-22
		 * 
		 * @param @param parentId
		 * @param @return
		 * 返回类型：DramaNavigation
		 * @exception throws
	 */
	int getMaxSequence(String parentId);
	
	/**
	 * 获取比当前排序值大的所有排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-22
		 * 
		 * @param @param sequence
		 * @param @return
		 * 返回类型：List<DramaNavigation>
		 * @exception throws
	 */
	List<DramaNavigation> findDramaNavigationBySequenceUp(DramaNavigation dramaNavigation);
	List<DramaNavigation> findDramaNavigationBySequenceDown(DramaNavigation dramaNavigation);
	/**
	 * 根据当前排序值修改上一个排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-22
		 * 
		 * @param @param seq
		 * @param @param sequence
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaNavigationBySequence(DramaNavigation dramaNavigation);
	
	/**
	 * 获取比当前排序值大的所有排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-10
		 * 
		 * @param @param dramaNavigation
		 * @param @return
		 * 返回类型：List<DramaNavigation>
		 * @exception throws
	 */
	List<DramaNavigation> findDramaNavigationBySortUp(
			DramaNavigation dramaNavigation);
	List<DramaNavigation> findDramaNavigationBySortDown(DramaNavigation dramaNavigation);
	/**
	 * 根据当前排序值修改上一个排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-10
		 * 
		 * @param @param dramaNavigation
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaNavigationBySort(DramaNavigation dramaNavigation);
	/**
	 * 更新推荐排序
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-10
		 * 
		 * @param @param recommend
		 * @param @param id
		 * 返回类型：void
		 * @exception throws
	 */
	void updateDramaNavigationRecommend(@Param("recommend")String recommend,@Param("id") String id);
	DramaNavigation findDramaNavigationMaxSort();
	
	/**
	 * 通过id获取DramaNavigation
		 * 
		 * 创建人：lizhenghg 创建时间：2016-10-10
		 * 
		 * @param @param DramaNavigation
		 * 返回类型：DramaNavigation
		 * @exception throws
	 */
	List<DramaNavigation> findDramaNavigationById(DramaNavigation dramaNavigation);

	/**
	 * 增加排序值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param dn 返回类型：void
	 * @exception throws
	 */
	void updateDNAddSequence(DramaNavigation dn);
	
	/**
	 * 减少排序值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param dn 返回类型：void
	 * @exception throws
	 */
	void updateDNSubSequence(DramaNavigation dn);

	/**
	 * 修改排序值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param status
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	void updateDNSequence(@Param("sequence") String sequeue, @Param("id") String id);
}
