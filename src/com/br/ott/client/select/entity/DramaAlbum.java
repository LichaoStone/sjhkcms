package com.br.ott.client.select.entity;

import com.br.ott.common.util.Pagination;

public class DramaAlbum extends Pagination {
	private Integer id;//
	private Integer albumId;// 专题ID,合辑ID
	private Integer dramaId;// 资产ID
	private Integer queue;//排序值
	/********** 用于页面查询 ****************/
	private String orderColumn;
	private Integer oldQueue;
	
	public Integer getOldQueue() {
		return oldQueue;
	}

	public void setOldQueue(Integer oldQueue) {
		this.oldQueue = oldQueue;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public Integer getQueue() {
		return queue;
	}

	public void setQueue(Integer queue) {
		this.queue = queue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public Integer getDramaId() {
		return dramaId;
	}

	public void setDramaId(Integer dramaId) {
		this.dramaId = dramaId;
	}

}