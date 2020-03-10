package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaAlbum;

public interface DramaAlbumMapper {
	/**
	 * 按分页查询
	 */
	List<DramaAlbum> findDramaAlbumByPage(DramaAlbum dramaAlbum);

	/**
	 * 按统计总记录数
	 */
	int getCountDramaAlbum(DramaAlbum dramaAlbum);

	/**
	 * 按ID查询
	 */
	DramaAlbum getDramaAlbumById(String id);

	/**
	 * 增加实体
	 */
	void insertDramaAlbum(DramaAlbum dramaAlbum);

	/**
	 * 修改实体
	 */
	void updateDramaAlbum(DramaAlbum dramaAlbum);

	/**
	 * 按条件查询
	 */
	List<DramaAlbum> findDramaAlbumByCond(DramaAlbum dramaAlbum);

	/**
	 * 按ID集合删除
	 */
	void deleteDramaAlbumByList(List<String> ids);

	/**
	 * 按合辑、资产ID集合删除
	 */
	void deleteDramaAlbumByAlbumId(String albumId);
	
	/**
	 * 按合辑、资产ID集合删除
	 */
	void deleteDramaAlbumByDramaId(String dramaId);
	
	/**
	 * 按ID删除
	 */
	void deleteDramaAlbumById(String id);

	/**
	 * 取得当前albumId最大值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param albumId
	 * @param @return 返回类型：int
	 * @exception throws
	 */
	int getMaxQueue(String albumId);

	/**
	 * 修改排序值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param queue
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	void updateDASequence(@Param("queue") String queue, @Param("id") String id);

	/**
	 * 增加排序值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param dramaAlbum 返回类型：void
	 * @exception throws
	 */
	void updateDAAddSequence(DramaAlbum dramaAlbum);

	/**
	 * 减少排序值
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param dramaAlbum 返回类型：void
	 * @exception throws
	 */
	void updateDASubSequence(DramaAlbum dramaAlbum);
	
	
	List<DramaAlbum> findDramaAlbumForAPI(DramaAlbum dramaAlbum);
}