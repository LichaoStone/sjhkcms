package com.br.ott.client.select.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.DramaAlbum;
import com.br.ott.client.select.mapper.DramaAlbumMapper;

@Service
public class DramaAlbumService {
	@Autowired
	private DramaAlbumMapper dramaAlbumMapper;

	/**
	 * 分页查询
	 */
	public List<DramaAlbum> findDramaAlbumByPage(DramaAlbum dramaAlbum) {
		dramaAlbum.setTotalResult(dramaAlbumMapper
				.getCountDramaAlbum(dramaAlbum));
		return dramaAlbumMapper.findDramaAlbumByPage(dramaAlbum);
	}

	/**
	 * 根据ID查询
	 */
	public DramaAlbum getDramaAlbumById(String id) {
		return dramaAlbumMapper.getDramaAlbumById(id);
	}

	/**
	 * 新增
	 */
	public void insertDramaAlbum(DramaAlbum dramaAlbum) {
		dramaAlbumMapper.insertDramaAlbum(dramaAlbum);
	}

	/**
	 * 修改
	 */
	public void updateDramaAlbum(DramaAlbum dramaAlbum) {
		dramaAlbumMapper.updateDramaAlbum(dramaAlbum);
	}

	/**
	 * 查询全部
	 */
	public List<DramaAlbum> findDramaAlbumByCond(DramaAlbum dramaAlbum) {
		return dramaAlbumMapper.findDramaAlbumByCond(dramaAlbum);
	}

	/**
	 * 根据多个ID删除数据
	 */
	public void deleteDramaAlbumByList(List<String> ids) {
		dramaAlbumMapper.deleteDramaAlbumByList(ids);
	}

	/**
	 * 根据ID删除
	 */
	public void deleteDramaAlbumById(String id) {
		DramaAlbum da = getDramaAlbumById(id);
		// 修改大于此资产的排序值（加1）
		if (da != null) {
			int max = getMaxQueue(da.getAlbumId().toString()) - 1;
			int oldQueue = da.getQueue();
			da.setQueue(max);
			da.setOldQueue(oldQueue);
			dramaAlbumMapper.updateDASubSequence(da);
		}
		dramaAlbumMapper.deleteDramaAlbumById(id);
	}

	public int getMaxQueue(String albumId) {
		return dramaAlbumMapper.getMaxQueue(albumId);
	}

	/**
	 * 修改列表排序
	 * 
	 * 创建人：pananz 创建时间：2017-3-17
	 * 
	 * @param @param id
	 * @param @param queue
	 * @param @param oldQueue 返回类型：void
	 * @exception throws
	 */
	public void updateDASequence(String id, String queue, String oldQueue) {
		DramaAlbum da = getDramaAlbumById(id);
		int old = Integer.parseInt(oldQueue);
		int now = Integer.parseInt(queue);
		da.setQueue(now);
		da.setOldQueue(old);
		if (old > now) {// 从大到小,小于当前要修改的排序值queue要加1
			dramaAlbumMapper.updateDAAddSequence(da);
		} else {// 从小到大,queue的排序值要减1
			dramaAlbumMapper.updateDASubSequence(da);
		}
		dramaAlbumMapper.updateDASequence(queue, id);
	}
}